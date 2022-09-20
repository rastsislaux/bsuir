#include "Lexer.h"

#define MAKE_TOKEN(TYPE) XmlLexer::Token(TYPE, row, col)
#define MAKE_TEXT_TOKEN(TYPE, TEXT) XmlLexer::Token(TYPE, row, col, TEXT)

XmlLexer::Lexer::Error::Error(const std::string& msg) : std::runtime_error(msg) { }

XmlLexer::Lexer::UnexpectedTokenError::UnexpectedTokenError(const std::string &msg)
: Error(msg) { }

char XmlLexer::Lexer::currentChar() {
    return source[index];
}

char XmlLexer::Lexer::relativeChar(size_t offset) {
    return source[index + offset];
}

XmlLexer::Lexer::Lexer(std::string source) {
    this->source = source;
}

XmlLexer::Token XmlLexer::Lexer::_next() {
    if (currentChar() == Symbol::SPACE) {
        move(1);
        return _next();
    }
    if (currentChar() == Symbol::NEWLINE) {
        newline();
        return _next();
    }
    if (currentChar() == Symbol::NULL_TERM) {
        return MAKE_TOKEN(Token::Type::END);
    }
    switch (currentChar()) {
        case XmlLexer::Symbol::CL_PAREN:
        case XmlLexer::Symbol::OP_PAREN:
        case XmlLexer::Symbol::CL_SHARD:
        case XmlLexer::Symbol::BANG:
        case XmlLexer::Symbol::EQUALS:
        case XmlLexer::Symbol::DOT:
            return parseUnaryToken();
        case XmlLexer::Symbol::OP_SHARD:
        case XmlLexer::Symbol::QUESTION:
        case XmlLexer::Symbol::DASH:
        case XmlLexer::Symbol::SLASH:
            return parseToken();
        case XmlLexer::Symbol::DBL_QUOTE:
            return parseStringLiteral();
        default:
            return parseWord();
    }
}

XmlLexer::Token XmlLexer::Lexer::parseUnaryToken() {
    move(1);
    switch (relativeChar(-1)) {
        case XmlLexer::Symbol::OP_SHARD:
            return MAKE_TOKEN(Token::Type::OP_SHARD_BRACKET);
        case XmlLexer::Symbol::CL_PAREN:
            return MAKE_TOKEN(Token::Type::CL_PAREN);
        case XmlLexer::Symbol::OP_PAREN:
            return MAKE_TOKEN(Token::Type::OP_PAREN);
        case XmlLexer::Symbol::CL_SHARD:
            return MAKE_TOKEN(Token::Type::CL_SHARD);
        case XmlLexer::Symbol::BANG:
            return MAKE_TOKEN(Token::Type::BANG);
        case XmlLexer::Symbol::EQUALS:
            return MAKE_TOKEN(Token::Type::EQUALS);
        case XmlLexer::Symbol::DOT:
            return MAKE_TOKEN(Token::Type::DOT);
    }
    throw UnreachableError();
}

XmlLexer::Token XmlLexer::Lexer::parseToken() {
    switch (currentChar()) {
        case XmlLexer::Symbol::OP_SHARD: {
            // Open comment
            if (relativeChar(1) == Symbol::BANG &&
                relativeChar(2) == Symbol::DASH &&
                relativeChar(3) == Symbol::DASH) {
                move(4); return MAKE_TOKEN(Token::Type::OP_COMMENT);
            }

            // Open close tag
            if (relativeChar(1) == Symbol::SLASH) {
                move(2); return MAKE_TOKEN(Token::Type::OP_CLOSE_TAG);
            }

            // Processing instruction
            if (relativeChar(1) == Symbol::QUESTION) {
                move(2); return MAKE_TOKEN(Token::Type::OP_INSTRUCTION);
            }
            break;
        }

        case Symbol::DASH: {
            // Close comment
            if (relativeChar(1) == Symbol::DASH &&
                relativeChar(2) == Symbol::CL_SHARD) {
                move(3); return MAKE_TOKEN(Token::Type::CL_COMMENT);
            }
            break;
        }

        case Symbol::QUESTION: {
            // Close instruction
            if (relativeChar(1) == Symbol::CL_SHARD) {
                move(2); return MAKE_TOKEN(Token::Type::CL_INSTRUCTION);
            }
        }

        case Symbol::SLASH: {
            // Close empty tag
            if (relativeChar(1) == Symbol::CL_SHARD) {
                move(2); return MAKE_TOKEN(Token::Type::CL_EMPTY_TAG);
            }
        }
    }
    return parseUnaryToken();
}

XmlLexer::Token XmlLexer::Lexer::parseStringLiteral() {
    std::string str;
    move(1);
    while (currentChar() != Symbol::DBL_QUOTE) {
        str += currentChar();
        move(1);
    }
    move(1);
    return MAKE_TEXT_TOKEN(Token::Type::STRING, str);
}

bool isNumberOrDigit(char c) {
    return ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'));
}

XmlLexer::Token XmlLexer::Lexer::parseWord() {
    std::string word;
    while (isNumberOrDigit(currentChar())) {
        word += currentChar();
        move(1);
    }
    return MAKE_TEXT_TOKEN(Token::Type::WORD, word);
}

void XmlLexer::Lexer::move(size_t offset) {
    col += offset;
    index += offset;
}

void XmlLexer::Lexer::newline() {
    col = 0;
    row++;
    index++;
}

XmlLexer::Token XmlLexer::Lexer::current() {
    return currentToken;
}

XmlLexer::Token XmlLexer::Lexer::next() {
    return currentToken = _next();
}

bool XmlLexer::Lexer::hasNext() {
    return currentToken.getType() != Token::Type::END;
}

void XmlLexer::Lexer::expect(XmlLexer::Token::Type expectedType) {
    if (currentToken.getType() != expectedType) {
        throw UnexpectedTokenError("Unexpected token type `" + currentToken.getTypeName() + "` at " + currentToken.getLocation() + ", expected: `"
                                    + currentToken.getTypeName(expectedType) + "`");
    }
}

#undef MAKE_TOKEN
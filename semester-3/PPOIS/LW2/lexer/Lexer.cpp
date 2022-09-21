#include "Lexer.h"

#include <utility>

#define MAKE_TOKEN(TYPE) XmlLexer::Token(TYPE, row, col)
#define MAKE_TEXT_TOKEN(TYPE, TEXT) XmlLexer::Token(TYPE, row, col, TEXT)

XmlLexer::Lexer::Error::Error(const std::string& msg) : std::runtime_error(msg) { }

std::string XmlLexer::Lexer::UnexpectedTokenError::makeExpectedString(XmlLexer::Token token,
                                                                      std::initializer_list<Token::Type> expected) {
    auto s = "Unexpected token type `" + token.getTypeName() + "`, expected: ";
    for (auto type : expected) {
        s.append(token.getTypeName(type));
        s.append(", ");
    }
    return s;
}

XmlLexer::Lexer::UnexpectedTokenError::UnexpectedTokenError(Token token, std::initializer_list<Token::Type> expected)
: Error(makeExpectedString(std::move(token), expected)) { }

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
        case XmlLexer::Symbol::CL_SQUARE:
        case XmlLexer::Symbol::OP_SQUARE:
        case XmlLexer::Symbol::OP_SHARD:
        case XmlLexer::Symbol::QUESTION:
        case XmlLexer::Symbol::DASH:
        case XmlLexer::Symbol::SLASH:
        case XmlLexer::Symbol::COMMA:
        case XmlLexer::Symbol::BAR:
        case XmlLexer::Symbol::AMPERSAND:
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
        case XmlLexer::Symbol::SLASH:
            return MAKE_TOKEN(Token::Type::SLASH);
        case XmlLexer::Symbol::OP_SQUARE:
            return MAKE_TOKEN(Token::Type::OP_SQUARE);
        case XmlLexer::Symbol::CL_SQUARE:
            return MAKE_TOKEN(Token::Type::CL_SQUARE);
        case XmlLexer::Symbol::COMMA:
            return MAKE_TOKEN(Token::Type::COMMA);
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

        case Symbol::BAR: {
            // OR
            if (relativeChar(1) == Symbol::BAR) {
                move(2); return MAKE_TOKEN(Token::Type::DBL_BAR);
            }
        }

        case Symbol::AMPERSAND: {
            if (relativeChar(1) == Symbol::AMPERSAND) {
                move(2); return MAKE_TOKEN(Token::Type::DBL_AMPERSAND);
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
    opLog.emplace_back(row, col, index);
    return currentToken = _next();
}

XmlLexer::Token XmlLexer::Lexer::previous() {
    opLog.pop_back();
    auto entry = opLog[opLog.size() - 1];
    row = entry.row;
    col = entry.col;
    index = entry.ind;
    return currentToken = _next();
}

bool XmlLexer::Lexer::hasNext() {
    return currentToken.getType() != Token::Type::END;
}

void XmlLexer::Lexer::expect(std::initializer_list<Token::Type> types) {
    Token t = currentToken;
    if (!std::any_of(types.begin(), types.end(), [t](Token::Type tp){ return tp == t.getType(); })) {
        throw UnexpectedTokenError(currentToken, types);
    }
}

#undef MAKE_TOKEN

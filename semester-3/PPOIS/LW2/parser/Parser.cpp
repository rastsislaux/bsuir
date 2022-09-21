//
// Created by rostislav on 9/20/22.
//

#include "Parser.h"

XmlParser::Parser::Parser(std::string source) : lexer(source) { }

Xml::Document XmlParser::Parser::parse() {
    root = std::make_shared<Xml::Document>();
    tag = nullptr;

    while(lexer.hasNext()) {
        auto token = lexer.next();
        switch (token.getType()) {
            case XmlLexer::Token::Type::OP_INSTRUCTION: {
                parseInstruction();
            } break;
            case XmlLexer::Token::Type::OP_COMMENT: {
                skipComments();
            } break;
            case XmlLexer::Token::Type::OP_SHARD_BRACKET: {
                openTag();
            } break;
            case XmlLexer::Token::Type::OP_CLOSE_TAG: {
                closeTag();
            } break;
            case XmlLexer::Token::Type::END: break;
            default:
                parseContent();
        }
    }

    return *root;
}

void XmlParser::Parser::parseContent() {
    auto cnt = tag->getContent();
    while (lexer.current().getType() != XmlLexer::Token::Type::OP_CLOSE_TAG &&
           lexer.current().getType() != XmlLexer::Token::Type::OP_SHARD_BRACKET &&
           lexer.current().getType() != XmlLexer::Token::Type::OP_COMMENT) {
         cnt += lexer.current().getText() + " ";
         lexer.next();
    }
    lexer.previous();
    tag->setContent(cnt);
}

void XmlParser::Parser::openTag() {
    auto newTag = std::make_shared<Xml::Tag>();
    newTag->setParent(tag);
    newTag->setRoot(root);

    lexer.next();
    lexer.expect({XmlLexer::Token::Type::WORD});
    newTag->setName(lexer.current().getText());
    lexer.next();
    while (lexer.current().getType() != XmlLexer::Token::Type::CL_SHARD &&
           lexer.current().getType() != XmlLexer::Token::Type::CL_EMPTY_TAG) {
        lexer.expect({XmlLexer::Token::Type::WORD});
        auto key = lexer.current().getText();
        lexer.next();
        lexer.expect({XmlLexer::Token::Type::EQUALS});
        lexer.next();
        lexer.expect({XmlLexer::Token::Type::STRING});
        auto value = lexer.current().getText();
        newTag->setAttribute(key, value);
        lexer.next();
    }
    if (lexer.current().getType() == XmlLexer::Token::Type::CL_EMPTY_TAG) {
        insertTag(newTag);
    } else {
        insertTag(newTag);
        tag = newTag;
    }
}

void XmlParser::Parser::closeTag() {
    lexer.next();
    lexer.expect({XmlLexer::Token::Type::WORD});
    if (lexer.current().getText() != tag->getName()) {
        throw Error("Unexpected tag closed: " + lexer.current().getText(), lexer.current().getLocation());
    }
    lexer.next();
    lexer.expect({XmlLexer::Token::Type::CL_SHARD});
    tag = tag->getParent();
}

void XmlParser::Parser::parseInstruction() {
    if (tag != nullptr) {
        throw Error("Unexpected construction: `processing instruction`.", lexer.current().getLocation());
    }

    std::map<std::string, std::string> instructionsMap;

    lexer.next();
    lexer.expect({XmlLexer::Token::Type::WORD});
    if (lexer.current().getText() != "xml") { throw Error("Only xml supported", lexer.current().getLocation()); }

    lexer.next();
    while (lexer.current().getType() != XmlLexer::Token::Type::CL_INSTRUCTION) {
        lexer.expect({XmlLexer::Token::Type::WORD});
        auto key = lexer.current().getText();
        lexer.next();
        lexer.expect({XmlLexer::Token::Type::EQUALS});
        lexer.next();
        lexer.expect({XmlLexer::Token::Type::STRING});
        auto value = lexer.current().getText();

        instructionsMap[key] = value;
        lexer.next();
    }

    root->setInstruction(instructionsMap);
}

void XmlParser::Parser::skipComments() {
    while (lexer.next().getType() != XmlLexer::Token::Type::CL_COMMENT);
}

void XmlParser::Parser::insertTag(const std::shared_ptr<Xml::Tag>& newTag) {
    if (tag == nullptr) {
        root->addChild(newTag);
    } else {
        tag->addChild(newTag);
    }
}

XmlParser::Parser::Error::Error(const std::string &msg, const std::string& location)
: std::runtime_error(msg + "(" + location + ")") { }


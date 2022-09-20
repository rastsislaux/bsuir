//
// Created by rostislav on 9/20/22.
//

#include "Parser.h"

XmlParser::Parser::Parser(std::string source) : lexer(source) { }

Xml::Document XmlParser::Parser::parse() {
    auto document = Xml::Document();

    while(lexer.hasNext()) {
        auto token = lexer.next();
        switch (token.getType()) {
            case XmlLexer::Token::Type::OP_INSTRUCTION: {
                auto instructions = parseInstruction();
                document.setInstruction(instructions);
            }
            break;

            case XmlLexer::Token::Type::OP_COMMENT: {
                skipComments();
            }
            break;

            case XmlLexer::Token::Type::END: break;

            default:
                throw Error("Unexpected token type `" + lexer.current().getTypeName() + "` ", lexer.current().getLocation());
        }
    }

    return document;
}

std::map<std::string, std::string> XmlParser::Parser::parseInstruction() {
    std::map<std::string, std::string> instructionsMap;

    lexer.next();
    lexer.expect(XmlLexer::Token::Type::WORD);
    if (lexer.current().getText() != "xml") { throw Error("Only xml supported", lexer.current().getLocation()); }

    lexer.next();
    while (lexer.current().getType() != XmlLexer::Token::Type::CL_INSTRUCTION) {
        lexer.expect(XmlLexer::Token::Type::WORD);
        auto key = lexer.current().getText();
        lexer.next();
        lexer.expect(XmlLexer::Token::Type::EQUALS);
        lexer.next();
        lexer.expect(XmlLexer::Token::Type::STRING);
        auto value = lexer.current().getText();

        instructionsMap[key] = value;
        lexer.next();
    }

    return instructionsMap;
}

void XmlParser::Parser::skipComments() {
    while (lexer.next().getType() != XmlLexer::Token::Type::CL_COMMENT);
}

XmlParser::Parser::Error::Error(const std::string &msg, const std::string& location)
: std::runtime_error(msg + "(" + location + ")") { }


#ifndef RSTSLV_LEXER
#define RSTSLV_LEXER

#include <exception>
#include <stdexcept>
#include <string>

#include "consts.h"
#include "Token.h"
#include "exceptions.h"

namespace XmlLexer {

    class Lexer {
        std::string source;

        size_t col = 0;
        size_t row = 0;
        size_t index = 0;

        Token currentToken = Token(Token::Type::ERROR, 0, 0, "Lexing is yet to be started.");

        char currentChar();

        char relativeChar(size_t offset);

        void newline();

        void move(size_t offset);

        Token parseUnaryToken();

        Token parseToken();

        Token parseStringLiteral();

        Token parseWord();

        Token _next();

    public:
        class Error : public std::runtime_error {
        public:
            explicit Error(const std::string &msg);
        };

        class UnexpectedTokenError : public Error {
        public:
            explicit UnexpectedTokenError(const std::string &msg);
        };

        [[maybe_unused]] explicit Lexer(std::string source);

        Token next();

        Token current();

        void expect(Token::Type expectedType);

        bool hasNext();
    };

};

#endif
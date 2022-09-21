//
// Created by rostislav on 9/20/22.
//

#ifndef XML_TREE_TOKEN_H
#define XML_TREE_TOKEN_H

#include <string>
#include <exception>
#include <stdexcept>
#include <map>
#include <boost/format.hpp>

namespace XmlLexer {

    class Token {
    public:
        enum class Type {
            OP_INSTRUCTION,
            CL_INSTRUCTION,
            OP_SHARD_BRACKET,
            CL_SHARD,
            OP_PAREN,
            CL_PAREN,
            TEXT,
            OP_COMMENT,
            CL_COMMENT,
            BANG,
            EQUALS,
            OP_CLOSE_TAG,
            CL_EMPTY_TAG,
            DOT,

            WORD,
            STRING,

            END,
            ERROR,
        };
    private:
        Type type;
        std::string text;
        size_t row;
        size_t col;

        #define GEN_ENTRY(TYPE) { Type::TYPE, #TYPE }
        std::map<Type, std::string> typeNameMap = {
                GEN_ENTRY(OP_INSTRUCTION),
                GEN_ENTRY(CL_INSTRUCTION),
                GEN_ENTRY(OP_SHARD_BRACKET),
                GEN_ENTRY(CL_SHARD),
                GEN_ENTRY(OP_PAREN),
                GEN_ENTRY(CL_PAREN),
                GEN_ENTRY(TEXT),
                GEN_ENTRY(OP_COMMENT),
                GEN_ENTRY(CL_COMMENT),
                GEN_ENTRY(BANG),
                GEN_ENTRY(END),
                GEN_ENTRY(STRING),
                GEN_ENTRY(WORD),
                GEN_ENTRY(EQUALS),
                GEN_ENTRY(OP_CLOSE_TAG),
                GEN_ENTRY(DOT),
                GEN_ENTRY(CL_EMPTY_TAG),
                GEN_ENTRY(ERROR),
        };
        #undef GEN_ENTRY

    public:
        std::string getTypeName();

        std::string getTypeName(Type type);

        class TokenError : public std::runtime_error {
        public:
            explicit TokenError(const std::string &msg);
        };

        class NotSupportedError : public TokenError {
        public:
            explicit NotSupportedError(const std::string &msg);
        };

        Token(Type type, size_t row, size_t col, std::string text = "");

        Type getType() const;

        std::string getText();

        std::string getLocation();

        std::string str();

        bool eof() {
            return type == Type::END;
        }
    };

}

#endif //XML_TREE_TOKEN_H

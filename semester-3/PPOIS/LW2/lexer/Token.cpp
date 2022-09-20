//
// Created by rostislav on 9/20/22.
//

#include "Token.h"

XmlLexer::Token::TokenError::TokenError(const std::string& msg) : std::runtime_error(msg) {

}

XmlLexer::Token::NotSupportedError::NotSupportedError(const std::string &msg) : TokenError(msg) {

}

XmlLexer::Token::Type XmlLexer::Token::getType() {
    return type;
}

std::string XmlLexer::Token::getText() {
    if (type != Type::TEXT && type != Type::ERROR) {
        throw NotSupportedError("This operation is not supported for this token type");
    }
    return text;
}

XmlLexer::Token::Token(XmlLexer::Token::Type type, size_t row, size_t col, std::string text) {
    this->type = type;
    this->row = row;
    this->col = col;
    this->text = text;
}

std::string XmlLexer::Token::str() {
    return (boost::format("[Token | Type: %1% | Row: %2% | Col: %3% | Text: `%4%`]") % typeNameMap[type] % row % col % text).str();
}

//
// Created by rostislav on 9/20/22.
//

#ifndef XML_TREE_CONSTS_H
#define XML_TREE_CONSTS_H

namespace XmlLexer::Symbol {
    static constexpr char OP_PAREN  = '(';
    static constexpr char CL_PAREN  = ')';
    static constexpr char OP_SHARD  = '<';
    static constexpr char CL_SHARD  = '>';
    static constexpr char QUESTION  = '?';
    static constexpr char DASH      = '-';
    static constexpr char DBL_QUOTE = '"';
    static constexpr char SLASH     = '/';
    static constexpr char BANG      = '!';
    static constexpr char SPACE     = ' ';
    static constexpr char NEWLINE   = '\n';
    static constexpr char NULL_TERM = '\0';
}

#endif //XML_TREE_CONSTS_H

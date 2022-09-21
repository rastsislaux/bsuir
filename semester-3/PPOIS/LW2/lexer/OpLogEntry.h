//
// Created by rostislav on 9/21/22.
//

#ifndef XML_TREE_OPLOGENTRY_H
#define XML_TREE_OPLOGENTRY_H

namespace XmlLexer {

    struct OpLogEntry {
        int row;
        int col;
        int ind;

        OpLogEntry(int row, int col, int ind) {
            this->row = row;
            this->col = col;
            this->ind = ind;
        }
    };

}

#endif //XML_TREE_OPLOGENTRY_H

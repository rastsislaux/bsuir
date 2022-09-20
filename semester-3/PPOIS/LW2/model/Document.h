//
// Created by rostislav on 9/20/22.
//

#ifndef XML_TREE_DOCUMENT_H
#define XML_TREE_DOCUMENT_H

#include <vector>
#include <string>
#include <map>

#include "Tag.h"

namespace Xml {

    class Document {

    protected:
        std::map<std::string, std::string> instructions;
        std::vector<Tag> children;
        std::string content;

    public:
        std::string getContent();

        std::vector<Tag> getChildren();

        std::string getInstruction(std::string name);

        void setInstruction(std::map<std::string, std::string> &instructions);
    };

}


#endif //XML_TREE_DOCUMENT_H

//
// Created by rostislav on 9/21/22.
//

#ifndef XML_TREE_HOLDER_H
#define XML_TREE_HOLDER_H

#include "../model/Document.h"
#include "Request.h"
#include <vector>

namespace XmlPath {

    class Holder {

        Xml::Document document;

        static std::vector<std::shared_ptr<Xml::Tag>> findInVector(std::vector<std::shared_ptr<Xml::Tag>> vect, std::string name);

    public:
        Holder(Xml::Document document);

        std::vector<std::shared_ptr<Xml::Tag>> find(XmlPath::Request req);;

        std::vector<std::shared_ptr<Xml::Tag>> filter(Request req, std::vector<std::shared_ptr<Xml::Tag>> tags);
    };

}


#endif //XML_TREE_HOLDER_H

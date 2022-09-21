//
// Created by rostislav on 9/21/22.
//

#ifndef XML_TREE_TAG_H
#define XML_TREE_TAG_H

#include <vector>
#include <string>
#include <map>
#include <memory>

#include "Document.h"

namespace Xml {

    class Document;

    class Tag {

        std::shared_ptr<Document> root;

        std::shared_ptr<Tag> parent;

        std::vector<std::shared_ptr<Tag>> children;

        std::string content;

        std::string name;

        std::map<std::string, std::string> attributes;

    public:
        void setName(std::string name);

        std::string getName();

        std::string getContent();

        void setContent(std::string content);

        std::vector<std::shared_ptr<Tag>> getChildren();

        void addChild(const std::shared_ptr<Tag>& child);

        std::string getAttribute(const std::string& key);

        void setAttribute(const std::string& key, std::string value);

        void setAttributes(std::map<std::string, std::string> attrs);

        void setRoot(std::shared_ptr<Document> root);

        std::shared_ptr<Document> getRoot();

        void setParent(std::shared_ptr<Tag> parent);

        std::shared_ptr<Tag> getParent();
    };

}


#endif //XML_TREE_TAG_H

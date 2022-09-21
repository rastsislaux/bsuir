//
// Created by rostislav on 9/20/22.
//

#include "Document.h"

std::string Xml::Document::getContent() {
    return content;
}

std::string Xml::Document::getInstruction(std::string name) {
    return instructions[name];
}

void Xml::Document::setInstruction(std::map<std::string, std::string>& instructions) {
    this->instructions = instructions;
}

void Xml::Document::addChild(const std::shared_ptr<Tag>& child) {
    children.emplace_back(child);
}

std::vector<std::shared_ptr<Xml::Tag>> Xml::Document::getChildren() {
    return children;
}

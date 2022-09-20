//
// Created by rostislav on 9/20/22.
//

#include "Document.h"

std::string Xml::Document::getContent() {
    return content;
}

std::vector<Xml::Tag> Xml::Document::getChildren() {
    return children;
}

std::string Xml::Document::getInstruction(std::string name) {
    return instructions[name];
}

void Xml::Document::setInstruction(std::map<std::string, std::string>& instructions) {
    this->instructions = instructions;
}

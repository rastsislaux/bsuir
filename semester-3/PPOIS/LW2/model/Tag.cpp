//
// Created by rostislav on 9/21/22.
//

#include "Tag.h"

#include <utility>

std::string Xml::Tag::getContent() {
    return content;
}

std::vector<std::shared_ptr<Xml::Tag>> Xml::Tag::getChildren() {
    return children;
}

std::string Xml::Tag::getAttribute(const std::string& key) {
    return attributes[key];
}

void Xml::Tag::setName(std::string name) {
    this->name = std::move(name);
}

std::string Xml::Tag::getName() {
    return name;
}

void Xml::Tag::setAttributes(std::map<std::string, std::string> attrs) {
    attributes = std::move(attrs);
}

void Xml::Tag::setAttribute(const std::string& key, std::string value) {
    attributes[key] = std::move(value);
}

void Xml::Tag::addChild(const std::shared_ptr<Tag>& child) {
    children.emplace_back(child);
}

void Xml::Tag::setRoot(std::shared_ptr<Document> root) {
    this->root = std::move(root);
}

std::shared_ptr<Xml::Document> Xml::Tag::getRoot() {
    return root;
}

void Xml::Tag::setParent(std::shared_ptr<Tag> parent) {
    this->parent = std::move(parent);
}

std::shared_ptr<Xml::Tag> Xml::Tag::getParent() {
    return parent;
}

void Xml::Tag::setContent(std::string content) {
    this->content = std::move(content);
}

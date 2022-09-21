//
// Created by rostislav on 9/21/22.
//

#include "Holder.h"

#include <utility>
#include <algorithm>

std::vector<std::shared_ptr<Xml::Tag>> XmlPath::Holder::find(XmlPath::Request req) {
    std::vector<std::shared_ptr<Xml::Tag>> tags = findInVector(document.getChildren(), req.path[0]);
    for (int i = 1; i < req.path.size(); i++) {
        tags = findInVector(tags[0]->getChildren(), req.path[i]);
    }
    if (!req.filters.empty()) {
        tags = filter(req, tags);
    }
    return tags;
}

std::vector<std::shared_ptr<Xml::Tag>> XmlPath::Holder::filter(XmlPath::Request req, std::vector<std::shared_ptr<Xml::Tag>> tags) {
    int filterNumber = 0;
    std::vector<std::shared_ptr<Xml::Tag>> prev;
    bool interrupt = false;
    while (!interrupt) {
        if (req.filters[filterNumber].oper == Filter::Operator::NO) {
            interrupt = true;
        }
        auto filter = req.filters[filterNumber++];
        auto filtered = filter.filter(tags);
        if (filterNumber == 1) { prev = filtered; continue; }
        auto oper = req.filters[filterNumber - 1].oper;
        switch (oper) {
            case Filter::Operator::OR:
                for (auto& elem : filtered) {
                    if (std::find(prev.begin(), prev.end(), elem) == prev.end()) {
                        prev.emplace_back(elem);
                    }
                }
                break;
            case Filter::Operator::AND:
                for (auto& elem : prev) {
                    if (std::find(filtered.begin(), filtered.end(), elem) == prev.end()) {
                        filtered.erase(std::remove(filtered.begin(), filtered.end(), elem));
                    }
                }
                prev = filtered;
                break;
        }
    }
    return prev;
}

std::vector<std::shared_ptr<Xml::Tag>>
XmlPath::Holder::findInVector(std::vector<std::shared_ptr<Xml::Tag>> vect, std::string name) {
    std::vector<std::shared_ptr<Xml::Tag>> tags;
    for (auto& tag : vect) {
        if (tag->getName() == name) {
            tags.emplace_back(tag);
        }
    }
    return tags;
}

XmlPath::Holder::Holder(Xml::Document document) {
    this->document = std::move(document);
}

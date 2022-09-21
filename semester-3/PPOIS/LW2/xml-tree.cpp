#include "parser/Parser.h"

#include <iostream>
#include <fstream>
#include <stdexcept>
#include <sstream>

#include "xmlpath/Request.h"
#include "xmlpath/Holder.h"

std::string readFile(std::string path) {
    std::string file_content;
    std::getline(std::ifstream(path), file_content, '\0');
    return file_content;
}

int main(int argc, char** argv) {
    std::string path;
    getline(std::cin, path);

    auto source = readFile(path);
    auto parser = XmlParser::Parser(source);
    auto document = parser.parse();

    std::string cmd;
    XmlPath::Holder holder(document);
    while (cmd != "!exit") {
        getline(std::cin, cmd);
        XmlPath::Request req(cmd);
        auto res = holder.find(req);
        int i = 0;
        for (auto& node : res) {
            std::cout << ++i << ". Name: " << node->getName() << "\n"
                             << "Text: " << node->getContent() << "\n"
                             << "Lang attr: " << node->getAttribute("lang") << std::endl;
        }

    }
}
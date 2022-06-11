//
// Created by rostislove on 16.5.22.
//

#include "setlib.h"

bool isEqual(string e1, string e2) {
    if (e1[0] == '{' ^ e2[0] == '{') return false;
    if (e1[0] == '{') {
        auto pe1 = parseString(e1);
        auto pe2 = parseString(e2);
        sort(pe1.begin(), pe1.end());
        sort(pe2.begin(), pe2.end());
        return pe1 == pe2;
    }
    return e1 == e2;
}

void checkForRepeats(const std::vector<std::string>& set) {
    for (const auto& elem : set)
        for (const auto& elem2 : set) {
            if (&elem == &elem2) continue;
            if (isEqual(elem, elem2))
            throw std::runtime_error("Failed to parse: found duplicate element: `" + elem + "`");
        }
}

void checkForBrackets(const std::string& str) {
    int curlyB = 0;
    int tupleB = 0;
    for (char ch : str) {
        if (ch == '{') curlyB++;
        else if (ch == '}') curlyB--;
        else if (ch == '<') tupleB++;
        else if (ch == '>') tupleB--;
    }

    if (curlyB > 0 || tupleB > 0)
        throw std::runtime_error("Failed to parse: there are unclosed brackets.");

    if (curlyB < 0 || tupleB < 0)
        throw std::runtime_error("Failed to parse: there are unopened brackets.");
}

std::vector<std::string> setl::parseString(std::string str, const int start, int finish) {
    using namespace std;

    if (finish == -1) finish = str.size() - 1;

    if (str[start] != '{') throw std::runtime_error("Failed to parse: No opening bracket at " + to_string(start));
    if (str[finish] != '}') throw std::runtime_error("Failed to parse: No closing bracket at " + to_string(finish));

    checkForBrackets(str);

    // Удаляем пробелы
    str.erase(
            std::remove_if(
                    str.begin(), str.end(), [](char ch){ return ch == ' '; }
            ), str.end()
    );

    str = str.substr(1, str.size() - 2);

    std::string content;
    std::vector<std::string> elements;
    int depth = 0;
    for (int i = 0; i <= str.size(); i++) {
        if (str[i] == '<' || str[i] == '{') {
            depth++;
            content += str[i];
        }
        else if (str[i] == '>' || str[i] == '}') {
            depth--;
            content += str[i];
        }
        else if ( depth != 0 || (str[i] != ',' && str[i] != '\0') ) {
            content += str[i];
        }
        else {
            elements.emplace_back(content);
            content = "";
        }
    }

    checkForRepeats(elements);

    return elements;
}

std::string setl::vectorToString(std::vector<std::string> set) {
    std::string str = "<";
    for (int i = 0; i < set.size(); i++) {
        str += set[i];
        if (i != set.size() - 1) {
            str += ", ";
        }
    }
    str += ">";
    return str;
}

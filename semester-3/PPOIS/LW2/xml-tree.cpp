#include "parser/Parser.h"

#include <iostream>

int main(int argc, char** argv) {
    auto lexer = XmlParser::Parser("<!-- special kind of processing instruction --> \n"
                                 "<?xml version=\"1.0\" test=\"hello, world\"?>\n");/*
                                 "<bookstore> <!-- Root tag -->\n"
                                 "    <book> <!-- nested tag -->\n"
                                 "        <!-- tag with an attribute -->\n"
                                 "        <title lang=\"ru\">Harry Potter</title>\n"
                                 "        <!-- text -->\n"
                                 "        This is a book about a wizard Harry Potter.\n"
                                 "        <price>15000</price>\n"
                                 "        <!-- empty tag -->\n"
                                 "        <count />\n"
                                 "    </book>\n"
                                 "    <book>\n"
                                 "        <title lang=\"ru\">Learning XML</title>\n"
                                 "        <price>20000</price>\n"
                                 "        <count>5</count>\n"
                                 "    </book>\n"
                                 "</bookstore>");*/

    auto doc = lexer.parse();
    auto i = doc.getInstruction("test");
    printf("%s", i.c_str());
}
//
// Created by rostislove on 16.5.22.
//

#ifndef LW3_SETPARSE_H
#define LW3_SETPARSE_H

#include <string>
#include <vector>
#include <stdexcept>

namespace setl {

     std::vector<std::string> parseString(std::string str, int start = 0, int finish = -1);

     std::string vectorToString(std::vector<std::string> set);

};

#endif //WANNCLANG_SETPARSE_H

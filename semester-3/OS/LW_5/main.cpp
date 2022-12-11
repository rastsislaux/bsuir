#include <iostream>
#include <iomanip>

#include "fs.h"

int main(int argc, char** argv) {

    if (argc < 3) {
        std::cout << "not enough args." << std::endl;
        exit(1);
    }

    std::string drive = std::string(argv[1]);
    std::string command = std::string(argv[2]);

    if (command == "create") {
        auto _fs = fs::create(drive);
        _fs.save_state();
    }

    if (command == "ls") {
        auto _fs = fs::load(drive);
        std::cout << "attrs" << std::setw(15) << "length" << std::setw(15) << "name" << std::endl;
        for (const auto& file : _fs.files) {
            std::cout << file.is_hidden() << file.is_readonly() << file.is_system()
                      << std::setw(15)
                      << file.content.size()
                      << std::setw(17)
                      << file.name
                      << std::endl;
        }
    }

    if (command == "touch") {
        if (argc < 4) {
            std::cout << "usage: {executable} {drive} touch {filename}" << std::endl;
            exit(1);
        }

        std::string filename = std::string(argv[3]);
        auto _fs = fs::load(drive);

        if (_fs.file_exists(filename)) {
            std::cout << "file already exists" << std::endl;
            exit(1);
        }

        _fs.add_file(fs::file{ filename, 0x0, {} });
        _fs.save_state();
    }

    if (command == "rm") {
        if (argc < 4) {
            std::cout << "usage: {executable} {drive} rm {filename}" << std::endl;
            exit(1);
        }

        std::string filename = std::string(argv[3]);
        auto _fs = fs::load(drive);

        _fs.rm_file(filename);
        _fs.save_state();
    }

    if (command == "cp") {
        if (argc < 5) {
            std::cout << "usage: {executable} {drive} cp {src} {trg}" << std::endl;
            exit(1);
        }

        std::string src_name = std::string(argv[3]);
        std::string trg_name = std::string(argv[4]);

        auto _fs = fs::load(drive);

        if (!_fs.file_exists(src_name)) {
            std::cout << "file not found" << std::endl;
            exit(1);
        }

        if (_fs.file_exists(trg_name)) {
            std::cout << "file already exists" << std::endl;
            exit(1);
        }

        _fs.cp_file(src_name, trg_name);
        _fs.save_state();
    }

    if (command == "mv") {
        if (argc < 5) {
            std::cout << "usage: {executable} {drive} mv {old} {new}" << std::endl;
            exit(1);
        }

        std::string old_name = std::string(argv[3]);
        std::string new_name = std::string(argv[4]);

        auto _fs = fs::load(drive);

        if (!_fs.file_exists(old_name)) {
            std::cout << "file not found" << std::endl;
            exit(1);
        }

        if (_fs.file_exists(new_name)) {
            std::cout << "file already exists" << std::endl;
            exit(1);
        }

        _fs.mv_file(old_name, new_name);
        _fs.save_state();
    }

    if (command == "cat") {
        if (argc < 4) {
            std::cout << "usage: {executable} {drive} cat {filename}" << std::endl;
            exit(1);
        }

        std::string filename = std::string(argv[3]);
        auto _fs = fs::load(drive);

        if (!_fs.file_exists(filename)) {
            std::cout << "file not found" << std::endl;
            exit(1);
        }

        auto f = _fs.get_file_by_name(filename);
        for (const auto& c : f.content) {
            std::cout << c;
        }
        std::cout << std::endl;
    }

    if (command == "write") {
        if (argc < 5) {
            std::cout << "usage: {executable} {drive} write {filename} {data...}" << std::endl;
            exit(1);
        }

        std::string filename = std::string(argv[3]);
        std::string content;
        for (int i = 4; i < argc; i++) {
            content += argv[i];
            content += ' ';
        }
        auto _fs = fs::load(drive);

        if (!_fs.file_exists(filename)) {
            std::cout << "file not found" << std::endl;
            exit(1);
        }

        _fs.set_content(filename, content);
        _fs.save_state();
    }

    return 0;
}

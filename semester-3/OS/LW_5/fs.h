//
// Created by rlipski on 10/12/2022.
//

#ifndef OS_LW_4_FS_H
#define OS_LW_4_FS_H


#include <string>
#include <fstream>
#include <utility>
#include <vector>
#include <cstring>
#include <iostream>
#include <algorithm>

class fs {
public:

    typedef unsigned char attrs_t;

    template<class T>
    static void convert_to_bytes(T anything, char* array) {
        memcpy(array, &anything, sizeof(T));
    }

    template<class T>
    static T convert_to_prim(char* str) {
        T trg;
        memcpy(&trg, str, sizeof(T));
        return trg;
    }

    static std::vector<char> str_to_vect(std::string str) {
        return { str.begin(), str.end() };
    }

    static std::string vect_to_str(std::vector<char> vect) {
        return { vect.begin(), vect.end() };
    }

    template<class T>
    static void write_prim(std::ostream& out, const T& value, std::streamsize length) {
        char* bytes = new char[length];
        convert_to_bytes(value, bytes);
        out.write(bytes, length);
    }

    static char* read_bytes(std::istream& in, int t) {
        char* bytes = new char[t + 1] { 0 };
        in.read(bytes, t);
        return bytes;
    }

    template<class T>
    static T read_prim(std::istream& in) {
        return convert_to_prim<T>(read_bytes(in, sizeof(T)));
    }

    struct file {
        static attrs_t const ATTR_READONLY = 0x1;
        static attrs_t const ATTR_HIDDEN = 0x2;
        static attrs_t const ATTR_SYSTEM = 0x4;

        std::string name;
        attrs_t attrs;
        std::vector<char> content;

        [[nodiscard]] bool is_readonly() const {
            return attrs & ATTR_READONLY;
        }

        [[nodiscard]] bool is_hidden() const {
            return attrs & ATTR_HIDDEN;
        }

        [[nodiscard]] bool is_system() const {
            return attrs & ATTR_SYSTEM;
        }
    };

    class fs_error: std::runtime_error {
    public:
        explicit fs_error(const std::string& msg) : std::runtime_error(msg) { }
    };

    const std::string image_path;

    std::vector<file> files;

    fs(std::string image_path) : image_path(std::move(image_path)) { }

    void save_state() {
        std::ofstream out(image_path, std::ios::binary);
        // write magic
        out.write("MYFS", 4);

        write_prim(out, files.size(), sizeof(size_t));

        for (const auto& file : files) {
            write_prim(out, file.name.length(), sizeof(long long));
            out.write(file.name.c_str(), file.name.size());
            write_prim(out, file.attrs, sizeof(attrs_t));
            write_prim(out, file.content.size(), sizeof(long long));
            auto content = vect_to_str(file.content);
            out.write(content.c_str(), content.size());
        }

        out.close();
    }

    static void check_magic(const std::string& magic) {
        if (magic != "MYFS") {
            throw fs_error("Incorrect file type");
        }
    }

    void load_state() {
        std::ifstream in(image_path, std::ios::binary);
        check_magic(read_bytes(in, 4));
        auto pages_count = read_prim<size_t>(in);
        for (int i = 0; i < pages_count; i++) {
            auto filename_length = read_prim<size_t>(in);

            std::string filename;
            for (int j = 0; j < filename_length; j++) {
                filename += read_prim<char>(in);
            }

            auto attrs = read_prim<attrs_t>(in);
            auto content_length = read_prim<size_t>(in);
            std::vector<char> content;
            for (int j = 0; j < content_length; j++) {
                content.emplace_back(read_prim<char>(in));
            }

            files.emplace_back(file{filename, attrs, content});
        }

        in.close();
    }

    void add_file(file f) {
        if (file_exists(f.name)) {
            throw fs_error("file exists");
        }
        this->files.emplace_back(f);
    }

    void rm_file(const std::string& filename) {
        erase_if(files, [filename](const file& f) {
            return f.name == filename;
        });
    }

    file get_file_by_name(const std::string& name) {
        auto it = std::find_if(files.begin(), files.end(), [name](file f) {
            return f.name == name;
        });
        if (it == files.end()) {
            throw fs_error("file not found");
        }
        return *it;
    }

    void cp_file(const std::string& src_name, const std::string& trg_name) {
        auto f = file{get_file_by_name(src_name)};
        f.name = trg_name;
        add_file(f);
    }

    void mv_file(const std::string& old_name, const std::string& new_name) {
        cp_file(old_name, new_name);
        rm_file(old_name);
    }

    bool file_exists(const std::string& filename) {
        return std::ranges::any_of(files.begin(), files.end(), [filename](const file& f) {
            return f.name == filename;
        });
    }

    void set_content(const std::string& filename, const std::string& content) {
        auto f = get_file_by_name(filename);
        f.content = std::vector<char>(content.begin(), content.end());
        rm_file(filename);
        add_file(f);
    }

    static fs create(std::string image_path) {
        return { std::move(image_path) };
    }

    static fs load(std::string image_path) {
        auto _fs = fs(std::move(image_path));
        _fs.load_state();
        return _fs;
    }

};


#endif //OS_LW_4_FS_H

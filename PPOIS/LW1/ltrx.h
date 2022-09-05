//
// Created by rostislav on 9/6/22.
//

#ifndef LTRX_LTRX_H
#define LTRX_LTRX_H


#include <vector>

namespace ltrx {

    template<typename T>
    class matrix {
    private:
        std::vector<std::vector<T>> _matrix;
        size_t width;
        size_t height;

    public:
        class functor {
        public:
            virtual T& invoke(T& elem) = 0;
        };

        class const_functor {
        public:
            virtual const T& invoke(const T& elem) = 0;
        };

    private:
        class inc_functor : public functor {
            T& invoke(T& elem) override {
                return elem++;
            }
        };

        class dec_functor : public functor {
            T& invoke(T& elem) override {
                return elem--;
            }
        };

    public:
        matrix(T default_value, size_t width, size_t height) {
            this->width = width;
            this->height = height;
            _matrix.resize(width);
            for (auto& line : _matrix) {
                line = std::vector<T>();
                line.resize(height, default_value);
            }
        }

        matrix& map(functor f) {
            for (const auto& line : _matrix) {
                for (auto& elem : line) {
                    elem = f.invoke(elem);
                }
            }
            return *this;
        }

        void for_each(const_functor f) const {
            for (const auto& line : _matrix) {
                for (const auto &elem : line) {
                    f.invoke(elem);
                }
            }
        }

        matrix& operator++() {
            this->map(inc_functor());
            return *this;
        }

        matrix operator++(int) {
            const matrix<T> temp = *this;
            this->map(inc_functor());
            return temp;
        }

        matrix& operator--() {
            this->map(dec_functor());
            return *this;
        }

        [[nodiscard]] std::pair<int, int> size() const {
            return {width, height};
        }
    };

}


#endif //LTRX_LTRX_H

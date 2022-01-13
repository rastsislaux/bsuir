//
// Created by rostislove on 03.01.2022.
//

#ifndef GRAPHICS_BUFFER_H
#define GRAPHICS_BUFFER_H

#include "unistd.h"
#include "cstdio"
#include "sys/ioctl.h"

class Buffer {

private:
    int width, height;
    char * buffer;

public:

    Buffer(int width, int height) {

        this->width = width;
        this->height = height;

        buffer = new char[width * height + 1];
        buffer[width * height] = '\0';

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                buffer[i + j * width] = '.';
                setCursorPosition(i, j);
                printf("%c", buffer[i + j * width]);
            }

    }

    void setImage(char * image) {

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++) {
                int index = i + j * width;
                if (image[index] != buffer[index]) {
                    buffer[index] = image[index];
                    setCursorPosition(i, j);
                    printf("%c", buffer[index]);
                }
            }

    }

    static void setCursorPosition(int j, int i) {
        printf("\033[%d;%dH", i+1, j+1);
    }

    static int getConsoleWidth() {
        struct winsize w;
        ioctl(STDOUT_FILENO, TIOCGWINSZ, &w);
        return w.ws_col;
    }

    static int getConsoleHeight() {
        struct winsize w;
        ioctl(STDOUT_FILENO, TIOCGWINSZ, &w);
        return w.ws_row;
    }

};


#endif //GRAPHICS_BUFFER_H

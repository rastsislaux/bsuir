//
// Created by rostislove on 04.01.2022.
//

#ifndef LW8_VIDEO_H
#define LW8_VIDEO_H

#define _floatframe std::vector<float>

#include "Animation2D.h"
#include "vector"
#include "fstream"

class Video : public Animation2D {

private:
    std::vector< _floatframe > frames;

public:

    Video(int width, int height, char * path) : Animation2D(width, height) {

        std::ifstream fin(path);

        if (!fin.is_open()) {
            printf("Failed to open a video file :(\n");
            exit(1);
        }

        while(frames.size() < 3000) {

            _floatframe temp;

            for (int i = 0; i < height; i++)
                for (int j = 0; j < width; j++) {
                    float tempf;
                    fin >> tempf;
                    temp.push_back(tempf);
                }

            frames.push_back(temp);

        }

    }

    char * generateFrame(int t) override {

        char * frame = new char[width * height + 1];
        frame[width * height] = '\0';

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                frame[i + j * width] = frames[t % frames.size()][i + j * width] > 120. ? '@' : ' ';

        nanosleep((const struct timespec[]){{0, 25000000L}}, NULL);
        return frame;

    }

};


#endif //LW8_VIDEO_H

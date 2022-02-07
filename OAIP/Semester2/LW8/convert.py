#! /bin/python3

import sys
import numpy

import moviepy.editor as mpy
import moviepy.video.fx.all as mpy2


def rgb_to_gray(pixel: numpy.ndarray):
    return sum(pixel)/pixel.size


def frame_to_string(frame):

    width = int(frame[0].size / 3)
    height = int(frame.size / width / 3)

    result = ''

    for j in range(height):
        for i in range(width):
            result += f"{rgb_to_gray(frame[j][i])} "
        result += '\n'

    return result


def main(args: list):

    video = mpy.VideoFileClip(args[0])
    video = mpy2.fx.resize.resize(video, (args[1], args[2]))

    with open(args[0] + '.txtv', 'w') as output_file:
        output_file.write(f"{args[1]} {args[2]} {video.fps}\n")

        for i, frame in enumerate(video.iter_frames()):
            output_file.write(
                frame_to_string(frame) + '\n'
            )


if __name__ == '__main__':
    main(sys.argv[1:])

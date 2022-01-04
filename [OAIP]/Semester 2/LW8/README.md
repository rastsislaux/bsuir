# How to run

1. `sudo apt install cmake`
2. cd into the working directory
3. `cmake .`
4. `make`
5. `./LW8`

## Video

In order to get video working you need to create a videofile in a special format:
1. Get your video in a working directory (works with mp4, not sure about other formats)
2. `pip3 install moviepy`
3. `python convert.py [videoname] [width] [height]`
4. Put .txtv file into the directory of binary file.

Important! You must check your console width&height before converting video.
If you try to play a video with a wrong resolution you'll see nothing but mess.

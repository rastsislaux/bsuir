#include "Animations2D.h"
#include "Video.h"
#include "Buffer.h"

int main() {

    int width   =   Buffer::getConsoleWidth(),
        height  =   Buffer::getConsoleHeight();

    system("clear");

    printf("Console resolution: %d x %d\n", width, height);

    printf("What animation would you like to see?"
           "\n\t1 - Sinus function"
           "\n\t2 - Cosinus function"
           "\n\t3 - Sinus and cosinus functions"
           "\n\t4 - Pumping Circle"
           "\n\t5 - Pumping Gradient"
           "\n\t6 - Falling ball"
           "\n\t7 - Spinning line"
           "\n\t8 - Show video"
           "\nYour choice: ");

    int mode;
    scanf("%d", &mode);

    Animation2D * animation;
    switch (mode) {
        case 1:
            animation = new SinusAnimation(width, height);
            break;
        case 2:
            animation = new CosinusAnimation(width, height);
            break;
        case 3:
            animation = new CosinusAndSinusAnimation(width, height);
            break;
        case 4:
            animation = new PumpingCircleAnimation(width, height);
            break;
        case 5:
            animation = new PumpingGradientAnimation(width, height);
            break;
        case 6:
            animation = new FallingBallAnimation(width, height);
            break;
        case 7:
            animation = new SpinningLine(width, height);
            break;
        case 8:
            animation = new Video(width, height, "video.mp4.txtv");
            break;
        default:
            printf("Wrong animation number.\n");
            return 1;
    }

    Buffer buffer(width, height);

    for (int t = 0; t < 100000; t++) {
        char * frame = (*animation).generateFrame(t);
        buffer.setImage(frame);
    }

    getchar();
    return 0;
}

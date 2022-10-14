#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>
#include <stdbool.h>
#include <sys/time.h>

int main() {
    int number = 1;

    // 1st
    printf("STR (%d -> %d)\n", getppid(), getpid());

    if (fork() == 0) {
        // 2nd
        number = 2;
        printf("2. (%d -> %d)\n", getppid(), getpid());
        if (fork() == 0) {
            // 5th
            number = 5;
            printf("5. (%d -> %d)\n", getppid(), getpid());
            if (fork() == 0) {
                // 6th
                number = 6;
                printf("6. (%d -> %d)\n", getppid(), getpid());
            } else {
                if (fork() == 0) {
                    // 7th
                    number = 7;
                    printf("7. (%d -> %d)\n", getppid(), getpid());
                }
            }
        }
    } else {
        if (fork() == 0) {
            // 3rd
            number = 3;
            printf("3. (%d -> %d)\n", getppid(), getpid());
            execl("/bin/time", "/bin/time", "-p", "/bin/ls");
        } else {
            if (fork() == 0) {
                // 4th
                number = 4;
                printf("4. (%d -> %d)\n", getppid(), getpid());
            }
        }
    }


    while (true) {
        struct timeval t;
        gettimeofday(&t, NULL);
        printf("%d. PID: %7d, PPID: %7d, %lldms\n", number, getpid(), getppid(), t.tv_sec*1000LL + t.tv_usec/1000);
        usleep(number * 200000);
    }
    printf("STP (%d -> %d)\n", getppid(), getpid());
    return 0;
}

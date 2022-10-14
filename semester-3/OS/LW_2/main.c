#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>
#include <sys/time.h>
#include <time.h>

int main() {
    int number = 1;
    printf("1. (%d -> %d)\n", getppid(), getpid());

    if (fork() == 0) {
        number = 2;
        printf("2. (%d -> %d)\n", getppid(), getpid());
    } else {
        if (fork() == 0) {
            number = 3;
            printf("3. (%d -> %d)\n", getppid(), getpid());
        }
    }

    time_t now = time(NULL);
    struct tm *tm_struct = localtime(&now);
    struct timeval t;
    gettimeofday(&t, NULL);
    printf("%d. time: %d:%d:%d:%ld\n", number, tm_struct->tm_hour, tm_struct->tm_min, tm_struct->tm_sec, t.tv_usec/1000);

    while(wait(NULL) > 0);
    printf("STP (%d -> %d)\n", getppid(), getpid());
    return 0;
}

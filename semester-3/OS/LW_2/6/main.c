#include <stdio.h>
#include <unistd.h>
#include <sys/wait.h>

int main() {
    // 1st
    printf("1. STR (%d -> %d)\n", getppid(), getpid());

    if (fork() == 0) {
        // 2nd
        printf("2. (%d -> %d)\n", getppid(), getpid());
        if (fork() == 0) {
            // 4th
            printf("4. (%d -> %d)\n", getppid(), getpid());

            if (fork() == 0) {
                // 5th
                printf("5. (%d -> %d)\n", getppid(), getpid());
            } else {
                if (fork() == 0) {
                    // 6th
                    printf("6. (%d -> %d)\n", getppid(), getpid());
                    execl("/bin/ls", "/bin/ls", "-l", NULL);
                } else {
                    if (fork() == 0) {
                        // 7th
                        printf("7. (%d -> %d)\n", getppid(), getpid());
                    }
                }
            }
        }
    } else {
        if (fork() == 0) {
            // 3rd
            printf("3. (%d -> %d)\n", getppid(), getpid());
        }
    }

    while(wait(NULL) > 0);
    printf("STP (%d -> %d)\n", getppid(), getpid());
    return 0;
}

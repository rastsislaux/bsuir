#!/usr/bin/env bash

printf "Example 1: set is parsed correctly with inner sets\n"
./main.py sets/A.txt
printf "\n"

printf "Example 2: set is parsed correctly with tuples containing sets\n"
./main.py sets/D.txt
printf "\n"

printf "Example 3: union works correctly with strings (A set contains one A, and C contains two As)\n"
./main.py sets/A.txt sets/C.txt
printf "\n"

printf "Example 4: union works correctly with tuples (A set contains one empty tuple, C contains three)\n"
./main.py sets/A.txt sets/B.txt
printf "\n"

printf "Example 5: parser detects missing braces\n"
./main.py sets/E.txt
./main.py sets/F.txt
./main.py sets/G.txt
printf "\n"

printf "Example 6: program does not lose control if the file's not found\n"
./main.py sets/A.txt sets/ABCDEFG.txt sets/B.txt
printf "\n"

printf "Example 7: program does not lose control if no args provided\n"
./main.py
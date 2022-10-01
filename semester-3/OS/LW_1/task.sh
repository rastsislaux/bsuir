#! /bin/bash

abs=$(realpath $1)
files=$(ls -lR $1 | grep '^-')
size=$(echo "$files" | awk '{size += $5} END { print size }')
sizeMB=$(echo "scale=2 ; $size / 1000000" | bc)
count=$(echo "$files" | wc -l)
echo "abs path: ${abs}, total size: ${sizeMB}MB, total count: ${count}"

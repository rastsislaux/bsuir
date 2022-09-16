#! /bin/bash

abs=$(realpath $1)
files=$(ls -lR $1 | grep '^-')
size=$(echo "$files" | awk '{size += $5} END { print size }')
count=$(echo "$files" | wc -l)
echo "abs path: ${abs}, total size: ${size}B, total count: ${count}"

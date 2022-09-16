#! /bin/bash

if gcc $1 -o $2; then
    echo "Compilation succesful"
    ./$2
else
    echo "Compilation error"
fi

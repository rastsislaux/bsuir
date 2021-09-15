#!/bin/bash
find $2 -maxdepth 1 -type f | xargs cat | tr -cd $1 | wc -m

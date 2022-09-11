#!/bin/bash
cat $2/* | tr -cd $1 | wc -m

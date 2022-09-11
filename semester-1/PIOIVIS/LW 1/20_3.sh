#!/bin/bash
grep -oF $1 $2/* | wc -l

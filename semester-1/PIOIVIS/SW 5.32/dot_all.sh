#! /bin/bash

for FILE in orig/*.dot; do
    dot -Tpng $FILE > $FILE.png
done

for FILE in results/*.dot; do
    dot -Tpng $FILE > $FILE.png
done

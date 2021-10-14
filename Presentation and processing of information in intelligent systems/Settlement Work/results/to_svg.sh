#! /bin/zsh
MY=0
for f in *.dot
do
	cat $f | dot -Tsvg > pic$MY.svg
	MY=$((MY+1))
done

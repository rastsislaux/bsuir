#! /bin/zsh
MY=0
for f in *.dot
do
	cat $f | dot -Tpng > pic$MY.png
	MY=$((MY+1))
done

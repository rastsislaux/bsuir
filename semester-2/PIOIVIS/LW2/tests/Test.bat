@echo off
@echo Test %1
@echo Test %1 >>%2.res
if exist input.txt del input.txt > nul
if exist output.txt del output.txt > nul
copy %1.in input.txt >nul
Timer %2.exe 1000 131072 >>%2.res
checker %1.in output.txt %1.out >>%2.res


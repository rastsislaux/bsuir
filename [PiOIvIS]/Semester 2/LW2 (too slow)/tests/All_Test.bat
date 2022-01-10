@echo off
if %1. == . goto noparm
if exist %1.res del %1.res
echo Task : Водородный поезд >>%1.res 
echo Program to test: %1 >>%1.res
echo ................ >>%1.res 
for %%i in (1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20) do call test.bat %%i %1
if exist input.txt del input.txt>Nul
if exist output.txt del output.txt>Nul
exit
:noparm
@echo Usage: All_Test <Filename without extension>


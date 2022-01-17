echo ''
echo 'Обновляемся...'
echo ''
git pull

echo ''
echo 'Создаём проект...'
echo ''
cmake .

echo ''
echo 'Собираем проект...'
echo ''
cmake --build .

echo ''
echo 'Тестируем...'
echo ''

cp ./Debug/LW2.exe ./tests
cd tests
./All_Tests.bat LW2

echo ''
echo 'Результаты:'
cat LW2.res

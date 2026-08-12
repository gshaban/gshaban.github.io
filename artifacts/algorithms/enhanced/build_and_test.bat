@echo off
setlocal

echo CS499 Enhanced eBid Binary Search Tree - Build and Test
where g++ >nul 2>nul
if errorlevel 1 (
  echo g++ was not found on PATH.
  echo Open the source in a C++ IDE or install MinGW/MSYS2 g++ and run:
  echo g++ -std=c++17 -Wall -Wextra -pedantic BinarySearchTree.cpp CSVparser.cpp -o ebid_bst.exe
  exit /b 1
)

echo Compiling...
g++ -std=c++17 -Wall -Wextra -pedantic BinarySearchTree.cpp CSVparser.cpp -o ebid_bst.exe
if errorlevel 1 exit /b 1

echo Running self-test...
ebid_bst.exe --self-test
if errorlevel 1 exit /b 1

echo.
echo To run the menu demo, use:
echo ebid_bst.exe eBid_Monthly_Sales_Dec_2016.csv 98223
endlocal

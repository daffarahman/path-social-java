@echo off
echo ========================================
echo    Path Clone MVP - Build Script
echo ========================================
echo.

echo Cleaning old class files...
if exist bin rmdir /s /q bin
mkdir bin

echo Compiling...
javac -d bin -sourcepath src src/madebydap/pathsocial/*.java src/madebydap/pathsocial/model/*.java src/madebydap/pathsocial/data/*.java src/madebydap/pathsocial/ui/*.java src/madebydap/pathsocial/ui/components/*.java src/madebydap/pathsocial/ui/style/*.java

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo    Compilation FAILED!
    echo ========================================
    pause
    exit /b %errorlevel%
)

echo.
echo ========================================
echo    Compilation successful!
echo    Launching Path...
echo ========================================
echo.

java -cp bin madebydap.pathsocial.App
pause

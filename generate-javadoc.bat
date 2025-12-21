@echo off
echo ========================================
echo    Path Social - JavaDoc Generator
echo ========================================
echo.

echo Cleaning old docs...
if exist docs rmdir /s /q docs

echo Generating JavaDoc...
javadoc -d docs -sourcepath src -subpackages madebydap -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 -author -version -classpath "lib\flatlaf-3.4.jar" -quiet

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================
    echo    JavaDoc generated successfully!
    echo    Open docs\index.html to view
    echo ========================================
) else (
    echo.
    echo ========================================
    echo    Error generating JavaDoc!
    echo ========================================
)

echo.
pause

@echo off
echo ========================================
echo    Path Social - JAR Build Script
echo ========================================
echo.

:: Set directories
set SRC_DIR=src
set BUILD_DIR=build
set LIB_DIR=lib
set JAR_NAME=PathSocial.jar
set MAIN_CLASS=madebydap.pathsocial.App

:: Clean and create build directory
echo Cleaning build directory...
if exist %BUILD_DIR% rmdir /s /q %BUILD_DIR%
mkdir %BUILD_DIR%
mkdir %BUILD_DIR%\classes

:: Compile all Java files
echo Compiling source files...
javac -d %BUILD_DIR%\classes -cp "%LIB_DIR%\*" -sourcepath %SRC_DIR% %SRC_DIR%\madebydap\pathsocial\*.java %SRC_DIR%\madebydap\pathsocial\model\*.java %SRC_DIR%\madebydap\pathsocial\data\*.java %SRC_DIR%\madebydap\pathsocial\ui\*.java %SRC_DIR%\madebydap\pathsocial\ui\components\*.java %SRC_DIR%\madebydap\pathsocial\ui\style\*.java

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo    Compilation FAILED!
    echo ========================================
    pause
    exit /b %errorlevel%
)

:: Extract FlatLaf classes into build directory (to bundle in JAR)
echo Extracting FlatLaf dependency...
cd %BUILD_DIR%\classes
jar -xf ..\..\%LIB_DIR%\flatlaf-3.4.jar
cd ..\..

:: Remove META-INF from extracted JAR (we'll create our own)
if exist %BUILD_DIR%\classes\META-INF rmdir /s /q %BUILD_DIR%\classes\META-INF

:: Create manifest file
echo Creating manifest...
mkdir %BUILD_DIR%\classes\META-INF
echo Manifest-Version: 1.0> %BUILD_DIR%\classes\META-INF\MANIFEST.MF
echo Main-Class: %MAIN_CLASS%>> %BUILD_DIR%\classes\META-INF\MANIFEST.MF
echo.>> %BUILD_DIR%\classes\META-INF\MANIFEST.MF

:: Create JAR file
echo Creating JAR file...
cd %BUILD_DIR%\classes
jar cfm ..\%JAR_NAME% META-INF\MANIFEST.MF *
cd ..\..

if %errorlevel% neq 0 (
    echo.
    echo ========================================
    echo    JAR creation FAILED!
    echo ========================================
    pause
    exit /b %errorlevel%
)

:: Clean up classes directory
echo Cleaning up...
rmdir /s /q %BUILD_DIR%\classes

echo.
echo ========================================
echo    Build successful!
echo    JAR file: %BUILD_DIR%\%JAR_NAME%
echo.
echo    Run with: java -jar %BUILD_DIR%\%JAR_NAME%
echo ========================================
echo.
pause

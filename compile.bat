@echo off
chcp 65001 >nul
echo ========================================
echo   校园二手交易平台 - 编译脚本
echo ========================================
echo.

cd src

echo 正在编译...
javac -encoding UTF-8 CampusMarketApp.java model/*.java service/*.java repository/*.java util/*.java

if %errorlevel% equ 0 (
    echo.
    echo 编译成功！
) else (
    echo.
    echo 编译失败，请检查错误信息
)

echo.
pause

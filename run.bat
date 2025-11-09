@echo off
chcp 65001 >nul
echo ========================================
echo   校园二手交易平台
echo ========================================
echo.

cd src
java -Dfile.encoding=UTF-8 CampusMarketApp

pause

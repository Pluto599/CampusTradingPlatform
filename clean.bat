@echo off
REM 删除常见编译产物目录
rd /s /q dist
rd /s /q build
rd /s /q out
rd /s /q target
rd /s /q node_modules

REM 删除 Python 编译产物
del /s /q *.pyc
for /d /r . %%d in (__pycache__) do @if exist "%%d" rd /s /q "%%d"

REM 删除 Java 编译产物
del /s /q *.class

echo 清理完成！

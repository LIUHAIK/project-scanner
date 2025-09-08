@echo off
chcp 65001 >nul
REM 项目扫描工具 - Windows批处理脚本
REM 用于执行项目扫描工具

echo 正在启动项目扫描工具...

REM 检查JAR文件是否存在
if not exist "project-scanner-1.0-SNAPSHOT.jar" (
    echo 错误：未找到 project-scanner-1.0-SNAPSHOT.jar 文件
    echo 请确保JAR文件与脚本在同一目录
    pause
    exit /b 1
)

REM 检查Java是否可用
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误：未找到Java运行时环境
    echo 请确保已安装Java 17或更高版本，并且已添加到系统PATH
    pause
    exit /b 1
)

REM 执行项目扫描
echo 开始扫描项目文件...
java -jar project-scanner-1.0-SNAPSHOT.jar

REM 检查执行结果
if errorlevel 1 (
    echo 扫描过程中出现错误
    pause
    exit /b 1
) else (
    echo 扫描完成！请查看生成的 project_files_yyyyMMdd_HHmmss.zip 文件
)

pause

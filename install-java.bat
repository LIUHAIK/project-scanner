@echo off
chcp 65001 >nul
echo 项目扫描工具 - Java自动安装脚本
echo.

REM 检查是否已经安装了Java
java -version >nul 2>&1
if not errorlevel 1 (
    echo Java已经安装，版本信息：
    java -version
    echo.
    echo 可以直接运行构建脚本了！
    pause
    exit /b 0
)

echo 正在检查Java安装...
echo.

REM 检查常见的Java安装路径
set "JAVA_PATHS="
set "JAVA_PATHS=%JAVA_PATHS%;C:\Program Files\Java\jdk-17\bin"
set "JAVA_PATHS=%JAVA_PATHS%;C:\Program Files\Java\jre-17\bin"
set "JAVA_PATHS=%JAVA_PATHS%;C:\Program Files (x86)\Java\jdk-17\bin"
set "JAVA_PATHS=%JAVA_PATHS%;C:\Program Files (x86)\Java\jre-17\bin"
set "JAVA_PATHS=%JAVA_PATHS%;C:\Program Files\Eclipse Adoptium\jdk-17.0.9.9-hotspot\bin"
set "JAVA_PATHS=%JAVA_PATHS%;C:\Program Files\Eclipse Adoptium\jre-17.0.9.9-hotspot\bin"

for %%p in (%JAVA_PATHS%) do (
    if exist "%%p\java.exe" (
        echo 找到Java安装: %%p
        set "JAVA_HOME=%%p\.."
        set "PATH=%%p;%PATH%"
        goto :found_java
    )
)

echo 未找到Java安装，正在下载Java 17...
echo.

REM 创建下载目录
if not exist "tools" mkdir "tools"

REM 下载Java 17
echo 正在下载Eclipse Temurin Java 17...
powershell -Command "& {Invoke-WebRequest -Uri 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.9%%2B9/OpenJDK17U-jdk_x64_windows_hotspot_17.0.9_9.msi' -OutFile 'tools\java17-installer.msi'}"

if not exist "tools\java17-installer.msi" (
    echo 下载失败，请手动下载Java 17
    echo 下载地址: https://adoptium.net/temurin/releases/?version=17
    echo.
    echo 或者使用以下命令手动安装：
    echo 1. 访问 https://adoptium.net/
    echo 2. 下载Java 17 (Windows x64)
    echo 3. 运行安装程序
    echo 4. 重新运行此脚本
    pause
    exit /b 1
)

echo 正在安装Java 17...
echo 请在弹出的安装窗口中完成安装，然后按任意键继续...
tools\java17-installer.msi

echo.
echo 安装完成后，正在检查Java...
timeout /t 5 /nobreak >nul

REM 重新检查Java安装
for %%p in (%JAVA_PATHS%) do (
    if exist "%%p\java.exe" (
        echo 找到Java安装: %%p
        set "JAVA_HOME=%%p\.."
        set "PATH=%%p;%PATH%"
        goto :found_java
    )
)

echo.
echo Java安装可能未完成，请手动检查：
echo 1. 确保Java 17已正确安装
echo 2. 确保Java已添加到系统PATH环境变量
echo 3. 重新运行此脚本
pause
exit /b 1

:found_java
echo.
echo Java安装成功！
echo 版本信息：
java -version
echo.
echo 现在可以运行构建脚本了：
echo - build.bat (构建JAR文件)
echo - build-exe-simple.bat (构建EXE文件，推荐)
echo - build-exe.bat (构建EXE文件，需要Launch4j)
echo.
pause


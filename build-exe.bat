@echo off
chcp 65001 >nul
echo 项目扫描工具 - EXE构建脚本
echo.

REM 检查Java是否安装
java -version >nul 2>&1
if errorlevel 1 (
    echo 错误: 未找到Java运行时环境
    echo 请确保已安装Java 17或更高版本
    pause
    exit /b 1
)

echo 正在创建目录结构...
if not exist "target\classes" mkdir "target\classes"
if not exist "target\lib" mkdir "target\lib"
if not exist "tools" mkdir "tools"

echo 正在编译Java源文件...
javac -d target\classes -cp "src\main\java" src\main\java\org\example\Main.java

if errorlevel 1 (
    echo 编译失败！
    pause
    exit /b 1
)

echo 正在创建JAR文件...
cd target\classes
jar cfm ..\project-scanner-1.0-SNAPSHOT.jar ..\..\MANIFEST.MF org\example\*.class
cd ..\..

echo 正在下载Launch4j...
if not exist "tools\launch4j" (
    echo 下载Launch4j工具...
    powershell -Command "& {Invoke-WebRequest -Uri 'https://sourceforge.net/projects/launch4j/files/launch4j-3/3.50/launch4j-3.50-win32.exe/download' -OutFile 'tools\launch4j-installer.exe'}"
    
    if exist "tools\launch4j-installer.exe" (
        echo 正在安装Launch4j...
        tools\launch4j-installer.exe /S
        timeout /t 10 /nobreak >nul
        
        REM 尝试找到Launch4j安装路径
        if exist "C:\Program Files (x86)\Launch4j\launch4jc.exe" (
            copy "C:\Program Files (x86)\Launch4j\launch4jc.exe" "tools\launch4jc.exe"
        ) else if exist "C:\Program Files\Launch4j\launch4jc.exe" (
            copy "C:\Program Files\Launch4j\launch4jc.exe" "tools\launch4jc.exe"
        ) else (
            echo 警告: 无法找到Launch4j，请手动安装
            echo 下载地址: https://launch4j.sourceforge.net/
            echo 安装后请将launch4jc.exe复制到tools目录
            pause
        )
    ) else (
        echo 下载Launch4j失败，请手动下载并安装
        echo 下载地址: https://launch4j.sourceforge.net/
        pause
        exit /b 1
    )
)

echo 正在创建EXE文件...
if exist "tools\launch4jc.exe" (
    tools\launch4jc.exe launch4j-config.xml
    if errorlevel 1 (
        echo EXE创建失败！
        pause
        exit /b 1
    )
    echo EXE文件创建成功！
) else (
    echo 错误: 未找到launch4jc.exe
    echo 请确保Launch4j已正确安装
    pause
    exit /b 1
)

echo 正在复制脚本文件...
copy "src\main\resources\project-scanner.bat" "target\"
copy "src\main\resources\project-scanner-utf8.bat" "target\"
copy "src\main\resources\project-scanner.sh" "target\"

echo 正在创建ZIP文件...
powershell -command "Compress-Archive -Path 'target\project-scanner.exe', 'target\project-scanner-1.0-SNAPSHOT.jar', 'target\project-scanner.bat', 'target\project-scanner-utf8.bat', 'target\project-scanner.sh' -DestinationPath 'target\project-scanner-1.0-SNAPSHOT-bin.zip' -Force"

echo.
echo 构建完成！
echo 生成的文件在 target 目录中：
echo - project-scanner.exe (可执行文件)
echo - project-scanner-1.0-SNAPSHOT.jar (JAR文件)
echo - project-scanner-1.0-SNAPSHOT-bin.zip (完整包)
echo.
pause

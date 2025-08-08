@echo off
chcp 65001 >nul
echo 项目构建脚本
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

echo 正在复制脚本文件...
copy "src\main\resources\project-scanner.bat" "target\"
copy "src\main\resources\project-scanner-utf8.bat" "target\"
copy "src\main\resources\project-scanner.sh" "target\"

echo 正在创建ZIP文件...
powershell -command "Compress-Archive -Path 'target\project-scanner-1.0-SNAPSHOT.jar', 'target\project-scanner.bat', 'target\project-scanner-utf8.bat', 'target\project-scanner.sh' -DestinationPath 'target\project-scanner-1.0-SNAPSHOT-bin.zip' -Force"

echo.
echo 构建完成！
echo 生成的文件在 target 目录中：
echo - project-scanner-1.0-SNAPSHOT.jar
echo - project-scanner-1.0-SNAPSHOT-bin.zip
echo.
pause

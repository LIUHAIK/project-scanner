#!/bin/bash

echo "项目构建脚本"
echo

# 检查Java是否安装
if ! command -v java &> /dev/null; then
    echo "错误: 未找到Java运行时环境"
    echo "请确保已安装Java 17或更高版本"
    exit 1
fi

echo "正在创建目录结构..."
mkdir -p target/classes
mkdir -p target/lib

echo "正在编译Java源文件..."
javac -d target/classes -cp "src/main/java" src/main/java/org/example/Main.java

if [ $? -ne 0 ]; then
    echo "编译失败！"
    exit 1
fi

echo "正在创建JAR文件..."
cd target/classes
jar cfm ../project-scanner-1.0-SNAPSHOT.jar ../../MANIFEST.MF org/example/*.class
cd ../..

echo "正在复制脚本文件..."
cp src/main/resources/project-scanner.bat target/
cp src/main/resources/project-scanner.sh target/
chmod +x target/project-scanner.sh

echo "正在创建ZIP文件..."
cd target
zip -r project-scanner-1.0-SNAPSHOT-bin.zip project-scanner-1.0-SNAPSHOT.jar project-scanner.bat project-scanner.sh
cd ..

echo
echo "构建完成！"
echo "生成的文件在 target 目录中："
echo "- project-scanner-1.0-SNAPSHOT.jar"
echo "- project-scanner-1.0-SNAPSHOT-bin.zip"
echo

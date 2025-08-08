#!/bin/bash

# 项目扫描工具 - Unix Shell脚本
# 用于执行项目扫描工具

echo "正在启动项目扫描工具..."

# 检查JAR文件是否存在
if [ ! -f "project-scanner-1.0-SNAPSHOT.jar" ]; then
    echo "错误：未找到 project-scanner-1.0-SNAPSHOT.jar 文件"
    echo "请确保JAR文件与脚本在同一目录"
    exit 1
fi

# 检查Java是否可用
if ! command -v java &> /dev/null; then
    echo "错误：未找到Java运行时环境"
    echo "请确保已安装Java 17或更高版本，并且已添加到系统PATH"
    exit 1
fi

# 检查Java版本
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "错误：Java版本过低，需要Java 17或更高版本"
    echo "当前版本：$(java -version 2>&1 | head -n 1)"
    exit 1
fi

# 执行项目扫描
echo "开始扫描项目文件..."
java -jar project-scanner-1.0-SNAPSHOT.jar

# 检查执行结果
if [ $? -eq 0 ]; then
    echo "扫描完成！请查看生成的 project_files_yyyyMMdd_HHmmss.zip 文件"
else
    echo "扫描过程中出现错误"
    exit 1
fi

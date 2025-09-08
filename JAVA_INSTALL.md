# Java安装指南

本文档说明如何安装Java 17，以便运行项目扫描工具。

## 自动安装（推荐）

运行自动安装脚本：
```cmd
install-java.bat
```

此脚本会：
1. 检查是否已安装Java
2. 自动下载Java 17安装程序
3. 引导您完成安装过程
4. 验证安装是否成功

## 手动安装

### 方法一：使用Eclipse Temurin（推荐）

1. 访问 https://adoptium.net/
2. 选择Java 17版本
3. 下载Windows x64 MSI安装包
4. 运行安装程序，按默认设置安装
5. 重启命令提示符或PowerShell

### 方法二：使用Oracle JDK

1. 访问 https://www.oracle.com/java/technologies/downloads/
2. 下载Java 17 (Windows x64)
3. 运行安装程序
4. 重启命令提示符或PowerShell

### 方法三：使用OpenJDK

1. 访问 https://jdk.java.net/17/
2. 下载Windows x64版本
3. 解压到 `C:\Program Files\Java\jdk-17`
4. 手动添加到PATH环境变量

## 验证安装

安装完成后，在命令提示符中运行：
```cmd
java -version
```

应该看到类似输出：
```
openjdk version "17.0.9" 2023-10-17
OpenJDK Runtime Environment Temurin-17.0.9+9 (build 17.0.9+9)
OpenJDK 64-Bit Server VM Temurin-17.0.9+9 (build 17.0.9+9, mixed mode, sharing)
```

## 环境变量设置

如果Java已安装但命令不可用，需要手动设置环境变量：

### 设置JAVA_HOME
1. 右键"此电脑" → 属性 → 高级系统设置 → 环境变量
2. 在"系统变量"中新建 `JAVA_HOME`
3. 值设为Java安装路径，如：`C:\Program Files\Eclipse Adoptium\jdk-17.0.9.9-hotspot`

### 添加到PATH
1. 在"系统变量"中找到 `Path`
2. 点击"编辑" → "新建"
3. 添加 `%JAVA_HOME%\bin`
4. 点击"确定"保存

## 常见问题

### 1. "java不是内部或外部命令"
- 确保Java已正确安装
- 检查PATH环境变量是否包含Java路径
- 重启命令提示符

### 2. "版本过低"
- 确保安装的是Java 17或更高版本
- 检查JAVA_HOME指向正确的版本

### 3. "权限不足"
- 以管理员身份运行安装程序
- 确保有足够的磁盘空间

### 4. "下载失败"
- 检查网络连接
- 尝试手动下载安装包
- 使用VPN或代理

## 安装完成后

Java安装成功后，您可以运行以下构建脚本：

```cmd
# 构建JAR文件
build.bat

# 构建EXE文件（推荐）
build-exe-simple.bat

# 构建EXE文件（需要Launch4j）
build-exe.bat
```

## 系统要求

- Windows 10/11 (64位)
- 至少2GB可用磁盘空间
- 管理员权限（安装时）
- 网络连接（下载时）


# 项目扫描工具

这是一个用于扫描项目中特定文件并生成压缩包的工具。

## 功能特性

- 扫描项目中的以下文件（不区分大小写）：
  - `build.gradle`
  - `settings.gradle`
  - `cmakelists.txt`
  - `package.json`
  - `pubspec.yaml`
- 记录项目中所有 `.so` 文件的文件名
- 将扫描到的文件复制到临时文件夹
- 生成包含所有文件的 ZIP 压缩包
- 支持 Windows 和 Mac/Linux 系统

## 构建项目

### 前提条件

- Java 17 或更高版本

### 构建步骤

#### 方法一：使用Maven（推荐）

如果您的系统已安装Maven 3.6或更高版本：

1. 克隆或下载项目到本地
2. 在项目根目录执行以下命令：

```bash
mvn clean package
```

#### 方法二：手动构建

如果您的系统没有安装Maven，可以使用提供的构建脚本：

**Windows:**
```cmd
build.bat
```

**Mac/Linux:**
```bash
chmod +x build.sh
./build.sh
```

构建完成后，会在 `target` 目录下生成以下文件：
- `project-scanner-1.0-SNAPSHOT.jar` - 可执行JAR文件
- `project-scanner-1.0-SNAPSHOT-bin.zip` - 包含可执行脚本的压缩包

## 使用方法

### 方法一：使用可执行脚本（推荐）

1. 解压 `project-scanner-1.0-SNAPSHOT-bin.zip` 文件
2. 将解压后的文件复制到要扫描的项目根目录
3. 根据操作系统执行相应的脚本：

**Windows:**
```cmd
project-scanner.bat
```

**Windows（解决乱码问题）:**
```cmd
project-scanner-utf8.bat
```

**Mac/Linux:**
```bash
chmod +x project-scanner.sh
./project-scanner.sh
```

### 方法二：直接使用JAR文件

1. 将 `project-scanner-1.0-SNAPSHOT.jar` 复制到要扫描的项目根目录
2. 执行以下命令：

```bash
java -jar project-scanner-1.0-SNAPSHOT.jar
```

## 输出结果

执行完成后，会在项目根目录生成 `project_files_yyyyMMdd_HHmmss.zip` 文件（包含时间戳），包含：

- 扫描到的所有目标文件（build.gradle, settings.gradle, cmakelists.txt, package.json, pubspec.yaml）
- `so_files.txt` 文件，记录项目中所有 .so 文件的文件名

## 注意事项

- 工具会自动扫描当前工作目录下的所有子目录
- 文件名匹配不区分大小写
- 对于同名文件，会保留所有文件并使用数字编号区分（如 build_2.gradle）
- 扫描完成后会自动清理临时文件夹
- 确保目标项目有足够的磁盘空间用于创建临时文件和压缩包

## 系统要求

- Java 17 或更高版本
- Windows 10/11 或 macOS 10.14+ 或 Linux
- 至少 100MB 可用磁盘空间

## 故障排除

### 常见问题

1. **"未找到Java运行时环境"**
   - 确保已安装Java 17或更高版本
   - 确保Java已添加到系统PATH环境变量

2. **"未找到JAR文件"**
   - 确保JAR文件与脚本在同一目录
   - 检查文件名是否正确

3. **权限错误（Mac/Linux）**
   - 确保shell脚本有执行权限：`chmod +x project-scanner.sh`

4. **Windows终端乱码**
   - 使用 `project-scanner-utf8.bat` 脚本
   - 或者手动设置终端代码页：`chcp 65001`

5. **磁盘空间不足**
   - 确保目标目录有足够的可用空间

## 开发信息

- 开发语言：Java 17
- 构建工具：Maven
- 主要依赖：Java标准库（无外部依赖）

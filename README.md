# 项目扫描工具

这是一个用于扫描项目中特定文件、解析依赖信息并生成详细分析报告的工具。

## 功能特性

### 📁 文件扫描
- 扫描项目中的以下文件（不区分大小写）：
  - `build.gradle` - Android/Java项目配置
  - `settings.gradle` - Gradle项目设置
  - `cmakelists.txt` - C/C++项目配置
  - `package.json` - React Native/Node.js项目配置
  - `pubspec.yaml` - Flutter项目配置
- 记录项目中所有 `.so` 文件的文件名
- 将扫描到的文件复制到临时文件夹
- 生成包含所有文件的 ZIP 压缩包

### 🔍 依赖分析
- **React Native依赖**: 解析`package.json`中的`dependencies`、`devDependencies`、`peerDependencies`
- **Flutter依赖**: 解析`pubspec.yaml`中的`dependencies`、`dev_dependencies`
- **原生依赖**: 解析`build.gradle`中的`implementation`、`compile`、`api`、`compileOnly`、`runtimeOnly`
- **C/C++依赖**: 解析`CMakeLists.txt`中的`find_package`、`target_link_libraries`
- **Gradle模块**: 解析`settings.gradle`中的`include`模块

### 📊 Excel报告生成
- 生成详细的依赖分析Excel报告（`.xlsx`格式）
- 包含依赖名称、版本号、依赖类型、文件路径、文件名
- 按依赖类型进行颜色编码区分
- 提供统计信息工作表，显示各类型依赖数量
- 支持 Windows 和 Mac/Linux 系统

## 快速开始

### 1. 下载并运行
```bash
# 下载最新的发布版本
wget https://github.com/LIUHAIK/project-scanner/releases/latest/download/project-scanner-1.0-SNAPSHOT.jar

# 在您的项目根目录运行
java -jar project-scanner-1.0-SNAPSHOT.jar
```

### 2. 查看结果
运行完成后，您将获得：
- `dependency_analysis_report.xlsx` - 详细的依赖分析报告
- `project_files_yyyyMMdd_HHmmss.zip` - 项目文件压缩包

### 3. 分析报告
打开Excel文件查看：
- 所有依赖的详细信息
- 按类型分组的统计信息
- 颜色编码的依赖分类

## 构建项目

### 前提条件

- Java 17 或更高版本

**如果未安装Java，请先运行：**
```cmd
install-java.bat
```

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

#### 方法三：构建EXE文件

**使用jpackage（推荐，需要Java 14+）：**
```cmd
build-exe-simple.bat
```

**使用Launch4j（需要额外安装）：**
```cmd
build-exe.bat
```

构建完成后，会在 `target` 目录下生成以下文件：
- `project-scanner-1.0-SNAPSHOT.jar` - 可执行JAR文件
- `project-scanner-1.0-SNAPSHOT-bin.zip` - 包含可执行脚本的压缩包

**EXE构建完成后，还会生成：**
- `项目扫描工具.exe` 或 `project-scanner.exe` - Windows可执行文件

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

### 方法三：使用EXE文件（Windows）

1. 将生成的exe文件复制到要扫描的项目根目录
2. 双击exe文件即可运行，或在命令行中执行：

```cmd
项目扫描工具.exe
```

## 输出结果

执行完成后，会在项目根目录生成以下文件：

### 📦 压缩包文件
- `project_files_yyyyMMdd_HHmmss.zip` 文件（包含时间戳），包含：
  - 扫描到的所有目标文件（build.gradle, settings.gradle, cmakelists.txt, package.json, pubspec.yaml）
  - `so_files.txt` 文件，记录项目中所有 .so 文件的文件名

### 📊 Excel分析报告
- `dependency_analysis_report.xlsx` - 依赖分析Excel报告，包含：
  - **主工作表**: 详细的依赖信息表格
    - 依赖名称
    - 版本号
    - 依赖类型（RN/Flutter/Native/C/C++）
    - 文件路径
    - 文件名
  - **统计工作表**: 依赖统计信息
    - 按类型统计依赖数量
    - 按文件统计依赖数量

### 🎨 报告特色
- **颜色编码**: 不同类型依赖使用不同颜色区分
  - 🟢 RN依赖 - 浅绿色
  - 🟡 Flutter依赖 - 浅黄色  
  - 🟠 原生依赖 - 浅橙色
  - 🔵 C/C++依赖 - 浅青色
- **自动列宽**: 根据内容自动调整列宽
- **边框样式**: 清晰的表格边框和标题样式

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

6. **Excel文件无法打开**
   - 确保已安装Microsoft Excel或兼容的办公软件
   - 检查文件是否被其他程序占用

7. **依赖解析不完整**
   - 检查项目文件格式是否正确
   - 某些复杂的依赖配置可能需要手动检查

## 使用示例

### 示例输出
```
正在扫描项目根目录: /path/to/your/project
复制文件: /path/to/your/project/package.json -> package.json
复制文件: /path/to/your/project/pubspec.yaml -> pubspec.yaml
解析package.json: /path/to/your/project/package.json - 找到 15 个依赖
解析pubspec.yaml: /path/to/your/project/pubspec.yaml - 找到 8 个依赖
解析build.gradle: /path/to/your/project/build.gradle - 找到 12 个依赖
解析CMakeLists.txt: /path/to/your/project/CMakeLists.txt - 找到 3 个依赖
Excel报告已生成: dependency_analysis_report.xlsx
依赖分析报告已生成: dependency_analysis_report.xlsx
共解析到 38 个依赖
扫描完成！
找到的目标文件: 4 个
找到的.so文件: 2 个
压缩文件已创建: project_files_20250908_171349.zip
```

### 支持的依赖类型示例

#### React Native (package.json)
```json
{
  "dependencies": {
    "react": "^18.2.0",
    "react-native": "0.72.6"
  },
  "devDependencies": {
    "@types/react": "^18.2.0"
  }
}
```

#### Flutter (pubspec.yaml)
```yaml
dependencies:
  http: ^0.13.5
  provider: ^6.0.5
```

#### Android/Java (build.gradle)
```gradle
dependencies {
    implementation 'com.google.guava:guava:31.1-jre'
    testImplementation 'org.junit.jupiter:junit-jupiter:5.8.1'
}
```

#### C/C++ (CMakeLists.txt)
```cmake
find_package(Boost REQUIRED)
target_link_libraries(myapp PRIVATE Boost::boost)
```

## 开发信息

- 开发语言：Java 17
- 构建工具：Maven
- 主要依赖：
  - Java标准库
  - Apache POI 5.2.4 (Excel文件生成)
  - Apache Commons (POI依赖)

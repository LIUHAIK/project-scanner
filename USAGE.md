# 项目扫描工具使用说明

## 快速开始

### 1. 构建工具

首先需要构建项目扫描工具：

**Windows:**
```cmd
build.bat
```

**Mac/Linux:**
```bash
chmod +x build.sh
./build.sh
```

### 2. 使用工具

构建完成后，将生成的文件复制到要扫描的项目根目录：

1. 解压 `target/project-scanner-1.0-SNAPSHOT-bin.zip`
2. 将解压出的文件复制到目标项目根目录
3. 执行相应的脚本：

**Windows:**
```cmd
project-scanner.bat
```

**Mac/Linux:**
```bash
chmod +x project-scanner.sh
./project-scanner.sh
```

## 功能说明

### 扫描的文件类型

工具会自动扫描以下文件（不区分大小写）：

- `build.gradle` - Gradle构建文件
- `settings.gradle` - Gradle设置文件
- `cmakelists.txt` - CMake构建文件
- `package.json` - Node.js包管理文件
- `pubspec.yaml` - Flutter/Dart包管理文件

### 扫描的目录

工具会递归扫描项目根目录下的所有子目录，包括：
- 源代码目录
- 资源目录
- 构建目录
- 依赖目录

### 输出结果

执行完成后，会在项目根目录生成 `project_files_yyyyMMdd_HHmmss.zip` 文件（包含时间戳），包含：

1. **复制的目标文件**：所有找到的 build.gradle、settings.gradle、cmakelists.txt、package.json、pubspec.yaml 文件
2. **so_files.txt**：记录项目中所有 .so 文件的文件名列表

## 示例输出

```
正在扫描项目根目录: /path/to/your/project
复制文件: /path/to/your/project/build.gradle -> build.gradle
复制文件: /path/to/your/project/app/build.gradle -> build_2.gradle
复制文件: /path/to/your/project/module/build.gradle -> build_3.gradle
复制文件: /path/to/your/project/package.json -> package.json
复制文件: /path/to/your/project/CMakeLists.txt -> CMakeLists.txt
已创建.so文件列表: /path/to/your/project/project_files/so_files.txt
ZIP文件已创建: /path/to/your/project/project_files_20241201_143022.zip
扫描完成！
找到的目标文件: 5 个
找到的.so文件: 2 个
压缩文件已创建: project_files_20241201_143022.zip
```

## 高级用法

### 直接使用JAR文件

如果不想使用脚本，也可以直接运行JAR文件：

```bash
java -jar project-scanner-1.0-SNAPSHOT.jar
```

### 自定义扫描

如果需要修改扫描的文件类型，可以编辑 `src/main/java/org/example/Main.java` 文件中的 `TARGET_FILES` 常量：

```java
private static final List<String> TARGET_FILES = Arrays.asList(
    "build.gradle", "settings.gradle", "cmakelists.txt", 
    "package.json", "pubspec.yaml", "your-custom-file.txt"
);
```

## 故障排除

### 常见错误

1. **Java未安装**
   ```
   错误: 未找到Java运行时环境
   ```
   解决方案：安装Java 17或更高版本

2. **权限不足**
   ```
   权限被拒绝
   ```
   解决方案：确保有足够的文件读写权限

3. **磁盘空间不足**
   ```
   java.io.IOException: 磁盘空间不足
   ```
   解决方案：清理磁盘空间，确保至少有100MB可用空间

4. **文件被占用**
   ```
   java.nio.file.FileSystemException: 文件正在被另一个进程使用
   ```
   解决方案：关闭可能占用文件的程序，重新运行

### 性能优化

- 对于大型项目，扫描可能需要较长时间
- 工具会自动跳过隐藏目录（以.开头的目录）
- 如果项目包含大量文件，建议在非高峰期运行

## 注意事项

1. **文件保留**：对于同名文件，会保留所有文件并使用数字编号区分（如 build_2.gradle）
2. **临时文件**：扫描过程中会创建临时文件夹，完成后会自动清理
3. **大小写敏感**：文件名匹配不区分大小写
4. **递归扫描**：会扫描所有子目录，包括构建目录
5. **网络依赖**：工具不需要网络连接，完全本地运行

## 技术支持

如果遇到问题，请检查：

1. Java版本是否符合要求（Java 17+）
2. 目标目录是否有足够的权限和空间
3. 是否有其他程序占用相关文件
4. 查看控制台输出的详细错误信息

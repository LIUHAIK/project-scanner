# EXE文件构建说明

本文档说明如何将项目扫描工具打包成Windows可执行文件（.exe）。

## 构建方法

### 方法一：使用jpackage（推荐）

**优点：**
- 使用Java内置工具，无需额外安装
- 生成的exe文件更小
- 支持现代Windows系统

**要求：**
- Java 14或更高版本
- Windows 10/11

**步骤：**
1. 确保已安装Java 14+
2. 运行构建脚本：
   ```cmd
   build-exe-simple.bat
   ```
3. 生成的exe文件位于 `target/项目扫描工具.exe`

### 方法二：使用Launch4j

**优点：**
- 兼容性更好
- 支持更多配置选项
- 可以自定义图标和版本信息

**要求：**
- Java 17或更高版本
- Launch4j工具

**步骤：**
1. 运行构建脚本：
   ```cmd
   build-exe.bat
   ```
2. 脚本会自动下载并安装Launch4j
3. 生成的exe文件位于 `target/project-scanner.exe`

## 手动安装Launch4j

如果自动安装失败，可以手动安装：

1. 访问 https://launch4j.sourceforge.net/
2. 下载最新版本的Launch4j
3. 安装到默认位置
4. 将 `launch4jc.exe` 复制到项目的 `tools` 目录

## 配置文件说明

### launch4j-config.xml

这是Launch4j的配置文件，包含以下主要设置：

- **JAR文件路径**：`target/project-scanner-1.0-SNAPSHOT.jar`
- **输出文件**：`target/project-scanner.exe`
- **Java版本要求**：最低Java 17
- **程序信息**：名称、版本、版权等

### 自定义配置

可以修改 `launch4j-config.xml` 来自定义exe文件：

```xml
<!-- 添加图标 -->
<icon>path/to/icon.ico</icon>

<!-- 修改程序名称 -->
<productName>自定义程序名</productName>

<!-- 修改版本信息 -->
<fileVersion>2.0.0.0</fileVersion>
```

## 使用EXE文件

### 直接运行
双击exe文件即可运行程序。

### 命令行运行
```cmd
项目扫描工具.exe
```

### 注意事项
1. exe文件需要Java运行时环境（JRE 17+）
2. 如果系统没有安装Java，程序会提示下载
3. exe文件比JAR文件大，但使用更方便

## 故障排除

### 常见问题

1. **"jpackage不是内部或外部命令"**
   - 确保安装了Java 14或更高版本
   - 检查JAVA_HOME环境变量

2. **"Launch4j安装失败"**
   - 手动下载并安装Launch4j
   - 确保有管理员权限

3. **"EXE文件无法运行"**
   - 检查是否安装了Java 17+
   - 尝试以管理员身份运行

4. **"生成的EXE文件过大"**
   - 这是正常现象，exe文件包含了Java运行时
   - 可以使用jpackage生成更小的文件

## 性能对比

| 文件类型 | 大小 | 启动速度 | 依赖要求 |
|---------|------|----------|----------|
| JAR文件 | ~10KB | 快 | 需要Java |
| EXE文件 | ~50MB | 中等 | 需要Java |
| 自包含EXE | ~100MB | 慢 | 无需Java |

## 推荐方案

- **开发测试**：使用JAR文件
- **用户分发**：使用jpackage生成的EXE文件
- **离线环境**：使用Launch4j生成的自包含EXE文件

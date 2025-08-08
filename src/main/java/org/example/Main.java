package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final List<String> TARGET_FILES = Arrays.asList(
        "build.gradle", "settings.gradle", "cmakelists.txt", 
        "package.json", "pubspec.yaml"
    );
    
    private static final String SO_FILES_TXT = "so_files.txt";
    private static final String OUTPUT_FOLDER = "project_files";
    
    public static void main(String[] args) {
        try {
            // 设置控制台编码为UTF-8
            System.setProperty("file.encoding", "UTF-8");
            System.setProperty("console.encoding", "UTF-8");
            
            // 获取当前工作目录
            String projectRoot = System.getProperty("user.dir");
            System.out.println("正在扫描项目根目录: " + projectRoot);
            
            // 创建输出文件夹
            Path outputPath = Paths.get(projectRoot, OUTPUT_FOLDER);
            if (Files.exists(outputPath)) {
                deleteDirectory(outputPath);
            }
            Files.createDirectories(outputPath);
            
            // 扫描并复制目标文件
            List<String> foundFiles = new ArrayList<>();
            scanAndCopyFiles(projectRoot, outputPath, foundFiles);
            
            // 扫描.so文件并记录
            List<String> soFiles = scanSoFiles(projectRoot);
            writeSoFilesList(outputPath, soFiles);
            
            // 创建ZIP文件（带时间戳）
            String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String zipFileName = "project_files_" + timestamp + ".zip";
            createZipFile(outputPath, Paths.get(projectRoot, zipFileName));
            
            // 清理临时文件夹
            deleteDirectory(outputPath);
            
            System.out.println("扫描完成！");
            System.out.println("找到的目标文件: " + foundFiles.size() + " 个");
            System.out.println("找到的.so文件: " + soFiles.size() + " 个");
            System.out.println("压缩文件已创建: " + zipFileName);
            
        } catch (Exception e) {
            System.err.println("扫描过程中发生错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void scanAndCopyFiles(String rootDir, Path outputPath, List<String> foundFiles) throws IOException {
        Path rootPath = Paths.get(rootDir);
        Map<String, Integer> fileNameCounts = new HashMap<>();
        
        Files.walk(rootPath)
            .filter(Files::isRegularFile)
            .forEach(filePath -> {
                String fileName = filePath.getFileName().toString().toLowerCase();
                if (TARGET_FILES.contains(fileName)) {
                    try {
                        // 生成唯一的文件名
                        String uniqueFileName = fileName;
                        if (fileNameCounts.containsKey(fileName)) {
                            fileNameCounts.put(fileName, fileNameCounts.get(fileName) + 1);
                            // 在扩展名前面插入数字编号
                            int lastDotIndex = fileName.lastIndexOf('.');
                            if (lastDotIndex > 0) {
                                String nameWithoutExt = fileName.substring(0, lastDotIndex);
                                String extension = fileName.substring(lastDotIndex);
                                uniqueFileName = nameWithoutExt + "_" + fileNameCounts.get(fileName) + extension;
                            } else {
                                uniqueFileName = fileName + "_" + fileNameCounts.get(fileName);
                            }
                        } else {
                            fileNameCounts.put(fileName, 1);
                        }
                        
                        Path targetPath = outputPath.resolve(uniqueFileName);
                        
                        Files.copy(filePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
                        foundFiles.add(filePath.toString());
                        System.out.println("复制文件: " + filePath + " -> " + uniqueFileName);
                    } catch (IOException e) {
                        System.err.println("复制文件失败: " + filePath + " - " + e.getMessage());
                    }
                }
            });
    }
    
    private static List<String> scanSoFiles(String rootDir) throws IOException {
        List<String> soFiles = new ArrayList<>();
        Files.walk(Paths.get(rootDir))
            .filter(Files::isRegularFile)
            .forEach(filePath -> {
                String fileName = filePath.getFileName().toString().toLowerCase();
                if (fileName.endsWith(".so")) {
                    soFiles.add(filePath.getFileName().toString());
                }
            });
        return soFiles;
    }
    
    private static void writeSoFilesList(Path outputPath, List<String> soFiles) throws IOException {
        Path soFilesPath = outputPath.resolve(SO_FILES_TXT);
        try (BufferedWriter writer = Files.newBufferedWriter(soFilesPath)) {
            for (String soFile : soFiles) {
                writer.write(soFile);
                writer.newLine();
            }
        }
        System.out.println("已创建.so文件列表: " + soFilesPath);
    }
    
    private static void createZipFile(Path sourcePath, Path zipPath) throws IOException {
        try (ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(zipPath))) {
            Files.walk(sourcePath)
                .filter(Files::isRegularFile)
                .forEach(filePath -> {
                    try {
                        String entryName = sourcePath.relativize(filePath).toString();
                        ZipEntry zipEntry = new ZipEntry(entryName);
                        zos.putNextEntry(zipEntry);
                        Files.copy(filePath, zos);
                        zos.closeEntry();
                    } catch (IOException e) {
                        System.err.println("添加文件到ZIP失败: " + filePath + " - " + e.getMessage());
                    }
                });
        }
        System.out.println("ZIP文件已创建: " + zipPath);
    }
    
    private static void deleteDirectory(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach(filePath -> {
                    try {
                        Files.delete(filePath);
                    } catch (IOException e) {
                        System.err.println("删除文件失败: " + filePath + " - " + e.getMessage());
                    }
                });
        }
    }
}
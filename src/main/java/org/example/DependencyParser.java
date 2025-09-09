package org.example;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

/**
 * 依赖解析器，用于解析不同文件格式中的依赖信息
 */
public class DependencyParser {
    
    /**
     * 解析package.json文件中的依赖
     */
    public static List<DependencyInfo> parsePackageJson(String filePath) {
        List<DependencyInfo> dependencies = new ArrayList<>();
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
            
            // 解析dependencies
            parseJsonDependencies(content, "dependencies", dependencies, filePath, "RN");
            // 解析devDependencies
            parseJsonDependencies(content, "devDependencies", dependencies, filePath, "RN");
            // 解析peerDependencies
            parseJsonDependencies(content, "peerDependencies", dependencies, filePath, "RN");
            
        } catch (IOException e) {
            System.err.println("解析package.json失败: " + filePath + " - " + e.getMessage());
        }
        return dependencies;
    }
    
    /**
     * 解析pubspec.yaml文件中的依赖
     */
    public static List<DependencyInfo> parsePubspecYaml(String filePath) {
        List<DependencyInfo> dependencies = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            boolean inDependencies = false;
            boolean inDevDependencies = false;
            
            for (String line : lines) {
                line = line.trim();
                
                if (line.equals("dependencies:")) {
                    inDependencies = true;
                    inDevDependencies = false;
                    continue;
                } else if (line.equals("dev_dependencies:")) {
                    inDependencies = false;
                    inDevDependencies = true;
                    continue;
                } else if (line.startsWith("dependency_overrides:") || 
                          line.startsWith("flutter:") ||
                          line.startsWith("environment:")) {
                    inDependencies = false;
                    inDevDependencies = false;
                    continue;
                }
                
                if ((inDependencies || inDevDependencies) && line.contains(":")) {
                    String[] parts = line.split(":");
                    if (parts.length >= 2) {
                        String name = parts[0].trim();
                        String version = parts[1].trim();
                        
                        // 清理版本号中的特殊字符
                        version = version.replaceAll("^[\"']|[\"']$", "");
                        
                        dependencies.add(new DependencyInfo(name, version, "Flutter", filePath, 
                                Paths.get(filePath).getFileName().toString()));
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("解析pubspec.yaml失败: " + filePath + " - " + e.getMessage());
        }
        return dependencies;
    }
    
    /**
     * 解析build.gradle文件中的依赖
     */
    public static List<DependencyInfo> parseBuildGradle(String filePath) {
        List<DependencyInfo> dependencies = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            boolean inDependencies = false;
            
            for (String line : lines) {
                line = line.trim();
                
                if (line.equals("dependencies {") || line.equals("dependencies{")) {
                    inDependencies = true;
                    continue;
                } else if (line.equals("}") && inDependencies) {
                    inDependencies = false;
                    continue;
                }
                
                if (inDependencies && line.contains("implementation") || 
                    line.contains("compile") || 
                    line.contains("api") ||
                    line.contains("compileOnly") ||
                    line.contains("runtimeOnly")) {
                    
                    // 解析依赖行
                    String dependency = extractDependencyFromGradleLine(line);
                    if (dependency != null) {
                        String[] parts = dependency.split(":");
                        if (parts.length >= 2) {
                            String name = parts[0] + ":" + parts[1];
                            String version = parts.length > 2 ? parts[2] : "";
                            
                            dependencies.add(new DependencyInfo(name, version, "Native", filePath, 
                                    Paths.get(filePath).getFileName().toString()));
                        }
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("解析build.gradle失败: " + filePath + " - " + e.getMessage());
        }
        return dependencies;
    }
    
    /**
     * 解析CMakeLists.txt文件中的依赖
     */
    public static List<DependencyInfo> parseCMakeLists(String filePath) {
        List<DependencyInfo> dependencies = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            
            for (String line : lines) {
                line = line.trim();
                
                // 查找find_package指令
                if (line.startsWith("find_package(")) {
                    String packageName = extractPackageFromCMakeLine(line);
                    if (packageName != null) {
                        dependencies.add(new DependencyInfo(packageName, "", "C/C++", filePath, 
                                Paths.get(filePath).getFileName().toString()));
                    }
                }
                
                // 查找target_link_libraries指令
                if (line.startsWith("target_link_libraries(")) {
                    List<String> libs = extractLibrariesFromCMakeLine(line);
                    for (String lib : libs) {
                        dependencies.add(new DependencyInfo(lib, "", "C/C++", filePath, 
                                Paths.get(filePath).getFileName().toString()));
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("解析CMakeLists.txt失败: " + filePath + " - " + e.getMessage());
        }
        return dependencies;
    }
    
    /**
     * 解析settings.gradle文件中的依赖
     */
    public static List<DependencyInfo> parseSettingsGradle(String filePath) {
        List<DependencyInfo> dependencies = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            
            for (String line : lines) {
                line = line.trim();
                
                // 查找include指令
                if (line.startsWith("include ")) {
                    String moduleName = extractModuleFromSettingsGradleLine(line);
                    if (moduleName != null) {
                        dependencies.add(new DependencyInfo(moduleName, "", "Native", filePath, 
                                Paths.get(filePath).getFileName().toString()));
                    }
                }
            }
            
        } catch (IOException e) {
            System.err.println("解析settings.gradle失败: " + filePath + " - " + e.getMessage());
        }
        return dependencies;
    }
    
    // 辅助方法
    private static void parseJsonDependencies(String content, String section, 
                                            List<DependencyInfo> dependencies, 
                                            String filePath, String type) {
        Pattern pattern = Pattern.compile("\"" + section + "\"\\s*:\\s*\\{([^}]+)\\}");
        Matcher matcher = pattern.matcher(content);
        
        if (matcher.find()) {
            String depsSection = matcher.group(1);
            Pattern depPattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\"");
            Matcher depMatcher = depPattern.matcher(depsSection);
            
            while (depMatcher.find()) {
                String name = depMatcher.group(1);
                String version = depMatcher.group(2);
                dependencies.add(new DependencyInfo(name, version, type, filePath, 
                        Paths.get(filePath).getFileName().toString()));
            }
        }
    }
    
    private static String extractDependencyFromGradleLine(String line) {
        // 提取引号中的依赖信息
        Pattern pattern = Pattern.compile("['\"]([^'\"]+)['\"]");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    private static String extractPackageFromCMakeLine(String line) {
        // 提取find_package中的包名
        Pattern pattern = Pattern.compile("find_package\\(\\s*([^\\s)]+)\\s*");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
    
    private static List<String> extractLibrariesFromCMakeLine(String line) {
        List<String> libraries = new ArrayList<>();
        // 提取target_link_libraries中的库名
        Pattern pattern = Pattern.compile("target_link_libraries\\([^)]+\\)");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            String content = matcher.group(0);
            // 提取库名（简单实现）
            String[] parts = content.split("\\s+");
            for (int i = 1; i < parts.length; i++) {
                String part = parts[i].trim();
                if (!part.isEmpty() && !part.equals("PRIVATE") && !part.equals("PUBLIC") && !part.equals("INTERFACE")) {
                    libraries.add(part);
                }
            }
        }
        return libraries;
    }
    
    private static String extractModuleFromSettingsGradleLine(String line) {
        // 提取include中的模块名
        Pattern pattern = Pattern.compile("include\\s+['\"]([^'\"]+)['\"]");
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}

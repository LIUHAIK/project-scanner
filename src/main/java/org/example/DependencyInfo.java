package org.example;

/**
 * 依赖信息数据模型
 */
public class DependencyInfo {
    private String name;           // 依赖名称
    private String version;        // 依赖版本
    private String type;           // 依赖类型：RN、Flutter、Native、C/C++
    private String filePath;       // 文件路径
    private String fileName;       // 文件名
    
    public DependencyInfo() {}
    
    public DependencyInfo(String name, String version, String type, String filePath, String fileName) {
        this.name = name;
        this.version = version;
        this.type = type;
        this.filePath = filePath;
        this.fileName = fileName;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getFilePath() {
        return filePath;
    }
    
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    
    public String getFileName() {
        return fileName;
    }
    
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    @Override
    public String toString() {
        return "DependencyInfo{" +
                "name='" + name + '\'' +
                ", version='" + version + '\'' +
                ", type='" + type + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}

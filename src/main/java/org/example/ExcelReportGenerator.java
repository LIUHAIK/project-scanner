package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Excel报告生成器
 */
public class ExcelReportGenerator {
    
    /**
     * 生成依赖分析Excel报告
     */
    public static void generateDependencyReport(List<DependencyInfo> dependencies, String outputPath) {
        try (Workbook workbook = new XSSFWorkbook()) {
            // 创建主工作表
            Sheet mainSheet = workbook.createSheet("依赖分析报告");
            
            // 创建标题行
            createHeaderRow(mainSheet, workbook);
            
            // 填充数据
            fillDataRows(mainSheet, workbook, dependencies);
            
            // 自动调整列宽
            autoSizeColumns(mainSheet);
            
            // 创建统计工作表
            createStatisticsSheet(workbook, dependencies);
            
            // 写入文件
            try (FileOutputStream fileOut = new FileOutputStream(outputPath)) {
                workbook.write(fileOut);
            }
            
            System.out.println("Excel报告已生成: " + outputPath);
            
        } catch (IOException e) {
            System.err.println("生成Excel报告失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 创建标题行
     */
    private static void createHeaderRow(Sheet sheet, Workbook workbook) {
        Row headerRow = sheet.createRow(0);
        
        // 创建标题样式
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        
        // 设置标题
        String[] headers = {"依赖名称", "版本号", "依赖类型", "文件路径", "文件名"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
    }
    
    /**
     * 填充数据行
     */
    private static void fillDataRows(Sheet sheet, Workbook workbook, List<DependencyInfo> dependencies) {
        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        
        // 为不同类型的依赖设置不同的颜色
        CellStyle rnStyle = workbook.createCellStyle();
        rnStyle.cloneStyleFrom(dataStyle);
        rnStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        rnStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        CellStyle flutterStyle = workbook.createCellStyle();
        flutterStyle.cloneStyleFrom(dataStyle);
        flutterStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
        flutterStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        CellStyle nativeStyle = workbook.createCellStyle();
        nativeStyle.cloneStyleFrom(dataStyle);
        nativeStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
        nativeStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        CellStyle cppStyle = workbook.createCellStyle();
        cppStyle.cloneStyleFrom(dataStyle);
        cppStyle.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        cppStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        
        int rowNum = 1;
        for (DependencyInfo dep : dependencies) {
            Row row = sheet.createRow(rowNum++);
            
            // 根据依赖类型选择样式
            CellStyle currentStyle = dataStyle;
            switch (dep.getType()) {
                case "RN":
                    currentStyle = rnStyle;
                    break;
                case "Flutter":
                    currentStyle = flutterStyle;
                    break;
                case "Native":
                    currentStyle = nativeStyle;
                    break;
                case "C/C++":
                    currentStyle = cppStyle;
                    break;
            }
            
            // 填充数据
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(dep.getName());
            cell0.setCellStyle(currentStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(dep.getVersion());
            cell1.setCellStyle(currentStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(dep.getType());
            cell2.setCellStyle(currentStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(dep.getFilePath());
            cell3.setCellStyle(currentStyle);
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(dep.getFileName());
            cell4.setCellStyle(currentStyle);
        }
    }
    
    /**
     * 自动调整列宽
     */
    private static void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 5; i++) {
            sheet.autoSizeColumn(i);
            // 设置最大列宽
            int currentWidth = sheet.getColumnWidth(i);
            if (currentWidth > 15000) {
                sheet.setColumnWidth(i, 15000);
            }
        }
    }
    
    /**
     * 创建统计工作表
     */
    private static void createStatisticsSheet(Workbook workbook, List<DependencyInfo> dependencies) {
        Sheet statsSheet = workbook.createSheet("统计信息");
        
        // 统计各类型依赖数量
        Map<String, Integer> typeCount = new HashMap<>();
        Map<String, Integer> fileCount = new HashMap<>();
        
        for (DependencyInfo dep : dependencies) {
            typeCount.put(dep.getType(), typeCount.getOrDefault(dep.getType(), 0) + 1);
            fileCount.put(dep.getFileName(), fileCount.getOrDefault(dep.getFileName(), 0) + 1);
        }
        
        // 创建标题
        Row titleRow = statsSheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("依赖类型统计");
        
        // 依赖类型统计
        int rowNum = 2;
        Row headerRow1 = statsSheet.createRow(rowNum++);
        headerRow1.createCell(0).setCellValue("依赖类型");
        headerRow1.createCell(1).setCellValue("数量");
        
        for (Map.Entry<String, Integer> entry : typeCount.entrySet()) {
            Row row = statsSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
        }
        
        // 文件统计
        rowNum += 2;
        Row titleRow2 = statsSheet.createRow(rowNum++);
        titleRow2.createCell(0).setCellValue("文件统计");
        
        rowNum++;
        Row headerRow2 = statsSheet.createRow(rowNum++);
        headerRow2.createCell(0).setCellValue("文件名");
        headerRow2.createCell(1).setCellValue("依赖数量");
        
        for (Map.Entry<String, Integer> entry : fileCount.entrySet()) {
            Row row = statsSheet.createRow(rowNum++);
            row.createCell(0).setCellValue(entry.getKey());
            row.createCell(1).setCellValue(entry.getValue());
        }
        
        // 自动调整列宽
        statsSheet.autoSizeColumn(0);
        statsSheet.autoSizeColumn(1);
    }
}

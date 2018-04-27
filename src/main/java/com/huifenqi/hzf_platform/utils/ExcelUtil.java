package com.huifenqi.hzf_platform.utils;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.activation.FileDataSource;
import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Ernest on 2016/10/24.
 */
public class ExcelUtil {

    private static Logger log = LoggerFactory.getLogger(ExcelUtil.class);

    public static final int MAX_ROW = 65536; // Excel文件最大行数
    public static final int MAX_COLUMN = 256; // Excel文件最大列数

    private static final int MAX_COLUMN_WIDTH = 255; // Excel文件最大列宽(字符数)

    private final static String excel2003L = ".xls";    //2003- 版本的excel
    private final static String excel2007U = ".xlsx";   //2007+ 版本的excel

    /**
     * 导出Excel
     *
     * @param out       输出流
     * @param titles    标题
     * @param data      数据
     * @param excelName excel的名称
     * @param firstLine 第一行内容，有的导出第一行是写入了提示语句，因此需要进行第一行合并
     */
    public static void exportExcel(OutputStream out, String excelName, String[] titles, List<Object[]> data,
                                   String firstLine) {
        HSSFWorkbook workBook = generateWorkbook(excelName, titles, data, firstLine);
        try {
            workBook.write(out);
        } catch (IOException e) {
            log.info("Excel导出工具出错" + e.getMessage());
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.info("关闭输出流出错" + e.getMessage());
            }
        }
    }

    public static FileDataSource exportExcel(HSSFWorkbook workBook, String excelName) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(excelName + ".xls");
            workBook.write(fos);
        } catch (IOException e) {
            log.info("Excel导出工具出错" + e.getMessage());
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                log.info("关闭输出流出错" + e.getMessage());
            }
        }
        FileDataSource fds = new FileDataSource(excelName + ".xls");
        return fds;
    }

    /**
     * 生成excel对象
     *
     * @param excelName
     * @param titles
     * @param data
     * @return
     */
    public static HSSFWorkbook generateWorkbook(String excelName, String[] titles, List<Object[]> data) {
        HSSFWorkbook workBook = generateWorkbook(excelName, titles, data, null);
        return workBook;
    }

    /**
     * 生成excel对象
     *
     * @param excelName
     * @param titles
     * @param data
     * @return
     */
    public static HSSFWorkbook generateWorkbook(String excelName, String[] titles, List<Object[]> data,
                                                String firstLine) {
        HSSFWorkbook workBook = new HSSFWorkbook();
        HSSFSheet sheet = workBook.createSheet(excelName);
        HSSFCellStyle style = workBook.createCellStyle();

        if (titles != null) {
            int contentStartIndex = 0;

            if (!StringUtil.isEmpty(firstLine)) {// 第一行有提示语句
                contentStartIndex = 1;
                HSSFRow firstRow = sheet.createRow(0);// 第0行
                HSSFCell cell = firstRow.createCell(0);// 单元格
                cell.setCellValue(firstLine);// 设置第一行的值
                sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, titles.length - 1));// 合并单元格
                HSSFCellStyle cellStyle = workBook.createCellStyle();
                cellStyle.setWrapText(true);
                cell.setCellStyle(cellStyle);
                // firstRow.setHeight((short)550);
            }

            // 初始化列宽
            int[] columWidth = new int[titles.length];
            for (int i = 0; i < titles.length; i++) {
                columWidth[i] = 0;
            }

            HSSFRow titleRow = sheet.createRow(contentStartIndex);
            for (int i = 0, size = titles.length; i < size; i++) {
                HSSFCell cell = titleRow.createCell(i);
                cell.setCellValue(titles[i]);

                // 计算列宽
                int width = getStringDisplayLength(titles[i]);
                if (width > columWidth[i]) {
                    columWidth[i] = width;
                }

            }
            contentStartIndex++;
            if (data != null && data.size() != 0) {// 写入Excel数据
                for (int i = 0, size = data.size(); i < size; i++) {
                    HSSFRow row = sheet.createRow(contentStartIndex + i);
                    for (int j = 0; j < data.get(i).length; j++) {
                        HSSFCell cell = row.createCell(j);
                        Object object = data.get(i)[j];
                        if (object == null) {
                            continue;
                        }
                        if (object instanceof Integer) {
                            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue((Integer) object);
                        } else if (object instanceof Long) {
                            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue((Long) object);
                        } else if (object instanceof Float) {
                            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue((Float) object);
                        } else if (object instanceof Double) {
                            cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                            cell.setCellValue((Double) object);
                        } else if (object instanceof Date) {
                            DataFormat format = workBook.createDataFormat();
                            style.setDataFormat(format.getFormat("yyyy-MM-dd HH:mm:ss"));
                            cell.setCellStyle(style);
                            cell.setCellValue((Date) object);
                        } else {
                            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                            cell.setCellValue(object.toString());
                        }

                        // 计算列宽
                        int width = getStringDisplayLength(object.toString());
                        if (width > columWidth[j]) {
                            columWidth[j] = width;
                        }
                    }
                }
            }

            // 设置列宽
            for (int i = 0; i < titles.length; i++) {
                int width = columWidth[i] + 2;
                width = width > MAX_COLUMN_WIDTH ? MAX_COLUMN_WIDTH : width;
                sheet.setColumnWidth(i, width * 256);
            }
        }
        return workBook;
    }

    /**
     * 设置背景颜色
     *
     * @param style
     * @param color
     */
    public static void setBackGroundColor(HSSFCellStyle style, Short color) {
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(color);
    }

    /**
     * 从excel中读取String类型格式
     *
     * @param cell
     * @return
     */
    public static String getStringDataFromCell(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    // 用于转化为日期格式
                    Date d = cell.getDateCellValue();
                    DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
                    return formater.format(d);
                } else {
                    cell.setCellType(HSSFCell.CELL_TYPE_STRING);// 转成字符串格式
                    return cell.getStringCellValue();
                }
            case HSSFCell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            default:
                return cell.getStringCellValue();
        }
    }

    /**
     * 解析excel的数据格式
     *
     * @param cell
     * @return
     */
    public static String parseExcelData(Cell cell) {
        String result = new String();
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_NUMERIC:// 数字类型
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = null;
                    if (cell.getCellStyle().getDataFormat() == HSSFDataFormat.getBuiltinFormat("h:mm")) {
                        sdf = new SimpleDateFormat("HH:mm");
                    } else {// 日期
                        sdf = new SimpleDateFormat("yyyy-MM-dd");
                    }
                    Date date = cell.getDateCellValue();
                    result = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 58) {
                    // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    double value = cell.getNumericCellValue();
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
                    result = sdf.format(date);
                } else {
                    double value = cell.getNumericCellValue();
                    CellStyle style = cell.getCellStyle();
                    DecimalFormat format = new DecimalFormat();
                    String temp = style.getDataFormatString();
                    // 单元格设置成常规
                    if (temp.equals("General")) {
                        format.applyPattern("#");
                    }
                    result = format.format(value);
                }
                break;
            case HSSFCell.CELL_TYPE_STRING:// String类型
                result = cell.getRichStringCellValue().toString();
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                result = "";
            default:
                result = "";
                break;
        }
        return result;
    }

    /**
     * 从excel类型的输入流中读取数据
     *
     * @param inputStream excel类型的输入流
     * @param startRowId  数据开始的行
     * @param sheetIndex  excel的索引，默认值为0
     * @return List<List<Object>> 如果无数据，返回null
     */
    public static List<List<String>> readDataFromInputStream(InputStream inputStream, int startRowId, int sheetIndex) {
        HSSFWorkbook wb = null;
        List<List<String>> data = null;
        if (inputStream == null) {
            return data;
        }
        try {
            wb = new HSSFWorkbook(inputStream);
            Sheet sheet = wb.getSheetAt(sheetIndex);
            int lastRowNum = sheet.getLastRowNum();
            data = new ArrayList<List<String>>();
            for (int i = startRowId; i <= lastRowNum; i++) {
                HSSFRow row = (HSSFRow) sheet.getRow(i);
                int cellLength = row.getLastCellNum();
                List<String> stringList = new ArrayList<String>();
                for (int j = 0; j < cellLength; j++) {
                    HSSFCell cell = row.getCell(j);
                    String cellValue = null;
                    if (cell != null) {
                        cellValue = getStringDataFromCell(cell);
                    }
                    stringList.add(cellValue);
                }
                data.add(stringList);
            }
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return data;
    }

    /**
     * 计算字符串宽度，统计每个字符：ASCII码127以下记作1，以上记作2
     */
    private static int getStringDisplayLength(String string) {

        int length = 0;

        if (string == null || string.length() == 0) {
            return length;
        }

        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c > 127) {
                length += 2;
            } else {
                length++;
            }
        }

        return length;
    }

    /**
     * 描述：获取IO流中的数据，组装成List<List<Object>>对象
     *
     * @param in,fileName
     * @return
     * @throws IOException
     */
    public static List<Map<Integer, Object>> getListByExcel(InputStream in, String fileName) throws Exception {
        List<Map<Integer, Object>> list = null;

        //创建Excel工作薄
        Workbook work = getWorkbook(in, fileName);
        if (null == work) {
            throw new Exception("Create the Excel workbook is null！");
        }
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        list = new ArrayList<>();

        //遍历Excel中所有的sheet
        for (int i = 0; i < work.getNumberOfSheets(); i++) {
            sheet = work.getSheetAt(i);
            if (sheet == null) {
                continue;
            }

            //遍历当前sheet中的所有行
            for (int j = sheet.getFirstRowNum(); j <= sheet.getLastRowNum(); j++) {
                row = sheet.getRow(j);
                if (row == null || row.getFirstCellNum() == j) {
                    continue;
                }

                //遍历所有的列
                //List<Object> li = new ArrayList<>();
                Map<Integer, Object> cellMap = new HashMap<>();
                for (int y = row.getFirstCellNum(); y < row.getLastCellNum(); y++) {
                    cell = row.getCell(y);
                    if (cell == null || cell.equals("")) {
                        continue;
                    }
                    cellMap.put(y, getCellValue(cell));
                }
                list.add(cellMap);

            }
        }
        work.close();
        return list;
    }

    /**
     * 描述：根据文件后缀，自适应上传文件的版本
     *
     * @param inStr,fileName
     * @return
     * @throws Exception
     */
    public static Workbook getWorkbook(InputStream inStr, String fileName) throws Exception {
        Workbook wb = null;
        String fileType = fileName.substring(fileName.lastIndexOf("."));
        if (excel2003L.equals(fileType)) {
            wb = new HSSFWorkbook(inStr);  //2003-
        } else if (excel2007U.equals(fileType)) {
            wb = new XSSFWorkbook(inStr);  //2007+
        } else {
            throw new Exception("解析的文件格式有误！");
        }
        return wb;
    }

    /**
     * 描述：对表格中数值进行格式化
     *
     * @param cell
     * @return
     */
    public static Object getCellValue(Cell cell) {
        Object value = null;
        //DecimalFormat df = new DecimalFormat("0.##");  //格式化number String字符
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  //日期格式化
        // df2 = new DecimalFormat("0.##");  //格式化数字

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                value = cell.getRichStringCellValue().getString();
                break;
            case Cell.CELL_TYPE_NUMERIC:
                if ("General".equals(cell.getCellStyle().getDataFormatString())) {
                    value = String.valueOf(cell.getNumericCellValue());
                } else {
                    value = String.valueOf(cell.getNumericCellValue());
                }
                break;
            case Cell.CELL_TYPE_BOOLEAN:
                value = cell.getBooleanCellValue();
                break;
            case Cell.CELL_TYPE_BLANK:
                value = "";
                break;
            default:
                break;
        }
        return value;
    }

    /**
     * @Title: exportExcel
     * @Description: 导出Excel的方法
     * @author: evan @ 2014-01-09
     * @param workbook
     * @param sheetNum (sheet的位置，0表示第一个表格中的第一个sheet)
     * @param sheetTitle  （sheet的名称）
     * @param headers    （表格的标题）
     * @param result   （表格的数据）
     * @param out  （输出流）
     * @throws Exception
     */
    public static void exportExcel(HSSFWorkbook workbook, int sheetNum,
                            String sheetTitle, String[] headers, List<List<String>> result,
                            OutputStream out) throws Exception {
        // 生成一个表格
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(sheetNum, sheetTitle);
        // 设置表格默认列宽度为20个字节
        sheet.setDefaultColumnWidth((short) 20);
        // 生成一个样式
        HSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
       // style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);
        style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        // 生成一个字体
        HSSFFont font = workbook.createFont();
        //font.setColor(HSSFColor.BLACK.index);
        font.setFontHeightInPoints((short) 12);
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        // 把字体应用到当前的样式
        style.setFont(font);

        // 指定当单元格内容显示不下时自动换行
        style.setWrapText(true);

        // 产生表格标题行
        HSSFRow row = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell((short) i);

            cell.setCellStyle(style);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text.toString());
        }
        // 遍历集合数据，产生数据行
        if (result != null) {
            int index = 1;
            for (List<String> m : result) {
                row = sheet.createRow(index);
                int cellIndex = 0;
                for (String str : m) {
                    HSSFCell cell = row.createCell((short) cellIndex);
                    cell.setCellValue(str.toString());
                    cellIndex++;
                }
                index++;
            }
        }
    }

    //just for test by arison
    public static void testExportExcel(String[] args) {
        try {
            OutputStream out = new FileOutputStream("D:\\test.xls");
            List<List<String>> data = new ArrayList<List<String>>();
            for (int i = 1; i < 5; i++) {
                List rowData = new ArrayList();
                rowData.add(String.valueOf(i));
                rowData.add("东霖柏鸿");
                data.add(rowData);
            }
            String[] headers = { "ID", "用户名" };
           // ExportExcelUtils eeu = new ExportExcelUtils();
            HSSFWorkbook workbook = new HSSFWorkbook();
            exportExcel(workbook, 0, "上海", headers, data, out);
            exportExcel(workbook, 1, "深圳", headers, data, out);
            exportExcel(workbook, 2, "广州", headers, data, out);
            //原理就是将所有的数据一起写入，然后再关闭输入流。
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}


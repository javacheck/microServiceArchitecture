package cn.self.cloud.commonutils.common;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * poi组件excel工具类
 */
public class ExcelUtil {

    /**
     * 写入文件
     * @param path 文件路径
     * @param list
     * @param sign true表示新建  false表示读取已存在新建sheet
     */
    public static void writeExcel(String path, List<Object> list, boolean sign){
        OutputStream out = null;
        Workbook wb = getWorkbook(path, sign);
        try {
            out = new FileOutputStream(path);
            Sheet sheet = wb.createSheet();
            putSheet(sheet, list);
            wb.write(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // IOUtil.close(out);
        }
    }


    /**
     * 读取excel指定页的数据
     * @param path
     * @param sheetnum
     * @return
     */
    public static List<Object> readExcel(String path, int sheetnum) {
        List<Object> listSheet = null;
        Workbook wb = getWorkbook(path, false);
        int sheets = wb.getNumberOfSheets();
        if(sheetnum <= sheets && sheetnum >=0){
            //获取sheet
            Sheet sheet = wb.getSheetAt(sheetnum);
            listSheet = getSheet(sheet);
        }
        return listSheet;
    }


    /**
     * 获取指定页sheet的数据
     * @param sheet
     * @return
     */
    private static List<Object> getSheet(Sheet sheet){
        List<Object> list = new ArrayList<>();
        // 获得表单的迭代器
        Iterator<Row> rows = sheet.rowIterator();
        while (rows.hasNext()) {
            // 获得行数据
            Row row = rows.next();
            // 获得行的迭代器
            Iterator<Cell> cells = row.cellIterator();
            List<String> rowList = new ArrayList<>();
            while (cells.hasNext()) {
                Cell cell = cells.next();
                String value;
                if(cell != null) {
                    switch (cell.getCellTypeEnum()) {

                        case FORMULA:
                            value = "" + cell.getCellFormula();
//                            value = "FORMULA value=" + cell.getCellFormula();
                            break;

                        case NUMERIC:
                            value = "" + cell.getNumericCellValue();
//                            value = "NUMERIC value=" + cell.getNumericCellValue();
                            break;

                        case STRING:
                            value = cell.getStringCellValue();
//                            value = "STRING value=" + cell.getStringCellValue();
                            break;

                        case BLANK:
                            value = "";
//                            value = "<BLANK>";
                            break;

                        case BOOLEAN:
                            value = "" + cell.getBooleanCellValue();
//                            value = "BOOLEAN value-" + cell.getBooleanCellValue();
                            break;

                        case ERROR:
                            value = "";
//                            value = "ERROR value=" + cell.getErrorCellValue();
                            break;

                        default:
                            value = "";
//                            value = "UNKNOWN value of type " + cell.getCellTypeEnum();
                    }
//                    System.out.println("CELL col=" + cell.getColumnIndex() + " VALUE=" + value);
                    rowList.add(value);
                }
            }

            list.add(rowList);

        }

        return list;
    }

    /**
     * 向指定页中写入数据
     * @param sheet
     * @param list
     */
    private static void putSheet(Sheet sheet, List<Object> list){
        Row row;
        Cell c;
        if(sheet != null && list != null){
            for(int i = 0; i < list.size(); i++){
                row =  sheet.createRow(i);
                Object obj = list.get(i);
                if(obj instanceof List){
                    List rowData = (List) obj;
                    for(int j = 0; j < rowData.size(); j++){
                        Object colData = rowData.get(j);
                        c = row.createCell(j);
                        String value = colData != null ? colData.toString() : "";
                        if(colData instanceof String){
                            c.setCellValue(value);
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取工作簿workbook
     * @param path
     * @param sign true表示新建 false表示读取
     * @return
     */
    private static Workbook getWorkbook(String path, boolean sign){
        Workbook wb = null;
        InputStream input = null;
        try {
            if(sign){
                if(path.endsWith(Const.EXCEL_XLS)){
                    //2003-2007
                    wb = new HSSFWorkbook();
                }else{
                    //2007+
                    wb = new XSSFWorkbook();
                }
            }else{
                input = new FileInputStream(path);
                if(path.endsWith(Const.EXCEL_XLSX)){
                    //2007+
                    wb = new XSSFWorkbook(input);
                }else{
                    //2003-2007
                    wb = new HSSFWorkbook(input);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            // IOUtil.close(input);
        }
        return wb;
    }


    public static void main(String[] args) {
        String path = "E:\\doc\\1.xlsx";
        /*int i = 1;
        List<Object> list = readExcel(path, i);
        System.out.println(list);*/

        List<Object> list = new ArrayList<>();
        List<Object> rowData = new ArrayList<>();
        rowData.add("123");
        rowData.add("1231");

        list.add(rowData);

        writeExcel(path, list, true);

    }
}

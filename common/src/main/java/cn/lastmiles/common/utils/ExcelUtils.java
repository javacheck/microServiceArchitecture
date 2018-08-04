package cn.lastmiles.common.utils;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddressList;

public abstract class ExcelUtils {

	/**
	 * 简单解析，所有的内容都可以转成字符串
	 * 
	 * @param in
	 * @return
	 */
	public static List<List<String>> simpleExcel(InputStream in) {// 输入输出流
		try {
			Workbook wb = WorkbookFactory.create(in);// 创建工作空间
			// 获取工作表
			Sheet sheet = wb.getSheetAt(0);// 获取第一个工作表
			int lastRowNum = sheet.getLastRowNum();// 获得表行数

			List<List<String>> list = new ArrayList<List<String>>();
			for (int i = 0; i <= lastRowNum; i++) {// 循环一行一行取数
				List<String> cellList = new ArrayList<String>();
				Row row = sheet.getRow(i);// 获取第i行的工作行,第0行是列头
				if (row != null) {
					int lastCellNum = sheet.getRow(0).getLastCellNum();// 获得表列数(这里取第一行列数为最大列表)
					boolean flag = false;
					for (int j = 0; j < lastCellNum; j++) {
						Cell cell = row.getCell(j);// 获取第i行的工作行的第i个单元格的值
						if (cell != null) {
							cell.setCellType(1);
							int type = cell.getCellType();
							if (type == Cell.CELL_TYPE_BLANK) {
								cellList.add(null);
							} else if (type == Cell.CELL_TYPE_NUMERIC) {
								cellList.add(Double.valueOf(
										cell.getNumericCellValue()).toString());
								flag = true;
							} else if (type == Cell.CELL_TYPE_STRING) {
								String value = cell.getStringCellValue();
								cellList.add(value);
								if (StringUtils.isNotBlank(value)) {
									flag = true;
								}
							} else if (type == Cell.CELL_TYPE_BOOLEAN) {
								cellList.add(Boolean.valueOf(
										cell.getBooleanCellValue()).toString());
								flag = true;
							} else {
								cellList.add(null);
							}
						} else {
							cellList.add(null);
						}
					}
					if (flag) {
						list.add(cellList);
					}
				}
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
    /**
     * 得到一个工作区最后一条记录的序号，相当于这个工作簿共多少行数据。
     * @param sheetOrder 工作区序号
     * @return INT 序号。
     * @throws IOException 根据excel路径加载excel异常。
     * @throws InvalidFormatException
     */
    public static int getSheetLastRowNum(int sheetOrder,InputStream in) throws InvalidFormatException, IOException  {
        Workbook workbook = WorkbookFactory.create(in);
        if ( null != in ) {
            in.close();
        }
        Sheet sheet = workbook.getSheetAt(sheetOrder);
        return sheet.getLastRowNum();
    }
    
    /**
     * 读取某个工作簿上的所有单元格的值。 
     * @param sheetOrder 工作簿序号，从0开始。
     * @return List<Object[]> 所有单元格的值。
     * @throws IOException 加载excel文件IO异常。
     * @throws FileNotFoundException excel文件没有找到异常。
     * @throws InvalidFormatException
     */
    public static List<Object[]> read(int sheetOrder,InputStream is) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(is);
        if ( null != is) {
            is.close();
        }
        Sheet sheet = workbook.getSheetAt(sheetOrder);
        // 用来记录excel值
        List<Object[]> valueList = new LinkedList<Object[]>();
        // 循环遍历每一行、每一列。
        for (Row row : sheet) {
            // 每一行
            Object[] rowObject = null;
            for (Cell cell : row) {
                // cell.getCellType是获得cell里面保存的值的type
                switch (cell.getCellType()) {
	                case Cell.CELL_TYPE_BOOLEAN:
	                    // 得到Boolean对象的方法
	                    rowObject = CollectionUtil.addObjectToArray(rowObject,
	                            cell.getBooleanCellValue());
	                    break;
	                case Cell.CELL_TYPE_NUMERIC:
	                    // 先看是否是日期格式
	                    if (DateUtil.isCellDateFormatted(cell)) {
	                        // 读取日期格式
	                        rowObject = CollectionUtil.addObjectToArray(rowObject,
	                                cell.getDateCellValue());
	                    } else {
	                        DecimalFormat df = new DecimalFormat();
	                        // 单元格的值,替换掉,
	                        String value = df.format(cell.getNumericCellValue())
	                                .replace(",", "");
	                        // 读取数字
	                        rowObject = CollectionUtil.addObjectToArray(rowObject,
	                                value);
	                    }
	                    break;
	                case Cell.CELL_TYPE_FORMULA:
	                    // 读取公式
	                    rowObject = CollectionUtil.addObjectToArray(rowObject,
	                            cell.getCellFormula());
	                    break;
	                case Cell.CELL_TYPE_STRING:
	                    // 读取String
	                    rowObject = CollectionUtil.addObjectToArray(rowObject, cell
	                            .getRichStringCellValue().toString());
	                    break;
	                default : rowObject = CollectionUtil.addObjectToArray(rowObject, null);
	                	break;
	            }
            }
            // 将这行添加到list。
            valueList.add(rowObject);
        }
        return valueList;
    }
        
    /**
     * 根据条件，生成工作薄对象到内存。
     * @param sheetName 工作表对象名称
     * @param fieldName 首列列名称
     * @param data 数据
     * @return HSSFWorkbook
     */
    public static HSSFWorkbook makeWorkBook(String sheetName, String[] fieldName, List<Object[]> data) {
        // 用来记录最大列宽,自动调整列宽。
        Integer collength[] = new Integer[fieldName.length];
 
        // 产生工作薄对象
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 产生工作表对象
        HSSFSheet sheet = workbook.createSheet();
        // 为了工作表能支持中文,设置字符集为UTF_16
        workbook.setSheetName(0, sheetName);
        // 产生一行
        HSSFRow row = sheet.createRow(0);
        
        // 写入各个字段的名称
        for (int i = 0; i < fieldName.length; i++) {
            // 创建第一行各个字段名称的单元格
        	HSSFCell cell  = row.createCell(i);
            // 设置单元格内容为字符串型
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);
            // 为了能在单元格中输入中文,设置字符集为UTF_16
            // cell.setEncoding(HSSFCell.ENCODING_UTF_16);
            // 给单元格内容赋值
            cell.setCellValue(new HSSFRichTextString(fieldName[i]));
            // 初始化列宽
            collength[i] = fieldName[i].getBytes().length;
        }

        // 临时单元格内容
        String tempCellContent = "";
        // 写入各条记录,每条记录对应excel表中的一行
        for (int i = 0; i < data.size(); i++) {
            Object[] tmp = data.get(i);
            // 生成一行
            row = sheet.createRow(i + 1);
            for (int j = 0; j < tmp.length; j++) {
            	HSSFCell cell  = row.createCell(i);
                // 设置单元格字符类型为String
                cell.setCellType(HSSFCell.CELL_TYPE_STRING);
                tempCellContent = (tmp[j] == null) ? "" : tmp[j].toString();
                cell.setCellValue(new HSSFRichTextString(tempCellContent));
 
                // 如果自动调整列宽度。
                if (true) {
                    if (j >= collength.length) { // 标题列数小于数据列数时。
                        collength = CollectionUtil.addObjectToArray(collength,
                                tempCellContent.getBytes().length);
                    } else {
                        // 如果这个内容的宽度大于之前最大的，就按照这个设置宽度。
                        if (collength[j] < tempCellContent.getBytes().length) {
                            collength[j] = tempCellContent.getBytes().length;
                        }
                    }
                }
            }
        }
 
        // 自动调整列宽度。
        if (true) {
            // 调整列为这列文字对应的最大宽度。
            for (int i = 0; i < fieldName.length; i++) {
                sheet.setColumnWidth(i, collength[i] * 5 * 256);
            }
        }
        return workbook;
    }
    
    /**
	 * 简单解析，所有的内容都可以转成字符串
	 * 
	 * @param in
	 * @return
	 */
	public static List<List<String>> readProductExcel(InputStream in) {// 输入输出流
		try {
			Workbook wb = WorkbookFactory.create(in);// 创建工作空间
			// 获取工作表
			Sheet sheet = wb.getSheetAt(0);// 获取第一个工作表
			int lastRowNum = sheet.getLastRowNum();// 获得表行数

			List<List<String>> list = new ArrayList<List<String>>();
			for (int i = 0; i <= lastRowNum; i++) {// 循环一行一行取数
				List<String> cellList = new ArrayList<String>();
				Row row = sheet.getRow(i);// 获取第i行的工作行,第0行是列头
				if (row != null) {
					int lastCellNum = sheet.getRow(1).getLastCellNum();// 获得表列数(这里取第二行列数为最大列表)
					boolean flag = false;
					for (int j = 0; j < lastCellNum; j++) {
						Cell cell = row.getCell(j);// 获取第i行的工作行的第i个单元格的值
						if (cell != null) {
							cell.setCellType(1);
							int type = cell.getCellType();
							if (type == Cell.CELL_TYPE_BLANK) {
								cellList.add(null);
							} else if (type == Cell.CELL_TYPE_NUMERIC) {
								cellList.add(Double.valueOf(
										cell.getNumericCellValue()).toString());
								flag = true;
							} else if (type == Cell.CELL_TYPE_STRING) {
								String value = cell.getStringCellValue();
								cellList.add(value);
								if (StringUtils.isNotBlank(value)) {
									flag = true;
								}
							} else if (type == Cell.CELL_TYPE_BOOLEAN) {
								cellList.add(Boolean.valueOf(
										cell.getBooleanCellValue()).toString());
								flag = true;
							} else {
								cellList.add(null);
							}
						} else {
							cellList.add(null);
						}
					}
					if (flag) {
						list.add(cellList);
					}
				}
			}
			return list;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	public static void generateModel(HSSFWorkbook workbook,HSSFSheet sheet,String sheetName,List<String> list,int showCellIndex,int startCellIndex,boolean bigData){
		HSSFRow row = null;
		HSSFCell cell = null;

		// ----->>>>>设置列数据的样式
		CellStyle hiddenStyle = workbook.createCellStyle();
		hiddenStyle.setAlignment(CellStyle.ALIGN_CENTER); // 对齐方式设置
		hiddenStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		int length = list.size();
		DVConstraint constraint = null;
		if(bigData){
			
			// ----->>>>>创建隐藏的Sheet页(存储数据源)
			HSSFSheet hidden = workbook.createSheet(sheetName);
			System.out.println(workbook.getNumberOfSheets());
			workbook.setSheetHidden((workbook.getNumberOfSheets()-1), true); 	//数据源sheet页不显示
			
		
			for (int i = 0; i < length; i++) {
				row = hidden.createRow(i);
				cell = row.createCell(0);
				cell.setCellValue(list.get(i));
			}
			
			Name namedCell = workbook.createName();
			namedCell.setNameName(sheetName);
			namedCell.setRefersToFormula(sheetName+"!$A$1:$A" + list.size()); // 表示A列1-N行作为下拉列表来源数据
			
			constraint = DVConstraint.createFormulaListConstraint(sheetName); // 生成下拉框内容(下拉框内容引用hiddenCell中的)
		} else {
			String[] str = new String[length];
			for (int i = 0; i < length; i++) {
				str[i] = list.get(i);
			}
			constraint = DVConstraint.createExplicitListConstraint(str);
		}
		
		// HSSF包下也有个CellRangeAddressList,但Deprecated,要用SS包这个
		CellRangeAddressList addressList = null;
		HSSFDataValidation validation = null;
		row = null;
		cell = null;
		
		for (int i = startCellIndex; i < 3000; i++) {
			row = sheet.createRow(i);
			cell = row.createCell(0);
			cell.setCellStyle(hiddenStyle);
			// 对（0，0）单元格有效 (顺序为 起始行 起始列 终止行 终止列),如果版本升高后出现问题,则是因为此顺序改变了(新顺序为 起始行 终止行 起始列 终止列) 
			addressList = new CellRangeAddressList(i, i, showCellIndex, showCellIndex); 
			
			validation = new HSSFDataValidation(addressList, constraint); // 绑定下拉框和作用区域
			
			validation.setEmptyCellAllowed(true);  
			validation.setShowPromptBox(false);  
			//validation.createErrorBox("无效输入!", "请选择下拉列表");  
            
			sheet.addValidationData(validation); // 对sheet页生效 
			validation.setShowErrorBox(false);// 取消弹出错误框
		}
	}
}
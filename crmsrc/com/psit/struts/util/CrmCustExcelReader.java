package com.psit.struts.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.psit.struts.entity.CusCorCus;

/**
 * 操作Excel表格的功能类
 */
public class CrmCustExcelReader {
	
	public static List GDBMap = new ArrayList();
	private POIFSFileSystem fs;
	private HSSFWorkbook wb;
	private HSSFSheet sheet;
	private HSSFRow row;

	public List initYHDBMapExcelReader() {
		GDBMap.clear();
		try {
			CrmCustExcelReader excelReader = new CrmCustExcelReader();
			// 对读取Excel表格内容测试
			String filePath=CrmCustExcelReader.class.getResource("")+"/客户信息.xls";
			InputStream is2 = new FileInputStream(filePath.substring(6,filePath.length()));
			List map = excelReader.readExcelContent(is2);	
			return map;
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取Excel表格表头的内容
	 * 
	 * @param InputStream
	 * @return String 表头内容的数组
	 */
	public String[] readExcelTitle(InputStream is) {
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		row = sheet.getRow(0);
		// 标题总列数
		int colNum = row.getPhysicalNumberOfCells();
		System.out.println("colNum:" + colNum);
		String[] title = new String[colNum];
		for (int i = 0; i < colNum; i++) {
			// title[i] = getStringCellValue(row.getCell((short) i));
			title[i] = getCellFormatValue(row.getCell((short) i));
		}
		return title;
	}

	/**
	 * 读取Excel数据内容
	 * 
	 * @param InputStream
	 * @return Map 包含单元格数据内容的Map对象
	 */
	public List readExcelContent(InputStream is) {
		List content = new ArrayList();
		String str1 = "";
		String str2 = "";
		String str3 = "";
		String str4 = "";
		try {
			fs = new POIFSFileSystem(is);
			wb = new HSSFWorkbook(fs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		sheet = wb.getSheetAt(0);
		// 得到总行数
		int rowNum = sheet.getLastRowNum();
		row = sheet.getRow(0);
		int colNum = row.getPhysicalNumberOfCells();
		// 正文内容应该从第二行开始,第一行为表头的标题
		for (int i = 1; i < rowNum; i++) {
			System.out.println(i);
			CusCorCus c=new CusCorCus();
			if(i==344){
			i=344;	
			}
			row = sheet.getRow(i);
			if(row==null)
				continue;
			int j = 0;
			while (j < colNum) {
				// 每个单元格的数据内容用"-"分割开，以后需要时用String类的replace()方法还原数据
				// 也可以将每个单元格的数据设置到一个javabean的属性中，此时需要新建一个javabean
				// str += getStringCellValue(row.getCell((short) j)).trim() +
				// "-";
				
				if (j == 0){
					str1 = getCellFormatValue(row.getCell((short) j)).trim();
					c.setCorName(str1);
				}
				else if(j == 1){
					str2 = getCellFormatValue(row.getCell((short) j)).trim();
					c.setCorAddress(str1);
				}
				else if(j ==2){
					str3 = getCellFormatValue(row.getCell((short) j)).trim();
					c.setCorNet(str3);
				}
				else if(j ==3){
					str4= getCellFormatValue(row.getCell((short) j)).trim();
				c.setCorPhone(str4);
				}
				j++;
			}

			content.add(c);
			str1 = "";
			str2 = "";
		}
		return content;
	}

	/**
	 * 获取单元格数据内容为字符串类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getStringCellValue(HSSFCell cell) {
		String strCell = "";
		switch (cell.getCellType()) {
		case HSSFCell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case HSSFCell.CELL_TYPE_NUMERIC:
			strCell = String.valueOf(cell.getNumericCellValue());
			break;
		case HSSFCell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case HSSFCell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		if (strCell.equals("") || strCell == null) {
			return "";
		}
		if (cell == null) {
			return "";
		}
		return strCell;
	}

	/**
	 * 获取单元格数据内容为日期类型的数据
	 * 
	 * @param cell
	 *            Excel单元格
	 * @return String 单元格数据内容
	 */
	private String getDateCellValue(HSSFCell cell) {
		String result = "";
		try {
			int cellType = cell.getCellType();
			if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
				Date date = cell.getDateCellValue();
				result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
						+ "-" + date.getDate();
			} else if (cellType == HSSFCell.CELL_TYPE_STRING) {
				String date = getStringCellValue(cell);
				result = date.replaceAll("[年月]", "-").replace("日", "").trim();
			} else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
				result = "";
			}
		} catch (Exception e) {
			System.out.println("日期格式不正确!");
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 根据HSSFCell类型设置数据
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellFormatValue(HSSFCell cell) {
		String cellvalue = "";
		if (cell != null) {
			// 判断当前Cell的Type
			switch (cell.getCellType()) {
			// 如果当前Cell的Type为NUMERIC
			case HSSFCell.CELL_TYPE_NUMERIC:
			case HSSFCell.CELL_TYPE_FORMULA: {
				// 判断当前的cell是否为Date
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					// 如果是Date类型则，转化为Data格式

					// 方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
					// cellvalue = cell.getDateCellValue().toLocaleString();

					// 方法2：这样子的data格式是不带带时分秒的：2011-10-12
					Date date = cell.getDateCellValue();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					cellvalue = sdf.format(date);

				}
				// 如果是纯数字
				else {
					// 取得当前Cell的数值
					cellvalue = String.valueOf(cell.getNumericCellValue());
				}
				break;
			}
				// 如果当前Cell的Type为STRIN
			case HSSFCell.CELL_TYPE_STRING:
				// 取得当前的Cell字符串
				cellvalue = cell.getRichStringCellValue().getString();
				break;
			// 默认的Cell值
			default:
				cellvalue = " ";
			}
		} else {
			cellvalue = "";
		}
		return cellvalue;

	}

	public static List initGDBMap() {
		try {
			CrmCustExcelReader excelReader = new CrmCustExcelReader();
			// 对读取Excel表格内容测试
			InputStream is2 = new FileInputStream("数据库映射表.xls");
			List l = excelReader.readExcelContent(is2);
			System.out.println("获得Excel表格的内容:");
			for (int i = 1; i <= l.size(); i++) {
				
			}
			return l;
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args) {
//		try {
//			// 对读取Excel表格标题测试
//			InputStream is = new FileInputStream("数据库映射表.xls");
//			YHDBMapExcelReader excelReader = new YHDBMapExcelReader();
//			String[] title = excelReader.readExcelTitle(is);
//			System.out.println("获得Excel表格的标题:");
//			for (String s : title) {
//				System.out.print(s + " ");
//			}
//
//			// 对读取Excel表格内容测试
//			InputStream is2 = new FileInputStream("数据库映射表.xls");
//			Map<Integer, String> map = excelReader.readExcelContent(is2);
//			System.out.println("获得Excel表格的内容:");
//			for (int i = 1; i <= map.size(); i++) {
//				wrFile(map.get(i) + "\r\n");
//			}
//			try {
//				writer.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//			System.out.println("未找到指定路径的文件!");
//			e.printStackTrace();
//		}
		System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
		System.out.println(CrmCustExcelReader.class.getClassLoader().getResource(""));
		System.out.println(ClassLoader.getSystemResource(""));
		System.out.println(CrmCustExcelReader.class.getResource(""));
	    System.out.println(CrmCustExcelReader.class.getResource("/")); //Class文件所在路径
		System.out.println(new File("/").getAbsolutePath());
		System.out.println(System.getProperty("user.dir"));
		
		List c=new CrmCustExcelReader().initYHDBMapExcelReader();
		Iterator it=c.iterator();

		while(it.hasNext()){
			CusCorCus key=(CusCorCus)it.next();
			System.out.println(key.getCorName());
			
		}
		
		
	}

	

	static FileWriter writer = null;

	public static void wrFile(String args) {
		try {
			if (writer == null)
				writer = new FileWriter("E:\\数据库命名改造\\1.txt");
			writer.write(args);
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
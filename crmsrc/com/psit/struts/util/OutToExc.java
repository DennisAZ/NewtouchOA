package com.psit.struts.util;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.funcs.person.data.YHPerson;

import com.psit.struts.entity.SalEmp;
import com.psit.struts.util.file.ZipUtils;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class OutToExc {
	private final String OUT_FOLDER = "outXls/";//存放导出临时文件目录
	private final int MAX_ROW = 50000;//单个excel文件最大行数(10000/sheet * 5)
	private String[] header;
	private String[][] datas;
	private HttpServletRequest request;
	
	public OutToExc(){
	}
	public OutToExc(String[] header, String[][] datas, HttpServletRequest request){
		this.header = header;
		this.datas = datas;
		this.request = request;
	}
	
	/**
	 * 导出xls文件下载 <br>
	 */
	public void downloadXls(HttpServletResponse response) {
		YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
		
		String seNo =String.valueOf(person.getSeqId());
		String fileName = seNo + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		FileOperator.createFolder(OUT_FOLDER, request);
		File xlsFile = outEntity(fileName);
		FileOperator.download(null, response, xlsFile.getPath(), xlsFile.getName());
		FileOperator.delFile(xlsFile.getPath(), null);
	}
	
	/**
	 * 导出实体成excel文件
	 * @param fileName 导出文件名（不包括后缀名）
	 * @return File 导出的文件对象
	 */
	private File outEntity(String fileName){
		File file = null;
		if(request!=null){
			String path = request.getSession().getServletContext().getRealPath("/") + OUT_FOLDER + fileName;
			try {
				if(datas.length > MAX_ROW){
					int fileCount;//生成的excel文件数
					int lastRows = datas.length%MAX_ROW;//最后一个excel文件的行数
					String tempZipFolder = OUT_FOLDER + fileName + "/";//excel文件临时文件夹
					File xlsFile = null;
					int startRow,endRow;
					String[][] splitDatas;
					
					file = new File(path + ".zip");//生成下载临时文件
					if(lastRows>0){
						fileCount = datas.length/MAX_ROW+1;
					}
					else{
						fileCount = datas.length/MAX_ROW;
					}
					//生成并压缩xls文件
					FileOperator.createFolder(tempZipFolder, request);
					for(int i=0; i<fileCount; i++){
						startRow = i*MAX_ROW;
						endRow = (i==(fileCount-1))?(i*MAX_ROW+lastRows):(i+1)*MAX_ROW;
						xlsFile = new File(request.getSession().getServletContext().getRealPath("/") + 
								tempZipFolder+fileName+"("+(startRow+1)+"-"+endRow+").xls");
						splitDatas = Arrays.copyOfRange(datas, startRow, endRow);
						createXls(xlsFile,header,splitDatas);
					}
					ZipUtils.zip(request.getSession().getServletContext().getRealPath("/") + tempZipFolder,file.getPath());
					FileOperator.delFile(tempZipFolder,request);
				}
				else{
					file = new File(path+".xls");//生成下载临时文件
					createXls(file,header,datas);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	private void createXls(File file,String [] header,String [][] datas) throws IOException, RowsExceededException, WriteException{
		WritableWorkbook wwb = Workbook.createWorkbook(file);
		//生成名为第一页的工作表,参数0表示这是第一页
		int num=1;
		WritableSheet sheet;
		sheet = wwb.createSheet("第"+num+"页", num);
		getHead(header,sheet);
		//m、n用于计数
		int m=0;
		int n=1;
		for(int i=0;i<datas.length;i++){
			for(int j=0;j<datas[i].length;j++){
				sheet.addCell(new Label(m++,n,datas[i][j]));
			}
			m = 0;
			if(n++ >9999){
				n=1;
				num++;
				sheet = wwb.createSheet("第"+num+"页", num);
				getHead(header,sheet);
			}
		}
		wwb.write();
		wwb.close();
	}
	/**
	 * 用于生成表头<br>
	 */
	private void getHead(String [] header,WritableSheet sheet){
		try{
			WritableFont wf = new WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,UnderlineStyle.NO_UNDERLINE);
			WritableCellFormat wcfF = new WritableCellFormat(wf);//定义单元格
	//		wcfF.setBackground(jxl.format.Colour.BLUE);//设置单元格的背景颜色
			wcfF.setAlignment(jxl.format.Alignment.CENTRE); //设置对齐方式
			for(int i=0;i<header.length;i++){
				sheet.addCell(new Label(i,0,header[i],wcfF));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 统计数据导出xls文件下载 <br>
	 * @param request	
	 * @param response
	 * @param header excel表头列名
	 * @param datas excel数据行
	 */
	public void downloadDataXls(HttpServletRequest request, HttpServletResponse response,
				String[][] header, String[][] datas) {
		String seNo = ((SalEmp)request.getSession().getAttribute("CUR_EMP")).getSeNo().toString();
		String fileCode = seNo + new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
		String folderPath = "outXls/";//存放临时文件目录
		String fileName = fileCode + ".xls";
		String filePath = folderPath + fileName;
		FileOperator.createFolder(folderPath, request);
		outStaData(header, datas, filePath, request);
		FileOperator.download(request, response, filePath, fileName);
		FileOperator.delFile(filePath, request);
	}
	/**导出实体成excel<br>
	 * @param header excele头
	 * @param datas 导出的数据
	 * @param path 临时文件路径
	 * @param HttpServletRequest,如绝对路径此处为null
	 */
	private void outStaData(String [][] header,String [][] datas, String path, HttpServletRequest request ){
		path = path.replace("/", "\\");
		if(request!=null){
			path = request.getSession().getServletContext().getRealPath("/") + path;
		}
		try {
			File file = new File(path);
			WritableWorkbook wwb = Workbook.createWorkbook(file);//生成一个名为"name.xls"的临时Excel文件
			//生成名为第一页的工作表,参数0表示这是第一页
			int num=1;
			WritableSheet sheet;
			sheet = wwb.createSheet("第"+num+"页", num);
			//生成表头
			try{
				jxl.write.WritableFont wf = new jxl.write.WritableFont(WritableFont.TIMES, 10, WritableFont.BOLD, false,UnderlineStyle.NO_UNDERLINE);
				jxl.write.WritableCellFormat wcfF = new jxl.write.WritableCellFormat(wf);//定义单元格
				wcfF.setAlignment(jxl.format.Alignment.CENTRE); //设置对齐方式
				for(int i=0;i<header.length;i++){
						for(int j=0;j<header[i].length;j++){
							if(i!=1 || j!=0){
								sheet.addCell(new Label(j,i,header[i][j],wcfF));
								if(i==0 && j==0){
									sheet.mergeCells(j, i, j, i+1);
								}else if(i== 0 && j!=0){
									sheet.mergeCells(j, i, j+1, i);
									j++;
								}
							}
						}
					}
				}catch(Exception e){
					e.printStackTrace();
			}
			//m、n用于计数
			int m=0;
			int n=2;
			for(int i=0;i<datas.length;i++){
				for(int j=0;j<datas[i].length;j++){
					sheet.addCell(new Label(m++,n,datas[i][j]));
				}
				m=0;
				n++;
			}
			wwb.write();
			wwb.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

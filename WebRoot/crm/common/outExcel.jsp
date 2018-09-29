<%@ page language="java" import="java.util.*,org.apache.poi.hssf.util.Region,org.apache.poi.hssf.usermodel.HSSFCellStyle,org.apache.poi.hssf.usermodel.HSSFWorkbook,org.apache.poi.hssf.usermodel.HSSFSheet,org.apache.poi.hssf.usermodel.HSSFRow,org.apache.poi.hssf.usermodel.HSSFCell,java.text.DecimalFormat"pageEncoding="UTF-8"%>


<%
    response.reset();
    //response.setContentType("application/vnd.ms-excel");
    response.setContentType("application/msexcel");
    response.setHeader("Content-disposition","attachment;filename=untitled.xls");//定义文件名
   // response.setHeader("Content-Disposition","attachment;filename="+new String("数据.xls".getBytes("UTF-8"),"UTF-8"));
    String result=request.getParameter("result");
    DecimalFormat f = new DecimalFormat("#,##0.0");
    HSSFWorkbook wb = new HSSFWorkbook();
    HSSFSheet sheet = wb.createSheet("sheet1");
    List list=new ArrayList();
    if(result.equals("countResult"))
     {
       list=(List)session.getAttribute("outCountList"); 
       //以下以写表头
        //表头为第一行
      HSSFRow row = sheet.createRow((short) 0);
//定义4列
        HSSFCell cell1 = row.createCell((short) 0);
        HSSFCell cell2 = row.createCell((short) 1);
        HSSFCell cell3 = row.createCell((short) 2);
        HSSFCell cell4 = row.createCell((short) 3);
        cell1.setEncoding((short)1);
        cell2.setEncoding((short)1);
        cell3.setEncoding((short)1);
        cell4.setEncoding((short)1);
 
//定义表头的内容
        cell1.setCellValue("企业名称");
        cell2.setCellValue("当月评审费用");
        cell3.setCellValue("当月监查1费用");
        cell4.setCellValue("当月监查2费用");
      for(int i=0;i<list.size();i++)
        {
//定义数据从第二行开始       
  row = sheet.createRow((short) i+1);
                cell1 = row.createCell((short) 0);
                cell2 = row.createCell((short) 1);
                cell3 = row.createCell((short) 2);
                cell4 = row.createCell((short) 3);
                
               cell1.setEncoding((short) 1);
               cell2.setEncoding((short) 1);
               cell2.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
               cell3.setEncoding((short) 1);
               cell3.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
               cell4.setEncoding((short) 1);
               cell3.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
              
              

//填充内容
        //Count countList=(Count)list.get(i);
        //cell1.setCellValue(countList.getSystemName());
        //cell2.setCellValue(f.format(countList.getSassCost()));
       // cell3.setCellValue(f.format(countList.getCheckCost1()));
        //cell4.setCellValue(f.format(countList.getCheckCost2()));
     
    }
    }

    wb.write(response.getOutputStream());
    response.getOutputStream().flush();
    response.getOutputStream().close();
%>
//package com.psit.struts.util;
//import java.awt.Color;
//import java.io.FileOutputStream;  
//import java.io.IOException;  
//import java.io.PrintWriter;
//import java.io.StringReader;
//import java.net.MalformedURLException;
//import java.util.Iterator;
//import java.util.List;
//import java.util.regex.Pattern;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.lowagie.text.Cell;  
//import com.lowagie.text.Document;  
//import com.lowagie.text.DocumentException;  
//import com.lowagie.text.Element;  
//import com.lowagie.text.Font;  
//import com.lowagie.text.Image;
//import com.lowagie.text.PageSize;  
//import com.lowagie.text.Paragraph;  
//import com.lowagie.text.Table;  
//import com.lowagie.text.rtf.RtfWriter2;
//import com.lowagie.text.html.simpleparser.HTMLWorker; 
//import com.lowagie.text.html.simpleparser.StyleSheet;
//
///**
// * ����Ϊword�ļ������� <br>
// * @author rabbit
// */
//public class OutToWord {
//	private Document document;
//	private Font titleFont;
//	private Font contextFont;
//	//�ļ�����
//	private String filePath;
//	private String fileName;
//	private HttpServletRequest request;
//	private HttpServletResponse response;
//	
//	/**
//	 * ֱ�����html��ʼ��
//	 * @param fileName
//	 * @param request
//	 * @param response
//	 */
//	public OutToWord(String fileName, HttpServletRequest request, HttpServletResponse response){
//		this.fileName = fileName;
//		fileName.equalsIgnoreCase(anotherString)
//    	this.request = request;
//    	this.response = response;
//	}
//	
//	public OutToWord(String titleStr,String filePath, String fileName,
//    		HttpServletRequest request, HttpServletResponse response) throws DocumentException, IOException{
//    	//��ʼ���ļ�����(����)
//    	this.filePath = filePath;
//    	this.fileName = fileName;
//    	this.request = request;
//    	this.response = response;
//		
//		String docPath = request.getSession().getServletContext().getRealPath("/").replace("\\", "/") + filePath;
//		//����ֽ�Ŵ�С
//		document = new Document(PageSize.A4);
//		RtfWriter2.getInstance(document,new FileOutputStream(docPath));
//		document.open();
//		//������������
////     	BaseFont bfChinese = BaseFont.createFont("STSongStd-Light","utf-8",BaseFont.NOT_EMBEDDED);
//     	//����������
//     	titleFont = new Font();
//     	titleFont.setSize(12);
//     	titleFont.setStyle(Font.BOLD);
//     	//����������
//     	contextFont = new Font();
//     	contextFont.setSize(10);
//     	contextFont.setStyle(Font.NORMAL);
////     	contextFont = new Font(bfChinese,10,Font.NORMAL);
//     	
//     	addDocTitle(titleStr);
//	}
//	public void addDocTitle(String titleStr) throws DocumentException{
//		Paragraph titleG = new Paragraph(titleStr);  
//	    //���ñ����ʽ���뷽ʽ  
//		titleG.setAlignment(Element.ALIGN_CENTER);  
//		titleG.setFont(titleFont);
//		document.add(titleG);
//	}
//	
//	
//    public void addDocContext(String contextString)throws DocumentException, IOException{  
//        Paragraph context = new Paragraph(contextString);  
//        context.setAlignment(Element.ALIGN_LEFT);  
//        context.setFont(contextFont);  
//        //�μ��  
//        context.setSpacingBefore(6);
//        //���õ�һ�пյ�����  
//        context.setFirstLineIndent(16);
//        document.add(context);  
//    }
//    
//    public void addImage(String imgPath) throws MalformedURLException, IOException, DocumentException{  
//    	Image png = Image.getInstance(imgPath);   
//        document.add(png); 
//    }
//    
//    public void addHTML(String html) throws IOException, DocumentException{
//    	Pattern patternRight = Pattern.compile("(<style.*type=.*>)(.*)(</style>)");
//		html = patternRight.matcher(html).replaceAll("");
//    	StringReader sr = new StringReader(html);
//    	List htmlList =  HTMLWorker.parseToList(sr, new StyleSheet());
//    	Iterator htmlIt = htmlList.iterator();
//    	while(htmlIt.hasNext()){
//    		Element e = (Element)htmlIt.next();
//    		if(e!=null){
//    			document.add(e); 
//    		}
//    	}
//    	sr.close();
//    }
//    
//    public void addDocTable(String tabHeader,String[][][] cells, int[] width) throws DocumentException{
//    	int colNum = width.length;
//    	if(cells!=null && cells.length>0){
//            Table table = new Table(colNum);  
//            table.setWidths(width);  
//            table.setWidth(95);//ռҳ���ȱ���  
//            table.setAlignment(Element.ALIGN_CENTER);//����  
////            table.setAlignment(Element.ALIGN_MIDDLE);//������  
//            table.setAutoFillEmptyCells(true);//�Զ�����  
//            table.setBorderWidth(1);//�߿��� 
//            if(tabHeader!=null){
//	            //���ñ�ͷ  
//	            Cell headerCell = new Cell(tabHeader);  
//	            headerCell.setBackgroundColor(Color.LIGHT_GRAY);
//	            headerCell.setHeader(true);  
//	            headerCell.setColspan(colNum);  
//	            table.addCell(headerCell);  
//	            table.endHeaders();
//            }
//            Cell cell = null;
//            Paragraph cellContext = null;
//            for(int i=0; i<cells.length; i++){
//            	for(int j=0; j<cells[i].length; j++){
//            		int cellAlign = Element.ALIGN_CENTER;
//            		if(cells[i][j][0]==null){
//            			cellContext = new Paragraph("");
//            		}
//            		else{
//            			cellContext = new Paragraph(cells[i][j][0]);
//            		}
//            		cellContext.setFont(contextFont);
//            		cell = new Cell(cellContext);
//            		if(cells[i][j].length>1){
//            			switch(cells[i][j][1].toCharArray()[0]){
//            			case 'c': cellAlign = Element.ALIGN_CENTER; break;
//            			case 'l': cellAlign = Element.ALIGN_LEFT; break;
//            			case 'r': cellAlign = Element.ALIGN_RIGHT; break;
//            			}
//            			if(cells[i][j].length>2){
//                			cell.setColspan(Integer.parseInt(cells[i][j][2]));
//                		}
//            			if(cells[i][j].length>3){
//            				cell.setRowspan(Integer.parseInt(cells[i][j][3]));
//            			}
//            		}
//            		cell.setHorizontalAlignment(cellAlign);  
//                    table.addCell(cell);
//            	}
//            }
//            document.add(table);  
//    	}
//    }
//    
//    public void closeDoc(){
//    	document.close();
//    }
//    
//    public void downloadWordFile(){
//    	closeDoc();//�ر��ļ�
//        try {
//        	String downloadPath = filePath.replace("/", "\\");
//			FileOperator.download(request, response, downloadPath, fileName);
//			FileOperator.delFile(downloadPath,request);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//    
//    public void newPage(){
//    	document.newPage();
//    }
//
//	public Document getDocument() {
//		return document;
//	}
//	
//	public void simpleDownloadWord(String content){
//		response.reset();
//		try {
//			fileName = fileName+".doc";
//			response.setHeader("content-disposition", "attachment;" + "filename=" + new String(fileName.getBytes(),"iso8859-1"));
//			response.setContentType("application/msword;charset=utf-8");
//			PrintWriter out = response.getWriter();
//			out.println(content);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//}
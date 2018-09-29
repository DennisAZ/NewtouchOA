package yh.subsys.oa.vmeet.act;

public class PdfTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		try {  

		      Runtime rt = Runtime.getRuntime();  

		       //rt.exec("D:\\java\\apache-tomcat-7.0.12\\webapps\\zlchat_jsp\\zlchat\\SWFTools\\pdf2swf.exe  "+cmd);  
		      rt.exec("D:\\SWFTools\\pdf2swf.exe d:\\temp\\a.pdf -o d:\\temp\\page%.swf -T 9 -i");
	      
		} catch (Exception ex) {  

	           System.out.println(ex.getMessage());  

	            ex.printStackTrace();  

		  } 
	       
	}

}

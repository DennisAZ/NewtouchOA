package yh.subsys.oa.vmeet.act;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.ne.so_net.ga2.no_ji.jcom.IDispatch;
import jp.ne.so_net.ga2.no_ji.jcom.ReleaseManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;


public class PPTUtil
{
	
	final static boolean isPosix = System.getProperty("os.name").toUpperCase()
	.indexOf("WINDOWS") == -1;

	final static String execExt = isPosix ? "" : ".exe"; 
	
	
	private static final Log log = LogFactory.getLog(PPTUtil.class);
	/**
	 * @param fileName 打开本地要转换的PPT文件的路径
	 * @param outputDir 输出目录
	 * @return
	 */
	public static int convert(String fileName, String outputDir,String fileNoExt,String swfToolDir)
	{
		int totalFrame = 0;
		try
		{
			/*
			String[] argv;
			Process process;
			process=Runtime.getRuntime().exec("mkdir "+outputDir);
			//process=Runtime.getRuntime().exec("gs -dSAFER -dBATCH -dNOPAUSE -r100 -dTextAlphaBits=4 -dGraphicsAlphaBits=4 -sDEVICE=jpeg -sOutputFile=/opt/%d.JPG /opt/zlchat.pdf");
			if(ext.equalsIgnoreCase("doc")||ext.equalsIgnoreCase("xls")||ext.equalsIgnoreCase("ppt")||ext.equalsIgnoreCase("pdf"))
			{
				if(ext.equalsIgnoreCase("doc")||ext.equalsIgnoreCase("xls")||ext.equalsIgnoreCase("ppt"))
				{
				    argv = new String[] {
					"java",
					"-jar", 
					"/opt/zlchat_linux/lib/jodconverter-cli.jar",
					fileName,
					outputDir+"/"+fileNoExt+"."+"pdf"};
					executeScript("convertToPdf",argv);
				}
				else if(ext.equalsIgnoreCase("pdf"))
				{
					argv = new String[] {
							"mv",
							fileName,
							outputDir+"/"+fileNoExt+"."+"pdf"};
					executeScript("convertToPdf",argv);
				}
			
			   argv=new String[]{
					"gs",
					"-dSAFER",
					"-dBATCH",
					"-dNOPAUSE",
					"-r100",
					"-dTextAlphaBits=4",
					"-dGraphicsAlphaBits=4",
					"-sDEVICE=jpeg",
					"-sOutputFile="+outputDir+"/"+"%d.JPG",
					outputDir+"/"+fileNoExt+"."+"pdf"
			     };
			    executeScript("convertToJPG",argv);
			}
			else
			{
				argv = new String[] {
						"cp",
						fileName,
						outputDir+"/"};
				executeScript("mv",argv);
				argv = new String[] {
						"convert",
						fileName,
						outputDir+"/1.JPG"};
				executeScript("convertToJPG",argv);
			}
			
			totalFrame = countFiles(outputDir);
			return totalFrame;
			*/
			
			//以下是调用swfTools转换pdf到swf文件
//			System.out.println(fileName);
//			System.out.println(outputDir);
//			System.out.println(swfToolDir);
//			System.out.println(fileNoExt);
			File dir=new File(outputDir);
			//创建目录
			if(dir.exists())
			{
				dir.delete();
			}
			else
			{
				dir.mkdir();
			}
			
			//转换
			
			String cmd=fileName+" -o "+outputDir + File.separator +"page%.swf -T 9 -i";
			 
			try {  

				      Runtime rt = Runtime.getRuntime();  
				      cmd=swfToolDir  + File.separator + "pdf2swf.exe   "+cmd;
				   //   System.out.println(cmd);
				      
				     //rt.exec("D:\\java\\apache-tomcat-7.0.12\\webapps\\zlchat_jsp\\zlchat\\SWFTools\\pdf2swf.exe  "+cmd);  
				     
				     rt.exec(cmd);
			       } catch (Exception ex) {  

			           System.out.println(ex.getMessage());  

			            ex.printStackTrace();  

				  }  


			
			return 2;
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());   
			return 0;
		}
		finally
		{
			
		}
	}
	
	public static void delFile(String fileName)
	{
		try
		{
			String[] command = new String[] { "/bin/sh", "-c", "rm -f " + fileName};
			Process process;
			process=Runtime.getRuntime().exec(command);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
	}
	public static int countFiles(String dir)
	{
		File imgDir=new File(dir);
		if(imgDir.exists()&&imgDir.isDirectory())
		{
			return (imgDir.list().length-1);
		}
		else
		{
			return 0;
		}
		
	}
	/**
	 * PPT文件改名,去掉前面的"幻灯片"等字
	 * @param oldName
	 * @return
	 */
	public static String rename(String oldName)
	{
		try
		{
			 String regExp = "((\\d+)\\.JPG)$";
			 Pattern p = Pattern.compile(regExp, Pattern.CASE_INSENSITIVE);
			 Matcher m = p.matcher(oldName);	 
			 boolean find=m.find();
			 int start = m.start();
			 String newName = oldName.substring(start);
			 return newName;			
		}
		catch (Exception e)
		{
			log.error("", e);
			return oldName.substring(3);
		}
		
	}
	
	
	static HashMap<String, Object> executeScript(String process, String[] argv) {
		HashMap<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("process", process);
		log.debug("process: " + process);
		log.debug("args: " + Arrays.toString(argv));
		
		try {
			Runtime rt = Runtime.getRuntime();
			returnMap.put("command", Arrays.toString(argv));
			
			//By using the process Builder we have access to modify the environment variables
			//that is handy to set variables to run it inside eclipse
			ProcessBuilder pb = new ProcessBuilder(argv);
			
			Map<String, String> env = pb.environment();
			
//			for (Iterator<String> iter = env.keySet().iterator();iter.hasNext();) {
//				String key = iter.next();
//				
//				System.out.println("key "+key+" value "+env.get(key));
//				//log.debug("key "+key);
//				
//			}

			Process proc = pb.start();

			//1-minute timeout for command execution
			long timeout = 240000;
			
			ErrorStreamWatcher errorWatcher = new ErrorStreamWatcher(proc);
			Worker worker = new Worker(proc);
			InputStreamWatcher inputWatcher = new InputStreamWatcher(proc);
			errorWatcher.start();
			worker.start();
			inputWatcher.start();
			try 
			{
				worker.join(timeout);
				if (worker.exit != null)
				{
					returnMap.put("exitValue", worker.exit);
					log.debug("exitVal: " + worker.exit);
					returnMap.put("error", errorWatcher.error);
				}
				else
				{
					returnMap.put("exception", "timeOut");
					returnMap.put("error", errorWatcher.error);
					returnMap.put("exitValue", -1);
				
					throw new TimeoutException();
				}
			} 
			catch(InterruptedException ex) 
			{				
			    worker.interrupt();
			    errorWatcher.interrupt();
			    inputWatcher.interrupt();
			    Thread.currentThread().interrupt();
			    throw ex;
			} 
			finally 
			{
			    proc.destroy();
			}			
		} 
		catch ( TimeoutException e)
		{			
			//Timeout exception is processed above
		}
		catch (Throwable t) 
		{
			//Any other exception is shown in debug window
			t.printStackTrace();
			returnMap.put("error", t.getMessage());
			returnMap.put("exitValue", -1);
		}
		return returnMap;
	}
	
	private static class Worker extends Thread 
	{
	  private final Process process;
	  private Integer exit;
	  
	  private Worker(Process process) 
	  {
		  this.process = process;
	  }
	
	  public void run() 
	  {
		  try 
		  { 
		      exit = process.waitFor();
		  } 
		  catch (InterruptedException ignore) 
		  {
		      return;
		  }
	  }  
	}
	
	//This one collects errors coming from script execution
	private static class ErrorStreamWatcher extends Thread 
	{
	  private String error;
	  private InputStream stderr;
	  private InputStreamReader isr;
	  private BufferedReader br;
	  
	  private ErrorStreamWatcher(Process process) 
	  {
		  error = "";
		  stderr = process.getErrorStream();
		  isr = new InputStreamReader(stderr);
		  br = new BufferedReader(isr);
	  }
	
	  public void run() 
	  {
		  try 
		  { 
			  String line = br.readLine();	
			  while (line != null) 
			  {
				  error += line;
				  log.debug("line: " + line);
				  line = br.readLine();
			  }
		  }
		  catch (IOException ioexception)
		  {
			  return;
		  }
	  }
	}
	
	//This one just reads script's output stream so it can 
	//finish normally, see issue 801
	private static class InputStreamWatcher extends Thread 
	{
	  private InputStream stderr;
	  private InputStreamReader isr;
	  private BufferedReader br;
	  
	  private InputStreamWatcher(Process process) 
	  {
		  stderr = process.getInputStream();
		  isr = new InputStreamReader(stderr);
		  br = new BufferedReader(isr);
	  }
	
	  public void run() 
	  {
		  try 
		  { 
			  String line = br.readLine();	
			  while (line != null) 
			  {
				  line = br.readLine();
			  }
		  }
		  catch (IOException ioexception)
		  {
			  return;
		  }
	  }
	}
		
}

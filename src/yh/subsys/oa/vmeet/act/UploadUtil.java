package yh.subsys.oa.vmeet.act;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 处理文件上传功能
 * BuildTime:  7:23:16 PM, Dec 11, 2008
 * @author liyu
 */
public class UploadUtil
{
	protected static Log log = LogFactory.getLog(UploadUtil.class);
	
	public static int MAX_UPLOAD_SIZE = 8 * 1024 * 1024;
	public static String regExp = "\\.(exe|bat|cgi|com|sh|jsp|php|asp|class|dll)$";
	public static String FILEINFO_NEWNAME = "uploadFileInfoNewFileName";
	public static String FILEINFO_OLDNAME = "uploadFileInfoOldName";
	public static String FILEINFO_SIZE = "uploadFileInfoSize";
	public static String FILEINFO_CREATEDTIME="uploadFileInfoTime";
	
	/**
	 * @param request
	 * @param uploadDir 上传到的目录
	 * @param newFilename 是否保留原文件名,null不保留原名
	 * @return
	 */
	@SuppressWarnings( { "unchecked", "unused" })
	public static HashMap uploadFile(HttpServletRequest request,
			String uploadDir, String newFilename)
	{
		HashMap fileInfo = new HashMap();
		try
		{
			request.setCharacterEncoding("GBK");
			if (!uploadDir.endsWith(File.separator))
				uploadDir += File.separatorChar;
			// 文件上傳部分
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			
			if (isMultipart)
			{
				
				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);
				upload.setHeaderEncoding("UTF-8");
				//upload.setFileSizeMax(MAX_UPLOAD_SIZE);
				upload.setSizeMax(MAX_UPLOAD_SIZE);
				
				// 开始读取上传信息
			List<FileItem> fileItems = upload.parseRequest(request);
				
				// 依次处理每个上传的文件
				Iterator iter = fileItems.iterator();
				while (iter.hasNext())
				{
					FileItem item = (FileItem) iter.next();
					// 忽略其他不是文件域的所有表单信息
					if (!item.isFormField())
					{						
						String name = item.getName(); // 获得文件名及路径(带路径)
						
						if(name.trim().equals(""))
							continue;
											
						long size = item.getSize(); // 文件大小
						if ((name == null || name.equals("")) && size == 0)
							continue;						
						String oldFileName = checkFileName(name); // 文件名(没有后缀)
						
						fileInfo.put(FILEINFO_SIZE, new Integer((int) size));
						fileInfo.put(FILEINFO_OLDNAME, oldFileName);
						
						if (oldFileName != null)
						{
							try
							{
								if (newFilename == null) // 不保留原名
								{
									String fileName = String.valueOf(System
											.currentTimeMillis()); // 新文件名
									
									// 后缀
									int idx = oldFileName.lastIndexOf(".");
									String subfix = (idx > 0 ? oldFileName
											.substring(idx) : "unknown");
									
									String newFileName = fileName + subfix;
									
							     File file = new File(uploadDir);
	                  if (!file.exists()) {
	                    file.mkdirs();
	                  }
									
									String savePath = uploadDir + newFileName;
									item.write(new File(savePath));
									log.debug(oldFileName + "---" + size + "---"
											+ savePath);
									
									fileInfo.put(FILEINFO_NEWNAME, newFileName);
								}
								else
								{
									item
											.write(new File(uploadDir
													+ newFilename));
									fileInfo.put(FILEINFO_NEWNAME, newFilename);
								}
								fileInfo.put(FILEINFO_CREATEDTIME, new Date());
								return fileInfo;
							}
							catch (Exception e)
							{
								log.error("上传文件失败:写入异常", e);
								e.printStackTrace();
								return null;
							}
						}
						else
						{
							throw new IOException("上传文件失败:路径格式错误");
						}
					}
					else
					{ // 如果item是正常的表单域
						String name = item.getFieldName();
						String value = item.getString("UTF-8");
						log.debug("表单域名为:" + name + "表单域值为:" + value);						
					}
				}
				return null;
			}
			else
			{
				return null;
			}
		}
		catch (IOException e)
		{
			log.error("上传文件失败", e);
			e.printStackTrace();
			return null;
		}
		catch (FileUploadException e)
		{
			log.error("上传文件失败", e);
			e.printStackTrace();
			return null;
		}
	}
	
	public static String checkFileName(String name) throws IOException
	{
		if (name == null || name.trim().length() == 0)
			return null;
		
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(name);		
		
		if (m.find())
		{
			throw new IOException(name + "为不允许上传的文件类型");
		}
		else
		{
			if (name.indexOf(File.separator) > 0)
			{
				name = name.substring(name.lastIndexOf(File.separator) + 1);
			}
			return name;
		}		
	}
	
	
	
}

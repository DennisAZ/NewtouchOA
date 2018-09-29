package yh.core.funcs.workflow.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.workflow.data.YHFlowRun;
import yh.core.funcs.workflow.data.YHFlowType;
import yh.core.global.YHSysProps;
import yh.core.util.YHGuid;


public class YHFlowUtil {

	/**
	 * add by add 张银友
	 * 从workflow取出内容 
	 * @param user
	 * @param sRunId
	 * @param sFlowId
	 * @param dbConn
	 * @param imgPath
	 * @return
	 * @throws Exception
	 */
	public String getContentFromFlow(YHPerson user,String sRunId,String sFlowId,Connection dbConn,String imgPath) throws Exception{
		YHFlowRunLogic frl = new YHFlowRunLogic();
		YHFlowRun flowRun = frl.getFlowRunByRunId(Integer.parseInt(sRunId),dbConn);
		YHFlowTypeLogic ftl = new YHFlowTypeLogic();
		YHFlowType ft = ftl.getFlowTypeById(Integer.parseInt(sFlowId), dbConn);
		Map result = frl.getPrintForm(user, flowRun, ft, true, dbConn, imgPath);
		String form = (String) result.get("form");
		form = form.replaceAll("\\\\\"", "\"");
		YHFeedbackLogic feedbackLogic = new YHFeedbackLogic();
		String feedbacks = feedbackLogic.getFeedbacksHtml(user,flowRun.getFlowId(), flowRun.getRunId(), dbConn);
		StringBuffer sb=new StringBuffer();
		sb.append("<div id=\"form\" style=\"margin-top:5px;margic-bottom:5px\">");
		sb.append(form);
		sb.append("</div>");
		if("".equals(feedbacks) || feedbacks==null){
			
		}else{
			sb.append("<div id='feedBack'><table width='100%'><tbody id='feedbackList'>")
			.append(feedbacks)
			.append("</tbody></table></div>");
		}
		return sb.toString();
		
	}
	
	/**
	 * add by zyy 张银友
	 * 复制工作流附件 
	 * @param attachName
	 * @param attachId
	 * @throws IOException
	 */
	public void copyAttachFlowToWhere(String attachName,String attachId,String where) throws IOException{
		String basePath=YHSysProps.getAttachPath();
		String[] ids=attachId.substring(0, attachId.length()-1).split(",");
		String[] names=attachName.substring(0, attachName.length()-1).split("\\*");
		for(int i=0;i<ids.length;i++){
			String floder=ids[i].substring(0, 4);
			String fileName=ids[i].substring(5)+"_"+names[i];
			String srcFile=basePath+"\\workflow\\"+floder+"\\"+fileName;
			String destFile=basePath+"\\"+where+"\\"+floder+"\\"+fileName;
			FileUtils.copyFile(new File(srcFile), new File(destFile));
		}
		 
	}
	
	
	/**
	 * 生成HMTL
	 * @param str   内容
	 * @param title  标题
	 * @throws IOException
	 */
	public String writeHtmlFile(String str,String title) throws IOException{
		String path=YHSysProps.getAttachPath()+File.separator+"workflow"+File.separator;
		Calendar c=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyMM");
		path+=sdf.format(c.getTime());
		SimpleDateFormat sdf2=new SimpleDateFormat("HHmmss");
		title=title.replace(":", "")+sdf2.format(c.getTime());
		path=path+File.separator+title+".html";
		System.out.println(path);
		FileUtils.writeStringToFile(new File(path), str);
		return path;
	}
	
	/**
	 * 将附件和表单正文进行压缩
	 * @param files
	 * @param zipName
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException 
	 */
	public  Map<String,String> zip(List<String> files,String zipName) throws IOException, NoSuchAlgorithmException {
		Map<String,String> map=new HashMap<String, String>();
		String path=YHSysProps.getAttachPath()+File.separator+"workflow"+File.separator;
		SimpleDateFormat sdf=new SimpleDateFormat("yyMM");
		SimpleDateFormat sdf2=new SimpleDateFormat("HHmmss");
		Calendar c=Calendar.getInstance();
		String floder=sdf.format(c.getTime());
		String str1=YHGuid.getRawGuid();//产生随机数
//		String str2="test"+sdf2.format(c.getTime())+".zip";
		String str2=zipName.replace(":", "")+sdf2.format(c.getTime())+".zip";
		map.put("attid", floder+"_"+str1);
		map.put("attname", str2);
		path=path+floder+File.separator+str1+"_"+str2;
		ZipOutputStream zipOutput = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(path)));
		byte data[] = new byte[1024];
		for(String file:files){
			File f=new File(file);
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			ZipEntry zipEntry = new ZipEntry(f.getName());
			zipOutput.putNextEntry(zipEntry);
			int len = 0;
			while ((len = bis.read(data)) != -1) {
				zipOutput.write(data, 0, len);
			}
			zipOutput.closeEntry();
			bis.close();
		}
		zipOutput.close();
		return map;
	}
	
	
	/**
	 * 生成html当附件，到指定目录下
	 * @param str
	 * @param title
	 * @param where
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException 
	 */
	public Map<String,Object> writeHtmlFile(String str,String title,String where) throws IOException, NoSuchAlgorithmException{
		Map<String,Object> map=new HashMap<String, Object>();
		String attName=title.replace(":", "")+".html";
		map.put("attName", attName);
		String path=YHSysProps.getAttachPath()+File.separator+where+File.separator;
		String str1=YHGuid.getRawGuid();//产生随机数
		Calendar c=Calendar.getInstance();
		SimpleDateFormat sdf=new SimpleDateFormat("yyMM");
		String floder=sdf.format(c.getTime());
		map.put("attId", floder+"_"+str1);
		path=path+floder+File.separator+str1+"_"+attName;
		System.out.println(path);
		FileUtils.writeStringToFile(new File(path), str);
		return map;
	}
	
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		String name="[{attachmentName:\"091027104590d9b261b3d9fc3f.jpg\",attachmentId:'1211_800bfb8afc19a179b72811b6ac7a37fb',ext:'jpg',priv:'1,0'},{attachmentName:\"0910271045250d394e818d4e0b.jpg\",attachmentId:'1211_a0e9cd64d55dad9b6b42f33238c203dc',ext:'jpg',priv:'1,0'},{attachmentName:\"3370710_1269964195908_1024x1024.jpg\",attachmentId:'1211_20ee86f537a300b750ae5200c08988cc',ext:'jpg',priv:'1,0'}]";
		ObjectMapper mapper=new ObjectMapper();
		List<Map<String, String>> list =mapper.readValue(name, List.class);
	}
}

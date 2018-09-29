package oa.spring.service;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.fileupload.FileItem;

import oa.spring.util.ContractCont;

public class FileUploadService {
	/**
	 * 附件批量上传页面处理
	 * 
	 * @return
	 * @throws Exception
	 */
	public StringBuffer uploadMsrg2Json(List items) throws Exception {

		Map<String, String> attr = null;
		long size = 0;
		if (null != items) {
			Iterator itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (item.isFormField()) {
					continue;
				} else {
					attr = new HashMap<String, String>();
					size += item.getSize();

					Calendar cld = Calendar.getInstance();
					int year = cld.get(Calendar.YEAR) % 100;
					int month = cld.get(Calendar.MONTH) + 1;
					String mon = month >= 10 ? month + "" : "0" + month;
					String hard = year + mon;

					String filePath = ContractCont.UPLOAD_HOME + File.separator
							+ ContractCont.MODULE + File.separator + hard
							+ File.separator;
					File dir = new File(filePath);
					if (!dir.exists()) {
						dir.mkdirs();
					}

					String fileName = item.getName();// 用户上传的文件名
					String rand = UUID.randomUUID().toString();
					String trueFileName = rand
							+ fileName.substring(fileName.lastIndexOf("."));// 对保存在服务器端的文件进行重命名，防止重复
					File file = new File(filePath + trueFileName);
					item.write(file);// 拷贝文件

					attr.put(hard + "_" + rand, fileName);
				}
			}
		}

		StringBuffer sb = new StringBuffer();
		String attachmentId = "";
		String attachmentName = "";
		try {
			Set<String> attrKeys = attr.keySet();
			for (String key : attrKeys) {
				String fileName = attr.get(key);
				attachmentId += key;
				attachmentName += fileName;
			}
			sb.append("{");
			sb.append("'attachmentId':").append("\"").append(attachmentId)
					.append("\",");
			sb.append("'attachmentName':").append("\"").append(attachmentName)
					.append("\",");
			sb.append("'size':").append("").append(size);
			sb.append("}");
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return sb;
	}

	public Map<String, String> getAttachHeard(String fileName, String directView) {
		HashMap<String, String> result = new HashMap<String, String>();
		String contentTypeDesc = "";
		Integer contentType = 0;
		String extName = "";
		int extNameIndex = fileName.lastIndexOf(".");
		if (extNameIndex > 0) {
			extName = fileName.substring(extNameIndex).toLowerCase();
		}
		int arrayIndex = -1;
		String[] extFileArray = { ".jpg", ".jpeg", ".bmp", ".gif", ".png",
				".html", ".htm", ".wmv", ".wav", ".mid", ".mht", ".pdf", ".swf" };
		String[] extDocArray = { ".doc", ".dot", ".xls", ".xlc", ".xll",
				".xlm", ".xlw", ".csv", ".ppt", ".pot", ".pps", ".ppz",
				".docx", ".dotx", ".xlsx", ".xltx", ".pptx", ".potx", ".ppsx",
				".rm", ".rmvb" };

		if (directView != null && "1".equals(directView.trim())) {
			arrayIndex = getIndex(extFileArray, extName);
			switch (arrayIndex) {
			case 0:
			case 1:
				contentType = 1;
				contentTypeDesc = "image/jpeg";
				break;
			case 2:
				contentType = 1;
				contentTypeDesc = "image/bmp";
				break;
			case 3:
				contentType = 1;
				contentTypeDesc = "image/gif";
				break;
			case 4:
				contentType = 1;
				contentTypeDesc = "image/png";
				break;
			case 5:
			case 6:
				contentType = 1;
				contentTypeDesc = "text/html";
				break;
			case 7:
			case 8:
			case 9:
			case 10:
				contentType = 1;
				contentTypeDesc = "application/octet-stream";
				break;
			case 11:
				contentType = 1;
				contentTypeDesc = "application/pdf";
				break;
			case 12:
				contentType = 1;
				contentTypeDesc = "application/x-shockwave-flash";
				break;
			default:
				contentType = 0;
				contentTypeDesc = "application/octet-stream";
				break;
			}
		} else {
			arrayIndex = getIndex(extDocArray, extName);
			switch (arrayIndex) {
			case 0:
			case 1:
				contentType = 1;
				contentTypeDesc = "application/octet-stream";
				break;
			case 2:
				contentType = 0;
				contentTypeDesc = "application/octet-stream";
				break;
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
				contentType = 1;
				contentTypeDesc = "application/octet-stream";
				break;
			case 8:
			case 9:
			case 10:
			case 11:
				contentType = 0;
				contentTypeDesc = "application/octet-stream";
				break;
			case 12:
			case 13:
			case 14:
			case 15:
			case 16:
			case 17:
			case 18:
				contentType = 1;
				contentTypeDesc = "application/octet-stream";
				break;
			case 19:
			case 20:
				contentType = 1;
				contentTypeDesc = "audio/x-pn-realaudio";
				break;
			default:
				contentType = 0;
				contentTypeDesc = "application/octet-stream";
				break;
			}
		}
		result.put("contentTypeDesc", contentTypeDesc);
		result.put("contentType", contentType.toString());
		return result;
	}

	private int getIndex(String[] array, String indexStr) {
		int result = -1;
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i].equals(indexStr.trim())) {
					result = i;
				}
			}
		}
		return result;
	}
}

package yh.rad.docs.act;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import yh.core.global.YHActionKeys;
import yh.core.servlet.YHServletUtility;
import yh.core.util.file.YHFileUtility;

public class YHCodeTrnsAct {
  public String trnsCode(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
	
	response.setCharacterEncoding("UTF-8");
	String ctxPath = (String)request.getAttribute(YHActionKeys.ACT_CTX_PATH);
	String page = request.getParameter("page");
	String filePath = ctxPath + page.replace("/", "\\");
	List<String> contentList = new ArrayList<String>();
	YHFileUtility.loadLine2Array(filePath, contentList);
	PrintWriter writer = response.getWriter();
	List rtList = new ArrayList();
	for (String lineStr : contentList) {
		//lineStr = lineStr.trim();
		if (lineStr.length() < 1) {
			continue;
		}
		lineStr = lineStr.replaceAll("<", "&lt").replaceAll(">", "&gt").replaceAll("\"", "&quot").replaceAll(" ", "&nbsp;&nbsp;");
		writer.write(lineStr);
		writer.write("<br>\r\n");
	}
	writer.flush();
	writer.close();
	return null;
  }
}

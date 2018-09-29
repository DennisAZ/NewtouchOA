package yh.pda.fileFolder.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.person.data.YHPerson;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.pda.fileFolder.data.YHPdaFileFolder;

public class YHPdaFileFolderAct {

  public void search(HttpServletRequest request, HttpServletResponse response) throws Exception{
    Connection dbConn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;
    try{
      int pageSize = Integer.parseInt(request.getParameter("pageSize") == "" || request.getParameter("pageSize") == null ? "5" : request.getParameter("pageSize"));
      int thisPage = Integer.parseInt(request.getParameter("thisPage") == "" || request.getParameter("thisPage") == null ? "1" : request.getParameter("thisPage"));
      //int totalPage = Integer.parseInt(request.getParameter("totalPage") == "" || request.getParameter("totalPage") == null ? "1" : request.getParameter("totalPage"));
      
      YHPerson person = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      dbConn = requestDbConn.getSysDbConn();
      
      String sql = " SELECT SEQ_ID, SUBJECT, SEND_TIME, ATTACHMENT_ID, ATTACHMENT_NAME, CONTENT "
                 + " from oa_file_content "
                 + " where SORT_ID=0 and USER_ID='"+person.getSeqId()+"' "
                 + " order by CONTENT_NO,SEND_TIME desc ";
      
      List<YHPdaFileFolder> list = new ArrayList<YHPdaFileFolder>();
      ps = dbConn.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      rs = ps.executeQuery();
      rs.last();
      int totalSize = rs.getRow();
      if (totalSize == 0) {
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("thisPage", 0);
        request.setAttribute("totalPage", 0);
        request.setAttribute("fileFolders", list);
        request.getRequestDispatcher("/pda/fileFolder/index.jsp").forward(request, response);
        return;
      }
      rs.absolute((thisPage-1) * pageSize + 1);
      int count = 0;
      while(!rs.isAfterLast()) {
        if(count >= pageSize)
          break;
        YHPdaFileFolder fileFolder = new YHPdaFileFolder();
        fileFolder.setSeqId(rs.getInt("SEQ_ID"));
        fileFolder.setSubject(rs.getString("SUBJECT"));
        fileFolder.setSendTime(rs.getTimestamp("SEND_TIME"));
        fileFolder.setAttachmentId(rs.getString("ATTACHMENT_ID"));
        fileFolder.setAttachmentName(rs.getString("ATTACHMENT_NAME"));
        String content = rs.getString("CONTENT");
        content = content == null ? "" : YHUtility.cutHtml(content);
        fileFolder.setContent(content);
        list.add(fileFolder);
        rs.next();
        count++;
      }
      request.setAttribute("pageSize", pageSize);
      request.setAttribute("thisPage", thisPage);
      request.setAttribute("totalPage", totalSize/pageSize + (totalSize%pageSize == 0 ? 0 : 1));
      request.setAttribute("fileFolders", list);
    }
    catch(Exception ex){
      ex.printStackTrace();
    }
    finally{
      YHDBUtility.close(ps, rs, null);
    }
    request.getRequestDispatcher("/pda/fileFolder/index.jsp").forward(request, response);
    return;
  }
}

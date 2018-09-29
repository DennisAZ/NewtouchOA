package yh.core.funcs.email.act;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import yh.core.data.YHRequestDbConn;
import yh.core.funcs.dept.logic.YHDeptLogic;
import yh.core.funcs.email.data.YHEmailBox;
import yh.core.funcs.email.logic.YHEmailBoxLogic;
import yh.core.funcs.person.data.YHPerson;
import yh.core.funcs.person.data.YHUserPriv;
import yh.core.funcs.person.logic.YHUserPrivLogic;
import yh.core.funcs.workflow.util.YHWorkFlowUtility;
import yh.core.global.YHActionKeys;
import yh.core.global.YHBeanKeys;
import yh.core.global.YHConst;
import yh.core.util.YHUtility;
import yh.core.util.db.YHDBUtility;
import yh.core.util.db.YHORM;
import yh.core.util.form.YHFOM;

public class YHEmailNameAct{
  public String getName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    int userId = person.getSeqId();
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      PreparedStatement ps = null;
      ResultSet rs = null;
      String  isUse = "1";
      String name = "";
      int seqId = 0;
      try {
        ps = dbConn.prepareStatement("select * from oa_email_name where USER_ID = '" + userId + "'");
        rs = ps.executeQuery();
        if(rs.next()){
           name =  YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("NAME")));
           isUse = rs.getString( "IS_USE");
          if (isUse ==null) {
            isUse = "1";
          }
          seqId = rs.getInt("SEQ_ID");
        }
      } catch (Exception e) {
        throw e;
      } finally{
        YHDBUtility.close(ps, rs, null);
      }
      String str =  "{name:\""+ name +"\" , isUse:\""+ isUse +"\" , nameId :\""+ seqId +"\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  public String getName2(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
    int userId = person.getSeqId();
    try{
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      PreparedStatement ps = null;
      ResultSet rs = null;
      String  isUse = "1";
      String name = "";
      int seqId = 0;
      try {
        ps = dbConn.prepareStatement("select * from oa_email_name where USER_ID = '" + userId + "'");
        rs = ps.executeQuery();
        if(rs.next()){
           name =  YHUtility.encodeSpecial(YHUtility.null2Empty(rs.getString("NAME")));
           isUse = rs.getString( "IS_USE");
          if (isUse ==null) {
            isUse = "1";
          }
          seqId = rs.getInt("SEQ_ID");
        }
      } catch (Exception e) {
        throw e;
      } finally{
        YHDBUtility.close(ps, rs, null);
      }
      name = this.setName(dbConn, name, person);
      String str =  "{name:\""+ name +"\" , isUse:\""+ isUse +"\" , nameId :\""+ seqId +"\"}";
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_DATA, str);
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  public String saveName(HttpServletRequest request,
      HttpServletResponse response) throws Exception {
    try{
      YHPerson person = (YHPerson)request.getSession().getAttribute("LOGIN_USER");
      int userId = person.getSeqId();
      YHRequestDbConn requestDbConn = (YHRequestDbConn) request.getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);
      Connection dbConn = requestDbConn.getSysDbConn();
      PreparedStatement ps = null;
      ResultSet rs = null;
      String  isUse = YHUtility.null2Empty(request.getParameter("IS_USE"));
      if ("".equals(isUse)) {
        isUse = "0";
      }
      String name =YHUtility.null2Empty(request.getParameter("NAME")) ;
      String nameId = request.getParameter("NAME_ID");
      if (!"0".equals(nameId) 
          && !"".equals(nameId)) {
        try {
          ps = dbConn.prepareStatement("update oa_email_name set NAME = ? , IS_USE = ?  where SEQ_ID = '" + nameId + "'");
          ps.setString(1, name);
          ps.setString(2, isUse);
          ps.executeUpdate();
        } catch (Exception e) {
          throw e;
        } finally{
          YHDBUtility.close(ps, rs, null);
        }
      } else {
        try {
          ps = dbConn.prepareStatement("insert into oa_email_name (USER_ID ,NAME , IS_USE) values (? , ? , ?)");
          ps.setInt(1, userId);
          ps.setString(2, name);
          ps.setString(3, isUse);
          ps.executeUpdate();
        } catch (Exception e) {
          throw e;
        } finally{
          YHDBUtility.close(ps, rs, null);
        }
      }
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存成功！");
    } catch(Exception e){
      request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);
      request.setAttribute(YHActionKeys.RET_MSRG, "保存失败: " + e.getMessage());
    }
    return "/core/inc/rtjson.jsp";
  }
  public String setName(Connection conn , String str ,YHPerson loginUser ) throws Exception {
    YHUserPrivLogic userPrivLogic = new YHUserPrivLogic();
    YHDeptLogic dept = new YHDeptLogic();
    int deptId = loginUser.getDeptId();
    String deptName = dept.getNameById(deptId, conn);
    StringBuffer sb = new StringBuffer();
    dept.getDeptNameLong(conn, deptId, sb);
    String longName = sb.toString();
    if (longName.endsWith("/")) {
      longName = longName.substring(0, longName.length() - 1);
    }
    YHUserPriv role = userPrivLogic.getRoleById(Integer.parseInt(loginUser.getUserPriv()) , conn);
    SimpleDateFormat df = new  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();
    String dateStr = df.format(date);
    String[] aDate = dateStr.split(" ");

    String curYear = aDate[0].split("-")[0];
    String curMon =  aDate[0].split("-")[1];
    String curDay =  aDate[0].split("-")[2];
    String curHour = aDate[1].split(":")[0];
    String curMinite = aDate[1].split(":")[1];
    String curSecond = aDate[1].split(":")[2];
    
    str = str.replaceAll("\\{Y\\}", curYear);
    str = str.replaceAll("\\{M\\}", curMon);
    str = str.replaceAll("\\{D\\}", curDay);
    str = str.replaceAll("\\{H\\}", curHour);
    str = str.replaceAll("\\{I\\}", curMinite);
    str = str.replaceAll("\\{S\\}", curSecond);
    str = str.replaceAll("\\{U\\}", loginUser.getUserName());
    str = str.replaceAll("\\{SD\\}", deptName);
    str = str.replaceAll("\\{R\\}", role.getPrivName());
    str = str.replaceAll("\\{LD\\}", longName);
    return str;
  }
}

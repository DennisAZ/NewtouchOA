<%@ page language="java" import="java.util.*"
 contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@ page import="java.lang.*"%>
<%@page import="yh.subsys.oa.asset.data.YHCpcptl"%>
<HTML>
<HEAD>
<META http-equiv="Content-Style-Type" content="text/css">
</HEAD>
<BODY>
<br />
<br />
<br />
<%
  try {
    response.setContentType("Application/msexcel");
    response.setHeader("Content-Disposition",
        "attachment;filename=asset.xls");
    StringBuffer cont = new StringBuffer("");
    List<YHCpcptl> list = (List<YHCpcptl>) request.getAttribute("listOut");
    YHCpcptl cp = new YHCpcptl();
    cont.append("<html><head><META content=\'text/html; charset=UTF-8\'  http-equiv=Content-Type></head><body>\r\n");
    cont.append("<table width='100%' border='1'>\r\n");
    cont.append("<tr>\r\n");
    cont.append("<td height='19'>编号</td>\r\n");
    cont.append("<td height='19'>固定资产名称</td>\r\n");
    cont.append("<td height='19'>规格型号</td>\r\n");
    cont.append("<td height='19'>数量 </td>\r\n");
    cont.append("<td height='19'>使用地点</td>\r\n");
    cont.append("<td height='19'>使用人签字</td>\r\n");
    cont.append("<td height='19'>专管员签字</td>\r\n");
    cont.append("<td height='19'>备注</td>\r\n");
    cont.append("</tr>\r\n");

    for (int i = 0; i < list.size(); i++) {
      cp = list.get(i);
      cont.append("<tr>\r\n");
      cont.append("<td>" + cp.getCptlNo() + "&nbsp;</td>\r\n");
      cont.append("<td>" + cp.getCptlName() + "&nbsp;</td>\r\n");
      cont.append("<td>" + cp.getCptlSpec() + "&nbsp;</td>\r\n");
      cont.append("<td>" + cp.getCptlQty() + "&nbsp;</td>\r\n");
      if (!YHUtility.isNullorEmpty(cp.getUseDept())) {
        cont.append("<td>" + cp.getUseDept() + "&nbsp;</td>\r\n");
      }
      if (YHUtility.isNullorEmpty(cp.getUseDept())) {
        cont.append("<td>&nbsp;</td>\r\n");
      }
      if (!YHUtility.isNullorEmpty(cp.getCpreUser())) {
        cont.append("<td>" + cp.getCpreUser() + "&nbsp;</td>\r\n");
      }
      if (YHUtility.isNullorEmpty(cp.getCpreUser())) {
        cont.append("<td>&nbsp;</td>\r\n");
      }
      if (!YHUtility.isNullorEmpty(cp.getKeeper())) {
        cont.append("<td>" + cp.getKeeper() + "&nbsp;</td>\r\n");
      }
      if (YHUtility.isNullorEmpty(cp.getKeeper())) {
        cont.append("<td>&nbsp;</td>\r\n");
      }
      if (cp.getCpreMemo() != null) {
        cont.append("<td>" + cp.getCpreMemo() + "&nbsp;</td>\r\n");
      }
      if (cp.getCpreMemo() == null) {
        cont.append("<td>&nbsp;</td>\r\n");
      }
      cont.append("</tr>\r\n");
    }
    cont.append("</table>\r\n");
    cont.append("</body></html>\r\n");
    response.getWriter().println(cont.toString());
    response.getWriter().close();
  } catch (Exception e) {
    out.println(e.toString());
  }
%>
</BODY>
</HTML>

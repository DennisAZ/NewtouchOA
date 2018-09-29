<%@ page language="java" import="java.util.*,yh.core.funcs.person.data.YHPerson" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  List<Map<String,String>> calendarList = (List<Map<String,String>>)request.getAttribute("calendarList");
  YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
  String curDateStr = YHUtility.getCurDateTimeStr();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>日程安排</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
</head>
<STYLE type=text/css>.HdrPaddingTop {
	PADDING-TOP: 5px
}
.HdrPaddingBttm {
	PADDING-BOTTOM: 5px
}
BODY {
	MARGIN-TOP: 2px; MARGIN-LEFT: 0px; COLOR: #000000; MARGIN-RIGHT: 0px
}
A {
	FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
BODY {
	FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TABLE {
	FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TD {
	FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TR {
	FONT-SIZE: 9pt; FONT-FAMILY: 宋体,MS SONG,SimSun,tahoma,sans-serif
}
TABLE {
	BORDER-RIGHT: 0px; BORDER-TOP: 0px; BORDER-LEFT: 0px; BORDER-BOTTOM: 0px
}
.FF {
	COLOR: #000000
}
</STYLE>

<body>
<P>
<TABLE cellSpacing=0 cellPadding=0 width=755 align=center border=0>
  <TBODY>
  <TR>
    <TD style="BORDER-BOTTOM: #000099 1px solid" colSpan="2"></TD>
  </TR>
  <TR>
    <TD noWrap><FONT class=FF><%=user.getUserName() %>&lt;<%=user.getUserId() %>&gt;</FONT></TD>
    <TD noWrap align=right><FONT class=FF>已打印：<%=curDateStr %>
  </FONT></TD></TR></TR>
  <TR>
    <TD colSpan=2></TD>
  </TR>
  <TR>
    <TD colSpan=2>
      <HR color=#808080 SIZE=3>
    </TD>
  </TR>
  </TBODY>
</TABLE>
<br>
 <%
   if(calendarList!=null){
     if(calendarList.size()>0){
 
       %>
  <table cellSpacing=0 cellPadding=0 width=640 align=center border=0>
  <tbody>
   <tr>
     <td class=HdrPaddingBttm width="100%">
   <%      
  for(int i=0; i<calendarList.size();i++){
    Map<String,String> map = new HashMap<String,String>();
    map = calendarList.get(i);
    String calTime = map.get("calTime");
    String endTime = map.get("endTime");
    String calTimeDate = "0000-00-00";
    String calTimeStr = "00-00";
    String endTimeDate = "0000-00-00";
    String endTimeStr = "00-00";
    String temp = "1";
    if(calTime!=null){
      calTimeDate = calTime.substring(0,10);
      calTimeStr = calTime.substring(11,calTime.length());
    }
    if(endTime!=null){
      endTimeDate = endTime.substring(0,10);
      if(!calTimeDate.equals(endTimeDate)){
        endTimeStr = endTime;
      }else{
        endTimeStr = endTime.substring(11,endTime.length());
      }
     if(i>0){
       Map<String,String> map2 = calendarList.get(i-1);
       String calTime2 = map2.get("calTime");
       String calTimeDate2 = "0000-00-00";
       if(calTime2!=null){
         calTimeDate2 = calTime2.substring(0,10);
         if(calTimeDate.equals(calTimeDate2)){
           temp = "2";
         }
       }
     }
   }
  %>
   <%if(temp.equals("1")){
    %>
     <br><b><%=calTimeDate %>：</b><br>&nbsp;&nbsp;&nbsp;<%=calTimeStr %> - <%=endTimeStr %>&nbsp;&nbsp;&nbsp;<%=map.get("content") %><br>
    <%
     } else{    
       %>
       <br>&nbsp;&nbsp;&nbsp;<%=calTimeStr %> - <%=endTimeStr %>&nbsp;&nbsp;&nbsp;<%=map.get("content") %><br>
          <% 
            }
          } %>
       </td>
     </tr>
  </tbody>
</table>
       <%
     }else{
       %>
            
 <table class="MessageBox" align="center" width="290">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">没有相关的日程记录！</div>
    </td>
  </tr>
</table>
       <%
     }
   } else{
     %>
     
     <table class="MessageBox" align="center" width="290">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">没有相关的日程记录！</div>
    </td>
  </tr>
</table>
     <%
   }
 %>
</body>
 
</html>

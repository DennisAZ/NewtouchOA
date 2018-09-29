<%@ page language="java" contentType="text/html; charset=UTF-8" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.calendar.data.*" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
  String bindUserOther = YHSysProps.getString("BIND_USERS_OTHERS");
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  boolean isAdmin = loginUser.isAdmin();
  
  Date date = new Date();
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
  SimpleDateFormat dateFormatWeek = new SimpleDateFormat("E");
  SimpleDateFormat dateFormatTime = new SimpleDateFormat("HH:mm:ss");
  String week = dateFormatWeek.format(date);
  String dateStr = dateFormat.format(date);
  int year = Integer.parseInt(dateStr.substring(0,4));
  int year1 = Integer.parseInt(dateStr.substring(0,4));
  int month = Integer.parseInt(dateStr.substring(5,7));
  int day = Integer.parseInt(dateStr.substring(8,10));
  Calendar time=Calendar.getInstance(); 
  time.clear(); 
  time.set(Calendar.YEAR,year); //year 为 int 
  time.set(Calendar.MONTH,month-1);//注意,Calendar对象默认一月为0           
  int maxDay=time.getActualMaximum(Calendar.DAY_OF_MONTH);//本月份的天数 
  List<YHCalendar> calendarList = new ArrayList<YHCalendar>();
  calendarList= (List<YHCalendar>)request.getAttribute("calendarList");
  String status = "0";
  String yearOnly = request.getParameter("yearOnly");
  String yearStr = request.getParameter("year");
  String monthStr = request.getParameter("month");
  if(yearOnly!=null){
    year1 = Integer.parseInt(yearOnly);
  }
  if(yearStr!=null){
    year = Integer.parseInt(yearStr);
  }
  if(monthStr!=null){
    month = Integer.parseInt(monthStr);
  }
  String weekToDate = request.getParameter("date");
  if(weekToDate!=null){
    year = Integer.parseInt(weekToDate.substring(0,4));
    month = Integer.parseInt(weekToDate.substring(5,7));
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>月考核</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var year1 = "<%=year1%>";
var month = "<%=month%>";
var day = "<%=day%>";
function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getManageGroup.act";
  var rtJson = getJsonRs(url);
  var flag = 0;
  var str = "";
  if(rtJson.rtState == "0"){
    var table=new Element('table',{ "width":"50%","class":"TableBlock","align":"center"})
    .update("<tbody id='tbody'><tr class='TableHeader'>"
      + "<td nowrap width='30%' align='center'>考核月份</td>"
      + "<td nowrap width='20%' align='center'>操作</td></tr><tbody>");
    $('listDiv').appendChild(table);
    var strs = "";
  	for(var i = 1; i <= 12; i++){
      flag++;
      if(i < 10){
        strs = "0" + i;
      }else{
        strs = i;
      }
  	  var trColor = (flag % 2 == 0) ? "TableLine1" : "TableLine2";
  	  var tr=new Element('tr',{'class':trColor});			
      table.firstChild.appendChild(tr);
      if(month < i){
        str = "<td align='center'>" + i + "月</td><td align='center'>";
      }else{
        if(day > 25){
          str = "<td align='center'>" + i + "月</td><td align='center'><a href=" 
          + "javascript:checkScore('"+strs+"');"
          + ">考核</a>&nbsp;&nbsp;"
          + "</td>";
        }else{
          str = "<td align='center'>" + i + "月</td><td align='center'><a href=" 
          + "javascript:checkScore('"+strs+"');"
          + ">考核</a>&nbsp;&nbsp;"
          + "</td>";
        }
        
      }
      tr.update(str);
  	}
  }else{
  	alert(rtJson.rtMsrg); 
  }
}

function My_Submit2(){
  var yearOnly = document.getElementById("yearOnly").value;
  //var month = document.getElementById("month").value;
  window.location="<%=contextPath%>/subsys/oa/hr/score/month/index.jsp?yearOnly="+yearOnly;
}

function setOnlyYear(index){
  var year = document.getElementById("yearOnly").value;
  //var month = document.getElementById("month").value;
  if(parseInt(year)<=2000){
    year = parseInt(year);
  }else if(parseInt(year)>=2049){
    year = parseInt(year);
  }else{
    year = parseInt(year)+parseInt(index);
  }
  window.location="<%=contextPath%>/subsys/oa/hr/score/month/index.jsp?yearOnly="+year;
}

function checkScore(month){
  location = "<%=contextPath%>/subsys/oa/hr/score/month/index1.jsp?year="+year1+"&month="+month;
}

function support(groupNames, seqId){
  var URL = "/yh/core/funcs/system/address/manage/support.jsp?seqId="+seqId+"&groupName="+encodeURIComponent(groupNames);
  openDialog(URL,'480', '280');
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/score.gif" align="absmiddle"><span class="big3">&nbsp;月考核</span><br>
    </td>
  </tr>
</table>

<br>
<a href="javascript:setOnlyYear(-1)";  title="上一年"><img  src="<%=imgPath%>/previouspage.gif"></img></a>
   <select id="yearOnly" name="yearOnly" style="height:22px;FONT-SIZE: 11pt;" onchange="My_Submit2();">
     <%
       for(int i = 2000; i < 2050; i++){
         if(i == year1){
     %>
     <option value="<%=i %>" selected="selected"><%=i %>年</option>
       <%}else{ %>
     <option value="<%=i %>"><%=i %>年</option>
       <%
           }
        }
       %>
   </select><a href="javascript:setOnlyYear(1);" class="ArrowButtonR" title="下一年"><img src="<%=imgPath%>/nextpage.gif"></img></a><span class="big3">月考核</span>

<br>
<div id="listDiv" align="center"></div>
</body>
</html>
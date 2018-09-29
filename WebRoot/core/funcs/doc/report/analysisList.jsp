<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% 
	String startDate=request.getParameter("startDate");
	String endDate=request.getParameter("endDate");
	String dept=request.getParameter("dept");
	String rid=request.getParameter("rid");
	String query=request.getParameter("query");
  String item[]=query.split(",");
  String str="";
  for(int i=0;i<item.length;i++){
    if(!"".equals(item[i])){
      String value=request.getParameter(item[i]);
      if(!"".equals(value) && value!=null){
        str+="&"+item[i]+"="+value;
      }
    }
  }
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>人事分析列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/report/js/reportLogic.js"></script>
<script type="text/javascript">
var str="<%=str%>";
function doInit(){
	 var param="rid=<%=rid%>&query=<%=query%>&dept=<%=dept%>&endDate=<%=endDate%>&startDate=<%=startDate%>";
  if(str!=""){
	   param+="<%=str%>";
  }
	  var url = "<%=contextPath%><%=moduleSrcPath %>/act/YHFlowReportAct/getTableListAct.act";
		var rtJson = getJsonRs(url,param);
	  if(rtJson.rtState == "0"){
	    var data= rtJson.rtData;
	    makeTable(data);
	  }
	
}

function makeTable(data){

	if(data.flag=="0"){
		   $("showNoData").style.display = 'block';
	  	}
	else{
	 $('showList').innerHTML=data.table;
  }
}

function Chart(ChartStr){
	  var param="data="+ChartStr;
	   var url = "<%=contextPath%><%=moduleContextPath %>/report/analysis.jsp?";
     window.location.href=url+param;	
}
</script>
</head>
<body onLoad="doInit();">
<div id="showList" algin="center" >

</div>
<div align="center" style="display:none" id="showNoData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>没有统计数据</div></td>

  </tr>
  </table>
  </div>
</body>
</html>
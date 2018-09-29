<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% 
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
<title>分析列表</title>
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
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/report/js/reportLogic.js"></script>
<script type="text/javascript">
var str="<%=str%>";
function doInit(){
	 var param="rid=<%=rid%>&query=<%=query%>";
  if(str!=""){
	   param+="<%=str%>";
  }
	  var url = "<%=contextPath%>/yh/core/funcs/allreport/act/YHDataReportAct/getTableListAct.act";
		var rtJson = getJsonRs(url,param);
	  if(rtJson.rtState == "0"){
	    var data= rtJson.rtData;
	    makeTable(data);
	  }
	
}

function makeTable(data){

	 $('showList').innerHTML=data.table;

}

function Chart(GroupItem,item,sql,rid){
	 var param="query=<%=query%>&groupItem="+GroupItem+"&item="+item+"&sql="+encodeURIComponent(sql)+"&rid="+rid;
	  if(str!=""){
		     param+="<%=str%>";
		  }
     var url = "<%=contextPath%>/core/funcs/workflow/report/analysis.jsp?";
     window.location.href=url+param;	
}
</script>
</head>
<body onLoad="doInit();">
<div id="showList" algin="center" ></div>
<div align="center" style="display:none" id="showNoData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>没有统计数据</div></td>

  </tr>
  </table>
  </div>
</body>
</html>
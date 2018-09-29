<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% 
	String startDate=request.getParameter("startDate");
	String endDate=request.getParameter("endDate");
	String deptId=request.getParameter("deptId");
	String module=request.getParameter("module");


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
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/salary/welfare_manager/js/welfaremanageLogic.js"></script>
<script type="text/javascript">
function doInit(){

	  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/analysis/act/YHHrStaffAnalysisAct/getAnalysisList.act?module=<%=module%>&startDate=<%=startDate%>&deptId=<%=deptId%>&endDate=<%=endDate%>";
		var rtJson = getJsonRs(url);
	  if(rtJson.rtState == "0"){
	    var data= rtJson.rtData.data;
	    makeTable(data);
	  }
	
}

function makeTable(data){
	var total=0;
	if(data.length<=0){
		   $("showNoData").style.display = 'block';
	     return;
		}
	for(var j=0;j<data.length;j++){
    total= parseFloat(total)+ parseFloat(data[j].count);
	}

	 var table = new Element('table', { "class" : "TableBlock", "id" : "beSortTable", "width" : "95%" }).update("<tbody id = 'tboday'><tr  class='TableHeader'><td >部门</td><td >离职人数</td><td >占查询结果总人数比例</td>");
	 $('showList').appendChild(table);
	 for(var i=0;i<data.length;i++){
	     var tr = new Element('tr', { "class" : "TableData"  });
       $('tboday').appendChild(tr);
        tr.update("<td align='center'>" + data[i].dept + "</td><td align='center'>"+data[i].count + "</td><td align='center'>" +(data[i].count/total)*100+"% </td>");
        
	     }
  

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
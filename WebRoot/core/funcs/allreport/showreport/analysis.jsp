<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ include file="/core/inc/header.jsp"%>
<%
  String groupItem = request.getParameter("groupItem");
  String item = request.getParameter("item");
  String sql = request.getParameter("sql");
  String rid = request.getParameter("rid");
  String query=request.getParameter("query");
  String items[]=query.split(",");
  String str="";
  for(int i=0;i<items.length;i++){
    if(!"".equals(items[i])){
      String value=request.getParameter(items[i]);
      if(!"".equals(value) && value!=null){
        str+="&"+items[i]+"="+value;
      }
    }
  }
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>统计分析</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/report/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/workflow/report/js/FusionCharts/FusionCharts.js"></script>
<script type="text/javascript">
var groupItem = "<%=groupItem%>";
var item = "<%=item%>";
var sql="<%=sql%>";
var rid="<%=rid%>";
var chose="";
var choseStr = "";
var str="<%=str%>";
function doInit(){
	getStrXml();
}
function getStrXml(){
	 var param="query=<%=query%>&groupItem="+groupItem+"&item="+item+"&sql="+encodeURIComponent(sql)+"&rid="+rid;
	  if(str!=""){
		     param+="<%=str%>";
		  }
    var url = "<%=contextPath%>/yh/core/funcs/workflow/act/YHFlowReportAct/getChartAct.act";
	  var rtJson = getJsonRs(url,param);
	  if(rtJson.rtState == "0"){
	     var data= rtJson.rtData.data;
       setChart(data);
   }
 }

function setChart(strXML){

	 if(strXML!=""){
		    if(chose == ""){
		      choseStr = "Pie3D.swf";
		    }
		    if(chose == "1"){
		      choseStr = "Pie3D.swf";
		    }
		    if(chose == "2"){
		      choseStr = "Column3D.swf";
		    }
		    var chart1 = new FusionCharts("<%=contextPath%>/subsys/oa/examManage/examOnline/FusionCharts/"+choseStr+"", "chart1Id", "600","300", "0", "0");
		    chart1.setTransparent("false");
		    chart1.setDataXML(strXML);
		    chart1.render("chart1div");
		 } else {
		   $("NoData").style.display = 'block';
		   }
}

function changeType(){
  var type=$("type").value;
  if(type=="0"){
     chose="1";
     getStrXml();
	  }else if(type=="1"){
		     chose="2";
		     getStrXml();
		  }

}
</script>
</head>
<body onLoad="doInit();">
 <div id="s" align="left">
 <select id="type" class="BigButton" onChange='changeType()' >
  <option value='0'>饼状图</option>
  <option value='1'>柱状图</option>
 </select>
</div>
<center>

<!-- START Script Block for Chart FactorySum -->
<div id="FactorySumDiv" align="center">
 <br>
 <div id="chart1div" align="center">

</div>

</center>
 <form name='frmUpdate' id="frmUpdate" >

</body>
</html>


<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.util.*" %>
<%@page import="java.text.SimpleDateFormat" %>
	<%
	 String rId=request.getParameter("rId");
	
	Date date=new Date();
	SimpleDateFormat matter=new SimpleDateFormat("yyyy-MM-dd");
	
	Calendar c = Calendar.getInstance();
	c.setFirstDayOfWeek(Calendar.MONDAY); // 如果以周一为一周第一天，默认周日
	c.setTime(date);
	c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // 如果以周一为一周第一天
	Date d = c.getTime();
	String mon=matter.format(d);
	c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	Date s=c.getTime();
	String sun=matter.format(s);
	String ton=matter.format(date);
	
	  //获取月的第一天，最后一天
	c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));      
	String monthLastDate=matter.format(c.getTime());
	String monthFirstDate=ton.substring(0,8)+"01";

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>统计报表</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href ="<%=cssPath %>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-ui.custom.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/orgselect.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/allreport/js/report.js"></script>

<script>
var queryStr="";
function doInit(){
  getJson();
}

function getJson(){
	 var url = contextPath + "/yh/core/funcs/allreport/act/YHDataReportAct/getReportByRidAct.act?rId=<%=rId%>";
     var json = getJsonRs(url);
     //alert(rsText);
     if(json.rtState == '0'){
       var data=json.rtData;
       if(data.userStr==""){
         $("showNoData").style.display="block";
         $("table").style.display="none";
        }else{
          $("reportTitle").innerHTML=data.rName;
	        var query=data.query;
	        var str="";
	        var que="";
	        for(var i=0;i<query.length;i++){
			      var inputStr =  getDataTypeStr(query[i].name,query[i].item,query[i].dataType);
			 
			      str=str+ inputStr;
			      if("DATE"==query[i].dataType){
			    	  que+=query[i].item+","+query[i].item+"1,";
				     }else{
		           que+=query[i].item+",";
				    }
		        str+="<br><hr>";
		       }
		    
	        $("query").value=que;
	        queryStr=que;
	        $("search").innerHTML=str;

	        for(var i=0;i<query.length;i++){
	           setQueryDate(query[i].item,query[i].dataType);
	           }
     }
    }
   }

jQuery(window).resize(function(){
  if(typeof(frm_report) == "undefined")
  {
    return;
  }
  jQuery('#report').height(jQuery(frm_report.document.body).attr('scrollHeight'));
});

function set_height()
{
    jQuery(window).triggerHandler('resize');
}

function export_excel()
{
    document.form1.is_excel.value = 1;
    document.form1.submit();
}
 

 function export_excel(){
     $("form1").action=contextPath+"/yh/core/funcs/allreport/act/YHDataReportAct/toExcel.act";
     //alert(rsText);
     $("form1").target="";
     $("form1").submit();
     $("form1").action="analysisList.jsp";
     $("form1").target="frm_report";
	 } 

 
</script>
</head>
<body  style="background:none"  onLoad="doInit();">
<div id="123">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
        <td class="Big"><img src="<%=imgPath%>/finance1.gif" align="absmiddle"><span class="big3"> 报表条件查询</span>&nbsp;
        </td>
    </tr>
</table>


<form  action="analysisList.jsp" method="get" target="frm_report"  name="form1" id="form1" onSubmit="  ">
<table width="70%" class="TableList" id="table" align="center">
  <tr >
  <td class="TableContent" colspan="2" align="center"><span align="center" class="big3" id="reportTitle"></span></td>
  
  </tr>
  <tr >
  <td class="TableContent" width="20%">自定义查询条件：</td>
  <td class="TableData">
  <div id="search"></div>

  </td>
  </tr>
  <tr class="TableControl">
  <td align="center" colspan=2> 
  <input type="hidden" value="<%=rId%>" name="rid" id="rid"/>
  <!--input type="hidden"  name="dept" id="dept"/-->
  <input type="hidden"  name="query" id="query"/>
  
  &nbsp;<input type="submit"  value="查询" class="BigButton"/>
  &nbsp;<input type="reset"  value="重置"  class="BigButton"/>
  &nbsp;<input type="button" onclick="export_excel();" value="导出Excel" class="BigButton"/>
  </td>
  </tr>
</table>
</form>
<div align="center" style="display:none" id="showNoData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>您无权限查看该报表</div></td>
  </tr>
  </table>

</div>
<div style="width:80%;margin:10px auto;" id="report">
    <iframe onLoad="jQuery(window).triggerHandler('resize');" style="width:100%;height:500px" frameborder=0 name="frm_report"  id="frm_report" align="center"></iframe>
</div>
<body>
</html>

<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
 <%@ include file="/core/inc/header.jsp" %>
 <%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>

<% 
 String rId=request.getParameter("rId");
 String fId=request.getParameter("fId");

%>

<html>
<head>
<title>报表权限设置 </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/flowreport/js/report.js"></script>
<script type="text/javascript">
 function doInit(){
     var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowReportAct/getReportPrivByRidAct.act?rId=<%=rId%>";
     var json = getJsonRs(url);
     if(json.rtState == '0'){
         var data=json.rtData.data;
         if(data.length<=0){
             $("noData").style.display="block";
             $("showtable").style.display="none"; 
           }else{
        
              for(var i=0;i<data.length;i++){
                var edit="<a href='edit.jsp?rid=<%=rId%>&fId=<%=fId %>&pId="+data[i].pId+"'> 编辑 </a> "
                         +"<a href=\"javascript:del('"+data[i].pId+"')\"> 删除 </a>";
                   
                var tr = new Element('tr', {"class" : "TableData" });
                     $('tbody').appendChild(tr);
                     tr.update("<td align='center'>" + data[i].userStr + "</td>"                                            
                             + "<td align='center'>" + data[i].deptStr+ "</td>"
                             + "<td align='center'>" + edit + "</td>");
                       }
                  }
         }
	 }

 function del(pid){
	 if(window.confirm("确认要删除该该权限吗 ？")){
	 var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowReportAct/delReportPrivByIdAct.act";
     var json = getJsonRs(url,"pId="+pid);
     if(json.rtState == '0'){
         alert("成功删除权限！");
         window.location.reload();
         }
	 }
	 }
</script>

<body onLoad="doInit();" class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 报表权限设置 -- <?=$r_name?></span><br>
    </td>
  </tr>
</table>

<div align="center">
<input type="button"  value="添加权限" class="BigButton" onClick="location='new.jsp?rid=<%=rId%>&fId=<%=fId %>';" title="添加权限">&nbsp;
<input type="button"  value="返回" class="BigButton" onClick="location='../list.jsp?fId=<%=fId %>'" title="返回">
</div>

<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> 报表权限管理</span>
    </td>
  </tr>
</table>
<br>
<div align="center" id="showtable">

<table class="TableList"  width="600"  align="center" >
 <tbody id="tbody">
 <tr class="TableHeader">
    <td nowrap align="center">管理员</td>
    <td nowrap align="center">权限范围</td>
    <td nowrap align="center">操作</td>
 </tr>
</tbody>
</table>
</div>

<div align="center" style="display:none" id="noData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>尚未设置权限！</div></td>
  </tr>
  </table>
  <br>
 </div>
</body>
</html>
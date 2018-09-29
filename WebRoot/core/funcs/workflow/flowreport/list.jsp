<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
 <%@ include file="/core/inc/header.jsp" %>
 <% 
  String fId=request.getParameter("fId");
  if(null==fId){
    fId="";
  }
 %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>报表管理</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
</head>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/flowreport/js/report.js"></script>
<script type="text/javascript">
 function doInit(){
   var fId='<%=fId%>';
	 if(fId!=""){
		  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowReportAct/getReportAct.act?fId="+fId;
	    var json = getJsonRs(url);
	    if(json.rtState == '0'){
	       var data=json.rtData.data;
	       if(data.length<=0){
	          $("noData").style.display="block";
	          $("tableData").style.display="none"; 
	        }else{
		   
             for(var i=0;i<data.length;i++){
               var edit="<a href='edit.jsp?rId="+data[i].rId+"&fId="+fId+"'> 编辑 </a> "
                        +"<a href='set_priv/index.jsp?rId="+data[i].rId+"&fId="+fId+"'> 权限设置 </a>"
                        +"<a href=\"javascript:del('"+data[i].rId+"')\"> 删除 </a>";
                  
            	 var tr = new Element('tr', {"class" : "TableData" });
            	      $('tbody').appendChild(tr);
            	      tr.update("<td align='center'>" + data[i].rName + "</td>" 
                    	      + "<td align='center'>" + data[i].createUser + "</td>"
            	              + "<td align='center'>" + data[i].createDate + "</td>"        	       
            	              + "<td align='center'>" + groupType(data[i].groupType)+ "</td>"
            	              + "<td align='center'>" + edit + "</td>");
            	        }
                 }
		        }
	    }
	 }

 function groupType(type){
    if(type=="0"){
         return "统计计算";
        }
    else if(type="1"){
        return "明细列表";
        }
	 }
 function edit(){
    window.location.href="new.jsp?fId=<%=fId%>";
	 }

 function del(rid){
	  if(window.confirm("确认要删除该报表 ？")){
		  var url = contextPath + "/yh/core/funcs/workflow/act/YHFlowReportAct/delReportByIdAct.act";
	      var json = getJsonRs(url,"rId="+rid);
	      if(json.rtState == '0'){
	        alert("报表已成功删除");
	        window.location.reload();
		   }
	  }
 }
</script>
<body onLoad="doInit();" class="bodycolor" topmargin="5">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
        <td class="Big">
          <img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3"> 报表设计管理</span>&nbsp;
        </td>
    </tr>
</table>
<% if(!"".equals(fId)) {%>
  <div id="tableData" algin="center" >
    <input type="button" value="新建报表" class="BigButton" onClick="edit();"/>
   <br/>
   <table border="0" width="100%" class="TableList" id="TableList"  align="center">
     <tbody class="tbody" id="tbody">
      <tr class="TableHeader">
	      <td width="40%" align="center">报表名称</td>
	      <td width="10%" align="center">创建人</td>
	      <td width="15%" nowrap align="center">创建日期</td>
	      <td width="15%" align="center">统计方式</td>
	      <td nowrap align="center">操作</td>
      </tr>
    </tbody>
   </table>
  </div>
<%
}else if("".equals(fId)){
  %>
  <div align="center"  id="showNoData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>请从左侧面板选择流程！</div></td>
  </tr>
  </table> 
  <% 
}
%>
<div align="center" style="display:none" id="noData">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>尚未创建报表！</div></td>
  </tr>
  </table>
  <br>
  <input type="button" value="新建报表" class="BigButton" onClick="edit();"/>
 </div>
</body>
</html>
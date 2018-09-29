<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
    <%@ page  import="java.util.List"%>
<%@ page  import="yh.subsys.inforesouce.data.*"%>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
<title>信息资源管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
 <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
 <script type="text/javascript" src="js/index3.js"></script>
 <%
 	String number = (String)request.getAttribute("number");
 %>
<script type="text/javascript"><!--
function addvalue(){
   //alert("ss");
   var seqId = $("seqId").value;
   var number = "<%=number%>";
   
   window.location.href = contextPath + "/subsys/inforesource/addvalue.jsp?seqId="+seqId+"&&number="+number;
}
function doInit(){
  var src=contextPath+"/yh/subsys/inforesouce/act/YHMateElementAct/selectvalue.act";
    document.form1.action=src;
    document.form1.submit();  
}
function back(){
  window.location.href=contextPath+"/subsys/inforesource/addvalue.jsp"
}
function deleteBookType(seqIda,seqIdb,number){
  var msg ="确定要删除该类别吗？";
  if(!window.confirm(msg)){
  }else{
   
    //var seqIda = document.getElementById("seqIda").value;
   // var seqIdb = document.getElementById("seqIdb").value;
   // var number = document.getElementById("number").value;
      // document.form1.action = contextPath + "/yh/subsys/inforesouce/act/YHMateValueAct/deleteMateValue.act?seqIda="+seqIda+"seqIdb="+seqIdb+"number="+number;  
     // document.form1.submit(); 
      window.location.href=contextPath + "/yh/subsys/inforesouce/act/YHMateValueAct/deleteMateValue.act?seqIda="+seqIda+"&seqIdb="+seqIdb+"&number="+number;
      return true;
    
  }
}
--></script>
<% 
List<YHMateValue> valuerang = (List<YHMateValue>)request.getAttribute("va");
String seqId = (String)request.getAttribute("seqId");
%>

</head>
<body onload="" topmargin="5" class="bodycolor">
<table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
  <tbody><tr>
    <td class="Big"><img width="22" height="20" src="<%=imgPath%>/system.gif"><span class="big3">值域定义</span>
    </td>
  </tr>
</tbody></table>
<form name="form1" id="form1" action="#">
<table width="40%" align="center" class="TableBlock">
     <tbody><tr class="TableHeader">
      <td nowrap="" colspan="2" title="值域定义">
        <b>值域定义</b>
      </td>
     </tr>
     <tr>
      <td align="center" colspan="2" class="TableHeader">
        <input type="button" onclick="addvalue();return false;" class="BigButton" value="增加">
      </td>
     </tr>
     <% if(valuerang!=null && valuerang.size()>0){
         for(int i = 0; i<valuerang.size(); i++){                %>
        <tr class="TableData">
          <td nowrap="" title="值域名称">
			
			<%= valuerang.get(i).getValueName()%>
			
          </td>
          <td nowrap="" align="center">
           <a href="<%=contextPath%>/subsys/inforesource/update.jsp?valueId=<%=valuerang.get(i).getTypeNumber()%>&valueName=<%=valuerang.get(i).getValueName()%>&aid=<%=valuerang.get(i).getSeqId()%>&pId=<%=seqId %>&&number=<%=number%>"> 编辑</a>&nbsp;&nbsp;
           <!-- 
           <a href="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateValueAct/deleteMateValue.act?seqIda=<%=valuerang.get(i).getSeqId()%>&seqIdb=<%=seqId%>&&number=<%=number%>"> 删除</a>
            -->
           <a onclick="javascript:deleteBookType(<%=valuerang.get(i).getSeqId()%>,<%=seqId%>,'<%=number%>');" href="javascript:void(0);">删除</a>	
           <input type="hidden" id = "seqIda" name ="seqIda" value ="<%=valuerang.get(i).getSeqId()%>"></input>
           <input type="hidden" id = "seqIdb" name ="seqIdb" value ="<%=seqId%>"></input>
           <input type="hidden" id = "number" name ="number" value ="<%=number%>"></input>
          </td>
        </tr>
       <% } }else{%> 
            
        
    </tbody>
    <table class="MessageBox" align="center" width="430">
    <tbody><tr>
    <td class="msg info">
      <div style="font-size: 12pt;" class="content">暂没有添加值域内容！</div>
    </td>
  </tr>
</tbody>
    </table>
    <%} %>
    </table>
    </form>
<br>
<center>
  <div><input type="button" onclick="window.history.go(-1)" name="back" class="BigButton" value="返回"></div>
  <input type="hidden" id="seqId" name="seqId" value="<%=seqId %>">
</center>
</body>
</html>
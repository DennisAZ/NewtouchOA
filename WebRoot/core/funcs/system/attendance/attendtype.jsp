<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%String flowId = request.getParameter("flowId"); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>设置排班类型</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/ExchangeSelect1.0.js"></script>
<script type="text/javascript">
function config_edit(seqId){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/configedit.jsp?seqId=" + seqId;
}
function config_general(seqId){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/configGeneral.jsp?seqId=" + seqId;
}
function config_score(seqId){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/configScore.jsp?seqId=" + seqId;
}
function config_delete(seqId){
  var requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendConfigAct/deleteConfigById.act?seqId=" + seqId; 
  window.location.href = requestURL;
}
function returnBefore(){
  window.location.href = "<%=contextPath%>/core/funcs/system/attendance/index.jsp";
}
function doOnLoad(){
  var requestURL; 
  var prcsJson; 
  requestURL = "<%=contextPath%>/yh/core/funcs/system/attendance/act/YHAttendConfigAct/selectConfig.act"; 
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  prcsJson = json.rtData;
  if(prcsJson.length>0){ 
    var length = prcsJson.length; 
    var table = new Element('table',{ "width":"95%","cellspacing":"0","cellpadding":"3","class":"TableList"}).update("<tbody id='tbody'><tr class='TableHeader' style='font-size:10pt'><td align='center'>编号</td><td align='center'>排班类型说明</td><td align='center'>第一次登记</td><td align='center'>第二次登记</td><td align='center'>第三次登记</td><td align='center'>第四次登记</td><td align='center'>第五次登记</td><td align='center'>第六次登记</td><td align='center'>操作</td></tr><tbody>"); 
    $('listDiv').appendChild(table); 
    for(var i=0; i<prcsJson.length;i++){ 
    var prcs = prcsJson[i]; 
    var seqId = prcs.seqId;
    var no = i + 1; 
    var dutyName = prcs.dutyName;
    var dutyTime1 = prcs.dutyTime1; 
    var dutyTime2 = prcs.dutyTime2; 
    var dutyTime3 = prcs.dutyTime3; 
    var dutyTime4 = prcs.dutyTime4; 
    var dutyTime5 = prcs.dutyTime5; 
    var dutyTime6 = prcs.dutyTime6;
    var tr = new Element('tr',{'width':'95%','class':'TableData','font-size':'10pt'}); 
    table.firstChild.appendChild(tr); 
    if(i==0){
      tr.update("<td align='center'>" 
          + no + "</td><td align='center'>" 
          + dutyName + "</td><td align='center'>" 
          + dutyTime1 + "</td><td align='center'>" 
          + dutyTime2 + "</td><td align='center'>" 
          + dutyTime3 + "</td><td align='center'>" 
          + dutyTime4 + "</td><td align='center'>" 
          + dutyTime5 + "</td><td align='center'>" 
          + dutyTime6 + "</td><td>" 
          + "<a href='#' onclick='config_general(\""+ seqId +"\")'> 公休日</a>&nbsp;&nbsp;&nbsp;" 
          + "<a href='#' onclick='config_edit(\""+ seqId +"\")'>编辑</a>&nbsp;&nbsp;&nbsp;" 
          + "<a href='#' onclick='config_score(\""+ seqId +"\")'>考核分数设定</a>&nbsp;&nbsp;&nbsp;</td>" 
          ); 
      }else{
        tr.update("<td align='center'>" 
            + no + "</td><td align='center'>" 
            + dutyName + "</td><td align='center'>" 
            + dutyTime1 + "</td><td align='center'>" 
            + dutyTime2 + "</td><td align='center'>" 
            + dutyTime3 + "</td><td align='center'>" 
            + dutyTime4 + "</td><td align='center'>" 
            + dutyTime5 + "</td><td align='center'>" 
            + dutyTime6 + "</td><td>" 
            + "<a href='#' onclick='config_general(\""+ seqId +"\")'> 公休日</a>&nbsp;&nbsp;&nbsp;" 
            + "<a href='#' onclick='config_edit(\""+ seqId +"\")'>编辑</a>&nbsp;&nbsp;&nbsp;" 
            + "<a href='#' onclick='config_delete(\""+ seqId +"\")'>删除</a>&nbsp;&nbsp;&nbsp;"
            + "<a href='#' onclick='config_score(\""+ seqId +"\")'>考核分数设定</a></td>"
            ); 
      }
    }   
  }else{
     addTable();
  }
 }
function addTable(){
  var table = new Element('table',{ "class":"TableList", "width":"100%", "align":"center"}).update("<tbody id='tbody'><tr class='TableHeader' style='font-size:10pt'><td align='center'>编号</td><td align='center'>排班类型说明</td><td align='center'>第一次登记</td><td align='center'>第二次登记</td><td align='center'>第三次登记</td><td align='center'>第四次登记</td><td align='center'>第五次登记</td><td align='center'>第六次登记</td><td align='center'>操作</td></tr><tbody>"
      + "<tr class='TableData'>"
      + "<td nowrap align='center'>1</td>"
      + "<td nowrap align='center'></td>"
      + "<td nowrap align='center'></td>"
      + "<td nowrap align='center'></td>"
      + "<td nowrap align='center'></td>"
      + "<td nowrap align='center'></td>"
      + "<td nowrap align='center'></td>"
      + "<td nowrap align='center'></td>"
      + "<td nowrap align='center'>"
      + "<a href='#' onclick='config_general()'>   公休日</a>"
      + "<a href='#' onclick='config_edit()'>   编辑</a>"
      + "</td></tr>"
     ); 
   $('listDiv').appendChild(table);
}
</script>
<body class="" topmargin="5" onload = "doOnLoad();">
<table border = "0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;新建排班类型</span><br>
    </td>
  </tr>
</table>
<div align="center">
<input type="button"  value="新建排班类型" class="BigButtonC" onClick="config_edit();"  title="新建排班类型">
</div>
<br>
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/views/attendance.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3"> &nbsp;管理排班类型</span><br>
    </td>
  </tr>
</table>
<br>
<div id = "listDiv">
</div>
<br>
<div align="center">
  <input type="button"  value="返回" class="BigButton" onClick="returnBefore();">
</div>
</body>
</html>
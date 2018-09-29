 <%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String giftType = request.getParameter("giftType");
  String giftId = request.getParameter("giftId");
  String fromDate = request.getParameter("fromDate");
  String toDate = request.getParameter("toDate");
  if(giftType==null){
    giftType = "";
  }
  if(giftId==null){
    giftId = "";
  }
  if(fromDate==null){
    fromDate = "";
  }
  if(toDate==null){
    toDate = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门领用汇总</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="/yh/core/js/orgselect.js" ></script>
<script type="text/javascript">
//var giftType = '<%=giftType%>';
//var giftId = '<%=giftId%>';
//var fromDate = '<%=fromDate%>';
//var toDate = '<%=toDate%>';
function doOnload(){
  //var deptIdTemp = document.getElementById("deptId").value;
  ///yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/getOfficeType.act
  //var requestUrl = "/yh/core/funcs/calendar/info/act/YHInfoAct/selectDeptToAttendance.act?deptId="
	var requestUrl ="<%=contextPath%>/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(requestUrl);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
 // alert(rsText);
 if(prcs.length>0){
    var table = new Element('table',{"bordercolor":"#000000", "width":"95%","style":"'border-collapse:'collapse'" ,"border":"1", "cellspacing":"0","cellpadding":"2" ,"bordercolor":"#000000" ,"class":"small" ,"align":"left"}).update("<tbody id='tbody'></tbody>");
    $("bodyDiv").update(table);
    for(var i=0;i<prcs.length;i++){
      var prc = prcs[i];
      var deptId =  prc.value;
      var deptIdDesc = prc.text;
      
      var giftOutPrcs = getGiftOutByDeptId(deptId);
      var giftOutStr = "";
      if(giftOutPrcs.length>0){ 
        giftOutStr = giftOutStr + " <table style='border-collapse:collapse' border=1 cellspacing=0 cellpadding=2 bordercolor='#000000' class=small cellpadding=3 align=left>"
        +"<tr bgcolor='#D3E5FA'>"
        +"<td nowrap align=center><b>办公用品库名称</b></td>"
        +"<td nowrap align=center><b>ID</b></td>"
        +"<td nowrap align=center><b>办公用品类型名称</b></td>"
        +"<td nowrap align=center><b>办公用品名称</b></td>"
        var total = 0;
        for(var j = 0; j<1;j++){
          var giftOut = giftOutPrcs[j];
          //var transTotal = giftOut.transTotal;
          giftOutStr = giftOutStr +  "<tr class=TableData>"
          +"<td>"+giftOut.depositoryName+"</td>"
          +"<td>"+giftOut.typeName+"</td>"
          +"<td>"+giftOut.proName+"</td>"
          +"<td>"+giftOut.seqId+"</td></tr>";
        }
      }
       newTrElement(table,[],2,{className:"TableData"},[deptIdDesc,giftOutStr])
    }  
  } 
}
function getGiftOutByDeptId(deptId){
 var requestURL = "<%=contextPath%>/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/getGiftOutByDeptId.act?deptId="+deptId;
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcs = json.rtData;
  //alert(rsText)
  return prcs;
}
function newTrElement(tbObj,tbAttri,index,tdAttri,config){
  var currentRows = tbObj.rows.length;//原来的行数
  var mynewrow = tbObj.insertRow(currentRows);
   //tr给属性赋值
  if(tbAttri.className){
    mynewrow.className = tbAttri.className;
  }
  if(tbAttri.width){
    mynewrow.width = tbAttri.width;
  }
  for(var i = 0 ; i<index ;i++){
    var currentCells = mynewrow.cells.length;
    var mynewcell=mynewrow.insertCell(currentCells);
    mynewcell.nowrap = true;
    //td给属性赋值
    if(tdAttri.className){
      mynewcell.className = tdAttri.className;
    }
    if(tdAttri.width){
      mynewcell.width = tdAttri.width;
    }
    if(tdAttri.colSpan){
      mynewcell.colSpan = tdAttri.colSpan;
    }
    if(tdAttri.align){
      mynewcell.align = tdAttri.align;
    }
    mynewcell.innerHTML = "<div align='left'>"+config[i]+"</div>";
  }
}
</script>
<!--  
<body topmargin="1" leftmargin="0" onload="doOnload();">
-->
<body topmargin="1" leftmargin="0" onload="doOnload();">
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/funcs/system/filefolder/images/notify_open.gif" align="absmiddle"><span class="big5">&nbsp;&nbsp;部门领用汇总</span><br>
    </td>
    <td align="right">
     <input type="button" class="SmallButton" value="关闭" onClick="window.close();">&nbsp;&nbsp;<input type="button" class="SmallButton" value="打印" onClick="window.print();">
   </td>
  </tr>
</table>
 
<br>
 <div id="bodyDiv" align="center"></div>
</body>
</html>

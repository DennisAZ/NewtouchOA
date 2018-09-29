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
<title>采购物品报表</title>
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
<script type="text/Javascript" src="<%=contextPath %>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var giftType = '<%=giftType%>';
var giftId = '<%=giftId%>';
var fromDate = '<%=fromDate%>';
var toDate = '<%=toDate%>';
function doOnload(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/selectReport.act?fromDate="+fromDate+"&toDate="+toDate+"&giftId="+giftId+"&giftType="+giftType;
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcs = json.rtData;
 //alert(rsText);
  if(prcs.length>0){
    var table = new Element('table',{"width":"80%", "class":"TableList", "align":"center"}).update("<tbody id='tbody'>"
        +"<tr class='TableHeader'>"
      	+"<td nowrap align='center'>礼品名称</td>"
          +"<td nowrap align='center'>数量</td>"
          +"<td nowrap align='center'>单价</td>"
  		+"<td nowrap align='center'>操作日期</td>" 
  		+"<td nowrap align='center'>操作员</td> "
  		+"<td nowrap align='center'>备注</td>"
        +"</tr></tbody>");
    $("bodyDiv").update(table);
    var total = 0;
    for(var i = 0; i<prcs.length;i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var tr1 = new Element('tr').update("<td nowrap align='center'>"+prc.giftName+"</td>"
          +"<td nowrap align='center'>"+prc.giftQty+"</td>"
          +"<td nowrap align='center'>"+prc.giftPrice+"</td>"
  		+"<td nowrap align='center'>"+prc.createDate.substr(0,10)+"</td>" 
  		+"<td nowrap align='center'>"+prc.giftCreator+"</td> "
  		+"<td nowrap align='center'>"+prc.giftMemo+"</td>");
  	  var tr2  = new Element('tr').update("<td nowrap align='right' colspan='6'>合计："+prc.giftTotal+"</td>");
  	  total = total + parseInt(prc.giftTotal,10);

  	  newTrElement(table,[],6,{align:"center"},[prc.giftName,prc.giftQty,prc.giftPrice,prc.createDate.substr(0,10),prc.giftCreator,prc.giftMemo,""]);
  	  newTrElement(table,[],1,{align:"right",colSpan:6},["合计："+prc.giftTotal]);
  	  //$("tbody").appendChild(tr1);
  	  //$("tbody").appendChild(tr2);
    }
    //var tr3  = new Element('tr').update("<td nowrap align='right' colspan='7'>共计："+total+"</td>");
	  //$("tbody").appendChild(tr3);
    newTrElement(table,[],1,{align:"right",colSpan:6},["共计："+total]);
  }else{
    $("bodyDiv").update( "<table class='MessageBox' align='center' width='350'>"
    +"<tr><td class='msg info'>"
      +"<div class='content' style='font-size:12pt'>无操作记录！！</div>"
    +"</td></tr></table>");
  }
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
    mynewcell.innerHTML = config[i];
  }
}
</script>
<body onload="doOnload();">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/funcs/system/filefolder/images/notify_open.gif" WIDTH="22" HEIGHT="22" align="absmiddle">
    	<span class="big5">采购物品报表</span>
    </td>
     <td align="right">
     <input type="button" class="SmallButton" value="关闭" onClick="window.close();">&nbsp;&nbsp;<input type="button" class="SmallButton" value="打印" onClick="window.print();">
   </td>
  </tr>
</table>
<br>
<div align="center" id="bodyDiv"></div>

</body>
</html>
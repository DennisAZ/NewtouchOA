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
<title>物品总表</title>
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
var giftType = '<%=giftType%>';
var giftId = '<%=giftId%>';
var fromDate = '<%=fromDate%>';
var toDate = '<%=toDate%>';
function doOnload(){
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/selectProductInfo.act?fromDate="+fromDate+"&toDate="+toDate+"&giftId="+giftId+"&giftType="+giftType;
  var json = getJsonRs(requestURL); 
  //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcs = json.rtData;
 //alert(rsText);
  if(prcs.length>0){
    var table = new Element('table',{"bordercolor":"#000000", "width":"95%","style":"'border-collapse:'collapse'" ,"border":"1", "cellspacing":"0","cellpadding":"2" ,"bordercolor":"#000000" ,"class":"small" ,"align":"center"}).update("<tbody id='tbody'>"
        +"<tr class='TableHeader'>"
      	+"<td nowrap align='center'>礼品ID</td>"
          +"<td nowrap align='center'>礼品名称</td>"
          +"<td nowrap align='center'>计量单位</td>"
  		+"<td nowrap align='center'>库存量</td> "
  		+"<td nowrap align='center'>领用量</td>"
        +"</tr></tbody>");
    $("bodyDiv").update(table);
    var total = 0;
    for(var i = 0; i<prcs.length;i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var useGiftQty = prc.useGiftQty;
      if(useGiftQty==''){
        useGiftQty = 0;
      }
      var tr = new Element('tr');
      $("tbody").appendChild(tr);//第一种方法，先添加一个tr 对象 在赋值
      tr.update("<td nowrap align='center'>"+seqId+"</td>"
          +"<td nowrap align='center'>"+prc.giftName+"</td>"
          +"<td nowrap align='center'>"+prc.giftUnit+"</td>"
  		+"<td nowrap align='center'>"+prc.giftQty+"</td>" 
  		+"<td nowrap align='center'>"+prc.useGiftQty+"</td>");
  	  //newTrElement(table,[],5,[seqId,prc.giftName,prc.giftUnit,prc.giftQty,prc.useGiftQty]);
    }
  }else{
    $("bodyDiv").update( "<table class='MessageBox' align='center' width='350'>"
    +"<tr><td class='msg info'>"
      +"<div class='content' style='font-size:12pt'>无操作记录！！</div>"
    +"</td></tr></table>");
  }
}
function newTrElement(tbObj,tbAttri,index,config){
  var currentRows = tbObj.rows.length;//原来的行数
  var mynewrow = tbObj.insertRow(currentRows);

  for(var i = 0; i<tbAttri.length ; i ++){
    if(tbAttri[i].className){
      mynewrow.className = tbAttri[i].className;
    }
    if(tbAttri[i].width){
      mynewrow.width = tbAttri[i].width;
    }
  }
  for(var i = 0 ; i<index ;i++){
    var currentCells = mynewrow.cells.length;
    var mynewcell=mynewrow.insertCell(currentCells);
    mynewcell.align = 'center';
    mynewcell.nowrap = true;
    mynewcell.innerHTML = config[i];
  }
}
</script>
<body topmargin="5" onload="doOnload();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
   <tr>
     <td class="Big"><img src="<%=imgPath%>/hrms.gif" align="absmiddle"><span class="big5">&nbsp;物品总量信息</span>
     <td align="right"><input type="button" class="SmallButton" value="关闭" onclick="window.close();">&nbsp;&nbsp;<input type="button" class="SmallButton" value="打印" onclick="window.print();"></td>
   </tr>
</table>
 <br>
 <div align="center" id="bodyDiv">
</div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
  String userId = request.getParameter("userId");
  if(userId==null){
    userId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>明细结果查看</title>
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
<script type="text/javascript">
function doOnload(){
  var userId = '<%=userId%>';
  var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/getOutstockByUserId.act?userId="+userId;
  var json = getJsonRs(requestURL); 
    //alert(rsText);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  }
  var prcs = json.rtData;
  if(prcs.length>0){
    var table = new Element('table',{"width":"95%" ,"class":"TableList"}).update("<tbody id='tbody'> <thead class='TableHeader'>"
            +"<td nowrap align='center'>礼品名称</td>"
            +"<td nowrap align='center'>领用人</td>"
            +"<td nowrap align='center'>数量</td>"
            +"<td nowrap align='center'>单价</td>"
            +"<td nowrap align='center'>操作日期</td>"
	        +"<td nowrap align='center'>操作人</td>"
            +"<td nowrap align='center'>备注</td>"
            +"</thead></tbody>");
    $("bodyDiv").appendChild(table);
    for(var i = 0;i<prcs.length;i++){
      var prc = prcs[i];
      var seqId = prc.seqId;
      var transFlagStr = prc.transFlag;
      var runId = prc.runId;
      var transFlag = prc.transFlag;
      var tr = new Element('tr',{}).update("<td nowrap align='center'>"+prc.giftName+"</td>"
           +"<td  nowrap align='center'>"+prc.transUser+"</td>"
           +"<td  nowrap align='center'>"+prc.useTrans+"</td>"
           +"<td  nowrap align='center'>"+prc.giftPrice+"</td>"
           +"<td  nowrap align='center'>"+prc.transDate.substr(0,10)+"</td>"
           +"<td  nowrap align='center'>"+prc.operator+"</td>"
           +"<td  nowrap align='center'>"+prc.transMemo+"</td>"
          );
     // $("tbody").appendChild(tr);
      newTrElement(table,[],7,[prc.giftName,prc.transUser,prc.useTrans,prc.giftPrice,prc.transDate.substr(0,10),prc.operator,prc.transMemo])
    }
    
    var currentRows = table.rows.length;//原来的行数
    var mynewrow = table.insertRow(currentRows);
    mynewrow.className = "TableFooter";

    var mynewcell=mynewrow.insertCell(0);
    mynewcell.align = "center";
    mynewcell.colSpan  = '7';
    mynewcell.innerHTML = "<input type='button' value='返回' class='BigButton' onClick='returnTerm();'>";
    //var tr = new Element('tr',{"class":"TableFooter"}).update("<td colspan='7' align='center'>"
        //+"<input type='button' value='返回' class='BigButton' onClick='returnTerm();'></td>");
    //$("tbody").appendChild(tr);
  }else{
    $("bodyDiv").update("<table class='MessageBox' align='center' width='320'>"
       + " <tr><td class='msg info'> <div class='content' style='font-size:12pt'>无操作记录！</div></td></tr>"
       +"</table>"
       +"<br>"
       +"<div align='center'><input type='button'  value='返回' class='BigButton' onClick='returnTerm();'></div></div>"
    );
  }
}
function newTrElement(tbObj,tbAttri,index,config){
  var currentRows = tbObj.rows.length;//原来的行数
  var mynewrow = tbObj.insertRow(currentRows);

  for(var i = 0; i<tbAttri.length ; i ++){
    if(tbAttri[i].className){
      tbObj.className = tbAttri[i].className;
    }
    if(tbAttri[i].width){
      tbObj.width = tbAttri[i].width;
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
function returnTerm(){
  window.location.href = "<%=contextPath%>/subsys/oa/gift_product/outstock/recodequery/queryTerm.jsp";
}
</script>
<body class="" topmargin="5" onload="doOnload();">
 
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/funcs/system/filefolder/images/notify_open.gif" WIDTH="22" HEIGHT="22" align="absmiddle"><span class="big3">&nbsp;礼品领用登记明细</span>
    </td>
  </tr>
</table>
 
<div align="center" id="bodyDiv">
</div>
 
</body>
</html>

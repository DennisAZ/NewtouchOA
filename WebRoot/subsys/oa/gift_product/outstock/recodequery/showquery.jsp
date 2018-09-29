<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String giftType = request.getParameter("giftType");
  String giftId = request.getParameter("giftId");
  String giftName = request.getParameter("giftName");
  String fromDate = request.getParameter("fromDate");
  String toDate = request.getParameter("toDate");
  if(giftType==null){
    giftType = "";
  }
  if(giftId==null){
    giftId = "";
  }
  
  if(giftName==null){
    giftName = "";
  }else{
    giftName = giftName.replaceAll("\"","\\\\\"");
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
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/workflow/workflowUtility/utility.js" ></script>
<script type="text/javascript">

function delete_vote(TRANS_ID)
{
   msg='确认放弃该操作？放弃该操作库存将回滚到操作前的状态！';
   if(window.confirm(msg))
   {
      URL="delete.php?TRANS_ID="+TRANS_ID;
      window.location=URL;
   }
}
function set_page()
{
 PAGE_START=(PAGE_NUM.value-1)*10+1;
 location="detail.php?TRUN=1&TRANS_FLAG=&PRO_ID=&OFFICE_PROTYPE=&PRO_NAME=&FROM_DATE=&TO_DATE=&PAGE_START="+PAGE_START+"";
}
function doOnload(){
  var giftType = '<%=giftType%>';
  var giftId = '<%=giftId%>';
  var giftName ="<%=giftName%>";
  var fromDate = '<%=fromDate%>';
  var toDate = '<%=toDate%>';
  var url = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/queryGiftByTemp.act?giftType="+giftType+"&giftId="+giftId+"&giftName="+encodeURI(giftName)+"&fromDate="+fromDate+"&toDate="+toDate;
  cfgs = {
   dataAction: url,
   container: "bodyDiv",
   colums: [
      {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
      {type:"text", name:"giftName", text:"礼品名称", width: "8%",align:"center"},
      {type:"text", name:"transUser", text:"领用人", width: "6%",align:"center"},
      {type:"hidden", name:"giftQty", text:"现存", width: "4%",align:"center"},
      {type:"text", name:"transQty", text:"数量", width: "4%",align:"center"},
      {type:"text", name:"transFlag", text:"退回数",align:"center", width: "4%",render:transFlagFun},
      {type:"text", name:"useTrans", text:"实际领用",align:"center", width: "4%" },           
      {type:"text", name:"transUses", text:"礼品用途", width: "8%",align:"center"},
      {type:"text", name:"giftPrice", text:"单价",align:"center", width: "6%"},
      {type:"date", name:"transDate", text:"操作日期", width:"8%" ,align:"center",render:TransDate},
      {type:"text", name:"operator", text:"经手人", width:"8%",align:"center"},
      {type:"text", name:"giftMemo", text:"备注", width:"12%",align:"center"},
      {type:"hidden", name:"runId", text:"工作流", width:"0%",align:"center"},
      {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"14%",render:Opts}]
 };
  pageMgr = new YHJsPage(cfgs);
 pageMgr.show();
 var total = pageMgr.pageInfo.totalRecord;
 if(total){
 }else{
   var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
       + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件的信息</div></td></tr>"
       );
   //$('bodyDiv').style.display="none";
   $('bodyDiv').update(table); 
   
 }
}
function TransDate(cellData, recordIndex, columIndex){
  var transDate = this.getCellData(recordIndex,"transDate");
  return transDate.substr(0,10);
}
function transFlagFun(cellData, recordIndex, columIndex){
  var transFlag = this.getCellData(recordIndex,"transFlag");
  if(parseInt(transFlag,10)<1){
    return "未退回";
  }
  return transFlag;
}
function Opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var runIdStr = this.getCellData(recordIndex,"runId");
  var opts = "<a href='javascript:updateVote("+seqId+");'>编辑</a>&nbsp;"
  +"<a href='javascript:deleteVote("+seqId+");'>删除</a>&nbsp;"
  +"<a href='javascript:backOut("+seqId+");'>退库</a>&nbsp;";
  if(runIdStr!=0&&runIdStr!=''){
   opts = "<a href='javascript:deleteVote("+seqId+");'>删除此次操作</a>&nbsp;"
        +"<a href='javascript:backOut("+seqId+");'>退库</a>&nbsp;"
   +"<a href='javascript:formViewByName("+runIdStr+" ,\"礼品领用\")' >工作流详情</a>";
}
  return opts;
}
function returnQuery(){
 window.location.href = "<%=contextPath%>/subsys/oa/gift_product/outstock/recodequery/index.jsp";
}
function deleteVote(seqId){
  msg='确认放弃该操作？放弃该操作库存将回滚到操作前的状态！';
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/giftProduct/outstock/act/YHGiftOutstockAct/delOutstockById.act?seqId="+seqId;
    var json = getJsonRs(requestURL); 
        //alert(rsText);
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    alert("删除成功！");
    window.location.reload();
   }
}
function updateVote(seqId){
  window.location.href ="<%=contextPath%>/subsys/oa/gift_product/outstock/updateOutstock.jsp?seqId="+seqId;
}
function backOut(seqId){
  var requestURL = "<%=contextPath%>/subsys/oa/gift_product/outstock/back.jsp?seqId="+seqId;
  window.location.href = requestURL;
}
</script>
</head>
<body  topmargin="5" onload="doOnload();">
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="/yh/core/funcs/system/filefolder/images/notify_open.gif" WIDTH="22" HEIGHT="22" align="absmiddle"><span class="big3">礼品管理明细</span>
    </td>
</tr>
</table>
 <div id="bodyDiv">
 

 </div>
      <br><div align="center">
               <input type="button" value="返回" class="BigButton" onClick="returnQuery();">
            </div> 
     </body>
</html>

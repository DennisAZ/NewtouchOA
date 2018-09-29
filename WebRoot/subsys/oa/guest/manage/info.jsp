<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>友好组织信息</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
function showGuest(seqId) {
  var myleft = (screen.availWidth - 800)/2;
  window.open("<%=contextPath%>/subsys/oa/guest/manage/new/showGuest.jsp?seqId="+seqId,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=800,height=500,left=" + myleft + ",top=50");
}
function doOnload() {
  selectGuest();
}
var pageMgr = null;
var cfgs = null;
function selectGuest(){
  var url = "<%=contextPath%>/yh/subsys/oa/guest/act/YHGuestAct/queryGuest.act";
   cfgs = {
    dataAction: url,
    container: "orgDiv",
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"guestNum", text:"贵宾编号 ", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"guestType", text:"贵宾类型 ", width: "6%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"guestName", text:"贵宾名称 ", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"guestUnit", text:"贵宾所在单位 ", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"hidden", name:"guestphone", text:"贵宾联系电话 ", width: "5%",align:"center",sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"deptId", text:"接待部门 ", width: "7%",align:"center",render:toDept,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"guestAttendTime", text:"来会时间  ", width: "5%",align:"center",render:toAttendDate, sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"guestLeaveTime", text:"离会时间 ", width: "5%",align:"center",render:toLeaveDate,sortDef:{type:0, direct:"asc"}},
       {type:"text", name:"guestDiner", text:"是否用餐  ", width: "5%",align:"center",render:toDiner,sortDef:{type:0, direct:"asc"}},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"8%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total>0){
  }else{
    $("orgDiv").style.display = "none";
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无合条件信息!</div></td></tr>"
        );
    $('returnNull').update(table); 
  }
}
function toOpts(cellData, recordIndex, columInde){
   var seqId = this.getCellData(recordIndex,"seqId");
   return   "<a href='#' onclick='showGuest(" + seqId + ");'>详细信息</a> "
            +"<a href='#' onclick='updateGuest(" + seqId + ");'>编辑</a> "
            +"<a href='#' onclick='clonGuest(" + seqId + ");'>克隆</a> "
            +"<a href='#' onclick='delGuest(" + seqId + ");'> 删除 </a> ";
}
function toAttendDate(cellData, recordIndex, columInde){
   var guestAttendTime = this.getCellData(recordIndex,"guestAttendTime");
   return guestAttendTime.substr(0,10);
}
function toLeaveDate(cellData, recordIndex, columInde){
  var guestLeaveTime = this.getCellData(recordIndex,"guestLeaveTime");
  return guestLeaveTime.substr(0,10);
}
function toDiner(cellData, recordIndex, columInde){
  var guestDiner = this.getCellData(recordIndex,"guestDiner");
  var guestDinerStr = "否";
  if(guestDiner=='1'){
    guestDinerStr = "是";
  }
  return guestDinerStr;
}
function toDept(cellData, recordIndex, columInde){
  var deptId = this.getCellData(recordIndex,"deptId");
  var requestURL = "<%=contextPath%>/yh/subsys/oa/guest/act/YHGuestAct/getDeptNameBySeqIds.act?deptId="+deptId; 
  var json=getJsonRs(requestURL); 
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  }
  var prc = json.rtData;
  if(prc.deptName){
    return prc.deptName;
  }
  return "";
}
function delGuest(seqId){
 if(window.confirm("确认要删除该记录吗？")){
   var requestURL = "<%=contextPath%>/yh/subsys/oa/guest/act/YHGuestAct/delGuest.act?seqId="+seqId; 
   var json=getJsonRs(requestURL); 
   if (json.rtState == '1') { 
     alert(json.rtMsrg); 
     return ; 
   }
   window.location.reload();
 }
}
function updateGuest(seqId){
  window.location.href = "<%=contextPath%>/subsys/oa/guest/manage/new/updateGuest.jsp?seqId="+seqId; 
}
function clonGuest(seqId){
  var url = "<%=contextPath%>/subsys/oa/guest/manage/new/clon.jsp?seqId="+seqId; 
  myleft=(screen.availWidth-500)/2;
  window.open(url,"","status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,resizable=yes,width=550,height=250,left="+myleft+",top=200");
}
</script>
</head>
<body onLoad="doOnload();" topmargin="5px">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" style="padding:10px;">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/logistics.gif" HEIGHT="20"><span class="big3"> 贵宾信息管理</span>
    </td>
  </tr>
</table>
<div id="orgDiv" style="padding:10px;"></div>
<div id="returnNull"></div>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
  String psnName = request.getParameter("psnName");
  psnName = YHUtility.encodeSpecial(psnName);
  if (psnName == null){
    psnName = "";
  }
  String sex = request.getParameter("sex");
  if (sex == null){
    sex = "";
  }
  String nickName = request.getParameter("nickName");
  nickName = YHUtility.encodeSpecial(nickName);
  if (nickName == null){
    nickName = "";
  }
  String deptName = request.getParameter("deptName");
  deptName = YHUtility.encodeSpecial(deptName);
  if (deptName == null){
    deptName = "";
  }
  String telNoDept = request.getParameter("telNoDept");
  telNoDept = YHUtility.encodeSpecial(telNoDept);
  if (telNoDept == null){
    telNoDept = "";
  }
  String addDept = request.getParameter("addDept");
  addDept = YHUtility.encodeSpecial(addDept);
  if (addDept == null){
    addDept = "";
  }
  String telNoHome = request.getParameter("telNoHome");
  telNoHome = YHUtility.encodeSpecial(telNoHome);
  if (telNoHome == null){
    telNoHome = "";
  }
  String addHome = request.getParameter("addHome");
  addHome = YHUtility.encodeSpecial(addHome);
  if (addHome == null){
    addHome = "";
  }
  String notes = request.getParameter("notes");
  notes = YHUtility.encodeSpecial(notes);
  if (notes == null){
    notes = "";
  }
  String groupId = request.getParameter("groupId");
  if (groupId == null){
    groupId = "";
  }
  String beginDate = request.getParameter("beginDate");
  if (beginDate == null){
    beginDate = "";
  }
  String endDate = request.getParameter("endDate");
  if (endDate == null){
    endDate = "";
  }
  String mobileNo = request.getParameter("mobileNo");
  mobileNo = YHUtility.encodeSpecial(mobileNo);
  if (mobileNo == null){
    mobileNo = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>联系人查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/address/public/js/publicUtil.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var psnNames = "<%=psnName%>";
var sexy = "<%=sex%>";
var nickNames = "<%=nickName%>";
var deptNames = "<%=deptName%>";
var telNoDepts = "<%=telNoDept%>";
var addDepts = "<%=addDept%>";
var telNoHomes = "<%=telNoHome%>";
var addHomes = "<%=addHome%>";
var notesStr = "<%=notes%>";
var groupIds = "<%=groupId%>";
var beginDate = "<%=beginDate%>";
var endDate = "<%=endDate%>";
var mobileNo = "<%=mobileNo%>";
var isPriv = "0";
function doInit(){
  //isPrivFunc();
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getPublicSearchGroup.act?psnName="+encodeURIComponent(psnNames)
             +"&sex="+sexy+"&nickName="+encodeURIComponent(nickNames)+"&deptName="+encodeURIComponent(deptNames)+"&telNoDept="+encodeURIComponent(telNoDepts)
             +"&addDept="+encodeURIComponent(addDepts)+"&telNoHome="+encodeURIComponent(telNoHomes)+"&addHome="+encodeURIComponent(addHomes)+"&notes="+encodeURIComponent(notesStr)
             +"&groupId="+groupIds+"&beginDate="+beginDate+"&endDate="+endDate+"&mobileNo="+encodeURIComponent(mobileNo);
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"psnName",  width: '10%', text:"姓名", render:psnNameFunc},       
         {type:"data", name:"sex",  width: '5%', text:"性别", render:sexRender},
         {type:"data", name:"deptName",  width: '15%', text:"单位名称", render:deptNameFunc},
         {type:"data", name:"telNoDept",  width: '10%', text:"工作电话"}, 
         {type:"data", name:"telNoName",  width: '10%', text:"家庭电话"},
         {type:"data", name:"mobilNo",  width: '15%', text:"手机 群发", render:mobilNoFunc, bindAction:bindAction, bindTitleAction: bindTitleAction, selfTitleStyle:{cursor:"pointer", textDecoration:"underline"}},
         {type:"data", name:"email",  width: '10%', text:"电子邮件"},
         {type:"data", name:"groupId",  width: '10%', text:"组名", render: groupNameFunc}, 
         {type:"selfdef", text:"操作", width: '15%',render:opts}]
    };
  var pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  //$('numUser').innerHTML = total;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 "+ total + " 条记录 ！";
    //WarningMsrg(mrs, 'msrg');
    showCntrl('delOpt');
  }else{
    WarningMsrg('通讯簿尚无记录', 'msrg');
  }
}

function bindAction(cellData, recordIndex, columIndex) {
  var mobilNo = this.getCellData(recordIndex,"mobilNo");
  //var cntrl = $("span_" + recordIndex + "_" + columIndex);
  //cntrl.observe("click", function(){alert(this.pageInfo.pageIndex);}.bind(this));
}

/**
 * 绑定到栏目标题的函数
 * @columeIndex          栏目的索引


 */
function bindTitleAction(columIndex) {
  var cntrl = $("headCell_" + columIndex);
  cntrl.observe('click', function(){
      var recordCnt = this.getRecordCnt();
      var tmpStr = "";
      for (var i = 0; i < recordCnt; i++) {
        if(this.getCellData(i, "mobilNo") != ""){
          tmpStr += this.getCellData(i, "mobilNo");
        }
        if (i < recordCnt - 1) {
          tmpStr += ",";
        }
      }
      location = contextPath + "/core/funcs/mobilesms/new/index.jsp?outUser="+ tmpStr;
    }.bind(this));
}

function opts(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var psnName = this.getCellData(recordIndex,"psnName");
  var groupId = this.getCellData(recordIndex,"groupId");
  isPrivFunc(groupId);
  if(isPriv == "0"){
    return "<center><a href=\"javascript:detail("+seqId+",'"+psnName+"');\">详情</a>&nbsp;<a href='edit.jsp?seqId="+seqId+"&groupId="+groupId+"');\">编辑</a>&nbsp;<a href=\"javascript:deleteSingle("+seqId+");\">删除</a></center>";
  }else{
    return "<center><a href=\"javascript:detail("+seqId+",'"+psnName+"');\">详情</a></center>";
  }
  //&nbsp;<a href='edit.jsp?seqId="+seqId+"&groupId="+groupId+"');\">编辑</a>&nbsp;<a href=\"javascript:deleteSingle("+seqId+");\">删除</a>
}

function isPrivFunc(groupIdStr){
  var urls = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getPublicSupportPriv.act";
  var rtJsons = getJsonRs(urls , "groupId=" +  groupIdStr);
  if(rtJsons.rtState == '0'){
    if(rtJsons.rtData == "1"){
      isPriv = "0";
    }else{
      isPriv = "1";
    }
  }
}

function sexRender(cellData, recordIndex, columIndex){
  if(cellData == "0"){
    return "<center>男</center>";
  }else if(cellData == "1"){
    return "<center>女</center>";
  }else{
    return "";
  }
}

function confirmDel() {
  if(confirm("确认要删除所选联系人吗？")) {
    return true;
  } else {
    return false;
  }
}

function deleteSingle(seqId){
  if(!confirmDel()) {
   return ;
  }  
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/deleteContact.act";
  var rtJson = getJsonRs(url, "sumStrs=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
    parent.menu.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function detail(seqId, psnName){
  var URL = "/yh/core/funcs/address/public/address/adddetail.jsp?seqId="+seqId+"&psnName="+encodeURIComponent(psnName);
  openDialog(URL,'750', '650');
}

function commitSearch(){
  location = "<%=contextPath %>/core/funcs/address/public/address/search.jsp";
  
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/address.gif" align="absmiddle"><span class="big3">&nbsp;查询结果</span>
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;">
</div>
<div id="delOpt" style="display:none">
</div>
<br>
<div id="msrg">
</div>

<div align="center">
 <input type="button"  value="返回" class="BigButton" onClick="location='search.jsp';">
</div>
</body>
</html>
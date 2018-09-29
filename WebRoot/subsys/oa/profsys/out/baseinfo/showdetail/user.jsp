<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String seqId = request.getParameter("seqId") == null ? "0" :  request.getParameter("seqId");
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
YHPerson user = (YHPerson) request.getSession().getAttribute(YHConst.LOGIN_USER);
%>
<html>
<head>
<title>项目成员管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href="<%=contextPath%>/core/styles/style1/css/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attach.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var projId = <%=seqId%>;
//查询是否有成员
var pageMgr = null;
var cfgs = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectMemAct/queryOutMemByProjId.act?projId=" + projId;
   cfgs = {
    dataAction: url,
    container: "giftList",
    moduleName:"profsys",  
    colums: [
       {type:"hidden", name:"seqId", text:"ID",align:"center", width:"1%"},
       {type:"text", name:"memNum", text:"成员编号", width: "6%",align:"center"},
       {type:"text", name:"memPosition", text:"职务", width: "6%",align:"center",render:toUser},
       {type:"text", name:"memName", text:"姓名", width: "6%",align:"center"},
       {type:"text", name:"memSex", text:"性别", width: "6%",align:"center",render:toSex},
       {type:"text", name:"memBirth", text:"出生年月", width: "6%",align:"center",render:toBirth},
       {type:"text", name:"memIdNum", text:"证件号码", width: "6%",align:"center"},
       {type:"text", name:"memPhone", text:"联系方式", width: "6%",align:"center",render:toInfo},
       {type:"hidden", name:"memMail", text:"邮件", width: "6%",align:"center"},
       {type:"hidden", name:"memFax", text:"传真", width: "6%",align:"center"},
       {type:"hidden", name:"memAddress", text:"地址", width: "6%",align:"center"},
       {type:"hidden", name:"attachId", text:"附件ID",align:"center",width:"1%"},
       {type:"text", name:"attach", text:"附件",align:"center",width:"8%",dataType:"attach"},
       {type:"selfdef",name:"opts",align:"center",align:"center", text:"操作", width:"6%",render:toOpts}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if (total <= 0) {
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无项目成员!</div></td></tr>"
        );
    $('giftList').style.display = "none";
    $('returnNull').update(table); 
  }
}
function toSex(cellData, recordIndex, columInde){
  var memSex = this.getCellData(recordIndex,"memSex");
  var sexStr = "";
  if(memSex==1){
    sexStr = "男";
  }
  if(memSex==2){
    sexStr = "女";
  }
  return sexStr;
}
//职位
function toUser(cellData,recordIndex,columIndex){
  var memPosition = this.getCellData(recordIndex,"memPosition");
  var seqId = "?seqId=" + memPosition;
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/profsys/act/out/YHOutProjectMemAct/userName.act" + seqId;
  var rtJson = getJsonRs(requestUrl);
  var userList = rtJson.rtData;
  return userList;
}
function toBirth(cellData, recordIndex, columInde){
  var memBirth = this.getCellData(recordIndex,"memBirth");
  return memBirth.substr(0,10);
}
function toOpts(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  return   "<a href='#' onclick='getMemById(" + seqId + ");'>查看明细</a> ";
}
function toInfo(cellData, recordIndex, columInde){
  var seqId = this.getCellData(recordIndex,"seqId");
  var memPhone = this.getCellData(recordIndex,"memPhone");
  var memMail = this.getCellData(recordIndex,"memMail");
  var memFax = this.getCellData(recordIndex,"memFax");
  var memAddress = this.getCellData(recordIndex,"memAddress");
  var infoStr = "<font color=red>电话：" + memPhone + ";邮箱：" + memMail + ";传真：" +memFax + ";地址：" + memAddress + ";</font>&nbsp;&nbsp;&nbsp;";
  return   "<div style='cursor:pointer' onclick=getTD(this," + seqId + ",\"" + encodeURIComponent(infoStr) + "\")>查看详情</div>";
}
function getTD(obj,seqId,info) {
  var objTD =obj.parentNode;
  var objTR =objTD.parentNode;
  var objTable = objTR.parentNode;
  currRowIndex = objTR.rowIndex;
  if ($("td_"+seqId)) {//删除一行
    objTable.deleteRow(currRowIndex+1);
  }else {
    var mynewrow = objTable.insertRow(currRowIndex + 1);//新建一行
    var cellnum = mynewrow.cells.length;
    mynewcell = mynewrow.insertCell(cellnum);
    mynewcell.id = "td_"+seqId;
    mynewcell.align = "right";
    mynewcell.colSpan = "9";
    mynewcell.innerHTML = decodeURIComponent(info);
  }
}
function deleteMemById(seqId){
  var msg="确定要删除该项 吗?";
  if(window.confirm(msg)){
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/YHProjectMemAct/deleteMemById.act?seqId=" + seqId; 
    var json=getJsonRs(requestURL); 
    if(json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ; 
    }
    alert("删除成功！");
    window.location.reload();
  }
}
//修改
function getMemById(seqId) {
  if (seqId != "") {
    var requestURL = "<%=contextPath%>/yh/subsys/oa/profsys/act/in/YHInProjectMemAct/getMemById.act?seqId=" + seqId; 
    var json = getJsonRs(requestURL);
    if (json.rtState == '1'){ 
      alert(json.rtMsrg); 
      return ;
    }
    var prc = json.rtData; 
    if (prc.seqId) {
      var seqId = prc.seqId;
      $("memNum").update(prc.memNum);
      if (prc.memRole == "0") {
        $("memRole").update("外办");
      }
      if (prc.memRole == "2") {
        $("memRole").update("外宾");
      }
      $("memName").update(prc.memName);
      if (prc.memSex == "1") {
        $("memSex").update("男");
      }
      if (prc.memSex == "2") {
        $("memSex").update("女");
      }
      $("memNation").update(prc.memNation);
      $("memBirthplace").update(prc.memBirthplace);
      $("memNativePlace").update(prc.memNativePlace);
      $("memBirth").update(prc.memBirth.substr(0,10));
      $("memIdNum").update(prc.memIdNum);
      $("memPhone").update(prc.memPhone);
      $("memMail").update(prc.memMail);
      $("memFax").update(prc.memFax);
      $("memAddress").update(prc.memAddress);
      $("memNote").update(prc.memNote);
      $("memPosition").value  = prc.memPosition;
      if ($("memPosition").value != "") {
        bindDesc([{cntrlId:"memPosition",dsDef:"USER_PRIV,SEQ_ID,PRIV_NAME"}]);
      }
      $("attachmentName").value = prc.attachmentName;
      $("attachmentId").value = prc.attachmentId;
      attachMenuUtil("showAtt","profsys",null,$('attachmentName').value ,$('attachmentId').value,true);
    }
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit()">
<INPUT type="hidden" name="memPosition" id="memPosition" value="">
<input type="hidden" id="qRen" name="qRen" value="">
<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small">
  <tr><td>
  <img src="<%=imgPath%>/notify_new.gif" align="absmiddle"/>
  <span class="big3">添加项目成员</span><td></tr>
</table>
<table class="TableBlock" border="0" width="80%" align="center">
    <tr>
      <td nowrap class="TableContent" width="90">成员编号：</td>
      <td nowrap class="TableData" id="memNum">
      </td>
     <td nowrap class="TableContent" width="90">成员身份：</td>
      <td nowrap class="TableData" id="memRole">
    </td>  
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">姓名：</td>
      <td nowrap class="TableData" colspan="3" id="memName"> 
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">性别：</td>
      <td nowrap class="TableData" id="memSex">
    </td>
        <td nowrap class="TableContent" width="90">职务：</td>
      <td nowrap class="TableData" id="memPositionDesc" >
      </td> 
     </tr>
     <tr>
      <td nowrap class="TableContent" width="90">民族：</td>
      <td nowrap class="TableData" id="memNation">
      </td>
      <td nowrap class="TableContent" width="90">籍贯：</td>
      <td nowrap class="TableData" id="memNativePlace">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableContent" width="90">出生地：</td>
      <td nowrap class="TableData" id="memBirthplace" >
      </td>
      <td nowrap class="TableContent" width="90">出生年月： </td>
      <td nowrap class="TableData" id="memBirth">  
      </td>
     </tr>
     <tr>
      <td nowrap class="TableContent" width="90">证件号码：</td>
      <td nowrap class="TableData" id="memIdNum">
      </td>
      <td nowrap class="TableContent" width="90">电话：</td>
      <td nowrap class="TableData" id="memPhone">
      </td>
    </tr>
     <tr>
      <td nowrap class="TableContent" width="90">邮箱：</td>
      <td nowrap class="TableData" id="memMail">
      </td>
      <td nowrap class="TableContent" width="90">传真：</td>
      <td nowrap class="TableData" id="memFax">
      </td> 
     </tr>
     <tr> 
      <td nowrap class="TableContent" width="90">地址：</td>
      <td nowrap class="TableData" colspan="3" id="memAddress">
      </td> 
     </tr>
     <tr>
      <td nowrap class="TableContent">备注：</td>
      <td class="TableData" colspan="3" id="memNote">    
</tr>
       <tr id="attr_tr">
      <td nowrap class="TableData">附件文档：</td>
      <td class="TableData" colspan="3">
      <input type = "hidden" id="attachmentName" name="attachmentName"></input>
       <input type = "hidden" id="attachmentId" name="attachmentId"></input>
        <span id="showAtt">无附件</span>
      </td>
    </tr>
   <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
      <div id="but">
      <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();   parent.window.close()">&nbsp;
      </div>
      </td>
  </tr> 
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
<div>
  <img src="<%=imgPath%>/user_group.gif" align="absmiddle"/>
  <span class="big3">项目成员列表</span>
</div>
<br>
<div id="giftList" style="padding-left:10px;padding-right:10px;"></div>
<div id="returnNull"></div>
</body>
</html>
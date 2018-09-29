<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建卷库</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">

function doInit(){
 　var beginParameters = {
     inputId:'beginDate0',
     property:{isHaveTime:false}
     ,bindToBtn:'beginDateImg0'
 };
 new Calendar(beginParameters);

 var endParameters = {
     inputId:'beginDate1',
     property:{isHaveTime:false}
     ,bindToBtn:'beginDateImg1'
 };
 new Calendar(endParameters);

 var beginParameters = {
     inputId:'endDate0',
     property:{isHaveTime:false}
     ,bindToBtn:'endDateImg0'
 };
 new Calendar(beginParameters);

 var endParameters = {
     inputId:'endDate1',
     property:{isHaveTime:false}
     ,bindToBtn:'endDateImg1'
 };
 new Calendar(endParameters);

 var mgr = new SelectMgr();
 mgr.addSelect({cntrlId: "roomId"
             , tableName: "RMS_ROLL_ROOM"
             , codeField: "SEQ_ID"
             , nameField: "ROOM_NAME"
             , value: "<%=0%>", isMustFill: "0"
             , filterField: ""
             , filterValue: ''
             , order: ""
             , reloadBy: ""
             , actionUrl: ""
             });
 mgr.loadData();
 mgr.bindData2Cntrl();

 var mgrSec = new SelectMgr();
 mgrSec.addSelect({cntrlId: "secret"
             , tableName: "CODE_ITEM"
             , codeField: "CLASS_CODE"
             , nameField: "CLASS_DESC"
             , value: "<%=0%>", isMustFill: "0"
             , filterField: "CLASS_NO "
             , filterValue: 'RMS_SECRET'
             , order: ""
             , reloadBy: ""
             , actionUrl: ""
             });
 mgrSec.loadData();
 mgrSec.bindData2Cntrl();

 var mgrKind = new SelectMgr();
 mgrKind.addSelect({cntrlId: "certificateKind"
             , tableName: "CODE_ITEM"
             , codeField: "CLASS_CODE"
             , nameField: "CLASS_DESC"
             , value: "<%=0%>", isMustFill: "0"
             , filterField: "CLASS_NO "
             , filterValue: 'RMS_CERTIFICATE_KIND'
             , order: ""
             , reloadBy: ""
             , actionUrl: ""
             });
 mgrKind.loadData();
 mgrKind.bindData2Cntrl();
  deptFunc();
}

function deptFunc(){
  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollRoomAct/selectDeptToAttendance.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var userId = rtJson.rtMsrg;
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deptId");
  for(var i=0;i<prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value; 
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  return userId;
}

function doSubmit(){
  if($("beginDate0").value && $("beginDate1").value){
    if($("beginDate0").value > $("beginDate1").value){
      alert("错误, 起始日期开始不能大于起始日期结束！");
      $("beginDate0").focus(); 
      $("beginDate0").select(); 
      return false;
    }
  }

  if($("endDate0").value && $("endDate1").value){
    if($("endDate0").value > $("endDate1").value){
      alert("错误, 终止日期开始不能大于终止日期结束！");
      $("endDate0").focus(); 
      $("endDate0").select(); 
      return false;
    }
  }
  
  var beginDate0 = $("beginDate0").value;
  if(beginDate0){
    if(!isValidDateStr(beginDate0)){
      alert("错误,起始日期格式不对，应形如 1999-01-02");
      $("beginDate0").focus();
      $("beginDate0").select();
      return false;
    }
  }

  var beginDate1 = $("beginDate1").value;
  if(beginDate1){
    if(!isValidDateStr(beginDate1)){
      alert("错误,起始日期格式不对，应形如 1999-01-02");
      $("beginDate1").focus();
      $("beginDate1").select();
      return false;
    }
  }

  var endDate0 = $("endDate0").value;
  if(endDate0){
    if(!isValidDateStr(endDate0)){
      alert("错误,终止日期格式不对，应形如 1999-01-02");
      $("endDate0").focus();
      $("endDate0").select();
      return false;
    }
  }

  var endDate1 = $("endDate1").value;
  if(endDate1){
    if(!isValidDateStr(endDate1)){
      alert("错误,终止日期格式不对，应形如 1999-01-02");
      $("endDate1").focus();
      $("endDate1").select();
      return false;
    }
  }
  var query = $("form1").serialize();
  location = "<%=contextPath%>/subsys/oa/rollmanage/search.jsp?"+query;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 案卷查询</span>
    </td>
  </tr>
</table>

<table class="TableBlock"  width="95%" align="center">
  <form action="" method="post" name="form1" id="form1">
  <TR>
      <TD class="TableData">案 卷 号：</TD>
      <TD class="TableData">
       <INPUT name="rollCode" id="rollCode" size=20 maxlength="100" dataType="Require" require="true" msg="文件号不能为空！" class="BigInput">
      </TD>
      <TD class="TableData">案卷名称：</TD>
      <TD class="TableData">
       <INPUT name="rollName" id="rollName" size=30 maxlength="100" class="BigInput">
      </TD>
  </TR>
  <TR>
      <TD class="TableData">所属卷库：</TD>
      <TD class="TableData">
	<select name="roomId" id="roomId">
	<option value="" ></option>
	</select>
      </TD>
      <TD class="TableData">归卷年代：</TD>
      <TD class="TableData">
        <input type="text" name="years" id="years" value="" size="10" maxlength="50" class="BigInput">
      </TD>
  </TR>
  <TR>
      <TD class="TableData">起始日期：</TD>
      <TD class="TableData">
        <input type="text" name="beginDate0" id="beginDate0" size="10" maxlength="10" class="BigInput" value="">
        <img id="beginDateImg0" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
		-
        <input type="text" name="beginDate1" id="beginDate1" size="10" maxlength="10" class="BigInput" value="">
        <img id="beginDateImg1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </TD>
      <TD class="TableData">终止日期：</TD>
      <TD class="TableData">
        <input type="text" name="endDate0" id="endDate0" size="10" maxlength="10" class="BigInput" value="">
        <img id="endDateImg0" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
        -
		<input type="text" name="endDate1" id="endDate1" size="10" maxlength="10" class="BigInput" value="">
        <img id="endDateImg1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </TD>
  </TR>
   <TR>
      <TD class="TableData">保管期限：</TD>
      <TD class="TableData">
       <INPUT name="deadline0" id="deadline0" size=10 class="BigInput">
	   -
       <INPUT name="deadline1" id="deadline1" size=10 class="BigInput">
      </TD>
      <TD class="TableData">案卷密级：</TD>
      <TD class="TableData">
	<select name="secret" id="secret">
	  <option value=""></option>
	</select>
      </TD>
  </TR>
   <TR>
      <TD class="TableData">全 宗 号：</TD>
      <TD class="TableData">
        <input type="text" name="categoryNo" id="categoryNo" value="" size="10" maxlength="50" class="BigInput">
      </TD>
      <TD class="TableData">目 录 号：</TD>
      <TD class="TableData">
        <input type="text" name="catalogNo" id="catalogNo" value="" size="10" maxlength="50" class="BigInput">
      </TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">档案馆号：</TD>
      <TD class="TableData">
        <input type="text" name="archiveNo" id="archiveNo" value="" size="10" maxlength="50" class="BigInput">
      </TD>
      <TD class="TableData">保险箱号：</TD>
      <TD class="TableData">
        <input type="text" name="boxNo" id="boxNo" value="" size="10" maxlength="50" class="BigInput">
     </TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">缩 微 号：</TD>
      <TD class="TableData">
        <input type="text" name="microNo" id="microNo" value="" size="10" maxlength="50" class="BigInput">
      </TD>
      <TD class="TableData">凭证类别：</TD>
      <TD class="TableData">
	<select name="certificateKind" id="certificateKind">
	  <option value=""></option>
	</select>
     </TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">凭证编号(起)：</TD>
      <TD class="TableData">
        <input type="text" name="certificateStart0" id="certificateStart0" value="" size="10" maxlength="50" class="BigInput">
        -
		<input type="text" name="certificateStart1" id="certificateStart1" value="" size="10" maxlength="50" class="BigInput">
      </TD>
      <TD class="TableData">凭证编号(止)：</TD>
      <TD class="TableData">
        <input type="text" name="certificateEnd0" id="certificateEnd0" value="" size="10" maxlength="50" class="BigInput">
		-
		<input type="text" name="certificateEnd1" id="certificateEnd1" value="" size="10" maxlength="50" class="BigInput">
      </TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">页    数：</TD>
      <TD class="TableData">
        <input type="text" name="rollPage0" id="rollPage0" value="" size="10" maxlength="50" class="BigInput" dataType="Number" require="false" msg="页数必须是数字！">
        -
		<input type="text" name="rollPage01" id="rollPage1" value="" size="10" maxlength="50" class="BigInput" dataType="Number" require="false" msg="页数必须是数字！">
      </TD>
      <TD class="TableData">所属部门：</TD>
      <TD class="TableData">
	<select name="deptId" id="deptId">
	<option value="" ></option>
	</select>
      </TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">备注：</TD>
      <TD class="TableData" colSpan=3><input type="text" name="remark" id="remark" value="" size="50" maxlength="100" class="BigInput"></TD>
   </TR>
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
        <input type="button" value="查询" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>
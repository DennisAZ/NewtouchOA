<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑案卷</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">

function doInit(){
　var beginParameters = {
    inputId:'beginDate',
    property:{isHaveTime:false}
    ,bindToBtn:'beginDateImg'
};
new Calendar(beginParameters);

var endParameters = {
    inputId:'endDate',
    property:{isHaveTime:false}
    ,bindToBtn:'endDateImg'
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

  var url = "<%=contextPath%>/yh/subsys/oa/rollmanage/act/YHRmsRollAct/getRmsRollDetail.act?seqId=${param.seqId}";
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    var data = json.rtData;
    bindJson2Cntrl(json.rtData);
    $('beginDate').value = data.beginDate.substr(0, 10);
    $('endDate').value = data.endDate.substr(0, 10);
    if (data.priority != 0){
    $('priority').value=data.priority;
    } else{
    $('priority').value="";
    }
    if($("manager") && $("manager").value.trim()){
      bindDesc([{cntrlId:"manager", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
    }
    deptFunc(data.deptId);
  }else{
    alert(rtJson.rtMsrg); 
  }
  
}

function deptFunc(deptId){
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
    if(deptId && (deptId == prc.value)){
      option.selected = true;
    }
  }
  return userId;
}

function doSubmit(){
  if($("rollCode").value.trim() == ""){ 
    alert("案卷号不能为空！");
    $("rollCode").focus();
    $("rollCode").select();
	return false;
  }

  if($("rollName").value.trim() == ""){ 
    alert("案卷名称不能为空！");
    $("rollName").focus();
    $("rollName").select();
	return false;
  }

  if($("beginDate").value && $("endDate").value){
    if($("beginDate").value > $("endDate").value){
      alert("错误, 起始日期不能大于终止日期！");
      $("beginDate").focus(); 
      $("beginDate").select(); 
      return false;
    }
  }
  
  var beginDate = $("beginDate").value;
  if(beginDate){
    if(!isValidDateStr(beginDate)){
      alert("错误,起始日期格式不对，应形如 1999-01-02");
      $("beginDate").focus();
      $("beginDate").select();
      return false;
    }
  }

  var endDate = $("endDate").value;
  if(endDate){
    if(!isValidDateStr(endDate)){
      alert("错误,终止日期格式不对，应形如 1999-01-02");
      $("endDate").focus();
      $("endDate").select();
      return false;
    }
  }
  if ($("priority").value!=""){
	if (!isNum($("priority").value)){
	    alert("优先级设置必须为正整数！");
		$("priority").focus();
		$("priority").select();
        return (false);
	}
	}
	 if ($("rollPage").value!=""){
	if (!isNum($("rollPage").value)){
	    alert("页数必须为正整数！");
		$("rollPage").focus();
		$("rollPage").select();
        return (false);
	}
	}
 var pars = Form.serialize($('form1'));
  
  var url = "<%=contextPath %>/yh/subsys/oa/rollmanage/act/YHRmsRollAct/update.act";
  var json = getJsonRs(url,pars);
  if(json.rtState == "0"){
    location = "<%=contextPath %>/subsys/oa/rollmanage/manage.jsp";
  }else{
    alert("编辑案卷失败！");
  }
}
function isNum(num){
   var reNum=/^[1-9]+\d*$/;
   return(reNum.test(num));
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3"> 编辑案卷</span>
    </td>
  </tr>
</table>


  <form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
  <table class="TableBlock" width="95%" align="center"><TR>
      <TD class="TableData">案 卷 号：<font style='color:red'>*</font></TD>
      <TD class="TableData">
       <INPUT name="rollCode" id="rollCode" size=20 maxlength="100" dataType="Require" require="true" msg="文件号不能为空！" class="BigInput">
      </TD>
      <TD class="TableData">案卷名称：<font style='color:red'>*</font></TD>
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
        <input type="text" name="beginDate" id="beginDate" size="10" maxlength="10" class="BigInput" value="">
        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </TD>
      <TD class="TableData">终止日期：</TD>
      <TD class="TableData">
        <input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="">
        <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </TD>
  </TR>
   <TR>
      <TD class="TableData">所属部门：</TD>
      <TD class="TableData">
	<select name="deptId" id="deptId">
	<option value="" ></option>
	</select>
      </TD>
      <TD class="TableData">编制机构：</TD>
      <TD class="TableData">
        <input type="text" name="editDept" id="editDept" value="" size="20" maxlength="50" class="BigInput">
      </TD>
  </TR>
   <TR>
      <TD class="TableData">保管期限：</TD>
      <TD class="TableData">
       <INPUT name="deadline" id="deadline" size=10 class="BigInput">
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
        <input type="text" name="certificateStart" id="certificateStart" value="" size="10" maxlength="50" class="BigInput">
      </TD>
      <TD class="TableData">凭证编号(止)：</TD>
      <TD class="TableData">
        <input type="text" name="certificateEnd" id="certificateEnd" value="" size="10" maxlength="50" class="BigInput">
      </TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">页    数：</TD>
      <TD class="TableData">
        <input type="text" name="rollPage" id="rollPage" value="" size="10" maxlength="50" class="BigInput" dataType="Number" require="false" msg="页数必须是数字！">
      </TD>
      <TD nowrap class="TableData">备注：</TD>
      <TD class="TableData"><input type="text" name="remark" id="remark" value="" size="30" maxlength="100" class="BigInput"></TD>
  </TR>
  <TR>
      <TD nowrap class="TableData">案卷管理员：</TD>
      <TD class="TableData" >
        <input type="hidden" name="manager" id="manager"> 	
        <input type="text" name="managerDesc" id="managerDesc" size="13" class="BigStatic" readonly>&nbsp;
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['manager', 'managerDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('manager').value='';$('managerDesc').value='';">清空</a>
      </TD>
       <td nowrap class="TableData">设置档案分类优先级
      </td>
      <td class="TableData">
         <input type="text" name="priority" id="priority" size="10" maxlength="20" class="BigInput">
      &nbsp;<span style="font-size:12px">（数字越低，优先级越高）</span>
      </td>
  </TR>    
    <tr align="center" class="TableControl">
      <td colspan="4" nowrap>
        <input type="hidden" name="OP" value="">
        <input type="hidden" name="seqId" id="seqId">  
        <input type="button" value="保存" class="BigButton" onClick="doSubmit();">&nbsp;&nbsp;
        <input type="reset" value="重置" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
  </table>
</form>
</body>
</html>
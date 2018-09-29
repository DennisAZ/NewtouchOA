<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询用户</title>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/person/js/personUtil.js"></script>
<script type="text/Javascript"> 
function doInit(){
  deptFunc();
  var mgrs = new SelectMgr();
  mgrs.addSelect({cntrlId: "userPriv"
               , tableName: "USER_PRIV"
               , codeField: "SEQ_ID"
               , nameField: "PRIV_NAME"
               , value: "0"
               , isMustFill: "0"
               , filterField: ""
               , filterValue: ""
               , order: "PRIV_NO asc"
               , reloadBy: ""
               , actionUrl: ""
               ,extData:""
  });
  mgrs.loadData();
  mgrs.bindData2Cntrl();

  var mgr = new SelectMgr();
  mgr.addSelect({cntrlId: "dutyType"
               , tableName: "oa_attendance_conf"
               , codeField: "SEQ_ID"
               , nameField: "DUTY_NAME"
               , value: "0"
               , isMustFill: "0"
               , filterField: "DUTY_TYPE"
               , filterValue: ""
               , order: ""
               , reloadBy: ""
               , actionUrl: ""
               ,extData:""
  });
  mgr.loadData();
  mgr.bindData2Cntrl();
}

function CheckForm(){
  var query = "";
  query = $("form1").serialize();
  location = "<%=contextPath%>/crm/customer/person/search.jsp?"+query;
  
}

function deptFunc(){
  var url = "<%=contextPath%>/yh/core/funcs/system/sealmanage/act/YHSealAct/selectDeptToAttendance.act";
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
  var option = document.createElement("option"); 
  option.value = 0; 
  option.innerHTML = "离职人员/外部人员"; 
  selects.appendChild(option);
  return userId;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;查询用户</span>
    </td>
  </tr>
</table>
<br/>
<form action="" method="post" name="form1" id="form1">
  <table class="TableBlock" width="90%" align="center">
   <tr>
    <td nowrap class="TableData" width="120">用户名：</td>
    <td nowrap class="TableData">
        <input type="text" name="userId" id="userId" class="BigInput" size="20" maxlength="20">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">真实姓名：</td>
    <td nowrap class="TableData">
        <input type="text" name="userName" id="userName" class="BigInput" size="10" maxlength="10">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">别名：</td>
    <td nowrap class="TableData">
        <input type="text" name="byname" id="byname" class="BigInput" size="10" maxlength="10">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">性别：</td>
    <td nowrap class="TableData">
        <select name="sex" id="sex" class="BigSelect">
        <option value=""></option>
        <option value="0">男</option>
        <option value="1">女</option>
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">部门：</td>
    <td nowrap class="TableData">
        <select name="deptId" id="deptId" class="BigSelect">
        <option value=""></option>
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">角色：</td>
    <td nowrap class="TableData">
        <select name="userPriv" id="userPriv" class="BigSelect">
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData" width="120">管理范围：</td>
    <td nowrap class="TableData">
        <select name="postPriv" id="postPriv" class="BigSelect">
          <option value=""></option>
          <option value="0">本部门</option>
          <option value="1">全体</option>
          <option value="2">指定部门</option>
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">是否允许登录OA系统：</td>
    <td nowrap class="TableData">
    	  <select name="notLogin" id="notLogin" class="BigSelect">
          <option value="2"></option>
          <option value="0">允许</option>
          <option value="1">禁止</option>
        </select>
      </td>
   </tr>
   <tr>
    <td nowrap class="TableData">是否允许查看用户列表：</td>
    <td nowrap class="TableData">
    	  <select name="notViewUser" id="notViewUser" class="BigSelect">
          <option value="2"></option>
          <option value="0">允许</option>
          <option value="1">禁止</option>
        </select>
     </td>
   </tr>
   <tr>
    <td nowrap class="TableData">是否允许显示桌面：</td>
    <td nowrap class="TableData">
    	  <select name="notViewTable" id="notViewTable" class="BigSelect">
          <option value="2"></option>
          <option value="0">允许</option>
          <option value="1">禁止</option>
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap class="TableData">考勤排班类型：</td>
    <td nowrap class="TableData">
        <select name="dutyType" id="dutyType" class="BigSelect">
        <option value=""></option>
        </select>
    </td>
   </tr>
   <tr style="display:none">
    <td nowrap class="TableData" width="120">按最后登录时间排序：</td>
    <td nowrap class="TableData">
        <select name="lastVisitTime" id="lastVisitTime" class="BigSelect">
          <option value=""></option>
          <option value="desc">降序</option>
          <option value="asc">升序</option>
        </select>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
        <input type="button" value="查询" class="BigButton" onclick="CheckForm();" title="查询用户" name="button">&nbsp;&nbsp;
    </td>
  </table>
</form>
<br>
</body>
</html>
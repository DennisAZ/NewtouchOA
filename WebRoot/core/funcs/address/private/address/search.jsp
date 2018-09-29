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
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>通讯簿</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
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
  mgr.addSelect({cntrlId: "groupId"
    , tableName: "OA_ADDRESS_TEAM"
      , codeField: "SEQ_ID"
        , nameField: "GROUP_NAME"
          , value: "ALL"
            , isMustFill: "1"
            , filterField: "USER_ID"
              , filterValue: '[no]'
                , order: ""
                  , reloadBy: ""
                    , actionUrl: ""
                      ,extData:[new CodeRecord("ALL","所有"),new CodeRecord("0","默认")]
                      });
  mgr.loadData();
  mgr.bindData2Cntrl();
}

function checkDate(cntrlId){
  var dateStr = $(cntrlId).value;
  return isValidDateStr(dateStr) ;
}

function commitSearch(){
  //alert(document.getElementById("groupId").value)
  var groupId = document.getElementById("groupId").value;
  var psnName = document.getElementById("psnName").value;
  var sex = document.getElementById("sex").value;
  var beginDate = document.getElementById("beginDate").value;
  var endDate = document.getElementById("endDate").value;
  var nickName = document.getElementById("nickName").value;
  var deptName = document.getElementById("deptName").value;
  var telNoDept = document.getElementById("telNoDept").value;
  var addDept = document.getElementById("addDept").value;
  var telNoHome = document.getElementById("telNoHome").value;
  var addHome = document.getElementById("addHome").value;
  var mobileNo = document.getElementById("mobileNo").value;
  var notes = document.getElementById("notes").value;
  
  if(checkDate("beginDate") == false){
    $("beginDate").focus();
    $("beginDate").select();
    alert("日期格式不对，请输入形：2010-01-01");
    return;
  }

  if(checkDate("endDate") == false){
    $("endDate").focus();
    $("endDate").select();
    alert("日期格式不对，请输入形：2010-01-01");
    return;
  }
  location = "<%=contextPath %>/core/funcs/address/private/address/searchsubmit.jsp?psnName="+encodeURIComponent(psnName)+"&sex="+sex+"&nickName="+encodeURIComponent(nickName)+"&deptName="+encodeURIComponent(deptName)+"&telNoDept="+encodeURIComponent(telNoDept)+"&addDept="+encodeURIComponent(addDept)+"&telNoHome="+encodeURIComponent(telNoHome)+"&addHome="+encodeURIComponent(addHome)+"&notes="+encodeURIComponent(notes)+"&mobileNo="+encodeURIComponent(mobileNo)+"&groupId="+groupId+"&beginDate="+beginDate+"&endDate="+endDate;
  
}

</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/address.gif" align="absmiddle"><span class="big3">&nbsp;联系人查询</span>
    </td>
  </tr>
</table>
<br>

<form name="form1" id="form1" method="post">
  <table class="TableBlock"  width="400"  align="center">
    <tr>
    <td nowrap class="TableData">分组：</td>
    <td nowrap class="TableData">
        <select name="groupId" class="BigSelect" id="groupId">
        </select>
    </td>
   </tr>
    <tr>
      <td nowrap class="TableData">姓名：</td>
      <td class="TableData"><input type="text" name="psnName" id="psnName" size="10" class="BigInput"></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 性别：</td>
      <td class="TableData">
        <select name="sex" id="sex" class="BigSelect">
          <option value="ALL">所有</option>
          <option value="0">男</option>
          <option value="1">女</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">生日：</td>
      <td class="TableData">
      	从

      	<input type="text" name="beginDate" id="beginDate" size="10" class="BigInput">
        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
        至

        <input type="text" name="endDate" id="endDate" size="10" class="BigInput">
        <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
     </td>
    </tr>      
    <tr>
      <td nowrap class="TableData"> 昵称：</td>
      <td class="TableData">
        <input type="text" name="nickName" id="nickName" size="25" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 单位名称：</td>
      <td class="TableData">
        <input type="text" name="deptName" id="deptName" size="25" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">工作电话：</td>
      <td class="TableData">
        <input type="text" name="telNoDept" id="telNoDept" size="25" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">单位地址：</td>
      <td class="TableData">
        <input type="text" name="addDept" id="addDept" size="25" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 家庭电话：</td>
      <td class="TableData">
        <input type="text" name="telNoHome" id="telNoHome" size="25" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 家庭住址：</td>
      <td class="TableData">
        <input type="text" name="addHome" id="addHome" size="25" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 手机：</td>
      <td class="TableData">
        <input type="text" name="mobileNo" id="mobileNo" size="25" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 备注：</td>
      <td class="TableData">
        <input type="text" name="notes" id="notes" size="25" class="BigInput">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableControl" colspan="2" align="center">
          <input type="button" value="查询" class="BigButton" title="进行查询" name="button" OnClick="commitSearch()">
      </td>
    </tr>
  </table>
    </form>
</body>
</html>
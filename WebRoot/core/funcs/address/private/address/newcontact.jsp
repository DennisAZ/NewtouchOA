<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*,yh.core.funcs.system.censorwords.data.YHCensorWords" %>
<%@ page import="yh.core.data.YHRequestDbConn, yh.core.global.YHBeanKeys" %>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新建联系人</title>
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
var loginUserId = <%=loginUser.getSeqId()%>;
var seqId = "<%=seqId%>";
function doInit(){
  var beginParameters = {
      inputId:'birthday',
      property:{isHaveTime:false}
      ,bindToBtn:'birthdayImg'
  };
  new Calendar(beginParameters);
  
  var mgr = new SelectMgr();
  mgr.addSelect({cntrlId: "groupId"
    , tableName: "OA_ADDRESS_TEAM"
      , codeField: "SEQ_ID"
        , nameField: "GROUP_NAME"
          , value: "<%=seqId%>", isMustFill: "1"
            , filterField: "USER_ID"
              , filterValue: '<%=loginUser.getSeqId()%>'
                , order: ""
                  , reloadBy: ""
                    , actionUrl: ""
                      ,extData:[new CodeRecord("0","默认")]
                      });
  mgr.loadData();
  mgr.bindData2Cntrl();
}

function checkStr(str){ 
  var re=/["']/; 
  return str.match(re); 
}

function commit(){
  var flag = "0";
  var groupFlag = "";
  var reg = /^[0-9]*$/;
  var psnNo = document.getElementById("psnNo");
  if(seqId == 0){
    groupFlag = 0;
  }
  if(!reg.test(psnNo.value)){
  	alert("排序号只能输入数字！");
  	psnNo.focus();
  	psnNo.select();
  	return false;
  }
  if($("psnName").value == ""){ 
    alert("联系人姓名不能为空！");
    $("psnName").focus();
	return false;
  }
  if(checkStr($("psnName").value)){
    alert("您输入的姓名含有'双引号'或者'单引号'请从新填写");
    $('psnName').focus();
    $('psnName').select();
    return false;
  }
  var birthday = $("birthday").value;
  if(birthday){
    if(!isValidDateStr(birthday)){
      alert("错误,生日时间格式不对，应形如 1999-01-02");
      $("birthday").focus();
      $("birthday").select();
      return false;
    }else {
      d1Arr=birthday.split('-');
      v1=new Date(d1Arr[0],d1Arr[1] - 1,d1Arr[2]);
      var now = new Date();
      v2 = new Date(now.getFullYear(),now.getMonth(),now.getDate());
      if (v1.getTime() >= v2.getTime()) {
        alert("生日不能大于当前时间");
      return false;
      }
    }
  }
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/insertContactPerson.act?seqId="+seqId;
  var rtJson = getJsonRs(url, mergeQueryString($("form1")));
  if(rtJson.rtState == "0"){
    parent.menu.location.reload();
    location = "<%=contextPath %>/core/funcs/address/private/address/index.jsp?groupId="+seqId+"&userId="+flag+"&groupFlag="+groupFlag;
  }else{
	alert(rtJson.rtMsrg);
  }
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/address.gif" align="absmiddle"><span class="big3">&nbsp;新建联系人</span>
    </td>
  </tr>
</table>
<table class="TableBlock"  width="450" align="center">
  <form method="post" name="form1" id="form1">
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.address.data.YHAddress"/>
  <input type="hidden" id="seqId" name="seqId" value=""/>
        <tr>
      <td nowrap class="TableHeader" colspan="2"><b>&nbsp;分组</b></td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 分组：</td>
      <td class="TableData">
        <select name="groupId" id="groupId" class="BigSelect">
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableHeader" colspan="2"><b>&nbsp;个人信息</b></td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 排序号：</td>
      <td class="TableData">
        <input type="text" name="psnNo" id="psnNo" size="12" maxlength="50" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 姓名：</td>
      <td class="TableData">
        <input type="text" name="psnName" id="psnName" size="20" maxlength="50" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 性别：</td>
      <td class="TableData">
        <select name="sex" id="sex" class="BigSelect">
          <option value=""></option>
          <option value="0">男</option>
          <option value="1">女</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData">生日：</td>
      <td class="TableData">
        <input type="text" name="birthday" id="birthday" size="10" maxlength="10" class="BigInput" value="">
        <img id="birthdayImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 昵称：</td>
      <td class="TableData">
        <input type="text" name="nickName" id="nickName" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 职务：</td>
      <td class="TableData">
        <input type="text" name="ministration" id="ministration" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 配偶：</td>
      <td class="TableData">
        <input type="text" name="mate" id="mate" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData"> 子女：</td>
      <td class="TableData">
        <input type="text" name="child" id="child" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableHeader" colspan="2"><b>&nbsp;联系方式（单位）</b></td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 单位名称：</td>
      <td class="TableData">
        <input type="text" name="deptName" id="deptName" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData">单位地址：</td>
      <td class="TableData">
        <input type="text" name="addDept" id="addDept" size="40" maxlength="100" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 单位邮编：</td>
      <td class="TableData">
        <input type="text" name="postNoDept" id="postNoDept" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 工作电话：</td>
      <td class="TableData">
        <input type="text" name="telNoDept" id="telNoDept" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 工作传真：</td>
      <td class="TableData">
        <input type="text" name="faxNoDept" id="faxNoDept" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableHeader" colspan="2"><b>&nbsp;联系方式（家庭）</b></td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 家庭住址：</td>
      <td class="TableData">
        <input type="text" name="addHome" id="addHome" size="40" maxlength="100" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 家庭邮编：</td>
      <td class="TableData">
        <input type="text" name="postNoHome" id="postNoHome" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 家庭电话：</td>
      <td class="TableData">
        <input type="text" name="telNoHome" id="telNoHome" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 手机：</td>
      <td class="TableData">
        <input type="text" name="mobilNo" id="mobilNo" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 小灵通：</td>
      <td class="TableData">
        <input type="text" name="bpNo" id="bpNo" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> 电子邮件：</td>
      <td class="TableData">
        <input type="text" name="email" id="email" size="25" maxlength="80" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> QQ号码：</td>
      <td class="TableData">
        <input type="text" name="oicqNo" id="oicqNo" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData"> MSN：</td>
      <td class="TableData">
        <input type="text" name="icqNo" id="icqNo" size="25" width="60" class="BigInput" value="">
      </td>
    </tr>

    <tr>
      <td nowrap class="TableHeader" colspan="2"><b>&nbsp;备 注</b></td>
    </tr>
    <tr>
      <td class="TableData" colspan="2">
        <textarea cols="60" name="notes" id="notes" rows="5" class="BigInput" wrap="on"></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" value="保存" class="BigButton" OnClick="commit()">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="window.history.back();">
      </td>
    </tr>
  </table>
  <!-- 
<br><center><input type="button" class="BigButton" value="返回" onClick="location='index.php?GROUP_ID=<?=$GROUP_ID?>'"></center>
-->
</body>
</html>
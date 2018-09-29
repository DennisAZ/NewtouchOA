<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="yh.core.funcs.person.data.YHPerson" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page import="java.text.*" %>
<%
  YHPerson loginUser = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
  String seqId = request.getParameter("seqId");
  if (seqId == null){
    seqId = "";
  }
  
  String groupId = request.getParameter("groupId");
  if (groupId == null){
    groupId = "";
  }
  String groupName = request.getParameter("groupName");
  groupName = YHUtility.encodeSpecial(groupName);
  if (groupName == null){
    groupName = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>编辑联系人</title>
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
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var groupIds = "<%=groupId%>";
var groupName = "<%=groupName%>";

function doInit(){
  var beginParameters = {
      inputId:'birthday',
      property:{isHaveTime:false}
      ,bindToBtn:'birthdayImg'
  };
  new Calendar(beginParameters);

  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getEditContactPerson.act";
  var rtJson = getJsonRs(url, "seqId="+seqId);
  if(rtJson.rtState == "0"){
    bindJson2Cntrl(rtJson.rtData);
    var birthdays = document.getElementById("birthday").value;
    var Strs = birthdays.substr(0,10);
    document.getElementById("birthday").value = Strs;
  }else{
  	alert(rtJson.rtMsrg); 
  }

  var mgr = new SelectMgr();
  mgr.addSelect({cntrlId: "groupId"
    , tableName: "OA_ADDRESS_TEAM"
      , codeField: "SEQ_ID"
        , nameField: "GROUP_NAME"
          , value: "<%=groupId%>"
            , isMustFill: "1"
            , filterField: "USER_ID"
              , filterValue: '<%=loginUser.getSeqId()%>' //'[no]'
                , order: ""
                  , reloadBy: ""
                    , actionUrl: ""
				      ,extData:[new CodeRecord("0","默认")]
                      });
  mgr.loadData();
  mgr.bindData2Cntrl();
}

function commit(){
  var flag = "0";
  var groupFlag = "0";
  var reg = /^[0-9]*$/;
  var psnNo = document.getElementById("psnNo");
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
  var birthday = $("birthday").value;
  if($("birthday").value){
    if(!isValidDateStr(birthday)){
      alert("错误,生日时间格式不对，应形如 1999-01-02");
      return false;
    }
  }
  //alert(document.getElementById("groupId").value);
  var groupId = document.getElementById("groupId").value;
  var psnName = document.getElementById("psnName").value;
  var sex = document.getElementById("sex").value;
  var nickName = document.getElementById("nickName").value;
  var birthday = document.getElementById("birthday").value;
  var ministration = document.getElementById("ministration").value;
  var mate = document.getElementById("mate").value;
  var child = document.getElementById("child").value;
  var deptName = document.getElementById("deptName").value;
  var addDept = document.getElementById("addDept").value;
  var postNoDept = document.getElementById("postNoDept").value;
  var telNoDept = document.getElementById("telNoDept").value;
  var faxNoDept = document.getElementById("faxNoDept").value;
  var addHome = document.getElementById("addHome").value;
  var postNoHome = document.getElementById("postNoHome").value;
  var telNoHome = document.getElementById("telNoHome").value;
  var mobilNo = document.getElementById("mobilNo").value;
  var bpNo = document.getElementById("bpNo").value;
  var email = document.getElementById("email").value;
  var oicqNo = document.getElementById("oicqNo").value;
  var icqNo = document.getElementById("icqNo").value;
  var notes = document.getElementById("notes").value;
  var psnNo = document.getElementById("psnNo").value;
  var url = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/updateEditContactPerson.act?seqId="+seqId;
  var rtJson = getJsonRs(url, "groupId="
      +groupId+"&psnName="+encodeURIComponent(psnName)+"&sex="+sex+"&nickName="
      +encodeURIComponent(nickName)+"&birthday="+birthday+"&ministration="+encodeURIComponent(ministration)+"&mate="
      +encodeURIComponent(mate)+"&child="+encodeURIComponent(child)+"&deptName="+encodeURIComponent(deptName)+"&addDept="+encodeURIComponent(addDept)+"&postNoDept="
      +encodeURIComponent(postNoDept)+"&telNoDept="+encodeURIComponent(telNoDept)+"&faxNoDept="+encodeURIComponent(faxNoDept)+"&addHome="
      +encodeURIComponent(addHome)+"&postNoHome="+encodeURIComponent(postNoHome)+"&telNoHome="+encodeURIComponent(telNoHome)+"&mobilNo="
      +encodeURIComponent(mobilNo)+"&bpNo="+encodeURIComponent(bpNo)+"&email="+encodeURIComponent(email)+"&oicqNo="+encodeURIComponent(oicqNo)+"&icqNo="
      +encodeURIComponent(icqNo)+"&notes="+encodeURIComponent(notes)+"&psnNo="+encodeURIComponent(psnNo));
  if(rtJson.rtState == "0"){
    parent.menu.location.reload();
    groupFlag = groupId;
    location = "<%=contextPath %>/core/funcs/address/private/address/index.jsp?seqId="+seqId+"&groupId="+groupId+"&groupName="+encodeURIComponent(groupName)+"&userId="+flag+"&groupFlag="+groupFlag;
    //parent.addressmain.location.reload();
  }else{
	alert(rtJson.rtMsrg);
  }
}

function returnIndex(){
  var groupFlag = "";
  var userId = "0";
  if(groupIds == "0"){
    groupFlag = "0";
  }
  location = "<%=contextPath %>/core/funcs/address/private/address/index.jsp?seqId="+seqId+"&groupId="+groupIds+"&groupFlag="+groupFlag+"&userId="+userId;
}
</script>
</head>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/address.gif" align="absmiddle"><span class="big3">&nbsp;编辑联系人</span>
    </td>
  </tr>
</table>

<br>
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
    <tr>
      <td class="TableData" colspan="4">
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="hidden" value="<?=$ADD_ID?>" name="ADD_ID">
        <input type="button" value="保存" class="BigButton" OnClick="commit()">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" OnClick="returnIndex()">
      </td>
    </tr>
  </table>
</form>

</body>
</html>
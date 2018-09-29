<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ page language="java" import="oa.core.funcs.bbs.act.BbsService,oa.core.funcs.bbs.act.BbsBoard" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html><head><script type="text/javascript">
/** 常量定义 **/
var TDJSCONST = {
  YES: 1,
  NO: 0
};

/** 变量定义 **/
var contextPath = "/yh";
var imgPath = "/yh/core/styles/style1/img";
var ssoUrlGPower = "";
var limitUploadFiles = "jsp,java,jspx,exe"
var signFileServiceUrl = "http://192.168.0.5:9000/BjfaoWeb/TitleSign";
var isOnlineEval = "0";
var useSearchFunc = "1";
var maxUploadSize = 500;
var isDev = "0";
var ostheme = "1";
</script> 

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>编辑版块</title>
<link rel="stylesheet" href="/yh/core/styles/style1/css/style.css">
<link rel="stylesheet" href="/yh/core/styles/style1/css/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/Javascript" src="/yh/core/js/sys.js"></script>
<script type="text/Javascript" src="/yh/core/js/prototype.js"></script>
<script type="text/Javascript" src="/yh/core/js/smartclient.js"></script>
<script type="text/Javascript" src="/yh/core/js/cmp/select.js"></script>
<script type="text/javascript" src="/yh/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="/yh/core/js/orgselect.js"></script>
<script type="text/javascript">
var isDemo="0";
//alert(isDemo);

function checkForm(){
	if($("dept").value.trim()=="" && $("role").value.trim()=="" && $("user").value.trim()==""){
		alert("请至少指定一种发布范围!");
		return(false);
	}
	
	return true;	
}

function checkStr(str){ 
	var re=/["\/\\:*?"<>|]/; 
	return str.match(re); 
}




</script>
</head>
<body>
<%BbsBoard bb=new BbsService().getBBSBoardByBoardId(request); %>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tbody><tr>
    <td class="Big"><img src="/yh/core/funcs/system/picture/images/sys_config.gif" width="22" height="20" align="absmiddle"><span class="big3"> 新建版块</span>
    </td>
  </tr>
</tbody></table>


<br>
<form action="addBoard.jsp" method="post" name="form1" id="form1" onsubmit="return CheckForm();">
<input type="hidden" value="<%=bb.getBoarderId()%>" name="boardId">
 <table class="TableBlock" width="85%" align="center">
    <tbody>
       <tr>
      <td nowrap="" class="TableData"> 排序号：</td>
      <td class="TableData">
        <input type="text" name="NO" id="bbsNo" size="36" maxlength="100" class="BigInput" value="<%=bb.getBoardNo()%>">
      </td>
    </tr>
     <tr>
      <td nowrap="" class="TableData"> 讨论区名称：</td>
      <td class="TableData">
        <input type="text" name="bbsName" id="bbsName" size="36" maxlength="100" class="BigInput" value="<%=bb.getBoardName()%>">
      </td>
    </tr>
     <tr>
      <td nowrap="" class="TableData"> 讨论区简介：</td>
      <td class="TableData">
        <input type="text" name="welcome" id="welcome" size="36" maxlength="100" class="BigInput" value="<%=bb.getWelcome()%>">
      </td>
    </tr>
    <tr>
      <td nowrap="" class="TableData">发布范围（部门）：</td>
      <td class="TableData">
        <input type="hidden" name="dept" id="dept" value="<%=bb.getDeptId()%>">
        <textarea cols="40" name="deptDesc" id="deptDesc" rows="2" class="BigStatic" wrap="yes" readonly="">
        <%=bb.getDeptName()%>
        </textarea>
        <a href="javascript:;" class="orgAdd" onclick="selectDept()">添加</a>
     		<a href="javascript:;" class="orgClear" onclick="$('dept').value='';$('deptDesc').value='';">清空</a>    
      </td>
    </tr>
    <tr>
      <td nowrap="" class="TableData">发布范围（角色）：</td>
      <td class="TableData">
        <input type="hidden" name="role" id="role" value="<%=bb.getPrivId()%>">
        <textarea cols="40" name="roleDesc" id="roleDesc" rows="2" class="BigStatic" wrap="yes" readonly=""><%=bb.getPrivName()%></textarea>
        <a href="javascript:;" class="orgAdd" onclick="selectRole();">添加</a>
      	<a href="javascript:;" class="orgClear" onclick="$('role').value='';$('roleDesc').value='';">清空</a> 
      </td>
   </tr>
   <tr>
      <td nowrap="" class="TableData">发布范围（人员）：</td>
      <td class="TableData">
        <input type="hidden" name="user" id="user" value="<%=bb.getUserId()%>">
        <textarea cols="40" name="userDesc" id="userDesc" rows="2" class="BigStatic" wrap="yes" readonly=""><%=bb.getUserName()%></textarea>
        <a href="javascript:;" class="orgAdd" onclick="selectUser();">添加</a>
      	<a href="javascript:;" class="orgClear" onclick="$('user').value='';$('userDesc').value='';">清空</a>
      </td>
   </tr>
      <tr>
      <td nowrap="" class="TableData">斑竹（人员）：</td>
      <td class="TableData">
        <input type="hidden" name="hoster" id="hoster" value="<%=bb.getBoaderHoster()%>">
        <textarea cols="40" name="hosterDesc" id="hosterDesc" rows="2" class="BigStatic" wrap="yes" readonly=""><%=bb.getBoaderHosterName()%></textarea>
        <a href="javascript:;" class="orgAdd" onclick="selectUser(['hoster','hosterDesc']);">添加</a>
      	<a href="javascript:;" class="orgClear" onclick="$('hoster').value='';$('hosterDesc').value='';">清空</a>
      </td>
   </tr>
   <tr>
      <td nowrap="" class="TableData">是否允许匿名帖：</td>
      <td class="TableData">
         <select name="ANONYMITY_YN" class="BigSelect">
         <%if(bb.getAnonymity().equals("1")){ %>
          <option value="1" selected>允许</option>
          <option value="0" >禁止</option>
          <%}else{ %>
          <option value="1" >允许</option>
          <option value="0" selected>禁止</option>
          <%} %>
        </select>
      </td>
   </tr>
   <!--  
    <tr>
      <td nowrap class="TableData">提醒：</td>
      <td class="TableData">
　　　　　　　<input type="checkbox" name="SMS_REMIND" id="SMS_REMIND" checked><label for="SMS_REMIND">使用内部短信提醒</label>&nbsp;&nbsp;      </td>
    　</tr>
    -->
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap="">
        <!-- <input type="button" value="确定" onclick="sendForm();" class="BigButton">&nbsp;&nbsp; -->
        <input type="button" value="确定" onclick="form1.submit()" class="BigButton">&nbsp;&nbsp;
        <input type="button" value="返回" class="BigButton" onclick="window.location.href='/yh/core/funcs/bbs/system/index.jsp'">
      </td>
    </tr>
  </tbody></table>
</form>

</body></html>
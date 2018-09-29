<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<style>
.pgPanel {
  display:none;
}
</style>
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var pageMgr;
function doInit(){
  var requestURL = "<%=contextPath%>/yh/core/funcs/setdescktop/log/act/YHSelfLogAct/getPage.act";
  var cfgs = {
    dataAction: requestURL,
    container: "listDiv",
    colums: [
       {type:"hidden", name:"seqId", text:"ID"},
       {type:"text", name:"userName", text:"用户", width:100,align:'center'},
       {type:"text", name:"time", text:"时间", width:150,align:'center',dataType:'dateTime',format:'yyyy-MM-dd HH:mm:ss'},
       {type:"text", name:"ip", text:"IP地址", width:100,align:'center'},
       {type:"selfdef", text:"IP所在地", width:100,align:'center', render:ipAddress},
       {type:"text", name:"type", text:"类型", width:80,align:'center',render:typeRender},
       {type:"text", name:"remark", text:"备注", width:100,align:'center'}
       ]
  };
  
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
}

function ipAddress() {
  return '';
}

function typeRender(cellData, recordIndex, columInde) {
  switch (cellData) {
  case '1': return "登录日志";
  case '2': return "登录密码错误"; 
  case '3': return "添加部门"; 
  case '4': return "编辑部门"; 
  case '5': return "删除部门"; 
  case '6': return "添加用户"; 
  case '7': return "编辑用户"; 
  case '8': return "删除用户"; 
  case '9': return "非法IP登录"; 
  case '10': return "错误用户名"; 
  case '11': return "admin密码清空"; 
  case '12': return "系统资源回收"; 
  case '13': return "考勤数据管理"; 
  case '14': return "修改登录密码"; 
  case '15': return "公告通知管理"; 
  case '16': return "公共文件柜"; 
  case '17': return "网络硬盘"; 
  case '18': return "软件注册"; 
  case '19': return "用户批量设置"; 
  case '20': return "个人文件柜"; 
  case '21': return "用户KEY验证失败"; 
  case '22': return "退出系统"; 
  case '23': return "员工离职"; 
  case '30': return "培训课程管理"; 
  default: return '';
  }
  
}
</script>
</head>

<body onload="doInit()">
<div class="PageHeader">
<table border="0" width="" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/system.gif" align="absmiddle"><span class="big3"> 最近20条系统安全日志</span><br>

    </td>
  </tr>
</table>
</div>
<div id="listDiv">
</div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.person.data.*" %>
<%@ include file="/core/inc/header.jsp"%>
<%
	request.setCharacterEncoding("UTF-8");
	String CUR_MENU = request.getParameter("CUR_MENU");
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
  String MODULE_ID = request.getParameter("MODULE_ID");
  String USER_SEQ_ID = request.getParameter("USER_SEQ_ID");
  String USER_DEPT = request.getParameter("USER_DEPT");
  String USER_PRIV = request.getParameter("USER_PRIV");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<link rel="stylesheet"	href="<%=cssPath%>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">
var TO_ID = "<%=TO_ID%>";
var TO_NAME = "<%=TO_NAME%>";
var CUR_MENU = "<%=CUR_MENU%>";
var MODULE_ID = "<%=MODULE_ID%>";
var USER_SEQ_ID = "<%=USER_SEQ_ID%>";
var USER_DEPT = "<%=USER_DEPT%>";
var USER_PRIV = "<%=USER_PRIV%>";
var tree;
var index;
function doInit()
{
	var tmpData = loadData();
	var data = {
			panel:'menuList',
			index:2,
			data:[{title:'已选人员', data:[]},
						{title:'按部门选择', action:getTree},
						{title:'按角色选择', data:tmpData},
						{title:'自定义组', data:[]},
						{title:'在线人员', data:[]}]
			};
	var menu = new MenuList(data);
	index = menu.getContainerId(2);
}
function getUserPriv(data)
{
	var urlRequest = '<%=contextPath%>/yh/core/module/priv_select/act/YHPrivSelectAct/selectPriv.act?PRIV_ID=' 
		+ data.privNo + '&TO_ID=' + TO_ID + '&TO_NAME=' + TO_NAME + '&PRIV_NAME=' + data.data 
		+ '&MODULE_ID=' + MODULE_ID + '&USER_SEQ_ID=' + USER_SEQ_ID + '&USER_PRIV=' + USER_PRIV;
	self.parent.user.location = urlRequest;
}
function loadData()
{
	var url = "<%=contextPath%>/yh/core/module/priv_select/act/YHPrivSelectAct/getUserPriv.act?TO_ID=" 
		+ TO_ID + "&TO_NAME=" + TO_NAME;
	var json = getJsonRs(url);
	if(json.rtState == '1')
	{
		alert(json.rtState);
		return;				
	}
	var prcJson = json.rtData;
	var tmp = [];
	if(prcJson.length == 0)
	{
		tmp.push({data:'未定义用户角色'});
	}
	for(var i = 0; i < prcJson.length; i++)
	{
		tmp.push({data:prcJson[i].privName, action:getUserPriv, privNo:prcJson[i].privNo});
	}
	return tmp;
}
function getTree()
{
  var url =  contextPath + "/yh/core/module/dept_select/act/YHDeptSelectAct/getTree.act?DEPT_PAR_ID=";
  tree = new DTree({bindToContainerId:index
    ,requestUrl:url
    ,isOnceLoad:false
    //,checkboxPara:{isHaveCheckbox:false,disCheckedFun:test,checkedFun:test2}
    ,linkPara:{clickFunc:getDeptInfo, linkAddress:'index.jsp?id=', target:'user'},isUserModule:true
    //,treeStructure:{isNoTree:false,regular:'3,2,2,4'}	
  });
  tree.show();
}
function getDeptInfo(id)
{
  var obj = tree.getNode(id);
  var parentId = obj.parentId;
  var deptLocal = encodeURI(obj.name);
  var deptParent = tree.getNode(parentId);
  if(deptParent == null)
  {
    deptParent = '无';
  }
  deptParent = deptParent.name;
  var parentWindowObj = window.parent.dialogArguments;
  var url = '<%=contextPath%>/yh/core/module/dept_select/act/YHDeptSelectAct/selectDept.act?DEPT_PAR_ID=' + id 
  	+ '&deptLocal=' + deptLocal + '&TO_ID=' + TO_ID + '&TO_NAME=' + TO_NAME + '&MODULE_ID=' + MODULE_ID 
  	+ '&USER_SEQ_ID=' + USER_SEQ_ID + '&USER_DEPT=' + USER_DEPT;
  self.parent.user.location = url;
}
//var $ = function(id) {return document.getElementById(id);};

function ShowSelected()
{
   parent.user.location="/yh/core/module/user_select/selected.jsp?TO_ID=<%=TO_ID%>&TO_NAME=<%=TO_NAME%>";
}
var ctroltime=null,key="";
function CheckSend()
{
	var kword = $("kword");
	if(kword.value == "按用户名或姓名搜索...")
	{
	   kword.value = "";
	}
  if(kword.value == "" && $('search_icon').src.indexOf("/yh/core/module/user_select/quicksearch.gif") == -1)
	{
	   $('search_icon').src = "/yh/core/module/user_select/quicksearch.gif";
	}
	if(key != kword.value && kword.value != "")
	{
     key = kword.value;
	   parent.user.location = "/yh/core/module/user_select/query.jsp?" + kword.value;
	   if($('search_icon').src.indexOf("/yh/core/module/user_select/quicksearch.gif") >= 0)
	   {
	   	   $('search_icon').src = "/yh/core/module/user_select/closesearch.gif";
	   	   $('search_icon').title = "清除关键字";
	   	   $('search_icon').onclick = function(){
		   	   	kword.value = '按用户名或姓名搜索...'; 
						$('search_icon').src = "/yh/core/module/user_select/quicksearch.gif";
						$('search_icon').title = "";
						$('search_icon').onclick = null;
					};
	   }
  }
  ctroltime = setTimeout(CheckSend, 100);
}
</script>
</head>

<body class=""  topmargin="1" leftmargin="0" onload="doInit()">
<div style="border:1px solid #000000;margin-left:2px;background:#FFFFFF;">
  <input type="text" id="kword" name="kword" value="按用户名或姓名搜索..." onfocus="ctroltime=setTimeout(CheckSend,100);" onblur="clearTimeout(ctroltime);if(this.value=='')this.value='按用户名或姓名搜索...';" class="SmallInput" style="border:0px; color:#A0A0A0;width:145px;"><img id="search_icon" src="/yh/core/module/user_select/quicksearch.gif" align=absmiddle style="cursor:pointer;">
</div>
<div id="menuList">
</div>

<div id="user_evection" style="display:none;"></div>
<div id="user_leave" style="display:none;"></div>
<div id="user_out" style="display:none;"></div>
</body>
</html>
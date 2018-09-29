<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>公共用户组管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">

function doInit(){
  var count = 0;
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHUserGroupAct/getUserGroup.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var table=new Element('table',{"width":"70%","class":"TableBlock","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader' align='center'>"
				+"<td nowrap align='center' width='200'>用户组名称</td>"
				+"<td nowrap align='center' width='60'>排序号</td>"				
				+"<td nowrap align='center' width='160'>操作</td></tr><tbody>");
		$('listDiv').appendChild(table);
  	for(var i = 0; i < rtJson.rtData.length; i++){
  	  count++;
  	  var seqId = rtJson.rtData[i].seqId;
  	  var groupName = rtJson.rtData[i].groupName;
  	  var orderNo = rtJson.rtData[i].orderNo;
  	  var tr=new Element('tr',{'class':'TableData'});			
			table.firstChild.appendChild(tr);
			tr.update("<td align='center'>"					
				+ groupName + "</td><td align='center'>"	
				+ orderNo + "</td><td align='center'>"
				+ "<a href='edit.jsp?seqId="+seqId+"'>编辑</a>&nbsp;&nbsp;"
				+ "<a href="			
				+ "javascript:deleteSingle('"+seqId+"');"
				+ ">删除   </a>"
				+ "<a href='setuser.jsp?seqId="+seqId+"'>设置用户  </a>"
				+ "</td>"					
			);
  	}
  }else{
  	alert(rtJson.rtMsrg); 
  }
  if(count==0){
    var div = document.getElementById("noCheck");
    div.style.display = "";
    var allTable = document.getElementById("allTable");
    allTable.style.display = "none";
  }
}

function confirmDel() {
  if(confirm("确认要删除该用户组吗？")) {
    return true;
  } else {
    return false;
  }
}

function deleteSingle(seqId){
  if(!confirmDel()) {
  	return ;
  }  
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHUserGroupAct/deleteUserGroup.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}
</script>
</head>

</html>
<body topmargin="5" onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" align="absmiddle"><span class="big3">&nbsp;新建公共用户组</span><br>
    </td>
  </tr>
</table>

<div align="center">
<input type="button" value="新建公共用户组" class="BigButtonC" onClick="location='newgroup.jsp';" title="创建新的公共用户组">
</div>
<br>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3">&nbsp;管理公共用户组</span>
    </td>
  </tr>
</table>

<br>
<div id="allTable" style="display:'';">
<div id="listDiv" align="center"></div>
<br>
<div align="center">
  <input type="button" value="返回" class="BigButton" title="返回" name="button2" OnClick="location='<%=contextPath%>/core/funcs/dept/deptinput.jsp'">
</div>
</div>
<div id="noCheck" style="display:none;">
<table class="MessageBox" align="center" width="280">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">无定义的用户组</div>
    </td>
  </tr>
</table>
</div>
</body>

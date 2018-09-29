<%@ page language="java" contentType="text/html; charset=UTF-8"
 pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<html>
<head>
<title>人力资源管理员设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var requestURI = "<%=contextPath%>/yh/subsys/oa/hr/setting/act/YHHrManagerAct";
function doInit(){
	var url = requestURI + "/selectDeptToAttendance.act";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "1"){
		alert(rtJson.rtMsrg); 
		return ;
	}
	var prcs = rtJson.rtData;
	if(prcs.length>0){
		var table = new Element('table',{"class":"TableList", "width":"80%","align":"center"});
		var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
			+ "<td nowrap width='28%' align='center'>部门名称</td>"
			+ "<td nowrap align='center'>人力资源管理员</td>"
			+ "<td nowrap width='10%' align='center'>操作</td>"
			+ "</tr><tbody>";
		table.update(strTable);
		$("bodyDiv").update(table);
		
		for(var i=0;i<prcs.length;i++){
			var prc = prcs[i];
			var deptId =  prc.value;
			var deptIdDesc = prc.text;
			var userName = getHrManager(deptId);
			var tr=new Element('tr',{'width':'90%','class':'TableData', 'font-size':'10pt'});			
			table.firstChild.appendChild(tr);
			var str = "<td align='left' width='28%' class='TableContent'>" + deptIdDesc + "</td>"
				+ "<td align='left'>" + userName + "</td>"
				+ "<td align='center'><a href=\"javascript:edit(" + deptId + ")\"> 编辑</a></td>"
			tr.update(str);
			
			//var giftOutStr = "";
						//newTrElement(table,[],2,{className:"TableData"},[deptIdDesc,giftOutStr]);
		}
		
	}
}
function getHrManager(deptId){
	var url = requestURI + "/getHrManager.act";
	var rtJson = getJsonRs(url,"deptId=" + deptId);
	var str = "";
	if(rtJson.rtState == "0"){
		var prcs = rtJson.rtData;
		str = prcs.userName;
	}else{
		alert(rtJson.rtMsrg); 
		return ;
	}
	return str
	
}



function newTrElement(tbObj,tbAttri,index,tdAttri,config){
  var currentRows = tbObj.rows.length;//原来的行数
  var mynewrow = tbObj.insertRow(currentRows);
   //tr给属性赋值
  if(tbAttri.className){
    mynewrow.className = tbAttri.className;
  }
  if(tbAttri.width){
    mynewrow.width = tbAttri.width;
  }
  for(var i = 0 ; i<index ;i++){
    var currentCells = mynewrow.cells.length;
    var mynewcell=mynewrow.insertCell(currentCells);
    mynewcell.nowrap = true;
    //td给属性赋值

    if(tdAttri.className){
      mynewcell.className = tdAttri.className;
    }
    if(tdAttri.width){
      mynewcell.width = tdAttri.width;
    }
    if(tdAttri.colSpan){
      mynewcell.colSpan = tdAttri.colSpan;
    }
    if(tdAttri.align){
      mynewcell.align = tdAttri.align;
    }
    mynewcell.innerHTML = config[i];
  }
}


function edit(deptId) {
  window.location = "<%=contextPath%>/subsys/oa/hr/setting/hrManager/edit.jsp?deptId=" + deptId;
}


</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3"
 class="small">
 <tr>
  <td class="Big"><img src="<%=imgPath%>/training.gif" align="absmiddle">
  <span class="big3"> 人力资源管理员设置</span></td>
 </tr>
</table>
<br>
<div id="bodyDiv" align="center"></div>

</body>
</html>
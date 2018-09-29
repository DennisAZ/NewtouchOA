<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>员工薪酬基数列表</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath %>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath %>/menu_left.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript"><!--
var index = "";
var index2 = "";
var groupIdStr = "";
var tree = null;
var statusIndex = "";
function doInit(){
	var data = {
	        //fix:true,
			panel:'menuList',
			index:'1',
			data:[{title:'在职人员'},
						{title:'离职人员/外部人员', action:newStatus}]
			};
	var menu = new MenuList(data);
	index = menu.getContainerId(1);
	statusIndex = menu.getContainerId(2);
	loadData();//离职人员
	menu.showItem(this,{},1);
}
//在职人员
function loadData(){
  $(index).update("");
  tree = new DTree({bindToContainerId:index
    , requestUrl:'<%=contextPath%>/yh/core/module/org_select/act/YHOrgSelectModule/getPersonTree.act?id='
    , isOnceLoad:false
    , treeStructure:{isNoTree:false}
    , linkPara:{clickFunc:getChildOrEdit}
    , isUserModule:true
    , isHaveTitle:true
  });
	tree.show(); 
	tree.open("organizationNodeId");
	var rootNode = tree.getFirstNode();
	var ul = $(tree.ulEncode + rootNode.nodeId);
	if (ul) {
		var firstLi = ul.firstChild;
		if (firstLi) {
			var id = firstLi.id;
			id = id.substring(tree.liEncode.length);
			//alert(id);加载树时打开
			var url = "<%=contextPath%>/subsys/oa/hr/manage/staffInfo/userList1.jsp?deptId=" + id;
			//parent.addressmain.location = url;
		}
	}
}
function getChildOrEdit(id){
	
	var dept = tree.getNode(id);
	var userName = dept.name;
	//alert(userName);
	var userId = dept.extData;
	if(id == "organizationNodeId"){
		return;
	}
	if(id.indexOf('r') == -1){  
		var urls = "<%=contextPath%>/yh/core/funcs/person/act/YHPersonAct/getPostPrivDept.act";
		var rtJsons = getJsonRs(urls, "deptId="+id);
		if(rtJsons.rtState == '0'){
			if(rtJsons.rtData[0].isPriv){
				//var url = "/subsys/oa/hr/salary/staffInsurance/wageList.jsp?deptId="+id;
				var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/staffInsurance/act/YHWageBaseSetAct/getSalItemIdAct.act?deptId="+id ;
				parent.addressmain.location = url;
			}
		}
	}else{
		//人员
		id = id.substring(1, id.length);
		//alert(id + "  userName>"+userName + " userId>>"+userId);
		//alert(encodeURI(userName));
		var url = contextPath+"/yh/subsys/oa/hr/salary/staffInsurance/act/YHWageBaseSetAct/getSalItemIdUserAct.act?personId="+id+ "&userName="+encodeURI(userName);
		parent.addressmain.location = url;
		
	}
}


//未记录所属部门
function newStatus(){
	$(statusIndex).update("");
	var urls = "<%=contextPath%>/yh/subsys/oa/hr/manage/staffInfo/act/YHHrDimissionAct/getNotRecordDeptList.act";
	var rtJson = getJsonRs(urls);
	//alert(rsText);
	if(rtJson.rtState == '0'){
		var prcs = rtJson.rtData;
		if(prcs.length>0){
			var table = "<table class='TableBlock' width='100%' align='center'>";
		var trStr = "";
			for(var i=0;i<prcs.length;i++){
				trStr += "<tr class='TableData' align='center'>"
							+ "<td nowrap width='80'>" + prcs[i].privName + "</td>"
							+ "<td><a href=javascript:showStaffInfo(\"" +  prcs[i].personId + "\",\""+ prcs[i].userName+"\")   >" + prcs[i].userName + "</a></td></tr>"
			}
			table = table + trStr+ "</table>";
			$(statusIndex).update(table);
		}
	}else{
		alert(rtJson.rtMsrg);
	}
  //var parent = window.parent.addressmain;
 // parent.location = "<%=contextPath%>/subsys/oa/hr/manage/staffInfo/batchUpdate.jsp";
}

function showStaffInfo(personId,userName){
	//var parent = window.parent.addressmain;
	var url = contextPath+"/yh/subsys/oa/hr/salary/staffInsurance/act/YHWageBaseSetAct/getSalItemIdUserAct.act?personId="+personId+ "&userName="+encodeURI(userName);
	parent.addressmain.location = url;
	
}
--></script>
</head>
<body topmargin="5" onload="doInit()" scroll="yes">
<div id="menuList">
</div>
</body>
</html>
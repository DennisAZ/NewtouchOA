<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*,yh.core.funcs.person.data.YHPerson" %>
<%@ include file="/core/inc/header.jsp" %>
<%
  request.setCharacterEncoding("UTF-8");
  ArrayList<YHPerson> personList = (ArrayList<YHPerson>)request.getAttribute("personList");
  String managerVal = null;
  if(personList == null){return;}
  for(Iterator it = personList.iterator(); it.hasNext();){
    YHPerson person = (YHPerson)it.next();
    managerVal = person.getUserName();
  }
  String treeId = request.getParameter("treeId");
  if (treeId == null) {
    treeId = "";
  }
  String deptParent = request.getParameter("deptParent");
  if (deptParent == null) {
  	deptParent = "";
  }
  String deptLocal = request.getParameter("deptLocal");
  if (deptLocal == null) {
    deptLocal = "";
  }
  String TO_ID = request.getParameter("TO_ID");
  String TO_NAME = request.getParameter("TO_NAME");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/dept/dept.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">
var TO_ID = "<%=TO_ID%>";
var TO_NAME = "<%=TO_NAME%>";
var deptParent = "<%=deptParent%>";
var deptLocal = "<%=deptLocal%>";
var managerVal = "<%=managerVal%>";

function doInit(){
  alert('hhh');
  var to_id = TO_ID;
  var to_name = TO_NAME;
  var parentWindowObj = window.parent.dialogArguments;
  var ids = parentWindowObj.document.getElementById(to_name).value.split(',');
  var idp = parentWindowObj.document.getElementById(to_id).value.split(',');
  var tableObj = document.getElementById("userTable");
  if(parentWindowObj.document.getElementById(to_name).value != ""){
  for(var i = 3; i < tableObj.rows.length; i++){
    var tablevalue = tableObj.rows[i].cells[0].firstChild.data;
    var tableIdValue = tableObj.rows[i].cells[0].lastChild.value;
    //alert(tableIdValue);
    for(var j = 0; j < idp.length; j++){
      //alert(idp[j]);
  	  if(idp[j] == trim(tableIdValue)){
		tableObj.rows[i].cells[0].style.backgroundColor = "rgb(0, 255, 0)";
		break;
	  }
	}
  }
 }else{
 }
}
function dic(){
  var to_name = TO_NAME;
  var parentWindowObj = window.parent.dialogArguments;
  var pramValue = trim(parentWindowObj.document.getElementById(to_name).value);
  var ids = pramValue.split(',');
  var table = document.getElementById("tbody");
  var userTable = document.getElementById("userTable");
  userTable.className = "TableBlock";
  var addtr = document.createElement("tr");
  var addtd = document.createElement("td");
  addtr.className = "TableControl";
  addtd.style.cursor = "pointer";
  addtd.align = "center";
  addtd.innerHTML = "全部添加";
  addtd.onclick = function(){
    add_all();
  }
  addtr.appendChild(addtd);
  table.appendChild(addtr);
  
  var deltr = document.createElement("tr");
  var deltd = document.createElement("td");
  deltr.className = "TableControl";
  deltd.style.cursor = "pointer";
  deltd.align = "center";
  deltd.innerHTML = "全部删除";
  deltd.onclick = function(){
    del_all();
  }
  deltr.appendChild(deltd);
  table.appendChild(deltr);
 
  for(var i = 0; i < ids.length; i++){
    var show1tr = document.createElement("tr");
  	var show1td = document.createElement("td");
  	show1tr.className = "TableControl";
    show1td.className = "menulines1";
    show1tr.style.cursor = "pointer";
    show1td.style.backgroundColor = "rgb(255, 255, 255)";
    show1td.align = "center";
    show1td.onclick = function(){
      click_user(this);
    }
    show1td.innerHTML = ids[i];
    show1tr.appendChild(show1td);
    table.appendChild(show1tr);
  }
}

function click_user(userName){
  alert("ddd");
  var to_id = TO_ID;
  var to_name = TO_NAME;
  var tdName = $(userName).firstChild.data.replace("\n","");
  var inputSeqId = userName.lastChild.value;
  //alert(tdName);
  var indexVal = null;
  var parentWindowObj = window.parent.dialogArguments;
  var pram = trim(parentWindowObj.document.getElementById(to_name).value);
  //alert('dddssss');
  var pramID = trim(parentWindowObj.document.getElementById(to_id).value);
  if(userName.style.backgroundColor != "rgb(0, 255, 0)"){
    userName.style.backgroundColor = "rgb(0, 255, 0)";
    if(pram == ""){
    parentWindowObj.document.getElementById(to_name).value = trim(pram + trim(tdName));
    parentWindowObj.document.getElementById(to_id).value = trim(pramID + trim(inputSeqId));
    }else{
      parentWindowObj.document.getElementById(to_name).value = trim(pram + "," + trim(tdName) );
      parentWindowObj.document.getElementById(to_id).value = trim(pramID + "," + trim(inputSeqId));
      }
    //alert(parentWindowObj.document.getElementById(to_id).value);
  }else{
    userName.style.backgroundColor = "rgb(255, 255, 255)";
    var ids = pram.split(',');
    var idp = pramID.split(',');
    var nameString = "";
    var iddString = "";
    var index = ids.indexOf(trim(tdName));
    if(index >= 0){
      ids.remove(index);
    }
    for(var i = 0; i < ids.length; i++){
     // if(trim(tdName) != ids[i] && ids[i] != ""){
        if(nameString)
          nameString += ",";
          nameString += ids[i];
      //}
    }
    for(var j = 0; j < idp.length; j++){
      if(trim(inputSeqId) != idp[j] && idp[j] != ""){
        if(iddString)
           iddString += ",";
           iddString += idp[j];
      }
    }
    //indexVal = pram.substr(0,pram.indexOf(tdName))+pram.substr(pram.indexOf(tdName)+1,pram.length-1);
    parentWindowObj.document.getElementById(to_name).value = trim(nameString);
    parentWindowObj.document.getElementById(to_id).value = trim(iddString);
  }
}
function add_all(){
  alert("aaa");
  var to_id = TO_ID;
  var to_name = TO_NAME;
  var tableObj = document.getElementById("userTable");
  var tds = tableObj.getElementsByTagName("userName");
  //var tdName = $(userName).firstChild.data.replace("\n","");
  var parentWindowObj = window.parent.dialogArguments;
  var pram = trim(parentWindowObj.document.getElementById(to_name).value);
  var pramId = trim(parentWindowObj.document.getElementById(to_id).value);
  //var pramID = parentWindowObj.document.getElementById(to_id).value;
  //alert(pramID);
  var ids = parentWindowObj.document.getElementById(to_name).value.split(',');
  var prams = parentWindowObj.document.getElementById(to_id).value.split(',');
  var iddString  = pramId;
  var idString = pram;
  for(var i = 3; i < tableObj.rows.length; i++){
    var tablevalue = tableObj.rows[i].cells[0].firstChild.data;
    var tableIdValue = tableObj.rows[i].cells[0].lastChild.value;
  	var isHave = false;
    for(var j = 0 ;j < ids.length ;j++){
  	  if(ids[j] == trim(tablevalue) && ids[j]){
  	    isHave = true;
  		break;
	  }
	}
    if(!isHave){
      if(idString){
        idString += ",";
      }
  	  idString += trim(tablevalue);
  	  //alert(idString);
  	}
    for(var x = 0 ;x < prams.length ;x++){
  	  if(prams[x] == trim(tableIdValue) && prams[x]){
  	    isHave = true;
  		break;
	  }
	}
  	if(!isHave){
      if(iddString){
        iddString += ",";
      }
      iddString += trim(tableIdValue);
  	}
	tableObj.rows[i].cells[0].style.backgroundColor = "rgb(0, 255, 0)";
  }
  parentWindowObj.document.getElementById(to_name).value = idString;
  parentWindowObj.document.getElementById(to_id).value = iddString;
}

function del_all(){
  var idVal = null;
  var indexVal = null;
  var indexIdVal = null;
  var to_id = TO_ID;
  var to_name = TO_NAME;
  var tableObj=document.getElementById("userTable");
  var parentWindowObj = window.parent.dialogArguments;
  var pram = trim(parentWindowObj.document.getElementById(to_name).value);
  var pramID = trim(parentWindowObj.document.getElementById(to_id).value);
  var idString = "";
  var nameString = "";
  var ddd = null;
  var pramArray = null;
  var prayArrId = null;
  var ids = pram.split(',');
  var idSeq = pramID.split(',');     
  for(var i = 3; i < tableObj.rows.length; i++){
    var tablevalue = tableObj.rows[i].cells[0].firstChild.data; 
    var tableIdValue = tableObj.rows[i].cells[0].lastChild.value;
    pramArray = new Array();
    prayArrId = new Array();
     
    if(tableObj.rows[i].cells[0].style.backgroundColor == "rgb(0, 255, 0)"){
      //indexVal = trim(tablevalue).split(',');    
      var index = ids.indexOf(trim(tablevalue));
      if(index >= 0){
        ids.remove(index);
      }           
        for(var j = 0; j < ids.length; j++){
    //      if(trim(tablevalue)!=ids[j]){
            pramArray.push(ids[j]);
      //    }
        }
        ids = pramArray;
        for(var x = 0; x < idSeq.length; x++){
          if(trim(tableIdValue) != idSeq[x]){
            prayArrId.push(idSeq[x]);
          }
        }
        idSeq = prayArrId;
    }
    tableObj.rows[i].cells[0].style.backgroundColor = "rgb(255, 255, 255)";
  }
  parentWindowObj.document.getElementById(to_name).value = trim(ids.join(","));
  parentWindowObj.document.getElementById(to_id).value = trim(idSeq.join(","));
}
</script>
</head>
<body class="bodycolor" topmargin="1" leftmargin="0" onload="doInit()">
<div id="user_DIV">
<table class="TableBlock" width="100%" name="userTable" id="userTable">
  <tr class="TableHeader">
    <td colspan="2" align="center">
      <b>
        <%=deptLocal%>
      </b>
    </td>
  </tr>
  <%
  if(personList.size() <= 0){
  %>
  <tr class="TableData" id="nullDIV">
    <td align="center">未定义用户</td>
  </tr>
  <%}else{%>
  <tr class="TableControl" id="Divtr">
    <td onclick="add_all(this);" style="cursor:pointer" align="center">全部添加</td>
  </tr>
  <tr class="TableControl" id="Divtr1">
    <td onclick="del_all(this);" style="cursor:pointer" align="center">全部删除</td>
  </tr>
  <%
  for(Iterator it = personList.iterator(); it.hasNext();){
    YHPerson person = (YHPerson)it.next();
  %> 
  <tr class="TableData" style="cursor:pointer" id="TableDataDIV">
    <td class="menulines1" id="userName" title="userName" style="background-color:rgb(255, 255, 255)" align="center" onclick="click_user(this)">
      <%=person.getUserName()%><span id="spanstate" title="userName" style="color:#FF0000;"></span><input type="hidden"  name="userNameID" value=<%=person.getSeqId()%>></td>
  </tr>
  <%
     }
    }
  %>
<tbody id="tbody">
</tbody>
</table>
</div>
</body>
</html>
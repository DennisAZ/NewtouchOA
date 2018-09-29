<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String treeId = request.getParameter("treeId");
  if(treeId == null){
    treeId = "";
  }
  String userId = request.getParameter("userId");
  if(userId == null){
    userId = "";
  }
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>部门管理</title>

<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">
var userId = "<%=userId%>";
var treeId = "<%=treeId%>";

function srcToDest(srcid,destid){
  var optionsObjects = document.getElementById(srcid);
  var optionsSubObjects = document.getElementById(destid);
  for(var i = 0;i < optionsObjects.length;i++){        
    if(optionsObjects.options[i].selected == true){
    var optionsvalue = optionsObjects.options[i].value; 
    var optionstext = optionsObjects.options[i].text;               
    addoptions(destid, optionstext ,optionsvalue); 
   }
  }
}           

function addoptions(objectid,textvalue,optionsvalue){    
  var optionsSubObjects = document.getElementById(objectid);    
  var hasexist=0;    
  for(var i = 0;i < optionsSubObjects.length;i++){                
    var optionsvalue_sub = optionsSubObjects.options[i].text;    
    if(optionsvalue_sub == textvalue) {  
      hasexist += 1;   
    } 
  }   
  var Browser_Name = navigator.appName;  
  if(hasexist == 0){    
    //optionsSubObjects.add(new Option(textvalue, optionsvalue));   
    if(Browser_Name == 'Netscape'){  
    optionsSubObjects.add(new Option(textvalue, optionsvalue),null);   
  } else {  
    optionsSubObjects.add(new Option(textvalue, optionsvalue));  
  }            
 }   
  for(i = 0;i < optionsSubObjects.length;i++){    
    optionsSubObjects.options[i].selected=true;    
 }  
}   
//将对象中所选的项删除   
function destToSrc(objectid)  {    
  var optionsObjects = document.getElementById(objectid);  
  for(var o = 0;o<optionsObjects.length;o++)  {    
    if(optionsObjects.options[o].selected == true)  {    
    var optionsvalue = optionsObjects.options[o].value;    
    var optionstext = optionsObjects.options[o].text;    
    removeoption(objectid,optionstext,optionsvalue)    
    }    
  }    
}    
//删除单个选项    
function removeoption(objectid,textvalue,optionsvalue)  {    
  var optionsSubObjects=document.getElementById(objectid);    
  for(var o=0;o<optionsSubObjects.length;o++) {    
    var optionsvalue_sub=optionsSubObjects.options[o].text;    
    if(optionsvalue_sub==textvalue)    
    optionsSubObjects.removeChild(optionsSubObjects.options[o]);  
  }    
}    
// 全选的js  
var allcheck=false;  
function checkall(){   //批量操作(选取)   
  var all=document.getElementsByTagName("input");  
  for(var i=0;i<all.length;i++) {  
    if(all[i].type=="checkbox") all[i].checked=!allcheck;  
  }  
  allcheck=!allcheck;  
}  


function doInit(){
  var url = "<%=contextPath%>/yh/core/funcs/dept/act/YHDeptPositionAct/getSelestData.act";
  var rtJson = getJsonRs(url, null);
  if (rtJson.rtState == "0") {
    var  selected = rtJson.rtData;
    //[{value:'1',name:'aaaa'},{value:'2',name:'bbbb'},{value:'3',name:'cccc'}];
    var disselected = [];
    new ExchangeSelectbox('parent',selected,disselected,true,false);
  }
}
</script>
</head>
<body onload="doInit()">
 <div id="deplist"></div>
 <form name="form1" id="form1">
 <div id="parent"></div>
 <br></br>
 
 </form>
</body>
</html>
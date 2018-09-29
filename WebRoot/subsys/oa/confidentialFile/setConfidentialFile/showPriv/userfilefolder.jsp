<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 

<%
	String seqId=request.getParameter("seqId");
    String userId = request.getParameter("userId");
	if(seqId==null){
	  seqId="";
	}	
  if(userId==null){
    userId = "";
  }
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>按用户显示</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript">
//var requestURL="<%=contextPath%>/yh/core/funcs/system/filefolder/act/YHFileSortAct";
var requestURL = "<%=contextPath%>/yh/subsys/oa/confidentialFile/act/YHSetConfidentialSortAct";
function doInit(){
  var userId = '<%=userId%>';
  var seqId = '<%=seqId%>';
  if(userId==''){
    $("listDiv").update("<img src='<%=contextPath %>/core/funcs/filefolder/images/notify_open.gif' align='absmiddle'><span class='big3'> 按用户显示</span>"
    + "<table class='MessageBox' align='center' width='240'>"
        +"<td class='msg info'>"
        +"<h4 class='title'>提示</h4>"
        +"<div class='content' style='font-size:12pt'>请选择用户</div>"
        +"</td></tr>"
        +"</table>");
  }else{
    USER_ID(userId);
    $("beforeDiv").style.display = "";
  }	
}
function selectUserInfo(userId){
  var URL = "<%=contextPath%>/yh/core/funcs/attendance/manage/act/YHManageAttendAct/selectUserInfo.act?userId="+userId;
  var rtJson = getJsonRs(URL);
  if(rtJson.rtState == "1"){
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prc = rtJson.rtData;
  var userName = prc.userName;
  return userName;
}
function USER_ID(userId){
	//alert(userId);
	var url = requestURL + "/getAllPersonIdStr.act?seqId=<%=seqId%>";
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	//alert(rsText);
	var prcsJson=json.rtData;
	//alert(rsText);
    var userName = selectUserInfo(userId);
    var info = "<table border='0' width='100%' cellspacing='0' cellpadding='3' class='small'>"
      +"<tr><td><img src='<%=imgPath%>/hrms.gif' align='absmiddle'> <span class='big3'> 文件权限 — " + userName + "</span>"
      + "<span class='small'>&nbsp;&nbsp;具有访问权限或所有者权限时，其他权限设置才有效。</span></td></tr>";
    $('listDiv').update(info);
	var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"})
	.update("<tbody id='tbody'><tr class='TableHeader' style='font-size:10pt'>"
			+"<td nowrap align='center'文件夹名称</td>"				
			+"<td nowrap align='center'>访问权限</td>"
			+"<td nowrap align='center'>新建权限</td>"				
			+"<td nowrap align='center'>下载/打印权限</td>"				
			+"<td nowrap align='center'>管理权限</td>"				
			+"<td align='center'>所有者</td></tr><tbody>");
	$('listDiv').appendChild(table);
	if(prcsJson.length>0){
		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			//alert(prcs.sortName);
			var sortName=prcs.sortName;
			var visiName=prcs.visiName;
			var newUserName=prcs.newUserName;
			var downUserName=prcs.downUserName;
			var manageName=prcs.manageName;
			var owner = prcs.ownerName;
			//alert("ownerName>>>"+owner);
			var visiNames = '';
			var visiTemp = '';
			var newUserNames = '';
			var newUserTemp = ''
			var downUserNames = '';
			var downUserTemp = '';
			var manageNames = '';
			var manageTemp = '';
			var ownerNames = '';
			var ownerTemp = '';
			if(visiName!=''){
		      visiNames = visiName.split(",");
		    }
			if(newUserName!=''){
			  newUserNames = newUserName.split(",");
	        }
			if(downUserName!=''){
			  downUserNames = downUserName.split(",");
	        }
			if(manageName!=''){
			  manageNames = manageName.split(",");
	        }
            if(owner!=''){
              ownerNames = owner.split(",");
            }
            for(var j = 0;j<visiNames.length;j++){
               if(userName == visiNames[j]){
                 visiTemp = "√";
                 break;
               }
            }
            for(var j = 0;j<newUserNames.length;j++){
              if(userName == newUserNames[j]){
                newUserTemp = "√";
                break;
              }
            }
            for(var j = 0;j<downUserNames.length;j++){
              if(userName == downUserNames[j]){
                downUserTemp = "√";
                break;
              }
           }
            for(var j = 0;j<manageNames.length;j++){
              if(userName == manageNames[j]){
                manageTemp = "√";
                break;
              }
           }
           for(var j = 0;j<ownerNames.length;j++){
              if(userName == ownerNames[j]){
                ownerTemp = "√";
                break;
              }
           }
			var tr=new Element('tr',{'width':'90%','class':'TableLine1','font-size':'10pt'});			
			$("tbody").appendChild(tr);
			tr.update("<td align='left'>"					
			  + sortName + "</td><td align='center'>"
			  + visiTemp + "</td><td align='center'>"
				+ newUserTemp + "</td><td align='center'>"					
				+ downUserTemp + "</td><td align='center'>"					
				+ manageTemp + "</td><td align='center'>"					
				+ ownerTemp+"</td>"
			);
		}
		
	}
	
}

</script>
</head >
<body onload="doInit();">
<div id="listDiv"></div>
<br>
<div id="beforeDiv" align="center" style="display:none">
   <input type="button" value="返回" class="BigButton" onClick="parent.parent.location='../index.jsp'">
</div>
</body >
</html>
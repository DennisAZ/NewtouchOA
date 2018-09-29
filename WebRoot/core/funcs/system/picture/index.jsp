<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String usePortalFunc = YHSysProps.getString("usePortalFunc");
	if(YHUtility.isNullorEmpty(usePortalFunc)){
		usePortalFunc = "";
	}

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>图片管理设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/ExchangeSelect.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">
var requestURL="<%=contextPath%>/yh/core/funcs/system/picture/act/YHPictureAct";
function doInit(){
	var usePortalFunc = "<%=usePortalFunc%>";
	var type = "PICTURE";
	//alert("doInit");
	var url=requestURL + "/getPicSortInfo.act";
	var json=getJsonRs(url);
	//alert(rsText);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	var prcsJson=json.rtData;
	if(prcsJson.length>0){
		var table=new Element('table',{ "width":"95%","class":"TableList","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
				+"<td nowrap align='center'>目录名称</td>"				
				+"<td nowrap align='center'>目录路径</td>"				
				+"<td nowrap align='center'>发布范围</td>"				
				+"<td align='center'>操作</td></tr><tbody>");
		$('listDiv').appendChild(table);

		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			//alert(prcs.sortName);
			var picName=prcs.picName;
			var sqlId=prcs.sqlId;
			var picPath=prcs.picPath;
			var deptName=prcs.deptName;
			var roleName=prcs.roleName;
			var userName=prcs.userName;
			var deptNameStr="";
			if(deptName!=""){
				var deptNames=deptName.split(",");
				if(deptName!=""){
					for(var j=0;j<deptNames.length;j++){
						if(deptNames[j]){
							deptNameStr+=deptNames[j]+",";
						}						
						if(j>1){
							deptNameStr+=" . . .";
							break;
						}
					}
				}
			}
			
			
			var roleNameStr="";
			var roleNames=roleName.split(",");
			if(roleName!=""){
				for(var j=0;j<roleNames.length;j++){
					if(roleNames[j]){
						roleNameStr+=roleNames[j]+",";
					}
					
					if(j>1){
						roleNameStr+=" . . .";
						break;
					}
				}
			}
			var userNameStr="";
			var userNames=userName.split(",");
			if(userName!=""){
				for(var j=0;j<userNames.length;j++){
					if (userNames[j]) {

					userNameStr+=userNames[j]+",";
					}
					if(j>1){
						userNameStr+=" . . .";
						break;
					}
				}				
			}

			var tr=new Element('tr',{'width':'90%','class':'TableLine1','font-size':'10pt'});			
			table.firstChild.appendChild(tr);
			
			var deptStr="";
			var roleStr="";
			var userStr="";
			var br="";

			var deptTitle="";
			var roleTitle="";
			var userTitle="";
			var nn="";
			if(deptName){
				deptStr="<font color=#0000FF><b>部门:</b></font>"+deptNameStr;
				deptTitle="部门:"+deptName;
			}
			if(roleName!=""){
				if(deptName!=""){
					br="<br>";
					nn="\n";
				}
				roleStr=br+"<font color=#0000FF><b>角色:</b></font>"+roleNameStr;
				roleTitle=nn+ "角色:"+roleName;
			}
			if(userName){
				if(deptName!="" && roleName!=""){
					br="<br>";
					nn="\n";
				}
				userStr=br+"<font color=#0000FF><b>人员:</b></font>"+userNameStr;
				userTitle=nn+"人员:"+userName;
			}
			var sendDeskTop = "";
			if(usePortalFunc == "1"){
				sendDeskTop = "<a href=\"<%=contextPath%>/core/funcs/portal/cfgPortal.jsp?type=3&picName=" + encodeURIComponent(picName) + "&publicPath=" + encodeURIComponent(picPath)+ "\">发布</a>&nbsp;&nbsp;";
			}
			tr.update("<td align='center'>"					
				+ picName + "</td><td align='left'>"					
				+ picPath + "</td><td align='left' title='"+deptTitle+roleTitle+userTitle  +"'>"					
				+ deptStr +roleStr+userStr   
				+ "</td><td align='center'>"					
				+ "<a href='editPicFolderInfo.jsp?seqId="+sqlId +"&contentId="+"'> 编辑</a>&nbsp;"
				+ "<a href='#'onclick='delete_Proces(\""+ sqlId +"\")'>删除 </a>&nbsp;"
				+ "<a href='setPriv/index.jsp?seqId="+sqlId +"'> 权限设置</a>&nbsp;"
				+ "<a href='#' onclick='menuCode(\""+type +"\"," +"\"" +sqlId+ "\");'>菜单定义指南</a>&nbsp;"
				+ sendDeskTop
				+ "&nbsp;</td>"
			);			
		}
	}	
}

function menuCode(type, id){
  var title = "菜单定义指南";
  var URL = contextPath + "/core/module/menucode/index.jsp?type=" + type + "&id=" + id;
  showModalWindow(URL , title , "menuWindow" ,600,350);
}
function delete_Proces(seqId){
	//alert(seqId);	
 	msg='确认要取消该图片目录吗？这不会删除该路径下的文件。';
 	if(window.confirm(msg)) {
		var url=requestURL+"/delPicFolderById.act";
		//alert(url);
  	var json=getJsonRs(url, 'seqId=' + seqId);
    if(json.rtState == '0'){
	    window.location.reload();	    		
    }else{
			alert(json.rtMsrg);
    }
	} 	
}

</script>
</head>
<body onload="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/picture/images/notify_new.gif" align="absmiddle"><span class="big3"> 新建图片目录</span><br>
    </td>
  </tr>
</table>

<div align="center">
<input type="button"  value="新建图片目录" class="BigButtonC" onClick="location='new/newPictureFolder.jsp';" title="新建图片目录">
</div>
<br>
 
<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=contextPath %>/core/funcs/system/netdisk/images/dian1.gif" width="100%"></td>
 </tr>
</table>
 
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/netdisk/images/notify_open.gif" align="absmiddle"><span class="big3"> 管理图片目录</span>
    </td>
  </tr>
</table>


<div id="listDiv"></div>


</body>
</html>
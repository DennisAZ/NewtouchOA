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
<title>网络硬盘设置</title>
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
var requestURL="<%=contextPath%>/yh/core/funcs/system/netdisk/act/YHNetdiskAct";	
function doInit(){
	var usePortalFunc = "<%=usePortalFunc%>";
	var type="NETDISK";
	var url=requestURL + "/getNetdiskFolderInfo.act";
	var json=getJsonRs(url);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	var prcsJson=json.rtData;
	if(prcsJson.length>0){
		var table=new Element('table',{ "width":"95%","class":"TableList","align":"center"})
		.update("<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
				+"<td nowrap align='center'>序号</td>"				
				+"<td nowrap align='center'>目录名称</td>"				
				+"<td nowrap align='center'>目录路径</td>"				
				+"<td nowrap align='center'>限制容量</td>"				
				+"<td nowrap align='center'>默认排序</td>"				
				+"<td align='center'>操作</td></tr><tbody>");
		$('listDiv').appendChild(table);

		for(var i=0;i<prcsJson.length;i++){
			var prcs=prcsJson[i];
			var sqlId=prcs.sqlId;
			var diskNo=prcs.diskNo;
			var diskName=prcs.diskName;
			
			var diskPath=prcs.diskPath;
			var spaceLimit=prcs.spaceLimit;
			var orderBy=prcs.orderBy;
			var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";
			var tr=new Element('tr',{'width':'90%','class':className,'font-size':'10pt'});
			table.firstChild.appendChild(tr);
			var sendDeskTop = "";
			if(usePortalFunc == "1"){
				sendDeskTop = "<a href=\"<%=contextPath%>/core/funcs/portal/cfgPortal.jsp?type=1&&publicPath=" + encodeURIComponent(diskPath) + "\" >发布</a>&nbsp;&nbsp;";
			}
			tr.update("<td align='center'>"					
				+ diskNo + "</td><td align='left'>"					
				+ diskName + "</td><td align='left'>"					
				+ diskPath + "</td><td align='center'>"					
				+ spaceLimit + "</td><td align='center'>"					
				+ orderBy + "</td><td align='center'>"					
				+ "<a href='editNeskFolder.jsp?seqId="+sqlId +"&contentId="+"'> 编辑</a>&nbsp;"
				+ "<a href='#'onclick='delete_Proces(\""+ sqlId +"\")'>删除 </a>&nbsp;"
				+ "<a href='setPriv/index.jsp?seqId="+sqlId +"'> 权限设置</a>&nbsp;"
				+ "<a href='#' onclick='menuCode(\""+type +"\"," +"\"" +sqlId+ "\");'>菜单定义指南</a>&nbsp;&nbsp;"
				+ sendDeskTop
				+ "&nbsp;</td>"
			);	
		}		
	}	
}
//发布方法 
function publish(diskPath){ 
	window.location.href = contextPath + "/core/funcs/portal/cfgPortal.jsp?type=1&&publicPath="+diskPath; 
	return true; 
}
function menuCode(type, id){
  //alert(type +" ...."+id);
  var title = "菜单定义指南";
  var URL = contextPath + "/core/module/menucode/index.jsp?type=" + type + "&id=" + id;
  showModalWindow(URL , title , "menuWindow" ,600,350);
}
function delete_Proces(seqId){
	//alert(seqId);	
 	msg='确认要取消该共享目录吗？这不会删除该路径下的文件。';
 	if(window.confirm(msg)) {
		var url=requestURL+"/delNetdiskFolderById.act";
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
<body class="" topmargin="5" onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/netdisk/images/notify_new.gif" align="middle"><span class="big3"> 新建共享目录</span><br>
    </td>
  </tr>
</table>

<div align="center">
<input type="button"  value="新建共享目录" class="BigButtonC" onClick="location='new/newNetdiskFolder.jsp';" title="新建共享目录">
</div>

<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=contextPath %>/core/funcs/system/netdisk/images/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/netdisk/images/notify_open.gif" align="middle"><span class="big3"> 管理共享目录</span>
    </td>
  </tr>
</table>
<br>
<div id="listDiv"></div>


</body>
</html>
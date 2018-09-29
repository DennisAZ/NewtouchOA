<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>     
<%@page import="java.net.URLEncoder" %>
<%
	String pathId = request.getParameter("DISK_ID");
	String seqIdStr = request.getParameter("seqId");
	String fileName = request.getParameter("fileName");
	String key = request.getParameter("key");
	
	int seqId = 0;
	if (seqIdStr != null) {
		seqId = Integer.parseInt(seqIdStr);
	}	

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检索结果</title>
<link href="<%=cssPath%>/style.css" rel="stylesheet" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu2Net.js"></script>
<script type="text/javascript">
var requestURL = "<%=contextPath%>/yh/core/funcs/netdisk/act/YHNetDiskAct";


function doInit(){


	//输出列表
	var url=requestURL+"/queryNetdiskInfoById.act";
	var pars = $("form1").serialize();
	var json = getJsonRs(url,pars);
	//alert(rsText);
	if(json.rtState=='0'){
		var prcJson=json.rtData;
 		if(prcJson.length>0){

 			var table=new Element('table',{ "width":"100%","class":"TableList","align":"center"});

 			var strTable="<tbody id='tbody'><tr class='TableHeader' align='center' style='font-size:10pt'>"
 				+	"<td align='center'> 选</td>"
 				+	"<td align='center'> 文件名</td>"
 				+	"<td align='center'> 路径</td>"
				+	"<td align='center'>大小</td>"
				+	"<td align='center'>类型</td>"
				+	"<td width='125' align='center'>修改时间</td>"
				+ "</tr><tbody>";

				table.update(strTable);
			$('listDiv').appendChild(table);

			for(var i=0;i<prcJson.length;i++){
				var prcs = prcJson[i];

				var dbSeqIde = prcs.dbSeqIde;
 				var urlString = prcs.urlString;
 				var filePath = prcs.filePath;
 				var absolutePath = prcs.absolutePath;
 				var officeFlag = prcs.officeFlag;
 				var fileName = prcs.fileName;
 				//alert(fileName);
 				var fileSize = prcs.fileSize;
 				var fileModifyTime = prcs.fileModifyTime;
 				var typeName = prcs.typeName;
 				var userPriv = prcs.userPriv;
 				var managePriv = prcs.managePriv;
 				var downPriv = prcs.downPriv;

 				//alert("userPriv>>"+userPriv + "  managePriv>>"+managePriv + "  downPriv>>"+downPriv +"  officeFlag>>"+officeFlag);

 				var checkBoxStr="<input type='checkbox' name='file_select' id='file_select' onClick='check_one(this)' value=\""+fileName  +"\" sub_url=\"" + filePath  +"\"  ></td>";

 				if(downPriv !=1 && officeFlag =="1"){
 					checkBoxStr="";
 	 			}

 				var hiddenDiv="<input type = 'hidden' id='returnAttId_" + i + "' name='returnAttId_" +i + "'></input>"
				+ "<input type = 'hidden' id='returnAttName_" + i + "' name='returnAttName_" + i + "'></input>"
				+ "<div id='attr_" + i + "'></div>";

 				var className = (i % 2 == 0) ? "TableLine1" : "TableLine2";  
 				var tr=new Element('tr',{'width':'90%','class':className,'font-size':'10pt'});
 				table.firstChild.appendChild(tr);

 				var str = "<td align='left'>"	
 					+ checkBoxStr
 					+ "<td align='left'>" + hiddenDiv + "</td>"
 					+ "<td align='left'>" + urlString + "</td>"
 					+ "<td align='center'>" + fileSize + "</td>"
 					+ "<td align='center'>" + typeName + "</td>"
 					+ "<td align='center'>" + fileModifyTime + "</td>"

 					tr.update(str);

 					$("returnAttId_"+i).value= absolutePath;
 					$("returnAttName_"+i).value= fileName;

 					var  selfdefMenu = {
 				        	office:["downFile","read","edit"], 
 					        img:["play"],  
 					        music:["play"],  
 							    video:["play"], 
 							    others:[]
 								}


 					
 					if(managePriv != 1){
 						selfdefMenu.office = selfdefMenu.office.without("edit");
 	 				}
 	 				if(downPriv != 1){
 	 					selfdefMenu.office = selfdefMenu.office.without("downFile");
 	 	 			}
 	 				if(userPriv != 1){
 						selfdefMenu.office = selfdefMenu.office.without("downFile","edit","read");
 	 				}
 					
					

 					attachMenuSelfUtil("attr_"+i,$('returnAttName_'+i).value,$('returnAttId_'+i).value,i , "","",selfdefMenu);
				
			}
			
			var tr1=new Element('tr',{'class':'TableControl'});			
			table.lastChild.appendChild(tr1);
			var spanCount = 6;
			tr1.update("<td align='left' colspan='" + spanCount +" '>"
				+	"&nbsp;"				
			  + "<input type='checkbox' name='allbox' id='allbox' onClick='check_all();'><label for='allbox' style='cursor:pointer'>全选</label>&nbsp;&nbsp;"
				+ "<a href='javascript:do_action(\"copy\")' id='copyFile'><img src='<%=contextPath%>/core/funcs/netdisk/images/copy.gif' align='center' border='0' title='复制此文件'>复制&nbsp;&nbsp;</a>"
				+ "<a href='javascript:do_action(\"cut\")' id='cutFile' style='display: none'><img src='<%=contextPath%>/core/funcs/netdisk/images/cut.gif' align='center' border='0' title='剪切此文件'>剪切&nbsp;&nbsp;</a>"
				+ "<a href='javascript:do_action(\"rename\");' id='renameFile' style='display: none'><img src='<%=contextPath%>/core/funcs/netdisk/images/folder_edit.gif' align='center' border='0' title='重命名此文件'>重命名&nbsp;&nbsp;</a>"
				+ "<a href='javascript:do_action(\"delete\");' id='delFile' style='display: none'><img src='<%=contextPath%>/core/funcs/netdisk/images/delete.gif' align='center' border='0' title='删除此文件'>删除&nbsp;&nbsp;</a>"
				+ "<a href='javascript:do_action(\"down\");' id='downFile' style='display: none'><img src='<%=imgPath%>/download.gif' align='center' border='0' title='下载此文件'><span id='label_down'>下载</span>&nbsp;&nbsp;</a>"
			  +"</td>"
			);

			if(managePriv ==1){
				$("cutFile").show();
				$("renameFile").show();
				$("delFile").show();
			}
			if(downPriv ==1 || officeFlag != "1"){
				$("downFile").show();
			}

			$("backDiv").show();
			
			
 		}else{
 			$("warnDiv").show();
 	 	}
		
		
		
	}else{
		alert(json.rtMsrg); 
    return ;	
	}
	
	

	
}



//全选
var selected_count = 0;
function check_all(){
	var file_list = document.getElementsByName("file_select");
	var all = $("allbox");
	if(!file_list)	{
		return;
	}
	for(var i = 0; i < file_list.length; i++)	{
		if(all.checked)		{
			file_list.item(i).checked = true;
		}	else{
			file_list.item(i).checked = false;
		}
	}
	if(all.checked && file_list.length > 1){
		selected_count = file_list.length;
	}	else{
		label_down.innerText = "下载";
		selected_count = 0;
	}
}


//单项选择
function check_one(el){
	if(!el.checked)	{
		$("allbox").checked = false;
	}
	if(!el.checked && selected_count > 0)	{
		selected_count--;
	}	else{
		selected_count++;
	}
	if(selected_count > 1){
	}	else{
		label_down.innerText = "下载";
	}
}



function do_action(action){
	var file_nodes = document.getElementsByName("file_select");
	var file_list = "";
	var sub_url = "";
	var file_count = 0;
	if(!file_nodes)	{
		return;
	}
	for(var i = 0; i < file_nodes.length; i++)	{
		if(file_nodes.item(i).checked){
			file_list += file_nodes.item(i).value + "*";
			sub_url += file_nodes.item(i).sub_url + "*";
      file_count++;
		}
	}
	//alert("file_list>>" +file_list +"  sub_url>>"+sub_url);
	if(file_count == 0)	{
		alert("请至少选择一个文件");
		return;
	}
	if((action == "rename") && file_count > 1){
		alert("请选择一个文件");
		return;
	}

  if((action=="rename" || action=="down") && file_count>1){
		alert("一次只能选择一个文件");
		return;
  }


	switch(action){ 
	case "copy":
	case "cut":
		var url = requestURL + "/doAction.act?FILE_LIST=" + encodeURIComponent(file_list) + "&NETDISK_ACTION=" + action + "&netdisk_sub_url=" + encodeURIComponent(sub_url); 
		var json = getJsonRs(url);
		alert("选择的文件已标记，\n请到目标目录中进行“粘贴”操作");
		break;
	case "rename":
		newWindow("<%=contextPath%>/core/funcs/netdisk/rename.jsp?seqId=<%=seqId%>&DISK_ID=" + encodeURIComponent(sub_url) + "&FILE_NAME=" + encodeURIComponent(file_list));
		break;
	case "delete":
		if(window.confirm("删除文件后将不可恢复，确定删除吗？"))
		{
			var encodeFileList=encodeURIComponent(file_list)
			var urlStr="&FILE_LIST=" + encodeFileList;
			var url = requestURL + "/delFile.act?FILE_LIST=" + encodeURIComponent(file_list) + "&netdisk_sub_url=" + encodeURIComponent(sub_url); 
			var json = getJsonRs(url);
			self.location.reload();
		}
		break;
	case "down":
		var fileNameStr;
		var sub_urlStr;
		if(file_list.lastIndexOf("*") != -1){
			fileNameStr = file_list.substring(0,file_list.lastIndexOf("*"));
		}
		
		if(sub_url.lastIndexOf("*") != -1){
			sub_urlStr = sub_url.substring(0,sub_url.lastIndexOf("*"));
		}
		
		downLoadFile(fileNameStr,sub_urlStr + "/" + fileNameStr);
  break;
}


	
}

function newWindow(url){
	loc_x = (screen.availWidth-300)/2;
	loc_y = (screen.availHeight-300)/2;
	window.open(url, "netdisk", 
			"height=200,width=350,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
			+ loc_y + ", left=" + loc_x + ", resizable=yes");
}




</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath%>/core/funcs/filefolder/images/folder_search.gif" align="absmiddle"><b><span class="Big1" id="folderName"> <%=pathId %> - 全文检索</span></b><br>
    </td>
    <td id="pageDiv" align="right" valign="bottom" class="small1">
	   <div id="pageInfo" style="float:right;"></div>
  	</td>
  </tr>
</table>


<div id="listDiv" align="center"></div>

<br>
<div align="center" id="backDiv" style="display: none">
  <input type="button" value="返回" class="BigButton" onclick="location.href='<%=contextPath %>/core/funcs/netdisk/fileList.jsp?seqId=<%=seqId %>&DISK_ID=<%=pathId %>'">
</div>

<br>
<div id="warnDiv" style="display:none">
<table class="MessageBox" align="center" width="380">
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt"><br>没有搜索到符合条件的文件</div>
    </td>
  </tr>
</table>
 
<br>
<div align="center">
  <input type="button" value="返回" class="BigButton" onclick="location='<%=contextPath %>/core/funcs/netdisk/fileList.jsp?seqId=<%=seqId %>&DISK_ID=<%=URLEncoder.encode(pathId,"UTF-8") %>'">
</div>


</div>


<div>
<form id="form1" name="form1" action="" method="post">
	<input type="hidden" id="seqId" name="seqId" value="<%=seqId%>">
	<input type="hidden" id="DISK_ID" name="DISK_ID" value="<%=URLEncoder.encode(pathId,"UTF-8")%>">
	<input type="hidden" id="fileName" name="fileName" value="<%=URLEncoder.encode(fileName,"UTF-8")%>">	
	
	<input type="hidden" id="key" name="key" value="<%=key%>">	

</form>
</div>





<br>
</body>
</html>
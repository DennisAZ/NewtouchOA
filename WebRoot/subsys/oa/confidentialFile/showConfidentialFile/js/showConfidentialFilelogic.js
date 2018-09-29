/**
 * 新建文件
 * @param seqId
 * @return
 */
function newFile(seqId){
  //var folderPath = $("sortName").innerHTML.trim();
	var folderPath = "";
  var url= contextPath + "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/new/newFile.jsp?seqId=" + seqId + "&folderPath=" + encodeURIComponent(folderPath);
  location.href = url;
}

/**
 * 数据对齐
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function alignFunc(cellData, recordIndex, columIndex){
	return "<center>" + cellData + "</center>";
}

/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function optsList(cellData, recordIndex, columIndex){
	var contentId = this.getCellData(recordIndex,"seqId");
	var sortId = this.getCellData(recordIndex,"sortId");
	 //var showInfoStr = "<a href=javascript:meetingDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
	var showInfoStr = "<a href=javascript:modefyFile('" + contentId + "','" + sortId + "')>编辑</a>&nbsp;"
     //+ "<a href=javascript:deleteMeeting('" + seqId + "','" + seqId + "')>删除</a>";
	return "<center>"+ showInfoStr + "</center>";
}

/**
 * 文件报送
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function fileSendReadFunc(cellData, recordIndex, columIndex){
	var contentId = this.getCellData(recordIndex,"seqId");
	var sortId = this.getCellData(recordIndex,"sortId");
	var str = "<a href=javascript:showFileSend('" + contentId + "','" + sortId + "')>" + cellData + "</a>&nbsp;"
	return str ;
}
/**
 * 查看文件报送
 * @param contentId
 * @param sortId
 * @return
 */
function showFileSend(contentId,sortId){
	location.href = contextPath + "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/read.jsp?sortId=" + sortId + "&contentId=" + contentId;
	
}

/**
 * 报送状态
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function fileSendFunc(cellData, recordIndex, columIndex){
	var str = "未报送";
	if(cellData == "1"){
		str = "已报送";
	}
	 return "<center>"+ str + "</center>";
}


/**
 * 编辑文件
 * @param contentId
 * @param folderId
 * @return
 */
function modefyFile(contentId,folderId){
	location.href = contextPath + "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/edit.jsp?seqId=" + folderId + "&contentId=" + contentId;
}

/**
 * folder.jsp下的选择框
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf(this)\" ></center>";
}
/**
 *  文件列表下的全选
 * @param field
 * @return
 */
var selected_count=0;
function checkAll(field) {
	var all =  $("checkAlls");
  var file_list = document.getElementsByName("deleteFlag");
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
		$(label_down).update("批量下载");
		selected_count = file_list.length;
	}	else{
		$(label_down).update("下载");
		selected_count = 0;
	}
}
function checkSelf(el){
	if(!el.checked)	{
		$("checkAlls").checked = false;
	}
	if(!el.checked && selected_count > 0)	{
		selected_count--;
	}	else{
		selected_count++;
	}
	if(selected_count > 1){
		$(label_down).update("批量下载");
	}	else{
		$(label_down).update("下载");
	}
}

//删除选择文件
function deleteArrang() {
  var idStrs = checkMags('deleteFlag');
  //alert(idStrs);
  if(!idStrs) {
    alert("要删除文件，请至少选择其中一个。");
    return;
  }
  var msg="确定要删除选择文件吗？这将不可恢复！";
  if(!window.confirm(msg)) {
    return ;
  }  
  var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct";
	var url = requestURLStr + "/delCheckedFile.act";
  var rtJson = getJsonRs(url, "seqIdStr=" + idStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}
/**
 * 取得选中选项
 * @param cntrlId
 * @return
 */
function checkMags(cntrlId){
  var ids= ""
  var checkArray = $$('input');
  for(var i = 0 ; i < checkArray.length ; i++){
    if(checkArray[i].name == cntrlId && checkArray[i].checked ){
      if(ids != ""){
        ids += ",";
      }
      ids += checkArray[i].value;
    }
  }
  return ids;
}

/**
 * 截取日期
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function sendDateFunc(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	var str = "";
	var sendDate = cellData;
	if(sendDate){
		str = sendDate.substr(0,sendDate.length - 2);
	}
	return "<center>"+ str +"</center>";
}
/**
 * 获取目录路径
 * @param seqId
 * @return
 */
function getFolderPathById(seqId){
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var url = requestURLStr + "/getSortNameById.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  //alert(rsText);
  if (rtJson.rtState == "0") {
  	var data = rtJson.rtData;
  	$("folderPath").innerHTML = data.folderPath;
  	document.getElementById("sortParentStr").value = data.sortParentId;
  } else {
    alert(rtJson.rtMsrg); 
  }
}
/**
 * 删除目录
 * @param seqId
 * @return
 */
function deleteSort(seqId){
	var msg="确定要删除该文件夹吗？这将删除该文件夹中的所有文件和子文件夹，且不可恢复！";
	if(window.confirm(msg)){
		var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHSetConfidentialSortAct";
		var url = requestURLStr + "/delFileSortInfoById.act";
  	var json=getJsonRs(url, "seqId=" + seqId);
    if(json.rtState == '0'){
    	var curTree = parent.frames["file_tree"].tree;  		
  		curTree.removeNode(seqId);
  		var sortParentStr = $("sortParentStr").value;
  		window.location.href = contextPath + "/subsys/oa/confidentialFile/showConfidentialFile/fileRegister/folder.jsp?seqId=" + sortParentStr;
    }else{
			alert(json.rtMsrg);
    }
	}
}
/**
 * 对文件操作
 * @param action
 * @return
 */
function doAction(action,folderId){
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct";
	var idStrs = checkMags('deleteFlag');
	var idStrArry = idStrs.split(",");
	var count = idStrArry.length;
  if(!idStrs) {
    alert("请至少选择其中一个。");
    return;
  }
	switch(action){
	case "copyFile":
	case "cutFile":
		$("paste_sort").hide();
		if(action == "copyFile"){
			var url = requestURLStr + "/copyFileByIds.act?folderSeqId=" + folderId + "&seqIdStrs=" + idStrs + "&action=" + action;
			var json=getJsonRs(url);
	    if(json.rtState == '0'){
	    	setCookie("confidentialAction",action);
	    	setCookie("folderIdCookie",folderId);
	    	alert("选择的文件已“复制”\n请到目标目录中进行“粘贴”操作"); 	
	    }else{
				alert(json.rtMsrg);
	    }
		}else{			
			var url= requestURLStr + "/copyFileByIds.act?seqIdStrs=" + idStrs + "&action=" + action;
			var json=getJsonRs(url);
	    if(json.rtState == '0'){
	    	setCookie("confidentialAction",action);
	    	setCookie("folderIdCookie",folderId);
	    	alert("选择的文件已“剪切”\n请到目标目录中进行“粘贴”操作");
	    }else{
				alert(json.rtMsrg);
	    }
		}
		break;
	case "fileSend": //文件报送
		var url= requestURLStr + "/fileSend.act?contentId=" + idStrs;
		var json = getJsonRs(url);
		if(json.rtState == '0'){
			alert("文件已报送");
    	window.location.reload(); 	
    }else{
			alert(json.rtMsrg);
    }
	  break;
	case "getBack": //文件撤回
	  
	  var url= requestURLStr + "/getBack.act?contentId=" + idStrs;
	  var json = getJsonRs(url);
	  if(json.rtState == '0'){
	    alert("文件已撤回");
	    window.location.reload(); 	
	  }else{
	    alert(json.rtMsrg);
	  }
	  break;
	case "downFile": //下载
		if(count>1 && window.confirm("一次下载多个文件需要在服务器上做压缩处理，会占用较多服务器CPU资源，确定继续下载吗？\n该操作请不要下载超过128MB的大文件")){
			location.href = requestURLStr + "/batchDownload.act?sortId=" + folderId + "&contentIdStr=" + idStrs + "&name=";
		}else if(count == 1){
			location.href = requestURLStr + "/batchDownload.act?sortId=" + folderId + "&contentIdStr=" + idStrs + "&name=";
		}
		break;
	}
}
/**
 * 粘贴文件
 * @param sortId
 * @return
 */
function pasteFile(sortId){
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct";
	var url = requestURLStr + "/pasteFile.act?sortId=" + sortId;
	var rtJson=getJsonRs(url);
  if(rtJson.rtState == '0'){
  	delCookie("confidentialAction");
  	delCookie("folderIdCookie");
 		window.location.reload();
  }
}
/**
 * 设置cookie
 * @param name
 * @param value
 * @return
 */
function setCookie(name,value){
  var Days = 30;
  var exp  = new Date();
  document.cookie = name + "="+ escape (value);
}
/**
 * 删除cookie
 * @param name
 * @return
 */
function delCookie(name){
  var Days = 30;
  var exp  = new Date();
  document.cookie = name + "=";
}
/**
 * 读取cookie
 * @param name
 * @return
 */
function getCookie(name){
  var arr = document.cookie.match(new RegExp("(^| )" + name + "=([^;]*)(;|$)"));
  if (arr != null){
    return unescape(arr[2]);
  }else{
    return "";
  }
}
/**
 * 显示管理
 * @return
 */
function showManage(folderId){
	var actionFlag = getCookie("confidentialAction");
	var folderIdCookie = getCookie("folderIdCookie");
	if(actionFlag=="copyFile"||actionFlag=="cutFile"){
		if(folderId != folderIdCookie){
			$("paste_file").show();	
		}
	}
	if(actionFlag=="copy" || actionFlag=="cut"){
		if(folderId != folderIdCookie){
			$("paste_sort").show();	
		}
	}
	getPrivate(folderId);
}
/**
 * 对文件夹操作(复制、剪贴)
 * @param action
 * @param seqId
 * @return
 */
function sortAction(action,folderId){
	$("paste_file").hide();
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	if(action == "copy"){
		var url = requestURLStr + "/copyFolderById.act?folderId=" + folderId + "&action=" + action;
		var json = getJsonRs(url);
    if(json.rtState == '0'){
    	setCookie("confidentialAction",action);
    	setCookie("folderIdCookie",folderId);
    	alert("选择的文件夹已“复制”\n请到目标目录中进行“粘贴”操作");    	
    }else{
			alert(json.rtMsrg);
    }		
	}else{
		var url = requestURLStr + "/copyFolderById.act?folderId=" + folderId + "&action=" + action;
		var json=getJsonRs(url);
    if(json.rtState == '0'){
    	setCookie("confidentialAction",action);
    	setCookie("folderIdCookie",folderId);
    	alert("选择的文件夹已“剪切”\n请到目标目录中进行“粘贴”操作");
    }else{
			alert(json.rtMsrg);
    }
	}
}
/**
 * 对文件夹操作(粘贴)
 * @param seqId
 * @return
 */
function pasteSort(seqId){
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var url = requestURLStr + "/pasteFolder.act?sortParent=" + seqId;
	var rtJson=getJsonRs(url);
  if(rtJson.rtState == '0'){
  	prcsJson = rtJson.rtData;
  	var action=prcsJson[0].action;
  	if(action == "copy"){
  		add_TreeNode(prcsJson);
  		delCookie("confidentialAction");
    	delCookie("folderIdCookie");
    }
    if(action == "cut"){
			var nodeId = prcsJson[0].seqId;
			var curTree = parent.frames["file_tree"].tree;  		
  		curTree.removeNode(nodeId);
  		add_TreeNode(prcsJson);  		
  		delCookie("confidentialAction");
    	delCookie("folderIdCookie");
    }  	
  }else{
		alert(json.rtMsrg);
  }	
}
/**
 * 增加树节点
 * @param prcsJson
 * @return
 */
function add_TreeNode(prcsJson){
	var curTree = parent.frames["file_tree"].tree;
	var curNode = curTree.getCurrNode();
	var nodeId = prcsJson[0].nodeId;
	var isHaveChild = prcsJson[0].isHaveChild;
	var nodeName = prcsJson[0].sortName;
 	var imgAddress = imgPath + "/dtree/folder.gif";
	var node = {
			parentId:curNode.nodeId,
			nodeId:nodeId,
			name:nodeName,
			isHaveChild:isHaveChild,
			extData:'',
			imgAddress:imgAddress
	}
	curTree.addNode(node);
	location.reload();
}

/**
 * 获取权限信息
 * @param seqId
 * @return
 */
var managePriv;
function getPrivate(seqId){
	var visiPriv;
	var newPriv;
	var downPriv;
	var ownerPriv;
	var dbSortParent;
	var dbSeqId;
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var url = requestURLStr + "/getAllPrivteById.act?seqId=" + seqId;
	var rtJson = getJsonRs(url);
	//alert(rsText);
	if(rtJson.rtState == '0'){
		var prcs = rtJson.rtData;
		visiPriv = prcs.visiPriv;
		managePriv = prcs.managePriv;
		newPriv = prcs.newPriv;
		downPriv = prcs.downPriv;
		ownerPriv = prcs.ownerPriv;
		dbSortParent = prcs.sortParent;
		dbSeqId = prcs.seqId;
		if(dbSortParent=="0"){	//如果根目录
			if(newPriv=="1"){
				$('newSubFolder').show();
				$('newFile').show();
				$('battUpload').show();
			}
			if(ownerPriv=="1"){
				$('setFolderPriv').show();
			}
		}else{
			if(newPriv=="1"){
				$('newSubFolder').show();
				$('newFile').show();
				$('battUpload').show();
			}
			if(ownerPriv=="1"){
				$('setFolderPriv').show();
			}
			var parentManagePriv = getParentPriv(dbSortParent);
			if(managePriv=="1"){
				$('reNameSubFolder').show();
				$('copyFolder').show();
				$('cutFolder').show();					
			}
			if(parentManagePriv !="0" && parentManagePriv == managePriv){
				$('delFolder').show();
			}
		}
	}else{
		alert(json.rtMsrg);
  }
}
/**
 * 获取上一级目录的权限
 * @param sortParent
 * @return
 */
function getParentPriv(sortParent){
	var parentManagePriv=0;
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var url = requestURLStr + "/getAllPrivteById.act?seqId=" + sortParent;
	var rtJson=getJsonRs(url);
	if(rtJson.rtState == '0'){
		var prcs = rtJson.rtData;
		parentManagePriv = prcs.managePriv;
	}else{
		alert(json.rtMsrg);
		return ;
	}
	return parentManagePriv;		
}
/**
 * 删除文件
 * @param contentId
 * @param folderId
 * @return
 */
function deleFile(contentId){
	var msg="确定要删除选择文件吗？这将不可恢复！";
	if(window.confirm(msg)){
		var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHConfidentialContentAct";
		var url = requestURLStr + "/delCheckedFile.act";
	  var rtJson = getJsonRs(url, "seqIdStr=" + contentId);
	  if (rtJson.rtState == "0") {
	    window.location.reload();
	  } else {
	    alert(rtJson.rtMsrg); 
	  }
	}
}
/**
 * 获取文件夹名
 * @param seqId
 * @return
 */
function getFolderName(seqId){
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var getFolderNameUrl = requestURLStr + "/getFolderName.act?seqId=" + seqId;
	var json = getJsonRs(getFolderNameUrl);
	if(json.rtState == '1'){
		alert(json.rtMsrg);
		return ;				
	}
	prcsJson = json.rtData;	
	$("folderName").innerHTML = prcsJson.folderName;
}
/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function searchOptsList(cellData, recordIndex, columIndex){
	var contentId = this.getCellData(recordIndex,"seqId");
	var sortId = this.getCellData(recordIndex,"sortId");
	var showInfoStr = "<a href=javascript:modefyFile('" + contentId + "','" + sortId + "')>编辑</a>&nbsp;"
			showInfoStr += "<a href=javascript:deleFile('" + contentId + "')>删除</a>&nbsp;"
	return "<center>"+ showInfoStr + "</center>";
}
/**
 * 报送状态
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function fileSendFunc(cellData, recordIndex, columIndex){
	var str = "未报送";
	if(cellData == "1"){
		str = "已报送";
	}
	 return "<center>"+ str + "</center>";
}
/**
 * 显示文件夹路径(全局搜索)
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function shwoFolderFunc(cellData, recordIndex, columIndex){
	var sortId = this.getCellData(recordIndex,"sortId");
	var str = "";
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var url = requestURLStr + "/getSortNameById.act";
  var rtJson = getJsonRs(url, "seqId=" + sortId);
  if (rtJson.rtState == "0") {
  	var data = rtJson.rtData;
  	str = "/" + data.folderPath;
  } else {
    alert(rtJson.rtMsrg); 
  }
  return str
}
/**
 * 操作(全局搜索)
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function globalSearchOptsList(cellData, recordIndex, columIndex){
	var contentId = this.getCellData(recordIndex,"seqId");
	var sortId = this.getCellData(recordIndex,"sortId");
	var visiPriv;
	var newPriv;
	var downPriv;
	var ownerPriv;
	var dbSortParent;
	var dbSeqId;
	var requestURLStr = contextPath + "/yh/subsys/oa/confidentialFile/act/YHShowConfidentialSortAct";
	var url = requestURLStr + "/getAllPrivteById.act?seqId=" + sortId;
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == '0'){
		var prcs = rtJson.rtData;
		managePriv = prcs.managePriv;
		newPriv = prcs.newPriv;
		downPriv = prcs.downPriv;
	}
	var showInfoStr = "";
	if(managePriv == "1"){
		 showInfoStr = "<a href=javascript:modefyFile('" + contentId + "','" + sortId + "')>编辑</a>&nbsp;"
			 						+ "<a href=javascript:deleFile('" + contentId + "')>删除</a>&nbsp;"
	}
	return "<center>"+ showInfoStr + "</center>";
}


function sms_submit(){
  
}




/**
 * 获取下拉框选项
 * getSecretFlag("T_COURSE_TYPE","tCourseTypes");
 * @param parentNo	代码编号
 * @param optDiv	绑定的div
 * @param extValue  要选中的值 * @return
 */
function getSecretFlag(parentNo,optDiv,extValue){
	var requestURLStr = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectChildCode.act?parentNo=" + parentNo;
	var rtJson = getJsonRs(requestURLStr);
	if(rtJson.rtState == "1"){
	  alert(rtJson.rtMsrg); 
	  return ;
	}
	var prcs = rtJson.rtData;
	var selects = document.getElementById(optDiv);
	for(var i=0;i<prcs.length;i++){
	  var prc = prcs[i];
	  var option = document.createElement("option"); 
	  option.value = prc.seqId; 
	  option.innerHTML = prc.codeName; 
	  selects.appendChild(option);
	  if(extValue && (extValue == prc.value)){
		  option.selected = true;
	  }
	}
}

/**
 * 操作
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function opts(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	var contentStatus = this.getCellData(recordIndex,"contentStatus");
	var control = "";
	var toSee = "<a href=javascript:toSee(" + seqId + ")>文章预览</a>&nbsp;";
	
	
  //编辑权限
  var editUserContent = this.getCellData(recordIndex,"editUserContent");
  var editUserContentBoo = isPermissions(editUserContent);
  var update = "";
  var deleteStr = "";
  if(editUserContentBoo){
    update = "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;";
    deleteStr = "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>&nbsp;";
  }
  
  //审批权限
  var approvalUserContent = this.getCellData(recordIndex,"approvalUserContent");
  var approvalUserContentBoo = isPermissions(approvalUserContent);
  var issued = "";
  var no = "";
  if(approvalUserContentBoo){
    issued = "<a href=javascript:toIssued(" + seqId + ")>签发</a>&nbsp;";
    no = "<a href=javascript:toNo(" + seqId + ")>否定</a>&nbsp;";
  }
  
  //发布权限
  var releaseUserContent = this.getCellData(recordIndex,"releaseUserContent");
  var releaseUserContentBoo = isPermissions(releaseUserContent);
  var release = "";
  if(releaseUserContentBoo){
    release = "<a href=javascript:toRelease(" + seqId + ")>发布</a>&nbsp;";
  }
  
  //回撤权限
  var recevieUserContent = this.getCellData(recordIndex,"recevieUserContent");
  var recevieUserContentBoo = isPermissions(recevieUserContent);
  var receive = "";
  if(recevieUserContentBoo){
    receive = "<a href=javascript:toReceive(" + seqId + ")>回撤</a>&nbsp;";
  }
  
	switch(contentStatus){
	  case 0 : control = issued + no + update + deleteStr;break;
	  case 1 : control = issued + no + update + deleteStr;break;
	  case 2 : control = update + deleteStr;break;
	  case 3 : control = release + receive;break;
	  case 4 : control = update + deleteStr;break;
	  case 5 : control = toSee + receive;break;
	}
	return "<center>"
				+ control
				+ "</center>";
}
function doEdit(seqId){
	location.href = contextPath + "/cms/content/modify.jsp?seqId=" + seqId;
}

function toIssued(seqId){
  var requestURLStr = contextPath + "/yh/cms/content/act/YHContentAct/toIssued.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURLStr);
  if(rtJson.rtState == "0"){
    location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function toNo(seqId){
  var requestURLStr = contextPath + "/yh/cms/content/act/YHContentAct/toNo.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURLStr);
  if(rtJson.rtState == "0"){
    location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function toRelease(seqId){
  var requestURLStr = contextPath + "/yh/cms/content/act/YHContentAct/toRelease.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURLStr);
  if(rtJson.rtState == "0"){
    if(rtJson.rtData == 1){
      alert("发布成功！")
      location.reload();
    }
    else if(rtJson.rtData == 0){
      alert("请正确配置该文章所在栏目的文章模板！"); 
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function toReleaseColumn(seqId){
  var requestURLStr = contextPath + "/yh/cms/column/act/YHColumnAct/toRelease.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURLStr);
  if(rtJson.rtState == "0"){
    if(rtJson.rtData == 1){
      alert("发布成功！")
      location.reload();
    }
    else if(rtJson.rtData == 0){
      alert("请正确配置该文章所在栏目的文章模板！"); 
    }
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function toReceive(seqId){
  var requestURLStr = contextPath + "/yh/cms/content/act/YHContentAct/toReceive.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURLStr);
  if(rtJson.rtState == "0"){
    location.reload();
  }else{
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 详细信息
 * @param seqId
 * @return
 */
function toSee(seqId){
  var requestURLStr = contextPath + "/yh/cms/column/act/YHColumnAct/toSee.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURLStr);
  if(rtJson.rtState == "0"){
    if(rtJson.rtData == 1){
      alert("该站点模板配置不正确！");
      return;
    }
    var URL = rtJson.rtData;
    newWindow(URL);
  }else{
    alert(rtJson.rtMsrg); 
  }
}
/**
 * 打开新窗口  newWindow(URL,'740', '540');
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height){
	var locX=(screen.width-width)/2;
	var locY=(screen.height-height)/2;
	window.open(url, "meeting", 
			"height=" +height + ",width=" + width +",status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
			+ locY + ", left=" + locX + ", resizable=yes");
}
function deleteSingle(seqId){
	if(!window.confirm("确认要删除该文章 ？")){
		return ;
	}
	var requestURLStr = contextPath + "/yh/cms/content/act/YHContentAct";
	var url = requestURLStr + "/deleteContent.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}
/**
 * 删除多个文件
 * @return
 */
function deleteAll(){
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("请至少选择其中一个文章！");
    return;
  }
  if(!window.confirm("确认要删除已选中的文章 ？")) {
    return ;
  } 
	var requestURLStr = contextPath + "/yh/cms/content/act/YHContentAct";
	var url = requestURLStr + "/deleteContent.act";
	var rtJson = getJsonRs(url, "seqId=" + idStrs );
	if (rtJson.rtState == "0") {
		window.location.reload();
	}else {
	 alert(rtJson.rtMsrg); 
	}
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
}

/**
 * 全选
 * @param field
 * @return
 */
function checkAll(field) {
  var deleteFlags = document.getElementsByName("deleteFlag");
  for(var i = 0; i < deleteFlags.length; i++) {
    deleteFlags[i].checked = field.checked;
  }
}

function checkSelf(){
  var allCheck = $('checkAlls');
  if(allCheck.checked){
    allCheck.checked = false;
  }
}
//取得选中选项
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

function checkBoxRenderCare(cellData, recordIndex, columIndex){
  var staffMobile = this.getCellData(recordIndex,"mobilNo");
  if(staffMobile && staffMobile.trim() != ""){
	  return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + staffMobile + "\" onclick=\"checkSelf()\" ></center>";
  }
  return "<center><input disabled type=\"checkbox\" name=\"deleteFlag\" value=\"" + staffMobile + "\" onclick=\"checkSelf()\" ></center>";
}
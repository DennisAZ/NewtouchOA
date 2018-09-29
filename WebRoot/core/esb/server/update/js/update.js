
function doEdit(seqId){
	location.href = contextPath + "/subsys/jtgwjh/notifyManage/modify.jsp?seqId=" + seqId;
}
/**
 * 详细信息
 * @param seqId
 * @return
 */
function sendDetail(seqId){
  var URL = contextPath + "/subsys/jtgwjh/notifyManage/sendDetail.jsp?seqId=" + seqId;
  newWindow(URL,'820', '500');
}
function reciveDetail(seqId){
	  var URL = contextPath + "/subsys/jtgwjh/notifyManage/reciveDetail.jsp?seqId=" + seqId;
	  newWindow(URL,'820', '500');
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
	if(!window.confirm("确认要删除该系统升级信息吗？")){
		return ;
	}
	var requestURLStr = contextPath + "/yh/core/esb/server/update/act/YHUpdateServerAct";
	var url = requestURLStr + "/deleteFile.act";
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
    alert("要删除系统升级信息，请至少选择其中一个。");
    return;
  }
  if(!window.confirm("确认要删除已选中的系统升级信息 ？")) {
    return ;
  } 
	var requestURLStr = contextPath + "/yh/core/esb/server/update/act/YHUpdateServerAct";
	var url = requestURLStr + "/deleteFile.act";
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



function renderCreateDate(cellData, recordIndex, columIndex){
    return cellData.substring(0, 19);
}

/**
 * 发送
 */
function sendSingle(jhNotifyId){
  var requestURLStr = contextPath + "/yh/subsys/jtgwjh/notifyManage/act/YHJhNotifyInfoAct";
  var url = requestURLStr + "/sendNotifyInfo.act";
  var rtJson = getJsonRs(url, "jhNotifyId=" + jhNotifyId );
  if (rtJson.rtState == "0") {
    alert("公告信息已加入发送队列！");
    window.location.reload();
  }else {
   alert(rtJson.rtMsrg); 
  }
}

function renderNotifyTitle(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  return "<a href=javascript:sendDetail(" + seqId + ")>"+cellData+"</a>";
}

function renderNotifyTitle2(cellData, recordIndex, columIndex){
	  var seqId = this.getCellData(recordIndex,"seqId");
	  return "<a href=javascript:reciveDetail(" + seqId + ")>"+cellData+"</a>";
	}
function renderDetail(cellData,recordIndex,columnIndex){
	 var seqId = this.getCellData(recordIndex,"seqId");
	 return"<a href=javascript:showDetail('" + seqId + "')>详情</a>";
}

function showDetail(seqId){
	  var url = contextPath + "/core/esb/server/update/detail.jsp?seqId=" + seqId;
	  newWindow(url,'820', '500');
	}


function changePublish(cellData,recordIndex,columIndex){
	var publish=this.getCellData(recordIndex,"publish");
	if(publish==0){return "未发送";}
	if(publish==1){return "已发送";}
}

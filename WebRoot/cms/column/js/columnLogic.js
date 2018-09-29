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
	return "<center>"
				+ "<a href=javascript:toSee(" + seqId + ")>站点预览</a>&nbsp;"
				+ "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;"
				+ "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>"
				+ "</center>";
}
function doEdit(seqId){
	location.href = contextPath + "/cms/station/modify.jsp?seqId=" + seqId;
}
/**
 * 详细信息
 * @param seqId
 * @return
 */
function toSee(seqId){
  alert("功能开发中，敬请期待~");
  //var URL = contextPath + "/cms/template/detail.jsp?seqId=" + seqId;
  //newWindow(URL,'820', '500');
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
	if(!window.confirm("确认要删除该栏目 ？")){
		return ;
	}
	var requestURLStr = contextPath + "/yh/cms/column/act/YHColumnAct";
	var url = requestURLStr + "/deleteColumn.act";
	var rtJson = getJsonRs(url, "seqId=" + seqId );
	if (rtJson.rtState == "0") {
	  if(rtJson.rtData == 1){
	    alert("请先删除该栏目的子栏目！");
	  }
	  else{
	    parent.parent.file_tree.location.reload();
	    window.location.reload();
	  }
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
    alert("请至少选择其中一个栏目！");
    return;
  }
  if(!window.confirm("确认要删除已选中的栏目 ？")) {
    return ;
  } 
	var requestURLStr = contextPath + "/yh/cms/column/act/YHColumnAct";
	var url = requestURLStr + "/deleteColumn.act";
	var rtJson = getJsonRs(url, "seqId=" + idStrs );
	if (rtJson.rtState == "0") {
    if(rtJson.rtData == 1){
      alert("部分栏目存在子栏目，请先删除子栏目！");
    }
	  parent.parent.file_tree.location.reload();
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
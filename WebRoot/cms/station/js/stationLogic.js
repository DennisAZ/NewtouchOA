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
	
	//发布权限
	var relUser = this.getCellData(recordIndex,"relUser");
	var relBoo = isPermissions(relUser);
	var relUserStr = "";
	if(relBoo){
	  relUserStr = "<a href=javascript:toRelease(" + seqId + ")>完全发布</a>&nbsp;";
	}
	
	//编辑权限
	var editUser = this.getCellData(recordIndex,"editUser");
	var editBoo = isPermissions(editUser);
	var editUserStr = "";
	if(editBoo){
	  editUserStr = "<a href=javascript:doEdit(" + seqId + ")>修改</a>&nbsp;";
	}


	//删除权限
	var delUser = this.getCellData(recordIndex,"delUser");
	var delBoo = isPermissions(delUser);
	var delUserStr = "";
	if(delBoo){
	  delUserStr = "<a href=javascript:deleteSingle(" + seqId + ")>删除</a>";
	}

	return "<center>"
		 + "<a href=javascript:toSee(" + seqId + ")>站点预览</a>&nbsp;"
		 + relUserStr
		 + editUserStr
		 + delUserStr
		 + "</center>";
}

function isPermissions(userPermissions){
  if(userPermissions == null || userPermissions == ""){
    return false;
  }
	var permissions = new Array("","","");
	var permissionsTemp = userPermissions.split("|");
	for(var j = 0; j < permissionsTemp.length; j++){
	  permissions[j] = permissionsTemp[j];
	}
	var permissionsDept = permissions[0] == null ? "" : permissions[0];
	permissionsDept = "," + permissionsDept + ",";
	var permissionsPriv = permissions[1] == null ? "" : permissions[1];
	permissionsPriv = "," + permissionsPriv + ",";
	var permissionsPerson = permissions[2] == null ? "" : permissions[2];
	permissionsPerson = "," + permissionsPerson + ",";
	
	if(permissionsDept.indexOf((","+personDeptId+",")) > -1 || ",0," == permissionsDept){
	  return true;
	}
	else if(permissionsPriv.indexOf((","+personUserPriv+",")) > -1){
	  return true;
	}
	else if(permissionsPerson.indexOf((","+personId+",")) > -1){
	  return true;
	}
	return false;
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
  var requestURLStr = contextPath + "/yh/cms/station/act/YHStationAct/toSee.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURLStr);
  if(rtJson.rtState == "0"){
    if(rtJson.rtData == 1){
      alert("该站点没有配置首页模板！");
      return;
    }
    var URL = rtJson.rtData;
    newWindow(URL);
  }else{
    alert(rtJson.rtMsrg); 
  }
}

function toRelease(seqId){
  var requestURLStr = contextPath + "/yh/cms/station/act/YHStationAct/toRelease.act?seqId=" + seqId;
  var rtJson = getJsonRs(requestURLStr);
  if(rtJson.rtState == "0"){
    if(rtJson.rtData == 1){
      alert("发布成功！")
      location.reload();
    }
    else if(rtJson.rtData == 0){
      alert("请正确配置该站点的索引模板！"); 
    }
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
	if(!window.confirm("确认要删除该站点 ？")){
		return ;
	}
	var requestURLStr = contextPath + "/yh/cms/station/act/YHStationAct";
	var url = requestURLStr + "/deleteStation.act";
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
    alert("请至少选择其中一个站点！");
    return;
  }
  if(!window.confirm("确认要删除已选中的站点 ？")) {
    return ;
  } 
	var requestURLStr = contextPath + "/yh/cms/station/act/YHStationAct";
	var url = requestURLStr + "/deleteStation.act";
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

/**
 * 字段居中显示
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function trainingCenterFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

function registerTypeFunc(cellData, recordIndex, columIndex){
  var registerType =  this.getCellData(recordIndex,"registerType");
  if(registerType == "1"){
    return "<center>第1次登记</center>";
  }
  if(registerType == "2"){
    return "<center>第2次登记</center>";
  }
  if(registerType == "3"){
    return "<center>第3次登记</center>";
  }
  if(registerType == "4"){
    return "<center>第4次登记</center>";
  }
  if(registerType == "5"){
    return "<center>第5次登记</center>";
  }
  if(registerType == "6"){
    return "<center>第6次登记</center>";
  }
  return "";
}

function fillTimeFunc(cellData, recordIndex,columInde) {
  var fillTime =  this.getCellData(recordIndex,"fillTime");
  return "<center>" +fillTime.substr(0,10)+ "</center>";
}
function assessingTimeFunc(cellData, recordIndex,columInde) {
	var str = "";
	if(cellData){
		str = cellData.substr(0,10);
	}
	return "<center>" + str + "</center>";
}
function assessingStatusFunc(cellData, recordIndex, columIndex){
  var assessingStatus = this.getCellData(recordIndex,"assessingStatus");
  if(assessingStatus == "0"){
    return "<center>待审批</center>";
  }else if(assessingStatus == "1"){
    return "<center><font color=green>已批准</font></center>";
  }else if(assessingStatus == "2"){
    return "<center><font color=red>未通过</font></center>";
  }
}

/**
 * 申请人
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function proposerFunc(cellData, recordIndex, columIndex){
	var str = "";
  var mProposer = this.getCellData(recordIndex,"proposer");
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/getUserName.act";
  var rtJson = getJsonRs(url, "userId=" + mProposer);
  if (rtJson.rtState == "0") {
  	str = rtJson.rtData
    return "<center>" + str + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 审批人
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function assessingOfficerFunc(cellData, recordIndex, columIndex){
  var mProposer = this.getCellData(recordIndex,"assessingOfficer");
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/getUserName.act";
  var rtJson = getJsonRs(url, "userId=" + mProposer);
  if (rtJson.rtState == "0") {
    if(rtJson.rtData == ""){
      return "<center><font color=green>批量审批</font></center>";
    }else{
      return "<center>" + rtJson.rtData + "</center>";
    }
    
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 详细信息
 * @param seqId
 * @return
 */
function approvalDetailFunc(seqId){
  var URL = contextPath + "/subsys/oa/fillRegister/approval/approvalDetail.jsp?seqId=" + seqId;
  newTrainingWindow(URL,'800','600');
}

/**
 * 打开新窗口  
 * @param url
 * @param width
 * @param height
 * @return
 */
function newTrainingWindow(url,width,height){
  var myleft = (screen.availWidth - width)/2;
  var mytop = (screen.availHeight - height)/2;
  window.open(url, "meeting", 
      "height=600,width=800,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" + mytop + ", left=" + myleft + ", resizable=yes");
}

function checkupFunc(seqId, assessingStatus){
  var msg = "";
  if(assessingStatus == "1"){
    msg = '确认要审批通过此补登记申请吗？请填写审批意见：';
  }else{
    msg = '确认要驳回此补登记申请吗？请填写驳回理由：';
  }
  $("confirm").innerHTML = "<font color=red>" + msg + "</font>";
  $("seqId").value = seqId;
  $("assessingStatus").value = assessingStatus;
  ShowDialog();
    
}

/**
 * 查看页面格局css操作
 * @return
 */
function ShowDialog(){
  $("apply").style.left = (parseInt(document.body.clientWidth) - parseInt($("apply").style.width))/2;
  $("apply").style.top = 150;
  $("overlay").style.width = document.body.clientWidth;
  if(parseInt(document.body.scrollHeight) < parseInt(document.body.clientHeight)){
     $("overlay").style.height = document.body.clientHeight;
  }else{
   //$("overlay").style.height = document.body.scrollHeight;
   $("overlay").style.display = 'block';
   $("apply").style.display = 'block';
  }
  window.scroll(0,0);
}

function HideDialog(){
  var div = $('apply');
  div.style.display = "none";
  var overlay = $('overlay');
  overlay.style.display = "none";
}

function doSubmit(){
  if($("assessingView").value == ""){ 
    alert("请填写审批意见！");
    $("assessingView").focus();
  return false;
  }
  var pars = Form.serialize($('form1'));
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/updateStatus.act";
  var rtJson = getJsonRs(url, pars);
  if (rtJson.rtState == "0") {
    var data = rtJson.rtData;
    location = contextPath + "/subsys/oa/fillRegister/approval/approval.jsp?data="+data;
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function checkBoxRender(cellData, recordIndex, columIndex){
  var diaId = this.getCellData(recordIndex,"seqId");
    return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
}

function checkSelf(){
  var allCheck = $('checkAlls');
  if(allCheck.checked){
    allCheck.checked = false;
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
  var mStatus = this.getCellData(recordIndex,"assessingStatus");
  if(mStatus == "0") {
    return "<center><a href=javascript:approvalDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
         + "<a href=javascript:checkupFunc(" + seqId + ",1)>批准</a>&nbsp;"
         + "<a href=javascript:checkupFunc(" + seqId + ",2)>拒绝</a>&nbsp;"
         + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
  }
  if(mStatus == "1") {
    return "<center><a href=javascript:approvalDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
         //+ "<a href=javascript:checkupFunc(" + seqId + ",2)>拒绝</a>&nbsp;"
         + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
  }
  if(mStatus == "2") {
    return "<center><a href=javascript:approvalDetailFunc(" + seqId + ")>详细信息</a>&nbsp;"
         //+ "<a href=javascript:checkupFunc(" + seqId + ",1)>批准</a>&nbsp;"
         + "<a href=javascript:deleteSingle(" + seqId + ")>删除</a></center>";
  }
}

function confirmDel() {
  if(confirm("确认要删除该补登记申请吗？")) {
    return true;
  } else {
    return false;
  }
}

function confirmDelAll() {
  if(confirm("确认要删除已选中的补登记申请吗？")) {
    return true;
  } else {
    return false;
  }
}

function deleteSingle(seqId){
  if(!confirmDel()) {
   return ;
  }  
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/deleteSingle.act";
  var rtJson = getJsonRs(url, "seqId=" + seqId);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

/**
 * 是否选中
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

/**
 * 批量删除
 * @return
 */
function deleteAll() {
  var idStrs = checkMags('deleteFlag');
  if(!idStrs) {
    alert("要删除补登记申请，请至少选择其中一个。");
    return;
  }
  if(!confirmDelAll()) {
    return ;
  }  
  var url = contextPath + "/yh/subsys/oa/fillRegister/act/YHAttendApprovalAct/deleteAll.act";
  var rtJson = getJsonRs(url, "sumStrs=" + idStrs);
  if (rtJson.rtState == "0") {
    window.location.reload();
  } else {
    alert(rtJson.rtMsrg); 
  }
}

function assessingStatusFunc(cellData, recordIndex, columIndex){
  var assessingStatus = this.getCellData(recordIndex,"assessingStatus");
  if(assessingStatus == "0"){
    return "<center>待审批</center>";
  }else if(assessingStatus == "1"){
    return "<center><font color=green>已批准</font></center>";
  }else if(assessingStatus == "2"){
    return "<center><font color=red>未通过</font></center>";
  }
}


/**
 * 打开新窗口  
 * @param url
 * @param width
 * @param height
 * @return
 */
function newWindow(url,width,height){
  var locX=(screen.width-width)/2;
  var locY=(screen.height-height)/2;
  window.open(url, "meeting", 
      "height=540,width=740,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes, top=" 
      + locY + ", left=" + locX + ", resizable=yes");
}

function Test(leaveDate1,leaveDate2){ 
//var leaveDate1 = document.getElementById("leaveDate1"); 
//var leaveDate2 = document.getElementById("leaveDate2"); 
  var leaveDate1Array = leaveDate1.value.trim().split(" "); 
  var leaveDate2Array = leaveDate2.value.trim().split(" "); 
  var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"　; 
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2); 
  
  if(leaveDate1.value){
    if(leaveDate1Array.length != 2){ 
      alert("错误,起始时间格式不对，应形如 2010-01-01 14:55:20"); 
      leaveDate1.focus(); 
      leaveDate1.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate1Array[0]) || leaveDate1Array[1].match(re1) == null || leaveDate1Array[1].match(re2) != null){ 
        alert("错误,起始时间格式不对，应形如 2010-01-01 14:55:20"); 
        leaveDate1.focus(); 
        leaveDate1.select(); 
        return false; 
      } 
    } 
  }
  if(leaveDate2.value){
    if(leaveDate2Array.length != 2){ 
      alert("错误,截止时间格式不对，应形如 2010-01-01 14:55:20"); 
      leaveDate2.focus(); 
      leaveDate2.select(); 
      return false; 
    }else{ 
      if(!isValidDateStr(leaveDate2Array[0])||leaveDate2Array[1].match(re1) == null || leaveDate2Array[1].match(re2) != null){ 
        alert("错误,截止时间格式不对，应形如 2010-01-01 14:55:20"); 
        leaveDate2.focus(); 
        leaveDate2.select(); 
        return false; 
      } 
    } 
  }
  if(leaveDate1.value && leaveDate2.value){
    if(leaveDate1.value > leaveDate2.value){
      alert("错误 起始时间不能大于截至时间！");
      leaveDate1.focus(); 
      leaveDate1.select(); 
      return false;
    }
  }
  return true;
}
//验证是否为日期(不包含时间)
function checkDate(leaveDate1,leaveDate2){
	//var leaveDate1 = document.getElementById("incentiveTime1"); 
	//var leaveDate2 = document.getElementById("incentiveTime2"); 
	var leaveDate1Array = leaveDate1.value.trim().split(" "); 
	var leaveDate2Array = leaveDate1.value.trim().split(" "); 
	if(!leaveDate1.value  || !leaveDate2.value){
		if(leaveDate1.value){
			if(!isValidDateStr(leaveDate1.value)){
				alert("日期格式不对，应形如 2000-01-01"); 
				leaveDate1.focus(); 
				leaveDate1.select(); 
				return false; 
			}
		}else if(leaveDate2.value){
			if(!isValidDateStr(leaveDate2.value)){
				alert("日期格式不对，应形如2000-01-01"); 
				leaveDate2.focus(); 
				leaveDate2.select(); 
				return false; 
			}
		}
		return true;
	}
	if(!isValidDateStr(leaveDate1.value)){
		alert("日期格式不对，应形如 2000-01-01"); 
		leaveDate1.focus(); 
		leaveDate1.select(); 
		return false; 
	}
	if(!isValidDateStr(leaveDate2.value)){
		alert("日期格式不对，应形如 2000-01-01"); 
		leaveDate2.focus(); 
		leaveDate2.select();  
		return false; 
	}
	if(leaveDate1.value >= leaveDate2.value){ 
		alert("开始日期不能大于或等于结束日期！"); 
		leaveDate1.select(); 
		leaveDate1.focus(); 
		return (false); 
	}
	return true;
}

function ClearUser(TO_ID, TO_NAME){
  if(TO_ID == "" || TO_ID == "undefined" || TO_ID == null){
    TO_ID = "TO_ID";
  TO_NAME = "TO_NAME";
  }
  document.getElementsByName(TO_ID)[0].value = "";
  document.getElementsByName(TO_NAME)[0].value = "";
  $("tInstitutionName").value = "";
}


/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件

 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"280\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
 
}

/**
 * 隐藏显示控件
 * 
 * @param cntrlId
 * @return
 */
function showCntrl(cntrlId) {
  if ($(cntrlId)) {
    if ($(cntrlId).style.display) {
      $(cntrlId).style.display = '';
    } else {
      $(cntrlId).style.display = 'none';
    }
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

function sexRender(cellData, recordIndex, columIndex){
  if(cellData == "0"){
    return "<center>男</center>";
  }else if(cellData == "1"){
    return "<center>女</center>";
  }else{
    return "";
  }
}

function deptNameFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

function telNoDeptFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

function telNoHomeFunc(cellData, recordIndex, columIndex){
  return "<center>" + cellData + "</center>";
}

function mobilNoFunc(cellData, recordIndex, columIndex){
  return "<center> <a href=\"javascript:mobilFun(" + cellData + ");\">"+cellData+"</a></center>";
}

function mobilFun(cellData){
  location = contextPath + "/core/funcs/mobilesms/new/index.jsp?outUser="+ cellData;
}

function emailFunc(cellData, recordIndex, columIndex){
  return "<center>"+cellData+"<center>";
}
function psnNameFunc(cellData, recordIndex, columIndex){
  return "<center>"+cellData+"</center>"
}
function psnNoFunc(cellData, recordIndex, columIndex){
  return "<center>"+cellData+"</center>"
}

function groupNameFunc(cellData, recordIndex, columIndex){
  var groupName = "";
  var groupId = cellData;
  if(groupId != 0){
    var urlss = contextPath + '/yh/core/funcs/address/act/YHAddressAct/getGroupName.act';
    var rtJsonss = getJsonRs(urlss, "seqId="+groupId);
    if (rtJsonss.rtState == "0") {
      groupName = rtJsonss.rtData[0].groupName;
    }
  }
  if(groupId == 0){
    groupName = "默认";
  }
  return groupName;
}

/**
 * 
 * @return
 */
function getChecked() {
 var ids= ""
 var checkArray = $$('input');
 for(var i = 0 ; i < checkArray.length ; i++){
   if(checkArray[i].name == "deleteFlag" && checkArray[i].checked ){
     if(ids != ""){
       ids += ",";
     }
     ids += checkArray[i].value;
   }
 }
  return ids;
}

function confirmDel() {
  if(confirm("确认要删除所选联系人吗？")) {
    return true;
  } else {
    return false;
  }
}

function mouseOverHander(show1td){
  show1td.onmouseover = function(){
    show1td.style.backgroundColor = "#E0E0E0";
  }
  show1td.onmouseout = function(){
    show1td.style.backgroundColor = "#FFFFFF";
  }
}

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

function getAllIds(cntrlId){
  var ids= ""
  var checkArray = $$('input');
  for(var i = 0 ; i < checkArray.length ; i++){
    if(checkArray[i].id == cntrlId){
      if(ids != ""){
        ids += ",";
      }
      ids += checkArray[i].value;
    }
  }
  return ids;
}

/**
 * 导出FoxMail格式
 * 
 * @return
 */
function expFoxMail(seqId, groupName){
  location = contextPath + "/yh/core/funcs/address/act/YHAddressAct/exportCsvPublicAddress.act?seqId="+seqId+"&groupName="+encodeURIComponent(groupName);
}

/**
 * 导出OutLook格式
 * 
 * @return
 */
function expOutLook(seqId, groupName){
  location = contextPath + "/yh/core/funcs/address/act/YHAddressAct/exportCsvPublicAddress.act?seqId="+seqId+"&groupName="+encodeURIComponent(groupName);
}

/**
 * 导入
 * 
 * @return
 */

function importAddressPublic(groupId){
  var flag = "1";
  location = contextPath + "/core/funcs/address/public/group/importPublic.jsp?flag="+flag+"&groupId="+groupId;
}

function importAddressPublic2(){
  if($("csvFile").value == ""){ 
    alert("请选择要导入的文件！");
    $("csvFile").focus();
    return false;
  }
  
  var csvFile = $("csvFile").value;
  var csvStr = csvFile.substr(csvFile.length - 3, csvFile.length);
  if(csvStr != "csv"){
    alert("错误,只能导入CSV文件!");
    $("csvFile").focus();
    $("csvFile").select();
    return false;
  }
  
  if($("csvFile").value != ""){
    $('form1').submit();
  }
}

function printFunc(groupId){
  var url = contextPath + "/core/funcs/address/public/group/print.jsp?groupId="+groupId;
  window.open(url);
}

function checkDate(cntrlId){
  var dateStr = $(cntrlId).value;
  return isValidDateStr(dateStr) ;
}

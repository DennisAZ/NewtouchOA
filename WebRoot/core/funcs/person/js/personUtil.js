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

function WarningMsrgLong(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"410\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=" <h4 class=\"title\">信息</h4>"
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

function timeFunc(cellData, recordIndex, columIndex){
  var time = this.getCellData(recordIndex,"time");
  var value = cellData.substr(0, cellData.length - 2);
  return "<center>" + value + "</center>";
}

function ipFunc(cellData, recordIndex, columIndex){
  var ip = this.getCellData(recordIndex,"ip");
  return "<center>" + cellData + "</center>";
}

function typeRender(){
  return "<center>用户批量设置<center>";
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

function confirmDel() {
  if(confirm("确认要删除所选用户吗？")) {
    return true;
  }else {
    return false;
  }
}

function confirmClear() {
  if(confirm("确认要清空所选用户的密码吗？")) {
    return true;
  }else {
    return false;
  }
}

function confirmVisitTime() {
  if(confirm("确认要清空所选用户的在线时长吗？")) {
    return true;
  }else {
    return false;
  }
}

/**
 * 选择角色
 * @return
 */
function selectRolePriv(retArray) {
  roleRetNameArray = retArray;
  var url =contextPath + "/core/funcs/person/MultiRoleSelect.jsp";
  openDialog(url , 280, 400);
}

/**
 * 导出用户
 * 
 * @return
 */
function exportDept(){
  location = contextPath + "/yh/core/funcs/person/act/YHPersonAct/exportToExcel.act";
}

/**
 * 导入用户
 * 
 * @return
 */
function importPerson(){
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

/**
 * 导出主题词
 * 
 * @return
 */
function exportWord(){
  location = contextPath + "/yh/core/funcs/doc/send/act/YHSubjectTermAct/exportToExcel.act";
}
/**
 * 导入主题词
 * 
 * @return
 */
function importWord(){
  var flag = "1";
  var parent = window.parent.wordInput;
  parent.location = contextPath + "/core/funcs/doc/docword/importword.jsp?flag="+flag;
}

function importWord2(){
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

/**
 * 验证特殊字符
 * @param str
 * @return
 */
function checkStr(str){ 
  var re=/[\\"']/; 
  return str.match(re); 
}
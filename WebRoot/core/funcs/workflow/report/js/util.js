/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件

 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"290\">";
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

/**
 * 判断小数位后２位
 * @param aValue
 * @return
 */
function isNumbers(aValue) { 
  var digitSrc = "0123456789"; 
  aValue = "" + aValue; 
  if (aValue.substr(0, 1) == "-") { 
    aValue = aValue.substr(1, aValue.length - 1); 
  } 
  var strArray = aValue.split("."); 
  // 含有多个“.” 
  if (strArray.length > 2) { 
    return false; 
  } 
  var tmpStr = ""; 
  for (var i = 0; i < strArray.length; i++) { 
    tmpStr += strArray[i]; 
  } 
  for (var i = 0; i < tmpStr.length; i++) { 
    var tmpIndex = digitSrc.indexOf(tmpStr.charAt(i)); 
    if (tmpIndex < 0) { 
  // 有字符不是数字 
      return false; 
    } 
  } 
  if(aValue.indexOf(".") != -1){
    var str = aValue.substr(aValue.indexOf(".")+1, aValue.length-1);
    if(str.length > 2){
      return false;
    }
    if(str.length == 0){
      return false;
    }
  }
  // 是数字 
  return true;
}

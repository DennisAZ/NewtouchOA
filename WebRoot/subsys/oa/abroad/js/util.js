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

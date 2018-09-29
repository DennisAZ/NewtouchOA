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

function roleStyle(cellData, recordIndex, columIndex){
  var privNo = this.getCellData(recordIndex,"privNo");
  var html = "<center>" + privNo + "</center>";
  return html;
}

function roleNameStyle(cellData, recordIndex, columIndex){
  var seqId = this.getCellData(recordIndex,"seqId");
  var privName = this.getCellData(recordIndex,"privName");
  var html = "";
  if(seqId == "1"){
    html = "<center><font color=red>" + privName + "</font ></center>";
  }else if(privName=="OA 安全员"){
	  html = "<center><font color=red>" + privName + "</font ></center>";
  }else if(privName=="OA 审计员"){
	  html = "<center><font color=red>" + privName + "</font ></center>";
  }else if(privName=="未分配角色"){
	  html = "<center><font color=#666666>" + privName + "</font ></center>";
  }else{
    html = "<center>" + privName + "</center>";
  }
  
  return html;
}


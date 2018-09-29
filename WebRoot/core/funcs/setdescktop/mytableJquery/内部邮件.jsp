<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
</head>
<body>
<div id="innerEmail" class="module_body" style="overflow:hidden;width:100%;">
  <div id="innerEmail_top" class="moduleTypeLink"><a href="javascript:getEmail(0);" id="today">全部</a> | <a href="javascript:getEmail(1);" id="ground">未读</a> | <a href="javascript:getEmail(2);" id="task">已读</a></div>
    <div id="innerEmail_ul" class="module_div" style="overflow:hidden;position:relative;" >
    <ul id="innerEmail_ul1"  type="disc">
    </ul>
    </div>
    <div style="clear:both;"></div>
  </div>
  <script>

window.getEmail = function (type){
  var lines = ${param.lines};
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/deskMoudel.act?type=" + type;
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    var prcs = rtJson.rtData.pageData;
    $('innerEmail_ul1').update("");
    //设置
    $('innerEmail_ul').setStyle({height: 20 * lines + 'px'});
    $('innerEmail_ul1').setStyle({position: 'relative'});

    $('innerEmail_ul1').setStyle({'top': '0px'});
    cfgModule({
      records:  prcs.length ,
      lines: lines,
      name: '内部邮件',
      showPage:  function(i){
        $('innerEmail_ul1').setStyle({'top': (- i * lines * 20) + 'px'});
      }
    });
    if (prcs.length > 0 ) {
      for(var i = 0; i < prcs.length ;i++){
        var prc = prcs[i];
        var str = getLiMail(prc);
        $('innerEmail_ul1').insert(str);
      }
    } else {
      $('innerEmail_ul1').update("<li>暂无内部邮件 </li>");
    }
  }
}
window.mailStatus = function (mailBodyId,typesy){
  var  param = "&bodyId=" + mailBodyId + "&type=" + typesy;
  var url = contextPath + "/yh/core/funcs/email/act/YHInnerEMailAct/getMailStatus.act";
  var rtJson = getJsonRs(url, param);
  if (rtJson.rtState == "0") {
    return rtJson.rtData;
  }else {
    alert(rtJson.rtMsrg); 
    return -1;
  } 
}
window.bindStatus = function (emailBodyId,type){
   var status = mailStatus(emailBodyId,type);
   var img = "";
   var title = "";
   var html = "";
   switch (status){   
     case "1" :  
       img = "mailDel";
       title = "收件人已删除";
       break;  
     case "2"   : 
       img = "mailClose";
       title = "收件人未读";
       break;
     case "3"   : 
       img = "mailOpen";
       title = "收件人已读";
       break; 
//     case "4"   : 
//       img = "mailDel";
//       title = "发件人已删除";
 //      break; 
     case "5"   : 
       img = "mailNew";
       title = "新邮件";
       break;
     case "6"   : 
       img = "mailOpen";
       title = "已读";
       break;
     default :
       img = "";
       title = "";  
   }   
   if(img){
     html = "<img  src=\"" + contextPath + "/core/funcs/email/image/" + img + ".gif\" title=\"" + title + "\"align=\"absmiddle\">";
   }
   return html;
 }
window.getUserNameById = function (userId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getUserName.act";
  var rtJson = getJsonRs(url , "userId=" +  userId);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }
}
window.getLiMail = function (prc) {
  //var deptName = getUserNameById(prc.fromId);
 // var status = bindStatus(prc.emailBodyId,2);
 var url = contextPath + "/core/funcs/email/inbox/read_email/index.jsp";
 if ("1"== prc.isWebmail) {
   url = contextPath + "/core/funcs/email/webbox/read_email/index.jsp";
 }
  var moduleBody = "<li><a title=\"发送内部邮件，部门：\"  href=\"javascript:emailFunc(" + prc.fromId + ");\">" + prc.fromName + " </a><a href=\"javascript:top.dispParts('" + url + "?seqId=" + prc.emailBodyId + "&mailId=" + prc.emailId + "');\">" + prc.subject + "</a>&nbsp;" + prc.stauts;
  return moduleBody;
}
window.emailFunc = function (id) {
  var toId = id;
  var url = contextPath + "/core/funcs/email/new/index.jsp?toId=" + toId;
  openDialog(url,'800', '650');
}
getEmail(0);

var scroll = ${param.scroll};
if (scroll){
  Marquee('innerEmail_ul1',80,1);
}
</script>
</body>
</html>

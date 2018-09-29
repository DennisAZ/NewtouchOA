/**
 * 查找document
 * 
 * @param n
 * @return
 */
function findObj(n) {
  return document.getElementById(n);
}
/**
 * 日期界面
 * 
 * @param year
 * @param month
 * @return
 */
function doCal(year, month) {
  var mutativeYear = "";
  var mutativeMonth = "";
  var dateStr = "";
  var nowDate, mutativeDate;
  nowDate = new Date();
  mutativeDate = new Date();
  if (year) {
    mutativeDate.setFullYear(year);
  }
  if (month) {
    mutativeDate.setMonth(month - 1);
  }
  mutativeDate.setDate(1);
  mutativeYear = mutativeDate.getYear();
  mutativeMonth = mutativeDate.getMonth();
  var mutativeDay = mutativeDate.getDay();
  mutativeYear = mutativeYear < 1900 ? mutativeYear + 1900 : mutativeYear;
  document.getElementById("yearMonth").innerHTML = mutativeYear + "-"
      + (mutativeMonth + 1);
  for (i = 0; i < 41; i++) {
    if ((i < mutativeDay) || (mutativeMonth != mutativeDate.getMonth())) {
      dateStr = "&nbsp;";
    } else {
      dateStr = mutativeDate.getDate();
      mutativeDate.setDate(mutativeDate.getDate() + 1);
      var showMonthStr = (mutativeMonth + 1) < 10 ? "0" + (mutativeMonth + 1) : (mutativeMonth + 1);
      var showDateStr = dateStr < 10 ? "0" + dateStr  : dateStr;
      if (dateStr == nowDate.getDate() && mutativeYear == (nowDate.getYear() < 1900 ? nowDate.getYear() + 1900 : nowDate.getYear())
          && mutativeMonth == nowDate.getMonth()){
        dateStr = "<b><a href=\"diaryBody.jsp?DiaDateDiary=" + mutativeYear
            + "-" + showMonthStr + "-" + showDateStr
            + "\" target='diaryBody'><font color='red'>" + dateStr
            + "</font></a></b>";
      } else {
        dateStr = "<b><a href=\"diaryBody.jsp?DiaDateDiary=" + mutativeYear
            + "-" + showMonthStr + "-" + showDateStr
            + "\" target='diaryBody'>" + dateStr + "</a></b>";
      }
    }
    findObj('day' + i).innerHTML = dateStr;
  }
}
/**
 * 月份加减
 * 
 * @param op
 * @return
 */
function setMon(op) {
  var ym = document.getElementById("yearMonth").innerHTML.split("-");
  if (op == -1) {
    ym[1] = ym[1] - 1;
    if (ym[1] <= 0) {
      ym[1] = 12;
      ym[0] = ym[0] - 1;
    }
  } else if (op == 1) {
    ym[1] = parseInt(ym[1]) + parseInt(1);
    if (parseInt(ym[1]) > 12) {
      ym[1] = 1;
      ym[0] = parseInt(ym[0]) + parseInt(1);
    }
  }
  doCal(ym[0], ym[1]);
}
/**
 * 清除类容
 * 
 * @param ctrlId
 * @param ctrlIdDesc
 * @return
 */
function Clear() {
  var args = $A(arguments);
  for ( var i = 0; i < args.length; i++) {
    var cntrl = $(args[i]);
    if (cntrl) {
      if (cntrl.tagName.toLowerCase() == "td"
          || cntrl.tagName.toLowerCase() == "div"
          || cntrl.tagName.toLowerCase() == "span") {
        cntrl.innerHTML = '';
      } else {
        cntrl.value = '';
      }
    }
  }
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
 * 显示日期控件
 * 
 * @return
 */
function showCalendar(cntrlId, isHaveTime, imgId) {
  var beginParameters = {
    inputId : cntrlId,
    property : {
      isHaveTime : isHaveTime
    },
    bindToBtn : imgId
  };
  new Calendar(beginParameters);
}
/**
 * 绑定日期 
 * @param date
 * @param cntrlId
 * @return
 */
function showDate(date, cntrlId) {
  var dateShow = null;
  var showStr = "";
  try {
    if (Object.isString(date)) {
      dateShow = Date.parse(date);
    } else {
      dateShow = date;
    }
    showStr += (dateShow.getYear() < 1900 ? 1900 + dateShow.getYear() : dateShow.getYear() )+ "-";
    showStr += ((dateShow.getMonth() + 1) < 10 ? ("0" + (dateShow.getMonth() + 1)) : (dateShow.getMonth() + 1)) + "-";
    showStr += dateShow.getDate() < 10 ? "0" + dateShow.getDate() : dateShow.getDate();
  } catch (e) {
    showStr = date;
  }
  if (cntrlId) {
    $(cntrlId).value = showStr;
  } else {
    return showStr;
  }
}
/**
 * <DIV class=diary_type>工作日志 | 日志日期：2010-03-05 | 最后修改：2010-03-05 14:42:18 | <A
 * href="read.php?DIA_ID=6&amp;FROM_FLAG=1&amp;DIA_COUNT=1&amp;DIA_ID_STR=7,6,">点评(0)</A></DIV>
 * <DIV style="OVERFLOW-X: auto; OVERFLOW-Y: auto; WIDTH: 100%; HEIGHT: 100%"
 * class=content>sssss </DIV>
 * 
 * @return
 */
function bindOneNews(json,showOp,type) {
  if(!type){
    type = "0";
  }
  var obj = json.data;
  var news = "<table width='100%' class='TableTop subject'><tr><td class='left'></td><td class='center'><A href=\"" + contextPath + "/core/funcs/diary/comment/index.jsp?diaId=" 
  + obj.seqId + "&type=" + type + "\">" + obj.subject + "</A></td><td class='right'></td></tr></table>" 
    + "<DIV class=diary_type>";
  if (obj.diaType == "1") {
    news += "工作日志";
  } else {
    news += "个人日志";
  }
  news += " | 日志日期："
      + cutDate(obj.diaDate)
      + " | 最后修改："
      + obj.diaTime
      + " | ";
  if(!showOp){
    news += "<A href=\"" + contextPath + "/core/funcs/diary/comment/index.jsp?diaId=" + obj.seqId + "\">点评(" + json.reviewCount + ")</A>" ;
  }else{
    news += "日志作者:" + getUserNameById(obj.userId);
  }
  news += "</DIV>"
      + "<DIV style=\"OVERFLOW-X: auto; OVERFLOW-Y: auto;\" class=content>"
      + obj.content + "</DIV>";
  if (obj.attachmentId.trim()) {
    news += "<DIV class=content><BR><BR>附件：<BR><span id=\"attr_" + obj.seqId + "\"></span></DIV>"
  }
  news += "<DIV class=operate>";
  if(!showOp){
   news += "<INPUT class=SmallButtonB onclick=\"showReader('" + obj.seqId
        + "')\" value=阅读人员 type=button name=button> "
        + "<INPUT class=SmallButtonB onclick=\"showDiaDetaile('" + obj.seqId
        + "')\" value=查看全文 type=button name=button> ";
    if (!json.lock) {
      news += "<INPUT class=SmallButtonA onclick=\"editDiary('" + obj.seqId
          + "')\" value=编辑 type=button name=button> "
    }
   if(!json.lock && !json.reviewCount) {
      news += "<INPUT onclick = checkOne(this); value=" + obj.seqId + " type=checkbox name=\"diaCheckBox\" class=\"diaCheckBox\">";
   }
  }
  news += " </DIV>";
  return news;
}
/**
 * 消息提示
 * @param msrg
 * @param cntrlId 绑定消息的控件
 * @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
 * @return
 */
function WarningMsrg(msrg, cntrlId,type ) {
  var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"360\">";
  if(!type){
    type = "info";
  }
  msrgDom += " <tr>  <td class=\"msg " + type + "\">"
  msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
      + " </td> </tr> </table>";
  $(cntrlId).innerHTML = msrgDom;
}
/**
 * 显示操作面板
 * @param cntrlId
 * @return
 */
function showOpreater(cntrlId) {
  var opStr = " <DIV class=\"one_diary\">"
      + "<div class=\"operatebar\">"
      + "<input type=\"checkbox\" name=\"allbox\" id=\"allbox_for\" onClick=\"checkAll();\"><label for=\"allbox_for\">全选</label> &nbsp;"
      + " <a href=\"javascript:deleteAll();\" title=\"删除所选日志\"><img src=\"" + imgPath + "/cmp/email/inbox/delete.gif\" align=\"absMiddle\">&nbsp;删除</a>&nbsp;"
      + "</div></DIV>";
  $(cntrlId).insert(opStr, 'bottom');
}
/**
 * 取得所有的ckeckBox
 * box -- id = 'diaCheckBox'
 * @return
 */
function checkAll(){
	
  var checks = $$('input.diaCheckBox');
 var i = 0 ;
 for (; i < checks.length; i++){
   if($('allbox_for').checked){
     checks[i].checked = true;
   } else{
     checks[i].checked = false;
   }
 }
}
/**
 * 
 * @param el
 * @return
 */
function checkOne(el){
   if(!el.checked)
     $('allbox_for').checked=false;
}
/**
 * 组织所有的以选择日志IDs
 * @return
 */
function selectCheckBox(){
   var checks = $$('input.diaCheckBox');
  var ids = "";
  for(var i = 0 ; i < checks.length ; i++){
    if(checks[i].checked){
      if(ids != ""){
        ids += ",";
      }
      ids += checks[i].value;
    }
  }
  return ids;
}

/**
 * 工作日志查询界面的一条工作日志内容
 * @param json
 * @return
 */
function bindOneNews2Info(json) {
  
  
  
  
  var obj = json.data;
  
  var news = "<table width='100%' class='TableTop subject'><tr><td class='left'></td><td class='center'><A href=\"" + contextPath + "/core/funcs/diary/comment/index.jsp?diaId=" 
  + obj.seqId + "&type=1\">" + obj.subject + " ("+ getUserNameById(obj.userId) + ")</A></td><td class='right'></td></tr></table>" 
    + "<DIV class=diary_type>";
  
  if (obj.diaType == "1") {
    news += "工作日志";
  } else {
    news += "个人日志";
  }
  news += " | 日志日期："
      + cutDate(obj.diaDate)
      + " | 最后修改："
      + obj.diaTime
      + "</DIV>"
      + "<DIV style=\"OVERFLOW-X: auto; OVERFLOW-Y: auto;\" class=content>"
      + obj.content + "</DIV>";
  if (obj.attachmentId.trim()) {
    news += "<DIV class=content><BR><BR>附件：<BR><span id=\"attr_" + obj.seqId + "\"></span></DIV>"
  }
  news += "<div class=\"content\" id=\"comment_" + obj.seqId + "\"></div>";
  news += "<DIV class=operate>"
      +"<INPUT class=SmallButtonC onclick=\"share(\'" + obj.seqId
      + "\',\'" + obj.userId + "\')\" value=指定共享范围  type=button name=button> "
      + "<INPUT class=SmallButton onclick=\"commentEdit('" + obj.seqId
      + "','" + obj.userId + "')\" value=点评 type=button name=button> ";
  news += " </DIV></div>";
  
  return news;
}
/**
 *绑定一条评论
 * @param cntrlId 需要绑定的控件
 * @param index 评论楼层
 * @param commentId 评论seqId
 * @param userId 评论用户Id
 * @param comDate 评论时间
 * @param comflag 评论状态
 * @param content 评论内容
 * @param isDel 是否显示删除操作
 * @param isShowOp 是否显示员工操作
 * @return
 */
function bindComment(cntrlId,index,commentId,userId,comDate,comflag,content,isDel,isShowOp){
  if(!$(cntrlId)){
    return;
  }
  var news = " <input type= \"hidden\" id=\"user_" + commentId + "_" + index + "\" name=\"user_" + commentId + "_" + index + "\" value=\"" + userId + "\"></input>"
  	+ "<font color=\"#0000FF\">" + index + "楼  "
    + "<span id=\"user_" + commentId + "_" + index + "Desc\"></span>&nbsp;&nbsp;<span id=\"comDate\">" + comDate + "</span></font>&nbsp;";
  if(comflag != "1"){
    if(isShowOp){
      news +="&nbsp;&nbsp;<input type=\"button\" value=\"阅读确认\" class=\"SmallButtonB\" title=\"确认阅读过该点评\"  onClick=\"commentReaded(" + commentId + ")\">";
    }else{
      news += "<font color=red>未读</font>&nbsp;";
    }
  }else{
    news += "<font color=green>已读</font>&nbsp;";
  }
  if(isDel){
    news += "<a href=\"javascript:deleteComment('" + commentId + "');\">删除</a>";
  }
  if(isShowOp){
    news +="&nbsp;&nbsp;<input type=\"button\" value=\"回复点评\" class=\"SmallButtonB\" title=\"回复点评\"  onclick=\"replyComment(" + commentId + ")\">";
  }
  news += "<div class=\"replycontent\" id=\"reply_" + commentId + "\">"
    + "<p style=\'margin:5px 5px 0px 5px;padding:5px;\'><span id=\"commentContent\">" + content + "</span></p>"            
    + "</div>";
  //$(cntrlId).insert(news,'bottom');
    $(cntrlId).insert(news,'bottom');
    var cntr = "user_" + commentId + "_" + index;
    bindDesc([{cntrlId:cntr, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
}
/**
 * 绑定回复的显示
 * @param cntrlId
 * @param obj
 * @param type
 * @return
 */
function bindCommentReply(cntrl,obj,type){
  var news = "<div style=\'word-break:break-all;dispaly: block;background-color: #E1E1E1;margin:0px 5px 5px 5px;padding:5px;border:1px solid #666666;\'>"
    + "<input type=\"hidden\" id=\"userId_" + obj.commentId + "_" + obj.seqId + "\" value=\"" + obj.replyer + "\"></input>"
    + "回复：" + obj.replyComment + "<br><br><i>回复时间：" + obj.replyTime+ "&nbsp;&nbsp;回复人：<span id=\"userId_" + obj.commentId + "_" + obj.seqId + "Desc\"></span></i>&nbsp;&nbsp;&nbsp;&nbsp;";
    
    if(type){
      news += "<a href=\"javascript:replyComment(" + obj.commentId + "," + obj.seqId + ",\'1\')\">编辑</a>"
        + "&nbsp;<a href=\"javascript:deleteReply(\'" + obj.seqId + "\')\">删除</a>";
    }
     news += "</div>";
  $(cntrl).insert(news,'bottom');
  var cntr = "userId_" + obj.commentId + "_" + obj.seqId;
  bindDesc([{cntrlId:cntr, dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
}
/**
 * 取得用户姓名
 * @return
 */
function getUserNameById(userId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getUserName.act";
  var rtJson = getJsonRs(url , "userId=" +  userId);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }
}
/**
 * 取得部门名字
 * @return
 */
function getDeptNameById(deptId){
  var url = contextPath + "/yh/core/funcs/diary/act/YHDiaryAct/getDeptName.act";
  var rtJson = getJsonRs(url , "deptId=" +  deptId);
  if(rtJson.rtState == '0'){
    return rtJson.rtData;
  }
}
/**
 * 显示附件
 * @param attrIds
 * @param attrNames
 * @param cntrId
 * @return
 */
function showAttach(attrIds,attrNames,cntrId,readOnly,prec){
  if(!readOnly){
    readOnly = false;
  }
  attachMenuUtil(cntrId,"diary",null,attrNames,attrIds,readOnly,prec ,"","",true);
}
function isValidDateStrTime(str) { 
  var type1 = "^(([0-1]?)[0-9])?([2][0-3])?\:[0-5]?[0-9]\:[0-5]?[0-9]$"; 
  var type2 = "^\:[0-5]?[0-9]\:[0-5]?[0-9]$"; 
  var re1 = new RegExp(type1); 
  var re2 = new RegExp(type2); 
  var strArray = str.trim().split(" "); 
  if(strArray.length!=2){ 
    return false; 
  }else{ 
    if(!isValidDateStr(strArray[0])||strArray[1].match(re1) == null || strArray[1].match(re2) != null){ 
      return false; 
    } 
   } 
  }
/** 
* 判断是否为合法的日期串yyyy-MM-dd 

* 
* @str 日期串 

*/ 
function isValidDateStr(str) { 
  if (!str) { 
    return; 
   } 
   var r = str.match(/^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2})$/); 
   if (r == null) { 
     return false; 
    } 
   if (parseInt(r[1]) > 9999 || parseInt(r[1]) < 1753) { 
     return false; 
    } 
    var d = new Date(r[1], r[3]-1, r[4]); 
    return (d.getFullYear() == r[1] && (d.getMonth() + 1) == r[3] && d.getDate() == r[4]); 
}
function checkDate(cntrlId){
  var dateStr = $(cntrlId).value;
  return isValidDateStr(dateStr) ;
}
function cutDate(dateStr){
  if(!dateStr){
    return;
  }
  return dateStr.substring(0,10);
}

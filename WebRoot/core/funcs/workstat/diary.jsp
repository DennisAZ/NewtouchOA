<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
//获取第一天的日期和最后一天的日期
   //获取该月第一天和左后一天  
      SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM-dd");
      Calendar   cDay1   =   Calendar.getInstance();   
      cDay1.setTime(new Date());   
      int lastDay   =   cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);   
      Date   lastDate   =   cDay1.getTime();   
      lastDate.setDate(lastDay);   
    
    String date=curTime.format(lastDate);
    String startDate=date.substring(0,7)+"-01";
    String endDate=date;

  String startDateStr =request.getParameter("startDate");
  String endDateStr = request.getParameter("endDate");
  
  startDateStr=startDateStr.substring(0,10);
  endDateStr=endDateStr.substring(0,10);
  
  String userId =request.getParameter("userId") == null ? "" :  request.getParameter("userId");
  String attachmentName ="";
  String subject ="";
  String key1 = "";
  String key2 = "";
  String key3 = "";
  String diaType ="0";
   
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<title>日志查询</title>
<script type="text/javascript">

var pageMgr = null;
var userId = "<%=userId%>";
function doInit() {
  var param = "attachmentName=<%=attachmentName%>&startDate=<%=startDateStr%>&endDate=<%=endDateStr%>&subject=<%=subject%>&key1=<%=key1%>&key2=<%=key2%>&key3=<%=key3%>&userId=<%=userId%>&diaType=<%=diaType%>";
  param = encodeURI(param);
  var url =  contextPath + "/yh/core/funcs/workstat/act/YHWorkStatAct/searchDiarySelf.act?" + param;
  var cfgs = {
    dataAction: url,         
    container: "listContainer",
    moduleName:"diary",
    colums: [
       {type:"selfdef", text:"选择", width: 40,render:checkBoxRender},
       {type:"hidden", name:"seqId", text:"顺序号", width: 150, dataType:"int"},
	   
       {type:"data", name:"diaDate", text:"日期", width: 150, dataType:"date"},       
       {type:"data", name:"subject", text:"日志标题", width: 150,render:subjectRender},
       {type:"data", name:"attach", text:"附件", width: 350, dataType:"attach"},
       {type:"hidden", name:"attachId"},
       {type:"selfdef", text:"点评", width: 40,render:commentRender},
       {type:"selfdef", text:"操作",width: 200, render:optRender}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    showCntrl('listContainer');
    var mrs = " 共 "+ total + " 条记录 ！";
    WarningMsrg(mrs, 'msrg');
    showCntrl('delOpt');
  }else{
    WarningMsrg('无日志记录！', 'msrg');
  }
}
function bindTitle(userId){
  var userName = getUserNameById(userId);
  $('ser_title').innerHTML = "["+ userName + "--- 工作日志查询]";
}
/**
 * subject 描画事件
 */
function optRender(cellData, recordIndex, columIndex) {
  var cntrl = $("sub_" + recordIndex + "_" + columIndex);
  var diaDate =  this.getCellData(recordIndex,"diaDate");
  var diaId = this.getCellData(recordIndex,"seqId");
  var html = "";
  if(!isLocked(cutDate(diaDate))){
    html += "";
  }
  return html; 
}
/**
 * subject 描画
 */
function subjectRender(cellData, recordIndex, columIndex) {
  //alert(cellData);
  var diaId = this.getCellData(recordIndex,"seqId");
  return "<A  id=\"sub_" + recordIndex + "_" + columIndex + "\" href=\"" + contextPath + "/core/funcs/workstat/diary_read.jsp?diaId=" + diaId + "\">" + cellData + "</A >";
}
/**
 * 
 */
 function checkBoxRender(cellData, recordIndex, columIndex){
   var diaId = this.getCellData(recordIndex,"seqId");
   var diaDate =  this.getCellData(recordIndex,"diaDate");
   var count = isComment(diaId);
   if(count != "0" || isLocked(cutDate(diaDate))){
      return "";
    }
   return "<input type=\"checkbox\" name=\"check_diay_search\" value=\"" + diaId + "\" onclick=\"checkSelf()\" >";
 }
 
 function commentRender(cellData, recordIndex, columIndex){
   var diaId = this.getCellData(recordIndex,"seqId");
   var count = isComment(diaId);
   if(count != "0" ){
      return "<A  id=\"comment_" + recordIndex + "_" + columIndex + "\"  href=\"" + contextPath + "/core/funcs/diary/comment/index.jsp?diaId=" + diaId + "\"> 查看 </A >";
    }
   return "";
 }
 function checkSearchAll(){
   var checkArray = $$('input');
   for(var i = 0 ; i < checkArray.length ; i++){
     if(checkArray[i].name == "check_diay_search" ){
      checkArray[i].checked = $('allbox').checked ;
     }
   }
 }
 function checkSelf(){
    var allCheck = $('allbox');
    if(allCheck.checked){
      allCheck.checked = false;
    }
}
 function del(){
   var ids = "";
   var checkArray = $$('input');
   for(var i = 0 ; i < checkArray.length ; i++){
     if(checkArray[i].name == "check_diay_search" && checkArray[i].checked ){
       if(ids != ""){
         ids += ",";
       }
       ids += checkArray[i].value;
     }
   }
   deleteDiary(ids);
 }
 function goBack(){
   location = contextPath + "/core/funcs/diary/query/index.jsp";
 }
</script>
</head>
<body onLoad="doInit()">
<div align="center" class="Big1">
<b id="ser_title"></b>
</div>
<br>

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/diary.gif" WIDTH="18" HEIGHT="18" align="absmiddle"><span class="big3"> 查询结果</span>
    </td>
  </tr>
</table>
<div id="listContainer" style="display:none">
</div>
<div id="delOpt" style="display:none">
  <input type="checkbox"  id="allbox" onClick="checkSearchAll();">
  <label for="allbox">全选</label> &nbsp;
  <input type="button"  value="删除" class="SmallButton" onClick="del();" title="删除所选日志"> &nbsp;
</div>
<div id="msrg">
</div>
<center>
 <input type="button" class="SmallButton" onClick="javascript:window.close();" value="关闭"></input>
</center>
</body>
</html>
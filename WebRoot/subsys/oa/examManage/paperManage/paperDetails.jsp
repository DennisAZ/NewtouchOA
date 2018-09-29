<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
 <%
  String paperSeqId = request.getParameter("paperSeqId") == null ? "" :  request.getParameter("paperSeqId");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>试题管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/examManage/js/paperManageLogic.js"></script>
<script type="text/javascript">
var paperSeqId = "<%=paperSeqId%>";
var pageMgr = null;
var total = 0;
function doInit(){
  if(paperSeqId){
    $("paperSeqId").value = paperSeqId;
    $("questionsCount").value = getQuestionsCount(paperSeqId);
    //$("questionsList").value = getQuestionsList(paperSeqId);
  }else{
    paperSeqId = getPaperSeqId();
    $("paperSeqId").value = getPaperSeqId();
    $("questionsCount").value = getQuestionsCount(paperSeqId);
    //$("questionsList").value = "";
  }
  var url = "<%=contextPath%>/yh/subsys/oa/examManage/act/YHExamPaperAct/getExamPaperListJson.act?paperSeqId="+paperSeqId;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         //{type:"hidden", name:"questionsCount", text:"顺序号", dataType:"String"},
         //{type:"hidden", name:"paperGrade", text:"顺序号", dataType:"String"},
         {type:"data", name:"roomId",  width: '20%', text:"所属题库", render:roomNameFunc},       
         {type:"data", name:"questionsType",  width: '20%', text:"题型", render:questionsTypeFunc},
         {type:"data", name:"questionsRank",  width: '20%', text:"难度", render:questionsRankFunc},
         {type:"data", name:"questions",  width: '20%', text:"题目", render:paperCenterFunc}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      WarningMsrg('目前尚未选择试题', 'msrg');
    }
    $("roomName").innerHTML = getRoomName(paperSeqId);
    $("roomId").value = getRoomId(paperSeqId);
}

function reloadFunc(){
  //parent.navigateFrame.location.reload();
  if(total == 0){
    location = contextPath + "/subsys/oa/examManage/paperManage/paperDetails.jsp?paperSeqId="+paperSeqId;
  }else{
    pageMgr.refreshAll();
  }
}

function doSubmit(){

}
</script>
</head>
<body onload="doInit()" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" align="absmiddle"><span class="big3"> 本试卷包含的试题</span><br>
    </td>
  </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
</div>
<div id="msrg">
</div>
<br>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" align="absmiddle"><span class="big3"> 试题生成</span><br>
    </td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="TableList">
  <tr class="TableHeader">
      <td colspan="6" nowrap>&nbsp;</td>
  </tr>
    <tr>
      <td nowrap class="TableData">所属题库：</td>
      <td class="TableData">
      <span id="roomName"></span>
      </td>
      <td nowrap class="TableData">题型：</td>
      <td class="TableData">
        <select name="questionsType" id="questionsType">
          <option value="" >全部</option>
          <option value="0">单选</option>
          <option value="1" >多选</option>
        </select>
      </td>
      <td nowrap class="TableData">难度：</td>
      <td class="TableData">
        <select name="questionsRank" id="questionsRank">
          <option value="" >全部</option>
          <option value="0" >低</option>
          <option value="1" >中</option>
          <option value="2" >高</option>
        </select>
      </td>
    </tr>
    <tr>
    <tr align="center" class="TableControl">
    <td colspan="6" nowrap>
    <input type="hidden" name="paperSeqId" id="paperSeqId" value="">
    <input type="hidden" name="roomId" id="roomId" value="">
    <input type="hidden" name="questionsCount" id="questionsCount" value="">
    <input type="hidden" name="questionsList" id="questionsList" value="">
    <input type="hidden" name="paperGrade" id="paperGrade" value="">
    <input type="button" value="自动选题" class="BigButton" onclick="autoTopics();">&nbsp;
    <input type="button" value="手动选题" class="BigButton" onClick="handTopics()">&nbsp;
    <input type="button" value="返回" class="SmallButton" onClick="location='<%=contextPath%>/subsys/oa/examManage/paperManage/manage.jsp';">
    </td>
    </tr>
</table>
</form>
</body>
</html>
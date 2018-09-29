<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>会议查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/meetingmanagelogic.js"></script>
<script type="text/javascript">

function doInit(){
  autoBeginEnd();
  var mgrSec = new SelectMgr();
  mgrSec.addSelect({cntrlId: "mRoom"
              , tableName: "MEETING_ROOM"
              , codeField: "SEQ_ID"
              , nameField: "MR_NAME"
              , value: "<%=0%>", isMustFill: "1"
              , filterField: " "
              , filterValue: ''
              , order: ""
              , reloadBy: ""
              , actionUrl: ""
                ,extData:[new CodeRecord("","全部")]
              });
  mgrSec.loadData();
  mgrSec.bindData2Cntrl();
  
 　var beginParameters = {
     inputId:'beginDate',
     property:{isHaveTime:false}
     ,bindToBtn:'beginDateImg'
 };
 new Calendar(beginParameters);

 var endParameters = {
     inputId:'endDate',
     property:{isHaveTime:false}
     ,bindToBtn:'endDateImg'
 };
 new Calendar(endParameters);
}

function doSubmit(){
  var beginDate = $("beginDate").value;
  if(beginDate){
    if(!isValidDateStr(beginDate)){
      alert("错误,起始日期格式不对，应形如 2010-01-02");
      $("beginDate").focus();
      $("beginDate").select();
      return false;
    }
  }

  var endDate = $("endDate").value;
  if(endDate){
    if(!isValidDateStr(endDate)){
      alert("错误,终止日期格式不对，应形如 2010-01-02");
      $("endDate").focus();
      $("endDate").select();
      return false;
    }
  }
  var query = $("form1").serialize();
  location = "<%=contextPath%>/subsys/oa/meeting/query/search.jsp?"+query;
}

</script>
</head>
<body onload="doInit()" topmargin="5">
  <table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" align="center">
    <tr>
      <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absmiddle"><span class="big3"> 会议查询</span></td>
    </tr>
  </table><br>
  <form enctype="multipart/form-data" action=""  method="post" name="form1" id="form1">
   <table class="TableBlock" width="400" align="center">
    <tr class="TableLine1">
      <td nowrap>名称：</td>
      <td nowrap><INPUT type=text name="mName" id="mName" size=34 class=BigInput></td>
    </tr>
    <tr class="TableLine2">
      <td nowrap>申请人：</td>
      <td nowrap>
        <input type="text" name="mProposerDesc" id="mProposerDesc" size="10" class="BigStatic" readonly>&nbsp;
        <input type="hidden" name="mProposer" id="mProposer" value="">
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['mProposer', 'mProposerDesc']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('mProposer').value='';$('mProposerDesc').value='';">清空</a>
      </td>
    </tr>

    <tr class="TableLine1">
      <td nowrap>开始时间：</td>
      <td nowrap>
      从<input type="text" name="beginDate" id="beginDate" size="10" maxlength="10" class="BigInput" value="">
        <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
      至 <input type="text" name="endDate" id="endDate" size="10" maxlength="10" class="BigInput" value="">
        <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer">
    </tr>
    <tr class="TableLine2">
      <td nowrap>会议室：</td>
      <td nowrap>
      <select name="mRoom" id="mRoom">
       <option value="" selected>全部</option>
   
      </select></td>
    </tr>
    <tr class="TableLine1">
    <td nowrap>会议状态：</td>
      <td nowrap>
      <select name="mStatus">
       <option value="" selected>全部</option>
       <option value="0">待批</option>
       <option value="1">已准</option>
       <option value="2">进行中</option>
       <option value="3">未准</option>
       <option value="4">已结束</option>
      </select></td>
    </tr>
    <tr class="TableControl">
     <td colspan="9" align="center">
      <input type="button" value="查询" class="BigButton" onclick="doSubmit();">&nbsp;&nbsp;
     </td>
    </tr>
   </table>
   </form>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>发文统计查询</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/jtgwjh/sendDoc/js/sendLogic.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
  $('docTitle').focus();
	setDate();
}

function doSubmit(){
	if(checkForm()){
	  $('search').style.display = 'none';
    $('result').style.display = '';
    if(pageMgr){
      getGroup();
      return;
    }
	  var url = "<%=contextPath%>/yh/subsys/jtgwjh/search/act/YHDocSearchAct/getSendListJson.act";
	  var cfgs = {
	    dataAction: url,
	    container: "listContainer",
	    sortIndex: 1,
	    sortDirect: "desc",
	    paramFunc: getParam,
	    colums: [
	       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
	       {type:"data", name:"docTitle",  width: '15%', text:"文件标题" ,align: 'left' ,render: renderDocTitle},
	       {type:"data", name:"docType",  width: '8%', text:"文件类型" ,align: 'center' ,render: renderDocType},
	       {type:"hidden", name:"urgentType",  width: '8%', text:"紧急程度" ,align: 'center' ,render: renderUrgentType},
	       {type:"hidden", name:"securityLevel",  width: '8%', text:"密级" ,align: 'center' ,render: renderSecurityLevel},
	       {type:"data", name:"docNo",  width: '10%', text:"文号" ,align: 'center' },
	       {type:"hidden", name:"reciveDept",  width: '10%', text:"联网接收单位" ,align: 'center' },
	       {type:"data", name:"reciveDeptDesc",  width: '15%', text:"联网接收单位" ,align: 'center' ,render: renderReciveDeptDesc},
	       {type:"hidden", name:"createDatetime",  width: '10%', text:"创建时间" ,align: 'center' ,render: renderCreateDatetime},
	       {type:"hidden", name:"isStamp",  width: '10%', text:"是否盖章" ,align: 'center' },
	       {type:"hidden", name:"stampComplete",  width: '10%', text:"盖章是否完成" ,align: 'center' },
	       {type:"data", name:"status",  width: '10%', text:"状态" ,align: 'center' ,render: renderStatus},
	       {type:"hidden", name:"sendDatetime",  width: '10%', text:"发送时间" ,align: 'center' }]
	  };
	  pageMgr = new YHJsPage(cfgs);
	  pageMgr.show();
	  var total = pageMgr.pageInfo.totalRecord;
	  if(total){
	    showCntrl('listContainer');
	    var mrs = " 共 " + total + " 条记录 ！";
	    showCntrl('delOpt');
	  }else{
	    WarningMsrg('无符合条件的发文登记信息', 'msrg');
	  }
	}
}

function getParam(){
  var queryParam = $("form1").serialize();
  return queryParam;
}

function getGroup(){
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }
  else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $("listContainer").style.display = "";
    $('delOpt').style.display = "";
    $('msrg').style.display = "none";
  }else{
    $('msrg').style.display = "";
    $("listContainer").style.display = "none";
    $('delOpt').style.display = "none";
    WarningMsrg('无发文登记信息', 'msrg');
  }
}

function checkForm(){
  var sendDate1 = $("sendDate1").value;
  if(sendDate1){
    if(!isValidDateStr(sendDate1)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("sendDate1").focus();
      $("sendDate1").select();
      return false;
    }
  }
  var sendDate2 = $("sendDate2").value;
  if(sendDate2){
    if(!isValidDateStr(sendDate2)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("sendDate2").focus();
      $("sendDate2").select();
      return false;
    }
  }
  var sendDate3 = $("sendDate3").value;
  if(sendDate3){
    if(!isValidDateStr(sendDate3)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("sendDate3").focus();
      $("sendDate3").select();
      return false;
    }
  }
  var sendDate4 = $("sendDate4").value;
  if(sendDate4){
    if(!isValidDateStr(sendDate4)){
      alert("调动日期格式不对，应形如 2010-01-02");
      $("sendDate4").focus();
      $("sendDate4").select();
      return false;
    }
  }
  return true;
}

//日期
function setDate(){
  var date1Parameters = {
    inputId:'sendDate1',
    property:{isHaveTime:false}
    ,bindToBtn:'date1'
  };
  new Calendar(date1Parameters);

  var date2Parameters = {
	  inputId:'sendDate2',
	  property:{isHaveTime:false}
	  ,bindToBtn:'date2'
  };
  new Calendar(date2Parameters);
  
  var date3Parameters = {
    inputId:'sendDate3',
    property:{isHaveTime:false}
    ,bindToBtn:'date3'
  };
  new Calendar(date3Parameters);

  var date4Parameters = {
    inputId:'sendDate4',
    property:{isHaveTime:false}
    ,bindToBtn:'date4'
  };
  new Calendar(date4Parameters);
}

function renderStatus(cellData, recordIndex, columIndex){
  var str = "";
  var sendDatetime = this.getCellData(recordIndex,"sendDatetime");
  if(sendDatetime){
    str = '已发送';
  }
  else{
    str = '未发送';
  }
  return str;
}

function back2Search(){
  $('result').style.display = 'none';
  $('search').style.display = '';
}
</script>
</head>
<body onload="doInit();">

<div id="search">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/infofind.gif"><span class="big3"> 发文统计查询</span></td>
  </tr>
</table>
<br>
<form action=""  method="post" name="form1" id="form1" >
<table class="TableBlock" align="center" >
  <tr>
    <td nowrap class="TableContent">文件标题： </td>
    <td class="TableData">
      <input type="text" name="docTitle" id="docTitle" value="" size="37">
    </td>
    <td nowrap class="TableContent">文件类型： </td>
    <td class="TableData">
      <select id="docType" name="docType" style="width:71px;">
        <option value=''></option>
        <option value='0'>普通</option>
        <option value='1'>公文</option>
      </select>
    </td>
    <td nowrap class="TableContent">密级： </td>
    <td nowrap class="TableData" >
      <select id="securityLevel" name="securityLevel" style="width:71px;">
        <option value=''></option>
        <option value='0'>非密</option>
        <option value='1'>内部</option>
        <option value='2'>秘密</option>
        <option value='3'>机密</option>
      </select>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent">文号： </td>
    <td class="TableData">
      <input type="text" name="docNo" id="docNo" value="" size="37">
    </td>
    <td nowrap class="TableContent">紧急程度： </td>
    <td class="TableData" colspan="3">
      <select id="urgentType" name="urgentType" style="width:71px;">
        <option value=''></option>
        <option value='0'>一般</option>
        <option value='1'>紧急</option>
        <option value='2'>特急</option>
      </select>
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableContent">接收单位： </td>
    <td class="TableData" colspan="5">
      <input type="hidden" id="reciveDept" name="reciveDept" value="">
      <textarea id="reciveDeptDesc" name="reciveDeptDesc" rows="3" style="width:80%" class="BigStatic" readonly></textarea>
      <a href="javascript:void(0);" class="orgAdd" onClick="selectOutDept(['reciveDept', 'reciveDeptDesc']);">添加</a>
      <a href="javascript:void(0);" class="orgClear" onClick="$('reciveDept').value='';$('reciveDeptDesc').value='';">清空</a>
    </td>
  </tr>
  
  <tr>
    <td nowrap class="TableContent">创建日期： </td>
    <td nowrap class="TableData" >
      <input type="text" name="sendDate1" id="sendDate1" class="BigInput" size="8" maxlength="10" value="" >&nbsp;
      <img id="date1" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    &nbsp;至&nbsp;
      <input type="text" name="sendDate2" id="sendDate2" class="BigInput" size="8" maxlength="10" value="" >&nbsp;
      <img id="date2" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    </td>
    <td nowrap class="TableContent">发送日期： </td>
    <td nowrap class="TableData" colspan="3">
      <input type="text" name="sendDate3" id="sendDate3" class="BigInput" size="8" maxlength="10" value="" >&nbsp;
      <img id="date3" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    &nbsp;至&nbsp;
      <input type="text" name="sendDate4" id="sendDate4" class="BigInput" size="8" maxlength="10" value="" >&nbsp;
      <img id="date4" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand"> 
    </td>
  </tr>
    <tr align="center" class="TableControl">
      <td colspan="6" nowrap>
        <input type="button" value="查询" onclick="doSubmit();" class="BigButton">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">&nbsp;&nbsp;
      </td>
    </tr>
 </table>
</form>
</div>

<div id="result" style="display:none;">
<table border="0" width="80%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif"><span class="big3"> 发文统计查询结果 </span></td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
<div id="delOpt" style="display:none">
</div>
<div id="msrg">
</div>
<div id="backDiv" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="back2Search();">&nbsp;&nbsp;
</div>
</div>

</body>
</html>
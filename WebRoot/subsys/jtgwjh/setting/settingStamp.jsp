<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<html>
<head>
<title>设置</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript">
var paraName = "ZHUBAN_STAMP_USER";//主办盖章人员
function doInit() {
  //本部
  var url = "<%=contextPath%>/yh/subsys/jtgwjh/setting/act/YHJhSysParaAct/selectObj.act?paraName=" + paraName;
  var json = getJsonRs(url);
  if(json.rtState == '1'){ 
    alert(json.rtMsrg); 
    return ; 
  } 
  var prc = json.rtData;
  if (prc.seqId) {
    $("seqId").value = prc.seqId;
    $("paraName").value = prc.paraName;
    $("paraValue").value = prc.paraValue;
    bindDesc([{cntrlId:"paraValue",dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
}
//修改数据或者添加

function checkForm() {
  if ($("paraValue").value == "0" || $("paraValue").value == "ALL_DEPT") {
    alert("请重新选择人员！");
    return;
  }
  var pars = $('form1').serialize() ;
  var requestURL = "<%=contextPath%>/yh/subsys/jtgwjh/setting/act/YHJhSysParaAct/addAndUpdate.act";
  var json = getJsonRs(requestURL,pars);
  if (json.rtState == '1') { 
    alert(json.rtMsrg); 
    return ; 
  } else {
    alert("保存成功！");
    window.location.reload();
  }
}
</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit();">
<form id="form1"  name="form1">
<input type="hidden" name="BNUser" id="BNUser" value="">
<input type="hidden" name="dtoClass" value="yh.subsys.jtgwjh.setting.data.YHJhSysPara">
<input type="hidden" name="seqId" id="seqId" value="">
<input type="hidden" name="paraName" id="paraName" value="ZHUBAN_STAMP_USER">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/edit.gif" align="absmiddle"><span class="big3"> 主办盖章人员设置</span>
    </td>
  </tr>
</table>
<table class="TableBlock" width="600" align="center" >
   <tr>
    <td nowrap class="TableContent"> 请选择主办盖章人员：</td>
    <td nowrap class="TableData">
      <input type="hidden" name="paraValue"  id="paraValue" value="">
      <textarea cols="50" name="paraValueDesc" id="paraValueDesc" rows="5" class="BigStatic" wrap="yes" readonly></textarea>
      <a href="javascript:;" class="orgAdd" onClick="javascript:selectUser(['paraValue','paraValueDesc']);">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('paraValue').value='';$('paraValueDesc').value='';">清空</a>
    </td>
   </tr>
   <tr>
    <td nowrap  class="TableControl" colspan="2" align="center">
      <input type="button" value="确定" class="BigButton" onClick="checkForm();">&nbsp;&nbsp;
      <input type="button" value="清空" class="BigButton" onClick="$('paraValue').value='';$('paraValueDesc').value='';">&nbsp;&nbsp;
      <input type="button" class="BigButton" onClick="javascript:history.back()" value="返回">
    </td>
    </tr>
</table>
  </form>
</body>
</html>
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>固定资产管理</title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/orgselect.js" ></script>

<script>
function exportExcel() {
  var param = "CPRE_FLAG=" + document.getElementById("CPRE_FLAG").value + "&CPTL_NAME=" + encodeURIComponent(document.getElementById("CPTL_NAME").value)  + "&CPTL_SPEC=" + document.getElementById("CPTL_SPEC").value 
  + "&DEPT_ID=" + document.getElementById("DEPT_ID").value  + "&TYPE_ID=" + document.getElementById("TYPE_ID").value  + "&USE_FOR=" + document.getElementById("USE_FOR").value 
  + "&USE_STATE=" + document.getElementById("USE_STATE").value  + "&USE_USER=" + encodeURIComponent(document.getElementById("USE_USER").value) ;
  var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCpImportAct/outAsset.act?" + param;
  window.location.href = url;
}
function doInit() {
  var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlRecordAct/cpTypeId.act";
  var json = getJsonRs(url);
  var rtData = json.rtData;
  for(var i = 0; i < rtData.length; i++) {
    var opt = document.createElement("option");
      opt.value = rtData[i].typeId;
      opt.text = rtData[i].typeId;   
      var selectObj = $('TYPE_ID');
      selectObj.options.add(opt, selectObj.options ? selectObj.options.length : 0); 
  }
  dept();
  doSpec();
  //doUseState();
}
function doSpec() {
  var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpAssetTypeAct/assetTypeId.act";
  var json = getJsonRs(url);
  var rtData = json.rtData;
  for(var i = 0; i < rtData.length; i++) {
    var opt = document.createElement("option");
    opt.value = rtData[i].seqId;
    opt.text = rtData[i].typeName;   
    var selectObj = $('CPTL_SPEC');
    selectObj.options.add(opt, selectObj.options ? selectObj.options.length : 0); 
  }
}
function doUseState() {
  var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlRecordAct/useStateList.act";
  var json = getJsonRs(url);
  var rtData = json.rtData;
  for(var i = 0; i < rtData.length; i++) {
    var opt = document.createElement("option");
    opt.value = rtData[i].useState;
    opt.text = rtData[i].useState; 
    var selectObj = $('USE_STATE');
    selectObj.options.add(opt, selectObj.options ? selectObj.options.length : 0); 
  }
}
function dept(){
//得到部门
  var deptId = '1';
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/selectDeptToGift.act";
  var rtJson = getJsonRs(requestUrl);
  if (rtJson.rtState == "1") {
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  var selects = document.getElementById("DEPT_ID");
  for(var i = 0; i < prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value;
    option.innerHTML = prc.text; 
    selects.appendChild(option); 
  }
}
function checkForm (sheet,single) {
  var param1 = "sheet=" + sheet + "&single=" +single;
  var param = encodeURI(param1);
  var url = contextPath + "/subsys/oa/asset/manage/record/newSheet.jsp?" + param;
  window.location.href = url;
}

function checkForm2() {
  var pars = $('form1').serialize() ;
  var param = encodeURI(pars);
  var url = "<%=contextPath %>/subsys/oa/asset/manage/record/query.jsp?" + param;
  window.location = url;
}
</script>
</head>
<body class="bodycolor" topmargin="5" onload="doInit()">

<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_new.gif" ><span class="big3"> 增加固定资产使用记录</span><br>
    </td>
  </tr>
</table>
<div align="center">
  <input type="button"  value="增加领用单" class="BigButton" onclick="checkForm('领用单','1')" title="增加领用单">&nbsp;&nbsp;&nbsp;
  <input type="button"  value="增加返库单" class="BigButton" onclick="checkForm('返库单','2')" title="增加返库单">
</div>
<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="/images/dian1.gif" width="100%"></td>
 </tr>
</table>

<table border="0" width="90%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/asset.gif" width="18" height="17"><span class="big3"> 固定资产使用记录查询</span>
    </td>
  </tr>
</table>
<form  name="form1"  id="form1" method="post">
<table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData" width="100"> 记录类别：</td>
      <td class="TableData"> 
    <select name='CPRE_FLAG' id="CPRE_FLAG">
      <option value="1">领用单</option>
      <option value="2">返库单</option>
      </select>
    </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 资产名称：</td>
      <td class="TableData"> 
        <input type="text" name="CPTL_NAME" id="CPTL_NAME" size="30" maxlength="200" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 资产类别：</td>
      <td class="TableData"> 
      <select name="CPTL_SPEC" id="CPTL_SPEC">
        <option value=""></option>
        <option value="0">未指定类别</option>
      </select>
      </td>
    </tr>    
  <tr>
      <td nowrap class="TableData" width="100"> 使用部门：</td>
      <td class="TableData">
      <select name="DEPT_ID" id="DEPT_ID">
        <option value=""></option>
      </select>
      </td>
    </tr>
   
    <tr style="display:none">
      <td nowrap class="TableData" width="100"> 分类代码：</td>
      <td class="TableData"> 
      <select name="TYPE_ID" id="TYPE_ID">
        <option value=""></option>
      </select>      
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData" width="100"> 使用方向：</td>
      <td class="TableData"> 
      <select name="USE_FOR" id="USE_FOR">
        <option value=""></option>
        <option value="使用方向">使用方向</option>
        <option value="自用">自用</option>
      </select>      
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 使用状况：</td>
      <td class="TableData"> 
      <select name="USE_STATE" id="USE_STATE">
        <option value=""></option>
        <option value="1">未使用</option>
        <option value="2">不需用</option>
        <option value="3">在用</option>
        <option value="4">维修</option>
      </select>           
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 使用人：</td>
      <td class="TableData"> 
        <input type="text" name="USE_USERName" id="USE_USERName" size="15" maxlength="20"  class="BigInput" readonly value="">
      <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['USE_USER','USE_USERName']);">选择</a>
      <input type="hidden" name="USE_USER" id="USE_USER" value="">
      <a href="javascript:;" class="orgClear" onClick="$('USE_USER').value='';$('USE_USERName').value='';">清空</a>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" onClick="checkForm2()" value="查询" class="BigButton">&nbsp;&nbsp;
        <input type="button"  value="导出Excel" class="BigButton" onClick="exportExcel()">
      </td>
    </tr>
  </table>
</form>
</body>
</html>

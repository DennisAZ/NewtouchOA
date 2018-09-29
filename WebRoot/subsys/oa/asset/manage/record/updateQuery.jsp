<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="yh.subsys.oa.asset.data.YHCpCptlInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHCpCptlInfo cpcp = (YHCpCptlInfo) request.getAttribute("type");
String flage = "1";
String flage2 = "领用单";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改</title>
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
<%if(cpcp!=null){ %>
<script type="text/Javascript">
//表单提交验证
function checkForm() {
if (document.getElementById("cpreQty").value == "") {
    alert("数量不能为空！");
    document.getElementById("cpreQty").focus();
    document.getElementById("cpreQty").select();
    return false;
  }
  var num = /^[0-9]*$/;
  if (!$("cpreQty").value) { 
    alert("数量不能为空！");
    selectLast($("cpreQty"));
    document.getElementById("cpreQty").select();
    return false;
  }
  if (!num.exec(document.getElementById("cpreQty").value)) { 
    alert("数量只能为整数！");
    selectLast($("cpreQty"));
    document.getElementById("cpreQty").select();
    return false;
  }
  return true;
}

function checkForm2(){
if(checkForm()){
  var pars = $('form1').serialize() ;
  var param = pars;
  var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlRecordAct/editAsset.act";
  var json = getJsonRs(url,param);
  if (json.rtState == "1") {
    alert(json.rtMsrg);
  } else {
    alert("修改成功!");
     window.parent.opener.location.reload();
     parent.window.close();
  }
}
}
function doInit(){
//得到部门
  var deptId = '1';
  var requestUrl = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/selectDeptToGift.act";
  var rtJson = getJsonRs(requestUrl);
  if (rtJson.rtState == "1") {
    alert(rtJson.rtMsrg); 
    return ;
  }
  var prcs = rtJson.rtData;
  var selects = document.getElementById("deptId");
  for(var i = 0; i < prcs.length;i++){
    var prc = prcs[i];
    var option = document.createElement("option"); 
    option.value = prc.value;
    option.innerHTML = prc.text; 
    selects.appendChild(option);
  }
  var de = <%=cpcp.getUseDept()%>;
  selectValue(de);
}
function selectValue(val){
  if(document.getElementById("cptlSpec").value.trim() != ""){
    bindDesc([{cntrlId:"cptlSpec", dsDef:"CP_ASSET_TYPE,SEQ_ID,TYPE_NAME"}]);
  }
  var otypo = document.getElementById("deptId");
  for (var i = 0; i < otypo.options.length; i++) {
      if (otypo.options[i].value == val) {
        otypo.options[i].selected = true;
      }
  }
}
</script>
</head>
<body topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/asset.gif"><span class="big3">修改<%=flage2%></span><br>
    </td>
  </tr>
</table>
<br>

<table width="95%" border="0" cellspacing="0" cellpadding="0" height="3">
 <tr>
   <td background="<%=imgPath%>/dian1.gif" width="100%"></td>
 </tr>
</table>
 <form name="form1" id="form1" method="post">
 <input type="hidden" name="dtoClass" value="yh.subsys.oa.asset.data.YHCpCptlRecord">
 <input type="hidden" name="seqId" id="seqId" value="<%=cpcp.getSeqId()%>"/>
<table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData" width="100">记录类型：</td>
      <%if (cpcp.getUseFor().equals("1")) {
        flage = "1";
        flage2 = "领用单";
      %>
      <%}%>
      <%if (cpcp.getUseFor().equals("2")) {
        flage = "2";
        flage2 = "返库单";
      %><%}%>
      <td class="TableData">
        <input type="hidden" name="cpreFlag" id="cpreFlag" value="<%=flage%>">
        <%=flage2%>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100">资产名称：<label style="color: red">*</label></td>
      <td class="TableData"> 
        <input type="text" name="cptlName" id="cptlName" size="30" maxlength="200" class="BigInput" value="<%=cpcp.getCptlName().replace("\"","'") %>" readonly>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100">编号：</td>
      <td class="TableData">
       <input type="text" name="cptlNo" id="cptlNo" size="30" maxlength="200" class="BigInput" value="<%=cpcp.getCptlNo().replace("\"","'") %>" readonly>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100">资产类别： </td>
      <td class="TableData">
       <input type="hidden" name="cptlSpec" id="cptlSpec" size="30" maxlength="200" class="BigInput" value="<%=cpcp.getCptlSpec()%>" readonly>
       <input type="text" name="cptlSpecDesc" id="cptlSpecDesc" size="30" maxlength="200" class="BigInput" readonly>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100">部门：</td>
      <td class="TableData"> 
         <select name="deptId" id="deptId">
         </select>
         </td>
    </tr>
     <tr>
      <td nowrap class="TableData" width="100">数量： <label style="color: red">*</label></td>
      <td class="TableData"> 
         <input type="text" name="cpreQty" id="cpreQty" size="10" maxlength="23" class="BigInput" value="" /> 数字&nbsp;</td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 保管地点：</td>
      <td class="TableData"> <%if(!YHUtility.isNullorEmpty(cpcp.getSafekeeping())) {%>
      <input type="text" name="cprePlace" id="cprePlace"  size="30" maxlength="30" class="BigInput" value="<%=cpcp.getSafekeeping().replace("\"","'") %>">
      <%} else {%> 
       <input type="text" name="cprePlace" id="cprePlace"  size="30" maxlength="30" class="BigInput" value="">
      <%} %>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100">原因：</td>
      <td class="TableData"> 
         <textarea cols="35" name="cpreReason" id="cpreReason" rows="2" class="BigInput" wrap="yes"><%if(!YHUtility.isNullorEmpty(cpcp.getKeeper())){ %><%=cpcp.getKeeper()%><%} %></textarea>
      </td>
    </tr>

    <tr>
      <td nowrap class="TableData" width="100">备注： </td>
      <td class="TableData"> 
        <textarea cols="35" name="cpreMemo" id="cpreMemo" rows="2" class="BigInput" wrap="yes"><%if(!YHUtility.isNullorEmpty(cpcp.getRemark())){ %><%=cpcp.getRemark()%><%} %></textarea>
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td colspan="3" nowrap>
        <input type="button" onClick="checkForm2()" value="保存" class="BigButton">&nbsp;
        <input type="button" value="关闭" class="BigButton" onClick="javascript:parent.opener.location.reload();parent.window.close()">
      </td>
    </tr>
  </table>
</form>
<%} else { %>
<table class="MessageBox" align="center" width="340">
  <tr>
    <td class='msg info'>
    <div class='content' style='font-size: 12pt'>该固定资产已删除</div>
    </td>
  </tr>
</table>
<div align="center"> <input type="button" value="关闭" class="BigButton" onclick="javascript:parent.opener.location.reload();parent.window.close()">&nbsp;</div>
<%} %>
</body>
</html>
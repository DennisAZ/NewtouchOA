<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="yh.subsys.oa.asset.data.YHCpCptlInfo"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
YHCpCptlInfo cpcp = (YHCpCptlInfo) request.getAttribute("cpcp");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改固定资产</title>
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
  if (document.getElementById("cptlNo").value.trim() == "") {
    alert("编号不能为空!");
    document.getElementById("cptlNo").focus();
    document.getElementById("cptlNo").select();
    return false;
  }
  if (document.getElementById("cptlName").value.trim() == "") {
    alert("资产名称不能为空!");
    document.getElementById("cptlName").focus();
    document.getElementById("cptlName").select();
    return false;
  }
  if (document.getElementById("cptlSpec").value == "") {
    alert("资产类别不能为空!");
    document.getElementById("cptlSpec").focus();
    document.getElementById("cptlSpec").select();
    return false;
  }
  if (document.getElementById("listDate").value == "") {
    alert("单据日期不能为空!");
    document.getElementById("listDate").focus();
    document.getElementById("listDate").select();
    return false;
  }
  if(!isValidDateStr($("listDate").value)  && $("listDate").value != ""){
    alert("单据日期格式不对，应形如 1999-01-01"); 
    $("listDate").focus(); 
    $("listDate").select(); 
    return false; 
  }
  var re1 = /\,/gi;
  var moneyTemp = document.getElementById("cptlVal2").value.replace(re1,'');
  if (document.getElementById("cptlVal2").value != "") {
  if (isNumber(moneyTemp)) {
  } else {
    alert("非法数字");
    document.getElementById("cptlVal2").focus();
    document.getElementById("cptlVal2").select();
    return false;
  }
}
return true;
}
function checkForm2(){
  if (checkForm()) {
    var param = $('form1').serialize();
    var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlInfoAct/editAsset.act";
    var json = getJsonRs(url,param);
    if (json.rtState == "1") {
      alert(json.rtMsrg);
    } else {
      alert("修改成功!");
      window.location.reload();
      //parent.window.close();
    }
  }
}
function cptlSpec(){
  var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpAssetTypeAct/assetTypeId.act";
  var json = getJsonRs(url);
  var rtData = json.rtData;
  for(var i = 0; i < rtData.length; i++) {
    var opt = document.createElement("option");
    opt.value = rtData[i].seqId;
    opt.text = rtData[i].typeName;   
    var selectObj = $('cptlSpec');
    selectObj.options.add(opt, selectObj.options ? selectObj.options.length : 0); 
  }
  var typeName = <%=cpcp.getCptlSpec()%>;
  selectValue(typeName);
}
function selectValue(val){
  var otypo = document.getElementById("cptlSpec");
  for (var i = 0; i < otypo.options.length; i++) {
      if (otypo.options[i].value == val) {
        otypo.options[i].selected = true;
      }
  }
}

//千分符

function checkMoney(moneyStr){
  var re1 = /\,/gi;
  document.getElementById("cptlVal").value = $(moneyStr).value.replace(re1,'');
  var money  =  $(moneyStr).value;
  money = money.replace(re1,"");
  if(!isNumber(money)){
    alert("资产值应为数字!");
    $(moneyStr).focus();
    $(moneyStr).select();
     return ;
   }
  money = insertKiloSplit(money,2);
 $(moneyStr).value = money;
}

function doInit() {
  showCalendar('listDate',false,'beginDateImg');//时间
  cptlSpec();
  if(document.getElementById("keeper").value.trim() != ""){
    bindDesc([{cntrlId:"keeper", dsDef:"PERSON,SEQ_ID,USER_NAME"}]);
  }
}
</script>
</head>
<body topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/asset.gif"><span class="big3"> 修改固定资产</span><br>
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
 <input type="hidden" name="dtoClass" value="yh.subsys.oa.asset.data.YHCpCptlInfo">
  <input type="hidden" name="seqId" id="seqId" value="<%=cpcp.getSeqId()%>"/>
 <input type="hidden" name="createDate" id="createDate" value="<%=sf.format(new Date()) %>"/>
 <%if(!YHUtility.isNullorEmpty(cpcp.getUseUser())){%>
 <input type="hidden" name="useUser" id="useUser" value="<%=cpcp.getUseUser()%>"/>
 <%}%>
  <%if(!YHUtility.isNullorEmpty(cpcp.getUseUser())){%>
  <input type="hidden" name="useDept" id="useDept" value="<%=cpcp.getUseDept()%>"/>
 <%}%>
  <%if(!YHUtility.isNullorEmpty(cpcp.getUseState())){%>
 <input type="hidden" name="useState" id="useState" value="<%=cpcp.getUseState()%>"/>
 <%}%>
 <input type="hidden" name="noDeal" id="noDeal" value="<%=cpcp.getNoDeal()%>"/>
 <input type="hidden" name="inFinance" id="inFinance" value="<%=cpcp.getInFinance()%>"/>
 <input type="hidden" name="useFor" id="useFor" value=""/>
 <input type="hidden" name="afterIndate" id="afterIndate" value=""/>
 <input type="hidden" name="getDate" id="getDate" value=""/>
  <input type="hidden" name="typeId" id="typeId" value=""/>
<table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData" width="100"> 资产编号：<label style="color: red">*</label></td>
      <td class="TableData">
        <input type="text" name="cptlNo" id="cptlNo" size="20" maxlength="100" class="BigInput" value="<%=cpcp.getCptlNo().replace("\"","'") %>">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100">资产名称：<label style="color: red">*</label></td>
      <td class="TableData"> 
        <input type="text" name="cptlName" id="cptlName" size="30" maxlength="200" class="BigInput" value="<%=cpcp.getCptlName().replace("\"","'") %>">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 资产类别：<label style="color: red">*</label></td>
      <td class="TableData"> 
      <select name="cptlSpec" id="cptlSpec">
        <option value="">--请选择类别--</option>
      </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 资产值(单价)：</td>
      <td class="TableData"> 
       <input type="hidden" name="cptlVal" id="cptlVal" size="20" maxlength="13" class="BigInput" value="<%=cpcp.getCptlVal()%>"> 
         <input type="text" name="cptlVal2" id="cptlVal2" onblur="checkMoney('cptlVal2')" size="20" maxlength="13" class="BigInput" value="<%=YHUtility.getFormatedStr(cpcp.getCptlVal(),2)%>"> &nbsp;</td>
    </tr>
     <tr>
      <td nowrap class="TableData" width="100"> 资产数量：</td>
      <td class="TableData"> 
         <input type="text" name="cptlQty" id="cptlQty" size="10" maxlength="23" class="BigInput" value="<%=cpcp.getCptlQty()%>" > &nbsp;</td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 单据日期：<label style="color: red">*</label></td>
      <td class="TableData">
      <input type="text" id="listDate" name="listDate" size="10" maxlength="11" class="BigInput" value="<%=cpcp.getListDate().toString().substring(0,10)%>"> 
      <img id="beginDateImg" name="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">
</td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 专管员：</td>
      <td class="TableData"> 
        <input type="hidden" id="keeper" name="keeper" size="15" maxlength="20" class="BigInput" value="<%=cpcp.getKeeper()%>">
                <input type="text" name="keeperDesc" id="keeperDesc" size="20" class="BigInput" maxlength="20"  value="" readonly>
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['keeper','keeperDesc']);">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('keeperDesc').value='';$('keeper').value='';">清空</a>
      </td>
    </tr>
  <tr>
      <td nowrap class="TableData" width="100"> 保管地点：</td>
      <td class="TableData"> <%if(!YHUtility.isNullorEmpty(cpcp.getSafekeeping())) {%>
      <input type="text" name="safekeeping" id="safekeeping"  size="30" maxlength="50" class="BigInput" value="<%=cpcp.getSafekeeping().replace("\"","'") %>">
      <%} else {%> 
       <input type="text" name="safekeeping" id="safekeeping"  size="30" maxlength="50" class="BigInput" value="">
      <%} %>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 备注：</td>
      <td class="TableData"> 
        <textarea cols="35" name="remark" id="remark" rows="2" class="BigInput" wrap="yes"><%if(!YHUtility.isNullorEmpty(cpcp.getRemark())){ %><%=cpcp.getRemark()%><%} %></textarea>
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
<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
YHPerson person = (YHPerson)request.getSession().getAttribute(YHConst.LOGIN_USER);
SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加固定资产入库</title>
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
<script type="text/Javascript">
//表单提交验证
function checkForm() {
  if (document.getElementById("cptlNo").value.trim() == "") {
    alert("资产编号不能为空!");
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
  if (document.getElementById("cptlQty").value == "") {
    alert("资产数量不能为空！");
    document.getElementById("cptlQty").focus();
    document.getElementById("cptlQty").select();
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
  var num = /^[0-9]*$/;
  if (!$("cptlQty").value) { 
    alert("资产数量不能为空！");
    selectLast($("cptlQty"));
    document.getElementById("cptlQty").select();
    return false;
  }
  if (!num.exec(document.getElementById("cptlQty").value)) { 
    alert("资产数量只能为整数！");
    selectLast($("cptlQty"));
    document.getElementById("cptlQty").select();
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
    var pars = $('form1').serialize() ;
    var param = pars;
    var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlInfoAct/add.act";
    var json = getJsonRs(url,param);
    if (json.rtState == "1") {
      alert(json.rtMsrg);
    } else {
      alert("添加成功!");
      window.location = contextPath + "/subsys/oa/asset/manage/manage.jsp";
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
     $('msrgMs').show();
     return ;
   } else {
     $('msrgMs').hide();
   }
  money = insertKiloSplit(money,2);
 $(moneyStr).value = money;
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
}
function doInit() {
  showCalendar('listDate',false,'beginDateImg');//时间
  cptlSpec();
}
</script>
</head>
<body topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/asset.gif"><span class="big3"> 增加固定资产入库</span><br>
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
 <input type="hidden" name="typeId" id="typeId" value=""/>
 <input type="hidden" name="createDate" id="createDate" value="<%=sf.format(new Date()) %>"/>
 <input type="hidden" name="useUser" id="useUser" value=""/>
 <input type="hidden" name="useDept" id="useDept" value=""/>
 <input type="hidden" name="noDeal" id="noDeal" value=""/>
 <input type="hidden" name="inFinance" id="inFinance" value=""/>
 <input type="hidden" name="useState" id="useState" value="1"/>
 <input type="hidden" name="useFor" id="useFor" value=""/>
 <input type="hidden" name="afterIndate" id="afterIndate" value=""/>
 <input type="hidden" name="getDate" id="getDate" value=""/>
<table class="TableBlock" width="500" align="center">
    <tr>
      <td nowrap class="TableData" width="100"> 资产编号：<label style="color: red">*</label></td>
      <td class="TableData"> 
        <input type="text" name="cptlNo" id="cptlNo" size="20" maxlength="100" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100">资产名称：<label style="color: red">*</label></td>
      <td class="TableData"> 
        <input type="text" name="cptlName" id="cptlName" size="30" maxlength="200" class="BigInput" value="">
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
      <input type="hidden" name="cptlVal" id="cptlVal" size="20" maxlength="13" class="BigInput" value="0"> 
      <input type="text" name="cptlVal2" id="cptlVal2" onblur="checkMoney('cptlVal2')" size="20" maxlength="13" class="BigInput" value="">&nbsp;</td>
      <span id="msrgMs" style="color:red;display:none">*资产值应为数字!</span>
    </tr>
     <tr>
      <td nowrap class="TableData" width="100"> 资产数量：<label style="color: red">*</label></td>
      <td class="TableData"> 
         <input type="text" name="cptlQty" id="cptlQty" size="10" maxlength="23" class="BigInput" value="1" /> 数字&nbsp;</td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 单据日期：<label style="color: red">*</label></td>
      <td class="TableData">
      <input type="text" id="listDate" name="listDate" size="10" maxlength="11" class="BigInput" value=""> 
      <img id="beginDateImg" name="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor: hand">
</td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 专管员：</td>
      <td class="TableData"> 
        <input type="hidden" id="keeper" name="keeper" size="15" maxlength="20" class="BigInput" value="">
                <input type="text" name="keeperName" id="keeperName" size="20" class="BigInput" maxlength="20"  value="" readonly>
       <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['keeper','keeperName']);">添加</a>
         <a href="javascript:;" class="orgClear" onClick="$('keeperName').value='';$('keeper').value='';">清空</a>
      </td>
    </tr>
  <tr>
      <td nowrap class="TableData" width="100"> 保管地点：</td>
      <td class="TableData"> 
        <input type="text" name="safekeeping" id="safekeeping"  size="30" maxlength="50" class="BigInput" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width="100"> 备注：</td>
      <td class="TableData"> 
        <textarea cols="35" name="remark" id="remark" rows="2" class="BigInput" wrap="yes"></textarea>
      </td>
    </tr>   
    <tr align="center" class="TableControl">
      <td colspan="2" nowrap>
        <input type="button" onClick="checkForm2()" value="确认" class="BigButton">&nbsp;
        <input type="button" value="返回" class="BigButton" onClick="history.back();">
      </td>
    </tr>
  </table>
</form>
</body>
</html>

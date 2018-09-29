<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
String seqId = request.getParameter("seqId");
if(seqId == null) {
  seqId = "";
}

String pageNo = request.getParameter("pageNo");
if(pageNo == null) {
  pageNo = "";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加或修改标记</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
var seqId = "<%=seqId%>";
var pageNo = "<%=pageNo%>";

function getMaxNum(flagSort) {
  var url = "<%=contextPath%>/yh/core/funcs/specialflag/act/YHSpecialFlagAct/getMaxFlagCode.act";
  var rtJson = getJsonRs(url, flagSort); 

  if (rtJson.rtState == "0") {
    document.getElementById("code").innerHTML = rtJson.rtData[0].code;
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function doInit() {
  var mgr = new SelectMgr();
  mgr.addSelect({cntrlId: "flagSort"
    , tableName: "CODE_ITEM"
      , codeField: "CLASS_CODE"
        , nameField: "CLASS_DESC"
          ,isMustFill: "1"
            , filterField: "CLASS_NO"
              , filterValue: "S03"
                , order: "CLASS_CODE"
                  , reloadBy: ""
                    , actionUrl: ""});
  mgr.loadData();
  mgr.bindData2Cntrl();

  var sort = document.getElementById("flagSort").value;
  var url = "<%=contextPath%>/yh/core/funcs/specialflag/act/YHSpecialFlagAct/getSpecialFlag.act"; 
  if(seqId){
    var rtJson = getJsonRs(url, "seqId=" + seqId); 
    if (rtJson.rtState == "0") {
      bindJson2Cntrl(rtJson.rtData[0]);
      
      document.getElementById("seqId").value = seqId;
      document.getElementById("flagCodeOld").value = rtJson.rtData[0].flagCode;
      
      var param = "sort=" + rtJson.rtData[0].flagSort;
      getMaxNum(param);
    }else {
      alert(rtJson.rtMsrg); 
    }   
  }else {
    var param = "sort=" + sort;
    getMaxNum(param);
  }
}

function changeflagsort() {  
  var param = "sort=" + document.getElementById("flagSort").value;
  getMaxNum(param);
}

function check() {
  var cntrl = document.getElementById("flagCode")
  if(!cntrl.value) {
	alert("标记编号不能为空！");
	cntrl.focus();
	return false;
  }
  
  if(!isNumber(cntrl.value)){
	alert("标记编号必须填入数字！");
	cntrl.focus();
  	return false;
  }
 

  cntrl = document.getElementById("flagDesc");
  if(!cntrl.value) {
  	alert("标记描述不能为空！");
  	cntrl.focus();
  	return false;
  }
  return true;
}

function commitItem() {
  if(!check()){
    return;
  }

  var flagCode = document.getElementById("flagCode").value;
  if(seqId) {
    url = "<%=contextPath%>/yh/core/funcs/specialflag/act/YHSpecialFlagAct/updateSpecialFlag.act";
    var rtJson = getJsonRs(url, mergeQueryString($("flagInfoForm")));
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg);
      
      if(rtJson.rtMsrg == "成功编辑标记") {
        alert(document.getElementById("flagCode").value);
        document.getElementById("flagCodeOld").value = document.getElementById("flagCode").value;
      }
      var param = "sort=" + document.getElementById("flagSort").value;
      getMaxNum(param);
    }else {
      alert(rtJson.rtMsrg); 
    }     
  }else {
    var url = "<%=contextPath%>/yh/core/funcs/specialflag/act/YHSpecialFlagAct/addSpecialFlag.act";
    var rtJson = getJsonRs(url, mergeQueryString($("flagInfoForm")));
    
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg);  
      var no = rtJson.rtData[0].num;  
     
      if(no >= 1) {
        document.getElementById("flagCode").focus();
        return;
      }
      
      document.getElementById("flagCode").value = "";
      document.getElementById("flagDesc").value = "";

      document.getElementById("flagCodeOld").value = flagCode;

      document.getElementById("flagCode").focus();

      var param = "sort=" + document.getElementById("flagSort").value;
      getMaxNum(param);
    }else {
      alert(rtJson.rtMsrg); 
    }
  }
}

function goBack() {
  window.location.href = "<%=contextPath %>/core/funcs/specialflag/specialflaglist.jsp?pageNo=" + pageNo;
}

</script>
</head>
<body onload="doInit()">
<form name="flagInfoForm" id="flagInfoForm" method="post">
  <%
    if(seqId.equals("")) {
  %>   
    <h2><img src="<%=contextPath %>/core/styles/imgs/green_plus.gif"></img>添加标记</h2> 
  <%
    }else {
  %>    
    <h2><img src="<%=contextPath %>/core/styles/imgs/edit.gif"></img>修改标记</h2>
  <%
    }
  %>
  
  <input type="hidden" name="seqId" id="seqId" value="" />
  <input type="hidden" name="flagCodeOld" id="flagCodeOld" value="" />
  <input type="hidden" id="dtoClass" name="dtoClass" value="yh.core.funcs.specialflag.data.YHSpecialFlag"/>
  <table cellscpacing="1" cellpadding="3" width="450">
    <tr class="TableLine1">
      <td>标记类别:</td>
      <td>
        <select name="flagSort" id="flagSort" class="BigSelect" value="" onchange="changeflagsort()"></select>
      </td>
    </tr>
    
  <tr class="TableLine2">
    <td>标记编号:</td>
    <td>
      <font style="color:red">*</font>
      <input type="text" id="flagCode" name="flagCode" class="SmallInput" maxlength="5" value=""
      onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"
      />
                当前类别的最大标记编号是<span style="color:red" id="code"></span>
      ,建议填<span style="color:red">5</span>位编码          
    </td>
  </tr>
  
  <tr class="TableLine1">
    <td>标记描述:</td>
    <td>
      <font style="color:red">*</font><input type="text" id="flagDesc" name="flagDesc" class="SmallInput" value=""/>
  	</td>
  </tr>
  
<%
  if(seqId.equals("")) {
%>
  <tr class="TableLine1">
    <td colspan="2" align="center">
      <input type="button" value="提交" class="SmallButton" onclick="commitItem()">
    </td>
  </tr>
<%
  }else {
%>
  <tr class="TableLine1">
    <td colspan="2" align="center">
      <input type="button" value="提交" class="SmallButton" onclick="commitItem()">
      <input type="button" value="返回" class="SmallButton" onclick="goBack()">
    </td>
  </tr>
<%
  }
%>
  </table>
</form>
</body>
</html>
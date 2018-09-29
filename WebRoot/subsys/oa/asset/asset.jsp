<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>我的固定资产</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/select.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script> 
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript">
var pageMgr = null;
var cfgs = null;
function doInit(){
  var timestamps = new Date().valueOf();
  var url = "<%=contextPath%>/yh/subsys/oa/asset/act/YHCpCptlInfoAct/assetSelect.act";
   cfgs = {
    dataAction: url,
    container: "giftList",
    paramFunc: getParam,
    colums: [
       {type:"text", name:"seqId", text:"ID",align:"center", width:"5%"},
       {type:"text", name:"cptlNo", text:"固定资产编号",align:"center", width:"12%"},
       {type:"text", name:"cptlSpec", text:"资产类别", width: "10%",align:"center"},
       {type:"text", name:"cptlName", text:"固定资产名称", width: "8%",align:"center"},
       {type:"text", name:"typeId", text:"分类代码", width: "10%",align:"center"},
       {type:"text", name:"useState", text:"使用状况", width: "5%",align:"center",render:toUseState},
       {type:"text", name:"useFor", text:"使用方向 ", width: "5%",align:"center"},
       {type:"text", name:"caozuo", text:"操作",align:"center", width: "7%",render:toCao}]
  };
  pageMgr = new YHJsPage(cfgs);
  pageMgr.show();
  var total = pageMgr.pageInfo.totalRecord;//是否有数据
  if(total <= 0){
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件固定资产</div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').update(table); 
  }
  selectCptlSpec();
}
//返回操作 
function toCao(cellData, recordIndex,columInde){
  var seqId =  this.getCellData(recordIndex,"seqId");
  var cptlNo =  this.getCellData(recordIndex,"cptlNo");
  var cptlSpec =  this.getCellData(recordIndex,"cptlSpec");
  var cptlName =  this.getCellData(recordIndex,"cptlName");
  var re1 = /\'/gi;
  var re2 = /\"/gi;
  cptlNo = cptlNo.replace(re1,"&lsquo;");
  cptlNo = cptlNo.replace(re2,"&bdquo;");
  
  cptlSpec = cptlSpec.replace(re1,"&lsquo;");
  cptlSpec = cptlSpec.replace(re2,"&bdquo;");
  
  cptlName = cptlSpec.replace(re1,"&lsquo;");
  cptlName = cptlSpec.replace(re2,"&bdquo;");
  
  return "<input type='button' class='BigButton' value='选择数据' onclick='" 
  + "getParam2(" + seqId + ",\"" + cptlNo + "\",\"" + cptlSpec + "\",\"" + cptlName + "\");'>";
}

//返回状态
function toUseState(cellData, recordIndex,columInde){
  var useState =  this.getCellData(recordIndex,"useState");
  if (useState == "1") {
    return "未使用";
  }
  if (useState == "2") {
    return "不需用";
  }
  if (useState == "3") {
    return "在用";
  }else {
    return "";
  }
}
function getParam(){
  queryParam = $("form1").serialize();
  return queryParam;
}
//查询
function queryGift(){
  var num = /^[0-9]*$/;
  if (!num.exec(document.getElementById("SEQ_ID").value)) { 
    alert("ID只能为数字！");
    selectLast($("SEQ_ID"));
    document.getElementById("SEQ_ID").select();
    return;
  }
  if(!pageMgr){
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
  }else{
    pageMgr.search();
  }
  var total = pageMgr.pageInfo.totalRecord;
  if(total){
    $('giftList').style.display="";
    $('returnNull').style.display="none"; 
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>无符合条件固定资产 </div></td></tr>"
        );
    $('giftList').style.display="none";
    $('returnNull').style.display=""; 
    $('returnNull').update(table);  
  }
}

//子窗口 dialogArguments.setBindInfo(param);
function getParam2(cptlId,cptlNo,cptlSpec,cptlName) {
  var checkValue;
  var param1 = "cptlId=" + cptlId + "&cptlNo=" + cptlNo + "&cptlSpec=" + cptlSpec + "&cptlName=" + cptlName;
  var param = encodeURI(param1);
  
  window.opener.document.getElementById("cptlId").value = cptlId;
  window.opener.document.getElementById("CPTL_NO").value = cptlNo;
  window.opener.document.getElementById("CPTL_SPECDesc").value = cptlSpec;
  window.opener.document.getElementById("CPTL_NAME").value = cptlName;
  var otypo = window.opener.document.getElementById("CPTL_NAME");
  for (var i = 0;i < otypo.options.length; i++) {
    if (otypo.options[i].value != "") {
      checkValue = otypo.options[i].value.split(",^,")[0];
    }
    if (checkValue == cptlId) {
      otypo.options[i].selected = true;
    }
  }
  window.close();
}
function selectCptlSpec() {
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
</script>
</head>
<body onLoad="doInit();" topmargin="5">
<form method="post" name="form1" id="form1">
<div align='center'>
<fieldset style="width:80%;padding-bottom:5px;">
  <legend class="small" align=left><b>快速查询</b></legend>
<table cellspacing="1" class="small" align="left" cellpadding="3" width="100%">
  <tr>
</tr><tr> <td nowrap align="center">ID</td>
  <td nowrap class="TableData">
  
   <input type="text" name="SEQ_ID"  id="SEQ_ID" class="SmallInput" size="15" maxlength="10" value="">
   
  </td>
  <td nowrap align="center">固定资产编号</td>
  <td nowrap class="TableData">
  
  <input type="text" name="CPTL_NO" id="CPTL_NO" class="SmallInput" size="15" maxlength="100" value="">
  
    </td>
  <td nowrap align="center">固定资产名称</td>
  <td nowrap class="TableData">
  
        <input type="text" name="CPTL_NAME" id="CPTL_NAME" class="SmallInput" size="15" maxlength="100" value="">
        
    </td>
</tr>
<tr> <td nowrap align="center" >资产类别</td>
  <td nowrap class="TableData">
   <select name="CPTL_SPEC" id="CPTL_SPEC">
        <option value="">所有类别</option>
        <option value="0">未指定类别</option>
      </select>
    </td>
  <td nowrap align="center" style="display:none">分类代码</td>
  <td nowrap class="TableData" style="display:none">
   
         <input type="text" name="TYPE_ID" id="TYPE_ID" class="SmallInput" size="15" maxlength="100" value="">
        
    </td>
  <td nowrap align="center">使用状况</td>
  <td nowrap class="TableData">
  
     <input type="text" name="USE_STATE" id="USE_STATE" class="SmallInput" size="15" maxlength="100" value="">
    
    </td>
     <td nowrap align="center">使用方向</td>
  <td nowrap class="TableData">
   
         <input type="text" name="USE_FOR" id="USE_FOR" class="SmallInput" size="15" maxlength="100" value="">
        
    </td>
</tr>
   <tr>
      <td nowrap class="TableData" colspan="6" align="right">
        <input value="查询" type="button" onClick="queryGift()" class="SmallButton" title="查询" name="button">
     </td>
  </tr>
</table>
</fieldset>
</div>
<input type="hidden" value="<%=request.getParameter("cpreFlag")%>" name="cpreFlag" id="cpreFlag">
</form>
<br>
<div id="giftList" style="padding-left:3px;"></div>
<div id="returnNull">
</div>
</body>
</html>
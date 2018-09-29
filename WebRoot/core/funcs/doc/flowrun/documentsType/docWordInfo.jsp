<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<html>
<head>
<%
String flag = request.getParameter("flag");
%>
<title>选择文件字</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<style type="text/css">
.item {
  height: 28px;
  line-height: 28px;
  background-color: #fff;
  font-size: 12px;
  cursor: pointer;
  text-align: center;
  padding-left: 25px;
}
.item.hover {
  background-color: #edf6db;
}

.item.select {
  background-color: #edf6db;
}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<style>
.menulines{}
</style>

<script>

var menu_enter="";
var flagClose = '<%= flag%>';

</SCRIPT>
<script Language="JavaScript">
var parent_window = parent.dialogArguments;
var selectedColor = "rgb(0, 51, 255)";
var moveColor = "#ccc";
/**
 * 查看后缀ext是否在exts中


 */
function findIsIn(exts , ext){
  for(var i = 0 ;i < exts.length ; i++){
    var tmp = exts[i];
    if(tmp == ext){
      return true;
    }
  }
  return false;
}
function findStr(str , s) {
  if (!str) {
    return false;
  }
  var ss = str.split(",");
  return findIsIn(ss, s);
}

/**
 * 获取代码名称
 * @param seqId
 * @return
 */
function selectCodeById(seqId) {
	var str = "";
	var requestURLStr = contextPath + "<%=moduleSrcPath %>/act/YHDocumentsTypeAct/getDocumentsTypeById.act?seqId=" + seqId;
	var rtJson = getJsonRs(requestURLStr);
	//alert(rsText);
	if (rtJson.rtState == "1") {
		alert(rtJson.rtMsrg);
		return;
	}
	var prcs = rtJson.rtData;
	if(prcs.documentsName){
		str = prcs.documentsName;
	}
	return str;
}

/**
* 消息提示
* @param msrg
* @param cntrlId 绑定消息的控件


* @param type  消息类型[info|error||warning|forbidden|stop|blank] 默认为info
* @return
*/
function WarningMsrg(msrg, cntrlId,type ) {
 var msrgDom = "<table class=\"MessageBox\" align=\"center\" width=\"280\">";
 if(!type){
   type = "info";
 }
 msrgDom += " <tr>  <td class=\"msg " + type + "\">"
 msrgDom +=  "<div class=\"content\" style=\"font-size:12pt\">" + msrg + "</div>"
     + " </td> </tr> </table>";
 $(cntrlId).innerHTML = msrgDom;
}
/**
* 隐藏显示控件
* 
* @param cntrlId
* @return
*/
function showCntrl(cntrlId) {
 if ($(cntrlId)) {
   if ($(cntrlId).style.display) {
     $(cntrlId).style.display = '';
   } else {
     $(cntrlId).style.display = 'none';
   }
 }
}
function doinit(){
  var parent_window = parent.dialogArguments;
  doInit2();
  //getTrainingAjaxFunc();
}
var pageMgr = null;
function getParam(){
  queryParam = $("from1").serialize();
  return queryParam;
}
var obj = null;
var objDesc = null;
function doInit2(){
  obj =  parent_window.form1.documentsFont;
  objDesc = parent_window.form1.documentsFontDesc;
  var requestURI = contextPath + "<%=moduleSrcPath %>/act/YHDocumentsTypeAct";
  var url = requestURI + "/getDocumentsTypeListSelect2.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      paramFunc: getParam,
      colums: [
         {type:"hidden", name:"dwId"},
         {type:"data", name:"dwName",  width: '10%', text:"文件字" , render:funcRender}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
    }else{
      WarningMsrg('无符合条件的记录', 'msrg');
    }
}

function funcRender(cellData, recordIndex, columIndex){
  var dwId = this.getCellData(recordIndex,"dwId");
  var dwName = this.getCellData(recordIndex,"dwName");
  var val = obj.value;
  if (!val) {
    val = "";
  }
  var flag = "0";
  var backgroudColor = "";
  var className = "item";
  if (findStr(val , dwId)) {
    flag = "1";
    className = "item select";
  }
  var str = "<div isChecked=\""+ flag +"\" onclick=\"select1(this)\" onmouseover=\"divOnMouseover(this )\" onmouseout=\"divOnMouseout(this )\"  class=\""+ className +"\" id=\"dw_"+dwId+"\" name=\"dw_"+dwId+"\" align=\"center\" style=\"cursor:pointer\">" + dwName + "</div>";
  return str;  
}
function select1(div) {
  var isChecked = div.getAttribute("isChecked");
  var id = div.id;
  id = id.substr(3);
  if (isChecked == "1") {
    selectWord(id ,  div.innerHTML.trim());
    div.className = "item";
    div.setAttribute("isChecked" , "0");
  } else {
    disSelectWord(id , div.innerHTML.trim());
    div.isSelected = true;
    div.setAttribute("isChecked" , "1");
  }
}
function selectWord(id , userName) {
  var userStr = obj.value;
  var userDescStr = "";
  userDescStr = objDesc.value;
  if (userDescStr && userDescStr.trim) {
    userDescStr = userDescStr.trim();
  }
  objDesc.value = getOutofStr(userDescStr , userName);
  obj.value = getOutofStr(userStr , id);
}
function disSelectWord(id , userName) {
  var userStr = obj.value;
  var userDescStr = "";
  
  userDescStr = objDesc.value;
  if (userDescStr && userDescStr.trim) {
    userDescStr = userDescStr.trim();
  }
  if (obj.value.length > 0) {
    obj.value += "," + id;
  }else {
    obj.value = id ;
  }
  if(objDesc.value){
    objDesc.value += "," + userName;
  }else{
    objDesc.value = userName;
  }
}
function getOutofStr(str, s){
  var aStr = str.split(',');
  var strTmp = "";
  var j = 0 ;//控制重名
  for(var i = 0 ;i < aStr.length ; i++){
    if(aStr[i] && (aStr[i] != s || j != 0)){
        strTmp += aStr[i] + ',';
    }else{
      if(aStr[i] == s){
        j = 1;
      }  
    }
  }
  if (strTmp && strTmp.lastIndexOf(",") == strTmp.length - 1) {
    strTmp = strTmp.substring(0, strTmp.length - 1);
  }
  return strTmp;
}
function divOnMouseout(div) {
  var isChecked = div.getAttribute("isChecked");
  if (isChecked != "1") {
    div.className = "item";
  } else {
    div.className = "item select";
  }
}
function divOnMouseover(div) {
  var isChecked = div.getAttribute("isChecked");
  if (isChecked != "1") {
    div.className = "item hover";
  } else {
    div.className = "item select";
  }
}
//trainingStr += "<div class=\"menulines\" id=\"dw_"+dwId+"\" name=\"dw_"+dwId+"\" align=\"center\" style=\"cursor:pointer\" onMouseover=\"borderize_on("+dwId+")\" onMouseout=\"borderize_off("+dwId+")\" onclick=javascript:addPlanName('" + dwId.escapeHTML().replace(/\\/g,"\\\\").replace(/'/g,"\\\'").replace(/\"/g,"\\\"") + "','" + dwName.escapeHTML().replace(/\\/g,"\\\\").replace(/'/g,"\\\'").replace(/\"/g,"\\\"") + "') ></div>";
function getTrainingAjaxFunc(condition1){
  $('condition').value = condition1;
  pageMgr.search();
}
</script>
</head >
<body onload="doinit();" class="bodycolor"  topmargin="5">
<form id="from1">
 <input type="hidden" name="condition" id="condition">
</form>
<div id="listContainer" style="display:none;">
</div>
<div id="msrg"></div>
</body>
</html>

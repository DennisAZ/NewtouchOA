<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
String flowId = request.getParameter("flowId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>步骤列表</title>
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script language="JavaScript" src="../openWin.js"></script>
<script type="text/javascript">
var prcsJson;
var flowId = '<%=flowId%>';
var requestURL;
function doInit(){
  requestURL = contextPath + "/yh/core/funcs/workflow/act/YHFlowProcessAct";
  var url = requestURL + "/getProcessList.act";
  var json = getJsonRs(url , 'flowId=' + flowId);
  if(json.rtState == '1'){
    alert(json.rtMsrg);
    return ;
  }
  prcsJson = json.rtData.prcsList;
  $('flowNameSpan').innerHTML  = "&nbsp;管理流程步骤 ("+ json.rtData.flowName +")";
  var isSetPriv = json.rtData.isSetPriv;
  var firstPrcsSeqId;
  if(prcsJson.length > 0){
		var table = new Element('table',{ "width":"100%" , 'class':'TableList'}).update("<tbody id='tbody'><tr class='TableHeader'   style='font-size:10pt'><td  align=center>序号</td><td  align=center>名称</td><td  align=center>下一步骤</td><td  align=center>编辑该步骤的各项属性</td><td  align=center>操作</td></tr><tbody>");
		$('listDiv').appendChild(table);
		for(var i = 0 ; i < prcsJson.length ;i++){
		  var prcs = prcsJson[i];
		  var prcsTo = prcs.prcsTo;
		  prcsTo = prcs.prcsTo.replace(",0",",结束")
		  if(prcsTo == '0' || !prcsTo || prcsTo == "0,"){
				prcsTo = "结束";
	    }
	    if(prcs.prcsId == 1){
	      firstPrcsSeqId = prcs.tableId;
	    }
	    var tmp = "flowId="+flowId+"&seqId="+ prcs.tableId + "&isList=true";
	    var property = "javascript:parent.location.href = \""+contextPath+"/core/funcs/workflow/flowdesign/viewlist/setproperty/index.jsp?" + tmp + "\"";
	    var priv = contextPath+"/core/funcs/workflow/flowdesign/viewlist/setPriv/setOperatePriv.jsp?" + tmp;
	    var setField = contextPath+"/core/funcs/workflow/flowdesign/viewlist/setField/setField.jsp?" + tmp;
	    var hiddenField = contextPath+"/core/funcs/workflow/flowdesign/viewlist/setField/setHiddenField.jsp?" + tmp;
	    var setCondition = contextPath+"/core/funcs/workflow/flowdesign/viewlist/setCondition/setCondition.jsp?" + tmp;
        var setDoc = contextPath+"/core/funcs/workflow/flowdesign/viewlist/setDoc/setDoc.jsp?" + tmp;
	    var tr = new Element('tr',{'font-size':'10pt'});
	    table.firstChild.appendChild(tr); 
        var tdStr = "<td  align=center>"
          + prcs.prcsId + "</td><td align=center>"
          + prcs.prcsName  + "</td><td align=center>" 
          + prcsTo + "</td><td align=left>&nbsp;"
          + "<a href='"+ property + "'>基本属性</a>&nbsp;&nbsp;&nbsp;&nbsp;";
        if (prcs.flowType !=  'child') {
           tdStr += "<a href='"+ priv + "'><img src='"+imgPath+"/node_user.gif'/>经办权限</a>&nbsp;&nbsp;&nbsp;&nbsp;"
           + "<a href='"+ setField + "'>可写字段</a>&nbsp;&nbsp;&nbsp;&nbsp;"
           + "<a href='"+ hiddenField + "'>保密字段</a>&nbsp;&nbsp;&nbsp;&nbsp;"
           + "<a href='"+ setCondition + "'>条件设置</a>&nbsp;"; 
         //  + "<a href='"+ setDoc + "'>公文相关</a>&nbsp;" ;
         }
         tdStr += "</td><td  align=center>&nbsp;"
          + "<a href='#' onclick='Clone_Process(\""+ prcs.tableId +"\")'>克隆</a>&nbsp;"
          + "<a href='#' onclick='Del_Process(\""+ prcs.tableId +"\")'>删除</a>&nbsp;</td>";
        
        tr.update(tdStr);
 		if(i%2 == 0){
			tr.className = "TableLine2";
		}else{
			tr.className = "TableLine1";
		}
  		tr.onmouseover = function(){
  	      this.style.backgroundColor = '#FFF';
  	    }
  	    tr.onmouseout = function(){
  		  this.style.backgroundColor = '';
       	}
	  }
		if(!isSetPriv){
	    var content = "您还没有设置本流程第一步骤的经办权限，否则您将无法新建此工作。 请点击\"设置\"按钮进行设置。<div align=center>"
	    	+ "<input class='SmallButtonW' type='button' value='设置' onclick='set_user("+ firstPrcsSeqId +")'/>"
	    	+ "<input class='SmallButtonW' type='button' value='关闭' onclick='winClose()'/></div> ";
	    alertWin('系统提示', content  , 300, 100);
	  }
  }else{
    //提示
    $('hasData').hide();
    $('noData').show();
  }
}
function set_user(seqId){
  var tmp = "flowId="+flowId+"&seqId="+ seqId + "&isList=true";
  var url = "setPriv/setOperatePriv.jsp?" + tmp;
  this.location =  url ;
}
function Del_Process(seqId)
{
  msg='确认要删除该步骤么？';
  if(window.confirm(msg)){
    var url = requestURL + "/delProc.act";
    var json = getJsonRs(url, 'seqId=' + seqId);
    if(json.rtState == '0'){
      location.reload();
    }else{
      alert(json.rtMsrg); 
    }
  }
}
function Clone_Process(seqId)
{
  msg='确认要克隆该步骤么？';
  if(window.confirm(msg)){
    var url = requestURL + "/cloneProc.act";
    var json = getJsonRs(url, 'seqId=' + seqId);
    if(json.rtState == '0'){
      location.reload();
    }else{
      alert(json.rtMsrg); 
    }
  }
}
</script>
</head>

<body onload="doInit()">
<div id="hasData">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="absmiddle"><span class="big3" id="flowNameSpan"> </span><br>
    <div class="small1" style="padding-top:10px">请设定好各步骤的可写字段和经办权限（此经办权限是经办人员、经办部门、经办角色的合集）</div>
    </td>
  </tr>
</table>

<div id="listDiv"></div>
</div>
<div id="noData"  align=center style="display:none">
<table class="MessageBox" width="300" >
    <tbody>
        <tr>
            <td class="msg info">尚未定义步骤</td>
        </tr>
    </tbody>
</table>

</div>
</body>
</html>
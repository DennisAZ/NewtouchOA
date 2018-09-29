<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<style>
#search{
   width:189px;
   margin-left:0px;
   border:1px solid #154B76;
   background: url('image/bg_input_text.png') top left repeat-x;
   filter:alpha(opacity=80);-moz-opacity:0.8;opacity:0.8;
}
#search:hover{
   border:1px solid #99CC00;
   background: url('image/bg_input_text_hover.png') top left repeat-x;
}

#search input.SmallInput {font-size: 9pt; border:0px; color:#8896A0;width:155px;background:none;}
#search input.SmallInput:hover {border:0px; color:#000000;}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/javascript">
function editForm(id,sortId){
  if(id){
    parent.edit.location = "edit.jsp?sortId=" + sortId + "&seqId=" + id;
  }else{
    parent.edit.location = "edit.jsp?sortId=" + sortId ;
  }
}

var tree = null;
function doInit(){
 tree = new DTree({bindToContainerId:'sortList'
   ,requestUrl:contextPath + '/yh/core/funcs/workflow/act/YHFlowSortAct/getFromSortList.act?id='
   ,isOnceLoad:false//不是同步加载
   ,linkPara:{clickFunc:openNode}//为每个结点的a标签加下点击事件
 });
 tree.show(); 
}
function openNode(nodeId) {
  if (nodeId.indexOf("F") >= 0) {
    id = nodeId.substr(1).split(":");
    parent.edit.location = "edit.jsp?sortId=" + id[1] + "&seqId=" + id[0];
  } else {
    if (nodeId == '-1') {
      nodeId = 0 ;
    }
    parent.edit.location = "list.jsp?sortId=" + nodeId ;
  }
}
function getList(tmp){
  parent.edit.location = "list.jsp?sortId=" + tmp.extData;
}
function actionFuntion(tmp){ 
  editForm(tmp.extData,tmp.sortId);
}
function iconActionFuntion(tmp){ 
  openFormDesign(tmp.extData);
}

var interval=null,key="";
var KWORD;
function CheckSend()
{
	KWORD=$("kword");
	if(KWORD.value=="表单检索...")
	   KWORD.value="";
  if(KWORD.value=="" && $('search_icon').src.indexOf("/images/quicksearch.gif")==-1)
	{
	   $('search_icon').src= imgPath + "/quicksearch.gif";
	}
	if(key!=KWORD.value)
	{
     key=KWORD.value;
     var pageURL = parent.mainFrame.location.toString();     
     if(pageURL.indexOf("search.jsp")>0)
	     parent.mainFrame.doSearch(KWORD.value);
	   else
	   	 parent.mainFrame.location = "search.jsp?searchKey="+KWORD.value;
	   if($('search_icon').src.indexOf( imgPath + "/quicksearch.gif")>=0)
	   {
	   	   $('search_icon').src = imgPath + "/closesearch.gif";
	   	   $('search_icon').title="清除关键字";
	   	   $('search_icon').onclick=function(){
	   	   	                          KWORD.value='表单检索...';
	   	   	                          $('search_icon').src= imgPath + "/quicksearch.gif";
	   	   	                          $('search_icon').title="";
                                    $('search_icon').onclick=null;};
	   }
  }
}
</script>
</head>
<body onload='doInit()'> 
<br>
<table  border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="absmiddle"><span class="big3"> 新建表单</span><br>
    </td>
  </tr>
</table>
<br>
<div align="center">
<input type="button"  value="新建表单" class="BigButtonB" onClick="window.parent.edit.location='<%=contextPath %>/core/funcs/workflow/flowform/edit.jsp';" title="新建表单">
</div>
<br/>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="absmiddle"><span class="big3"> 管理表单</span><br>
    </td>
  </tr>
</table>
<div id="search">
<input type="text" id="kword" name="kword" value="表单检索..." onfocus="interval=setInterval(CheckSend,100);" onblur="clearInterval(interval);if(KWORD.value=='')KWORD.value='流程检索...';" class="SmallInput" /><img id="search_icon" src="<%=imgPath %>/quicksearch.gif" align=absmiddle style="cursor:pointer;" />
</div>
<br/>
<div id='sortList'></div>
<br>



</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
	String seqId=request.getParameter("seqId");
	if(seqId==null){
	  seqId="";
	}
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>

<script type="text/javascript">
var seqId='<%=seqId%>';
var tree = null;
function doInit(){	
  tree =  new DTree({bindToContainerId:'xtree' 
  ,requestUrl:contextPath + '/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/getGiftTypeTree.act?GIFT_PROTYPE=GIFT_PROTYPE&id='
  ,isOnceLoad:false
  ,checkboxPara:{isHaveCheckbox:false}
  , treeStructure:{isNoTree:false}
	,linkPara:{clickFunc:test}
	,treeStructure:{isNoTree:false}	
});

tree.show();
}
function test(id,temp){  
//var node=tree.getNode(id);//node得到的是你点击的结点的一个js对象
//var parentId=node.parentId;
//alert("curNode:"+node+"  parentId>>>:"+parentId);
//alert(id);
if(id){
  if(id.split(",").length>1){
  	url="editinstock.jsp?seqId=" + id.split(",")[0];
  	parent.file_main.location.href=url;
  }else{
  	url="blank.jsp?seqId=" + id ;
  	parent.file_main.location.href=url;
  }
}

}

function toAddCodeItem(){
    url="blank.jsp?type=1";
	parent.file_main.location.href=url;
}
function queryGift(){
  url="blank.jsp?type=2";
parent.file_main.location.href=url;
}
</script>
</head>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/notify_open[1].gif" align="absmiddle"><span class="big3"> 礼品列表</span><br>
    </td>
  </tr>
</table>
<br>

<body onload="doInit();">

<div id="xtree"></div>
<BR></BR>
<div id="buttonOpts"  align="center">
<input type="button"  value="新建礼品类别" class="BigButtonC" onClick="toAddCodeItem();" title="添加新的礼品类别">
</div>
 
<br>
<div align="center">
<input type="button"  value="礼品信息查询" class="BigButtonC" onClick="queryGift();" title="查询礼品信息">
</div>
<script language="JavaScript"> 
var td_id=1;
function setGroupPointer(theRow, thePointerColor,td_id_over)
{
	 if(td_id!=td_id_over)
     theRow.bgColor = thePointerColor; 
} 
 
function clickMenu(SORT_ID)
{
 
    parent.list.location="list.php?SORT_ID="+SORT_ID;
    targetelement=document.all(SORT_ID);
    if (targetelement.style.display=="none")
        targetelement.style.display='';
    else
        targetelement.style.display="none";
}
 
function view_group(theRow,group_id,form_id)
{
   parent.list.location="edit.php?SORT_ID="+group_id+"&FORM_ID="+form_id;
   td_id=group_id;
     theRow.bgColor='';
}
</script>
 

</div>


</body>
</html>
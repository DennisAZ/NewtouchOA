<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品列表</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">
var tree = null;
function doInit(){
  tree =  new DTree({bindToContainerId:'xtree' 
    ,requestUrl:contextPath + '/yh/subsys/oa/officeProduct/query/act/YHOfficeQueryAct/getQueryTree.act?GIFT_PROTYPE=GIFT_PROTYPE&id='
    ,isOnceLoad:false
    ,checkboxPara:{isHaveCheckbox:false}
    , treeStructure:{isNoTree:false}
  	,linkPara:{clickFunc:test}
  	,treeStructure:{isNoTree:false}	
  });

  tree.show();
}
function test(id){  
//alert('you click>>>>' + id  );
var node=tree.getNode(id);//node得到的是你点击的结点的一个js对象
var parentId=node.parentId;
//alert("curNode:"+node+"  parentId>>>:"+parentId);
var extData = node.extData;//库seqId
	if(id){
		var idArry = id.split(",");
		if(idArry.length ==1){
			 //alert(idArry[0] + "  extDataP>>" + extData);
			//产品
			var url = "<%=contextPath%>/subsys/oa/officeProduct/query/detail.jsp?proSeqId=" + idArry[0] + "&typeId=" + parentId.split(",")[0] + "&storeId=" + extData;
			parent.file_main.location.href = url;
		}else	if(idArry.length>2 && idArry.length<4){
			//alert(idArry[0] + "  extDataT>>" + extData);
			//类型
			var url = "<%=contextPath%>/subsys/oa/officeProduct/query/query.jsp?typeId=" + idArry[0] + "&storeId=" + extData;
			parent.file_main.location.href = url;
		}
		//var url="folder.jsp?seqId=" + id 
		var url="privacyFlag.jsp?seqId=" + id 
		//parent.file_main.location.href = url;
	}
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" align="absmiddle"><span class="big3"> 办公用品列表</span><br>
    </td>
  </tr>
</table>
<div id="xtree"></div>




</body>
</html>
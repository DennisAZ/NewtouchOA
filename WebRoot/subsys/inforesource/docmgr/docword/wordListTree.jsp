<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>主题词管理</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/menu_left.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript">
var tree = null;
var num=0;
function doInit(){
  var data = {
			  panel:'menuList',
			  index:'1',
			  data:[{title:'主题词列表'}]
			 };
  var menu = new MenuList(data);
  index = menu.getContainerId(1);
  menu.showItem(this,{},1);
  var url = contextPath + "/yh/subsys/inforesouce/docmgr/act/YHWordTreeAct/getTree.act?id=";
  var wordTree={bindToContainerId:index
                ,requestUrl:url
                ,isOnceLoad:false
                ,checkboxPara:{isHaveCheckbox:false}
                ,linkPara:{clickFunc:test,linkAddress:'index.jsp?id=',target:'_blank'}
		        ,isUserModule:true
		        ,isHaveTitle:true
		        ,treeStructure:{isNoTree:false}	
               }
  tree = new DTree(wordTree);
  tree.show();
  var obj = tree.getFirstNode();
  tree.open(obj.nodeId); 
}

function test(id){
  var obj = tree.getNode(id);
  var parentId = obj.parentId;
  if(parentId != '0'){
    window.parent.wordInput.location = "<%=contextPath%>/subsys/inforesource/docmgr/docword/wordEdit.jsp?treeId=" + id;
  }else{
  	var rtJson = getJsonRs(contextPath + "/yh/subsys/inforesouce/docmgr/act/YHSubjectTermAct/getSeqId.act");
  	if(rtJson.rtState=="0"){
      var data=rtJson.rtData;
      var nodeId = data.split(',')
      if(num%2==0){
	    for(var i=0;i<nodeId.length;i++){
	      tree.open(nodeId[i]);
	    }
	    num++;
      }else{
        for(var i=nodeId.length;i>=0;i--){
          tree.close(nodeId[i]);
        }
        num++;
      }
	}
  }
}
</script>
</head>
<body onload="doInit()" style="overflow-x:hidden;">
 <div id="menuList">
</div>
</body>
</html>
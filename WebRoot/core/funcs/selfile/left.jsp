<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String ext_filter=request.getParameter("EXT_FILTER");
	if(ext_filter==null){
		ext_filter="";
	}
	
	String div_id=request.getParameter("DIV_ID");
	if(div_id==null){
		div_id="";
	}
	String dir_field=request.getParameter("DIR_FIELD");
	if(dir_field==null){
		dir_field="";
	}
	String name_field=request.getParameter("NAME_FIELD");
	if(name_field==null){
		name_field="";
	}
	String type_field=request.getParameter("TYPE_FIELD");
	if(type_field==null){
		type_field="";
	}
	String multi_select=request.getParameter("MULTI_SELECT");
	if(multi_select==null){
		multi_select="";
	}
	

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/tree.css">
<link rel="stylesheet" href = "<%=cssPath%>/menu_left.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/MenuList.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js"></script>
<script type="text/javascript">
//alert("left.jsp>>>ext_filter:"+'<%=ext_filter%>' +"  div_id:"+'<%=div_id%>' +"  dir_field:"+ '<%=dir_field%>' +"  name_field:"+ '<%=name_field%>' +"  type_field:"+ '<%=type_field%>' +"  multi_select:"+ '<%=multi_select%>');

var tree = null;
var peersonTree = null;
var index3 = "";
var index4 = "";
function doInit(){
	var data = {
	        //fix:true,      //默认第一个不能收缩

			panel:'menuList',
			index3:'3',
      index4:'4',
			data:[{title:'已选文件', action:selectedFile},
						{title:'网络硬盘', action:netdisk},
						{title:'个人文件柜',action:personFolder},
						{title:'公共文件柜',action:loadFileTree}]
			};
	var menu = new MenuList(data);
	index3 = menu.getContainerId(3);
	//loadData();
    index4 = menu.getContainerId(4);
	//loadFileTree();
	//menu.showItem(this,{},4);
    
    //location = "<%=contextPath%>/yh/core/funcs/address/act/YHAddressAct/getMb.act";
}


function personFolder(){

  personUrl="<%=contextPath%>/core/funcs/selfile/selPersonFolder.jsp?EXT_FILTER=<%=ext_filter%>&DIV_ID=<%=div_id %>&DIR_FIELD=<%=dir_field %>&NAME_FIELD=<%=name_field %>&TYPE_FIELD=<%=type_field %>&MULTI_SELECT=<%=multi_select %>&seqId=0"; 
	parent.file_main.location.href=personUrl;
  loadPersonTree();
}





function selectedFile(){
	//alert("selected.jsp");
	var url="selected.jsp?EXT_FILTER=<%=ext_filter%>&DIV_ID=<%=div_id %>&DIR_FIELD=<%=dir_field %>&NAME_FIELD=<%=name_field %>&TYPE_FIELD=<%=type_field %>&MULTI_SELECT=<%=multi_select %>";
	//alert(url);
	parent.file_main.location.href=url;
}

function netdisk(){	
	//alert("netdisk");
	var url="netdisk.jsp?EXT_FILTER=<%=ext_filter%>&DIV_ID=<%=div_id %>&DIR_FIELD=<%=dir_field %>&NAME_FIELD=<%=name_field %>&TYPE_FIELD=<%=type_field %>&MULTI_SELECT=<%=multi_select %>";
	//alert(url);
	parent.file_main.location.href=url;
}

function loadPersonTree(){	
  var container = document.getElementById(index3);
  var divStr = document.createElement("div");
  divStr.id="peersonXtree";
  container.style.overflow = 'auto';

  peersonTree =  new DTree({bindToContainerId:index3
    ,requestUrl:contextPath + '/yh/core/funcs/personfolder/act/YHPersonFolderAct/getPersonTree.act?id='
    ,isOnceLoad:false
    ,checkboxPara:{isHaveCheckbox:false}
  	,linkPara:{clickFunc:test}
  	,treeStructure:{isNoTree:false}	
  	, isUserModule:true
  	,treeId:'person'  //用于区分树
  });

  peersonTree.show();
}



function loadFileTree(){
	
	//alert("lo");
	var container = document.getElementById(index4);
	//alert(container);
  var divStr = document.createElement("div");
  //alert(divStr);
  
  divStr.id="xtree";
  //container.appendChild(divStr);	
  //alert( $("xtree").id);
  container.style.overflow = 'auto';
  tree =  new DTree({bindToContainerId:index4
    ,requestUrl:contextPath + '/yh/core/funcs/system/filefolder/act/YHFileSortAct/getPrivTree.act?id='
    ,isOnceLoad:false
    ,checkboxPara:{isHaveCheckbox:false}
  	,linkPara:{clickFunc:test}
  	,treeStructure:{isNoTree:false}	
  	, isUserModule:true
  	,treeId:'folder'  //用于区分树
  });

  tree.show();
  
}

function test(id , treeId){  
//alert('you click' + id);
//var node=tree.getNode(id);//node得到的是你点击的结点的一个js对象
//var parentId=node.parentId;
//alert("curNode:"+node+"  parentId>>>:"+parentId);

	if (treeId == 'folder') {
		if(id){
			url="folder.jsp?EXT_FILTER=<%=ext_filter%>&DIV_ID=<%=div_id %>&DIR_FIELD=<%=dir_field %>&NAME_FIELD=<%=name_field %>&TYPE_FIELD=<%=type_field %>&MULTI_SELECT=<%=multi_select %>&seqId=" + id ;
			parent.file_main.location.href=url;
		}
	} else if(treeId == 'person') {
		//alert("id>>>>"+id);
		personUrl="<%=contextPath%>/core/funcs/selfile/selPersonFolder.jsp?EXT_FILTER=<%=ext_filter%>&DIV_ID=<%=div_id %>&DIR_FIELD=<%=dir_field %>&NAME_FIELD=<%=name_field %>&TYPE_FIELD=<%=type_field %>&MULTI_SELECT=<%=multi_select %>&seqId=" + id ;
		parent.file_main.location.href=personUrl;
	}

}


function unloadHandler() {
  var ParentWindow=parent.opener.window;
  if (ParentWindow && ParentWindow.restoreFile) {
    var i = '<%=div_id%>'.substr('SelFileDiv'.length);
    ParentWindow.restoreFile(i);
  }
}
</script>

</head>
<body onload="doInit();" onbeforeunload="unloadHandler()">
<div id="menuList"></div>

</body>
</html>
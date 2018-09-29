<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">
var prcsJson = [{prcsId:1,prcsName:'开始',tableId:12,flowType:'start',flowTitle:'开始',fillcolor:'#50A625',leftVml:'21',topVml:'85',prcsTo:'2'}
,{prcsId:2,prcsName:'审批',tableId:13,flowType:'',flowTitle:'审批',fillcolor:'#EEEEEE',leftVml:'246',topVml:'299',prcsTo:'1,3,4'}
,{prcsId:4,prcsName:'审批2',tableId:16,flowType:'child',flowTitle:'审批2',fillcolor:'#70A0DD',leftVml:'344',topVml:'333',prcsTo:'5'}
,{prcsId:5,prcsName:'条件',tableId:17,flowType:'',flowTitle:'条件',fillcolor:'#EEEEEE',leftVml:'344',topVml:'333',prcsTo:'3',condition:'ddddd'}
,{prcsId:3,prcsName:'结束',tableId:14,flowType:'end',flowTitle:'结束',fillcolor:'#F4A8BD',leftVml:'454',topVml:'124',prcsTo:'0'}];
function doInit(){
  if(prcsJson.length > 0){
	var table = new Element('table',{ "width":"100%"}).update("<tbody id='tbody'><tr class='TableHeader' style='font-size:10pt'><td>序号</td><td>名称</td><td>下一步骤</td><td>编辑该步骤的各项属性</td><td>操作</td></tr><tbody>");
	$('listDiv').appendChild(table);
	for(var i = 0 ; i < prcsJson.length ;i++){
	  var prcs = prcsJson[i];
	  var prcsTo = prcs.prcsTo;
	  if(prcsTo == '0' || !prcsTo){
		prcsTo = "结束";

	  }
	  var tr = new Element('tr',{'font-size':'10pt'}).update("<td>"
	  	  + prcs.prcsId +"</td><td>"
	  	  + prcs.prcsName  +"</td><td>" 
	  	  + prcsTo + "</td><td>&nbsp;<a herf='#'>基本属性</a>&nbsp;<a herf='#'>经办权限</a>&nbsp;<a herf='#'>可写字段</a>&nbsp;<a herf='#'>保密字段</a>&nbsp;<a herf='#'>条件设置</a>&nbsp;</td><td>&nbsp;<a href='#'>克隆</a>&nbsp;<a href='#'>删除</a>&nbsp;</td>");
	  if(i%2 == 0){
		tr.className = "TableLine2";

	  }else{

	    tr.className = "TableLine1";

	  }
	  table.firstChild.appendChild(tr);  
  	}
  }else{
  //提示

  }

  
}

</script>
</head>

<body onload="doInit()">

<div id="listDiv"></div>
</body>
</html>
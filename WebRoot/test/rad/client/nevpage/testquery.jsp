<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src = "/yh/rad/grid/grid.js">
</script>
<script type="text/Javascript" src="/yh/core/js/datastructs.js"></script>
<script type="text/Javascript" src="/yh/core/js/sys.js" ></script>
<script type="text/Javascript" src="/yh/core/js/prototype.js" ></script>
<script type="text/Javascript" src="/yh/core/js/smartclient.js" ></script>
<link rel="stylesheet" type="text/css" href="/yh/rad/grid/grid.css"/>
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<title>grid test query</title>
<script type="text/javascript">

var hd =[
       	  [
	       	{header:"编号",name:"seqId",width:"10%"},
	       	{header:"标记类型号",name:"flagSort",width:"20%"},
	       	{header:"标记描述",name:"flagSortDesc",width:"40%"}
	      ],
	      {
    		header:"操作",
    		oprates:[
	               new YHOprate('增加',true,function(record,index){alert(record.getField('flagSort').value+":"+index)}),
                 new YHOprate('oprate4',false,function(){alert("oprate4")}),
                 new YHOprate('删除',true,function(){alert("oprate2")})
              ]
        }
];
var url = "/yh/yh/rad/grid/act/YHGridNomalAct/doquery.act?tabNo=11114";
var grid = new YHGrid(hd,url,null,10);
 function loads(){
	 grid.rendTo('grid');
 }
 function onsubmitid(){
	 grid.removeFilter();
	 grid.addFilter(2,"SEQ_ID","<");
   grid.doquery();
 }
 function onsubmitsor(){
	 grid.removeFilter();
	 grid.addFilter(1,"FLAG_SORT","<");
   grid.doquery();
 }
 function onsubmit(){
	 grid.removeFilter();
	 grid.addFilter(1,"FLAG_SORT","<");
	 grid.addFilter(2,"SEQ_ID","<");
   grid.doquery();
 }
</script >
</head>
<body onload="loads()">
<div id = "grid"></div>
标记类型号模糊查询：<input id = "FLAG_SORT_from"  class="SmallInput">
<input type="button" value="模糊查询" onclick="onsubmitsor()"  class="BigButton">
<br>
编号范围查询：从：<input id = "SEQ_ID_from"  class="SmallInput">
到： <input id = "SEQ_ID_to"  class="SmallInput">
<input type="button" value="范围查询" onclick="onsubmitid()"  class="BigButton">
<br>
标记类型号 + 编号范围的组合查询：<input type="button" value="组合查询" onclick="onsubmit()" class="BigButton"  class="BigButton">
</body>
</html>
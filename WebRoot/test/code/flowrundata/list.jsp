<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列出所有的标记</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" type="text/css" href="/yh/rad/dsdef/css/tableList.css" />
<script type="text/javascript" src="<%=contextPath %>/rad/dsdef/js/gridtable.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/rad/grid/grid.js"></script>
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/rad/grid/grid.css" />
<script type="text/javascript">
  var pN  = <%=request.getParameter("pageNo")%>;
  if(!pN){
	pN = 1;
  }
  var hd =[
	         [
            {header:"seqId",name:"seqId",hidden:true}
           ,{header:"RUN_ID",name:"runId",hidden:false ,width:50}
           ,{header:"ITEM_ID",name:"itemId",hidden:false ,width:50}
           ,{header:"ITEM_DATA",name:"itemData",hidden:false ,width:50}
           		      ],
		      {
	   	   	  header:"操作"
	   	   	  ,oprates:[new YHOprate('编辑',true,editorRecord),
	   		              new YHOprate('删除',true,deleteRecord)
	          	       ]
	          	       , width: 100
	         }
	       ];
  var url = "/yh/yh/rad/grid/act/YHGridNomalAct/jsonTest.act?tabNo=11125";
  var grid = new YHGrid(hd, url, null, 5, pN);
  function doInit(){
	  grid.rendTo('grid');	  
  }
  function editorRecord(record, index) {
    var pagNo = grid.getPageNo() + 1 ;
    window.location.href = "<%=contextPath %>/test/code/flowrundata/input.jsp?seqId=" + record.getField('seqId').value
    + "&pageNo=" + pagNo;
  }
  function deleteRecord(record, index) {
	  if(!confirmDel()) {
	    return ;
    }
    var url = "<%=contextPath %>/test/cy/code/act/YHFlowRunDataAct/deleteField.act";
    var rtJson = getJsonRs(url, "seqId=" + record.getField('seqId').value);
    alert(rtJson.rtMsrg); 
    if (rtJson.rtState == "0") {
      window.location.reload();
    }
  }
  function confirmDel() {
    if(confirm("确认删除！")) {
      return true;
    }else {
      return false;
    }
  }
</script>
</head>
<body onload="doInit()">
  <div id="grid" style="width: 900px; height: 400px;">
  </div>
</body>
</html>
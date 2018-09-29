<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列出所有的标记</title>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/grid.js"></script>
<link rel="stylesheet" type="text/css" href="<%=cssPath%>/cmp/grid.css" />
<script type="text/javascript">
  var pN  = <%=request.getParameter("pageNo")%>;
  if(!pN){
	pN = 1;
  }
  
  var hd =[
	       [
            {header:"标记主键",name:"seqId",hidden:true, width:80}
            ,{header:"标记分类",name:"flagSortDesc",hidden:false, width:80}
		    ,{header:"标记编号",name:"flagCode",hidden:false, width:80}
		    ,{header:"标记描述",name:"flagDesc",hidden:false, width:60}
		   ],
		    {
	   	   	 header:"操作",
	   	   	 oprates:[
		    		  new YHOprate('编辑',true,mendFlag),
	   		          new YHOprate('删除',true,deleteFlag)
	          	     ], width: 100
	         }
	       ];
  var url = "/yh/yh/rad/grid/act/YHGridNomalAct/jsonTest.act?tabNo=11114";
  var grid = new YHGrid(hd, url, null, 5, pN);

  function doInit(){
	grid.rendTo('flag');	  
  }

  function mendFlag(record, index) {
    var pagNo = grid.getPageNo() + 1 
    window.location.href = "<%=contextPath %>/core/funcs/specialflag/specialflaginput.jsp?seqId=" + record.getField('seqId').value
    + "&pageNo=" + pagNo;
  }
	
  function confirmDel() {
    if(confirm("确认删除！")) {
      return true;
    }else {
      return false;
    }
  }
  
  function deleteFlag(record, index) {
	if(!confirmDel()) {
	  return ;
    }
      
    var url = "<%=contextPath %>/yh/core/funcs/specialflag/act/YHSpecialFlagAct/deleteSpecialFlag.act";
    var rtJson = getJsonRs(url, "seqId=" + record.getField('seqId').value);
    if (rtJson.rtState == "0") {
      alert(rtJson.rtMsrg); 
      window.location.reload();
    }else {
        alert(rtJson.rtMsrg); 
    }
  }
</script>
</head>
<body onload="doInit()">
  <div id="flag" style="width: 900px; height: 400px;">
  </div>
</body>
</html>
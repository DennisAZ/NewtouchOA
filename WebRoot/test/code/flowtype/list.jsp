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
           ,{header:"FLOW_NAME",name:"flowName",hidden:false ,width:50}
           ,{header:"FORM_SEQ_ID",name:"formSeqId",hidden:false ,width:50}
           ,{header:"FLOW_DOC",name:"flowDoc",hidden:false ,width:50}
           ,{header:"FLOW_TYPE",name:"flowType",hidden:false ,width:50}
           ,{header:"MANAGE_USER",name:"manageUser",hidden:false ,width:50}
           ,{header:"FLOW_NO",name:"flowNo",hidden:false ,width:50}
           ,{header:"FLOW_SORT",name:"flowSort",hidden:false ,width:50}
           ,{header:"AUTO_NAME",name:"autoName",hidden:false ,width:50}
           ,{header:"AUTO_NUM",name:"autoNum",hidden:false ,width:50}
           ,{header:"AUTO_LEN",name:"autoLen",hidden:false ,width:50}
           ,{header:"QUERY_USER",name:"queryUser",hidden:false ,width:50}
           ,{header:"FLOW_DESC",name:"flowDesc",hidden:false ,width:50}
           ,{header:"AUTO_EDIT",name:"autoEdit",hidden:false ,width:50}
           ,{header:"NEW_USER",name:"newUser",hidden:false ,width:50}
           ,{header:"QUERY_ITEM",name:"queryItem",hidden:false ,width:50}
           ,{header:"COMMENT_PRIV",name:"commentPriv",hidden:false ,width:50}
           ,{header:"DEPT_ID",name:"deptId",hidden:false ,width:50}
           ,{header:"FREE_PRESET",name:"freePreset",hidden:false ,width:50}
           ,{header:"FREE_OTHER",name:"freeOther",hidden:false ,width:50}
           ,{header:"QUERY_USER_DEPT",name:"queryUserDept",hidden:false ,width:50}
           ,{header:"MANAGE_USER_DEPT",name:"manageUserDept",hidden:false ,width:50}
           ,{header:"EDIT_PRIV",name:"editPriv",hidden:false ,width:50}
           ,{header:"LIST_FLDS_STR",name:"listFldsStr",hidden:false ,width:50}
           ,{header:"ALLOW_PRE_SET",name:"allowPreSet",hidden:false ,width:50}
           ,{header:"MODEL_ID",name:"modelId",hidden:false ,width:50}
           ,{header:"MODEL_NAME",name:"modelName",hidden:false ,width:50}
           		      ],
		      {
	   	   	  header:"操作"
	   	   	  ,oprates:[new YHOprate('编辑',true,editorRecord),
	   		              new YHOprate('删除',true,deleteRecord)
	          	       ]
	          	       , width: 100
	         }
	       ];
  var url = "/yh/yh/rad/grid/act/YHGridNomalAct/jsonTest.act?tabNo=11123";
  var grid = new YHGrid(hd, url, null, 5, pN);
  function doInit(){
	  grid.rendTo('grid');	  
  }
  function editorRecord(record, index) {
    var pagNo = grid.getPageNo() + 1 ;
    window.location.href = "<%=contextPath %>/test/code/flowtype/input.jsp?seqId=" + record.getField('seqId').value
    + "&pageNo=" + pagNo;
  }
  function deleteRecord(record, index) {
	  if(!confirmDel()) {
	    return ;
    }
    var url = "<%=contextPath %>/test/cy/code/act/YHFlowTypeAct/deleteField.act";
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
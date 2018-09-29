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
           ,{header:"FLOW_SEQ_ID",name:"flowSeqId",hidden:true ,width:74}
           ,{header:"PRCS_ID",name:"prcsId",hidden:true ,width:74}
           ,{header:"PRCS_NAME",name:"prcsName",hidden:true ,width:74}
           ,{header:"PRCS_USER",name:"prcsUser",hidden:true ,width:74}
           ,{header:"PRCS_ITEM",name:"prcsItem",hidden:true ,width:74}
           ,{header:"HIDDEN_ITEM",name:"hiddenItem",hidden:true ,width:74}
           ,{header:"PRCS_DEPT",name:"prcsDept",hidden:true ,width:74}
           ,{header:"PRCS_PRIV",name:"prcsPriv",hidden:true ,width:74}
           ,{header:"PRCS_TO",name:"prcsTo",hidden:true ,width:74}
           ,{header:"SET_LEFT",name:"setLeft",hidden:true ,width:74}
           ,{header:"SET_TOP",name:"setTop",hidden:true ,width:74}
           ,{header:"PLUGIN",name:"plugin",hidden:true ,width:74}
           ,{header:"PRCS_ITEM_AUTO",name:"prcsItemAuto",hidden:true ,width:74}
           ,{header:"PRCS_IN",name:"prcsIn",hidden:true ,width:74}
           ,{header:"PRCS_OUT",name:"prcsOut",hidden:true ,width:74}
           ,{header:"FEEDBACK",name:"feedback",hidden:true ,width:74}
           ,{header:"PRCS_IN_SET",name:"prcsInSet",hidden:true ,width:74}
           ,{header:"PRCS_OUT_SET",name:"prcsOutSet",hidden:true ,width:74}
           ,{header:"AUTO_TYPE",name:"autoType",hidden:true ,width:74}
           ,{header:"AUTO_USER_OP",name:"autoUserOp",hidden:true ,width:74}
           ,{header:"AUTO_USER",name:"autoUser",hidden:true ,width:74}
           ,{header:"USER_FILTER",name:"userFilter",hidden:true ,width:74}
           ,{header:"TIME_OUT",name:"timeOut",hidden:true ,width:74}
           ,{header:"TIME_EXCEPT",name:"timeExcept",hidden:true ,width:74}
           ,{header:"SIGNLOOK",name:"signlook",hidden:true ,width:74}
           ,{header:"TOP_DEFAULT",name:"topDefault",hidden:true ,width:74}
           ,{header:"USER_LOCK",name:"userLock",hidden:true ,width:74}
           ,{header:"MAIL_TO",name:"mailTo",hidden:true ,width:74}
           ,{header:"SYNC_DEAL",name:"syncDeal",hidden:true ,width:74}
           ,{header:"SYNC_DEAL_CHECK",name:"syncDealCheck",hidden:true ,width:74}
           ,{header:"TURN_PRIV",name:"turnPriv",hidden:true ,width:74}
           ,{header:"CHILD_FLOW",name:"childFlow",hidden:true ,width:74}
           ,{header:"GATHER_NODE",name:"gatherNode",hidden:true ,width:74}
           ,{header:"ALLOW_BACK",name:"allowBack",hidden:true ,width:74}
           ,{header:"ATTACH_PRIV",name:"attachPriv",hidden:true ,width:74}
           ,{header:"AUTO_BASE_USER",name:"autoBaseUser",hidden:true ,width:74}
           ,{header:"CONDITION_DESC",name:"conditionDesc",hidden:true ,width:74}
           ,{header:"RELATION",name:"relation",hidden:true ,width:74}
           ,{header:"REMIND_FLAG",name:"remindFlag",hidden:true ,width:74}
           ,{header:"DISP_AIP",name:"dispAip",hidden:true ,width:74}
           ,{header:"TIME_OUT_TYPE",name:"timeOutType",hidden:true ,width:74}
           		      ],
		      {
	   	   	  header:"操作"
	   	   	  ,oprates:[new YHOprate('编辑',true,editorRecord),
	   		              new YHOprate('删除',true,deleteRecord)
	          	       ]
	          	       , width: 100
	         }
	       ];
  var url = "/yh/yh/rad/grid/act/YHGridNomalAct/jsonTest.act?tabNo=11121";
  var grid = new YHGrid(hd, url, null, 5, pN);
  function doInit(){
	  grid.rendTo('grid');	  
  }
  function editorRecord(record, index) {
    var pagNo = grid.getPageNo() + 1 ;
    window.location.href = "<%=contextPath %>/test/code/flowprocess/input.jsp?seqId=" + record.getField('seqId').value
    + "&pageNo=" + pagNo;
  }
  function deleteRecord(record, index) {
	  if(!confirmDel()) {
	    return ;
    }
    var url = "<%=contextPath %>/test/cy/code/act/YHFlowProcessAct/deleteField.act";
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
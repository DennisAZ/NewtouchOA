<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>待批申请</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/transInfoLogic.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/funcs/workflow/workflowUtility/utility.js"></script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
  var url = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct/getTranshistoryListJson.act";
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"proName",  width: '10%', text:"名称", render:infoCenterFunc},       
         {type:"data", name:"transFlag",  width: '10%', text:"登记类型", render:transFlagFunc},
         {type:"data", name:"borrower",  width: '10%', text:"申请人", render:borrowerFunc},
         {type:"data", name:"transQty",  width: '10%', text:"数量", render:transQtyFunc},
         {type:"data", name:"transDate",  width: '10%', text:"申请日期", render:transDateFunc},
         {type:"hidden", name:"proId",  width: '10%', text:"产品seqId", render:infoCenterFunc},
         {type:"hidden", name:"cycleNo",  width: '10%', text:"批量Id"},
         {type:"hidden", name:"flowId"},
         {type:"hidden", name:"runId"},
         {type:"selfdef", text:"操作", width: '10%',render:optsList}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      //$("totalSpan").innerHTML = total;
      showCntrl('listContainer');
      //var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    }else{
      //$("spanDiv").style.display = 'none';
      WarningMsrg("无办公用品登记信息", 'msrg');
    }
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small" align="center">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/sentbox.gif" WIDTH="20" HEIGHT="20" align="middle"><span class="big3"> 待批申请</span>
    </td></tr>
</table>
<div id="listContainer" style="display:none;width:95;"></div>
<div id="msrg">
</div>


</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
  String proName = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("proName")));
  String proDesc = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("proDesc")));
  String proCode = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("proCode")));
  String officeDepository = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("officeDepository")));
  String officeProtype = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("officeProtype")));
  
  //System.out.println(proName+"=="+proDesc+"=="+proCode+"=="+officeDepository+"=="+officeProtype);
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/transInfoLogic.js"></script>
<script type="text/javascript">

function infoCenterFunc1(cellData, recordIndex, columIndex) {
	var proLowstock = cellData;
	var proMaxstock = this.getCellData(recordIndex, "proMaxstock");

	
	var optStr = proLowstock + "-" + proMaxstock;
	
	return "<center>" +optStr + "</center>";
}
//单价
function infoCenterPriceFunc(cellData, recordIndex, columIndex){
	return "<center>" + cellData +"元"+ "</center>";
}
//操作
function OperateFunc(cellData, recordIndex, columIndex){
	var proId = this.getCellData(recordIndex,"seqId");
  //var officeTypeId = this.getCellData(recordIndex,"officeTypeId");
  var typeDepository = this.getCellData(recordIndex,"typeDepository");
  var typeId = this.getCellData(recordIndex,"typeId");
  //alert(proId+"=="+typeDepository +"=="+typeId);
  return "<center>"
	    + "<a href=javascript:applicationSingle("+proId+","+typeDepository+","+typeId+")>申请</a>"
	    + "</center>";
}
//申请处理   /yh/subsys/oa/officeProduct/personal/record.jsp
function applicationSingle(proId,typeDepository,typeId){
	
	location.href = contextPath + "/yh/subsys/oa/officeProduct/person/act/YHPersonalOfficeRecordAct/OfficeDepositoryInfo.act?PRO_ID=" +proId+"&DEPOSITORY="+typeDepository+"&TYPE_ID="+typeId;
}

</script>
<script type="text/javascript">
var pageMgr = null;
function doInit(){
	var param = "";
	  param = "proName=" + encodeURIComponent("<%=proName%>");
	  param += "&proDesc=" + encodeURIComponent("<%=proDesc%>");
	  param += "&proCode=" + encodeURIComponent("<%=proCode%>");
	  param += "&officeDepository=" + encodeURIComponent("<%=officeDepository%>");
	  param += "&officeProtype=" + encodeURIComponent("<%=officeProtype%>");
  var url = "<%=contextPath%>/yh/subsys/oa/officeProduct/query/act/YHOfficeQueryAct/getOfficeProductsListJson.act?"+param;
 //alert(url);
    var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
				{type:"hidden", name:"seqId",  width: '10%', text:"办公用品ID"},     
         {type:"data", name:"proName",  width: '10%', text:"名称",render:infoCenterFunc},       
         {type:"data", name:"typeName",  width: '10%', text:"类别",render:infoCenterFunc},
         {type:"data", name:"proCode",  width: '10%', text:"编码",render:infoCenterFunc},
         {type:"data", name:"proUnit",  width: '10%', text:"计量单位",render:infoCenterFunc},
         {type:"data", name:"proPrice",  width: '10%', text:"单价",render:infoCenterPriceFunc},
         {type:"hidden", name:"proSupplier",  width: '10%', text:"供应商",render:infoCenterFunc},
         {type:"data", name:"proLowstock",  width: '10%', text:"警戒库存范围",render:infoCenterFunc1},
         {type:"data", name:"proStock",  width: '10%', text:"当前库存",render:infoCenterFunc},
         {type:"data", name:"proDesc",  width: '10%', text:"描述",render:infoCenterFunc},
         {type:"hidden", name:"proMaxstock",  width: '10%', text:"最高警戒库存",render:infoCenterFunc},
         {type:"hidden", name:"officeTypeId",  width: '10%', text:"产品库对应类型ID",render:infoCenterFunc},
         {type:"hidden", name:"typeId",  width: '10%', text:"办公用品类型ID",render:infoCenterFunc},
         {type:"hidden", name:"typeDepository",  width: '10%', text:"办公用品类型对应产品库Id",render:infoCenterFunc},
         {type:"selfdef", text:"操作", width: '10%',render:OperateFunc}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      
     // showCntrl('delOpt');
    }else{
      WarningMsrg("无办公用品信息记录", 'msrg');
      
    } 
    showCntrl('backDiv');
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="95%" cellspacing="0" cellpadding="3" class="small" align="center">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_open.gif" WIDTH="20" HEIGHT="20" align="middle"><span class="big3">办公用品查询结果</span>
    </td></tr>
</table>
<div id="listContainer" style="display:none;width:95;"></div>
<div id="msrg">
</div>
<div id="backDiv" style="display:none" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/officeProduct/query/query.jsp';">&nbsp;&nbsp;
</div>

</body>
</html>
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
  
 // System.out.println(proName+"=="+proDesc+"=hh="+proCode+"=hh="+officeDepository+"=hh="+officeProtype);
%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>办公用品信息查询</title>
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
//操作
function OperateFunc(cellData, recordIndex, columIndex){
	 var depoId = this.getCellData(recordIndex,"depoId");
  var typeId = this.getCellData(recordIndex,"typeId");
  var proId = this.getCellData(recordIndex,"seqId");
  return "<center>"
	    + "<a href=javascript:applicationSingle("+depoId+","+typeId+","+proId+")>编辑</a>"
	    + "</center>";
}

//申请处理  
function applicationSingle(depoId,typeId,proId){
	location.href = contextPath + "/subsys/oa/officeProduct/product/edit.jsp?proSeqId=" +proId+"&storeId="+depoId+"&typeId="+typeId;
}
/**
 * 创建人名称
 */
function proCreateNameFunc(cellData, recordIndex, columIndex){
  var url = contextPath + "/yh/subsys/oa/officeProduct/query/act/YHOfficeQueryAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + cellData);
  if (rtJson.rtState == "0") {
    return "<center>" + rtJson.rtData + "</center>";
  } else {
    alert(rtJson.rtMsrg); 
  }
}
function checkBoxRender(cellData, recordIndex, columIndex){
	  var diaId = this.getCellData(recordIndex,"seqId");
	    return "<center><input type=\"checkbox\" name=\"deleteFlag\" value=\"" + diaId + "\" onclick=\"checkSelf()\" ></center>";
	}
function checkSelf(){
	  var allCheck = $('checkAlls');
	  if(allCheck.checked){
	    allCheck.checked = false;
	  }
	}

function checkAll(field) {
	  var deleteFlags = document.getElementsByName("deleteFlag");
	  for(var i = 0; i < deleteFlags.length; i++) {
	    deleteFlags[i].checked = field.checked;
	  }
	}

function deleteAll(){
	  var idStrs = checkMags('deleteFlag');
	  if(!idStrs) {
	    alert("要删除办公用品信息，请至少选择其中一个。");
	    return;
	  }
	  if(!window.confirm("确认要删除已选中的办公用品信息？")) {
	    return ;
	  } 
		var requestURLStr = contextPath + "/yh/subsys/oa/officeProduct/query/act/YHOfficeQueryAct";
		var url = requestURLStr + "/deleteOfficeProducts.act";
		var rtJson = getJsonRs(url, "seqId=" + idStrs );
		if (rtJson.rtState == "0") {
			window.location.reload();
		}else {
		 alert(rtJson.rtMsrg); 
		}
	}

function checkMags(cntrlId){
	  var ids= ""
	  var checkArray = $$('input');
	  for(var i = 0 ; i < checkArray.length ; i++){
	    if(checkArray[i].name == cntrlId && checkArray[i].checked ){
	      if(ids != ""){
	        ids += ",";
	      }
	      ids += checkArray[i].value;
	    }
	  }
	  return ids;
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
  var url = "<%=contextPath%>/yh/subsys/oa/officeProduct/query/act/YHOfficeQueryAct/queryOfficeProductsListJson.act?"+param;
 //alert(url);
    var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},     
         {type:"hidden", name:"seqId",  width: '10%', text:"办公用品ID"}, 
         {type:"data", name:"proName",  width: '10%', text:"办公用品名称",render:infoCenterFunc},       
         {type:"data", name:"typeName",  width: '10%', text:"办公用品类别",render:infoCenterFunc},
         {type:"data", name:"proUnit",  width: '10%', text:"计量单位",render:infoCenterFunc},
         {type:"hidden", name:"proSupplier",  width: '10%', text:"供应商",render:infoCenterFunc},
         {type:"data", name:"proLowstock",  width: '10%', text:"警戒库存",render:infoCenterFunc},
         {type:"data", name:"proStock",  width: '10%', text:"当前库存",render:infoCenterFunc},
         {type:"data", name:"proCreator",  width: '10%', text:"创建人",render:proCreateNameFunc},
         {type:"data", name:"manager",  width: '10%', text:"管理人",render:proCreateNameFunc}, 
         {type:"hidden", name:"officeTypeId",  width: '10%', text:"产品库对应类型ID",render:infoCenterFunc},
         {type:"hidden", name:"depoId",  width: '10%', text:"办公用品库ID",render:infoCenterFunc},
         {type:"hidden", name:"typeId",  width: '10%', text:"办公用品类型ID",render:infoCenterFunc},
         {type:"hidden", name:"typeDepository",  width: '10%', text:"办公用品类型对应产品库Id",render:infoCenterFunc},
         {type:"selfdef", text:"操作", width: '10%',render:OperateFunc}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
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
<div id="delOpt" style="display:none">
<table class="TableList" width="100%">
<tr class="TableControl">
      <td colspan="19">
         <input type="checkbox" name="checkAlls" id="checkAlls" onClick="checkAll(this);"><label for="checkAlls">全选</label> &nbsp;
         <a href="javascript:deleteAll();" title="删除所选记录"><img src="<%=imgPath%>/delete.gif" align="absMiddle">删除所选记录</a>&nbsp;
      </td>
 </tr>
</table>
</div>
<div id="msrg">
</div>
<div id="backDiv" style="display:none" align="center">
<br>
  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/officeProduct/product/query.jsp';">&nbsp;&nbsp;
</div>

</body>
</html>
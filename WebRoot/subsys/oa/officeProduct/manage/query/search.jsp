<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String transFlag = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("TRANS_FLAG")));
	String dept = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("dept")));
	String user = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("user")));
	String officeDepository = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("OFFICE_DEPOSITORY")));
	String officeProtype = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("officeProtype")));
	String officePro = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("officePro")));
	String proName = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("PRO_NAME")));
	String beginDate = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("beginDate")));
	String endDate = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("endDate")));
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查询结果</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/officeProduct/js/manageQueryLogic.js"></script>
<script type="text/javascript">
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
	    alert("要删除办公用品登记信息，请至少选择其中一个。");
	    return;
	  }
	  if(!window.confirm("确认要删除已选中的办公用品登记信息？")) {
	    return ;
	  } 
		var requestURLStr = contextPath + "/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
		var url = requestURLStr + "/deleteOfficeTranshistory.act";
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
function doInit(){
	var arttId ="";
	var param = "";
	param = "transFlag=" + encodeURIComponent("<%=transFlag%>");
	param += "&dept=" + encodeURIComponent("<%=dept%>");
	param += "&user=" + encodeURIComponent("<%=user%>");
	param += "&officeDepository=" + encodeURIComponent("<%=officeDepository%>");
	param += "&officeProtype=" + encodeURIComponent("<%=officeProtype%>");
	param += "&officePro=" + encodeURIComponent("<%=officePro%>");
	param += "&proName=" + encodeURIComponent("<%=proName%>");
	param += "&beginDate=" + encodeURIComponent("<%=beginDate%>");
	param += "&endDate=" + encodeURIComponent("<%=endDate%>");
	
	var url = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct/getTransDetailListJson.act?" + param;
	var cfgs = {
	    dataAction: url,
	    container: "listContainer",
	    sortIndex: 1,
	    sortDirect: "desc",
	    colums: [
	       {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
	       {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
	       {type:"data", name:"proName",  width: '10%', text:"办公用品名称", render:infoCenterFunc},
	       {type:"data", name:"transFlag",  width: '10%', text:"登记类型", render:transFlagFunc},
	       {type:"data", name:"borrower",  width: '10%', text:"申请人", render:borrowerFunc},
	       {type:"data", name:"transQty",  width: '10%', text:"数量", render:transQtyFunc},
	       {type:"data", name:"price",  width: '10%', text:"单价", render:infoCenterFunc},
	       {type:"data", name:"transDate",  width: '10%', text:"操作日期", render:transDateFunc},
	       {type:"data", name:"operator",  width: '10%', text:"操作员", render:borrowerFunc},
	       {type:"data", name:"remark",  width: '10%', text:"备注", render:infoCenterFunc},
	       
	       {type:"hidden", name:"proUnit",  width: '', text:"数量单位", render:infoCenterFunc},
	       {type:"hidden", name:"proId",  width: '', text:"产品seqId", render:infoCenterFunc},
	       {type:"hidden", name:"proPrice",  width: '', text:"产品价格", render:infoCenterFunc},
	       {type:"hidden", name:"company",  width: '', text:"公司", render:infoCenterFunc},
	       {type:"hidden", name:"band",  width: '', text:"厂地", render:infoCenterFunc},
	       {type:"selfdef", text:"操作", width: '10%',render:optsList}]
	  };
	  pageMgr = new YHJsPage(cfgs);
	  pageMgr.show();
	  var total = pageMgr.pageInfo.totalRecord;
	  if(total){
		  
	    showCntrl('listContainer');
	    var mrs = " 共 " + total + " 条记录 ！";
	    showCntrl('delOpt');
	  }else{
	    WarningMsrg('无符合条件的记录！', 'msrg');
	  }
}



</script>
</head>
<body onload="doInit();">
	<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/infofind.gif" align="middle">
    <span class="big3"> 办公用品管理明细 </span><br>
   </td>
	</tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;"></div>
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
<div id="msrg"></div>
<br>
<div id="backDiv" style="display: " align="center">
<br>

  <input type="button" value="返回" class="BigButton" onclick="window.location.href='<%=contextPath %>/subsys/oa/officeProduct/manage/query/query.jsp';">&nbsp;&nbsp;
</div>


</body>
</html>
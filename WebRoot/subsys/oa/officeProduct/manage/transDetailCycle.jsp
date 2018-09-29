<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<%
String cycleNo = request.getParameter("cycleNo");
if(YHUtility.isNullorEmpty(cycleNo)){
  cycleNo = "0";
}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>处理待批申请</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var len = 0;
function doInit(){
  var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
  var url = requestURI + "/getTransDetailByCycleNo.act";
  var rtJson = getJsonRs(url,"cycleNo=<%=cycleNo%>");
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    len = data.length;
    var htmlTableHead = '<input type="hidden" id="len" name="len" value="'+len+'">'+
    	                 '<table width="70%" align="center" class="TableBlock">'+
												  '<tr>'+
												    '<td nowrap class="TableData" width="100px">申请类型：</td>'+
												    '<td class="TableData" width="200px"><span id="transName">'+data[0].transName+'</span> </td>'+
												    '<td nowrap class="TableData" width="100px">申请人：</td>'+
												    '<td class="TableData" width="50px" colspan="2"><span id="borrowerName">'+data[0].borrowerName+'</span> </td>'+
												    '<td nowrap class="TableData" width="100px">申请时间：</td>'+
												    '<td class="TableData" width="200px"><span id="transDate">'+data[0].transDate.substring(0,10)+'</span></td>'+
												  '</tr>'+
												  '<tr>'+
												    '<td nowrap class="TableData" colspan="7">申请列表：</td>'+
												  '</tr>'+
												  '<tr>'+
									          '<td nowrap class="TableData">办公用品类别：</td>'+
									          '<td nowrap class="TableData">办公用品名称：</td>'+
									          '<td nowrap class="TableData">警戒库存范围：</td>'+
									          '<td nowrap class="TableData" width="25px">单价：</td>'+
									          '<td nowrap class="TableData" width="25px">申请数量：</td>'+
									          '<td nowrap class="TableData">处理：</td>'+
									          '<td nowrap class="TableData">实际数量：</td>'+
									        '</tr>';
		var htmlTableFoot = '  <tr>'+
											      '<td nowrap class="TableData">提醒申请人：</td>'+
											      '<td class="TableData" colspan="6">'+
											         '<input type="hidden" name="smsRemind" id="smsRemind" value="">'+
											         '<input type="checkbox" name="smsRemindStr" id="smsRemindStr"><label for="smsRemind">使用内部短信提醒</label>&nbsp;'+
											      '</td>'+
											    '</tr> '+
											    '<tr>'+
											      '<td nowrap class="TableData">提醒物品调度员：</td>'+
											      '<td class="TableData" colspan="6">'+
											         '<input type="hidden" name="smsRemind1" id="smsRemind1" value="">'+
											         '<input type="checkbox" name="smsRemind1Str" id="smsRemind1Str"><label for="smsRemind1">使用内部短信提醒</label>&nbsp;'+
											      '</td>'+
											    '</tr>'+
											    '<tfoot align="center" class="TableFooter">'+
											      '<td colspan="7" nowrap>'+
											        '<input type="hidden" name="borrower" id="borrower" value="'+data[0].borrower+'">'+
											        '<input type="hidden" name="operatorId" id="operatorId" value="'+data[0].operatorId+'">'+
											        '<input type="button" value="处理" class="BigButton" onClick="doSubmit();">&nbsp;&nbsp;'+
											        '<input type="button" value="返回" class="BigButton" onClick="javascript:history.go(-1)">'+
											      '</td>'+
											    '</tfoot>'+
											  '</table>';
		var htmlStr = "";
    for(var i = 0; i < data.length; i++){
      var proStock = data[i].proStock;
      var proMaxstock = data[i].proMaxstock;
      var proLowstock = data[i].proLowstock;
      var infoStr = "";
      if(proStock*1 > proMaxstock*1){
        infoStr = "<font color=red>&nbsp;大于最高警戒库存</font>";
      }
      if(proStock*1 < proLowstock*1){
        infoStr = "<font color=red>&nbsp;小于最低警戒库存</font>";
      }
      var htmlTableBody =  '<tr>'+
          '<td class="TableData"><span id="typeName'+i+'">'+data[i].typeName+'</span></td>'+
          '<td class="TableData"><span id="proName'+i+'">'+data[i].proName+'</span> (库存:<span id="proStockStr'+i+'">'+data[i].proStock+'</span> <span id="infoStr'+i+'">'+infoStr+'</span>)</td>'+
          '<td class="TableData"><span id="proLowstock'+i+'">'+data[i].proLowstock+'</span>  至<span id="proMaxstock'+i+'">'+data[i].proMaxstock+'</span> </td>'+
          '<td class="TableData"><span id="proPrice'+i+'">'+data[i].proPrice+'</span>元</td>'+
          '<td class="TableData"><input type="hidden" id="transQty'+i+'" name="transQty'+i+'" value="'+Math.abs(data[i].transqty)+'" > <span id="transQtyStr'+i+'">'+Math.abs(data[i].transqty)+'</span> </td>'+
          '<td nowrap class="TableData">'+
            '<input type="radio" name="setPriv'+i+'" id="radioAgree'+i+'" value="1" onclick="agree('+i+');" checked><label for="radioAgree'+i+'">同意</label>&nbsp;&nbsp;'+
            '<input type="radio" name="setPriv'+i+'" id="radioDisagree'+i+'" value="2" onclick="disagree('+i+');"><label for="radioDisagree'+i+'">不同意</label>'+
          '</td>'+
          '<td nowrap class="TableData">'+
            '<div id="addPriv'+i+'">实际数量：'+
            '<input type="text" size="10" class="BigInput" name="factQty'+i+'" id="factQty'+i+'" maxlength="10" value="'+Math.abs(data[i].transqty)+'"></div>'+
            '<div id="removePriv'+i+'" style="display:none;">不同意理由：'+
            '<input type="text" size="10" class="BigInput" name="removeReason'+i+'" id="removeReason'+i+'" value=""></div>'+
            '<input type="hidden" name="proId'+i+'" id="proId'+i+'" value="'+data[i].proId+'">'+
            '<input type="hidden" name="transId'+i+'" id="transId'+i+'" value="'+data[i].transId+'">'+
            '<input type="hidden" name="transFlag'+i+'" id="transFlag'+i+'" value="'+data[i].transFlag+'">'+
            '<input type="hidden" name="proStock'+i+'" id="proStock'+i+'" value="'+data[i].proStock+'">'+
          '</td>'+
        '</tr>';
      htmlStr = htmlStr + htmlTableBody;
    }
    $('inTable').innerHTML = htmlTableHead + htmlStr + htmlTableFoot;
  }else{
    alert(rtJson.rtMsrg);
  }
}
function agree(i){
  if($('radioAgree'+i).checked){
    $("removeReason"+i).value = "";
    $("addPriv"+i).style.display="";
    $("removePriv"+i).style.display="none";
  }

}
function disagree(i){
  if($('radioDisagree'+i).checked) {
    $("factQty"+i).value = "";
    $("addPriv"+i).style.display="none";
    $("removePriv"+i).style.display="";
  }
}

function checkForm(){
	for(var i = 0; i < len; i++){
	  if($('radioAgree'+i).checked){
	    var factQty = $("factQty"+i).value;
	    if(factQty.trim() ==""){
	      alert("实际数量不能为空！");
	      $("factQty"+i).focus();
	      $("factQty"+i).select();
	      return false;
	    }
	    if(factQty.trim() =="0"){
	      alert("实际数量不能为0！");
	      $("factQty"+i).focus();
	      $("factQty"+i).select();
	      return false;
	    }
	    if(!isPositivInteger(factQty)){
	      alert("实际数量必须是正整数！");
	      $("factQty"+i).focus();
	      $("factQty"+i).select();
	      return false;
	    }
	    if(factQty.trim() <0){
	      alert("实际数量不能小于0！");
	      $("factQty"+i).focus();
	      $("factQty"+i).select();
	      return false;
	    }
	    var transFlag = $("transFlag"+i).value;
	    var proStock = $("proStock"+i).value;
	    if(transFlag =="1" || transFlag == "2"){
	      if(parseInt(factQty)>parseInt(proStock)){
	        //alert("实际数量不能大于库存数量！");
	        $("factQty"+i).focus();
	        $("factQty"+i).select();
	        return false;
	      }
	    }
	  }
	}
  return true;
}

function doSubmit(){
  if(checkForm()){
    if($('smsRemindStr').checked){
      $('smsRemind').value = 1;
    }
    if($('smsRemind1Str').checked){
      $('smsRemind1').value = 1;
    }
    var requestURI = "<%=contextPath%>/yh/subsys/oa/officeProduct/manage/act/YHOfficeTranshistoryAct";
    var pars = Form.serialize($('form1'));
    var url = requestURI + "/transHandleCycle.act";
    var rtJson = getJsonRs(url,pars);
    if(rtJson.rtState == "0"){
      $("showFormDiv").hide();
      $("remindDiv").show();
    }else{
      alert(rtJson.rtMsrg);
    }
  }
}

</script>
</head>
<body onload="doInit();">
<div id="showFormDiv">
<table border="0" width="50%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3">&nbsp;处理待批申请</span>
    </td>
  </tr>
</table>
<br>
<form enctype="" action=""  method="post" name="form1" id="form1">
<div id="inTable"></div>
</form>
</div>
<div id="remindDiv" style="display: none">
<table class="MessageBox" align="center" width="320">
  <tr>
    <td class="msg info">
      <h4 class="title">提示</h4>
      <div class="content" style="font-size:12pt">待批申请处理成功！</div>
    </td>
  </tr>
</table>
<br><center>
  <input type="button" class="BigButton" value="返回" onclick="window.location.href='<%=contextPath %>/subsys/oa/officeProduct/manage/transInfo.jsp';">
</center>
</div>


</body>
</html>
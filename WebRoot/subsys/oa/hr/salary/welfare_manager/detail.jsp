<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
	String seqId = request.getParameter("seqId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工福利详细信息</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript">
function doInit(){
	var url = "<%=contextPath%>/yh/subsys/oa/hr/salary/welfare_manager/act/YHHrWelfareManageAct/getWelfareDetail.act?seqId=<%=seqId%>";
	var rtJson = getJsonRs(url);
	if(rtJson.rtState == "0"){
		var data = rtJson.rtData;
		data.paymentDate=data.paymentDate.substr(0,10);
		data.addTime=data.addTime.substr(0,10);
		bindJson2Cntrl(data);
		$('staffName').innerHTML = staffNameFunc(data.staffName);
		$('welfareItem').innerHTML = getWelfareItem(data.welfareItem);
		$('welfarePayment').innerHTML = careFeesRMB(data.welfarePayment);
		$('taxAffares').innerHTML=taxAffaresFunc(data.taxAffares);
   }
}

function taxAffaresFunc(tax){
   if(tax=='1') return '是';
   if(tax=='0') return '否';
   else{
    return '';
   }
}


/**
 * 单位员工名称
 * @param cellData
 * @param recordIndex
 * @param columIndex
 * @return
 */
function staffNameFunc(staffname){

  var url = contextPath + "/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/getUserName.act";
  var rtJson = getJsonRs(url, "userIdStr=" + staffname);
 
  if (rtJson.rtState == "0") {
	  //alert(rtJson.rtData);
    return rtJson.rtData;
    
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function getWelfareItem(seqId){
  var urls = contextPath + "/yh/subsys/oa/hr/setting/act/YHHrCodeAct/selectCodeById.act?seqId="+seqId;
  var rtJsons = getJsonRs(urls);
  var prc = rtJsons.rtData;
  if(rtJsons.rtState == '0'){
    if(prc.codeName){
      return prc.codeName;
    }
    return "";
  }else{
    alert(rtJson.rtMsrg);
  }
}

function careFeesRMB(careFees){
	if(careFees){
		careFees = insertKiloSplit(careFees,2);
	}
  return careFees+'元';
}
</script>
</head>
<body onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath %>/meeting.gif" width="17" height="17"><span class="big3"> 员工福利详细信息</span><br>
    </td>
  </tr>
</table>

<br>
<table class="TableBlock" width="90%" align="center">
  <tr>
    <td nowrap align="left" width="120" class="TableContent">姓名：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="staffName"></div></td>
    <td nowrap align="left" width="120" class="TableContent">福利项目：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="welfareItem"></div></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">工资月份：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="welfareMonth"></div></td>
    <td nowrap align="left" width="120" class="TableContent">发放日期：</td>
    <td align="left" class="TableData" width="180"><div id="paymentDate"></div></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">福利金额：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="welfarePayment"></div></td>
    <td nowrap align="left" width="120" class="TableContent">是否纳税：</td>
    <td nowrap align="left" class="TableData" width="180"><div id="taxAffares"></div></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">发放物品：</td>
    <td nowrap align="left" class="TableData" colspan="3"><div id="freeGift"></div></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">备注：</td>
    <td nowrap align="left" class="TableData" colspan="3"><div id="remark"></div></td>
  </tr>
  <tr>
    <td nowrap align="left" width="120" class="TableContent">登记日期：</td>
    <td nowrap align="left" class="TableData" colspan="3"><div id="addTime"></div></td>
  </tr>
  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="关闭" class="BigButton" onClick="window.close();" title="关闭窗口">
    </td>
  </tr>
</table>
</body>
</html>
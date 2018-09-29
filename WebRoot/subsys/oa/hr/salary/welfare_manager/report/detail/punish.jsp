<%@ page language="java" import="java.util.*,java.text.SimpleDateFormat,yh.core.funcs.person.data.YHPerson,yh.core.funcs.calendar.data.*" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String staffName = YHUtility.encodeSpecial(YHUtility.null2Empty(request.getParameter("userId")));
  
   //获取该月第一天和左后一天  
      SimpleDateFormat curTime = new SimpleDateFormat("yyyy-MM-dd");
      Calendar   cDay1   =   Calendar.getInstance();   
      cDay1.setTime(new Date());   
      int lastDay   =   cDay1.getActualMaximum(Calendar.DAY_OF_MONTH);   
      Date   lastDate   =   cDay1.getTime();   
      lastDate.setDate(lastDay);   
    
    String date=curTime.format(lastDate);
    String startDate=date.substring(0,7)+"-00";
    String endDate=date;
  String incentiveTime1 = request.getParameter("startTime");
  String incentiveTime2 = request.getParameter("endTime");
  
  String incentiveItem ="";
  String incentiveType ="2";
  

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>惩罚信息查询 </title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/page.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffIncentive/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/hr/manage/staffIncentive/js/staffIncentiveLogic.js"></script>
<script> 
var pageMgr = null;
function doInit(){
 	var param = "";
  param = "staffName=" + encodeURIComponent("<%=staffName%>");
  param += "&incentiveTime1=" + encodeURIComponent("<%=incentiveTime1%>");
  param += "&incentiveTime2=" + encodeURIComponent("<%=incentiveTime2%>");
  param += "&incentiveItem=" + encodeURIComponent("<%=incentiveItem%>");
  param += "&incentiveType=" + encodeURIComponent("<%=incentiveType%>");
  var url = "<%=contextPath%>/yh/subsys/oa/hr/manage/act/YHHrStaffIncentiveAct/queryIncentiveListJson.act?" + param;
  var cfgs = {
      dataAction: url,
      container: "listContainer",
      sortIndex: 1,
      sortDirect: "desc",
      colums: [
         {type:"selfdef", text:"选择", width: '5%', render:checkBoxRender},
         {type:"hidden", name:"seqId", text:"顺序号", dataType:"int"},
         {type:"data", name:"staffName",  width: '20%', text:"单位员工", render:staffNameFunc},
         {type:"data", name:"incentiveItem",  width: '20%', text:"惩罚项目", render:incentiveItemFunc},
         {type:"data", name:"incentiveTime",  width: '10%', text:"惩罚日期", render:splitDateFunc},
         {type:"data", name:"incentiveType",  width: '10%', text:"惩罚属性", render:incentiveTypeFunc},
         {type:"data", name:"incentiveAmount",  width: '10%', text:"惩罚金额", render:incentiveAmountFunc},
         {type:"selfdef", text:"操作", width: '15%',render:opts1}]
    };
    pageMgr = new YHJsPage(cfgs);
    pageMgr.show();
    var total = pageMgr.pageInfo.totalRecord;
    if(total){
      showCntrl('listContainer');
      var mrs = " 共 " + total + " 条记录 ！";
      showCntrl('delOpt');
    //  showCntrl('backDiv');
    }else{
      WarningMsrg('无符合条件的惩罚信息', 'msrg');
    
    }
}

function opts1(cellData, recordIndex, columIndex){
	var seqId = this.getCellData(recordIndex,"seqId");
	return "<center>"
				+ "<a href=javascript:staffPunishDetail(" + seqId + ")>详细信息</a>&nbsp;"
				+ "</center>";
}

function staffPunishDetail(seqId){

		  var URL = contextPath + "/subsys/oa/hr/salary/report/detail/incentiveDetail.jsp?seqId=" + seqId;
      location.href=URL;
		  //newWindow(URL,'820', '500');

}

</script>
</head>
<body topmargin="5" onLoad="doInit()">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
 <tr>
   <td class="Big"><img src="<%=imgPath%>/infofind.gif" align="absMiddle"><span class="big3">&nbsp;惩罚信息查询结果 </span>
   </td>
 </tr>
</table>
<br>
<div id="listContainer" style="display:none;width:100;">
</div>
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
<div id="close"  align="center">
<br>
 
  <input type="button" value="关闭" class="BigButton" onClick="window.close();">&nbsp;&nbsp;
</div>



</body>
</html>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String type=request.getParameter("type");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>删除/恢复确认</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
    	body{
			background:#fff;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript">
	/*
		1:回款计划
		2:回款记录
	*/
		function loadDelObj(){
			var delType="${delType}";
			var innerObj="";
			//载入删除类型
			switch(parseInt(delType)){
				case 1:
					innerObj="该客户";
					break;
				/*case 2:
					innerObj="该个人客户";
					break;*/
				case 3:
					innerObj="该联系人";
					break;
					
				case 4:
					innerObj="该销售机会";
					break;
				case 5:
					innerObj="该报价记录";
					break;
				case 6:
					innerObj="该来往记录";
					break;
				case 7:
					innerObj="该客户服务";
					break;
				case 8:
					innerObj="该订单";
					break;
				/*case 9:
					innerObj="该合同";
					break;*/
				case 10:
					innerObj="该回款计划";
					break;
				case 11:
					innerObj="该回款记录";
					break;
				case 12:
					innerObj="该开票记录";
					break;
				case 13:
					innerObj="该产品";
					break;
				case 14:
					innerObj="该报告";
					break;
				case 15:
					innerObj="该工作安排";
					break;
				case 16:
					innerObj="该消息";
					break;
				case 17:
					innerObj="该入库单";
					break;
				case 18:
					innerObj="该出库单";
					break;
				case 19:
					innerObj="该仓库调拨";
					break;
				case 20:
					innerObj="该盘点记录";
					break;
				case 21:
					innerObj="该库存流水";
					break;
				case 22:
					innerObj="该项目";
					break;
				case 23:
					innerObj="该子项目";
					break;
				case 24:
					innerObj="该项目工作";
					break;
				case 25:
					innerObj="该行动历史";
					break;
				case 26:
					innerObj="该员工档案";
					break;
				case 27:
					innerObj="该供应商";
					break;
				case 28:
					innerObj="该供应商联系人";
					break;
				case 29:
					innerObj="该询价单";
					break;
				case 30:
					innerObj="该采购单";
					break;
				case 32:
					innerObj="该付款计划";
					break;
				case 33:
					innerObj="该付款记录";
					break;
				case 34:
					innerObj="该收票记录";
					break;
				
			}
			$("delObj").innerHTML=innerObj;
		}
		
    	function delConfirm(){
		    if('${type}'=='del'){
				  waitSubmit("delete","彻底删除中...");
				  waitSubmit("cancel1");
			  }
			if('${type}'=='rec'){
				waitSubmit("delete","恢复中...");
				waitSubmit("cancel1");
			}
			   
			var delType="${delType}";
			switch(parseInt(delType)){
				case 1:
				    if('${type}'=='del')
					   self.location.href="customAction.do?op=delCorCus&cusCode=${code}";
					if('${type}'=='rec')
					{//alert('${type}');
					   self.location.href="customAction.do?op=recCorCus&cusCode=${code}";
					   }
					break;
			   /* case 2:
				    if('${type}'=='del')
					   self.location.href="customAction.do?op=delPerCus&id=${code}";
					if('${type}'=='rec')
					   self.location.href="customAction.do?op=recPerCus&id=${code}";
					break;*/
			    case 3:
				    if('${type}'=='del')
					   self.location.href="customAction.do?op=delCusContact&id=${code}";
					if('${type}'=='rec')
					   self.location.href="customAction.do?op=recContact&id=${code}";
					break;
				case 4:
				    if('${type}'=='del')
					   self.location.href="cusServAction.do?op=delOpp&id=${code}";
					if('${type}'=='rec')
					   self.location.href="cusServAction.do?op=recSalOpp&id=${code}";
					break;
				case 5:
				    if('${type}'=='del')
					   self.location.href="cusServAction.do?op=delQuo&id=${code}";
					if('${type}'=='rec')
					   self.location.href="cusServAction.do?op=recQuote&id=${code}";
					break;
				case 6:
				    if('${type}'=='del')
					   self.location.href="cusServAction.do?op=delPra&id=${code}";
					if('${type}'=='rec')
					   self.location.href="cusServAction.do?op=recSalPra&id=${code}";
					break;
			    case 7:
				    if('${type}'=='del')
					   self.location.href="cusServAction.do?op=delServer&id=${code}";
					if('${type}'=='rec')
					   self.location.href="cusServAction.do?op=recServ&id=${code}";
					break;
				case 8:
				    if('${type}'=='del')
					   self.location.href="orderAction.do?op=delOrder&id=${code}";
					if('${type}'=='rec')
					   self.location.href="orderAction.do?op=recOrder&id=${code}";
					break;
				/*case 9:
				    if('${type}'=='del')
					   self.location.href="orderAction.do?op=delContract&id=${code}";
					if('${type}'=='rec')
					   self.location.href="orderAction.do?op=recContract&id=${code}";
					break;*/
				case 10:
				    if('${type}'=='del')
					   self.location.href="paidAction.do?op=delPaidPlan&id=${code}";
					if('${type}'=='rec')
					   self.location.href="paidAction.do?op=recPaidPlan&id=${code}";
					break;
			    case 11:
				    if('${type}'=='del')
					   self.location.href="paidAction.do?op=delPaidPast&id=${code}";
					if('${type}'=='rec')
					   self.location.href="paidAction.do?op=recPaidPast&id=${code}&relId=${relId}";
					break;
				case 12:
				    if('${type}'=='del')
					   self.location.href="invAction.do?op=delInvoice&id=${code}&invType=0";
					if('${type}'=='rec')
					   self.location.href="invAction.do?op=recInvoice&id=${code}&invType=0";
					break;
				case 13:
				    if('${type}'=='del')
					   self.location.href="prodAction.do?op=delProduct&id=${code}";
					if('${type}'=='rec')
					   self.location.href="prodAction.do?op=recProduct&id=${code}";
					break;
				case 14:
				    if('${type}'=='del')
					   self.location.href="messageAction.do?op=delRrepLim&id=${code}";
					if('${type}'=='rec')
					   self.location.href="messageAction.do?op=recRrepLim&id=${code}";
					break;
				case 15:
				    if('${type}'=='del')
					   self.location.href="salTaskAction.do?op=deleteSalTask&id=${code}";
					if('${type}'=='rec')
					   self.location.href="salTaskAction.do?op=recSalTask&id=${code}";
					break;
				case 16:
				    if('${type}'=='del')
					   self.location.href="messageAction.do?op=delEmail&id=${code}";
					if('${type}'=='rec')
					   self.location.href="messageAction.do?op=recEmail&id=${code}";
					break;
				case 17:
				    if('${type}'=='del')
					   self.location.href="wmsManageAction.do?op=delWarIn&id=${code}";
					if('${type}'=='rec')
					   self.location.href="wmsManageAction.do?op=recWarIn&id=${code}";
					break;
				case 18:
				    if('${type}'=='del')
					   self.location.href="wwoAction.do?op=delWarOut&id=${code}";
					if('${type}'=='rec')
					   self.location.href="wwoAction.do?op=recWarOut&id=${code}";
					break;
			    case 19:
				    if('${type}'=='del')
					   self.location.href="wwoAction.do?op=delWmsChange&id=${code}";
					if('${type}'=='rec')
					   self.location.href="wwoAction.do?op=recWmsChange&id=${code}";
					break;
			    case 20:
				    if('${type}'=='del')
					   self.location.href="wwoAction.do?op=delWmsCheck&id=${code}";
					if('${type}'=='rec')
					   self.location.href="wwoAction.do?op=recWmsCheck&id=${code}";
					break;
			    case 21:
				    if('${type}'=='del')
					   self.location.href="wwoAction.do?op=delWmsLine&id=${code}";
					if('${type}'=='rec')
					   self.location.href="wwoAction.do?op=recWmsLine&id=${code}";
					break;
				case 22:
				    if('${type}'=='del')
					   self.location.href="projectAction.do?op=delPro&id=${code}";
					if('${type}'=='rec')
					   self.location.href="projectAction.do?op=recProject&id=${code}";
					break;
			   case 23:
				    if('${type}'=='del')
					   self.location.href="projectAction.do?op=delStage&id=${code}";
					if('${type}'=='rec')
					   self.location.href="projectAction.do?op=recSubPro&id=${code}";
					break;
			   case 24:
				    if('${type}'=='del')
					   self.location.href="projectAction.do?op=deleteProTask&id=${code}";
					if('${type}'=='rec')
					   self.location.href="projectAction.do?op=recProTask&id=${code}";
					break;
			   case 25:
				    if('${type}'=='del')
					   self.location.href="projectAction.do?op=delHisTask&id=${code}";
					if('${type}'=='rec')
					   self.location.href="projectAction.do?op=recHisTask&id=${code}";
					break;
			   case 26:
				    if('${type}'=='del')
					   self.location.href="empAction.do?op=deleteSalEmp&id=${code}";
					if('${type}'=='rec')
					   self.location.href="empAction.do?op=recSalEmp&id=${code}";
					break;
			 case 27:
				    if('${type}'=='del')
					   self.location.href="salSupplyAction.do?op=delSup&id=${code}";
					if('${type}'=='rec')
					{//alert('${type}');
					   self.location.href="salSupplyAction.do?op=recSup&id=${code}";
					   }
					break;
			  case 28:
				    if('${type}'=='del')
					   self.location.href="salSupplyAction.do?op=delSupContact&id=${code}";
					if('${type}'=='rec')
					{//alert('${type}');
					   self.location.href="salSupplyAction.do?op=recSupCon&id=${code}";
					   }
					break;
			  case 29:
				    if('${type}'=='del')
					   self.location.href="salPurAction.do?op=delInquiry&id=${code}";
					if('${type}'=='rec')
					   self.location.href="salPurAction.do?op=recInquiry&id=${code}";
					break;
			  case 30:
				    if('${type}'=='del')
					   self.location.href="salPurAction.do?op=delSalPurOrd&id=${code}";
					if('${type}'=='rec')
					   self.location.href="salPurAction.do?op=recSalPurOrd&id=${code}";
					break;
			  case 32:
				    if('${type}'=='del')
					   self.location.href="salPurAction.do?op=delPlan&id=${code}";
					if('${type}'=='rec')
					   self.location.href="salPurAction.do?op=recPaidPlan&id=${code}";
					break;
			  case 33:
				    if('${type}'=='del')
					   self.location.href="salPurAction.do?op=delPast&id=${code}";
					if('${type}'=='rec')
					   self.location.href="salPurAction.do?op=recPaidPast&id=${code}";
					break;
			  case 34:
				    if('${type}'=='del')
					   self.location.href="invAction.do?op=delInvoice&id=${code}&invType=1";
					if('${type}'=='rec')
					   self.location.href="invAction.do?op=recInvoice&id=${code}&invType=1";
					break;
			   case 35:
			        if('${type}'=='del')
			           self.location.href="phoneListAction.do?op=delPhone&phlId=${code}";	
			        break;
			  case 36:
			        if('${type}'=='del')
			           self.location.href="caseAction.do?op=delAddress&adrId=${code}";	
			        break; 
			  case 37:
			        if('${type}'=='del')
			           self.location.href="phoneListAction.do?op=batchDelPhone&phlIds=${code}";	
			        break;         
			}
		}

	window.onload=function(){
		loadDelObj();
	}
  </script>
  </head>
  
  <body> 
  	<div id="delConfirm"> 
        <br/>
		<%if(type.equals("del")){%>
        <img style="vertical-align:middle;" src="crm/images/content/recycle.gif"/>&nbsp;&nbsp;&nbsp;确定要彻底删除<span id="delObj"></span>吗？<br/><br/>
		<%}%>
		<%if(type.equals("rec")){%>
        <img style="vertical-align:middle;" src="crm/images/content/recycle.gif"/>&nbsp;&nbsp;&nbsp;确定要恢复<span id="delObj"></span>吗？<br/><br/>
		<%}%>
        <button id="delete" class ="butSize1" onClick="delConfirm()">确定</button>		 
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <button id="cancel1" class ="butSize1" onClick="cancel()">取消</button>
    </div> 
	</body>
</html>

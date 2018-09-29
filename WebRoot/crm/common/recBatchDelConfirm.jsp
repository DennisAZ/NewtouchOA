<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>批量删除确认</title>
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
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript">
		function loadDelObj(){
			var delType="${delType}";
			var innerObj="";
			//载入删除类型
			switch(parseInt(delType)){
				case 1:
					innerObj="所选客户";
					break;
				case 2:
					innerObj="所选联系人";
					break;
				case 3:
				    innerObj="所选销售机会";
				    break;
				case 4:
					innerObj="所选来往记录";
					break;
				case 5:
					innerObj="所选订单";
					break;
				case 6:
					innerObj="所选回款计划";
					break;
				case 7:
					innerObj="所选回款记录";
					break;	
				case 8:
					innerObj="所选产品";
					break;	
				case 9:
					innerObj="所选客户服务";
					break;
				case 10:
					innerObj="所选已发报告";
					break;
				case 11:
					innerObj="所选工作安排";
					break;
				case 12:
					innerObj="所选员工档案";
					break;			
			}
			$("delObj").innerHTML=innerObj;
		}
		
    	function delConfirm(){
			waitSubmit("delete","删除中...");
			waitSubmit("cancel1");
			var delType="${delType}";
			var subForm=$("sub");//获得提交的form对象
			var op=$("op");
			var priKey=parent.document.getElementsByName("priKey");//被选中的待删除的主键数组
			var code="";
			for (var i = 0; i < priKey.length; i++) {
				if (priKey[i].checked==true)
					code += priKey[i].value + ",";
			}
			$("code").value=code;
			switch(parseInt(delType)){
				case 1:
				case 2:
				case 3:
				case 4:
					subForm.action="customAction.do";
				    op.value="recBatchDel";
					subForm.submit();
					break;
				case 5:
					subForm.action="orderAction.do";
				    op.value="recBatchDel";
					subForm.submit();
					break;
				case 6:
				case 7:
					subForm.action="paidAction.do";
				    op.value="recBatchDel";
					subForm.submit();
					break;
				case 8:
					subForm.action="prodAction.do";
				    op.value="recBatchDel";
					subForm.submit();
					break;
				case 9:
				case 10:
				case 11:
				case 12:
					subForm.action="customAction.do";
				    op.value="recBatchDel";
					subForm.submit();
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
        <form id="sub" action="" method="post" style="margin:0px">
        <input type="hidden" id="op" name="op" value="" />
        <input type="hidden" id="type" name="type" value="${type}" />
        <input type="hidden" id="code" name="code" value="" />
        <img style="vertical-align:middle;" src="crm/images/content/recycle.gif"/>&nbsp;&nbsp;&nbsp;确定要彻底删除<span id="delObj"></span>吗？<br/><br/>
        <button id="delete" class ="butSize1" onClick="delConfirm()">确定</button>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <button id="cancel1" class ="butSize1" onClick="cancel()">取消</button>
        </form>		 
        
    </div> 
	</body>
</html>

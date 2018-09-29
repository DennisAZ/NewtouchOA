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
					innerObj="所选客户服务";
					break;
				case 6:
					innerObj="所选消息";
					break;	
			    case 7:
					innerObj="所选联系人";
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
				    $("type").value='${type}';
				    subForm.action="customAction.do";
				    op.value="batchDel";
					subForm.submit();
					break;
				case 2:
				    subForm.action="customAction.do";
				    op.value="batchDelContact";
					subForm.submit();
					break;
			    case 3:
				    subForm.action="cusServAction.do";
				    op.value="batchDelSalOpp";
					subForm.submit();
					break;
				case 4:
				    subForm.action="cusServAction.do";
				    op.value="batchDelSalPra";
					subForm.submit();
					break;
				case 5:
				    subForm.action="cusServAction.do";
				    op.value="batchDelServ";
					subForm.submit();
					break;
				case 6:
				    $("type").value='${type}';
				    subForm.action="messageAction.do";
				    op.value="batchDelMess";
					subForm.submit();
					break;
				case 7:
				    subForm.action="salSupplyAction.do";
				    op.value="batchDelSupCon";
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
        <input type="hidden" id="type" name="type" value="" />
        <input type="hidden" id="code" name="code" value="" />
        <img style="vertical-align:middle;" src="crm/images/content/recycle.gif"/>&nbsp;&nbsp;&nbsp;确定要删除<span id="delObj"></span>吗？<br/><br/>
        <button id="delete" class ="butSize1" onClick="delConfirm()">确定</button>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <button id="cancel1" class ="butSize1" onClick="cancel()">取消</button>
        </form>		 
        
    </div> 
	</body>
</html>

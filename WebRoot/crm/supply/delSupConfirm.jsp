<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String delType=request.getParameter("delType");
String code=request.getParameter("code");
String isIfrm=request.getParameter("isIfrm");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>删除确认</title>
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
		function loadDelObj(){
			var delType;
			if(<%=delType%>!=null){
				delType=<%=delType%>;
			}
			else{
				delType="${delType}";
			}
			var innerObj="";
			//载入删除类型
			switch(parseInt(delType)){

				case 2:
					innerObj="该供应商联系人";
					break;
				case 11:
					innerObj="该采购单";
					break;
				case 12:
					innerObj="该询价单";
					break;
				
			}
			$("delObj").innerHTML=innerObj;
		}
		
    	function delConfirm(){
			waitSubmit("delete","删除中...");
			waitSubmit("cancel1");
			var delType;
			if(<%=delType%>!=null){
				delType=<%=delType%>;
			}
			else{
				delType="${delType}";
			}

			switch(parseInt(delType)){
				case 1:
					self.location.href="salSupplyAction.do?op=deleteSup&supId=${code}";
					break;
					
				case 2:
					self.location.href="salSupplyAction.do?op=delSupCon&id=${code}";
					break;
					
				case 11:
					self.location.href="../salPurAction.do?op=deleteSpo&spoId="+<%=code%>;
					break;
				case 12:
					var url="../salPurAction.do?op=deleteInq&inqId="+<%=code%>;
					if(<%=isIfrm%>!=null&&<%=isIfrm%>==1){
						url+="&isIfrm=1";
					}
					self.location.href=url;
					
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
        <img style="vertical-align:middle;" src="crm/images/content/recycle.gif"/>&nbsp;&nbsp;&nbsp;确定要删除<span id="delObj"></span>吗？<br/><br/>
        <button id="delete" class ="butSize1" onClick="delConfirm()">确定</button>		 
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <button id="cancel1" class ="butSize1" onClick="cancel()">取消</button>
    </div> 
	</body>
</html>

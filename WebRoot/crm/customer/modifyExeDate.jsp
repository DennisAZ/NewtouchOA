<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>修改联系日期</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <style type="text/css">
		#divBorder {
			border:#4e80c9 2px solid;
			padding:2px;
		}
		#modDate {
			padding:4px;
			height:100%;
			background:#4e80c9;
		}
		.smallBut {
			margin-top:8px;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript">
    	function savConfirm(){
    	    var praExeDate=$("pid").value;
			waitSubmit("save","保存中...");
			waitSubmit("doCancel");
			self.location.href="cusServAction.do?op=changeExeDate&id=${code}&praExeDate="+praExeDate;
		}
  </script>
  </head>
  
  <body> 
  	<div id="divBorder">
        <div id="modDate"> 
            <div class="white" align="left" style="height:30px; padding-top:5px">&nbsp;&nbsp;修改联系日期</div>
            
            <input name="praExeDate"  type="text" value="" class="Wdate inputSize2"  ondblClick="clearInput(this)" style="cursor:hand;" readonly="readonly" size="20" id="pid" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/><br/>
            
            <button id="save" class ="smallBut" onClick="savConfirm()">保存</button>		 
            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <button id="doCancel" class ="smallBut" onClick="cancel()">取消</button>
        </div>
    </div>
    <script type="text/javascript">
             $("pid").value='${praExeDate}'.substring(0,10);
         </script> 
	</body>
</html>

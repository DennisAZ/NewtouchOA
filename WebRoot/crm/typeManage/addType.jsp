<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>添加类别</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/type.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript">   
		function initType(){
			if(parent.document.getElementById("typeId")!=null){
				var typType = parent.document.getElementById("typeId").value;
				$("typType").value=typType;
			}    
		}

		function check()
		{
			var errStr = "";
			if(isEmpty("ordTypeName")){
				errStr+="- 未输入类别名称！\n";
			}
			else if(checkLength("ordTypeName",50)){
				errStr+="- 类别名称不能超过50个字！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("Submit","保存中...");
				waitSubmit("doCancel");
				$("register").submit();
			}				  
		}
		
		window.onload=function(){
			initType();
		}
	</script>
</head>

<body>
		<div class="inputDiv">
			<form id="register" name="register" method="post" action="typeAction.do">
				<input type="hidden" name="op" value="addType" />
				<input type="hidden" id="typType" name="typeList.typType"/>
				<table class="normal dashTab noBr" style="width:98%" cellspacing="0" cellpadding="0">
                	<tbody>
                    	<tr>
                            <th><span class='red'>*</span>名称：</th>
                            <td><input type="text" name="typeList.typName" id="ordTypeName" onBlur="autoShort(this,50)"/></td>
                        </tr>
                        <tr>
                            <th>是否启用：</th>
                            <td>
                                <input type="radio" name="typeList.typIsenabled" id="isE1" value="1" checked/><label for="isE1">是&nbsp;</label>
                                <input type="radio" name="typeList.typIsenabled" id="isE2" value="0"/><label for="isE2">否</label>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" style="text-align:center; border:0px">
                                <input type="button" class="butSize1" onClick="check()" id="Submit" value="保存"/>						
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <input type="button" class="butSize1" id="doCancel" value="取消" onClick="cancel()"></td>
                        </tr>
                    </tbody>
					
				</table>
			</form>
		</div>
</body>
</html>

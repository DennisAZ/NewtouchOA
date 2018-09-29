<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <title>添加国家或地区</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/type.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
   	<script type="text/javascript">       
        function check(){
			var errStr = "";
			if(isEmpty("ordTypeName")){
				errStr+="- 未输入国家或地区名称！\n";
			}
			else if(checkLength("ordTypeName",50)){
				errStr+="- 国家或地区名称不能超过50个字！\n";
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
    </script>
</head>

<body>
	<div class="inputDiv">
        <form id="register" name="register" method="post" action="customAction.do">
            <input type="hidden" name="op" value="addCountry" />
            <table class="normal dashTab noBr" style="width:98%" cellspacing="0" cellpadding="0">
            	<tbody>
                	<tr>
                        <th><span class='red'>*</span>国家或地区名称：</th>
                        <td><input type="text" name="cusArea.areName" id="ordTypeName" onBlur="autoShort(this,50)"/></td>
                    </tr>
                    <tr>
                        <th>是否启用：</th>
                        <td>
                            <input type="radio" name="cusArea.areIsenabled" id="isE1" value="1" checked/><label for="isE1">是&nbsp;</label>
                            <input type="radio" name="cusArea.areIsenabled" id="isE2" value="0"/><label for="isE2">否</label>
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" style="border:0px; text-align:center">
                            <input type="button" class="butSize1" name="button" id="Submit" value="保存"  onClick="check()"/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input type="button" class="butSize1" value="取消" id="doCancel" onClick="cancel()"></td>
                    </tr>
                </tbody>
            </table>
        </form>
		</div>
</body>
</html>

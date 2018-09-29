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
	<title>添加产品类别</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/type.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
	 <script type="text/javascript">       
		function check()
		{
			var errStr = "";
			if(isEmpty("ordTypeName")){
				errStr+="- 未输入产品类别名称！\n";
			}
			else if(checkLength("ordTypeName",50)){
				errStr+="- 产品类别名称不能超过50个字！\n";
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
        <form id="register" method="post" action="wwoAction.do">
            <input type="hidden" name="op" value="addProType" />
			<input type="hidden" name="newWpt" value="newWpt">
            <table class="normal dashTab noBr" style="width:98%" cellspacing="0" cellpadding="0">
            	<tbody>
                	<tr>
                        <th style="border-bottom:0px"><span class='red'>*</span>产品类别名称：</th>
                        <td style="border-bottom:0px"><input type="text" name="wptName" id="ordTypeName" onblur="autoShort(this,50)"/></td>
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

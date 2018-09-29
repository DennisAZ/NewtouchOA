<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>保存提成率</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript">
    	function check(){
			var errStr = "";
			if(isEmpty("psbPrice")){   
				errStr+="- 未填写未达标单价！\n"; 
			}
			if(isEmpty("psbRate")){   
				errStr+="- 未填写提成率！\n"; 
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("doSave");
				waitSubmit("doCancel");
				return $("saveForm").submit();
			}
		}
		
		//初始化表单
		function initForm(){
			<c:if test="${empty prodSalBack}">
			//新建
			disableInput('psbId');
			$("opMethod").value = "saveProdSalBack";
			</c:if>
			
			<c:if test="${!empty prodSalBack}">
			//编辑
			$("opMethod").value = "updProdSalBack";
			$("psbId").value="${prodSalBack.psbId}";
			</c:if>
		}
		
    	window.onload=function(){
			initForm();
		}
  	</script>
  </head>

<body>
	<div class="inputDiv">
	<form id="saveForm" action="prodAction.do" method="post">
			<input type="hidden" id="opMethod" name="op"/>
			<input type="hidden" id="isIfrm" name="isIfrm" />
            <input type="hidden" id="psbId" name="prodSalBack.psbId"/>
            <input type="hidden" name="prodId" value="${prodId}"/>
			<table class="dashTab inputForm single" cellpadding="0" cellspacing="0">
            	<tbody>
                	<tr>
                    	<th class="required">未达标单价：<span class='red'>*</span></th>
                        <td><input class="inputSize2" name="prodSalBack.psbPrice" id="psbPrice" type="text" onBlur="checkPrice(this)" value="${prodSalBack.psbPrice}" /></td>
                    </tr>
                    <tr class="noBorderBot">
                    	<th class="required">提成率：<span class='red'>*</span></th>
                        <td><input class="inputSize2" name="prodSalBack.psbRate" id="psbRate" type="text" onBlur="checkIsNum(this)" value="${prodSalBack.psbRate}" /></td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="2">
                        <input id="doSave" class="butSize1" type="button" value="保存" onClick="check()" />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
                        </td>
                    </tr>
                </tbody>
			</table>
		</form>
    </div>
	</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>执行客服服务</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/cus.js"></script>
    <script type="text/javascript" src="js/chooseBrow.js"></script>
	<script type="text/javascript" >
    	function check(){
			var errStr = "";
			if(isEmptyByName("serMethod")){
				errStr+="- 未选择客服方式！\n"; 
			}
			if(isEmpty("serRemark")){
				 errStr+="- 未填写处理结果！\n";
			}
			else if(checkLength("serRemark",4000)){
				errStr+="- 处理结果不能超过4000个字！\n";
			}
			if(isEmpty("soUserName")){   
				errStr+="- 未选择处理人！\n"; 
			}
			if(isEmpty("servsExeDate")){
				errStr+="- 未选择处理日期！\n"; 
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
		 	else {
				waitSubmit("save","执行中...");
				waitSubmit("doCancel");
				return $("create").submit();
			}
		}
		
		function chooseCus(){
			parent.addDivBrow(1,1);
		}
		
		window.onload=function(){
			$("servsExeDate").value = "${date}";
		}
  </script>
  </head>
  
  <body>

  <div class="inputDiv">
  	<form id="create" action="cusServAction.do" method="post">
  		<input type="hidden" name="op" value="exeServ" />
  		<input type="hidden" id="serId" name="serId" value="${serId}" />
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            <thead>
            </thead>  
            <tbody> 
                <tr>
                    <th class="requried">客服方式：<span class="red">*</span></th>
                    <td colspan="3" class="longTd">
                        <input type="radio" name="serMethod" id="med1" value="电话" /><label for="med1">电话&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <input type="radio" name="serMethod" id="med2" value="传真" /><label for="med2">传真&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <input type="radio" name="serMethod" id="med3" value="邮寄" /><label for="med3">邮寄&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <input type="radio" name="serMethod" id="med4" value="上门" /><label for="med4">上门&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <input type="radio" name="serMethod" id="med5" value="其他" /><label for="med5">其他&nbsp;</label>
                    </td>
                </tr>
                <tr>
                    <th class="requried">处理结果：<span class="red">*</span></th>
                    <td colspan="6"><textarea class="inputSize2L" rows="6" name="serRemark" id="serRemark" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>
                <tr class="noBorderBot">
					<th class="requried">处理人：<span class="red">*</span></th>
                    <td>
						<input type="hidden" name="empId" id="seNo" value="${limUser.salEmp.seNo}"/>
                     	<input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${CUR_EMP.seName}"/>" ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
					</td>
                    <th class="requried">处理日期：<span class="red">*</span></th>
                    <td>
                    	<input name="servsExeDate" id="servsExeDate" type="text" class="Wdate inputSize2" style="cursor:hand" ondblClick="clearInput(this)" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/>
                    </td>
                </tr>
                <tr class="submitTr">
                    <td colspan="4">
                    <input id="save" class="butSize1" type="button" value="保存" onClick="check()" />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
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
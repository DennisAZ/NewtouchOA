<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>新建客户服务</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript" src="crm/js/chooseBrow.js"></script>
	<script type="text/javascript" >
    	function check(){
			var errStr = "";
			if(isEmpty("servTitle")){
				 errStr+="- 未填写客户服务主题！\n";
			}
			else if(checkLength("servTitle",300)){
				errStr+="- 客户服务主题不能超过300个字！\n";
			}
			if(isEmpty("serContent")){
				 errStr+="- 未填写客服内容！\n";
			}
			else if(checkLength("serContent",4000)){
				errStr+="- 客服内容不能超过4000个字！\n";
			}
			if(isEmpty("cusName")){   
				errStr+="- 未选择客户！\n"; 
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
		 	else {
				waitSubmit("save","保存中...");
				waitSubmit("doCancel");
				return $("create").submit();
			}
		}
		
		function chooseCus(){
			parent.addDivBrow(1);
		}
  </script>
  </head>
  
  <body>

  <div class="inputDiv">
  	<form id="create" action="cusServAction.do" method="post">
  		<input type="hidden" name="op" value="saveCusServs" />
  		<!--<input type="text" id="code" name="code" value="${fileName}" />-->
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="descTitleL">主题：<span class='red'>*</span></th>
                    <th class="descTitleR" colspan="3"><input id="servTitle" class="inputSize2L" type="text" name="cusServ.serTitle" onBlur="autoShort(this,300)"/></th>
                </tr>	
            </thead>
        	<tbody>
                <tr>
                    <th class="required">对应客户：<span class='red'>*</span></th>
                    <td colspan="3" class="longTd">
                    	<input id="cusName" class="inputSize2SL lockBack" type="text" value="<c:out value="${cusCorCusInfo.corName}"/>" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                        <button class="butSize2" onClick="chooseCus()">选择</button>
                        <input type="hidden" name="cusId" id="cusId" value="${cusCorCusInfo.corCode}" />
                        <c:if test="${!empty cusCorCusInfo}">
                        <input type="hidden" name="isIfrm" value="1"/>
                        </c:if>
                    </td>
                </tr>   
            </tbody>
            <thead>
            </thead>  
            <tbody> 
                <tr>
                   <th>状态：</th>
                   <td colspan="3" class="longTd">
                        <input type="radio" name="cusServ.serState" id="sta1" value="待处理"  checked/><label for="sta1">待处理&nbsp;&nbsp;</label>
                        <input type="radio" name="cusServ.serState" id="sta2" value="已处理"><label for="sta2">已处理&nbsp;&nbsp;</label>
                   </td>
                </tr>  
                <tr>
                    <th>客服方式：</th>
                    <td colspan="3" class="longTd">
                        <input type="radio" name="cusServ.serMethod" id="med1" value="电话" /><label for="med1">电话&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <input type="radio" name="cusServ.serMethod" id="med2" value="传真" /><label for="med2">传真&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <input type="radio" name="cusServ.serMethod" id="med3" value="邮寄" /><label for="med3">邮寄&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <input type="radio" name="cusServ.serMethod" id="med4" value="上门" /><label for="med4">上门&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        <input type="radio" name="cusServ.serMethod" id="med5" value="其他" /><label for="med5">其他&nbsp;</label>
                    </td>
                </tr>
                <tr class="noBorderBot">
                    <th class="requried">客服内容：<span class="red">*</span></th>
                    <td colspan="6"><textarea class="inputSize2L" rows="6" id="serContent" name="cusServ.serContent" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>
            </tbody>
            <thead>
          	   	<tr>
                	<td colspan="4"><div>反馈信息</div></td>
                </tr>
            </thead>  
            <tbody>
                <tr class="noBorderBot">
                    <th>处理结果：</th>
                    <td colspan="6"><textarea class="inputSize2L" rows="6" name="cusServ.serRemark" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>
                <tr class="noBorderBot">
					<th>处理人：</th>
                    <td>
						<input type="hidden" name="empId" id="seNo" value=""/>
                     	<input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" value="" ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
					</td>
                    <th>处理日期：</th>
                    <td>
                    	<input name="servsExeDate"  type="text" class="Wdate inputSize2" style="cursor:hand" ondblClick="clearInput(this)" readonly="readonly" id="pid" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/>
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

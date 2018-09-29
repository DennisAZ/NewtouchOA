<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>修改客户服务</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript" src="crm/js/chooseBrow.js"></script>
	<script type="text/javascript" >
     function initForm(){
          	var serMethod ='<c:out value="${cusServ.serMethod}"/>';
          	var serState='${cusServ.serState}'
			if(serMethod == "电话"){ 
				$("method1").checked=true;
			}
			if(serMethod == "传真"){
				$("method2").checked=true;
			}
			if(serMethod == "邮寄"){
				$("method3").checked=true;
			}
			if(serMethod == "上门"){
				$("method4").checked=true;
			}
			if(serMethod == "其他"){
				$("method5").checked=true;
			}
			if(serState == "待处理"){
				$("state1").checked=true;
			}
			if(serState == "已处理"){
				$("state2").checked=true;
			}
			
			/*if("${cusServ.serCosTime}"==""){
				$("cosTime").selectIndex=0;
			}
			else{
				$("cosTime").value="${cusServ.serCosTime}";
			}*/
			dateInit("pid","${cusServ.serExeDate}");
     	}
		
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
			if(isEmpty("cusId")){   
				errStr+="- 未选择客户！\n"; 
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
		 	else {
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("edit").submit();
			}
		}
		
		function chooseCus(){
			parent.addDivBrow(1);
		}

		window.onload=function(){
			if('${cusServ}'!=null&&'${cusServ}'!=''){
				initForm();
			}
		}
  	</script>
  </head>
  
  <body>

  <div class="inputDiv">
  	<logic:notEmpty name="cusServ">
        <form id="edit" action="cusServAction.do" method="post">
            <input type="hidden" name="op" value="changeCusServInfo" />
            <input type="hidden" id="code" name="code" value="${cusServ.serCode}" />
		    <input type="hidden" name="isIfrm" value="${isIfrm}" />
            <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<thead>
                    <tr>
                        <th class="descTitleL">主题：<span class='red'>*</span></th>
                        <th class="descTitleR" colspan="3"><input id="servTitle" class="inputSize2L" type="text" name="cusServ.serTitle" value="<c:out value="${cusServ.serTitle}"/>" onBlur="autoShort(this,300)"/></th>
                    </tr>	
                </thead>
                <tbody>
                    <tr>
                        <th class="required">对应客户：<span class='red'>*</span></th>
                        <td colspan="3" class="longTd">
                            <input id="cusName" class="inputSize2SL lockBack" type="text" ondblClick="clearInput(this,'cusCode')" title="此处文本无法编辑，双击可清空文本" readonly value="<c:out value="${cusServ.cusCorCus.corName}"/>"/>&nbsp;
                            <button class="butSize2" onClick="chooseCus()">选择</button>
                            <input type="hidden" name="cusId" id="cusId" value="${cusServ.cusCorCus.corCode}" />
                        </td>
                    </tr>
            	</tbody>
                <thead>
                </thead> 
                <tbody>
                    <tr>
                       <th>状态：</th>
                       <td colspan="3" class="longTd">
                            <input id="state1" type="radio" name="cusServ.serState" value="待处理" /><label for="state1">待处理&nbsp;&nbsp;</label>
                            <input id="state2" type="radio" name="cusServ.serState" value="已处理"><label for="state2">已处理&nbsp;&nbsp;</label>
                       </td>
                    </tr>
                    <tr>
                        <th>客服方式：</th>
                        <td colspan="3" class="longTd">
                            <input id="method1" type="radio" name="cusServ.serMethod" value="电话" /><label for="method1">电话&nbsp;&nbsp;&nbsp;&nbsp;</label>
                            <input id="method2" type="radio" name="cusServ.serMethod" value="传真" /><label for="method2">传真&nbsp;&nbsp;&nbsp;&nbsp;</label>
                            <input id="method3" type="radio" name="cusServ.serMethod" value="邮寄" /><label for="method3">邮寄&nbsp;&nbsp;&nbsp;&nbsp;</label>
                            <input id="method4" type="radio" name="cusServ.serMethod" value="上门" /><label for="method4">上门&nbsp;&nbsp;&nbsp;&nbsp;</label>
                            <input id="method5" type="radio" name="cusServ.serMethod" value="其他" /><label for="method5">其他&nbsp;&nbsp;&nbsp;&nbsp;</label>
                        </td>
                    </tr>
                    <tr class="noBorderBot">
                        <th class="required">客服内容：<span class="red">*</span></th>
                        <td colspan="6"><textarea class="inputSize2L" rows="6"  id="serContent" name="cusServ.serContent" onBlur="autoShort(this,4000)">${cusServ.serContent }</textarea></td>
                    </tr>
               	</tbody>
                <thead>
                    <tr>
                        <td colspan="4"><div>反馈信息</div></td>
                    </tr>
                </thead>  
                <tbody>
                    <tr>
                        <th>处理结果：</th>
                        <td colspan="6"><textarea class="inputSize2L" rows="6" name="cusServ.serRemark" onBlur="autoShort(this,4000)">${cusServ.serRemark}</textarea></td>
                    </tr>
                    <tr  class="noBorderBot">
						<th>处理人：</th>
                        <td>
						<input type="hidden" name="empId" id="seNo" value="${cusServ.salEmp.seNo}"/>
                     	<input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${cusServ.salEmp.seName}"/>" ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
						</td>
                        <th>处理日期：</th>
                        <td>
                        	<input name="servsExeDate" type="text" class="Wdate inputSize2" style="cursor:hand" ondblClick="clearInput(this)" readonly="readonly" id="pid" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" value="${cusServ.serExeDate}"/>
                        </td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>
    </logic:notEmpty>
    <logic:empty name="cusServ">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该客户服务已被删除</div>
	</logic:empty>
    </div>
  </body>
</html>

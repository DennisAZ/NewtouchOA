<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>保存收票记录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script> 
    <script type="text/javascript" src="crm/js/chooseBrow.js"></script>
    <script type="text/javascript" src="crm/sup.js"></script>
    <script type="text/Javascript" src="core/js/cmp/select.js" ></script>
    <script type="text/Javascript" src="core/js/orgselect.js" ></script>
    <script type="text/javascript" src="core/js/sys.js"></script>
    <script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
    <script type="text/javascript">
    	function check(){
			var errStr = "";
			if(isEmpty("supName")){
				errStr+="- 未选择供应商！\n";
			}
			if(isEmpty("suiMon")){   
				errStr+="- 未填写票据金额！\n"; 
			}
			if(isEmpty("seId")){   
				errStr+="- 未选择收票人！\n"; 
			}
			if(isEmpty("suiDate")){
				errStr+="- 未选择收票日期！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("create").submit();
			}
		}
		

		//供应商选择
		function chooseSup(){
			parent.addDivBrow(21);
		}
		
		//采购单选择
		function choosePurOrd(){
			parent.addDivBrow(23);
		}
		//初始化表单
		function initForm(){
			<logic:notEmpty name="supInvoice">
				removeTime("suiDate","${supInvoice.suiDate}",1);
				$("typId").value = "${supInvoice.typeList.typId}";
				$("puoCode").value = "${supInvoice.erpPurchase.code}";
				$("puoId").value = "${supInvoice.erpPurchase.id}";
				$("seId").value = "${supInvoice.yhPerson.seqId}";
				$("soUserName").value = "<c:out value="${supInvoice.yhPerson.userName}"/>";
			</logic:notEmpty>
			<logic:empty name="supPaidPast">
				$("supId").value = parent.document.getElementById("supId").value;
				$("supName").value = parent.document.getElementById("supName").value;		
			</logic:empty>
		}
	 function doInit(){
			initTime();
		}
		function initTime(){
  			var beginTimePara = {
            inputId:'suiDate',
       <%-- property:{isHaveTime:true},--%>
            bindToBtn:'pod_dateImg'
         };
        
        new Calendar(beginTimePara);
      }
    	window.onload=function(){
			initForm();
			doInit();
		}
  	</script>
  </head>

<body>
	<div class="inputDiv">
	<form id="create" action="supAction.do" method="post">
			<input type="hidden" name="op" value="saveSupInvoice"/>
			<input type="hidden" name="isIfrm" value="${isIfrm}"/>
            <input type="hidden" name="supId" value="${supId}"/>
            <input type="hidden" name="suiId" value="${supInvoice.suiId}"/>
   			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<tbody>
					 <tr>
                        <th class="required">对应供应商：<span class='red'>*</span></th>
                        <td colspan="3" class="longTd">
	                    	<input type="hidden" name="supId" id="supId" value="${supInvoice.supplier.supId}" />
	                        <input id="supName" class="inputSize2SL inputBoxAlign lockBack" type="text" ondblClick="clearInput(this,'supId')" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${supInvoice.supplier.supName}"/>" readonly/>&nbsp;
	                        <!--  <button class="butSize2 inputBoxAlign" onClick="chooseSup()">选择</button>-->
                        </td>
                    </tr>
                    <tr>
                    	<th>对应采购单：</th>
                        <td colspan="3" class="longTd">
	                    	<input type="hidden" name="puoId" id="puoId" value="${supInvoice.purOrd.puoId}" />
	                        <input id="puoCode" class="inputSize2SL inputBoxAlign lockBack" type="text" ondblClick="clearInput(this,'supId')" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${supInvoice.purOrd.puoCode}"/>" readonly/>&nbsp;
	                        <button class="butSize2 inputBoxAlign" onClick="choosePurOrd();return false;">选择</button>
                        </td>
                    </tr>
                   <tr>
                    	<th>发票号：</th>
                        <td colspan="3"><input type="text" class="inputSize2L" name="supInvoice.suiCode" value="<c:out value="${supInvoice.suiCode}"/>" onBlur="autoShort(this,200)"/></td>
                    </tr>
                	<tr>
                    	<th class="required">票据金额：<span class='red'>*</span></th>
                        <td><input name="supInvoice.suiMon" id="suiMon" type="text" class="inputSize2" onBlur="checkPrice(this)" value="<fmt:formatNumber value='${supInvoice.suiMon}' pattern='#0.00'/>"/></td>
                        <th>票据类型：</th>
                        <td>
                        	<logic:notEmpty name="typList">
                            <select name="typId" id="typId" class="inputSize2 inputBoxAlign">
                                <option value="">请选择</option>
                                <logic:iterate id="type" name="typList">
                                    <option value="${type.typId}">${type.typName}</option>
                                </logic:iterate>
                            </select>
                            </logic:notEmpty>
                            <logic:empty name="typList">
                                <select class="inputSize2 inputBoxAlign" disabled>
                                    <option>未添加</option>
                                </select>
                            </logic:empty>
                            <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                    </tr>
                    <tr>
                        <th class="required">收票人：<span class='red'>*</span></th>
                                 <td>
                         <input type="hidden" name="seId" id="seId"> 	
        <input type="text" name="soUserName" id="soUserName" size="13" class="BigStatic" readonly>&nbsp;
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['seId', 'soUserName']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('seId').value='';$('soUserName').value='';">清空</a>
                        </td>
                  
                        <th class="required">收票日期：<span class='red'>*</span></th>
                        <td>
                         <input type="text" id="suiDate" name="suiDate" size="19" maxlength="19" class="BigInput" value="" readOnly>
        <img src="<%=imgPath%>/calendar.gif" id="pod_dateImg" align="absMiddle" border="0" style="cursor:pointer">
                        
					</td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="3" name="supInvoice.suiRemark" id="spsContent" type="text" onBlur="autoShort(this,200)" >${supInvoice.suiRemark}</textarea></td>
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
    </div>
	</body>
</html>

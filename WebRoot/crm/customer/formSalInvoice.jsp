<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>保存开票记录</title>
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
    <script type="text/Javascript" src="core/js/cmp/select.js" ></script>
    <script type="text/Javascript" src="core/js/orgselect.js" ></script>
    <script type="text/javascript" src="core/js/sys.js"></script>
    <script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
    <script type="text/javascript">
     function doInit(){
			initTime();
		}
		function initTime(){
  			var beginTimePara = {
            inputId:'sinDate',
       <%-- property:{isHaveTime:true},--%>
            bindToBtn:'pod_dateImg'
         };
        
        new Calendar(beginTimePara);
      }
    	function check(){
			var errStr = "";
			if(isEmpty("cusId")){
				errStr+="- 未选择客户！\n";
			}
			if(isEmpty("sinMon")){   
				errStr+="- 未填写票据金额！\n"; 
			}
			if(isEmpty("seId")){   
				errStr+="- 未选择开票人！\n"; 
			}
			if(isEmpty("sinDate")){
				errStr+="- 未选择开票日期！\n";
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
		
		//选择订单后的回调函数
		function selOrdCallBack(){
			disableInput("cusName");
		}
		function clearOrdInput(obj){
			clearInput(obj,'ordId');
			restoreInput("cusName,selCusButton");
		}
		
		//初始化表单
		function initForm(){
			<logic:empty name="sal">
			//新建
			disableInput('sinId');
			$("opMethod").value = "saveSalInvoice";
			$("soUserName").value="${LOGIN_USER.userName}";
			$("seId").value = "${LOGIN_USER.seqId}";
			if(parent.document.getElementById("corCode")!=null){//客户详情下新建
				$("isIfrm").value="1";
				$("cusName").value = parent.document.getElementById("cusName").value;
				$("cusId").value = parent.document.getElementById("corCode").value;
			}
			if(parent.document.getElementById("ordCode")!=null){//在详情页面下新建，默认填入订单信息，保存成功后刷新iframe
				disableInput('cusName,selCusButton');
				$("ordId").value = parent.document.getElementById("ordCode").value;
				$("ordTil").value = parent.document.getElementById("ordTil").value;
				$("cusName").value = parent.document.getElementById("ordCusName").value;
				$("cusId").value = parent.document.getElementById("ordCusCode").value;
				$("isIfrm").value="1";
				$("soUserName").value=parent.document.getElementById("ordSeName").value;
				$("seId").value = parent.document.getElementById("ordSeId").value;
			}
			</logic:empty>
			
			<logic:notEmpty name="sal">
			//编辑
			//disableInput('userCode');
			$("opMethod").value = "modSalInvoice";
			$("sinId").value = "${sal.sinId}";
			$("sinRemark").value = "<c:out value="${sal.sinRemark}"/>";
			$("cusName").value = '<c:out value="${sal.cusCorCus.corName}"/>';
			$("cusId").value = "${sal.cusCorCus.corCode}";
			$("soUserName").value = "${sal.person.userName}";
			$("seId").value = "${sal.person.seqId}";
			
		
				$("ordTil").value = "${sal.erpSalOrder.orderTitle}";
				$("ordId").value = "${sal.erpSalOrder.orderId}";
				selOrdCallBack();
			$("sinMon").value="${sal.sinMon}";
			if('${sal.sinIsrecieve}'=='0'){
				$("isInv1").checked="true";
			}
			else{
				$("isInv2").checked="true";
			}
			$("typId").value="${sal.typeList.typId}";
			removeTime("sinDate","${sal.sinDate}",1);
			if(parent.document.getElementById("corCode") !=null){//客户详情下编辑
				$("isIfrm").value="1";
			}
			if(parent.document.getElementById("ordCode")!=null){//在详情页面下，保存成功后刷新iframe
				$("isIfrm").value="1";
			}
			</logic:notEmpty>
		}
		
    	window.onload=function(){
    		$("seId").value="${LOGIN_USER.seqId}";
			$("soUserName").value="${LOGIN_USER.userName}";
			initForm();
			doInit();

		}
  	</script>
  </head>

<body>
	<div class="inputDiv">
	<form id="create" action="customAction.do" method="post">
			<input type="hidden" id="opMethod" name="op"/>
			<input type="hidden" id="isIfrm" name="isIfrm" />
            <input type="hidden" id="sinId" name="sinId"/>
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<tbody>
					 <tr>
                     	<th class="required">对应客户：<span class='red'>*</span></th>
                        <td colspan="3" class="longTd">
                        
                            <input id="cusName" class="inputSize2SL lockBack" type="text" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                            <!-- <button id="selCusButton" class="butSize2" onClick="parent.addDivBrow(1)">选择</button> -->
                            <input type="hidden" name="cusId" id="cusId" />
                        </td>
                    </tr>
                     <tr>  
                    	<th>对应订单：</th>
                        <td colspan="3" class="longTd">
                        	<logic:notEmpty name="sal">
                            <input type="text" id="ordTil" class="inputSize2SL inputBoxAlign lockBack" title="此处文本无法编辑，双击可清空文本" ondblClick="clearOrdInput(this)" readonly/>&nbsp;<button class="butSize2 inputBoxAlign" onClick="parent.addDivBrow(9,['${sal.cusCorCus.corCode}','${sal.cusCorCus.corName}']);return false;">选择</button>
                            </logic:notEmpty>
                            <logic:empty name="sal">
                             <input type="text" id="ordTil" class="inputSize2SL inputBoxAlign lockBack" title="此处文本无法编辑，双击可清空文本" ondblClick="clearOrdInput(this)" readonly/>&nbsp;<button class="butSize2 inputBoxAlign" onClick="parent.addDivBrow(9,['${cusInfo.corCode}','${cusInfo.corName}']);return false;">选择</button>
                          
                            </logic:empty>
                            <input type="hidden" id="ordId" name="ordId"/>
                        </td>
                    </tr>
                     <tr>
      
                        <th class="required">开票日期：<span class='red'>*</span></th>
                    <td class="TableData">
        <input type="text" id="sinDate" name="sinDate" size="19" maxlength="19" class="BigInput" value="" readOnly>
        <img src="<%=imgPath%>/calendar.gif" id="pod_dateImg" align="absMiddle" border="0" style="cursor:pointer">
						</td>
						                  <th class="required">开票人：<span class='red'>*</span></th>
                        <td>
                         <input type="hidden" name="seId" id="seId"> 	
        <input type="text" name="soUserName" id="soUserName" size="13" class="BigStatic" readonly>&nbsp;
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['seId', 'soUserName']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('seId').value='';$('soUserName').value='';">清空</a>
                        </td>
                      </tr>
                	<tr>
                    	<th class="required">票据金额：<span class='red'>*</span></th>
                        <td><input name="salInvoice.sinMon" id="sinMon" type="text" class="inputSize2" onBlur="checkPrice(this)"/></td>
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
                    	<th>是否收票记录：</th>
                        <td colspan="3"><input type="radio" name="salInvoice.sinIsrecieve" id="isInv1" value="0" checked/><label for="isInv1">否&nbsp;&nbsp;&nbsp;&nbsp;</label><input type="radio" name="salInvoice.sinIsrecieve" id="isInv2" value="1"/><label for="isInv2">是</label></td>
                    </tr>
                     
                   
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="3" name="salInvoice.sinRemark" id="sinRemark" type="text" onBlur="autoShort(this,200)"></textarea></td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
                        </td>
                    </tr>
                    <tr>
                        <td class="tipsTd" colspan="4">
                            <div class="tipsLayer">
                                <ul>
                                    <li>选择<span class="impt">对应订单</span>后，<span class="impt">对应客户</span>将不能修改，双击清空<span class="impt">对应订单</span>后可重新选择客户。</li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </tbody>
			</table>
		</form>
    </div>
	</body>
</html>

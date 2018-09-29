<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>修改订单</title>
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
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script> 
	<script type="text/javascript" src="crm/js/chooseBrow.js"></script>
    
    
    <script type="text/javascript">
		function initForm(){
			if($("typeId")!=null){
				$("typeId").value = "${order.salOrderType.typId}";
			}
			if($("souId")!=null){
				$("souId").value = "${order.salOrderSou.typId}";
			}
			if($("stateId")!=null){
				$("stateId").value = "${order.sodShipState.typId}";
			}
			dateInit("deadLine",'${order.sodDeadline}');
			dateInit("conDate",'${order.sodConDate}');
			dateInit("startDate",'${order.sodOrdDate}');
			dateInit("endDate",'${order.sodEndDate}');
			dateInit("sodComiteDate",'${order.sodComiteDate}');
		}
		
    	function check(){
			var errStr = "";
			if(isEmpty("ordTitle")){   
				errStr+="- 未填写订单主题！\n"; 
			}
			else if(checkLength("ordTitle",300)){
				errStr+="- 订单主题不能超过300个字！\n";
			}
			if(isEmpty("cusId")){
				errStr+="- 未选择客户！\n";
			}
			if(checkLength("ordNum",150)){
				errStr+="- 订单号不能超过150位！\n";
			}
			if($("isRepeat").value==1){
				errStr+="- 此订单号已存在！\n";
			}
			 if(isEmpty("conDate")){   
				errStr+="- 未填写签订日期！\n"; 
			}
			if(isEmpty("soUserName")){   
				errStr+="- 未选择签单人！\n"; 
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave","保存中...");
				waitSubmit("doCancel");
				return $("update").submit();
			}
		}
		
		function checkOrdNum(obj){
			if(obj != undefined){
				autoShort(obj,150);
				checkIsRepeat(obj,'orderAction.do?op=checkOrdCode&type=ord&code=','<c:out value="${order.sodNum}"/>');
			}
			else{
				autoShort($('ordNum'),150);
				checkRepeatForm(new Array('orderAction.do?op=checkOrdCode&type=ord&code=','ordNum','<c:out value="${order.sodNum}"/>'))
			}
		}
		
		window.onload=function(){
			if('${order}'!=null&&'${order}'!=""){
				initForm();
			}
		}
  	</script>
  </head>

<body>
	<div class="inputDiv">
		<logic:notEmpty name="order">
		<form id="update" action="orderAction.do" method="post">
			<input type="hidden" name="op" value="updOrder"/>
            <input type="hidden" name="type" value="1"/>
            <input type="hidden" id="isIfrm" name="isIfrm" value="${isIfrm}"/>
            
            <!-- 原订单数据 -->
            <input type="hidden" name="SalOrdCon.sodCode" value="${order.sodCode}" />
            <input type="hidden" name="oldSodNum" value="<c:out value="${order.sodNum}"/>" />
            <input type="hidden" id="isRepeat" />
        	<div id="errDiv" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此订单号在系统(包括回收站)中已存在</div>
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
				<thead>
					<tr>
						<th class="descTitleL">订单主题：<span class='red'>*</span></th>
						<th class="descTitleR" colspan="3"><input class="inputSize2L" type="text" name="SalOrdCon.sodTil" id="ordTitle" value="<c:out value="${order.sodTil}"/>" onBlur="autoShort(this,300)"/></th>
					</tr>
				</thead>
				<tbody>
                    <tr>
                        <th class="required">对应客户：<span class='red'>*</span></th>
                        <td>
                            <input id="cusName" class="inputSize2S lockBack" type="text" value="<c:out value="${order.cusCorCus.corName}"/>" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                            <button class="butSize2" onClick="parent.addDivBrow(1)">选择</button>
                            <input type="hidden" name="cusId" id="cusId" value="${order.cusCorCus.corCode}" />
                        </td>
                        <th>订单号：</th>
                        <td><input type="text" class="inputSize2" name="SalOrdCon.sodNum" id="ordNum" onBlur="checkOrdNum(this)" value="<c:out value="${order.sodNum}"/>"/></td>
                    </tr>
                    <!--<tr>
                        <th>付款方式：</th>
                        <td>
                            <select name="SalOrdCon.sodPaidMethod" id="paidMethod" class="inputSize2">
                                <option value="">请选择</option>
                                <option value="支票">支票</option>
                                <option value="现金">现金</option>
                                <option value="邮政汇款">邮政汇款</option>
                                <option value="银行电汇">银行电汇</option>
                                <option value="网上支付">网上支付</option>
                                <option value="其他">其他</option>
                            </select></td>
                         <th>客户签约人：</th>
						<td><input type="text" name="SalOrdCon.sodCusCon" value="${order.sodCusCon}" class="inputSize2" onBlur="autoShort(this,100)"/></td>
                    </tr>-->
                	<tr>
                    	<th>总金额：</th>
                        <td><input type="text" class="inputSize2" name="SalOrdCon.sodSumMon" id="sumMon" onBlur="checkPrice(this)" value="<bean:write name='order' property='sodSumMon' format='0.00'/>" /></td>
                        <th>交货期：</th>
                        <td><input name="sodComiteDate" value="" id="sodComiteDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/>
                        </td>
                    </tr>
                    <tr>
                        <th class="required">签订日期：<span class='red'>*</span></th>
                        <td><input name="conDate" value="" id="conDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/>
                        </td>
                        <th class="required">签单人：<span class='red'>*</span></th>
                        <td>
                            <input type="hidden" name="empId" id="seNo" value="${order.salEmp.seNo}"/>
                            <input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${order.salEmp.seName}"/>" ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
                            <button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
                        </td>
                    </tr>
                    <tr>
                        <th>条件与条款：</th>
                        <td colspan="3" class="noBorderBot">
                            <textarea name="SalOrdCon.sodContent" id="sodContent" class="inputSize2L" rows="5" onBlur="autoShort(this,4000)">${order.sodContent}</textarea>
                        </td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3">
                            <textarea name="SalOrdCon.sodRemark" id="ordRemark" class="inputSize2L" rows="5" onBlur="autoShort(this,4000)">${order.sodRemark}</textarea>
                        </td>
                    </tr>
				</tbody>
               	<thead>
               		<tr>
                        <td colspan="4"><div>辅助信息</div></td>
                    </tr>
            	</thead>
       			<tbody>
       				<tr>
                        <th>订单类别：</th>
                        <td>
                            <logic:notEmpty name="typeList">
                            <select name="typeId" id="typeId" class="inputSize2 inputBoxAlign">
                                <option value="">请选择</option>
                                <logic:iterate id="typeList" name="typeList">
                                    <option value="${typeList.typId}">${typeList.typName}</option>
                                </logic:iterate>
                            </select>
                            </logic:notEmpty>
                            <logic:empty name="typeList">
                                <select class="inputSize2 inputBoxAlign" disabled>
                                    <option>未添加</option>
                                </select>
                            </logic:empty>
                            <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                        <th>订单来源：</th>
                        <td>
                            <logic:notEmpty name="souList">
                            <select name="souId" id="souId" class="inputSize2 inputBoxAlign">
                                <option value="">请选择</option>
                                <logic:iterate id="souList" name="souList">
                                    <option value="${souList.typId}">${souList.typName}</option>
                                </logic:iterate>
                            </select>
                            </logic:notEmpty>
                            <logic:empty name="souList">
                                <select class="inputSize2 inputBoxAlign" disabled>
                                    <option>未添加</option>
                                </select>
                            </logic:empty>
                            <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                         </td>
                    </tr>
                	<th>开始日期：</th>
                    <td>
                        <input name="startDate" id="startDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                    <th>结束日期：</th>
                    <td>
                        <input name="endDate" id="endDate" type="text" class="inputSize2 Wdate" style="cursor:hand"
readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
					<tr class="noBorderBot">
                		<th>订单状态：</th>
                        <td>
                            <logic:notEmpty name="stateList">
                            <select name="stateId" id="stateId" class="inputSize2 inputBoxAlign">
                                <option value="">请选择</option>
                                <logic:iterate id="stateList" name="stateList">
                                    <option value="${stateList.typId}">${stateList.typName}</option>
                                </logic:iterate>
                            </select>
                            </logic:notEmpty>
                            <logic:empty name="stateList">
                                <select class="inputSize2 inputBoxAlign" disabled>
                                    <option>未添加</option>
                                </select>
                            </logic:empty>
                            <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                         </td>
                        <th>交付日期：</th>
                        <td colspan="3">
                            <input name="deadLine" value="" id="deadLine" type="text" class="inputSize2 Wdate" style="cursor:hand"  readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/>
                        </td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input id="dosave" class="butSize1" type="button" value="保存" onClick="checkOrdNum()" />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
                        </td>
                    </tr>
                    <tr>
                        <td class="tipsTd" colspan="4">
                            <div class="tipsLayer">
                                <ul>
                                    <li>①&nbsp;<span class="impt">签单人</span>为此订单的实际所有者，在订单统计和销售情况分析中将根据签单人进行统计。</li>
                                    <li>②&nbsp;保存订单后在订单详情内编辑订单明细。</li>
                                </ul>
                            </div>
                        </td>
                    </tr>
				</tbody>
			</table>
            
		</form>
        </logic:notEmpty>
	   	<logic:empty name="order">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该订单已被删除</div>
	  	</logic:empty>
	</div>
	</body>
</html>

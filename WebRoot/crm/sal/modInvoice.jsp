<%@ page language="java" pageEncoding="UTF-8"%>
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
	<title>编辑票据记录</title>
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
    
    <script type="text/javascript">
    	function check(){
			var errStr = "";
			if(isEmpty("sinCon")){   
				errStr+="- 未填写开票内容！\n"; 
			}
			if(isEmpty("invTypeId")){
				errStr+="- 未填写票据类别！\n";
			}
			
			if(isEmpty("sinMon")){   
				errStr+="- 未填写金额！\n"; 
			}
			if(isEmpty("invDate")){   
				errStr+="- 未填写开票日期！\n"; 
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
	
		function init(){
			$("invDate").value="${invInfo.sinDate}".substring(0,10);
			if($("invTypeId")!=null)
			  $("invTypeId").value="${invInfo.salInvType.typId}"
		    $("sinPlan").value="${invInfo.sinIsPlaned}"
			if('${invInfo.sinIsPaid}'=="0")
			  $("isP1").checked=true;
		    if('${invInfo.sinIsPaid}'=="1")
			  $("isP2").checked=true;
		}
		
    	window.onload=function(){
			init();
		}
  	</script>
  </head>

<body>
	<div class="inputDiv">
	 <logic:notEmpty name="invInfo">
		<form id="create" action="invAction.do" method="post">
           <input type="hidden" name="op" value="modInv"/>
            <input type="hidden" name="invId" value="${invInfo.sinId}"/>
			<table class="normal dashTab noBr" style="width:98%" cellpadding="0" cellspacing="0">
				<tbody>
            	<tr>
                	<th><span class='red'>*</span>开票内容：</th>
					<td colspan="3">
						<textarea id="sinCon" name="salInvoice.sinCon" style="width:453px" rows="1" onBlur="autoShort(this,100)">${invInfo.sinCon}</textarea>
					</td>
                </tr>
			    <logic:equal value="0" name="invoiceType">
				<tr>
                	<th>对应订单：</th>
                    <td colspan="3">
                    <span id="ordSelSpan" class="textOverflow" style="width:450px" title="${invInfo.salOrdCon.sodTil}">${invInfo.salOrdCon.sodTil}</span>&nbsp;
                    </td>
                </tr>
				</logic:equal>
				<logic:equal value="1" name="invoiceType">
				 <tr>
                	<th>对应采购单：</th>
                    <td colspan="3">
                    <span id="purSelSpan" class="textOverflow" style="width:450px" title="${invInfo.salPurOrd.spoTil}">${invInfo.salPurOrd.spoTil}</span>&nbsp;
                    </td>
                </tr>
				</logic:equal>
				<tr>
					<th><span class='red'>*</span>票据类别：</th>
					<td>
                    	<logic:notEmpty name="invType">
                    	<select name="invTypeId" id="invTypeId" class="inputSize2 inputBoxAlign">
							<option value="">请选择</option>
							<logic:iterate id="invType" name="invType">
								<option value="${invType.typId}">${invType.typName}</option>
							</logic:iterate>
						</select>
                        </logic:notEmpty>
                        <logic:empty name="invType">
                        	<select id="invTypeId" class="inputSize2 inputBoxAlign" disabled>
                                <option>未添加</option>
                            </select>
                        </logic:empty>
                    	<img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>    
                    </td>
                    <th>发票编号：</th>
					<td id="countTd"><input name="salInvoice.sinCode" type="text" class="inputSize2" value="${invInfo.sinCode}" onBlur="autoShort(this,50)"/></td>
				</tr>
				<tr>
					<th><span class='red'>*</span>金额：</th>
					<td colspan="3">
                    	<input id="sinMon" name="salInvoice.sinMon" value="<bean:write name='invInfo' property='sinMon' format='0.00'/>" type="text" class="inputSize2"  onblur="checkPrice(this)"/>
                    </td>
				</tr>
                
                <tr>
                	<th><span class='red'>*</span>开票日期：</th>
					<td>
						<input id="invDate" name="invDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/>
					</td>
                    <th>开票人：</th>
					<td><input name="salInvoice.sinResp" type="text" class="inputSize2" value="${invInfo.sinResp}" onBlur="autoShort(this,50)"/></td>
                </tr>
                
				<tr>
                	<th>
					<logic:equal value="0" name="invoiceType">是否已回款：</logic:equal>
					<logic:equal value="1" name="invoiceType">是否已付款：</logic:equal>
					</th>
                   	<td>
                    <logic:equal value="0" name="invoiceType">
                    	<input type="radio" name="salInvoice.sinIsPaid" id="isP1" value="0" checked/><label for="isP1">未回款&nbsp;</label><input type="radio" name="salInvoice.sinIsPaid" id="isP2" value="1"/><label for="isP2">已回款</label>
                    </logic:equal>
                     <logic:equal value="1" name="invoiceType">
                    	<input type="radio" name="salInvoice.sinIsPaid" id="isP1" value="0" checked/><label for="isP1">未付款&nbsp;</label><input type="radio" name="salInvoice.sinIsPaid" id="isP2" value="1"/><label for="isP2">已付款</label>
                    </logic:equal>
                    </td>
					<th>
					<logic:equal value="0" name="invoiceType">有无回款计划：</logic:equal>
					<logic:equal value="1" name="invoiceType">有无付款计划：</logic:equal>
					</th>
					<td>
					<logic:equal value="0" name="invoiceType">
					<select id="sinPlan" name="salInvoice.sinIsPlaned" class="inputSize2">
                    		<option value="无回款计划">无回款计划</option>
                            <option value="已有回款计划">已有回款计划</option>
                            <option value="已回款无须计划">已回款无须计划</option>
                    </select>
					</logic:equal>
					<logic:equal value="1" name="invoiceType">
					<select id="sinPlan" name="salInvoice.sinIsPlaned" class="inputSize2">
                    		<option value="无付款计划">无付款计划</option>
                            <option value="已有付款计划">已有付款计划</option>
                            <option value="已付款无须计划">已付款无须计划</option>
                    </select>
					</logic:equal>
                    </td>
				</tr>
                
                <tr>
                	<th style="vertical-align:top">备注：</th>
					<td colspan="3"><textarea name="salInvoice.sinRemark" style="width:453px" rows="3" onBlur="autoShort(this,4000)">${invInfo.sinRemark}</textarea></td>
				</tr>
				
				<tr>
					<td colspan="4" style="border:0px; text-align:center"><input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
					</td>
				</tr>
                </tbody>
			</table>
		</form>
	 </logic:notEmpty>
	 <logic:empty name="invInfo">
	 	<logic:equal value="0" name="invoiceType">
			<div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该开票记录已被删除</div>
		</logic:equal>
		<logic:equal value="1" name="invoiceType">
			<div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该收票记录已被删除</div>
		</logic:equal>    
	</logic:empty>
    </div>
	</body>
</html>

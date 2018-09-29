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
	<title>编辑采购单</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="crm/js/chooseBrow.js"></script>
    <script type="text/javascript">
    	function check(){
			
			var errStr = "";
			if(isEmpty("spoTil")){   
				errStr+="- 未填写采购主题！\n"; 
			}
			else if(checkLength("spoTil",300)){
				errStr+="- 采购主题不能超过300个字！\n";
			}
			if(checkLength("spoCode",150)){
				errStr+="- 采购单号不能超过150位！\n";
			}
			if($("isRepeat").value==1){
				errStr+="- 此采购单号已存在！\n";
			}
			if(isEmpty("conDate")){   
				errStr+="- 未填写采购日期！\n"; 
			}
			if(isEmpty("soUserName")){   
				errStr+="- 未填写采购单采购人！\n"; 
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
		function checkSpoCode(obj){
			if(obj != undefined){
				autoShort(obj,150);
				checkIsRepeat(obj,'salPurAction.do?op=checkSpoCode&spoCode=','${salPurOrd.spoCode}');
			}
			else{
				autoShort($('spoCode'),150);
				checkRepeatForm(new Array('salPurAction.do?op=checkSpoCode&spoCode=','spoCode','${salPurOrd.spoCode}'));
			}
		}
		
		function init(){
			if("${salPurOrd.spoIsend}"=="0"){
				$("sta1").checked="true";
			}
			if("${salPurOrd.spoIsend}"=="1"){
				$("sta2").checked="true";
			}
			if("${salPurOrd.spoIsend}"=="2"){
				$("sta3").checked="true";
			}
		}
		window.onload=function(){
			$("typeId").value='${salPurOrd.typeList.typId}';
			init();
			$("conDate").value='${salPurOrd.spoConDate}'.substring(0,10);
		}

  	</script>
  </head>

<body>
	<div class="inputDiv">
		<form id="create" action="salPurAction.do" method="post">
			<input type="hidden" name="op" value="updateSpo"/>
            <input type="hidden" id="isRepeat" />
			<input type="hidden" name="spoId"  value="${salPurOrd.spoId}"/>
			<!--<input type="hidden" name="userCode" value="${salPurOrd.limUser.userCode}" id="userCode"/>-->
        	<div id="errDiv" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此采购单号在系统(包括回收站)中已存在</div>
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<thead>
                	<th class="descTitleL">采购主题：<span class='red'>*</span></th>
                    <th class="descTitleR" colspan="3"><input onBlur="autoShort(this,300)" class="inputSize2L" type="text" name="salPurOrd.spoTil" id="spoTil" value="${salPurOrd.spoTil}" /></th>
                </thead>
                <tbody>
                	<tr>
                        <th>对应供应商：</th>
                        <td><span class="textOverflowS" title="${salPurOrd.salSupplier.ssuName}">${salPurOrd.salSupplier.ssuName}&nbsp;</span></td>
                        <th>对应项目：</th>
                        <td><span class="textOverflowS" title="${salPurOrd.project.proTitle}">${salPurOrd.project.proTitle}&nbsp;</span></td>
                    </tr>
    
                    <tr>
                        <th>采购单号：</th>
                        <td>
                            <input type="text" class="inputSize2" name="salPurOrd.spoCode" value="${salPurOrd.spoCode}" id="spoCode" onBlur="checkSpoCode(this)"/></td>
                        <th>采购单类别：</th>
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
                                <select class="inputSize2 inputBoxAlign" id="typeId" disabled>
                                    <option value="">未添加采购单类别</option>
                                </select>
                            </logic:empty>
                            <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/></td>
                    </tr>
                    
                    <tr>
                        <th>负责账号：</th>
                        <td>${salPurOrd.limUser.userSeName}&nbsp;</td>
						<th class="required">采购人：<span class='red'>*</span></th>
                        <td>
						<input type="hidden" name="empId" id="seNo" value="${salPurOrd.salEmp.seNo}"/>
                     	<input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" value="${salPurOrd.salEmp.seName}" ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
						</td>
                    </tr>
                    <tr>
                        <th>总金额：</th>
                        <td><input type="text" class="inputSize2" name="salPurOrd.spoSumMon" id="sumMon" value="<bean:write name='salPurOrd' property='spoSumMon' format='0.00'/>" onBlur="checkPrice(this)"/></td>
						<th>采购日期：<span class='red'>*</span></th>
                        <td><input name="conDate" value="" id="conDate" type="text" ondblClick="clearInput(this)" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"/></td>
					</tr>
					<tr>
                        <th>交付状态：</th>
                        <td colspan="3" class="longTd">
                            <input type="radio" name="salPurOrd.spoIsend" id="sta1" value="0" checked/><label for="sta1">未交付</label>
                            <input type="radio" name="salPurOrd.spoIsend" id="sta2" value="1"/><label for="sta2">部分交付</label>
                            <input type="radio" name="salPurOrd.spoIsend" id="sta3" value="2"/><label for="sta3">全部交付</label></td>
                    </tr>
                    <tr>
                        <th>条件与条款：</th>
                        <td colspan="3">
                            <textarea name="salPurOrd.spoContent" class="inputSize2L" rows="5" onBlur="autoShort(this,4000)">${salPurOrd.spoContent}</textarea>					
                        </td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3">
                            <textarea name="salPurOrd.spoRemark" class="inputSize2L" rows="5" onBlur="autoShort(this,4000)">${salPurOrd.spoRemark}</textarea>					
                        </td>
                    </tr>
                    
                    <tr class="submitTr">
                        <td colspan="4"><input id="dosave" class="butSize1" type="button" value="保存" onClick="checkSpoCode()" />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" /></td>
                    </tr>
                </tbody>
            	
			</table>
		</form>
        
    </div>
	</body>
</html>

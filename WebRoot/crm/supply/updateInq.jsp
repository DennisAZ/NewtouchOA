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
	<title>编辑询价单</title>
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
			if(isEmpty("inqTitle")){   
				errStr+="- 未填写询价主题！\n"; 
			}
			else if(checkLength("inqTitle",100)){
				errStr+="- 询价主题不能超过100个字！\n";
			}
			if(isEmpty("inqDate")){   
				errStr+="- 未填写询价日期！\n"; 
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
  	</script>
  </head>

<body>
	<div class="inputDiv">
		<form id="create" action="salPurAction.do" method="post">
			<input type="hidden" name="op" value="updateInq"/>
            <input type="hidden" name="inqId" value="${inquiry.inqId}"/>
			<logic:notEmpty name="isIfrm">
            	<input type="hidden" name="isIfrm" value="1"/>
            </logic:notEmpty>
			<logic:equal value="2" name="isEdit" >              
			 <input type="hidden" name="isIfrm" value="1"/>
			</logic:equal>
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<thead>
                	<tr>
                        <th class="descTitleL">询价主题：<span class='red'>*</span></th>
                        <th class="descTitleR" colspan="3"><input onBlur="autoShort(this,100)" class="inputSize2L" type="text" name="inquiry.inqTitle" id="inqTitle" value="${inquiry.inqTitle}"/></th>
                    </tr>
                </thead>
                <tbody>
                	<tr>
                        <th class="required">对应供应商：<span class='red'>*</span></th>
                        <td><span class="textOverflowS" title="${inquiry.salSupplier.ssuName}"> ${inquiry.salSupplier.ssuName}&nbsp;</span></td>
                        <th>对应项目：</th>
                        <td><span class="textOverflowS" title="${inquiry.project.proTitle}">${inquiry.project.proTitle}&nbsp;</span></td>				
                    </tr>
                    <tr>
                        <th class="required">询价日期：<span class='red'>*</span></th>
                        <td><input name="inqDate" value="" id="inqDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                        <th>询价人：</th>
                        <td>
						<input type="hidden" name="empId" id="seNo" value="${inquiry.salEmp.seNo}"/>
                     	<input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" value="${inquiry.salEmp.seName}" ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
                        </td>
                    </tr>
                    <tr>
                        <th>询价金额：</th>
                        <td colspan="3"><input type="text" class="inputSize2" name="inquiry.inqPrice" value="<bean:write name='inquiry' property='inqPrice' format='0.00'/>" onBlur="checkPrice(this)"/></td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3">
                            <textarea name="inquiry.inqRemark" class="inputSize2L" rows="3"  onblur="autoShort(this,4000)">${inquiry.inqRemark}</textarea></td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="4">
                        <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" /></td>
                    </tr>
                </tbody>
			</table>
		</form>
        <script type="text/javascript">
			removeTime("inqDate","${inquiry.inqDate}",1);
		</script>
    </div>
	</body>
</html>

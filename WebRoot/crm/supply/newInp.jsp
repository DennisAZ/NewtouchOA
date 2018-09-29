<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>创建询价单</title>
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
		/*function findSubPro(){
			//空方法，不可删。
			return null;
		}*/
    	function check(){
			
			var errStr = "";
			
			if(isEmpty("inqTitle")){   
				errStr+="- 未填写询价主题！\n"; 
			}
			else if(checkLength("inqTitle",100)){
				errStr+="- 询价主题不能超过100个字！\n";
			}
			if(isEmpty("supName")){
				errStr+="- 未选择供应商！\n";
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
			<input type="hidden" name="op" value="newInp"/>
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<thead>
                	<tr>
                        <th class="descTitleL">询价主题：<span class='red'>*</span></th>
                        <th class="descTitleR" colspan="3"><input class="inputSize2L" type="text" name="inquiry.inqTitle" id="inqTitle" onBlur="autoShort(this,100)"/></th>
                    </tr>
                </thead>
                <tbody>
                	<tr>
                    	<th class="required">对应供应商：<span class='red'>*</span></th>
                        <td>
                        <logic:equal value="sy" name="sInfo" >
                            <input id="supName" class="inputSize2S inputBoxAlign lockBack" type="text" value="" ondblClick="clearInput(this,'supId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                            <button class="butSize2 inputBoxAlign" onClick="parent.addDivBrow(7)">选择</button>
                            <input type="hidden" name="ssuId" id="supId" value="" />
                        </logic:equal>
                        <logic:equal value="sn" name="sInfo" >
                        <input type="hidden" name="ssuId" id="supId" value="${supId}" />
                        <span id="supName" class="textOverflow" title="${supName}">${supName}&nbsp;</span>
                        <input type="hidden" name="isIfrm" value="1"/>
                        </logic:equal></td>
						<th>对应项目：</th>
						<td>
						<logic:equal value="py" name="pInfo" >
						<input name="proTitle" id="proTitle" value="${proTitle}" type="text" class="inputSize2S inputBoxAlign lockBack" ondblClick="clearInput(this,'proId')" title="此处文本无法编辑，双击可清空文本" readonly>&nbsp;
                        <button class="butSize2 inputBoxAlign" onClick="parent.addDivBrow(6)">选择</button>
						<input type="hidden" name="proId"  id="proId" value=""/>
					    </logic:equal>
						<logic:equal value="pn" name="pInfo" >
						<input type="hidden" name="proId" id="proId" value="${proId}" />
                        <input type="hidden" name="isIfrm" value="1"/>
						<span id="proName" class="textOverflowS" title="${proName}">${proName}&nbsp;</span>
                        </logic:equal>
						</td>				
					</tr>
                    <tr>
                        <th class="required">询价日期：<span class='red'>*</span></th>
                        <td><input name="inqDate" value="" id="inqDate" type="text" class="inputSize2 Wdate" style="cursor:hand" readonly="readonly" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/></td>
                        <th>询价人：</th>
                        <td>
						<input type="hidden" name="empId" id="seNo" value="${limUser.salEmp.seNo}"/>
                     	<input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" value="${limUser.userSeName}" ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
						</td>
                    </tr>
                    <tr>
                        <th>询价金额：</th>
                        <td colspan="3"><input type="text" class="inputSize2" name="inquiry.inqPrice" id="sumMon" onBlur="checkPrice(this)"/></td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3">
                            <textarea name="inquiry.inqRemark" class="inputSize2L" rows="3" onBlur="autoShort(this,4000)"></textarea></td>
                    </tr>
                    
                    <tr class="submitTr">
                        <td colspan="4" >
                        <input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
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

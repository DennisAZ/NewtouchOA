<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<title>保存运费标准</title>
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
    	function check(){
			var errStr = "";
			if(isEmpty("ptrAmt")){   
				errStr+="- 未填写运费金额！\n"; 
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("doSave");
				waitSubmit("doCancel");
				return $("saveForm").submit();
			}
		}
		
		//初始化表单
		function initForm(){
			<c:if test="${empty prodTrans}">
			//新建
			disableInput('ptrId');
			$("opMethod").value = "saveProdTrans";
			getCityInfo("cou");
			</c:if>
			
			<c:if test="${!empty prodTrans}">
			//编辑
			$("opMethod").value = "updProdTrans";
			$("ptrId").value="${prodTrans.ptrId}";
			$("expId").value="${prodTrans.ptrExpTypeList.typId}";
			$("country").value="${prodTrans.ptrProv.areId}";
			getCityInfo('cou','${prodTrans.ptrCity.prvId}','${prodTrans.ptrDistrict.cityId}');
			</c:if>
		}
		
    	window.onload=function(){
			initForm();
		}
  	</script>
  </head>

<body>
	<div class="inputDiv">
	<form id="saveForm" action="prodAction.do" method="post">
			<input type="hidden" id="opMethod" name="op"/>
			<input type="hidden" id="isIfrm" name="isIfrm" />
            <input type="hidden" id="ptrId" name="prodTrans.ptrId"/>
            <input type="hidden" name="prodId" value="${prodId}"/>
			<table class="dashTab inputForm single" cellpadding="0" cellspacing="0">
            	<tbody>
                	<tr>
                    	<th>物流公司：</th>
                        <td>
                        <c:if test="${!empty expList}">
                        <select id="expId" name="expId" class="inputSize2 inputBoxAlign">
                            <option value="">请选择</option>
                            <c:forEach var="expList" items="${expList}">
                            <option value="${expList.typId}"><c:out value="${expList.typName}"/></option>
                            </c:forEach>
                        </select>
                        </c:if>
                        <c:if test="${empty expList}">
                            <select id="expId" class="inputSize2 inputBoxAlign" disabled="disabled">
                                <option>未添加</option>
                            </select>
                        </c:if>
                        <img src="images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                    </tr>
                    <tr>
                    	<th>计价单位：</th>
                        <td><input class="inputSize2" name="prodTrans.ptrUnit" id="ptrUnit" type="text" onBlur="autoShort(this,50)" value="${prodTrans.ptrUnit}" /></td>
                    </tr>
                    <tr>
                        <th>省份：</th>
                        <td>
                        	<select class="inputBoxAlign inputSize2" id="country"  name="provId" onChange="getCityInfo('cou')">
                                <c:if test="${!empty provList}">
                                	<c:forEach var="provList" items="${provList}">
                                   	<option value="${provList.areId}">${provList.areName}</option>
                                   	</c:forEach>
                                </c:if>
                            </select>
                            <img src="images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    	</td>
                    </tr>
                    <tr>
                    	<th>城市：</th>
                        <td>
                            <select class="inputBoxAlign inputSize2" id="pro" name="cityId" onChange="getCityInfo('province')">
                            </select>
                            <img src="images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>	
                        </td>
                    </tr>
                    <tr>
                        <th>区县：</th>
                        <td>
                            <select class="inputBoxAlign inputSize2" id="city" name="districtId">
                            </select>
                            <img src="images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                    </tr>
                	<tr class="noBorderBot">
                    	<th class="required">运费单价：<span class='red'>*</span></th>
                        <td><input class="inputSize2" name="prodTrans.ptrAmt" id="ptrAmt" type="text" onBlur="checkPrice(this)" value="${prodTrans.ptrAmt}" /></td>
                    </tr>
                    <tr class="submitTr">
                        <td colspan="2">
                        <input id="doSave" class="butSize1" type="button" value="保存" onClick="check()" />
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

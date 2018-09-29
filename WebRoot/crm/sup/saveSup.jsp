<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>保存供应商</title>
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
    <script type="text/javascript" src="crm/sup/sup.js"></script>
    <script type="text/javascript">
    	function check(){
			var errStr = "";
			if(isEmpty("supName")){
				errStr+="- 未填写供应商名称！\n";
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
		
		function initForm(){
			if("${supplier}"!=""){
				//$("area1Id").value='${supplier.supArea1.areId}';
				//getCityInfo(['cou','area1Id','area2Id','area3Id'],'${supplier.supArea2.prvId}','${supplier.supArea3.cityId}');
				if($("supTypeId")!=null){ $("supTypeId").value = "${supplier.supType.typId}";}
			}
			else{
				//getCityInfo("cou");
			}
		}
  	</script>
  </head>
  
  <body>
	<div class="inputDiv">
		<form id="saveForm" action="supAction.do" method="post">
			<input type="hidden" name="op" value="saveSup"/>
            <input type="hidden" name="supId" value="${supplier.supId}" />
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<tbody>
					 <tr>
                     	<th class="required">供应商名称：<span class='red'>*</span></th>
                        <td><input class="inputSize2" type="text" name="supplier.supName" id="supName" value="<c:out value="${supplier.supName}"/>" onBlur="autoShort(this,100)"/></td>
                     	<th>供应商编号：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supCode" value="<c:out value="${supplier.supCode}"/>" onBlur="autoShort(this,100)"/></td>
                    </tr>
                   <!-- <tr>
                    	<th>地区：</th>
                        <td colspan="3" class="longTd">
                            <select class="inputBoxAlign inputSize2" style="width:138px;" id="area1Id"  name="area1Id" onChange="getCityInfo(['cou','area1Id','area2Id','area3Id'])">
                                <c:if test="${!empty area1List}">
                                	<c:forEach var="area1List" items="${area1List}">
                                   	<option value="${area1List.areId}"><c:out value="${area1List.areName}"/></option>
                                   	</c:forEach>
                                </c:if>
                            </select>&nbsp;
                            <select class="inputBoxAlign inputSize2" style="width:138px;" id="area2Id" name="area2Id" onChange="getCityInfo(['province','area1Id','area2Id','area3Id'])">
                            </select>&nbsp;
                            <select class="inputBoxAlign inputSize2" style="width:138px;" id="area3Id" name="area3Id">
                            </select>
                            <img src="images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                    </tr>-->
                    <tr>
                    	<th>类别：</th>
                        <td><c:if test="${!empty supType}">
                            <select id="supTypeId" name="supTypeId" class="inputSize2 inputBoxAlign">
                                <option value=""></option>
                                <c:forEach var="supTypeObj" items="${supType}">
                                <option value="${supTypeObj.typId}"><c:out value="${supTypeObj.typName}"/></option>
                                </c:forEach>
                            </select>
                            </c:if>
                            <c:if test="${empty supType}">
                                <select class="inputSize2 inputBoxAlign" disabled="disabled">
                                    <option>未添加</option>
                                </select>
                            </c:if>
                            <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/></td>
                    	<th>联系人：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supContactMan" value="<c:out value="${supplier.supContactMan}"/>" onBlur="autoShort(this,50)"/></td>
                    </tr>
                    <tr>
                    	<th>电话：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supPhone" value="<c:out value="${supplier.supPhone}"/>" onBlur="autoShort(this,100)"/></td>
                        <th>手机：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supMob" value="<c:out value="${supplier.supMob}"/>" onBlur="autoShort(this,100)"/></td>
                    </tr>
                    <tr>
                    	<th>传真：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supFex" value="<c:out value="${supplier.supFex}"/>" onBlur="autoShort(this,100)"/></td>
                        <th>电邮：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supEmail" value="<c:out value="${supplier.supEmail}"/>" onBlur="autoShort(this,100)"/></td>
                    </tr>
                    <tr>
                    	<th>地址：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supAdd" value="<c:out value="${supplier.supAdd}"/>" onBlur="autoShort(this,1000)"/></td>
                        <th>邮编：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supZipCode" value="<c:out value="${supplier.supZipCode}"/>" onBlur="autoShort(this,50)"/></td>   
                    </tr>
                    <tr>
                     	<th>网址：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supNet" value="<c:out value="${supplier.supNet}"/>" onBlur="autoShort(this,100)"/></td>
                        <th>QQ：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supQq" value="<c:out value="${supplier.supQq}"/>" onBlur="autoShort(this,100)"/></td>
                    </tr>
                    <tr>
                    	<th>供应产品：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="5" name="supplier.supProd" onBlur="autoShort(this,4000)"><c:out value="${supplier.supProd}"/></textarea></td>
                    </tr>
                    <tr>
                    	<th>开户行：</th>
                        <td colspan="3"><input class="inputSize2L" type="text" name="supplier.supBank" value="<c:out value="${supplier.supBank}"/>" onBlur="autoShort(this,100)"/></td>
                    </tr>
                    <tr>
                    	<th>开户名称：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supBankName" value="<c:out value="${supplier.supBankName}"/>" onBlur="autoShort(this,50)"/></td>
                    	<th>开户账号：</th>
                        <td><input class="inputSize2" type="text" name="supplier.supBankCode" value="<c:out value="${supplier.supBankCode}"/>" onBlur="autoShort(this,50)"/></td>
                    </tr>
                   	<tr class="noBorderBot">
                    	<th>备注：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="5" name="supplier.supRemark" onBlur="autoShort(this,4000)"><c:out value="${supplier.supRemark}"/></textarea></td>
                    </tr>
                    <tr class="submitTr">
                    <td colspan="4">
                    <input id="doSave" class="butSize1" type="button" value="保存" onClick="check()" />
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
    <script type="text/javascript">
    initForm();
    </script>
</html>

<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>保存出库单</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script> 
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/sup/sup.js"></script>
    <script type="text/javascript">
    	function check(){
			var errStr = "";
			if(isEmpty("pouCode")){
				errStr+="- 未填写出库单号！\n";
			}
			if(isEmpty("pouDate")){
				errStr+="- 未选择出库日期！\n";
			}
			if(isEmpty("pstId")){
				errStr+="- 未选择对应库存！\n";
			}
			if(isEmpty("pouOutNum")){
				errStr+="- 未填写出库数量！\n";
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
			if("${prodOut}"!=""){
				removeTime("pouDate","${prodOut.pouDate}",1);
			}
		}
		
		//库存选择
		function chooseProdStore(){
			parent.addDivBrow(22);
		}
  	</script>
  </head>
  
  <body>
	<div class="inputDiv">
		<form id="saveForm" action="supAction.do" method="post">
			<input type="hidden" name="op" value="saveProdOut"/>
            <input type="hidden" name="pouId" value="${prodOut.pouId}" />
			<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            	<tbody>
					 <tr>
                     	<th class="required">出库单号：<span class='red'>*</span></th>
                        <td><input class="inputSize2" type="text" name="prodOut.pouCode" id="pouCode" value="<c:out value="${prodOut.pouCode}"/>" onBlur="autoShort(this,100)"/></td>
						<th class="required">出库日期：<span class='red'>*</span></th>
						<td>
						    <input name="pouDate" id="pouDate" type="text" class="inputSize2 Wdate" style="cursor:hand"
                                 readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})"  value="<c:out value="${prodOut.pouDate}"/>" />
						</td>
                    </tr>
                    <tr>
                        <th class="required">对应库存：<span class='red'>*</span></th>
                        <td>
	                    	<input type="hidden" name="pstId" id="pstId" value="${prodOut.prodStore.pstId}" />
	                        <input id="pstName" class="inputSize2S inputBoxAlign lockBack" type="text" ondblClick="clearInput(this,'pstId')" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${prodOut.prodStore.pstName}"/>" readonly/>&nbsp;
	                        <button class="butSize2 inputBoxAlign" onClick="chooseProdStore()">选择</button>
                        </td>
                        <th class="required">出库数量：<span class='red'>*</span></th>
                    	<td colspan="3">
                    		<input class="inputSize2" type="text" name="prodOut.pouOutNum" id="pouOutNum" value="<c:out value="${prodOut.pouOutNum}"/>" onBlur="checkPrice(this)"/>
                    	</td>
                    </tr>
                    <tr>
                    	<th>领料人</th>
                        <td><input class="inputSize2" type="text" name="prodOut.pouPickMan" id="pouPickMan" value="<c:out value="${prodOut.pouPickMan}"/>" onBlur="autoShort(this,50)"/></td>
                        <th>经手人：</th>
                        <td><input class="inputSize2" type="text" name="prodOut.pouRespMan" id="pouRespMan" value="<c:out value="${prodOut.pouRespMan}"/>" onBlur="autoShort(this,50)"/></td>
                    </tr>
                   	<tr class="noBorderBot">
                    	<th>备注：</th>
                        <td colspan="3"><textarea class="inputSize2L" rows="5" name="prodOut.pouRemark" onBlur="autoShort(this,4000)"><c:out value="${prodOut.pouRemark}"/></textarea></td>
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

<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>编辑来往记录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
	<script type="text/javascript" >
		
		/*function findOpp(){
	         var cusCode=$("cusId").value;
		     var url = "cusServAction.do";
			 var pars = "op=loadSalOpp&cusCode="+cusCode+"&ran=" + Math.random();
			 new Ajax.Request(url,{
			 	method: 'get',
				parameters: pars,
				onSuccess: function(transport) {
					var docXml = transport.responseXML;
					if(docXml!=null||docXml!=""){    
						var sel=$("oppId");//获得select对象
						sel.options.length=0; 
						sel.add(new Option("",""));
						var n=docXml.getElementsByTagName("option").length;
						for(var i=0;i<n;i++){
								var selOption=docXml.getElementsByTagName("option")[i];
								if(selOption!=null){
									var opValue=selOption.getAttribute("value");
									var opTxt=selOption.childNodes[0].nodeValue;     
									sel.options.add(new Option(opTxt,opValue)); //添加到options集合中
								}
						}
				   }
				},
				
				onFailure: function(transport){
					if (transport.status == 404)
						alert("您访问的url地址不存在！");
					else
						alert("Error: status code is " + transport.status);
				}	
			 });
	   	}*/
		
	 	function initForm(){
			if('${salPra}'!=""){
				createCusSel("praType","t_praType","${salPra.praType}");
				dateInit('pid','${salPra.praExeDate}');
			}
		}
	 
    	function check(){
			var errStr = "";
		  	if(isEmpty("pid")){   
				errStr+="- 未选择联系日期！\n"; 
			}
			if(isEmpty("soUserName")){   
				errStr+="- 未选择执行人！\n"; 
			}
			if(isEmpty("remark")){
				 errStr+="- 未填写联系内容！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("doSave");
				waitSubmit("doCancel");
		 		return $("edit").submit();
			 }
		}

		window.onload=function(){
			initForm();
		}
  	</script>
  </head>
  
  <body>

  <div class="inputDiv">
  	<c:if test="${!empty salPra}">
  	<form id="edit" action="cusServAction.do" method="post">
   		<input type="hidden" name="op" value="changeSalPraInfo" />
        <input type="hidden" name="salPra.praId" value="${salPra.praId}" />
        <c:if test="${type ==1}">
                <input type="hidden" name="cusId" id="cusId" value="${salPra.cusCorCus.corCode}" />
        </c:if>
        <c:if test="${type ==2}">
                <input type="hidden" name="conId" id="conId" value="${salPra.cusContact.conId}" />
        </c:if>

		<input type="hidden" id="isIfrm" name="isIfrm" value="${isIfrm}"/>
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
            <tbody>
                <!--<tr style="display:none">
                    <th class="required">对应客户：<span class='red'>*</span></th>
                    <td colspan="3" class="longTd">
                    	<input id="cusName" class="inputSize2SL lockBack" type="text" ondblClick="clearInput(this,'cusCode')" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${salPra.cusCorCus.corName}"/>" readonly />&nbsp;<button id="selCusButton" class="butSize2" onClick="parent.addDivBrow(1)">选择</button>   	
                    </td>
                </tr>-->
                <tr>
                    <th class="required">联系日期：<span class='red'>*</span></th>
                    <td><input name="praExeDate" id="pid"  type="text" class="Wdate inputSize2" style="cursor:hand" readonly="readonly" ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'})"/></td>
                    <th>联系类型：</th>
                    <td><select id="praType" name="salPra.praType" class="inputSize2"></select></td>
                </tr>
                <tr>
              		<th class="required">执行人：<span class='red'>*</span></th>
                    <td>
						<input type="hidden" name="empId" id="seNo" value="${salPra.person.seqId}"/>
                     	<input id="soUserName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${salPra.person.userName}"/>" ondblClick="clearInput(this,'seNo')" readonly/>&nbsp;
                     	<button class="butSize2" onClick="parent.addDivBrow(12)">选择</button>
					</td>
                    <th>费用：</th>
                    <td><input class="inputSize2" name="salPra.praCost" type="text" onBlur="checkPrice(this)" value="<fmt:formatNumber value='${salPra.praCost}' pattern='#0.00'/>"/></td>
                </tr>
                <tr>
	                 <c:if test="${type ==1}">
	                	<th class="required">联系人：</th>
	                     <td colspan="3"><input type="hidden" name="conId" id="conId" value="${salPra.cusContact.conId}"/>
	                     	<input id="conName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本"  ondblClick="clearInput(this,'conId')" value="<c:out value="${salPra.cusContact.conName}"/>" readonly/>&nbsp;
	                     	<button class="butSize2" onClick="parent.addDivBrow(3,${salPra.cusCorCus.corCode})">选择</button>
	                    </td>
	                </c:if>
	               <!--  <c:if test="${type ==2}">
		                <th>对应客户：</th>
	                    <td colspan="3" class="longTd"><input type="hidden" name="cusId" id="cusId" value="${salPra.cusCorCus.corCode}"/>
	                    	<input id="cusName" class="inputSize2SL lockBack" type="text" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" value="<c:out value="${salPra.cusCorCus.corName}"/>" readonly />&nbsp;
	                    	<button id="selCusButton" class="butSize2" onClick="parent.addDivBrow(1)">选择</button>   	
	                    </td>
	                </c:if>
	                 -->
                </tr>
                <tr class="noBorderBot">
                    <th class="required">联系内容：<span class="red">*</span></th>
                    <td colspan="3"><textarea  id="remark" class="inputSize2L" rows="9" name="salPra.praRemark" onBlur="autoShort(this,4000)">${salPra.praRemark}</textarea></td>
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
    </c:if>
	<c:if test="${empty salPra}">
    	<div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该来往记录已被删除</div>
	</c:if>
    </div>
  </body>
</html>

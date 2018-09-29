<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>新建来往记录</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript" src="core/js/sys.js"></script>
    <script type="text/Javascript" src="core/js/cmp/select.js" ></script>
    <script type="text/Javascript" src="core/js/orgselect.js" ></script>
    <script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
    <!--<script type="text/javascript" src="js/chooseBrow.js"></script>-->
	<script type="text/javascript" >

		function initTime(){
  			var beginTimePara = {
            inputId:'praExeDate',
            property:{isHaveTime:true},
            bindToBtn:'pod_dateImg'
         };
        
        new Calendar(beginTimePara);

      }
     	/*function findOpp(){
	         var cusId=$("cusId").value;
		     var url = "cusServAction.do";
			 var pars = "op=loadSalOpp&cusCode="+cusId+"&ran=" + Math.random();
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
	  
    	function check(){
			var errStr = "";
			
			 /*if(isEmpty("cusName")){   
				errStr+="- 未选择客户！\n"; 
			 }*/
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
		 		return $("saveForm").submit();
			 }
		 	
		}
		
		function chooseCus(){
			parent.addDivBrow(1);
		}
		
		//改变执行日期前文字
		/*function changeTxt(isExecuted){
			if(isExecuted==1)
			  {
				 $("dateText").innerHTML="联系日期：<span class='red'>*</span>";
				 $("pid").value="";
				 $("pid").onclick=function(){WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d'});}
			  }
			else
			  {
				 $("dateText").innerHTML="待联系日期：<span class='red'>*</span>";
				 $("pid").value="";
				 $("pid").onclick=function(){WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd',minDate:'%y-%M-%d'});}
			  }
		}*/
		
		function initForm(){
			createCusSel("praType","t_praType");
		}
		window.onload = function(){
		   initForm();
		   initTime();
		}
  	</script>
  </head>
  
  <body>
	<div class="inputDiv">
  	<form id="saveForm" action="cusServAction.do" method="post">
  		<input type="hidden" name="op" value="saveSalPras" />
  		<input type="hidden" id="code" name="code" value="time" />
  		<c:if test="${!empty cusInfo}">
       		<input type="hidden" name="cusId" id="cusId" value="${cusInfo.corCode}" />
        </c:if>
        <c:if test="${!empty cusContact}">
       		<input type="hidden" name="conId" id="conId" value="${cusContact.conId}" />
        </c:if>
        
        <input type="hidden" name="isIfrm" value="${isDesc}"/><!-- 保存后是否刷新iframe -->
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
               <tbody>
                <!--<tr style="display:none">
                    <th class="required">对应客户：<span class='red'>*</span></th>
                    <td colspan="3" class="longTd">
                        <input id="cusName" class="inputSize2SL inputBoxAlign lockBack" type="text" value="<c:out value="${cusInfo.corName}"/>" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                        <button class="butSize2 inputBoxAlign" onClick="chooseCus()">选择</button>
                    </td>
                </tr>-->
                <tr>
                    <th class="required">联系日期：<span class='red'>*</span></th>
                    <td>
                    <input type="text" id="pid"   name="praExeDate" size="19" maxlength="19" class="BigInput" value="${date}" readOnly>
       					 <img src="<%=imgPath%>/calendar.gif" id="pod_dateImg" align="absMiddle" border="0" style="cursor:pointer">
                    </td>
                    <th>联系类型：</th>
                    <td><select id="praType" name="salPra.praType" class="inputSize2"></select></td>
                </tr>
                <tr>
			     	<th class="required">执行人：<span class='red'>*</span></th>
                    <td>
                      <input type="hidden" name="empId" id="empId" value="${LOGIN_USER.seqId}"> 	
       				 <input type="text" name="soUserName" id="soUserName" size="13" class="BigStatic" value="${LOGIN_USER.userName}" readonly>&nbsp;
        				<a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['empId', 'soUserName']);">添加</a>
       					 <a href="javascript:;" class="orgClear" onClick="$('empId').value='';$('soUserName').value='';">清空</a>
                    </td>
                    <th>费用：</th>
                    <td><input class="inputSize2" name="salPra.praCost" type="text" onBlur="checkPrice(this)"/></td>
                </tr>
                <tr>
	                <c:if test="${!empty cusInfo}">
	                	<th class="required">联系人：</th>
	                    <td colspan="3"><input type="hidden" name="conId" id="conId" value=""/>
	                     	<input id="conName" class="inputSize2S lockBack" type="text" title="此处文本无法编辑，双击可清空文本"  ondblClick="clearInput(this,'conId')" readonly/>&nbsp;
	                     	<button onClick="parent.addDivBrow(3,${cusInfo.corCode});return false;">选择</button>
	                    </td>
	                 </c:if>
	                 <!--<c:if test="${!empty cusContact}">
	                    <th>对应客户：</th>
	                    <td colspan="3" class="longTd"><input type="hidden" name="cusId" id="cusId" value=""/>
	                        <input id="cusName" class="inputSize2SL inputBoxAlign lockBack" type="text" value="<c:out value="${cusInfo.corName}"/>" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
	                        <button class="butSize2 inputBoxAlign" onClick="chooseCus()">选择</button>
	                    </td>
	                 </c:if>
	                 -->
                </tr>
                <tr class="noBorderBot">
                    <th class="required">联系内容：<span class="red">*</span></th>
                    <td colspan="3"><textarea class="inputSize2L" rows="9" id="remark" name="salPra.praRemark" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>
                <tr class="submitTr">
                    <td colspan="4"><input id="doSave" class="butSize1" type="button" value="保存" onClick="check()" />
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

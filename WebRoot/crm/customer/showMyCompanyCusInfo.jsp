<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>客户信息修改</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
        <script type="text/javascript" src="core/js/sys.js"></script>
    <script type="text/Javascript" src="core/js/cmp/select.js" ></script>
    <script type="text/Javascript" src="core/js/orgselect.js" ></script>
    <script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
    <script language="javascript" type="text/javascript">
    	if ('${msg1}' != ""){
			alert("${msg1}");
		}

		function initForm(){
			createCusSel("corState","t_state","${cusCorCusInfo.corState}");
			createCusCb("t_color","corColorTd", "cusCorCus.corColor","${cusCorCusInfo.corColor}");
			createCusSel("corHot","t_hot","${cusCorCusInfo.corHot}");
			createCusSel("cardType","t_cardType","${cusCorCusInfo.corCardType}");
			createCusSel("corRelation","t_relation","${cusCorCusInfo.corRelation}");
			
		   	$("country").value='${cusCorCusInfo.cusArea.areId}';
		   	getManageCityInfo('cou','${cusCorCusInfo.cusProvince.prvId}','${cusCorCusInfo.cusCity.cityId}');
			if($("souId") != null){
				$("souId").value='${cusCorCusInfo.corSou.typId}';
			}
			if($("industryId")!=null){
				$("industryId").value='${cusCorCusInfo.cusIndustry.typId}';
			}
			if($("cType")!=null){
				$("cType").value='${cusCorCusInfo.cusType.typId}';
			}
			
			//判断父页面是否为全部客户
			if(parent.document.getElementById("isAllCus")!=null){
				$("isAllCus").value=parent.document.getElementById("isAllCus").value;
			}
			
			dateInit("corOnDate",'${cusCorCusInfo.corOnDate}');
		}

	   function check(){
	   		var errStr = "";
			 if(isEmpty("corName")){
				 errStr+="- 未填写客户名称！\n";
			 }
			 else if(checkLength("corName",100)){
				errStr+="- 客户名称不能超过100个字！\n";
			 }
			 if($("isRepeat").value==1){
				errStr+="- 该客户名称已存在！\n";
			 }
			 if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			 }
		 
		 	waitSubmit("dosave");
			waitSubmit("doCancel");
		 	return $("edit").submit();
		}
		
		function checkCusName(obj){
			if(obj != undefined){
				autoShort(obj,100);
				checkIsRepeat(obj,'customAction.do?op=checkIsRepeatName&cusName=','<c:out value="${cusCorCusInfo.corName}"/>');
			}
			else{
				autoShort($('corName'),100);
				checkRepeatForm(new Array('customAction.do?op=checkIsRepeatName&cusName=','corName','<c:out value="${cusCorCusInfo.corName}"/>'));
			}
			
		}
		
		window.onload=function(){
			if('${cusCorCusInfo}'!=null&&'${cusCorCusInfo}'!=""){
				initForm();
				initTime();
			}
		}
		     function initTime(){
  				var beginTimePara = {
     			inputId:'corOnDate',
                <%--      property:{isHaveTime:true},--%>
                bindToBtn:'pod_dateImg'
  };
  new Calendar(beginTimePara);
 }
  	</script>
  </head>
  
  <body>
  <div class="inputDiv">
  	 <c:if test="${!empty cusCorCusInfo}">
	<form id="edit" action="customAction.do" method="post">
    	<input type="hidden" name="op" value="updCus" />
      	<input type="hidden" name="corCode" value="${cusCorCusInfo.corCode}" />
        <input type="hidden" name="isAllCus" value=""/>
        <input type="hidden" id="isRepeat" value=""/>
        <div id="errDiv" class="errorDiv redWarn" style="display:none;">&nbsp;<img class="imgAlign" src="crm/images/content/fail.gif" alt="警告"/>&nbsp;此客户名称在系统(包括回收站)中已存在</div>
       	<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="descTitleL">客户名称：<span class='red'>*</span></th>
                    <th class="descTitleR" colspan="3"><input type="text" id="corName" class="inputSize2L" name="cusCorCus.corName" value="<c:out value="${cusCorCusInfo.corName}"/>" onBlur="checkCusName(this)"/>
                    </th>
                </tr>
            </thead>
            <tbody>
            	<tr>
                	<th>助记简称：</th>
                    <td><input class="inputSize2" type="text" id="corMne" name="cusCorCus.corMne" value="<c:out value="${cusCorCusInfo.corMne}"/>" onBlur="autoShort(this,20)"/></td>
                	<th>客户编号：</th>
                    <td><input class="inputSize2" type="text" name="cusCorCus.corNum" value="<c:out value="${cusCorCusInfo.corNum}"/>" onBlur="autoShort(this,50)"/></td>
                </tr>
            </tbody>
            <thead>
            </thead>
            <tbody>
                <tr>
                    <th>负责人：</th>
                    
                    <td>
                    
                    <input type="hidden" name="seNo" id="seNo" value="${cusCorCusInfo.person.seqId}"> 	
        <input type="text" name="soUserName" id="soUserName" size="13" class="BigStatic" value="<c:out value="${cusCorCusInfo.person.userName}"/>" readonly>&nbsp;
        <a href="javascript:;" class="orgAdd" onClick="selectSingleUser(['seNo', 'soUserName']);">添加</a>
        <a href="javascript:;" class="orgClear" onClick="$('seNo').value='';$('soUserName').value='';">清空</a>
                     </td>
                    <th>客户状态：</th>
                    <td><select id="corState" name="cusCorCus.corState" class="inputSize2"></select></td>
                </tr>
            	<tr>
                	<th>客户类型：</th>
                    <td>
                        <c:if test="${!empty cusTypeList}">
                        <select name="cType" id="cType" class="inputSize2 inputBoxAlign">
                            <option  value="">请选择</option>
                            <c:forEach var="cusType" items="${cusTypeList}" >
                            <option value="${cusType.typId}">${cusType.typName}</option>
                            </c:forEach>
                        </select>
                        </c:if>
                        <c:if test="${empty cusTypeList}">
                            <select id="cType" class="inputSize2 inputBoxAlign" disabled>
                                <option>未添加</option>
                            </select>
                        </c:if>
                        <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    </td>
                    <th>客户级别：</th>
                    <td><select id="corHot" name="cusCorCus.corHot" class="inputSize2"><option value="">请选择</option></select></td>
               	</tr>
               	<tr>
                    <th>标色：</th>
                    <td id="corColorTd">&nbsp;</td>
                    <th>关系等级：</th>
                    <td><select id="corRelation" name="cusCorCus.corRelation" class="inputSize2"></select></td>
                </tr>
                <tr>
                    <th>最高余额：</th>
                    <td><input type="text" class="inputSize2" name="cusCorCus.corRecvMax" onBlur="checkPrice(this)" value="<fmt:formatNumber value='${cusCorCusInfo.corRecvMax}' pattern='#0.00'/>"/></td>
                    <th>最低月销售额：</th>
                    <td><input type="text" class="inputSize2" name="cusCorCus.corSaleNum" onBlur="checkPrice(this)" value="<fmt:formatNumber value='${cusCorCusInfo.corSaleNum}' pattern='#0.00'/>"/></td>
                </tr>
                <tr>
                    <th>账龄（天）：</th>
                    <td><input type="text" class="inputSize2" name="cusCorCus.corAging" onBlur="checkIsNum(this)" value="${cusCorCusInfo.corAging}" /></td>
                    <th>到期日期：</th>
                    <td>
                            <input type="text" id="corOnDate" name="corOnDate" size="19" maxlength="19" class="BigInput" value="" readOnly>
        <img src="<%=imgPath%>/calendar.gif" id="pod_dateImg" align="absMiddle" border="0" style="cursor:pointer" >
                   </td>
                </tr>
                <tr>
                    <th>证件类型：</th>
                    <td><select id="cardType" name="cusCorCus.corCardType" class="inputSize2"><option value="">请选择</option></select>
                    </td>
                    <th>证件号码：</th>
                    <td><input class="inputSize2" type="text" name="cusCorCus.corCardNum" value="${cusCorCusInfo.corCardNum}" onBlur="autoShort(this,25)"/></td>
            	</tr>
            	<tr class="noBorderBot">
                    <th>客户简介：</th>
                    <td colspan="3"><textarea class="inputSize2L" id="corComInf" name="cusCorCus.corComInf" rows="5" onBlur="autoShort(this,4000)"><c:out value="${cusCorCusInfo.corComInf}"/></textarea></td>
                </tr>
			</tbody>
            <thead>
                <tr>
                    <td colspan="4"><div>联系方式</div></td>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <th>所在地区：</th>
                    <td colspan="3" class="longTd">
                        <select class="inputBoxAlign" id="country"  name="countryId" style="width:135px" onChange="getManageCityInfo('cou')">
                            <c:if test="${!empty cusAreaList}">
                               <c:forEach var="cusAreaList" items="${cusAreaList}">
                               <option value="${cusAreaList.areId}">${cusAreaList.areName}</option>
                               </c:forEach>
                            </c:if>
                        </select>&nbsp;
                        <select class="inputBoxAlign" id="pro" name="provinceId" style="width:135px" onChange="getManageCityInfo('province')">
                        </select>&nbsp;
                        <select class="inputBoxAlign" id="city" name="cityId" style="width:135px" >
                        </select>
                        <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    </td>
                </tr>
                <tr>
                    <th>地址：</th>
                    <td colspan="3"><input class="inputSize2L" type="text" name="cusCorCus.corAddress" value="<c:out value="${cusCorCusInfo.corAddress}"/>"  onblur="autoShort(this,1000)"/></td>
                </tr>
                <tr>
                    <th>电话：</th>
                    <td>
                      <input class="inputSize2" type="text"  name="cusCorCus.corPhone" value="<c:out value="${cusCorCusInfo.corPhone}"/>" onBlur="autoShort(this,25)"/>
                    </td>
                    <th>手机：</th>
                    <td>
                      <input class="inputSize2" type="text"  name="cusCorCus.corCellPhone" value="<c:out value="${cusCorCusInfo.corCellPhone}"/>" onBlur="autoShort(this,25)"/>
                    </td>
                </tr>
                <tr>	
                	<th>传真：</th>
                    <td><input class="inputSize2" type="text" name="cusCorCus.corFex" value="<c:out value="${cusCorCusInfo.corFex}"/>" onBlur="autoShort(this,25)"/></td>
                    <th>邮编：</th>
                    <td><input class="inputSize2" type="text" name="cusCorCus.corZipCode" value="<c:out value="${cusCorCusInfo.corZipCode}"/>"  onblur="autoShort(this,25)"/></td>
                </tr>
                <tr>
                	<th>网址：</th>
                    <td colspan="3" class="longTd">http://<input class="inputSize2L" style="width:394px;" type="text" name="cusCorCus.corNet" value="<c:out value="${cusCorCusInfo.corNet}"/>" onBlur="autoShort(this,250)"/></td>
                </tr>
                <tr class="noBorderBot">
                    <th>电子邮件：</th>
                    <td>
                      <input class="inputSize2" type="text" name="cusCorCus.corEmail" value="<c:out value="${cusCorCusInfo.corEmail}"/>" onBlur="autoShort(this,100)"/>
                    </td>
                    <th>QQ：</th>
                    <td>
                      <input class="inputSize2" type="text"  name="cusCorCus.corQq" value="<c:out value="${cusCorCusInfo.corQq}"/>" onBlur="autoShort(this,25)"/>
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
                	<th>行业：</th>
                    <td>
                        <c:if test="${!empty industryList}">
                        <select id="industryId" name="industryId" class="inputSize2 inputBoxAlign">
                            <option value="">请选择</option>
                               <c:forEach var="industryList" items="${industryList}">
                               <option value="${industryList.typId}">${industryList.typName}</option>
                               </c:forEach>
                        </select>
                        </c:if>
                        <c:if test="${empty industryList}">
                            <select id="industryId" class="inputSize2 inputBoxAlign" disabled>
                                <option>未添加</option>
                            </select>
                        </c:if>
                        <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    </td>
				    <th>来源：</th>
                    <td>
                    	<c:if test="${!empty souList}">
                        <select id="souId" name="souId" class="inputSize2 inputBoxAlign">
                            <option value="">请选择</option>
                               <c:forEach var="souList" items="${souList}">
                               <option value="${souList.typId}">${souList.typName}</option>
                               </c:forEach>
                        </select>
                        </c:if>
                        <c:if test="${empty souList}">
                            <select class="inputSize2 inputBoxAlign" disabled>
                                <option>未添加</option>
                            </select>
                        </c:if>
                        <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    </td>
				</tr>
				<tr>
                	<th>人员规模：</th>
                    <td><input class="inputSize2" type="text" name="cusCorCus.corPerSize" onBlur="autoShort(this,50)" value="<c:out value="${cusCorCusInfo.corPerSize}"/>"/></td>
                    <th>常用发货方式：</th>
                	<td><input class="inputSize2" name="cusCorCus.corSendType" value="<c:out value="${cusCorCusInfo.corSendType}" />"  onBlur="autoShort(this,100)"/></td>
				</tr>
                <tr>
                	<th>税号：</th>
                	<td><input class="inputSize2" type="text" name="cusCorCus.corTaxNum" value="<c:out value="${cusCorCusInfo.corTaxNum}" />" onBlur="autoShort(this,100)"></td>
                	<th>开户行及账号：</th>
					<td><input class="inputSize2" type="text" name="cusCorCus.corBankCode" value="<c:out value="${cusCorCusInfo.corBankCode}"/>" onBlur="autoShort(this,100)"/></td>
                </tr>
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td colspan="6"><textarea class="inputSize2L" id="corRemark" name="cusCorCus.corRemark" rows="4" onBlur="autoShort(this,4000)"><c:out value="${cusCorCusInfo.corRemark}"/></textarea></td>
                </tr>
                <tr class="submitTr">
                	<td colspan="4">
                    <input id="dosave" class="butSize1" type="button" value="保存" onClick="checkCusName()" />
               		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
              		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                	<input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
                    </td>
                </tr>
                <tr>
                    <td class="tipsTd" colspan="4">
                        <div class="tipsLayer">
                            <ul>
                                <li>&nbsp;使用<span class="impt">星标标记</span>便于快速查找客户，可以直接在客户列表点亮星标。</li>
                            </ul>
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
	</form>
     </c:if>
	 <c:if test="${empty cusCorCusInfo}">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该客户已被删除</div>
	</c:if>
	</div>
  </body>
</html>

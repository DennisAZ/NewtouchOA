<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
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
    <title>新建日程</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
   <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
   <link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
   <script type="text/javascript" src="crm/js/prototype.js"></script>
   <script type="text/javascript" src="crm/js/common.js"></script>
   <script type="text/javascript" src="crm/js/formCheck.js"></script>
   <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
       <script type="text/javascript" src="core/js/sys.js"></script>
   <script  type="text/javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
    <script type="text/javascript" >
        function doInit(){
			initTime();
		}
		function initTime(){
  			var beginTimePara = {
            inputId:'schStartDate',
       <%-- property:{isHaveTime:true},--%>
            bindToBtn:'pod_dateImg'
         };
         var beginTimePara2 = {
            inputId:'schedule.schStartTime',
            property:{isHaveTime:true},
            bindToBtn:'pod_dateImg2'
         };
         var beginTimePara3 = {
            inputId:'schedule.schEndTime',
            property:{isHaveTime:true},
            bindToBtn:'pod_dateImg3'
         };
        new Calendar(beginTimePara);
        new Calendar(beginTimePara2);
        new Calendar(beginTimePara3);
      }
    	function check(){
			var errStr = "";
			if(isEmpty("schStartDate")){
				errStr+="- 未选择日期！\n";
			} 
			if(isEmpty("schTitle")){
				errStr+="- 未填写日程内容！\n";
			}
			else if(checkLength("schTitle",100)){
				errStr+="- 日程内容不能超过100个字！\n";
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
		
		function chooseCus(){
			parent.addDivBrow(1);
		}
    </script>
  </head>
  
  <body onload="doInit()">
    <div class="inputDiv">
  	<form id="create" action="messageAction.do" method="post">
   		<input type="hidden" name="op" value="saveSchedule" />
   		<input type="hidden" name="isIfrm" value="${isIfrm}" />
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<tbody>
        		<tr>
        			<th>对应客户：</th>
                    <td colspan="3" class="longTd">
                    	<input type="hidden" name="cusId" id="cusId" value="${cusInfo.corCode}" />
                        <input id="cusName" class="inputSize2SL inputBoxAlign lockBack" type="text" value="<c:out value="${cusInfo.corName}"/>" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                        <!-- <button class="butSize2 inputBoxAlign" onClick="chooseCus()">选择</button> -->
                    </td>
        		</tr>
            	<tr>
                    <th class="required">日期：<span class='red'>*</span></th>
                    						<td class="TableData">
        <input type="text" id="schStartDate" name="schStartDate" size="19" maxlength="19" class="BigInput" value="" readOnly>
        <img src="<%=imgPath%>/calendar.gif" id="pod_dateImg" align="absMiddle" border="0" style="cursor:pointer">
						</td>
                   
                    <th>日程类别：</th>
                    <td> 
                        <logic:notEmpty name="schTypeList">
                        <select name="scheduleType" id="schType" class="inputSize2 inputBoxAlign">
                            <option value="">请选择</option>
                              <logic:iterate id="schTypeList" name="schTypeList">
                              <option value="${schTypeList.typId}">${schTypeList.typName}</option>
                              </logic:iterate>
                        </select>
                        </logic:notEmpty>
                        <logic:empty name="schTypeList">
                             <select id="schType" class="inputSize2 inputBoxAlign" disabled>
                                <option>未添加</option>
                            </select>
                         </logic:empty>
                        <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    </td>
                </tr>
                <tr>
                    <th>开始时间：</th>
                   <td class="TableData">
        				<input type="text" id="schedule.schStartTime" name="schedule.schStartTime" size="19" maxlength="19" class="BigInput" value="" readOnly>
       					 <img src="<%=imgPath%>/calendar.gif" id="pod_dateImg2" align="absMiddle" border="0" style="cursor:pointer">
					</td>
                    <th> 结束时间：</th>
                    <td class="TableData">
        				<input type="text" id="schedule.schEndTime" name="schedule.schEndTime" size="19" maxlength="19" class="BigInput" value="" readOnly>
       					 <img src="<%=imgPath%>/calendar.gif" id="pod_dateImg3" align="absMiddle" border="0" style="cursor:pointer">
					</td>

                </tr> 
                <tr class="noBorderBot">
                    <th class="required">日程内容：<span class='red'>*</span></th>
                    <td colspan="3">
                        <textarea id="schTitle" name="schedule.schTitle" class="inputSize2L" rows="9" onBlur="autoShort(this,100)"></textarea>
                    </td>
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
    </div>
  </body>
</html>

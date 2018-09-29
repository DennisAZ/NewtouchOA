<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>修改日程安排</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" >
		function showSelect(){
			if($("schType")!=null){
				if("${schedule.schType.typName}"=="")
					$("schType").selectIndex=0;
				else
					$("schType").value="${schedule.schType.typId}";
			}
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
				 return $("edit").submit();
			}
		}
		
		function chooseCus(){
			parent.addDivBrow(1);
		}
		
		window.onload=function(){
			if('${schedule}'!=null&&'${schedule}'!=""){
				showSelect();
			}
		}
    </script>
  </head>
  
  <body>

  <div class="inputDiv">
  	<logic:notEmpty name="schedule">
  	<form id="edit" action="messageAction.do" method="post">
        <input type="hidden" name="op" value="changeSchedule" />
        <input type="hidden" name="schId" value="${schedule.schId}"/>
        <input type="hidden" name="isIfrm" value="${isIfrm}" />
        <table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<tbody>
        	<tr>
        		<th>对应客户：</th>
        		<td colspan="3" class="longTd">
                    	<input type="hidden" name="cusId" id="cusId" value="${schedule.cusCorCus.corCode}" />
                        <input id="cusName" class="inputSize2SL inputBoxAlign lockBack" type="text" value="<c:out value="${schedule.cusCorCus.corName}"/>" ondblClick="clearInput(this,'cusId')" title="此处文本无法编辑，双击可清空文本" readonly/>&nbsp;
                        <button class="butSize2 inputBoxAlign" onClick="chooseCus()">选择</button>
        		</td>
        	</tr>
            <tr>
            	<th class="required">日期：<span class='red'>*</span></th>
                <td>
                   <input name="schStartDate"  type="text" class="inputSize2 Wdate" style="cursor:hand;" readonly="readonly" id="sid" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" ondblClick="clearInput(this)"/>
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
                <td><input name="schedule.schStartTime"  value="${schedule.schStartTime}" type="text" class="inputSize2 Wdate" style="cursor:hand;" readonly="readonly" ondblClick="clearInput(this)"  onClick="WdatePicker({skin:'default',dateFmt:'HH:mm'})"/></td>
                <th>结束时间：</th>
                <td><input name="schedule.schEndTime"  value="${schedule.schEndTime}" type="text" class="inputSize2 Wdate" style="cursor:hand;" readonly="readonly"  ondblClick="clearInput(this)" onClick="WdatePicker({skin:'default',dateFmt:'HH:mm'})"/></td>
            </tr>
            <tr class="noBorderBot">
                <th class="required">日程内容：<span class='red'>*</span></th>
                <td colspan="3"><textarea id="schTitle" class="inputSize2L" rows="9" name="schedule.schTitle" onBlur="autoShort(this,100)">${schedule.schTitle}</textarea>
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
        <script type="text/javascript">
             $("sid").value='${schedule.schStartDate}'.substring(0,10);
        </script>
	</form>
    </logic:notEmpty>
    <logic:empty name="schedule">
        <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该日程已被删除</div>
    </logic:empty>
    </div>
  </body>
</html>

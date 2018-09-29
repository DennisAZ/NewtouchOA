<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0,count1=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>新建工作安排</title>
    <link rel="shortcut icon" href="favicon.ico"/>    
 	 <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
     <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
     <link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
     <script type="text/javascript" src="crm/js/prototype.js"></script>
	 <script type="text/javascript" src="crm/js/oa.js"></script>
     <script type="text/javascript" src="crm/js/formCheck.js"></script>
	 <script type="text/javascript" src="crm/js/common.js"></script>
	 <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript">       
		function check(){
			var errStr = "";
		 	if(isEmpty("satTitle")){
			 	errStr+="- 未填写工作名称！\n";
		 	}
		 	else if(checkLength("satTitle",200)){
				errStr+="- 工作名称不能超过200个字！\n";
			}
			if(isEmpty("nodeIds")){
				errStr+="- 未选择执行人！\n";
			}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosend","提交中...");
				waitSubmit("doCancel");
				return $("create").submit();			  
			}
		}
  </script> 
</head>
  <body>
  <div class="inputDiv">
  	<form action="salTaskAction.do" method="post" name="create">
  	<input type="hidden" name="op" value="newSalTask">
		<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="descTitleL">工作名称：<span class='red'>*</span></th>
                    <th class="descTitleR" colspan="3"><input name="salTask.stTitle" id="satTitle" type="text" class="inputSize2L" onBlur="autoShort(this,200)"/></th>				
                </tr>
            </thead>
			<tbody>
            	<tr>
                    <th>类别：</th>
                    <td>
                        <logic:notEmpty name="salTaskType">
                        <select name="taskType" class="inputSize2 inputBoxAlign">
                            <option  value="">&nbsp;</option>
                            <logic:iterate id="stt" name="salTaskType" scope="request">
                            <option value="${stt.typId}">${stt.typName}</option>
                            </logic:iterate>
                        </select>
                        </logic:notEmpty>
                        <logic:empty name="salTaskType">
                        <select class="inputSize2 inputBoxAlign" disabled>
                            <option  value="">未添加</option>
                        </select>
                        </logic:empty>
                        <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                    </td>
                    <th>优先级：</th>
                    <td>
                    <input type="radio" name="salTask.stLev" id="lev1" value="0" checked="checked"><label for="lev1">低&nbsp;&nbsp;</label>
                    <input type="radio" name="salTask.stLev" id="lev2" value="1"><label for="lev2">中&nbsp;&nbsp;</label>
                    <input type="radio" name="salTask.stLev" id="lev3" value="2"><label for="lev3">高&nbsp;&nbsp;</label></td>
                </tr>
                <tr>
                    <th>状态：</th>
                    <td colspan="3" class="longTd">
                    <input type="radio" name="salTask.stStu" id="sta1" value="0" checked="checked"><label for="sta1">执行中&nbsp;&nbsp;</label><input type="radio" name="salTask.stStu" id="sta2" value="1"><label for="sta2">已完成&nbsp;&nbsp;</label><input type="radio" name="salTask.stStu" id="sta3" value="2"><label for="sta3">取消&nbsp;&nbsp;</label></td>
                </tr>
                <tr>
                	<th>开始日期：</th>
                    <td>
                    <input name="satStartDate" type="text" ondblClick="clearInput(this)"  readonly="readonly" class="Wdate inputSize2" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" style="cursor:hand"/>
                    </td>
                    <th>结束日期：</th>
                    <td>
                    <input name="satFinDate" type="text" ondblClick="clearInput(this)"  readonly="readonly" class="Wdate inputSize2" onClick="WdatePicker({skin:'default',dateFmt:'yyyy-MM-dd'})" style="cursor:hand"/></td>
                </tr>
                <tr>
                    <th class="required"><a href="javascript:void(0)" title="点击选择执行人" onClick="parent.addDivBrow(12,'getNames');return false;">执行人</a>：<span class='red'>*</span></th>
                    <td colspan="3" class="longTd">
                        <a id="nodeNames" class="nodeNamesLayer inputSize2L" title="点击选择执行人" href="javascript:void(0)" onClick="parent.addDivBrow(12,'getNames');return false;"></a>
                        <input type="hidden" id="nodeIds" name="nodeIds"/>
                    </td>
                </tr>
                <tr class="noBorderBot">
                    <th>描述：</th>
                    <td colspan="3"><textarea class="inputSize2L" rows="10" name="salTask.stRemark" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>
                <tr class="submitTr">
                    <td colspan="4">
                    <input type="button" class="butSize1" id="dosend" value="提交" onClick="check()">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input type="button" class="butSize1" id="doCancel"  value="取消" onClick="cancel()"></td>
                </tr>	
            </tbody>
					
	  </table>
	</form>
  </div>
  </body>
</html>

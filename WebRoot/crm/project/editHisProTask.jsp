<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>编辑项目日志(未使用)</title>
    <link rel="shortcut icon" href="favicon.ico"/>    
 	 <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
     <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	 <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
     <link rel="stylesheet" type="text/css" href="css/style.css"/>
     <script type="text/javascript" src="js/prototype.js"></script>
	 <script type="text/javascript" src="js/sal.js"></script>
	 <script type="text/javascript" src="js/common.js"></script>
	 <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
     <script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript">       
			function check(){
				var errStr = "";
				if(isEmpty("prtaTitle")){   
					errStr+="- 未填写项目日志主题！\n"; 
				}
				else if(checkLength("prtaTitle",300)){
					errStr+="- 日志主题不能超过300个字！\n";
				}
				if(errStr!=""){
					errStr+="\n请返回修改...";
					alert(errStr);
					return false;
				}
				else{
					waitSubmit("dosend","保存中...");
					waitSubmit("doCancel");
					return $("change").submit();			
				}  
			}
		function showState()
		{
			$("staId").value='${proTask.proStage.staId}'
		
			
		}	
	 
		window.onload=function(){
			if('${proTask}'!=null&&'${proTask}'!=""){
			    showState();
			}
		
		}
  </script> 
</head>
  <body>
  <div class="inputDiv">
    <logic:notEmpty name="proTask">
  	<form action="projectAction.do" method="post" name="change">
  	<input type="hidden" name="op" value="modHisProTask">
  	<input type="hidden" name="prtaId" value="${proTask.prtaId}">
		<table class="dashTab inputForm" cellpadding="0" cellspacing="0">
			<thead>
				<tr>
					<th class="descTitleL">日志主题：<span class='red'>*</span></th>
					<th class="descTitleR" colspan="3"><input name="proTask.prtaTitle" id="prtaTitle" value="${proTask.prtaTitle}" type="text" class="inputSize2" onBlur="autoShort(this,300)"></th>				
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>对应项目：</th>
					<td colspan="3"><span class="textOverflowL" title="${project.proTitle}">${project.proTitle}&nbsp;</span></td>				
				</tr>
				<tr>
					<th>子项目：</th>
					<td colspan="3">
					<select name="staId" id="staId" class="inputSize2L">
                    <option value="" ></option>
					<logic:equal value="y" name="proStage" >
                    <logic:notEmpty name="project" property="proStages">
                           <logic:iterate id="pStage" name="project" property="proStages">
                           <logic:equal value="1" name="pStage" property="staIsdel">
                           <option value="${pStage.staId}">${pStage.staTitle}</option>
                           </logic:equal>
                           </logic:iterate>
                    </logic:notEmpty>
					</logic:equal>
                  </select>
					
					</td>				
			</tr>
			<tr>
				<th>日志内容：</th>
				<td colspan="3"><textarea class="inputSize2L" rows="6" onBlur="autoShort(this,4000)" name="proTask.prtaDesc">${proTask.prtaDesc}</textarea></td>
			</tr>
            <tr class="noBorderBot">
				<th>提交人：</th>
				<td>${proTask.prtaName}&nbsp;</td>
                <th>提交时间：</th>
                <td><span id="inpDate"></span>&nbsp;</td>
			</tr>
			<tr class="submitTr">
				<td colspan="4">
				<input type="button" class="butSize1" id="dosend" value="保存" onClick="check()">
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<input id="doCancel" type="button" class="butSize1" value="取消" onClick="cancel()"></td>
			</tr>
			</tbody>			
	  </table>
      <script type="text/javascript">
			 removeTime('inpDate','${proTask.prtaRelDate}',2);
		</script>
	</form>

   </logic:notEmpty>
   <logic:empty name="proTask">
    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该项目日志已被删除！</div>
   </logic:empty>
  </div>
  </body>
</html>

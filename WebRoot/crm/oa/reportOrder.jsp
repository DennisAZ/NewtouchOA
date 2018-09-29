<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>创建报告(流程-未使用-)</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
   	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="crm/editor/comm.css" />
     <style type="text/css">
	.inputSize3{
	border:0px;
	border-bottom:#fff 1px solid;
	width:160px;
	background:none; 
	color: #000000; 
}
	</style>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script language="javascript" src="crm/editor/all.js"></script>
    <script language="javascript" src="crm/editor/editor.js"></script>
    <script language="javascript" src="crm/editor/editor_toolbar.js"></script>
  	<script type="text/javascript">
		
		function check(){
			//把iframe里编辑器的值传入隐藏的textarea
			DoProcess();
			if(document.getElementById("repTitle").value==""||document.getElementById("repTitle").value==null){
				alert("请填写报告主题！");
				return false;
			}
			if(document.getElementById("repType").value==""||document.getElementById("repType").value==null){
				alert("请选择报告类别！");
				return false;
			}
			 return document.getElementById("saveReport").submit();
		}
		function check1(){
			//把iframe里编辑器的值传入隐藏的textarea
			DoProcess();
			var allUsers=document.getElementsByName("accUser");
			document.getElementById("isSend").value="1";
			if(document.getElementById("repTitle").value==""||document.getElementById("repTitle").value==null){
				alert("请填写报告主题！");
				return false;
			}
			if(document.getElementById("repSendTitle").value==""||document.getElementById("repSendTitle").value==null){
				alert("请填写发送主题！");
				return false;
			}
			if(document.getElementById("step1").value==""||document.getElementById("step1").value==null)
			{
				alert("请选择第一步的批复人！")
				return false;
			}
			return document.getElementById("saveReport").submit();
		}
	  function addStep(tableName1) 
	  { 
		var tableName=document.getElementById("repStep");
		var row =tableName.insertRow(tableName.rows.length-1); 
		var i = tableName.rows.length-1; 
		if(tableName1=='repStep') 
		{
			var col = row.insertCell(0);
			col.innerHTML = "<div align='center'>步骤"+i+"：</div>"; 
			col = row.insertCell(1); 
			col.innerHTML = "<span>批复人：<input style='width:255px' class='inputSize3 inputBoxAlign' type='text' id='step"+i+"' />&nbsp;<input type='button' value='选择批复人' onClick='isExistDiv("+i+")'/> </span>"; 
			col = row.insertCell(2); 
			col.innerHTML = "<div>是否该步所有人都批复完进入下步：<input type='radio' name='isAllApp"+i+"' value='0' checked>否<input type='radio' name='isAllApp"+i+"' value='1'>是</div>"; 
			col = row.insertCell(3); 
			col.innerHTML = "<span style='display:none'><INPUT type='checkbox' name='num' checked='checked' value="+i+"></span><div id='udiv"+i+"' style='display:none'></div>"; 
			col.style.borderRight="0px";
		} 
      } 
function delTableRow(){
  var tableName=document.getElementById("repStep");
  var i=tableName.rows.length-2;
   if(i==0)
   {
   	   alert("已是最后一行无法删除");
   	   return false;
   }
   else{
	if(confirm("确定删除这条数据吗?")){
		tableName.deleteRow(i);	
	}
  }
}
	</script>
  </head>
  
  <body>
  	<div class="inputDiv">
    	<form action="messageAction.do" id="saveReport" name="saveReport" method="post">
            <input type="hidden"  name="op" value="saveReport" />
            <input type="hidden" id="code" name="code" value="time" />
            <input type="hidden" id="code" name="repCode" value="${repCode}" />
            <input type="hidden"  name="saveType" value="report" />
            <input type="hidden"  name="isSend" value="0" />
            <input type="hidden"  name="repFromCode" value="${limUser.userCode}" />
            <div class="grayBack" id="accept" style="padding:5px; margin:5px">
                <table class="normal lineborder" width="96%" border="0" align="center" cellpadding="0" cellspacing="0">
                     <tr>
                        <td width="60px"><div align="right">发送主题：</div></td>
                        <td style="padding-left:6px">
                            <input class="inputSize2" style="width:400px" type="text" id="repSendTitle" name="report.repSendTitle"/>
                        </td>
                     </tr>
                </table>
				 <table id="repStep"  class="normal" width="100%" border="0" cellpadding="0" cellspacing="0">
                     <tr>
                     <td width="12%"><div align="center">步骤1：</div></td>
                     <td width="37%"><span>批复人：<input style="width:255px" class="inputSize3 inputBoxAlign" type="text" id="step1" />&nbsp;<input type="button" value="选择批复人" onClick="isExistDiv(1)"/> </span></td>
					 <td width="51%"><div>是否该步所有人都批复完进入下步：<input type="radio" name="isAllApp1" value="0" checked>否<input type="radio" name="isAllApp1" value="1">是</div></td>
                     <td><span style='display:none'><INPUT type='checkbox' name='num' checked='checked' value="1"></span><div id="udiv1" style='display:none'></div></td>
                    </tr>
				     <tr>
                       <td><div align="center"><input type="button" onClick="addStep('repStep')" value="添加下一步"/></div></td>
                       <td><div align="left"><input type="button" onClick="delTableRow()" value="删除最后一步"></div></td>
	                   <td><div align="center">&nbsp;</div></td>
	                   <td><div align="center">&nbsp;</div></td>
                    </tr>
                </table>
            </div>
       		<table class="normal dashborder" width="650px" border="0" align="center" cellpadding="0" cellspacing="0">
              	<tr>
                    <td><div align="right">报告主题：</div></td>
                    <td colspan="3"><input type="text" name="report.repTitle" id="repTitle" class="inputSize2" style="width:400px" /></td>
          		</tr>
                <tr>
                  	<td width="80px"><div align="right">报告类别：</div></td>
                  	<td width="100px">
                  	<select id="repType" name="report.repType">
                        <option value="">请选择</option>
                        <logic:notEmpty name="repTypeList">
                            <logic:iterate id="repTypeList" name="repTypeList">
                                <option value="${repTypeList.typName}">${repTypeList.typName}</option>
                            </logic:iterate>
                        </logic:notEmpty>
                    </select>
                  	</td>
                    <td width="80px"><div align="right">提交人：</div></td>
                    <td width="400px">${limUser.userSeName}</td>
      			</tr>
              	<tr>
                    <td valign="top"><div align="right">内容：</div></td>
                    <td colspan="3"><!--<textarea name="report.repContent" rows="8" cols="60"></textarea>-->
                    	<textarea id="content" name="content" style="display:none;"></textarea>
						<script language="javascript">
                            gFrame = 1;//1-在框架中使用编辑器
                            gContentId = "content";//要载入内容的content ID
                            OutputEditorLoading();
                        </script>
                        <iframe id="HtmlEditor" class="editor_frame" frameborder="0" marginheight="0" marginwidth="0" style="width:100%;height:300px;overflow:visible;" ></iframe>
                    </td>
              	</tr>
               	<tr>
                     <td colspan="4" style="border:0px; text-align:center">
                     <input class="butSize1" type="button" onClick="check()" value="保存待发" />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                     <input class="butSize1" type="button" value="发送" onClick="check1()"/>
                     </td>
               	</tr>
    		</table>
            
    	</form>
	</div>
  </body>
</html>

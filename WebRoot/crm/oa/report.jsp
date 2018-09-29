<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0,count1=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>创建报告</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
   	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="crm/editor/comm.css" />
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script language="javascript" src="crm/editor/all.js"></script>
    <script language="javascript" src="crm/editor/editor.js"></script>
    <script language="javascript" src="crm/editor/editor_toolbar.js"></script>
  	<script type="text/javascript">
		function check(){
			//把iframe里编辑器的值传入隐藏的textarea
			DoProcess();
			var errStr = "";
		 	if(isEmpty("repTitle")){
			 	errStr+="- 未填写报告主题！\n";
		 	}
			if(isEmpty("nodeIds")){
				 errStr+="- 未选择批复人！\n";
			 }
		 	else if(checkLength("repTitle",100)){
				errStr+="- 报告主题不能超过100个字！\n";
			}
			if(isEmpty("repType")){
			 	errStr+="- 未选择报告类别！\n";
		 	}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave","保存中...");
				waitSubmit("dosend");
			 	return $("saveReport").submit();
			}
		}
		function check1(){
			
			//把iframe里编辑器的值传入隐藏的textarea
			DoProcess();
			
			//var allUsers=$N("accUser");
			var errStr = "";	
		 	if(isEmpty("nodeIds")){
				 errStr+="- 未选择批复人！\n";
			 }
		   if(isEmpty("repTitle")){
			 	errStr+="- 未填写报告主题！\n";
		 	}
		 	else if(checkLength("repTitle",100)){
				errStr+="- 报告主题不能超过100个字！\n";
			}
			if(isEmpty("repType")){
			 	errStr+="- 未选择报告类别！\n";
		 	}
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				$("isSend").value="1";
				waitSubmit("dosave");
				waitSubmit("dosend","发送中...");
				return $("saveReport").submit();
			}
		}
		
		function initReport(){
			if($("repType")!=null)
				$("repType").value="${reportInfo.repType.typId}";
			$("repCode").value="${reportInfo.repCode}";
			$("upFiles").onclick=function(){
				parent.commonPopDiv(2,'${reportInfo.repCode}','rep','doc');
			}
			
		}
		window.onload=function() {
			if("${isReady}"!="")
				initReport();
			if("<c:out value="${reportInfo.repRecName}"/>"!=""){
				var meRec="<c:out value="${reportInfo.repRecName}"/>".split("|");
				$("nodeIds").value=meRec[0];
				$("nodeNamesInput").value=meRec[1];
				$("nodeNames").innerHTML=meRec[1];
			}
		}
	</script>
  </head>
  
  <body>
  	<div class="inputDiv">
    	<form action="messageAction.do" id="saveReport" name="saveReport" method="post">
        	<logic:empty name="isReady">
            	<input type="hidden"  name="op" value="saveReport" />
            </logic:empty>
            <logic:notEmpty name="isReady">
            	<input type="hidden"  name="op" value="updateSendReport" />
            </logic:notEmpty>
            <input type="hidden" id="repCode" name="repCode" value="${repCode}" />
            <input type="hidden"  name="saveType" value="report" />
            <input type="hidden"  name="isSend" value="0" />
            <!--<input type="hidden"  name="repFromCode" value="${limUser.userCode}" />-->
       		<table class="dashTab inputForm miniTh" cellpadding="0" cellspacing="0">
              	<tbody>
                	<tr>
                        <th class="required">报告主题：<span class='red'>*</span></th>
                        <td colspan="3" class="longTd"><input type="text" id="repTitle" name="report.repTitle" class="inputSize2L inputBoxAlign" value="<c:out value="${reportInfo.repTitle}"/>" onBlur="autoShort(this,100)"/>&nbsp;&nbsp;<input id="upFiles" type="button" class="butSize1 inputBoxAlign" value="添加附件" onClick="parent.commonPopDiv(2,'${repCode}','rep','doc')"/></td>
                    </tr>
                	<tr>
                        <th class="required"><a title="点击选择批复人" href="javascript:void(0)" onClick="parent.addDivBrow(12,'getNames');return false;">批复人</a>：<span class='red'>*</span></th>
                        <td colspan="3" class="longTd">
                            <a id="nodeNames" class="nodeNamesLayer" title="点击选择批复人" style="width:100%" href="javascript:void(0)" onClick="parent.addDivBrow(12,'getNames');return false;"></a>
                            <input type="hidden" id="nodeIds" name="nodeIds"/>
                            <input type="hidden" id="nodeNamesInput" name="nodeNamesInput"/>
                        </td>
                    </tr>
                	<tr>
                        <th class="required">报告类别：<span class='red'>*</span></th>
                        <td>
                        <logic:notEmpty name="repTypeList">
                            <select id="repType" name="repType" class="inputSize2 inputBoxAlign">
                                <option value="">请选择</option>
                                <logic:iterate id="repTypeList" name="repTypeList">
                                    <option value="${repTypeList.typId}">${repTypeList.typName}</option>
                                </logic:iterate>
                            </select>
                         </logic:notEmpty>
                         <logic:empty name="repTypeList">
                             <select id="repType" class="inputSize2 inputBoxAlign" disabled>
                                <option>未添加</option>
                            </select>
                         </logic:empty>
                        <img src="crm/images/content/plugin.gif" alt="该字段可在类别管理中自定义" style="vertical-align:middle"/>
                        </td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>内容：</th>
                        <td colspan="3" class="longTd"><!--<textarea name="report.repContent" rows="8" cols="60"></textarea>-->
                            <textarea id="content" name="report.repContent" style="display:none;">${reportInfo.repContent}</textarea>
                            <script language="javascript">
                                gFrame = 1;//1-在框架中使用编辑器
                                gContentId = "content";//要载入内容的content ID
                                OutputEditorLoading();
                            </script>
                            <iframe id="HtmlEditor" class="editor_frame" frameborder="0" marginheight="0" marginwidth="0" style="width:100%;height:300px;overflow:visible;" ></iframe>
                        </td>
                    </tr>
                    <tr class="submitTr">
                         <td colspan="4">
                         <input id="dosave" class="butSize1" type="button" onClick="check()" value="保存待发" />
                         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                         <input id="dosend" class="butSize1" type="button" value="发送" onClick="check1()"/>
                         </td>
                    </tr>
                </tbody>
                
    		</table>
    	</form>
	</div>
  </body>
</html>

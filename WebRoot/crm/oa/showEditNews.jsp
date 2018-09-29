<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>编辑新闻公告</title>
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
    <script type="text/javascript" >
		
		function showSelect()
	 	{
	 	    var isTop='${newsInfo.newIstop}';
			switch("${newsInfo.newType}"){
				case "新闻":
					$("type1").checked=true;
					break;
				case "公告":
					$("type2").checked=true;
					break;
				case "业内动态":
					$("type3").checked=true;
					break;
				case "通知":
					$("type4").checked=true;
					break;
			}
	 	} 
		
    	function check(){
		   	DoProcess();
			var errStr = "";
			if(isEmpty("newTitle")){
				 errStr+="- 未填写主题！\n";
			 }
			 else if(checkLength("newTitle",100)){
				errStr+="- 主题不能超过100个字！\n";
			 }
			 if(isEmpty("nodeIds")){
				 errStr+="- 未选择接收人！\n";
			 }
			 if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosave","保存中...");
				return $("change").submit();
			}
		}
		
		function loadData(){
			var userNames = '';
			var userIds = '';
			<logic:notEmpty name="newsInfo" property="RNewLims">
				<logic:iterate id="rnLims" name="newsInfo" property="RNewLims">
						userNames += '<c:out value="${rnLims.salEmp.seName}[${rnLims.salEmp.limRole.rolName}]"/>'+';&nbsp;';
						userIds += '${rnLims.salEmp.seNo}' + ',';
				</logic:iterate>
			</logic:notEmpty>
			$("nodeNames").innerHTML = userNames;
			$("nodeIds").value = userIds;
		}
		window.onload=function(){
			if('${newsInfo}'!=null&&'${newsInfo}'!=""){
				showSelect();
			}
			loadData();
		}
    </script>
  </head>
  
  <body>
  	<logic:notEmpty name="newsInfo">
    <div class="inputDiv">
  	<form id="change" name="change" action="messageAction.do" method="post">
   		<input type="hidden" name="op" value="modifyNewsInfo" />
   		<input type="hidden"  name="newCode" value="${newsInfo.newCode}" />
        <table class="dashTab inputForm miniTh" cellpadding="0" cellspacing="0">
        	<tbody>
            	<tr>
                    <th class="required">主题：<span class='red'>*</span></th>
                    <td colspan="3" class="longTd">
                        <input id="newTitle" name="news.newTitle" value="<c:out value="${newsInfo.newTitle}"/>" class="inputSize2" style="width:100%" type="text" onBlur="autoShort(this,100)"/>
                    </td>
                </tr>
                <tr>
                     <th class="required"><a href="javascript:void(0)" title="点击选择执行人" onClick="parent.addDivBrow(12,'getNames');return false;">接收人</a>：<span class='red'>*</span></th>
                     <td colspan="3" class="longTd">
                     	<a id="nodeNames" title="点击选择执行人" class="nodeNamesLayer"  style="width:100%" href="javascript:void(0)" onClick="parent.addDivBrow(12,'getNames');return false;"></a>
                     	<input type="hidden" id="nodeIds" name="nodeIds"/>
                     </td>
                </tr>
                <tr>
                    <th>分类：</th>
                    <td colspan="3" class="longTd">
                         <input type="radio" name="news.newType" id="type1" value="新闻" checked/><label for="type1">新闻&nbsp;&nbsp;&nbsp;</label>
                        <input type="radio" name="news.newType" id="type2" value="公告"/><label for="type2">公告&nbsp;&nbsp;&nbsp;</label>
                        <input type="radio" name="news.newType" id="type3" value="业内动态"/><label for="type3">业内动态&nbsp;&nbsp;&nbsp;</label>
                        <input type="radio" name="news.newType" id="type4" value="通知"/><label for="type4">通知&nbsp;&nbsp;&nbsp;</label>
                    </td>
             	</tr>
                <tr class="noBorderBot">
                	<th>置顶：</th>
                    <td colspan="3" class="longTd">
                        <input type="radio" id="newRad2" name="news.newIstop" value="0"><label for="newRad2">是&nbsp;&nbsp;&nbsp;</label>
                 		<input type="radio" id="newRad1" name="news.newIstop" value="1" checked><label for="newRad1">否&nbsp;&nbsp;&nbsp;</label>
                    </td>
                </tr>
               	<tr class="noBorderBot">
                    <td colspan="4" style="width:100%">
                        <textarea id="content" name="content" style="display:none;">${newsInfo.newContent}</textarea>
						<script language="javascript">
                            gFrame = 1;//1-在框架中使用编辑器
                            gContentId = "content";//要载入内容的content ID
                            OutputEditorLoading();
                        </script>
                        <iframe id="HtmlEditor" class="editor_frame" frameborder="0" marginheight="0" marginwidth="0" style="width:100%;height:400px;overflow:visible;" ></iframe>
                    </td>
                </tr>
                <tr class="submitTr">
                    <td colspan="4"><input id="dosave" class="butSize1" type="button" value="保存" onClick="check()" />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="doCancel" class="butSize1" type="button" value="取消" onClick="cancel()" />
                    </td>
                </tr>
            </tbody>
        </table>
	</form>
    </div>
    </logic:notEmpty>
    <logic:empty name="newsInfo">
        <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该新闻已被删除</div>
    </logic:empty>
  </body>
</html>

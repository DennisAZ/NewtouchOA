<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
    <title>新建新闻公告</title>
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
    <script type="text/javascript" src="crm/js/oa.js"></script>
    <script language="javascript" src="crm/editor/all.js"></script>
    <script language="javascript" src="crm/editor/editor.js"></script>
    <script language="javascript" src="crm/editor/editor_toolbar.js"></script>
    <script type="text/javascript" > 
    	function check(){
			//把iframe里编辑器的值传入隐藏的textarea
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
					waitSubmit("dosend","发送中...");
					waitSubmit("doCancel");
					return $("create").submit();
			}
			
		}
    </script>
  </head>
  
  <body>
    <div class="inputDiv">
  	<form id="create" name="create" action="messageAction.do" method="post">
   		<input type="hidden" name="op" value="saveNews" />
   		<input type="hidden" id="newCode"  name="newCode" value="${newCode}" />
   		<!--<input type="hidden"  name="newFromCode" value="${limUser.userCode}" />-->
        <table class="dashTab inputForm miniTh" cellpadding="0" cellspacing="0">
        	<tbody>
            	<tr>
                    <th class="required">主题：<span class='red'>*</span></th>
                    <td colspan="3" class="longTd">
                        <input id="newTitle" name="news.newTitle" class="inputSize2" type="text" style="width:100%" onBlur="autoShort(this,100)"/>
                    </td>
                </tr>
                <tr>
                    <th class="required"><a title="点击选择接收人" href="javascript:void(0)" onClick="parent.addDivBrow(12,'getNames');return false;">接收人</a>：<span class='red'>*</span></th>
                    <td colspan="3" class="longTd">
                        <a id="nodeNames" title="点击选择接收人" class="nodeNamesLayer" style="width:100%" href="javascript:void(0)" onClick="parent.addDivBrow(12,'getNames');return false;"></a>
                        <input type="hidden" id="nodeIds" name="nodeIds"/>
                        <input type="hidden" id="nodeNamesInput" name="nodeNamesInput"/>
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
                        <textarea id="content" name="content" style="display:none;"></textarea>
                        <script language="javascript">
                            gFrame = 1;//1-在框架中使用编辑器
                            gContentId = "content";//要载入内容的content ID
                            OutputEditorLoading();
                        </script>
                        <iframe id="HtmlEditor" class="editor_frame" frameborder="0" marginheight="0" marginwidth="0" style="width:100%;height:400px;overflow:visible;" ></iframe>
                    </td>
                </tr>
                <tr class="submitTr">
                    <td colspan="4">
                    <input id="dosend" class="butSize1" type="button" value="确定提交" onClick="check()" />
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="doCancel" class="butSize1" type="reset" value="清除重置" onClick="UnDoProcess()" /></td>
                </tr>
            </tbody>
        </table>
	</form>
    </div>
  </body>
</html>

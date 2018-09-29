<%@ page language="java"  pageEncoding="UTF-8"%>
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
    
    <title>发送消息</title>
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
  	<script type="text/javascript">
		function check(type){
			//把iframe里编辑器的值传入隐藏的textarea
			DoProcess();
			var errStr = "";
			if(isEmpty("meTitle")){
				 errStr+="- 未填写消息主题！\n";
			 }
			 else if(checkLength("meTitle",100)){
				errStr+="- 消息主题不能超过100个字！\n";
			 }
			 if("${sendType}"!="reply"&&isEmpty("nodeIds")){
				 errStr+="- 未选择接收人！\n";
			 }
			 if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				if(type!=null&&type=="1"){
					waitSubmit("save","保存中...");
					waitSubmit("doSend");
					return $("saveMess").submit();
				}else{
					if("${sendType}"!="reply"){

						$("isSend").value="1";
						waitSubmit("dosend","发送中...");
						waitSubmit("save");
						return $("saveMess").submit();
					}
					else{
					waitSubmit("dosend","发送中...");
					waitSubmit("save");
					$("isSend").value="2";
					return $("saveMess").submit();
					}
				}
			}	
		}
		
		function loadReply(){
			var sendDate="${MessInfo.meDate}".substring(0,16);
			$("meTitle").value="回复「<c:out value="${MessInfo.meTitle}"/>」";
			
			$("content").value="<br/><br/><div style='color:#999; font-size:12px; margin:5px 0px 5px 10px; padding:5px; border:2px solid #ccc;'><div style='font-weight:600;background:#ccc; color:#fff;'>&nbsp;原消息&nbsp;</div><u>发送人：<c:out value="${MessInfo.meInsUser}"/><br/>主&nbsp;&nbsp;&nbsp;题：<c:out value="${MessInfo.meTitle}"/><br/>日&nbsp;&nbsp;&nbsp;期："+sendDate+"</u><br/><br/>"+$("content").value+"</div>";//直接'+内容'会无法读取
		}
		
		function loadForward(){
			var sendDate="${MessInfo.meDate}".substring(0,16);
			$("meTitle").value="转发「<c:out value="${MessInfo.meTitle}"/>」";
			$("content").value="<br/><br/><div style='font-size:12px; margin:5px 0px 5px 10px;'><div style='font-weight:600;'>-------------------&nbsp;原消息&nbsp;-------------------</div><u>发送人：<c:out value="${MessInfo.meInsUser}"/><br/>主&nbsp;&nbsp;&nbsp;题：<c:out value="${MessInfo.meTitle}"/><br/>日&nbsp;&nbsp;&nbsp;期："+sendDate+"</u><br/><br/>"+$("content").value+"</div>";
		}
		
		function loadToSend(){
			var meRec="<c:out value="${MessInfo.meRecName}"/>".split("|");
			$("nodeIds").value=meRec[0];
			$("nodeNamesInput").value=meRec[1];
			$("nodeNames").innerHTML=meRec[1];
		}
		
		window.onload=function(){
			if("${sendType}"=="reply"){//回复
				loadReply();
			}
			else if("${sendType}"=="forward"){//转发
				loadForward();
			}
			else if("${MessInfo}"!=""){//待发
				
				loadToSend();
			}
		}
	</script>
  </head>
  
  <body>
  	<div class="inputDiv">
    	<form action="messageAction.do" id="saveMess" name="saveMess" method="post">
        	<input type="hidden"  name="op" value="saveMessage" />
           	<input type="hidden" id="code" name="code" value="time" />
           	<input type="hidden"  name="meCode" value="${meCode}" />
           	<!--<input type="hidden"  name="meFromCode" value="${limUser.userCode}" />-->
           	<input type="hidden" id="isSend"  name="isSend" value="0" />
            <!-- 回复消息保存接收人,中间表id(更新消息回复状态) -->
			<input type="hidden" name="accUserId" value="${MessInfo.salEmp.seNo}"/>
            <input type="hidden" name="rmlId" value="${rmlId}"/>
            
            <table class="dashTab inputForm miniTh" cellpadding="0" cellspacing="0">  
            	<tbody>
                	<logic:notEqual name="sendType" value="reply">
                	<tr>
                        <th class="required"><a title="点击选择接收人" href="javascript:void(0)" onClick="parent.addDivBrow(12,'getNames');return false;">接收人</a>：<span class='red'>*</span></th>
						<td colspan="3" class="longTd">
                            <a id="nodeNames" class="nodeNamesLayer" style="width:100%" title="点击选择接收人" href="javascript:void(0)" onClick="parent.addDivBrow(12,'getNames');return false;"></a>
                            <input type="hidden" id="nodeIds" name="nodeIds"/>
                            <input type="hidden" id="nodeNamesInput" name="nodeNamesInput"/>
                        </td>
                    </tr>
                    </logic:notEqual>
            		<logic:equal name="sendType" value="reply">
                    <tr>
                    	<th>接收人：</th>
                        <td colspan="3" class="longTd">${MessInfo.salEmp.seName}</td>
                    </tr>
                	</logic:equal>
                    <tr class="noBorderBot">
                        <th class="required">主题：<span class='red'>*</span></th>
                        <td colspan="3" class="longTd">
                            <input class="inputSize2L inputBoxAlign" type="text" id="meTitle" name="message.meTitle" value="<c:out value="${MessInfo.meTitle}"/>" onBlur="autoShort(this,100)"/>&nbsp;&nbsp;
                            <input type="button" class="butSize1 inputBoxAlign" value="添加附件" onClick="parent.commonPopDiv(2,'${meCode}','mes','doc')"/>
                        </td>
                    </tr>
                    <tr class="noBorderBot">
                        <td colspan="4" style="width:100%">
                            <textarea id="content" name="message.meContent" style="display:none;">${MessInfo.meContent}</textarea>
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
                            <input id="save" class="butSize1" type="button" value="保存为草稿" onClick="check(1)"/>
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            <input id="dosend" class="butSize1" type="button" value="立即发送" onClick="check(2)"/>
                        </td>
                    </tr>
                </tbody>
       		</table>
        </form>
    </div>
  </body>
</html>

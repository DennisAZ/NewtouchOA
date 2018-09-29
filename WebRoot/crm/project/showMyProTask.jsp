<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//String stId=request.getParameter("stId");
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>完成情况(未使用)</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/att.css"/>
    <style type="text/css">
		#att{
			padding:5px;
			text-align:left;
		}
		#log{
			text-align:left;
			padding:10px;
			background-color:#fff;
			border:#999999 1px solid;
		}

		/* 退回弹出层 */
    	#backDiv{
			display:none;
			border:#ea1d1d solid 2px;
			background-color:#fef7eb; 
			text-align:left;
			padding:5px;
		}
		/* 完成评价层 */
    	#finDiv{
			display:none;
			border:#4e80c9 solid 2px;
			background-color:#ebf3fe; 
			text-align:left;
			padding:5px;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript">
    	
		function showBackInp(){
			var button = $("backButton");
			var backDiv = $("backDiv");
			var button2 = $("finButton");
			var finDiv = $("finDiv");
			if(backDiv.style.display=="none"){
				if(finDiv.style.display!="none"){
					finDiv.style.display="none";
					button2.innerHTML="确认完成";
					button2.className="blue";
					button2.onmouseout=function(){
						button2.className="blue";
					}
				}
				backDiv.style.display="block";
				button.innerHTML="关闭";
				button.className="redBack";
				button.onmouseout=function(){
					button.className="redBack";
				}
			}
			else{
				backDiv.style.display="none";
				button.innerHTML="退回";
				button.className="red";
				button.onmouseout=function(){
					button.className="red";
				}
			}
		}
		function showFinInp(){
			var button = $("finButton");
			var finDiv = $("finDiv");
			var button2 = $("backButton");
			var backDiv = $("backDiv");
			if(finDiv.style.display=="none"){
				if(backDiv.style.display!="none"){
					backDiv.style.display="none";
					button2.innerHTML="退回";
					button2.className="red";
					button2.onmouseout=function(){
						button2.className="red";
					}
				}
				finDiv.style.display="block";
				button.innerHTML="点击关闭";
				button.className="deepBlueBg";
				button.onmouseout=function(){
					button.className="deepBlueBg";
				}
			}
			else{
				finDiv.style.display="none";
				button.innerHTML="确认完成";
				button.className="blue";
				button.onmouseout=function(){
					button.className="blue";
				}
			}
		}
		
		function doBack(n){
		  waitSubmit('doback','处理中...');
		  if(n==1)
			{
				return $("backForm").submit();
			}
		  if(n==2){
				return $("finForm").submit();
			}
		}
		
		window.onload=function(){
			//在收到工作时隐藏退回按钮
			if(parent.document.getElementById("noBack")!=null&&$("backButton")!=null){
				$("backButton").style.display="none";
			}
			//在收到工作时隐藏确认完成按钮
			if(parent.document.getElementById("noBack")!=null&&$("finButton")!=null){
				$("finButton").style.display="none";
			}
		}
    </script>

  </head>
  
  <body>
  <logic:notEmpty name="proTaskLim" property="proTask">
  	<div style="padding:5px; width:378px" class="autoBr">
    	<logic:notEmpty name="proTaskLim" property="attachments">
            <div id="att" class="grayBack">
                <div class="blackblue bold" style="padding:5px; padding-top:2px">工作附件</div>
                <logic:iterate id="attList" name="proTaskLim" property="attachments">
                    <span id="fileDLLi<%=count%>"></span>
                    <script type="text/javascript">createFileToDL(<%=count%>,'${attList.attId}','${attList.attPath}','${attList.attName}','${attList.attSize}','${attList.attDate}')</script>
                    <% count++;%>
                </logic:iterate>
            </div><br/>
        </logic:notEmpty>
        
        <div id="log">
        	<div class="gray" style="padding-bottom:3px">
            	完成情况
                <logic:notEqual value="1" name="proTaskLim" property="proTask.prtaState">
                    <logic:equal value="1" name="proTaskLim" property="ptlIsfin">
                        <span style="width:180px"></span>
                        <span id="finButton" class="blue" style="padding:4px; cursor:pointer" onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className='blue'" onClick="showFinInp()">确认完成</span>
                        <span id="backButton" class="red" style="padding:4px; cursor:pointer" onMouseOver="this.className='redBack'" onMouseOut="this.className='red'" onClick="showBackInp()">退回</span>
                    </logic:equal>
                </logic:notEqual>
            </div>
            <div id="backDiv" style="display:none">
                <form id="backForm" action="projectAction.do" method="post" style="padding:0px; margin:0px">
                    <input type="hidden" name="op" value="backTask">
                    <input type="hidden" name="prtaId" value="${proTaskLim.proTask.prtaId}">
					<input type="hidden" name="ptlId" value="${proTaskLim.ptlId}">
                    <input type="hidden" name="actorName" value="${proTaskLim.ptlName}">
                    <div class="deepred bold">退回原因</div>
                    <div><textarea name="bacReason" cols="38" rows="5"></textarea></div>
                    <div style="text-align:center; padding-top:5"><input id="doback" type="button" onClick="doBack(1)" value="确认退回" class="butSize1"></div>
                </form>
            </div>
			 <div id="finDiv" style="display:none">
                <form id="finForm" action="projectAction.do" method="post" style="padding:0px; margin:0px">
                    <input type="hidden" name="op" value="finSomeTask">
                    <input type="hidden" name="prtaId" value="${proTaskLim.proTask.prtaId}">
					<input type="hidden" name="ptlId" value="${proTaskLim.ptlId}">
					<input type="hidden" name="actorName" value="${proTaskLim.ptlName}">
					
                    <div class="blue bold">完成评价</div>
                    <div><textarea name="bacReason" cols="38" rows="5"></textarea></div>
                    <div style="text-align:center; padding-top:5"><input  type="button" onClick="doBack(2)" value="确认完成" class="butSize1"></div>
                </form>
            </div>
            <logic:notEmpty name="proTaskLim" property="ptlDesc">
            	${proTaskLim.ptlDesc}
            </logic:notEmpty>
            <logic:empty name="proTaskLim" property="ptlDesc">
            	暂无完成情况...
            </logic:empty>
        </div>
    </div>
     </logic:notEmpty>
    <logic:empty name="proTaskLim" property="proTask">
        <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该工作已被删除</div>
    </logic:empty>
  </body>
</html>

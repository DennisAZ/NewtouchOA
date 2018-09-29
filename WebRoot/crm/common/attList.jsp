<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>附件列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="crm/css/style.css">
	<link rel="stylesheet" type="text/css" href="crm/css/att.css">

    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript">
		window.onload=function(){
			if("${noUp}"==""){
				$("listCon").style.height="315px";
			}
		}
    </script>
  </head>
  
  <body>
  	<div id="input">
    	<c:if test="${empty noUp}"><iframe frameborder="0" scrolling=no src="crm/common/docUpload.jsp"></iframe></c:if>
        <input type="hidden" id="extType" value="${extType}"/>
        <input type="hidden" id="type" value="${type}"/>
        <input type="hidden" id="id" value="${id}"/>
        <input type="hidden" id="hasIcon" value="${hasIcon}"/>
        <div class="xpTab blackblue"><span class="xpTabSelected">&nbsp;附件列表&nbsp;</span></div>
        <div id="listCon" class="divWithScroll">
        	<ul>
            <c:if test="${!empty obj.attachments}">
            	<c:forEach var="attList" items="${obj.attachments}" varStatus="idx">
                <li>
                    <span id="fileDLLi${idx.index}"></span>
                    <script type="text/javascript">createFileToDL("${idx.index}","${attList.attId}","${attList.attPath}","<c:out value="${attList.attName}"/>","${attList.attSize}","${attList.attDate}")</script>
                    <c:if test="${!empty canDel}"><span class="red udLine" style="cursor:pointer" onClick="delAttFile('${extType}','${type}','${id}','${attList.attId}','${hasIcon}','${attList.attPath}')">删除</span></c:if>
                </li>
                </c:forEach>
            </c:if>
            </ul>
            <c:if test="${empty obj.attachments}">
                <div id="noAtt">暂未上传附件...</div>
                <script type="text/javascript">
					if($F("hasIcon")!="0"){
						var img;
						if("${extType}"!="reload"){
							if("${extType}"=="descList"||parent.frames['ifrList']==null){
								if(parent.document.getElementById("${id}y")!=null){
									img=parent.document.getElementById("${id}y");
								}
								else{
									img=parent.document.getElementById("${id}n");
								}
								img.src="crm/images/content/attachNo.gif";
							}
							else if(parent.frames['ifrList']!=null){
								if(parent.frames['ifrList'].document.getElementById("${id}y")!=null){
									img=parent.frames['ifrList'].document.getElementById("${id}y");
								}
								else{
									img=parent.frames['ifrList'].document.getElementById("${id}n");
								}
								if(img!=undefined){
									img.src="crm/images/content/attachNo.gif";
								}
							}
						}
						else{
							if("${fromDel}"=="1"){
								parent.history.go(0);
							}
						}
					}
				</script>
            </c:if>
        </div>
        
    </div>
  </body>
</html>

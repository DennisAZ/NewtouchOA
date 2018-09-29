<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int c=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>新闻公告内容</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
   	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="crm/css/oa.css"/>
	<script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript">		
		
		createProgressBar();
		window.onload=function(){
			closeProgressBar();
		}
	</script>
    
  </head>
  <body>
  	 <logic:notEmpty name="newsInfo">
    <div id="newsStyle">
    	<div id="newsTitle">
        	<logic:equal value="0" name="newsInfo" property="newIstop">
            <img src="crm/images/content/upButton.gif" alt="置顶"/>
            </logic:equal>
        	<logic:notEmpty name="newsInfo" property="newType">
            	[${newsInfo.newType}]
            </logic:notEmpty>
            ${newsInfo.newTitle}
        </div>
        <div id="newsInf">
        	发布时间:<label id="ndate"></label>&nbsp;&nbsp;&nbsp;&nbsp;
        	<logic:equal value="0" name="isEdit">
        		发布人:${newsInfo.newInsUser}&nbsp;&nbsp;&nbsp;&nbsp;所属部门：${newsInfo.salEmp.salOrg.soName}
            </logic:equal>
            <logic:equal value="1" name="isEdit">
            	接收人:
                <logic:notEmpty name="newsInfo" property="RNewLims">
				      <logic:iterate id="RNewLims" name="newsInfo" property="RNewLims">
				      ${RNewLims.salEmp.seName}&nbsp;
				      </logic:iterate>
			      </logic:notEmpty>
            </logic:equal>
        </div>
        <div id="newsContent">
        	${newsInfo.newContent}
        </div>
		<div style="text-align:left; border-top:solid 1px #999999; padding-top:10px">
        	<logic:equal value="1" name="isEdit">
                <logic:notEmpty name="newsInfo" property="attachments">
                    <img style="cursor:pointer" class="imgAlign" src="crm/images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${newsInfo.newCode}','news','reload')"/>
                 </logic:notEmpty>
                 <logic:empty name="newsInfo" property="attachments">
                    <img style="cursor:pointer" class="imgAlign" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${newsInfo.newCode}','news','reload')"/>
                 </logic:empty>
            </logic:equal>
             附件：&nbsp;&nbsp;&nbsp;&nbsp;
             <logic:iterate id="attList" name="newsInfo" property="attachments">
                <span id="fileDLLi<%=c%>"></span>
                <script type="text/javascript">createFileToDL(<%=c%>,"${attList.attId}","${attList.attPath}","${attList.attName}","${attList.attSize}","${attList.attDate}")</script>
                <% c++;%>
            </logic:iterate>&nbsp;
		</div>
        <div class="gray" style="text-align:right; padding:5px;">
        	<logic:notEmpty name="newsInfo" property="newUpdUser">
            （于&nbsp;<span id="upDate"></span>&nbsp;被&nbsp;${newsInfo.newUpdUser}&nbsp;重新编辑）
            </logic:notEmpty>
        </div>
    </div>
    <script type="text/javascript">
    	removeTime("ndate","${newsInfo.newDate}",2);
		if($("upDate")!=null){
			removeTime("upDate","${newsInfo.newUpdDate}",2);
		}
    </script>
	</logic:notEmpty>
	 <logic:empty name="newsInfo">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该新闻公告已被删除</div>
	</logic:empty>
  </body>
</html>

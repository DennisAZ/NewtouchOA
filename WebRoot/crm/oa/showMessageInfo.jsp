<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>消息正文</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
   	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="crm/css/oa.css"/>
    <link rel="stylesheet" type="text/css" href="crm/css/att.css"/>
	<script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/oa.js"></script>
    <script type="text/javascript">	
		
		createProgressBar();
		window.onload=function(){
				closeProgressBar();
		}
	</script>
  </head>
  
  <body style="padding-top:10px;">
  <logic:notEmpty name="MessInfo">
  	<div id="mesStyle">
    	<logic:empty name="haveSend">
    	<div id="mesTools" class="deepBlueBg">
            <span onMouseOver="this.className='orangeBack'" onMouseOut="this.className=''" onClick="oaPopDiv(11,'${MessInfo.meCode}','${rmlId}')" ><img style="vertical-align:middle" src="crm/images/content/mesreply.gif" alt="回复消息"/>&nbsp;回复消息</span>
            <span onMouseOver="this.className='orangeBack'" onMouseOut="this.className=''" onClick="oaPopDiv(12,'${MessInfo.meCode}')"><img style="vertical-align:middle" src="crm/images/content/mesforward.gif" alt="转发消息"/>&nbsp;转发消息</span>
            <span onMouseOver="this.className='orangeBack'" onMouseOut="this.className=''" onClick="window.close()" ><img style="vertical-align:middle" src="crm/images/content/whiteClose.gif" alt="关闭页面"/>&nbsp;关闭页面</span>
        </div>
        </logic:empty>
    	<div id="mesTop" class="grayBack">
        	<table style="width:98%" class="normal" cellpadding="0px" cellspacing="0px">
                <div class="middle bold" style="padding:5px;"><span>${MessInfo.meTitle}&nbsp;</span></div>
                <tr>
                	<td class="gray" style="text-align:right">接收人：</td>
                    <td class="blackblue">
                    	 <logic:notEmpty name="MessInfo" property="RMessLims">
                         	<logic:iterate id="RMessLims" name="MessInfo" property="RMessLims">
                            	${RMessLims.rmlRecUser}.
                          	</logic:iterate>
                         </logic:notEmpty>
                    </td>
                </tr>
                <tr>
                	<td class="gray" style="text-align:right">时&nbsp;&nbsp;&nbsp;&nbsp;间：</td>
                	<td class="blackblue itali"><span id="sendDate"></span></td>
                </tr>
                <tr>
                	<td style="width:55px;text-align:right;" class="gray">发送人：</td>
                    <td class="blackblue">
                    	<logic:equal name="curUser" value="${MessInfo.salEmp.seNo}">
                        	<span class="orangeBack" style=" padding:1px">${MessInfo.meInsUser}</span>
                        </logic:equal>
                        <logic:notEqual name="curUser" value="${MessInfo.salEmp.seNo}">
                        	${MessInfo.salEmp.seName}
                        </logic:notEqual>
                  	</td>
                </tr>
                <logic:notEmpty name="MessInfo" property="attachments">
                <tr>
                	<td colspan="2" style="height:8px;"></td>
                </tr>
                <tr>
                	<td colspan="2" style="height:1px; background-color:#d1d3da;"></td>
                </tr>
                <tr>
                	<td colspan="2" style="height:5px;"></td>
                </tr>
                <tr>
                	<td class="gray" style="text-align:right">附&nbsp;&nbsp;&nbsp;件：</td>
                	<td>
                    	<logic:iterate id="attList" name="MessInfo" property="attachments">
                            <span id="fileDLLi<%=count%>"></span>
                            <script type="text/javascript">createFileToDL(<%=count%>,"${attList.attId}","${attList.attPath}","${attList.attName}","${attList.attSize}","${attList.attDate}")</script>
                            <% count++;%>
                    	</logic:iterate>
                    </td>
                </tr>
                </logic:notEmpty>
            </table>
        </div>
        <div id="mesCon">
        	${MessInfo.meContent}
        </div>
    </div>
    <script type="text/javascript">
    	removeTime('sendDate','${MessInfo.meDate}',2);
    </script>
    </logic:notEmpty>
    <logic:empty name="MessInfo">
        <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该消息已被删除</div>
    </logic:empty>
  </body>
</html>

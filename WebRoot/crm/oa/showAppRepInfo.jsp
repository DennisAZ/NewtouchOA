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
    <title>查看报告</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="crm/css/att.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/oa.js"></script>
 	<script type="text/javascript">
		
 		function showMem(objDiv){
   			var target=$(objDiv);
   			if (target.style.display=="block"){
      			target.style.display="none";
    		} 
    		else
      			target.style.display="block";
 		}
		
		//查看已发报告时隐藏批复功能
		function loadNoApp(){
			if("${notAppInf}"=="1"){
				var img=$("showMore");
				img.src="crm/images/content/hide.gif";
				img.alt="点击收起";
				$("othApps").style.display="block";
			}
		}
		
		
		createProgressBar();
		window.onload=function(){
			if('${reportInfo}'!=null&&'${reportInfo}'!=''){
				loadNoApp();
			}
			closeProgressBar();
		}
   </script>
 </head>
  
  <body>
  	<logic:notEmpty name="reportInfo">
  	<div class="descInf">
    	<table class="dashTab descForm" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="descTitleL">报告主题：</th>
                    <th class="descTitleR" colspan="3">
                        ${reportInfo.repTitle}&nbsp;
                    </th>
                </tr>
            </thead>
        	<tbody>
            
            </tbody>
            
      		<tr>
                <th>报告类别：</th>
                <td>${reportInfo.repType.typName}&nbsp;</td>
                <th>提交人：</th>
                <td>${reportInfo.repInsUser}&nbsp;</td>
           	</tr>
      		<tr>
          		<th>报告内容：</th>
          		<td colspan="3" style="height:100px;font-weight:normal;">${reportInfo.repContent}&nbsp;</td>
        	</tr>
       		<tr>
            	<th>
                    附件：
               </th>
                <td colspan="3">
                  <logic:iterate id="attList" name="reportInfo" property="attachments">
                        <span id="fileDLLi<%=count%>"></span>
                    	<script type="text/javascript">createFileToDL(<%=count%>,"${attList.attId}","${attList.attPath}","${attList.attName}","${attList.attSize}","${attList.attDate}")</script>
                        <% count++;%>
                    </logic:iterate>&nbsp;
                </td>
            </tr>
			<tr>
            	<th>提交日期：</th>
          		<td colspan="3"><span id="inpDate"></span>&nbsp;</td>
            </tr>
            <logic:notEmpty name="reportInfo" property="RRepLims">
                <logic:notEqual name="userCount" value="1">
                    <tr>
                        <th style="border-bottom:0px">相关批复：</th>
                        <td colspan="3" style="border-bottom:0px; font-weight:normal;">
                        	<div onMouseOver="this.className='orangeBack'" onMouseOut="this.className=''" onClick="showHide('othApps','showMore')" style="cursor:pointer;"><img id="showMore" class="imgAlign" src="crm/images/content/showmore.gif" alt="点击展开"/>&nbsp;点击展开/收起</div>
                        	<div id="othApps" style="display:none">
                            <logic:iterate id="rRepLims" name="reportInfo" property="RRepLims">
                            	<logic:equal value="1" name="rRepLims" property="rrlIsappro">
                            		<div class="grayBack" style="padding:5px; margin:5px;">
                                    	<ul style="margin:0; padding:0; list-style:none;">
                                        	<li>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;批复人：${rRepLims.rrlRecUser}&nbsp;</li>
                                            <li>&nbsp;批复意见：${rRepLims.rrlContent}&nbsp;</li>
                                            <li>&nbsp;批复日期：<span id="appDate<%=count%>"></span></li>
                                    	</ul>
                                	</div>
                            	</logic:equal>
                                <logic:equal value="0" name="rRepLims" property="rrlIsappro">
                                  	<div class="grayBack" style="padding:5px; margin:5px;">
                                    	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;批复人：${rRepLims.rrlRecUser}&nbsp;<span class="red">未批</span>
                                     </div>
                                </logic:equal>
                                <script type="text/javascript">
                                    dateFormat2("appDate","${rRepLims.rrlOpproDate}",<%=count%>);
                                </script>
                                <% count++; %>
                            </logic:iterate>
                            </div>
                        </td>
                     </tr>
                </logic:notEqual>
            </logic:notEmpty>
            <logic:empty name="notAppInf">
            	<logic:notEmpty name="reportInfo" property="RRepLims">
                <logic:iterate id="rRepLims" name="reportInfo" property="RRepLims">
            	<logic:equal value="${limUser.salEmp.seNo}" name="rRepLims" property="salEmp.seNo">
                <tr>
                    <td colspan="4" style="border:0; font-weight:normal;">
                        <div class="appLayer">
                        	<div class="appLayerTitle">我的批复意见</div>
                        	<logic:equal value="1" name="rRepLims" property="rrlIsappro">
                            <div>${rRepLims.rrlContent}</div>
                            <div class="gray" style="text-align:right;">批复日期：<span id="myAppDate"></span></div>
                            <script type="text/javascript">
								removeTime("myAppDate","${rRepLims.rrlOpproDate}",2);
							</script>
						   	</logic:equal>
                            <logic:equal value="0" name="rRepLims" property="rrlIsappro">
                                <form action="messageAction.do" style="margin:0px; padding:0px" id="modReport" method="post">
                                    <input type="hidden"  name="op" value="changeReport" />
                                    <input type="hidden"  name="repCode" value="${reportInfo.repCode}" />
                                    <!-- 批复成功跳转回该页面需要mark参数 -->
                                    <input type="hidden" name="mark" value="appRepInfo"/>
                                    <input type="hidden"  name="rrlId" value="${rRepLims.rrlId}" />
                                    
                                    <div style="text-align:center"><textarea name="repApproContent" rows="4" style="width:100%"></textarea></div>
                                    <div style="text-align:center; padding:5px">
                                    	<input class="butSize1" type="submit" value="确认批复" />
                                    </div>
                                </form>	
                            </logic:equal>
                        </div>
                    </td>
                </tr>
                </logic:equal>
            	</logic:iterate>
                </logic:notEmpty>
            </logic:empty>
        </table>
        <script type="text/javascript">
        	removeTime("inpDate","${reportInfo.repDate}",2);
        </script>
    </div>
    </logic:notEmpty>
    <logic:empty name="reportInfo">
        <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该报告已被删除</div>
    </logic:empty>
  </body>
</html>

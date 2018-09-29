<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
Page pageobj=(Page)request.getAttribute("page"); 
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>仓库设置</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/wms.js"></script>
    <script type="text/javascript">
    	
		createProgressBar();
		window.onload=function(){
			
			closeProgressBar();
		}
    </script>
  </head>
  
  <body>
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;库存管理 > 仓库设置</div>
				<table class="mainTab" cellpadding="0" cellspacing="0">
                    <tr>
                        <th>
                            <div id="tabType">
                                <div id="tabType1" class="tabTypeWhite" onClick="self.location.href='wmsManageAction.do?op=wmsStroSearch'">
                                    仓库列表
                                </div>
                                <div id="tabType2" style="padding:0px; cursor:default">
                                </div>
                            </div>
                        </th>
                        <td>
                            <a href="javascript:void(0)" onClick="wmsPopDiv(1);return false;" class="newBlueButton">新建仓库</a>
                        </td>
                    </tr>
            	</table>
			 	<div id="listContent">
                    <logic:notEmpty name="wmsStro">
                        <ul class="ulHor">	
                            <logic:iterate id="wm" name="wmsStro" scope="request">
                                <li style="padding:20px;">
                                    <div class="lightGrayBack" onMouseOver="this.className='blueBox'" onMouseOut="this.className='lightGrayBack'" style="width:120px; height:125px;  padding:5px">
                                        <div style="text-align:right">
                                            <span>&nbsp;<img onClick="wmsPopDiv(12,'${wm.wmsCode}')" style="cursor:pointer" src="images/content/detail.gif" border="0" alt="查看详细"/>&nbsp;</span>
                                            <span>&nbsp;<img onClick="wmsPopDiv(11,'${wm.wmsCode}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑"/>&nbsp;</span>
                                            <span>&nbsp;<img onClick="wmsDelDiv(1,'${wm.wmsCode}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/></span>
                                        </div>
                                        <div class="bold autoBr" style="cursor:pointer" onClick="wmsPopDiv(12,'${wm.wmsCode}')" title="${wm.wmsName}">
                                            <img src="images/icon/sys/stro.gif"/><br/>
                                            <span class="textOverflow">${wm.wmsName}</span>
                                            <logic:notEmpty name="wm" property="wmsStroType"><div class="brown textOverflow">[${wm.wmsStroType.typName}]</div></logic:notEmpty>
                                        </div>
                                     </div>
                                 </li>
                            </logic:iterate>
                        </ul>
                        <div class="HackBox"></div>
                	</logic:notEmpty>
                    <logic:empty name="wmsStro">
                        <div class="dataList gray" style="padding-top:30px; text-align:center">	
                            暂未添加仓库...
                        </div>
                    </logic:empty>  
                    <div class="tipsLayer" style="margin:5px 15px 0px 15px;">
                        <ul>
                            <li>①&nbsp;不要轻易关闭仓库。</li>
                            <li>②&nbsp;关闭仓库之前请确认该仓库没有产品。</li>
                            <li>③&nbsp;仓库关闭后，库存流水仍可看到，但是仓库名字为空。</li>
                        </ul>
                    </div>           
        		</div>
  			</div> 
	 	</div>
  </body>
  <script type="text/javascript">loadTabTypeWidth();</script>
</html>

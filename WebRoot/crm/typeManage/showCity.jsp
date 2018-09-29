<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	int count=0;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache"/>
	<meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="expires" content="0"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/typ.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/type.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript">
    	
		createProgressBar();
   		window.onload=function(){
			
			closeProgressBar();
		}
    </script>
	<title>城市</title>
    <link rel="shortcut icon" href="favicon.ico"/>
</head>

<body>
	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;系统设置 > 类别管理 > 城市</div>
            <table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                    <div id="tabType">
                        <div id="tabType1" class="tabTypeWhite" style="cursor:default">类别管理</div>
                    </div>
                    </th>
                    <td id="addCity"><a href="javascript:void(0)" onClick="addDivNew(4);return false;" class="newBlueButton">添加城市</a></td>
                	<script type="text/javascript">displayLimAllow("sy012","addCity")</script>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
                <table class="normal"align="center" border="0" cellpadding="0" cellspacing="0" width="100%">
                    <tr>
                        <td class="typeLeftTd">
                        	<!-- 左侧菜单序号,显示左侧选中状态 -->
                        	<input type="hidden" id="typeId" value="43"/>
                        	<script type="text/javascript">createIfmLoad('ifmLoad');</script>
                        	<iframe onload="loadEnd('ifmLoad')" src="typeManage/typeMenu.jsp" frameborder="0" scrolling="no" class="typeLeftMenu"></iframe>
                        </td>
                        <td style="vertical-align:top">
                            <div class="typeRightForm">
                            <logic:notEmpty name="msg">
                        		<div class="updSuc">修改成功！</div>
                            </logic:notEmpty>
                            <form action="customAction.do" id="typeSaveForm" method="post">
                            <input type="hidden" name="op" value="modifyCity"/>
                            <table class="normal rowstable noBr" cellpadding="0" cellspacing="0">
                                <tr>
                                	<th style="width:5%;">ID</th>
                                    <th style="width:5%;">是否启用</th>
                                    <th style="width:25%;">国家名称</th>
                                    <th style="width:25%;">省份名称</th>
                                    <th style="width:40%;border-right:0px">城市名称</th>
                                </tr>
                                <logic:notEmpty name="typeList">
                                    <logic:iterate id="typeList" name="typeList" offset="1">
                                        <tr id="tr<%= count%>" onMouseOver="focusTr2('tr',<%= count%>,1)" onMouseOut="focusTr2('tr',<%= count%>,0)">
                                        	<td style="text-align:center">${typeList.cityId}&nbsp;</td>
                                            <td style="text-align:center"><input type="checkbox" name="enabledType" ${typeList.enabledType} value="${typeList.cityId}" /></td>
                                            <td>${typeList.cusProvince.cusArea.areName}&nbsp;</td>
                                            <td>${typeList.cusProvince.prvName}&nbsp;</td>
                                            <td>
                                                <input type="hidden" name="typeId" value="${typeList.cityId}" />
                                                <input type="text" name="typeName" class="inputSize2" style="width:90%" value="<c:out value="${typeList.cityName}"/>"  onblur="nameCheck(this)"/>
                                            </td>
                                        </tr>
                                        <script type="text/javascript">
                                            rowsBg2('tr',<%=count%>);
                                        </script>
                                        <% count++; %>
                                    </logic:iterate>
                                    <logic:notEmpty name="isEmpty">
                                        <tr>
                                            <td colspan="5" class="noDataTd">暂未添加城市...</td>
                                        </tr>
                                    </logic:notEmpty>
                                    <logic:empty name="isEmpty">
                                        <tr>
                                            <td id="saveCity" colspan="5" style="text-align:center" class="orangeBack"><input id="doSave" type="button" class="butSize1" value="保存" onClick="doSubmit()" /></td>
                                        	<script type="text/javascript">displayLimAllow("sy013","saveCity")</script>
                                        </tr>
                                    </logic:empty>
                                </logic:notEmpty>
                                <logic:empty name="typeList">
                                    <tr>
                                        <td colspan="5" class="noDataTd">暂未添加城市...</td>
                                    </tr>
                               </logic:empty>
                            </table>
                        </form>
                        </div>
                        </td>
                    </tr>
                </table>
        </div>
	   </div>
	</div>
	</body>
    
</html>

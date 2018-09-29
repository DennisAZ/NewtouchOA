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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
	<title>省份</title>
    <link rel="shortcut icon" href="favicon.ico"/>
</head>

<body>
	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;系统设置 > 类别管理 > 省份</div>
			<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                    <div id="tabType">
                        <div id="tabType1" class="tabTypeWhite" style="cursor:default">类别管理</div>
                    </div>
                    </th>
                    <td id="addProv"><a href="javascript:void(0)" onClick="addDivNew(5);return false;" class="newBlueButton">添加省份</a></td>
                	<script type="text/javascript">displayLimAllow("sy012","addProv")</script>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
			<div id="listContent">
				<table class="normal" cellpadding="0" cellspacing="0" style="width:100%">
                    <tr>
                        <td class="typeLeftTd">
                        	<!-- 左侧菜单序号,显示左侧选中状态 -->
                        	<input type="hidden" id="typeId" value="42"/>
                        	<script type="text/javascript">createIfmLoad('ifmLoad');</script>
                        	<iframe onload="loadEnd('ifmLoad')" src="typeManage/typeMenu.jsp" frameborder="0" scrolling="no" class="typeLeftMenu"></iframe>
                        </td>
                        <td style="vertical-align:top">
                            <div class="typeRightForm">
                            <logic:notEmpty name="msg">
                        		<div class="updSuc">修改成功！</div>
                            </logic:notEmpty>
                            <form action="customAction.do" id="typeSaveForm" method="post">
                            <input type="hidden" name="op" value="modifyProvince"/>
                            <table class="normal rowstable noBr" cellpadding="0" cellspacing="0">
                                <tr>
                                	<th style="width:5%;">ID</th>
                                    <th style="width:5%;">是否启用</th>
                                    <th style="width:30%;">国家名称</th>
                                    <th style="width:40%;">省份名称</th>
                                    <th style="width:20%;border-right:0px">添加城市</th>
                                </tr>
                                <logic:notEmpty name="typeList">
                                    <logic:iterate id="typeList" name="typeList" offset="1">
                                        <tr id="tr<%= count%>" onMouseOver="focusTr2('tr',<%= count%>,1)" onMouseOut="focusTr2('tr',<%= count%>,0)">
                                        	<td style="text-align:center">${typeList.prvId}&nbsp;</td>
                                            <td><input type="checkbox" name="enabledType" ${typeList.enabledType} value="${typeList.prvId}"/></td>
                                            <td>${typeList.cusArea.areName}&nbsp;</td>
                                            <td>
                                                <input type="hidden" name="typeId" value="${typeList.prvId}" />
                                                <input type="text" name="typeName" value="<c:out value="${typeList.prvName}"/>" class="inputSize2" style="width:90%" onBlur="nameCheck(this)"/>
                                            </td>
                                            <td class="blue">[<a href="javascript:void(0);" onClick="addDivNew(4,'${typeList.prvId}');return false;">添加下级区县</a>]</td>
                                        </tr>
                                        <script type="text/javascript">
                                            rowsBg2('tr',<%=count%>);
                                        </script>
                                        <% count++; %>
                                    </logic:iterate>
                                    <logic:notEmpty name="isEmpty">
                                        <tr>
                                           <td colspan="5" class="noDataTd">暂未添加省份...</td>
                                        </tr>
                                    </logic:notEmpty>
                                    <logic:empty name="isEmpty">
                                         <tr>
                                            <td id="saveProv" colspan="5" style="text-align:center" class="orangeBack"><input id="doSave" type="button" class="butSize1" value="保存" onClick="doSubmit()" /></td>
                                        	<script type="text/javascript">displayLimAllow("sy013","saveProv")</script>
                                        </tr>
                                    </logic:empty>
                                </logic:notEmpty>
                                <logic:empty name="typeList">
                                    <tr>
                                        <td colspan="5" class="noDataTd">暂未添加省份...</td>
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

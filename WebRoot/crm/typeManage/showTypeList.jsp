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
    <title>类别列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
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
		//加载类别标识名称
		function loadTypTxt(){
			var typTxt=$("typTxt").value;
			$("typTxt_1").innerHTML=typTxt;
			if($("typTxt_2")!=null){
				$("typTxt_2").innerHTML="添加"+typTxt;
			}
			$("typTxt_3").innerHTML=typTxt;
		}
		
		//新建类别
		function toAddType(){
			addDivNew(1,$("typTxt").value);
		}
		
		//锁定第一行数据
		function loadLock(obj){
			var tds = obj.childElements();
			tds[1].childElements()[0].disabled=true;
			tds[1].childElements()[0].checked=true;
			var checkEl = document.createElement("input");
			checkEl.type="checkbox";
			checkEl.name="enabledType";
			checkEl.style.display="none";
			checkEl.value=tds[1].childElements()[0].value;
			tds[1].appendChild(checkEl);
			checkEl.checked=true;
			tds[2].childElements()[0].disabled=true;
			var hideEl = document.createElement("input");
			hideEl.type="hidden";
			hideEl.name="typeName";
			hideEl.value=tds[2].childElements()[0].value;
			tds[2].appendChild(hideEl);
		}
		
    	
		createProgressBar();
   		window.onload=function(){
			
			if("${isLock}"!=""&&"${isLock}"=="1"){
				loadLock($("tr0"));
			}
			loadTypTxt();
			closeProgressBar();
		}
    </script>
	
</head>

<body>
	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;系统设置 > 类别管理 > <span id="typTxt_1"></span></div>
			<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                    <div id="tabType">
                        <div id="tabType1" class="tabTypeWhite" style="cursor:default">类别管理</div>
                    </div>
                    </th>
                    <logic:notEmpty name="isInsert">
                    <logic:equal name="isInsert" value="1">
                    <td id="addType"><a href="javascript:void(0)" id="typTxt_2" onClick="toAddType();return false;" class="newBlueButton"></a></td>
                     <script type="text/javascript">displayLimAllow("sy012","addType")</script>
                    </logic:equal>
                    </logic:notEmpty>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
			<div id="listContent">
				<table class="normal" cellpadding="0" cellspacing="0" style="width:100%">
                    <tr>
                        <td class="typeLeftTd">
                        	<input type="hidden" id="typTxt" name="typTxt" value="<c:out value="${typTxt}"/>"/><!-- 类别标识名称 -->
                        	<script type="text/javascript">createIfmLoad('ifmLoad');</script>
                        	<iframe onload="loadEnd('ifmLoad')" src="typeManage/typeMenu.jsp" frameborder="0" scrolling="no"></iframe>
                        </td>
                        <td style="vertical-align:top">
							<div class="typeRightForm">
                            <logic:notEmpty name="msg">
                        		<div class="updSuc">修改成功！</div>
                            </logic:notEmpty>
                            <form action="typeAction.do" id="typeSaveForm" method="post">
                                <input type="hidden" name="op" value="modifyType"/>
                                <input type="hidden" name="isInsert" value="${isInsert}"/>
                                <input type="hidden" name="isEdit" value="${isEdit}"/>
                                <input type="hidden" name="isLock" value="${isLock}"/>
                                <!-- 类别标识,显示左侧选中状态 -->
                                <input type="hidden" id="typeId" name="typType" value="${typType}"/><!-- 类别标识 -->
                                <table class="normal rowstable noBr" cellpadding="0" cellspacing="0">
                                    <tr>
                                    	<th style="width:5%">ID</th>
                                        <th style="width:10%">是否启用</th>
                                        <th style="width:85%; border-right:0px" id="typTxt_3"></th>
                                    </tr>
                                    <logic:notEmpty name="typeList">
                                    <logic:iterate id="typeList" name="typeList">
                                        <tr id="tr<%= count%>" onMouseOver="focusTr2('tr',<%= count%>,1)" onMouseOut="focusTr2('tr',<%= count%>,0)">	
                                        	<td style="text-align:center">${typeList.typId}&nbsp;</td>
                                            <td style="text-align:center"><input type="checkbox" name="enabledType" ${typeList.enabledType} value="${typeList.typId}" /></td>
                                            <td>
                                                <input type="text" name="typeName" value="<c:out value="${typeList.typName}"/>" class="inputSize2" style="width:90%"  onblur="nameCheck(this)"/>
                                                <input type="hidden" name="typeId" value="${typeList.typId}"/>
                                            </td>
                                        </tr>
                                        <script type="text/javascript">
											<logic:equal name="typeList" property="typDesc" value="lock">
											loadLock($('tr<%=count%>'));
											</logic:equal>
                                            rowsBg2('tr',<%=count%>);
                                        </script>
                                        <% count++; %>
                                    </logic:iterate>
                                    <tr>
                                        <td id="saveType" colspan="3" style="text-align:center" class="orangeBack"><input id="doSave" type="button" class="butSize1" value="保存" onClick="doSubmit()" /></td>
                                    	<script type="text/javascript">displayLimAllow("sy013","saveType")</script>
                                    </tr>
                                    </logic:notEmpty>
                                    <logic:empty name="typeList">
                                        <tr>
                                            <td colspan="3" class="noDataTd">暂未添加类别...</td>
                                        </tr>
                                   </logic:empty>
                                </table>
                            </form>
                            <logic:equal value="3" name="typType">
                            	<div class="tipsLayer" style="width:98%; margin-top:5px;">
                                	<ul><li>请保持机会阶段的顺序，交换机会阶段的顺序会引起数据错乱。</li></ul>
                                </div>
                            </logic:equal>
                            </div>
                            
                            
						</td>
					</tr>
				</table>
			</div>
	   </div>
	</div>
	</body>
    
</html>

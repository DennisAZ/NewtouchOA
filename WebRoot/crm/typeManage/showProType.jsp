<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
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
		function addType(){
			if(isEmpty("wpt")){
				alert("请填写下级类别名称！");
				return false;
			}
			else if(checkLength("wpt",50)){
				alert("下级类别名称不能超过50字！");
				return false;
			}
			else{
				return $("typeSaveForm").submit();
			}
		}
		function modifyType(){
			if(isEmpty("wpt1")){
				alert("请填写类别名称！");
				return false;
			}
			else if(checkLength("wpt1",50)){
				alert("类别名称不能超过50字！");
				return false;
			}
			else{
				return $("typeSaveForm1").submit();
			}
		}
		function modifyUpType(){
		    var currProId='${wmsProType.wptId}';
		    var upTypeId=$("upTypeId").value;
		    var oldUpTypeId='${wmsProType.wmsProType.wptId}';
		    if(upTypeId==oldUpTypeId){
		      alert("您未做任何修改！");
		      return false;
		     }
			if(currProId==upTypeId){
				alert("不能将自己设为自己的上级！");
				return false;
			}
			else{
				return $("modUpTypeForm").submit();
			}
		}

		function delType(id){
			if(confirm("确认删除此类别吗？")){
				window.location.href="wwoAction.do?op=delProType&wptId="+id;
			}
			return false;
		}
    	
		createProgressBar();
   		window.onload=function(){
			
			closeProgressBar();
		}
    </script>
	<title>产品类别</title>
    <link rel="shortcut icon" href="favicon.ico"/>
</head>

<body>
<div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;系统设置 > 类别管理 > 产品类别</div>
			<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                    <div id="tabType">
                        <div id="tabType1" class="tabTypeWhite" style="cursor:default">类别管理</div>
                    </div>
                    </th>
                    <td id="addCat"><a href="javascript:void(0)" onClick="addDivNew(2);return false;" class="newBlueButton">添加新分类</a></td>
                    <script type="text/javascript">displayLimAllow("sy012","addCat")</script>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
			<div id="listContent">
				<table class="normal" cellpadding="0" cellspacing="0" style="width:100%">
                    <tr>
                        <td class="typeLeftTd">
                        	<!-- 左侧菜单序号,显示左侧选中状态 -->
                        	<input type="hidden" id="typeId" value="14"/>
                        	<script type="text/javascript">createIfmLoad('ifmLoad1');</script>
                        	<iframe onload="loadEnd('ifmLoad1')" src="typeManage/typeMenu.jsp" frameborder="0" scrolling="no" class="typeLeftMenu"></iframe>
                        </td>
						<td style="width:250px;vertical-align:top"><script type="text/javascript">createIfmLoad('ifmLoad2');</script>
                        	<iframe onload="loadEnd('ifmLoad2')" src="wwoAction.do?op=proTypeList" scrolling="no" frameborder="0" style="height:450px; width:250px;"></iframe></td>
                        <td style="vertical-align:top; padding-top:15px; padding-right:30px;">
                        <div style="padding:10px; text-align:left;width:450px;" class="grayBack">
                        <c:if test="${!empty wmsProType}">
                            <div>
                            	<table class="normal" style="width:450px;">
                                	<tr>
                                    	<td class="blue bold middle"><span class="gray">当前类别：</span><c:out value="${wmsProType.wptName}"/></td>
                                        <td style="text-align:right"><span style="cursor:pointer; padding:2px;" onMouseOver="this.className='redBack'" onMouseOut="this.className='red'" class="red" onClick="delType('${wmsProType.wptId}')"/>删除此类别</span></td>
                                    </tr>
                                </table>
                            </div>
                            <div style="padding:5px">
                            	<div class="blue">添加下级类别</div>
                            	<form action="wwoAction.do" id="typeSaveForm" method="post" style="padding:0px; margin:0px">
                                    <input type="hidden" name="op" value="addProType"/>
                                    <input type="hidden" name="wptId" value="${wmsProType.wptId}"/>
                                    <input type="text" class="inputSize2 inputBoxAlign" name="wptName" id="wpt" style="width:300px" onBlur="autoShort(this,50)"/>
                                    &nbsp;<input class="butSize3 inputBoxAlign" type="button" value="点击添加" onClick="addType()">
                                 </form>
							</div>
							  <div style="padding:5px">
                            	<div class="blue">上级类别(双击文本框可以取消上级)</div>
                            	<form action="wwoAction.do" id="modUpTypeForm" method="post" style="padding:0px; margin:0px">
                                    <input type="hidden" name="op" value="modUpProType"/>
                                    <input type="hidden" name="currWptId" value="${wmsProType.wptId}"/>
                                    <input type="hidden" name="upTypeId" id="upTypeId" value="${wmsProType.wmsProType.wptId}"/>
                                    <input readonly type="text" class="inputSize2 inputBoxAlign lockBack" name="upTypeName" id="upTypeName" value="<c:out value="${wmsProType.wmsProType.wptName}"/>" ondblClick="clearInput(this,'upTypeId')" title="此处文本无法编辑，双击可清空文本" style="width:221px"/>&nbsp;<button class="butSize2 inputBoxAlign" onClick="addDivBrow(16,2)">点击选择</button>
                                    &nbsp;<input class="butSize3 inputBoxAlign" type="button" value="点击保存" onClick="modifyUpType()">
                                 </form>
							</div>
                            <div style="padding:5px">
								<div class="blue">修改当前类别</div>
                            	<form action="wwoAction.do" id="typeSaveForm1" method="post" style="padding:0px; margin:0px">
                                    <input type="hidden" name="op" value="modifyProType"/>
                                    <input type="hidden" name="wptId" value="${wmsProType.wptId}"/>
                                    <input type="text" class="inputSize2 inputBoxAlign" name="wptName" value="<c:out value="${wmsProType.wptName}"/>" id="wpt1" style="width:300px" onBlur="autoShort(this,50)"/>
                                    &nbsp;<input id="fixButton" class="butSize3 inputBoxAlign" type="button" value="点击修改" onClick="modifyType()">
                                    <script type="text/javascript">displayLimAllow("sy013","fixButton")</script>
                                </form>
                            </div>
						</c:if>
                        <c:if test="${empty wmsProType}">
						<div style="padding:5px; text-align:left" class="blue middle"><img src="images/content/toLeft.gif" style="vertical-align:middle; margin-bottom:3px;"/>&nbsp;选择左侧列表中类别进行编辑</div>
						</c:if>
                        </div>
						</td>
					</tr>
				</table>
			</div>
	   </div>
	</div>
	</body>
</html>

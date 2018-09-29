<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>权限分配</title>
    <link rel="shortcut icon" href="favicon.ico"/>   
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/rights.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
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
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;系统设置 > 权限分配</div>
			<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeWhite">
                                权限分配
                            </div>
                        </div>
                    </th>
                </tr>
            </table>
			<div id="listContent">
            	<div id="leftMenu">
                   <script type="text/javascript">createIfmLoad('ifmLoad');</script>
                   <iframe src="lim/funList.jsp" onload="loadAutoH(this,'ifmLoad')" frameborder="0" scrolling="no"></iframe>
                </div>
                <div id="rightContent">
					<div id="userDiv" class="blueBorder">
				    	<div class="blueBorderTop">&nbsp;账号信息&nbsp;&nbsp;&nbsp;</div>
                        <div class="grayBack" style="text-align:center">
                            <table id="descTab" class="normal dashborder" style="width:95%" cellpadding="0" cellspacing="0">
                                <tr>
                                     <th style="width:80px;">账号：</th>
                                     <td style="width:40%"><a href="userAction.do?op=userManage">${user.userCode}&nbsp;</a></td>
                                     <th style="width:80px;"><nobr>账号对应职位：</nobr></th>
                                     <td style="width:40%">${user.limRole.rolName}&nbsp;</td>
                                </tr>
                                 <tr>
                                     <th>登陆名：</th>
                                     <td>${user.userLoginName}&nbsp;</td>
                                     <th>使用人姓名：</th>
                                     <td>${user.salEmp.seName}&nbsp;</td>
                                </tr>
                                 <tr>
                                     <th style="border:0px"><nobr>使用人职位：</nobr></th>
                                     <td style=" border:0px">${user.salEmp.limRole.rolName}&nbsp;</td>
                                     <th style="border:0px">所属部门：</th>
                                     <td style=" border:0px">${user.salOrg.soName}&nbsp;</td>
                                </tr>
                            </table>
                        </div>
                        <div id="rightsList">
                      	<form action="userAction.do" id="sub">
                      		<input type="hidden" name="op" value="setFunLim">
                      		<input type="hidden" name="userCode" id="userCode" value="${user.userCode}">
                      		<input type="hidden" id="typeId" name="funType"  value="${funType}">
                            <span id="rightsTitle">项目管理</span>&nbsp;
                            <logic:iterate id="limRight" name="proOperList" >
                            <logic:equal value="pro005" name="limRight" property="limFunction.funCode">
                            <input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}&nbsp;</label>
                            </logic:equal>
                            <% count++; %>
                            </logic:iterate>
                            <div id="rightsTab">
								<table class="normal">	
                                    <tr>
				          				<th colspan="2"><input type="checkbox" name="checkAll" id="checkAll1" onClick="cascadeSel('pro1','checkAll1')"><label for="checkAll1">项目</label></th>
			           				</tr>
									<tr>
										<td id="pro1" colspan="4">
						   					<logic:iterate id="limRight" name="proOperList" >
						    				<logic:equal value="pro001" name="limRight" property="limFunction.funCode">
											<logic:notEqual value="p005" name="limRight" property="rigCode">	
											<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}&nbsp;</label>
											</logic:notEqual>
											<logic:equal value="p005" name="limRight" property="rigCode">
							    			<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}项目&nbsp;</label>
											</logic:equal>
						   					</logic:equal>
						   					<logic:equal value="pro007" name="limRight" property="limFunction.funCode">
						   					<logic:equal value="p018" name="limRight" property="rigCode">
							    			<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">查看项目日志&nbsp;</label>
											</logic:equal>
											<logic:equal value="p019" name="limRight" property="rigCode">
							    			<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">查看项目工作&nbsp;</label>
											</logic:equal>
											<logic:equal value="p026" name="limRight" property="rigCode">
							    			<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">查看项目订单&nbsp;</label>
											</logic:equal>
											<logic:equal value="p027" name="limRight" property="rigCode">
							    			<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">查看项目报价记录&nbsp;</label>
											</logic:equal>
											<logic:equal value="p028" name="limRight" property="rigCode">
							    			<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">查看项目询价单&nbsp;</label>
											</logic:equal>
											<logic:equal value="p029" name="limRight" property="rigCode">
							    			<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">查看项目采购单&nbsp;</label>
											</logic:equal>
						   					</logic:equal>
											<% count++; %>
					      					</logic:iterate>
										</td>
									</tr>
						  			<tr>
				          				<th colspan="2"><input type="checkbox" name="checkAll" id="checkAll2" onClick="cascadeSel('pro2','checkAll2')"><label for="checkAll2">子项目</label></th>
			            			</tr>
									<tr>
										<td id="pro2" colspan="4">
						    				<logic:iterate id="limRight" name="proOperList" >
						        			<logic:equal value="pro002" name="limRight" property="limFunction.funCode">
								 			<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}&nbsp;</label>
						   	    			</logic:equal>
                                			<% count++; %>
					       					</logic:iterate>
										</td>
									</tr>
					   				<tr>
				          				<th colspan="2"><input type="checkbox" name="checkAll" id="checkAll3" onClick="cascadeSel('pro3','checkAll3')"><label for="checkAll3">项目工作</label></th>
			           				</tr>
									<tr>
										<td id="pro3" colspan="4">
						   				<logic:iterate id="limRight" name="proOperList" >
						       			<logic:equal value="pro003" name="limRight" property="limFunction.funCode">
										<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}&nbsp;</label>
						   	   			</logic:equal>
                               			<% count++; %>
					      				</logic:iterate>
										</td>
									</tr>
						 			<tr>
				          				<th colspan="2"><input type="checkbox" name="checkAll" id="checkAll4" onClick="cascadeSel('pro4','checkAll4')"><label for="checkAll4">项目日志</label></th>
			          				</tr>
									<tr>
										<td id="pro4" colspan="4">
						   					<logic:iterate id="limRight" name="proOperList" >
						       				<logic:equal value="pro006" name="limRight" property="limFunction.funCode">
											<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}&nbsp;</label>
						   	   				</logic:equal>
                               				<% count++; %>
					      					</logic:iterate>
										</td>
									</tr>
									<tr>
										<td  colspan="4" style="text-align:center">
                                      		<input class="butSize1" type="button" id="modButton" value="保存" onClick="document.getElementById('sub').submit()">&nbsp;
										</td>
									</tr>
								</table>
                            </div>
						</form>
                        </div>
					</div>
        		</div>
  			</div> 
	 	</div>
	</div>
  </body>
</html>

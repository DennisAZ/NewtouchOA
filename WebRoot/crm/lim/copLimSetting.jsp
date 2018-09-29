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
    <title>oa权限分配</title>
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
                                     <th>账号：</th>
                                     <td style="width:40%"><a href="userAction.do?op=userManage">${user.userCode}&nbsp;</a></td>
                                     <th><nobr>账号对应职位：</nobr></th>
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
                            <span id="rightsTitle">协同办公</span>&nbsp;
                            <logic:iterate id="limRight" name="copOperList" >
                                <logic:equal value="cop006" name="limRight" property="limFunction.funCode">
                                		<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}&nbsp;</label>
                                </logic:equal>
                                <% count++; %>
                            </logic:iterate>
                           	<div id="rightsTab">
                                <table class="normal">	
                                    <tr>
                                        <th><input type="checkbox" name="checkAll" id="checkAll1" onClick="cascadeSel('cop1','checkAll1')"><label for="checkAll1">新闻公告</label></th>
                                    </tr>
                                    <tr>
                                        <td id="cop1">
                                        <logic:iterate id="limRight" name="copOperList" >
                                            <logic:equal value="cop001" name="limRight" property="limFunction.funCode">
                                            	<logic:equal value="ac020" name="limRight" property="rigCode">
                                                	<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}&nbsp;</label>
                                                </logic:equal>
                                            	<logic:equal value="c001" name="limRight" property="rigCode">
                                                	<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}新闻公告&nbsp;</label>
                                                </logic:equal>
                                                <logic:equal value="c004" name="limRight" property="rigCode">
                                                	<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">查看新闻公告&nbsp;</label>
                                                </logic:equal>
                                            </logic:equal>
                                            <% count++; %>
                                        </logic:iterate>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th><input type="checkbox" name="checkAll" id="checkAll2" onClick="cascadeSel('cop2','checkAll2')"><label for="checkAll2">日程</label></th>
                                    </tr>
                                    <tr>
                                        <td id="cop2">
                                        <logic:iterate id="limRight" name="copOperList" >
                                            <logic:equal value="cop002" name="limRight" property="limFunction.funCode">
                                            <input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}&nbsp;</label>
                                            </logic:equal>
                                            <% count++; %>
                                        </logic:iterate>
                                        </td>
                                    </tr>
                                    <!--<tr>
                                        <th><input type="checkbox" name="checkAll" id="checkAll3" onClick="cascadeSel('cop3','checkAll3')"><label for="checkAll3">工作安排</label></th>
                                    </tr>
                                    <tr>
                                        <td id="cop3">
                                        <logic:iterate id="limRight" name="copOperList" >
                                            <logic:equal value="cop003" name="limRight" property="limFunction.funCode">
                                            <input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}&nbsp;</label>
                                            </logic:equal>
                                            <% count++; %>
                                        </logic:iterate>
                                        </td>
                                    </tr>-->
                                    <tr>
                                        <th><input type="checkbox" name="checkAll" id="checkAll4" onClick="cascadeSel('cop4','checkAll4')"><label for="checkAll4">报告</label></th>
                                    </tr>
                                    <tr>
                                        <td id="cop4">
                                        <logic:iterate id="limRight" name="copOperList" >
                                            <logic:equal value="cop004" name="limRight" property="limFunction.funCode">
                                            <input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}&nbsp;</label>
                                        </logic:equal>
                                        <% count++; %>
                                        </logic:iterate>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th><input type="checkbox" name="checkAll" id="checkAll5" onClick="cascadeSel('cop5','checkAll5')"><label for="checkAll5">消息</label></th>
                                    </tr>
                                    <tr>
                                        <td id="cop5">
                                            <logic:iterate id="limRight" name="copOperList" >
                                                <logic:equal value="cop005" name="limRight" property="limFunction.funCode">
                                                <input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limOperate.opeDesc}&nbsp;</label>
                                            </logic:equal>
                                            <% count++; %>
                                            </logic:iterate>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="text-align:center"><input class="butSize1" type="button" id="modButton" value="保存" onClick="$('sub').submit()">&nbsp;
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

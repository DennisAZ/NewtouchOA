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
	 <script language="javascript">
	 	function selAll() {    
			var allCheck=$("allCheck");
			var chkbLim= $N("chkbLim1");
			for (var i = 0;i<chkbLim.length;i++){
				if (allCheck.checked == true)
				   chkbLim[i].checked = true;  
				else
				   chkbLim[i].checked = false;
		   }
		}
   		function  checkIsAll(obj){
			var allCheck=$("allCheck");
			if(allCheck.checked==true&&obj.checked==false)
			{
				if(confirm("你已分配该用户对所有仓库可见的权限，\n如取消该仓库的权限则会取消该用户对\n所有仓库可见的权限，是否确认该操作？"))
				  allCheck.checked=false;
				else
				  obj.checked=true;
			}
	   	}
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
                          	<input type="hidden" name="setLim" value="wms"/>
                          	<input type="hidden" id="typeId" name="funType"  value="${funType}">
                            <span id="rightsTitle">库存管理</span>&nbsp;
							<logic:iterate id="limRight" name="wmsOperList" >
						      <logic:equal value="wms001" name="limRight" property="limFunction.funCode">
						      <logic:equal value="w015" name="limRight" property="rigCode">
								<input type="checkbox" name="chkbLim"  id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limFunction.funDesc}${limRight.limOperate.opeDesc}&nbsp;</label>
						      </logic:equal>
						      <logic:equal value="w001" name="limRight" property="rigCode">
								<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">仓库设置&nbsp;</label>
						      </logic:equal>
						      <logic:equal value="w002" name="limRight" property="rigCode">
								<input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">仓库数据整理&nbsp;</label>
						      </logic:equal>
						      </logic:equal>
                              <% count++; %>
					        </logic:iterate>
                            <div id="rightsTab">
								<table class="normal">	
									<tr>
				          				<th>仓库可见权限</th>
			           				</tr>
									<tr>
										<td>
                                           <logic:iterate id="limRight" name="wmsOperList" >
                                            <logic:equal value="wms006" name="limRight" property="limFunction.funCode">
                                            <logic:equal value="w016" name="limRight" property="rigCode">
                                                <input type="checkbox" name="chkbLim" id="allCheck" onClick="selAll()" value="${limRight.rigCode}" ${limRight.enabledType}><label for="allCheck">全部仓库&nbsp;${limRight.limOperate.opeDesc}</label>&nbsp;
                                            </logic:equal>
                                            <logic:notEqual value="w016" name="limRight" property="rigCode">
                                                <input type="checkbox" name="chkbLim1" id="limCB<%=count%>" onClick="checkIsAll(this)" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.rigWmsName}&nbsp;${limRight.limOperate.opeDesc}&nbsp;</label>
                                            </logic:notEqual>
                                           </logic:equal>
                                           <% count++; %>
                                          </logic:iterate>
										</td>
									</tr>
						 			<tr>
				         				<th><input type="checkbox" name="checkAll" id="checkAll1" onClick="cascadeSel('wms1','checkAll1')"><label for="checkAll1">入库</label></th>
			            			</tr>
									<tr>
										<td id="wms1">
                                        <logic:iterate id="limRight" name="wmsOperList" >
                                            <logic:equal value="wms002" name="limRight" property="limFunction.funCode">
                                             <input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limFunction.funDesc}${limRight.limOperate.opeDesc}&nbsp;</label>
                                            </logic:equal>
                                            <% count++; %>
                                       </logic:iterate>
										</td>
									</tr>
					  				<tr>
				          				<th><input type="checkbox" name="checkAll" id="checkAll2" onClick="cascadeSel('wms2','checkAll2')"><label for="checkAll2">出库</label></th>
			            			</tr>
									<tr>
										<td id="wms2">
                                        <logic:iterate id="limRight" name="wmsOperList" >
                                            <logic:equal value="wms003" name="limRight" property="limFunction.funCode">
                                             <input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limFunction.funDesc}${limRight.limOperate.opeDesc}&nbsp;</label>
                                            </logic:equal>
                                            <% count++; %>
                                       </logic:iterate>
                                        </td>
									</tr>
						 			<tr>
				          				<th><input type="checkbox" name="checkAll" id="checkAll3" onClick="cascadeSel('wms3','checkAll3')"><label for="checkAll3">仓库调拨</label></th>
			            			</tr>
									<tr>
										<td id="wms3">
                                        <logic:iterate id="limRight" name="wmsOperList" >
                                            <logic:equal value="wms004" name="limRight" property="limFunction.funCode">
                                             <input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limFunction.funDesc}${limRight.limOperate.opeDesc}&nbsp;</label>
                                            </logic:equal>
                                            <% count++; %>
                                       </logic:iterate>
										</td>
									</tr>
					    			<tr>
				          				<th><input type="checkbox" name="checkAll" id="checkAll4" onClick="cascadeSel('wms4','checkAll4')"><label for="checkAll4">库存盘点</label></th>
			            			</tr>
									<tr>
										<td id="wms4">
                                        <logic:iterate id="limRight" name="wmsOperList" >
                                            <logic:equal value="wms005" name="limRight" property="limFunction.funCode">
                                             <input type="checkbox" name="chkbLim" id="limCB<%=count%>" value="${limRight.rigCode}" ${limRight.enabledType}><label for="limCB<%=count%>">${limRight.limFunction.funDesc}${limRight.limOperate.opeDesc}&nbsp;</label>
                                            </logic:equal>
                                            <% count++; %>
                                       </logic:iterate>
										</td>
									</tr>
									<tr>
										<td style="text-align:center"><input class="butSize1" type="button" id="modButton" value="保存" onClick="document.getElementById('sub').submit()">&nbsp;
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

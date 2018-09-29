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
    <title>账号分配</title>
    <link rel="shortcut icon" href="favicon.ico"/>
 	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    
    <style type="text/css">
		#rightDesc{
			padding:10px;
			float:right;
		}
		
		.blueBorderTop span{
			padding:4px;
			cursor:pointer;
		}
		#descTab td{
			height:35px;
		}
		
		.orgBorder{
			padding:0px;
		}
		
		.comLogo img{
			border:#eee 4px solid;
		}
		a.blueA:link,a.blueA:visited {
			padding:2px 5px 0px 5px; 
			height:22px;
			cursor:pointer;
			font-size:12px;
			text-decoration:none;
			color:#999;
			background:#f6f9fd;
			border:#d7dce3 1px solid;
		}
		a.blueA:hover, a.blueA:active {
			padding:2px 5px 0px 5px; 
			height:22px;
			cursor:pointer;
			font-size:12px;
			text-decoration:none;
			
			color:#234995;
			background:#fafaf9;
			border:#7187e3 1px solid;
		}

    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sys.js"></script>
	<script type="text/javascript">
		function showDiv(){
			//显示授权信息或账号信息
			if("${isDefault}"=="1"){
				$("defaultDiv").style.display="block";
			}
			else{
				$("defaultDiv").style.display="none";
				$("userDiv").style.display="block";
			}
		}
		//锁定用户
		function lockUser(code,n){
			switch(n){
				case 2:
					if(confirm("是否要锁定该用户？")){
						if('${limUser.userCode}'=='${userCode}'){
							alert('您不能锁定自身账号！')
						}else{
							window.location.href='userAction.do?op=lockUser&userCode='+code+'&islock=1';
						}
					}
					break;
				case 3:
					if(confirm("是否要解除该用户的锁定？")){
						window.location.href='userAction.do?op=lockUser&userCode='+code+'&islock=0';
					}
					break;
			}
		}
		
	 	createProgressBar();	
		window.onload=function(){
			showDiv();
			closeProgressBar();
		}
	</script>
	
  </head>
  
  <body>
  	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;系统设置 > 帐号设置
            </div>
			<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                            <div id="tabType1" class="tabTypeWhite" onClick="self.location.href='userAction.do?op=userManage'">帐号设置</div>
                        </div>
                    </th>
                    <td>
                        <a href="javascript:void(0)" onClick="addDiv(2);return false;" class="newBlueButton">添加账号</a>
                    </td>
                </tr>
            </table>
			 <div id="listContent">
				<table class="normal" align="left" cellpadding="0"  cellspacing="0" style="width:50%">
                	<tr>
                    	<td style=" padding:10px; padding-bottom:0px;">
                        	<div style="padding:4px" class="grayBack">
                         	<nobr>&nbsp;&nbsp;账号总数：<span class='red'>${allUse}</span>&nbsp;&nbsp;&nbsp;已启用：<span class='red'>${isUse}</span>&nbsp;&nbsp;&nbsp;未启用：<span class='red'>${notUse}</span></nobr></div>
                        </td>
                    </tr>
                	<tr>
						<td style="vertical-align:top; padding:10px;">
                        <script type="text/javascript">createIfmLoad('ifmLoad');</script>
                        <iframe frameborder="0" style="border:#CCCCCC 1px solid;" onload="loadAutoH(this,'ifmLoad')" scrolling="auto" width="100%" src="userAction.do?op=limUserTree&type=sysSet"></iframe>
                        </td>
                    </tr>
                </table>
				<div id="rightDesc">
                	<div style="display:none"><!-- 排版用 --></div>
                    <div id="defaultDiv" class="blueBorder" style="display:none">
                        <logic:notEmpty name="limUser">
                        <div class="blueBorderTop">
                        	<table class="normal white" width="98%">
                                <tr>
                                    <td class="bold middle" style="border:0px; vertical-align:top">授权信息</td>
                                </tr>
                        	</table>
                        </div>
                        <button class="butSize2" style=" position:absolute; top:28px; right:40px;cursor:pointer; padding-top:1px;width:90px;" onClick="addDiv(10)">导入授权信息</button>
                        <div>
                        	<table id="descTab" class="normal dashborder noBr" style="width:98%" cellpadding="0" cellspacing="0">
                            	<tr>
                                    <th style=" width:70px; vertical-align:top;"><nobr>公司logo：</nobr></th>
                                    <td class="comLogo">
                                    <logic:notEmpty name="comOrg">
                                    	<logic:notEmpty name="comOrg" property="soRemark"><img id="uploadImg" src="${comOrg.soRemark}" /></logic:notEmpty>
                                    	<logic:empty name="comOrg" property="soRemark"><img id="uploadImg" src="crm/images/nav/nav_logo.png"/></logic:empty>
                                    	<iframe style="display:block; width:100%; height:48px; margin-top:5px;" frameborder="0" src="fileAction.do?op=showImg&id=${comOrg.soCode}&type=logo"></iframe>
                                    </logic:notEmpty>
                                    </td>
                                </tr>
                                <tr>
                                    <th><nobr>公司名称：</nobr></th>
                                    <td>${comOrg.soName}&nbsp;</td>
                                </tr>
                                <!--<tr>
                                    <th>类型：</th>
                                    <td><logic:equal value="0" name="userType" >试用版</logic:equal>
                                    <logic:equal value="1" name="userType">正式版</logic:equal>&nbsp;
                                    </td>
                                </tr>-->
                                <tr>
                                    <th>用户数：</th>
                                    <td><span class="orange middle bold">${allUse}</span>&nbsp;个</td>
                                </tr>
                                <tr>
                                    <th>启用时间：</th>
                                    <td>${startDate}&nbsp;</td>
                                </tr>
                                <tr>
                                    <th style="border:0px">到期时间：</th>
                                    <td style="border:0px">${endDate}&nbsp;</td>
                                </tr>
                            </table>
                        </div>
                        </logic:notEmpty>
                    </div>
                    
                    <div id="userDiv" class="blueBorder" style="display:none">
                        <div class="blueBorderTop">
                        	<table class="normal white" width="98%">
                                <tr>
                                    <td class="middle bold"><nobr>
                                    	&nbsp;账号信息&nbsp;&nbsp;&nbsp;
                                    </nobr></td>
                                    <td width="80px">
                                    	<span id="addUser" onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" onClick="addDiv(1,'${limUser.userCode}')"><nobr><img style="vertical-align:middle" src="crm/images/content/whiteAdd.gif" alt="添加下级"/>&nbsp;添加下级</nobr></span>
                                    </td>
                                    <td width="80px">
                                    	<span onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" onClick="addDiv(3,'${limUser.userCode}')"><nobr><img style="vertical-align:middle" src="crm/images/content/whiteEdit.gif" alt="编辑账号"/>&nbsp;编辑账号</nobr></span>
                                    </td>
                                    <td width="80px"><span onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" onClick="self.location.href='userAction.do?op=getFunOperate&funType=cus&userCode=${limUser.userCode}'"><nobr><img style="vertical-align:middle" src="crm/images/content/whiteEdit.gif" alt="设置权限"/>&nbsp;设置权限</nobr></span></td>
                                    <logic:equal value="2" name="limUser" property="userIsenabled">
                                    <td style="height:15px; border:0px; padding:0px; width:80px"><span onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" style="cursor:pointer; padding:4px"  onClick="lockUser('${limUser.userCode}',3)"><nobr><img style="vertical-align:middle" src="crm/images/content/whiteClose.gif" alt="解除锁定"/>&nbsp;解除锁定</nobr></span></td>	
                                    </logic:equal>
                                     <logic:notEqual value="2" name="limUser" property="userIsenabled">
                                    <td style="height:15px; border:0px; padding:0px; width:80px"><span onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" style="cursor:pointer; padding:4px"  onClick="lockUser('${limUser.userCode}',2)"><nobr><img style="vertical-align:middle" src="crm/images/content/whiteLock.gif" alt="锁定账号"/>&nbsp;锁定账号</nobr></span></td>	
                                    </logic:notEqual>	
                                    <td width="80px"><span onMouseOver="this.className='deepBlueBg'" onMouseOut="this.className=''" onClick="addDiv(4,'${limUser.userCode}')"><nobr><img style="vertical-align:middle" src="crm/images/content/whiteClose.gif" alt="关闭账号"/>&nbsp;关闭账号</nobr></span></td>
                                    
                                </tr>
                            </table>
                        </div>
                        <logic:notEmpty name="limUser">
                        <table id="descTab" class="normal dashborder" style="width:95%" cellpadding="0" cellspacing="0">
                            <tr>
                                <th>账号ID：</th>
                                <td style="width:40%">${limUser.userCode}&nbsp;
                                	<logic:equal value="2" name="limUser" property="userIsenabled">
                                        <span class="red">[被锁定]</span>
                                    </logic:equal>
                                </td>
                                <th>上级账号：</th>
                                <td style="width:40%">
                                <logic:notEmpty name="limUser" property="limUser">
                                    <logic:notEmpty name="limUser" property="limUser.userSeName">
                                        <span class="bold">${limUser.limUser.userSeName}</span>
                                    </logic:notEmpty>
                                    [${limUser.limUser.userCode}]
                                </logic:notEmpty>
                                <logic:empty name="limUser" property="limUser">
                                	无
                                </logic:empty>
                                </td>
                            </tr>
                            <tr>
                            	<th><nobr>使用人姓名：</nobr></th>
                                <td>${limUser.salEmp.seName}&nbsp;</td>
                            	<th>所属部门：</th>
                                <td>${limUser.salOrg.soName}&nbsp;</td>
                            </tr>
                            <tr>
                                <th style="width:80px;"><nobr>对应职位：</nobr></th>
                                <td style="width:40%">${limUser.limRole.rolName}&nbsp;</td>
                                <th>对应职级：</th>
                                <td>${limUser.limRole.rolLev}&nbsp;</td>	
                            </tr>
                            
                            <tr>
                            	<th>登录名：</th>
                                <td>${limUser.userLoginName}&nbsp;</td>
                                <th><nobr>最近登录IP：</nobr></th>
                       			<td>${limUser.userIp}&nbsp;</td>
                            </tr>         
                            <tr>
                            	<th>登录状态：</th>
                                <td colspan="3">
                                <logic:notEmpty name="limUser" property="userIsLogin">
                                    <logic:equal value="1" name="limUser" property="userIsLogin"><span class="deepGreen">在线</span></logic:equal>
                                    <logic:equal value="0" name="limUser" property="userIsLogin">离线</logic:equal>
                                </logic:notEmpty>
                                <logic:empty name="limUser" property="userIsLogin">
                                    <span class="gray">从未登录</span>
                                </logic:empty>
                                &nbsp;</td>
                            </tr>
                            <tr>
                            	<th style="border:0px;">备注：</th>
                                <td colspan="3" style="border:0px">${limUser.userDesc}&nbsp;</td>
                            </tr>
                        </table>
                        </logic:notEmpty>
                    </div>
                    <div class="HackBox"></div>
                   
                </div>
        	</div>
  		</div> 
	 </div>
  </body>
  <script type="text/javascript">loadTabTypeWidth();</script>
</html>

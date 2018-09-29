<%@ page language="java" import="java.util.*"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//清缓存
	response.setHeader("Pragma","No-cache");   
	response.setHeader("Cache-Control","no-cache");   
	response.setDateHeader("Expires", 0);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>导航</title>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css">
    <style type="text/css">
		.iconGray {
			vertical-align:middle;
			/*filter:gray;*/
			FILTER: alpha(opacity=70);
			opacity:0.7;
		}
		.iconOver {
			vertical-align:middle;
		}
    	/*=== 导航 ===*/
		#contentbox{
			background:url(images/nav/nav_back.png) repeat-x top;
			width:100%;
			height:80px;
			text-align:left;
		}
		/* logo */
		#navLogo{
			margin-left:10px;
			width:600px;
			height:50px;
		}
		
		/* 导航菜单 */
		#menu {
			position:absolute;
			top:50px;
			margin:0px;
			padding:0px;
			width:100%;
		}
		#menu table {
			width:100%;
			border:0;
			
		}
		
		#funMenus {
			background:url(images/nav/menu_r_border.gif) right no-repeat;
			padding:0 1px 0 0;
		}		
		.menuNormal,.menuCur,.menuOver{
			background:url(images/nav/menu.gif) left no-repeat;
			height:29px;
			width:75px;
			vertical-align:top;
			padding:9px 0 0 0;
			font-size:12px;
			text-align:center;
			cursor:pointer;
			color:#fff;
		}
		
		.menuCur{
			background:url(images/nav/menu_cur.png) no-repeat;
			padding:10px 0 0 0;
		}
		
		.menuOver{
			background:url(images/nav/menu_over.png) no-repeat;
		}
		
		/* 用户登录信息 */
		#topNotice {
			margin:0px;
			padding:0px;
			display:inline;
			text-align:right;
			height:30px;
			position:absolute;
			top:15px;
			right:25px;
		}
		/* 用户功能菜单 */
		#iconMenu {
			/*padding-right:15px;*/
		}
		#iconMenu span {
			height:25px;
			width:50px;
			text-align:left;
		}
		#iconMenu img {
			cursor:pointer;
		}
		
		
		/* 欢迎语 */
		#welcomeInf {
			text-align:left;
			width:325px;
			padding:2px 16px 0 0;
		}
		#welcomeInf div {
			padding:2px 8px 2px 8px;
			border:#666666 1px solid;
			background:#fff;
			font-size:12px;
		}
		
		/* 最小化导航栏 */
		#hideNav{
			position:absolute;
			top:30px;
			right:5px;
			width:50px;
			height:16px;
			padding-top:2px;
			padding-left:5px;
			background-image:url(images/nav/hideButton.gif);
			background-repeat:no-repeat;
			background-position:right;
			font-size:12px;
			cursor:pointer;
		}
		#restoreNav {
			margin-top:4px;
			margin-right:15px;
			padding-top:2px;
			padding-right:20px;
			width:50px;
			height:16px;
			background-image:url(images/nav/showButton.gif);
			background-repeat:no-repeat;
			background-position:right;
			font-size:12px;
			cursor:pointer;
		}
		.showOver {
			background-color:#fff;
			border:#333333 1px solid;
		}
		
		
		#showNav {
			background:url(images/nav/nav_bg.gif) repeat-x;
			border-bottom:#333333 1px solid;
			height:25px;
			width:100%;
			text-align:left;
		}
		
		.oldLogo{
			background: url(images/nav/nav_logo.png) no-repeat;
			_filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=noscale, src="images/nav/nav_logo.png");
			_background: none,
		}
		.newLogo{
			background: url(images/nav/nav_logo_upload.png) no-repeat;
			_filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=noscale, src="images/nav/nav_logo_upload.png");
			_background: none,
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript">
		var MENU_ITEM_NUM = 7;//菜单选项卡数
		var ITEM_DESK_INDEX = 1,ITEM_CUS_INDEX = 2,ITEM_SAL_INDEX = 3,ITEM_SUP_INDEX = 4,ITEM_OA_INDEX = 5,ITEM_EMP_INDEX = 6,ITEM_STAT_INDEX = 7;
		//得到未读信息条数	
		var isNoRead=0;//标识是否有未读信息
   		function getNoReadMesCount(){
        	var url = "messageAction.do";
			var pars = "op=getNoReadMesCount&ran=" + Math.random();/*ran为随机数*/

			new Ajax.Request(url,{
				method:'get',
				parameters: pars,
				onSuccess:function(transport){
					var response = transport.responseText.split(",");
					if(!isNaN(response[0])&&response!=""){//检查out是否正常返回
						if($("mailLi")!=null&&$("mailLi").style.display!="none"){
							if(response[0]!=null&&response[0]!="0"){
								response[0]=response[0].toString();
								$("mailLi").style.width=55+response[0].length*6+"px";
								$("mesCount").innerHTML="<label class='small' style='padding-left:1px'>("+response[0]+")</label>";
								$("mailIcon").title="您有"+response[0]+"封新消息";
								isNoRead=1;
								//iconBlink();
						   }
						   else{
								$("mailLi").style.width="50px";
								$("mesCount").innerHTML="";
								$("mailIcon").title="我的消息";
								isNoRead=0;
								$("mailIcon").src="images/icon/nav/mail.gif";
						   }
					   }
					   if($("schLi")!=null&&$("schLi").style.display!="none"){
						   if(response[1]!=null&&response[1]!="0"){
								response[1]=response[1].toString();
								$("schLi").style.width=55+response[1].length*6+"px";
								$("schCount").innerHTML="<label class='small' style='padding-left:1px'>("+response[1]+")</label>";
								$("schIcon").title="您今天有"+response[1]+"个日程安排";
								
						   }
							else{
								$("schLi").style.width="50px";
								$("schCount").innerHTML="";
								$("schIcon").title="我的日程";
								
						   }
					  }
					 /* if($("taskLi")!=null&&$("taskLi").style.display!="none"){
						   if(response[2]!=null&&response[2]!="0"){
								response[2]=response[2].toString();
								$("taskLi").style.width=55+response[2].length*6+"px";
								$("taskCount").innerHTML="<label class='small' style='padding-left:1px'>("+response[2]+")</label>";
								$("taskIcon").title="您有"+response[2]+"个待办工作";
								
						   }
							else{
								$("taskLi").style.width="50px";
								$("taskCount").innerHTML="";
								$("taskIcon").title="我的工作安排";
								
						   }
						}*/
						if($("repLi")!=null&&$("repLi").style.display!="none"){
						   if(response[3]!=null&&response[3]!="0"){
								response[3]=response[3].toString();
								$("repLi").style.width=55+response[3].length*6+"px";
								$("repCount").innerHTML="<label class='small' style='padding-left:1px'>("+response[3]+")</label>";
								$("repIcon").title="您有"+response[3]+"个待批报告";
								
						   }
						   else{
								$("repLi").style.width="50px";
								$("repCount").innerHTML="";
								$("repIcon").title="我的报告";
								
						   }
						}
					  	setTimeout("getNoReadMesCount()", 10000);
					}
				},
				onFailure:function(transport){
					//if (transport.status == 404)
						//alert("您访问的url地址不存在！");
					//else
						//alert("Error: status code is " + transport.status);
				}
			});
   		}
/*		
		var speed=500;//闪烁速度
		function iconBlink(){
			if(isNoRead!=0){
				$("mailIcon").src="images/icon/nav/mail_new.gif";
			  	setTimeout("iconBlink2()",speed)
			}
		}
		
		function iconBlink2(){
			if(isNoRead!=0){
				$("mailIcon").src="images/icon/nav/mail.gif";
				setTimeout("iconBlink()",speed)
			}
		}*/

	
		//选项卡切换
		function changeImg(){
			var maxN = MENU_ITEM_NUM;//选项卡数
			var menuWidth=0;
			for(var i=1;i<=maxN;i++){
				var o=$("menu"+i);
				if(o!=null){
					o.onmouseover=function(){
						if(this.className!="menuCur")
							this.className="menuOver";
					}
					
					o.onmouseout=function(){
						if(this.className!="menuCur")
							this.className="menuNormal";
					}
					
					o.onclick=function(){
						var n=this.id.substring(4);
						for(var j=1;j<=maxN;j++){
							var obj=$("menu"+j);
							if(obj!=null){
								obj.className="menuNormal";
								if(j==n)
									obj.className="menuCur";
							}
						}
						menuJump(n);
					}
					if(menuWidth==0){
						o.click();
					}
					menuWidth+=75;
				}
			}
			$("funMenus").style.width=(menuWidth+1)+"px";
		}
		
		//选项卡跳转
		function menuJump(n){
			switch(parseInt(n)){
					case ITEM_DESK_INDEX://工作台
						parent.mainFrame.location="userAction.do?op=toDeskTop";
						break;
						
					case ITEM_CUS_INDEX://客户管理
						parent.mainFrame.location="customer/cusDeskTop.jsp";
						break;
					case ITEM_SAL_INDEX://销售管理
						parent.mainFrame.location="sal/salDeskTop.jsp";
						break;
					/*case 4://客服管理
						parent.mainFrame.location="server/servDeskTop.jsp";
						break;*/
					/*case 4://库存管理
						parent.mainFrame.location="wmsManageAction.do?op=wmsStroList";
						//showMenu();
						closeMenu();
						break;
					case 5://项目管理
						parent.mainFrame.location="projectAction.do?op=getMyProject";
						//parent.leftFrame.location="proMenu.jsp";
						//parent.mainFrame.location="project/proDeskTop.jsp";
						//showMenu();
						closeMenu();
						break;*/
					case ITEM_OA_INDEX://协同办公
						parent.mainFrame.location="oa/oaDeskTop.jsp";
						break;
					case ITEM_SUP_INDEX://采购管理
						parent.mainFrame.location="sup/supDeskTop.jsp";
						break;
					/*case 8://财务管理
						parent.leftFrame.location="accMenu.jsp";
						parent.mainFrame.location="account/accDeskTop.jsp";
						showMenu();
						break;*/
					case ITEM_EMP_INDEX://人员管理
						parent.mainFrame.location="hr/hrDeskTop.jsp";
						break;
					case ITEM_STAT_INDEX://统计分析
						parent.mainFrame.location="statAction.do?op=toSalStat&statType=salM";
						break;
					
				}
		}
	
		
		//最小化
		function hideNav(isHiden){
			if(isHiden==1){
				$("contentbox").show();
				$("showNav").hide();
				parent.frmset1.rows="80,*";
			}	
			else {
				$("contentbox").hide();
				$("showNav").show();
				parent.frmset1.rows="28,*";
			}
		}
		
		//还原滑过
		function showRestore(isShow){
			if(isShow==1){
				$("restoreNav").className="showOver";
				$("restoreNav").innerHTML="还原";
			}
			else{
				$("restoreNav").className="";
				$("restoreNav").innerHTML="";
			}
		}
		//收起滑过
		function showHide(isShow){
			if(isShow==1){
				$("hideNav").className="showOver";
				$("hideNav").innerHTML="收起";
			}
			else{
				$("hideNav").className="";
				$("hideNav").innerHTML="";
			}
		}
		
		
		//小图标跳转
		function optionJump(n){
			var maxOption=MENU_ITEM_NUM;
			switch(n){
				case 0:
					//选项卡切换到工作台
					for(var j=1;j<=maxOption;j++){
						var obj=$("menu"+j);
						if(obj!=null){
							obj.className="menuNormal";
							if(j==ITEM_DESK_INDEX)
								obj.className="menuCur";
						}
					}
					parent.mainFrame.location="userAction.do?op=toDeskTop";
					break;
					
				case 1:
					//选项卡切换到协同办公
					for(var j=1;j<=maxOption;j++){
						var obj=$("menu"+j);
						if(obj!=null){
							obj.className="menuNormal";
							if(j==ITEM_OA_INDEX)
								obj.className="menuCur";
						}
					}
					parent.mainFrame.location="messageAction.do?op=toListAllMess&mesType=all";
					break;
					
				case 2:
					//选项卡切换到协同办公
					for(var j=1;j<=maxOption;j++){
						var obj=$("menu"+j);
						if(obj!=null){
							obj.className="menuNormal";
							if(j==ITEM_OA_INDEX)
								obj.className="menuCur";
						}
					}
					parent.mainFrame.location="messageAction.do?op=toShowSchList";
					break;
				
				case 3:
					//选项卡切换到协同办公
					for(var j=1;j<=maxOption;j++){
						var obj=$("menu"+j);
						if(obj!=null){
							obj.className="menuNormal";
							if(j==ITEM_OA_INDEX)
								obj.className="menuCur";
						}
					}
					parent.mainFrame.location="salTaskAction.do?op=toListMyTaskSearch&tip=tip";
					break;
				
				case 4:
					//选项卡切换到协同办公
					for(var j=1;j<=maxOption;j++){
						var obj=$("menu"+j);
						if(obj!=null){
							obj.className="menuNormal";
							if(j==ITEM_OA_INDEX)
								obj.className="menuCur";
						}
					}
					parent.mainFrame.location="messageAction.do?op=toListApproRep&isApped=a";
					break;
					
				case 5:
					for(var i=1;i<=maxOption;i++){
						var obj=$("menu"+i);
						if(obj!=null){
							obj.className="menuNormal";
						}
					}
					parent.mainFrame.location="userAction.do?op=goUpdatePwd";
					break;
					
				case 6:
					for(var i=1;i<=maxOption;i++){
						var obj=$("menu"+i);
						if(obj!=null){
							obj.className="menuNormal";
						}
					}
					parent.mainFrame.location="system/sysDeskTop.jsp";
					break;
					
				case 7:
					for(var i=1;i<=maxOption;i++){
						var obj=$("menu"+i);
						if(obj!=null){
							obj.className="menuNormal";
						}
					}
					if(confirm('用户即将退出，确定退出该页面吗？'))
			  			parent.location.href="sessiondel.jsp";
					break;
					
			}
		}
		
		//加载小图标事件
		function loadMenuEvent(){
			var imgs=$$("img");
			for(var i=0;i<imgs.length;i++){
				if(imgs[i].id!="comLogo"){
					imgs[i].className="iconGray";
					imgs[i].onmouseover=function(){
						this.className="iconOver";
					}
					imgs[i].onmouseout=function(){
						this.className="iconGray";
					}
				}
			}
			
		}
		
		//加载logo
		function loadNavLogo(){
			var url='empAction.do';
			var pars = 'op=getTopOrgLogo&ran=' + Math.random();
			new Ajax.Request(url,{
				method:'get',
				parameters: pars,
				onSuccess: function(transport){
					var logoPath = transport.responseText;
					if(logoPath=='null'||logoPath==''){
						logoPath = 'images/nav/nav_logo.png';
					}
					if(getBrowser()=='IE:6.0'){
						$('navLogo').setStyle({
							'filter': 'progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled=true, sizingMethod=noscale, src=\"'+ logoPath +'\")'
						});
					}
					else{
						$('navLogo').setStyle({
							'background':'url('+ logoPath +') no-repeat left center'
						});
					}
					
				},
				onFailure: function(transport){
					if (transport.status == 404)
						alert("您访问的url地址不存在！");
					else
						alert("Error: status code is " + transport.status);
				}
			});
		}
		
		
		window.onload=function(){
			loadMenuEvent();
			//getNoReadMesCount();
			loadNavLogo();
			//changeImg();
		}
  	</script>
  </head>
  
  <body>
    <div id="mainbox">
    	<div id="contentbox">
			<div id="navLogo"></div>
            <div id="hideNav" class="normal" title="收起" onMouseOver="showHide(1)" onMouseOut="showHide(0)" onClick="hideNav(0)">&nbsp;</div>  
            <div id="topNotice">
            	<span id="iconMenu" class="white">
                    <span>
                        <img src="images/icon/nav/deskTop.gif" alt="首页" onClick="optionJump(0)"/>                    
                    </span>
                    <span id='mailLi' style="display:none">
                    	<nobr><img id='mailIcon' src='images/icon/nav/mail.gif' alt='我的消息' onclick='optionJump(1)'/><label id='mesCount'></label></nobr>
                    </span>
                    <span id='schLi' style="display:none">
                    	<nobr><img id='schIcon' src='images/icon/nav/sch.gif' alt='我的日程' onclick='optionJump(2)'/><label id='schCount'></label></nobr>
                    </span>
                    <!--<span id='taskLi' style="display:none">
                    	<nobr><img id='taskIcon' src='images/icon/nav/task.gif' alt='我的工作安排' onclick='optionJump(3)'/><label id='taskCount'></label></nobr>
                    </span>-->
                    <span id='repLi' style="display:none">
                    	<nobr><img id='repIcon' src='images/icon/nav/rep.gif' alt='我的报告' onclick='optionJump(4)'/><label id='repCount'></label></nobr>
                    </span>
                    <span>
                        <img src="images/icon/nav/editInf.gif" alt="修改登录信息" onClick="optionJump(5)"/>                    
                    </span>
                    <span id='sysLi' style="display:none">
                    	<img src='images/icon/nav/option.gif' alt='系统设置' onclick='optionJump(6)'/>
                    </span>
                    <script type="text/javascript">
						var iconRigs = "ac024|ac021|ac023|sy017";
						var iconIds = new Array("mailLi","schLi","repLi","sysLi");
						displayLimAllow(iconRigs,iconIds);
					</script>
                    <span>
                        <img src="images/icon/nav/exit.gif" alt="安全退出" onClick="optionJump(7)"/>                    
                    </span>                
                </span>           
          	</div>
      		<div id="menu">
            	<table class="noBr" cellspacing="0" cellpadding="0">
                	<tr>
                        <td>
                            <div id="funMenus">
                            <table cellpadding="0" cellspacing="0">
                                <tr id="fuMenuTr">
                                	<td id="menu1" class="menuNormal">首页</td>
                                    <script type="text/javascript">
										var menuRigs = "r030|s029|apu029|c025|h008|b028";
										var menuHtmls = new Array();
										menuHtmls[0] = "<td id='menu2' class='menuNormal'>客户管理</td>";
										menuHtmls[1] = "<td id='menu3' class='menuNormal'>销售管理</td>";
										/*menuHtmls[2] = "<td id='menu4' class='menuNormal'>客服管理</td>";*/
										menuHtmls[2] = "<td id='menu4' class='menuNormal'>采购管理</td>";
										menuHtmls[3] = "<td id='menu5' class='menuNormal'>协同办公</td>";
										menuHtmls[4] = "<td id='menu6' class='menuNormal'>人员管理</td>";
										menuHtmls[5] = "<td id='menu7' class='menuNormal'>统计报表</td>";
										//var menuRigs = "r030|s029|w015|p016|c025|apu029|af019|h008|b028";
										/*menuHtmls[0] = "<td id='menu2' class='menuNormal'>客户管理</td>";
										menuHtmls[1] = "<td id='menu3' class='menuNormal'>销售管理</td>";
										menuHtmls[2] = "<td id='menu4' class='menuNormal'>库存管理</td>";
										menuHtmls[3] = "<td id='menu5' class='menuNormal'>项目管理</td>";
										menuHtmls[4] = "<td id='menu6' class='menuNormal'>协同办公</td>";
										menuHtmls[5] = "<td id='menu7' class='menuNormal'>采购管理</td>";
										menuHtmls[6] = "<td id='menu8' class='menuNormal'>财务管理</td>";
										menuHtmls[7] = "<td id='menu9' class='menuNormal'>人事管理</td>";
										menuHtmls[8] = "<td id='menu10' class='menuNormal'>统计分析</td>";*/
										writeLimAllow(menuRigs,menuHtmls,"fuMenuTr","bottom",changeImg);
                                    </script>
                                </tr>
                            </table>
                            </div>					
                        </td>
                        <td>&nbsp;</td>
                        <td id="welcomeInf">
                            <div>
                                <nobr>&nbsp;欢迎您，<c:if test="${!empty CUR_EMP}"><span class="bold textOverflow" title="<c:out value="${CUR_EMP.seName}"/>" style="width:80px"><c:out value="${CUR_EMP.seName}"/></span>&nbsp;</c:if>&nbsp;今天是${TODAY_E}</nobr></div>                  	
                        </td>
                 	</tr>
              </table>    
    		</div>
   	  	</div>
      	<div id="showNav" style="display:none">
        	<table width="100%" cellpadding="0px" cellspacing="0px">
            	<tr>
                	<td><div class="white middle" style=" padding:4px;padding-left:10px;">富阳中耐CRM </div></td>
                    <td style="text-align:right;vertical-align:top">
                    	<div id="restoreNav" title="还原" onMouseOver="showRestore(1)" onMouseOut="showRestore(0)" onClick="hideNav(1)"></div>
                    </td>
                </tr>
            </table>
        </div>
  	</div>
</body>
</html>

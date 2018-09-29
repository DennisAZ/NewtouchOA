<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean"  prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>工作台</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/desk.css"/>
    <style type="text/css">
    	.deepGray{
			border:1px solid #dadada;
		}
		#cal{background-color:#fff;width:260px; height:284px;border:1px solid #c3d9ff;font-size:12px;margin:8px 0 0 8px}
		#cal #top{height:29px;line-height:29px;background:#e7eef8;color:#003784;padding-left:10px}
		#cal #top select{font-size:12px}
		#cal #top input{padding:0}
		#cal ul#wk{margin:0;padding:0;height:25px}
		#cal ul#wk li{float:left;width:35px;text-align:center;line-height:25px;list-style:none}
		#cal ul#wk li b{font-weight:normal;color:#c60b02}
		#cal #cm{ clear:left;border-top:1px solid #ddd;position:relative}
		#cal #cm .cell{position:absolute;width:36px;height:30px;text-align:center;margin:2px 8px 0px 4px;}
		#cal #cm .cell .so{font:bold 14px arial;}
		#cal #bm{text-align:right;height:0px;}
		#cal #fd{display:none;position:absolute;border:1px solid #feb23b;background:#feffcd;padding:1px;line-height:21px;width:150px}
		#cal #fd b{font-weight:normal;color:#c60a00}
		
		/* 自定义工作台 */
		#defTitle{
			text-align:left; 
			padding:2px 6px 3px 15px;
			background-color:#e9eff6;
		}
		#defBottom {
			text-align:center;
			padding:1px;
			background-color:#e9eff6;
		}
		
		#defDeskTop{
			width:98%;
			text-align:left;
			border:#a4b0c1 2px solid;
			background-color:#fff;
		}
		
		#defDeskTop span{
			white-space:nowrap;
			width:150px;
			text-align:left;
			padding-left:10px;
			padding-bottom:8px;
		}
		#defDeskTop .orangeBox input{
			margin:0px;
			vertical-align:middle;
		}
		
		.widgets{
			width:260px;
			font-size:12px;
			margin:8px 0 0 8px;
			background-color:#fff;
			border:#ccccff 1px solid;
			text-align:left;
		}
		.widTitle{
			width:100%;
			padding:8px 0px 5px 5px;
			background:#e7eef8;
			border-bottom:#ccccff 1px solid;
			text-align:left;
		}
		.widIcon1,.widIcon2{
			background-image:url(images/icon/nav/sch.gif);
			background-repeat:no-repeat;
			background-position:5px 2px;
			padding-left:30px;
		}
		.widIcon2{
			background-image:url(images/content/package.gif);
		}
		.widTitle table{
			width:100%;
		}
		.widTitleTxt {
			color:#333;
			font-size:13px;
			font-weight:600;
		}
		.widTitleA {
			text-align:right;
		}
		.widTitleA a:link {
			color:#999;
			text-decoration:none;
		}
		.widTitleA a:visited {
			color:#999;
			text-decoration:none;
		}
		.widTitleA a:hover {
			color:#2b62ac;
			text-decoration:none;
		}
		.widTitleA a:active {
			color:#2b62ac;
			text-decoration:none;
		}
		
		.toolsMenu {
			margin:0px;
			padding:0px;
		}
		.toolsMenu a {
			width:100%;
			padding:6px;
			padding-left:20px;
		}
		.toolsMenu a:link {
			color:#26568d;
			text-decoration:none;
		}
		.toolsMenu a:visited {
			color:#26568d;
			text-decoration:none;
		}
		.toolsMenu a:hover {
			color:#000;
			background-color:#e2eaf6;
			background-image:url(images/content/green_go.gif);
			background-repeat:no-repeat;
			background-position:3px 5px;
			text-decoration:none;
		}
		.toolsMenu a:active {
			color:#000;
			text-decoration:none;
			background-color:#e2eaf6;
		}
		.toolsURL {
			margin-left:15px;
			color:#b1b1b1;
			display:none;
		}
		.webIcon {
			background-image:url(images/content/web.gif);
			background-repeat:no-repeat;
			background-position:4px 1px;
			width:16px;
			height:12px;
		}
    </style>
        <!--[if IE]>
        <style>
        #cal #top{padding-top:4px}
        #cal #top input{width:80px}
        #cal #fd{width:180px}
        </style>
		<![endif]-->
	<script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/desk.js"></script>
    <script type="text/javascript" src="js/oa.js"></script>
	<script type="text/javascript">
		var MENU_item_NUM = 7;//菜单选项卡数
		var item_CUS_INDEX = 2;
		var item_SAL_INDEX = 3;
		var item_OA_INDEX = 5;
		//点击更多 跳转
		function toMore(n){
			switch(n){
					
				//新闻
				case 0:
					//选项卡切换到协同办公
					changeNav(item_OA_INDEX);
					parent.mainFrame.location="messageAction.do?op=toListAllNews";
					break;
					
				//报告
				case 1:
					//选项卡切换到协同办公
					changeNav(item_OA_INDEX);
					parent.mainFrame.location="messageAction.do?op=toListApproRep";
					break;
				
				//日程
				case 2:
					//选项卡切换到协同办公
					changeNav(item_OA_INDEX);
					parent.mainFrame.location="messageAction.do?op=toShowSchList";
					break;
				
				//收到的工作
				case 3:
					//选项卡切换到协同办公
					changeNav(item_OA_INDEX);
					parent.mainFrame.location="salTaskAction.do?op=toListMyTaskSearch";
					break;
				
				//销售业绩统计
				case 4:break;
				
				//回款计划
				case 5:
					//选项卡切换到销售
					changeNav(item_SAL_INDEX);
					parent.mainFrame.location="paidAction.do?op=toListPaidPlan&range=0";
					break;
					
				//机会
				case 6:
					//选项卡切换到客户
					changeNav(item_CUS_INDEX);
					parent.mainFrame.location="cusServAction.do?op=toListSalOpps&range=0";
					break;

				//日程
				case 11:
					//选项卡切换到协同办公
					changeNav(item_OA_INDEX);
					parent.mainFrame.location="messageAction.do?op=toShowSchList";
					break;
			}
		}
		
		//选项卡切换
		function changeNav(index){
			var doc = parent.topFrame.document;
			for(var j=1;j<=MENU_item_NUM;j++){
				var obj=doc.getElementById("menu"+j);
				if(doc.getElementById("menu"+j)!=null){
					obj.className="menuNormal";
					if(j==index)
						obj.className="menuCur";
				}
			}
		}
		
		//全选
		function checkAll(obj){
			var array=$N("isClose");
			if(obj.checked==true){
				for(var i=0;i<array.length;i++){
					array[i].checked=true;
				}
			}
			else{
				for(var i=0;i<array.length;i++){
					array[i].checked=false;
				}
			}
		}
		
		//打开自定义工作台
		function showDefDeskTop(){
			initDefDeskTop();
			$('defDeskTop').style.display='block';
		}
		
		function showOver(isOver,obj){
			if(obj.alt=="展开"){
				if(isOver==1){
					obj.src="images/content/whiteShow2.gif";
				}
				else{
					obj.src="images/content/whiteShow.gif";
				}
			}
			else{
				if(isOver==1){
					obj.src="images/content/whiteHide2.gif";
				}
				else{
					obj.src="images/content/whiteHide.gif";
				}
			}
		}
		function moreOver(isOver,obj){
			if(isOver==1){
				obj.src="images/content/more2.gif";
			}
			else{
				obj.src="images/content/more1.gif";
			}
		}
		function closeOver(isOver,obj){
			if(isOver==1){
				obj.src="images/content/close2.gif";
			}
			else{
				obj.src="images/content/close1.gif";
			}
		}
		function changeOver(isOver,obj){
			if(isOver==1){
				obj.src="images/content/whiteChange2.gif";
			}
			else{
				obj.src="images/content/whiteChange1.gif";
			}
		}
		
		//刷新
		function reMod(id){
			$(id).childNodes[1].childNodes[2].contentWindow.history.go(0);
		}
		
		createProgressBar();
    	window.onload=function(){
			B=D[GET]("body")[0];
			//H=D[GET]("html")[0];
			Drop.init(D[GEI]("container"));
			Drag.addEvent();
			initDefDeskTop();
			closeProgressBar();
		}
    </script>

    <!--[if IE]>
    <script type="text/javascript">
    IE=true;
    </script>
    <![endif]-->
    <![if !IE]><![endif]>
    </head>
<body>

<div id="deskBox">
	<input type="hidden" id="curPage" value="1"/>
	<input type="hidden" id="today" value="${TODAY}"/>
    <input type="hidden" id="tomorrow" value="${TMR}"/>
    <input type="hidden" id="afTom" value="${AF_TMR}"/>
    <input type="hidden" id="yesterday" value="${YDAY}"/>
    <input type="hidden" id="bfYesd" value="${BF_YDAY}"/>
	<div id="deskTop">
    	<div>
            <span style="float:left; font-size:14px; background:url(images/icon/nav/deskTop.gif) no-repeat; padding:5px 0 0 25px;">工作台</span>
            <span style="float:right; padding-right:8px; padding-top:4px; height:24px;">
                <span style=" font-size:12px; font-weight:500;cursor:pointer; padding:2px;" class="deepGray" onMouseOver="this.className='grayBack blue'" onMouseOut="this.className='deepGray'" onClick="showDefDeskTop()"><img src="images/content/wheel.gif" alt="自定义工作台" class="imgAlign"/>&nbsp;自定义工作台</span>
            </span>
        </div>
    </div>
    <div id="defDeskTop" style="display:none">
        <div id="defTitle" class="bold">
            勾选将要在工作台显示的模块&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <input type="checkbox" id="checkAll" onClick="checkAll(this)"><label for="checkAll" class="normal" style="font-weight:500">全选</label>
        </div>
        <span><input type="checkbox" name="isClose" id="pTip" value="prodTip" checked/><label for="pTip">发货提醒</label></span>
        <span><input type="checkbox" name="isClose" id="sTip" value="statTip" checked/><label for="sTip">上月末未达到规定金额的单位</label></span>
        <span><input type="checkbox" name="isClose" id="stTip" value="stateTip" checked/><label for="stTip">显示状态</label></span>
        <span><input type="checkbox" name="isClose" id="recTip" value="recvTip" checked/><label for="recTip">应收款信息</label></span>
        <span><input type="checkbox" name="isClose" id="mnews" value="modNews" checked/><label for="mnews">最近的新闻公告</label></span>
        <!--<span><input type="checkbox" name="isClose" id="mopp" value="modOpp" checked/><label for="mopp">最近关注的销售机会</label></span>-->
        <!--<span><input type="checkbox" name="isClose" id="mpaid" value="modPaid" checked/><label for="mpaid">回款计划</label></span>-->
        <span><input type="checkbox" name="isClose" id="msale" value="modMySale" checked/><label for="msale">我6个月的销售业绩统计</label></span>
        <!--<span><input type="checkbox" name="isClose" id="mcus" value="modCus" checked/><label for="mcus">应收余额超额客户</label></span>
        <span><input type="checkbox" name="isClose" id="mcon" value="modCon" checked/><label for="mcon">最近需关注的联系人</label></span>-->
        <span><input type="checkbox" name="isClose" id="mrep" value="modRep" checked/><label for="mrep">待阅报告</label></span>
        <span><input type="checkbox" name="isClose" id="mSch" value="modSch" checked/><label for="mSch">我的日程</label></span>
        <span><input type="checkbox" name="isClose" id="cusTip" value="cusProd" checked/><label for="cusTip">最后发货日期提醒</label></span>
        <!--<span><input type="checkbox" name="isClose" id="mmta" value="modMyTask" checked/><label for="mmta">收到的待办工作</label></span>-->
		<div id="defBottom">
        	<button class="smallBut" onClick="setDeskTop()">保存</button>
        	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <button class="smallBut" onClick="$('defDeskTop').style.display='none'">关闭</button>
      	</div>
  	</div>
    
    <div style="width:260px; float:left; padding-left:5px; padding-bottom:10px;">
    	<!--<div class="widgets">
        	<div class="widTitle widIcon1">
            	<table class="normal nopd">
                	<tr>
                    	<td class="widTitleTxt">我的日程</td>
                        <td class="widTitleA">
                        	<a href="javascript:void(0)" onClick="popDiv(1);return false;" style="background:url(images/content/smalladd.gif) no-repeat; background-position:0px 0px; padding-left:12px; ">新建</a>&nbsp;&nbsp;
                        </td>
                    </tr>
                </table>
            </div>
            <div style="padding:8px">
                <div style="padding:2px; border-bottom:#dadada 1px solid;">
                    <logic:notEmpty name='curSchList'>
                        <div class="orange bold">今天的日程安排</div>
                        <logic:iterate id='schedules' name='curSchList'>
                            <div class="modCon">
                                <img src="images/content/blueArr.gif"/>&nbsp;
                                <logic:equal value='已完成' name='schedules' property='schState'>
                                    <logic:notEmpty name='schedules' property='schType'>
                                        <span class='brown'>[${schedules.schType.typName}]</span>
                                    </logic:notEmpty>
                                    <a href="javascript:void(0)" onClick="popDiv(2,${schedules.schId});return false;">${schedules.schTitle}</a>
                                    <span><img class="imgAlign" src='images/content/suc.gif' alt='已完成'/></span>
                               </logic:equal>
                               <logic:equal value='未完成' name='schedules' property='schState'>
                                    <logic:notEmpty name='schedules' property='schType'>
                                        <span class='brown'>[${schedules.schType.typName}]</span>
                                    </logic:notEmpty><a href="javascript:void(0)" onClick="popDiv(2,${schedules.schId});return false;">${schedules.schTitle}</a>
                                    <span id='${schedules.schId}'><img class="imgAlign" src='images/content/execute.gif' border='0' style='cursor:pointer' onClick="modifySchState('${schedules.schId}')" alt='点击完成'/></span>
                                    <span id='y${schedules.schId}' style='display:none'><img class="imgAlign" src='images/content/suc.gif' alt='已完成'/></span>
                                </logic:equal>
                                <span class='gray small'><nobr>${schedules.schStartTime}-${schedules.schEndTime}</nobr></span>
                           </div>
                        </logic:iterate>
                    </logic:notEmpty>
                    <logic:empty name='curSchList'>
                        <span class="gray">&nbsp;&nbsp;今天没有日程安排</span>
                    </logic:empty>
                </div>  
            </div>
        	<div style="padding:8px; padding-top:0px;">
                <div class="gray bold" style="padding:3px;">七天内</div>
                <logic:notEmpty name='lastSchList'>
                    <logic:iterate id='lastSch' name='lastSchList' indexId='schCount'>
                        <div class="modCon">
                            <img src="images/content/blueArr.gif"/>&nbsp;
                            <logic:notEmpty name='lastSch' property='schType'>
                                <span class='brown'>[${lastSch.schType.typName}]</span>
                            </logic:notEmpty>
                            <a href="javascript:void(0)" onClick="popDiv(2,${lastSch.schId});return false;">${lastSch.schTitle}</a>&nbsp;
                            <span class='gray small'><nobr><label id="sdate1${schCount}"></label>&nbsp;</nobr></span>
                            <span class='gray small'><nobr>${lastSch.schStartTime}-${lastSch.schEndTime}</nobr></span>
                            <script type="text/javascript">
                                dateFormat('sdate1','${lastSch.schStartDate}','${schCount}');
                            </script>
                       </div>
                    </logic:iterate>
                </logic:notEmpty>
                <logic:empty name='lastSchList'>
                    <span class="gray">&nbsp;&nbsp;七天内没有待办日程</span>
                </logic:empty>
             </div>
             <div id="schTA" class="widTitleA" style="background:#e7eef8; padding:3px;"></div>
        </div>-->
        <div id="cal">
            <div id="top">公元&nbsp;<select style="width:60px"></select>&nbsp;年&nbsp;<select style="width:40px"></select>&nbsp;月<br>农历<span></span>年&nbsp;[&nbsp;<span></span>年&nbsp;]&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input class="smallBut" type="button" value="回到今天" title="点击后跳转回今天" style="padding:0px"></div>
            <ul id="wk"><li>一</li><li>二</li><li>三</li><li>四</li><li>五</li><li><b>六</b></li><li><b>日</b></li></ul>
            <div id="cm"></div>
            <div id="bm">&nbsp;</div>
        </div>
        <script type="text/javascript" src="js/bdcalendar110.js"></script>
    	<!--<div class="widgets">
        	<div class="widTitle" style="padding:6px 8px 4px 8px;">
            	<table class="normal nopd">
                	<tr>
                    	<td class="widTitleTxt">今日天气</td>
                        <td style="text-align:right">
                        	<div id="allWea" class="widTitleA">
                            	<a href="javascript:void(0)" onClick="popDiv(3);return false;">全国天气</a><span class="webIcon" title="此功能需连接网络"></span>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
            <div id="weaContent" style="padding:4px 0px 5px 0px;">
            	<script type="text/javascript">createIfmLoad('ifmLoad1');</script>
                <iframe src="deskTop/weather.html" onload="loadEnd('ifmLoad1')" frameborder="0" scrolling="no" style="width:258px; height:110px;"></iframe>
            </div>
            <div id="weaError" class="gray" style=" display:none; padding:5px; padding-top:30px; text-align:center;">
            	无法加载天气信息,请检查您的网络设置
            </div>
        </div>-->
        
        <div class="widgets">
        	<div class="widTitle widIcon2">
            	<table class="normal nopd">
                	<tr>
                    	<td class="widTitleTxt">工具箱</td>
                    </tr>
                </table>
            </div>
            <div>
            	<ul class="toolsMenu">
                	<li><a href="javascript:void(0)" onClick="window.open('empAction.do?op=toShowAddBook','公司通讯录','width=1000px,height=620px,toolbar=no,resizable=yes,status=no,scrollbars=yes')">公司通讯录&nbsp;</a></li>
                	<li><a href="javascript:void(0)" onClick="window.open('deskTop/calculator.htm','科学计算器','width=585px,height=360px,toolbar=no,resizable=no,status=no')">科学计算器&nbsp;</a></li>
                    <li><a href="http://ditu.google.cn/?hl=zh-CN/" target="_blank">电子地图<span class="webIcon" title="此功能需连接网络"></span><span class="toolsURL">ditu.google.cn</span></a></li>
                    <li><a href="http://translate.google.cn/?hl=zh-CN/" target="_blank">在线翻译<span class="webIcon" title="此功能需连接网络"></span><span class="toolsURL">translate.google.cn</span></a></li>
                    <li><a href="http://www.boc.cn/sourcedb/whpj/index.html" target="_blank">实时汇率查询<span class="webIcon" title="此功能需连接网络"></span></a></li>
                    <li><a href="javascript:void(0)" onClick="window.open('deskTop/phoneFrom.html','手机归属地查询','width=380px,height=230px,toolbar=no,resizable=no,status=no')">手机归属地查询<span class="webIcon" title="此功能需连接网络"></span></a></li>
                    <!--<li><a href="javascript:void(0)" onClick="window.showModelessDialog('deskTop/bussearch.html','公交查询','dialogWidth=390px;dialogHeight=260px;status:0;')">公交查询<span class="webIcon" title="此功能需连接网络"></span><span class="toolsURL">8684.com</span></a></li>-->
                    <li><a href="http://www.8684.com" target="_blank">公交线路查询<span class="webIcon" title="此功能需连接网络"></span><span class="toolsURL">8684.com</span></a></li>
                    
                    <!--<li><a href="http://www.cn11.org/" target="_blank">火车查询</a></li>
                    <li><a href="http://www.caac.gov.cn/S1/GNCX/" target="_blank">航班查询</a></li>-->
                    
                </ul>
            </div>
        </div>
        <!--
        <div class="widgets" >
        	<div class="widTitle" style="padding:6px 8px 4px 8px;">
            	<table class="normal nopd">
                	<tr>
                    	<td class="widTitleTxt">当前版本：富阳中耐CRM v2.0</td>
                    </tr>
                </table>
            </div>
             <div class="widTitleA" style=" text-align:left;height:25px; padding:10px 5px 10px 10px;">
             	<div><a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=1085922489&site=qq&menu=yes">技术支持：<img style="border:0; vertical-align:middle" src="http://wpa.qq.com/pa?p=2:1085922489:41" alt="枫软技术支持"></a></div>
                <div style="padding:10px 5px 0 5px; text-align:right;"><a target="_blank" href="http://www.frcrm.com/">[&nbsp;枫软信息&nbsp;|&nbsp;frcrm.com&nbsp;]</a></div>
             </div>
        </div>-->
    </div>
    <span class="container" id="container">
        <ul class="column" id="col1">
        	<li class="module hide" id="prodTip" style="display:none">
                <div class="title">
                    <span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">发货提醒</span>
                    <span id="prodTil" class="tilIcon">
                        <img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('prodTip')"/>&nbsp;&nbsp;
                        <img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                    <script type="text/javascript">createIfmLoad('ifmLoad8');</script>
                    <iframe onload="loadEnd('ifmLoad8')" style="width:95%" frameborder="0" src="orderAction.do?op=getProdTip&filter=date3"></iframe>
                </div>
            </li>
            <li class="module hide" id="stateTip" style="display:none">
                <div class="title">
                    <span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">客户开发状态</span>
                    <span id="stateTil" class="tilIcon">
                        <img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('stateTip')"/>&nbsp;&nbsp;
                        <img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                    <script type="text/javascript">createIfmLoad('ifmLoad10');</script>
                    <iframe onload="loadEnd('ifmLoad10')" style="width:95%" frameborder="0" src="customAction.do?op=toShowCusState&corState=0"></iframe>
                </div>
            </li>
        	<li class="module hide" id="modNews" style="display:none">
                <div class="title">
                    <span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">最近的新闻公告</span>
                    <span id="newsTil" class="tilIcon">
                        <img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('modNews')"/>&nbsp;&nbsp;
                        <img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                    <script type="text/javascript">createIfmLoad('ifmLoad2');</script>
                    <iframe onload="loadEnd('ifmLoad2')" style="width:100%" frameborder="0" src="messageAction.do?op=getNewsReps"></iframe>
                </div>
            </li>
            <li class="module hide" id="modMySale" style="display:none">
                <div class="title">
                	<span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">我6个月的销售业绩统计</span>
                    <span id="mySaleTil" class="tilIcon">
                        <img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('modMySale')"/>&nbsp;&nbsp;
                    	<img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                	<script type="text/javascript">createIfmLoad('ifmLoad6');</script>
                   	<iframe onload="loadEnd('ifmLoad6')" style="width:100%" id="salTab" scrolling="no" frameborder="0" src="statAction.do?op=getLastSalTable"></iframe>
                </div>
           	</li>
            <!--<li class="module3 hide" id="modMyTask" style="display:none">
                <div class="title3">
                	<span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">收到的待办工作</span>
                    <span id="myTaskTil" class="tilIcon">
                    	<img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('modMyTask')"/>&nbsp;&nbsp;
                    	<img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                	<script type="text/javascript">createIfmLoad('ifmLoad5');</script>
                    <iframe onload="loadEnd('ifmLoad5')" style="width:100%" frameborder="0" src="salTaskAction.do?op=getTodoMyTask"></iframe>
                </div>
           	</li>-->
        </ul>
        <ul class="column" id="col2">
            <li class="module hide" id="statTip" style="display:none">
                <div class="title">
                	<span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">上月末未达到规定金额的单位</span>
                    <span id="statTil" class="tilIcon">
                    	<img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('statTip')"/>&nbsp;&nbsp;
                    	<img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                	<script type="text/javascript">createIfmLoad('ifmLoad9');</script>
                    <iframe onload="loadEnd('ifmLoad9')" style="width:100%" frameborder="0" src="statAction.do?op=getLTsals"></iframe>
                </div>
           	</li>
           	<li class="module hide" id="recvTip" style="display:none">
                <div class="title">
                	<span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">应收款信息</span>
                    <span id="recvTil" class="tilIcon">
                    	<img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('recvTip')"/>&nbsp;&nbsp;
                    	<img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                	<script type="text/javascript">createIfmLoad('ifmLoad11');</script>
                    <iframe onload="loadEnd('ifmLoad11')" style="width:95%" frameborder="0" src="customAction.do?op=toShowRecvAmt"></iframe>
                </div>
           	</li>
            <li class="module hide" id="modRep" style="display:none">
                <div class="title">
                	<span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">待阅报告</span>
                    <span id="repTil" class="tilIcon">
                    	<img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('modRep')"/>&nbsp;&nbsp;
                    	<img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                	<script type="text/javascript">createIfmLoad('ifmLoad3');</script>
                    <iframe onload="loadEnd('ifmLoad3')" style="width:100%" frameborder="0" src="messageAction.do?op=getLastRep"></iframe>
                </div>
           	</li>
            <li class="module1 hide" id="modSch" style="display:none">
                <div class="title1">
                	<span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">我的日程</span>
                    <span id="schTil" class="tilIcon">
                    	<img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('modSch')"/>&nbsp;&nbsp;
                    	<img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                	<script type="text/javascript">createIfmLoad('ifmLoad7');</script>
                   	<iframe onload="loadEnd('ifmLoad7')" style="width:100%" frameborder="0" src="messageAction.do?op=listSchOfDeskTop"></iframe>
                </div>
           	</li>
             
             <li class="module hide" id="cusProd" style="display:none">
                <div class="title1">
                	<span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">最后发货日期提醒</span>
                    <span id="cusProdTil" class="tilIcon">
                    	<img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('cusProd')"/>&nbsp;&nbsp;
                    	<img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                	<script type="text/javascript">createIfmLoad('ifmLoad12');</script>
                   	<iframe onload="loadEnd('ifmLoad12')" style="width:100%" frameborder="0" src="customAction.do?op=getDeskCusProd"></iframe>
                </div>
           	</li>
        	<!--<li class="module4 hide" id="modPaid" style="display:none">
                <div class="title4">
                	<span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">回款计划</span>
                    <span id="paidTil" class="tilIcon">
                    	<img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('modPaid')"/>&nbsp;&nbsp;
                    	<img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                	<script type="text/javascript">createIfmLoad('ifmLoad7');</script>
                    <iframe onload="loadEnd('ifmLoad7')" style="width:100%" frameborder="0" src="paidAction.do?op=getToDoPaid"></iframe>
                </div>
           	</li>-->
           
           <!--<li class="module1 hide" id="modOpp" style="display:none">
                <div class="title1">
                	<span class="tilTxt"><img onMouseOver="showOver(1,this)" onMouseOut="showOver(0,this)" onClick="fold()" src="images/content/whiteShow.gif" alt="展开"/></span><span class="tilTxt2">最近关注的销售机会</span>
                    <span id="oppTil" class="tilIcon">
                    	<img onMouseOver="changeOver(1,this)" onMouseOut="changeOver(0,this)" src="images/content/whiteChange1.gif" alt="刷新" onClick="reMod('modOpp')"/>&nbsp;&nbsp;
                    	<img onMouseOver="closeOver(1,this)" onMouseOut="closeOver(0,this)" src="images/content/close1.gif" alt="移除该模块" onClick="closeMod()"/>&nbsp;
                    </span>
                </div>
                <div class="cont">
                	<script type="text/javascript">createIfmLoad('ifmLoad8');</script>
                   	<iframe onload="loadEnd('ifmLoad8')" style="width:100%" frameborder="0" src="cusServAction.do?op=getOppByExeDate&isDeskTop=1&range=0"></iframe>
                </div>
           	</li>-->
        </ul>
    </span>
</div>
	<div id="tempDiv" style="display:none"></div>
    <script type="text/javascript">
		//判断页面权限
		/*writeLimAllow("c025","<a href='javascript:void(0)' onClick='toMore(11);return false;'>更多</a>&nbsp;&nbsp;","schTA");//日程权限*/
		//var ids = new Array("newsTil","repTil","myTaskTil","mySaleTil","paidTil","oppTil");
		var ids = new Array("prodTil","newsTil","stateTil","statTil","recvTil","repTil","schTil","mySaleTil","cusProdTil");
		var htmlArr = new Array();
		for(var i = 0; i<ids.length;i++){
			htmlArr[i] = "<img onMouseOver='moreOver(1,this)' onMouseOut='moreOver(0,this)' src='images/content/more1.gif' alt='查看更多' onClick='toMore(" + i + ")'/>&nbsp;&nbsp;&nbsp;";
		}
		writeLimAllow("c025|c025|c025|c025|c025|c025|c025|s029|c025",htmlArr,ids,"top");
		
    	ModPos.init("container","${limUser.userCode}");//根据cookies初始化小模块
    </script>
</body>
</html>
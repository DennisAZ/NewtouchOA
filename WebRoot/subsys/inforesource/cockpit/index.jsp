<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<%
String attach =  YHSysProps.getString("attachFilePath");
if(attach == null || attach.length() < 1){
  attach = "attach";
}else{
  attach = attach.substring(attach.indexOf("attach")).trim();
}
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>首页</title>
<link href="css.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/index1.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/FusionCharts.js"></script>
<script type="text/JavaScript" src="js/common.js"></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/const.js'></script>
<script type="text/JavaScript" src="<%=contextPath%>/subsys/inforesource/tree1/js/jquery.js"></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/Page.js'></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.autocomplete.js"></script>
<script type="text/JavaScript" src="js/index.js"></script>
<script type="text/JavaScript" >
 var attachFilePath = "<%=attach%>";
   $(document).ready(function(){     
     getHotArticle("",1);//热点文章
     getHotPictrues();   //热点图片  
     getHotPerson(0);
     createColumnChart();   
   });
   function createLineChart(keyId) {
     var chart = new FusionCharts("Charts/Line.swf", "lineChart", "362", "135", "0", "0");
     chart.setJSONUrl(contextPath + "/yh/subsys/inforesouce/act/YHCockpitAct/getLineData.act?keyId=" + keyId);		   
     chart.render("row-a-right");
   }
   function createColumnChart(){
     var chart2 = new FusionCharts("Charts/Column2D.swf", "columnChart", "493", "175", "0", "1");
     chart2.setJSONUrl(contextPath + "/yh/subsys/inforesouce/act/YHCockpitAct/getColumnData.act");	
     chart2.render("row-b-left");
   }
   var typeConst = "userName";
   function getColumnChart(type  , keyId , searchText ) {
     var chart2 = new FusionCharts("columnChart");
     typeConst = type;
     chart2.setJSONUrl(contextPath + "/yh/subsys/inforesouce/act/YHCockpitAct/getColumnData.act?type=" + type);	
     getLineData(keyId , searchText );
   }
   function getLineData(keyId, name) {
     var chart = new FusionCharts("lineChart");
     chart.setJSONUrl(contextPath + "/yh/subsys/inforesouce/act/YHCockpitAct/getLineData.act?keyId=" + keyId + "&type=" + typeConst);		   
     touchGraphSearch(name)
   }
   function changePal(type) {
     if (type == "userName") {
       getHotPerson(1);
       } else if (type == "address") {
       MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','hide','label-b2','','show','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide');
       getHotAddress();
       } else if (type == "org") {
       MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','hide','label-c2','','show','label-d','','show','label-d2','','hide');
       getHotOrganization();
       } else if (type == "subject" ) {
       MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','hide','label-d2','','show');
       getHotKeyword();
       }
   }
   
</script>
</head>
<body onload="MM_preloadImages('images/button-search-hover.jpg')">
  <div id="container">
      <div id="header">
	      	<div id="search-box">
		      <div id="Search-bg"><input id="searchTxt" type="text" class="Search-bg-form" value=""/>
		      </div>
		      <div id="goto"><a href="javascript:void(0)" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image1','','images/button-search-hover.jpg',1)" onclick="doSeach()"><img src="images/button-search.jpg" name="Image1" width="74" height="26" border="0" id="Image1" /></a></div>
			  <div class="clear"></div>
		    </div>			
			<div id="input">
			<input type="radio" class="header-input"  value="1" name="selType" checked/>
			<input type="radio" class="header-input2" value="2" name="selType"/>
			<input type="radio" class="header-input3" value="3" name="selType"/>
			</div>
	  </div>
	  <div id="content">
	     <div id="boxone">
		     <div id="boxone-tit"></div>
			 <div id="boxone-cen">
			       <div id="bone-left">
				      <!--<ul>
					     <li><a href="javascript:void(0)">易建联12分奇才憾负尼克斯阿联两失绝杀</a><span>10-18</span></li>
						 <li><a href="javascript:void(0)">易建联操刀绝杀大盘点 曾末节17分0.4秒手刃骑士</a><span>10-18</span></li>
						 <li><a href="javascript:void(0)">国际6号秀对决中国易完胜 我们的阿联已经长大了</a><span>10-18</span></li>
						 <li><a href="javascript:void(0)">易建联创季前赛篮板新低 对手不强为何难显统治力</a><span>10-18</span></li>
						 <li><a href="javascript:void(0)">阿联关键时刻错失绝杀 末节6分获桑德斯无限信任</a><span>10-18</span></li>
						 <li><a href="javascript:void(0)">易建联12分奇才憾负尼克斯阿联两失绝杀</a><span>10-18</span></li>
						 <li><a href="javascript:void(0)">易建联操刀绝杀大盘点 曾末节17分0.4秒手刃骑士</a><span>10-18</span></li>
						 <li><a href="javascript:void(0)">国际6号秀对决中国易完胜 我们的阿联已经长大了</a><span>10-18</span></li>
					  </ul>
				   --></div>
				   <div id="bone-right">
				        <div class="row-a">
						   <!--<ul>
						     <li class="img-li">
							    <span class="img-sp"><a href="javascript:void(0)"><img src="images/bg.gif" border="0" width="70px" height="57px"/></a></span>
								<span class="img-tit"><a href="javascript:void(0)">五个文字</a></span>
							 </li>
							 <li class="img-li-a">
							    <span class="img-sp"><a href="javascript:void(0)"><img src="#" border="0" width="70px" height="57px"/></a></span>
								<span class="img-tit"><a href="javascript:void(0)">五个文字</a></span>
							 </li>
							 <li class="img-li-a">
							    <span class="img-sp"><a href="javascript:void(0)"><img src="images/bg3.jpg" border="0" width="70px" height="57px"/></a></span>
								<span class="img-tit"><a href="javascript:void(0)">五个文字</a></span>
							 </li>
							 <li class="img-li-a">
							    <span class="img-sp"><a href="javascript:void(0)"><img src="images/bg4.jpg" border="0" width="70px" height="57px"/></a></span>
								<span class="img-tit"><a href="javascript:void(0)">五个文字</a></span>
							 </li>
							 <li class="img-li-a">
							    <span class="img-sp"><a href="javascript:void(0)"></a></span>
								<span class="img-tit"><a href="javascript:void(0)">五个文字</a></span>
							 </li>
						   </ul>
						--></div>
						<div class="row-b">
						<!--<ul>
						     <li class="img-li">
							    <span class="img-sp"><a href="javascript:void(0)"><img src="images/bg4.jpg" border="0" width="70px" height="57px"/></a></span>
								<span class="img-tit"><a href="javascript:void(0)">五个文字</a></span>
							 </li>
							 <li class="img-li-a">
							    <span class="img-sp"><a href="javascript:void(0)"><img src="#" border="0"  width="70px" height="57px"/></a></span>
								<span class="img-tit"><a href="javascript:void(0)">五个文字</a></span>
							 </li>
							 <li class="img-li-a">
							    <span class="img-sp"><a href="javascript:void(0)"><img src="#" border="0"  width="70px" height="57px"/></a></span>
								<span class="img-tit"><a href="javascript:void(0)">五个文字</a></span>
							 </li>
							 <li class="img-li-a">
							    <span class="img-sp"><a href="javascript:void(0)"><img src="images/bg2.gif"  border="0"width="70px" height="57px"/></a></span>
								<span class="img-tit"><a href="javascript:void(0)">五个文字</a></span>
							 </li>
							 <li class="img-li-a">
							    <span class="img-sp"><a href="javascript:void(0)"><img src="#" border="0"  width="70px" height="57px"/></a></span>
								<span class="img-tit"><a href="javascript:void(0)">五个文字</a></span>
							 </li>
						   </ul>--></div>
				   </div>
			 </div>
			 <div id="boxone-fot"></div>
		 </div>
          <!-- 下半部分 -->
		 <div id="boxtwo">
		 <!-- 开始这里 -->
		     <div id="menu-box">
<div id="flashlabel">
					     <div id="label-a"><a href="javascript:void(0)" onclick="changePal('userName')">
               <img src="images/b-renwu.jpg" border="0" onclick="MM_showHideLayers('label-a','','hide','label-a2','','show','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide')" /></a></div>
						 <div id="label-a2"><a href="javascript:void(0)" 
             onclick="MM_showHideLayers('label-a','','hide','label-a2','','show','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide');">
             <img src="images/b-renwu-cik.jpg" border="0" /></a></div>
						 <div id="label-b"><a href="javascript:void(0)" onclick="changePal('address')"><img src="images/b-diqu.jpg" border="0" /></a></div>
						 <div id="label-b2"><a href="javascript:void(0)" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','hide','label-b2','','show','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide')"><img src="images/b-diqu-cik.jpg" border="0" /></a></div>
						 <div id="label-c"><a href="javascript:void(0)" onclick="changePal('org')"><img src="images/b-zhuzhi.jpg" border="0" /></a></div>
						 <div id="label-c2"><a href="javascript:void(0)" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','hide','label-c2','','show','label-d','','show','label-d2','','hide')"><img src="images/b-zhuzhi-cik.jpg" border="0" /></a></div>
						 <div id="label-d"><a href="javascript:void(0)" onclick="changePal('subject')"><img src="images/b-guanjian.jpg" border="0" /></a></div>
						 <div id="label-d2"><a href="javascript:void(0)" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','hide','label-d2','','show')"><img src="images/b-guanjian-cik.jpg" border="0" /></a></div>
			 </div>
			 </div>
		 <!-- 结束这里-->
			 <div id="hidden-box-a">
			     <div class="box-row-a">
				     <div class="row-a-left">
					    <!--<ul>
						  <li><span class="name-font"><a href="javascript:void(0)">邓华德</a></span><span class="txt-font"><a href="javascript:void(0)">巴西篮协欲上诉FIBA 矛头直指邓华德要求追加处罚</a></span></li>
						  <li><span class="name-font"><a href="javascript:void(0)">科比</a></span><span class="txt-font"><a href="javascript:void(0)">蛰伏181天科比拨云见日 征服全世界他只用11分钟 
</a></span></li>
						  <li><span class="name-font"><a href="javascript:void(0)">邓华德</a></span><span class="txt-font"><a href="javascript:void(0)">巴西篮协欲上诉FIBA 矛头直指邓华德要求追加处罚</a></span></li>
						  <li><span class="name-font"><a href="javascript:void(0)">邓华德</a></span><span class="txt-font"><a href="javascript:void(0)">巴西篮协欲上诉FIBA 矛头直指邓华德要求追加处罚</a></span></li>
						  <li><span class="name-font"><a href="javascript:void(0)">科比</a></span><span class="txt-font"><a href="javascript:void(0)">蛰伏181天科比拨云见日 征服全世界他只用11分钟 
</a></span></li>
						  <li><span class="name-font"><a href="javascript:void(0)">邓华德</a></span><span class="txt-font"><a href="javascript:void(0)">巴西篮协欲上诉FIBA 矛头直指邓华德要求追加处罚</a></span></li>
						</ul>
						--><div class="clear"></div>
					 </div>
					 <div  id="row-a-right"  class="row-a-right">
           
           </div>
				 </div>
				 <div class="box-row-b">
				      <div id="row-b-left" class="row-b-left"></div>

					  <div id="row-b-right" class="row-b-right">
<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
      id="smalltouchgraph" width="365" height="175"
      codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
      <param name="movie" value="smalltouchgraph.swf" />
      <param name="quality" value="high" />
      <param name="bgcolor" value="#000000" />
      <param name="allowScriptAccess" value="sameDomain" />
      <embed src="smalltouchgraph.swf" quality="high" bgcolor="#000000"
         width="365" height="175" name="smalltouchgraph" align="middle"
        play="true"
        loop="false"
        quality="high"
        allowScriptAccess="sameDomain"
        type="application/x-shockwave-flash"
        pluginspage="http://www.adobe.com/go/getflashplayer">
      </embed>
  </object>
</div>   
				 </div>
			 </div>		 
		 </div>
	  </div>
	  <div id="foot">Copyright 2010. <%=StaticData.SOFTCOMPANY%></div>  
  </div>
</body>
</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	String openCondtion = request.getParameter("openCondtion");  //弹出页面时用
  if(openCondtion == null || openCondtion.length() < 1 || openCondtion == ""){
    openCondtion = "";
  }
%>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>图片管理</title>
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/img.css" rel="stylesheet" type="text/css" />

<link href="<%=contextPath %>/subsys/inforesource/tree1/css/tree.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/css-other.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/index1.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/default/style.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />

<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/const.js'></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.hotkeys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.jstree.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.autocomplete.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/img/img.js'></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/img/common.js'></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/img/imgPage.js'></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/img/tag.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/img/tree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/cockpit/js/FusionCharts.js"></script>
<script type="text/javascript">
 var conData = "<%=openCondtion%>";
 $(document).ready(function(){
  doSeachAllImage(1);
  if(conData){
    $("#imgSearch").val(conData);
  }
  ajax1();
});
</script>
</head>
<body>
<div id="container">
    <div id="left-box">
	   <div id="left-top">
	       <iframe id="sampleframe1" name="sampleframe1" width="100%" height="100%" frameborder="0" src="tree1/menu2.jsp" style="border: 0px"></iframe>
	   </div>
	   <div class="clear"></div>
	   
	   <div id="left-cen"  class="left1">
	       <div class="leftHead"></div>
		   <div id="left-frame" style="height:auto;min-height:462px;">		<!-- tree1/_demo/tree.jsp -->		   
		       <div id="leftPic"></div>
		    </div>
		   <div class ="leftFoot"></div>
	   </div>
	   
	   <!-- 个人tag -->
	   <div id="left-cen" class="left2" style="display:none;">
	       <div class="leftHead"></div>
		   <div id="left-frame">		
		   <iframe id="sampleframe2" name="sampleframe2" width="100%" height="100%" frameborder="0" src="<%=contextPath %>/subsys/inforesource/tree1/_demo/selftag.jsp" style="border: 0px"></iframe>   
		       </div>
		   <div class ="leftFoot"></div>
	   </div>
	   	
	  <!-- 元数据 --> 
	   <div id="left-cen"  class="left3" style="display:none;">
	     <div class="leftHead"></div>  
		   <div id="left-frame" style="height:auto;min-height:462px;" > 		<!-- tree1/_demo/tree.jsp -->   	  
		   	   <div id="container"  class="treeHeight">
		        <div id="demo" style="width:185px;overflow:hidden;"></div>
		       </div> 
		       <div class ="line"></div>
			     <div class="button-bg">
							<div class="but-a"><a href="javascript:void(0);" onclick="filtag();return false;"></a></div>
							<div class="but-b"><a href="javascript:void(0);" onclick="javascript:tomanage();"></a></div>
					</div>  
		   </div> 		  
		   <div class ="leftFoot"></div>
	   </div>
	   <div id="left-pic">
	    <div class ="leftHead"></div>	    
			<div id="left-pic-frame"><a href="javascript:void(0);">图片框架</a></div>
			<div class ="leftFoot"></div>
	   </div>
	</div>

    <div id="right-box">
	    <div id="right-top">
		   <span class="label-span"><a href="javascript:void(0);" onclick="doTemp();return false;">美国</a></span>			 
			 <span class="label-span"><a href="javascript:void(0);" onclick="doTemp();return false;">民生</a></span>					 
		</div>
		<div id="right-search">
		    <div id="search-box">
		      <div id="Search-bg"><form action="" method="get"><input type="text" id="imgSearch" class="Search-bg-form" />
		   </form></div>
		      <div id="goto"><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image1','','<%=contextPath %>/subsys/inforesource/tree1/images/button-search-hover.jpg',1)"><img src="images/button-search.jpg" name="Image7" width="106" height="25" border="0" id="Image1" /></a></div>
		    </div>
		</div>
		<div id="right-content">
		   <div class="r-h-t"></div>
		   <div id="r-con-cen">
		       <div id="up-box">
			      <div id="up-left">
				     <div id="up-left-head">
					    <div class="choice-box">
						  <ul>
						    <!--li是快捷标签-->
						    <li><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image8','','images/x-hover.gif',1)"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/x-a.gif" name="Image8" width="16" height="14" border="0" id="Image8" /></a><a href="#">外交部</a></li>
						    <!--li是快捷标签-->
						    <li><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image8','','images/x-hover.gif',1)"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/x-a.gif" name="Image8" width="16" height="14" border="0" id="Image8" /></a><a href="#">中国</a></li>
							<!--li是快捷标签-->
						    <li><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image8','','images/x-hover.gif',1)"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/x-a.gif" name="Image8" width="16" height="14" border="0" id="Image8" /></a><a href="#">新闻</a></li>
						  </ul>
					   </div>
					 <div class="clear"></div>
					 </div>
					 <!--这里是FLASH图开始-->
					 <div id="flash-box">
					   <div id="flash-con">
					      <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" 
									id="linechart" width="524" height="138" 
									codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab"> 
									<param name="movie" value="swf/linechart.swf" /> 
									<param name="quality" value="high" /> 
									<param name="bgcolor" value="#869ca7" /> 
									<param name="allowScriptAccess" value="sameDomain" /> 
									<embed src="swf/linechart.swf" quality="high" bgcolor="#869ca7" 
									width="524" height="138" name="linechart" align="middle" 
									play="true" 
									loop="false" 
									quality="high" 
									allowScriptAccess="sameDomain" 
									type="application/x-shockwave-flash" 
									pluginspage="http://www.adobe.com/go/getflashplayer"> 
									</embed> 
              </object>
					   </div>
					   <div id="flashlabel">
					     <div id="label-a"><a href="#"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/man.jpg" border="0" onclick="MM_showHideLayers('label-a','','hide','label-a2','','show','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide')" /></a></div>
						 <div id="label-a2"><a href="#" onclick="MM_showHideLayers('label-a','','hide','label-a2','','show','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/man-ck.jpg" border="0" /></a></div>
						 <div id="label-b"><a href="#" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','hide','label-b2','','show','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/place.jpg" border="0" /></a></div>
						 <div id="label-b2"><a href="#" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','hide','label-b2','','show','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/place-ck.jpg" border="0" /></a></div>
						 <div id="label-c"><a href="#" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','hide','label-c2','','show','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/z-keyword.jpg" border="0" /></a></div>
						 <div id="label-c2"><a href="#" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','hide','label-c2','','show','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/z-keyword-ck.jpg" border="0" /></a></div>
						 <div id="label-d"><a href="#" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','hide','label-d2','','show','label-e','','show','label-e2','','hide')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/g-keyword.jpg" border="0" /></a></div>
						 <div id="label-d2"><a href="#" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','hide','label-d2','','show','label-e','','show','label-e2','','hide')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/g-keyword-ck.jpg" border="0" /></a></div>
						 <div id="label-e"><a href="#" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','hide','label-e2','','show')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/time.jpg" border="0" /></a></div>
						 <div id="label-e2"><a href="#" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','hide','label-e2','','show')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/time-ck.jpg" border="0" /></a>
						 </div>
					   </div>
					 </div>
					 <!--这里是通用框集开始-->
					<div id="imgContainer">
				    
					</div>
					 <!--这里是通用框集结束-->
					 		 			 
					 <!--下边是翻页-->
					 <div class="next-page">					 
           </div>
				  </div>				  
				   <!--这里开始是右边相关-->
				  <div id="up-right">
				    <div id="up-right-head"></div>
					<!--这里开始是右边框架集-->
				    <div class="up-right-con-a">
					    <span class="h3"><a href="#">2010图书信息</a></span>
						<span class="block">
						<a href="#">我们在生活中经常发现,形势的出现和发展是不以人的意志为转移的,无论我们在主观上承认与否......</a>
						</span>
						<span class="block">
						    <p class="con-txt2"><a href="#">2010图书信息</a></p>
						    <p class="con-dot">&nbsp;-&nbsp;</p>
						    <p class="con-txt2"><a href="#">内容简介</a></p>
						</span></div>
						<div class="up-right-con-a">
					    <span class="h3"><a href="#">超强台风或23日登陆粤</a></span>
						<span class="block">
						<a href="#">今年的第１３号台风"鲇鱼"１９日８时再次加强为超强台风，逐渐逼近广东沿海并将可能于２３日登陆广东。......</a>
						</span>
						<span class="block">
						    <p class="con-txt2"><a href="#">2010图书信息</a></p>
						    <p class="con-dot">&nbsp;-&nbsp;</p>
						    <p class="con-txt2"><a href="#">内容简介</a></p>
						</span></div>
						<div class="up-right-con-a">
					    <span class="h3"><a href="#">中日民间交流迅速回暖</a></span>
						<span class="block">
						<a href="#">两国多批次青少年互访成行，维持了友好交流的气氛与基础 本月12日起，400名中国大学生开始访问日本；同时，1000名日本青少年访华参观南京世博会的行程也即将启动。......</a>
						</span>
						<span class="block">
						    <p class="con-txt2"><a href="#">2010图书信息</a></p>
						    <p class="con-dot">&nbsp;-&nbsp;</p>
						    <p class="con-txt2"><a href="#">内容简介</a></p>
						</span></div>
						<div class="up-right-con-a">
					    <span class="h3"><a href="#">第三届北京人权论坛开幕</a></span>
						<span class="block">
						<a href="#">第三届“北京人权论坛”19日上午在北京开幕。来自28个国家和地区以及联合国等国际组织的近百名人权高级官员和专家学者出席论坛。......</a>
						</span>
						<span class="block">
						    <p class="con-txt2"><a href="#">2010图书信息</a></p>
						    <p class="con-dot">&nbsp;-&nbsp;</p>
						    <p class="con-txt2"><a href="#">内容简介</a></p>
						</span></div>
						<div class="up-right-con-a">
					    <span class="h3"><a href="#">严打制售假冒伪劣商品</a></span>
						<span class="block">
						<a href="#">１９日，国务院常务会议决定，从２０１０年１０月底开始，在全国开展为期半年的打击侵犯知识产权和制售假冒伪劣商品专项行动。

           ......</a>
						</span>
						<span class="block">
						    <p class="con-txt2"><a href="#">2010图书信息</a></p>
						    <p class="con-dot">&nbsp;-&nbsp;</p>
						    <p class="con-txt2"><a href="#">内容简介</a></p>
						</span></div>
						
					<!--这里是右边框架集结束--> 
				  </div>
			   </div>
			   <div class="clear"></div>
			   <div id="down-box">
                  <div class="down-box-top"></div>
				  <div class="down-box-cen">
				     <div class="db-up">
					   <ul>
					     <li><a href="#">形势与政策论文</a></li>
						 <li><a href="#">形势与政策心得</a></li>
						 <li><a href="#">2010形势与政策</a></li>
						 <li><a href="#">2010形势与政策论文</a></li>
						 <li><a href="#">形势与政策学习心得</a></li>
					   </ul>
					 </div>
                     <div class="db-up">
					   <ul>
					   <li><a href="#">形势与政策论文</a></li>
						 <li><a href="#">形势与政策心得</a></li>
						 <li><a href="#">2010形势与政策</a></li>
						 <li><a href="#">2010形势与政策论文</a></li>
						 <li><a href="#">形势与政策学习心得</a></li>
					   </ul>
					 </div>
				  </div>
			   </div>
		   </div>
		   <div class="rightFoot"></div>
		</div>
	</div>
</div>
</body>
<script>
function changeChart(fal) {
  
}
/*var chart2 = new FusionCharts("cockpit/Charts/Column2D.swf", "columnChart", "524", "138", "0", "1");
chart2.setXMLUrl(contextPath + "/yh/subsys/inforesouce/act/YHCockpitAct/getColumnData.act");	
chart2.render("flash-con");
*/
var chart = new FusionCharts("cockpit/Charts/Line.swf", "lineChart", "524", "138", "0", "0");
chart.setXMLUrl(contextPath + "/yh/subsys/inforesouce/act/YHCockpitAct/getLineData.act");		   
chart.render("flash-con");

</script>

</html>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
	String openCondtion = request.getParameter("openCondtion");  //弹出页面时用
  if(openCondtion == null || openCondtion.length() < 1 || openCondtion == ""){
    openCondtion = "nodata";
  }
 String attach =  YHSysProps.getString("attachFilePath");
 if(attach == null || attach.length() < 1){
   attach = "attach";
 }else{
   attach = attach.substring(attach.indexOf("attach")).trim();
 }
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>文件管理中心</title>
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/css.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/tree.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/css-other.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/index1.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/basic.css" rel="stylesheet" type="text/css" />

<link href="<%=contextPath %>/subsys/inforesource/tree1/css/default/style.css" rel="stylesheet" type="text/css" />
<link href="<%=contextPath %>/subsys/inforesource/tree1/css/jquery.autocomplete.css" rel="stylesheet" type="text/css" />
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/const.js'></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.js"></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/common.js'></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/index-common.js'></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/Page.js'></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/Page2.js'></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/tag.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/tree.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.hotkeys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.jstree.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.autocomplete.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/cockpit/js/FusionCharts.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/selTree.js"></script>
<script type='text/javascript' src='<%=contextPath %>/subsys/inforesource/tree1/js/jquery.simplemodal.js'></script>
<script type="text/javascript">
var openData = "";
var attachFilePath = "<%=attach%>";
$(document).ready(function(){
  openData = "<%=openCondtion%>"; 
  if(openData != "nodata"){
    $("#searchText").val(openData);    
    Search(1, 10);
    relationWordAjax(openData); 
  }else{
    articleListByKeyIDs("",1); 
  }
  ajax1();
});
var typeConst = "2";
function changePal(type) {
  typeConst = type ;
  //时间
  if (type == "1") {
    MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','hide','label-e2','','show');
    showChart("line" , searchTime);
    //关键字
  } else if (type == "5") {
    MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','hide','label-d2','','show','label-e','','show','label-e2','','hide');
    showChart("column" , searchTime);
    //地区
  } else if (type == "3") {
    MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','hide','label-b2','','show','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide');
    showChart("column" , searchAdress);
    //主题词
  } else if (type == "4" ) {
    MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','hide','label-c2','','show','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide');
    showChart("column" , searchOrg);
    //人物
  } else {
    MM_showHideLayers('label-a','','hide','label-a2','','show','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide');
    showChart("column" , searchPerson);
  }
}
function changeDate(data) {
  var data2 = [];
  for (var i = 0 ;i < data.length; i++) {
     var str = data[i].label ;
     var date = str.substr(4);
     var year = str.substring(2,4);
     
     if (date.indexOf("0") == 0) {
       date = date.substr(1) + "月";
     } else {
       date = date + "月";
     }
     date = year + "年" + date;
     var tmp = {};
     tmp.value = data[i].value;
     tmp.label =  date;
     data2.push(tmp);
  }
  return data2;
}
function showChart(type , data) {
  //data = [{"label":"大","value":"333"},{"label":"小","value":"44"}];
  var src = contextPath + "/subsys/inforesource/cockpit/Charts/Line.swf";
  var chartData = null;
  if (type == "column") {
    src = contextPath + "/subsys/inforesource/cockpit/Charts/Column2D.swf";
    chartData = {"chart":{"unescapeLinks":"0","bgColor":"FFFFFF,FFFFFF" ,"showBorder":"0","useroundedges":"1",    "showborder":"0",    "exportenabled":"1",    "exportshowmenuitem":"0"},"data":[]};
  } else {
    chartData = {"chart":{"shownames":"0","showLabels":"1","showColumnShadow":"1","animation":"1","showAlternateHGridColor":"1" ,"AlternateHGridColor":"ff5904","divLineColor":"ff5904",  "divLineAlpha":"20" ,"alternateHGridAlpha":"5" ,"canvasBorderColor":"666666", "baseFontColor":"666666" ,"lineColor":"FF5904",  "lineAlpha":"85" , "showValues":"1" , "rotateValues":"1" ,"valuePosition":"auto" , "paleteThemeColor":"6699FF"},"data":[]};
  }
  var chart = new FusionCharts(src, "chart", "524", "138", "0", "0");
  chartData.data = data;
  chart.setJSONData(chartData);		   
  chart.render("flash-con");
}
function downLoad(){
  var requestURL = contextPath + "/yh/subsys/inforesouce/act/YHBeachDownLoadFileAct";
  location.href = requestURL + "/beanchDownload.act";
}
$(document).ready(function () {
	$('#basic-modal input.basic, #basic-modal a.basic').click(function (e) {
		e.preventDefault();
		$('#basic-modal-content').modal();
	});
});
</script>
</head>
<body  onload="">
<div id="container">
    <div id="left-box">
	   <div id="left-top">
	       <iframe id="sampleframe1" name="sampleframe1" width="100%" height="100%" frameborder="0" src="tree1/menu1.jsp" style="border: 0px"></iframe>
	   </div>
	   <div class="clear"></div>
	   <!-- tag -->
	   <div id="left-cen"  class="left1">
	       <div class="leftHead"></div>
		   <div id="left-frame" style="height:auto;min-height:462px;">		<!-- tree1/_demo/tree.jsp -->
		     <!--<iframe id="sampleframe1" name="sampleframe1" width="100%" height="100%" frameborder="0" src="/subsys/inforesource/tree1/_demo/tag.jsp" style="border: 0px; overflow:hidden"></iframe>    
		       
		       -->
		       <div id="leftPic"></div>
		    </div>
		   <div class ="leftFoot"></div>
	   </div>
	   <!-- 个人tag -->
	   <div id="left-cen" class="left2">
	       <div class="leftHead"></div>
		   <div id="left-frame" style="height:auto;min-height:462px;">		
            <div id="container"  class="treeHeight">
		        <div id="demo2" style="width:185px;overflow:hidden;"></div>
		       </div> 
         
		       </div>
		   <div class ="leftFoot"></div>
	   </div>	  
	   <!-- 元数据 --> 
	   <div id="left-cen"  class="left3" >
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
	       <!--  <div><img src="images/left-head.jpg"/></div>  -->
	       <div class ="leftHead"></div>
			<div id="left-pic-frame" style="margin-top:0px">
			<object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000"
			id="smalltouchgraph" width="186" height="230" 
			codebase="http://fpdownload.macromedia.com/get/flashplayer/current/swflash.cab">
			<param name="movie" value="swf/smalltouchgraph.swf<%
      if (!"nodata".equals(openCondtion)) {
        out.print("?data=" + openCondtion);
      }%>"/>
			<param name="quality" value="high" />
			<param name="bgcolor" value="#000000" />
			<param name="allowScriptAccess" value="sameDomain" />
			<embed src="swf/smalltouchgraph.swf" quality="high" bgcolor="#000000"
				width="202" height="252" name="smalltouchgraph" align="middle"
				play="true"
				loop="false"
				quality="high"
				allowScriptAccess="sameDomain"
				type="application/x-shockwave-flash"
				pluginspage="http://www.adobe.com/go/getflashplayer">
			</embed>
	</object>
	 
	<div id="leftPic"></div></div>
			<div class ="leftFoot"></div>
	   </div>
	</div>
    <div id="right-box">
	    <div id="right-top">
		   <!--   <span class="label-span"><a href="#">自标签一</a></span>
			 <span class="label-span"><a href="#">自标签二</a></span>
			 <span class="label-span"><a href="#">自标签一</a></span>	 	-->		 
		</div>
		<div id="right-search">
		    <div id="search-box">
		      <div id="Search-bg"><input type="text" id="searchText" class="Search-bg-form"/>
		   </div>
		      <div id="goto"><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image1','','<%=contextPath %>/subsys/inforesource/tree1/images/button-search-hover.jpg',1)"><img src="images/button-search.jpg"  onclick="doSearch();" name="Image7" width="106" height="25" border="0" id="Image1" /></a></div>
		    </div>
		</div>
		<div id="right-content">
		   <div class="yy"></div>
		   <div id="r-con-cen">
		       <div id="up-box">
			      <div id="up-left">
				     <div id="up-left-head">
					    <div class="choice-box">
						  <ul id="label-up">
						    <!--li是快捷标签
						   <li><a href="#"><img src="images/x-a.gif" name="Image8" width="16" height="14" border="0" id="Image8" /></a></li>
						    <li><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image8','','images/x-hover.gif',1)"><img src="images/x-a.gif" name="Image8" width="16" height="14" border="0" id="Image8" /></a><a href="#">词组词</a></li>
						    <li><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image8','','images/x-hover.gif',1)"><img src="images/x-a.gif" name="Image8" width="16" height="14" border="0" id="Image8" /></a><a href="#">词组词</a></li>
						  -->
						  </ul>
						  <span id='basic-modal'>
                 <input type="button" onclick="downLoad();"  value="下载"/>  
                 <input type="button" id="aaaa"  class='basic demo' value="批量转存"/>
             </span>
					   </div>
					 <div class="clear"></div>
					 </div>
					 <!--这里是FLASH图开始-->
					 <div id="flash-box">
					   <div id="flash-con"><!--这里是FLASH图框架宽式524px高是138px-->
					   </div>
					    <div id="flashlabel">
					   <div id="label-a"><a href="javascript:void(0)" onclick="changePal('2')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/man.jpg" border="0" /></a></div>
						 <div id="label-a2"><a href="javascript:void(0)" onclick="MM_showHideLayers('label-a','','hide','label-a2','','show','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/man-ck.jpg" border="0" /></a></div>
						 <div id="label-b"><a href="javascript:void(0)" onclick="changePal('3')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/place.jpg" border="0" /></a></div>
						 <div id="label-b2"><a href="javascript:void(0)" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','hide','label-b2','','show','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/place-ck.jpg" border="0" /></a></div>
						 <div id="label-c"><a href="javascript:void(0)" onclick="changePal('4')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/z-keyword.jpg" border="0" /></a></div>
						 <div id="label-c2"><a href="javascript:void(0)" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','hide','label-c2','','show','label-d','','show','label-d2','','hide','label-e','','show','label-e2','','hide')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/z-keyword-ck.jpg" border="0" /></a></div>
						 
						 <div id="label-e"><a href="javascript:void(0)"  onclick="changePal('1')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/time.jpg" border="0" /></a></div>
						 <div id="label-e2"><a href="javascript:void(0)" onclick="MM_showHideLayers('label-a','','show','label-a2','','hide','label-b','','show','label-b2','','hide','label-c','','show','label-c2','','hide','label-d','','show','label-d2','','hide','label-e','','hide','label-e2','','show')"><img src="<%=contextPath %>/subsys/inforesource/tree1/images/time-ck.jpg" border="0" /></a>
						 </div>
					   </div>
					 </div>
					 <!--这里是通用框集开始-->
					 <div id ="contents">
					 <!--  
				    <div class="up-left-con-a">
					    <span class="h1"><a href="#">形势与政策</a></span>
							<span class="h2"><a href="#"> _ 维基百科</a></span> 
							 <p class="con-txt"><a href="#">《形势与政策》内容包括了与国内外形势与政策紧密联系的8个专题，对帮助青年大学生深刻理解和领会党的最新理论成果、认识当前国内国际政治经济形势......</a></p>
						    <p class="con-txt2"><a href="#">2010图书信息</a></p>
						    <p class="con-dot">&nbsp;-&nbsp;</p>
						    <p class="con-txt2"><a href="#">内容简介</a></p>
						    <p class="con-dot">&nbsp;-&nbsp;</p>
						    <p class="con-txt2"><a href="#">图书目录</a></p>
						    <p class="con-dot">&nbsp;-&nbsp;</p>
						    <p class="con-txt2"><a href="#">2009年图书信息</a></p>
					</div> -->
				</div>	
					 <!--这里是通用框集结束-->			 
					 <!--下边是翻页-->
<div class="next-page">
   
	   <!--  <table width="526" border="0" cellspacing="0" cellpadding="0">
	   <tr>
	    <td width="63"><a href="#">《&nbsp;上一页</a></td>
	    <td width="68" class="page-td"><a href="#">1</a></td>
	    <td width="24" class="page-td"><a href="#">2</a></td>
	    <td width="24" class="page-td"><a href="#">3</a></td>
	    <td width="24" class="page-td"><a href="#">4</a></td>
	    <td width="24" class="page-td"><a href="#">5</a></td>
	    <td width="24" class="page-td"><a href="#">6</a></td>
	    <td width="24" class="page-td"><a href="#">7</a></td>
	    <td width="24" class="page-td"><a href="#">8</a></td>
	    <td width="24" class="page-td"><a href="#">9</a></td>
	    <td width="24" class="page-td"><a href="#">10</a></td>
			<td width="24" class="page-td"><a href="#">11</a></td>
			<td width="24" class="page-td"><a href="#">12</a></td>
		  <td width="131"><a href="#">&nbsp;&nbsp;&nbsp;&nbsp;下一页&nbsp;》</a></td>
	   </tr>
	  </table>
    -->
</div>
				  </div>				  
				   <!--这里开始是右边相关-->
				  <div id="up-right">
				    <div id="up-right-head"></div>
				    
					<!--这里开始是右边框架集-->
					<div id="right-contents">
				   <div class="up-right-con-a">
					  <!--  <span class="h3"><a href="#">2010图书信息</a></span>
						<span class="block">
						<a href="#">我们在生活中经常发现,形势的出现和发展是不以人的意志为转移的,无论我们在主观上承认与否......</a>
						</span>
						<span class="block">
						    <p class="con-txt2"><a href="#">2010图书信息</a></p>
						    <p class="con-dot">&nbsp;-&nbsp;</p>
						    <p class="con-txt2"><a href="#">内容简介</a></p>
						</span>  -->
					</div></div>
					<!--这里是右边框架集结束--> 
				  </div>
			   </div>
			   <div class="clear"></div>
			   <div id="down-box">
                  <div class="down-box-top"></div>
				  <div class="down-box-cen">
				     <!--<div class="db-up">
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
				  -->
				  <font size=2>暂无搜索词，请点击搜索。</font>
				  </div>
			   </div>
		   </div>
		   <div class="rightFoot"></div>
		   <!--  <div><img src="images/right-foot.jpg" /></div> -->
		</div>
	</div>
</div>
<div id="basic-modal-content">
	<h3>批量转存</h3>
   <iframe src="<%=contextPath%>/subsys/inforesource/savefile/index.jsp?attachId=&attachName=&module=" width='100%' height='450px;'></iframe>
</div>
</body>
</html>
				
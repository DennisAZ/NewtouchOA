<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>jsTree v.1.0 - full featured demo</title>
	<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.cookie.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.hotkeys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/subsys/inforesource/tree1/js/jquery.jstree.min.js"></script>
	<link href="css/default/style.css" rel="stylesheet" type="text/css" />

<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<style type="text/css">
	html, body { 
		margin:0; padding:0; 
		font: normal 12px verdana;
		background-color:#f0f1f6;
		
	}
	body, td, th, pre, code, select, option, input, textarea { font-family:verdana,arial,sans-serif; font-size:12px; }
	.demo, .demo input, .jstree-dnd-helper, #vakata-contextmenu { font-size:12px; font-family:Verdana; }
	#container {
	overflow:hidden;
	position:relative;

}
	#demo {
	font: normal 13px verdana;
	width:auto;

}

	#menu { height:30px; overflow:auto; }
	#text { margin-top:1px; }

	#alog { font-size:9px !important; margin:5px; border:1px solid silver; }

	.node_input {
	  display:block;
		height:18px;
		width:91px;
		border:none;
    position:relative;
    top:1px;
    left:30px;
		background: url(imgs/input_text.jpg) no-repeat left top;
	}
	
	.tree_header {
		cursor: pointer;
		height:27px;
		background: url(imgs/tree_header.jpg) no-repeat left top;
	}
	
	a {
	  outline-style:none;
	}
	
	a:link,a:visited,a:active,a:hover {
		text-decoration: none;
	}
	
	.tree_header span{
		position:relative;
		left:30px;
		top:8px;
	}
	
  .node_input input{
    width:90px;
    display:block;
    border:none;
    background-color:transparent;
  }
	</style>
	<style>
	#txt-a {
	  word-wrap: break-word;
	}
	#font1{
	  position:relative;
	  font-family: "微软雅黑", "宋体";
	  color:#908743;
	}
	#font2{
	
		 font-family: "微软雅黑", "宋体";
		 color:#456412;
	}
	#font3{
		font-family: "微软雅黑", "宋体";
		color:red;
	}
	#font4{
		font-family: "微软雅黑", "宋体";
		color:112200;
	}
	.font4 .span{
		font-family: "微软雅黑", "宋体";
		color:112200;
	}

	#font5{
	font-family: "微软雅黑", "宋体";
	color:Fuchsia;
	}
	.font5 .span{
		font-family: "微软雅黑", "宋体";
		color:Fuchsia;
	}
	#font6{
	font-family: "微软雅黑", "宋体";
	color:Aqua;
	}
	#font7{
		font-family: "微软雅黑", "宋体";
		color:Lime;
	}
	#font8{
		font-family: "微软雅黑", "宋体";
		color:Olive;
	}
	
	#fonyh{
		color:#999900;
		font-family: "微软雅黑", "宋体";

	}
	#font10{
		font-family: "微软雅黑", "宋体";
		color:#779900;
		font-variant:normal|small-caps； 
	}
	.font11 {
	   position:relative;
		 font-family: "微软雅黑", "宋体";
		 color:#4523RR;
	}
	.font12{
		font-family: "微软雅黑", "宋体";
		color:555666;
	}
	.font13{
		font-family: "微软雅黑", "宋体";
		color:#005611;
	}
	.font14{
		font-family: "微软雅黑", "宋体";
		color:#990099;
	}
	.font15{
		font-family: "微软雅黑", "宋体";
		style:color#red;
		font-variant:normal|small-caps； 
	}

	#anniu{
		position:relative;
		margin-top:250px;
		margin-left:5px;
	}
	</style>
	<script type="text/javascript">
	 $("document").ready(function(){	
	 ajax();
});
 /**
  * 返回第一层tag
  */
 	function ajax(){
 	  $.ajax({
 			url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/firstLevelTag.act",
 		  type:"post",			
 			success:function(data){
 			//alert(data);
 	 		var temp = eval("("+data+")");
 	 		insertData(temp);
 			}
 	  });	 
 	}
 	/**
 	 * 插入tag云图
 	 */
 		function insertData(data){ 
 			if(data){ 
 					if(data.rtState == '0'){
 						$("#leftPic").empty();
 						var rtdata = data.rtData;
 						//alert(rtdata);
 						var cont = "";	
 						var contname = "";
 						if(rtdata.length > 0){	
 	            var arr = new Array();
 	            for(var j=0; j<rtdata.length; j++){
 	             var intnums = rtdata[j].nTime;
 	             if(!intnums){
 	    				    intnums =  rtdata[j].nTimes;  
 	      				}    				 
 	    				  var number = parseInt(intnums);
 	    				  arr.push(number);
 	            }
 	            arr = arr.sort(sortNumber); //sortNumber 是排序的一张写法 	
 	            		
 	            var count=14; 
 	  				  var original=new Array;//原始数组 
 	  				  for (var i=0;i<count;i++){ 
 	  				  original[i]=i+1; 
 	  				  } 
 	  				  original.sort(function(){ 
 	  				  return 0.5 - Math.random();//随机排序 的一种
 	  				  }); 		
 	  				for(var i=0; i<rtdata.length && i<14; i++){  				
 	  				  contname = rtdata[i].Keyword; 
 	  				  var intnum = rtdata[i].nTime;
 	  				  if( !intnum ){
 	  				    intnum =  rtdata[i].nTimes;  
 	    				 }  				 
 	  				  var number = parseInt(intnum); 
 	           // alert("font"+original[i]+"==="+rtdata[i].Keyword);
 	  					cont += "<a id=\"font"+original[i]+"\" onclick = KeyWord(";
 							cont += rtdata[i].KeyID;
 							cont += ",";
 							cont += "'"+rtdata[i].Keyword+"'";
 							cont +=	") style=\"cursor:pointer;\">";
 							cont += "<font size=\""+returnFont(arr, intnum)+"\">"+rtdata[i].Keyword+"</font>";
 							cont += "</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";						
 	      		}
 					}else{
 	 					cont +="<br><br>"
 	           cont += "<div style=\"margin-top:-5px;\"><font size=3 color=\"#001199\">暂</font><font color=\"#00fffa\">无</font><font size=3 color=\"#22FF22\">TAG</font><font color=\"#00fffa\">云</font><font  size=3 color=\"#22FF22\">图</font><font color=\"#FF2222\">标</font>签<div>";
 					}
  						$("#leftPic").append(cont);
 				}
 			}
 		}	
 		/**
 		* 数组进行排序
 		*/
 		function sortNumber(a, b){
 		  return a-b;
 		}
 		/**
 		* 返回改权值下的字体
 		*/
 		function returnFont(arr, ntime){
 		  var max = arr[arr.length-1];
 		  var min = arr[0];
 		  var MAXFONT = 5;//设置最大字体为4
 		  var flag = parseFloat((max - min)/MAXFONT);
 		  for(var i=0; i<MAXFONT; i++){
 		    if(ntime >= (flag*i) && ntime < (flag*(i+1))){		
 			    if(i==0){i=1;}   
 			     return (i+1);
 			  }
 		  }	
 		  return MAXFONT; 
 		}
 		/**
    点击 最热主题词  所返回主题词内容
**/
function KeyWord(keyId,keyword){
   var len = window.parent.getLen();//文档列表中的长度
   if(len==0){
     alert("没有相关文档列表");
     return false;
    }
   
  var leng = window.parent.getLeng();
  
  if(leng == 6){
    alert("最大层次为六层");
    return false;
   }
   rowTable(keyId,keyword); //content.jsp页面显示级别关系如：科技>>教育>>文化
   window.parent.setColKeyWord(keyId,keyword); //点击tag云图  显示相关文档		
   $.ajax({
   url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/Keyword.act?keyID=" + keyId,    
   type:"post",
   success:function(data){// alert(data);
     var temp = eval("("+data+")");
     //alert(temp);
     insertData(temp);
   }
 });
   window.parent.doClickTag(keyword);
}
/*
显示tag 云图的层次关系
如：科技>>教育>>文化
*/
function rowTable(keyID,keyWord){
	var key = keyID;
	if(key){
	  window.parent.setLayer(keyID,keyWord);
	}  
}		
/**
 * 根据返回的tagIds重新查找tag云图
 */
	function getTagsByTagIds(tagIds){			
	  $.ajax({
			url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/Keyword.act?keyID=" + tagIds ,
		  type:"post",			  
			success:function(data){
	    var temp = eval("("+data+")");		
	     insertData(temp);
			}
	  });		 
  }

	</script>
</head>
<body style="overflow-y:hidden;overflow-x:hidden;">
<form id="form1" name="form1" onsubmit="" action="">
<div id="container">
<div id="demo" style="width:165px;overflow:hidden;"></div>
</div>
</form>
<div>
<!-- 
<input type="text" value="" onkeydown="keydown(event)"/> 
 -->
</div>
<div id="leftPic">
</div>
</body>
</html>
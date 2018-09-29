<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>图片新闻</title>
	<link rel="stylesheet" href="<%=contextPath %>/core/funcs/news/imgNews/css/website.css" type="text/css" media="screen"/>
  <script type="text/javascript" src="<%=contextPath %>/core/js/jquery.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/funcs/news/imgNews/js/jquery.tinycarousel.min.js">	
	</script>
	<script type="text/javascript">

    var imgNewPath = contextPath + "/yh/core/funcs/office/ntko/act/YHNtkoAct/upload.act?";   
		jQuery(document).ready(function(){
		  findNewsAndPic();
	    jQuery('#slider-code').tinycarousel({ axis: 'y', pager: true, interval:true, display: 1,intervaltime:2000, beginFirst:true});
		});
	

		function findNewsAndPic(){
			jQuery.ajax({
					url:contextPath + "/yh/core/funcs/news/act/YHImgNewsAct/findNewsAndPicturesAjax.act" ,
				  type:"post",	
					async:false,                                 
					success:function(data){				    
			 		  var toJson = eval( "(" + data + ")" );
			 		  if(toJson){
	            setPicture(toJson);
				 		}
					}
			  });
		} 

		/**
		*  添加图片和分页按钮
		*/
		function setPicture(toJson){
			var obj = toJson.rtData;
			var len = obj.length;			
			var pic = "";
			var pageNo= "";
			var pno = 0;
			var title = "";
      for(var i=0; i<len; i++){
        var img = obj[i].imgPaths;
        var imgLen = img.length;
        if(imgLen > 0 ){          
          var imgpath = imgNewPath + img[0].path;    
	          pic += "<li>";
	          pic += "<img width='236' height='121' src=\""+ imgpath +"\">";
	          pic += "</li>";
            pageNo += "<li><a rel=\"" + (pno++) + "\" class=\"pagenum\" href=\"#\">"+ pno +"</a></li>";        
        }
        if(i <5){
	        title += "<li>";
	          title += "<a href=\"#\">" + obj[i].subject +"</a>";
	        title += "</li>";
        }
      }
     jQuery("#overview").html(pic); 
     jQuery("#pager").html(pageNo);
     jQuery("#subjects").html(title);
		}
	</script>	
		
</head>
<body>
<table>
<tr>
<td>
<div id="slider-code">   
    <div class="viewport">
        <ul id="overview" class="overview"></ul>		
    </div>    
    <div >
       <ul id="pager" class="pager">        
    </ul>
	</div>
</div>
</td>
<td>
  <div id="news">
    <ul id="subjects"></ul>
  </div>
</td>
</tr>
</table>
</body>
</html>

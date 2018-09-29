<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"  isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>图片新闻</title>
	<link rel="stylesheet" href="<%=contextPath %>/core/funcs/news/imgNews/css/website.css" type="text/css" media="screen"/>
  <script type="text/javascript" src="<%=contextPath %>/core/js/jquery.js"></script>	
  <script type="text/javascript">
    function subAStr(str){
      var cont = 18;
      if(str.length <= cont){
        return str;
      }
      return str.substring(0, cont) + "....";
    }
    function openImageWindow(id) {
      window.open("imageWindow.jsp?id=" + id  , "imgWindow");
    }
  </script>
</head>
<body>
 <table>
 <tr>
  <td>
  <div id="slider-code">  
 	<script type="text/javascript">
	
 	parent.resizePanel("图片新闻", function(width) {
 	 	if(document.getElementById("objId")){
 	 	  document.getElementById("objId").width = width*0.50; 	 	 
 	 	}else    
      document.getElementById("emId").width = width*0.50;
 	});
 	var imgNewPath = contextPath + "/getFile?uploadFileNameServer=";   
 	var focus_width=250;
	var focus_height=150;
	var text_height=17;
	var swf_height = focus_height+text_height;
	var pics = "";
	var links='';
	var texts='';
  var title = "";
	jQuery.ajax({
		url:contextPath + "/yh/core/funcs/news/act/YHImgNewsAct/findNewsAndPicturesPathAjax.act" ,
	  type:"get",	
		async:false,                                 
		success:function(data){				    
 		  var toJson = eval( "(" + data + ")" );
 		  if(toJson){
 		   var obj = toJson.rtData;
 			 var len = obj.length;			
 		   var pics = "";
 		   var texts = "";	
 		   if(len > 5 ){      //最多显示5个图片
          len =5;
 	 		  }
	 		 
       for(var i=0; i<len; i++){
         var img = obj[i].imgPaths;         
         var imgLen = img.length;        
         if(imgLen > 0 ){ 
            pics  += imgNewPath + img[0].path +"|" ;    
            texts += subAStr(obj[i].subject) +"|" ;   
            links += "imageWindow.jsp?id=" + obj[i].seqId + "|";         
         }
         if(i <5){
 	        title += "<li>";
 	          title += "<a href=\"javascript:openImageWindow("+obj[i].seqId+")\" title=\""+ obj[i].subject +"\">" + obj[i].subject +"</a>";
 	        title += "</li>";
         }         
       }
      pics = pics.substring(0, pics.lastIndexOf("|")); 
      texts = texts.substring(0, pics.lastIndexOf("|")); 
      links = links.substring(0, pics.lastIndexOf("|"));
      document.write('<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000"'+' id="objId"'
			      +'codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0"'
			      +' width="'+ focus_width +'" height="'+ swf_height +'">');
			
			document.write('<param name="allowScriptAccess" value="sameDomain">'
			      +'<param name="movie" value="Flash.swf">'
			+'<param name="quality" value="high">'
			+'<param name="bgcolor" value="#aba087f">');
			document.write('<param name="menu" value="false"><param name=wmode value="transparent">');
			
			document.write('<param name="FlashVars"'
			      +'value="pics='+pics
			      +'&links='+links
						+'&texts='+texts
						+'&borderwidth='+focus_width
						+'&borderheight='+focus_height
						+'&textheight='+text_height+'">');
			
			document.write('<embed src="Flash.swf" wmode="transparent"' + ' id="emId"'
			      +'FlashVars="pics='+pics
						+'&links='+links
						+'&texts='+texts
						+'&borderwidth='+focus_width
						+'&borderheight='+focus_height
						+'&textheight='+text_height
						+'" menu="false" bgcolor="#ffffff" quality="high" width="'+ focus_width 
						+'" height="'+ focus_height +'" allowScriptAccess="sameDomain" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer">');
			document.write('</object>');        
	 		}
	 		//$("#subjects").html(title);
		}	
  });
	</script>	
	</div>
	</td>
	<td>
		<div id="news">
	    <ul id="subjects" style="margin-left: 5pt;">
	      <script type="text/javascript">
	       document.write(title);
	      </script>
	    </ul>
	  </div>
  </td>
	</tr>
	</table>
</body>
</html>

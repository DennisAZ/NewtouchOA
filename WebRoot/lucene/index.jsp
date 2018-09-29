<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<link rel="stylesheet" type="text/css" href="css/index.css" />
<script type="text/javascript" src="/yh/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<title>站内全文检索</title>
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}

-->
</style></head>
<script type="text/javascript" >


  function   keydown()   {
	if   (window.event.keyCode   ==   "13" && document.getElementById("search_content").value!=""){   
	       search();
	      }
	} 
  function search(){
	  
      var content= document.getElementById("search_content").value;
     location.href="lucene.jsp?content="+encodeURI(content);
	  } 

  function search_on_key_up(){//动态提供词汇建议
	  var text="";
	  var content= document.getElementById("search_content").value;
	  content= content.trim();
	  if(content!=""){
	     var urls =contextPath+"/yh/lucene/act/YHPersonServiceAct/getLuceneLogAct.act";
	      var rtJsons = getJsonRs(urls,"content="+content);
	        if(rtJsons.rtState == '0'){
              var dataList=rtJsons.rtData.data;
               for(var i=0;i<dataList.length;i++){
                    var log_str=dataList[i].content;
                     text+="<li onclick=\"getLiStr('"+log_str+"')\">"+log_str+"</li>";
               }
            text="<ul>"+text+"</ul>";
			if(dataList.length=0){
			  text="";
			 }
	        }
	        
	    }
	  $("show_log").innerHTML=text;

	  }

  var is_on_list=false;
  function getLiStr(str){  //选择建议词汇
	  $("search_content").focus();
	  document.getElementById("search_content").value=str;
	 
	  }
  function search_lose_foccuz(){//失去光标
	  if(is_on_list){
			$("show_log").innerHTML="";
		  }
   }
  function div_mouse_over(){
     is_on_list=false;
  }
   function div_mouse_out(){
     is_on_list=true;
  }


   function getHotnews(){
       var text="";
       var urls =contextPath+"/yh/lucene/act/YHPersonServiceAct/getHotNews.act";
        var rtJsons = getJsonRs(urls);
          if(rtJsons.rtState == '0'){
            var data=rtJsons.rtData.data;
            for(var i=0;i<data.length;i++){
               if(i==0){
                 text+="<li class=\"hots_first\"><a target='_blank' href=\""+data[i].url+"\">"+(i+1)+"."+data[i].content+"</a></li>";
               }else{
                 text+="<li ><a target='_blank' href=\""+data[i].url+"\">"+(i+1)+"."+data[i].content+"</a></li>";
                }
              }
 
            $("hot_news").innerHTML=text;
          }
      }
   
</script>


<body onkeydown=" keydown();" onload="getHotnews();">
<div align="center" id="container">
  <div id="container_back">
  <div id="user_info"><a href="#">admin</a>|<a href="#">个人日志</a>|<a href="#">搜索日志</a></div>
      <div id="yh_logo"></div>
	  <div id="input_div">
		  <div id="input_radio"><input onkeyup="search_on_key_up();"   onblur="search_lose_foccuz();" id="search_content" name="search_content" /></div> 
		  <div onclick="search();" id="input_button"></div>
		  <div id="show_log" onmouseover="div_mouse_over();" onmouseout="div_mouse_out();"></div>

	  </div>
     <div id="content_rss">
	  <span >信息订阅</span>
	  <ul>
	  <li class="rss_first"><a href="#">最新通知</a></li><li><a href="#">绩效考核</a></li><li><a href="#">社保</a></li>
	  </ul>
	  <div class="clear"></div>
    </div>
   <div id="content_hots">
	  <span>实时热点</span>
	  <ul id="hot_news">

	  </ul>
	    <div class="clear"></div>
    </div>
	<div id="links">
	  <span>热门连接：</span>
	  <a href="#" target="_blank"><%=StaticData.SOFTCOMPANY%></a>
	   <a href="#"><%=StaticData.SOFTCOMPANY%></a>
	    <a href="#" target="_blank"><%=StaticData.SOFTCOMPANY%>[OA论坛]</a>
     <div class="clear"></div>
	</div>
	
	 </div>
  </div>



</div>



</body>
</html>

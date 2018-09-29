<%@ page language="java" contentType="text/html; charset=utf-8"  pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String content=request.getParameter("content");
if(null==content){
  content="";
}

String stationId=request.getParameter("stationId");
if(null==stationId){
  stationId="";
}

String str=request.getParameter("str");

%>
<title>站内全文检索</title>
<link rel="stylesheet" type="text/css" href="css/lucene.css" />
<script type="text/javascript" src="/yh/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="/yh/core/js/jquery/yh/core/js/jquery/jquery.min1.6.2.js"></script>
<script type="text/javascript" src="/yh/core/js/jquery/jquery.blockUI.js"></script>
<script type="text/javascript" src="/yh/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/lucene/js/ps1.js"></script>
<script type="text/javascript" ><!--
var stationId="<%=stationId%>";
 function doInit(){
  // modulePriv("GXFW"); //判断模块权限
   
    //  setDays();//大事件
   $("search_content").value="<%=content%>";

     addluLog();//添加搜索日志
     search(0,10);
  // sysLog("200E1001",$("content").value);
     getRecoment();//获取推荐词汇
   }

 function str(){
   if($("search_content").value=='请输入您要查询的内容'){
     $("search_content").value="";
   }
   }

 function addluLog(){
     var content= document.getElementById("search_content").value;
     var urls =contextPath+"/yh/lucene/act/YHPersonServiceAct/addLuceneLogAct.act";
      var rtJsons = getJsonRs(urls,"content="+content);
        if(rtJsons.rtState != '0'){
          alert("日志添加出错！");
        }
       
    }

/**
 * 推荐词汇
 */

 function getRecoment(){//
     var text="";
     var content="<%=content%>";
     content= content.trim();
     if(content!=""){
        var urls =contextPath+"/yh/lucene/act/YHPersonServiceAct/getRecomendKeyAct.act";
         var rtJsons = getJsonRs(urls,"content="+content);
           if(rtJsons.rtState == '0'){
               var dataList=rtJsons.rtData.data;
                for(var i=0;i<dataList.length;i++){
                     var log_str=dataList[i].content;
                      text+="<span><a href='lucene.jsp?content="+encodeURI(log_str)+"' target='_parent'>"+log_str+"</a></span>";
                }
           
           }
           
       }
     $("tj-span").innerHTML=text;

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

	  function getLiStr(str){  //选择建议词汇
	    document.getElementById("search_content").value=str;
	    }
	  function search_lose_foccuz(){//失去光标
	          $("show_log").innerHTML="";
	    }
	 
--></script>


<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /><style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-top: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style></head>

<body onLoad="doInit()">
<div align="center" id="container">
<div align="center" id="container_back">
<div id="head">
  <div id="user_info"><a href="#">admin</a>|<a href="#">个人日志</a>|<a href="#">搜索日志</a></div>
  <div id="top">
      <div id="input_div">
	     <div id="input_radio"><input id="search_content" onkeyup="search_on_key_up();" onblur="search_lose_foccuz()" name="search_content" /></div> 
	  	 <div onclick="search(0,10);" id="input_button"></div>
		   <div id="show_log"></div>
	  </div>
  </div>
 
</div>
<div id="service">
  <div id="search_result">
      总共搜索到<font id="search_num"></font>个结果
  </div>
  <div id="result">

 </div>
  <div id="quanx"  align="left">
     <input type="checkbox" onClick="selectAll();" name="all" id="all" title="全选"></input> <span id="selectAll">全 选</span> 
	   <input type="button" onclick="jionresult()" name="button" id="merge" value="查找结果合并"></input>
	   <div class="clear"></div>
  </div>
     <div id="tuijian">
     <div id="tj-title">相关推荐:</div>
     <div id="tj-span">
    
             
     </div>
     </div>
  <div  id="page">
 
  </div>
   
 
</div>
<div id="foot">
    
</div>
</div>
</div>
<form action="" name="form1" id="form1" method="post">
<input type="hidden" name="str" id="str" value=""></input>
</form>
</body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title></title>
    <link rel="stylesheet" href = "<%=cssPath%>/cmp/AssistInput.css">
     <link rel="stylesheet" href ="<%=cssPath%>/style.css">
	<script type="text/javascript" src="<%=contextPath %>/raw/lh/fckeditor/fckeditor.js"></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
	<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
   <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/AssistInput1.0.js"></script>
    <script type="text/javascript">
	  var url = contextPath + '/raw/lh/assistinput/YHAssistAct/assistInput.act?str=';
	  function doInit(){
        new AssistInuput({bindToId:'test1'
             ,requestUrl:url
             ,showLength:5
			 ,func:test
		 });
        new AssistInuput({bindToId:'test2'
          ,requestUrl:url
          ,showLength:8
		 //,func:test
	 });
       }
      function test(event,id){
		alert("event："+ event + ",input value:" + id);
      }
    </script>
  </head>
  
  <body onload="doInit()">
  <div id="di" style="width:800px">
   
   please input:<input type="text" id="test1" value="" >please input:<input type="text" id="test2" value="" >
   
  <br>

  </div>
 
  <div id="di2">
  
  </div>
  </body>
</html>
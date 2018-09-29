<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% 
 String Gid= request.getParameter("Gid"); 

%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=contextPath%>/core/frame/ispirit/n12/org/style/smsbox.css">

<script type="text/javascript" src="<%=contextPath%>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath%>/core/js/jquery/yh/core/js/jquery/jquery.min1.6.2.js"></script>

<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Menu.js" ></script>
<html xmlns="http://www.w3.org/1999/xhtml">
<style type="text/css">
<!--
body {
	margin-left: 0px;
	margin-right: 0px;
	margin-bottom: 0px;
}
-->
</style><head>
<script type="text/Javascript">
   function  doInit(){
	     $("MSG_GROUP_ID").value="<%=Gid%>";
	   
	   var filterStr= $("search_cont").value;
	   var Gid =$("MSG_GROUP_ID").value;
	   var start =$("start_page").value;
	   
	   getGroupMsgHistory( filterStr,Gid );
	 
	  
	
   }
   
   function scroll(){
	   window.scrollTo(0,document.body.scrollHeight);
   }

   function getGroupMsgHistory( filterStr,Gid ){//获取历史数据，查询及分页功能
	  // alert(filterStr+":"+Gid);
	   var url = "<%=contextPath%>/yh/core/funcs/system/ispirit/n12/group/act/YHImGroupAct/getGroupMsgHistory.act";
	   var start=$("start_page").value;
	   $("page").value=start;
	    var param="filterStr="+filterStr+"&Gid="+Gid+"&start="+start;
	   var rtJson = getJsonRs(url,param);
	    if (rtJson.rtState == "0") {
        var hContent= rtJson.rtData.data;
        
        //设置翻页
        $("pageCount").innerHTML=rtJson.rtData.pageCount;
        $("pageCountNum").value=rtJson.rtData.pageCount;
        var divStr="";
        var i=hContent.length-1;
        jQuery("#show_history_content").html("");
        for(;i>=0;i--){  
         appendMsg(hContent[i].msgUserName, hContent[i].msgTime, hContent[i].msgContent);	
        }
      //  $("show_history_content").innerHTML=divStr;
	    }
	    window.scrollTo(0, document.body.scrollHeight);
	   
   }

   
   function nextPage(){
	   var pageCount = $("pageCountNum").value;
	   var start=$("start_page").value;
	   pageCount=parseInt(pageCount);
	   start=parseInt(start);
	   start=start+1;
	   if(start>pageCount){
		   start =pageCount;
	   }
	   $("start_page").value=start;
	   doInit();
   }
   
   function prevPage(){
	     var pageCount = $("pageCountNum").value;
	     var start=$("start_page").value;
	     pageCount=parseInt(pageCount);
	     start=parseInt(start);
	     start=start-1;
	     if(start<=0){
	    	 start=1;
	     }
	     $("start_page").value="1";
	     doInit();
   }
   function jumpPage(){
	   var pageCount = $("pageCountNum").value;
     pageCount=parseInt(pageCount);
	   var page = $("page").value;
	   page=parseInt(page);
	   if(page>pageCount || page<=0){
		   return ;
	   }
	   $("start_page").value=page;
	     doInit();  
   }
   function firstPage(){
	   $("start_page").value="1";
	     doInit();
   }
   function lastPage(){
	   var pageCount = $("pageCountNum").value;
	   
	   $("start_page").value=pageCount;
	     doInit();
   }
   
   
</script>

</head>
<body style=" background-color:#FFFFCC; " onLoad="doInit();">



<div id="show_history_content">
 
</div>
<div style="padding-bottom:10px;"></div>


<br>


</body>
<div id="control_div" >
<div id="topNav" style="display:none">
<form action="" name="form1" method="get">
 <span>当前群记录</span>&nbsp;&nbsp;&nbsp;<span>查询：</span><input type="text" id="se" name="se" class="SmallInput" value="" />
  <input type="submit" value="搜索" class="SmallButton" onClick="doInit();" />
  <input type="hidden" id="MSG_GROUP_ID" name="MSG_GROUP_ID" value=""  />
  <input type="hidden" id="start_page" name="start_page" value="1" />
  <input type="hidden" id="pageCountNum" name="pageCountNum" value="1" />
</form>
</div>
<div id="page_bar"><span id='page_buttton'><input type='text' name='search_cont' id='search_cont' ><a href='javascript:doInit()'><img title='查询' width='16px' height='16px' src='images/infofind.gif'/></a></span><div id='page_icon'><span title='首页' id='page_first'><a href='javascript:firstPage()'><img src='images/first.gif'/></a></span><span  title='上一页' id='page_latist'><a href='javascript:prevPage();'><img src='images/prev.gif'/></a></span><span id='present_page'><a href='#'><input size='2' id='page'/>/<font id='pageCount'></font></a></span><span id='page_next'  title='下一页'><a href='javascript:nextPage()'><img src='images/next.gif'/></a></span><span id='page_last'  title='下一页'><a href='javascript:lastPage();'><img src='images/last.gif'/></a></span><span id='submit'  title='跳转'><a href='javascript:jumpPage()'><img src='images/nowait.gif'/></a></span></div></div>

</div>
<a name="bottom"></a>
<br><a id="bottom1" href="#bottom"></a> <br>
</html>
<script>
/* var obj_a = document.getElementById("bottom1");
if(document.all) //for IE
   obj_a.click();
else if(document.createEvent){ //for FF
   var ev = document.createEvent('HTMLEvents');
   ev.initEvent('click', false, true);
   obj_a.dispatchEvent(ev);
} */
//document.getElementByTagName( 'body').scrollTop=document.getElementTagName ( 'body').scrollHeight ;



function appendMsg(user, time, content) {
	  var ctn = jQuery("<div></div>");
	  var ut = jQuery("<div class='user_time'></div>")
	  var ct = jQuery("<div></div>");
	  ct.html(content);
	  ut.append("<span>" + user + "</span>&nbsp;&nbsp;<span>" + time + "</span>");
	  ctn.append(ut).append(ct);

	  jQuery("#show_history_content").append(ctn);
	 
	}

//setInterval("doInit();",2000);
</script>




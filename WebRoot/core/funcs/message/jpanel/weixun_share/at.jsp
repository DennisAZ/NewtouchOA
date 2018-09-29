<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml10-transitional.dtd">
<html lang="zh-cn" xml:lang="zh-cn" xmlns="http://www.w3.org/1999/xhtml">
<head>

<% 

  String seqId=request.getParameter("userId");

%>

<title>我的微讯</title><link rel="stylesheet" type="text/css" href="styles/weixunshare.css" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/message/js/messageutil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/message/jpanel/weixun_share/inc/js/config.js"></script>
<script type="text/javascript">
var page = 0;
var seq_id =<%=seqId%>;
var pageNo = "0";
var pageSize = "5";
var totlePage = "0";
var total=0;
var num=0;

function init(){
    var url = "<%=contextPath%>/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/getWeiXunMention.act?pageNo=" + pageNo  + "&pageSize=" + pageSize+"&seqId="+seq_id;
    var promStr="";
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "0"){
    totlePage = (rtJson.rtData.totalRecord%pageSize == 0)?Math.floor(rtJson.rtData.totalRecord/pageSize) - 1 : Math.floor(rtJson.rtData.totalRecord/pageSize);
    total=rtJson.rtData.totalRecord;
    for(var i = 0; i < rtJson.rtData.pageData.length; i++){
      var obj = rtJson.rtData.pageData[i];
      promStr += toPanelInBoxStr1(obj);
      num=num+1;
    
    }
    var sss=  $("talkList").innerHTML;
  
    promStr=sss+promStr;
    setNum();
    $("talkList").innerHTML=promStr;

    }
}

function setNum(){
	$("num").innerHTML=num;
	if(num>4){
		$("moreList").style.display="block";
	}else{
		  $("moreList").style.display="none";
	}
}

function get_more(){
    if(num==total){
      alert("数据已经全部显示");
      return; 
    }
    pageNo=parseInt(pageNo)+1;
    pageSize=parseInt(pageSize);
    //if(pageNo*pageSize
    init();
  }
  
  
//获取当前人员信息
function getUserInfo(){
   var person = getPersonInfo("");
   $("user_name").innerHTML=person.userName;
   $("userImg").src=contextPath+"/yh/core/funcs/message/weixun_share/act/YHWeiXunShareAct/getPersonAvator.act?seqId="+person.userId;
   $("user_dept").innerHTML=" 部门: "+person.deptName;
   
}

function doInit(){

  // getUserInfo();
     init();
     BroadCastBlock();
}

</script>
</head>
<body onload="doInit();">

   <div class="main sub_main">
      <div class="clearfix"></div>
      
      <div class="talkBox" id="talkBox">
         <h2 class="hrline"><a  class="fr" href="index.jsp">返回</a><em><label for="msgTxt"></label></em></h2>
            <div id="theader">
               <span class="topic_title">@提到我的</span>
               <span class="topic_stat">共<span id='num'>0</span>条微讯</span>
            </div>
         <span class="bg"></span>   
      </div>
      
   
         <div id="no_result" style="display:none" class="dC"><span class="SMessage">暂无记录</span></div>
     
      
      <div class="AL">
         
         <ul id="talkList" class="LC">
            
     
         </ul>
         <div class="clearfix"></div>
      </div>
      
    
      <div class="moreFoot" id="moreList">
         <a href="javascript:get_more()" hidefocus="hidefocus">
            <em class="ico_load"></em>更多<em></em>
         </a>
      </div>
      <?
         }
      ?>      
   </div>
<div id="overlay"></div>
<div id="BroadCastBlock" class="dialogBlock module" style="display: none;width:400px;">
  <h4 class="moduleHeader"><span class="title">分享这条微讯</span><a class="option" href="javascript:;"><img src="<%=imgPath%>/close.png"/></a></h4>
  <div class="module_body" style="height:270px;">
   <iframe id="wxiframe" src="" frameborder="0" width="100%" height="100%" scrolling="no"></iframe>
  </div>
</div>
</body>
</html>
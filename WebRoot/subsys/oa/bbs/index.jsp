<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<title></title>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>

<script type="text/javascript">

function doInit(){
	  var requestURL = "<%=contextPath%>/yh/subsys/oa/bbs/act/YHBbsAct/getLoginInfo.act";
	  var rtJson = getJsonRs(requestURL); 
	  if(rtJson.rtState == "0"){
		  var data = rtJson.rtData;
      if(data.role=="1"){
        $("set").style.display="block";
          }
      if(data.url==""){
         $("show").style.display="block";
          }else{
                 $("url").value=data.url;
                 $("userId").value=data.userId;
                 $("userName").value=data.userName;
                 $("email").value=data.email;
                 $("birth").value=data.birth;
                 if(data.role!="1"){
                  goToDiscuz();
                  }
         }
	    }
}

function goToDiscuz(){
	var url=$("url").value;
	if(url==""){
	   alert("Discuz没有设置URL,请先设置URL!");
	   return;
		}
	var userId=$("userId").value;
  var userName=$("userName").value;
	var email=$("email").value;
	var birth=$("birth").value;
	 var url=url+"?LOGIN_USER_ID="+userId+"&EMAIL="+email+"&BIRTH="+birth+"&USERNAME="+userName;

		     window.location.href=url;
}

function saveBbsUrl(){
	  var url=$("url").value;
	 
	  var requestURL = "<%=contextPath%>/yh/subsys/oa/bbs/act/YHBbsAct/saveBbsUrlAct.act";
    var rtJson = getJsonRs(requestURL,"url="+url); 
    if(rtJson.rtState == "0"){
          alert("URL保存成功");
          window.location.reload();
        }

	  
	}



</script>
</head>
<body class="bodycolor" topmargin="5" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/meeting.gif" width="17" height="17"><span class="big3">Discuz论坛</span><br>
    </td>
  </tr>
</table>
<br>
<table style="display:none" class="TableBlock" name="set" id="set" width="90%" align="center">
  <tr>
    <td nowrap colspan="2" align="left" class="TableContent">管理可设置集成的Discuz的URL，例如：http://localhost:88/general/bbs/index.php</td>

  </tr>
  <tr>
    <td nowrap align="left" class="TableContent">集成DiscuzURL:</td>
    <td nowrap align="left" class="TableContent" >
    <input type="text" class="BigInput" size="50" name="url" id="url" value=""></input>
     <input type="hidden"   name="userId" id="userId" value=""></input>
     <input type="hidden"   name="userName" id="userName" value=""></input>
     <input type="hidden"  name="email" id="email" value=""></input>
     <input type="hidden"  name="birth" id="birth" value=""></input>
    
    </td>

  </tr>

  <tr align="center" class="TableControl">
    <td colspan="4">
      <input type="button" value="保存" class="BigButton" onClick="saveBbsUrl()" title="保存设置">
       <input type="button" value="进入论坛" class="BigButton" onClick="goToDiscuz();" title="进入论坛">
      
    </td>
  </tr>
</table>


<div align="center" style="display:none" id="show">
  <table class="MessageBox" align="center" width="240">
  <tr>
  <td class="msg info"><div class='content' style='font-size:12pt'>系统还没有集成Discuz，请安装Discuz后，以管理员身份设置Discuz的URL。详情请咨询YH技术支持！</div></td>
  </tr>
  </table>
           
</div>



</body>
</html>
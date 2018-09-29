<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp"%>
<%@ page import="com.bjca.sso.processor.*,com.bjca.sso.bean.*,java.util.*,java.net.*,com.bjca.security.*"%>
<%@ page import="java.util.*"%>
<%
String ieTitle = (String)request.getAttribute("ieTitle");

if (ieTitle == null || ieTitle.length() < 1) {
  ieTitle = StaticData.SOFTTITLE;
}

Integer randomInt = (Integer)request.getSession().getAttribute("RANDOM_NUMBER");

int randomNum = 123456;
if (randomInt != null) {
  randomNum = randomInt;
}

%>
<%
	//服务器证书
    String userid = ""; 
	String BJCA_SERVER_CERT = request.getParameter("BJCA_SERVER_CERT");
	//票据
	String BJCA_TICKET =request.getParameter("BJCA_TICKET");
	//票据
	String BJCA_TICKET_TYPE = request.getParameter("BJCA_TICKET_TYPE");
	//打印信息：

 
	TicketManager ticketmag = new TicketManager();
	//验证签名及解密

    UserTicket userticket = ticketmag.getTicket(BJCA_TICKET,
        BJCA_TICKET_TYPE, BJCA_SERVER_CERT);
  
	//处理票据信息
	if (userticket != null) {
		//用户姓名 
		String username1 = userticket.getUserName();//这个是由 bjca 配置的，也许该值为空。


		String username = new String(username1.getBytes("ISO-8859-1"));
		//用户32位唯一标识码


        userid = userticket.getUserUniqueID();
        session.setAttribute("UUMS_USERINFORMATION",userid);
	} else {
		//request.getRequestDispatcher("error.jsp").forward(request,
	//			response);//这里是临时的错误页面，可以修改错误页面


	}
	userid = "48191f881c10ec0c1246eb08db503b3d";
%>
<html>
<head>
<title><%=ieTitle %></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<meta name="author" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="keywords" content="<%=StaticData.SOFTKEYWORD%>" />
<meta name="description" content="<%=StaticData.SOFTKEYWORD%>" />
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/javascript">

function getUserNameByCa(){
  var url = contextPath + "/yh/subsys/portal/bjca/act/YHBjcaSSOAct/getUserNameByCa.act?userId=<%=userid%>";
  var json = getJsonRs(url);
  if(json.rtState == "0"){
    return json.rtData;
  }else{
   return "";
  }
}
function loginByCa(){
  
  var userName = getUserNameByCa();
  var url = contextPath + "/yh/core/funcs/system/act/YHSystemAct/doLoginIn.act?CA_USER=1&userName=" + encodeURIComponent(userName) ;
  try {
    var json = getJsonRs(url);
    loginComplete(json);
  } catch (e){
    alert('服务器连接中断');
  }
}

function loginComplete(json) {
  if (json.rtState == "0"){
    //记录上次成功登陆的用户名
    switch (json.rtData.menuType) {
    case '1':
      window.location.href = contextPath + "/core/frame/2/index.jsp";
      break;
    case '2':
      window.open(contextPath + "/core/frame/2/index.jsp", "_blank","top=0,left=0,toolbar=yes," +
           "location=yes, directories=no, status=no, scrollbars=yes," +
           "resizable=yes, copyhistory=no, width=" + window.screen.width + "," + 
           "height=" + window.screen.height);
       window.opener=null;
       window.open("","_self");
       window.close();
      return;
    case '3': 
      window.open(contextPath + "/core/frame/2/index.jsp", "_blank","top=0,left=0,toolbar=no," +
           "location=no, directories=no, status=no, menubar=no, scrollbars=yes," +
           "resizable=yes, copyhistory=no, width=" + window.screen.width + "," +
           "height=" + window.screen.height);
      window.opener=null;
      window.open("","_self");
      window.close();
      return;
    default:
      window.location.href = contextPath + "/core/frame/2/index.jsp";
    }
  } else{
    switch(json.rtData.code){
      case 0:{
        alert(json.rtMsrg);
        break;
      }
      case 1:{
        
      }
      case 2:{
      }
      case 3:{
        alert(json.rtMsrg);
        break;
      }
      case 4:{
        alert(json.rtMsrg);
        break;
      }
      case 5:{
        alert(json.rtMsrg);
        break;
      }
      case 6:{
        window.location = contextPath + "/core/frame/pass.jsp";
        break;
      }
      case 7:{
        window.location = contextPath + "/core/frame/pass.jsp";
        break;
      }
      case 8:{
        alert('用户名或密码错误超过 ' + json.rtData.msg.times + ' 次，请等待' + json.rtData.msg.minutes + '分钟后重试!');
        break;
      }
      case 9:{
        alert(json.rtMsrg);
        break;
      }
      case 10:{
        alert("ca登录错误:" + json.rtMsrg);
        break;
      }
      default:{
        alert("登录失败!");
      }
    }
  }
}
//登录

</script>
</head>
<body onload="loginByCa()">
</body>
</html>

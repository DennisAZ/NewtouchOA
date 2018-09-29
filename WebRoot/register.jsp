<%@ page language="java"  pageEncoding="utf-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>CMS用户注册</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<link rel="stylesheet" type="text/css" href="<%=contextPath%>/core/styles/style1/css/index1.css"/>
	<script type="text/javascript" src="core/js/jquery.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
    <script  type="text/Javascript">
    function regist(){
     var duty_type="1";
     var dept_id=<%=YHSysProps.getString("DEPT_ID")%>;
     var POST_PRIV="0";
     var USER_PRIV="323";
     var user_id=jQuery('#user_id').val();
     if (checkUserID(user_id)){
         alert('该用户已经注册！！！')
         return false;
     }
     var userName=jQuery('#userName').val();
     var pwd1=jQuery('#pwd1').val();
     var pwd2=jQuery('#pwd2').val();
     if (pwd1 != pwd2){
       alert('两次密码输入不一样，请重新输入!!')
       return false;
     }
     
     var url=contextPath + "/yh/core/funcs/person/act/YHPersonAct/regist.act"
     var rtJson = getJsonRs(url, "user_id=" + user_id+"&duty_type="+duty_type+"&dept_id="+dept_id+"&POST_PRIV="+POST_PRIV+"&password="+pwd1+"&username="+userName+"&USER_PRIV="+USER_PRIV);
     if (rtJson.rtMsrg=='1'){
      alert('注册成功');
      var url = contextPath + ("/yhindex.jsp" || "/core/frame/webos/index.jsp");
      location.href = "/yh/yhindex.jsp";
 }
    }
    //账号验证，账号是否被注册
    function checkUserID(userId){
    var url = contextPath + "/yh/core/funcs/person/act/YHPersonAct/selectPerson.act";
      var rtJson = getJsonRs(url, "userId=" + userId);
      if (rtJson.rtState == "0") {
        if (rtJson.rtData[0].num == 1) {
          return true;
        } else if (rtJson.rtData[0].num == 0) {
          return false;
        }
      }
    }
    </script>
  </head>
  
  <body>
   <form method="post" id='loginForm'>
	<div class="loginbox">
	  <div class="loginlogo"><img src="<%=contextPath%>/core/styles/style1/img/systop/logo.jpg" /></div>
	  <div class="loginbg">
		<div class="loginmain" style="width:500px">
		  <div class="loginright" style="width:400px;text-align: center;">
			<h1>用户注册/ REGISTER</h1>
			<ul>
			<li>  账号：<input type="text" name="user_id" id="user_id"/></li>
            <li>  姓名：<input type="text" name="userName" id="userName"/></li>
            <li>  密码：<input type="password" name="pwd1" id="pwd1"/></li>
            <li style="margin-left:-23px">  确认密码：<input type="password" name="pwd2" id="pwd2"/></li>
            <li style="margin-left:70px"> <input type="reset" value="重置"> <input type="submit" value="注册" onclick="regist();return false;"></li>
			</ul>
		  </div>
		</div>
	  </div>
	</div>
	</form>
  </body>
</html>

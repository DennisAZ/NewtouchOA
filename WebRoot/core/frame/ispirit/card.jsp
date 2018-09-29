<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
  String userId = request.getParameter("UID");
  if (YHUtility.isNullorEmpty(userId)) {
    userId = "";
  }
%>
<html>
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link rel="stylesheet" type="text/css" href="./style/card.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script type="text/javascript">
function queryInfo() {
  var url = contextPath + '/yh/core/funcs/setdescktop/userinfo/act/YHUserinfoAct/queryCardInfo.act';
  $.ajax({
    type: "GET",
    dataType: "text",
    url: url,
    data: {
      userId: '<%=userId%>'
    },
    success: function(text){
      var json = YH.parseJson(text);
      if (json.rtState == '0') {
        var d = json.rtData;
        $('.name').html(d.userName);
        $('.priv').html(d.privName);
        $('#dept').html(d.deptName);
        if (d.mobilNo) {
          $('#mobilNo').html(d.mobilNo);
        }
        else {
          $('#mobil').hide();
        }
        $('#tel').html(d.tel);
        $('#email').html(d.email);
        $('#qq').html(d.qq);

        if (d.photo) {
          var filePathStr = "<%=YHSysProps.getAttachPath().replace('\\','/') + "/" + "hrms_pic"%>" ;
          var imgStr = "<%=contextPath %>/attachment/photo/" + d.photo;
          $('.avatar img').attr("src", imgStr);
        }
        else {
          $('.avatar a').html('<span>暂无头像</span>');
        }

        $('.avatar a').attr('href', contextPath + '/core/funcs/userinfo/person.jsp?windows=1&userId=<%=userId%>');
      }
    }
  });
}
queryInfo();
</script>
<style>

*{padding:0px;margin:0px;}
body{
   font-size:12px;
   padding:5px;
   background:#ebebeb;
}
table{
   font-size:12px;
   border-collapse:collapse;
}
a:link,
a:hover,
a:active,
a:visited,{
   color:#000;
   text-decoration:none;
}
img{
   border:0px;
}
a img{
   border:0px;
}

table.card{
}
table.card td{
   padding:3px;
}
table.card td.left{
   padding-right:0px;
}
table.card td.right{
   padding-left:0px;
}
table.card .avatar{
   float:left;
   padding:2px 5px 0px 3px;
   width:48px !important;
   height:60px !important;
   width:56px;
   height:56px;
   background: url('style/images/avatar_bg.png') left top no-repeat;
   text-align:center;
}
table.card .avatar img{
   width:48px;
   width:48px;
}
table.card .info{
   float:left;
   padding-left:10px;
   margin-top:5px;
}
table.card .info .name{
   font-size:14px;
   font-weight:bold;
   padding-bottom:5px;
}
</style>
</head>
<body>
<table class="card">
   <tr>
      <td colspan="2" valign="top">
         <div class="avatar"><a href="" target="_blank"><img src="/attachment/photo/1.gif"></a></div>
         <div class="info">

            <div class="name"></div>
            <div class="priv"></div>
         </div>
      </td>
   </tr>
   <tr>
      <td class="left" nowrap>部门：</td>

      <td class="right" id="dept"></td>
   </tr>
   <tr id="mobil">
      <td class="left" nowrap>手机：</td>
      <td class="right" id="mobilNo"></td>
   </tr>
   <tr>
      <td class="left" nowrap>电话：</td>

      <td class="right" id="tel"></td>
   </tr>
   <tr>
      <td class="left" nowrap>邮箱：</td>
      <td class="right" id="email"></td>
   </tr>
   <tr>
      <td class="left" nowrap>QQ：</td>

      <td class="right" id="qq"></td>
   </tr>
</table>
</body>
</html>
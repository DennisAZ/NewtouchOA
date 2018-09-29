<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<title>Insert  title  here</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script  type="text/javascript">
var id = '';
$(document).ready(function() {
  var url = contextPath + '/yh/core/funcs/portal/act/YHPortalAct/listAllPortals.act';

  var info = $('#info span');
  $.post(url, function(t) {
    var json = YH.parseJson(t);
    if (json.rtState == '0') {
      json = json.rtData;
		  var list = $("#list");
      $.each(json.records, function(i, e) {
        (i == 0) && changeInfo(i, e);
        var div = $('<div class="portal"></div>');
        var img = $('<div></div>');
        var span = $('<span></span>');
        span.html(e.name);
        list.append(div.append(img).append(span));
        div.click(function() {
          changeInfo(i, e);
        });
      });
    }
    
  });

  function changeInfo(i, e) {
    id = e.id;
    info.html(e.name + '<br>' + e.remark);
  }
});
function save(i, e) {
  var url = contextPath + '/yh/core/funcs/portal/act/YHPortalAct/setDefaultPortal.act';

  $.post(url, {id: id}, function(t) {
    var json = YH.parseJson(t);
    if (json.rtState == '0') {
      parent.location.reload();
    }
  });
}
</script>

<style>
.theme-option {
  text-align: center;
  margin: 10px 10px;
  width: 217px;
  height: 140px;
  float: left;
}

.theme {
  opacity: 0.8;
  width: 217px;
  height: 130px;
  background-positioin: left top;
}

.theme:hover {
  opacity: 1;
}

.portal {
  cursor: pointer;
}

.portal > div {
  width: 117px;
  height: 110px;
  background: url(<%=contextPath%>/core/frame/webos/styles/style1/imgs/portal_0.png);
}

.portal > div:hover {
  width: 117px;
  height: 110px;
  background: url(<%=contextPath%>/core/frame/webos/styles/style1/imgs/portal_0_hover.png);
}

.portal > span {

}

.theme > div{
  float: left;
  width: 217px;
  height: 130px;
  background: url(<%=contextPath%>/core/frame/webos/styles/style1/imgs/theme_select_border.png) left top;
}

.theme-border img {
  height: 100%;
  width: 100%;
}

body {
  text-align: center;
}

#list {
  float: right;
}

#info {
  float: left;
  width: 331px;
  height: 234px;
  background: url(<%=contextPath%>/core/frame/webos/styles/style1/imgs/portal_info_bg.png) left top;
}

#info span {
  position:relative;
  top: 35px;
}

#list > div {
  float: left;
}

#list {
  width: 380px;
}

button {
  width: 84px;
  height: 25px;
  border: none;
}

#save {
  background: url(<%=contextPath%>/core/frame/webos/styles/style1/imgs/portal_save.png) left top;
}

#save:hover {
  background: url(<%=contextPath%>/core/frame/webos/styles/style1/imgs/portal_save_hover.png) left top;
}

#design {
  width: 116px;
  background: url(<%=contextPath%>/core/frame/webos/styles/style1/imgs/design_personal_portal.png) left top;
}

#design:hover {
  background: url(<%=contextPath%>/core/frame/webos/styles/style1/imgs/design_personal_portal_hover.png) left top;
}
</style>
</head>
<body>
<div id="container">
  <div id="info">
    <span></span>
  </div>
  <div id="list">
    
  </div>
  <div style="clear: both;">
  </div>
</div>
<button id="save" onclick="save()"></button>
<button id="design" onclick="parent.open('<%=contextPath %>/core/frame/webos/design/index.jsp?type=design&id=personal')"></button>
</body>
</html>
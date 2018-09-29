<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page isELIgnored="false"%>
<!DOCTYPE  html  PUBLIC  "-//W3C//DTD  HTML  4.01  Transitional//EN"  "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>公交查询</title>
<meta  http-equiv="Content-Type"  content="text/html;  charset=UTF-8">
<link  rel="stylesheet"  href  ="<%=cssPath%>/style.css">
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/datastructs.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/prototype.js"  ></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/smartclient.js"  ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script  type="text/Javascript" >
function submitForm(){
	  var pars = Form.serialize($('form1'));
	  if($('seqId').value == "")
		  var url = "<%=contextPath %>/yh/core/funcs/utilapps/info/bus/act/YHBusAct/addBus.act";
	  else
		  var url = "<%=contextPath %>/yh/core/funcs/utilapps/info/bus/act/YHBusAct/updateBus.act";
	  var json = getJsonRs(url,pars);
	  if(json.rtState == "0"){
	    location = "<%=contextPath%>/core/funcs/utilapps/info/bus/add.jsp?seqId="+$('seqId').value;
	  }
	  else{
		  if($('seqId').value == "")
			  alert("新增失败");
			else
				alert("修改失败");
	  }
}
</script>
</head>

<body class="bodycolor" topmargin="5">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
<c:choose>
  <c:when test="${empty param.seqId}">
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/info.gif" height="22" width="20" align="absmiddle"><span class="big3">  新建线路</span>
  </c:when>
  <c:otherwise>
    <td class="Big"><img src="/yh/core/styles/imgs/menuIcon/info.gif" height="22" width="20" align="absmiddle"><span class="big3">  编辑线路</span>
  </c:otherwise>
</c:choose>
    </td>
  </tr>
</table>
<form action="" method="post" id="form1">
<table class="TableBlock" width="450" align="center">
  <input type="hidden" id="address" name="address" value="${param.city}"/>
  <tr>
    <td nowrap class="TableContent" width="80">城市：</td>
    <td class="TableData" colspan="3">
    <c:choose>
      <c:when test="${param.city == 'BJ'}">
                    北京
      </c:when>
      <c:when test="${param.city == 'CD'}">
                    成都
      </c:when>
      <c:when test="${param.city == 'CQ'}">
                    重庆
      </c:when>
      <c:when test="${param.city == 'GZ'}">
                    广州
      </c:when>
      <c:when test="${param.city == 'HZ'}">
                    杭州
      </c:when>
      <c:when test="${param.city == 'KM'}">
                    昆明
      </c:when>
      <c:when test="${param.city == 'NJ'}">
                    南京
      </c:when>
      <c:when test="${param.city == 'QD'}">
                    青岛
      </c:when>
      <c:when test="${param.city == 'SH'}">
                    南京
      </c:when>
      <c:when test="${param.city == 'SZ'}">
                    深圳
      </c:when>
      <c:when test="${param.city == 'TJ'}">
                    天津
      </c:when>
      <c:when test="${param.city == 'WH'}">
                    武汉
      </c:when>
      <c:when test="${param.city == 'XA'}">
                    西安
      </c:when>
    </c:choose>
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent" width="80">线路：</td>
    <td class="TableData" colspan="3">
      <input type="text" name="lineId" id="lineId" size="8" maxlength="100" class="BigInput" value="${param.lineId }">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent" width="80">首班车时间：</td>
    <td class="TableData" colspan="3">
      <input type="text" name="startTime" id="startTime" size="8" maxlength="100" class="BigInput" value="${param.startTime}">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent" width="80">末班车时间：</td>
    <td class="TableData" colspan="3">
      <input type="text" name="endTime" id="endTime" size="8" maxlength="100" class="BigInput" value="${param.endTime}">
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent" width="80">车型：</td>
    <td class="TableData" colspan="3">
      <input type="text" name="busType" id="busType" size="8" maxlength="100" class="BigInput" value="${param.busType}">(如：普通车，空调车，小区专线，旅游线路)
    </td>
  </tr>
  <tr>
    <td nowrap class="TableContent" width="80">途经站点：</td>
    <td class="TableData" colspan="3">
      <textarea name="passBy" id="passBy" class="BigInput" cols="45" rows="8">${param.passBy}</textarea><br> 注意： 请用英文逗号分隔开，如：车道沟,板井村

    </td>
  </tr>
  <tr class="TableControl">
    <td nowrap colspan="4" align="center">
      <input type="hidden" value="yh.core.funcs.utilapps.info.bus.data.YHBus" name="dtoClass" id="dtoClass">
      <input type="hidden" id="seqId" name="seqId" value="${param.seqId}" >
      <input type="button" value="保存" class="BigButton" onclick="submitForm()">&nbsp;&nbsp;
      <input type="button" class="BigButton" value="返回" onclick="history.back();">
    </td>
  </tr>
</table>
</form>
</body>
</html>
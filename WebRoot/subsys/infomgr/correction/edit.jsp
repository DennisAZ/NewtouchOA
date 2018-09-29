<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增双语标示</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function submitForm(){
  $('form1').submit();
}

function doInit(){
  var queryJson = getJsonRs("<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHCorrectionAct/queryRecord.act?seqId=${param.seqId}");
  if (queryJson.rtState == "0") {
    initSelectState($('type'),queryJson.rtData.type);
    $('picture').value = queryJson.rtData.picture;
    $('content').value = queryJson.rtData.content;
    $('flag').value = queryJson.rtData.flag;
    $('changes').innerHTML = queryJson.rtData.changes;
    $('location').value = queryJson.rtData.location;
    $('seqId').value = queryJson.rtData.seqId;
    $('address').value = queryJson.rtData.address;
    $('correctDate').value = queryJson.rtData.correctDate;
    $('correcter').value = queryJson.rtData.correcter;
    $('workplace').value = queryJson.rtData.workplace;
    $('changes').value = queryJson.rtData.changes;
    $('tel').value = queryJson.rtData.tel;
    $('email').value = queryJson.rtData.email;
    $('image').src = '<%=contextPath%>/bilingual/correction/thumb-' + encodeURIComponent(queryJson.rtData.picture);
  
  }else {
    alert(queryJson.rtMsrg);
  }
}

var initSelectState = function(e,value){
  e.childElements().each(function(e,i){
    if(e.value == value){
      e.selected = true;
    }
  });
}

</script>
</head>
<body onload="doInit()">
<form method="post" name="form1" id="form1" action="<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHCorrectionAct/modifyCorrection.act">
  <table style="width:400px;" class="TableList" align="center" >
    <tr>
      <td nowrap class="TableData" width=100> 标示错误内容：</td>
      <td nowrap class="TableData" width=220>
        <textarea class="BigInput" id="content" name="content"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 标示错误图片：</td>
      <td nowrap class="TableData" width=220>
        <img id="image" src="">       
      </td>
    </tr>
        <tr>
      <td nowrap class="TableData" width=100> 建议更改为：</td>
      <td nowrap class="TableData" width=220>
        <textarea class="BigInput" id="changes" name="changes"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 标示牌类型：</td>
      <td nowrap class="TableData" width=220>
        <select name="type" id="type">
				  <option value="0">英文标识</option>
				  <option value="1">英文菜单</option>
				  <option value="2">组织结构职务职称</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 标示牌所在区县：</td>
      <td nowrap class="TableData" width=220>
        <select name="location" id="location">
         <option value="0">昌平区</option>
          <option value="1">朝阳区</option>
          <option value="2">崇文区</option>
          <option value="3">大兴区</option>
          <option value="4">东城区</option>
          <option value="5">房山区</option>
          <option value="6">丰台区</option>
          <option value="7">海淀区</option>
          <option value="8">怀柔区</option>
          <option value="9">门头沟区</option>
          <option value="10">密云县</option>
          <option value="11">平谷区</option>
          <option value="12">石景山区</option>
          <option value="13">顺义区</option>
          <option value="14">通州区</option>
          <option value="15">西城区</option>
          <option value="16">宣武区</option>
          <option value="17">延庆县</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 标示牌具体位置：</td>
      <td nowrap class="TableData" width=220>
        <input type="text" class="BigInput" size=30  name="address" id="address" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 检查日期：</td>
      <td nowrap class="TableData" width=220>
        <input type="text" id="correctDate" name="correctDate" readonly="true"/>
        <img border="0" align="absMiddle" style="" src="<%=imgPath%>/calendar.gif" id="correctDateImg">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 纠错人姓名：</td>
      <td nowrap class="TableData" width=220>
        <input type="text" class="BigInput" size=30  name="correcter" id="correcter" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 纠错人所在单位/院校：</td>
      <td nowrap class="TableData" width=220>
        <input type="text" class="BigInput" size=30  name="workplace" id="workplace" value="">
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData" width=100> 纠错人联系电话：</td>
      <td nowrap class="TableData" width=220>
        <input type="text" class="BigInput" size=30  name="tel" id="tel" value="">
      </td>
    </tr>
     <tr>
      <td nowrap class="TableData" width=100> Email：</td>
      <td nowrap class="TableData" width=220>
        <input type="text" class="BigInput" size=30  name="email" id="email" value="">
      </td>
    </tr>
    <tr class="TableControl" align="center" >
       <td colspan="2" nowrap >
         <input type="hidden" name="picture" id="picture">
         <input type="hidden" name="seqId" id="seqId">
         <input type="hidden" name="flag" id="flag">
         <input type="button" value="确定" class="BigButton" onclick="submitForm()"/>
       </td>
     </tr>
  </table>
</form>
</body>
</html>
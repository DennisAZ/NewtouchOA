<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>新增双语标示</title>
<link rel="stylesheet" href="<%=cssPath%>/style.css"/>
<link  rel="stylesheet"  href  =  "<%=cssPath%>/cmp/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script  type="text/Javascript"  src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<style>
body{
  background:transparent;
}
</style>
<script type="text/javascript">
function doInit(){
  calendarInit();
}

/**
 * 初始化时间组件
 */
function calendarInit(){
  var beginParameters = {
      inputId:'correctDate',
      property:{isHaveTime:false},
      bindToBtn:'correctDateImg'
  };
  new Calendar(beginParameters);
}

function submitForm(){
  if(!(/\.(jpg|gif|bmp)$/).exec($F('pictureFile').toLowerCase()) && $F('pictureFile')){
    alert('格式有误,请选择jpg/gif/bmp文件上传!');
    $('pictureFile').value = '';
    return;    
  }

  if (!$('changes').value || !$('content')){
    alert('请输入错误内容和您的建议更改内容!');
    return false;
  }

  var date = new Date();
  var y = date.getFullYear();
  var m = date.getMonth() + 1;
  m = (m > 9) ? m : '0' + m;
  var d = date.getDate();
  d = (d > 9) ? d : '0' + d;
  var dateStr = y + '-' + m + '-' + d;
  
  if ($F('correctDate') > dateStr){
    alert('检查日期不能大于今天');
    return;    
  }
  
  $('picture').value = $F('pictureFile');
  $('form1').submit();
}
</script>
</head>
<body onload="doInit()">
<form method="post" name="form1" id="form1" action="<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHCorrectionAct/add4website.act" enctype="multipart/form-data">
  <table style="width:100%;border:none;" class="TableList" align="center" >
    <tr>
      <td nowrap class="TableData" width=100> 标示错误内容：</td>
      <td nowrap class="TableData" width=220>
        <textarea class="BigInput" id="content" name="content"></textarea>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 标示错误图片：</td>
      <td nowrap class="TableData" width=220>
        <input class="BigInput" name="pictureFile" id="pictureFile" size="30" type="file">
        <input type="hidden" name="picture" id="picture">       
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
      <td nowrap class="TableData" width=110>
        <select name="type" id="type">
			  <option value="0">英文标识</option>
			  <option value="1">英文菜单</option>
			  <option value="2">组织结构职务职称</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 标示牌所在区县：</td>
      <td nowrap class="TableData" width=110>
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
        <input type="text" id="correctDate" name="correctDate" readonly/>
        <img border="0" align="absMiddle" style="" src="<%=imgPath%>/calendar.gif" id="correctDateImg"/>
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
    <tr align="center" >
       <td colspan="2" nowrap >
         <input type="button" value="提交" class="BigButton" onclick="submitForm()"/>
       </td>
     </tr>
  </table>
</form>
</body>
</html>
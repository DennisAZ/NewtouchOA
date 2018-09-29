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
  if(!$F('cnName')){
    alert('中文名称不能为空');
    $('cnName').select();
    return;
  }

  if(!$F('enName')){
    alert('英文名称不能为空');
    $('enName').select();
    return;
  }

  if(!(/\.(mp3|wav|wma)$/).exec($F('ATTACHMENT').toLowerCase()) && $F('ATTACHMENT')){
    alert('格式有误,请选择mp3/wav/wma文件上传!');
    $('ATTACHMENT').value = '';
    return;    
  }
  
  $('soundFile').value = $F('ATTACHMENT');
  $('form1').submit();
}

function doInit(){
  var queryJson = getJsonRs("<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHBilingualAct/queryRecord.act?seqId=${param.seqId}");
  if (queryJson.rtState == "0") {
    initSelectState($('type'),queryJson.rtData.type);
    $('enable').value = queryJson.rtData.enable;
    $('cnName').value = queryJson.rtData.cnName;
    $('enName').value = queryJson.rtData.enName;
    $('seqId').value = queryJson.rtData.seqId;
    $('formfile').value = queryJson.rtData.soundFile;
  
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

function checkboxChange(){
  if($('isSoundFile').checked)
    $('soundFileTr').show();
  else
    $('soundFileTr').hide();
}
</script>
</head>
<body onload="doInit()">
<form method="post" name="form1" id="form1" action="<%=contextPath%>/yh/subsys/infomgr/bilingual/act/YHBilingualAct/modifyBilingual.act" enctype="multipart/form-data">
  <table width="400px" class="TableList" align="center" >
    <tr>
      <td nowrap class="TableData" width=100> 类别：</td>
      <td nowrap class="TableData" width=220>
        <select name="type" id="type">
  <option value="0">职务职称</option>
  <option value="1">菜谱</option>
  <option value="2">标识标准</option>
        </select>
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 中文名称：</td>
      <td  nowrap class="TableData" width=220>
        <input type="text" class="BigInput" size=35  name="cnName" id="cnName">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 英文名称：</td>
      <td nowrap class="TableData" width=220>
        <input type="text" class="BigInput" size=35 name="enName" id="enName" value="">
      </td>
    </tr>
    <tr>
      <td nowrap class="TableData" width=100> 是否修改语音文件：</td>
      <td nowrap class="TableData" width=220>
        <input type="checkbox" class="SmallInput" id="isSoundFile" name="isSoundFile" size=10 onclick="checkboxChange()"/>
      </td>
    </tr>
    <tr id="soundFileTr" style="display:none">
      <td nowrap class="TableData" width=100> 英文语音文件：</td>
      <td nowrap class="TableData" width=220>
        <input class="BigInput" name="ATTACHMENT" id="ATTACHMENT" size="20" type="file"/>        
        <input type="hidden" name="soundFile" id="soundFile"/>
        <input type="hidden" name="formfile" id="formfile"/>
      </td>
    </tr>
    <tr class="TableControl" align="center" >
       <td colspan="2" nowrap >
         <input type="hidden" name="enable" id="enable">
         <input type="button" value="确定" class="BigButton" onclick="submitForm()"/>
         <input type="hidden" name="seqId" id="seqId">
       </td>
     </tr>
  </table>
</form>
</body>
</html>
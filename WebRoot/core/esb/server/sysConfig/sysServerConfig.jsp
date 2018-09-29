<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>配置系统设置</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
function doInit(){
  var url = "<%=contextPath%>/yh/core/esb/server/sysConfig/act/SysConfigAct/getSysConfig.act";
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;
    bindJson2Cntrl(data);
  }else{
    alert(rtJson.rtMsrg);
  }
}

</script>
</head>
<body onload="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td><img src="<%=imgPath %>/notify_new.gif" align="middle"><span class="big3"> 配置系统设置</span>&nbsp;&nbsp;
    </td>
  </tr>
</table>

<br>
<form action="<%=contextPath%>/yh/core/esb/server/sysConfig/act/SysConfigAct/updateSysConfig.act" id="" method="post">
<table class="TableBlock" width="400" align="center">
  
  <tr>
    <td align="left" width="120" class="TableContent" style="border-bottom:#9c9c9c 1px solid;">发送数据分份大小：</td>
    <td align="left" class="TableData" width="180"><input type="text" id="uploadPartSize" name="uploadPartSize">字节</td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent" style="border-bottom:#9c9c9c 1px solid;">接收数据分份大小：</td>
    <td align="left" class="TableData" width="180"><input type="text" id="downloadPartSize" name="downloadPartSize">字节</td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent" style="border-bottom:#9c9c9c 1px solid;">上传超时限制：</td>
    <td align="left" class="TableData" width="180"><input type="text" id="maxUploadTime" name="maxUploadTime">毫秒</td>
  </tr>
  <tr>
    <td align="left" width="120" class="TableContent" style="border-bottom:#9c9c9c 1px solid;">下载超时限制：</td>
    <td align="left" class="TableData" width="180"><input type="text" id="maxDownloadTime" name="maxDownloadTime">毫秒</td>
  </tr>
  <tr>
    <td  align="left" width="120" class="TableContent"  style="border-bottom:#9c9c9c 1px solid;">服务器缓存目录：</td>
    <td align="left" class="TableData" width="180">
        <input type="text" name="uploadCacheDir" id="uploadCacheDir" value="" class="BigInput" >&nbsp;
    </td>
   </tr>
  <tr>
    <td align="center" colspan="2">
      <input type="submit" value="修改">
      <input type="button" value="返回">
    </td>
  </tr>
</table>
</form>
</body>
</html>
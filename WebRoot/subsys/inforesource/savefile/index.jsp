<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 



<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>文件转存</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/javascript">

</script>

</head>
<body>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=imgPath%>/save_file.gif" align="middle"><span class="big3"> 文件批量转存 </span>
    </td>
    </tr>
</table>

<form name="form1" action="<%=contextPath%>/yh/subsys/inforesouce/act/YHBeachDownLoadFileAct/getPersonFolderInfo.act">
<table class="TableBlock" width="90%" align="center">
    <tr>
      <td nowrap class="TableHeader">
      <img src="<%=imgPath%>/green_arrow.gif" align="middle" WIDTH="20" HEIGHT="18"> 请选择转存位置：
      </td>
    </tr>
    <tr class="TableData" height="60">
      <td>                                                                    
        <input type="radio" name="PLACE" id="FOLDER2" onclick="form1.action='<%=contextPath%>/yh/subsys/inforesouce/act/YHBeachDownLoadFileAct/getPersonFolderInfo.act' " checked><label for="FOLDER2" title="将文件转移至个人文件柜根目录">个人文件柜</label>
        <input type="radio" name="PLACE" id="FOLDER1" onclick="form1.action='<%=contextPath%>/yh/subsys/inforesouce/act/YHBeachDownLoadFileAct/getFolderInfo.act'"><label for="FOLDER1" title="将文件转移至公共文件柜">公共文件柜</label>
        <input type="radio" name="PLACE" id="NETDISK" onclick="form1.action='<%=contextPath%>/yh/subsys/inforesouce/act/YHBeachDownLoadFileAct/getNetDiskList.act' "><label for="NETDISK" title="将文件转移至网络硬盘">网络硬盘</label>
      </td>
    </tr>
    <tr>
      <td nowrap align="center" class="TableControl">
        <input type="submit"  value="下一步" class="SmallButtonW" >&nbsp;&nbsp;
        <input type="button"  value="关闭" class="SmallButtonW" onClick="window.close();">
      </td>
    </tr>
</table>
</form>


</body>
</html>
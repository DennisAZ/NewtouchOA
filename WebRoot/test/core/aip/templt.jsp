<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/core/inc/header.jsp" %>
<title>AIP Template</title>
<link href="<%=cssPath %>/style.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript">
var isOpen = false;
var isLoad = false;
function sel_file()
{
  var obj = $("HWPostil1");
  obj.LoadFile("");
  isLoad = true;
}
function convert()
{
  var obj = $("HWPostil1");
  if($("T_NAME").value=="")
  {
    alert("请输入模板名称！");
    return;
  }
  if(isLoad==false)
  {
    alert("请选中模板文件！");
    return;
  }
  if(isOpen == true)
  {
    var content = obj.GetCurrFileBase64();
    //把内容上传到服务器
    var rtJson = getJsonRs(contextPath + "/test/core/act/YHAIPAct/uploadAip.act",
        {T_NAME: $("T_NAME").value, CONTENT: content});
    if (rtJson.rtState == "0") {
      alert("保存成功！");
    }else {
      alert(rtJson.rtMsrg);
    }
  }
  else
  {
    alert("文档尚未转换完毕！请稍候！");
    convert();
  }   
}
</script>
<SCRIPT LANGUAGE=javascript FOR=HWPostil1 EVENT=NotifyDocOpened>
isOpen = true;
</SCRIPT>
</head>
<body>
<OBJECT id=HWPostil1 style="WIDTH:0;HEIGHT:0" classid=clsid:FF3FE7A0-0578-4FEE-A54E-FB21B277D567 codeBase='<%=contextPath %>/test/core/aip/HWPostil.cab#version=3,0,4,8' >"
</OBJECT>
<table class="TableList" width="80%" align="center">
  <tr>
    <td class="TableContent">模板名称</td>
    <td class="TableData">
      <input type="text" class="SmallButtonC" size=25 name="T_NAME" id="T_NAME">
    </td>
  </tr>
  <tr>
    <td class="TableContent">模板文件</td>
    <td class="TableData" >
      <span id="file_name"></span>
      <input type="button" value="选择模板文件"  class="SmallButtonC"  onclick="sel_file();">（word文档）
    </td>
  </tr>
  <tr>
    <td class="TableControl" align="center" colspan=2>
      <input type="button"  value="新建打印模板"  class="SmallButtonC"  onclick="convert();">
    </td>
  </tr>
</table>
</body>
</html>
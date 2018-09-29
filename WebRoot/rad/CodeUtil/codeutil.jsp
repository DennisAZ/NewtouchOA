<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<title>java代码快速生成</title>
</head>
<script type="text/javascript">
function doSbumit(){
  var rtJson = getJsonRs(contextPath + "/yh/yh/core/act/YHCodeUtilAct/loginCheck.act", mergeQueryString($("tempVaue")));
}
function doBrowse(path){
  try{
    var Message = "\u8bf7\u9009\u62e9\u6587\u4ef6\u5939";  //选择框提示信息
    var Shell = new ActiveXObject("Shell.Application");
    var Folder = Shell.BrowseForFolder(0, Message, 64, 'D:\\project\\yh');//起始目录为：我的电脑
    //var Folder = Shell.BrowseForFolder(0,Message,0); //起始目录为：桌面
    if(Folder != null) {
      Folder = Folder.items();  // 返回 FolderItems 对象
      Folder = Folder.item();  // 返回 Folderitem 对象
      Folder = Folder.Path;   // 返回路径
      if(Folder.charAt(Folder.length - 1) != "\\") {
        Folder = Folder + "\\";
      }
        document.getElementById(path).value = Folder;
        return Folder;
    }
  }catch (e){
    alert(e.message);
  }
}
</script>
<body>
  <div>
	  <form name = "tempVaue" id = "tempVaue"  onSubmit='return check()'>
               模板文件  :<input name="template" type="file"> <br>
               输出文件夹:<input name="out" type="text" id = "out"> <input type="button" value="浏览" onclick="doBrowse('out')"> <br>
              包名 : <input name="packageName" type="text"> <br>
	         类名   : <input name="className" type="text"> <br>
	         日志信息 : <input name="loggerValue" type="text"> <br>
	  </form>
	  <input type="button" value="自动生成代码" onclick="doSbumit()">
	  </div>
</body>
</html>
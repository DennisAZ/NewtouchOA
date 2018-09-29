<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<base href="<%=basePath%>"></base>
    <title>导入客户</title>
	<link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
	<style type="text/css">
		body{
			background:#fff;
		}
    	#xlsImpLayer {
			padding:10px;
		}
		#xlsImpLayer form {
			text-align:left;
			margin:0;
			padding:0 0 0 5px;
		}
		#impTil {
			font-weight:bold;
			color:#666;
			padding:4px 0 4px 0;
			text-align:left;
		}
		#impButton {
			padding:5px 0 18px 0;
			text-align:left;
		}
    </style>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript">
    	function startImport(form,value){
			var path = value;
			if(path==""){
				alert("请先选择excel文件!");
				return false;
			}
			else{
				while (path.indexOf("\\") != -1) {
					path = path.slice(path.indexOf("\\") + 1);
				}
				var ext = path.slice(path.lastIndexOf(".")).toLowerCase();
				if(ext != '.xls'){
					alert("请上传excel文件!");
					return false;
				}
				else{
					waitSubmit("submitButton","导入中，请稍候...");
					$("impGif").show();
            		form.action="fileAction.do?op=importCus";
					form.submit();
				}
			}
			
		}
    </script>
</head>

<body>
	<div id="xlsImpLayer">
    	<form method="post" action="" enctype="multipart/form-data">
            <div id="impTil">选择excel文件：</div>
            <input id="file" type="file" name="file" style="width:300px;"/>&nbsp;
            <div id="impButton">
            <input class="butSize2" id="submitButton" type="button" onClick="startImport(this.form, this.form.file.value)" value="开始导入"/>
            <span id="impGif" style="display:none;"><img style="vertical-align:middle" src="crm/images/gif/uploading.gif" alt="上传中..."/>正在导入...</span>
            </div>
        </form>
        <div class="tipsLayer">
            <ul>
                <li>①&nbsp;&nbsp;请使用系统提供的excel模板导入数据。</li>
                <li>②&nbsp;&nbsp;导入前请检查excel表格内数值格式正确，如果格式不正确将无法成功导入数据。</li>
            </ul>
        </div>
    </div>
</body>
</html>

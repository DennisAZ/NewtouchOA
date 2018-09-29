<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>上传图片</title>
    <link rel="shortcut icon" href="favicon.ico"/>
   
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	<link rel="stylesheet" type="text/css" href="css/style.css">
    <style type="text/css">
    	body{
			background-color:#FFFFFF;
		}	
		
		#imgDiv{
			text-align:center; 
			background-color:#E3E3E3; 
			height:60px;
		}
		#picContent{
			height:160px;
			padding-top:0px; 
			padding-bottom:2px;
		}
		
		#button{
			position:absolute;
			top:161px;
			left:0px;
			cursor:pointer; 
			width:100%; 
			padding:5px;
			FILTER: alpha(opacity=60);
			opacity:0.6;
		}
		#upForm{
			position:absolute;
			left:0px;
			padding-top:4px;
			background-color:#fff;
			width:100%;
			FILTER: alpha(opacity=90);
			opacity:0.9;
		}
		
		.formPosition{
			top:120px;
			height:60px;
		}
		.rsPosition{
			height:35px;
			top:148px;
		}
    </style>
    
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript">
		
		function showUpDiv(){
			$("fail").hide();
			$("button").hide();
			$("upImg").show();
			$("upForm").className="formPosition";
			$("upForm").show();
			
		}
		
		function showButton(){
			$("button").show();
			$("suc").hide(); 
			$("fail").hide(); 
			$("upForm").hide();
		}
		
		//文件类型判断
		function limitFile(form, file){
			var type = "${type}";
			var id = "${id}";
			var picPath="${path}";
			
			if (!file) {
				return;
			}
			var errMsg="";
			//后缀名数组
			var extArray = new Array(".gif", ".jpeg",".jpg", ".png",".bmp"); 
			if(!checkSuf(file,extArray)){
				errMsg = "只允许上传以下文件类型：" + (extArray.join("  ")) + ",请重新选择文件上传";
			}
			if(errMsg != ""){
				alert(errMsg);
			}
			else{
				$("upbotton").disabled=true;
				$("loadinf").style.display="inline";
				form.action="fileAction.do?op=upload&id="+id+"&type="+type+"&picPath="+encodeURIComponent(picPath);
				form.submit();
			}
		}
		
		function init(path,msg){
			if($("imgPre").width>240){
				$("imgPre").width=240;
			}
			if($("imgPre").height>180){
				$("imgPre").height=180;
			}
			
			if(msg=="suc"){
				$("upForm").show();
				$("upForm").className="rsPosition";
				$("suc").show(); 
				$("fail").hide(); 
				setTimeout("showButton()",1000);
			}
			else if(msg=="fail"){
				$("upForm").className="rsPosition";
				$("upForm").show();
				$("fail").show(); 
			}
			else{
				$("button").show();
			}
		}
		
		//改变透明度
		function toAlpha(isToAlpha,obj){
			if(isToAlpha==1){
				obj.filters.alpha.opacity=60;
			}
			else
				obj.filters.alpha.opacity=85;
		}

		window.onload=function(){
			init('${path}','${msg}');
		}
  </script>
  </head>
  
  <body>
    <div id="imgDiv">
    	<div id="picContent">
            <logic:notEmpty name="path">
                <img id="imgPre" src="${path}"/>
            </logic:notEmpty>
            <logic:empty name="path">
            	<img id="imgPre" style="display:none"/>
                <div style="padding:10px; color:#666666; font-siez:16px; font-weight:bold;">无产品图片</div>
            </logic:empty>
        </div>
        
        <div id="button" style="display:none" class="blueBg" onClick="showUpDiv()" onMouseOver="toAlpha(0,this)" onMouseOut="toAlpha(1,this)">
            <logic:equal name="type" value="empPic">
            上传照片
            </logic:equal>
            <logic:equal name="type" value="pro">
            上传图片
            </logic:equal>
        </div>
    	<div id="upForm" style="display:none">
            <div id="suc" style="display:none;">
                &nbsp;&nbsp;<img src="images/content/suc.gif" style="vertical-align:middle;"/>&nbsp;上传成功！&nbsp;
            </div>
        
            <div id="fail" style="display:none;">
                &nbsp;&nbsp;<img src="images/content/fail.gif" style="vertical-align:middle;"/>&nbsp;上传失败！&nbsp;
                请<a href="javascript:void(0)" onClick="showUpDiv();return false;">重新上传</a>
                <div class="red">
                  ${errorMsg}
                 </div>
            </div>
            <div id="upImg" style="display:none;">
                <form style="padding:0px; margin:0px;" method="post" action="" enctype="multipart/form-data">
                    <input class="butSize1 inputBoxAlign" style="width:190px; margin-bottom:5px" id="file" type="file" name="file"/><br/>
                    <input class="butSize1 inputBoxAlign" type="button" id="upbotton" style="cursor:pointer;" value="上传" onClick="limitFile(this.form, this.form.file.value)"/><label id="loadinf" class="blue" style="display:none">&nbsp;上传中...</label>
                </form>
            </div>
       </div>
    </div>
    
  </body>
</html>

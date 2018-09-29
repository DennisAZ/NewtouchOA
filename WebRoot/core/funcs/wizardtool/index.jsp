<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>向导工具</title>
<style type="text/css">
#left{
float:left;
position:relative;
width:200px;
border:1px solid #cccccc
}
#right{
float:left;
position:relative;
margin-left:5px;
width:800px;
border:1px solid #cccccc
}
#deployIframe{
border: 1px solid #699;
height: 30px;
}
</style>

<link rel="stylesheet" href ="<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/fck/fckeditor/fckeditor.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Menu.js" ></script>
<script type="text/javascript">
var folderTree = null ;
var nowFilePath = "";

var actionUrl = contextPath + "/yh/core/funcs/wizardtool/act/YHWizardToolAct";
var pageMenuData = [
                {name:'打开',action:rightMenuOpen,icon:imgPath + '/cmp/rightmenu/addStep.gif'}
				 ,'-'
				 ,{name:'发布',action:deployToLocation,icon:imgPath + '/cmp/rightmenu/addStep.gif'}
				 ,{name:'下载',action:downFile,icon:imgPath + '/cmp/rightmenu/addStep.gif'}
				 ,{name:'删除',action:deleteFile,icon:imgPath + '/cmp/rightmenu/delItem.gif'}
				 ,{name:'重命名',action:rename,icon:imgPath + '/cmp/rightmenu/ico4-4.gif'}
				 ];
var folderMenuData = [
                {name:'新建',action:newFolder,icon:imgPath + '/cmp/rightmenu/addStep.gif'}
                ,{name:'新建页面',action:newPage,icon:imgPath + '/cmp/rightmenu/addStep.gif'}
       			,'-'
                ,{name:'发布',action:test,icon:imgPath + '/cmp/rightmenu/addStep.gif'}
				 ,{name:'删除',action:deleteFile,icon:imgPath + '/cmp/rightmenu/delItem.gif'}
				 ,{name:'重命名',action:rename,icon:imgPath + '/cmp/rightmenu/ico4-4.gif'}
				 ];
function test(event, extData){
  
}
function deployToLocation(event, path){
  window.open(actionUrl + "/deployToLocation.act?path=" + path
      , 'deploy'
      , 'height=20, width=200 , toolbar=no, menubar=no, scrollbars=no,resizable=no,location=no, status=no'
  ); 
}
function downFile(event, path){
  var url = actionUrl + "/downPage.act?path=" + path;
  window.open(url);
}
function rename(event, path){
  var node = folderTree.getNode(path);
  if(node.parentId != '0'){
    window.open("rename.jsp?path=" + path
        , 'newwindow'
        , 'height=20, width=200 , toolbar=no, menubar=no, scrollbars=no,resizable=no,location=no, status=no'
    ); 
  }else{
		alert('根目录不准修改');
  }
}
function newFolder(event, id){
  var json = getJsonRs(actionUrl + "/newFolder.act","path=" + id);
  if(json.rtState == '0'){
    folderTree.addNode(json.rtData);
  }
}
function newPage(event, id){
  var json = getJsonRs(actionUrl + "/newPage.act","path=" + id);
  if(json.rtState == '0'){
    folderTree.addNode(json.rtData);
    folderTree.nodeClick(json.rtData.nodeId);
  }
}
function deleteFile(event, id){
  if(confirm('确认删除吗？')){
    var json = getJsonRs(actionUrl + "/deleteFile.act","path=" + id);
  	if(json.rtState == '0'){
    	folderTree.removeNode(id);
    	nowFilePath = id;
    	var oEditor = FCKeditorAPI.GetInstance('htmlContent') ;
        oEditor.SetData("");
  	} 
  }
}
function doInit(){
  $('left').oncontextmenu = function(event){
	var event = event || window.event;
	Event.stop(event);
  }
  var config = {bindToContainerId:'left'
    				,requestUrl:actionUrl + '/getFolder.act'
					,isOnceLoad:true
					,linkPara:{clickFunc:openHtml}
					,contextMenu:openRight
				};
  folderTree = new DTree(config);
  folderTree.show();
}
function openRight(event, nodeId){
  if(nodeId.endsWith(".html")){
    var menu = new Menu({bindTo:nodeId , menuData:pageMenuData});
    menu.show(event);
  }else{
    var menu = new Menu({bindTo:nodeId , menuData:folderMenuData});
    menu.show(event);
  }     
}

function rightMenuOpen(event, id){
  openHtml(id); 
}


function openHtml(id){
  if(id.endsWith(".html")){
	var url = actionUrl + "/getFile.act";
  	var json = getJsonRs(url , "path=" + id);
  	widgetData = json.rtData.data;
  	nowFilePath = id;
  	var oEditor = FCKeditorAPI.GetInstance('htmlContent') ;
    oEditor.SetData(json.rtData.htmlContent);
  }
}
function jsonToInput(){
  var data = $H(widgetData);
  var dataDiv = $('widgetDataDiv');
  if(dataDiv){
    document.submitForm.removeChild(dataDiv);
  }
  dataDiv = document.createElement("div");
  dataDiv.id = "widgetDataDiv";
  data.each(function(pair){ 
  	var hid = document.createElement('input');
      hid.type = "hidden";
      hid.name = pair.key;
      hid.value = pair.value;
      dataDiv.appendChild(hid);
	}); 
  document.submitForm.appendChild(dataDiv);
}
function deploy(){
  jsonToInput();
  document.submitForm.action = actionUrl + "/deploy.act";
  document.submitForm.target = "deployIframe";
  document.submitForm.submit();
}
function ExecuteCommand( commandName )
{
  // Get the editor instance that we want to interact with.
  var oEditor = FCKeditorAPI.GetInstance('htmlContent') ;

  // Execute the command.
  oEditor.Commands.GetCommand( commandName ).Execute() ;
}
</script>
</head>
<body onload="doInit()">
<div id="aa"></div>
<div id="left">

</div>
<div id="right">
<div id="fck">
<form name="submitForm" id="submitForm" action="<%=contextPath %>/yh/core/funcs/wizardtool/act/YHWizardToolAct/previewPage.act" method="post" target="preview">
<script type="text/javascript">
widgetData = {} ;
var sBasePath = contextPath+'/core/js/cmp/fck/fckeditor/';
var oFCKeditor = new FCKeditor( 'htmlContent' ) ;
oFCKeditor.BasePath    = sBasePath ;
oFCKeditor.Height    = 400;
var sSkinPath = sBasePath + 'editor/skins/office2003/';
oFCKeditor.Config['SkinPath'] = sSkinPath ;
oFCKeditor.Config['PreloadImages'] =
                sSkinPath + 'images/toolbar.start.gif' + ';' +
                sSkinPath + 'images/toolbar.end.gif' + ';' +
                sSkinPath + 'images/toolbar.buttonbg.gif' + ';' +
                sSkinPath + 'images/toolbar.buttonarrow.gif' ;
//oFCKeditor.Config['FullPage'] = true ;
oFCKeditor.ToolbarSet    = 'wizardtool' ;
oFCKeditor.Value = '' ;
oFCKeditor.Create();
</script>
</form>
</div>
<iframe name="deployIframe" id="deployIframe" style="display:none"></iframe>
</div>
<input type="button" onclick="ExecuteCommand( 'assistInput' )" value="点我"/>
</body>
</html>
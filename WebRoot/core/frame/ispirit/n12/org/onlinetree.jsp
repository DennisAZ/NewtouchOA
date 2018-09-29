<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%
String onlineRefStr = YHSysProps.getString("$ONLINE_REF_SEC");
if (onlineRefStr == null || "".equals(onlineRefStr.trim())) {
  onlineRefStr = "3600";
}



%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>在线人员</title>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">

<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/DTree1.0.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/frame/ispirit/n12/js/Menu.js" ></script>
<script type="text/Javascript"> 
var tree =  null;

function onlineView() {
  var url = "<%=contextPath%>/yh/core/funcs/system/interfaces/act/YHInterFacesAct/getOnlineView.act";
  var rtJson = getJsonRs(url);
  if (rtJson.rtState == "0") {
    if(rtJson.rtData.paraValue == 1){
      return;
    }else if(rtJson.rtData.paraValue == 2){
      window.location.href="<%=contextPath%>/core/frame/parallelTree.jsp";
    }
  }else {
    alert(rtJson.rtMsrg); 
  }
}

function doInit(){

  var onlineRef = '<%=onlineRefStr%>';
  if (isNaN(onlineRef)) {
    onlineRef = 120;
  }
  setInterval(reload, onlineRef * 1000);
  
 // onlineView();
  tree = new DTree({requestUrl:'<%=contextPath%>/yh/core/funcs/system/ispirit/n12/org/act/YHIsPiritOrgAct/getOnLineTree.act?MODULE_ID='+ 1 +'&privNoFlag=0&id='
    , isOnceLoad:true
    , treeStructure:{isNoTree:false}
    , linkPara:{clickFunc:getChildOrEdit}
    , aOnmuseover:showImage
    //, onmuseout:hideImage
    , isHaveTitle:true
    //, sort:'asc'
  });
 tree.show(); 
 //var node = tree.getFirstNode();
 //tree.open(node.nodeId);
}

function reload() {
  window.location.reload();
}

function smsFunc(aa,bb ,id) {
  var fromId = id;
  var url = "<%=contextPath%>/core/funcs/message/smsback.jsp?fromId="+fromId;
  
  if(typeof(window.external.OA_SMS) != 'undefined'){  //在精灵中打开
	  
      window.external.OA_SMS(url,"","OPEN_URL");
  
   }else{    //在浏览器中打开
	   
    window.open (url, 'newwindow', 'height=340, top='+(screen.height-340)/2+',left='+(screen.width-700)/2+', width=700, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=n o, status=no');

   }
}
function emailFunc(aa,bb ,id) {
  var toId = id;
  var url = "<%=contextPath%>/core/funcs/email/new/index.jsp?toId="+toId;
  
  if(typeof(window.external.OA_SMS) != 'undefined'){  //在精灵中打开
	  window.external.OA_SMS(url,"","OPEN_URL");
  }else{  //在浏览器中打开
    window.open (url, 'newwindow', 'height=600, top='+(screen.height-600)/2+',left='+(screen.width-800)/2+', width=800, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=n o, status=no');

  }
  }

function hideImage(event , id , a , li){
  var image = $('append-' + id);
  if (image) {
    var parent = image.parentNode;
    if (parent) {
      parent.style.width = '';
    }
    image.hide();
  }
  var image1 = $('append1-' + id);
  if (image1) {
    image1.hide();
  }
}
function showImage(event , id , a , li){

	// FILTER: alpha(opacity=100); 
	
  if (id.startsWith("r")) {
    id = id.substring(1 , id.length);
    var menuDataM = [
                   {name:'',tip:"发微讯",action:smsFunc,icon:imgPath + '/msg_i.png',extData:id}
                   ,{tip :'发邮件',name:'',action:emailFunc,icon:imgPath + '/email_i.png',extData:id}
                //   ,{tip:'浏览器 在线',name:'',action:iEFunc,icon:imgPath + '/client_type_0_i.png',extData:id}
                   ];
    var userName = a.innerHTML;
    var len = userName.length;
    var num=len * 10 +10;
    num=num+"px";
 
    var menu = new Menu({id:"ispirit_menu",bindTo:a ,expandType: 1, menuData:menuDataM ,attachCtrl:true},{border:'none',width:'80px',position :'absolute',marginTop:'-20px',paddingLeft:num,display:"block"});
    menu.show(event);
  }
}

function getChildOrEdit(id){
  if(id.indexOf('r')!= -1){
    id=id.substring(1,id.length);
   //dispParts("/yh/core/funcs/userinfo/index.jsp?userId="+id+"&windows=1",0);

    if(typeof(window.external.OA_SMS) != 'undefined' &&  window.top.bIMLogin== true){  //在精灵中打开
     //  var name =getUserName(id);
       window.external.OA_SMS(id,"aa","SEND_MSG");
    }else{
    	//在浏览器中打开
    	var url="/yh/core/funcs/message/smsback.jsp?fromId="+id;
    	 window.external.OA_SMS(url,"","OPEN_URL");
     //window.open (url, 'newwindow', 'height=340, top='+(screen.height-340)/2+',left='+(screen.width-700)/2+', width=700, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=n o, status=no');

    }
    
  }
}

function iEFunc(id){
	alert(id);
}

function getUserName(id){
	var url=contextPath + "/yh/core/funcs/system/ispirit/n12/org/act/YHIsPiritOrgAct/getUserName.act?uid="+id;
	 var json = getJsonRs(url,pars);
	 if(json.rtState=="0"){
		 return json.rtData.data;
	 }else{
		 return "";
	 }
	
}
</script>
</head>
<style type="text/css">
#RightMenu div{
background-color:#E8EBF2;
  float:left;
  padding-left:4px;
}

</style>
<body onload="doInit()" style=" background-color:#E8EBF2; ">
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>全部人员</title>
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
function doInit(){
  tree = new DTree({
     requestUrl:'<%=contextPath%>/yh/core/module/org_select/act/YHOrgSelectAct/getAllTree.act?MODULE_ID='+ 2 +'&privNoFlag=0&id='
    , isOnceLoad:false
    , treeStructure:{isNoTree:false}
    , linkPara:{clickFunc:getChildOrEdit}
    , aOnmuseover:showImage
    //, onmuseout:hideImage
    , isHaveTitle:true
  });
 tree.show(); 
 //var node = tree.getFirstNode();
 //tree.open(node.nodeId);
 
}

function smsFunc(aa,bb ,id) {
  var fromId = id;
  var url = "<%=contextPath%>/core/funcs/message/smsback.jsp?fromId="+fromId;

  //dispParts("/yh/core/funcs/userinfo/index.jsp?userId="+id+"&windows=1",0);
  if(typeof(window.external.OA_SMS) != 'undefined' ){  //在精灵中打开
   //  var name =getUserName(id);
     window.external.OA_SMS(url,"","OPEN_URL");
  }else{
    window.open (url, 'newwindow', 'height=340, top='+(screen.height-340)/2+',left='+(screen.width-700)/2+', width=700, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=n o, status=no');
  }
}
function emailFunc(aa,bb ,id) {
  var toId = id;
  var url = "<%=contextPath%>/core/funcs/email/new/index.jsp?toId="+toId;
  if(typeof(window.external.OA_SMS) != 'undefined' ){  //在精灵中打开
	   //  var name =getUserName(id);
	     window.external.OA_SMS(url,"","OPEN_URL");
	  }else{
	    window.open (url, 'newwindow', 'height=340, top='+(screen.height-340)/2+',left='+(screen.width-700)/2+', width=700, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=n o, status=no');
	  }
}

function hideImage(event , id , a , li){
  var image = $('append-' + id);
  if (image) {
    var parent = image.parentNode;
    if (parent) {
      parent.style.width = '';
    }
    image.style.visibility = "hidden";
  }
  var image1 = $('append1-' + id);
  if (image1) {
    image1.style.visibility = "hidden";
  }
}
function showImage(event , id , a , li){

 if (id.startsWith("r")) {
	    id = id.substring(1 , id.length);
	    var menuDataM = [
	                   {name:'',title:"发微讯",action:smsFunc,icon:imgPath + '/msg_i.png',extData:id}
	                   ,{title :'发邮件',name:'',action:emailFunc,icon:imgPath + '/email_i.png',extData:id}
	                 //  ,{title:'浏览器在线',name:'',action:iEFunc,icon:imgPath + '/client_type_0_i.png',extData:id}
	                   ];

	    var userName = a.innerHTML;
	    var len = userName.length;
	    var num=len * 10 +10;
	    num=num+"px";
	 
	    var menu = new Menu({id:"ispirit_menu",bindTo:a ,expandType: 1, menuData:menuDataM ,attachCtrl:true},{border:'none',width:'80px',position :'absolute',marginTop:'-20px',paddingLeft:num,display:"block"});


	    menu.show(event);
	  }
}

function getChildOrEdit(id){////////////////////////////////////////////
	  if(id.indexOf('r')!= -1){
	    id=id.substring(1,id.length);
	   //dispParts("/yh/core/funcs/userinfo/index.jsp?userId="+id+"&windows=1",0);
	    if(typeof(window.external.OA_SMS) != 'undefined' &&  window.top.bIMLogin== true){  //在精灵中打开
	     //  var name =getUserName(id);
	       window.external.OA_SMS(id,"","SEND_MSG");
	    }else{
	      //在浏览器中打开
	      var url="/yh/core/funcs/message/smsback.jsp?fromId="+id;
	      window.external.OA_SMS(url,"","OPEN_URL");
	    }
	    
	  }
	}
	
function iEFunc(id){
	  alert(id);
	}
	
</script>
<style type="text/css">
#RightMenu div{
  background-color:#E8EBF2;
  background-color:#E8EBF2;
  float:left;
  padding-left:4px;
}
</style>
</head>
<body onload="doInit()" style="background:transparent;"></body>
</html>
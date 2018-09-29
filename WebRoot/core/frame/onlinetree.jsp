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
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Menu.js" ></script>

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
  
  onlineView();
  tree = new DTree({requestUrl:'<%=contextPath%>/yh/core/module/org_select/act/YHOrgSelectAct/getOnLineTree.act?MODULE_ID='+ 1 +'&privNoFlag=0&id='
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
  window.open (url, 'newwindow', 'height=340, top='+(screen.height-340)/2+',left='+(screen.width-700)/2+', width=700, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=n o, status=no');




}

function emailFunc(aa,bb ,id) {
  var toId = id;
  var url = "<%=contextPath%>/core/funcs/email/new/index.jsp?toId="+toId;
  window.open (url, 'newwindow', 'height=600, top='+(screen.height-600)/2+',left='+(screen.width-800)/2+', width=800, toolbar=no, menubar=no, scrollbars=yes, resizable=yes,location=n o, status=no');
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
  //var image = $('append-' + id);
 // if (image) {
   // var parent = image.parentNode;
   // if (parent) {
     // parent.style.width = '150px';
  //  }
   // image.show();
 // }
  //var image1 = $('append1-' + id);
  //if (image1) {
    //image1.show();
 // }

  if (id.startsWith("r")) {
    id = id.substring(1 , id.length);
    var menuDataM = [
                   {name:'微讯',action:smsFunc,icon:imgPath + '/msg.png',extData:id}
                   ,{name:'邮件',action:emailFunc,icon:imgPath + '/email.png',extData:id}
                   ];
    var menu = new Menu({bindTo:a , menuData:menuDataM ,attachCtrl:true},{border:'1px solid #69F',width:'50px',position :'absolute',backgroundColor:'#FFFFFF',padding:'5px',display:"block"});
    menu.show(event);
  }
}

function getChildOrEdit(id){
  if(id.indexOf('r')!= -1){
    id=id.substring(1,id.length);
   //dispParts("/yh/core/funcs/userinfo/index.jsp?userId="+id+"&windows=1",0);
  
    if (top.openUrl) {
     top.openUrl({
       url: contextPath + "/core/funcs/userinfo/index.jsp?userId="+id+"&windows=1",
       text: '用户信息'
     });
    }
    else {
    	top.dispParts(contextPath + "/core/funcs/userinfo/index.jsp?userId="+id+"&windows=1");
    }
  }
}
</script>
</head>
<body onload="doInit()" style="background:transparent;">
</body>
</html>
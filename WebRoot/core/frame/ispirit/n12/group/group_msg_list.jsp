<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% 
 String Gid= request.getParameter("Gid"); 

%>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/Menu.js" ></script>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script type="text/Javascript">

   function  doInit(){
	   $("MSG_GROUP_ID").value="<%=Gid%>";
     var Gid =$("MSG_GROUP_ID").value;
   
     
  
    

   }

   function getGroupMsgSent( Gid ){//获取历史数据，查询及分页功能
     var url = "<%=contextPath%>/yh/core/funcs/system/ispirit/n12/group/act/YHImGroupAct/getGroupMsgSent.act";
      var param="&Gid="+Gid;
     var rtJson = getJsonRs(url,param);
      if (rtJson.rtState == "0") {
        var hContent= rtJson.rtData;
        var divStr="";
        for(var i=0;i<hContent.length;i++){
          divStr+="<div class=\"user_time\">"+hContent[i].msgUserName+"&nbsp;&nbsp;";
          divStr+=hContent[i].msgTime;
          divStr+="</div>";
          divStr+="<div>";
          divStr+=hContent[i].msgContent;
         //历史附件处理
         //
         divStr+="</div>";
          
        }

        $("show_content").innerHTML=divStr;
      }
     
   }

</script>

</head>
<body class="group_body" onload="doInit();">
  <input type="hidden" id="MSG_GROUP_ID" name="MSG_GROUP_ID" value=""  />
<br><a id="bottom1" href="#bottom"></a> <br>

<div id="show_content">
 
</div>


<div style="padding-bottom:10px;"></div>

<a name="bottom"></a>
<br>
<script>
var obj_a = document.getElementById("bottom1");
if(document.all) //for IE
   obj_a.click();
else if(document.createEvent){ //for FF
   var ev = document.createEvent('HTMLEvents');
   ev.initEvent('click', false, true);
   obj_a.dispatchEvent(ev);
}
</script>



</div>

</body>
</html>


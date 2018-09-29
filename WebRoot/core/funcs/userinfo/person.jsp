<%@ page language="java" import="java.util.*" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%@page import="yh.subsys.oa.hr.manage.staffInfo.act.YHHrStaffInfoAct"%>
<%@page import="java.io.File"%><html>
<%
  String userId=request.getParameter("userId");
  String windows=request.getParameter("windows");

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title></title>
<link rel="stylesheet" href="<%=cssPath%>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<link rel="stylesheet" href="<%=cssPath%>/person.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/subsys/oa/meeting/js/util.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/userinfo/js/userinfoUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/userinfo/js/userinfo.js"></script>
<script type="text/javascript">

//获取用户信息      
function doInit(){    
  var url = "<%=contextPath%>/yh/core/funcs/userinfo/act/YHUserInfoAct/getUserDetailAct.act";
  var param="userId=<%=userId%>";
  var rtJson = getJsonRs(url,param);

  if(rtJson.rtState == "0"){
    var data = rtJson.rtData;

	   data.userPriv=getUserPriv(data.userPriv);
        var data2 = getDeptNo(data.deptId);
        var telNo = "";
        if (data2) {
          telNo = data2.deptNo;
        }
        var faxNo = "";
        if (data2) {
          faxNo = data2.faxNo;
        }
	   	$("telNo").innerHTML = telNo
	   data.deptId=getDept(data.deptId);	   
	   data.sex=getSexName(data.sex);
       data.auatar=getAvatar(data.auatar,data.sex);
	   bindJson2Cntrl(data); 
     
      if(data.mobilNoHidden=="1"){
         $("phone").style.display="none";
         }
      $('faxNoDept').innerHTML = faxNo;
	   
	   $("faxNo").innerHTML = faxNo;	  //  window.showModalDialog("menu2.jsp", window, "dialogWidth:300px;dialogHeight:300px;");
	    var contact="<a class=\"sms\"   href=\"javascript:openWindow('"+data.seqId+"',1)\"></a><a class=\"email\" href=\"javascript:openWindow('"+data.seqId+"',2);\" ></a>"; 	  	

	    $("contact").innerHTML=contact;       
	    var status=getOnStatus(data.seqId,data.onStatus);	
	    $("online_status").innerHTML=status;	  
	    data.onLine=getOnlineLevel(data.onLine);      
	    $("online_time").innerHTML=data.onLine;
			$("userPrivOtherName").innerHTML=getUserPrivOtherName(data.userPrivOther);
   }
}

function openWindow(id,index){
	  if(index==1){ //sms
	  window.open("/yh/core/funcs/sms/smsback.jsp?fromId="+id,"",'height=400,width=700,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
	  }
	  else if(index==2){//email
	  window.open("/yh/core/funcs/email/new/index.jsp?toId="+id,"",'height=500,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,left=800,top=270,resizable=yes');
	  }
	}
	 


</script>

<body onLoad="doInit()">

<table width="880">
   <tr>
      <td width="610" align="left" valign="top">
         <div class="card">
            <div class="photo" id="auatar"  >
            
            </div>
            <div class="photo_cover"></div>
            <div class="card_detail">
               <div class="name" id="userName"></div>
               <div class="priv" id="userPriv"></div>
               <table class="table">
                  <tr><td class="item">电话：</td><td class="content" ><div id="telNoDept"></div></td></tr>
                  <tr name="phone" id="phone"><td class="item">手机：</td><td class="content" ><div id="mobilNo"></div></td></tr>
                  <tr><td class="item">邮箱：</td><td class="content" ><div id="email"></div></td></tr>
                  <tr><td class="item">QQ  ：</td><td class="content" ><div id="oicq"></div></td></tr>
                  <tr><td class="item">电话2：</td><td class="content" ><div id="bpNo"></div></td></tr>
                  <tr><td class="item">传真：</td><td class="content" ><div id="faxNo"></div></td></tr>
               </table>
            </div>
            <div class="concact" id="contact">			
            </div>
         </div>
         <div class="detail">
		
            <table class="detail_table">
               <tr><td class="item">部　　门：</td><td class="content" ><div id="deptId"></div></td></tr>
               <tr><td class="item">性　　别：</td><td class="content" ><div id="sex"></div></td></tr>
               <tr><td class="item">生　　日：</td><td class="content" ><div id="birthday"></div></td></tr>
               <tr><td class="item">辅助角色：</td><td class="content" ><div id="userPrivOtherName"></div></td></tr>
               <tr><td class="item">部门电话：</td><td class="content" ><div id="telNo"></div></td></tr>
               <tr><td class="item">部门传真：</td><td class="content" ><div id="faxNoDept"></div></td></tr>
               <tr><td class="item">MSN     ：</td><td class="content" ><div id="msn"></div></td></tr>
               <tr><td class="item">家庭地址：</td><td class="content" ><div id="addHome"></div></td></tr>
               <tr><td class="item">邮　　编：</td><td class="content" ><div id="postNoHome"></div></td></tr>
               <tr><td class="item">家庭电话：</td><td class="content" ><div id="telNoHome"></div></td></tr>
            </table>
	
         </div>
      </td>
      <td width="270" align="right" valign="top">
         <div class="other">
            <div class="online_time" id="online_time" ></div>
            <div class="online_status" id="online_status">                                                                     
              

            </div>
            <div class="remark">
               <table class="remark_table">
                  <tr><td class="item">用户留言：</td><td class="content"><div id="myStatus"></div></td></tr>
                  <tr style="display:none"><td class="item">论坛昵称：</td><td class="content" ><div id="nickName"></div></td></tr>
                  <tr  style="display:none"><td class="item">论坛签名：</td><td class="content" ><div id="bbsSignature"></div></td></tr>
               </table>
            </div>
        <div class="bottom" >
		  
		     <a href='javascript:history.go(-1)' class='back'></a>
			
         </div>   
		  
		   </div>
		 
			
               
            
      </td>
   </tr>
</table>
</body>
</html>
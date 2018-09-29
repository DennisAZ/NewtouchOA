 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
 
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%@ page  import="java.util.List, yh.core.funcs.doc.receive.data.YHDocReceive,java.net.URLEncoder,yh.core.funcs.doc.receive.data.YHDocConst"%>

<%
String webroot = request.getRealPath("/");
 List<YHDocReceive>docs = (List)request.getAttribute("docs");
 String flag = (String)request.getAttribute("ftype");
 if(flag==null || flag == ""){
    flag = "0";
 }
 String column = (String)request.getAttribute("column");
 String asc = (String)request.getAttribute("asc");
 String col = "";
 String ord = "";
 if(column != null && column != ""){
    col = column;
 }
 if(asc != null && asc != ""){
   ord = asc;
 }
%>
<html>
<head>
<title></title>
<link rel="stylesheet" href="<%=cssPath %>/page.css"/>
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css"/>
<link rel="stylesheet" href="<%=cssPath%>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css" type="text/css" />
<link rel="stylesheet" href ="<%=cssPath %>/cmp/tab.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Calendarfy.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/doc/receive/js/common.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/workflow.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/doc/workflowUtility/utility.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/doc/receive/js/MultiUserSelect.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/funcs/doc/receive/js/common.js" ></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/attachMenu.js"></script>
<script type="text/Javascript">
  function sendTo(seqId, id){//userId签收人Id
    var userId = document.getElementById(id).value;
    if( !userId || userId == "0" ){
      alert("请选择签收人");
      return false;
    }
    window.location.href = contextPath + "/yh/core/funcs/doc/receive/act/YHDocReceiveAct/updateDocReceive.act?seqId=" + seqId +"&userId="+ userId;
    return true;
  }

  function alarmTo(seqId, toId, docNo){
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocReceiveAct/alarmToRead.act";
    var param = "seqId="+ seqId + "&toId=" + toId + "&docNo="+docNo;
    var rtJson = getJsonRs(url, param);
    if(rtJson.rtState != "0"){
      alert("提醒失败");
    }else{
      alert("提醒成功");
    }
  }

  function selectAll(n){
    var boj = document.getElementsByName(n);	
    var ckObj = document.getElementById("allbox_for");
    var ischeck = ckObj.checked;
    if(ischeck == true && boj){  //全选

      for(var i=0; i<boj.length; i++){
        if(boj[i].checked == false && boj[i].disabled == false){
          boj[i].checked =true;
        }
      }
    }
    if(ischeck == false && boj){//全不选

      for(var i=0; i<boj.length; i++){
        if(boj[i].checked == true){
          boj[i].checked = false;
        }
      }
    }
  }

  function checkBoxSel(id){
    var cobj = document.getElementById(id);
    var ckObj = document.getElementById("allbox_for");
    var ischeck = cobj.checked;
    if(ischeck == false){    //不选

      ckObj.checked = false;
    }
  }

  /**
  *  批量提醒
  */
  function beanchAlarm(name){
    var boj = document.getElementsByName(name);	
    var f1 = 0;
    for(var i=0; i<boj.length; i++){
      if(boj[i].checked == false){
        f1 = f1 + 0;
      }else{
        f1 = f1 + 1;
      }
    }
   if(f1 > 0){
     checkAlarm();
   }else if(f1 == 0){
     alert("至少选择一项!");
   }
  }

  /**
   *  批量确认签收
   */
   function beanchConfrim(name){
     var boj = document.getElementsByName(name);	
     var f1 = 0;
     var userId = "";
     var seqIds = "";
     for(var i=0; i<boj.length; i++){
       if(boj[i].checked == false){
         f1 = f1 + 0;
       }else{
         f1 = f1 + 1;
         var uId = "userId_"+boj[i].id;
         var val = $(uId).value;
         var wh = document.getElementById("wh_"+boj[i].id).innerText;
          seqIds += boj[i].id +",";
          userId += document.getElementById("userId_"+boj[i].id).value+",";
         if(!val){
           alert("文号为:"+ wh +"的项签收人为空");
           return;
         }
       }
     }
    if(f1 > 0){
      seqIds = seqIds.substring(0, seqIds.lastIndexOf(","));
      userId = userId.substring(0, userId.lastIndexOf(","));
      $("seqIds").value = seqIds;
      $("userIds").value = userId;
      checkConfirm();
    }else if(f1 == 0){
      alert("至少选择一项!");
    }
   }

  function checkAlarm(){
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocReceiveAct/beanchAlarm.act";
    $("form1").action = url;
    $("form1").submit();
    return false;
  }

  function checkConfirm(){
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocReceiveAct/beanChConfirm.act";
    $("form1").action = url;
    $("form1").submit();
    return false;
  }

  function checkDetail(runId, status){
    if(runId != null && runId != "" && runId > 0){
      formViewByRunId(runId);
    }else{
      alert("该收文尚未办理,不能查看批办单");
    }
  }

  function edit(seqId){
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocReceiveAct/editDoc.act?seqId="+ seqId;
    window.open(url);
  }

  function cancel(seqId){
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocReceiveAct/cancel.act?seqId="+ seqId;
    var rtJson = getJsonRs(url);
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }else{
      alert(rtJson.rtMsrg); 
      window.location.href = window.location.href;
    }
    return;
  }

  /**
  *  批量打印
  */
  function doPrint(name){
    var boj = document.getElementsByName(name);	
    var f1 = 0;
    var printIds = "";
    for(var i=0; i<boj.length; i++){
      if(boj[i].checked == false){
        f1 = f1 + 0;
      }else{
        f1 = f1 + 1;
        printIds += boj[i].id + ",";
      }
    }
   if(f1 > 0){
     printIds = printIds.substring(0, printIds.length-1);
     var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocReceiveAct/print.act?printIds="+ printIds;
     var myleft = (screen.availWidth-780)/2 ;
     window.open(url,"read_vote","height=600,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
     
   }else if(f1 == 0){
     alert("至少选择一项!");
   }
  }

  function siglePrint(seqId){
    var url = contextPath + "/yh/core/funcs/doc/receive/act/YHDocReceiveAct/print.act?printIds="+ seqId;
    var myleft = (screen.availWidth-780)/2 ;
    window.open(url,"read_vote","height=600,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
  }

  function clearValue(id, name){
    document.getElementById(id).value = "";
    document.getElementById(name).value = "";
  }

  function printDetail(){
    var url = contextPath + "/core/funcs/doc/receive/detail.jsp";
    openWin(url);
  }

  function orderListioner(){
    var obj = document.getElementsByTagName("u");
    for(var i=0; i<obj.length; i++){
      obj[i].attachEvent("onclick", myFunc);   
    }
  }

  function myFunc(obj){
    var o= obj.srcElement["id"];
    var tt = document.getElementById(o);
    var nodes = tt.childNodes;
    if(!nodes[1] || nodes[1].src == ""){
      tt.innerHTML += "<img src='"+ imgUp +"' id='"+ o +"'>";
      createOrder(o, "asc");
    }else{
         if(nodes[1].src.indexOf(imgUp)>-1){
            nodes[1].src = imgDown;
            createOrder(o, "desc");
          }else if(nodes[1].src.indexOf(imgDown)>-1){
            nodes[1].src = imgUp;
            createOrder(o, "asc");
          }
     }
    removeNode(o);
    document.getElementById("form1").submit();
  }

  function removeNode(id){
    var obj = document.getElementsByTagName("u");
    for(var i=0; i<obj.length; i++){
      var nId = obj[i].id;
      if(nId && nId!= id){
        var tt = document.getElementById(nId);
        var nodes = tt.childNodes;
        if(nodes[1]){
          tt.removeChild(nodes[1]);
        }
      }
    }
  }

  
  function createOrder(colum, method){
    document.getElementById("colum").value = colum;
    document.getElementById("asc").value = method;
  }

  function doInit(){
    orderListioner();
    var col = "<%=col%>";
    var ord = "<%=ord %>";
    if(col){
      var tt = document.getElementById(col);
      $("colum").value = col;
      if(!ord){
        tt.innerHTML += "<img src='"+ imgUp +"' id='"+ col +"'/>";
        $("asc").value = ord;
      }else if(ord == "asc"){
        tt.innerHTML += "<img src='"+ imgUp +"' id='"+ col +"'/>";
        $("asc").value = ord;
      }else if(ord == "desc"){
        tt.innerHTML += "<img src='"+ imgDown +"' id='"+ col +"'/>";
        $("asc").value = ord;
      }
    }
  }
  var bm = "";
  /**
  *弹出批量选择签收人的页面
  */
  function doBeanChSelUser(name){
    var oq = beanchSel(name);
    if(oq){
      var url = contextPath + "/core/funcs/doc/receive/beachwrite.jsp?bm="+bm;
      window.open(url, "window","height=200,width=350,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,left=450,top=200,resizable=no");
    }
  }

  /**
   *  批量选择签收人

   */
   function beanchSel(name){
     var boj = document.getElementsByName(name);	
     var f1 = 0;
     var flag = 0;
     var sum = 0;
     for(var i=0; i<boj.length; i++){
       if(boj[i].checked == false){
         f1 = f1 + 0;
       }else{
         f1 = f1 + 1;
         var uId = "userId_"+boj[i].id;
         var val = $(uId).value;
         var wh = document.getElementById("wh_"+boj[i].id).innerText;
         bm = document.getElementById("hid_"+boj[i].id).value; //部门id
         for(var j=i; j<boj.length; j++){
           if(boj[j].checked == true){
             var dept = document.getElementById("hid_"+boj[j].id).value; //部门id
             if(bm != dept){
                alert("所选择的部门不是同一个部门");
                return false;
             }
           }
         }
       }
     }
    if(f1 > 0){
      return true;
     }else if(f1 == 0){
      alert("至少选择一项!");
      return false;
    }
   }

  /**
  * 批量填入人名和人名Id
  */
   function beanchUser(name, pid){
     var boj = document.getElementsByName("selAll");
     if(boj){	
	     for(var i=0; i<boj.length; i++){
	       if(boj[i].checked == true){
	         var uId = "userId_"+boj[i].id;
	         var uName = "userName_"+boj[i].id;
	         $(uName).value = name;
	         $(uId).value = pid;
	       }
	     }
     }
   }
</script>
</head>
 
<body class="bodycolor" topmargin="5" onload="doInit();">
 <form action="<%=contextPath %>/yh/core/funcs/doc/receive/act/YHDocReceiveAct/faDocReceive.act" id="form1" name="form1" method="post">
      <input type="hidden" value="" id="colum" name="colum"/>
      <input type="hidden" value="" id="asc"  name="asc"/>
      <input type="hidden" value="<%=flag%>" id="ftype"  name="ftype"/>
      <input type="hidden" value="" id="seqIds"  name="seqIds"/>
      <input type="hidden" value="" id="userIds"  name="userIds"/>
  </form>
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
<tr>
  <td class="Big"><img src="<%=imgPath %>/book.gif" width="22" height="18"><span class="big3"> 
   <%
     if("1".equalsIgnoreCase(flag)){%>
                已签收文
   <%}else if ("0".equalsIgnoreCase(flag)){%>
               未签收文
   <%}else if ("2".equalsIgnoreCase(flag)){%>
               未登记收文
   <%}%>
  </span>&nbsp;
  </td>
  <td valign="bottom" class="small1" align="center">共<span class="big4">&nbsp;<%=docs.size() %></span>&nbsp;条记录

  </td>
</tr>
</table>

<form id="form1" name="form1">
<table class="TableList" width="95%" align="center">
<tr class="TableHeader">
  <%
    if(!"1".equalsIgnoreCase(flag)){%>
      <td nowrap align="center">选择&nbsp;</td>
   <%} %>
  <td nowrap align="center" style="cursor:hand;" ><u id="TITLE ">标题</u>&nbsp;</td>
  <td nowrap align="center" style="cursor:hand;"><u id="DOC_NO">文号</u>&nbsp;</td>  
  <td nowrap align="center" style="cursor:hand;"><u id="COPIES">份数</u>&nbsp;</td>
   <td nowrap align="center" style="cursor:hand;"><u id="CONF_LEVEL">密级</u>&nbsp;</td>
  <td nowrap align="center" style="cursor:hand;"><u id="DOC_TYPE">类型</u>&nbsp;</td>
  <td nowrap align="center" style="cursor:hand;"><u id="FROMUNITS">来文单位</u>&nbsp;</td>
   <td nowrap align="center" style="cursor:hand;"><u id="REC_DOC">来文正文</u>&nbsp;</td>
  <td nowrap align="center" style="cursor:hand;"><u id="SPONSOR">承办部室</u>&nbsp;</td>
  <td nowrap align="center" style="cursor:hand;"><u id="CREATE_USER_ID">签收人</u>&nbsp;</td>
  <td nowrap align="center">操作&nbsp;</td>
</tr>
<%
  for(int i=0; i<docs.size(); i++){
    String spo = docs.get(i).getSponsor();
  %>
    <tr class="TableLine1">
    <%
    if(!"1".equalsIgnoreCase(flag)){%>
      <td nowrap align="center">
      <%
        if(spo == null || spo == ""){%>
           <input type="checkbox" id="<%=docs.get(i).getSeq_id() %>" name="selAll" disabled/>&nbsp;
        <%} else{%>
           <input type="checkbox" id="<%=docs.get(i).getSeq_id() %>" name="selAll" onclick="checkBoxSel('<%=docs.get(i).getSeq_id() %>')" value="<%=docs.get(i).getSeq_id() %>"/>&nbsp;
           <input type="hidden" id="hid_<%=docs.get(i).getSeq_id() %>" value="<%=spo%>"/>
        <%}%>
     </td>
    <%} %>
		  <td align="center"><%=docs.get(i).getTitle() %>&nbsp;</td>
		  <td align="center" id="wh_<%=docs.get(i).getSeq_id() %>"><%=docs.get(i).getDocNo() %>&nbsp;</td>
		  <td align="center"><%=docs.get(i).getCopies() %>&nbsp;</td>
		  <td align="center"><%=docs.get(i).getConfLevelName(webroot)%>&nbsp;</td>
		  <td align="center"><%=docs.get(i).getDocTypeName(webroot) %>&nbsp;</td>   
		  <td align="center"><%=docs.get(i).getFromUnits() %>&nbsp;</td>
      <% 
      String docName = docs.get(i).getRecDocName() ;
      String docId = docs.get(i).getRecDocId();
      %>
      <td align="center" >
      <div  id="showAtt<%=docs.get(i).getSeq_id() %>"></div>
        <% 
        if (!YHUtility.isNullorEmpty(docId)) {
          %>
      <script type="text/javascript">
        attachMenuUtil("showAtt<%=docs.get(i).getSeq_id() %>","doc",null,"<%=docName %>","<%=docId %>",true,"<%=docs.get(i).getSeq_id() %>");
      </script>
       <% } else { out.print("无"); }  %>
      </td>
       <td align="center"><%=docs.get(i).getFromUnits() %>&nbsp;</td>
		  <%
		   if(!"1".equalsIgnoreCase(flag)){
		    if(docs.get(i).getSponsor()!=null && docs.get(i).getSponsor()!=""){%>
		      <td align="center">
           <input type="hidden" id="userId_<%=docs.get(i).getSeq_id() %>" name="userId_<%=docs.get(i).getSeq_id() %>" value="" />
           <input type="text"  id="userName_<%=docs.get(i).getSeq_id() %>" name="userName_<%=docs.get(i).getSeq_id() %>" class="" style="width:50px;height:13px;" value="" readonly/>
           <a href="javascript:void(0);" class="orgAdd" onclick="selectSingleUserByDept(['userId_<%=docs.get(i).getSeq_id() %>', 'userName_<%=docs.get(i).getSeq_id() %>'],'<%=docs.get(i).getSponsor() %>')" title="添加收件人">添加</a>
           <a href="javascript:void(0);" class="orgClear" onclick="javascript:clearValue('userId_<%=docs.get(i).getSeq_id() %>','userName_<%=docs.get(i).getSeq_id() %>');" title="清空收件人">清空</a>
	  	</td>
		  <%}else{%>
		    <td align="center">&nbsp;</td>
		  <% }
		    }else{%>
		      <td align="center"><%=docs.get(i).getToUserName() %>&nbsp;</td>
		    <%}
		  %>
	  	
		  <td nowrap align="center">
		     <%
		       if("1".equalsIgnoreCase(flag)){
             String nextName = "已办结";
             if (!nextName.equals(docs.get(i).getNext().getPrcsName())) {
                %>
		         <a href="javascript:cancel('<%=docs.get(i).getSeq_id() %>');" >退回</a>	&nbsp;	
            <% } %>
             <a href="javascript:void(0);" onclick="checkDetail('<%=docs.get(i).getRunId() %>','<%=docs.get(i).getStatus() %>');">查看批办单</a>	&nbsp;	    
		      <%
                  }else{%>
		         <%
		        if(spo == null || spo == ""){%>
		           <a href="javascript:edit('<%=docs.get(i).getSeq_id() %>');">修改</a>	&nbsp;	
		           <a href="javascript:printDetail();">打印批办单</a>
		       <%}else{%>
		          <a href="javascript:void(0);" onclick="sendTo('<%=docs.get(i).getSeq_id() %>', 'userId_<%=docs.get(i).getSeq_id() %>')">确认签收</a>	&nbsp;	    
		          <a href="javascript:siglePrint('<%=docs.get(i).getSeq_id() %>');">打印</a>	&nbsp;	
		       <%}%>
		      <%}%>
		      
	    </td>
   </tr>
 <%}%>
  <%
   if(!"1".equalsIgnoreCase(flag) && docs != null && docs.size() >0){%>
     <tr class="TableControl">
      <td colspan="19">
      <input type="checkbox" name="allbox" id="allbox_for" onclick="selectAll('selAll');"><label for="allbox_for">全选</label> &nbsp;
      <!-- <a href="javascript:beanchAlarm('selAll');" title="批量提醒"><img src="<%=imgPath%>/msg.png" align="absMiddle">批量提醒</a>&nbsp; -->
	    <a href="javascript:beanchConfrim('selAll');" title="批量签收"><img src="<%=imgPath%>/folder_edit.gif" align="absMiddle">批量签收</a>&nbsp;
	     <a href="javascript:doPrint('selAll');" title="批量打印"><img src="<%=imgPath%>/folder_edit.gif" align="absMiddle">批量打印</a>&nbsp;
	     <a href="javascript:doBeanChSelUser('selAll');" title="批量选择签收人"><img src="<%=imgPath%>/user_group.gif" align="absMiddle">批量选择签收人</a>&nbsp;
      </td>
   </tr>
 <%}%>
</table>
</form>
</body>
</html>


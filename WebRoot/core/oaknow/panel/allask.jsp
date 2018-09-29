<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.core.oaknow.util.*"%>
<%@ page  import="yh.core.oaknow.data.*"%>
<html>
<head>
<title>知道管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css" />
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/style.css"/>
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css" />
<link rel="stylesheet" href = "<%=cssPath%>/style.css" />
<script type="text/Javascript">
function doInit(){  
  var beginParameters = {
      inputId:'startTime',
      property:{isHaveTime:false}
      ,bindToBtn:'beginDateImg'
        };
  new Calendar(beginParameters);
  var endParameters = {
      inputId:'endTime',
      property:{isHaveTime:false}
      ,bindToBtn:'endDateImg'
        };
  new Calendar(endParameters);
  fillSel(${status});
}

function  pager(currNo){
	$('currNo').value = currNo;
	document.getElementById("form1").submit();	
}
function fillSel(flag){
  if(flag=="undefined" || flag==null || flag ===""){
    $('status').value = -1;
   }else{
    $('status').value = flag;
   }
}
function ajax(askId, flag){
  var param = "askId="+ askId +"&flag="+ flag;
  var url = "<%=contextPath %>/yh/core/oaknow/act/YHOAKnowManageAct/toTuiJian.act";
  var rtjson = getJsonRs(url, param);
  if(rtjson.rtState == '0'){		
    document.getElementById("form1").submit();
  }else{
    alert(rtjson.rtMsrg); 
    return;
  }
}
function deleteAsk(askId, status){
  var msg='确认要删除该问题吗?';
  if(window.confirm(msg)){
    var param = "askId="+ askId +"&status="+ status;
    var url = "<%=contextPath %>/yh/core/oaknow/act/YHOAKnowManageAct/deleteAsk.act";
    var rtjson = getJsonRs(url, param);
    if(rtjson.rtState == '0'){		
      document.getElementById("form1").submit();
    }else{
      alert(rtjson.rtMsrg); 
      return;
    }
  }
}

function checkDate(){
  var start = $("startTime").value;
  var end = $("endTime").value;
  if(start != ""){    
	  if(!isValidDateStr(start)){
	    alert("起始时间格式不正确,如1999-10-10");
	    document.getElementById("startTime").focus();
	    return false;
	  }
  }
  if(end != ""){
	  if(!isValidDateStr(end)){
	   alert("结束时间格式不正确,如1999-10-10");
	   document.getElementById("endTime").focus();
	   return false;
	  }
  }
  document.getElementById("form1").submit();
  
}
</script>
<script src="/inc/js/module.js"></script>
<%
List<YHOAAsk> askList = (List<YHOAAsk> )request.getAttribute("askList");
YHPageUtil pu = (YHPageUtil)request.getAttribute("page");
%>
</head>
<body class="mbodycolor" topmargin="5" onload = "doInit();">
<div>
	<div class="gt">
	知道管理 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</div>
	<form name="form1" id="form1" action='<%=contextPath%>/yh/core/oaknow/act/YHOAKnowManageAct/gotoManage.act?' method='post' style="float:left">
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text"   onFocus="this.select()" name="ask" value="${ask}" class="BigInput"/>&nbsp;&nbsp;
	<select id="status" name="status">
   	<option value="-1" selected>所有</option>
    <option value="1" >已解决</option>
    <option value="0" >未解决</option>
  </select>
  &nbsp;&nbsp;
	  从<input type="text" id="startTime" onfocus="this.select();" name="startTime" class="BigInput" value="${startTime}" style="width:80px;" />
	  <img id="beginDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" />
	  至<input type="text" id="endTime" onfocus="this.select();" name="endTime" class="BigInput" value="${endTime}" style="width:80px;" />
	  <img id="endDateImg" src="<%=imgPath%>/calendar.gif" align="absMiddle" border="0" style="cursor:pointer" />
    &nbsp;&nbsp;<input type="button" onclick="checkDate();" class="BigButton" value="搜索" />&nbsp;&nbsp;
	  &nbsp;<!-- <input type="button" class="BigButton" value="导出" onClick="" /> -->
	  <input name="currNo" id="currNo" type="hidden" value=""/>
	</form>  
</div>

<div style="clear:both;text-align: center;">
  <%
       	if(askList.size()!=0){%>
   <TABLE class="tlists">
       <TR class="header">
         <TD width="45%">问题</TD>
         <TD width="20%">提问时间</TD>

         <TD width="5%">回</TD>         
         <TD width="5%">评</TD> 
         <TD width="10%">状态</TD>
         <TD width="15%">操作</TD>
       </TR>
     <%
       	  for(int i=0; i<askList.size(); i++){
       	  %>
       	  <tr>
       	  <td class="tctd tctd1">&nbsp;
       	  <a href="<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/editAsk.act?askId=<%=askList.get(i).getSeqId()%>"  title="<%=askList.get(i).getAsk() %>"><%=askList.get(i).getAsk()%></a>
       	  <%
       	  	if(askList.get(i).getCommend() == 1){
       	  	  %>
       	  	  &nbsp;&nbsp;<img src="<%=contextPath%>/core/styles/oaknow/images/commend.gif" /> 
       	  	  <%
       	  	}
       	  %>
       	  </td>
		       <td class="tctd"><%=askList.get(i).getCreateDateStr() %></td>
		       <td class="tctd"><%=askList.get(i).getAskReplyCount() %></td>
		       <td class="tctd"><%=askList.get(i).getCommendCount() %></td>
		       <td class="tctd">
		       		<%if(askList.get(i).getStatus() == 1){
		       		  %>
		       		  <img src="<%=contextPath%>/core/styles/oaknow/images/ok.gif" title="问题已解决" />
		       		  <%
		       		}else{
		       		  %>
		       		  <img src="<%=contextPath%>/core/styles/oaknow/images/no.gif" title="问题未解决" />
		       		  <%
		       		} 
		       		%>
		       </td>
		       
		       <td class="tctd">
		       		<%
		       			if(askList.get(i).getStatus()==1 && askList.get(i).getCommend() == 0){
		       			  %>
		       			  <a href="javascript:void(0);" onclick="ajax(<%=askList.get(i).getSeqId()%>,1); return false;">推荐</a>
		       			  <%
		       			}else if(askList.get(i).getStatus()==1 && askList.get(i).getCommend() == 1){
		       			  %>
		       			  <a href="javascript:void(0);" onclick="ajax(<%=askList.get(i).getSeqId()%>,0); return false;">取消推荐</a>
		       			  <%
		       			}
		       		%>		       	 
		         <a href="javascript:void(0);" onclick="deleteAsk(<%=askList.get(i).getSeqId()%>,<%=askList.get(i).getStatus()%>); return false;">删除</a>
		      </td>		
		      </tr>
       	  <%
       	}
         %></TABLE><%}else{
       	  %>
       	  <br />
       	  <br />
			  <table align="center" width="340" class="MessageBox">
         <tbody>
          <tr>
				    <td class="msg info">
				      <h4 class="title" align="left">提示</h4>
				      <div style="font-size: 12pt;" class="content">没有符合条件的问题！</div>
				    </td>
          </tr>
        </tbody>
      </table>     	  
       	  <%
       	}
       %>
   
</div>
<div class="pagebar"><!-- 分页 -->
   <%
    if(askList.size()!=0){
   	 if(pu.getCurrentPage()>1){
   %>
   	<a href=javascript:pager(1)>首页</a>&nbsp;&nbsp;<a href=javascript:pager(<%=pu.getCurrentPage()-1%>)>上一页&nbsp;&nbsp;</a>
   <%
   	 }
     if(pu.getCurrentPage() -4 >0){
       for(int no = pu.getCurrentPage()-4; no<pu.getCurrentPage(); no++){
         %>
         	<a href=javascript:pager(<%=no%>)><%=no%></a>&nbsp;&nbsp;
         <%
       }       
     }else{
       for(int no=1; no<pu.getCurrentPage(); no++){
         %>
        	<a href=javascript:pager(<%=no%>)><%=no%></a>&nbsp;&nbsp;
        <%
       }
     } 
     if(pu.getPagesCount()>1){
     %>
     		<a href=javascript:pager(<%=pu.getCurrentPage()%>)>[<%=pu.getCurrentPage()%>]</a>
     <%   
     }
     if(pu.getCurrentPage()+5 < pu.getPagesCount()){
       for(int no2= pu.getCurrentPage()+1; no2<pu.getCurrentPage()+5; no2++){
         %>
         	<a href=javascript:pager(<%=no2%>)><%=no2%></a>&nbsp;&nbsp;
         <%
       }
     }else{
       for(int no2=pu.getCurrentPage()+1; no2<=pu.getPagesCount(); no2++){
         %>
        	<a href=javascript:pager(<%=no2%>)><%=no2%></a>&nbsp;&nbsp;
        <%
       }
    }
     if(pu.getCurrentPage() < pu.getPagesCount()){
       %>
       <a href=javascript:pager(<%=pu.getCurrentPage()+1 %>)>下一页</a>&nbsp;
       <a href=javascript:pager(<%=pu.getPagesCount() %>)>末页</a>
       <%
     }
     }
   %>
  </div> 
</body>
</html>
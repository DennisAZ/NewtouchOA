<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="tempheader.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.core.oaknow.data.*"%>

<html>
<head>
<%
  YHOAAsk ask = (YHOAAsk)request.getAttribute("ask");
  List<YHAskAnswer> otherAnswers = (List<YHAskAnswer>)request.getAttribute("otherAnswers");
  List<YHOAComment> pingLun = ( List<YHOAComment>)request.getAttribute("pinLun");
  YHAskAnswer goodAnswer = (YHAskAnswer)request.getAttribute("goodAnswer");
  yh.core.funcs.person.data.YHPerson user = (yh.core.funcs.person.data.YHPerson)request.getSession().getAttribute("LOGIN_USER");
  List<YHOAAsk> askList = (List<YHOAAsk> )request.getAttribute("askList");
  List<YHCategoriesType> types = (List<YHCategoriesType> )request.getAttribute("types");
  String cont = (String)request.getAttribute("showFlag");
%>
<title>${ask.ask} - OA知道</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script language="javascript"><!--
function showDialog(answerId){    //id是传入后台的最佳答案的id	  
  document.getElementById("comment").value=""; 
   $("overlay").style.display = 'block';  
	$("commentBlock").style.left = (parseInt(document.body.clientWidth) - parseInt($("commentBlock").style.width))/2;
  $("commentBlock").style.top  = 150;
  $("overlay").style.width  =  document.body.clientWidth+15;
  if(parseInt(document.body.scrollHeight) < parseInt(document.body.clientHeight) )
     $("overlay").style.height  =  document.body.clientHeight;
  else
     $("overlay").style.height  =  document.body.scrollHeight;
  document.getElementById("commentBlock").style.display = 'block';	
	window.scroll(0,0);
}

function answer(askId){//回答对话框  
  document.getElementById("answerContent").value=""; 
	$("answerBlock").style.left = (parseInt(document.body.clientWidth) - parseInt($("answerBlock").style.width))/2;
  $("answerBlock").style.top  = 150;
  $("overlay").style.width  =  document.body.clientWidth +15;
  if(parseInt(document.body.scrollHeight) < parseInt(document.body.clientHeight) )
     $("overlay").style.height  =  document.body.clientHeight;
  else
     $("overlay").style.height  =  document.body.scrollHeight;
  $("overlay").style.display = 'block';
	$("answerBlock").style.display = 'block';
	window.scroll(0,0);
}

function dialogClose(id){
  $(id).hide();
  document.getElementById("overlay").style.display="";
}

function toAnswerAjax(){      //回答问题
  var juge = $("answerContent").value;
  if(juge =="" || juge ==" " || juge==null){alert("回答内容不能为空！");document.getElementById("answerContent").focus();return false;}
  var queryParam = $("answer").serialize();
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/toAnswerAjax.act", queryParam);
  if(rtJson.rtState == "1"){  //回答失败
    alert(rtJson.rtMsrg);
    return;
  }else{
    alert(rtJson.rtMsrg);     //回答成功
    dialogClose('answerBlock');
  //  window.location.reload();
    document.getElementById("reurl").submit();
   }
  }

function toAnswerPingLun(){      //对最佳答案的评论
  var juge = $("comment").value;
  if(juge =="" || juge ==" " || juge==null){alert(" 评论内容不能为空！");document.getElementById("comment").focus();return false;}
  var queryParam = $("pinglun").serialize();
   // alert(queryParam);
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/goodAnsPingLun.act", queryParam);
  if(rtJson.rtState == "1"){  //对最佳答案的评论失败
    alert(rtJson.rtMsrg);
    return;
  }else{
    alert("评论成功!");     //对最佳答案的评论成功
    dialogClose('commentBlock');
   // window.location.reload();
    document.getElementById("reurl").submit();
  }
  }
  
function replyAnswer(answerUserId, askId, answerId){  //采纳为答案

  var param = "userId="+answerUserId+"&&askId="+askId+"&&answerId="+answerId;
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/changeToGoodAnswer.act", param);
  if(rtJson.rtState == "1"){  //采纳为答案失败

    alert(rtJson.rtMsrg);
    return;
  }else{
    alert(rtJson.rtMsrg);     //采纳为答案成功

   // window.location.reload();
    document.getElementById("reurl").submit();
  }
}
/**
 * askId 问题id, flag: 0 取消推荐 ；1 设为推荐
 */
function tuiJianStatus(askId, flag){
  var param = "askId="+askId+"&&flag="+flag;
  var rtJson = getJsonRs("<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/tuiJianStatus.act", param);
  if(rtJson.rtState == "0"){
    //window.location.reload();
    document.getElementById("reurl").submit();
  }
}

function jump(selfId, parentId){
  var url = "<%=contextPath%>/yh/core/oaknow/act/YHOAKnowTypeAct/findType.act?typeId=";
	window.location.href = url+ selfId + "&parentId="+parentId;			
}

function getBack(){
	window.close();
	//var showFlag='<%=cont%>'; 
	//if(showFlag == 'CONTENT'){	
		//top.dispDesk();
	//}else{
	 // history.back();
	//}
	
}
--></script>
</head>
<body  topmargin="5" >
  <br />
  <!--评论html begin  -->
  <div id="commentBlock" class="dialogBlock" style="width:450px;height:300px;z-index:2">
    <div class="dialoghead">
       <table class="topBarTable" border="0" cellspacing="0" cellpadding="0">
         <tr>
           <td><span class="sp1">评论对话框</span></td>
           <td align="right"><img src="<%=contextPath%>/core/styles/oaknow/images/dialogclose.gif" onclick="javascript:dialogClose('commentBlock')">&nbsp;</td>
         </tr>
       </table>
    </div>
    <div style="text-align:center;">
        <br />
        <form name="pinglun" id="pinglun">
           <input type="hidden" value="<%=ask.getSeqId()%>" name="askId">
           <TABLE width="100%" border="0">
             <TR>
               <TD align="right" style="font-size:12px;">&nbsp;&nbsp;评论：</TD>
               <TD align="center"><textarea cols="40" rows="8" name="comment" id="comment"></textarea></TD>

              </TR>
           </TABLE>
           </form>
        <br />
        <input type="button" class="subbcss" value="提 交" onClick="toAnswerPingLun();" />
    </div>
  </div>
  <!--评论html end  -->
  <!--回答html begin  -->
  <div id="answerBlock" class="answerBlock" style="width:450px;height:300px;z-index:3;">
    <div class="dialoghead">
       <table class="topBarTable" border="0" cellspacing="0" cellpadding="0" width="100%">
         <tr>
           <td><span class="sp1">回答对话框</span></td>
           <td align="right"><img src="<%=contextPath%>/core/styles/oaknow/images/dialogclose.gif" onClick="javascript:dialogClose('answerBlock');" />&nbsp;</td>
         </tr>
       </table>
    </div>

    <form name="answer" id="answer">
    <div style="text-align:center;">
       <input type="hidden" value="${ask.seqId}" name="askId">
        <br />
           <TABLE width="100%">
             <TR>
               <TD align="right" style="font-size:12px;">回答内容：</TD>
               <TD align="center"><textarea cols="42" rows="8" name="content"  id="answerContent"></textarea></TD>
              </TR>

           </TABLE>
        <br />
        <input type="button" class="subbcss" value="提 交" onClick="toAnswerAjax();" />
    </div>
    </form>
  </div>
  <!--回答html end  -->
<div class="">
  <!--search begin  -->
<%@ include file="head2.jsp" %> 
  <!--search end  -->

  <div class="navbar">
    <a onclick="javascript:gotoIndex(); return false;" href="javascript:void(0);">${oaName}</a>
    <%
    	if(types.size() == 1){
    	  %>
    	  &nbsp;&raquo;&nbsp;<%=types.get(0).getName()%>
    	  <%
    	}else if(types.size() == 2){
    	  %>
    	  &nbsp;&raquo;&nbsp;<a href="javascript:void(0);" onclick="jump('<%=types.get(1).getSeqId()%>','<%=types.get(1).getPearentId()%>' ); return false;"><%=types.get(1).getName()%></a>&nbsp;&raquo;&nbsp;<%=types.get(0).getName()%>
    	  <%
    	}
    %>
  </div>
  <DIV class="mb12 bai">
    <DIV class=rg_1></DIV>
    <DIV class=rg_2></DIV>
    <DIV class=rg_3></DIV>
    <DIV class=rg>

      <DIV class=t1>
        <DIV class=ico>
          <DIV class="iok"></DIV>
        </DIV>          
          <% 
            if(ask.getStatus() == 1){
          %>
            已解决   
            <%
              if(ask.getCommend()==1){%>
                <img src="<%=contextPath%>/core/styles/oaknow/images/jianicon.gif" width="20" height="17" />
            <%}
              if(user.getUserId().equalsIgnoreCase("admin") || user.isAdminRole()){//如果用户是管理员和oa管理员


                %>
                  <a href="<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/editAsk.act?askId=${ask.seqId}">&nbsp;编辑</a>
                <%
                  if(ask.getCommend() != 1){ //问题为未推荐 
                    %>
                      <a href="javascript:tuiJianStatus('${ask.seqId}','1')">&nbsp;推荐</a>
                    <%
                  }else{
                   %>
                     <a href="javascript:tuiJianStatus('${ask.seqId}','0')">&nbsp;取消推荐</a>
                   <%
                  }
              }
            %>         
          <%
            }
            else{
          %>
            未解决            
          <%
            if(user.getUserId().equalsIgnoreCase("admin") || user.isAdminRole()){
              %>
               <a href="<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/editAsk.act?askId=${ask.seqId}">&nbsp;编辑</a>
              <%
             }
            }
          %>
      </DIV>
      <DIV class=bc0>
         <DIV class=p90>

           <DIV class="f14 B wr"><CQ>${ask.ask}</CQ></DIV>
           <DIV class=wr>
              <SPAN class=gray>提问者：${ask.creatorName}&nbsp;&nbsp;提问时间：${ask.createDateStr}</SPAN>
           </DIV>
           <DIV class="f14 wr"><pre>${ask.askComment}</pre></DIV>
           <DIV class="f14 wr"></DIV>
           <%if(ask.getStatus() == 0){%>
             <DIV class=p90>
                 <a onmouseout="this.className='answer2';" style="cursor: pointer;" onmouseover="this.className='answer1';" onmousedown="this.className='answer3';" href="javascript:answer();" class="answer2"></a>
             </DIV>
          <%} %>
         </DIV>
      </DIV>
    </DIV>
    <DIV class=rg_4></DIV>
    <DIV class=rg_5></DIV>
    <DIV class=rg_1></DIV>
  </DIV>
  
  <!-- 最佳答案 -->
  <% if(ask.getStatus() == 1){%>
  <DIV class="mb12 bai">
    <DIV class=rr_1></DIV>
    <DIV class=rr_2></DIV>
    <DIV class=rr_3></DIV>
    <DIV class=rr>
      <DIV class=t1>
         <DIV class=ico>
            <DIV class=ibest></DIV>
         </DIV>最佳答案


      </DIV>

      <DIV class=bc0 style="PADDING-RIGHT: 0pt; PADDING-LEFT: 0pt; PADDING-BOTTOM: 5px; PADDING-TOP: 5px">
         <DIV class=wr>
            <DIV class="f14 p90 pl10"><pre>${goodAnswer.answerComment}</pre></DIV>
            <DIV class="gray" style="MARGIN: 5px 5px 8px;text-align: right;"><a href="javascript:void(0)" onclick="javascript:showDialog(${goodAnswer.answerId})">评论</a>&nbsp;&nbsp;回答者：${goodAnswer.userName} &nbsp;&nbsp;回答时间：${goodAnswer.answerTimeStr}</DIV>
         </DIV>
      </DIV>
    </DIV>
    <DIV class=rr_4></DIV>
    <DIV class=rr_5></DIV>
    <DIV class=rr_1></DIV>
  </DIV>
 <%}%>
<!--   相关问题 -->
 <DIV class="mb12 bai">
    <DIV class=rg_1></DIV>
    <DIV class=rg_2></DIV>
    <DIV class=rg_3></DIV>
    <DIV class=rg>
      <DIV class=t1>

        <DIV class=ico>
          <DIV class=irelate></DIV>
        </DIV>
        相关问题
      </DIV>
      <DIV class=bc0>
         <DIV class=p90>
            <TABLE class=wr cellSpacing=0 cellPadding=0 border=0>
            <%if(askList!=null && askList.size()!=0){
              for(int i=0; i<askList.size(); i++){
              %>
              <tr>
              	<td class=f14 vAlign=top width=100%>&#8226;
              	    <a href="<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/findAskRef.act?askId=<%=askList.get(i).getSeqId()%>">
              	    <%=askList.get(i).getAsk()%>
              	    </a>
              	</td>	
             </tr>		          		
              <%
              }
            }else{
              %>
              	<tr>
              	<TR>
                <TD class=gray vAlign=top width=10>&#8226; </TD>
                <TD class=f14>
                	无相关问题

                </TD>

              </TR>
              	</tr>
              <%
            } 
            %>            
            </TABLE>
         </DIV>
      </DIV>
    </DIV>

    <DIV class=rg_4></DIV>
    <DIV class=rg_5></DIV>
    <DIV class=rg_1></DIV>
  </DIV>

<!--其他回答  -->
<% if(otherAnswers != null && otherAnswers.size() != 0){%>
<DIV class="mb12 bai">
     <DIV class=rg_1></DIV>
     <DIV class=rg_2></DIV>
     <DIV class=rg_3></DIV>
     <DIV class=rg>
        <DIV class=t1 style="POSITION: relative">
           <DIV class=ico>
              <DIV class=ianswer></DIV>
           </DIV>
                            其他回答
        </DIV>
            <DIV class=bc0 style="PADDING-RIGHT: 0pt; PADDING-LEFT: 0pt; PADDING-BOTTOM: 5px; PADDING-TOP: 5px">
            <DIV class=wr>           
             <%
                 for(int i=0; i<otherAnswers.size(); i++){
             %>
                  <DIV class="f14 p90 pl10"><pre><%=otherAnswers.get(i).getAnswerComment() %></pre></DIV>
                  <DIV class=gray style="MARGIN: 5px 5px 8px;text-align: right;">
                     <%
                       if(ask.getStatus() == 0){
                         if(user.getSeqId() == ask.getCreatorId() || user.getUserId().equals("admin")){
                     %>
                       <a href="javascript:void(0)" onclick="replyAnswer('<%=otherAnswers.get(i).getAnswerUserId() %>','${ask.seqId}','<%=otherAnswers.get(i).getAnswerId()%>')">采纳答案</a>
                     <%    
                         }
                       }
                     %>
                      回答者：<%=otherAnswers.get(i).getUserName() %>&nbsp;&nbsp;回答时间:<%=otherAnswers.get(i).getAnswerTimeStr() %>
                  </div>
                  <DIV id=Lg></DIV>
             <%
                 }
             %>
         </DIV>
         </DIV>
        </DIV> 
           
     <DIV class=rg_4></DIV>
     <DIV class=rg_5></DIV>
     <DIV class=rg_1></DIV>
  </DIV>
<%}%>
 
<% if(pingLun.size() != 0){ %>
<DIV class="mb12 bai">
   <DIV class=rg_1></DIV>
   <DIV class=rg_2></DIV>
   <DIV class=rg_3></DIV>

      <DIV class=rg>
         <DIV class=t1>
            <DIV class=ico>
                <DIV class=icomment></DIV>
            </DIV>
             对最佳答案的评论
         </DIV>
         <DIV class=bc0 style="PADDING-RIGHT: 0pt; PADDING-LEFT: 0pt; PADDING-BOTTOM: 5px; PADDING-TOP: 5px">
         <DIV class=wr>
         
         <%
             for(int i=0; i<pingLun.size(); i++){
         %>  
                  <DIV class="f14 p90 pl10"><pre><%=pingLun.get(i).getComment()%></pre></DIV>
                  <DIV class=gray style="MARGIN: 5px 5px 8px;text-align: right;">评论者：<%=pingLun.get(i).getUserName()%></DIV>
                  <DIV id=Lg></DIV>
         <%      
             }
         %>
        </DIV>
        </DIV>
   </DIV>
   <DIV class=rg_4></DIV>
   <DIV class=rg_5></DIV>
   <DIV class=rg_1></DIV>
</DIV>
<%} %>
<br><center><input type="button" class="BigButton" value="关闭" onclick="javascript:getBack();return false;"></center>
<br><br><div align=center style='font-size:9pt;color:gray'></div></div>
<input type="hidden" name="REMINDACT" id="REMINDACT" value="" />
<div id="p" class="loginbox" style="width:402px;height:250;"></div>
<div style="z-index:1;" id="overlay"></div>
<form id="reurl" name="reurl" action ="<%=contextPath%>/yh/core/oaknow/act/YHOAAskReferenceAct/findAskRef.act">
	<input type="hidden" value="<%=ask.getSeqId()%>" name="askId">
</form>
</body>
</html>
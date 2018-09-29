<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="tempheader.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="yh.core.oaknow.data.*"%>
<%
  YHOAAsk ask = (YHOAAsk)request.getAttribute("ask");
  List<YHAskAnswer> otherAnswers = (List<YHAskAnswer>)request.getAttribute("otherAnswers");
  List<YHOAComment> pingLun = ( List<YHOAComment>)request.getAttribute("pinLun");
  YHAskAnswer goodAnswer = (YHAskAnswer)request.getAttribute("goodAnswer");
  yh.core.funcs.person.data.YHPerson user = (yh.core.funcs.person.data.YHPerson)request.getSession().getAttribute("LOGIN_USER");
  List<YHOAAsk> askList  = (List<YHOAAsk>)request.getAttribute("askList");   
  List<YHCategoriesType> kinds  = (List<YHCategoriesType>)request.getAttribute("kinds"); 
%>
<html>
<head>
<title>OA知道</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js" ></script>
<script type="text/javascript">
function agreeToGoodAnswer(goodAnswerId, newAnswerId, oldUserId, newUserId, oldAskId, newAskId){
  var queryParam = "goodAnswerId=" + goodAnswerId +"&newAnswerId=" + newAnswerId + "&oldUserId="+oldUserId+"&newUserId="+newUserId+"&oldAskId="+oldAskId +"&newAskId="+newAskId;
  var url = "<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/agreeToGoodAnswer.act";
	var rtJson = getJsonRs(url, queryParam);
	if(rtJson.rtState == 0){
	 // window.location.reload();
		 document.getElementById("reurl").submit();
	}else{
		return;
	}
}

function deleteAnswer(answerId, askId, flag, userId){
  var queryParam = "answerId=" + answerId +"&askId=" + askId + "&flag=" + flag +"&userId=" + userId;
  msg='确认要删除该答案吗？';
  if(window.confirm(msg))
  {
	  var url = "<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/deleteAnswer.act";
		var rtJson = getJsonRs(url, queryParam);
		if(rtJson.rtState == 0){
		 // window.location.reload();
		  document.getElementById("reurl").submit();
		}else{
			return;
		}
  }
}

function deletePingLun(commentId){
  var queryParam = "commentId=" + commentId ;
  msg='确认要删除该评论吗？';
  if(window.confirm(msg))
  {
	  var url = "<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/deleteComment.act";
		var rtJson = getJsonRs(url, queryParam);
		if(rtJson.rtState == 0){
		  //window.location.reload();
		  document.getElementById("reurl").submit();
		}else{
			return;
		}
  }
}

function doInit(){
  var catelogies = ${toJson};
  var sel = document.getElementById("categorieid");
  var options = "";
  for(var i=0; i<catelogies.length; i++){
			//options +=  "<option value=\""+ catelogies[i].seqId +"\" >"+catelogies[i].name+"</option>";
		sel.options.add(new Option(catelogies[i].name, catelogies[i].seqId));
		for(var j=0; j<catelogies[i].list.length; j++){
				//options +=  "<option value=\""+ catelogies[i].list[j].seqId +"\" >"+"&nbsp;&nbsp;"+catelogies[i].list[j].name+"</option>";
			sel.options.add(new Option("  "+catelogies[i].list[j].name, catelogies[i].list[j].seqId));
		}
  }
  $('categorieid').value = ${ask.typeId};
}

function edit(id){
  var newid = "edit_" + id;
  document.getElementById(newid).disabled = false; 
  document.getElementById("eidtButton_" + id).style.display = 'none'; 
  document.getElementById("save_" + id).style.display = ''; 
}

function getChange(id){ //获得id框的内容
  return $("edit_" + id).value;
}

function saveChange(answerId){//answerId 答案的id, content 答案的内容
  var param = "answerId=" + answerId + "&content=" + getChange(answerId);
  var url = "<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/changeAnswers.act";
  var rtJson = getJsonRs(url, param);
  if(rtJson.rtState == 0){
    opedit(answerId);
	}else{
		alert("保存失败");
		return;
	}
}

function opedit(answerId){
  var newid = "edit_" + answerId;
  $("edit_" + answerId).value == getChange(answerId);
  document.getElementById(newid).disabled = true; 
  document.getElementById("eidtButton_" + answerId).style.display = ''; 
  document.getElementById("save_" + answerId).style.display = 'none'; 
}

function editAsk(){
  document.getElementById("ask").disabled = false; 
  document.getElementById("tab").disabled = false; 
  document.getElementById("askcontent").disabled = false; 
  document.getElementById("categorieid").disabled = false;
  document.getElementById("editButton").style.display = 'none';  
  document.getElementById("saveButton").style.display = '';  
}

function ajax(){
  var ask = $("ask").value;
  if(splitStr(ask)==" " || splitStr(ask)==""){
		alert("问题不能为空！");
		document.getElementById("ask").focus();
		return;
  }
  var typeId = $("categorieid").value;
  var keyword = splitStr($("tab").value);
  var content = $("askcontent").value;
  var askId = ${ask.seqId};
  if(keyword.split(" ").length >5){
		alert("标签个数不能超过5个");
		return;
  }else{  
	  var param = "askId="+askId+"&typeId=" + typeId +"&keyword=" + keyword +"&content="+content +"&ask="+ask;
	  var url = "<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/changeAsk.act";
	  var rtJson = getJsonRs(url, param);
	  if(rtJson.rtState == 0){
	    $("ask").value = ask;
	    $("categorieid").value = typeId;
	    $("tab").value = keyword;
	    $("askcontent").value = content;
	    document.getElementById("ask").disabled = true; 
	    document.getElementById("tab").disabled = true; 
	    document.getElementById("askcontent").disabled = true; 
	    document.getElementById("categorieid").disabled = true;
	    document.getElementById("editButton").style.display = '';  
	    document.getElementById("saveButton").style.display = 'none';  
		}else{
			alert("保存失败");
			return;
		}
  }
}
//全部替换，用s2替换所有s1


function splitStr(str){
  var arr = trim(str);
  var reg = /\s{2,}/g; //把多个空格转换为一个空格
  var newStr = arr.replace(reg," "); 
  return newStr;
} 
</script>
<script type="text/javascript">
   function gotoIndex(){
		window.location.href = "<%=contextPath%>/yh/core/oaknow/act/YHOAKnowAct/OAKnowIndex.act";
  }
   function jump(selfId, parentId){
   	window.location.href = "/yh/yh/core/oaknow/act/YHOAKnowTypeAct/findType.act?typeId="+ selfId + "&parentId="+parentId;			
   }
   function answer(askId){//回答对话框  
     document.getElementById("answerContent").value=""; 
   	$("answerBlock").style.left = (parseInt(document.body.clientWidth) - parseInt($("answerBlock").style.width))/2;
     $("answerBlock").style.top  = 150;
     $("overlay").style.width  =  document.body.clientWidth;
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
      // window.location.reload();
       document.getElementById("reurl").submit();
      }
   }
</script>
</head>
<body  topmargin="5" onload="doInit();">
<div class="bodydiv">
<div class="askbody"><br />
	<!--search begin  -->
	 <%@ include file="editaskhead.jsp" %>
	<!--search end  -->
	<form name="form2"  method='post'>
	<div class="navbar">
		<a href="javascript:void(0);" onclick="javascript:gotoIndex();">
		 <a onclick="javascript:gotoIndex(); return false;" href="javascript:void(0);">${oaName}</a>
    <%
    	if(kinds.size() == 1){
    	  %>
    	  &nbsp;&raquo;&nbsp;<%=kinds.get(0).getName()%>
    	  <%
    	}else if(kinds.size() == 2){
    	  %>
    	  &nbsp;&raquo;&nbsp;<a href="javascript:void(0);" onclick="jump('<%=kinds.get(1).getSeqId()%>','<%=kinds.get(1).getPearentId()%>' ); return false;"><%=kinds.get(1).getName()%></a>&nbsp;&raquo;&nbsp;<%=kinds.get(0).getName()%>
    	  <%
    	}
    %>

  <div class="mb12 bai">
    <div class=rg_1></div>
    <div class=rg_2></div>
    <div class=rg_3></div>
    <div class=rg>
      <div class=t1>
        <div class=ico>
          <div class=iok></div>
        </div>
				<%
				  if(ask.getStatus()==1){
				    %>
				    	  已解决 
				    <%
				  }else{
				    %>
				    	未解决
				    <%
				  }
				%>
           </div>
      <div class=bc0>
         <div class=p90>
           <div class="f14 B wr">问题 &nbsp;<input type="text" id="ask" name="ask" value="${ask.ask}" size="35" disabled/>&nbsp;
           	问题分类
            <select name="categorieid" id="categorieid" disabled>
                      <!--<option value="1" >分类1</option>
                      <option value="2" >&nbsp;&nbsp;分类2</option>
                      <option value="3" selected>&nbsp;&nbsp;分类3</option>
                      <option value="4" >&nbsp;&nbsp;分类6</option>
                      <option value="5" >分类4</option>          
            --></select>&nbsp;
                             标签 &nbsp;
           <input type="text" id="tab" name="tab" value="${ask.replyKeyWord} " style="width:150px;" class="" disabled/>
           &nbsp;(最多5个，空格隔开)
           </div>
           <div class=wr>
              <SPAN class=gray>提问者：${ask.creatorName }&nbsp;&nbsp;提问时间：${ask.createDateStr}</SPAN>
           </div>
           <div class="f14 wr"><textarea name="askcontent" style="width:910px;" cols="122" rows="11" id="askcontent" disabled>${ask.askComment}</textarea></DIV>
						<%if(ask.getStatus() == 0){%>
             <div class=p90>
                 <a onmouseout="this.className='answer2';" style="cursor: pointer;" onmouseover="this.className='answer1';" onmousedown="this.className='answer3';" href="javascript:answer();" class="answer2"></a>
             </div>
           <%} %>
            <div class=gray style="MARGIN: 5px 5px 8px;text-align: right;">            	
            	<span id="editButton" style="display:''"><a href="javascript:void(0)" onclick="javascript:editAsk();">编辑&nbsp;</a></span>
            	<span id="saveButton" style="display:none"><a href="javascript:void(0)" onclick="javascript:ajax();">保存</a></span>
            </div>
           <div class=p90>
           </div>
         </div>
      </div>
    </div>
    <div class=rg_4></div>
    <div class=rg_5></div>
    <div class=rg_1></div>

  </div>
    <% if(ask.getStatus() == 1){%>
  <div class="mb12 bai">
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
            <DIV class="f14 p90 pl10">
            	<textarea cols="122" rows="15" style="width:910px;" name="AC_3" disabled id="edit_${goodAnswer.answerId}" >${goodAnswer.answerComment}</textarea>
            </DIV>
            <DIV class=gray style="MARGIN: 5px 5px 8px;text-align: right;">
              <span><a href="javascript:void(0)"  id="eidtButton_${goodAnswer.answerId}" style="display:''" onclick="edit(${goodAnswer.answerId})"> 编辑</a></span>
              <span id="save_${goodAnswer.answerId}" style="display:none">              
              		<a href="javascript:void(0)" onclick="saveChange(${goodAnswer.answerId})">保存</a>
              </span> 
             	<a href="javascript:deleteAnswer('${goodAnswer.answerId}','${goodAnswer.askId}','1','${goodAnswer.answerUserId}');">删除</a>

            	回答者：${goodAnswer.userName} &nbsp;&nbsp;回答时间：${goodAnswer.answerTimeStr}           
            </DIV>
         </DIV>
      </DIV>
    </DIV>
    <DIV class=rr_4></DIV>
    <DIV class=rr_5></DIV>
    <DIV class=rr_1></DIV>
  </DIV>
  <%}%>

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
              	    <a href="<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/editAsk.act?askId=<%=askList.get(i).getSeqId()%>">
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
<% if(otherAnswers != null && otherAnswers.size() != 0){%>
  <DIV class="mb12 bai">
     <DIV class=rg_1></DIV>
     <DIV class=rg_2></DIV>
     <DIV class=rg_3></DIV>
     <DIV class=rg>
        <DIV class=t1 style="position: relative">
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
             		 <DIV class="f14 p90 pl10">
	             		 <textarea cols="122" style="width:910px;" disabled rows="10" style="width:910px;" name="AC_7" id="edit_<%=otherAnswers.get(i).getAnswerId()%>"><%=otherAnswers.get(i).getAnswerComment() %>
	             		 </textarea>
             		 </DIV>
             		 <DIV class=gray style="MARGIN: 5px 5px 8px;text-align: right;">
             		 		<span><a href="javascript:void(0)"  id="eidtButton_<%=otherAnswers.get(i).getAnswerId()%>" style="display:''" onclick="edit(<%=otherAnswers.get(i).getAnswerId()%>)"> 
             		 					编辑</a>
             		 		</span>
             		 		<span id="save_<%=otherAnswers.get(i).getAnswerId()%>" style="display:none">              
              		    <a href="javascript:void(0)" onclick="saveChange(<%=otherAnswers.get(i).getAnswerId()%>)">保存</a>
                    </span> 
             		    <a href="javascript:agreeToGoodAnswer('${goodAnswer.answerId}','<%=otherAnswers.get(i).getAnswerId()%>','${goodAnswer.answerUserId}','<%=otherAnswers.get(i).getAnswerId()%>','${goodAnswer.askId}','<%=otherAnswers.get(i).getAskId()%>')">采纳答案</a>&nbsp;&nbsp;
             		    <a href="javascript:deleteAnswer('<%=otherAnswers.get(i).getAnswerId()%>','<%=otherAnswers.get(i).getAskId()%>','0','<%=otherAnswers.get(i).getAnswerUserId()%>');">删除</a>
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
  <%} %>
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
         			   <DIV class="f14 p90 pl10"><p><%=pingLun.get(i).getComment()%></p></DIV>
	         			   <DIV class=gray style="MARGIN: 5px 5px 8px;text-align: right;">	         			    
	         			    <a href="javascript:deletePingLun('<%=pingLun.get(i).getCommentId() %>');">删除</a>
	         			          评论者：<%=pingLun.get(i).getUserName()%>
	         			   </DIV>
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
</form>
</div>
</div>

<div id="overlay" style="z-index:1;"></div>
<!--回答html begin  -->
<div id="answerBlock" class="answerBlock" style="width:450px;height:300px; z-index:2;">
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
  <form id="reurl" name="reurl" action ="<%=contextPath %>/yh/core/oaknow/act/YHOAAskReferenceAct/editAsk.act">
	 <input type="hidden" value="<%=ask.getSeqId()%>" name="askId">
  </form>
  <br><center><input type="button" class="BigButton" value="返回" onclick="history.back();"></center>
</body>
</html>
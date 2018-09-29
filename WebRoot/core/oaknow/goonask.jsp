<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.core.oaknow.data.*"%>
<%@ page  import="yh.core.util.YHUtility"%>
<head>
<title>OA知道</title>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<%
YHOAAsk ask = (YHOAAsk)request.getAttribute("ask");
String tit = ask.getAsk();
String tits = YHUtility.encodeSpecial(tit);
%>
<script type="text/javascript"><!--
function doInit(){
	var catelogies = ${jsonString};
	var options = "";	
	var sel = document.getElementById("TypeLevel1");
	for(var i=0; i<catelogies.length; i++){
	  sel.options.add(new Option(catelogies[i].name, catelogies[i].seqId));
	  if(i==0){
		  $('TypeLevel1').value = catelogies[0].seqId ;
		  changeType1();
		}
	}
	var ask = '<%=tits%>';
	findReference(ask,1);
}

function changeType1(){
	var catelogies = ${jsonString};
	var id = $('TypeLevel1').value;
	var sel = document.getElementById("TypeLevel2");
	sel.options.length = 1;
	for(var j=0; j<catelogies.length; j++){
		if(catelogies[j].seqId == id){		
			for(var i=0; i<catelogies[j].list.length; i++){
			  sel.options.add(new Option(catelogies[j].list[i].name, catelogies[j].list[i].seqId));
			}	
		}		
	}
	$('typeId').value=id;		
}

function changeType2(){
  var id = $('TypeLevel2').value;
  if(id == "-1"){
		return;
  }else{
    $('typeId').value=id;	  
  }
 
}

function dosubmit(){
  var url = "<%=contextPath%>/yh/core/oaknow/act/YHOAKnowAct/saveAsk.act";
  var tag = splitStr($("keyword").value);
  if(tag.split(" ").length >5){
		alert("标签个数不能超过5个");
		document.getElementById("keyword").focus();
		return;
  }else{
    $("keyword").value = tag;
  }
  var title= $("title").value;
	var typeId = $("typeId").value;
	if(typeId == "" || typeId == null){
		alert("请用admin登陆，先创建分类！");
		return false;
	}
	var keyword = $("keyword").value;
	var content = $("content").value;
	var param = "title="+title +"&typeId="+ typeId +"&keyword="+ keyword +"&content="+content;
  var rtjson = getJsonRs(url, param);
  if(rtjson.rtState == '0'){
		//alert("保存成功！");
		//window.location.reload();
		window.location.href="<%=contextPath%>/core/oaknow/askok.jsp";
  }else{
    alert("保存失败！"); 
    return;
  }
}

function findReference(param, currNo){
  var title = "title="+param +"&currNo=" + currNo;
  var url = contextPath + "/yh/core/oaknow/act/YHOAKnowAct/askQuestionByPage.act";
  var rtjson = getJsonRs(url, title);
  var temp = "";
  if(rtjson.rtState !== '1'){
  var len = rtjson.rtData.page.length;
  var currNo = rtjson.rtData.currNo;
  var totalNo = rtjson.rtData.totalNo;
  if(len && len != 0){
		for(var i=0; i<len; i++){
			temp += "<a href='<%=contextPath%>/yh/core/oaknow/act/YHOAAskReferenceAct/findAskRef.act?askId=" + rtjson.rtData.page[i].seqId +"'>";
			temp += rtjson.rtData.page[i].ask +"</a><br>";			
		}
		$('showarea').innerHTML = temp;		
  }else{
    $('showarea').innerHTML = "没有相关的问题！";
  }
	  if(totalNo > 1){
	   createPage(currNo, totalNo);
	  }
  }else{
    $('showarea').innerHTML = "没有相关的问题！";
  }
}
//分页
function createPage(currNo, totalNo){
  var pageNo="";
  if(currNo > 1){
		pageNo += "<a href=javascript:findReference('<%=tits%>',1);>首页</a>&nbsp;&nbsp;";
		pageNo += "<a href=javascript:findReference('<%=tits%>',"+(currNo-1)+")>上一页</a>&nbsp;&nbsp;";
  }
  if(currNo - 4 >0){
	  for(var no = currNo - 4; no< currNo; no++){    //当前页前面的页数
	   pageNo += "<a href=javascript:findReference('<%=tits%>',"+no+");>"+ no + "</a>"+"&nbsp;&nbsp;";
	  }
  }else{
    for(var no =1; no< currNo; no++){    //当前页前面的页数
 	   pageNo += "<a href=javascript:findReference('<%=tits%>',"+no+");>"+ no + "</a>"+"&nbsp;&nbsp;";
 	  }
  }
  if(totalNo >1){
   pageNo += "<a href=\"javascript:findReference('<%=tits%>',"+ currNo +");\">"+"【"+ currNo +"】"+ "</a>&nbsp;&nbsp;";
  }
  if(currNo+5 < totalNo){
	  for(var no2 = currNo +1; no2< currNo+5; no2++){
	   pageNo += "<a href=javascript:findReference('<%=tits%>',"+no2+");>"+ no2 + "</a>&nbsp;&nbsp;";
	  }
  }else{
    for(var no2 = currNo +1; no2<= totalNo; no2++){
 	   pageNo += "<a href=javascript:findReference('<%=tits%>',"+no2+");>"+ no2 + "</a>&nbsp;&nbsp;";
 	  }
  }
  if(currNo < totalNo){
    pageNo += "<a href=javascript:findReference('<%=tits%>',"+(currNo+1)+");>下一页</a>&nbsp;&nbsp;";
		pageNo += "<a href=javascript:findReference('<%=tits%>',"+ totalNo + ");>末页</a>";
  }
  $('pagebar').innerHTML = pageNo;
}


function splitStr(str){
  var arr = trim(str);
  var reg = /\s{2,}/g; //把多个空格转换为一个空格
  var newStr = arr.replace(reg," "); 
  return newStr;
} 
--></script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href = "<%=contextPath%>/core/styles/oaknow/css/wiki.css">

</head>
<body topmargin="5" onload="doInit();">
<div class="bodydiv">
	<br /><div class="askbody">
		 <%@ include file="head.jsp" %> 
</div>
<div class="goonbody">

<div class="navbar">
	<a href="<%=contextPath%>/yh/core/oaknow/act/YHOAKnowAct/OAKnowIndex.act" style="font-size:11px;">${oaName}</a>&nbsp;&raquo;&nbsp;<span style="font-size:11px;">提问</span>
</div>
<div class="tt">1、与您的提问" ${asktitle} "相关的已解决问题：</div>

<div class="tb" >
  <div id="showarea">	
	</div>
  <div class="pagebar" id="pagebar" >
  </div>
</div>
<div class="tt">2、如果您没有找到合适的回答，请继续提问：</div>
<div class="tb">
	<form name="form1"  id="form1">
	<table class="atb">

		<tr>
		  <td>您的提问：</td><td><input type="text" name="title" id="title" size="30" value="${asktitle}" class="askincss2" /></td>
		</tr>
		<tr>
	    <td>标签：</td>
	    <td>
        <input type="text" size="30" name="keyword" id="keyword" value="${askkey}" onfocus="this.select();" class="askincss2" />
        (说明：用空格隔开多个标签，最多可填写 5 个，标签用于问题关联)
	    </td>
    </tr>
		<tr>
	    <td>问题描述：</td><td><textarea name="content" id="content" rows="6" cols="60">${ask.askComment}</textarea>(说明：问题说明越详细，回答也会越准确！)</td>
    </tr>
		<tr>
	    <td>问题分类：</td>

	    <td>
      	<input type="hidden" id="typeId" name="typeId" value="">
    		<table border="0" cellspacing="0" cellpadding="0">
          <tr>
          	<td>
          	  <SELECT id=TypeLevel1 style="WIDTH: 100px" size=13 name=TypeLevel1 onchange="changeType1()">          		  
          	  </SELECT>
          	</td>
            <td width="20" style="height: 100px">
            	<div style="height:100%;padding-top:80px;">&nbsp;<B>→</B></div>
            </td>
            <td>
            	<SELECT onChange="changeType2();" id=TypeLevel2 style="WIDTH: 100px" size=13 name=TypeLevel2>
            		<option value="-1" selected>请选择</option>
            	</SELECT>
            </td>
          </tr>
        </table>
	    </td>
	  </tr>
    <tr><td></td><td><input type="button" value="提交" class="BigButton"  onclick="dosubmit();"/></td></tr>
  </table>
  </form>
</div>
</div>
</div>
</body>
</html>
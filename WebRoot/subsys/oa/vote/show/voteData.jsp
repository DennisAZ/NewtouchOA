<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*,java.text.SimpleDateFormat"%>
<%@include file="/core/inc/header.jsp" %>
<%
  String voteId = request.getParameter("voteId")==null ? "" : request.getParameter("voteId");
  String itemId = request.getParameter("itemId")==null ? "" : request.getParameter("itemId");
  String fieldName = request.getParameter("fieldName")==null ? "" : request.getParameter("fieldName");
  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
  String curDateStr = sf.format(new Date());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>投票数据</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript">

function doInit(){
  var seqId = '<%=voteId%>';
  var itemId = '<%=itemId%>';
  var fieldName = '<%=fieldName%>';
 getVoteById(seqId);
}

function parseStr(str,itemId){
  var count = 0;
  var newStr = str;
  for(var i = 0;i<str.length-5;i++){
    if(str.substr(i,6)=='{text}'){     
      count++;
      newStr = newStr.replace("{text}","&nbsp;<u>详情</u>");
    }
    if(str.substr(i,8)=='{number}'){
      count++;
      newStr = newStr.replace("{number}","&nbsp;<u >详情</u>");
    }
    if(str.substr(i,10)=='{textarea}'){
      count++;
      newStr = newStr.replace("{textarea}","&nbsp;<u >详情</u>");
    }
  }
  var newStrArray = new Array();
  newStrArray[0] = newStr;
  newStrArray[1] = count;
  return newStrArray;
}

function CheckparseStr(str){
  var b = false;
  for(var i = 0;i<str.length-5;i++){
    if(str.substr(i,6)=='{text}'){     
      return true;
    }
    if(str.substr(i,8)=='{number}'){
      return true;
    }
    
    if(str.substr(i,10)=='{textarea}'){
      return true;
    }
  }
  return b;
}
function getVoteById(seqId){
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/selectVoteById2.act?seqId="+seqId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  var userId = rtJson.rtMsrg;
  var voteIds = "";//所有投票ID包括子投票
  var checkboxVoteIds = "";//所有投票类型是多选的（type=1）取得vote_id字符串
  var checkboxMinNum = "";//所有投票类型是多选的（type=1）取得minNum字符串
  if(prc.seqId){
     seqId = prc.seqId;
     var type = prc.type;
    //投票以及投票项目
     var table = new Element('table',{"class":"TableBlock","width":"100%" ,"align":"center"}).update("<tbody id='tbody'>"
         +"<tr ><td align='center'>"+prc.subject+"</td></tr></tbody>");
     $("itemDiv").appendChild(table);
     var json =  getVoteItem(seqId);
     var itemList = json.rtData;
     for(var i =0 ;i<itemList.length;i++){
       var item = itemList[i];
       if((type=='0' || type =='1')&& CheckparseStr(item.itemName)){
         var tr = new Element('tr',{"class":"TableHeader"});
         $("tbody").appendChild(tr);
         var parseStrArray = parseStr(item.itemName,item.seqId);
         tdStr = "<label for=''>" +parseStrArray[0]+"</label><br>"  ;
         tr.update("<td align='center'>"+tdStr+"</td>" );

         var tr = new Element('tr',{});
         $("tbody").appendChild(tr);
         var dataCount = parseStrArray[1];
         var dataStr = "";
         for(var j = 1;j<dataCount + 1;j++){
           var data = getData(item.seqId,j);
           var fieldData = "";
           if(data.fieldData){
             fieldData = data.fieldData;
           }
           dataStr =dataStr + "<a name='INPUT_ITEM_'><b>【输入项"+j+"】：</b></a><br>";
           dataStr =dataStr+ "<li>" + fieldData + "</li>";
         }
         tr.update("<td >"+dataStr+"</td>" );
      }
    }
    var tr = new Element('tr',{});
    $("tbody").appendChild(tr);
    var buttonStr = "";
    buttonStr = buttonStr + " <input type='button' value='关闭' class='BigButton' onClick='javascript:window.close();'>";
    tr.update("<td align='center'  > "+buttonStr+"</td>");
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>该投票暂不可用!</div></td></tr>"
        );
    $('returnNull').update(table); 
  }
}
function getVoteItem(voteId){
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteItemAct/selectItemByVoteId.act?voteId="+voteId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  //var prcs = rtJson.rtData;
  return rtJson;
}
function getData(itemId,fieldName){
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteDataAct/getDataByItemId.act?itemId="+itemId + "&fieldName="+fieldName;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  return prc;
}
function getItemBySeqId(seqId){
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteItemAct/getItemBySeqId.act?seqId="+seqId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  return prc;
}

</script>
</head>
<body  topmargin="5px" onLoad="doInit()">
<br>
<div id="returnNull">
<div id="itemDiv">
</div>
</div>
</body>
</html>

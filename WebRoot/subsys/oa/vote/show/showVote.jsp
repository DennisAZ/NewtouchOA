<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.*,java.text.SimpleDateFormat"%>
<%@include file="/core/inc/header.jsp" %>
<%
  String seqId = request.getParameter("seqId")==null ? "" : request.getParameter("seqId");
  SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
  String curDateStr = sf.format(new Date());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>已发布的投票</title>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href="<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/orgselect.js" ></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/page.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<script type="text/javascript">
var  selfdefMenu = {
  	office:["downFile","dump","read"], 
    img:["downFile","dump","play"],  
    music:["downFile","dump","play"],  
    video:["downFile","dump","play"], 
    others:["downFile","dump"]
	}

function doOnloadFile(seqId,attr){
  attachMenuSelfUtil(attr,"vote",$('attachmentName').value ,$('attachmentId').value, '','',seqId,selfdefMenu);
}
function doInit(){
  var seqId = '<%=seqId%>';
  getVoteById(seqId);
}

function parseStr(str,voteId,itemId){
  var count = 0;
  var newStr = str;
  for(var i = 0;i<str.length-5;i++){
    if(str.substr(i,6)=='{text}'){     
      count++;
      newStr = newStr.replace("{text}","&nbsp;<a href=\"javascript:voteData("+voteId+","+itemId+","+count+")\" style=\"text-decoration: underline;\">详情</a>");
    }
    if(str.substr(i,8)=='{number}'){
      count++;
      newStr = newStr.replace("{number}","&nbsp;<a href=\"javascript:voteData("+voteId+","+itemId+","+count+")\" style=\"text-decoration: underline;\">详情</a>");
    }
    if(str.substr(i,10)=='{textarea}'){
      count++;
      newStr = newStr.replace("{textarea}","&nbsp;<a href=\"javascript:voteData("+voteId+","+itemId+","+count+")\" style=\"text-decoration: underline;\">详情</a>");
    }
  }
  return newStr;
}
function voteData(voteId,itemId,fieldName){
  URL="<%=contextPath%>/subsys/oa/vote/show/voteData.jsp?itemId="+itemId+"&fieldName="+fieldName+"&voteId="+voteId;
  myleft=(screen.availWidth-300)/2;
  window.open(URL,"vote_data","height=300,width=500,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=250,left="+myleft+",resizable=yes");
   
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
     $("seqId").value = seqId;
     $("subject").innerHTML = prc.subjectMain;
     $("subject2").innerHTML = prc.subjectMain;
     var type = prc.type;

     var maxNum = 0;
     var minNum = 0;
     var typeDesc = "";
     if(type=='0'){
       typeDesc = "radio";
     } else if(type=="1"){
       typeDesc="checkbox";
       minNum = prc.minNum;
       maxNum = prc.maxNum;
       checkboxVoteIds = checkboxVoteIds + seqId + ",";
       checkboxMinNum = checkboxMinNum + minNum + ",";
     }else{
       $typeDesc="text";
     }
     voteIds = voteIds + seqId + ",";
     var seqIdIndex = 1;
    //投票以及投票项目
     var table = new Element('table',{"class":"TableBlock","width":"100%" ,"align":"center"}).update("<tbody id='tbody'>"
         +"<tr class='TableHeader'><td align='center' colspan='4'>"+seqIdIndex + "、"+prc.subject+"</td></tr></tbody>");
     $("itemDiv").appendChild(table);
     var json =  getVoteItem(seqId);
     var itemList = json.rtData;
     var count = json.rtMsrg.split(",");
     var sumCount = count[0];
     if(sumCount=='0'){
       sumCount = "1";
     }
     var maxCount = count[1];
     if(type=='0' || type =='1'){
       for(var i =0 ;i<itemList.length;i++){
         var tr = new Element('tr',{});
         $("tbody").appendChild(tr);
         var item = itemList[i];
         var tdStr = "";
         var headerItem = "";
         if(itemList.length>26){
           headerItem = String.fromCharCode(i%26+65) + (parseInt(((i)/26),10)+1) ;
         }else{
           headerItem =  String.fromCharCode(i+65);;
         }
         var perStr = 200*item.voteCount/sumCount;
         if(perStr == 0){
           perStr = 1;
         }
         tdStr = "<label for=''>"+ headerItem +" 、" + parseStr(item.itemName,seqId,item.seqId)+"</label><br>"  ;
         tr.update("<td>"+tdStr+"</td>"
             +"<td width='200'><div style=\"width:"+perStr+"px;background:url('<%=imgPath%>/vote_bg.gif'); \">&nbsp;</div></td>"
             +" <td>"+(100*item.voteCount/sumCount).toFixed(2)+"%</td>"
              +"<td>"+item.voteCount+"票</td>" );


       }
     }else{
       var tr = new Element('tr',{});
       $("tbody").appendChild(tr);
       tdStr = "" + getData(seqId,0);
       tr.update("<td  colspan='4'>"+tdStr+"</td>");
     }
     //子投票以及子投票项目
    var childTitle = getChiidVoteByParent(seqId);
    for(var i =0 ;i<childTitle.length;i++){//子投票
      var tr = new Element('tr',{ "class":"TableHeader"});
      $("tbody").appendChild(tr);
       var childVote = childTitle[i];
       voteIds = voteIds + childVote.seqId + ",";
       var type = childVote.type;
       var maxNum = 0;
       var minNum = 0;
       var typeDesc = "";
       if(type=='0'){
         typeDesc = "radio";
       } else if(type=="1"){
         typeDesc="checkbox";
         minNum = prc.minNum;
         maxNum = prc.maxNum;
         checkboxVoteIds = checkboxVoteIds + childVote.seqId + ",";
         checkboxMinNum = checkboxMinNum + minNum + ",";
       }else{
         $typeDesc="text";
       }
       seqIdIndex++;
       tr.update("<td align='center'  colspan='4'>"+seqIdIndex+"、"+childVote.subject+"</td>");

       if(type=='0' || type =='1'){
         var json = getVoteItem(childVote.seqId);
         var itemChildList = json.rtData;
         var count = json.rtMsrg.split(",");
         var sumCount = count[0];
         if(sumCount=='0'){
           sumCount = "1";
         }
         var maxCount = count[1];
         for(var j =0 ;j<itemChildList.length;j++){//子投票项目
           var item = itemChildList[j];
           var tr = new Element('tr',{});
           $("tbody").appendChild(tr);
           var tdStr = "";
           var headerItem = "";
           if(itemList.length>26){
             headerItem = String.fromCharCode(j%26+65) + (parseInt(((j)/26),10)+1) ;
           }else{
            headerItem =  String.fromCharCode(j+65);;
           }
           var perStr = 200*item.voteCount/sumCount;
           if(perStr == 0){
             perStr = 1;
           }
           tdStr =  "<label for=''>"+ headerItem +" 、"+  parseStr(item.itemName,childVote.seqId,item.seqId)+"</label><br>"  ;  
           tr.update("<td>"+tdStr+"</td>"
               +"<td ><div style=\"width:"+perStr+"px;background:url('<%=imgPath%>/vote_bg.gif'); \">&nbsp;</div></td>"
               +"<td>"+100*item.voteCount/sumCount+"%</td>"
             +"<td>"+item.voteCount+"票</td>" );
         }
       }else{
         var tr = new Element('tr',{});
         $("tbody").appendChild(tr);
         tdStr = "" + getData(childVote.seqId,0);
         tr.update("<td  colspan='4'>"+tdStr+"</td>");    
      }
    }
    if(prc.attachmentId !=''){
      var tr = new Element('tr',{});
      $("tbody").appendChild(tr);
      tr.update("<td><span id='attr'></span></td>");
      doOnloadFile($("attr"),seqId);
    } 

    var tr = new Element('tr',{});
    $("tbody").appendChild(tr);
    var buttonStr = "";
    buttonStr = buttonStr + "<input type='button' value='打印' class='BigButton' onClick=\"document.execCommand('Print');\">&nbsp;&nbsp";
    buttonStr = buttonStr + " <input type='button' value='关闭' class='BigButton' onClick='javascript:window.close();'>";
    tr.update("<td align='center'  colspan='4'> "+buttonStr+"</td>");
    $("voteIds").value = voteIds;
    $("checkboxVoteIds").value = checkboxVoteIds;
    $("checkboxMinNum").value = checkboxMinNum;
  }else{
    var table = new Element('table',{ "class":"MessageBox" ,"align":"center","width":"340" }).update("<tr>"
        + "<td class='msg info'><div class='content' style='font-size:12pt'>该投票暂不可用!</div></td></tr>"
        );
    $('returnNull').update(table); 
  }
}
function findId(readers,userId){
   var arrayReaders = readers.split(",");
   for(var i= 0; i<arrayReaders.length;i++){
     if(arrayReaders[i]==userId){
        return false;
     }
   }
   return true;
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
  if(rtJson.rtState == "0"){
    var prc = rtJson.rtData;
  	var s = "<ol style='padding-left: 20px;'>";
	if(prc && prc.length > 0){
 	  prc.each(function(e, i) {
 	 	s += "<li>" + e.fieldData + "</li>";
 	  });
 	  return s + "</ol>";
	}
  }
  else {
	alert(rtJson.rtMsrg); 
  }
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
function getChiidVoteByParent(parentId){
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteItemAct/getChiidVoteByParent.act?parentId="+parentId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prcs = rtJson.rtData;
  return prcs;
}
//导出投票数据
function outVote() {
  var seqId = "<%=seqId%>";
  window.location.href = "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/outVote.act?seqId=" + seqId;
}
</script>
</head>
<body  topmargin="5px" onLoad="doInit()">
<br>
<div id="returnNull">
<table border="0" width="100%" cellspacing="0" cellpadding="3" align="center" class="small">
  <tr>
    <td><img src="<%=imgPath %>/vote.gif" align="absmiddle"><span class="big3"> 投票结果 -<span id="subject"></span> <br>
    </td>
    </tr>
  <tr>
    <td class="small1"  id="subject2"></td>
  </tr>
</table>

<form action="" id="form1" name="form1" method="post">
<div id="itemDiv">

 
</div>
 <input name="checkboxVoteIds" id="checkboxVoteIds" type="hidden" value="">
  <input name="checkboxMinNum" id="checkboxMinNum" type="hidden" value="">
 <input name="voteIds" id="voteIds" type="hidden" value="">
  <input name="itemId" id="itemId" type="hidden" value="">
    <input name="anonymity" id="anonymity" type="hidden" value="">
    <input name="seqId" id="seqId" type="hidden" value="">
</form>
<input type="hidden" id="attachmentId" name="attachmentId"></input>
<input type="hidden" id="attachmentName" name="attachmentName"></input>
</div>
</body>
</html>

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
<script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>

<script type="text/javascript">
function CheckForm(){
  //检查投票是否都以填写

   var voteArray=document.form1.voteIds.value.substr(0,document.form1.voteIds.value.length-1).split(",");
   for(i=0; i<voteArray.length;i++){
      var obj=document.getElementsByName("vote"+voteArray[i]);
      if(!obj)
         continue;
      for(j=0;j< obj.length;j++)
      { 
      	 if(obj.item(j).checked)
      	    break;
      }
      if(j==obj.length && obj.length != 0)//去掉没有投票项目的

      {
      	 alert("请选择投票选项！");
         return (false);
      }
   }
   //检查投票是多选项大于等于最小数
   var checkbox_array = document.form1.checkboxVoteIds.value.substr(0,document.form1.checkboxVoteIds.value.length-1).split(",");
   var min_num_array = document.form1.checkboxMinNum.value.substr(0,document.form1.checkboxMinNum.value.length-1).split(",");
     for(i=0; i<checkbox_array.length;i++)
   {
      var checked_count=0;
      var vote_id=checkbox_array[i];
      for (j=0; j< document.getElementsByName("vote"+vote_id).length; j++)
      {
         if(document.getElementsByName("vote"+vote_id).item(j).checked)
             checked_count++;
      }
      if(checked_count< min_num_array[i])//////////////////////////////
      { 
         alert("第"+(i+1)+"个多选子投票 最少要选择"+min_num_array[i]+"项！");
         return (false);
      }
   }
   //检查所有的输入框是否都已填写，以及必须是数字类型的
   for(i=0; i< document.form1.elements.length; i++)
   {
      if(document.form1.elements[i].name.substr(0,10)!="VOTE_DATA_")
         continue;
      
      var name_array=document.form1.elements[i].name.split("_");
      var item = document.getElementById("item"+name_array[2]);
      if(item && (item.type=="radio" || item.type=="checkbox") && !item.checked)
         continue;
      
      if(document.form1.elements[i].value=="")
      {
         alert("所有项目都必填！");
         document.form1.elements[i].focus();
         return false;
      }
      
      if(document.form1.elements[i].number && !isNumber(document.form1.elements[i].value))
      {
         alert("请输入数字！");
         document.form1.elements[i].focus();
         document.form1.elements[i].select();
         return false;
      }
   }
   
  //document.form1.submit();
   //修改投票人Id
   var prc = updateTitleReaders($("seqId").value);
   var isReader = prc.isReader;
   if(isReader=='1'){
     alert("提示：您已经进行过投票");
     return false;
   }
   //更新投票项目数据库

   var anonymity = $("anonymity").value;
   var itemIds = document.form1.itemId.value;
   updateItemUserId(anonymity,itemIds);

   //添加vote_data  voteArray 投票和子投票是文本输入的 type=2
   for(var i= 0; i<voteArray.length;i++){
     if($("VOTE_DATA_"+voteArray[i])){
      // alert($("VOTE_DATA_"+voteArray[i]));
       addData(voteArray[i],0);
     }
   }
   //添加vote_data  voteArray 投票和子投票是单选和多选的的 type=0或者是type=1
   var itemArray=document.form1.itemId.value.substr(0,document.form1.itemId.value.length-1).split(",");
   for(var i= 0; i<itemArray.length;i++){
     for(var j = 1;;j++){
       if($("VOTE_DATA_"+itemArray[i]+"_" + j)){
         addDataToItem(itemArray[i],j,0);
       }else{
         break;
       }
     } 
   }
   window.location.reload();
   opener.location.reload();
}
function updateTitleReaders(seqId){
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/updateReaders.act?seqId="+seqId;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  //isReader判断是否已投票0未投过1为已投过
  return prc;
}
function updateItemUserId(anonymity,itemIds){
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteItemAct/updateUserId.act?seqIds="+itemIds + "&anonymity="+anonymity;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
  var prc = rtJson.rtData;
  //isReader判断是否已投票0未投过1为已投过
  return prc;
}
function addData(voteId,fieldName){
  var fieldData = $("VOTE_DATA_"+voteId).value;
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteDataAct/addData.act?itemId="+voteId + "&fieldData="+encodeURIComponent(fieldData) + "&fieldName=0";
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
}
function addDataToItem(itemId,count,fieldName){
  var fieldData = $("VOTE_DATA_"+itemId+"_"+count).value;
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteDataAct/addData.act?itemId="+itemId + "&fieldData="+encodeURIComponent(fieldData) + "&fieldName="+count;
  var rtJson = getJsonRs(url); 
  if(rtJson.rtState == "1"){
	alert(rtJson.rtMsrg); 
	return ; 
  }
}
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

function parseStr(str,itemId){
  var count = 0;
  var newStr = str;
  for(var i = 0;i<str.length-5;i++){
    if(str.substr(i,6)=='{text}'){     
      count++;
      newStr = newStr.replace("{text}","<input name='VOTE_DATA_"+itemId+"_"+count+"' id='VOTE_DATA_"+itemId+"_"+count+"' type=text size=20 class=SmallInput>");
    }
    if(str.substr(i,8)=='{number}'){
      count++;
      newStr = newStr.replace("{number}","<input name='VOTE_DATA_"+itemId+"_"+count+"' id='VOTE_DATA_"+itemId+"_"+count+"' type=text size=5 class=SmallInput number=true>");
    }
    if(str.substr(i,10)=='{textarea}'){
      count++;
      newStr = newStr.replace("{textarea}","<textarea name='VOTE_DATA_"+itemId+"_"+count+"' id='VOTE_DATA_"+itemId+"_"+count+"' cols=45 rows=5 class=SmallInput></textarea>");
    }
  }
  return newStr;
}
function getVoteById(seqId){
  var url= "<%=contextPath%>/yh/subsys/oa/vote/act/YHVoteTitleAct/selectVoteById.act?seqId="+seqId;
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
     $("subject").setStyle(prc.subjectFont);
     $("content").innerHTML = prc.content;
     $("fromName").innerHTML = prc.fromName;
     $("fromName").title = prc.deptName;
     $("anonymity").value = prc.anonymity;
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
     if(prc.endDate!=''){
       $("date").innerHTML = "有效日期：" + prc.beginDate.substr(0,10) + "至" + prc.endDate.substr(0,10);
     }else{
       $("date").innerHTML = "发布日期：" +   prc.beginDate.substr(0,10);
     }

     voteIds = voteIds + seqId + ",";
     var seqIdIndex = 1;
    //投票以及投票项目
     var table = new Element('table',{"class":"TableBlock","width":"100%" ,"align":"center"}).update("<tbody id='tbody'>"
         +"<tr class='TableHeader'><td>"+seqIdIndex + "、"+prc.subject+"</td></tr></tbody>");
     $("itemDiv").appendChild(table);
     var itemList = getVoteItem(seqId);
     var tr = new Element('tr',{});
     $("tbody").appendChild(tr);
     var tdStr = "";
     if(type=='0' || type =='1'){
       for(var i =0 ;i<itemList.length;i++){
         var item = itemList[i];
         var headerItem = "";
         if(itemList.length>26){
           headerItem = String.fromCharCode(i%26+65) + (parseInt(((i)/26),10)+1) ;
         }else{
           headerItem =  String.fromCharCode(i+65);;
         }
         tdStr = tdStr + " <input name='vote"+seqId+"' id='item"+item.seqId+"' type='"+typeDesc+"'  value='"+item.seqId+"'  onclick='AddValue("+seqId+","+item.seqId+","+maxNum+")'><label for=''>"+ headerItem +" 、" + parseStr(item.itemName,item.seqId)+"</label><br>"  ;
       }
     }else{
       tdStr = "<textarea name='VOTE_DATA_"+seqId+"' id='VOTE_DATA_"+seqId+"' cols='45' rows='5'></textarea> ";
     }

     tr.update("<td>"+tdStr+"</td>");
     //子投票以及子投票项目
    var childTitle = getChiidVoteByParent(seqId);
    for(var i =0 ;i<childTitle.length;i++){//子投票

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
         minNum = childVote.minNum;
         maxNum = childVote.maxNum;
         checkboxVoteIds = checkboxVoteIds + childVote.seqId + ",";
         checkboxMinNum = checkboxMinNum + minNum + ",";
       }else{
         $typeDesc="text";
       }
       seqIdIndex++;
       var tr = new Element('tr',{"class":"TableHeader"});
       $("tbody").appendChild(tr);
       tr.update("<td>"+seqIdIndex+"、"+childVote.subject+"</td>");
       var itemChildList = getVoteItem(childVote.seqId);

       var tr = new Element('tr',{});
       $("tbody").appendChild(tr);
       var tdStr = "";
       if(type=='0' || type =='1'){
         for(var j =0 ;j<itemChildList.length;j++){//子投票项目

           var item = itemChildList[j];
           var headerItem = "";
           if(itemList.length>26){
             headerItem = String.fromCharCode(j%26+65) + (parseInt(((j)/26),10)+1) ;
           }else{
             headerItem =  String.fromCharCode(j+65);;
           }
           tdStr =  tdStr + " <input name='vote"+childVote.seqId+"' id='item"+item.seqId+"' value='"+item.seqId+"' onclick='AddValue("+childVote.seqId+","+item.seqId+","+maxNum+")' type='"+typeDesc+"' value='"+headerItem+"'><label for=''>"+ headerItem +" 、"+  parseStr(item.itemName,item.seqId)+"</label><br>"  ;  
         }
       }else{
         tdStr = "<textarea name='VOTE_DATA_"+childVote.seqId+"' id='VOTE_DATA_"+childVote.seqId+"'  cols='45' rows='5'></textarea> ";
     
       }
       tr.update("<td>"+tdStr+"</td>");
    }
    //添加附件
    if(prc.attachmentId !=''){
      var tr = new Element('tr',{});
      $("tbody").appendChild(tr);
      tr.update("<td>附件文件：<br><span id='attr'></span>"
            +"<input type='hidden' name='attachmentId' id='attachmentId' value='"+prc.attachmentId+"'>"
            +"<input type='hidden' name='attachmentName' id='attachmentName' value='"+prc.attachmentName+"'>"
            +"</td>");
      doOnloadFile(seqId,$("attr"));
    } 
    var tr = new Element('tr',{});
    $("tbody").appendChild(tr);
    var buttonStr = "";
    if(findId(prc.readers,userId)&&(prc.endDate=="" || prc.endDate.substr(0,10) > '<%=curDateStr%>')){
      buttonStr = buttonStr + "<input type='button' value='投票' class='BigButton' onClick='CheckForm();'>&nbsp;&nbsp;";
    }

    if(prc.viewPriv != '2'){
      if(prc.viewPriv == '0' && findId(prc.readers,userId)){
        buttonStr = buttonStr + "<input type='button' value='查看结果' class='BigButton' onClick=\"alert('投票后才能查看投票结果');\">&nbsp;&nbsp";
      }else{
        buttonStr = buttonStr + "<input type='button' value='查看结果' class='BigButton' onClick=\"showVote("+seqId+");\">&nbsp;&nbsp"; 
      }
    }
    buttonStr = buttonStr + " <input type='button' value='关闭' class='BigButton' onClick='javascript:window.close();'>";
    tr.update("<td align='center'> "+buttonStr+"</td>");
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
function showVote(seqId){
  var URL="<%=contextPath%>/subsys/oa/vote/show/showVote.jsp?seqId="+seqId;
  myleft=(screen.availWidth-780)/2;
  window.open(URL,"read_vote","height=500,width=780,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=150,left="+myleft+",resizable=yes");
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
  var prcs = rtJson.rtData;
  return prcs;
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

function AddValue(vote_id,item_id,max_num){
   var item_id_str= document.form1.itemId.value;

   if($("item"+item_id).type =="checkbox" && max_num>0){//判断是否超过最大数
      checked_count=0;
      for (i=0; i< document.getElementsByName("vote"+vote_id).length; i++){
         if(document.getElementsByName("vote"+vote_id).item(i).checked)
            checked_count++;
         if(checked_count>max_num){
            alert("最多只能选择"+max_num+"项！");
            $("item"+item_id).checked=false;
            return;
         }
      }
  }else if($("item"+item_id).type=="radio") {
     for (i=0; i< document.getElementsByName("vote"+vote_id).length; i++){
         var radio_id = document.getElementsByName("vote"+vote_id).item(i).value;
         //alert(radio_id);
         if(item_id_str.indexOf(radio_id+",")==0)
   	        item_id_str=item_id_str.replace(radio_id+",","");
         else if(item_id_str.indexOf(","+radio_id+",")>0)
   	        item_id_str=item_id_str.replace(","+radio_id+",",",");
     }
  }
   
   if(item_id_str.indexOf(item_id+",")==0)
   	 item_id_str=item_id_str.replace(item_id+",","");
   else if(item_id_str.indexOf(","+item_id+",")>0)
   	 item_id_str=item_id_str.replace(","+item_id+",",",");
   else
      item_id_str+=item_id+",";
  // alert(item_id_str+"------------------");
  document.form1.itemId.value=item_id_str;
}
</script>
</head>
<body  topmargin="5px" onLoad="doInit()">
<br>
<div id="returnNull">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big3" align="center"><span id="subject" style=""></span></td>
  </tr>
  <tr>
    <td class="small1" id="content"></td>
  </tr>
  <tr>
    <td align="right" class="small1">发布人：<u title="" style="cursor:pointer" id="fromName"></u>
    
    <span id="date"></span>
</td>
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

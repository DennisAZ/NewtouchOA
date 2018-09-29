
function getVMeetPriv(){
    var url = contextPath + "/yh/subsys/oa/vmeet/act/YHVMeetAct/getVMeetPriv.act";
    var rtJson = getJsonRs(url);
    if (rtJson.rtState == "0") {
       var data=rtJson.rtData;
       $("TO_ID2").value=data.id;
 
       $("TO_NAME2").value=data.name;
       var priv=data.userPriv;
       if(priv!="1"){
         $("addpriv").style.display="none";
         $("setPrivTitle").style.display="none";
           }
       var hasPriv=data.hasPriv;
       if(hasPriv!="1"){
           $("newVMtitle").style.display="none";
           $("newVM").style.display="none";
        }else{
            $("smsContent").innerHTML="请参加["+data.curUser+"]主持的视频会议，点击短信链接进入。";
           }
       
    } else {
      alert(rtJson.rtMsrg); 
    }


    }

 


function CheckForm()
{
   if(document.form1.TO_ID.value=="")
   { alert("请添加参会人员！");
     return (false);
   }

   if(document.form1.CONTENT.value=="")
   { alert("会议邀请短信内容不能为空！");
     return (false);
   }

   return (true);
}



function clearUser(id){
	if(id==2){
		 $("TO_ID2").value="";
		 $("TO_NAME2").value="";
	}else if(id==1){
	     $("TO_ID").value="";
	     $("TO_NAME").value="";
		}
}

function savePriv(){
   var toId= $("TO_ID2").value;

   var toName=$("TO_NAME2").value;
   var url = contextPath + "/yh/subsys/oa/vmeet/act/YHVMeetAct/setVMeetPriv.act";
   var rtJsons = getJsonRs(url,"toId="+toId);
   if (rtJsons.rtState == "0") {
    
     alert("新建权限已经修改！");
     window.location.reload();
   } else {
     alert(rtJsons.rtMsrg); 
   }
	
}

function saveVMeet(){
	   var toId= $("TO_ID").value;
	   var content=$("smsContent").value;
	   if(toId=="" || content=="" ){
		   alert("请填写完整新建会议信息！");
		   return false;
	   }
	   var url = contextPath + "/yh/subsys/oa/vmeet/act/YHVMeetAct/addVMeetInfo.act";
	   var rtJsons = getJsonRs(url,"toId="+toId+"&content="+content);
	   if (rtJsons.rtState == "0") {
	    
	     alert("新建视频会议成功！");
	     window.location.reload();
	   } else {
	     alert(rtJsons.rtMsrg); 
	   }
	   
	}

function getBeginVM(){
	  var url = contextPath + "/yh/subsys/oa/vmeet/act/YHVMeetAct/getLastBeginMeetAct.act"; 
	  var rtJsons = getJsonRs(url);
	     if (rtJsons.rtState == "0") {
	      var data=rtJsons.rtData;
	      for(var i=0;i<data.length;i++){
  	      var tr = new Element('tr', {
  		        "class" : "TableData"
  		      });
          var attend="<a href=\"javascript:attendVM('"+data[i].seqId+"');\"> 参加会议 </a>";
          var delet="<a href=\"javascript:delVM('"+data[i].seqId+"');\"> 删除会议 </a>";
  	      $('beginVMTbody').appendChild(tr);
  	      var td = new Element("td");
  	      td.align = 'center';
  	      tr.appendChild(td);
  	      td.update(data[i].addTime);
  	      var td2 = new Element("td");
  	      tr.appendChild(td2);
  	      td2.align = 'center';
          td2.update(data[i].content);
          var td3 = new Element("td");
          tr.appendChild(td3);
          td3.align = 'center';
          td3.update(attend+delet);
          
	     }
	     if(data.length<=0){
	    	 $("beginVM").style.display="none";
	    	 $("showBeginData").style.display="block";
	     }
	     } else {
	       alert(rtJsons.rtMsrg); 
	     }

}
	
function attendVM(seqId){
	var url = "checkUser.jsp?seqId="+seqId;
	window.open(url);
}


function delVM(seqId){
	if(window.confirm("确定删除会议?")){
	  var url = contextPath + "/yh/subsys/oa/vmeet/act/YHVMeetAct/deleteVMeetAct.act";
	   var rtJsons = getJsonRs(url,"seqId="+seqId);
	   if (rtJsons.rtState == "0") {

	     window.location.reload();
	   } else {
	     alert(rtJsons.rtMsrg); 
	   }
	}
}


function getInvitedVM(){
	  var url = contextPath + "/yh/subsys/oa/vmeet/act/YHVMeetAct/getLastInvitedMeetAct.act"; 
	  var rtJsons = getJsonRs(url);
	     if (rtJsons.rtState == "0") {
	      var data=rtJsons.rtData;
	      for(var i=0;i<data.length;i++){
	      var tr = new Element('tr', {
		        "class" : "TableData"
		      });
          var attend="<a href=\"javascript:attendVM(\'"+data[i].seqId+"\');\">参加会议</a>";
       
	      $('invitedVMTbody').appendChild(tr);
        var td = new Element("td");
        td.align = 'center';
        tr.appendChild(td);
        td.update(data[i].addTime);
        var td2 = new Element("td");
        tr.appendChild(td2);
        td2.align = 'center';
        td2.update(data[i].content);
        var td3 = new Element("td");
        tr.appendChild(td3);
        td3.align = 'center';
        td3.update(attend);
	      }
	     if(data.length<=0){
	    	 $("invitedVM").style.display="none";
	    	 $("showinvitedData").style.display="block";
	     }
	     } else {
	       alert(rtJsons.rtMsrg); 
	     }

}
	


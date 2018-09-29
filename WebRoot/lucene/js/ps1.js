 function search(index,size){
	 if(index<0 || index==""){
		 index=0;
	 }
	 if(size==""){
		 size=10;
	 }


	  
       str+=",jionstr";
		  var content=$("search_content").value;
	   if(content==""){
		      alert("查询内容不能为空！");
		      return null;
	   }

	 var urls =contextPath+"/yh/lucene/act/YHPersonServiceAct/getLucenceAct.act";
    var param="content="+content+"&size="+size+"&index="+index+"&stationId="+stationId;
	  var rtJsons = getJsonRs(urls,param);
	    var text="";
	    if(rtJsons.rtState == '0'){
	       var data=rtJsons.rtData.data[0].data;
	       for(var i=0;i<data.length;i++){
            text+=" <div id=\"bookmark\"> ";
	    	text+="   <div class=\"box-up\"></div>";
	    	text+="    <div class=\"box-center\">";
	    	text+="   ";
	    	text+="   <input type=\"checkbox\" name=\"jionstr\" value=\"\" />    <a href=\"javascript:openMessage('"+data[i].url+"')\"><strong>"+data[i].title+"</strong></a>";
	    	text+="      <p>";
            text+=data[i].content;
	      	text+="      </p>";
	    	text+="      <div id=\"links_time\" align=\"left\" >";
	    	text+=" <span id=\"url\">URL:"+data[i].url+"</span><span id=\"time\">&nbsp;&nbsp;-----------"+data[i].time+"</span>";
	    	text+="      </div>";
	    	text+="    </div>";
	    	text+="    <div class=\"box-down\"></div>" ;
	    	text+="		</div>";
	    	   
	       }
           var ff="0";
	       if(data.length<=0){ff="1";
	    	   text="<div align=\"center\">" +
	    	   		"<br/>" +
	    	   		"<font color=\"red\">" +
	    	   		"《很抱歉，没有查询的到相应的信息！》" +
	    	   		"</font>" +
	    	   		"<br/>" +
	    	   		"</div";
	    	//   $("pageRun").style.display="none";
	    	   $("quanx").style.display="none";
	       }
	      $("result").innerHTML=text;
	
	      data=rtJsons.rtData.data[0];
	    	var page="";
	    	var e=data.length-1;
	    	var beforeIndex=parseInt(data.pageIndex)-1;
	    	var nextIndex=parseInt(data.pageIndex)+1;
	        
	    	var lastIndex=parseInt(data.pageCount);
	    	lastIndex--;
	    	if(nextIndex>lastIndex){
	    		nextIndex=lastIndex;
	    	}
	    	var nowIndex=parseInt(data.pageIndex)+1;
	    	page="   <span>共 "+data.pageCount+" 页</span><span>当前第 "+nowIndex+" 页</span>  " +
			" <span><a href=\"javascript:search(0,10)\">&lt;&lt;首页</a></span> " +
					" <span><a href=\"javascript:search("+beforeIndex+",10)\">上一页</a></span>   " +
							" <span><a href=\"javascript:search("+nextIndex+",10)\">下一页</a></span>     " +
									"<span><a href=\"javascript:search("+lastIndex+",10)\">末页&gt;&gt;</a></span>" +
									"<span> 跳到<input type='text' size=1 onchange=\" search(this.value-1,10);\"/> </span>页  " ;
                
	    	$("page").innerHTML=page;
	
	    	$("page").style.display="";  
	    	$("quanx").style.display="";      
	        $("search_num").innerHTML=data.totalCount;
	        if(ff=="1"){
	        	//  $("totalNum").innerHTML="";
	        	  $("quanx").style.display="none";
	        	  $("page").style.display="none";  
	        }
	    }else{
	       alert(rtJsons.rtMsrg);
		    }
	    
	 }
 
 function openMessage(url){
	 window.open(url);
 }
 
 
function jionresult(){
	 var str="";
		var obj = document.getElementsByName("jionstr"); 

	   for(var i=0;i<obj.length;i++){
	      if(obj[i].checked==true){
	         str+=obj[i].value;
	         str+=",";
	        }
		   }
	   if(str==""){
		      alert("请选择查询结果！");
		      return null;
			   }
     //alert(str);
	   var urls =contextPath+"/yh/show/PersonService/act/YHPersonServiceAct/getJionResultAct.act";

	   $("str").value=str;
	   $("form1").action=urls;
	   $("form1").submit();
	

}
 

function selectAll(){
	 var str="";
	 var cb=document.getElementsByName("all"); 
	 if(cb[0].checked==true){
		//全部选中
		 var obj = document.getElementsByName("jionstr"); 
		   for(var i=0;i<obj.length;i++){
		      obj[i].checked=true; 
		   }
	 }else{ 
		 //全部不选中
		 var obj = document.getElementsByName("jionstr"); 
		   for(var i=0;i<obj.length;i++){
		      obj[i].checked=false;
		   }
		 
	 }
	 
	 return;
	 
	 
	 
	   var obj = document.getElementsByName("jionstr"); 
	   for(var i=0;i<obj.length;i++){
	      if(obj[i].checked==true){
	         str+=obj[i].value;
	         str+=",";
	        }
		   }
	   if(str==""){
		      alert("请选择查询结果！");
		      return null;
			   }
    //alert(str);
	   var urls =contextPath+"/yh/show/PersonService/act/YHPersonServiceAct/getJionResultAct.act";

	   $("str").value=str;
	   $("form1").action=urls;
	   $("form1").submit();
	

}


 
 
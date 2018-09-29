<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<%
String useRealData = YHSysProps.getString("useSignFileService");
%>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<link href="css/css.css" rel="stylesheet" type="text/css" />
<link href="css/css-Preview.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/touchGraph.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/keymap.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/JavaScript">

function MM_findObj(n, d) { //v4.01
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
  if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function MM_showHideLayers() { //v6.0
  var i,p,v,obj,args=MM_showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=MM_findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
    obj.visibility=v; }
}

var url = contextPath + "/yh/subsys/inforesouce/act/YHTouchGraphAct/getArray.act?id=";
function clickFunTest(event, id, childWindow) {
  touchGraphUrl = url + id;
  if (event.ctrlKey) {
    var ids = childWindow.selectedNode;
    if (!findIsIn(ids , id)) {
      ids += id + ",";
      childWindow.selectedNode = ids;
    }
  } else {
    childWindow.location.reload();
  }
}
function dbClickFunTest(event, id, childWindow) {
 // touchGraphUrl = url + id;
 // childWindow.location.reload();
  if (event.ctrlKey) {
    var ids = childWindow.selectedNode;
    if (!findIsIn(ids , id)) {
      ids += id + ",";
      //childWindow.selectedNode = ids;
    }
    touchGraphUrl = url + ids;
    childWindow.location.reload();
  }
}
function cDbClickFunTest(event, id, childWindow) {
  
}
function cClickFunTest(event, id, childWindow) {
  if (event.ctrlKey) {
    var ids = childWindow.selectedNode;
    if (!findIsIn(ids , id)) {
      ids += id + ",";
      childWindow.selectedNode = ids;
    }
  }
}
/**
 * 查看后缀ext是否在exts中


 */
function findIsIn(exts , ext){
  for(var i = 0 ;i < exts.length ; i++){
    var tmp = exts[i];
    if(tmp == ext){
      return true;
    }
  }
  return false;
}
</script>
	<style>
	#txt-a {
	  word-wrap: break-word;
	}
	.font1{
	  color:#908743;
	}
	.font1 .a{
	 color:#blue;
	 background:#32CD99;
	}
	
	.font11 {
	 color:#4523RR;
	}
	.font11 .a{
	 color:#4523RR;
	}
	.font2{
	color:#456412;
	}
	#font2 .a{
	color:#green;
	background:#426F42;
	}
	.font3{
	color:red;
	}
	.font3 .a{
	color:red;
	}
	.font4{
	color:112200;
	}
	.font4 .span{
	color:112200;
	}
	.font12{
	color:555666;
	}
	.font5{
	color:Fuchsia;
	}
	.font5 .span{
	color:Fuchsia;
	}
	
	.font6{
	color:Aqua;
	}
	.font6 .span{
	color:Aqua
	}
	
	.font13{
	color:#005611;
	}
	.font7{
	color:Lime;
	}
	.font7 .span{
	color:Lime;
	background:#9370DB;
	}
	.font8{
	color:Olive;
	}
	.font8 .span{
	color:#Olive;
	background:#4D4DFF;
	}
	.font14{
	color:#990099;
	}
	.fonyh{
	font-family:华文彩云；	}
	.fonyh .span{
	color:#Navy;
	background:#FF6EC7;
	}
	.font10{
	
	font-variant:normal|small-caps； 
	}
	.font10 .span{
	color:Yellow;
	}
	.font15{
	style:color#red;
	font-variant:normal|small-caps； 
	}
	#anniu{
	position:relative;
	margin-top:250px;
	margin-left:5px;
	
	}
	</style>


<script type="text/JavaScript"><!--
	$("document").ready(function(){	
		 ajax();
	});
	
	/**
	 *  获得tag图中第一级tag
	*/
	function getFirstLiveTag(){			
		var url = signFileServiceUrl + "/GetFirstLevelTagList?jsoncallback=?";	
		$.getJSON(url, function(data){
			
		});
	}

	
/**
 * 返回第一层tag
 */
	function ajax(){
	  $.ajax({
			url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/firstLevelTag.act",
		  type:"post",			
			success:function(data){
			//alert(data);
	 		var temp = eval("("+data+")");
	 		insertData(temp);
			}
	  });	 
	}

	 function clickTag(){
	   window.parent.clear();
	   window.parent.setLeng();
	   ajax();
	 }
	 
/**
 * 插入tag云图
 */
	function insertData(data){
		if(data){
				if(data.rtState == '0'){
					$("#txt-a").empty();
					var rtdata = data.rtData;
				  //alert(rtdata)
					var cont = "";	
					var contname = "";
					if(rtdata.length > 0){	
            var arr = new Array();
            for(var j=0; j<rtdata.length; j++){
             var intnums = rtdata[j].nTime;
             if(!intnums){
    				    intnums =  rtdata[j].nTimes;  
      				}    				 
    				  var number = parseInt(intnums);
    				  arr.push(number);
            }
            arr = arr.sort(sortNumber); //sortNumber 是排序的一张写法 	
            		
            var count=13; 
  				  var original=new Array;//原始数组 
  				  for (var i=0;i<count;i++){ 
  				      original[i]=i+1; 
  				  } 
  				  original.sort(function(){ 
  				  return 0.5 - Math.random();//随机排序 的一种
  				  }); 				
  				for(var i=0; i<rtdata.length && i<13; i++){  				
  				  contname = rtdata[i].Keyword; 
  				  var intnum = rtdata[i].nTime;
  				  if( !intnum ){
  				    intnum =  rtdata[i].nTimes;  
    				 }  				 
  				  var number = parseInt(intnum); 
            
  					cont += " <a class=\"font"+original[i]+"\" onclick = keyword(";
						cont += rtdata[i].KeyID;
						cont += ",";
						cont += "'"+rtdata[i].Keyword+"'";
						cont +=	") style=\"cursor:pointer;\">";
					
						/*if(parseInt(rtdata[i].nTime) < 5){
							cont += "<font size=\""+(Math.ceil(rtdata[i].nTime/1.5))+"\">"+rtdata[i].Keyword+"</font>";
						}else{
						cont += "<font size=\""+(Math.ceil(rtdata[i].nTime/3))+"\">"+rtdata[i].Keyword+"</font>";
						}*/
						
						cont += "<font size=\""+returnFont(arr, intnum)+"\">"+rtdata[i].Keyword+"</font>";
						cont += "</a>";						
      		}
				}else{
           cont += "<div style=\"margin-top:-5px;\"><font color=\"#001199\">暂</font><font color=\"#00fffa\">无</font><font color=\"#22FF22\">TAG</font><font color=\"#00fffa\">云</font><font color=\"#22FF22\">图</font><font color=\"#FF2222\">标</font>签<div>";
				}
					//cont += "<div id=\"anniu\"><a type=\"button\" style=\"cursor:pointer;color:red\" onclick=\"Back()\">返回</a><div>";
					$("#txt-a").append(cont);
			}
		}
	}
	
	/*
	function Back(){
    var keyID = "";
    var keyWord = "";
    window.parent.setLaye(keyID,keyWord);
  }
  */
  
	/**
      点击 最热主题词  所返回主题词内容	**/
  function keyword(keyId,keyword){
     var len = window.parent.getLen();//文档列表中的长度
     if(len==0){
       alert("没有相关文档列表");
       return false;
      }
    var leng = window.parent.getLeng();
    
    if(leng == 6){
      alert("最大层次为六层");
      return false;
     }
     rowTable(keyId,keyword); //content.jsp页面显示级别关系如：科技>>教育>>文化
     keyWordColTable(keyId,keyword);				
     $.ajax({
     url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/Keyword.act?keyID=" + keyId,    
     type:"post",
     success:function(data){
       var temp = eval("("+data+")");
       //alert(temp);
       insertData(temp);
     }
   }); 
  }
  /**
          返回与指定主题词相关的文档列表
  **/
   function keyWordColTable(keyID,keyWord){ //getArticleListByKeyIDs
     window.parent.setColKeyWord(keyID,keyWord);   
   }
 
  
  /*
       显示tag 云图的层次关系       如：科技>>教育>>文化
  */
	function rowTable(keyID,keyWord){
   var key = keyID;
		if(key){
	    window.parent.setLayer(keyID,keyWord);
		} 
	}

	/**
	 * 点击文件名的时候，显示文件主题词	 */
		function getArticleTags(fileId){
			var userReal = "<%=useRealData%>";
			$("#txt-c").html("");
			if("1" == userReal){
				  $.ajax({
						url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getArticleTags.act?fileId=" + fileId ,
					  type:"post",			  
						success:function(data){
				 			var temp = eval("("+data+")");	
				 			$("#and-c1").show();	
				 			if(temp){ 			
					 			if(temp.rtData.length > 0){
					 			//解析数据 todo
						 			var len = temp.rtData.length;
						 			var keyword = "";
						 			for(var i=0; i<len; i++){
							 			//keyId += temp.rtData[i].KeyID;备用
                    keyword += "<a>"+temp.rtData[i].Keyword +"</a></br>";
								 	}
						 			$("#txt-c").html(keyword);
							 	}else{
			            $("#txt-c").html("暂无主题词!");
								}
				 			}
						}
				  });		 
			}else{
			  var arry = ["提供的接口 知道首页问题","操作菜单","租赁合同  子元素的类型","软件环境 房屋租赁 甲方","乙方"];	   
	      var i = parseInt(Math.random()*(3))
	      $("#txt-c").html(arry[i]);
      }    
		  
		}
		
   /**
   * 根据返回的tagIds重新查找tag云图
   */
		function getTagsByTagIds(tagIds){			
		  $.ajax({
				url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/Keyword.act?keyID=" + tagIds ,
			  type:"post",			  
				success:function(data){
		    var temp = eval("("+data+")");		
		     insertData(temp);
				}
		  });		 
	  }

	/**
	 * 返回fileId的摘要
	*/
	function findZhaiYao(fileId){
	  $.ajax({
			url:contextPath + "/yh/subsys/inforesouce/act/YHFileMetaSaveAct/findAbstract.act?fileId=" + fileId ,
		  type:"post",			  
			success:function(data){
			$("#txt-b").empty();
	    var temp = eval("("+data+")");
	     if(temp.rtState == '0'){
	       $("#txt-b").append(temp.rtData);
		   }
			}
	  });		
  }

	/**
	* 数组进行排序  从小排到大
	*/
	function sortNumber(a, b){
	  return a-b;
	}

	/**
	* 返回改权值下的字体	*/
	function returnFont(arr, ntime){
	  var max = arr[arr.length-1];//最大值，之前已排好序sort
	  var min = arr[0];
	  var MAXFONT = 6;//设置最大字体为4
	  var flag = parseFloat((max - min)/MAXFONT);//
	  for(var i=0; i<MAXFONT; i++){//在循环6 次中总有一次 适合ntime范围
	    if(ntime >= (flag*i) && ntime < (flag*(i+1))){//1-9==1, 2-9==2返回等于后的权值		
		    if(i==0){i=1;}   
		     return (i+1);
		  }
	  }	
	  return MAXFONT; //超出范围就显示 最大权值
	}

	
--></script>
</head>

<body style="overflow-y:hidden;overflow-x:hidden">
  <div id="preview-con">
    <div id="p-Label">
    <div id="and-a"><img src="images/p-a02.gif" onclick="javascript:clickTag();return false;" /></div>
    <div id="and-b"><img src="images/p-b01.gif" onclick="MM_showHideLayers('and-a','','hide','and-b','','hide','and-c','','show','and-a1','','show','and-b1','','show','and-c1','','hide','txt-a','','hide','txt-b','','show','txt-c','','hide')" /></div>
    <div id="and-c"><img src="images/p-c02.gif" onclick="MM_showHideLayers('and-a','','hide','and-b','','show','and-c','','hide','and-a1','','show','and-b1','','hide','and-c1','','show','txt-a','','hide','txt-b','','hide','txt-c','','show')" /></div>
    <div id="and-a1"><img src="images/p-a01.gif" onclick="MM_showHideLayers('and-a','','show','and-b','','show','and-c','','show','and-a1','','hide','and-b1','','hide','and-c1','','hide','txt-a','','show','txt-b','','hide','txt-c','','hide')" /></div>
    <div id="and-b1"><img src="images/p-b02.gif" /></div>
    <div id="and-c1"><img src="images/p-c01.gif" /></div>   
    </div> <!--<input type="button" value="点击" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest')"/> -->
       <div id="p-txt">
       <div id="txt-a">
       <!--<div id="font1"><a type="button"  style="color:blue;cursor:pointer"  onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">科技</a></div>
      <div id="font11"><a type="button"  style="color:black;cursor:pointer"  onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">国际组织</a></div>
       <div id="font2"><a type="button" style="color:green;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">示范</a></div>
       <div id="font3"><a type="button" style="color:red;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">创建</a></div>
       <div id="font4"><a type="button" style="color:112200;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">通知</a></div>
       <div id="font12"><a type="button" style="color:#550066;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">管理</a></div>
       <div id="font5"><a type="button" style="color:Fuchsia;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">外交</a></div>
       <div id="font6"><a type="button" style="color:Aqua;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">活动</a></div>
       <div id="font13"><a type="button" style="color:#005611;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">资料</a></div>
       <div id="font7"><a type="button" style="color:Lime;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">物价</a></div>
       <div id="font8"><a type="button" style="color:Olive;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">行文</a></div>
      <div id="font14"><a type="button" style="color:#990099;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">友好交往</a></div>
       <div id="fonyh"><a type="button" style="color:Navy;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">外国专家</a></div>
       <div id="font10"><a type="button" style="color:Yellow;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">礼宾</a></div>
       <div id="font15"><a type="button" style="color:#897634;cursor:pointer" onclick="showTouchGrap(url ,'clickFunTest','cClickFunTest','dbClickFunTest','cDbClickFunTest')">留学生</a></div>
        --></div>
        
        
      <div id="txt-b" id="">平谷赏花每年四月中下旬，平谷都会举办桃花节，您可以漫游桃花海。此外，平谷还有一些景点值得一游，如京城第三大水库———金海湖，以及京东大峡谷、京东大溶洞等。
      行车线路：北京———京顺路———枯柳树环岛右转———平顺快速路———平谷向东17公里到金海湖景区门票：京东...   
</div>
      <div id="txt-c">主题词主题词</div>
   </div>
  </div>
  <div id="test"></div>
</body>
</html>


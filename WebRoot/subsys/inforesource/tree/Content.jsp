<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ include file="/core/inc/header.jsp" %>
    <%@ page import="java.util.List" %>
    <%@ page  import="yh.subsys.inforesouce.data.*"%>
    
 <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/touchGraph.js"></script>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>无标题文档</title>
<style type="text/css">
#copyData{ 
 font-style: normal; 
 font-size: 85px; 
 font-size: 85%; 
 } 
 
</style>
<style>
	.fileTitle{
		width:20px;		
		overflow:hidden;
		text-overflow:ellipsis;
		white-space:nowrap;
    }
    
 #Related-bg a {
		color:#000000;
		display:block;
		float:left;
		font-family:"宋体";
		font-size:13px;
		height:15px;
		margin-left:10px;
		padding-top:10px;
		text-align:center;
		width:150px;
 }
 
 #Related-bg h1 {
		color:#666666;
		display:block;
		float:left;
		font-family:Arial,Helvetica,sans-serif;
		font-size:12px;
		font-weight:lighter;
		height:15px;
		margin-left:14px;
		padding-top:10px;
		text-align:center;
		text-decoration:none;
		width:60px;
}
 #Related-bg h2 {
	font-family: Geneva, Arial, Helvetica, sans-serif;
	font-size: 12px;
	color: #666666;
	font-weight: lighter;
	text-decoration: none;
	display: block;
	height: 15px;
	width: 55px;
	float: left;
	padding-top: 10px;
	margin-left: 13px;
	text-align: center;
}
 #Related-bg h3 {
	font-family: "宋体";
	font-size: 13px;
	color: #000000;
	font-weight: lighter;
	text-decoration: none;
	display: block;
	height: 15px;
	width: 82px;
	float: left;
	padding-top: 10px;
	margin-left: 9px;
	text-align: center;
}
 #Related-bg h4 {
	font-family: "宋体";
	font-size: 12px;
	color: #003300;
	font-weight: lighter;
	text-decoration: none;
	display: block;
	height: 15px;
	width: 78px;
	float: left;
	padding-top: 10px;
	margin-left: 8px;
	text-align: center;
}
 #Related-bg h5 {
	font-family: "宋体";
	font-size: 13px;
	color: #0000FF;
	font-weight: lighter;
	display: block;
	height: 15px;
	width: 60px;
	float: left;
	padding-top: 10px;
	margin-left: 15px;
	text-align: center;
}
	#Related-bg li {
	border-bottom:1px dashed #CCCCCC;
	height:28px;
	}
	#Left{
	  float:left; 
	 }
	#Right{
	 float:right;
	}
</style>
<link href="css/css.css" rel="stylesheet" type="text/css" />
<link href="css/css-content.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="css/jquery.autocomplete.css" />
<link rel="stylesheet" type="text/css" href="css/pager.css" />
<script type="text/javascript" src="js/jquery.js"></script>
<script type='text/javascript' src='js/jquery.bgiframe.min.js'></script>
<script type='text/javascript' src='js/jquery.ajaxQueue.js'></script>
<script type='text/javascript' src='js/thickbox-compressed.js'></script>
<script type='text/javascript' src='js/jquery.autocomplete.js'></script>
<script type='text/javascript' src='js/localdata.js'></script>
<script type='text/javascript' src='js/Page.js'></script>
<script type='text/javascript' src='js/Page2.js'></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/keymap.js"></script>
	<script type="text/JavaScript"><!--
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

	$(document).ready(function(){
	  
	  $("#searchInput").focus().autocomplete(test);
	});
	function MM_swapImgRestore() { //v3.0
	  var i,x,a=document.MM_sr; for(i=0;a&&i<a.length&&(x=a[i])&&x.oSrc;i++) x.src=x.oSrc;
	}
	
	function MM_preloadImages() { //v3.0
	  var d=document; if(d.images){ if(!d.MM_p) d.MM_p=new Array();
	    var i,j=d.MM_p.length,a=MM_preloadImages.arguments; for(i=0; i<a.length; i++)
	    if (a[i].indexOf("#")!=0){ d.MM_p[j]=new Image; d.MM_p[j++].src=a[i];}}
	}
	
	function MM_findObj(n, d) { //v4.01
	  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	  if(!x && d.getElementById) x=d.getElementById(n); return x;
	}
	function MM_swapImage() { //v3.0
	  var i,j=0,x,a=MM_swapImage.arguments; document.MM_sr=new Array; for(i=0;i<(a.length-2);i+=3)
	   if ((x=MM_findObj(a[i]))!=null){document.MM_sr[j++]=x; if(!x.oSrc) x.oSrc=x.src; x.src=a[i+2];}
	}



/**
 * 查找标签
 */
	 function ajax(){
	   var url = contextPath + "/yh/subsys/inforesouce/act/YHMateNodeAct/findTagNameAjax.act";//
	   $.getScript(url, function(data){    
	     loadData(data);
	   });
	 }
   function loadData(data){ 
     var dataObj=eval("("+data+")");//把string转化为json对象     
     var temp ="";
     for(var i=0; i<dataObj.length; i++){ 
       temp += "<span class=\"label-span\"><a href=\"javascript:void(0);\" onclick=\"toSeachByAjax('','"+ dataObj[i]["nodes"] +"')\">"+dataObj[i]["tagName"]+"</a></span>";
     }
     $("#label-down").append(temp);
   } 



function modAjax(modules, treeNode){   
  $("#dataa").html("");
  $(".hiddiv").attr("id","tab-a-con");//把class=hiddiv 的id 值赋值为tab-a-con
  $.ajax({
		url:contextPath + "/yh/subsys/inforesouce/act/YHFileMetaSaveAct/findFileList.act" ,
	  type:"post",
		data:"nodes="+ treeNode +"&modules="+modules,		
		//async:false,                                  //同步问题(false), 异步：(true)
		success:function(data){	
 		  setData(data); 
 		  var data = getData();
 	    var toJson = eval("("+data+")"); 
 	    var len  = toJson.rtData.length;
 	    //alert(len+"****");
 	    setLen(len);
 	    var page = new Pager(len, 1);  	  
 	    setPage(page); 	
 	    pagerNo(1); 	 
 	    createPage(1, page);		   
		}
  });
}


/**
 * 初始加载，默认从第一页开始
 */
$("document").ready(function(){
	  ajax();
    modAjax("","");    

    
});
 
  
/**
 * 选择后调用的方法
 */
function toSeachByAjax(modules, treeNode){ 
  modAjax(modules, treeNode); 
}

/**
 * 把返回的json串保存在这个页面上
 */
var data = ""
function setData(data){
  this.data = data;
}
function getData(){
  return this.data;
}

var page = null;
function setPage(page){
  this.page = page;
}

function getPage(){
  return this.page;
}


/**
 * 点击文件名，显示这个文件相关的主题词
 */
function getArticleTags(fileId){
  //调用父页面的getArticleTags方法
  window.parent.getArticleTags(fileId);
  window.parent.getArticleAbstract(fileId);
}

/**
 * 获得文件的相关文件列表 */
 var len = 0;
 function setLen(len){
   this.len = len;
 }
 function getLen(){
   return this.len;
 }
function getRelationArticleTags(fileId){
  $.ajax({
		url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getRelationArticleList.act?fileId="+ fileId,
	  type:"post",	
		success:function(data){	
    $("#Related-bg").empty();
    $("#page-b").empty();
      var toJson = eval("("+data+")");
      if(toJson){
        var rtdata = toJson.rtData;		
        //var len = toJson.rtData.Rows.length;
        var len = toJson.rtData.length;
        //var totalNo = toJson.rtData.nTotalRec +1;
	      //var currNo = toJson.rtData.nStartPage +1;	       
         if(len >0){
           var temp = "";
	          for(var j=0; j<len; j++){
		          var tName = rtdata[j].FILE_PATH;
		          var subtitle= "";			          
		          var moudle = tName.substring(tName.indexOf("\\attach\\")+8,  tName.length);			          
		          moudle = moudle.substring(0,moudle.indexOf("\\"));			          		         
		          var fName = tName.substring(tName.indexOf(moudle)+39+moudle.length, tName.length);  			         
		          var last = fName.substring(fName.indexOf(".")+1, fName.length); 
		          //var titleName = rtdata[j].TITLE;				      
		          //var size = rtdata[j].FILE_SIZE;	  
		          //var datep = rtdata[j].UPDATE_TIME;
		          //alert(datep);
		          //datep = datep.replaceAll("\/","").replaceAll("Date","").replaceAll("\\(","").replaceAll("\\)","");
		          //var showdate = new Date(parseInt(datep));
		          //var datestr = showdate.getFullYear()+"-"+ (showdate.getMonth()+1) +"-" + showdate.getDate();	
		          
		          var readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
		          readUrl += "&attachmentId="+ rtdata[j].FILE_ID +"&moudle="+ moudle +"&op=5&signKey=&print=";
		          if(fName.length >12){
		            subtitle = fName.substring(0,12)+" ...";
		          }else{
		            subtitle = fName;
		          }
	            temp += "<li style=\"list-style-type:none;\">";
	            //temp += "<input type=\"checkbox\" class=\"con-input\" value=\""+ rtdata[j].SEQ_ID +"\" />";
	            temp += "<span class='fileTitle'><a href=\"javascript:void(0);\" title=\""+ fName +"\"><font size='2px;'>"+ subtitle+"</font></a></span>";
	            temp += "<h1>"+ Math.ceil(89367/1024) +"kb</h1>";
	            temp += "<h2>"+ last +"</h2>";
	            temp += "<h3>"+ "2010-08-20" +"</h3>";
	            temp += "<h4>系统部</h4>";
	            temp += "<h5 onclick=openWin(\'"+ getUrl(fName,rtdata[j].FILE_ID, moudle) +"\') style='cursor:pointer;' size='2px;' color='blue'>阅读</h5>";
	            temp += "</li>";  
	          }
	         $("#Related-bg").append(temp);	         
	         //createPageNo(totalNo, currNo, "pageNumberb", "notag");
         }else{
            $("#Related-bg").html("没有相关文档!");
         }
      }
		}
  });
}

/**
 * fName  文件名
   file_id 文件id
   moudle 类型如：news
 */
function getUrl(fName, file_id, moudle){
  var readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
  readUrl += "&attachmentId="+ file_id +"&moudle="+ moudle +"&op=5&signKey=&print=";  
  return readUrl;
}


	/**
	 * 点击文件名，相关的主题词，相关的列表显示
	 */
	function clickFileName(fileId){
	  setFileId(fileId);
	  getArticleTags(fileId);
	  getRelationArticleTags(fileId);	

	  $("#tab-a-con a").bind("click",function(){ 
      $("#tab-a-con a").each(function(){
  		var c = $(this).css("color");  	
  		if(c == "red"){
  		  $(this).css("color","");
  		}
  	});
  	$(this).css("color","red");
   });  
	}
	
	function clear(){
	  $("#label-up").html("&nbsp;");
	  articleListByKeyIDs("",1);
	}

	/**
	*通过keyIds查找相关的文档	 words 没有用
	*/
  function articleListByKeyIDs(keyIds,currNo2){
    $(".hiddiv").attr("id","tab-a-con");
	  $.ajax({
	    url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getArticleListByKeyIDs.act?KeyIDs=" + keyIds +"&nStartPage=" + (currNo2-1) +"&nPageSize=8",
	    type:"post",
	    success:function(data){
	       $("#dataa").empty();	       
	       var toJson = eval("("+data+")");
	       if(toJson){
		       var len = toJson.rtData.Rows.length;
		       var rtdata = toJson.rtData.Rows;	
		       var page = new Pager(toJson.rtData.nTotalRec, currNo2-1);
		       var totalNo = page.getPageCount();
		       var currNo = toJson.rtData.nStartPage +1;
		       
	          if(len>0){
	           setLen(len);
		          var temp = "";
		          for(var j=0; j<len; j++){
			          var tName = rtdata[j].FILE_PATH;	//D:\yh\attach\email\1109\681b8d81a339a1a3b4e22e0661d8a55f_文档 7153.docx
			          var subtitle = "";		          
			          var moudle = tName.substring(tName.indexOf("\\attach\\")+8,  tName.length);			          
			          moudle = moudle.substring(0,moudle.indexOf("\\"));			 // email     		         
			          var fName = tName.substring(tName.indexOf(moudle)+39+moudle.length, tName.length); // 文档 7153.docx	
			          var titleName = rtdata[j].TITLE;				      
			          var size = rtdata[j].FILE_SIZE;	         
			          var last = fName.substring(fName.indexOf(".")+1, fName.length); 
			          var datep = rtdata[j].UPDATE_TIME;
			          datep = datep.replaceAll("\/","").replaceAll("Date","").replaceAll("\\(","").replaceAll("\\)","");
			          var showdate = new Date(parseInt(datep));
			          var datestr = showdate.getFullYear()+"-"+ (showdate.getMonth()+1) +"-" + showdate.getDate();			         	          
			          var readUrl = contextPath + "/subsys/funcs/office/ntko/indexNtko.jsp?attachmentName="+encodeURI(fName);
			          readUrl += "&attachmentId="+ rtdata[j].FILE_ID +"&moudle="+ moudle +"&op=5&signKey=&print=";
			          if(titleName.length >12){
			            subtitle = titleName.substring(0,12)+" ...";
			          }else{
			            subtitle = titleName;
			          }
			          temp += "<li>";
		            //temp += "<input type=\"checkbox\" class=\"con-input\" value=\""+ rtdata[j].SEQ_ID +"\" />";
		            temp += "<span class='fileTitle'><a title=\""+ titleName +"\"href=\"javascript:void(0);\"  onclick=\"clickFileName('"+ rtdata[j].FILE_ID +"')\">"+ subtitle+"</a></span>";
		            temp += "<h1>"+ Math.ceil(size/1024) +"kb</h1>";
		            temp += "<h2>"+ last +"</h2>";
		            temp += "<h3>"+ datestr +"</h3>";
		            temp += "<h4>系统部</h4>";
		            temp += "<h5 onclick=openWin(\'"+ getUrl(fName,rtdata[j].FILE_ID, moudle) +"\') style='cursor:pointer;'>阅读</h5>";
		            temp += "</li>";    
		          }
		         $("#dataa").append(temp);		         
		         createPageNo(totalNo, currNo, "pageNumber", "tag");		         
	          }else {
	            setLen(0);
	            $("#dataa").html("没有相关文档");
	          }
	        }
	      }
	    }); 
	  $("#Related-bg").empty();
	 }

  /**
  * 删除tag路径，1.删除tag标签，2,tag云图更新，3,文件列表更新
  */
  function deleteTags(keyId, keyWord){
		var key =  window.parent.getKeyIds();
		var word = window.parent.getKeywords();		
		var keyArr = key.split(",");
		var wordArr = word.split(",");
		var newKey = "";
		var newWord = "";
		if(keyArr && wordArr){
       for(var i=0; i<keyArr.length; i++){
         if(keyArr[i] && keyArr[i]!=keyId){//当点击删除时（id和name相同时newKey="",newWord=""） id和名称 相同的 去掉，不相同的保存，在调用layer方法 重新显示 主题词的关系如：科技>教育>文学>教育  把教育去掉
           newKey += keyArr[i] + ",";
           newWord += wordArr[i] + ","; 
         }
       }
		}
		var newKeyLength = newKey.split(",");
		window.parent.setKeyIds(newKey);
		window.parent.setKeyWords(newWord);	
		layer(newKey, newWord);
		articleListByKeyIDs(newKey,1);  //更新文件列表
		window.parent.getTagsByTagIds(newKey);
		window.parent.setLeng(newKeyLength.length-1);
  }

 /**
  *显示标签
  */
	function layer(keyIds, words){
		//alert(keyIds+"=="+words);
	  $("#label-up").empty();
	  $("#label-up").html("&nbsp;");
	  var ids = new Array();
	  var id = keyIds.split(",");
	  var wds = words.split(",");
	  var temp = "";
	  for(var i=0; i<id.length; i++){
	    if(wds[i] && id[i]){
	       temp += "<a href='javascript:void(0);' class='table-a-label-a' onclick=deleteTags("+ id[i] +",'"+wds[i]+"')>";
	       temp += "<img src=\"images/x.gif\" width='9px;' heigth='9px;' border=\"0\"/>"
	       temp += "<font size='2'>"+wds[i]+"></font> ";
	       temp += "</a>"
	    }
	  }
	  $("#label-up").append(temp);
	}
	function laye(keyIds, words){
	  var ids = new Array();
	  var id = keyIds.split(",");
	  var wds = words.split(",");
  	ids.push(id.length-1);
	   var num = ids[0];
	  // alert(wds[num-1]);
	  if(wds[num-1] && id[num-1]){
	    deleteTags(id[num-1],wds[num-1]);
	  }
	}

  /**
  *  替换s1为s2
  */
  String.prototype.replaceAll  = function(s1,s2){   
    return this.replace(new RegExp(s1,"gm"),s2);   
  }

  var fileId = "";
  function setFileId(fileId){
		this.fileId = fileId;
  }
  function getFileId(){
		return this.fileId;
  }
  function showTouchGrap() {
    var data = $("#searchDate").val();
    if(data == ""){
       alert("关键字不能为空");
       $("#searchDate").focus();
       return false;
      }
    var url = contextPath + "/core/module/touchgraph/main.jsp?data="+encodeURIComponent(data);
    window.open(url);
  }
  /**
   $.ajax({
		url:contextPath + "/yh/subsys/inforesouce/act/YHFileMetaSaveAct/findFileList.act" ,
	  type:"post",
		data:"nodes="+ treeNode +"&modules="+modules,		
		//async:false,                                  //同步问题(false), 异步：(true)
		success:function(data){		 
 		  setData(data); 
 		  var data = getData();
 	    var toJson = eval("("+data+")"); 
 	    var len  = toJson.rtData.length;
 	    //alert(len+"****");
 	    setLen(len);
 	    var page = new Pager(len, 1);  	  
 	    setPage(page); 	
 	    pagerNo(1); 	 
 	    createPage(1, page);		   
		}
  });
  **/
  /*
  function getFullText(data){
     $.ajax({
       url:contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getFullText.act?q="+data+"&limit="+10,
       type:"post",
       success:function(data){
         
       }
     });
  }
  $('#searchDate').bind('keypress', function(event) { 
    var data = $("#searchDate").val();
    if (event.keyCode == 13){ 
    //设置回车事件后的失去焦点事件无效 
    getFullText(data);
    return false;
    } 
  });
  *///高级搜索引擎   在文本框中输入字 会自动弹出相关的词
  $(document).ready(function () {
    var url = contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getFullText.act";
    try{
	    $("#searchDate").autocomplete(url,
	    {   max:10,//最多显示 相关的10个词
		      scrollHeight: 300, //最大高度为 300
	        multiple: true,
	        multipleSeparator: " ",
	        dataType: "json",
	        parse: function (data) {
	            return $.map(data.rtData, function (row) {
	                return {
	                    data: row,
	                    value: "" + row.Count,
	                    result: "" + row.Term
	                }
	            });
	        },
	        formatItem: function (row) {
	          //  return "<span class='Left' align='left' style='margin:170px;'>" + row.Term + "</span><span align='right' style='margin:100px;' class='Right'>" + row.Count + "结果</span>";
	           return "<span id='Left'>" + row.Term + "</span><span id='Right'>" + row.Count + "结果</span>";
            
		        }
	    }).result(function (e, row) {Search(1,6)});
    }(e){
         
    }
   // $("#btnSearch").click(function(){                
      //  Search($("#txtKeyword").val());
   // });
});
  //Search(row.Term);
   function Search(currNo2,pageSize){
          var keyword = $("#searchDate").val();
              keyword = encodeURIComponent(keyword);
              if(!keyword){
                alert("搜索内容不能为空！");
                return ;
              }
              $("#dataa").html("");
              $(".hiddiv").attr("id","");//把class=hiddiv 的id 置为空
            $.ajax({
                    async: true,
                    cache: false,
                    type: 'GET',
                    dataType: 'json',
                    url: contextPath + "/yh/subsys/inforesouce/act/YHOutURLAct/getFullTextDocList.act?nStartPage="+(currNo2-1)+"&nPageSize="+pageSize,
                    data: { q: keyword},//把数据带到后台
                    success: function (data) {
                      if(data){
		                     var toJson =  data;
		           		       var currNo = toJson.CurPage +1;
		           		       var totalNo = toJson.TotalPage;
		                     var len  = toJson.items.length;
		                     if(len > 0){		                       
		                       var temp = "";
		                       for(var i=0; i<len; i++){
		                          //显示文件列表
		                         temp += toJson.items[i].Title+"----> 大小：";
		                         temp += toJson.items[i].FILE_SIZE;
		                         temp +="<br>";
		                         temp += toJson.items[i].Description;
		                         temp +="<br>-----------------------------------<br>";
		                       }
		                       $("#dataa").append(temp);			                                
		          		         createPageNo2(totalNo, currNo, "pageNumber");		   
		                     }else {
			                     $("#pageNumber").html("");
		        	            $("#dataa").html("没有搜索结果！");
		        	          }
                      }
                    }
              });
        }  
--></script>
</head>
<body style="overflow-y:hidden">
<div id="table-a">
        <div id="Search">
       <div id="Search-bg">
       <form name="form1" method="get">
       <input type="text" class="Search-bg-form"  id="searchDate" name="searchDate"/>
       </form>
       </div> 
       <div id="go"> 
       <a href="javascript:;" onclick="Search(1,6);" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image1','','images/go-b.jpg',1)"><img src="images/go.jpg" name="Image1" width="39" height="22" border="0" id="Image1" /></a>
       <a href="javascript:;" id="go-id" name="SearchId" onclick="showTouchGrap()"><img src="images/dj.gif"  width="32" height="21" border="0"  /></a>
       </div>
    </div>
    <div id="line-a"></div> 
    <div id="label"> 
      <div id="label-up">&nbsp;</div>
      <div id="label-down"></div>
    </div>
    <!-- linshi shiyong
        <div id="title"   onmouseout="orderout()" onmouseover="orderover()">
    -->    
        <!-- <div id="title">
        <input type="checkbox" class="con-input" name="allbox" onclick="javascript:checkAll(this);" value="" />
        </div>-->  
    <div id="tab-a-con" class="hiddiv">
      <!--  
        <ul id="inser">
        </ul> 
     -->
      <ul id="dataa">
        </ul> 
    </div> 
   <div id="page-a">   
    <ul id="pageNumber">
       
    </ul>     
  </div>
</div>
<div class="clear"></div>
<!-- <div id="table-b">
<div id="Related-title">
<a href="javascript:;" id="Related-id" onclick="showTouchGrap()"><img width="45" height="23" border="0" src="images/ss.gif" align="right" style="margin: 10px;"/></a>

</div>
 <div id="Related-bg">
<ul id="copyData"> <!--<h1 style="display:inline;"> 横向显示数据   <div id="Related-bg">  
</ul>
  </div>
  <div id="Related-fot"></div>
</div>
  <div id="page-b">   
    <ul id="pageNumberb"></ul>     
  </div>-->
</body>
</html>


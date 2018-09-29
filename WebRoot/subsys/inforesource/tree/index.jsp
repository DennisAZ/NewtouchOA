<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>YH文件件管理中心</title>
<link href="css/css.css" rel="stylesheet" type="text/css" />
<link href="css/css-left.css" rel="stylesheet" type="text/css" />
<link href="css/css-content.css" rel="stylesheet" type="text/css" />
<link href="css/css-right.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="js/jquery.js"></script>
<style>
#difins{
position:relative;
top:0px;
left:-30px;

}
#dingyis{
position:relative;
top:19px;
left:30px;
 FONT-WEIGHT:bold;
}
</style>
<script type="text/JavaScript">
<!--
function MM_jumpMenu(targ,selObj,restore){ //v3.0
  eval(targ+".location='"+selObj.options[selObj.selectedIndex].value+"'");
  if (restore) selObj.selectedIndex=0;
}

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
var ftype="1";
function tomanage(){
  var url = contextPath + "/yh/subsys/inforesouce/act/YHMateShowAct/toManage.act?ftype="+getFtype();
  myleft=(screen.availWidth-500)/2;
  window.open(url,"read_news","height=800,width=900,status=1,toolbar=no,menubar=no,location=no,scrollbars=yes,top=100,left="+myleft+",resizable=yes"); 
}

function getIFrameDOM(id){//兼容IE、Firefox的iframe DOM获取函数
  return document.getElementById(id).contentDocument || document.frames[id].document;
}

function refreshMe(){  
  window["treeSampleframe"].findTypeMenu(getFtype()); 
}

function filtag(){//填写 自定义标签
  var url = contextPath + "/subsys/inforesource/tagname.jsp";   
   window.open(url, "window","height=200,width=350,status=0,toolbar=no,menubar=no,location=no,scrollbars=yes,left=220,top=180,resizable=no");
}
var jsonval = "";

function setJsonval(jsonval){
  this.jsonval=jsonval;
}
function getJsonval(){
  return jsonval;
}
/**
 * 树选择节点后，把值存放在这里
 */
	var node="";
	function setNode(node){  
	  this.node=node;
	}
	
	function getNode(){
	  return node;
	}
	
	function freshContent(){
	  var treeNode = getNode(); 
	  rightOpt(treeNode);
	}
	
	function refreshContent(){
	  var a = getIFrameDOM("contentframe");
	  a.location.reload();
	}
	/**
	 * 树选择节点后，把值存放在这里
	 */
	 var seqId = "";
	 var nodeIds = "";
	function setNodeIds(seqId,nodeIds){
	  seqId = seqId;  
	  nodeIds = nodeIds;
	}
	//设计 教育》》科技》》文化 的长度
	 var leng = 0;
	 function setLeng(leng){
	   this.leng = leng;
	 }
	 function getLeng(){
	   return this.leng;
	 }
	/**
	 * 点击tag云图标签后，content.jsp上会出现层次关系
	 */
   var keyId = "";   
   var keyword = "";
   function setLayer(keyId,keyword){
     var num = getKeyIds().split(",");
     var leng = num.length;   
     if(num.length < 7){
       //setLeng(num.length);
	     this.keyId = getKeyIds() + keyId+",";//每次点击tag都增加，首次keyId为空 
	     this.keyword = getKeywords()+keyword +","; 
            window["contentframe"].layer(getKeyIds(), getKeywords());//调用子页面（Content.jsp）方法     }
     setLeng(leng);
   }
   function setLaye(keyId,keyword){
     this.keyId = getKeyIds() + keyId+"";  
     this.keyword = getKeywords()+keyword +"";
          
     window["contentframe"].laye(getKeyIds(), getKeywords());//调用子页面（Preview.jsp）方法
   }

   function clear(){
     this.keyId = "";
     this.keyword = "";
     window["contentframe"].clear();     
   }

  function setColKeyWord(keyId,keyword){
    //alert(getKeywords());
    window["contentframe"].articleListByKeyIDs(getKeyIds(), getKeywords());
  }
  // 列表内容为空不可以点击
 
  function getLen(){
    return window["contentframe"].getLen();     
  }

  
//-->
</script>
<script type="text/javascript">
//
	$("document").ready(function(){
		//点击checkbox是触发    //$("#right-cen").hide(); 默认让树结构隐藏
	  $(".menu-r-input").bind("click",function(){	  
	    var modules = "";
			$(".menu-r-input").each(function(){
				if(this.checked == true){
				  modules += this.value + ",";		 
				}
			});		
		 setModules(modules);	 
		 leftOpt(modules);	
		   
	  });

    //点击图片是触发
    $("#menu-r a").bind("click", function(){    
			var ch = $(this).prev(".menu-r-input");						
			var flag = ch.attr("checked");			
			if(flag == false){				
			  ch.attr("checked", true)
			}else{
			  ch.attr("checked", false)
			}
			var modules = "";
			$(".menu-r-input").each(function(){
				if(this.checked == true){
				  modules += this.value + ",";		 
				}
			});		
		 setModules(modules);	 
		 leftOpt(modules);	 
    });
	});
	var mods = "";
	function setModules(mods){
	  this.mods = mods;
	}
	
	function getModules(){
	  return this.mods;
	}

/**
 * 点击左侧的功能模块的功能
 */
function leftOpt(modules){
  //先判断有没有选择树,如果选择了树，则从选择得 列表中进行筛选，
  //如果没有选择树，则直接进行查询
  
  var treeNode = getNode();                     //看看选择树了么
  if(treeNode){  //没有选择树，则直接进行查询		       
    window["contentframe"].toSeachByAjax(modules, treeNode);
  }else{     
    window["contentframe"].toSeachByAjax(modules, "");
  }
}
/**
 * 点击左边的树进行的操作
 */
	function rightOpt(treeNode){  
	  var modules  = getModules(); // 获得右边选中的节点  
	  if(modules){  // 如果选择了左侧的功能列表   
	    window["contentframe"].toSeachByAjax(modules, treeNode);
	  }else{ 
	    window["contentframe"].toSeachByAjax("", treeNode);
	  }
	}

	/**
	 * 点击文件名，显示这个文件相关的主题词
	 */
	function getArticleTags(fileId){
	  //调用子页面的ajax
	  window["preframe"].getArticleTags(fileId);
	}	

	function setKeyIds(keyIds){
    this.keyId = keyIds;
	}

  function setKeyWords(keyword){
    this.keyword = keyword;
  }

  function getKeyIds(){
		 return this.keyId;
  }

  function getKeywords(){
		return this.keyword;
  }

  function getTagsByTagIds(tagIds){
		window["preframe"].getTagsByTagIds(tagIds);
  }

  function getArticleAbstract(fileId){
    window["preframe"].findZhaiYao(fileId);
  }
  // jquery特有方法，显示隐藏文本域
  function triggle(){
    var show = $("#dingyis").text();
    if(show == "隐藏"){
      $("#dingyis").html("<font color=\"#001199\" size= \"2\">显</font><font size= \"2\" color=\"#00fffa\">示</font>");
    }else{
      $("#dingyis").html("<font size = \"2\" color=\"#00fffa\">隐</font><font size = \"2\" color=\"#22FF22\">藏</font>");
    }
    $("#right-cen").slideToggle("slow");//其实就是本文的全部，一个slideToggle即实现的菜单的自动下拉以收起 （意思是产生滑动效果）   有参数slow,normal和fast，默认是normal
  } //slideToggle(speed,callback);其有两个参数，一个是speed，表速度，数字或字符串关键字，如是数字，表毫秒，如字符串关键字，则有slow,normal和fast，默认是normal。另一个参数是callback，指回调函数 

  function setFtype(ftype){
		this.ftype = ftype;
  }
  function getFtype(){
		return this.ftype;
  }
  function typeMenu(obj){
    var typemenu = obj.value;
    setFtype(typemenu);
    window["treeSampleframe"].findTypeMenu(typemenu);
  }
  
</script>
</head>
<body onload="MM_preloadImages('images/Preservation-b.gif','images/definition-b.gif','images/a1.gif','images/a2.gif','images/a3.gif','images/a4.gif','images/a5.gif','images/a6.gif','images/a7.gif','images/a8.gif')">
<div id="container">
    <div id="top"><iframe id="sampleframe" name="sampleframe" width="100%" height="100%" frameborder="0" src="top.jsp" style="border: 0px"></iframe></div>
       <div id="center">
           <div id="left">
	             <div id="left-a"></div>
	                <div id="left-menu">
					  <div id="menu-r">
					      <ul>
						    <li><input type="checkbox" class="menu-r-input"  value="email" />
						    <a href="javascript:void(0);"  onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image3','','images/a1.gif',1)"><img src="images/a1-1.gif" name="Image3" width="72" height="21" border="0" id="Image3" /></a><!--<span class="menu-span-a">(25)</span><span class="menu-span-b">135kb</span></li>-->
							
							<li><input type="checkbox" class="menu-r-input"  value="personFolder" />
						    <a href="javascript:void(0);"  onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image4','','images/a2.gif',1)"><img src="images/a2-1.gif" name="Image4" width="72" height="21" border="0" id="Image4" /></a>	</li>
							
							<li><input type="checkbox" class="menu-r-input"  value="file_folder" />
						    <a href="javascript:void(0);"  onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image5','','images/a3.gif',1)"><img src="images/a3-1.gif" name="Image5" width="72" height="21" border="0" id="Image5" /></a></li>
							
							<li><input type="checkbox" class="menu-r-input"  value="gongwen" /><!-- 公文管理 -->
						    <a href="javascript:void(0);"  onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image6','','images/a4.gif',1)"><img src="images/a4-1.gif" name="Image6" width="72" height="21" border="0" id="Image6" /></a>	</li>
							
							<li><input type="checkbox" class="menu-r-input"  value="huiyiguanli" /><!-- 会议管理 -->
						    <a href="javascript:void(0);"  onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image7','','images/a5.gif',1)"><img src="images/a5-1.gif" name="Image7" width="72" height="21" border="0" id="Image7" /></a></li>
							
							<li><input type="checkbox" class="menu-r-input"  value="neibutaolun" /><!-- 内部讨论区-->
						    <a href="javascript:void(0);"  onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image8','','images/a6.gif',1)"><img src="images/a6-1.gif" name="Image8" width="72" height="21" border="0" id="Image8" /></a>	</li>
							<li><input type="checkbox" class="menu-r-input" value="notify" />
						    <a href="javascript:void(0);"  onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image9','','images/a7.gif',1)"><img src="images/a7-1.gif" name="Image9" width="72" height="21" border="0" id="Image9" /></a></li>
							<li><input type="checkbox" class="menu-r-input" value="news" />
						    <a href="javascript:void(0);"  onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image10','','images/a8.gif',1)"><img src="images/a8-1.gif" name="Image10" width="72" height="21" border="0" id="Image10" /></a></li>
						  </ul>
					  </div>
	                </div>
	             <div id="left-b"></div>
	             <div id="left-Preview">
				     <div id="preview-box">
				      <iframe id="preframe" name="preframe" width="100%" height="100%" frameborder="0" src="Preview.jsp" style="border: 0px"></iframe>
					 </div>
				 </div>
		   </div>		  
		  <div id="cen">
			 <div id="table" >
				 <iframe id="contentframe" name="contentframe" width="100%" height="100%" frameborder="0" src="Content.jsp" style="border: 0px">
				 </iframe>
			 </div>
		  </div>		  	 
       <div id="right">
	         <div id="right-top">	
	         <!--    
       <span id="dingyis" style="cursor:pointer;" onclick="javascript:triggle();"><font size = "2" color="#22FF22">显</font><font size = "2" color="#FF2222">示</font></span>
	         -->
			     <div id="Fileclass">
				   <form action="" method="get">
				   <!-- 
				     <select name="menu1" onchange="MM_jumpMenu('parent',this,0)">  -->
					     <select name="menu1" id ="menu1" onchange="typeMenu(this)">
                  <option value="1">文本类型</option>
                  <option value="2">图片类型</option>
                  <option value="3">视频类型</option>
	            </select>
				   </form>
				 </div>
			 </div>
		   <div id="right-cen">
			   <div id="right-tree">
			     <iframe id="treeSampleframe" name="treeSampleframe" width="100%" height="100%" frameborder="0" src="tree.jsp" style="border: 0px; overflow:hidden"></iframe>
			   </div>
			 <div id="right-down">
			 <div id="Preservation"><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image1','','images/Preservation-b.gif',1)" onclick="javascript:filtag();return false;"><img src="images/Preservation-a.gif" name="Image1" width="77" height="26" border="0" id="Image1" /></a></div>
			 <div id ="dingyi"><a href="javascript:void(0);" onclick="javascript:tomanage();" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image2','','images/definition-b.gif',1)"><img style="padding-top: 4px;margin-left: 29px;" src="images/definition-a.gif" name="Image2" width="26" height="26" border="0" id="Image2" /></a></div>
			 </div>
			 <div class="clear"></div>
			 </div>
	   </div>
       </div>
    <div id="foot"></div>
</div>
</body>

</html>

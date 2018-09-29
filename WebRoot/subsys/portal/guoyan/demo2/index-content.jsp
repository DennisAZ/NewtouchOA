<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header2.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>国务院发展研究中心</title>
<link href="style/css/css.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="style/css/jq-yhtheme.css" />
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.core.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.widget.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.draggable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.yh.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.columnlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.container.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.panel.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.cardlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.columnlayout.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/ux/jquery.ux.module.js"></script>
<style type="text/css">
<!--
body {
	background-color: #FFFFFF;
}

.column-txt {
	width: 172px;
	height: 119px;
	margin-top: 25px;
	margin-left: 8px;
}
.column-txt-p {
	display: block;
	line-height: 23px;
	font-family: "宋体";
	font-size: 14px;
	padding-left: 15px;
}
.column-txt-p2 {
	display: block;
	line-height: 23px;
	font-family: "宋体";
	font-size: 14px;
	padding-left: 93px;
}
.column-txt a {
	font-family: "宋体";
	font-size: 14px;
}
.column-txt a:hover {
	font-family: "宋体";
	font-size: 14px;
	color: #0000CC;
	text-decoration: none;
}

.column-txt2 {
	width: 157px;
	height: 129px;
	margin-top: 9px;
	margin-left: 19px;
}
.column-txt2 li {
	margin: 0px;
	line-height: 24px;
}
.column-txt2 a {
	font-family: "宋体";
	font-size: 14px;
}
.column-txt2 a:hover {
	font-family: "宋体";
	font-size: 14px;
	color: #0000FF;
	text-decoration: underline;
}


-->
</style>
<script type="text/JavaScript"><!--
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
$(document).ready(function() {
  /*
  var leftWidth = 0.45;
  window.columnLayout = new ColumnLayout({
    container: jQuery('body'),
    items: [{
      contentEl: jQuery('#leftContainer'),
      columnWidth: leftWidth,
      padding: '10 10 10 10'
    },{
      contentEl: jQuery('#rightContainer'),
      columnWidth: 1 - leftWidth,
      padding: '10 10 10 10'
    }]
  });
  */

  var urls = [
    'modules/cfg/ldhd.js',
    'modules/cfg/zxtz.js',
    'modules/cfg/xsyj.js',
    'modules/cfg/yzqk.js'
    //'modules/cfg/5.js',
    //'modules/cfg/6.js'
  ];
  
  $.each(urls, function(i, e) {
    $.get(e, function(data) {
      var cfg = YH.parseJson(data);
      cfg.data = data;
      //cfg.width = '100%';
      //cfg.tbar = [];
      cfg.id = 'm' + i;
     /* if (cfg.moreLink) {
        cfg.tbar.push({
          id: 'more',
          handler: function(e, target, panel) {
            alert('查看更多链接' + cfg.moreLink);
          }
        });
      }*/
      //cfg.style['padding'] = '2px 0px 10px 0px';
      var temp = new YH.Panel(cfg);
    }, 'text');
  });

  //调用拖动控件
  jQuery(".column").sortable({
    connectWith: '.column',
    handle: '.jq-panel-tl',
    revert: true,
    activate: function(event, ui) {
    
      ui.item.css({
        'width': ui.placeholder.innerWidth()
      });
      
      ui.placeholder.css({
        'height': ui.item.innerHeight()
      });
      /*
      jQuery('.column').css({
        'height': jQuery('body').height()
      });*/
    },
    start: function(event, ui) {
      
    }
  });

  var leftWidth = 0.55;
  new YH.ColumnLayout({
    container: jQuery('body'),
    items: [{
      contentEl: jQuery('#leftContainer'),
      columnWidth: leftWidth,
      padding: '10 10 150 10'
    },{
      contentEl: jQuery('#rightContainer'),
      columnWidth: 1 - leftWidth,
      padding: '10 10 150 10'
    }]
  });
});
--></script>

<style>

</style>
</head>

<body>
<div>
<table cellspacing="5px">
  <tr height="150px">
    <td id="ldhd" width="425px">
    </td>
    <td id="zxtz" width="365px" >
    </td>
    <td id="ldzlx" style="text-align:left">
      <div class="box-column">
        <div class="column-tit">
        <a href="#" target="mainframe" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image17','','images/tit-01hover.jpg',1)">
        <img src="images/tit-01.jpg" name="Image17" width="75" height="21" border="0" id="Image17" /></a>
      </div>
      <div class="column-txt">
        <p class="column-txt-p">主&nbsp;&nbsp;&nbsp;&nbsp;任：&nbsp;<a href="lead.jsp?type=a1&userName=张玉台">张玉台</a></p>
        <p class="column-txt-p">党组书记：<a href="lead.jsp?type=b1&userName=李伟">&nbsp;李&nbsp;  伟</a></p>
        <p class="column-txt-p">副&nbsp;主&nbsp;任：<a href="lead.jsp?type=c1&userName=刘世锦">&nbsp;刘世锦</a></p>
        <p class="column-txt-p2"><a href="lead.jsp?type=d1&userName=侯云春">侯云春</a></p>
        <p class="column-txt-p2"><a href="lead.jsp?type=e1&userName=卢中原">卢中原</a></p>
       </div>
     </div>
    </td>
  </tr>
	 <tr height="179px">
      <td id="yzqk" width="425px">
      </td>
      <td  width="365px">
      <div class="box-research">
         <div class="po-tit"></div>
       <div class="clear"></div>
      <div class="po-con2">
        <div class="line-up">
        <img src="images/book-icon.jpg" border="0"/>
        <a href="modules/research/research_list.jsp?type=1" class="po-con2-a">《专题调研》</a>
        <img src="images/book-icon.jpg" border="0"/>
        <a href="modules/research/research_list.jsp?type=2" class="po-con2-a">《调研报告》</a>      </div>
      <div class="line-cen">
        <img src="images/book-icon.jpg" border="0"/>
        <a href="modules/research/research_list.jsp?type=3" class="po-con2-a">《调研专刊》</a>
        <img src="images/book-icon.jpg" border="0"/>
        <a href="modules/research/m_research_list.jsp" class="po-con2-a">《会议资料库》</a>     </div>
      <div class="line-dw">
        <img src="images/book-icon.jpg" border="0"/>
        <a href="modules/research/research_list.jsp?type=5" class="po-con2-a">《择要》</a>
      </div>
      </div>
     </div>
      </td>
      <td id="" style="text-align:left" >
       <div class="box-column2">
         <div class="column-tit2"><a href="#" target="mainframe" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image18','','images/tit-02hover.jpg',1)"><img src="images/tit-02.jpg" name="Image18" width="75" height="21" border="0" id="Image18" /></a></div>
         <div class="column-txt2">
         <MARQUEE style="WIDTH: 157px; HEIGHT:129px" scrollAmount=1 scrollDelay=80 direction=up width=157 height=129>

        <ul>
        <li><a href="dept.jsp?type=bgt&deptName=办公厅(人事局)" target="mainframe">办公厅(人事局)</a></li>
        <li><a href="dept.jsp?type=xxzx&deptName=信息中心" target="mainframe">信息中心</a></li>
		<li><a href="dept.jsp?type=qy&deptName=企业研究所" target="mainframe">企业研究所</a></li>
        <li><a href="dept.jsp?type=hgb&deptName=宏观经济研究部" target="mainframe">宏观经济研究部</a></li>
        <li><a href="dept.jsp?type=scs&deptName=市场经济研究所" target="mainframe">市场经济研究所</a></li>

      </ul>
      </MARQUEE>
     </div>
     </div>
      </td>
  </tr>
  <tr>
    <td colspan="3">
     <div class="box-c">
      <div class="box-c-left">
	     <div class="c-tit"></div>
		 <div class="c-menubox">
		   <div class="c-previous"><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image10','','images/previous-b.jpg',1)"><img src="images/previous-a.jpg" name="Image10" width="13" height="85" border="0" id="Image10" /></a></div>
		   <div class="c-menu">
		    <ul>
			  <li><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image12','','images/nu-1h.jpg',1)"><img src="images/nu-1a.jpg" name="Image12" width="108" height="136" border="0" id="Image12" /></a></li>
			  <li><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image13','','images/nu-2h.jpg',1)"><img src="images/nu-2a.jpg" name="Image13" width="108" height="136" border="0" id="Image13" /></a></li>
			  <li><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image14','','images/nu-3h.jpg',1)"><img src="images/nu-3a.jpg" name="Image14" width="108" height="136" border="0" id="Image14" /></a></li>
			  <li><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image15','','images/nu-4h.jpg',1)"><img src="images/nu-4a.jpg" name="Image15" width="108" height="136" border="0" id="Image15" /></a></li>
			  <li><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image16','','images/nu-5h.jpg',1)"><img src="images/nu-5a.jpg" name="Image16" width="108" height="136" border="0" id="Image16" /></a></li>
			</ul>
		   </div>
			<div class="c-next"><a href="#" onmouseout="MM_swapImgRestore()" onmouseover="MM_swapImage('Image11','','images/next-b.jpg',1)"><img src="images/next-a.jpg" name="Image11" width="13" height="85" border="0" id="Image11" /></a></div>
		 </div>
	   </div>
	   <div class="box-c-right">
	       <div class="c-up">
		      <div class="c-up-left">
			      <a href="#">清华学术期刊</a>
				  <a href="#">清华学术期刊</a>
			  </div>
			  <div class="c-up-right">
			      <a href="#">北大学术期刊</a>
				  <a href="#">北大学术期刊</a>
			  </div>
		   </div>
		   <div class="c-dw">

		   </div>
	   </div>	
     </div>
    </td>
  </tr>
</table>
</div>
</body>
</html>
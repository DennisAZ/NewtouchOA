<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	String contextPath = request.getContextPath();
	if (contextPath.equals("")) {
	  contextPath = "/yh";
	}
%>
<html>
<head>
    <title>warehouse_nav</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <meta http-equiv="imagetoolbar" content="no"/>
    <meta name="apple-mobile-web-app-capable" content="yes"/>
    <link href="resources/css/jquery-ui-themes.css" type="text/css" rel="stylesheet">
    <link href="resources/css/axure_rp_page.css" type="text/css" rel="stylesheet">
    <link href="warehouse_nav_files/axurerp_pagespecificstyles.css" type="text/css" rel="stylesheet">
<!--[if IE 6]>
    <link href="warehouse_nav_files/axurerp_pagespecificstyles_ie6.css" type="text/css" rel="stylesheet">
<![endif]-->
    <script src="data/sitemap.js"></script>
    <script src="resources/scripts/jquery-1.7.1.min.js"></script>
    <script src="resources/scripts/axutils.js"></script>
    <script src="resources/scripts/jquery-ui-1.8.10.custom.min.js"></script>
    <script src="resources/scripts/axurerp_beforepagescript.js"></script>
    <script src="resources/scripts/messagecenter.js"></script>
<script src='warehouse_nav_files/data.js'></script>
</head>
<body>
<div id="main_container">

<div id="u0" class="u0_container"   >
<div id="u0_img" class="u0_normal detectCanvas"   style="cursor:pointer" onclick="javascript:window.location.href='<%=contextPath %>/springViews/erp/productInfo/frame.jsp';"></div>
<div id="u1" class="u1" style="visibility:hidden;"  >
<div id="u1_rtf"></div>
</div>
</div>
<div id="u2" class="u2_container"   >
<div id="u2_img" class="u2_normal detectCanvas"></div>
<div id="u3" class="u3" style="visibility:hidden;"  >
<div id="u3_rtf"></div>
</div>
</div>
<div id="u4" class="u4_container"   >
<div id="u4_img" class="u4_normal detectCanvas"   style="cursor:pointer" onclick="javascript:window.location.href='<%=contextPath %>/springViews/erp/warehouse/dbManage.jsp';"></div>
<div id="u5" class="u5" style="visibility:hidden;"  >
<div id="u5_rtf"></div>
</div>
</div>
<div id="u6" class="u6_container"   >
<div id="u6_img" class="u6_normal detectCanvas"  style="cursor:pointer" onclick="javascript:window.location.href='<%=contextPath %>/springViews/erp/productUnit/index.jsp';"></div>
<div id="u7" class="u7" style="visibility:hidden;"  >
<div id="u7_rtf"></div>
</div>
</div>
<div id="u8" class="u8_container"   >
<div id="u8_img" class="u8_normal detectCanvas"  style="cursor:pointer" onclick="javascript:window.location.href='<%=contextPath %>/springViews/erp/warehouse/index.jsp';"></div>
<div id="u9" class="u9" style="visibility:hidden;"  >
<div id="u9_rtf"></div>
</div>
</div>
<div id="u10" class="u10"  >
<div id="u10_rtf"><p style="text-align:left;"><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">销售</span><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">出货</span></p></div>
</div>
<div id="u11" class="u11_container"   >
<div id="u11_img" class="u11_normal detectCanvas"  style="cursor:pointer" onclick="javascript:window.location.href='<%=contextPath %>/springViews/erp/warehouse/returnManage.jsp';"></div>
<div id="u12" class="u12" style="visibility:hidden;"  >
<div id="u12_rtf"></div>
</div>
</div>
<div id="u13" class="u13"  >
<div id="u13_rtf"><p style="text-align:left;"><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">销售</span><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">退</span><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">货</span></p></div>
</div>
<div id="u14" class="u14_container"   >
<div id="u14_img" class="u14_normal detectCanvas" style="cursor:pointer" onclick="javascript:window.location.href='<%=contextPath %>/springViews/erp/warehouse/returnManage.jsp';"></div>
<div id="u15" class="u15" style="visibility:hidden;"  >
<div id="u15_rtf"></div>
</div>
</div>
<div id="u16" class="u16"  >
<div id="u16_rtf"><p style="text-align:left;"><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">采购</span><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">退货</span></p></div>
</div>
<div id="u17" class="u17_container"   >
<div id="u17_img" class="u17_normal detectCanvas"   style="cursor:pointer" onclick="javascript:window.location.href='<%=contextPath %>/springViews/erp/warehouse/pIndex.jsp';"></div>
<div id="u18" class="u18" style="visibility:hidden;"  >
<div id="u18_rtf"></div>
</div>
</div>
<div id="u19" class="u19"  >
<div id="u19_rtf"><p style="text-align:left;"><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">采购</span><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">入库</span></p></div>
</div>
<div id="u20" class="u20"  >
<div id="u20_rtf"><p style="text-align:left;"><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">库存报损</span></p></div>
</div>
<div id="u21" class="u21"  >
<div id="u21_rtf"><p style="text-align:left;"><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">库存</span><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">查询</span></p></div>
</div>
<div id="u22" class="u22"  >
<div id="u22_rtf"><p style="text-align:left;"><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">产品维护</span></p></div>
</div>
<div id="u23" class="u23"  >
<div id="u23_rtf"><p style="text-align:left;"><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">产品</span><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">参数</span><span style="font-family:Arial;font-size:13px;font-weight:normal;font-style:normal;text-decoration:none;color:#333333;">字典</span></p></div>
</div>
</div>
<div class="preload"><img src="warehouse_nav_files/u0_normal.png" width="1" height="1"/><img src="warehouse_nav_files/u2_normal.png" width="1" height="1"/><img src="warehouse_nav_files/u4_normal.png" width="1" height="1"/><img src="warehouse_nav_files/u6_normal.png" width="1" height="1"/><img src="warehouse_nav_files/u8_normal.png" width="1" height="1"/><img src="warehouse_nav_files/u11_normal.png" width="1" height="1"/><img src="warehouse_nav_files/u17_normal.png" width="1" height="1"/></div>
</body>
<script src="resources/scripts/axurerp_pagescript.js"></script>
<script src="warehouse_nav_files/axurerp_pagespecificscript.js" charset="utf-8"></script>
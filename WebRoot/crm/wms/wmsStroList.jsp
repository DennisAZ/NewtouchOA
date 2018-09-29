<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>仓库列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
	<link rel="stylesheet" type="text/css" href="css/wms.css"/>
    <link rel="stylesheet" type="text/css" href="css/guide.css">
    <style type="text/css">
    	.deepGray{
			border:#fff 1px solid;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript">
		
		function changeWmsStro(agr,name){
			parent.mainFrame.location="wms/wmsDeskTop.jsp?wmsName="+encodeURIComponent(name)+"&wmsCode="+agr;
		}
		
		createProgressBar();
		window.onload=function(){
			closeProgressBar();
		}
	</script>
  </head>
  
  <body>
  <div id="deskBox">
  	<div id="title">库存管理 > 选择仓库</div>
    <div id="topWords">
    	<table style="width:96%" class="normal nopd">
        	<tr>
            	<td>您要选择哪个仓库？</td>
                <td style="text-align:right">
                	<span id="wmsSet" class="bold deepGray" style="display:none;font-size:13px; text-align:center; cursor:pointer; padding:5px; width:100px" onMouseOver="this.className='grayBack bold'" onMouseOut="this.className='bold deepGray'" onClick="self.location.href='wmsManageAction.do?op=wmsStroSearch'">
                        <img src="images/content/wheel.gif" alt="设置仓库" class="imgAlign"/>&nbsp;设置仓库
                    </span>
                    <script type="text/javascript">
                        var rigs = "w001";
                        var ids = new Array();
                        ids[0] = "wmsSet";
                    </script>
                </td>
            </tr>
        </table>
    </div>
    
    <div style="text-align:center">
    	<logic:notEmpty name="wmsStro">
        	<div id="allWms" style="display:none" class="topWms lightGrayBack blue" onMouseOver="this.className='blueBox topWms'" onMouseOut="this.className='lightGrayBack topWms blue'" onClick="changeWmsStro('0','全部仓库')">	
            	<div class="blueBg" style="padding:2px;">&nbsp;全部仓库&nbsp;</div>
        	</div>
            <script type="text/javascript">
				rigs += "|w016";
				ids[1] = "allWms";
           	</script>
        </logic:notEmpty>
        <div style="text-align:left; height:300px; width:90%" class="divWithScroll2">
            <logic:notEmpty name="wmsStro">
                <logic:iterate id="wms" name="wmsStro" scope="request" indexId="count">
                    <span id="wms${count}" title="${wms.wmsName}" class="allWms lightGrayBack" style="display:none" onMouseOver="this.className='blueBox allWms'" onMouseOut="this.className='allWms lightGrayBack'" onClick="changeWmsStro('${wms.wmsCode}','${wms.wmsName}')">
                        <div class="textOverflow bold">${wms.wmsName}&nbsp;</div>
                        <logic:notEmpty name="wms" property="wmsStroType"><div class="textOverflow brown" title="类别">[${wms.wmsStroType.typName}]</div></logic:notEmpty>
                        <logic:notEmpty name="wms" property="wmsLoc"><div class="textOverflow gray" title="地区">${wms.wmsLoc}</div></logic:notEmpty>
                    </span>
                    <script type="text/javascript">
						rigs += "|${wms.wmsCode}";
						ids[(${count}+2)] = "wms${count}";
					</script>
                </logic:iterate>
            </logic:notEmpty>
            <logic:empty name="wmsStro"><span class="gray">系统暂未添加任何仓库！</span></logic:empty>
        </div>
    </div>   
    <script type="text/javascript">displayLimAllow(rigs,ids);</script>
 </div>
 </body>
</html>

<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>选择产品(入库明细)</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>

    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/choosePro.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript">
    	function save(){
			waitSubmit("dosave","保存中...");
			return $("register").submit();
		}
		
		
		createProgressBar();	
		window.onload=function(){
			
			closeProgressBar();
		}
    </script>
  </head>
  <body>
  <logic:notEmpty name="wmsWarIn">
      <div id="mainbox">
    	<div id="contentbox">
        	<div id="title">入库管理 > 入库单 > 入库明细</div>
			<table class="normal" cellpadding="0" cellspacing="0" style="width:100%">
				<tr>
					<td style="width:35%; text-align:center; vertical-align:top; padding-left:10px;">
                    	<div class="orgBorder" >
                        	<div class="orgBorderTop">选择产品</div>
                            <script type="text/javascript">createIfmLoad('ifmLoad');</script>
                        	<iframe onload="loadAutoH(this,'ifmLoad')" frameborder="0" scrolling="auto" width="100%" src="wmsManageAction.do?op=searchProType"></iframe>
                        </div>
                    </td> 
					<td style="vertical-align:top; padding-top:6px;">
             			<div id="listContent" style="height:550px;">
							<div class="grayBack" style="margin-bottom:10px; padding:6px;">
                            	<img id="hideImg" class="imgAlign" src="images/content/hide.gif" onClick="showHide('ordDesHide','hideImg')" style="cursor:pointer;" alt="点击收起"/>&nbsp;<span class="blue">入库单</span>
                            	<div id="ordDesHide" style="display:block; text-align:center;">
                                	<table class="normal lineborder" style="width:95%" cellpadding="0px" cellspacing="0px">
                                    	<tr>
                                        	<td>主题：</td>
                                            <th colspan="3">${wmsWarIn.wwiTitle}&nbsp;</th>
                                        </tr>
                                    	<tr>
                                        	<td><nobr>入库单号：</nobr></td>
                                            <th style="width:40%">${wmsWarIn.wwiCode}&nbsp;</th>
                                        	<td>仓库：</td>
                                            <th style="width:40%"> ${wmsWarIn.wmsStro.wmsName}&nbsp;</th>
                                        </tr>
                                        <tr>
                                        	<td style="border:0px">填单人：</td>
                                            <th style="border:0px"> ${wmsWarIn.wwiInpName}&nbsp;</th>
                                        	<td style="border:0px"><nobr>填单日期：</nobr></td>
                                            <th style="border:0px"><span id="inpDate"></span>&nbsp;</th>
                                        </tr>
                                    </table>
                                </div>
                            </div>
							<div class="dataList">
                                <form id="register" action="wmsManageAction.do" method="post" name="register">
                                <input type="hidden" name="op" value="wwiPro"> 
								<input type="hidden" name="wwiId" value="${wmsWarIn.wwiId}">
                                <table id="rwpRow" class="normal rowstable noBr" style="width:100%" cellpadding="0" cellspacing="0" >
                                    <tr>
                                        <th style="width:32%" >产品名称/型号</th>
                                        <th style="width:10%" >产品编号</th>
                                        <th style="width:10%">数量</th>
                                        <th style="width:10%">单位</th>
                                        <th style="width:30%">备注</th>
                                        <th style="border-right:0px; width:8%" colspan="2">操作</th>
                                    </tr>
                                     <logic:notEmpty name="wmsWarIn">
                                     <logic:iterate id="rwp" name="wmsWarIn" property="RWinPros">
                                      <tr>
                                         <td>
                                         	${rwp.wmsProduct.wprName}<logic:notEmpty name="rwp" property="wmsProduct.wprModel">/${rwp.wmsProduct.wprModel}</logic:notEmpty>
                                         </td>
                                         <td>
                                         	${rwp.wmsProduct.wprCode}&nbsp;
                                         </td>
                                         <td>
                                         	<INPUT class="inputSize2" style="width:95%" type="text" name="${rwp.wmsProduct.wprId}num"  value="<bean:write name='rwp' property='rwiWinNum' format='0.00'/>" onBlur="checkIsNum(this)"/>
                                         </td>
                                         <td>${rwp.wmsProduct.typeList.typName}&nbsp;</td>
                                         <td>
                                         	<textarea rows="1" name="${rwp.wmsProduct.wprId}remark" style="width:95%" onBlur="autoShort(this,500)">${rwp.rwiRemark}&nbsp;</textarea>
                                         </td>
                                         <td>
                                         	&nbsp;<img src="images/content/del.gif" onClick="delTable(this,'rwpRow')" alt="删除" style="cursor:pointer"/>
                                        	<div style="display:none;"><INPUT type="checkbox" name="wprId"checked="checked" value="${rwp.wmsProduct.wprId}"></div>&nbsp;
                                         </td>			
                                      </tr>
                                      </logic:iterate>
                                    </logic:notEmpty>
                                    </table>
                                    <div style="text-align:center; margin-top:8px;">
                                    	<input type="button" id="dosave" onClick="save()" class="butSize3" value="保存明细">
                                    </div>
                                </form>
							</div>
                         </div>
					</td>
				</tr>
			</table>
		</div>
	</div>	
    <script type="text/javascript">
	 removeTime('inpDate','${wmsWarIn.wwiInpTime}',1);
  </script>
  
    
  </logic:notEmpty>
    <logic:empty name="wmsWarIn">
        <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该入库单已被删除</div>
	</logic:empty>
 </body>
</html>

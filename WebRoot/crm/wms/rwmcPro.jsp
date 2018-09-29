<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>仓库盘点明细</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript" src="js/formCheck.js"></script>
    <script type="text/javascript">       
		function check(){
		  	if($("rs")!=null){
				  if($("rs").innerText!=""||$("rs").innerText!=null)
				 {
					 alert("盘点产品不能为空!");
					 return false;		   
				  }
			}
			waitSubmit("dosave","保存中...");
			return $("wwo").submit();
							  
		}
		createProgressBar();
		window.onload=function(){
			if("${wmsCheck}"!=null&&"${wmsCheck}"!="")
				loadTabShort("tab");//表格内容省略
			closeProgressBar();
		}
  </script> 
  </head>
  
  <body>
  <logic:notEmpty name="wmsCheck">
   <div id="mainbox">
    	<div id="contentbox">
        	<div id="title">库存管理 > 库存盘点 > 盘点明细</div>
            <div class="descInf">
			<table class="dashTab descForm" cellpadding="0" cellspacing="0">
            	<thead>
                	<tr>
                        <th class="sysCodeL">盘点单号：</th>
                        <th colspan="3" class="sysCodeR">${wmsCheck.wmcCode}&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                	<tr>
                        <th>主题：</th>
                        <td colspan="3">${wmsCheck.wmcTitle}&nbsp;</td>				
                    </tr>
                    <tr>
                        <th>仓库：</th>
                        <td>${wmsCheck.wmsStro.wmsName}&nbsp;</td>
                        <th>填单人：</th>
                        <td>${wmsCheck.wmcInpName}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>盘点状态：</th>
                        <td colspan="3"><logic:equal value="0" name="wmsCheck" property="wmcState"><img src="images/content/database.gif" alt="盘点中" class="imgAlign"/>&nbsp;盘点中</logic:equal><logic:equal value="1" name="wmsCheck" property="wmcState"><img src="images/content/db_ok.gif" alt="盘点结束" class="imgAlign"/>&nbsp;盘点结束</logic:equal><logic:equal value="2" name="wmsCheck" property="wmcState"><img src="images/content/dbError.gif" alt="已撤销" class="imgAlign"/>&nbsp;已撤销</logic:equal>
                        </td>
                    </tr>
                    <tr>
                        <th>盘点人：</th>
                        <td>${wmsCheck.wmcOpman}&nbsp;</td>
                        <th>盘点时间：</th>
                        <td><span id="zhDate" title="执行时间"></span>&nbsp;</td>
                    </tr>
					<logic:equal value="2" name="wmsCheck" property="wmcState">
					<tr>
                        <th>撤销人：</th>
                        <td>${wmsCheck.wmcCanMan}&nbsp;</td>
                        <th>撤销时间：</th>
                        <td><span id="canDate" title="撤销时间"></span>&nbsp;</td>
                    </tr>
					</logic:equal>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3">${wmsCheck.wmcRemark}&nbsp;</td>
                    </tr>
                </tbody>
                <thead>
                    <tr>
                        <td colspan="4"><div>
                            盘点差异&nbsp;&nbsp;
                            <logic:notEqual value="1" name="wmsCheck" property="wmcAppIsok">
                                <span class="blackblue" style="padding:4px; cursor:pointer" onMouseOver="this.className='grayBack'" onMouseOut="this.className='blackblue'" onClick="wmsPopDiv(52,'${wmsCheck.wmcId}')"><img class="imgAlign" src="images/content/add.gif" alt="新建盘点产品"/></span>
                            </logic:notEqual>
                            <logic:equal value="1" name="wmsCheck" property="wmcAppIsok">
                                <span style="padding:4px;">
                                    <img class="imgAlign" src="images/content/lock.gif" alt="盘点单已审核通过，无法修改明细"/>
                                </span>
                            </logic:equal>
                        </div></td>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="subTab" colspan="4">
                            <table id="tab" class="rowstable" style="width:100%;" cellspacing="0" cellpadding="0">
                                <tr>
                                    <th style="width:28%">产品名称/型号[单位]</th>
                                    <th style="width:14%;">产品编号</th>
                                    <th style="width:10%">账面数量</th>
                                    <th style="width:10%">实际数量</th>
                                    <th style="width:10%">差异</th>
                                    <th style="width:8%">模式</th>
                                    <th style="width:12%">备注</th>
                                    <th style="width:8%;border-right:0px">操作</th>
                                </tr>
                                <logic:notEmpty name="wmsCheck" property="RWmsChanges">
                                <logic:iterate id="rwc" name="wmsCheck" property="RWmsChanges">
                                <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)">
                                    <td>&nbsp;<a href="wmsManageAction.do?op=wmsProDesc&wprId=${rwc.wmsProduct.wprId}" target="_blank" style="cursor:pointer">${rwc.wmsProduct.wprName}<logic:notEmpty name="rwc" property="wmsProduct.wprModel">/${rwc.wmsProduct.wprModel}</logic:notEmpty></a><logic:notEmpty name="rwc" property="wmsProduct.typeList"><span class="brown">[${rwc.wmsProduct.typeList.typName}]</span></logic:notEmpty>
                                    </td>
                                    <td>&nbsp;${rwc.wmsProduct.wprCode}</td>
                                    <td><bean:write name="rwc" property="rmcWmsCount" format="###,##0.00"/>&nbsp;</td>
                                    <td><bean:write name="rwc" property="rmcRealNum" format="###,##0.00"/>&nbsp;</td>
                                    <td><bean:write name="rwc" property="rwcDifferent" format="###,##0.00"/>&nbsp;</td>
                                    <td>
                                        <logic:equal value="0" name="rwc" property="rmcType">不计成本</logic:equal>
                                        <logic:equal value="1" name="rwc" property="rmcType">计算成本</logic:equal>
                                    </td>
                                    <td>${rwc.rmcRemark}&nbsp;</td>
                                    <td>
                                        <logic:notEqual value="1" name="wmsCheck" property="wmcAppIsok">
                                        <img onClick="wmsPopDiv(53,'${rwc.rwcId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑盘点产品"/>&nbsp;&nbsp;
                                        <img onClick="wmsDelDiv(12,'${rwc.rwcId}','${wmsCheck.wmcId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>
                                        </logic:notEqual>&nbsp;
                                   </td>			
                                </tr>
                                <script type="text/javascript">
                                    rowsBg('tr',<%=count%>);
                                </script>
                                <% count++; %>
                                </logic:iterate>
                                </logic:notEmpty>
                                <logic:empty name="wmsCheck" property="RWmsChanges">
                                    <tr>
                                       <td colspan="8" class="noDataTd"><div id="rs">未找到相关数据!</div></td>
                                   </tr>
                                 </logic:empty>
                            </table>
                        </td>
                    </tr>
                </tbody>
                <thead>
                	<tr>
                		<td colspan="4"><div>审核信息</div></td>
                    </tr>
                </thead>
                <tbody>
                	<tr>
                        <th>审核状态：</th>
                        <td><logic:notEmpty name="wmsCheck" property="wmcAppIsok">
                            <logic:equal value="0" name="wmsCheck" property="wmcAppIsok"><img class="imgAlign" src="images/content/fail.gif" alt="未通过">&nbsp;未通过</logic:equal><logic:equal value="2" name="wmsCheck" property="wmcAppIsok"><img class="imgAlign" src="images/content/time.gif" alt="审核中">&nbsp;审核中</logic:equal><logic:equal value="1" name="wmsCheck" property="wmcAppIsok"><img class="imgAlign" src="images/content/suc.gif" alt="已通过">&nbsp;已通过</logic:equal>
                        </logic:notEmpty>
                        <logic:empty name="wmsCheck" property="wmcAppIsok">
                            <span class="gray" title="未审核(请添加产品)">未审核</span>
                        </logic:empty>
                        <span id="wmcAppDate" title="审核时间" class="gray"></span>
                        </td>
                        <th>审核人：</th>
                        <td>${wmsCheck.wmcAppMan}&nbsp;</td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>审核内容：</th>
                        <td colspan="3">${wmsCheck.wmcAppDesc}&nbsp;</td>
                    </tr>
                   	<logic:notEqual value="1" name="wmsCheck" property="wmcAppIsok">
                   	<logic:notEmpty name="wmsCheck" property="RWmsChanges">
                        <tr>
                            <td class="subTab" colspan="4">
                                <div id="appCheckLayer" style="display:none">
                                    <div class="orangeBack" style="margin-bottom:5px"><input type="button" class="butSize3" value="审核盘点" onClick="$('appOrd').show()"/></div>
                                    <div id="appOrd" style="height:130px;width:100%; display:none; margin-top:5px;">
                                        <form action="wwoAction.do" method="post" id="app">
                                            <input type="hidden" name="op" value="appWmc">
                                            <input type="hidden" name="wmcId" value="${wmsCheck.wmcId}">
                                            <div class="orangeBox" style="text-align:left; padding:10px; padding-top:5px;">
                                                <div><input type="radio" name="wmcAppIsok" id="isok1" value="1"  checked="checked"><label for="isok1">通过&nbsp;</label>&nbsp;&nbsp;&nbsp;
                                                        <input type="radio" name="wmcAppIsok" id="isok2" value="0" ><label for="isok2">不通过</label>
                                                </div>
                                                <div class="bold gray middle" style="padding:5px; padding-top:10px;">审核内容</div>
                                                <div style="text-align:center"><textarea rows="4" style="width:100%" name="wmcAppDesc" onBlur="autoShort(this,4000)">${wmsCheck.wmcAppDesc}</textarea></div>
                                                <div style="text-align:center">
                                                    <input type="button" class="butSize1" id="dosave" value="提交">
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                                    <input type="button" class="butSize1" id="hiddenApp" value="取消">
                                                </div>
                                            </div>
                                        </form>
                                        <script type="text/javascript">
                                            $("dosave").onclick=function(){
                                                waitSubmit("dosave","保存中...");
                                                return $("app").submit();
                                            }
                                            $("hiddenApp").onclick=function(){
                                                $("appOrd").hide();
                                            }
                                        </script>
                                    </div>
                                </div>
                                <script type="text/javascript">
									displayLimAllow("w013","appCheckLayer");
								</script>
                            </td>
                        </tr>
                    </logic:notEmpty>
                    </logic:notEqual>
         		</tbody>
	  		</table>
            <div style="padding-top:8px; text-align:center;">
            	<logic:empty name="wmsCheck" property="RWmsChanges">
                    <div class="orangeBox" style="padding:10px; text-align:center;">
                        <span class="brown">请先添加盘点明细...</span>
                    </div>
                </logic:empty>
                <logic:equal value="1" name="wmsCheck" property="wmcAppIsok">
                	<logic:equal value="0" name="wmsCheck" property="wmcState">
                    	<div id="confirmLayer" class="orangeBack" style="display:none">
                            <form action="wwoAction.do" method="post" id="wwo" style="padding:0px; margin:0px">
                                <input type="hidden"  name="op" value="wmsCheck"/>
                                <input type="hidden" name="wmcId" value="${wmsCheck.wmcId}"/>	
                                <input type="button" class="butSize3" id="dosave" value="确认盘点" onClick="check()"/>
                            </form>
                        </div>
                    	<script type="text/javascript">
							displayLimAllow("w012","confirmLayer");
						</script>
                   	</logic:equal>
                    <logic:equal value="1" name="wmsCheck" property="wmcState">
                    	<div id="cancelCheckLayer" class="grayBg" style="display:none">
                            <input name="button" class="butSize3" type="button" onClick="wmsDelDiv(14,'${wmsCheck.wmcId}')" value="撤销库存盘点"/>
                        </div>
                   		<div class="grayBack" style="padding:10px; margin-top:5px;">
                            <span class="blue"><img src="images/content/bigSuc.gif" alt="已完成" style="vertical-align:middle"/>&nbsp;已确认盘点</span>
                        </div>
                        <script type="text/javascript">displayLimAllow("w014","cancelCheckLayer");</script>
                   	</logic:equal>
                    <logic:equal value="2" name="wmsCheck" property="wmcState">
                        <div class="redWarn" style="padding:10px; margin:0;">
                            <img src="images/content/bigFail.gif" alt="已撤销" style="vertical-align:middle"/>&nbsp;已撤销盘点
                        </div>	
                    </logic:equal>
                </logic:equal>
                <logic:equal value="2" name="wmsCheck" property="wmcAppIsok">
                    <div class="orangeBox" style="padding:10px;">
                        <span class="brown">该盘点申请正在审核中，审核通过方可确认盘点...</span>
                    </div>
                </logic:equal>
                <logic:equal value="0" name="wmsCheck" property="wmcAppIsok">
                    <div class="redWarn" style="padding:10px; margin:0;">
                        <span class="red">该盘点申请未通过审核...</span>
                    </div>
                </logic:equal>
            </div>  
            <div class="descStamp">
                由
                <span>${wmsCheck.wmcInpName}</span>
                于
                <span id="inpDate"></span>&nbsp;
                录入<logic:notEmpty name="wmsCheck" property="wmcAltName">，最近由
                <span>${wmsCheck.wmcAltName}</span>
                于
                <span id="changeDate"></span>&nbsp;
                修改
                </logic:notEmpty>
            </div>  
  		</div>
   </div>
  	<script type="text/javascript">							
		removeTime("zhDate","${wmsCheck.wmcDate}",2);
		removeTime("wmcAppDate","${wmsCheck.wmcAppDate}",2);	
		removeTime("inpDate","${wmsCheck.wmcInpDate}",2);
		removeTime("changeDate","${wmsCheck.wmcAltDate}",2);
		removeTime("canDate","${wmsCheck.wmcCanDate}",2);
	</script>
    </div>
    </logic:notEmpty>
      <logic:empty name="wmsCheck">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该盘点单已被删除</div>
	</logic:empty>
  </body>
</html>

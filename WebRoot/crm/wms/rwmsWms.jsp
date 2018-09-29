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
    
    <title>仓库调拨明细</title>
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
		function checkRs(){
			var mg='${msg}';
			if(mg!=null && mg!=""){
				alert(mg);       
			}
		}
		function showDiv(){
			$("wunDiv").style.display="block";
		}
		function checkMat(){
			var name=$("wwoName");
			if(name==null||name.value==""){
				//$("error").innerText="请填写调出领料人。";
				alert("请填写调出领料人！");
				return false;
			}
			return window.location.href='wwoAction.do?op=wchCheckOut&wchId=${wmsChange.wchId}&wmsCode=${wmsCode}&wchMatName='+encodeURIComponent(name.value);			  
		}
		
		function showButton(){
			var in1=$("in1");
			var in2=$("in2");
			var out1=$("out1");
			var out2=$("out2");
			//未出库
			if("${wmsChange.wchState}"=="0"){
				if(out1!=null)
					out1.disabled=false;
				if(out2!=null)
					out2.disabled=false;
				
				if(in1!=null)
					in1.value="产品尚未出库，请等待出库...";
				if(in2!=null)
					in2.value="产品尚未出库，请等待出库...";
			}
			//已出库，未入库
			else if("${wmsChange.wchState}"=="2"){
				if(in1!=null)
					in1.disabled=false;
				if(in2!=null)
					in2.disabled=false;
					
				if(out1!=null)
					out1.value="产品已出库，请等待确认入库...";
				if(out2!=null)
					out2.value="产品已出库，请等待确认入库...";
			}
			//完成
			else if("${wmsChange.wchState}"=="3"){
				$("endDiv").style.display="block";
			}
		}
		
		createProgressBar();
		window.onload=function(){
			if("${wmsChange}"!=null&&"${wmsChange}"!=""){
				checkRs();
				loadTabShort("tab");//表格内容省略
				//showButton();
			}
			closeProgressBar();
		}
  </script> 
  </head>
  
  <body>
      <logic:notEmpty name="wmsChange">
       	<div id="mainbox">
            <div id="contentbox">
                <div id="title">库存管理 > 库间调拨 > 调拨单明细</div>
                <div class="descInf">
                <table class="dashTab descForm" cellpadding="0" cellspacing="0">
                	<thead>
                    	<tr>
                            <th class="sysCodeL">调拨单号：</th>
                            <th class="sysCodeR" colspan="3">${wmsChange.wchCode}&nbsp;</th>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                            <th>主题：</th>
                            <td colspan="3">${wmsChange.wchTitle}&nbsp;</td>				
                        </tr>
                        <tr>
                            <th id="stroOut">调出仓库：</th>
                            <td>${wmsChange.wmsStroByWchOutWms.wmsName}&nbsp;</td>
                            <th>预计调出日期：</th>
                            <td><span id="outDate"></span>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>调出人：</th>
                            <td>${wmsChange.checkOutName}&nbsp;</td>
                            <th>调出时间：</th>
                            <td><span id="wchOutTime" title="调出时间"></span>&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>调出领料人：</th>
                            <td colspan="3">${wmsChange.wchMatName}&nbsp;</td>
                        </tr>
                        <tr>
                            <th id="stroIn">调入仓库：</th>
                            <td>${wmsChange.wmsStroByWchInWms.wmsName}&nbsp;</td>
                            <th>预计调入日期：</th>
                            <td><span id="inDate1"></span>&nbsp;</td>
                        </tr>
                        <tr>
                            <th>调入人：</th>
                            <td>${wmsChange.checkInName}&nbsp;</td>
                            <th>调入时间：</th>
                            <td><span id="wchInTime" title="调入时间"></span>&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>调拨状态：</th>
                            <td><logic:equal value="0" name="wmsChange" property="wchState"><img src="images/content/database.gif" class="imgAlign" alt="未执行"/>&nbsp;未执行</logic:equal><logic:equal value="2" name="wmsChange" property="wchState"><img src="images/content/database_go.gif" class="imgAlign" alt="已出库"/>&nbsp;已出库</logic:equal><logic:equal value="3" name="wmsChange" property="wchState"><img src="images/content/db_ok.gif" class="imgAlign" alt="已入库"/>&nbsp;已入库</logic:equal><logic:equal value="4" name="wmsChange" property="wchState"><img src="images/content/dbError.gif" alt="已撤销" class="imgAlign"/>&nbsp;已撤销</logic:equal>
                            </td>
                            <th>填单人：</th>
                            <td>${wmsChange.wchInpName}&nbsp;</td>
                        </tr>
						<logic:equal value="4" name="wmsChange" property="wchState">
						<tr>
							<th>撤销人：</th>
							<td>${wmsChange.wchCanMan}&nbsp;</td>
                            <th>撤销时间：</th>
                            <td><span id="canDate" title="撤销时间"></span>&nbsp;</td>
						</tr>
						</logic:equal>
                        <tr class="noBorderBot">
                            <th>备注：</th>
                            <td colspan="3">${wmsChange.wchRemark}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                            <td colspan="4"><div>
                                调拨明细&nbsp;&nbsp;
                                <logic:notEqual value="1" name="wmsChange" property="wchAppIsok">
                                    <span class="blackblue" style="padding:4px; cursor:pointer" onMouseOver="this.className='grayBack'" onMouseOut="this.className='blackblue'" onClick="wmsPopDiv(42,'${wmsChange.wchId}')"><img class="imgAlign" src="images/content/add.gif" alt="新建调拨产品"/></span>
                                </logic:notEqual>
                                <logic:equal value="1" name="wmsChange" property="wchAppIsok">
                                	<span style="padding:4px;">
                                        <img class="imgAlign" src="images/content/lock.gif" alt="调拨单已审核通过，无法修改明细"/>
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
                                        <th style="width:40%;">&nbsp;产品名称/型号</th>
                                        <th style="width:18%;">&nbsp;产品编号</th>
                                        <th style="width:14%;">&nbsp;调拨数量[单位]</th>
                                        <th style="width:20%;">&nbsp;备注</th>
                                        <th style="width:8%;border-right:0px">操作</th>
                                    </tr>
                                    <logic:notEmpty name="wmsChange" property="RWmsWmses">
                                        <logic:iterate id="rww" name="wmsChange" property="RWmsWmses">
                                        <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)">
                                            <td>&nbsp;<a href="wmsManageAction.do?op=wmsProDesc&wprId=${rww.wmsProduct.wprId}" target="_blank" style="cursor:pointer">${rww.wmsProduct.wprName}<logic:notEmpty name="rww" property="wmsProduct.wprModel">/${rww.wmsProduct.wprModel}</logic:notEmpty></a>
                                            </td>
                                            <td>&nbsp;${rww.wmsProduct.wprCode}</td>
                                            <td><bean:write name="rww" property="rwwNum" format="###,##0.00"/>&nbsp;
                                            <logic:notEmpty name="rww" property="wmsProduct.typeList">[${rww.wmsProduct.typeList.typName}]</logic:notEmpty>
                                            </td>
                                            <td>${rww.rwwRemark}&nbsp;</td>
                                            <td>
                                            <logic:notEqual value="1" name="wmsChange" property="wchAppIsok">
                                            <img onClick="wmsPopDiv(43,'${rww.rwwId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑调拨产品"/>&nbsp;&nbsp;
                                            <img onClick="wmsDelDiv(11,'${rww.rwwId}','${wmsChange.wchId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>
                                            </logic:notEqual>&nbsp;
                                            </td>			
                                        </tr>
                                        <script type="text/javascript">
                                            rowsBg('tr',<%=count%>);
                                         </script>
                                        <% count++; %>
                                        </logic:iterate>
                                        </logic:notEmpty>
                                        <logic:empty name="wmsChange" property="RWmsWmses">
                                            <tr>
                                                <td colspan="5" class="noDataTd">未找到相关数据!</td>
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
                            <td><logic:notEmpty name="wmsChange" property="wchAppIsok">
                                <logic:equal value="0" name="wmsChange" property="wchAppIsok"><img class="imgAlign" src="images/content/fail.gif" alt="未通过">&nbsp;未通过</logic:equal><logic:equal value="2" name="wmsChange" property="wchAppIsok"><img class="imgAlign" src="images/content/time.gif" alt="审核中">&nbsp;审核中</logic:equal><logic:equal value="1" name="wmsChange" property="wchAppIsok"><img class="imgAlign" src="images/content/suc.gif" alt="已通过">&nbsp;已通过</logic:equal>
                            </logic:notEmpty>
                            <logic:empty name="wmsChange" property="wchAppIsok">
                                <span class="gray" title="未审核(请添加产品)">未审核</span>
                            </logic:empty>
                            <span id="wchAppDate" title="审核时间" class="gray"></span>
                            </td>
                            <th>审核人：</th>
                            <td>${wmsChange.wchAppMan}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>审核内容：</th>
                            <td colspan="3">${wmsChange.wchAppDesc}&nbsp;</td>
                        </tr>
                        <logic:notEqual value="1" name="wmsChange" property="wchAppIsok">
                        <logic:notEmpty name="wmsChange" property="RWmsWmses">
                            <tr>
                                <td class="subTab" colspan="4">
                                	<div id="appWchLayer" style="display:none">
                                        <div class="orangeBack" style="margin-bottom:5px"><input type="button" class="butSize3" value="审核调拨单" onClick="$('appOrd').show()"/></div>
                                        <div id="appOrd" style="height:130px;width:100%; display:none; margin-top:5px;">
                                            <form action="wwoAction.do" method="post" id="app">
                                                <input type="hidden" name="op" value="appWch">
                                                <input type="hidden" name="wchId" value="${wmsChange.wchId}">
                                                <input type="hidden" name="wmsCode" value="${wmsCode}">
                                                <div class="orangeBox" style="text-align:left; padding:10px; padding-top:5px;">
                                                    <div><input type="radio" name="wchAppIsok" id="isok1" value="1"  checked="checked"><label for="isok1">通过&nbsp;</label>&nbsp;&nbsp;&nbsp;
                                                            <input type="radio" name="wchAppIsok" id="isok2" value="0" ><label for="isok2">不通过</label>
                                                    </div>
                                                    <div class="bold gray middle" style="padding:5px; padding-top:10px;">审核内容</div>
                                                    <div style="text-align:center"><textarea rows="4" style="width:100%" name="wchAppDesc" onBlur="autoShort(this,4000)">${wmsChange.wchAppDesc}</textarea></div>
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
                                        displayLimAllow("w010","appWchLayer");
                                    </script>
                                </td>
                             </tr>
                        </logic:notEmpty>
                        </logic:notEqual>
                    </tbody>
                </table>
                <div style="padding-top:8px; text-align:center;">
                	<logic:empty name="wmsChange" property="RWmsWmses">
                        <div class="orangeBox brown" style="padding:10px; text-align:center;">
                            请先添加调拨明细...
                        </div>
                    </logic:empty>
                    <logic:equal value="1" name="wmsChange" property="wchAppIsok">
                    	<!-- 未出库 -->
                        <logic:equal value="0" name="wmsChange" property="wchState">
                            <div id="confirmOutLayer" style="display:none;">
                            	<div class="orangeBack">
                                <logic:notEmpty name="wmsCode">
                                <logic:equal value='${wmsCode}' name='wmsChange' property='wmsStroByWchOutWms.wmsCode'>
                                    <input name="button" class="butSize3" type="button" onClick="showDiv()" value="调出库管确认"/>
                                </logic:equal>
                                </logic:notEmpty>
                                <logic:empty name="wmsCode">
                                    <input name="button" class="butSize3" type="button" onClick="showDiv()" value="调出库管确认"/>
                                </logic:empty>
                                </div>
                                <div id="wunDiv" class="orangeBox" style="display:none;width:100%; text-align:center; height:40px; padding:5px; margin-top:4px;">
                                    <img src="images/content/warn.gif"  alt="提醒" style="vertical-align:middle"/>&nbsp;请填写调出领料人：<input type="text"  onblur="autoShort(this,50)" name="wchMatName" class="inputSize2 inputBoxAlign" id="wwoName" value="${wmsChange.wchMatName}"/>&nbsp;&nbsp;&nbsp;
                                    <input type="button" class="butSize3 inputBoxAlign" id="dosave"  onClick="checkMat()" value="确认调出">
                                </div>
                            </div>
                            <script type="text/javascript">displayLimAllow("w009","confirmOutLayer");</script>
                        	<div class="grayBack blue" style="padding:10px; margin-top:5px;">
                                产品尚未出库，请等待出库...
                            </div>
                        </logic:equal>
                        <!-- 已出库 -->
                        <logic:equal value="2" name="wmsChange" property="wchState">
                            <div class="orangeBack">
                                <span id="confirmInLayer" style="display:none">
                                    <logic:notEmpty name="wmsCode">
                                    <logic:equal value='${wmsCode}' name='wmsChange' property='wmsStroByWchInWms.wmsCode'>
                                        <input name="button" class="butSize3" type="button" onClick="window.location.href='wwoAction.do?op=wchCheckIn&wchId=${wmsChange.wchId}&wmsCode=${wmsCode}'" value="调入库管确认"/>
                                    </logic:equal>
                                    </logic:notEmpty>
                                    <logic:empty name="wmsCode">
                                        <input name="button" class="butSize3" type="button" onClick="window.location.href='wwoAction.do?op=wchCheckIn&wchId=${wmsChange.wchId}&wmsCode=${wmsCode}'" value="调入库管确认"/>
                                    </logic:empty>
                                 </span>
                                 <span id="cancelChangeLayer" style="display:none">
                                 	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <input name="button" class="butSize3" type="button" onClick="wmsDelDiv(15,'${wmsChange.wchId}')" value="撤销库间调拨"/>
                                </span>
                            </div>
                        	<div class="grayBack blue" style="padding:10px; margin-top:5px;">
                               	产品已出库，请等待确认入库...
                            </div>
                            <script type="text/javascript">displayLimAllow("w009|w011",new Array("confirmInLayer","cancelChangeLayer"));</script>
                        </logic:equal>
                        <!-- 已入库 -->
                        <logic:equal value="3" name="wmsChange" property="wchState">
                        	<div id="cancelChangeLayer" class="grayBg" style="display:none">
                        		<input name="button" class="butSize3" type="button" onClick="wmsDelDiv(10,'${wmsChange.wchId}')" value="撤销库间调拨"/>
                            </div>
                        	<div class="grayBack blue" style="padding:10px; margin-top:5px">
                                <img src="images/content/bigSuc.gif" alt="已完成" style="vertical-align:middle"/>&nbsp;已完成调拨
                            </div>
                            <script type="text/javascript">displayLimAllow("w011","cancelChangeLayer");</script>
                        </logic:equal>
                        <logic:equal value="4" name="wmsChange" property="wchState">
                        	<div class="redWarn" style="padding:10px; margin:0;">
                                <img src="images/content/bigFail.gif" alt="已撤销" style="vertical-align:middle"/>&nbsp;已撤销调拨
                            </div>
                        </logic:equal>
                    </logic:equal>
                    <logic:equal value="2" name="wmsChange" property="wchAppIsok">
                        <div class="orangeBox brown" style="padding:10px;">
                        	该调拨申请正在审核中，审核通过方可开始调拨...
                        </div>
                    </logic:equal>
                    <logic:equal value="0" name="wmsChange" property="wchAppIsok">
                        <div class="redWarn" style="padding:10px; margin:0;">
                        	该调拨申请未通过审核...
                        </div>
                    </logic:equal>
                </div>
            	
                 <div class="descStamp">
                    由
                    <span>${wmsChange.wchInpName}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<logic:notEmpty name="wmsChange" property="wchAltName">，最近由
                    <span>${wmsChange.wchAltName}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </logic:notEmpty>
                </div> 
			</div>
            <script type="text/javascript">
				removeTime("outDate","${wmsChange.wchOutDate}",1);
				removeTime("inDate1","${wmsChange.wchInDate}",1);
				removeTime("inpDate","${wmsChange.wchInpDate}",2);
				removeTime("changeDate","${wmsChange.wchAltDate}",2);	
				removeTime("wchInTime","${wmsChange.wchInTime}",2);
				removeTime("wchOutTime","${wmsChange.wchOutTime}",2);
				removeTime("wchAppDate","${wmsChange.wchAppDate}",2);
				removeTime("canDate","${wmsChange.wchCanDate}",2);	
				if("${wmsCode}"=="${wmsChange.wmsStroByWchOutWms.wmsCode}"){
					$("stroOut").className="orangeBack";
					$("stroOut").title="当前仓库";
				}
				else if("${wmsCode}"=="${wmsChange.wmsStroByWchInWms.wmsCode}"){
					$("stroIn").className="orangeBack";
					$("stroIn").title="当前仓库";
				}
				
				
             </script>
            </div>
        </div>
     </logic:notEmpty>
     <logic:empty name="wmsChange">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该调拨单已被删除</div>
	</logic:empty>
  </body>
</html>

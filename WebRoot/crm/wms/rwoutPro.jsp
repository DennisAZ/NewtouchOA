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
    
    <title>出库单明细</title>
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
		function update(){
			var ms='${msg1}'
			if(ms!=null && ms!=""){
				alert(ms);
			}
		}
		function showDiv(){
			$("wunDiv").style.display="block";
		}

		function checkRs(){	
			if($("isEmpty")!=null){
				  if($("isEmpty").innerText!=""||$("isEmpty")!=null){
					 alert("请添加出库产品!");
					 return false;		   
				  }
			}
			if($("wwoName")==null||$("wwoName").value==""){
				//$("error").innerText="请填写领料人。";
				alert("请填写调出领料人！");
				return false;
			}
			waitSubmit("dosave","保存中...");
			return $("wwo").submit();			  
		}
		
		createProgressBar();
		window.onload=function(){
			if("${wmsWarOut}"!=null&&"${wmsWarOut}"!=""){
				update();
				//表格内容省略
				loadTabShort("tab");
			}
			closeProgressBar();
		}
  </script> 
  </head>
  
  <body>
   <logic:notEmpty name="wmsWarOut">
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title">库存管理 > 出库管理 > 出库明细</div>
            <div class="descInf">
			<table class="dashTab descForm" cellpadding="0" cellspacing="0">
            	<thead>
                	<tr>
                        <th class="sysCodeL">出库单号：</th>
                        <th class="sysCodeR" colspan="3">${wmsWarOut.wwoCode}&nbsp;</th>
                    </tr>
                </thead>
            	<tbody>
                	<tr>
                        <th>主题：</th>
                        <td>${wmsWarOut.wwoTitle}&nbsp;</td>
                        <th>对应订单：</th>
                        <td class="mlink">
                        <logic:notEmpty name="wmsWarOut" property="salOrdCon">
                        <a href="orderAction.do?op=showOrdDesc&code=${wmsWarOut.salOrdCon.sodCode}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看订单详情" style="border:0px;">${wmsWarOut.salOrdCon.sodTil}</a>&nbsp;</logic:notEmpty>&nbsp;
                        </td>			
                    </tr>
                    <tr>
                        <th>仓库：</th>
                        <td>${wmsWarOut.wmsStro.wmsName}&nbsp;</td>
                        <th>填单人：</th>
                        <td>${wmsWarOut.wwoInpName}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>出库状态：</th>
                        <td><logic:equal value="0" name="wmsWarOut" property="wwoState"><img src="images/content/database.gif" alt="未出库" class="imgAlign"/>&nbsp;未出库</logic:equal><logic:equal value="1" name="wmsWarOut" property="wwoState"><img src="images/content/db_ok.gif" alt="已出库" class="imgAlign"/>&nbsp;已出库</logic:equal><logic:equal value="2" name="wmsWarOut" property="wwoState"><img src="images/content/db_ok.gif" alt="已发货" class="imgAlign"/>&nbsp;已发货</logic:equal><logic:equal value="3" name="wmsWarOut" property="wwoState"><img src="images/content/dbError.gif" alt="已撤销" class="imgAlign"/>&nbsp;已撤销</logic:equal>
                        </td>
                        <th>领料人：</th>
                        <td>${wmsWarOut.wwoUserName}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>经手人：</th>
                        <td>${wmsWarOut.wwoResName}&nbsp;</td>
                        <th>出库时间：</th>
                        <td><span id="outDate" title="出库时间"></span>&nbsp;</td>
                    </tr>
					<logic:equal value="3" name="wmsWarOut" property="wwoState">
						<tr>
							<th>撤销人：</th>
							<td>${wmsWarOut.wwoCanMan}&nbsp;</td>
                            <th>撤销时间：</th>
                            <td><span id="canDate" title="撤销日期"></span>&nbsp;</td>
						</tr>
					</logic:equal>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3">${wmsWarOut.wwoRemark}&nbsp;</td>
                    </tr>
                </tbody>
                <thead>
                	<tr>
                        <td colspan="4"><div>
                            出库明细&nbsp;&nbsp;
                            <logic:notEqual value="1" name="wmsWarOut" property="wwoAppIsok">
                                <logic:empty name="wmsWarOut" property="salOrdCon">
                                <span class="blackblue" style="padding:4px; cursor:pointer" onMouseOver="this.className='grayBack'" onMouseOut="this.className='blackblue'" onClick="wmsPopDiv(32,'${wmsWarOut.wwoId}')"><img class="imgAlign" src="images/content/add.gif" alt="新建出库产品"/></span>
                                </logic:empty>
                                <logic:notEmpty name="wmsWarOut" property="salOrdCon">
                                	<span style="padding:4px;">
                                        <img class="imgAlign" src="images/content/lock.gif" alt="此出库单由订单生成，无法修改明细"/>
                                    </span>
                                </logic:notEmpty>
                            </logic:notEqual>
                            <logic:equal value="1" name="wmsWarOut" property="wwoAppIsok">
                            	<span style="padding:4px;">
                                    <img class="imgAlign" src="images/content/lock.gif" alt="出库单已审核通过，无法修改明细"/>
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
                                    <th style="width:14%;">&nbsp;出库数量[单位]</th>
                                    <th style="width:20%;">&nbsp;备注</th>
                                    <th style="width:8%;border-right:0px">操作</th>
                                </tr>
                                <logic:notEmpty name="wmsWarOut" property="RWoutPros">
                                <logic:iterate id="rwp" name="wmsWarOut" property="RWoutPros">
                                <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)">
                                    <td>&nbsp;<a href="wmsManageAction.do?op=wmsProDesc&wprId=${rwp.wmsProduct.wprId}" target="_blank" style="cursor:pointer">${rwp.wmsProduct.wprName}<logic:notEmpty name="rwp" property="wmsProduct.wprModel">/${rwp.wmsProduct.wprModel}</logic:notEmpty></a>
                                    </td>
                                    <td>&nbsp;${rwp.wmsProduct.wprCode}</td>
                                    <td><bean:write name="rwp" property="rwoWoutNum" format="###,##0.00"/>&nbsp;
                                    <logic:notEmpty name="rwp" property="wmsProduct.typeList">[${rwp.wmsProduct.typeList.typName}]</logic:notEmpty></td>
                                    <td>${rwp.rwoRemark}&nbsp;</td>
                                    <td>
                                    <logic:notEqual value="1" name="wmsWarOut" property="wwoAppIsok">
                                    <logic:empty name="wmsWarOut" property="salOrdCon">
                                    <img onClick="wmsPopDiv(33,'${rwp.rwoId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑出库产品"/>&nbsp;&nbsp;
                                    <img onClick="wmsDelDiv(8,'${rwp.rwoId}','${wmsWarOut.wwoId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>	
                                    </logic:empty>
                                    </logic:notEqual>&nbsp;
                                    </td>			
                                </tr>
                                <script type="text/javascript">
                                    rowsBg('tr',<%=count%>);
                                 </script>
                                <% count++; %>
                                </logic:iterate>
                                </logic:notEmpty>
                                <logic:empty name="wmsWarOut" property="RWoutPros">
                                    <tr>
                                        <td colspan="5" class="noDataTd"><div id="isEmpty">未找到相关数据!</div></td>
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
                        <td><logic:notEmpty name="wmsWarOut" property="wwoAppIsok">
                            <logic:equal value="0" name="wmsWarOut" property="wwoAppIsok"><img class="imgAlign" src="images/content/fail.gif" alt="未通过">&nbsp;未通过</logic:equal><logic:equal value="2" name="wmsWarOut" property="wwoAppIsok"><img class="imgAlign" src="images/content/time.gif" alt="审核中">&nbsp;审核中</logic:equal><logic:equal value="1" name="wmsWarOut" property="wwoAppIsok"><img class="imgAlign" src="images/content/suc.gif" alt="已通过">&nbsp;已通过</logic:equal>
                        </logic:notEmpty>
                        <logic:empty name="wmsWarOut" property="wwoAppIsok">
                            <span class="gray" title="未审核(请添加产品)">未审核</span>
                        </logic:empty>
                        <span id="wwiAppDate" title="审核时间" class="gray"></span>
                        </td>
                        <th>审核人：</th>
                        <td>${wmsWarOut.wwoAppMan}&nbsp;</td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>审核内容：</th>
                        <td colspan="3">${wmsWarOut.wwoAppDesc}&nbsp;</td>
                    </tr>
                    <logic:notEqual value="1" name="wmsWarOut" property="wwoAppIsok">
                    <logic:notEmpty name="wmsWarOut" property="RWoutPros">
                        <tr>
                            <td class="subTab" colspan="4">
                            	<div id="appWwoLayer" style="display:none">
                                    <div class="orangeBack" style="margin-bottom:5px"><input type="button" class="butSize3" value="审核出库单" onClick="$('appOrd').show()"/></div>
                                    <div id="appOrd" style="height:130px;width:100%; display:none; margin-top:5px;">
                                        <form action="wwoAction.do" method="post" id="app">
                                            <input type="hidden" name="op" value="appWwo">
                                            <input type="hidden" name="wwoId" value="${wmsWarOut.wwoId}">
                                            <div class="orangeBox" style="text-align:left; padding:10px; padding-top:5px;">
                                                <div><input type="radio" name="wwoAppIsok" id="isok1" value="1"  checked="checked"><label for="isok1">通过&nbsp;</label>&nbsp;&nbsp;&nbsp;
                                                        <input type="radio" name="wwoAppIsok" id="isok2" value="0" ><label for="isok2">不通过</label>
                                                </div>
                                                <div class="bold gray middle" style="padding:5px; padding-top:10px;">审核内容</div>
                                                <div style="text-align:center"><textarea rows="4" style="width:100%" name="wwoAppDesc" onBlur="autoShort(this,4000)">${wmsWarOut.wwoAppDesc}</textarea></div>
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
									displayLimAllow("w007","appWwoLayer");
								</script>
                            </td>
                        </tr>
                   	</logic:notEmpty>
					</logic:notEqual>
                </tbody>
	  		</table>
            <div style="text-align:center;">
            	<logic:empty name="wmsWarOut" property="RWoutPros">
                    <div class="orangeBox" style="padding:10px; text-align:center;">
                        <span class="brown">请先添加出库明细...</span>
                    </div>
                </logic:empty>
                <logic:equal value="1" name="wmsWarOut" property="wwoAppIsok">
                	<logic:equal value="0" name="wmsWarOut" property="wwoState">
                    	<div id="confirmLayer" style="display:none">
                            <form action="wwoAction.do" method="post" id="wwo" style="padding:0px; margin:0px">
                                <div class="orangeBack">
                                    <input type="hidden"  name="op" value="outWmsStro"/>
                                    <input type="hidden" name="wmsCode" value="${wmsWarOut.wmsStro.wmsCode}">
                                    <input type="hidden" name="wwoId" value="${wmsWarOut.wwoId}">
                                    <input type="button" class="butSize3" value="确认出库" onClick="showDiv()"/>			
                                </div>
                                <div id="wunDiv" class="orangeBox" style="display:none;width:100%; height:40px; padding:5px; margin-top:5px;">
                                    <img src="images/content/warn.gif" alt="提醒" style="vertical-align:middle"/>&nbsp;请填写领料人：<input type="text" name="wwoUserName" onBlur="autoShort(this,50)" class="inputSize2 inputBoxAlign" id="wwoName" value="${wmsWarOut.wwoUserName}">&nbsp;&nbsp;&nbsp;
                                    <input type="button" class="butSize3 inputBoxAlign" id="dosave"  onClick="checkRs()" value="确认出库">
                                    <span id="error" style="color:#FF0000;"></span>
                                </div>		
                            </form>
                        </div>
                        <script type="text/javascript">
							displayLimAllow("w006","confirmLayer");
						</script>
                    </logic:equal>
                    <logic:equal value="1" name="wmsWarOut" property="wwoState">
                    	<div id="cancelOutLayer" class="grayBg" style="display:none">
                            <input name="button" class="butSize3" type="button" onClick="wmsDelDiv(7,'${wmsWarOut.wwoId}')" value="撤销出库"/>
                        </div>
                   		<div class="grayBack" style="padding:10px; margin-top:5px;">
                            <span class="blue"><img src="images/content/bigSuc.gif" alt="已确认" style="vertical-align:middle"/>&nbsp;已确认出库</span>
                        </div>
                        <script type="text/javascript">displayLimAllow("w008","cancelOutLayer");</script>
                   </logic:equal>
                   <logic:equal value="3" name="wmsWarOut" property="wwoState">
                   		<div class="redWarn" style="padding:10px; margin:0">
                            <img src="images/content/bigFail.gif" alt="已撤销" style="vertical-align:middle"/>&nbsp;已撤销出库
                        </div>
                   </logic:equal>
                </logic:equal>
                <logic:equal value="2" name="wmsWarOut" property="wwoAppIsok">
                    <div class="orangeBox" style="padding:10px;">
                        <span class="brown">该出库申请正在审核中，审核通过方可确认出库...</span>
                    </div>
                </logic:equal>
                <logic:equal value="0" name="wmsWarOut" property="wwoAppIsok">
                    <div class="redWarn" style="padding:10px; margin:0;">
                        <span class="red">该出库申请未通过审核...</span>
                    </div>
                </logic:equal>
            </div>
            <div class="descStamp">
                由
                <span>${wmsWarOut.wwoInpName}</span>
                于
                <span id="inpDate"></span>&nbsp;
                录入<logic:notEmpty name="wmsWarOut" property="wwoAltName">，最近由
                <span>${wmsWarOut.wwoAltName}</span>
                于
                <span id="changeDate"></span>&nbsp;
                修改
                </logic:notEmpty>
            </div> 
     	</div>
  		</div>
		<script type="text/javascript">	
            removeTime("inpDate","${wmsWarOut.wwoInpDate}",2);
            removeTime("changeDate","${wmsWarOut.wwoAltDate}",2);
            removeTime("outDate","${wmsWarOut.wwoOutDate}",2);
            removeTime("wwoAppDate","${wmsWarOut.wwoAppDate}",2);
			removeTime("canDate","${wmsWarOut.wwoCanDate}",2);
        </script>	
    </div>
    </logic:notEmpty>
     <logic:empty name="wmsWarOut">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该出库单已被删除</div>
	</logic:empty>
  </body>
</html>

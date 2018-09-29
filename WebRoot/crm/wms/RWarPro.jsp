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
    
    <title>入库单明细</title>
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
		  	if($("wwi")!=null){
				  if($("wwi").innerText!=""||$("wwi").innerText!=null){
					 alert("请添加入库产品!");
					 return false;		   
				  }
			}
			waitSubmit("dosave","保存中...");
			return $("register").submit();
							  
		}

		createProgressBar();
		window.onload=function(){
			if("${wmsWarIn}"!=null&&"${wmsWarIn}"!="")
				loadTabShort("tab");//表格内容省略
			closeProgressBar();
		}
  </script> 
  </head>
  <body>
  	<logic:notEmpty name="wmsWarIn">
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title">库存管理 > 入库管理 > 入库单明细</div>
            <div class="descInf">
			<table class="dashTab descForm" cellpadding="0" cellspacing="0">
            	<thead>
                	<tr>
                        <th class="sysCodeL">入库单号：</th>
                        <th class="sysCodeR" colspan="3">${wmsWarIn.wwiCode}&nbsp;</th>
                    </tr>
                </thead>
                <tbody>
                	<tr>
                        <th>主题：</th>
                        <td>${wmsWarIn.wwiTitle}&nbsp;</td>
                        <th>对应采购单：</th>
                        <td class="mlink">
                        <logic:notEmpty name="wmsWarIn" property="salPurOrd"><a href="salPurAction.do?op=spoDesc&spoId=${wmsWarIn.salPurOrd.spoId}" target="_blank" style=" cursor:pointer"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看采购单详情" style="border:0px;">${wmsWarIn.salPurOrd.spoTil}</a>
                        </logic:notEmpty>&nbsp;
                        </td>				
                    </tr>
                    <tr>
                        <th>仓库：</th>
                        <td>${wmsWarIn.wmsStro.wmsName}&nbsp;</td>
                        <th>填单人：</th>
                        <td>${wmsWarIn.wwiInpName}&nbsp;</td>
                    </tr>
                    <tr>
                        <th>入库状态：</th>
                        <td colspan="3"><logic:equal value="0" name="wmsWarIn" property="wwiState"><img src="images/content/database.gif" alt="未入库" class="imgAlign"/>&nbsp;未入库</logic:equal><logic:equal value="1" name="wmsWarIn" property="wwiState"><img src="images/content/db_ok.gif" alt="已入库" class="imgAlign"/>&nbsp;已入库</logic:equal><logic:equal value="2" name="wmsWarIn" property="wwiState"><img src="images/content/dbError.gif" alt="已撤销" class="imgAlign"/>&nbsp;已撤销</logic:equal>
                        </td>
                    </tr>
                    <tr>
                    	<th>经手人：</th>
                        <td>${wmsWarIn.wwiOpman}&nbsp;</td>
                    	<th>入库时间：</th>
                        <td><span id="inDate" title="入库时间"></span>&nbsp;</td>
                    </tr>
					<logic:equal value="2" name="wmsWarIn" property="wwiState">
						<tr>
							<th>撤销人：</th>
							<td>${wmsWarIn.wwiCanMan}&nbsp;</td>
                            <th>撤销时间：</th>
                            <td><span id="canDate" title="撤销时间"></span>&nbsp;</td>
						</tr>
					</logic:equal>
                    <tr class="noBorderBot">
                        <th>备注：</th>
                        <td colspan="3">${wmsWarIn.wwiRemark}&nbsp;</td>
                    </tr>
                </tbody>
                <thead>
                	<tr>
                        <td colspan="4">
                        	<div>
                            	入库明细&nbsp;&nbsp;
                                <logic:notEqual value="1" name="wmsWarIn" property="wwiAppIsok">
                                    <logic:empty name="wmsWarIn" property="salPurOrd">
                                    <span style="padding:4px; cursor:pointer" onMouseOver="this.className='grayBack'" onMouseOut="this.className='blackblue'" onClick="window.open('wmsManageAction.do?op=toWwiPro&wwiId=${wmsWarIn.wwiId}')"><img class="imgAlign" src="images/content/edit.gif" alt="编辑入库明细"/></span>
                                    </logic:empty>
                                    <logic:notEmpty name="wmsWarIn" property="salPurOrd">
                                    <span style="padding:4px;">
                                        <img class="imgAlign" src="images/content/lock.gif" alt="此入库单由采购单生成，无法修改明细"/>
                                    </span>
                                    </logic:notEmpty>
                                </logic:notEqual>
                                <logic:equal value="1" name="wmsWarIn" property="wwiAppIsok">
                                	<span style="padding:4px;">
                                        <img class="imgAlign" src="images/content/lock.gif" alt="入库单已审核通过，无法修改明细"/>
                                    </span>
                                </logic:equal>
                            </div>
                    	</td>
                   	</tr>
                </thead>
                <tbody>
                	<tr>
                    	<td class="subTab" colspan="4">
                        	<table id="tab" class="rowstable" style="width:100%;" cellspacing="0" cellpadding="0">
                                <tr>
                                    <th style="width:40%;">&nbsp;产品名称/型号</th>
                                    <th style="width:18%;">&nbsp;产品编号</th>
                                    <th style="width:14%;">&nbsp;入库数量[单位]</th>
                                    <th style="width:20%;">&nbsp;备注</th>
                                    <th style="width:8%;border-right:0px">操作</th>
                                </tr>
                                <logic:notEmpty name="wmsWarIn" property="RWinPros" >
                                <logic:iterate id="rwp" name="wmsWarIn" property="RWinPros">
                                <tr id="tr<%= count%>" onMouseOver="focusTr('tr',<%= count%>,1)" onMouseOut="focusTr('tr',<%= count%>,0)">
                                    <td>&nbsp;<a href="wmsManageAction.do?op=wmsProDesc&wprId=${rwp.wmsProduct.wprId}" target="_blank" style="cursor:pointer">${rwp.wmsProduct.wprName}<logic:notEmpty name="rwp" property="wmsProduct.wprModel">/${rwp.wmsProduct.wprModel}</logic:notEmpty></a>
                                    </td>
                                    <td>&nbsp;${rwp.wmsProduct.wprCode}</td>
                                    <td>&nbsp;<bean:write name="rwp" property="rwiWinNum" format="###,##0.00"/>
                                    <logic:notEmpty name="rwp" property="wmsProduct.typeList">[${rwp.wmsProduct.typeList.typName}]</logic:notEmpty></td>
                                    <td>${rwp.rwiRemark}&nbsp;</td>
                                    <td>
                                    <logic:notEqual value="1" name="wmsWarIn" property="wwiAppIsok">
                                    <logic:empty name="wmsWarIn" property="salPurOrd">
                                    <img onClick="wmsPopDiv(22,'${rwp.rwiId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑入库产品"/>&nbsp;&nbsp;
                                    <img onClick="wmsDelDiv(4,'${rwp.rwiId}','${wmsWarIn.wwiId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>
                                    </logic:empty>
                                    </logic:notEqual>
                                    &nbsp;
                                    </td>			
                                </tr>
                                <script type="text/javascript">
                                    rowsBg('tr',<%=count%>);
                                 </script>
                                <% count++; %>
                                </logic:iterate>
                                </logic:notEmpty>
                                <logic:empty name="wmsWarIn" property="RWinPros">
                                    <tr>
                                        <td colspan="5" class="noDataTd"><div id="wwi">未找到相关数据!</div></td>
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
                        <td><logic:notEmpty name="wmsWarIn" property="wwiAppIsok">
                            <logic:equal value="0" name="wmsWarIn" property="wwiAppIsok"><img class="imgAlign" src="images/content/fail.gif" alt="未通过">&nbsp;未通过</logic:equal><logic:equal value="2" name="wmsWarIn" property="wwiAppIsok"><img class="imgAlign" src="images/content/time.gif" alt="审核中">&nbsp;审核中</logic:equal><logic:equal value="1" name="wmsWarIn" property="wwiAppIsok"><img class="imgAlign" src="images/content/suc.gif" alt="已通过">&nbsp;已通过</logic:equal>
                        </logic:notEmpty>
                        <logic:empty name="wmsWarIn" property="wwiAppIsok">
                            <span class="gray" title="未审核(请添加产品)">未审核</span>
                        </logic:empty>
                        <span id="wwiAppDate" title="审核时间" class="gray"></span>
                        </td>
                        <th>审核人：</th>
                        <td>${wmsWarIn.wwiAppMan}&nbsp;</td>
                    </tr>
                    <tr class="noBorderBot">
                        <th>审核内容：</th>
                        <td colspan="3">${wmsWarIn.wwiAppDesc}&nbsp;</td>
                    </tr>
                    <logic:notEqual value="1" name="wmsWarIn" property="wwiAppIsok">
                    <logic:notEmpty name="wmsWarIn" property="RWinPros">
                   		<tr>
                        	<td class="subTab" colspan="4">
                            	<div id="appWwiLayer" style="display:none">
                                    <div class="orangeBack" style="margin-bottom:5px"><input type="button" class="butSize3" value="审核入库单" onClick="$('appOrd').show()"/></div>
                                    <div id="appOrd" style="height:130px;width:100%; display:none; margin-top:5px;">
                                        <form action="wmsManageAction.do" method="post" id="app">
                                            <input type="hidden" name="op" value="appWwi">
                                            <input type="hidden" name="wwiId" value="${wmsWarIn.wwiId}">
                                            <div class="orangeBox" style="text-align:left; padding:10px; padding-top:5px;">
                                                <div><input type="radio" name="wwiAppIsok" id="isok1" value="1"  checked="checked"><label for="isok1">通过&nbsp;</label>&nbsp;&nbsp;&nbsp;
                                                        <input type="radio" name="wwiAppIsok" id="isok2" value="0" ><label for="isok2">不通过</label>
                                                </div>
                                                <div class="bold gray middle" style="padding:5px; padding-top:10px;">审核内容</div>
                                                <div style="text-align:center"><textarea rows="4" style="width:100%" name="wwiAppDesc"  onblur="autoShort(this,4000)">${wmsWarIn.wwiAppDesc}</textarea></div>
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
									displayLimAllow("w004","appWwiLayer");
								</script>
                        	</td>
                    	</tr>
                    </logic:notEmpty>
                    </logic:notEqual>
                </tbody>
	  		</table>
            <div style="text-align:center">
                <logic:empty name="wmsWarIn" property="RWinPros">
                    <div class="orangeBox" style="padding:10px; text-align:center;">
                        <span class="brown">请先添加入库明细...</span>
                    </div>
                </logic:empty>
                <logic:equal value="1" name="wmsWarIn" property="wwiAppIsok">
                    <logic:equal value="0" name="wmsWarIn" property="wwiState">
                        <div id="confirmLayer" class="orangeBack" style=" display:none;text-align:center">
                            <form action="wmsManageAction.do" method="post"  id="register" style="padding:0px; margin:0px">
                                <input type="hidden"  name="op" value="saveRwarPro">
                                <input type="hidden" name="wmsCode" value="${wmsWarIn.wmsStro.wmsCode}">
                                <input type="hidden" name="wwiId" value="${wmsWarIn.wwiId}">
                                <input type="button" class="butSize3" style="width:100px" id="dosave" value="确认入库" onClick="check()">
                            </form>
                        </div>
                        <script type="text/javascript">
                            displayLimAllow("w003","confirmLayer");
                        </script>
                   </logic:equal>
                   <logic:equal value="1" name="wmsWarIn" property="wwiState">
                        <div id="cancelInLayer" class="grayBg" style="display:none">
                            <input name="button" class="butSize3" type="button" onClick="wmsDelDiv(3,'${wmsWarIn.wwiId}')" value="撤销入库"/>
                        </div>
                        <div class="grayBack blue" style="padding:10px; margin-top:5px;">
                            <img src="images/content/bigSuc.gif" alt="已完成" style="vertical-align:middle"/>&nbsp;已确认入库
                        </div>
                        <script type="text/javascript">displayLimAllow("w005","cancelInLayer");</script>
                   </logic:equal>
                   <logic:equal value="2" name="wmsWarIn" property="wwiState">
                        <div class="redWarn" style="padding:10px; margin:0;">
                            <img src="images/content/bigFail.gif" alt="已撤销" style="vertical-align:middle"/>&nbsp;已撤销入库
                        </div>
                   </logic:equal>
                </logic:equal>
                <logic:equal value="2" name="wmsWarIn" property="wwiAppIsok">
                    <div class="orangeBox" style="padding:10px;">
                        <span class="brown">该入库申请正在审核中，审核通过方可入库...</span>
                    </div>
                </logic:equal>
                <logic:equal value="0" name="wmsWarIn" property="wwiAppIsok">
                    <div class="redWarn" style="padding:10px; margin:0;">
                        <span class="red">该入库申请未通过审核...</span>
                    </div>
                </logic:equal>
            </div>
            <div class="descStamp">
                由
                <span>${wmsWarIn.wwiInpName}</span>
                于
                <span id="inpDate"></span>&nbsp;
                录入<logic:notEmpty name="wmsWarIn" property="wwiAltName">，最近由
                <span>${wmsWarIn.wwiAltName}</span>
                于
                <span id="changeDate"></span>&nbsp;
                修改
                </logic:notEmpty>
            </div> 
          </div>
        </div>
     </div>
     <script type="text/javascript">
	 removeTime('inDate','${wmsWarIn.wwiInDate}',2);
	 removeTime("wwiAppDate","${wmsWarIn.wwiAppDate}",2);
	 removeTime('inpDate','${wmsWarIn.wwiInpTime}',2);
	 removeTime("changeDate","${wmsWarIn.wwiAltTime}",2);
	 removeTime("canDate","${wmsWarIn.wwiCanDate}",2);
  </script>
  </logic:notEmpty>
  <logic:empty name="wmsWarIn">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该入库单已被删除</div>
	</logic:empty>
  </body>
  
</html>

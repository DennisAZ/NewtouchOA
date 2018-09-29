<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>显示项目详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <link rel="stylesheet" type="text/css" href="css/att.css"/>
	<script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/pro.js"></script>
    <script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript">
		function showDiv(){
			$("onDiv").show();
			$("offDiv").hide();
		}
		function hidDiv(){
			$("onDiv").hide();
			$("offDiv").show();
		}
		function loadStyle(obj){
			obj.style.cursor="pointer";
			obj.onmouseover=function(){
				obj.className="grayBack blue";
			}
			obj.onmouseout=function() {
				obj.className="blackblue";
			}
		}
				
		//编辑明细样式
		function loadEditRopStyle(){
			var obj=$("editRop");
			var obj2=$("editEmp");
			loadStyle(obj);
			loadStyle(obj2);
			obj.onclick=function() {
				projPopDiv(12);
			}
			obj2.onclick=function() {
				addDivBrow(12,'proj');
			}
		}
		
		function showInput(){
			if($("proTxtInput").style.display=="none"){
				$("proTxtShow").hide();
				$("proTxtInput").show();
			}
			else{
				$("proTxtShow").show();
				$("proTxtInput").hide();
			}
		}
		
		//ajax修改当前进度
		function submitProTxt(){
			var url="projectAction.do";
			waitSubmit("submitTxt","提交中...");
			waitSubmit("cancelTxt");
			new Ajax.Request(url,{
				method:'post',
				parameters: $('proLogForm').serialize(true),
				onSuccess: function(response){
					//$("proLogTxt").remove();
					var newLog = response.responseText;
					$("someLogTxt").innerHTML = newLog;
					$("allLogTxt").innerHTML = newLog;
					if(shortDiv("someLogTxt",150)){
						$("logMoreButton").show();
					}
					/*var logDiv = document.createElement("span");
					logDiv.id="proLogTxt";
					logDiv.innerHTML = 
					logDiv.className="divWithScroll2";
					logDiv.style.height="100px";
					$("logTd").insert(logDiv);*/
					
					$("proTxtShowInf").innerHTML=$("progressTxt").value+"<br/><span class='gray'>"
										+ "${TODAY}"
										+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;由&nbsp;"
										+ "${limUser.userSeName}&nbsp;录入</span><br/><br/>" ;
					$("progressTxt").value="";
					restoreSubmit("submitTxt","提交");
					restoreSubmit("cancelTxt");
					showInput();
				},
				onFailure: function(response){
					if (response.status == 404)
						alert("您访问的地址不存在！");
					else
						alert("Error: status code is " + response.status);
				}
			});
			
		}
		
		//ajax修改成员职责
		function sendDuc(obj,id){
			var dutyDivId = "dutyDiv"+id;
			var dutyDivIdHide = "dutyDivn"+id;
			var newDuty = $("dutyInput"+id).value;
			var acuId = "acu"+id;
			var dutySpanId = "dutySpan" + id;
			waitSubmit(obj,"保存中...");
			var url="projectAction.do";
			var pars = "op=updateProActor&actId="+id+"&actDuty="+encodeURIComponent(newDuty);
			new Ajax.Request(url,{
				method:'post',
				parameters: pars,
				onSuccess: function(response){
					var res = response.responseText;
					if(res == '1'){
						restoreSubmit(obj,"保存");
						showAndHide(dutyDivId,dutyDivIdHide);
						$(dutyDivId).innerHTML=newDuty;
						
						if($(acuId)!=null){
							$(acuId).title=newDuty;
							$(acuId).innerHTML=newDuty;
						}
						else{
							$(dutySpanId).innerHTML = "[<span id='" + acuId + "' title='职责：" + newDuty +"'>" + newDuty + "</span>]";
                           	shortText(acuId,10);
						}
					}
				},
				onFailure: function(response){
					if (response.status == 404)
						alert("您访问的地址不存在！");
					else
						alert("Error: status code is " + response.status);
				}
			});
		}
		//显示编辑成员职责层
		function toEditDuty(showDivId,hideDivId,inputId){
			showAndHide(showDivId,hideDivId);
			$(inputId).focus();
		}
		//ajax修改项目成员
		function treeCallBack(nodes){
			var url = 'projectAction.do';
			var pars = 'op=updProMem&proId=${proInfo.proId}&memId=' + nodes[0] + "&ran=" + Math.random();
			new Ajax.Request(url,{
				method:'post',
				parameters: pars,
				onSuccess: function(response){
					if(response.responseText == '1'){
						self.history.go(0);
					}
				},
				onFailure: function(response){
					if (response.status == 404)
						alert("您访问的地址不存在！");
					else
						alert("Error: status code is " + response.status);
				}
			});
		}
		
		//载入当前项目成员id
		function loadProMem(){
			var memIds = "";
			<logic:notEmpty name="proInfo" property="proActors">
			<logic:iterate id="pActor" name="proInfo" property="proActors">
				<logic:equal value="1" name="pActor" property="actIsdel">
					memIds += '${pActor.salEmp.seNo}' + ',';
				</logic:equal>
			</logic:iterate>
			</logic:notEmpty>
			$("nodeIds").value = memIds;
		}

		createProgressBar();	
		window.onload=function(){
			if('${proInfo}'!=null&&'${proInfo}'!=""){
				loadEditRopStyle();
				loadProMem();
				//进度日志收缩层
				if($("someLogTxt")!=null && shortDiv("someLogTxt",150)){
					$("logMoreButton").show();
				}
				//表格内容省略
				loadTabShort("tab");
			}
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
  	<logic:notEmpty name="proInfo">
	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title">项目管理 > 项目详细信息</div>
            <div class="descInf">
            	<table class="dashTab descForm" cellpadding="0" cellspacing="0">
					<thead>
						<tr>
							<th class="descTitleL">项目名称：</th>
							<th class="descTitleR" colspan="3">
								${proInfo.proTitle}&nbsp;
							</th>
						</tr>
					</thead>
					<tbody>
                        <tr> 
                            <th class="blue">对应客户：</th>
                            <td class="blue mlink">
                             <logic:notEmpty name="proInfo" property="cusCorCus">
                                <a href="customAction.do?op=showCompanyCusDesc&corCode=${proInfo.cusCorCus.corCode}" target="_blank"><img class="imgAlign" src="images/content/showDesc.gif" alt="查看客户详情" style="border:0px;">${proInfo.cusCorCus.corName}</a>
                            </logic:notEmpty>&nbsp;
                            </td>
                            <th>负责账号：</th>
                            <td>${proInfo.limUser.userSeName}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>项目类别：</th>
                            <td colspan="3">${proInfo.proType.typName}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>项目状态：</th>
                            <td>
                                <logic:equal value="0" name="proInfo" property="proState">失败</logic:equal>
                                <logic:equal value="1" name="proInfo" property="proState">有效</logic:equal>
                                <logic:equal value="2" name="proInfo" property="proState">搁置</logic:equal>
                                <logic:equal value="3" name="proInfo" property="proState">放弃</logic:equal>
                            </td>
                            <th>项目阶段：</th>
                            <td>
                            <logic:equal value="0" name="proInfo" property="proPeriod">项目跟踪</logic:equal>
                            <logic:equal value="1" name="proInfo" property="proPeriod">签约实施</logic:equal>
                            <logic:equal value="2" name="proInfo" property="proPeriod">结项验收</logic:equal>
                            <logic:equal value="3" name="proInfo" property="proPeriod">完成归档</logic:equal>
                            &nbsp;
                            </td>
                        </tr>
                        <tr>
                            <th>立项日期：</th>
                            <td>
                                <span id="proCreDate"></span>&nbsp;
                                <input type="hidden" name="descCreDate" id="descCreDate">
                                <input type="hidden" name="descFinDate" id="descFinDate">
                                <input type="hidden" name="proId" id="proId" value="${proInfo.proId}">
                            </td>
                            <th>预计完成日期：</th>
                            <td>
                            <logic:notEmpty  name="proInfo" property="proFinDate">
                              <span id="proFinDate"></span>&nbsp;
                            </logic:notEmpty>
                            <logic:empty  name="proInfo" property="proFinDate">
                              <span class="gray">未指定预计完成时间</span>
                            </logic:empty>
                            </td>
                    	</tr>
                    	<tr>
                            <th>项目概要：</th>
                            <td colspan="3">${proInfo.proDesc}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>备注：</th>
                            <td colspan="3">${proInfo.proRemark}&nbsp;</td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>
                                <logic:notEmpty name="proInfo" property="attachments">
                                    <img style="vertical-align:middle; cursor:pointer" src="images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${proInfo.proId}','proj','reload')"/>
                                 </logic:notEmpty>
                                 <logic:empty name="proInfo" property="attachments">
                                    <img style="vertical-align:middle; cursor:pointer" src="images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${proInfo.proId}','proj','reload')"/>
                                 </logic:empty>
                                 附件：
                            </th>
                            <td colspan="3">
                                 <logic:iterate id="attList" name="proInfo" property="attachments" indexId="count2">
                                    <span id="fileDLLi${count2}"></span>
                                    <script type="text/javascript">createFileToDL('${count2}','${attList.attId}','${attList.attPath}','${attList.attName}','${attList.attSize}','${attList.attDate}')</script>
                                </logic:iterate>&nbsp;
                            </td>
                        </tr>
					</tbody>
					<thead>
                        <tr>
                            <td colspan="4">
                                <input type="hidden" id="nodeIds" />
                                <div>项目组成员&nbsp;&nbsp;
                                    <span id="editEmp" class="blackblue" style="padding:4px">
                                    <img class="imgAlign" src="images/content/add.gif" alt="编辑项目成员"/>
                                    </span>
                                </div>
                            </td>
                        </tr>
					</thead>
					<tbody>
					  <tr>
                        <th style="border-bottom:0;">项目组成员：</th>
                        <td class="subTab" colspan="3" style="text-align:left">
							<logic:notEmpty name="proInfo" property="proActors">
							<div id="actorsNames">
                                <logic:iterate id="pActor" name="proInfo" property="proActors">
                                    <logic:equal value="1" name="pActor" property="actIsdel">
                                        <logic:equal value="${proInfo.limUser.salEmp.seNo}" name="pActor" property="salEmp.seNo"><span class="bold" title="负责账号">${pActor.salEmp.seName}</span></logic:equal><logic:notEqual value="${proInfo.limUser.salEmp.seNo}" name="pActor" property="salEmp.seNo">${pActor.salEmp.seName}</logic:notEqual><span class="brown" id="dutySpan${pActor.actId}"><logic:notEmpty name="pActor" property="actDuty">
                                        [<span id="acu${pActor.actId}" title="职责：${pActor.actDuty}">${pActor.actDuty}</span>]<script type="text/javascript">shortText("acu${pActor.actId}",10);</script></logic:notEmpty></span>.&nbsp;
                                    </logic:equal>
                                </logic:iterate>&nbsp;
                                <span class="blue">[<a href="javascript:void(0)" onClick="showAndHide('actorsList','actorsNames')">查看详细列表</a>]</span>
							</div>
							<div id="actorsList" style="display:none;">
                                <table id="actTab" class="rowstable" cellpadding="0" cellspacing="0" style="width:100%">
                                    <tr>
                                        <th style="width:28%">成员名称</th>
                                        <th style="width:60%">职责</th>
                                        <th style="width:12%;border-right:0px;">操作</th>
                                    </tr>
                                    <logic:iterate id="pa" name="proInfo" property="proActors" indexId="actorsId2">
                                    <logic:equal value="1" name="pa" property="actIsdel">
                                    <tr id="tr${actorsId2}" onMouseOver="focusTr('tr',${actorsId2},1)" onMouseOut="focusTr('tr',${actorsId2},0)">
                                        <td>${pa.salEmp.seName}&nbsp;</td>
                                        <td>
                                        <div id="dutyDiv${pa.actId}">${pa.actDuty}&nbsp;</div>
                                        <div id="dutyDivn${pa.actId}" style="display:none">
                                        <input type="input" id="dutyInput${pa.actId}" class="inputSize2 inputBoxAlign" style="width:90%" value="${pa.actDuty}"/><button class="inputBoxAlign" onClick="sendDuc(this,${pa.actId})">保存</button>
                                        </div>
                                        </td>
                                        <!--<td><a href="javascript:void(0)" onClick="getUserDiv('projectAction.do?op=loadMyProTask&proId=${proInfo.proId}&seNo=${pa.salEmp.seNo}','proTask')">点击查看</a></td>-->
                                        <td>
                                        <img onClick="toEditDuty('dutyDivn${pa.actId}','dutyDiv${pa.actId}','dutyInput${pa.actId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                                        <logic:notEqual value="${proInfo.limUser.salEmp.seNo}" name="pa" property="salEmp.seNo">
                                        <img onClick="projDelDiv(5,'${pa.actId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>
                                        </logic:notEqual>&nbsp;
                                        </td>
                                        <script type="text/javascript">
                                            rowsBg('tr','${actorsId2}');
                                        </script>
                                        </logic:equal>
                                        </logic:iterate>
                                        <script type="text/javascript">
                                       /* function getUserListHTML(userList){
                                            var str="<ul>";
                                            var hasTask=false;
                                            for(var i=0;i<userList.length;i++){
                                            var text=userList[i].getAttribute("taskName");
                                            if(text!=null&&text!="")
                                                hasTask=true;
                                                str+="<li><span class='textOverflow2' title='"+text+"'>"+text+"</span>&nbsp;"
                                                if(userList[i]!=null){
                                                    switch(userList[i].getAttribute("taskSta")){
                                                        case '0':
                                                            str+="<img style='vertical-align:middle;' src='images/content/doing.gif' alt='执行中'/>";
                                                            break;
                                                        case '1':
                                                            str+="<img style='vertical-align:middle;' src='images/content/tofinish.gif' alt='已提交'/>";
                                                            break;
                                                        case '2':
                                                            str+="<img style='vertical-align:middle;' src='images/content/alert.gif' alt='被退回'/>";
                                                            break;
                                                        case '3':
                                                            str+="<img style='vertical-align:middle;' src='images/content/finish.gif' alt='已完成'/>";
                                                            break;
                                                    }
                                                    str+="</li>";
                                                }	
                                            }
                                            if(!hasTask){
                                                str+="<span class='deepGray'>未分配工作</span>";
                                            }
                                            str+="</ul>";
                                            return str;
                                        }*/
                                        </script>
                                    </table>
                          		<div>
                                <div class="blue">
                                	[<a href="javascript:void(0)" onClick="showAndHide('actorsNames','actorsList')">返回</a>]
                                </div>
                                
                                </div>
                            </div>
                            <script type="text/javascript">
                                loadTabShort("actTab");
                            </script>
                            </logic:notEmpty>
                            <logic:empty name="proInfo" property="proActors">
                                <span class="gray">未添加项目成员</span>
                            </logic:empty>	
                            </td>
                    	</tr>
					</tbody>
					<thead>
						<tr>
							<td colspan="4"><div>项目进度</div></td>
						</tr>
					</thead>
					<tbody>
                        <tr class="noBorderBot">
                            <td colspan="4" style="font-weight:normal">
                            <div style="border:#f2e25c 1px solid; background:#fefee5; padding:8px 10px 8px 10px;">
                                <div class="brown bold middle" style="padding:5px 0 8px 0">
                                    当前进度
                                    <img src="images/content/edit.gif" alt="修改" style=" cursor:pointer" class="imgAlign" onClick="showInput()"/>
                                </div>
                                <span id="proTxtShow" style="height:50px">
                                    <span id="proTxtShowInf" style="float:left">
                                        <logic:notEmpty name="proInfo" property="proPro">${proInfo.proPro}</logic:notEmpty>
                                        <logic:empty name="proInfo" property="proPro"><span class="gray">未录入当前进度</span></logic:empty>
                                    </span>
                                </span>
                                <span id="proTxtInput" style="display:none; vertical-align:top; height:50px">
                                    <form id="proLogForm" style="margin:0; padding:0">
                                        <input type="hidden" name="op" value="updateProLog"/>
                                        <input type="hidden" name="pId" value="${proInfo.proId}"/>
                                        <textarea id="progressTxt" name="proTxt" class="inputBoxAlign" rows="2" style="width:100%" onBlur="autoShort(this,200)"></textarea>
                                    </form>
                                    <div style=" padding:4px;"><button class="smallBut inputBoxAlign" id="submitTxt" onClick="submitProTxt()">提交</button>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<button class="smallBut inputBoxAlign" id="cancelTxt" onClick="showInput()">取消</button>
                                    </div>
                                </span>
                             </div>
                            </td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>
                                进度日志：
                            </th>
                            <td colspan="3" style="font-weight:normal;" id="logTd">
                            	<logic:notEmpty name="proInfo" property="proProLog">
                                <div id="allLog" style="display:none;">
                                    <div id="allLogTxt">${proInfo.proProLog}</div>
                                    <a class="moreInfDiv" href="javascript:void(0)" onClick="showAndHide('someLog','allLog')">-&nbsp;点击收起</a>
                                </div>
                                <div id="someLog">
                                    <div id="someLogTxt">${proInfo.proProLog}</div>
                                    <a id="logMoreButton" class="moreInfDiv" style="display:none" href="javascript:void(0)" onClick="showAndHide('allLog','someLog')">+&nbsp;点击展开</a>
                                </div>
                                </logic:notEmpty>
                            	<logic:empty name="proInfo" property="proProLog">&nbsp;</logic:empty>
                            </td>
                        </tr>
                    </tbody>
					<thead>
                        <tr>
                            <td colspan="4">
                                <div>子项目&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                    <span id="editRop" class="blackblue" style="padding:4px">
                                    <img class="imgAlign" src="images/content/add.gif" alt="新建子项目"/>
                                    </span>
                                </div>
                            </td>
                        </tr>
					</thead>
					<tbody>
                        <tr>
                            <td class="subTab" colspan="4">
                                <table id="tab" class="rowstable"  cellpadding="0" cellspacing="0" style="width:100%">
                                    <tr>
                                        <th style="width:5%">附件</th>
                                        <th style="width:28%">子项目名称</th>
                                        <th style="width:25%">目标</th>
                                        <th style="width:10%">开始日期</th>
                                        <th style="width:10%">结束日期</th>
                                        <th style="width:14%">备注</th>
                                        <th style=" width:8%;border-right:0px;">操作</th>
                                    </tr>
                                    <logic:notEmpty name="subPros">
                                    <logic:iterate id="pStage" name="subPros" indexId="count">
                                    <tr id="subProTr${count}" onMouseOver="focusTr('subProTr','${count}',1)" onMouseOut="focusTr('subProTr','${count}',0)">
                                        <td style="text-align:center">
                                            <logic:notEmpty name="pStage" property="attachments">
                                                <img id="${pStage.staId}y" style="vertical-align:middle; cursor:pointer" src="images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${pStage.staId}','psta','descList')"/>                              			</logic:notEmpty>
                                            <logic:empty name="pStage" property="attachments">
                                                <img id="${pStage.staId}n" style="vertical-align:middle; cursor:pointer" src="images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${pStage.staId}','psta','descList')"/>
                                            </logic:empty>
                                        </td>
                                        <td>${pStage.staTitle}&nbsp;</td>
                                        <td>${pStage.staAim}&nbsp;</td>
                                        <td><span id="staStartDate${count}"></span>&nbsp;</td>
                                        <td><span id="staEndDate${count}"></span>&nbsp;</td>
                                        <td>${pStage.staRemark}&nbsp;</td>
                                        <td>
                                            &nbsp;<img onClick="projPopDiv(13,'${pStage.staId}')" style="cursor:pointer" src="images/content/edit.gif" border="0" alt="编辑"/>&nbsp;
                                            <img onClick="projDelDiv(2,'${pStage.staId}')" style="cursor:pointer" src="images/content/del.gif" border="0" alt="删除"/>
                                       </td>			
                                    </tr>
                                    <script type="text/javascript">	
                                        dateFormat('staStartDate','${pStage.staStartDate}','${count}');
                                        dateFormat('staEndDate','${pStage.staEndDate}','${count}');
                                        rowsBg('subProTr','${count}');
                                    </script>
                                    </logic:iterate>
                                    </logic:notEmpty>	
                                    <logic:empty name="subPros">
                                        <tr>
                                            <td class="noDataTd" colspan="7">未添加子项目</td>
                                        </tr>
                                     </logic:empty>			
                                </table>      
                            </td>
                        </tr>
					</tbody>
					<thead>
						<tr>
							<td colspan="4"><div>关联数据</div></td>
						</tr>
					</thead>
              </table>
                <div class="xpTab">
                    <!--<span id="xpTab1" class="xpTabGray" style="display:none" onClick="swapTab(1,6,'projectAction.do?op=getAllProTask&proId=${proInfo.proId}')">
                      &nbsp;项目工作&nbsp;
                    </span>-->
                    <span id="xpTab1" class="xpTabGray" style="display:none" onClick="swapTab(1,5,'projectAction.do?op=getAllProHisTask&proId=${proInfo.proId}')">
                      &nbsp;项目日志&nbsp;
                    </span>
                    <span id="xpTab2" class="xpTabGray" style="display:none" onClick="swapTab(2,5,'cusServAction.do?op=getProQuo&proId=${proInfo.proId}&proTitle=${proInfo.proTitle}')">
                          &nbsp;报价记录&nbsp;
                    </span>
                    <span id="xpTab3" class="xpTabGray" style="display:none" onClick="swapTab(3,5,'projectAction.do?op=getProOrder&proId=${proInfo.proId}')">
                          &nbsp;项目订单&nbsp;
                    </span>
                    <span id='xpTab4' class='xpTabGray' style="display:none" onClick="swapTab(4,5,'salPurAction.do?op=getProInq&proId=${proInfo.proId}&proTitle=${proInfo.proTitle}')">
                        &nbsp;询价单&nbsp;
                    </span>
                    <span id='xpTab5' class='xpTabGray' style="display:none" onClick="swapTab(5,5,'salPurAction.do?op=getProSpo&proId=${proInfo.proId}&proTitle=${proInfo.proTitle}')">
                        &nbsp;采购单&nbsp;
                    </span>
                    <script type="text/javascript">
						var rigs = "p018|p027|p026|p028|p029";
						var ids = new Array("xpTab1","xpTab2","xpTab3","xpTab4","xpTab5");
						displayLimAllow(rigs,ids,loadXpTabSel);
                    </script>
                </div>
                <div class="HackBox"></div>
                <div id="ifrContent" class="tabContent" style="display:none">
                    <iframe id="ifrList" src="" scrolling="no" frameborder="0"></iframe>  
                </div>
                <div class="descStamp">
                    由
                    <span>${proInfo.proInpUser}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<logic:notEmpty name="proInfo" property="proUpdUser">，最近由
                    <span>${proInfo.proUpdUser}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </logic:notEmpty>
                </div>
              	<script type="text/javascript">
				  removeTime("proCreDate","${proInfo.proCreDate}",1);
				  removeTime("proFinDate","${proInfo.proFinDate}",1);
				  removeTime("inpDate","${proInfo.proInsDate}",2);
				  removeTime("changeDate","${proInfo.proModDate}",2);
				  $("descCreDate").value="${proInfo.proCreDate}".substring(0,10);
				  $("descFinDate").value="${proInfo.proFinDate}".substring(0,10);
			 	</script>
        	</div>
        </div>
  	</div>
  </logic:notEmpty>
  <logic:empty name="proInfo">
		    <div class="redWarn"><img src="images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该项目已被删除</div>
	  </logic:empty>
  </body>
</html>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//清缓存
response.setHeader("Pragma","No-cache");   
response.setHeader("Cache-Control","no-cache");   
response.setDateHeader("Expires", 0);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title><logic:equal value="2" name="range">全部</logic:equal><logic:notEqual value="2" name="range">我及下属的</logic:notEqual>来往记录高级查询</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="StyleSheet" href="crm/css/dtree.css" type="text/css">
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/dtree.js"></script>
    <script language="javascript" type="text/javascript">
		function check(){
			return $("supSear").submit();
	   	}

	   function showSelect(){
			$("type").value='${type}';
			$("state").value='${state}';
			$("isPrice").value='${isPrice}';
		}
		function chooseCus(){
			addDivBrow(2);
		}
		
		//树初始化
		var treeName1 = 'praSupUT';
		var treeName2 = 'praSupET';
		var praSupUT = new dTree(treeName1);
		var praSupET = new dTree(treeName2);
		
		praSupUT.config.showRootIcon=false;//不显示root图标
		praSupUT.config.hasCheckBox=true;
		praSupUT.config.folderLinks=false;
		praSupET.config.showRootIcon=false;//不显示root图标
		praSupET.config.hasCheckBox=true;
		praSupET.config.folderLinks=false;
											
		createProgressBar();
		window.onload=function(){
			showSelect();
			showCheckBox('${limCode}',treeName1);
			showCheckBox('${empCode}',treeName2);
			//表格内容省略
			loadTabShort("tab");
			//增加清空按钮
			createCancelButton('supSear',0,5,'searButton','after');
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
  	<div id="mainbox">
    	<div id="contentbox">
        	<div id="title">客户管理 > 来往记录 > <logic:equal value="2" name="range">全部</logic:equal><logic:notEqual value="2" name="range">我及下属的</logic:notEqual>来往记录高级查询</div>
              	<div class="descInf">
                  	<form action="cusServAction.do" name="supSear" id="supSear" method="get">
                        <input type="hidden" name="op" value="praSupSearch" />
                        <input type="hidden" name="range" value="${range}" />
						<input type="text" style="display:none" name="cusCode" id="cusCode" value="${code}" />
                      	<table class="supSearForm" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th>负责账号</th>
                                <td>
                                	<input type="text" style="display:none" name="userCBName" id="userCBName"/>
                                    <div>
										<script type="text/javascript">
										$("userCBName").value="nodes"+treeName1;
										praSupUT.add('${orgTopCode}',-1,'负责账号列表','','负责账号列表','_parent');
									  </script>
									<logic:notEmpty name="orgList">
									<logic:iterate id="ol" name="orgList" scope="request">
									<script type="text/javascript">
										var id='${ol.soCode}'
										var pid='${ol.salOrg.soCode}';
										praSupUT.add(id,pid,'${ol.soName}','','${ol.soName}','_parent','crm/images/dtree/orgnode.gif','crm/images/dtree/orgopen.gif');
									</script>
										<logic:notEmpty name="ol" property="limUsers">
											<logic:iterate id="lu" name="ol" property="limUsers">
												<logic:equal name="lu" property="userIsenabled" value="1">
												<script type="text/javascript">
													var fid='${lu.userCode}';
													var name='${lu.salEmp.seName}';
													var role='${lu.salEmp.limRole.rolName}';
													var name1;
													if(role!=null&&role!=""){
														name1=name+"-"+role;
													}									
													if(pid!=null&&pid!=""){	
														praSupUT.add(fid,id,name1,null,name1,'_parent',null,null,null,'${lu.userCode}');
													}
												</script>
												</logic:equal>
											</logic:iterate>
									</logic:notEmpty>
								</logic:iterate>
								<script type="text/javascript">
									document.write(praSupUT);
								</script>
								</logic:notEmpty>
								</div>
                             	</td>
                            </tr>
                            <tr>
                            	<th>联系人</th>
                                <td>
                                	<input type="text" style="display:none"  name="empCBName" id="empCBName"/>
                                    <div>
										<script type="text/javascript">
											$("empCBName").value="nodes"+treeName2;
                                            //id, pid, name, url, title, target, icon, iconOpen, open	
                                            praSupET.add('${orgTopCode}',-1,'员工列表','','员工列表','_self');
                                        </script>
                                        <logic:notEmpty name="orgList2">
                                            <logic:iterate id="salOrg" name="orgList2">
                                                <logic:notEqual value="${orgTopCode}" name="salOrg" property="soCode">
                                                <script type="text/javascript">
                                                praSupET.add('${salOrg.soCode}','${salOrg.salOrg.soCode}','${salOrg.soName}','','${salOrg.soName}','_self','crm/images/dtree/orgnode.gif','crm/images/dtree/orgopen.gif');
                                                </script>
                                                </logic:notEqual>
												<logic:notEmpty name="salOrg" property="salEmps">
													<logic:iterate id="emp" name="salOrg" property="salEmps">
														<logic:notEqual name="emp" property="seRap" value="离职">
														<script type="text/javascript">
														var empImg;
														if('${emp.seSex}'=='女'){
															empImg = 'crm/images/dtree/empnode_w.gif';
														}
														else{
															empImg = 'crm/images/dtree/empnode_m.gif';
														}
														praSupET.add('${emp.seNo}','${emp.salOrg.soCode}','${emp.seName}[${emp.limRole.rolName}]','','${emp.seName}[${emp.limRole.rolName}]','_self',empImg,empImg,null,'${emp.seNo}');
														</script>
														</logic:notEqual>
													</logic:iterate>
											</logic:notEmpty>
                                            </logic:iterate>
                                        </logic:notEmpty> 
                                        <script type="text/javascript">
                                            document.write(praSupET);
                                        </script>
                                        </div>
                                </td>
                            </tr>
                            <tr>
                            	<th>基本信息</th>
                                <td>
                                	<table class="innerSearForm" cellpadding="0" cellspacing="0">
                                    	<tr>
                                        	<th>摘要：</th>
                                            <td><input class="inputSize2" type="text" name="ptitle" value="${ptitle}" /></td>
                                            <th>对应客户：</th>
                                            <td><input id="cusName" name="cusName" class="inputSize2S lockBack" type="text" value="${cusName}" readonly ondblClick="clearInput(this,'cusCode')"  title="此处文本无法编辑，双击可清空文本"/>&nbsp;<button class="butSize2" onClick="chooseCus()">选择</button></td>
                                        </tr>
                                        <tr>
                                        	<th>状态：</th>
                                            <td>
                                            	<select class="inputSize2" id="state" name="state">
                                                   <option value="">请选择</option>
                                                   <option value="未执行">未执行</option>
                                                   <option value="已执行">已执行</option>
                                                </select>
                                            </td>
                                            <th>联系类型：</th>
                                            <td>
                                            	<select class="inputSize2" id="isPrice" name="isPrice">
                                                   <option value="">请选择</option>
                                                   <option value="客户来访">客户来访</option>
                                                   <option value="我方去访">我方去访</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                        	<th>联系方式：</th>
                                            <td>
                                            	<select class="inputSize2" id="type" name="type">
                                                    <option value="">请选择</option>
                                                    <option value="电话">电话</option>
                                                    <option value="传真">传真</option>
                                                    <option value="电邮">电邮</option>
                                                    <option value="邮寄">邮寄</option>
                                                    <option value="网络">网络</option>
                                                    <option value="拜访">拜访</option>
                                                    <option value="其他">其他</option>
                                                </select>
                                            </td>
                                            <th>执行日期：</th>
                                            <td>
                                            	<input name="exeTime"  value="${exeTime}" type="text" class="Wdate shortDate" readonly="readonly" id="pid" ondblClick="clearInput(this)" onFocus="var endExeTime=$('endExeTime');WdatePicker({onpicked:function(){endExeTime.focus();},maxDate:'#F{$dp.$D(\'endExeTime\')}'})"/>
                                    			&nbsp;到&nbsp;<input name="endExeTime"  value="${endExeTime}" type="text" class="Wdate shortDate" readonly="readonly" id="endExeTime" ondblClick="clearInput(this)" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>创建时间：</th>
                                            <td colspan="3">
                                            	<input name="startTime"  value="${startTime}" type="text" class="Wdate shortDate" readonly="readonly" id="pid1" ondblClick="clearInput(this)" onFocus="var endTime=$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}'})"/>
                                    			&nbsp;到&nbsp;<input name="endTime"  value="${endTime}" type="text" class="Wdate shortDate" readonly="readonly" id="endTime" ondblClick="clearInput(this)" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid1\')}'})"/>
                                            </td>
                                        </tr>
                           			</table>
                                </td>
                            </tr>
                            <tr class="searSubmitArea">
                            	<th>&nbsp;</th>
                                <td><input class="butSize1 inputBoxAlign" id="searButton" type="button" onClick="check()" value="开始搜索" /></td>
                            </tr>
                        </table>
                	</form>	
                    <table id="tab" class="normal rowstable" style="width:98%" cellpadding="0" cellspacing="0">		
                       <tr>
                            <th style="width:3%"><input name="allCheck" id="allCheck" type="checkbox" onClick="selectAll()"></th>
                            <th style="width:7%">联系方式</th>
                            <th style="width:28%">摘要</th>
                            <th style="width:12%">对应客户</th>
                            <th style="width:16%">(计划)执行日期</th>
                            <th style="width:4%">状态</th>
                            <th style="width:10%">联系人</th>
                            <th style="width:10%">录入日期</th>
                            <th style="width:10%;border-right:0px">操作</th>
                      </tr>
                            <logic:notEmpty name="salPraList">
                            <logic:iterate id="salPras" name="salPraList" indexId="count">
                            <tr id="tr${count}" onMouseOver="focusTr('tr',${count},1)" onMouseOut="focusTr('tr',${count},0)" onDblClick="descPop('cusServAction.do?op=showSalPraInfo&id=${salPras.praId}')">
                                <td><input name="priKey" type="checkbox" value="${salPras.praId}"></td>
                                <td>${salPras.praType}&nbsp;</td>
                                <td>
                                <a href="cusServAction.do?op=showSalPraInfo&id=${salPras.praId}" target="_blank" style="cursor:pointer">${salPras.praTitle}</a>                           
                                </td>
                                <td class="mLink">
                                <logic:notEmpty name="salPras" property="cusCorCus">
                                    <a title="${salPras.cusCorCus.corName}" href="customAction.do?op=showCompanyCusDesc&corCode=${salPras.cusCorCus.corCode}" target="_blank"><img src="crm/images/content/showDesc.gif" alt="查看客户详情" class="imgAlign" style="border:0px;">${salPras.cusCorCus.corMne}</a>                          
                                </logic:notEmpty>&nbsp;
                                </td>
                                <td>
                                <logic:equal value="已执行" name="salPras" property="praState">
                                    <span id="exe${count}"></span>
                                </logic:equal>
                                <logic:equal value="未执行" name="salPras" property="praState">
                                    <span id="jhexe${count}"></span>&nbsp;
                                    <span id="exePraLayer${count}" style="display:none">
                                    	<span id="${salPras.praId}ToMod">[<a class="aAlign" href="javascript:void(0)" onClick="cusPopDiv(42,['${salPras.praId}','${salPras.praExeDate}']);return false;">修改</a>]</span>
                                            <span id="${salPras.praId}ToExe"><img src="crm/images/content/execute.gif" style="cursor:pointer; vertical-align:middle" alt="点击执行" onClick="modifyPraState('${salPras.praId}','${salPras.praExeDate}','${salPras.cusCorCus.corCode}','${TODAY}')"/>&nbsp;</span>
                                    </span>
                                    <script type="text/javascript">displayLimAllow("r015","exePraLayer${count}");</script>  
                                </logic:equal>               
                                </td>
                                <td>
                                <logic:equal value="已执行" name="salPras" property="praState">
                                    <img style="vertical-align:middle" src="crm/images/content/suc.gif" alt="${salPras.praState}"/>                            </logic:equal>
                                <logic:equal value="未执行" name="salPras" property="praState"><span id="y${salPras.praId}" style="display:none"><img style="vertical-align:middle" src="crm/images/content/suc.gif" alt="已执行"/></span><span id="${salPras.praId}"><img style="vertical-align:middle" src="crm/images/content/time.gif" alt="未执行"/></span></logic:equal>                            
                                </td>
                                <td><span id="${salPras.praId}PraEmp">${salPras.salEmp.seName}&nbsp;</span></td>
                                <td><span id="ins${count}"></span>&nbsp;</td>
                                <td>&nbsp;
                                    <a href="cusServAction.do?op=showSalPraInfo&id=${salPras.praId}" target="_blank"><img src="crm/images/content/detail.gif" border="0" alt="查看详细"/></a>&nbsp;&nbsp;
                                    <logic:equal value="${limUser.userCode}" name="salPras" property="limUser.userCode">
                                    <img onClick="cusPopDiv(41,['${salPras.praId}','1'])" style="cursor:pointer" src="crm/images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                                    </logic:equal>
                                    <img onClick="cusDelDiv(4,'${salPras.praId}')" style="cursor:pointer" src="crm/images/content/del.gif" border="0" alt="删除"/>&nbsp;
                                </td>
                            </tr>
                            <script type="text/javascript">
                                dateFormat('exe','${salPras.praExeDate}','${count}');
                                dateFormat('jhexe','${salPras.praExeDate}','${count}');
                                dateFormat('ins','${salPras.praInsDate}','${count}');
                                rowsBg('tr','${count}');
                            </script>
                          </logic:iterate>
                          </logic:notEmpty> 
                          <logic:empty name="salPraList">
                                <tr>
                                    <td colspan="9" class="noDataTd">未找到相关数据!</td>
                                </tr>
                      		</logic:empty> 
                            <tr>
                                <td colspan="9" style="vertical-align:top">
                                <div class="bottomBar">
                                    <span class="gray" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='gray'" onClick="checkBatchDel(4,'来往记录')"><img src="crm/images/content/del.gif" border="0" alt="批量删除" style="vertical-align:middle; margin-bottom:2px;"/>&nbsp;批量删除</span>
                                </div>
                                </td>
                            </tr>
                      </table>
                
                   <logic:notEmpty name="salPraList">
                        <script type="text/javascript">
                            createPage('cusServAction.do?op=praSupSearch&range=${range}&ptitle='+ encodeURIComponent('${ptitle}') +'&type='+ encodeURIComponent('${type}') +'&state='+ encodeURIComponent('${state}') +'&cusCode=${code}&cusName='+ encodeURIComponent('${cusName}') +'&limCode=${limCode}&empCode=${empCode}&isPrice='+ encodeURIComponent('${isPrice}') +'&exeTime=${exeTime}&endExeTime=${endExeTime}&startTime=${startTime}&endTime=${endTime}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                        </script>
                    </logic:notEmpty>
                </div>
            
                    
   	  </div>
		</div>
  </body>
</html>

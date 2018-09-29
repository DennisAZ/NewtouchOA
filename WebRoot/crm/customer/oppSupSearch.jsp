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
    <title><logic:equal value="2" name="range">全部</logic:equal><logic:notEqual value="2" name="range">我及下属的</logic:notEqual>销售机会高级查询</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="StyleSheet" href="crm/css/dtree.css" type="text/css">
    
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
	<script type="text/javascript" src="crm/js/dtree.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script language="javascript" type="text/javascript">
     	function check(){
			return $("supSear").submit();
	   	}

	   	function showSelect(){
			$("hot").value='${hot}';
			$("state").value='${state}';
			$("possible").value='${possible}';
			if($("stage")!=null)
				$("stage").value='${stage}';
		}
		function chooseCus(){
			addDivBrow(2);
		}
		
		//树初始化
		var treeName = 'oppSupUT';
		var oppSupUT = new dTree(treeName);
		oppSupUT.config.showRootIcon=false;//不显示root图标
		oppSupUT.config.hasCheckBox=true;
		oppSupUT.config.folderLinks=false;
										
		createProgressBar();
		window.onload=function(){
			showSelect();
			showCheckBox('${limCode}',treeName);
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
        	<div id="title">客户管理 > 销售机会 > <logic:equal value="2" name="range">全部</logic:equal><logic:notEqual value="2" name="range">我及下属的</logic:notEqual>销售机会高级查询</div>
              	<div class="descInf">
                  	<form action="cusServAction.do" name="supSear" id="supSear" method="get">
                        <input type="hidden" name="op" value="oppSupSearch" />
                        <input type="hidden" name="range" value="${range}" />
                        <input  type="text" style="display:none" name="cusCode" id="cusCode" value="${code}" />
                        <table class="supSearForm" cellpadding="0" cellspacing="0">
                        	<tr>
                            	<th>负责账号</th>
                                <td>
                                	<input type="text" style="display:none" name="userCBName" id="userCBName"/>
                                	<div>
                                    <script type="text/javascript">
                                    	$("userCBName").value="nodes"+treeName;
										oppSupUT.add('${orgTopCode}',-1,'负责账号列表','','负责账号列表','_parent');
                                    </script>
									<logic:notEmpty name="orgList">
									<logic:iterate id="ol" name="orgList" scope="request">
									<script type="text/javascript">
										var id='${ol.soCode}'
										var pid='${ol.salOrg.soCode}';
										oppSupUT.add(id,pid,'${ol.soName}','','${ol.soName}','_parent','crm/images/dtree/orgnode.gif','crm/images/dtree/orgopen.gif');
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
														oppSupUT.add(fid,id,name1,null,name1,'_parent',null,null,null,'${lu.userCode}');
													}
												</script>
												</logic:equal>
											</logic:iterate>
									</logic:notEmpty>
								</logic:iterate>
								<script type="text/javascript">
									document.write(oppSupUT);
								</script>
								</logic:notEmpty>
								</div>
                                </td>
                            </tr>
                            <tr>
                            	<th>基本信息</th>
                                <td>
                                	<table class="innerSearForm" cellpadding="0" cellspacing="0">
                                    	<tr>
                                        	<th>机会主题：</th>
                                            <td><input class="inputSize2" type="text" id="otitle" name="otitle" value="${otitle}" /></td>
                                            <th>对应客户：</th>
                                            <td>
                                            	<input id="cusName" name="cusName" class="inputSize2S lockBack" type="text" value="${cusName}" readonly ondblClick="clearInput(this,'cusCode')"  title="此处文本无法编辑，双击可清空文本"/>&nbsp;
                                        		<button class="butSize2" onClick="chooseCus()">选择</button>
                                            </td>
                                        </tr>
                                        <tr>
                                    		<th>发现日期：</th>
                                    		<td colspan="3">
                                            	<input name="findTime"  value="${findTime}" type="text" class="Wdate shortDate" readonly="readonly" id="findTime" ondblClick="clearInput(this)" onFocus="var endFindTime=$('endFindTime');WdatePicker({onpicked:function(){endFindTime.focus();},maxDate:'#F{$dp.$D(\'endFindTime\')}'})"/>
                                    			&nbsp;到&nbsp;<input name="endFindTime"  value="${endFindTime}" type="text" class="Wdate shortDate" readonly="readonly" id="endFindTime" ondblClick="clearInput(this)" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'findTime\')}'})"/>
                                    		</td>
                                    	</tr>
                                 		<tr>
                                    		<th>关注日期：</th>
                                    		<td>
                                            	<input name="exeTime"  value="${exeTime}" type="text" class="Wdate shortDate" readonly="readonly" id="pid" ondblClick="clearInput(this)" onFocus="var endExeTime=$('endExeTime');WdatePicker({onpicked:function(){endExeTime.focus();},maxDate:'#F{$dp.$D(\'endExeTime\')}'})"/>
                                    			&nbsp;到&nbsp;<input name="endExeTime"  value="${endExeTime}" type="text" class="Wdate shortDate" readonly="readonly" id="endExeTime" ondblClick="clearInput(this)" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/>
                                            </td>
                                            <th>机会热度：</th>
                                            <td>
                                                <select class="inputSize2" id="hot" name="hot">
                                                    <option value="">请选择</option>
                                                    <option value="低热度">低</option>
                                                    <option value="中热度">中</option>
                                                    <option value="高热度">高</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>预计签单日期：</th>
                                            <td>
                                            	<input name="signTime"  value="${signTime}" type="text" class="Wdate shortDate" readonly="readonly" id="signTime" ondblClick="clearInput(this)" onFocus="var endSignTime=$('endSignTime');WdatePicker({onpicked:function(){endSignTime.focus();},maxDate:'#F{$dp.$D(\'endSignTime\')}'})"/>
                                    			&nbsp;到&nbsp;<input name="endSignTime"  value="${endSignTime}" type="text" class="Wdate shortDate" readonly="readonly" id="endSignTime" ondblClick="clearInput(this)" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'signTime\')}'})"/>
                                             </td>
                                            <th>创建时间：</th>
                                            <td>
                                            	<input name="startTime"  value="${startTime}" type="text" class="Wdate shortDate" readonly="readonly" id="pid1" ondblClick="clearInput(this)" onFocus="var endTime=$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}'})"/>
                                    			&nbsp;到&nbsp;<input name="endTime"  value="${endTime}" type="text" class="Wdate  shortDate" readonly="readonly" id="endTime" ondblClick="clearInput(this)" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid1\')}'})"/>
                                            </td>
                                		</tr>
                                     </table>
                            	</td>
                            </tr>
                            <tr>
                            	<th>机会阶段</th>
                                <td>
                                	<table class="innerSearForm" cellpadding="0" cellspacing="0">
                                    	<tr>
                                        	<th>机会状态：</th>
                                    		<td colspan="3">
                                                <select class="inputSize2" id="state" name="state">
                                                   <option value="">请选择</option>
                                                   <option value="跟踪">跟踪</option>
                                                   <option value="成功">成功</option>
                                                   <option value="搁置">搁置</option>
                                                   <option value="失效">失效</option>
                                                </select>
                                            </td>
                                        </tr>
                                    	<tr>
                                        	<th>阶段：</th>
                                            <td>
                                            	<logic:notEmpty name="oppStageList">
                                                <select id="stage" name="stage" class="inputSize2">
                                                    <option  value="">请选择</option>
                                                    <logic:iterate id="oppStage" name="oppStageList" >
                                                    <option value="${oppStage.typId}">${oppStage.typName}</option>
                                                    </logic:iterate>
                                                </select>
                                                </logic:notEmpty>
                                                <logic:empty name="oppStageList">
                                                    <span class="gray">未添加</span>
                                                </logic:empty>
                                            </td>
                                            <th>可能性：</th>
                                            <td>
                                            	<select class="inputSize2" id="possible" name="possible">
                                                    <option value="">请选择</option>
                                                    <option value="0">0%</option>
                                                    <option value="10">10%</option>
                                                    <option value="20">20%</option>
                                                    <option value="30">30%</option>
                                                    <option value="40">40%</option>
                                                    <option value="50">50%</option>
                                                    <option value="60">60%</option>
                                                    <option value="70">70%</option>
                                                    <option value="80">80%</option>
                                                    <option value="90">90%</option>
                                                    <option value="100">100%</option>
                                                </select>
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
	
                    <table id="tab" class="normal rowstable" cellpadding="0" cellspacing="0" style="width:98%">		
                        <tr>
                                <th style="width:3%"><input name="allCheck" id="allCheck" type="checkbox" onClick="selectAll()"></th>
                                <th style="width:5%">状态</th>
                                <th style="width:24%">机会主题</th>
                                <th style="width:10%">机会热度</th>
                                <th style="width:14%">对应客户</th>
                                <th style="width:10%">发现日期</th>
                                <th style="width:10%">阶段</th>
                                <th style="width:6%">可能性</th>
                                <th style="width:8%">阶段停留</th>
                                <th style=" width:10%;border-right:0px">操作</th>
                            </tr>
                            <logic:notEmpty name="salOppList">	
                            <logic:iterate id="salOpps" name="salOppList" indexId="count">
                            <tr id="tr${count}" onMouseOver="focusTr('tr','${count}',1)" onMouseOut="focusTr('tr','${count}',0)" onDblClick="descPop('cusServAction.do?op=showSalOppInfo&id=${salOpps.oppId}')">
                                <td><input name="priKey" type="checkbox" value="${salOpps.oppId}"></td>
                                <td>${salOpps.oppState}&nbsp;</td>
                                <td><a href="cusServAction.do?op=showSalOppInfo&id=${salOpps.oppId}" target="_blank" style="cursor:pointer">${salOpps.oppTitle}</a></td>
                                <td>${salOpps.oppLev}&nbsp;</td>
                                <td class="mLink">
                                <logic:notEmpty name="salOpps" property="cusCorCus">
                                    <a title="${salOpps.cusCorCus.corName}" href="customAction.do?op=showCompanyCusDesc&corCode=${salOpps.cusCorCus.corCode}" target="_blank"><img src="crm/images/content/showDesc.gif" alt="查看客户详情" class="imgAlign" style="border:0px;">${salOpps.cusCorCus.corMne}</a>
                                </logic:notEmpty>&nbsp;
                                </td>
                                <td><span id="exe${count}"></span>&nbsp;</td>
                                <td><logic:notEmpty name="salOpps" property="oppStage">${salOpps.oppStage.typName}</logic:notEmpty>&nbsp;</td>
                                <td><logic:notEmpty name="salOpps" property="oppPossible">${salOpps.oppPossible}%</logic:notEmpty>&nbsp;</td>
                                <td>
                                <logic:equal value="跟踪" name="salOpps" property="oppState">
                                ${salOpps.oppDayCount}天&nbsp;
                                </logic:equal>
                                <logic:notEqual value="跟踪" name="salOpps" property="oppState">
                                &nbsp;
                                </logic:notEqual>
                                </td>
                                <td>&nbsp;
                                    <a href="cusServAction.do?op=showSalOppInfo&id=${salOpps.oppId}" target="_blank"><img src="crm/images/content/detail.gif" border="0" alt="查看详细"/></a>&nbsp;&nbsp;
                                    <img onClick="cusPopDiv(31,'${salOpps.oppId}','1')" style="cursor:pointer" src="crm/images/content/edit.gif" border="0" alt="编辑"/>&nbsp;&nbsp;
                                    <img onClick="cusDelDiv(3,'${salOpps.oppId}')" style="cursor:pointer" src="crm/images/content/del.gif" border="0" alt="删除"/>&nbsp;
                                </td>
                            </tr>
                            <script type="text/javascript">
                                dateFormat('exe','${salOpps.oppFindDate}','${count}');
                                rowsBg('tr','${count}');
                            </script>
                          </logic:iterate>
                          </logic:notEmpty> 
                          <logic:empty name="salOppList">
                                <tr>
                                    <td colspan="10" class="noDataTd">未找到相关数据!</td>
                                 </tr>
                          </logic:empty>
                                <tr>
                                    <td colspan="10" style="vertical-align:top">
                                    <div class="bottomBar">
                                        <span class="gray" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='gray'" onClick="checkBatchDel(3,'销售机会')"><img src="crm/images/content/del.gif" border="0" alt="批量删除" style="vertical-align:middle; margin-bottom:2px;"/>&nbsp;批量删除</span>
                                    </div>
                                    </td>
                                </tr>
                         </table>
                
                   <logic:notEmpty name="salOppList">
                        <script type="text/javascript">
                            createPage('cusServAction.do?op=oppSupSearch&range=${range}&otitle='+ encodeURIComponent('${otitle}') +'&hot='+ encodeURIComponent('${hot}') +'&state='+ encodeURIComponent('${state}') +'&cusCode=${code}&cusName='+ encodeURIComponent('${cusName}') +'&limCode=${limCode}&exeTime=${exeTime}&endExeTime=${endExeTime}&startTime=${startTime}&endTime=${endTime}&signTime=${signTime}&endSignTime=${endSignTime}&findTime=${findTime}&endFindTime=${endFindTime}&stage=${stage}&possible=${possible}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
                        </script>
                    </logic:notEmpty>
                </div>
        	</div>
		</div>
  </body>
</html>

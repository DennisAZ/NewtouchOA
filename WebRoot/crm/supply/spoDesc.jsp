<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	int count=0;
	int c=0;
	int j=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>显示采购单详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="crm/css/att.css"/>
	<style type="text/css">
		.descMod {
			padding:5px;
		}
		.descModTil{
			color:#1e3a56;
			background-color:#d9dffd;
			font-weight:600;
			font-size:14px;
			margin-bottom:5px;
			padding:2px;
			padding-left:5px;
		}
		.lightBg {
			border:#CCCCCC 1px solid;
			background-color:#EEEEEE;
		}
    </style>
    
    <script type="text/javascript" src="js/prototype.js"></script>
    <script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/spo.js"></script>
	<script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript">
		var ms='${msg}';
		if(ms!=null && ms!=""){
			alert(ms);
		}
		function showDiv(){
			$("onDiv").show();
			$("offDiv").hide();
		}
		function hidDiv(){
			$("onDiv").hide();
			$("offDiv").show();
		}
		function heji(n){
			var allnum=0;
			var allPrice=0;
			var totalPrice=0;
			for(var i=0;i<n;i++){
				allnum+=parseInt($F("num"+i));
				allPrice+=parseFloat($F("allprice"+i));
				totalPrice+=parseFloat($F("price"+i)*$F("num"+i));
			}
			$("ap").innerHTML="￥"+changeTwoDecimal(allPrice);
		}
	
		//编辑明细样式
		function loadEditRop(){
			var obj=$("editRop");
			obj.style.cursor="pointer";
			//obj.className="blueBg";
				
			obj.onmouseover=function(){
				obj.className="grayBack blue";
			}
			
			obj.onmouseout=function() {
				obj.className="blackblue";
			}
			
			obj.onclick=function() {
				window.open("salPurAction.do?op=goRspo&spoId=${salPurOrd.spoId}");
			}
			
		}
		
		function loadXpTabStyle(n){
			var i = 1;
			while(i<=n){
				if($("xpTab"+i)==null){
					i++;
				}
				else {
					$("xpTab"+i).className="xpTabSelected";
					$("tabContent"+i).show();
					break;
				}
			}
		}
		
		
		createProgressBar();	
		window.onload=function(){
			if('${salPurOrd}'!=null&&'${salPurOrd}'!=''){
				if('${salPurOrd.spoAppIsok}'!="1"){
					loadEditRop();
				}
				//表格内容省略
				loadTabShort("tab");
				//if($("tab2")!=null)
					//loadTabShort("tab2");
				//loadXpTabStyle(4);
				
			}
			
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
  	<div id="mainbox">
      <logic:notEmpty name="salPurOrd">
		<div id="contentbox">
        	<div id="title">采购管理 > 采购单详细信息</div>
            <div class="descInf">
            	<table class="dashTab descForm" style="width:100%" cellpadding="0" cellspacing="0">
                	<thead>
                    	<tr>
                            <th class="descTitleL">采购主题：</th>
                            <th class="descTitleR" colspan="3">
                                ${salPurOrd.spoTil}&nbsp;
                            </th>
                        </tr>
                        <!--<tr>
                            <td colspan="4"><div>基本信息</div></td>
                        </tr>-->
                    </thead>
                	<tbody>
                    	<tr>
                        	<th class="blue">对应供应商：</th>
                            <td class="blue mlink">
                                <logic:notEmpty name="salPurOrd" property="salSupplier">
                                    <a href="salSupplyAction.do?op=showSupplyDesc&supId=${salPurOrd.salSupplier.ssuId}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看供应商详情" style="border:0px;">${salPurOrd.salSupplier.ssuName}</a>
                                </logic:notEmpty>&nbsp;
                            </td>
                            <th class="blue">对应项目：</th>
                            <td class="blue mlink">
                            <logic:notEmpty name="salPurOrd" property="project">
                            <a href="projectAction.do?op=showProDesc&proId=${salPurOrd.project.proId}" style="cursor:pointer" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看项目详情" style="border:0px;">${salPurOrd.project.proTitle}</a>
                            </logic:notEmpty>&nbsp;
                            </td>
                        </tr>
                    	<tr>
                            <th>采购单号：</th>
                            <td>${salPurOrd.spoCode}&nbsp;</td>
                            <th>采购类别：</th>
                            <td>${salPurOrd.typeList.typName}&nbsp; </td>
                        </tr>
                        
                        <tr>
							<th>负责账号：</th>
                            <td>${salPurOrd.limUser.userSeName}&nbsp;</td>
                           	<th>采购人：</th>
                            <td>${salPurOrd.salEmp.seName}&nbsp;</td>
                            
                        </tr>
    
                        <tr>
                            <th>总金额：</th>
                            <td>
                                <span id="sumMon"></span>&nbsp;<span class="gray" title="采购金额-明细金额">[明细金额：<span id="totalPrice"></span>]</span>
    &nbsp;</td>
						  <th>采购日期：</th>
                          <td><span id="conDate"></span>&nbsp;</td>
                        </tr>
                        <tr>
                            <th class="orangeBack">已付款金额：</th>
                            <td>
                                <span id="paidMon"></span><span id="noPaid" class="gray"></span>
                            </td>
                            <th>交付状态：</th>
                            <td>&nbsp;
                                <logic:equal value="0" name="salPurOrd" property="spoIsend">未交付
                                    <img src="crm/images/content/tofinish.gif" alt="未交付" style="vertical-align:middle"/>
                                </logic:equal>
                                <logic:equal value="1" name="salPurOrd" property="spoIsend">部分交付
                                    <img src="crm/images/content/doing.gif" alt="部分交付" style="vertical-align:middle"/>
                                </logic:equal>
                                <logic:equal value="2" name="salPurOrd" property="spoIsend">全部交付
                                    <img src="crm/images/content/finish.gif" alt="全部交付" style="vertical-align:middle"/>
                                </logic:equal>
                                
                                <logic:equal value="1" name="salPurOrd" property="spoAppIsok">
                                 <span class="blue">&nbsp;[<a href="javascript:void(0)" onClick="delDiv(8,'${salPurOrd.spoId}','${salPurOrd.spoIsend}');return false;" >修改</a>]</span>
                                </logic:equal>
                            </td>
                        </tr>
                        <tr>
                            <th>
                                    <logic:notEmpty name="salPurOrd" property="attachments">
                                        <img style="vertical-align:middle; cursor:pointer" src="crm/images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${salPurOrd.spoId}','spo','reload')"/>
                                     </logic:notEmpty>
                                     <logic:empty name="salPurOrd" property="attachments">
                                        <img style="vertical-align:middle; cursor:pointer" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${salPurOrd.spoId}','spo','reload')"/>
                                     </logic:empty>
                                     附件：
                            </th>
                            <td colspan="3">
                                 <logic:iterate id="attList" name="salPurOrd" property="attachments">
                                    <span id="fileDLLi<%=c%>"></span>
                    				<script type="text/javascript">createFileToDL(<%=c%>,'${attList.attId}','${attList.attPath}','${attList.attName}','${attList.attSize}','${attList.attDate}')</script>
                                    <% c++;%>
                                </logic:iterate>&nbsp;
                            </td>
                        </tr>
                        
                        <tr class="noBorderBot">
                            <th>备注：</th>
                            <td colspan="3">${salPurOrd.spoRemark}&nbsp;</td>
                        </tr>
                    </tbody>
                    <thead>
                    	<tr>
                            <td colspan="4"><div>条件与条款</div></td>
                        </tr>
                    </thead>
					<tbody>
                    	<tr class="noBorderBot">
                            <th>条件与条款：</th>
                            <td colspan="3"><div class="divWithScroll2" style="height:120px;">${salPurOrd.spoContent}&nbsp;</div></td>
                        </tr>
                    </tbody>
					<thead>
                    	<tr>
                            <td colspan="4">
                            <div>采购明细&nbsp;&nbsp;
                            <logic:notEqual value="1" name="salPurOrd" property="spoAppIsok">
                                <span id="editRop" class="blackblue" style="padding:4px;">
                                <img class="imgAlign" src="crm/images/content/edit.gif" alt="编辑明细"/>
                                </span>
                            </logic:notEqual>
                            <logic:equal value="1" name="salPurOrd" property="spoAppIsok">
                                <span id="editRop" class="blackblue" style="padding:4px;">
                                <img class="imgAlign" src="crm/images/content/lock.gif" alt="已锁定"/>
                                </span>
                            </logic:equal>
                            </div></td>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                            <td colspan="4" class="subTab">
                                <table id="tab" class="rowstable"  cellpadding="0" cellspacing="0" style="width:100%">
                                <tbody>
                                    <tr>
                                        <th style="width:30%">产品名称/型号</th>
                                        <th style="width:10%">数量[单位]</th>
                                        <th style="width:10%">已安排入库</th>
                                        <th style="width:10%">实际入库</th>
                                        <th style="width:10%">单价</th>
                                        <th style="width:10%">总价</th>
                                        <th style=" width:10%; border-right:0px">备注</th>
                                    </tr>
                                    <logic:notEmpty name="salPurOrd" property="rspoPros">
                                    <logic:iterate id="rpp" name="salPurOrd" property="rspoPros">
                                    <tr id="ropTr<%=count%>" onMouseOver="focusTr('ropTr',<%= count%>,1)" onMouseOut="focusTr('ropTr',<%= count%>,0)">
                                        <td>&nbsp;<logic:equal value="1" name="rpp" property="wmsProduct.wprIscount"><img src="crm/images/content/database.gif" alt="计算库存" class="imgAlign"/></logic:equal>${rpp.wmsProduct.wprName}<logic:notEmpty name="rpp" property="wmsProduct.wprModel">/${rpp.wmsProduct.wprModel}</logic:notEmpty></td>
                                        <td><bean:write name="rpp" property="rppNum" format="###,##0.00"/><logic:notEmpty name="rpp" property="wmsProduct.typeList">[${rpp.wmsProduct.typeList.typName}]</logic:notEmpty><input type="hidden" id="num<%=count%>" value="${rpp.rppNum}"/></td>
                                        <td><span id="ord_anpai<%=count%>">
                                        <logic:equal value="1" name="rpp" property="wmsProduct.wprIscount">
                                            <bean:write name="rpp" property="rppOutNum" format="###,##0.00"/>
                                        </logic:equal>
                                        <logic:equal value="0" name="rpp" property="wmsProduct.wprIscount">
                                        ——
                                        </logic:equal>
                                        <input type="hidden" id="outNum<%=count%>" value="${rpp.rppOutNum}"/>&nbsp;</span></td>
                                        <td><span id="realNum<%=count%>">
                                        <logic:equal value="1" name="rpp" property="wmsProduct.wprIscount">
                                            <bean:write name="rpp" property="rppRealNum" format="###,##0.00"/>
                                        </logic:equal>
                                        <logic:equal value="0" name="rpp" property="wmsProduct.wprIscount">
                                        ——
                                        </logic:equal>
                                        <input type="hidden" id="wpr<%=count%>" value="${rpp.wmsProduct.wprCode}"/>
                                        </span>&nbsp;</td>
                                        <td>￥<bean:write name="rpp" property="rppPrice" format="###,##0.00"/><input type="hidden" id="price<%=count%>" value="${rpp.rppPrice}"/></td>
                                        <td>￥<bean:write name="rpp" property="rppSumMon" format="###,##0.00"/><input type="hidden" id="allprice<%=count%>" value="${rpp.rppSumMon}"/></td>
                                        <td>${rpp.rppRemark}&nbsp;</td>			
                                    </tr>
                                    <script type="text/javascript">	
                                        //已安排数量与采购数量差异
                                        if('${rpp.rppOutNum}'>'${rpp.rppNum}'){
                                             $("ord_anpai"+<%=count%>).innerHTML="<span class='red'><bean:write name='rpp' property='rppOutNum' format='###,##0.00'/></span>";
                                        }
                                        //实际入库数量与采购数量差异
                                        if('${rpp.rppRealNum}'>'${rpp.rppNum}'){
                                             $("realNum"+<%=count%>).innerHTML="<span class='red'><bean:write name='rpp' property='rppRealNum' format='###,##0.00'/></span>";
                                        }
                                        rowsBg('ropTr',<%=count%>);
                                    </script>
                                    <%count++;%>
                                    </logic:iterate>
                                    </logic:notEmpty>	
                                    <logic:empty name="salPurOrd" property="rspoPros">
                                        <tr>
                                            <td class="noDataTd" colspan="7">未添加产品</td>
                                        </tr>
                                    </logic:empty>
                                    <tr class="lightBlueBg bold">
                                        <td>合计</td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <td>&nbsp;</td>
                                        <td>——</td>
                                        <td><span id="ap">&nbsp;</span></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <script type="text/javascript">heji(<%=count%>);</script>	
                                </tbody>		
                                </table>   
                                <logic:notEmpty name="msg">
                                    <script type="text/javascript">  
                                        var ms='${msg}';     
                                        alert(ms);
                                    </script> 
                                </logic:notEmpty>
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
                            <td>
                                <logic:notEmpty name="salPurOrd" property="spoAppIsok">
                                    <logic:equal value="0" name="salPurOrd" property="spoAppIsok"><img class="imgAlign" src="crm/images/content/fail.gif" alt="未通过"/>&nbsp;未通过</logic:equal>
                                    <logic:equal value="1" name="salPurOrd" property="spoAppIsok"><img class="imgAlign" src="crm/images/content/suc.gif" alt="已通过/">&nbsp;已通过</logic:equal>
                                </logic:notEmpty>
                                <logic:empty name="salPurOrd" property="spoAppIsok">
                                    <img class="imgAlign" src="crm/images/content/time.gif" alt="待审核"/>&nbsp;待审核
                                </logic:empty>
                            </td>
                            <th>审核人：</th>
                            <td>${salPurOrd.spoAppMan}&nbsp;<span id="spoAppDate"></span></td>
                        </tr>
                        <tr class="noBorderBot">
                            <th>审核记录：</th>
                            <td colspan="3">
							 <logic:notEmpty name="salPurOrd" property="spoAppDesc">
							<div id="onDiv" style="display:none;">${salPurOrd.spoAppDesc}
								<div class="lightBg gray" style=" marin-top:15px; margin-right:10px;cursor:pointer; width:auto; padding:2px;text-align:center" onClick="hidDiv()" onMouseOver="this.className='grayBack blue'" onMouseOut="this.className='lightBg gray'">∧点击收起</div>
							</div>
							<div id="offDiv">
							<div  style="height:30px; text-overflow:none;overflow:hidden">${salPurOrd.spoAppDesc}
							</div>
							<div class="lightBg gray" style="marin-top:15px; margin-right:10px;cursor:pointer; width:auto; padding:2px; text-align:center" onClick="showDiv()" onMouseOver="this.className='grayBack blue'" onMouseOut="this.className='lightBg gray'">∨点击展开</div></div>
							 </logic:notEmpty>
							</td>
                        </tr>
                        <tr class="noBorderBot">
                            <td colspan="4" style="text-align:center; padding-bottom:5px">
                                <logic:equal value="1" name="salPurOrd" property="spoAppIsok">
                                	<div id="cancelAppLayer" class="grayBg" style="display:none">
                                        <button class="butSize3" onClick="delDiv(4,'${salPurOrd.spoId}')">撤销审核</button>&nbsp;&nbsp;
                                    </div>
                                    <script type="text/javascript">displayLimAllow("pu008","cancelAppLayer");</script>
                                </logic:equal>
                                
                                <logic:notEqual value="1" name="salPurOrd" property="spoAppIsok">
                                	<div id="appSpoLayer" style="display:none">
                                        <div class="orangeBack">
                                            <input type="button" class="butSize3" value="审核采购单" onClick="$('appOrd').show()"/>
                                        </div>
                                        <div id="appOrd" style="height:130px;width:100%; display:none; margin-top:5px;">
                                            <form action="salPurAction.do" method="post" id="app">
                                                <input type="hidden" name="op" value="appSpo"/>
                                                <input type="hidden" name="code" value="${salPurOrd.spoId}"/>
                                                <div class="appLayer">
                                                    <div><input type="radio" name="spoAppIsok" id="isok1" value="1"  checked="checked"><label for="isok1">通过&nbsp;</label>&nbsp;&nbsp;&nbsp;
                                                            <input type="radio" name="spoAppIsok" id="isok2" value="0" ><label for="isok2">不通过</label>
                                                    </div>
                                                    <div class="appLayerTitle">审核内容</div>
                                                    <div style="text-align:center"><textarea rows="4" style="width:100%" name="spoAppDesc"  onblur="autoShort(this,4000)"></textarea></div>
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
                                	<script type="text/javascript">displayLimAllow("pu007","appSpoLayer");</script>
                                </logic:notEqual>
                            </td>
                        </tr>
                    </tbody>
					
                    
                    <logic:equal value="1" name="salPurOrd" property="spoAppIsok">
                    <thead>
                        <tr>
                            <td colspan="4"><div>关联数据</div></td>
                        </tr>
                    </thead>
                    </logic:equal>
                 </table>
				<logic:equal value="0" name="salPurOrd" property="spoAppIsok">
                    <div class="orangeBox" style="padding:10px; text-align:center;">
                        <span class="brown">请等待审核通过...</span>
                    </div>
                 </logic:equal>
                 <logic:empty name="salPurOrd" property="spoAppIsok">
                    <div class="orangeBox" style="padding:10px; text-align:center">
                        <span class="brown">请等待审核通过...</span>
                    </div>
                </logic:empty>
                
                <logic:equal value="1" name="salPurOrd" property="spoAppIsok">
                    <!-- 新建付款的时候传入付款页面 -->
                    <input type="hidden" id="spoId" value="${salPurOrd.spoId}"/>
                    <!--<input type="hidden" id="spoResp" value="${salPurOrd.limUser.userSeName}"/>
					<input type="hidden" id="spoUserCode" value="${salPurOrd.limUser.userCode}"/>-->
					<input type="hidden" id="supName" value="${salPurOrd.salSupplier.ssuName}"/>
                    <input type="hidden" id="supId" value="${salPurOrd.salSupplier.ssuId}"/>
					<input type="hidden" id="purTil" value="${salPurOrd.spoTil}"/>
                    <div class="xpTab">
                        <span id="xpTab1" style="display:none" class="xpTabGray" onClick="swapTab(1,4,'salPurAction.do?op=getSpoPaidPlan&id=${salPurOrd.spoId}')">
                        	&nbsp;付款计划&nbsp;
                        </span>
                        <span id="xpTab2" style="display:none" class="xpTabGray" onClick="swapTab(2,4,'salPurAction.do?op=getSpoPaidPast&id=${salPurOrd.spoId}')">
                            &nbsp;付款记录&nbsp;
                       	</span>
                        <span id="xpTab3" style="display:none" class="xpTabGray" onClick="swapTab(3,4,'salPurAction.do?op=getSpoInv&id=${salPurOrd.spoId}')">
                            &nbsp;收票记录&nbsp;
                        </span>
                        <span id="xpTab4" style="display:none" class="xpTabGray" onClick="swapTab(4,4,'salPurAction.do?op=getSpoWwi&id=${salPurOrd.spoId}')">
                        	&nbsp;入库安排&nbsp;
                        </span>
                        <script type="text/javascript">
							var rigs = "apu032|apu033|af024|pu031";
							var ids = new Array("xpTab1","xpTab2","xpTab3","xpTab4");
							displayLimAllow(rigs,ids,loadXpTabSel);
                        </script>
                    </div>
                    <div class="HackBox"></div>
					<div id="ifrContent" class="tabContent" style="display:none">
                    <iframe id="ifrList" src="" scrolling="no" frameborder="0"></iframe>  
                   </div>  
                 </logic:equal>
                <div class="descStamp">
                        由
                        <span>${salPurOrd.spoInpUser}</span>
                        于
                        <span id="inpDate"></span>&nbsp;
                        录入<logic:notEmpty name="salPurOrd" property="spoAltUser">，最近由
                        <span>${salPurOrd.spoAltUser}</span>
                        于
                        <span id="changeDate"></span>&nbsp;
                        修改
                        </logic:notEmpty>
                    </div>
                    <script type="text/javascript">
						var p;//应付款
						$("sumMon").innerHTML="￥"+changeTwoDecimal("${salPurOrd.spoSumMon}");
						/* 使用数据库字段 */
						if("${salPurOrd.spoPaidMon}"==""){
							p=${salPurOrd.spoSumMon};
							$("paidMon").innerHTML="暂未付款";
						}
						else{
							p=parseFloat("${salPurOrd.spoSumMon}")-parseFloat("${salPurOrd.spoPaidMon}");
							$("paidMon").innerHTML="￥"+changeTwoDecimal("${salPurOrd.spoPaidMon}");
						}
						
						$("noPaid").innerHTML="&nbsp;[剩余应付款：<span class='red'>￥"+changeTwoDecimal(p)+"</span>]";
						
						//在采购单信息中显示明细总价
						if($("ap").innerText.indexOf("￥")!=-1){
							$("totalPrice").innerHTML="￥"+$("ap").innerText.substring(1);
							$("totalPrice").className="blue";
						}

						removeTime("conDate","${salPurOrd.spoConDate}",1);
						removeTime("inpDate","${salPurOrd.spoCreDate}",2);
						removeTime("changeDate","${salPurOrd.spoAltDate}",2);
						removeTime("sodAppDate","${salPurOrd.spoAppDate}",2);
					</script>
            </div>
	    </div>
  	  </logic:notEmpty>
	 <logic:empty name="salPurOrd">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该采购单已被删除</div>
	</logic:empty>
    
	</div>
  </body>

</html>

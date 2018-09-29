<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	int count=0,count1=0,count2=0,count3=0;
	int c=0;
	int j=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>显示询价单详情</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="stylesheet" type="text/css" href="crm/css/att.css"/>
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/spo.js"></script>
	<script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript">		
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
				window.open("salPurAction.do?op=goRinq&inqId=${inquiry.inqId}");
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
		if('${inquiry}'!=null&&'${inquiry}'!=''){
				loadEditRop();
				//表格内容省略
				loadTabShort("tab");
				if($("planTab")!=null)
					loadTabShort("planTab");
				if($("pastTab")!=null)
					loadTabShort("pastTab");
				if($("invTab")!=null)
					loadTabShort("invTab");
				if($("tab2")!=null)
					loadTabShort("tab2");
				loadXpTabStyle(4);
			}
			
			closeProgressBar();
		}
  	</script>
  </head>
  
  <body>
  	<div id="mainbox">
      <logic:notEmpty name="inquiry">
		<div id="contentbox">
        	<div id="title">采购管理 > 询价管理 > 询价单详细信息</div>
            <div class="descInf">
            	<table class="dashTab descForm" cellpadding="0" cellspacing="0">
                	<thead>
                    	<tr>
                            <th class="descTitleL">询价主题：</th>
                            <th class="descTitleR" colspan="3">
                                ${inquiry.inqTitle}&nbsp;
                            </th>
                        </tr>
                    </thead>
                	<tbody>
					 	<tr>
                            <th class="blue">对应供应商：</th>
                            <td class="blue mlink">
                                <logic:notEmpty name="inquiry" property="salSupplier">
                                    <a href="salSupplyAction.do?op=showSupplyDesc&supId=${inquiry.salSupplier.ssuId}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看供应商详情" style="border:0px;">${inquiry.salSupplier.ssuName}</a>
                                </logic:notEmpty>&nbsp;
                            </td>
                            <th class="blue">对应项目：</th>
                            <td class="blue mlink">
                            <logic:notEmpty name="inquiry" property="project">
                            <a href="projectAction.do?op=showProDesc&proId=${inquiry.project.proId}" target="_blank"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看项目详情" style="border:0px;">${inquiry.project.proTitle}</a>
                            </logic:notEmpty>&nbsp;
                            </td>
                        </tr>
                        <tr>
                            <th>询价日期：</th>
                            <td><span id="inqDate"></span>&nbsp;</td>
                            <th>询价人：</th>
                            <td>${inquiry.salEmp.seName}&nbsp;</td>
                        </tr>
                        <tr>
                        	<th>询价金额：</th>
                            <td colspan="3">
                              <logic:notEmpty name="inquiry" property="inqPrice">
                                    ￥<bean:write name="inquiry" property="inqPrice" format="###,##0.00"/>
                              </logic:notEmpty>
                              <logic:empty name="inquiry" property="inqPrice">￥0.00</logic:empty></td>
                            
                        </tr>
                        <tr class="noBorderBot">
                            <th>备注：</th>
                            <td colspan="3">${inquiry.inqRemark}&nbsp;</td>
                        </tr>
					</tbody>
                    <thead>
                    	<tr>
                            <td colspan="4">
                            <div>询价明细&nbsp;&nbsp;
                                <span id="editRop" class="blackblue" style="padding:4px;">
                                <img class="imgAlign" src="crm/images/content/edit.gif" alt="编辑明细"/>
                                </span>
                            </div></td>
                        </tr>
                    </thead>
                    <tbody>
                    	<tr>
                            <td colspan="4" class="subTab">
                                <table id="tab" class="rowstable" style="width:100%" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <th style="width:30%">产品名称/型号</th>
                                        <th style="width:10%">数量[单位]</th>
                                        <th style="width:10%">单价</th>
                                        <th style="width:10%">总价</th>
                                        <th style=" width:10%; border-right:0px">备注</th>
                                    </tr>
                                    <logic:notEmpty name="inquiry" property="rinqPros">
                                    <logic:iterate id="rpp" name="inquiry" property="rinqPros">
                                    <tr id="ropTr<%=count%>" onMouseOver="focusTr('ropTr',<%= count%>,1)" onMouseOut="focusTr('ropTr',<%= count%>,0)">
                                        <td>&nbsp;<logic:equal value="1" name="rpp" property="wmsProduct.wprIscount"><img src="crm/images/content/database.gif" alt="计算库存" class="imgAlign"/></logic:equal>${rpp.wmsProduct.wprName}<logic:notEmpty name="rpp" property="wmsProduct.wprModel">/${rpp.wmsProduct.wprModel}</logic:notEmpty></td>
                                        <td><bean:write name="rpp" property="rqpNum" format="###,##0.00"/><logic:notEmpty name="rpp" property="wmsProduct.typeList">[${rpp.wmsProduct.typeList.typName}]</logic:notEmpty><input type="hidden" id="num<%=count%>" value="${rpp.rqpNum}"/></td>
                                        <td>￥<bean:write name="rpp" property="rqpPrice" format="###,##0.00"/><input type="hidden" id="price<%=count%>" value="${rpp.rqpPrice}"/></td>
                                        <td>￥<bean:write name="rpp" property="rqpAllPrice" format="###,##0.00"/><input type="hidden" id="allprice<%=count%>" value="${rpp.rqpAllPrice}"/></td>
                                        <td>${rpp.rqpRemark}&nbsp;</td>			
                                    </tr>
                                    <script type="text/javascript">	
                                        rowsBg('ropTr',<%=count%>);
                                    </script>
                                    <%count++;%>
                                    </logic:iterate>
                                     </logic:notEmpty>	
                                     <logic:empty name="inquiry" property="rinqPros">
                                        <tr>
                                            <td class="noDataTd" colspan="5">未添加产品</td>
                                        </tr>
                                     </logic:empty>
                                    <tr class="lightBlueBg bold">
                                        <td>合计</td>
                                        <td>&nbsp;</td>
                                        <td>——</td>
                                        <td><span id="ap">&nbsp;</span></td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <script type="text/javascript">heji(<%=count%>);</script>		
                                </table>   
                            </td>
                        </tr>
                    </tbody>
                    
                 </table>
				
                <div class="descStamp">
                        由
                        <span>${inquiry.inqInpUser}</span>
                        于
                        <span id="inpDate"></span>&nbsp;
                        录入<logic:notEmpty name="inquiry" property="inqUpdUser">，最近由
                        <span>${inquiry.inqUpdUser}</span>
                        于
                        <span id="changeDate"></span>&nbsp;
                        修改
                        </logic:notEmpty>
                    </div>
                    <script type="text/javascript">
						removeTime("inpDate","${inquiry.inqInsDate}",2);
						removeTime("changeDate","${inquiry.inqUpdDate}",2);
						removeTime("inqDate","${inquiry.inqDate}",1);
					</script>
            </div>
	    </div>
  	  </logic:notEmpty>
	 <logic:empty name="inquiry">
		    <div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该询价单已被删除</div>
	</logic:empty>
    
	</div>
  </body>

</html>

<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>采购单详细资料</title>
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
    <script type="text/javascript" src="crm/sup/sup.js"></script>
	<script type="text/javascript">
		function initPage(){
			removeTime("puoPurDate","${purOrd.puoPurDate}",1);
			if('${purOrd.puoIsEnd}'=='0'){
				$("isEnd").innerHTML="未交付";
			}
			else{
				$("isEnd").innerHTML="已交付";
			}
		}
		function reCreateSumRow(){
			//sumValue[6] = 0;
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			if(obj.wmsProduct){
				ropIds.push(obj.rppId);
				dblFunc="descPop('prodAction.do?op=wmsProDesc&wprId="+obj.wmsProduct.wprId+"')";
				var proName="<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.wmsProduct.wprName+"</a>";
				datas = [ obj.wmsProduct.wprCode, proName,obj.wmsProduct.wprSalePrc, obj.rppPrice, obj.rppNum, [obj.rppSumMon,obj.rppSumMon],obj.rppRemark];

				return [datas,className,dblFunc,dataId];
			}
			else{
				return [];
			}
		}
		function loadRppList(sortCol,isDe,pageSize,curP){
			ropIds = [];
			var url = "supAction.do";
			var pars = [];
			pars.op = "listPuoPro";
			pars.puoId = "${purOrd.puoId}";
			var loadFunc = "loadRppList";
			var cols=[
				{name:"编号",width:"10%"},
				{name:"名称",align:"left",width:"15%"},
				{name:"标准价格",width:"10%",renderer:"money"},
				{name:"采购价格",width:"10%",renderer:"money"},
				{name:"采购数量",width:"15%"},
				{name:"总价",renderer:"money",align:"right",width:"15%"},
				{name:"备注",align:"left",width:"25%"}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		var ropIds = [];//明细产品ID集合
		var gridEl = new MGrid("puoProdListTab","dataList");
		gridEl.config.hasPage=false;
		gridEl.config.sortable=false;
		gridEl.config.isResize=false;
		gridEl.config.hasSumRow=true;
		gridEl.config.sumRowCallBack=reCreateSumRow;
		createProgressBar();
		window.onload=function(){
			initPage();
			loadRppList();
			closeProgressBar();
		}
  	</script>
  	</head>
  <body>
    <div id="descContainer">
    <c:if test="${!empty purOrd}">
        <div class="descInf">
            <div id="descTop">
                <span class="descOp"><c:if test="${purOrd.puoIsEnd==0}">[<a href="javascript:void(0);" onClick="supPopDiv(3,'${purOrd.puoId}');return false;" title="编辑">编辑</a>]</c:if>&nbsp;&nbsp;&nbsp;</span>
                采购单详细信息<a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a></div>
            <table class="dashTab descForm2" cellpadding="0" cellspacing="0">
            	<thead>
                </thead>
            	<tbody>
                	<tr>
                    	<th>采购单号：</th>
                        <td>${purOrd.puoCode}&nbsp;</td>
                    	<th>采购日期：</th>
                        <td><span id="puoPurDate"></span>&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>供应商：</th>
                        <td colspan="3" class="blue mlink">
                        	<c:if  test="${! empty purOrd.puoSup}">
                        		<a href="javascript:void(0)" onClick="descPop('supAction.do?op=toDescSup&supId=${purOrd.puoSup.supId}')"><img class="imgAlign" src="crm/images/content/showDesc.gif" alt="查看供应商详情" style="border:0px;">${purOrd.puoSup.supName}</a>
                        	</c:if>
                        	&nbsp;
                        </td>
                    </tr>
                    <tr>
                    	<th>采购总金额：</th>
                        <td id="puoM"><c:if test="${!empty purOrd.puoM}">￥<fmt:formatNumber value="${purOrd.puoM}" pattern="#,##0.00"/></c:if>&nbsp;</td>
                        <th>已付款金额：</th>
                        <td id="puoPaidM"><c:if test="${!empty purOrd.puoPaidM}">￥<fmt:formatNumber value="${purOrd.puoPaidM}" pattern="#,##0.00"/></c:if>&nbsp;</td>
                    </tr>
                    <tr>
                    	<th>采购人：</th>
                        <td>${purOrd.puoEmp.seName}&nbsp;</td>
                        <th>类别：</th>
                        <td>${purOrd.puoType.typName}&nbsp;</td>   
                    </tr>
                    <tr>
                     	<th>交付状态：</th>
                        <td colspan="3"><span id="isEnd"></span>&nbsp;&nbsp;<a href="javascript:void(0)" onClick="supPopDiv(4,'${purOrd.puoId}'); return false;">[修改]</a></td>
                    </tr>
                    <tr>
                    	<th>条件与条款：</th>
                        <td colspan="3">${purOrd.puoContent}&nbsp;</td>
                    </tr>
                   	<tr>
                    	<th>备注：</th>
                        <td colspan="3">${purOrd.puoRemark}&nbsp;</td>
                    </tr>
                   <tr class="noBorderBot">
                    	<th><c:if test="${purOrd.puoIsEnd==1}"><img class="imgAlign" src="crm/images/content/lock.gif" alt="采购单已交付，无法修改明细"/>&nbsp;</c:if>采购明细：
                    	</th>
                    	<td>
							<span style="padding:4px; font-size:14px;">
								<c:if test="${purOrd.puoIsEnd!=1}">
	                            	<span class="blue">[<a href="supAction.do?op=toUpdRpp&puoId=${purOrd.puoId}" target="_blank">编辑采购明细</a>]</span>
								</c:if>
                                <c:if test="${purOrd.puoIsEnd==1}">
                                    <span class="blue">[<a href="javascript:void(0)" onClick="">入库</a>]</span>
                                </c:if>
	                        </span>
                    	</td>
                    </tr>
                    <tr>
                        <td colspan="4" class="subTab"><div id="dataList" class="dataList" style="height:auto"></div></td>
                    </tr>     
                </tbody>
			</table>
            <div class="descStamp">
                由
                <span>${purOrd.puoCreMan}</span>
                于
                <span id="creTime"></span>&nbsp;
                录入<c:if test="${!empty purOrd.puoUpdMan}">，最近由
                <span>${purOrd.puoUpdMan}</span>
                于
                <span id="updTime"></span>&nbsp;
                修改
                </c:if>
            </div>
            <script type="text/javascript">
                removeTime("creTime","${purOrd.puoCreTime}",2);
                removeTime("updTime","${purOrd.puoUpdTime}",2);
            </script>
        </div>
    </c:if>
	<c:if test="${empty purOrd}">
		<div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该采购单不存在</div>
	</c:if>
	</div>
  </body>
</html>



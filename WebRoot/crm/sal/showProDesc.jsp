<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
int count1=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title>产品详细资料</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	 <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <script type="text/javascript" src="crm/js/prototype.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/sal.js"></script>
	<script type="text/javascript">
		function showImg(img){
			window.open("wms/pic.jsp?img="+img,"查看图片");
		}
		function edit(){
			ordPopDiv(51,'${wmsProduct.wprId}');
		}
		createProgressBar();
		window.onload=function(){
			if("${wmsProduct}"!=""){
				loadXpTabSel();
				reSizePic("productPic",300,150);
				if($("noTaxPrc")!=null){
					$("noTaxPrc").innerHTML = "(不含税"+changeTwoDecimal(parseFloat("${wmsProduct.wprSalePrc}")/(1+getSalTaxRate()))+")";
				}
			}
			closeProgressBar();
		}
	</script>
  </head>
  
  <body>
     <div id="mainbox">
     	<c:if test="${!empty wmsProduct}">
    	<div id="contentbox">
            <div class="descInf">
            	<div id="descTop">
            	<span class="descOp">[<a href="javascript:void(0);" onClick="edit();return false;" title="编辑">编辑</a>]</span>
            	产品详细信息&nbsp;<a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a></div>	
                <table class="dashTab descForm" cellpadding="0" cellspacing="0">
					<thead>
						<tr>
							<th class="descTitleL">产品名称：</th>
							<th class="descTitleR" colspan="3">
								${wmsProduct.wprName}&nbsp;
							</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<th>产品编号：</th>
							<td>${wmsProduct.wprCode}&nbsp;</td>
							<th rowspan="6" style="vertical-align:top; border-bottom:0px">图&nbsp;&nbsp;片：</th>
							<td rowspan="6" style="border-bottom:0px">
								<c:if test="${!empty wmsProduct.wprPic}">
									<img id="productPic" src="${wmsProduct.wprPic}" alt="产品图片" style="cursor:pointer;border:1px solid #D4D4D4" onClick="showImg('${wmsProduct.wprPic}')">
								</c:if>&nbsp;
							</td>
					  	</tr>
                        <tr>
                            <th>产品类别：</th>
                            <td>${wmsProduct.wmsProType.wptName}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>单位：</th>
                            <td>${wmsProduct.typeList.typName}&nbsp;</td>				
                        </tr>
                        <tr>
                            <th>标准价格：</th>
                            <td >
                            ￥<fmt:formatNumber value="${wmsProduct.wprSalePrc}" pattern="#,##0.00"/>&nbsp;
                            <c:if test="${wmsProduct.wprSalePrc >0}"><span id="noTaxPrc" class="deepGray"></span></c:if>
                            </td>
                        </tr>
                        <tr>
		                	 <th>普通提成率：</th>
                             <td>${wmsProduct.wprNormalPer}&nbsp;</td>	
		                </tr>
                        <tr>
		                	 <th>超额提成率：</th>
                             <td>${wmsProduct.wprOverPer}&nbsp;</td>	
		                </tr>
                        <tr>
                            <th>产品详情：</th>
                            <td colspan="3">${wmsProduct.wprDesc}&nbsp;</td>
                        </tr>
                        <tr>
                            <th>备注：</th>
                            <td colspan="3" >${wmsProduct.wprRemark}&nbsp;</td>
                        </tr>
                     	<!--<tr>
                            <th>销售情况：</th>
                            <td colspan="3">
                            &nbsp;<img src="crm/images/content/blueArr.gif" alt="查看"/>
                            <a href="prodAction.do?op=findOrdByWpr&wprId=${wmsProduct.wprId}" target="_blank">查看本产品销售历史</a>
                            </td>
                    	</tr>-->
                    	<tr  class="noBorderBot">
                    		<th>
                    			<c:if test="${!empty wmsProduct.attachments}">
                    				<img id="${wmsProduct.wprId}y" style="vertical-align:middle; cursor:pointer" src="crm/images/content/attach.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${wmsProduct.wprId}','allProd','reload')">  
                    			</c:if>
                    			<c:if test="${empty wmsProduct.attachments}">
                    				 <img id="${wmsProduct.wprId}n" style="vertical-align:middle; cursor:pointer" src="crm/images/content/attachNo.gif" border="0px" alt="附件管理" onClick="commonPopDiv(1,'${wmsProduct.wprId}','allProd','reload')">                       
                    			</c:if>
                    		 附件：      
                    		</th>
                    		<td>
                    			<c:forEach var="attList" items="${wmsProduct.attachments}" varStatus="fileIndex">
                                	<span id="fileDLLi${fileIndex.index}"></span>
                    			<script type="text/javascript">createFileToDL("${fileIndex.index}","${attList.attId}","${attList.attPath}","${attList.attName}","${attList.attSize}","${attList.attDate}")</script>
                            	</c:forEach>
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
                  <span id="xpTab1" class="xpTabGray" onClick="swapTab(1,5,'prodAction.do?op=toListProdSalBack&prodId=${wmsProduct.wprId}')">&nbsp;未达标提成率&nbsp;</span>
                  <span id="xpTab2" class="xpTabGray" onClick="swapTab(2,5,'prodAction.do?op=toListProdTrans&prodId=${wmsProduct.wprId}')">&nbsp;运费标准&nbsp;</span>
                  <span id="xpTab3" class="xpTabGray" onClick="swapTab(3,5,'prodAction.do?op=toListProdHis&prodId=${wmsProduct.wprId}')">&nbsp;销售明细&nbsp;</span>
                  <span id="xpTab4" class="xpTabGray" onClick="swapTab(4,5,'supAction.do?op=toListRPuoPro&wprId=${wmsProduct.wprId}')">&nbsp;采购单明细&nbsp;</span>
                  <span id="xpTab5" class="xpTabGray" onClick="swapTab(5,5,'supAction.do?op=toListProductStore&wprId=${wmsProduct.wprId}')">&nbsp;关联库存&nbsp;</span>
               </div>
               <div class="HackBox"></div>
				<div id="ifrContent" class="tabContent" style="display:none">
                    <iframe id="ifrList" src="" scrolling="no" frameborder="0"></iframe>  
                </div> 
                <div class="descStamp">
                    由
                    <span>${wmsProduct.wprCuserCode}</span>
                    于
                    <span id="inpDate"></span>&nbsp;
                    录入<c:if test="${!empty wmsProduct.wprEuserCode}">，最近由
                    <span>${wmsProduct.wprEuserCode}</span>
                    于
                    <span id="changeDate"></span>&nbsp;
                    修改
                    </c:if>
                </div>
                <script type="text/javascript">
                    removeTime("inpDate","${wmsProduct.wprCreDate}",2);
                    removeTime("changeDate","${wmsProduct.wprEditDate}",2);
                </script>
  			</div>
     	</div>
        </c:if>
        <c:if test="${empty wmsProduct}">
        	<div class="redWarn"><img src="crm/images/content/fail.gif" alt="警告" style="vertical-align:middle"/>&nbsp;该产品已被删除</div>
        </c:if>
  	</div>
  </body>
</html>

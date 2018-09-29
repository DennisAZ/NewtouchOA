<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
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
    
    <title>出入库历史</title>
    <link rel="shortcut icon" href="favicon.ico"/> 
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
	<script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript" src="js/FusionCharts.js"></script>
    <script type="text/javascript">
		function countAll(n){
			for(var i=n-1;i>-1;i--){
				var num=$("num"+i).innerText;
				if(i==n-1){
					$("allNum"+i).innerText=num.substring(1);
				}else{
					var j=i+1;
					var allnum=$("allNum"+j).innerText;
					allnum=parseFloat(allnum)+parseFloat(num);
					$("allNum"+i).innerText=changeTwoDecimal(Math.round(allnum*100)/100);
				}
			}
		}
		
		createProgressBar();	
		window.onload=function(){
			//表格内容省略
			loadTabShort("tab");
			closeProgressBar();
			changeChar(2);
		}
    </script>
  </head>
  
  <body>
  	<div class="disType" style="text-align:left">
			<span class="bold" style=" margin-left:10px;">显示类型:</span>
			<a href="javascript:void(0)" onClick="changeChar(2);return false;">折线图 </a>
			<a href="javascript:void(0)" onClick="changeChar(1);return false;">面积图 </a>
	</div>
    <div id="chartDiv" align="left"  style="padding:10px;"></div>
  	<div style="padding:10px; width:100%;">
  		<div style="padding:2px; padding-left:0px; text-align:left; color:#666666">
        	<img src="images/content/wpr_pro.gif" class="imgAlign"/>&nbsp;产品&nbsp;<span class="bold blue">${wmsProduct.wprName}<logic:notEmpty name="wmsProduct" property="wprModel">/${wmsProduct.wprModel}</logic:notEmpty><logic:notEmpty name="wmsProduct" property="typeList"><span class="brown">[${wmsProduct.typeList.typName}]</span></logic:notEmpty></span>&nbsp;的库存历史
        </div>
		<div class="dataList" >
        <table id="tab" class="normal rowstable" cellspacing="0" cellpadding="0">
          <tr>
            <th style="width:15%">日期</th>
            <th style="width:15%">经手人</th>
            <th style="width:15%">操作</th>
			<th style="width:25%">仓库</th>
            <th style="width:15%">变化数量</th>
            <th style="width:15%; border-right:0px">对应总库存</th>
          </tr>
          <logic:notEmpty name="wmsLine">
            <logic:iterate id="rwc" name="wmsLine" scope="request">
              <tr id="tr1<%=count%>" onMouseOver="focusTr('tr1',<%= count%>,1)" onMouseOut="focusTr('tr1',<%= count%>,0)">
                <td><span id="date<%=count%>">&nbsp;</span></td>
                <td>${rwc.wliMan}&nbsp;</td>
                <td>
                    <logic:equal value="0" name="rwc" property="wliType">入库</logic:equal>
                    <logic:equal value="1" name="rwc" property="wliType">出库</logic:equal>
                    <logic:equal value="2" name="rwc" property="wliType">调拨调入</logic:equal>
                    <logic:equal value="3" name="rwc" property="wliType">调拨调出</logic:equal>
                    <logic:equal value="4" name="rwc" property="wliType">盘点差异</logic:equal>
				</td>
				<td>${rwc.wmsStro.wmsName}&nbsp;</td>	
                <td><span id="num<%=count%>">
                    <logic:notEmpty name="rwc" property="wliOutNum">-<bean:write name="rwc" property="wliOutNum" format="0.00"/></logic:notEmpty>
                    <logic:notEmpty name="rwc" property="wliInNum">+<bean:write name="rwc" property="wliInNum" format="0.00"/></logic:notEmpty>
                </span></td>
                <td><!--<span id="allNum<%=count%>">&nbsp;</span>--> <bean:write name="rwc" property="wmsAllNum" format="0.00"/></td>
              </tr>
              <script type="text/javascript">
				dateFormat('date','${rwc.wliDate}',<%=count%>)
				/*var num=$("num<%=count%>").innerText;
				if(<%=count%>==0){
					$("allNumnum<%=count%>").innerText=num.substring(1);
				}else{
					var i=parseInt(<%=count%>)-1;
					var allnum=$("allNumnum"+i).innerText;
					allnum=parseFloat(allnum)+parseFloat(num);
					$("allNumnum<%=count%>").innerText=changeTwoDecimal(Math.round(allnum*100)/100);
				}*/
				rowsBg('tr1',<%=count%>);
	 	 		</script>
              <%count++;%>
            </logic:iterate>
			<script type="text/javascript">//countAll(<%=count%>)</script>
			
          </logic:notEmpty>
          <logic:empty name="wmsLine">
            <tr>
              <td colspan="6"><div align="center" class="gray" id="rs">本产品暂时没有出入库历史...</div></td>
            </tr>
          </logic:empty>
        </table>
		 <logic:notEmpty name="wmsLine">
              <script type="text/javascript">
							createPage('wwoAction.do?op=wprHistory&wprId=${wmsProduct.wprId}','${page.rowsCount}','${page.pageSize}','${page.currentPageNo}','${page.prePageNo}','${page.nextPageNo}','${page.pageCount}');
			 </script>      	
         </logic:notEmpty>  
		 </div>
   	</div>
	
	<script language="JavaScript">
	function changeChar(n){
		  var char;
		  if(n=='1'){
		  	  char='./salStat/freeChart/charts/Area2D.swf';
		  }
		  if(n=='2'){
		  	  char='./salStat/freeChart/charts/Line.swf';
		  }
		  var chart1 = new FusionCharts(char, "chart1Id", "900", "500");	
		  var url=escape('wwoAction.do?op=getWprHisXML&wprId=${wmsProduct.wprId}&type='+n);
			chart1.setDataURL(url);
			chart1.render("chartDiv");
		}
	</script>
	<!--<div style="padding:10px; width:100%;">
		<div style=" text-align:left; padding:20px 2px 2px 0px; color:#666666">
        	<img src="images/content/wpr_pro.gif" class="imgAlign"/>&nbsp;产品&nbsp;<span class="bold blue">${wmsProduct.wprName}<logic:notEmpty name="wmsProduct" property="wprModel">/${wmsProduct.wprModel}</logic:notEmpty><logic:notEmpty name="wmsProduct" property="typeList"><span class="brown">[${wmsProduct.typeList.typName}]</span></logic:notEmpty></span>&nbsp;的单据操作历史
        </div>
        <table align="left" class="normal graytable" width="80%" border="0" cellspacing="0" cellpadding="0">
          <tr>
		 	 <th width="15%"><nobr>单据类型</nobr></th>
             <th width="15%"><nobr>日期</nobr></th>
             <th width="15%"><nobr>经手人</nobr></th>
			 <th width="25%"><nobr>仓库</nobr></th>
             <th width="10%"><nobr>数量</nobr></th>
			 <th width="10%"><nobr>状态</nobr></th>
			 <th style="width:10%;border-right:0px">操作</th>
          </tr>
          <logic:notEmpty name="wmsLine2">
            <logic:iterate id="rwc" name="wmsLine2" scope="request">
              <tr id="tr2<%=count1%>" onMouseOver="focusTr('tr2',<%= count1%>,1)" onMouseOut="focusTr('tr2',<%= count1%>,0)">
			    <td>
                    <logic:equal value="0" name="rwc" property="wliType">入库单</logic:equal>
                    <logic:equal value="1" name="rwc" property="wliType">出库单</logic:equal>
                    <logic:equal value="2" name="rwc" property="wliType">调拨调入</logic:equal>
                    <logic:equal value="3" name="rwc" property="wliType">调拨调出</logic:equal>
                    <logic:equal value="4" name="rwc" property="wliType">盘点</logic:equal>
				</td>
                <td><span id="da<%=count1%>">&nbsp;</span></td>
                <td>${rwc.wliMan}&nbsp;</td> 
				<td>${rwc.wmsStro.wmsName}&nbsp;</td>	
                <td>
					<logic:notEmpty name="rwc" property="wliOutNum">-<bean:write name="rwc" property="wliOutNum" format="###,##0.00"/></logic:notEmpty>
                    <logic:notEmpty name="rwc" property="wliInNum">+<bean:write name="rwc" property="wliInNum" format="###,##0.00"/></logic:notEmpty>
                </td>
                <td style="text-align:center">
						<logic:equal value="0" name="rwc" property="wliState"><img src="images/content/working.gif" alt="未执行"></logic:equal>
						<logic:equal value="1" name="rwc" property="wliState"><img src="images/content/suc.gif" alt="已执行"></logic:equal>
						<logic:equal value="2" name="rwc" property="wliState"><img src="images/content/warn.gif" alt="已撤销"></logic:equal>
				</td>
				<td>
							<logic:equal value="0" name="rwc" property="wliType"><a href="javascript:void(0);"onClick="addDivNew(5,'${rwc.wliTypeCode}');return false;" style="cursor:pointer">对应单据</a></logic:equal>
							<logic:equal value="1" name="rwc" property="wliType"><a href="javascript:void(0);"onClick="addDivNew(10,'${rwc.wliTypeCode}');return false;" style="cursor:pointer">对应单据</a></logic:equal>
							<logic:equal value="2" name="rwc" property="wliType"><a href="javascript:void(0);"onClick="addDivNew(17,'${rwc.wliTypeCode}');return false;" style="cursor:pointer">对应单据</a></logic:equal>
							<logic:equal value="3" name="rwc" property="wliType"><a href="javascript:void(0);"onClick="addDivNew(17,'${rwc.wliTypeCode}');return false;" style="cursor:pointer">对应单据</a></logic:equal>
							<logic:equal value="4" name="rwc" property="wliType"><a href="javascript:void(0);"onClick="addDivNew(21,'${rwc.wliTypeCode}');return false;" style="cursor:pointer">对应单据</a></logic:equal>
							</td>
              </tr>
              <script type="text/javascript">
				dateFormat('da','${rwc.wliDate}',<%=count1%>);
				rowsBg('tr2',<%=count1%>);
	 	 	</script>
              <%count1++;%>
            </logic:iterate>
          </logic:notEmpty>
          <logic:empty name="wmsLine2">
            <tr>
              <td colspan="6"><div align="center" class="gray" id="rs">本产品暂时没有单据操作历史...</div></td>
            </tr>
          </logic:empty>
        </table>
  	</div>-->
  </body>
</html>

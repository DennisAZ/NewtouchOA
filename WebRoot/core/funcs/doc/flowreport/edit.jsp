<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ include file="/core/funcs/doc/workflowUtility/workflowheader.jsp" %>
<%
 String rId=request.getParameter("rId");
String fId=request.getParameter("fId");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>编辑报表</title>
</head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<style type="text/css">
.sortable { list-style-type: none; margin: 0; padding: 0; }
.sortable li { margin: 3px 3px 3px 0; padding: auto; float: left; width: 100px; height: 20px; font-size: 9pt; text-align: center;cursor:move;border:1px solid #eee;background:#ddd;}
</style>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-1.4.2.js"></script>
<script language="JavaScript">
jQuery.noConflict();
</script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery-ui.custom.min.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/jquery/jquery.ui.sortable.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Accordion.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/workflowUtility/utility.js"></script>
<script type="text/javascript" src="<%=contextPath %><%=moduleContextPath %>/flowreport/js/report.js"></script>
<script type="text/javascript">
  var rId='<%=rId%>';
  var fId='<%=fId%>';
function doInit(){
   addOption();
   jInit();
   InitList();
}

function InitList(){
	  var url = contextPath + "<%=moduleSrcPath %>/act/YHFlowReportAct/editReportAct.act";
	  var json = getJsonRs(url,"rId="+rId);
	  if(json.rtState == '0'){	  
		  bindJson2Cntrl(json.rtData); 
		  var data = json.rtData.listItem;
		  jQuery("#field").val("field_list");
		  addItemFor(data);
      data = json.rtData.queryItem;
      jQuery("#field").val("query_list");
      addItemFor(data);
    }
 }

function addItemFor(data){
	  for(var i=0;i<data.length;i++){
          addItem(data[i]);
    }	
}
</script>

<body class="bodycolor" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
        <td class="Big"> <img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3">  编辑报表</span>&nbsp;
        </td>
    </tr>
</table>
<form action="<%=contextPath %><%=moduleSrcPath %>/act/YHFlowReportAct/updateReportAct.act?rId=<%=rId %>&fId=<%=fId %>" enctype="multipart/form-data" method="post" id="form1" name="form1">
<table border="0" width="100%" class="TableList"  align="center">
    <tr>
        <td class="TableContent" width=100 align="right">报表名称：</td>
        <td class="TableData">
        <input type="text" name="r_name" size="30" id="r_name" value="">
        </td>
    </tr>

    <tr>
        <td class="TableContent" align="right">统计方式：</td>
        <td class="TableData">
        <input type="radio" name="group_type" id="GroupType1" value="0" /><label for="GroupType1">按分组统计计算</label>
        <input type="radio" name="group_type" id="GroupType2" value="1" ><label for="GroupType2">按分组列出详情</label>
        </td>
    </tr>
    <tr>
        <td class="TableContent" align="right">分组字段：</td>
        <td class="TableData">
        <select name="group_field" id="group_field">
            <option value="run_id">流水号</option>
           
        </select>
        </td>
    </tr>
    <tr>
        <td class="TableContent" align="right">报表字段：<br/>
        <a title="添加报表显示字段" class="orgAdd"  id="addFieldBtn" href="#" onClick="addFieldBtn();"> 添加 </a></td>
        <td class="TableData">
        <ul id="field_list" class="sortable">
        </ul>
        </td>
    </tr>
    <tr>
        <td class="TableContent" align="right">查询条件字段：<br/>
        <a title="添加查询字段" class="orgAdd" href="#" id="addQueryBtn"> 添加 </a></td>
        <td class="TableData">
        <ul id="query_list" class="sortable">
        </ul>         
        </td>
    </tr>
    <tr class="TableContorl">
        <td colspan="2" align="center">
          <input type="hidden" name="rid" value=""/>
          <input type="hidden" name="list_item" id="list_item" value=""/>
          <input type="hidden" name="query_item" id="query_item" value=""/>
          <input type="submit" class="BigButton" value="保存"/>
          <input type="button" class="BigButton" onClick="location = 'list.jsp?fId=<%=fId %>'" value="返回"/>
        </td>
    </tr>
</table>
</form>
<div id="field_div" style="display:none">
    <table border="0" width="100%" class="TableList"  align="center">
        <tr>
            <td class="TableContent">选择表单字段</td>
            <td class="TableData" align="left">
            <select name="field_name" id="field_name">
                <option value="" selected></option>
                <option value="run_id">流水号</option>
               
            </select>
            </td>
        </tr>
        <tr>
            <td class="TableContent" >字段显示名称</td>
            <td class="TableData" align="left">
            <input type="text" size=15 class="SmallInput" name="disp_name" id="disp_name">
            </td>
        </tr>
        <tr id="tcalc">
            <td class="TableContent">计算方式</td>
            <td align="left" class="TableData">
                <input type="radio" name="calc_type" value="0" id="calc_sum" /><label for="calc_sum">求和</label>
                <input type="hidden" name="calc_type" value="1" id="calc_avg_weight" />
                <input type="radio" name="calc_type" value="2" id="calc_avg" /><label for="calc_avg">求平均</label>
                <input type="hidden" name="calc_type" value="3" id="calc_def" onClick="setCalc('3');" />
                <div id="formula_div" style="display:none">自定义公式：<input type="text" name="calc_formula" id="calc_formula" value=""></div>
                <div id="weight_div" style="display:none">权重字段：                <select name="calc_weight" id="calc_weight">
                <option value=""> </option>
                 </select>
                </div>
            </td>
        </tr>
        <!--
        <tr id="tcalc">
            <td class="TableContent">计算条件</td>
            <td>
               <select name="field_name" id="field_name">
                <option value="run_id">流水号</option>
                <?foreach ($element_array as $element):?>
                <option value="<?=$element['field']?>"><?=$element['title']?></option>
                <? endforeach;?>
            </select>
            </td>
        </tr>
        -->
        <tr class="TableContorl">
            <td colspan="2" align="center">
            <input type="hidden" id="field" value="">
            <input type="hidden" id="item_old" value="">
            <input type="button" class="SmallButton" id="doAddFieldBtn" value="添加"/>
            <input type="button" class="SmallButton" style="display:none;" id="doUpdateFieldBtn" value="更新"/>
            <input type="button" class="SmallButton" style="display:none;" id="doDelFieldBtn" value="删除"/>
            <input type="button" class="SmallButton" id="CancelAddFieldBtn" onClick="clo();" value="关闭"/>
            </td>
        </tr>
    </table>
</div>
</body>
</html>

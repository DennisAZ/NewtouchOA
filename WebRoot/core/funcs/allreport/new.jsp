<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%
 String rId=request.getParameter("rId");

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>新建报表</title>
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
<script type="text/javascript" src="<%=contextPath %>/core/funcs/allreport/js/report.js"></script>
<script type="text/javascript">
var rId='<%=rId%>';
var mId=rId;
function doInit(){
   $("flow_id").value = rId;
   addOption(rId);
   jInit();
 }


function setName(){
  //alert($("field_name").selectedIndex);
  var tempindex=$("field_name").selectedIndex;
  $("disp_name").value=$("field_name").options[tempindex].text;
}

function setName1(){
	  //alert($("field_name").selectedIndex);
	  var tempindex=$("group_name").selectedIndex;
	  $("disp_name").value=$("group_name").options[tempindex].text;
	}
</script>

<body class="bodycolor" onLoad="doInit();">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
    <tr>
        <td class="Big"> <img src="<%=imgPath%>/notify_open.gif" align="absmiddle"><span class="big3">  新建报表</span>&nbsp;
        </td>
    </tr>
</table>
<form action="<%=contextPath %>/yh/core/funcs/allreport/act/YHAllReportAct/addReportAct.act" enctype="multipart/form-data" method="post" id="form2" name="form2">
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
        <input type="radio" name="group_type" id="GroupType1" value="0" onClick="addOption1('<%=rId%>')" /><label for="GroupType1">按分组统计计算</label>
        <input type="radio" name="group_type" id="GroupType2" value="1" onClick="addOption2('<%=rId%>')" ><label for="GroupType2">按分组列出详情</label>
        </td>
    </tr>
    <tr>
        <td class="TableContent" align="right">分组字段：</td>
        <td class="TableData">
        <select name="group_field" id="group_field">
          
           
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
          <input type="hidden" id="flow_id" name="flow_id" value=""/>
          <input type="hidden" id="list_item" name="list_item" id="list_item" value=""/>
          <input type="hidden" id="query_item" name="query_item" id="query_item" value=""/>
          <input type="submit"  class="BigButton" value="保存"/>
          <input type="button" class="BigButton" onClick="history.go(-1)" value="返回"/>
        </td>
    </tr>
</table>
</form>
<div id="field_div" style="display:none">
    <table border="0" width="100%" class="TableList"  align="center">
        <tr>
            <td class="TableContent">选择表单字段</td>
            <td class="TableData" align="left">
            <select name="field_name" id="field_name" onChange="setName(); ">
                <option value="" selected></option>

            </select>
            
           
            </td>
        </tr>
        <tr>
            <td class="TableContent" >字段显示名称</td>
            <td class="TableData" align="left">
            <input type="text" size=15 class="SmallInput" name="disp_name" id="disp_name">
                 <input type="hidden" name="calc_type" checked value="0" id="calc_sum" />
                <input type="hidden" name="calc_type" value="1" id="calc_avg_weight" />
                <input type="hidden" name="calc_type" value="2" id="calc_avg" />
                <input type="hidden" name="calc_type" value="3" id="calc_def" onClick="setCalc('3');" />
          
            </td>
        </tr>
       
           
               
          
       
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

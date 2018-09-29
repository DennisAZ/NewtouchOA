<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.inforesouce.data.*"%>
<html>
<head>
<title>信息资源管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link rel="stylesheet" href = "<%=cssPath%>/cmp/Calendar.css">
<link rel="stylesheet" type="text/css" href="/inc/js/jquery/page/css/page.css"/>
<link rel="stylesheet" type="text/css" href="/theme/<?=$LOGIN_THEME?>/calendar.css"/>
<style>
.tip {position:absolute;display:none;text-align:center;font-size:9pt;font-weight:bold;z-index:65535;background-color:#DE7293;color:white;padding:5px}
.auto{text-overflow:ellipsis;white-space:nowrap;overflow:hidden;}
</style>
<link rel="stylesheet" href="<%=cssPath %>/page.css">
<link rel="stylesheet" href ="<%=cssPath %>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/Calendar.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
 <script type="text/javascript" src="<%=contextPath%>/core/js/cmp/Calendarfy.js"></script>
 <script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
 <script type="text/javascript" src="js/index3.js"></script>
 <%
 String id = (String)request.getParameter("seqId");
 int seqId = 0;
 if(id!=null){
   seqId = Integer.parseInt(id);
 }
 
 String sub = (String)request.getParameter("sub");//不为空为新建子元素
 String subBuild = "";
 if(sub != null && sub.length()>0){
   subBuild = sub;
 }
 
 String ftypes = request.getParameter("ftypes");
 %>
<script type="text/javascript">
function doInit(){
  var subs = "<%=subBuild%>"; 
  if(subs !=""){  
		$("element_type").update("<option value='1' selected>简单型</option>");
  }
  var ftypes = "<%=ftypes%>";
  var arrTypes = null;
  if(ftypes && ftypes !="null"){
    arrTypes = ftypes.split(",");
  } 
  if(arrTypes){
		for(var i=0; i<arrTypes.length; i++){
			if(arrTypes[i]){
         $("eleType"+arrTypes[i]).checked = true;
			}
		}
		var one = $("eleType1").checked;
		var two = $("eleType2").checked;
		var three = $("eleType3").checked;	
		if(!one){
		  $("eleType1").disabled = true;
		}
		if(!two){
		  $("eleType2").disabled = true;
		}
		if(!three){
		  $("eleType3").disabled = true;
		}
  }
}
  function save(){
    var boardNo = $("BOARD_NO").value;
    var cnName = $("cn_NAME").value;
    if(boardNo == "" || boardNo == "null"){
       alert("编号不能为空");
       return false;
     }
    if(cnName == "" || cnName == "null"){
       alert("中文名称不能为空");
       return false;
     }
    var type1 ="";
    var type2 ="";
    var type3 ="";
    var typetext = $("eleType1").checked;
    var typepic = $("eleType2").checked;
    var typesound = $("eleType3").checked;
    if(typetext){
       type1 = $("eleType1").value;
    }

    if(typepic){
       type2 = $("eleType1").value;
    }
    if(typesound){
       type3 = $("eleType1").value;
    }
  
    if(!type1 && !type2 && !type3){
	      alert("请选择文件类型");
	      return false;
      
    }else{
        $("form1").submit();
    }
  }

  function findNOs(){
    var nos = $("BOARD_NO").value;
    if(!isNumber(nos)){
			alert("请输入数字!");
			$("BOARD_NO").focus();
			return false;
    }else{
      if(parseInt(nos) <= 0){
				alert("编号应该大于0");
				$("BOARD_NO").focus();
				return false;
      }else if(parseInt(nos) >200){
        alert("编号应该小于200");
				$("BOARD_NO").focus();
				return false;
      }else{
        //用ajax判断编号是否存在
        var url = contextPath + "/yh/subsys/inforesouce/act/YHMateElementAct/isExitNos.act";
        var param  = "nos="+nos +"&seqId=";
        var json = getJsonRs(url, param);
        if(json.rtState == "0"){
          var state = json.rtData;
          if(state == '1'){
						alert("此编码已存在!");		
						$("BOARD_NO").focus();
						return false;
          }
        }
      }
    }
  }
</script>
</head>
<body onload="doInit();">
  <table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
  <tr>
    <td class="Big"><img width="22" height="20" src="<%=imgPath%>/edit.gif"><span class="big3"> 新建元数据</span><br>
    </td>
  </tr>
</table>
<form name="form1" id="form1" method="get" action="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateElementAct/addelement.act">
<table width="600" align="center" class="TableBlock">
     <tr>
      <td nowrap="" class="TableData">编号：<font style="color:red">*</font></td>
      <td class="TableData">
        <input type="text" value="" class="BigInput" maxlength="50" size="4" id="BOARD_NO" name="BOARD_NO" onblur="findNOs(); return false;">
        <span style="">输入数字(1--200)</span>
     </td>
    </tr>
    <tr>
      <td nowrap="" class="TableData">中文名称：<font style="color:red">*</font></td>
      <td class="TableData">
        <input type="text" value="" class="BigInput" maxlength="50" size="30" id="cn_NAME" name="cn_NAME">
      </td>
    </tr>
	<tr>
      <td nowrap="" class="TableData">英文名称：</td>
      <td class="TableData">
        <input type="text" value="" class="BigInput" maxlength="50" size="30" id="en_NAME" name="en_NAME">
      </td>
    </tr>

    <tr>
      <td valign="top" nowrap="" class="TableData">定义：</td>
      <td class="TableData">
      	<textarea cols="34" rows="2" id="define_TEXT" name="define_TEXT"></textarea>
      </td>
    </tr>
     <tr>
      <td valign="top" nowrap="" class="TableData">目的：</td>
      <td class="TableData">
      	<textarea cols="34" rows="2" id="aim_TEXT" name="aim_TEXT"></textarea>
      </td>
    </tr>

	  <tr>
      <td nowrap="" class="TableData">约束性：</td>
      <td class="TableData">
        <select class="BigSelect" id="constraint" name="constraint">
          <option value="1">必选</option>
          <option value="2">条件选</option>
          <option value="3">可选</option>
        </select>
      </td>
    </tr>
	<tr>
      <td nowrap="" class="TableData">可重复性</td>
      <td class="TableData">
        <select  class="BigSelect"  id="repeat" name="repeat">
          <option value="2">可重复</option>
          <option value="1">不可重复</option>
        </select>
      </td>
    </tr>
	  <tr>
      <td nowrap="" class="TableData">元素类型：</td>     
      <td class="TableData">
        <select class="BigSelect" id="element_type" name="element_type">
          <option value="1">简单型</option>
          <option value="2">容器型</option>
          <option value="3">复合型</option>
        </select>
      </td>    
    </tr>
	  <tr>
      <td nowrap="" class="TableData">数据类型：</td>
      <td class="TableData">
        <select class="BigSelect" id="typeId" name="typeId">
          <option value="1">数值型</option>
          <option value="2">字符型</option>

        </select>
      </td>
    </tr>
   <tr>
      <td nowrap="" class="TableData">文件类型：<font style="color:red">*</font></td>
      <td class="TableData">
        <input type="checkbox" id="eleType1" name="eleType" value='1' checked>文本类型 </input>      
        <input type="checkbox" id="eleType2" name="eleType" value='2'>图片类型 </input>       
        <input type="checkbox" id="eleType3" name="eleType" value='3'>视频类型</input>        
      </td>
    </tr>
    <tr align="center" class="TableControl">
      <td nowrap="" colspan="2">
        <input type="hidden" id="BOARD_ID" name="BOARD_ID" value="">
        <input type="button" name="check" onclick="save()" class="BigButton" value="保存">&nbsp;&nbsp;
        <input type="button" onclick="history.back();" name="back" class="BigButton" value="返回">
      </td>
    </tr>
    <input type="hidden" id="parentId"  name="pId" value="<%=seqId%>"></input><!-- 如果 seqId为0，则是新建，如果不是0，则是新建子元素-->
</table>
</form>
</body>
</html>
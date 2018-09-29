<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@ page  import="java.util.List"%>
<%@ page  import="java.lang.*"%>
<%@ page  import="yh.subsys.inforesouce.data.*"%>
<html>
<head>
<title>资源管理</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
 String id = (String)request.getAttribute("seqId");
 int seqId = 0;
 if(id!=null){
   seqId = Integer.parseInt(id);
 } 
 YHMateType type = (YHMateType)request.getAttribute("type");
 String parentId = type.getParentId();//寻找父id,如果不为空则文编辑子类，为空或0则修改父类
 int pid = 0;
 if(parentId==null || parentId =="" || "null".equals(parentId)){
   pid = 0;
 }else{
   pid = Integer.parseInt(parentId);
 }
 %>
<script type="text/javascript">
function CheckForm()
{ // alert("ddd");//getElementsByName
 // var no = document.form1.getElementsByName("BOARD_NO");
 var ss =  document.form1.BOARD_NO.value;
  alert(ss);
  if(isNaN(ss)){
    alert("编号必须是数字!!");
    return false;
    }
 // document.form1.submit();
  return true;
   }

function doinit(){
  var paId = <%=pid%>;
  var elemId = <%=type.getElementId()%>;
  var ftypes = "<%=type.getElement_type()%>";//接收的是文本类型，图片类型，或视频类型
  var selOption = "";
  var arrTypes = null;
  if(ftypes && ftypes !="null"){
    arrTypes = ftypes.split(",");
  } 
  if(paId == 0){ //修改父类
    //判断修改的类型
    if(elemId == '1'){//是简单类型
      selOption += "<option value='1'>简单型</option>";
      selOption += "<option value='2'>容器型</option>";
      selOption += "<option value='3'>符合型</option>";
    }else if(elemId == '2'){       
      selOption += "<option value='2'>容器型</option>";
      selOption += "<option value='3'>符合型</option>";
    }else if(elemId == '3'){
      selOption += "<option value='3'>符合型</option>";
    }
    if(arrTypes){
  		for(var i=0; i<arrTypes.length; i++){
  			if(arrTypes[i]){
           $("eleType"+arrTypes[i]).checked = true;//点击编辑时，显示编辑页面 有几个复选框被选中
  			}
  		}  		
    }
  }else{      //修改子类,判读父类的类型    if(arrTypes){
  		for(var i=0; i<arrTypes.length; i++){
  			if(arrTypes[i]){
           $("eleType"+arrTypes[i]).checked = true;
  			}
  		}
  		var one = $("eleType1").checked;
  		var two = $("eleType2").checked;
  		var three = $("eleType3").checked;	
  		if(!one){// 判断父元素下的子元素  如果没有选中 就不可以选中了（因为父元素已是文本类型了，子元素必须是文本类型）
  		  $("eleType1").disabled = true;
  		}
  		if(!two){
  		  $("eleType2").disabled = true;
  		}
  		if(!three){
  		  $("eleType3").disabled = true;
  		}
    }
    selOption += "<option value='1'>简单型</option>";
  } 
  $("element_type").update(selOption);
  $("constraint").value="<%=type.getConstraint()%>";
  $("repeat").value = "<%=type.getRepeat()%>";
  $("element_type").value="<%=type.getElementId()%>";
  $("typeId").value = "<%=type.getTypeId()%>"; 
}

function save(){
  var type1 ="";
  var type2 ="";
  var type3 ="";
  var typetext = $("eleType1").checked;
  var typepic = $("eleType2").checked;
  var typesound = $("eleType3").checked;
  if(!$("BOARD_NO").value){
    alert("编号不能为空！");
    $("BOARD_NO").focus();
    return false;
  }
  if(!$("cn_NAME").value){
    alert("中文名不能为空！");
    $("cn_NAME").focus();
    return false;
  }
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
      var param  = "nos="+nos + "&seqId=" + <%=seqId%>;
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
<body onload="doinit()">
  <table width="100%" cellspacing="0" cellpadding="3" border="0" class="small">
  <tr>
    <td class="Big"><img width="22" height="20" src="<%=imgPath%>/edit.gif"><span class="big3"> 编辑元数据</span><br>
    </td>
  </tr>
</table>
<!-- onsubmit="return CheckForm();" -->
<form name="form1" id="form1" method="get" action="<%=contextPath%>/yh/subsys/inforesouce/act/YHMateTypeAct/updateMate.act">
<table width="600" align="center" class="TableBlock">
     <tr>
      <td nowrap="" class="TableData">编号：*</td>
      <td class="TableData">
        <input type="text" onfocusout="findNOs();" class="BigInput" maxlength="50" size="4" id="BOARD_NO" name="BOARD_NO" value="<%=type.getNumberId().replace("MEX", "").replace("M","")%>">
     		<span style="">输入数字(1--200)</span>
     </td>
    </tr>
    <tr>
      <td nowrap="" class="TableData">中文名称：*</td>
      <td class="TableData">
        <input type="text" class="BigInput" maxlength="50" size="30" id="cn_NAME" name="cn_NAME" value="<%=type.getcNname()%>">
      </td>
    </tr>
	<tr>
      <td nowrap="" class="TableData">英文名称：</td>
      <td class="TableData">
        <input type="text"  class="BigInput" maxlength="50" size="30" id="en_NAME" name="en_NAME" value="<%=type.geteNname()%>">
      </td>
    </tr>

    <tr>
      <td valign="top" nowrap="" class="TableData">定义：</td>
      <td class="TableData">
      	<textarea cols="34" rows="2" id="define_TEXT" name="define_TEXT" ><%=type.getDefine()%></textarea>
      </td>
    </tr>
     <tr>
      <td valign="top" nowrap="" class="TableData">目的：</td>
      <td class="TableData">
      	<textarea cols="34" rows="2" id="aim_TEXT" name="aim_TEXT" ><%=type.getAim()%></textarea>
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
          <!--<option value="1">简单型</option>
          <option value="2">容器型</option>
        	<option value="3" >复合型</option>        
        --></select>
      </td>
    </tr>
	  <tr>
      <td nowrap="" class="TableData">数据类型：</td>
      <td class="TableData">
        <select class="BigSelect" id="typeId" name="typeId">        
          <option value="0">--</option>
          <option value="1">数值型</option>
          <option value="2">字符型</option>
          <option value="3">日期类型</option>             
        </select>
      </td>
    </tr> 
    <tr>
      <td nowrap="" class="TableData">文件类型：*</td>
      <td class="TableData">
        <input type="checkbox" id="eleType1" name="eleType" value='1' >文本类型 </input>      
        <input type="checkbox" id="eleType2" name="eleType" value='2'>图片类型 </input>       
        <input type="checkbox" id="eleType3" name="eleType" value='3'>视频类型</input>        
      </td>
    </tr>  
    <tr align="center" class="TableControl">
      <td nowrap="" colspan="2">
        <input type="hidden" id="BOARD_ID" name="BOARD_ID" value="">
        <input type="button" onclick="javascript:save();"  class="BigButton" value="保存">&nbsp;&nbsp;
        <input type="button" onclick="history.back();" name="back" class="BigButton" value="返回">
      </td>
    </tr>
    <input type="hidden" id="seqId"  name="seqId" value="<%=seqId%>"></input>
</table>
</form>
</body>
</html>
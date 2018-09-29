<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>礼品入库管理</title>
<link rel="stylesheet"	href="<%=cssPath%>/style.css">
<link rel="stylesheet" href = "<%=cssPath %>/style.css">
<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/cmp/ExchangeSelect.css" type="text/css" />
<script type="text/Javascript"	src="<%=contextPath%>/core/js/datastructs.js"></script>
<script type="text/Javascript"  src="<%=contextPath%>/core/js/sys.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/prototype.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/smartclient.js"></script>
<script type="text/Javascript"	src="<%=contextPath%>/core/js/cmp/select.js"></script>
<script type="text/javascript">
//var parent_window =window.opener;
function setvalue(optext,opvalue,kname)
{
 
     option_text=optext;
     option_value=opvalue;
     var my_option =parent_window.document.createElement("OPTION");
     my_option.text=option_text;
     my_option.value=option_value;
     //parent_window.form1.SOURCE.add(my_option,0);
     parent_window.document.all(kname).add(my_option,0);
     //parent_window.form1.SOURCE.selectedIndex=0;
      parent_window.document.all(kname).selectedIndex=0;
     //parent_window.form1.SOURCE.selected;
      parent_window.document.all(kname).selected;
     //parent.close();
}
 
function CheckForm(){
  if(document.form1.codeNo.value.trim()==""){ 
    alert("编号不能为空！");
    document.form1.codeNo.focus();
    document.form1.codeNo.select();
    return (false);
   }
  if(document.form1.codeName.value.trim()==""){ 
    alert("名称不能为空！");
    document.form1.codeName.focus();
    document.form1.codeName.select();
    return (false);
  }
  return true;
}
function Init(){
  if(CheckForm()){
    var requestUrl = "<%=contextPath%>/yh/subsys/oa/giftProduct/instock/act/YHGiftInstockAct/addCodeItem.act?GIFT_PROTYPE=GIFT_PROTYPE";
    var rtJson = getJsonRs(requestUrl,mergeQueryString($("form1")));
    if(rtJson.rtState == "1"){
      alert(rtJson.rtMsrg); 
      return ;
    }
    var prc = rtJson.rtData;
    if(prc.addType==2){
     alert("代码名称"+ document.form1.codeName.value+"已存在！");
     return;
    }else{
      parent.file_tree.location.reload();
      document.form1.codeNo.value='';
      document.form1.codeName.value='';
        
      $("returnDiv").style.display = '';
    } 
  }
}
</script>
</head>
 
<body class="" topmargin="5" onLoad="">
<table border="0" width="100%" cellspacing="0" cellpadding="3" class="small">
  <tr>
    <td class="Big"><img src="<%=contextPath %>/core/funcs/system/filefolder/images/edit.gif" WIDTH="22" HEIGHT="20" align="absmiddle"><span class="big3">&nbsp;&nbsp;礼品类别定义<font color='red'>(礼品类别的删除在系统自定义代码中)</font></span>
    </td>
  </tr>
</table>
 
<br>
  <form action="#"  method="post" id="form1" name="form1" onSubmit="return CheckForm();">
  
<table width="450" class="TableBlock" align="center" >
 <tr>
    <td nowrap class="TableContent" width="120">编号：</td>
    <td nowrap class="TableData">
        <input type="text" name="codeNo" class="BigInput" size="20" maxlength="40" value="">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">排序号：</td>
    <td nowrap class="TableData">
        <input type="text" name="codeOrder" class="BigInput" size="20" maxlength="40" value="">&nbsp;
    </td>
   </tr>
   <tr>
    <td nowrap class="TableContent">名称：</td>
    <td nowrap class="TableData">
        <input type="text" name="codeName" class="BigInput" size="20" maxlength="100" value="">&nbsp;
    </td>
   </tr>
   <tfoot align="center" class="TableFooter">
    <td colspan="2" align="center">
        <input type="hidden" name="CODE_ID" value="GIFT_PROTYPE">
        <input type="hidden" name="K_Name" value="GIFT_PROTYPE">
        <input type="button" value="确定" class="BigButton" onclick="Init();">&nbsp;&nbsp;
        <input type="reset" value="重填" class="BigButton">
    </td>
   </tfoot>

</table>
  </form>
  <div align="center" id="returnDiv" style="display:none">
 <table class="MessageBox" align="center" width="290" >
  <tr>
    <td class="msg info">
      <div class="content" style="font-size:12pt">增加成功！</div>
    </td>
  </tr>
</table>
 </div>
</body>
</html>

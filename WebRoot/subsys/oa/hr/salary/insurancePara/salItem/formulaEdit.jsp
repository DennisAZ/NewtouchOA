	<%@ page language="java" contentType="text/html; charset=UTF-8"
	    pageEncoding="UTF-8"%>
	<%@ include file="/core/inc/header.jsp" %>
	<%@ page  import="yh.subsys.oa.hr.salary.insurancePara.salItem.data.*"%> 
  <%@ page  import="java.util.List"%>
	<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
	<html>
	<head>
	<%
	YHSalItem SalItemList = (YHSalItem)request.getAttribute("findSlaItemList");
	//System.out.println(SalItemList.getSeqId());
	%>
	
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="<%=cssPath %>/cmp/tab.css" type="text/css" />
	<link rel="stylesheet" href = "<%=cssPath%>/style.css">
	<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
	<script type="text/javascript" src="<%=contextPath %>/core/funcs/system/syslog/js/sysyearlog.js"></script>
	<title>薪酬项目定义</title>
	<script type="text/javascript">
	var parent_window =window.opener;
	//存储光标的位置
	function storeCaret (textEl)
	{
	    if (textEl.createTextRange)
		    //textEl.caretPos 向文本框当前光标位置插入内容
	        textEl.caretPos = document.selection.createRange().duplicate();//获取选中内容
	}
	//将LIST内容插入到光标指定位置
	function insertAtCaret (textEl, text)
	{
	    if (textEl.createTextRange && textEl.caretPos)
	    {
	        var caretPos = textEl.caretPos;
	        caretPos.text = text;//caretPos.text.charAt(caretPos.text.length - 1) == ' ' ? text + ' ' : text;
	    }
	    else
	    {
	        textEl.value += text;
	    }
	    textEl.focus();
	}
	//公式校验
	function funCheck ()
	{
	    if (document.formMain.textFormula.value == "")
	    {
	        alert ("请输入公式内容！");
	        document.formMain.textFormula.focus ();
	        return;
	    }
	    reVal = /([^<\(\+\-\*\/]|[\+\-\*\/]{2,})(\[|\(|<)/;
	    if (reVal.test(document.formMain.textFormula.value))
	    {
	        //alert (RegExp.$1 +"1\n"+RegExp.$2);
	        alert ("公式不正确，请检查括号及+-*/是否匹配！");
	        return;
	    }
	    reVal = /(\]|\)|>)([^>\)\+\-\*\/]|[\+\-\*\/]{2,})/;
	    if (reVal.test(document.formMain.textFormula.value))
	    {
	        //alert (RegExp.$1 +"2\n"+RegExp.$2);
	        alert ("公式不正确，请检查括号及+-*/是否匹配！");
	        return;
	    }
	    //reVal = /(([^\(\+\-\*\/]|[\+\-\*\/]{2,})(\[|\())|((\]|\))([^\)\+\-\*\/]|[\+\-\*\/]{2,}))/;
	    //if (reVal.test(document.formMain.textFormula.value))
	    //{
	    //    alert ("公式不正确，请检查括号及+-*/是否匹配！");
	    //    return;
	    //}

	}
	//公式保存
	function SaveInfo ()
	{
	    if (document.formMain.textFormula.value == "")
	    {
	        alert ("请输入公式内容！");
	        document.formMain.textFormula.focus ();
	        return;
	    }
	    reVal = /([^<\(\+\-\*\/]|[\+\-\*\/]{2,})(\[|\(|<)/;
	    if (reVal.test(document.formMain.textFormula.value))
	    {
	        alert ("公式不正确，请检查括号及+-*/是否匹配！");
	        return;
	    }
	    reVal = /(\]|\)|>)([^>\)\+\-\*\/]|[\+\-\*\/]{2,})/;
	    if (reVal.test(document.formMain.textFormula.value))
	    {
	        alert ("公式不正确，请检查括号及+-*/是否匹配！");
	        return;
	    }
	   var s1=document.formMain.textFormula.value;
		 var re;
	   re=/\[([^$,\]])*/gi;
	   var r=s1.replace(re, "[");
	   //alert(r);
	   parent_window.form1.FORMULA.value=r;
	   parent_window.form1.FORMULANAME.value=document.formMain.textFormula.value;
	   parent.close();
	}


	function UpdateInfo(ITEM_ID)
	{
	    if (document.formMain.textFormula.value == "")
	    {
	        alert ("请输入公式内容！");
	        document.formMain.textFormula.focus ();
	        return;
	    }
	    reVal = /([^<\(\+\-\*\/]|[\+\-\*\/]{2,})(\[|\(|<)/;
	    if (reVal.test(document.formMain.textFormula.value))
	    {
	        alert ("公式不正确，请检查括号及+-*/是否匹配！");
	        return;
	    }
	    reVal = /(\]|\)|>)([^>\)\+\-\*\/]|[\+\-\*\/]{2,})/;
	    if (reVal.test(document.formMain.textFormula.value))
	    {
	        alert ("公式不正确，请检查括号及+-*/是否匹配！");
	        return;
	    }
	   var s1=document.formMain.textFormula.value;
		 var re;
	   re=/\[([^$,\]])*/gi;
	   var r=s1.replace(re, "[");
	   
	   document.formMain.FormulaID.value=r;
	   $("formMain").submit();
	   parent.close();
	}
  function doInit(){
	  $("selectList").innerHTML="";
		 var selectList = $('selectList');
		 var options =""; 
		 var url = contextPath + '/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/getSalItemNameAct.act';
		 var rtJson = getJsonRs(url);
		 if(rtJson.rtState =="0"){
		   var rtData = rtJson.rtData;
		   var listData = rtData.listData;
		   for(var i =0; i<listData.length; i++){
			   selectList.options.add(new Option(listData[i].itemName, listData[i].slaitemId));
			  }
		 }else{
			 alert(rtJson.rtMsrg); 
		}
    notInputEnglish();
	}
  function notInputEnglish(){
   $('textFormula').onkeyup=function(){
	  var data =  $('textFormula').value;
    if(isNaN(data)){
    	$('textFormula').value = ''
        	return;
    }
    /*if(data!=null && data.length>0){
     var str = data.substring(data.length-1,data.length);
  	    if(isNaN(str)){
  	    	$('textFormula').value =data.substring(0,data.length-1);
          return;
        } 
     }*/
    return;
	 }
 }

	</script>
	</head>
	<body  class="panel" onload="doInit();">
<form  method="post" action="<%=contextPath%>/yh/subsys/oa/hr/salary/insurancePara/salItem/act/YHSalaryItemAct/upSaveSalaryItemAct.act" name="formMain" id ="formMain">
<%if(SalItemList!=null){ %>
<input name="FormulaID" id="FormulaID" type="hidden" value="<%=SalItemList.getFormula()==null?"":SalItemList.getFormula() %>">
<input name="slaitemId" id="slaitemId" type="hidden" value="<%=SalItemList.getSlaitemId()==0?"":SalItemList.getSlaitemId()%>">
<input name="seqId" id="seqId" type="hidden" value="<%=SalItemList.getSeqId()==0?"":SalItemList.getSeqId() %>">
<%} %>
  <table class="small" align="center" border="0" cellpadding="0" cellspacing="0">
   <tbody>
    <tr>
      <td colspan="5">
        <table border="0" cellpadding="0" cellspacing="0" width="500">
	        <tbody><tr class="TableHeader">
	            <td align="left">
	            	 &nbsp; &nbsp;
	               <script language="javascript">
	               	if(parent_window.form1.ITEM_NAME.value!="")
	               	document.write(parent_window.form1.ITEM_NAME.value+"=")
	               </script>
	            </td>
	          </tr>
	          <tr class="TableData">
	          <td align="left">
	       <%if(SalItemList!=null){ %>
	         <textarea name="textFormula" id="textFormula" cols="80" rows="5" class="formclass" onselect="storeCaret(this);" onclick="storeCaret(this);" onkeyup="storeCaret(this);"><%=SalItemList.getFormulaname()==null?"":SalItemList.getFormulaname()%></textarea>
	       <%}else{ %>
	          <textarea name="textFormula" id="textFormula" cols="80" rows="5" class="formclass" onselect="storeCaret(this);" onclick="storeCaret(this);" onkeyup="storeCaret(this);"></textarea>
	         <%} %>
	         </td>
	         </tr>
	        </tbody>
        </table>
      </td>
    </tr>
    <tr>
      <td colspan="5" width="53%">
      	<br><b>可选字段</b></td>
    </tr>
    <tr>
      <td width="12%">
        <select name="selectList" id="selectList" size="10" onchange="insertAtCaret(document.formMain.textFormula, '[' + document.formMain.selectList.item(document.formMain.selectList.selectedIndex).text+'$'+document.formMain.selectList.item(document.formMain.selectList.selectedIndex).value + ']');">
            <!--    <option value="1">职务工资</option>
               <option value="2">奖金</option>
               <option value="3">津贴</option>
               <option value="4">劳务费</option>
               <option value="5">房贴</option>
               <option value="6">哈哈哈</option>
                -->
          </select>
       </td>
      <td width="16%">
		    <table width="159">
		       <tbody>
	           <tr valign="middle">
	            <td width="35">
	              <input name="btnAdd" value=" 1 " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '1');" type="button">
	            </td>
	            <td width="35">
	              <input name="btnDec" value=" 2 " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '2');" type="button">
	            </td>
	            <td width="35">
	              <input name="btnMul" value=" 3 " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '3');" type="button">
	            </td>
	            <td width="36">
	              <input name="btnAdd" value=" + " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '+');" type="button">
	            </td>
	          </tr>
            <tr valign="middle">
	            <td width="35">
	              <input name="btnAdd" value=" 4 " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '4');" type="button">
	            </td>
	            <td width="35">
	              <input name="btnDec" value=" 5 " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '5');" type="button">
	            </td>
	            <td width="35">
	              <input name="btnMul" value=" 6 " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '6');" type="button">
	            </td>
	            <td width="36">
	              <input name="btnDec" value=" - " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '-');" type="button">
	            </td>
           </tr>
           <tr valign="middle">
            <td width="35">
              <input name="btnAdd" value=" 7 " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '7');" type="button">
            </td>
            <td width="35">
              <input name="btnDec" value=" 8 " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '8');" type="button">
            </td>
            <td width="35">
              <input name="btnMul" value=" 9 " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '9');" type="button">
            </td>
            <td width="36">
              <input name="btnMul" value=" * " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '*');" type="button">
            </td>
          </tr>
          <tr valign="middle">
          	<td width="35">
              <input name="btnDiv" value=" 0 " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '0');" type="button">
            </td>
            <td width="35">
              <input name="btnDiv" value=" . " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '.');" type="button">
            </td>
            <td width="35">
              <input name="btnLeft" value=" = " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '=');" type="button">
            </td>
            <td width="36">
              <input name="btnDiv" value=" / " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '/');" type="button">
            </td>
          </tr>
          <tr valign="middle">
	           <td width="35">
	             <input name="btnLeft" value=" &lt; " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '<');" type="button">
	           </td>
	           <td width="35">
	             <input name="btnRight" value=" &gt; " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '>');" type="button">
	           </td>
	           <td width="35">
	             <input name="btnLeft" value=" ( " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, '(');" type="button">
	           </td>
	           <td width="36">
	             <input name="btnRight" value=" ) " class="formclass_show4" onclick="insertAtCaret(document.formMain.textFormula, ')');" type="button">
	           </td>
           </tr>
		       <tr>
	            <td colspan="2">
	              <input name="btnCheck" value=" 校验 " onclick="funCheck ()" class="BigButton" type="button">
	            </td>
				      <td colspan="2">
	              &nbsp;<input name="btnClean" value=" 清空 " onclick='javascript:document.formMain.textFormula.value="";' class="BigButton" type="button">
	            </td>
				   </tr>
				  <tr>
           <td colspan="2">
           <%if(SalItemList!=null && SalItemList.getSlaitemId()!=0){ %>
           <input type="button" value="保存" class="BigButton"  name="button" onClick="UpdateInfo(<%=SalItemList.getSlaitemId()%>);">
             
           <%}else{ %>
                <input name="btnCheck" value="确定 " onclick="SaveInfo()" class="BigButton" type="button">
           <%} %>
           </td>
					 <td colspan="2">
					 </td>
		     </tr>
		        </tbody>
        </table>
      </td>
      <td width="1%">　</td>
			<td width="24%">
		<div align="center" id="returnDiv" style="">
	     <table class="MessageBox" align="center" width="430" >
	       <tr>
	        <td class="msg info">
	                <div class="content" style="font-size:12pt">
	                  <span>
		                                                个人所得税的输入为&lt;参与所得税计算的薪酬项目&gt;-个税起征
						                           额.例如'&lt;[应发薪酬]&gt;-800'(表示的是[应发薪酬]参与所得税
						                           计算)或是&lt;[标准薪酬]+[奖金]&gt;-800(表示的是[标准薪酬]与
							        [奖金]之和参与所得税计算
						       </span>  
						     </div>
	          </td>
	       </tr>
	    </table>
   </div>
</td>
</tr>
</tbody>
</table>
</form>
</body>
	</html>
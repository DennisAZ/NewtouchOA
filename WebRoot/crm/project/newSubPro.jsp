<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>    
    <title>新建子项目</title>
    <link rel="shortcut icon" href="favicon.ico"/>    
 	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="cache-control" content="no-cache"/>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <link rel="stylesheet" type="text/css" href="css/style.css"/>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/pro.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/formCheck.js"></script>
	<script type="text/javascript" src="My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript"> 
		function check(){
			var pStaDate=$("pStaDate").value;
		    var pEndDate=$("pEndDate").value;
		    //var descCreDate=parent.document.getElementById("descCreDate").value;
		   // var descFinDate=parent.document.getElementById("descFinDate").value;
	  		var errStr = "";
			if(isEmpty("staTitle")){   
				errStr+="- 未填写子项目名称！\n"; 
			}
			else if(checkLength("staTitle",300)){
				errStr+="- 子项目名称不能超过300个字！\n";
			}
			if(isEmpty("pStaDate")){   
				errStr+="- 未填写开始日期！\n"; 
			}
			/*else if(descCreDate!=""&&pStaDate<descCreDate){
				errStr+="- 子项目开始日期不能小于立项日期！\n"; 
			}*/
			if(isEmpty("pEndDate")){   
				errStr+="- 未填写结束日期！\n"; 
			}
 			/*else if(descFinDate!=""&&pEndDate>descFinDate){
				errStr+="- 子项目结束日期不能超过项目完成日期！\n"; 
			}*/
			if(errStr!=""){
				errStr+="\n请返回修改...";
				alert(errStr);
				return false;
			}
			else{
				waitSubmit("dosend","保存中...");
				waitSubmit("doCancel");
		    	return $("create").submit();	
			}		  
		}
		
		window.onload=function(){
		  	$("proId").value=parent.document.getElementById("proId").value;
		}
  </script> 
</head>
  <body>
  <div class="inputDiv">
    <form action="projectAction.do" method="post"  id="create">
        <input type="hidden" name="op" value="saveSubPro">
        <input type="hidden" name="proId" id="proId" value="">
        <table class="dashTab inputForm single" cellpadding="0" cellspacing="0">
        	<thead>
            	<tr>
                    <th class="descTitleL">子项目名称：<span class='red'>*</span></th>
                    <th class="descTitleR"><input name="proStage.staTitle" id="staTitle" type="text" class="inputSize2" onBlur="autoShort(this,300)"/></th>
                </tr>
            </thead>
            <tbody>
            	<tr>
                    <th>目标：</th>
                    <td><input name="proStage.staAim" id="staAim" type="text" class="inputSize2" onBlur="autoShort(this,300)"></td>				
                </tr>
                
                <tr>
                    <th class="required">开始日期：<span class='red'>*</span></th>
                    <td><input name="pStaDate"  type="text"  readonly="readonly"  class="Wdate inputSize2" style="cursor:hand" id="pStaDate" onFocus="WdatePicker({onpicked:function(){$('pEndDate').focus();},maxDate:'#F{$dp.$D(\'pEndDate\')}'})" ondblClick="clearInput(this)"/></td>
                </tr>
                <tr>
                    <th class="required">结束日期：<span class='red'>*</span></th>
                    <td><input name="pEndDate"  type="text"  readonly="readonly" ondblClick="clearInput(this)"  class="Wdate inputSize2" style="cursor:hand" id="pEndDate" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pStaDate\')}'})"/></td>
                </tr>
            
                <tr class="noBorderBot">
                    <th>备注：</th>
                    <td><textarea class="inputSize2" rows="1" name="proStage.staRemark" onBlur="autoShort(this,4000)"></textarea></td>
                </tr>
                <tr class="submitTr">
                    <td colspan="2">
                    <input type="button" class="butSize1" id="dosend" value="保存" onClick="check()">
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                    <input id="doCancel" type="button" class="butSize1" value="取消" onClick="cancel()"></td>
                </tr>
            </tbody>
            			
        </table>
    </form>
  </div>
  
  </body>
</html>

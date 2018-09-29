<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath%>/views.css"
	type="text/css" />
<link rel="stylesheet" href="<%=cssPath%>/style.css"
	type="text/css" />
<link rel="stylesheet" type="text/css" href="/yh/rad/dsdef/css/tableList.css" />
<script type="text/javascript" src="<%=contextPath %>/rad/dsdef/js/gridtable.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/Javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/rad/grid/grid.js"></script>
<link rel="stylesheet" type="text/css" href="<%=contextPath %>/rad/grid/grid.css" />
<!--<link rel="stylesheet" type="text/css" href="/yh/rad/dsdef/css/tableList.css" />-->
<title>列表</title>
</head>
<script type="text/javascript">
  var hd =[
	       [
		    {header:"表编码",name:"tableNo",hidden:false, width:80},
		    {header:"表名称",name:"tableName",hidden:false, width:100},
		    {header:"类名称",name:"className",hidden:false, width:100},
		    {header:"表描述",name:"tableDesc",hidden:false, width:200},
		    {header:"seqId",name:"seqId",hidden:true},
		    {header:"表类型",name:"categoryNo",hidden:false, width:60}
		   ],
		    {
	   	   	 header:"操作",
	   	   	 oprates:[
		    		  new YHOprate('修改',true,show1),
	   		          new YHOprate('删除',true,deleteTable),
	   		         new YHOprate('生成物理结构',true,toPhyicsAction)
	          	     ], width: 150
	         }
	       ];
	//var url = "/yh/raw/cy/act/YHGridNomalAct/jsonTest.act?tabNo=99999";
	var url = "/yh/yh/rad/grid/act/YHGridNomalAct/jsonTest.act?tabNo=99999&orderBy=order by TABLE_NO DESC";
	var grid = new YHGrid(hd,url,null,8);
function loads(){
  grid.rendTo('divieeeeee');
}
function toPhyicsAction(record, index) {
  var tableNo = record.getField('tableNo').value;
  toPhyics(tableNo);
}
function doSbumit() {
  var div = document.getElementById("tableForm");
  var divieeeeee =  document.getElementById("divieeeeee");
  var rtJson = getJsonRs(contextPath + "/yh/rad/dsdef/act/YHDsDefAct/updateDsDef.act", mergeQueryString($("dataList")));
  if (rtJson.rtState == 0) {
    alert(rtJson.rtMsrg);
	grid.reShow(url);
	div.style.display = "none";
	divieeeeee.style.display = "";
  }else {
	alert(rtJson.rtMsrg);
  }
}
function checkTabIsExist(){
  var tableName = $("tableName").value;
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/tabIsExist.act?tableName="+tableName;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == 0){
    if(rtJson.rtData == "1"){
      return true;
    }else{
      return false;
    }
  }else{
    return true;
  }
}
function dropTab(){
  var tableName = $("tableName").value;
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/dropTab.act?tableName="+tableName;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == 0){
    alert(rtJson.rtMsrg);
  }else{
    alert(rtJson.rtMsrg);
  }
}
function toPhyics(tableNo){
  if(checkTabIsExist()){
    if(window.confirm("此表的物理结构已经存在,是否先删除此表结构!")){
      dropTab();
    }else{
      return;
    }
  }
  if(!window.confirm("确认生成物理结构!")){
    return;
  }
  //var tableNo = $("tableNo").value;
  var url = contextPath + "/yh/rad/dsdef/act/YHDsDef2DBAct/toPhysicsDb.act?tableNo="+tableNo;
  var rtJson = getJsonRs(url);
  if(rtJson.rtState == 0){
    alert(rtJson.rtMsrg);
  }else{
    alert(rtJson.rtMsrg);
  }
}
</script>
<body onload="loads()">
  <div style="width:1px;height:10px;"></div>
 <div id="divieeeeee" style="width: 900px; height: 400px;"></div>
 <div id="tableForm" style="display: none;" name="tableForm">
 <form  name="dataList" id="dataList"> 
  <input type=hidden name="id" id="count" value=0> 
  <input type=hidden name="YHDsTable" value="yh.core.data.YHDsTable"> 
  <input type=hidden name="YHDsField" value="yh.core.data.YHDsField">
  <input type=hidden name="tableNo1" value="">
  <div  class=tableDiv>
   <table id="table" >
    <thead>
	 <tr>
		<td>表编码</td>
		<td><input type=text id=tableNo name=tableNo size=13  onkeyup  ="onChange(this.value)" class = "SmallInput" maxlength = "5"><font style='color:red'>*</font></td>
		<td>表名称</td>
		<td><input type=text id=tableName name=tableName size=13 class = "SmallInput" onchange = "onChTableName(this.value)"><font style='color:red'>*</font></td>
		<td>类名称</td>
		<td><input type=text id=className name=className size=13 class = "SmallInput"><font style='color:red'>*</font></td>
		<td>表描述</td>
		<td><input type=text id=tableDesc name=tableDesc size=13 class = "SmallInput"><font style='color:red'>*</font></td>
		<td><input type=hidden id=seqId name=seqId size=13></td>
		<td>表类型</td>
		<td><select id="categoryNo" name="categoryNo">
			<option value="1">代码表</option>
			<option value="2">小编码表</option>
			<option value="3">参数表</option>
			<option value="4">数据主表</option>
			<option value="5">数据从表</option>
			<option value="6">多对多关系表</option>
		</select></td>
		<td><input type=button name=submit class="SmallButtonW " onclick="doSbumit();" value="提交"/></td>
    
	 </tr>
	 </thead>
	 <tbody id="tbodytable">
	</tbody>
  </table>
  </div>
  <input type=button class="SmallButtonW" value="添加字段 " onclick="addField()">
 <div id="inputDiv" style="display: none;" name="inputDiv">
 <input type="text" name="tableNoDiv" id="tableNoDiv">
 </div>
 <table id="tableF" class="">
	<thead>
		<tr>
			<td>字段编码</td>
			<td>字段名称</td>
			<td>字段描述</td>
			<td>按钮1</td>
			<td>按钮2</td>
		</tr>
	</thead>
  <tbody id="tbody">
  </tbody>
 </table>
 <div id="table_div" style="width:200;height:200;display: none; position: absolute; padding-top:50px" name="tableN_div"></div>
 <br>
 <div id="table_div2" style="display: none; position: absolute;" name="tableN_div">
 <input type="hidden" id="divId" value="" />
 <table id="tableDiv" class="TableLine1 ">
	<thead>
		<tr>
			<td>字段编码</td>
			<td><input type=text id="fieldNo" name="fieldNo" value="" maxlength = "8" class = "SmallInput"></td>
			<td>字段名称</td>
			<td><input type=text id="fieldName" name="fieldName" value="" class = "SmallInput" onchange ="onFieldTableName(this.value)"></td>
		</tr>
		<tr>
			<td>字段描述</td>
			<td><input type=text id="fieldDesc" name="fieldDesc" value="" class = "SmallInput"></td>
			<td>属性名称</td>
			<td><input type=text id="propName" name="propName" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>参照表</td>
			<td><input type=text id="fkTableNo" name="fkTableNo" value="" class = "SmallInput"></td>
			<td>参照表2</td>
			<td><input type=text id="fkTableNo2" name="fkTableNo2" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>编码字段</td>
			<td><input type=text id="fkRelaFieldNo" name="fkRelaFieldNo"value="" class = "SmallInput"></td>
			<td>名称字段</td>
			<td><input type=text id="fkNameFieldNo" name="fkNameFieldNo"value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>筛选条件</td>
			<td><input type=text id="fkFilter" name="fkFilter" value="" class = "SmallInput"></td>
			<td>编码类别</td>
			<td><input type=text id="codeClass" name="codeClass" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>缺省值</td>
			<td><input type=text id="defaultValue" name="defaultValue" value="" class = "SmallInput"></td>
			<td>显示方式</td>
			<td><select id="formatMode">
				<option value="number">数字</option>
				<option value="text">文本</option>
				<option value="data">日期</option>
				<option value="amt">金额</option>
			    </select>
			</td>
		</tr>
		<tr>
			<td>格式规则</td>
			<td><input type=text id="formatRule" name="formatRule" value="" class = "SmallInput"></td>
			<td>错误消息</td>
			<td><input type=text id="errorMsrg" name="errorMsrg" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>数值长度</td>
			<td><input type=text id="fieldPrecision" name="fieldPrecision" value="" class = "SmallInput"></td>
			<td>小数位数</td>
			<td><input type=text id="fieldScale" name="fieldScale" value="" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>数据类型</td>
			<td><select id="dataType" name="dataType">
				<option value="-7">BIT</option>
				<option value="-6">TINYINT</option>
				<option value="5">SMALLINT</option>
				<option value="4">INTEGER</option>
				<option value="-5">BIGINT</option>
				<option value="6">FLOAT</option>
				<option value="7">REAL</option>
				<option value="8">DOUBLE</option>
				<option value="2">NUMERIC</option>
				<option value="3">DECIMAL</option>
				<option value="1">CHAR</option>
				<option value="12">VARCHAR</option>
				<option value="-1">LONGVARCHAR</option>
				<option value="91">DATE</option>
				<option value="92">TIME</option>
				<option value="93">TIMESTAMP</option>
				<option value="-2">BINARY</option>
				<option value="-3">VARBINARY</option>
				<option value="-4">LONGVARBINARY</option>
				<option value="0">NULL</option>
				<option value="1111">OTHER</option>
				<option value="2000">JAVA_OBJECT</option>
				<option value="2001">DISTINCT</option>
				<option value="2002">STRUCT</option>
				<option value="2003">ARRAY</option>
				<option value="2004">BLOB</option>
				<option value="2005">CLOB</option>
				<option value="2006">REF</option>
				<option value="70">DATALINK</option>
				<option value="16">BOOLEAN</option>
			</select>
			<td>主键</td>
			<td><select id="isPrimaryKey" name=isPrimaryKey>
				<option value="1">是</option>
				<option value="0">否</option>
			    </select>
			</td>
		</tr>
		<tr>
			<td>自增</td>
			<td><select id="isIdentity" name="isIdentity">
				<option value="1">是</option>
				<option value="0">否</option>
			    </select>
			</td>
			<td>显示长度</td>
			<td><input type="text" id="displayLen" name="displayLen" class = "SmallInput"></td>
		</tr>
		<tr>
			<td>必填</td>
			<td><select id="isMustFill" name="isMustFill">
				<option value="1">是</option>
				<option value="0">否</option>
			    </select>
		   </td>
			<td><input type="button" name="saveDiv" value="保存" onclick="save()" class="SmallButton "></td>
			<td><input type="button" name="close" value="关闭" onclick="closeDiv()" class="SmallButton "></td>
		</tr>
	</thead>
	<tbody id="tbodyDiv">
	</tbody>
</table>
</div>
</form>
</div>
</body>
</html>

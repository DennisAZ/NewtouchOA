//定义一个field类型 并且定义get/set方法
function YHField(key,value){
	this.value = value;
	this.key = key;
}
YHField.prototype.getKey = _getKey;
YHField.prototype.getValue = _getValue;
function _getKey(){
	return this.key;
}
function _getValue(){
	return this.value;
}

//定义一个record类用来存储多个field
//一个record对应多个field
function YHRecord(){
	var _fields = new Array();
	this.fields = _fields ;
}
YHRecord.prototype.addField = _addField;
YHRecord.prototype.getField = _getField;
function _addField(field){
	this.fields.push(field);
	return this;
}
function _getField(key){
	var fields = this.fields ;
	for(var i = 0; i < fields.length; i ++){
		if(fields[i].getKey()==key){
			
			return fields[i];
		}
	}
		
}
/*var f = new YHField("dd","cc");
var f1 = new YHField("dd","cc");
var f2 = new YHField("dd","cc");

var r = new YHRecord();
r.addField(f).addField(f1).addField(f2);

for ( var f4 in r.fields) {
	
	alert(f4.getKey());
}*/

//定义一个解析json数据的类
function DataJSONParser(json){
	this.json = json ;
}
DataJSONParser.prototype.parser = _parser ; 
//将json数据解析成一组YHRecod对象 返回一个包含YHRecod对象的数组
function _parser(hdarray){
	var rcs = new Array();
	var jsonText = this.json;
	for(var i = 0; i < jsonText.recods.length; i++){
		var rc = new YHRecord();
		for(var j = 0; j < hdarray.length; j++){
			var key = hdarray[j].name ;
			var value = jsonText.recods[i][key];
			var field = new YHField(key,value);
			rc.addField(field);
		}
		rcs.push(rc);
	}
	return rcs;
}
/**
 * @param domId  所要放置Grid组件的dom组件间id
 * @param hdarray grid头部信息数组
 * @param url 获得数据的url地址
 * @param isSynchronous 是否同步操作
 */
function YHGrid(domId,hdarray,url,isSynchronous,stytl){
	this.data ;
	this.herder ;
	this.el ;
	
}
YHGrid.prototype.init = _init ; 
YHGrid.prototype.rendTo = _rendTo ; 
YHGrid.prototype.createGrid = _createGrid ; 

function _init(domId,hdarray,url,isSynchronous){
	
}
function _createGrid(recods,stytl,hdarray){
	
	var gridDiv = $C('div');
	var gridTitleDiv = $C('div');
	var gridDataDiv = $C('div');
	var gridBarDiv = $C('div');
	var gridTable = $C('table');
	
	with(gridDiv){
		setAttribute('id','YH_grid');
		
		/*style.position = "absolute";
		style.index = '99';
		style.visibility = "hidden";
		style.border = '1px solid #999999';*/
	}
	
	with(gridTitleDiv){
		setAttribute('id','YH_grid_title');
		setAttribute('align','left');
	}
	
	with(gridDataDiv){
		setAttribute('id','YH_grid_data');
	}
	
	with(gridBarDiv){
		setAttribute('id','YH_grid_bar');
	}
	
	with(gridTable){
		setAttribute('id','YH_grid_table');
	}
	gridTable = _createtable(gridTable,hdarray,recods);
	gridDataDiv.appendChild(gridTable);
	gridDiv.appendChild(gridTitleDiv);
	gridDiv.appendChild(gridDataDiv);
	gridDiv.appendChild(gridBarDiv);
	alert(gridDiv);
	
}
function _createTh(_table,hdarray){
	var tr = document.createElement('tr');
	//var th = document.createElement('th');
	for(var i = 0;i < hdarray.length;i++){
		var th = document.createElement('th');
		th.appendChild(document.createTextNode(hdarray[i].header));
		th.setAttribute('name',hdarray[i].name);
		tr.appendChild(th);
	}
	_table.appendChild(tr);
	return _table;
}

function _createtable(_table,hdarray,recods){
	_table = _createTh(_table,hdarray);
	//var tr = document.createElement('tr');
	//var td =  document.createElement('td');
	for(var i = 1; i <= recods.length;i ++){
		var recod = recods[i-1];
		var tabRow = _table.insertRow(i);
		for(var j = 0; j < hdarray.length; j ++){
			var td = tabRow.insertCell(j);
			td.innerHTML=recod.getField(hdarray[j].name).getValue;
		}
	}
	return _table;
}

function _rendTo(domId){
	
}

function $C(tag){
	return document.createElement(tag);
}

var hd =[{header:"hID",name:"id"},{header:"hName",name:"name"},{header:"hAge",name:"age"}];
var jso = {"recods":[{"id":"","name":"","age":""},{"id":"","name":"","age":""},{"id":"","name":"","age":""}]};
var jsss = new DataJSONParser(jso);
var recods =  jsss._parser(hd)
var grid = new YHGrid();
grid.createGrid(recods,null,hd);
/*
 * 
 * function add(){
	var id = document.getElementById("count");
	
	id.value =parseInt(id.value)+1;
	var table =  document.getElementById("tbody");
	var tr = document.createElement("tr");
	
	var td =  document.createElement("td");
	td.appendChild(document.createTextNode(id.value));
	tr.appendChild(td);
	
	td =  document.createElement("td");
	var input =  document.createElement("input");
	input.setAttribute("type","text");
	input.setAttribute("name","StudyObj_name_"+id.value);
	input.setAttribute("value","nihao");
	td.appendChild(input);
	tr.appendChild(td);
	
	td = document.createElement("td");
	var input = document.createElement("input");
	input.setAttribute("type","text");
	input.setAttribute("name","StudyObj_leixing_"+id.value);
	input.setAttribute("value","");
	td.appendChild(input);
	tr.appendChild(td);
	
	td = document.createElement("td");
	td.appendChild(document.createTextNode("是："));
	var input = document.createElement("input");
	input.setAttribute("type","radio");
	input.setAttribute("name","StudyObj_sex_"+id.value);
	input.setAttribute("value","1");
	td.appendChild(input);
	td.appendChild(document.createTextNode("否:"));
	var input = document.createElement("input");
	input.setAttribute("type","radio");
	input.setAttribute("name","StudyObj_sex_"+id.value);
	input.setAttribute("value","0");
	td.appendChild(input);
	tr.appendChild(td);
	
	td =  document.createElement("td");
	td.appendChild(document.createTextNode("党员"));
	var input = document.createElement("input");
	input.setAttribute("type","checkbox");
	input.setAttribute("name","StudyObj_address_"+id.value);
	input.setAttribute("checked","checked");
	input.setAttribute("value","dangyuan");
	td.appendChild(input);
	td.appendChild(document.createTextNode("团员"));
	var input = document.createElement("input");
	input.setAttribute("type","checkbox");
	input.setAttribute("name","StudyObj_address_"+id.value);
	input.checked=true;
	input.setAttribute("value","tuanyuan");
	td.appendChild(input);
	td.appendChild(document.createTextNode("群众"));
	var input = document.createElement("input");
	input.setAttribute("type","checkbox");
	input.setAttribute("name","StudyObj_address_"+id.value);
	input.setAttribute("checked","checked");
	input.setAttribute("value","qunzhong");
	td.appendChild(input);
	tr.appendChild(td);
	table.appendChild(tr);
	
}
 * 
 * 
 * 
 * 
 * 
 * 
 * 	<div class="tobBar" id="tobBar" align="left">
   				
   				<a href="<%=basePath %>MyJsp.jsp">
   					<img src="<%=basePath %>image/add.gif" width="26" height="23" title="增加一个用户"/>
   				</a>
   				
   				
   			</div>
   			<div class="dataList" id="dataList">
	   			<table class="mytable" id="tab" align="left" width="500px">
	   			 		<tr>
	   			 			 <th align="left" width="50px" height="20">
	   			 					<h6>全选<input id="allcheck" type="checkbox" onclick="allcheck()" />
   									反选<input id="fcheck" type="checkbox" onclick="allfcheck()"/></h6>
   							  </th>
		   			 		  <th align="center" ><font style="font-famliy:华文行楷;">用户ID</font></th>
		   			 		  <th align="center">中文名</th>
		   			 		  <th align="center">英文名</th>
		   			 		  <th align="center">出生日期</th>
		   			 		  <th align="center">操作</th>
		   			 	 </tr>
				    		<%	
				    			int pages;
				    			Integer in= (Integer)request.getAttribute("pages") ;
				    			if(in==null){
				    					pages=1;
				    				}
				    				else
				    					pages=(in).intValue();
				    			int  totlesPage =((Integer) session.getAttribute("totlePages")).intValue() ;
				    			List list = (List)request.getAttribute("list");
				    			for(int i=0 ;i<list.size();i++){
				    			  
				   			 %>
			    		 <tr var="<%=((UserM)list.get(i)).getId()%>" onmouseover="rowOver(this)" onmouseout="rowOut(this)" onclick="selectRow(this)" ondblclick ="location.href='updatelist.do?user_id=<%=((UserM)list.get(i)).getId()%>'">
			    		 	<td align="center"><input type="checkbox" name="datacheck" id="<%=((UserM)list.get(i)).getId()%>"  /> </td>
			    		 	<td align="center"><a href="<%=basePath %>findById.do?id=<%=((UserM)list.get(i)).getId()%>"><%=((UserM)list.get(i)).getId() %></a></td>
			    		 	<td align="center"><%=((UserM)list.get(i)).getUserNameCn() %></td>
			    		 	<td align="center"><%=((UserM)list.get(i)).getUserNameEn() %></td>
			    		 	<td align="center"><%=((UserM)list.get(i)).getBrithday().toString() %></td>
			    		 	<td align="center"><a href="<%=basePath %>deletemanager.do?user_id=<%=((UserM)list.get(i)).getId()%>">删除</a>&nbsp;&nbsp;<a href="<%=basePath %>updatelist.do?user_id=<%=((UserM)list.get(i)).getId()%>">修改</a></td>
			    		 </tr>
		    		          <% } %>
	    		</table>
    		</div>
		   	<div class="botumBar" id="botumBar" align="center">
		   	<input type="button" onclick="find('<%=basePath%>findajax.do?pages=1')" value="第一页">
		   	&nbsp; 
		   	<input type="button" onclick="find('<%=basePath%>findajax.do?pages=<%=(pages<=1)?1:pages-1 %>')" value="上一页">
		   	&nbsp; 
		   	<input type="button" onclick="find('<%=basePath%>findajax.do?pages=<%=(pages>=totlesPage)?totlesPage:pages+1 %>')" value="下一页">
		   	&nbsp; 
		   	<input type="button" onclick="find('<%=basePath%>findajax.do?pages=<%=totlesPage %>')" value="最后一页">&nbsp;
	    	 		当前第<span id="pagesN"><span>/<span id="totlesP"><span>页 <br>
	    	 		<input type="button" onclick="find('<%=basePath%>findajax.do')">
	    	</div>
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * */


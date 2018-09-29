<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>客户信息列表</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
	<script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
	<script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
  	<script language="javascript" type="text/javascript">
		//载入标签样式
		function loadTabType(listType){
		
			var tabLength = 3;
			if("${range}"=="1"){
				tabLength = 4;
			}
			if(listType!=''){
				switch(listType){
					//待开发
					case '0':
						$("tabType3").className="tabTypeWhite";
						setOtherStyle(3,tabLength);
						break;
						
					//开发中
					case '1':
						$("tabType2").className="tabTypeWhite";
						setOtherStyle(2,tabLength);
						break;
						
					//已归属
					case '2':
						$("tabType1").className="tabTypeWhite";
						setOtherStyle(1,tabLength);
						break;
						
					
					case 'a':
						$("tabType4").className="tabTypeWhite";
						setOtherStyle(4,tabLength);
						break;
				}
			}
		}
		//标签跳转
		function tabReload(cusState){
			self.location.href="customAction.do?op=toListCustomersByOnlyArea&range=${range}&cusState="+cusState+"&str=${str}&areaName=${areaName}";
		}
		function loadFilter(){
			if('${range}'=='1'){
				setCuritemStyle($("filter").value,["all","tag1","none","date7","date15","date30"]);	
			}else{
				setCuritemStyle($("filter").value,["tag1","none","date7","date15","date30"]);	
			}
		}
		//列表筛选按钮链接
		function filterList(filter){
			$("filter").value=filter;
			loadList();
		}
		
		function toAssignC(){
			if(checkBoxIsEmpty("priKey")){
				cusPopDiv(12);
			}
		}
		function out(type){
			var range = "${range}";
			var corName = $("corName").value;
			var cusType = $("cusType").value;
			var corAdd = $("corAdd").value;
			var seName = "";
			if(range == "1"){
				seName = $("seName").value;
			}
			var cusInd = $("cusInd").value;
			var filter = $("filter").value;
			var color = $("color").value;
			var startTime = "";
			var endTime = "";
			if(type=="1"){
				if(checkBoxIsEmpty("priKey")){
					cusPopDiv(13,[range,corName,cusType,corAdd,seName,startTime,endTime,cusInd,type,filter,color]);
				}
			}
			else{
				cusPopDiv(13,[range,corName,cusType,corAdd,seName,startTime,endTime,cusInd,type,filter,color]);
			}
		}
		
		function batchColor(){
			if(checkBoxIsEmpty("priKey")){
				cusPopDiv(16);
			}
		}
		
		function fillSeName(){
			     var sel =  $("empName");
				 var empName = sel.options[sel.selectedIndex].text;
				 if(empName != "请选择"){
					 $("seName").value = sel.options[sel.selectedIndex].text;
				}else{
					 $("seName").value ="";
				}
				
		}
		
		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.corCode;
			var dblFunc="descPop('customAction.do?op=showCompanyCusDesc&corCode="+dataId+"&flag=0')";
			var cusMark = "";
			if(obj.corTempTag=="0"){
				cusMark="<span class='grayMark' id='"+dataId+"' style='cursor:hand' onClick=\"addTemp("+dataId+")\" title='添加/取消标记'></span>";
			}
			else{
				cusMark="<span class='redMark' id='"+dataId+"' style='cursor:hand' onClick=\"addTemp("+dataId+")\" title='添加/取消标记'></span>";
			}
			var corName=cusMark+"<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.corName+"</a>";
			var cusType = "";
			if(obj.cusType!=undefined){
				cusType = obj.cusType.typName;
			}
			var lastDate = "";
			if(obj.corLastDate !=undefined){
				lastDate=obj.corLastDate.substring(0,10)+"&nbsp;<img src='crm/images/content/add_cell.gif' class='imgAlign' style='cursor:pointer' alt='添加来往记录' onClick=\"cusPopDiv(4,["+dataId+",'0'])\"/>";
			}
			else{
				lastDate="<span class='gray'>未联系</span>&nbsp;<img src='crm/images/content/add_cell.gif' class='imgAlign' style='cursor:pointer' alt='添加来往记录' onClick=\"cusPopDiv(4,["+dataId+",'0'])\"/>";
			}
			className=getCusColorClass(obj.corColor);
			var funcCol = "<img src='crm/images/content/detail.gif' onClick=\""+dblFunc+"\" class='hand' alt='查看详细'/>&nbsp;&nbsp;<img onClick=\"cusPopDiv(11,"+dataId+")\" style='cursor:pointer' src='crm/images/content/edit.gif' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"cusDelDiv(1,"+dataId+")\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/>";
			datas = [getCusTxtValue('t_state',obj.corState), corName, getCusLev(obj.corHot), cusType, obj.corPhone, obj.corCellPhone,obj.corAddress, obj.corRemark,obj.person?obj.person.userName:"", lastDate, funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "customAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "getCustomersByOnlyArea";
			pars.range="${range}";
			pars.cusState="${cusState}";
			pars.str="${str}";
			
			var loadFunc = "loadList";
			var cols=[
				{name:"状态"},
				{name:"名称",align:"left"},
				{name:"级别",align:"left"},
				{name:"类型"},
				{name:"电话"},
				{name:"手机"},
				{name:"地址",align:"left"},
				{name:"备注",isSort:false,align:"left"},
				{name:"负责人"},
				{name:"最近联系日期"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,loadFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
			loadFilter();
		}
		
    	var gridEl = new MGrid("cusTab","dataList");
    	gridEl.config.hasCheckBox = true;
		createProgressBar();
		window.onload=function(){
			createCusSel("color","t_color");
			loadTabType("${cusState}");
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5,'searButton','after');
		}
  	</script>
  </head>
  
  <body>
    <div id="mainbox">
    	<div id="contentbox">
        	<div id="title"><a class="refreshButton" href="javascript:void(0)" onClick="self.history.go(0)" title="重新载入页面"></a>&nbsp;CRM管理 > <c:if test="${range!=1}">【<font color="red">${areaName }</font>】区域下的</c:if><c:if test="${range==1}">全部</c:if>客户</div>
  	   		<table class="mainTab" cellpadding="0" cellspacing="0">
                <tr>
                    <th>
                        <div id="tabType">
                         	<div id="tabType1" onClick="tabReload('2')">已归属</div>
                        	<div id="tabType3" onClick="tabReload('0')">待开发</div>
                         	<div id="tabType2" onClick="tabReload('1')">开发中</div>
                            <c:if test="${range==1}"><div id="tabType4" onClick="tabReload('a')">全部</div></c:if>
                        </div>
                    </th>
                </tr>
            </table>
            <script type="text/javascript">loadTabTypeWidth();</script>
            <div id="listContent">
             	<div class="listSearch">
                	<form style="margin:0; padding:0;" id="searchForm" onSubmit="loadList();return false;" >
                    	<input type="text" id="filter" name="filter" style="display:none"/>
                        客户名称：<input style="width:130px" class="inputSize2 inputBoxAlign" type="text" id="corName" name="corName" onBlur="autoShort(this,100)"/>&nbsp;
                        客户类型：<c:if test="${!empty cusTypeList}">
                        		<select id="cusType" name="cusType" class="inputBoxAlign inputSize2" style="width:100px">
                         			<option  value="">请选择</option>
                                    <c:forEach var="cusType" items="${cusTypeList}">
                         				<option value="${cusType.typId}">${cusType.typName}</option>
                         			</c:forEach>
                                </select>
                             </c:if>
                             <c:if test="${empty cusTypeList}">
                             	<select id="cusType" class="inputBoxAlign"  disabled>
                             		<option value="">未添加</option>
                             	</select>
                             </c:if>&nbsp;
                        标色：<select id="color" name="color" class="inputBoxAlign inputSize2" style="width:80px"><option value="">请选择</option></select>&nbsp;
                       <br/> 行业：<c:if test="${!empty cusIndList}"><select id="cusInd" name="cusInd" class="inputBoxAlign inputSize2" style="width:100px"><option  value="">请选择</option><c:forEach var="cusIndu" items="${cusIndList}"><option value="${cusIndu.typId}">${cusIndu.typName}</option></c:forEach></select></c:if><c:if test="${empty cusIndList}"><select id="cusInd" class="inputBoxAlign" disabled><option value="">未添加</option></select></c:if>&nbsp;
                        地址：<input style="width:140px" class="inputSize2 inputBoxAlign" type="text" id="corAdd"  name="corAdd" onBlur="autoShort(this,50)"/>&nbsp;  
                   	  <c:if test="${range==1}">
                   	  负责人：<input style="width:90px" class="inputSize2 inputBoxAlign" type="text" id="seName" name="seName" onBlur="autoShort(this,100)"/>&nbsp;
                   		<c:if test="${!empty empList}">
                   			<select id="empName" class="inputBoxAlign inputSize2" style="width:100px;"  onChange="fillSeName()">
                   				<option value="">请选择</option>
                   				<c:forEach var="emp" items="${empList}">
                   					<option value="${emp.seNo}">${emp.seName}</option>
                   				</c:forEach>
                   			</select>
                   		</c:if>
                   		<c:if test="${empty empList}">
                   			<select id="empName" class="inputBoxAlign" disabled>
                            	<option value="">未添加</option>
                            </select>
                   		</c:if>&nbsp;
                   	 </c:if>
                     <input type="submit" class="butSize3 inputBoxAlign" id="searButton" value="查询" />&nbsp;&nbsp;
                     <!--<a class="supSearButton inputBoxAlign" href="customAction.do?op=toCusSupSearch&range=${range}" target="_blank" style="float:none">高级查询</a>-->
                    </form>
                </div>
                <div id="topChoose" class="listTopBox">
                	<c:if test="${range==1}">
                		<a href="javascript:void(0)" onClick="filterList('all')" >&nbsp;未分配的客户&nbsp;</a>
                	</c:if>
                		<a href="javascript:void(0)" onClick="filterList('tag1')" >&nbsp;星标客户&nbsp;</a>
                		<a href="javascript:void(0)" onClick="filterList('none')" >&nbsp;从未联系过&nbsp;</a>
                		<a href="javascript:void(0)" onClick="filterList('date7')" >&nbsp;7-15天未联系&nbsp;</a>
                		<a href="javascript:void(0)" onClick="filterList('date15')" >&nbsp;15-30天未联系&nbsp;</a>
                		<a href="javascript:void(0)" onClick="filterList('date30')" >&nbsp;30天及以上未联系&nbsp;</a>
	            </div>
                <div id="toolsBarBottom" class="bottomBar" >
					批量操作：&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="out('0')">导出查询结果</span>
                        &nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="out('1')">导出所选客户</span>
                    <c:if test="${range==1}">
                        &nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="toAssignC()">修改客户状态</span>
                        &nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="cusPopDiv(15)">导入客户</span>
                    </c:if>
                    	&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="batchColor()">批量标色</span>
               	</div>
                <div id="dataList" class="dataList"></div>
            </div>
  		</div> 
	</div>
  </body>
</html>

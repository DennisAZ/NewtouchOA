<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%@ taglib uri="http://struts.apache.org/tags-logic"  prefix="logic"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//清缓存
response.setHeader("Pragma","No-cache");   
response.setHeader("Cache-Control","no-cache");   
response.setDateHeader("Expires", 0);  
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    <title>客户高级查询</title>
    <link rel="shortcut icon" href="favicon.ico"/>
    <meta http-equiv="x-ua-compatible" content="ie=EmulateIE7" />
	<link rel="stylesheet" type="text/css" href="crm/css/style.css"/>
    <link rel="StyleSheet" href="crm/css/dtree.css" type="text/css">
    
    <script type="text/javascript" src="crm/js/prototype.js"></script>
    <script type="text/javascript" src="crm/My97DatePicker/WdatePicker.js"></script>
	<script type="text/javascript" src="crm/js/dtree.js"></script>
    <script type="text/javascript" src="crm/js/common.js"></script>
    <script type="text/javascript" src="crm/js/cus.js"></script>
    <script type="text/javascript" src="crm/js/formCheck.js"></script>
    <script language="javascript" type="text/javascript">
	 function showArea(){
		$("country").value="1";
		getCityInfo("cou");
	}
	 function toAssignC(){
			if(checkBoxIsEmpty("priKey")){
				cusPopDiv(12);
			}
		}
	  function check(){
			return $("supSear").submit();
	   }

	
		var treeName = 'supUT';
		var supUT = new dTree(treeName);
		supUT.config.showRootIcon=false;//不显示root图标
		supUT.config.hasCheckBox=true;
		supUT.config.folderLinks=false;

		function out(type){
		var range = "${range}";
		var limCode = "${limCode}";
		var allName = $("allName").value;
		var allMne = $("allMne").value;
		var allCode = $("allCode").value;
		var allTemp = $("allTemp").value;
		var industryId = $("indId").value;
		var allStar = $("allStar").value;
		var souId = $("souId").value;
		var cState = $("cState").value;
		var allHot = $("allHot").value;
		var relLev = $("relLev").value;
		var corPerSize = $("corPerSize").value;
		var startTime = $("startTime").value;
		var endTime = $("endTime").value;
		var allCountry = $("country").value;
		var allProvince = $("pro").value;
		var allCity = $("city").value;
		var update = $("update").value;
		var address = $("address").value;
		var contact = $("contact").value;
		var phone = $("phone").value;
		var fex = $("fex").value;
		var remark = $("remark").value;
		if(type=="1"){
			if(checkBoxIsEmpty("priKey")){
				cusPopDiv(14,range,limCode,allName,allMne,allCode,allTemp,industryId,allStar,souId,cState,allHot,relLev,
					corPerSize,startTime,endTime,allCountry,allProvince,allCity,update,address,contact,phone,fex,remark,type);
			}
		}
		else{
			cusPopDiv(14,range,limCode,allName,allMne,allCode,allTemp,industryId,allStar,souId,cState,allHot,relLev,
					corPerSize,startTime,endTime,allCountry,allProvince,allCity,update,address,contact,phone,fex,remark,type);
			}
		}

		function dataMapper(obj){
			var datas,className,dblFunc,dataId;
			dataId = obj.corCode;
			var dblFunc="descPop('customAction.do?op=showCompanyCusDesc&corCode="+dataId+"')";
			var cusMark = "";
			if(obj.corTempTag=="0"){
				cusMark="<div class='grayMark' id='"+dataId+"' style='cursor:hand' onClick=\"addTemp("+dataId+")\" title='添加/取消标记'></div>";
			}
			else{
				cusMark="<div class='redMark' id='"+dataId+"' style='cursor:hand' onClick=\"addTemp("+dataId+")\" title='添加/取消标记'></div>";
			}
			var corName="&nbsp;&nbsp;&nbsp;<a href='javascript:void(0)' onclick=\""+dblFunc+"\">"+obj.corName+"</a>";
			var cusState = "";
			if(obj.cusState!=undefined){
				cusState = obj.cusState.typName;
			}
			var lastDate = "";
			if(obj.corLastDate !=undefined){
				lastDate=obj.corLastDate.substring(0,10)+"&nbsp;<img src='crm/images/content/add_cell.gif' class='imgAlign' style='cursor:pointer' alt='添加来往记录' onClick=\"cusPopDiv(4,["+dataId+",'0'])\"/>";
			}
			else{
				lastDate="<span class='gray'>未联系</span>&nbsp;<img src='crm/images/content/add_cell.gif' class='imgAlign' style='cursor:pointer' alt='添加来往记录' onClick=\"cusPopDiv(4,["+dataId+",'0'])\"/>";
			}
			switch(obj.corColor){
				case '0': className="customerNormal";break;
				case '1': className="customerRed";break;
				case '2': className="customerGreen";break;
				case '3': className="customerBlue";break;
			}
			var funcCol = "<img src='crm/images/content/detail.gif' onClick=\""+dblFunc+"\" class='hand' alt='查看详细'/>&nbsp;&nbsp;<img onClick=\"cusPopDiv(11,"+dataId+")\" style='cursor:pointer' src='crm/images/content/edit.gif' alt='编辑'/>&nbsp;&nbsp;<img onClick=\"cusDelDiv(1,"+dataId+")\" style='cursor:pointer' src='crm/images/content/del.gif' alt='删除'/>";
			datas = [cusMark, getCusLev(obj.corHot), corName, cusState, obj.corAddress, obj.corPhone, obj.corCellPhone, obj.corRemark,obj.salEmp?obj.salEmp.seName:"",lastDate,funcCol ];
			return [datas,className,dblFunc,dataId];
		}
		function loadList(sortCol,isDe,pageSize,curP){
			var url = "customAction.do";
			var pars = $("searchForm").serialize(true);
			pars.op = "supSearch";
			pars.range="${range}";
			
			var sortFunc = "loadList";
			var cols=[
				{name:"标记",width:"50"},
				{name:"级别"},
				{name:"名称"},
				{name:"类型"},
				{name:"地址"},
				{name:"电话"},
				{name:"手机"},
				{name:"备注",isSort:false},
				{name:"负责人"},
				{name:"最近联系日期"},
				{name:"操作",isSort:false}
			];
			gridEl.init(url,pars,cols,sortFunc,sortCol,isDe,pageSize,curP);
			gridEl.loadData(dataMapper);
		}
		createProgressBar();
		var gridEl = new MGrid("supSearList","dataList");
		gridEl.config.hasCheckBox = true;
		window.onload=function(){
			showCheckBox('${limCode}',treeName);
			showArea();
			//表格内容省略
			loadList();
			//增加清空按钮
			createCancelButton(loadList,'searchForm',-50,5,'searButton','after');
			//closeProgressBar();
		}

		
  	</script>
  </head>
  
  <body>
  	<div id="mainbox">
    	<div id="contentbox">
        	<div class="descInf">
            	<div id="descTop">客户高级查询</div>
                  	<form  id="searchForm" onSubmit="loadList();return false;">
                        <table class="supSearForm" cellpadding="0" cellspacing="0">
                        	<logic:equal name="range" value="1">
                        	<tr>
                            	<th>负责人</th>
                                <td>
                                	<input type="text" style="display:none" name="userCBName" id="userCBName"/>
                                    <div>
										<script type="text/javascript">
                                            $("userCBName").value="nodes"+treeName;
                                            supUT.add('${orgTopCode}',-1,'人员列表','','人员列表','_parent');
                                        </script>
                                        <logic:notEmpty name="orgList">
                                        <logic:iterate id="ol" name="orgList" scope="request">
                                        <script type="text/javascript">
                                            var id='${ol.soCode}'
                                            var pid='${ol.salOrg.soCode}';
                                            supUT.add(id,pid,'<c:out value="${ol.soName}"/>','','<c:out value="${ol.soName}"/>','_parent','crm/images/dtree/orgnode.gif','crm/images/dtree/orgopen.gif');
                                        </script>
                                        <logic:notEmpty name="ol" property="limUsers">
                                            <logic:iterate id="lu" name="ol" property="limUsers">
                                                <logic:equal name="lu" property="userIsenabled" value="1">
                                                <script type="text/javascript">
                                                    var fid='${lu.salEmp.seNo}';
                                                    var name='<c:out value="${lu.salEmp.seName}"/>';
                                                    var role='<c:out value="${lu.salEmp.limRole.rolName}"/>';
                                                    var name1;
                                                    if(role!=null&&role!=""){
                                                        name1=name+"-"+role;
                                                    }									
                                                    if(pid!=null&&pid!=""){	
                                                        supUT.add(fid,id,name1,null,name1,'_parent',null,null,null,'${lu.salEmp.seNo}');
                                                    }
                                                </script>
                                                </logic:equal>
                                            </logic:iterate>
                                        </logic:notEmpty>
                                        </logic:iterate>
                                        <script type="text/javascript">
                                            document.write(supUT);
                                        </script>
                                        </logic:notEmpty>
                                    </div>
                                </td>
                            </tr>
                        	</logic:equal>
                            <tr>
                            	<th>基本信息</th>
                                <td>
                                	<table class="innerSearForm" cellpadding="0" cellspacing="0">
                                        <tr>
                                            <th>客户名称：</th>
                                            <td style="width:40%"><input class="inputSize2" type="text" id="allName" name="allName"/></td>
                                            <th>助记简称：</th>
                                            <td style="width:50%"><input class="inputSize2" type="text" id="allMne" name="allMne"/></td>
                                        </tr>
                                        <tr>
                                            <th>客户编号：</th>
                                            <td><input class="inputSize2" type="text" id="allCode" name="allCode"/></td>
                                            <th>星标标记：</th>
                                            <td>
                                                <select class="inputSize2" id="allTemp" name="allTemp">
                                                    <option value="">请选择</option>
                                                    <option value="1">是</option>
                                                    <option value="0">否</option>
                                                </select>
                                            </td>
                                         </tr>
                                        <tr>
                                        	<th>所属行业：</th>
                                            <td>
                                                <select class="inputSize2" id="indId" name="industryId">
                                                   <option value="">请选择</option>
                                                   <logic:notEmpty name="industryList">
                                                   <logic:iterate id="industryList" name="industryList">
                                                    <option value="${industryList.typId}">${industryList.typName}</option>
                                                   </logic:iterate>
                                                   </logic:notEmpty>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                        	<th>来源：</th>
                                            <td>
                                            	<select class="inputSize2" id="souId" name="souId">
                                                	<option value="">请选择</option>
                                                	<logic:notEmpty name="souList">
                                                   	<logic:iterate id="souList" name="souList">
                                                    <option value="${souList.typId}">${souList.typName}</option>
                                                   	</logic:iterate>
                                                   	</logic:notEmpty>
                                                </select>
                                            </td>
                                            <th>客户状态：</th>
                                            <td><logic:notEmpty name="cusStateList">
                                                <select id="cState" name="cusState" class="inputSize2">
                                                    <option  value="">请选择</option>
                                                    <logic:iterate id="cusState" name="cusStateList" >
                                                    <option value="${cusState.typId}">${cusState.typName}</option>
                                                    </logic:iterate>
                                                </select>
                                                </logic:notEmpty>
                                                <logic:empty name="cusStateList">
                                                    <span class="gray">未添加</span>
                                                </logic:empty>
                                            </td>
                                        </tr>
                                        <tr>
                                        	<th>客户级别：</th>
                                            <td>
                                                <select class="inputSize2" id="allHot" name="allHot">
                           							<option value="">请选择</option>
                                                    <option value="1星">1星</option>
                                                    <option value="2星">2星</option>
                                                    <option value="3星">3星</option>
                                                    <option value="4星">4星</option>
                                                    <option value="5星">5星</option>
                                                    <option value="6星">6星</option>
                                                    <option value="7星">7星</option>
                                                </select>
                                            </td>
                                        	<th>关系等级：</th>
                                            <td>
                                                <select id="relLev" name="relLev" class="inputSize2">
                                                    <option  value="">请选择</option>
                                                    <option value="一般">一般</option>
                                                    <option value="较好">较好</option>
                                                    <option value="密切">密切</option>
                                                    <option value="较差">较差</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                        	<th>人员规模：</th>
                                            <td>
                                            	<select class="inputSize2" id="corPerSize" name="corPerSize">
                                                	<option value="">请选择</option>
                                                    <option value="10人以内">10人以内</option>
                                                    <option value="10-20人">10-20人</option>
                                                    <option value="20-30人">20-30人</option>
                                                    <option value="30-50人">30-50人</option>
                                                    <option value="50-100人">50-100人</option>
                                                    <option value="100人以上">100人以上</option>
                                                 </select>
                                            </td>
                                            <th>创建时间：</th>
                                            <td><input name="startTime" type="text" class="Wdate shortDate" readonly="readonly" id="pid" ondblClick="clearInput(this)" onFocus="var endTime=$('endTime');WdatePicker({onpicked:function(){endTime.focus();},maxDate:'#F{$dp.$D(\'endTime\')}'})"/>
                                            &nbsp;到&nbsp;
                                            <input name="endTime" type="text" class="Wdate shortDate" readonly="readonly" id="endTime" ondblClick="clearInput(this)" onFocus="WdatePicker({minDate:'#F{$dp.$D(\'pid\')}'})"/></td>
                                        </tr>
                                	</table>
                                </td>
                            </tr>
                            <tr>
                            	<th style="border:0">联系方式</th>
                                <td style="border:0">
                                	<table class="innerSearForm"  cellpadding="0" cellspacing="0">
                                 		<tr>
                                    		<th>所在地区：</th>
                                            <td>
                                                <select id="country"  name="allCountry" onChange="getCityInfo('cou')">
                                                    <logic:notEmpty name="cusAreaList">
                                                    <logic:iterate id="cusAreaList" name="cusAreaList">
                                                        <option value="${cusAreaList.areId}">${cusAreaList.areName}</option>
                                                    </logic:iterate>
                                                    </logic:notEmpty>
                                                </select>(国家)
                                                <select id="pro" name="allProvince" onChange="getCityInfo('province')"></select>(省)
                                                <select id="city" name="allCity"></select>(市)
                                            </td>
                                            <th>更新情况：</th>
                                           	<td>
                                                <select class="inputSize2" id="update" name="update">
                                                    <option value="">请选择</option> 
                                                    <option value="u7">7-15天未更新</option>
                                                    <option value="u15">15-30天未更新</option>
                                                    <option value="u30">30天及以上未更新</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                        	<th>地址：</th>
                                            <td style="width:40%"><input class="inputSize2" type="text" id="address" name="address"/></td>
                                            <th>联系情况：</th>
                                            <td style="width:50%">
                                                <select class="inputSize2" id="contact" name="contact">
                                                    <option value="">请选择</option>
                                                    <option value="noContact">从未联系过</option>
                                                    <option value="c7">7-15天未联系</option>
                                                    <option value="c15">15-30天未联系</option>
                                                    <option value="c30">30天及以上未联系</option>
                                                </select>
                                            </td>
                                        </tr>
                                        <tr>
                                            <th>电话：</th>
                                            <td ><input class="inputSize2" type="text" id="phone" name="phone"/></td>
                                            <th >传真：</th>
                                            <td><input class="inputSize2" type="text" id="fex" name="fex"/></td>
                                        </tr>
                                        <tr>
                                        	<td align="right" valign="top"><div align="right">备注：</div></td>
	                            			<td colspan="3"><textarea rows="3" cols="30" style="width:255px" id="remark" name="remark" onBlur="autoShort(this,4000)"></textarea></td>
                                        </tr>
                                        <tr>
                                        	<th>&nbsp;</th>
                                        	<td></td>
                                        </tr>
                                        <tr>
				                           	<th>&nbsp;</th>
				                           	<td>
				                               	<input type="submit" class="butSize1 inputBoxAlign" id="searButton" value="开始搜索" />
				                            </td>
                           				</tr>
                              		</table>
                                </td>
                            </tr>
                        </table>
                    </form>
                 	<div id="toolsBarBottom" class="bottomBar" >
                        批量操作：&nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="out('0')">导出查询结果</span>
                      		 &nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="out('1')">导出所选客户</span>
                       <logic:equal value="1" name="range">
                       &nbsp;&nbsp;<span class="grayBack" onMouseOver="this.className='orangeBox red'" onMouseOut="this.className='grayBack'" onClick="toAssignC()">分配负责人</span>
                   	  </logic:equal> 
                    </div>
					<div id="dataList"></div>
                </div>
        	</div>
		</div>
  </body>
</html>

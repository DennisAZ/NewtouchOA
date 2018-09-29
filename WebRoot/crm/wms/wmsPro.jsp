<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int count=0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>   
    <title>选择产品</title><!--(库存)-->
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<link rel="StyleSheet" href="css/dtree.css" type="text/css">
    <style type="text/css">
    	body{
			background-color:#FFFFFF;
			overflow:auto
			
		}
		#searchPro {
			 margin:8px; 
			 padding:0px;
		}
		a {
			zoom:1;
		}
    </style>
    <script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/dtree.js"></script>
    <script type="text/javascript" src="js/choosePro.js"></script>
	<script type="text/javascript" src="js/common.js"></script>
    <script type="text/javascript" src="js/wms.js"></script>
	<script type="text/javascript">
	function check(){
		var obj=$("pName");
		if(obj==null||obj.value==""){
			alert("请输入查询条件");
			return false;
		}else{
			return $("form").submit();
		}
	}
	var wmsProTree = new dTree('wmsProTree');
	wmsProTree.config.folderLinks=true;
	wmsProTree.config.showRootIcon = false;
	//id, pid, name, url, title, target, icon, iconOpen, open		
	wmsProTree.add(0,-1,'产品类别','','产品类别','_parent');
	</script>
  </head>
 
  <body>
  <div style="padding:5px;">
  	<div align="left">
		<form action="prodAction.do" id="form" method="post">
			<input type="hidden" name="op" value="proSearch">
			<input type="hidden" name="agr" value="2">
			<input type="hidden" name="wm" value="${wm}">
			查询产品<span class="gray">&nbsp;(可按产品名称/编号/型号查询)</span>
			<input type="text" class="inputSize2 inputBoxAlign" style="width:70%" id="pName" name="pName" value="${wprName}"/>
			<input type="button" id="Submit" class="butSize1 inputBoxAlign" value="查询" onClick="check()">
		</form>
		<logic:notEmpty name="wmsProduct">
			<table class="normal" cellpadding="0" cellspacing="0" border="0" width="95%">
			    <tr>
                    <td style="border-top:#CCCCCC 1px dashed; line-height:8px">&nbsp;</td>
                </tr>
                <tr>
                    <td class="orange">
                        <img id="hideImg" class="imgAlign" src="images/content/hide.gif" onClick="showHide('searchResult','hideImg')" style="cursor:pointer;" alt="点击收起"/>&nbsp;查询结果：
                    </td>
                </tr>
				<tr>
					<td>
						 <div id="searchResult" style="display:block;">
							 <ul class="listtxt2">
						 <logic:iterate id="pro" name="wmsProduct" scope="request">
						 		<li>&nbsp;
						  <logic:empty name="wm">
							<a href="javascript:void(0);" onClick="tbladdrow(1,'rwpRow','${pro.wprId}',new Array('${pro.wprName}','${pro.wprCode}'),'${pro.wprModel}','${pro.typeList.typName}');return false;"><img src="images/content/ball.gif" style="vertical-align:middle; border:0px"/>${pro.wprName}<logic:notEmpty name="pro" property="wprModel">/${pro.wprModel}</logic:notEmpty><logic:notEmpty name="pro" property="typeList">[${pro.typeList.typName}]</logic:notEmpty></a>
							</logic:empty>
							<logic:notEmpty name="wm">
							<a href="javascript:void(0);" onClick="choosePro(1,'${pro.wprId}','${pro.wprName}');return false;">${pro.wprName}<logic:notEmpty name="pro" property="wprModel">/${pro.wprModel}</logic:notEmpty><logic:notEmpty name="pro" property="typeList">[${pro.typeList.typName}]</logic:notEmpty></a>
							</logic:notEmpty>
								</li>
						</logic:iterate>
                            </ul>
						</div>
					</td>
				</tr>
			</table>
		</logic:notEmpty>
		</div>
		<hr style="border:#fba01f 1px solid"/>
  		<fieldset>
  			<legend class="orange">&nbsp;产品列表&nbsp;</legend>
			<div align="left">
				<logic:notEmpty name="wmsProduct1">
						<script type="text/javascript">		
						wmsProTree.add('noCode','','(未分类)','','未分类','_parent');
						</script>
                            	<logic:iterate id="pro1" name="wmsProduct1" scope="request">
								<logic:equal value="1" name="pro1" property="wprIscount">
								<logic:notEmpty name="wm"><div style="display:none" id="wms"></div></logic:notEmpty>
										<script type="text/javascript">	
										var id='noCode';
										var id1='pro${pro1.wprId}';
										var name='${pro1.wprName}';
										var m='${pro.wprModel}';
										var n='${pro.typeList.typName}';
										var name1;
										var wm=$("wms");
										if(m!=null&&m!=""){
											name1=name+"/"+m;
											if(n!=null&&n!=""){
											name1=name1+"["+n+"]";
											}
										}else{
											name1=name;
											if(n!=null&&n!=""){
											name1=name1+"["+n+"]";
											}
										}
										var Link;
										if(wm==null){
											Link="javascript:tbladdrow(1,'rwpRow','${pro1.wprId}',new Array('${pro1.wprName}','${pro1.wprCode}'),'${pro1.wprModel}','${pro1.typeList.typName}','${pro1.wprCode}')";
										}else{
											Link="javascript:choosePro(1,'${pro1.wprId}','"+name1+"')";
										}
										if(id1!=null&&id1!=""){	
										wmsProTree.add(id1,id,name1,Link,name1,'_parent');
										}
										</script>
										</logic:equal>
                                </logic:iterate>
            </logic:notEmpty>
				 <logic:notEmpty name="wmsProType">
					<logic:iterate id="pt" name="wmsProType" scope="request">
						<script type="text/javascript">
						var p='orgimg/folder.gif';
						var pi='orgimg/folderopen.gif';	
						wmsProTree.add('${pt.wptId}','${pt.wmsProType.wptId}','(${pt.wptName})','','${pt.wptName}','_parent',p,pi);
						</script>
						<logic:notEmpty name="wm"><div style="display:none" id="wm1"></div></logic:notEmpty>
                                <logic:iterate id="wp" name="pt" property="wmsProducts">
								<logic:equal value="1" name="wp" property="wprIscount">
									<script type="text/javascript">	
										var id='${wp.wmsProType.wptId}';
										var id1='pro${wp.wprId}';
										var name='${wp.wprName}';
										var m='${wp.wprModel}';
										var n='${wp.typeList.typName}';
										var name1;
										var wm=$("wm1");
										if(m!=null&&m!=""){
											name1=name+"/"+m;
											if(n!=null&&n!=""){
											name1=name1+"["+n+"]"
											}
										}else{
											name1=name;
											if(n!=null&&n!=""){
											name1=name1+"["+n+"]";
											}
										}
										var Link;
										if(wm==null){
											Link="javascript:tbladdrow(1,'rwpRow','${wp.wprId}',new Array('${wp.wprName}','${wp.wprCode}'),'${wp.wprModel}','${wp.typeList.typName}','${wp.wprCode}')";
										}else{
											Link="javascript:choosePro(1,'${wp.wprId}','"+name1+"')";
										}
										if(id1!=null&&id1!=""){
											wmsProTree.add(id1,id,name1,Link,name1,'_parent');
										}	
									</script>
									</logic:equal>
                                </logic:iterate>
					</logic:iterate>
            </logic:notEmpty>
			<script type="text/javascript">
					document.write(wmsProTree);
					wmsProTree.openAll();
			</script>
			</div>
	</fieldset>
	<logic:empty name="wmsProType">
		<logic:empty name="wmsProType1">
			<div class="dataList title blue bold" style="padding-top:30px">${msg}</div>
		</logic:empty>
	</logic:empty>
	</div>
  </body>
</html>

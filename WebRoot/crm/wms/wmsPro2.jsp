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
    <title>选择产品</title><!--(采购单)-->
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="x-ua-compatible" content="ie=EmulateIE7"/>
    <meta http-equiv="cache-control" content="no-cache"/>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link rel="stylesheet" type="text/css" href="css/style.css"/>
	<link rel="StyleSheet" href="css/dtree.css" type="text/css">
    <style type="text/css">
    	body{
			background-color:#FFFFFF;
			overflow-y:hidden;
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
	<script type="text/javascript">
	function check(){
		var obj=$("pName");
		if(obj==null||obj.value==""){
			alert("请输入查询条件");
			return false;
		}else{
			return $("searchPro").submit();
		}
	}
	var wmsPro2Tree = new dTree('wmsPro2Tree');
	wmsPro2Tree.config.folderLinks=true;
	wmsPro2Tree.config.showRootIcon = false;
	//id, pid, name, url, title, target, icon, iconOpen, open		
	wmsPro2Tree.add(0,-1,'产品类别','','产品类别','_parent');
	</script>
  </head>
 
  <body>
  	<div style="padding:5px;">
        <div align="left">
            <form id="searchPro" action="prodAction.do" method="post">
            	<input type="hidden" name="op" value="proSearch">
                <input type="hidden" name="ord" value="spo">
                <input type="hidden" name="agr" value="1">
                查询产品<span class="gray">&nbsp;(可按产品名称/编号/型号查询)</span>
                <input type="text" class="inputSize2 inputBoxAlign" style="width:70%" id="pName" name="pName" value="${wprName}"/>
                <input type="button" id="Submit" value="查询" class="butSize1 inputBoxAlign" onClick="check()">
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
                                <li>
                                &nbsp;
                                <a href="javascript:void(0);" onClick="tbladdrow(3,'ordDesc','${pro.wprId}','${pro.wprName}','${pro.wprModel}','${pro.typeList.typName}','${pro.wprSalePrc}');return false;"><img src="images/content/ball.gif" style="vertical-align:middle; border:0px"/>${pro.wprName}<logic:notEmpty name="pro" property="wprModel">/${pro.wprModel}</logic:notEmpty><logic:notEmpty name="pro" property="typeList">[${pro.typeList.typName}]</logic:notEmpty></a><logic:equal value="1" name="pro" property="wprIscount"><img src="images/content/database.gif" alt="计算库存"></logic:equal >
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
						wmsPro2Tree.add('noCode','','(未分类)','','未分类','_parent');
						</script>
                            	<logic:iterate id="pro" name="wmsProduct1" scope="request">
                            	
										<script type="text/javascript">	
										var id='noCode';
										var id1='pro${pro.wprId}';
										var name='${pro.wprName}';
										var m='${pro.wprModel}';
										var n='${pro.typeList.typName}';
										var isCount='${pro.wprIscount}';
										var name1;
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
										if(isCount!=0){
											name1=name1+"<img src='images/content/database.gif' alt='计算库存'>";
										}
										var Link="javascript:tbladdrow(3,'ordDesc','${pro.wprId}','${pro.wprName}','${pro.wprModel}','${pro.typeList.typName}','${pro.wprSalePrc}');";	
										if(id1!=null&&id1!=""){	
										wmsPro2Tree.add(id1,id,name1,Link,name1,'_parent');
										}
										</script>
                               
                                </logic:iterate>
            </logic:notEmpty>
             <logic:notEmpty name="wmsProType">
					<logic:iterate id="pt" name="wmsProType" scope="request">
						<script type="text/javascript">
						var p='orgimg/folder.gif';
						var pi='orgimg/folderopen.gif';	
						wmsPro2Tree.add('${pt.wptId}','${pt.wmsProType.wptId}','(${pt.wptName})','','${pt.wptName}','_parent',p,pi);
						</script>
                                <logic:iterate id="wp" name="pt" property="wmsProducts">
                               
									<script type="text/javascript">	
										var id='${wp.wmsProType.wptId}';
										var id1='pro${wp.wprId}';
										var name='${wp.wprName}';
										var m='${wp.wprModel}';
										var n='${wp.typeList.typName}';
										var isCount='${wp.wprIscount}';
										var name1;
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
										if(isCount!=0){
											name1=name1+"<img src='images/content/database.gif' alt='计算库存'>";
										}
										var Link="javascript:tbladdrow(3,'ordDesc','${wp.wprId}','${wp.wprName}','${wp.wprModel}','${wp.typeList.typName}','${wp.wprSalePrc}');";
										if(id1!=null&&id1!=""){
											wmsPro2Tree.add(id1,id,name1,Link,name1,'_parent');
										}	
									</script>
                               
                                </logic:iterate>
					</logic:iterate>
            </logic:notEmpty>
			<script type="text/javascript">
					document.write(wmsPro2Tree);
					wmsPro2Tree.openAll();
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

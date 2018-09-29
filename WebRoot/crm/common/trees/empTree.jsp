<%@ page contentType="text/html;" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
	<title>员工列表树</title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<link rel="StyleSheet" href="css/dtree.css" type="text/css">
    <style type="text/css">
    	body{
			margin:0;
			padding:5px;
		}
    </style>
    
	<script type="text/javascript" src="js/prototype.js"></script>
	<script type="text/javascript" src="js/dtree.js"></script>
    <script type="text/javascript" src="js/chooseBrow.js"></script>
</head>

<body>
	<div>
	<script type="text/javascript">
		var type='${type}';//树类型
		var url;
        var empTree = new dTree('empTree');//树名与树对象名必须一致
		var target = '_self';//跳转参数
		
		switch(type){
			case 'getNames':
				//多选并返回文本
				empTree.config.hasCheckBox = true;
				empTree.config.isReturnName = true;
				break;
			case 'proj':
				empTree.config.hasCheckBox = true;
				empTree.config.hasCallBack = true;
				break;
				
		}
        empTree.config.showRootIcon=false;//不显示root图标
        empTree.config.folderLinks=false;	
        //id, pid, name, url, title, target, icon, iconOpen, open	
        empTree.add('${orgTopCode}',-1,'员工列表','','员工列表','_self');
    </script>
    <c:if test="${!empty orgList}">
    	<c:forEach var="salOrg" items="${orgList}">
		 	<script type="text/javascript">
			empTree.add('${salOrg.soCode}','${salOrg.salOrg.soCode}','<c:out value="${salOrg.soName}"/>','','<c:out value="${salOrg.soName}"/>',target,'images/dtree/orgnode.gif','images/dtree/orgopen.gif');
			</script>
        </c:forEach>
        <c:if test="${!empty salEmps}">
        	<c:forEach var="emp" items="${salEmps}">
            <c:if test="${emp.seRap!='离职'}">
            <script type="text/javascript">
                var empImg;
                if('${emp.seSex}'=='女'){
                    empImg = 'images/dtree/empnode_w.gif';
                }
                else{
                    empImg = 'images/dtree/empnode_m.gif';
                }
                var nodeTxt = '<c:out value="${emp.seName}"/>[<c:out value="${emp.limRole.rolName}"/>]';
				
				var nodeTitle = '<c:out value="${emp.seName}-${emp.seCode}[${emp.limRole.rolName}]"/>';
                switch(type){
                    case 'getNames':
                        url = '';
                        break;
                    case 'addTd'://设置销售指标选择人员
                        url = "javascript:addTbRow('${emp.seNo}','<c:out value="${emp.seName}"/>');";
						target="_parent";
                        break;
					case 'sys'://添加账号选择人员
						if('${emp.seUserCode}'==''){
							url = 'javascript:chooseEmpComplete(\'${emp.seNo}\',\'<c:out value="${emp.seName}"/>\',\'<c:out value="${emp.limRole.rolName}"/>\');';
						}
						else{
							url='';
							nodeTxt += "<span style='font-size:11px; font-style:italic; color:#b4b4b4'>已分配</span>";
							nodeTitle += "已分配了账号<c:out value="${emp.seUserCode}"/>";
						}
						break;
                    default:
                        url = 'javascript:chooseEmpComplete(\'${emp.seNo}\',\'<c:out value="${emp.seName}"/>\');';
                }
                if(empTree.config.hasCheckBox){
                    empTree.add('${emp.seNo}','${emp.salOrg.soCode}',nodeTxt,url,nodeTitle,target,empImg,empImg,'','${emp.seNo}');
                }
                else{
                    empTree.add('${emp.seNo}','${emp.salOrg.soCode}',nodeTxt,url,nodeTitle,target,empImg,empImg);
                }
                
            </script>
            </c:if>
            </c:forEach>
        </c:if>
	</c:if>
    
	<script type="text/javascript">
		document.write(empTree);
		empTree.openAll();
		if(empTree.config.hasCheckBox){
			var idsEl;
			if(parent.document.getElementById("popInside")!=null){
				idsEl=parent.document.getElementById("popInside").contentWindow.document.getElementById("nodeIds");
					
			}
			else{
				idsEl=parent.document.getElementById("nodeIds");
			}
			if(idsEl!=null&&idsEl.value!=""){
				empTree.setChecked(idsEl.value);
			}
		}
	</script>
	</div>
</body>
</html>
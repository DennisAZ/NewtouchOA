<%@ page contentType="text/html;" language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core"  prefix="c"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
	<title>账号列表树</title>
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
        var userTree = new dTree('userTree');//树名与树对象名必须一致
		var rootTxt = '账号列表';
		var rootUrl = '';
		var target = '_self';
		userTree.config.folderLinks=true;
		userTree.config.showRootIcon = false;
		
		switch(type){
			case 'getNames':
				//多选并返回文本
				userTree.config.hasCheckBox = true;
				userTree.config.isReturnName = true;
				break;
		}
		
		if(type == 'sysSet'){
			rootTxt = '授权信息';
			rootUrl = 'userAction.do?op=userManage';
			target = '_parent';
			
			userTree.config.showRootIcon = true;
		}
		if(userTree.config.hasCheckBox)
		 	userTree.config.folderLinks=false;

		//id, pid, name, url, title, target, icon, iconOpen, open	
		userTree.add(0,-1,rootTxt,rootUrl,rootTxt,target);
    </script>
    	<c:if test="${!empty limUsers}">
        	<c:forEach var="user" items="${limUsers}">
            <script type="text/javascript">
                var name='<c:out value="${user.userSeName}"/>';
                var role='<c:out value="${user.limRole.rolName}"/>';
				var part='<c:out value="${user.salOrg.soName}"/>';
				var sex='<c:out value="${user.salEmp.seSex}"/>';
				var empImg;
				var titleName=name;
				if(role!=""||part!=""){
					titleName+="[";
					if(role!=""){
						titleName+=role;
					}
					if(part!=""){
						titleName+="-"+part;
					}
					titleName+="]";
				}
                if(sex=='女'){
                    empImg = 'images/dtree/empnode_w.gif';
                }
                else{
                    empImg = 'images/dtree/empnode_m.gif';
                }
				
                switch(type){
					case 'getNames':
                        url = '';
                        break;
                    case 'sysSet'://账号设置左侧列表
                        url = 'userAction.do?op=getUser&userCode=${user.userCode}';
                        break;
                    default:
                        url = 'javascript:chooseUserComplete(\'${user.userCode}\',\''+name+'\');';
                }
                if(userTree.config.hasCheckBox){
                    userTree.add('${user.userCode}','${user.limUser.userCode}',titleName,url,titleName,target,empImg,empImg,'','${user.userCode}');
                }
                else{	
                    userTree.add('${user.userCode}','${user.limUser.userCode}',titleName,url,titleName,target,empImg,empImg);
                }
            </script>
            </c:forEach>
        </c:if>
    <script type="text/javascript">
		document.write(userTree);
		userTree.openAll();
	    if(userTree.config.hasCheckBox){
			var idsEl;
			if(parent.document.getElementById("popInside")!=null){
				idsEl=parent.document.getElementById("popInside").contentWindow.document.getElementById("nodeIds");
			}
			else{
				idsEl=parent.document.getElementById("nodeIds");
			}
			if(idsEl!=null&&idsEl.value!=""){
				userTree.setChecked(idsEl.value);
			}
		}
	</script>
	</div>
</body>
</html>
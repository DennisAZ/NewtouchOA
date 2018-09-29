<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"></base>
    
    <title></title>
    <link rel="shortcut icon" href="favicon.ico"/>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    

	<script type="text/javascript">
			var ms1='${msg}';//成功返回上一个页面
			var error='${error}';//错误返回当前页面
			var noMsg='${noMsg}'//不弹出信息直接返回上一页
			var suc='${suc}'//返回当前页并刷新
			if(ms1!=null && ms1!=""){
				if(parent!=window){
					if(parent.window.opener!=null){
						try{
						parent.window.opener.history.go(0);
						}
						catch(error){
						}
					}
					parent.window.opener=null;
					parent.window.open("",'_self',""); 
					parent.window.close();
				}
				else{
					if(window.opener!=null){
						try{
						window.opener.history.go(0);
						}
						catch(error){
						}
					}
					window.opener=null;
					window.open("",'_self',""); 
					window.close();
				}
			}
			if(error!=null && error!=""){
				alert(error);
				history.back(-1);
			}
			if(noMsg!=null && noMsg!=""){
				if(window.opener!=null){
					try{
					window.opener.history.go(0);
					}
					catch(error){
					}
				}
				window.opener=null;
				window.open("",'_self',""); 
				window.close();
				/*if(parent.window.opener==null){
					if(window.opener!=null){
						window.opener.history.go(0);
					}
					window.opener=null;
					window.open("",'_self',""); 
					window.close();
				}
				else{
					if(parent.opener!=null){
						parent.opener.history.go(0);
					}
					parent.window.opener=null;
					parent.window.open("",'_self',""); 
					parent.window.close();
				}*/
			}
			if(suc!=null&&suc!=""){
				//history.back(-1);  
 				self.location.reload(); 
			}
	</script>
  </head>
  <body>
    
  </body>
</html>

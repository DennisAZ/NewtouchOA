<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/core/inc/header.jsp" %>
<%@page import="java.util.ArrayList"%>
<%@page import="yh.core.util.file.YHFileUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="yh.core.servlet.YHServletUtility"%>

<head>
<title>yh</title>
<style type="text/css">
ul {
font-size:16px;

}


</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=cssPath %>/views.css" type="text/css" />
<link rel="stylesheet" href="<%=cssPath %>/style.css" type="text/css" />
<script type="text/Javascript" src="<%=contextPath%>/core/js/datastructs.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/sys.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/prototype.js" ></script>
<script type="text/Javascript" src="<%=contextPath%>/core/js/smartclient.js" ></script>
<script language="JavaScript" type="text/javascript">
</script>
</head>
<body>
<h1>YH开发环境搭建</h1>
<ul>
<li>检查d盘下面是否有eclipse和project两个文件夹，如有应先备份这两个文件夹，然后将YHDevelop.zip(\\192.168.0.5\soft\环境搭配里有此文件)文件解压到<span style="color:red">d:\</span>目录下。注：<span style="color:red">解压的目录不可变更。</span></li>
<li>运行d:\project\mysql\bin目录下的registYHMysql.bat以及startYHMysql.bat</li>
<li>打开eclipse，启动tomcat然后在浏览器中输入地址：http://localhost，<br/>
<img  src="../image/login.jpg" style="width:50%;height:50%"><br/>
看到以上登陆界面，说明开发环境搭建成功。<br/>
<img  src="../image/personlist.jpg"><br/>
看到以上界面，说明数据库连接成功。<br/>
</li>
<li>如果登陆界面未打开,请检查80端口,是否被占用，如占用可以修改目录d: \project\yhdev\tomcat\conf下的server.xml文件。

</li>
<li>开发实例,客户端代码片断：</br>
<div>
function doInit() {<br>
&nbsp;&nbsp;//请求服务器端地址对应类及方法：test.YHUserAct.getUserList();<br>
&nbsp;&nbsp;var url = contextPath + "/test/YHUserAct/getUserList.act";<br>
&nbsp;&nbsp;//发送json请求<br>
&nbsp;&nbsp;var json = getJsonRs(url);<br>
&nbsp;&nbsp;//判断json返回状态并判断是否成功。"0"表式成功<br>
 &nbsp;&nbsp; if (json.rtState == "0") {<br>
 &nbsp;&nbsp;&nbsp;&nbsp;//取出返回的结果。<br>
   &nbsp;&nbsp;&nbsp;&nbsp; var data = json.rtData;<br>
   &nbsp;&nbsp;&nbsp;&nbsp; for (var i = 0 ; i < data.length ; i++) {<br>
     &nbsp;&nbsp;&nbsp;&nbsp; var d = data[i];<br>
    &nbsp;&nbsp;&nbsp;&nbsp;  addTr(d , i);<br>
  &nbsp;&nbsp; &nbsp;&nbsp; }<br>
  &nbsp;&nbsp;}<br>
}<br>


</div>
完整代码，请参见:/webroot/yh/demo/personlist.jsp</br>
服务器端代码片断：

<div>
Connection dbConn = null;<br>
    &nbsp;&nbsp;try{<br>
      &nbsp;&nbsp;YHRequestDbConn requestDbConn = (YHRequestDbConn) request<br>
      .getAttribute(YHBeanKeys.REQUEST_DB_CONN_MGR);<br>
     &nbsp;&nbsp; dbConn = requestDbConn.getSysDbConn();<br>
      &nbsp;&nbsp;Statement stm = null;<br>
      &nbsp;&nbsp;ResultSet rs = null;<br>
      &nbsp;&nbsp;StringBuffer sb  = new StringBuffer();<br>
      //拼装json数组串,形式[{userName:'ddd',deptName:'dddd',privName:'dddd'},{userName:'ddd',deptName:'dddd',privName:'dddd'}]
      <br>&nbsp;&nbsp;sb.append("[");<br>
      &nbsp;&nbsp;int count = 0;<br>
      &nbsp;&nbsp;try {<br>
        &nbsp;&nbsp;&nbsp;&nbsp;String query = "select USER_NAME,DEPT_NAME,PRIV_NAME from PERSON,oa_department,USER_PRIV WHERE PERSON.DEPT_ID = DEPARTMENT.SEQ_ID AND PERSON.USER_PRIV = USER_PRIV.SEQ_ID ";
         <br>&nbsp;&nbsp;&nbsp;&nbsp;stm = dbConn.createStatement();
         <br>&nbsp;&nbsp;&nbsp;&nbsp;rs = stm.executeQuery(query);
         <br>&nbsp;&nbsp;&nbsp;&nbsp;while (rs.next()) {<br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String userName = rs.getString("USER_NAME");<br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String deptName = rs.getString("DEPT_NAME");<br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;String privName = rs.getString("PRIV_NAME");<br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sb.append("{");<br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sb.append("userName:'" + userName + "'");<br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sb.append(",deptName:'" + deptName + "'");<br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sb.append(",privName:'" + privName + "'");<br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sb.append("},");<br>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;count++;<br>
        &nbsp;&nbsp;&nbsp;&nbsp;}<br>
      &nbsp;&nbsp;} catch(Exception ex) {<br>
       &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; throw ex;<br>
      &nbsp;&nbsp;&nbsp;&nbsp;} finally {<br>
       &nbsp;&nbsp;&nbsp;&nbsp; YHDBUtility.close(stm, rs, null); <br>
      &nbsp;&nbsp;&nbsp;&nbsp;}<br>
     &nbsp;&nbsp;&nbsp;&nbsp; if (count > 0) {<br>
        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;sb.deleteCharAt(sb.length() - 1);<br>
     &nbsp;&nbsp;&nbsp;&nbsp; }<br>
      //拼装完成<br/>
      &nbsp;&nbsp;&nbsp;&nbsp;
      sb.append("]");<br>
      &nbsp;&nbsp;&nbsp;&nbsp; //json状态设为0,并设置json消息串,以及返回的数据

      &nbsp;&nbsp;&nbsp;&nbsp;request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_OK);<br>
      &nbsp;&nbsp;&nbsp;&nbsp;request.setAttribute(YHActionKeys.RET_MSRG, "成功取出流程父分类数据！");<br>
      &nbsp;&nbsp;&nbsp;&nbsp;request.setAttribute(YHActionKeys.RET_DATA, sb.toString());<br>
    }catch(Exception ex) {<br>
      &nbsp;&nbsp;&nbsp;&nbsp;request.setAttribute(YHActionKeys.RET_STATE, YHConst.RETURN_ERROR);<br>
     &nbsp;&nbsp;&nbsp;&nbsp; request.setAttribute(YHActionKeys.RET_MSRG, ex.getMessage());<br>
      &nbsp;&nbsp;&nbsp;&nbsp;throw ex;<br>
    }<br>
     //如果是前端用的是getJson则返回页面必须是"/core/inc/rtjson.jsp"
    return "/core/inc/rtjson.jsp";<br>
</div>
完整代码，请参见:/src/test/YHUserAct.java</br>
</li>
</ul>
</body>
</html>
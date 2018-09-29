<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <script src="char.js"></script>
<title>Insert title here</title>
</head>
<body>
  <script language="javascript">
   var myChart=new chart(700,400,100,50,'测试统计图','万元');
   myChart.setColWidth(30);
   myChart.addColumn(540,'李' ,'red');
   myChart.addColumn(530,'王','green');
   myChart.addColumn(640,'张','blue');
   myChart.addColumn(730,'刘','#cc3333');
   myChart.addColumn(40,'陈','#3333cc');
   myChart.show();
  </script>
 </body>
</html>

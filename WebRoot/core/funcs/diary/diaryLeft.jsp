<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ include file="/core/inc/header.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" href = "<%=cssPath%>/diary.css">
<link rel="stylesheet" href = "<%=cssPath%>/style.css">
<script type="text/javascript" src="<%=contextPath %>/core/js/prototype.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/tab.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/datastructs.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/sys.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/smartclient.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/Menu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/js/cmp/attachMenu.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryUtil.js"></script>
<script type="text/javascript" src="<%=contextPath %>/core/funcs/diary/js/diaryLogic.js"></script>
<title>left</title>
<script type="text/javascript">
function doInit(){
  doCal();
  showCommentDiary('new_reply');
  showShareDiary('new_share');
}
</script>
</head>
<body onload="doInit()">
<div class="container">
	<div id="left_top">
	  <a class="ToolBtn2" href="<%=contextPath %>/core/funcs/diary/new/index.jsp" target="diaryBody" title="新建日志"><span>新建</span></a>
	  <a class="ToolBtn2" href="<%=contextPath %>/core/funcs/diary/query/index.jsp" target="diaryBody" title="查询日志"><span>查询</span></a>
	  <a class="ToolBtn2" href="<%=contextPath %>/core/funcs/diary/last.jsp" target="diaryBody" title="最新10篇日志"><span>最新</span></a>  
	</div>
  <div id="calendar_table">
     <table width="100%" class="BlockTop">
       <tr>
         <td class="left">
         </td>
         <td class="center" align="center">
          <table cellspacing=0 cellpadding=0 width="100%" border=0>
            <tr>
              <td width="20%" align=right><a class="PrevMonth" href="javascript:setMon(-1);" title="上一月"></a></td>
              <td align=middle width="60%" id="yearMonth" class="YearMonth"></td>
              <td width="20%" align=left><a class="NextMonth" href="javascript:setMon(1);" title="下一月"></a></td>
            </tr>
          </table>
         </td>
         <td class="right">
         </td>
       </tr>
     </table>
     <TABLE id=module_calendar cellSpacing=0 cellPadding=0 width="100%" align=center border=0>
       <TR class=head align=middle>
         <TD>日</TD>
         <TD>一</TD>
         <TD>二</TD>
         <TD>三</TD>
         <TD>四</TD>
         <TD>五</TD>
         <TD>六</TD>
        </TR>
        <TR class=row align=middle>
          <TD id="day0"></TD>
          <TD id="day1"></TD>
          <TD id="day2"></TD>
          <TD id="day3"></TD>
          <TD id="day4"></TD>
          <TD id="day5"></TD>
          <TD id="day6"></TD>
        </TR>
        <TR class=row align=middle>
          <TD id="day7"></TD>
          <TD id="day8"></TD>
          <TD id="day9"></TD>
          <TD id="day10"></TD>
          <TD id="day11"></TD>
          <TD id="day12"></TD>
          <TD id="day13"></TD>
        </TR>
        <TR class=row align=middle>
          <TD id="day14"></TD>
          <TD id="day15"></TD>
          <TD id="day16"></TD>
          <TD id="day17"></TD>
          <TD id="day18"></TD>
          <TD id="day19"></TD>
          <TD id="day20"></TD>
        </TR>
        <TR class=row align=middle>
          <TD id="day21"></TD>
          <TD id="day22"></TD>
          <TD id="day23"></TD>
          <TD id="day24"></TD>
          <TD id="day25"></TD>
          <TD id="day26"></TD>
          <TD id="day27"></TD>
        </TR>
        <TR class=row align=middle>
          <TD id="day28"></TD>
          <TD id="day29"></TD>
          <TD id="day30"></TD>
          <TD id="day31"></TD>
          <TD id="day32"></TD>
          <TD id="day33"></TD>
          <TD id="day34"></TD>
        </TR>
        <TR class=row align=middle>
          <TD id="day35"></TD>
          <TD id="day36"></TD>
          <TD id="day37"></TD>
          <TD id="day38"></TD>
          <TD id="day39"></TD>
          <TD id="day40"></TD>
          <TD id="day41"></TD>
        </TR>
      </TABLE>
  </div>
  
   <table width="100%" class="BlockTop">
     <tr>
       <td class="left">
       </td>
       <td class="center" align="center">
        最新点评
       </td>
       <td class="right">
       </td>
     </tr>
     </table>
<div id="new_reply" style="overflow:auto;" class="new_reply">
</div>
   <table width="100%" class="BlockTop">
     <tr>
       <td class="left">
       </td>
       <td class="center" align="center">
       共享日志
       </td>
       <td class="right">
       </td>
     </tr>
     </table>
<div id="new_share" style="overflow:auto;" class="new_reply">
</div>
</div>
</body>
</html>
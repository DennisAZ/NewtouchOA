<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<br></br>
启动日期：<input onclick="new MyCalendar(event,this)" type="text" readonly name="startDate" id="startDate"></input>
责任人：<input type="text" name="person" name="person" value=""></input>
审核人：<input type="text" name="person2" name="person2" value=""></input>
<table class="lineTable" width="100%" cellspacing="1" cellpadding="3">
  <tr>
    <td>
              审核日期：&nbsp;
      <input onclick="new MyCalendar(event,this)" name="checkDate1" id="checkDate1" type="text" value="" readonly size="10"></input>&nbsp;&nbsp;
              审核人：&nbsp;
      <input name="checkPerson1" id="checkPerson1" type="text" value="" size="10"></input>&nbsp;&nbsp;
    </td>
  </tr>
  <tr>
    <td>
              审核项目：&nbsp;
              代码规范、工作模式、工作完成情况、是否解决未知问题、是否有突破难点
    </td>
  </tr>
  <tr>
    <td>
              审核意见：&nbsp;
      <textarea name="checkDesc1" id="checkDesc1" cols="70" rows="2"></textarea>
    </td>
  </tr>
  <tr>
    <td>
              总体评价：&nbsp;&nbsp;&nbsp;
               小红花<input type="radio" name="result1" id="result1" value="0"></input>&nbsp;&nbsp;
               掌声<input type="radio" name="result1" id="result1" value="1"></input>&nbsp;&nbsp;
                咖啡<input type="radio" name="result1" id="result1" value="2"></input>&nbsp;&nbsp;
                辛苦<input type="radio" name="result1" id="result1" value="3"></input>&nbsp;&nbsp;
                警告<input type="radio" name="result1" id="result1" value="4"></input>&nbsp;&nbsp;
    </td>
  </tr>
</table>

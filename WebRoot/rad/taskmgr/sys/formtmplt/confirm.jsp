<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<br></br>
责任人：<input type="text" name="person" name="person" value=""></input>
互测人：<input type="text" name="person1" name="person1" value=""></input>
验收人：<input type="text" name="person2" name="person2" value=""></input>
<table class="lineTable" width="100%" cellspacing="1" cellpadding="3">
  <tr>
    <th width="60%" align="center" style="font-weight:bold;">
     项目           
    </th>
    <th width="5%" align="center" style="font-weight:bold;">
      未确认
    </th>
    <th width="5%" align="center" style="font-weight:bold;">
      确认
    </th>
    <th width="5%" align="center" style="font-weight:bold;">
     无该项
    </th>
    <th width="5%" align="center" style="font-weight:bold;">
     互测 
    </th>
    <th width="5%" align="center" style="font-weight:bold;">
     验收
    </th>
  </tr>
  <tr>
    <td>
     代码排版： 2空格缩进、不使用Tab、运算符左右留空格
    </td>
    <td align="center">
      <input type="radio" checked name="b0" id="b0" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="b0" id="b0" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="b0" id="b0" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkb00" id="checkb00" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkb01" id="checkb01" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     数据库命名，已经设置SEQ_ID、字段均大写、下划线
    </td>
    <td align="center">
      <input type="radio" checked name="b1" id="b1" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="b1" id="b1" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="b1" id="b1" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkb10" id="checkb10" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkb11" id="checkb11" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     Java命名：包名小于10字符、YH、YHI、Act、Logic前缀
    </td>
    <td align="center">
      <input type="radio" checked name="b2" id="b2" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="b2" id="b2" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="b2" id="b2" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkb20" id="checkb20" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkb21" id="checkb21" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     测试用例已经完成
    </td>
    <td align="center">
      <input type="radio" checked name="b3" id="b3" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="b3" id="b3" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="b3" id="b3" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkb30" id="checkb30" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkb31" id="checkb31" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     已经书写使用文档：应用场景介绍；接口，包括配置参数接口，构造函数、方法接口；典型案例，对应于使用场景
    </td>
    <td align="center">
      <input type="radio" checked name="b4" id="b4" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="b4" id="b4" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="b4" id="b4" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkb40" id="checkb40" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkb41" id="checkb41" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     功能需求都逐一确认过
    </td>
    <td align="center">
      <input type="radio" checked name="d0" id="d0" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="d0" id="d0" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="d0" id="d0" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check00" id="check00" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check01" id="check01" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     非功能需求都已经确认过
    </td>
    <td align="center">
      <input type="radio" checked name="d1" id="d1" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="d1" id="d1" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="d1" id="d1" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check10" id="check10" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check11" id="check11" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     界面布局没有明显瑕疵     
    </td>
    <td align="center">
      <input type="radio" checked name="d2" id="d2" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="d2" id="d2" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="d2" id="d2" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check20" id="check20" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check21" id="check21" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     页面颜色没有明显瑕疵         
    </td>
    <td align="center">
      <input type="radio" checked name="d3" id="d3" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="d3" id="d3" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="d3" id="d3" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check30" id="check30" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check31" id="check31" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     客户端校验已经逐一确认，必填、长度、数值、格式
    </td>
    <td align="center">
      <input type="radio" checked name="d4" id="d4" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="d4" id="d4" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="d4" id="d4" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check40" id="check40" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check41" id="check41" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     删除已经弹出确认框  
    </td>
    <td align="center">
      <input type="radio" checked name="d5" id="d5" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="d5" id="d5" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="d5" id="d5" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check50" id="check50" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check51" id="check51" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     已经检查编码重复   
    </td>
    <td align="center">
      <input type="radio" checked name="d6" id="d6" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="d6" id="d6" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="d6" id="d6" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check60" id="check60" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check61" id="check61" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     数据提取、绑定、缺省值已经确认   
    </td>
    <td align="center">
      <input type="radio" checked name="d7" id="d7" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="d7" id="d7" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="d7" id="d7" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check70" id="check70" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check71" id="check71" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
     数据展示格式：金额、日期已经确认  
    </td>
    <td align="center">
      <input type="radio" checked name="d8" id="d8" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="d8" id="d8" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="d8" id="d8" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check80" id="check80" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check81" id="check81" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
    数据库表级联更新、删除已经确认
    </td>
    <td align="center">
      <input type="radio" checked name="d9" id="d9" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="d9" id="d9" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="d9" id="d9" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check90" id="check90" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="check91" id="check91" value="1"></input>
    </td>
  </tr>
  <tr>
    <td>
    数据列表：翻页、样式、事件、筛选，排序已经处理
    </td>
    <td align="center">
      <input type="radio" checked name="dA" id="dA" value="0"></input>
    </td>
    <td align="center">
      <input type="radio" name="dA" id="dA" value="1"></input>
    </td>
    <td align="center">
      <input type="radio" name="dA" id="dA" value="2"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkA0" id="checkA0" value="1"></input>
    </td>
    <td align="center">
      <input type="checkbox" name="checkA1" id="checkA1" value="1"></input>
    </td>
  </tr>
</table>
<br></br>
任务完成情况简评：<br></br>
<textarea cols="40" rows="4" id="taskResultDesc" name="taskResultDesc"></textarea><br></br>
任务完成情况总体评价<br></br>
勋章<input type="radio" name="taskResult" id="taskResult" value="0"></input>&nbsp;&nbsp;
奖章<input type="radio" name="taskResult" id="taskResult" value="1"></input>&nbsp;&nbsp;
鼓励<input type="radio" name="taskResult" id="taskResult" value="2"></input>&nbsp;&nbsp;
通过<input type="radio" name="taskResult" id="taskResult" value="3"></input>&nbsp;&nbsp;
反省<input type="radio" name="taskResult" id="taskResult" value="4"></input>&nbsp;&nbsp;
<hr></hr>
互测情况简评：<br></br>
<textarea cols="40" rows="4" id="chekResultDesc" name="chekResultDesc"></textarea><br></br>
互测情况总体评价<br></br>
非常负责<input type="radio" name="chekResult" id="chekResult" value="0"></input>&nbsp;&nbsp;
较负责<input type="radio" name="chekResult" id="chekResult" value="1"></input>&nbsp;&nbsp;
基本负责<input type="radio" name="chekResult" id="chekResult" value="2"></input>&nbsp;&nbsp;
应付<input type="radio" name="chekResult" id="chekResult" value="3"></input>&nbsp;&nbsp;
没执行<input type="radio" name="chekResult" id="chekResult" value="4"></input>&nbsp;&nbsp;
<hr></hr>
完成日期：<input onclick="new MyCalendar(event,this)" readonly type="text" name="doneDate" name="doneDate" value=""></input>
验收日期：<input onclick="new MyCalendar(event,this)" readonly type="text" name="confirmDate" name="confirmDate" value=""></input><br></br>
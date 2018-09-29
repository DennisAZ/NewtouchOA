<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<table width="95%" class="TableBlock" align="center">
    <tr>
      <td class="TableContent">模块：</td>
      <td class="TableData">
        <input type="text" name="hmodule" id="hmodule" class="BigInput">
      </td>
    </tr>   
    <tr>
      <td class="TableContent">状态：</td>
      <td class="TableData">
         <input type="radio" name="status" id="status1" value="1">必选
         <input type="radio" name="status" id="status2" value="2">可选         <input type="radio" name="status" id="status0" value="0">停用
      </td>
    </tr>   
    <tr>
      <td class="TableContent">名称：</td>
      <td class="TableData">
        <input type="text" name="hname" id="hname" class="BigInput">
      </td>
    </tr>
    <tr>
      <td class="TableContent">描述：</td>
      <td class="TableData">
        <input type="text" name="hdesc" id="hdesc" class="BigInput">
      </td>
    </tr>
    <tr>
      <td class="TableData" colspan="2" align="center">
        <input type="button" value="确定" class="BigButton" onClick="checkForm()">
        <input type="button" onClick="window.close()" class="BigButton" value="关闭">
      </td>
    </tr>
    </table>

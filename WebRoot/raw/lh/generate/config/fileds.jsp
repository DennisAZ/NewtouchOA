<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="f" style="z-index=1" >
  <input type="button" value="刷新数据" onclick="refrcahData()" class="BigButton">
  <table>
    <tr>
      <td>
        <table id = "fieldTab">
          <tr class = "TableHeader ">
            <th>字段名</th><th>显示名称</th><th>显示宽度</th><th>是否显示</th><th>是否必填</th><th>显示控件类型</th>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  <input type="hidden" name="filedsVal" id="filedsVal">
  <!--<input type="button" value="确认字段信息" onclick="inputs('filedsVal','fieldTab')" class="BigButton">  -->
</div>

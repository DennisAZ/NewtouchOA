<div id="f" >
  <input type="button" value="刷新数据" onclick="refrcahData()">
    <table id = "fieldTab">
      <tr class = "TableHeader ">
        <th>字段名</th><th>显示名称</th><th>显示宽度</th><th>是否显示</th><th>是否必填</th>
      </tr>
    </table>
    <input type="hidden" name="filedsVal" id="filedsVal">
    <input type="button" value="确认字段信息" onclick="inputs('filedsVal','fieldTab')">
</div>

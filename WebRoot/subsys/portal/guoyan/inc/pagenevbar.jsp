<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="pgTopPanel">
<div class="pgPanel">
  <div>
             每页<select id="selectPageSize"></select>条
  </div>
  <div class="separator"></div>
  <div id="btnPgFirst" title="第一页" class="pgBtn pgFirst pgFirstDisabled"></div>
  <div id="btnPgPre" title="前一页" class="pgBtn pgPrev pgPrevDisabled"></div>
  <div class="separator"></div>
  <div>
           第<input name="pageIndex" id="pageIndex" onkeyup="value=value.replace(/[^\d]/g,'')" onbeforepaste="clipboardData.setData('text',clipboardData.getData('text').replace(/[^\d]/g,''))"  type="text" title="" value="1" size="5" class="SmallInput pgCurrentPage"> 页 / 共
    <span id="pageCount" class="pgTotalPage"></span> 页
  </div>
  <div class="separator"></div>
  <div title="下一页" id="btPgNext" class="pgBtn pgNext pgNextDisabled"></div>
  <div title="最后一页" id="btnPgLast" class="pgBtn pgLast pgLastDisabled"></div>
  <div class="separator"></div>
  <div id="btnRefresh" title="刷新" class="pgBtn pgRefresh"></div>
  <div class="separator"></div>
  <div id="pgSearchInfo" class="pgSearchInfo"></div>
</div>
<table id="dataTable" border="0" class="TableList pgTable" style="display:none">
  <tr id="dataHeader" class=TableTr>
  </tr>
</table>
</div>
<table id="pgMsrgPanel" class="MessageBox" align="center" width="300">
  <tr>
    <td class="msg info">
              没有查找到数据！
    </td>
  </tr>
</table>
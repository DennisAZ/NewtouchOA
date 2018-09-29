<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%><div>
  <form id="selectFilter">
   <table cellscpacing="1" cellpadding="3" width="450">
   <tr>
     <td>控件ID</td>
     <td><input type="text" id="field_name" name="cntrlId" class="SmallInput"></input></td>
   </tr>
   <tr>
     <td>关联的数据表名</td>
     <td><input type="text" name="tableName" value="CODE_ITEM" class="SmallInput"></input></td>
   </tr>
   <tr>
     <td>关联的编码字段</td>
     <td><input type="text" name="codeField" value="CLASS_CODE" class="SmallInput"></input></td>
   </tr>
   <tr>
     <td>关联的描述字段</td>
     <td><input type="text" name="nameField" value="CLASS_DESC" class="SmallInput"></input></td>
   </tr>
   <tr>
     <td>关联条件字段</td>
     <td><input type="text" name="filterField" value="CLASS_NO" class="SmallInput"></input></td>
   </tr>
   <tr>
     <td>关联条件值</td>
     <td><input type="text" name="filterValue" value="S03" class="SmallInput"></input></td>
   </tr>
   <tr>
     <td>排序字段</td>
     <td><input type="text" name="order" value="CLASS_CODE" class="SmallInput"></input></td>
   </tr>
   <tr>
     <td>reloadBy</td>
     <td><input type="text" name="reloadBy" value="" class="SmallInput"></input></td>
   </tr>
   <tr>
     <td>actionUrl</td>
     <td><input type="text" name="actionUrl" value="" class="SmallInput"></input></td>
   </tr>
   </table>
   </form>
   <input type="button" value="保存"   onclick="Hidediv()" class="ArrowButton"></input>
</div>

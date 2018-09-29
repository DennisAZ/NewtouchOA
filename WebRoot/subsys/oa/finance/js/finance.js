//未报销
function expenseTr(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行
  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.align='center';
  mynewcell.innerHTML = " <input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列
  mynewcell.colSpan="9";
  mynewcell.innerHTML = "全选&nbsp;<input type='button'  class='BigButton' value='  批量打印  ' onClick='printMail();' title='批量打印'> &nbsp;"
    + "<input type='button'  class='BigButton' value=' 作废处理  ' onClick='makeWaste();' title='作废标记'> &nbsp;";
}
//已报销
function expense2Tr(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行

  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.align='center';
  mynewcell.innerHTML = " <input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.colSpan="9";
  mynewcell.innerHTML = "全选&nbsp;<input type='button'  class='BigButton' value='  批量打印  ' onClick='printMail();' title='批量打印'> &nbsp;"
    + "<input type='button'  class='BigButton' value=' 报销处理  'onClick='reimbursemenMail();' title='报销状态'> &nbsp;"
    + "<input type='button'  class='BigButton' value=' 作废处理  ' onClick='makeWaste();' title='作废标记'> &nbsp;";
}
//作废处理
function expense3Tr(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行

  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.align='center';
  mynewcell.innerHTML = " <input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.colSpan="9";
  mynewcell.innerHTML = "全选&nbsp;<input type='button'  class='BigButton' value='  批量打印  ' onClick='printMail();' title='批量打印'> &nbsp;"
    + "<input type='button'  class='BigButton' value=' 报销处理  'onClick='reimbursemenMail();' title='报销状态'> &nbsp;";
}
//支出
function chequeTr(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行

  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.align='center';
  mynewcell.innerHTML = " <input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.colSpan="9";
  mynewcell.innerHTML = "全选&nbsp;<input type='button'  value=' 批量打印 ' class='BigButton' onClick='printMail();' title='批量打印'>";
}

//预算信息
function budgetTr(table){
  var currRowIndex = table.rows.length;
  var mynewrow = table.insertRow(currRowIndex);//新建一行

  mynewrow.className = "TableControl";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.align='center';
  mynewcell.innerHTML = " <input type='checkbox' name='allbox' id='allbox' onClick='checkAll();'>";
  var cellnum = mynewrow.cells.length;
  var mynewcell=mynewrow.insertCell(cellnum);//新建一列

  mynewcell.colSpan="9";
  mynewcell.innerHTML = "全选&nbsp;<input type='button'  class='BigButton' value='  批量打印  ' onClick='printMail();' title='批量打印'> &nbsp;"
    + "<input type='button'  class='BigButton' value=' 报销处理  'onClick='reimbursemenMail();' title='报销状态'> &nbsp;";
}
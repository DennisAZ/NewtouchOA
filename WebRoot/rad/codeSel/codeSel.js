function selectCode(options) {
  var child;
  var sort = options.sort;
  var tableNo = options.tableNo;
  var codeFieldNo = options.codeFieldNo;
  var nameFieldNo = options.nameFieldNo;
  var codeField = options.codeField;
  var nameField = options.nameField;
  var filterField = options.filterField;
  var filterValue = options.filterValue;
  var currValue = options.currValue;// 全选反选值  var orderBy = options.orderBy;
  // 传值 1.window对象 2.数据对象/或者grid对象
  var hd = [ [ {
    header : codeFieldNo,
    name : nameFieldNo
    ,width: 200
  }, {
    header : codeField,
    name : nameField
    ,width: 200
  } ] 
  ,{
    header:"操作",
    oprates:[
         new YHOprate('选择',true,function(record,index){
           setValue(record.getField('seqId').value, record.getField('userName').value);
           //document.getElementById("pre3").value = record.getField('tableNo').value;
           this.close();
           })
       ], width:50}];
  var url = "/yh/yh/rad/grid/act/YHGridNomalAct/jsonTest.act?tabNo=" + tableNo
      + "&orderBy=" + orderBy;
  var arg = [ window, hd, url, filterField, filterValue, currValue ];
  if (!sort) {
    // 默认
  } else if (sort == "1") {
    // 单选情况
    //openDialog('/yh/rad/codeSel/indexSingleG.jsp', );
    child = window.showModalDialog(
            '/yh/rad/codeSel/indexSingleG.jsp',
            arg,
            'dialogWidth:500px;scroll:auto;dialogHeight:350px;help:no;directories:no;location:no;menubar:no;resizeable:no;status:no;toolbar:no;');
  } else if (sort == "2") {
    // 多选情况
    child = window.showModalDialog(
            '/yh/rad/codeSel/indexComplexG.jsp',
            arg,
            'dialogWidth:450px;scroll:auto;dialogHeight:340px;help:no;directories:no;location:no;menubar:no;resizeable:no;status:no;toolbar:no;');
  } else {
    alert("暂不支持此sort ：" + sort);
  }

}

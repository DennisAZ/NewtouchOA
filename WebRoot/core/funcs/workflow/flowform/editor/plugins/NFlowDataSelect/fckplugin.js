//得到控件对象
var FCKObject = plugins({comName:"NFlowDataSelect"
  ,item:"NFlowDataSelect"
  ,page:"nflowdataselect.jsp"
  ,inco:"data.gif"
  ,width:500
  ,height:350
  ,dlgTitle:"流程数据选择控件属性"
  ,btnTitle:"流程数据选择控件"
});
//为控件对象注测行为
//注册add 方法
function add(code){
  FCK.InsertHtml(code);
}
FCKObject.RegisterAction('Add',add);
//注册右键菜单命令
addMenuItem(new Object() , "deleteFlowDataSelect" , deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object() ;
DSMenuListener.AddItems = function( contextMenu, tag, tagName ){
  if(tagName == 'BUTTON'
    && tag.className == "FLOWFETCH"){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteFlowDataSelect','删除控件');
    contextMenu.AddItem('NFlowDataSelect','流程数据选择控件属性');
  }
}
FCK.ContextMenu.RegisterListener( DSMenuListener ) ;
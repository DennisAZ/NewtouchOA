//得到控件对象
var FCKObject = plugins({comName:"NDataSelect"
  ,item:"NDataSelect"
  ,page:"ndataselect.jsp"
  ,inco:"data.gif"
  ,width:500
  ,height:350
  ,dlgTitle:"数据选择控件属性"
  ,btnTitle:"数据选择控件"
});
//为控件对象注测行为
//注册add 方法
function addDataSelect(code){
  FCK.InsertHtml(code);
 // alert(code);
}
FCKObject.RegisterAction('Add',addDataSelect);
//注册右键菜单命令
addMenuItem(new Object() , "deleteDataSelect" , deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object() ;
DSMenuListener.AddItems = function(contextMenu, tag, tagName){
  if(tagName == 'BUTTON' && tag.className == "DATA"){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NDataSelect','数据选择控件属性');
  }
}
FCK.ContextMenu.RegisterListener(DSMenuListener) ;
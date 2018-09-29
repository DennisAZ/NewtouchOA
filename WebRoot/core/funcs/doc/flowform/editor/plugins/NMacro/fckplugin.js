//得到控件对象
var FCKObject = plugins({comName:"NMacro"
  ,item:"NMacro"
  ,page:"nmacro.jsp"
  ,inco:"nmacro.gif"
  ,width:500
  ,height:350
  ,dlgTitle:"使用宏"
  ,btnTitle:"使用宏"});
//为控件对象注测行为

//注册add 方法
function addTextField(code){
  FCK.InsertHtml(code);
// alert(code);
}
FCKObject.RegisterAction('Add',addTextField);
//注册右键菜单命令
addMenuItem(new Object(), "deleteDataSelect", deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object() ;
DSMenuListener.AddItems = function(contextMenu, tag, tagName){
//alert(tag.type);   //tag是当前控件 控件.type可以获取他的类型
  //if(tag.type == 'text'&& (tag.className != "AUTO")&& (tag.className != "CALC")){
   // contextMenu.AddSeparator();
    //contextMenu.AddItem('deleteDataSelect','删除控件');
   // contextMenu.AddItem('NMacro','宏属性');
  //}
}
FCK.ContextMenu.RegisterListener(DSMenuListener);
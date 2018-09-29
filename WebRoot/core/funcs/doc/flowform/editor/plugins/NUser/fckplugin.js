//得到控件对象
var FCKObject = plugins({comName:"NUser"
  ,item:"NUser"
  ,page:"nuser.jsp"
  ,inco:"user.gif"
  ,width:340
  ,height:230
  ,dlgTitle:"部门人员"
  ,btnTitle:"部门人员"});
//为控件对象注测行为

//注册add 方法
function addUser(code){
  FCK.InsertHtml(code);
// alert(code);
}
FCKObject.RegisterAction('Add',addUser);
//注册右键菜单命令
addMenuItem(new Object() , "deleteDataSelect" , deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object();
DSMenuListener.AddItems = function(contextMenu, tag, tagName){
//alert(tag.type);   //tag是当前控件    tag.type可以获取他的类型
  if(tag && tag.className == 'USER' && tagName == 'IMG'){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NUser','部门人员控件属性');
  }
}
FCK.ContextMenu.RegisterListener(DSMenuListener);
//得到控件对象
var FCKObject = plugins({comName:"NListMenu"
  ,item:"NListMenu"
  ,page:"nlistmenu.jsp"
  ,inco:"listmenu.gif"
  ,width:400
  ,height:400
  ,dlgTitle:"下拉菜单属性"
  ,btnTitle:"下拉菜单"});
//为控件对象注测行为

//注册add 方法
function addSelect(code){
  FCK.InsertHtml(code);
// alert(code);
}
FCKObject.RegisterAction('Add',addSelect);
//注册右键菜单命令
addMenuItem(new Object() , "deleteDataSelect" , deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("");
  }
}
var DSMenuListener = new Object();
DSMenuListener.AddItems = function( contextMenu, tag, tagName ){
//alert(tag.type);   //tag是当前控件 控件.type可以获取他的类型
  //if(tag.className != 'AUTO'&& tagName != 'INPUT'&&tagName == 'SELECT'){
  //alert(tagName);
  if(tag && tagName == 'SELECT' && tag.className != 'AUTO' && tagName != 'INPUT'){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NListMenu','下拉菜单属性');
    if (parent.useInfoResSubsys == '1') {
      contextMenu.AddItem('NMetadata','元数据属性');
    }
  }
}
FCK.ContextMenu.RegisterListener( DSMenuListener ) ;
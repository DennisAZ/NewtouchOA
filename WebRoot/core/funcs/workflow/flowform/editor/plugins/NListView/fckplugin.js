//得到控件对象
var FCKObject = plugins({comName:"NListView"
  ,item:"NListView"
  ,page:"nlistview.jsp"
  ,inco:"listview.gif"
  ,width:480
  ,height:300
  ,dlgTitle:"列表控件"
  ,btnTitle:"列表控件"});
//为控件对象注测行为

//注册add 方法
function addListView(code){
  FCK.InsertHtml(code);
// alert(code);
}
FCKObject.RegisterAction('Add',addListView);
//注册右键菜单命令
addMenuItem(new Object() , "deleteDataSelect" , deleteDs);
function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object() ;
DSMenuListener.AddItems = function(contextMenu, tag, tagName){
//alert(tag.type);   //tag是当前控件 控件.type可以获取他的类型
  if(tagName == 'IMG' && tag.className == 'LIST_VIEW'
    && (tag.className != "AUTO")&& (tag.className != "CALC")){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NListView','列表控件属性');
    if (parent.useInfoResSubsys == '1') {
      contextMenu.AddItem('NMetadata','元数据属性');
    }
  }
}
FCK.ContextMenu.RegisterListener(DSMenuListener) ;
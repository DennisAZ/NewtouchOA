//得到控件对象
var FCKObject = plugins({comName:"NWordSelect"
  ,item:"NWordSelect"
  ,page:"NWordSelect.jsp"
  ,inco:"word.gif"
  ,width:340
  ,height:230
  ,dlgTitle:"主题词选择"
  ,btnTitle:"主题词选择"});
//为控件对象注测行为

//注册add 方法
function addWordSelect(code){
  FCK.InsertHtml(code);
// alert(code);
}
FCKObject.RegisterAction('Add',addWordSelect);
//注册右键菜单命令
addMenuItem(new Object() , "deleteWordSelect" , deleteWordSelect);

function deleteWordSelect(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object();
DSMenuListener.AddItems = function(contextMenu, tag, tagName){
//alert(tag.type);   //tag是当前控件    tag.type可以获取他的类型
  if(tag && tag.className == 'WORDSELECT' && tagName == 'IMG'){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteWordSelect','删除控件');
    contextMenu.AddItem('NWordSelect','主题词选择控件属性');
  }
}
FCK.ContextMenu.RegisterListener(DSMenuListener);
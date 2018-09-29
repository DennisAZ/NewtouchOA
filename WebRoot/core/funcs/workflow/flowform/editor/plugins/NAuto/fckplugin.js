//得到控件对象
var FCKObject = plugins({comName:"NAuto"
  ,item:"NAuto"
  ,page:"nauto.jsp"
  ,inco:"auto.gif"
  ,width:450
  ,height:240
  ,dlgTitle:"宏控件属性"
  ,btnTitle:"宏控件"});
//为控件对象注测行为

//注册add 方法
function addAuto(code){
  FCK.InsertHtml(code);
// alert(code);
}
FCKObject.RegisterAction('Add',addAuto);
//注册右键菜单命令
addMenuItem(new Object() , "deleteDataSelect" , deleteDs);

function deleteDs(){
  if(confirm("确定要删除？")){
    FCK.InsertHtml("") ;
  }
}
var DSMenuListener = new Object();
DSMenuListener.AddItems = function(contextMenu, tag, tagName){
  //alert(tag.className == 'AUTO');
  //alert(tag.type);   //tag是当前控件 控件.type可以获取他的类型
  if(tag && ((tag.className == 'AUTO' && tagName == 'INPUT')
      || (tag.className == 'AUTO'&&tagName == 'SELECT'))){
  //if(tagName == 'INPUT'&& tagName == 'SELECT'){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NAuto','宏控件属性');
    if (parent.useInfoResSubsys == '1') {
      contextMenu.AddItem('NMetadata','元数据属性');
    }
  }
}
FCK.ContextMenu.RegisterListener(DSMenuListener);
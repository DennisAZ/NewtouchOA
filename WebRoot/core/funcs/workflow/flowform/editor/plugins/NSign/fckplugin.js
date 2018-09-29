//得到控件对象
var FCKObject = plugins({comName:"NSign"
  ,item:"NSign"
  ,page:"sign.jsp"
  ,inco:"sign.gif"
  ,width:400
  ,height:240
  ,dlgTitle:"签章属性"
  ,btnTitle:"签章控件"});
//为控件对象注测行为

//注册add 方法
function addSign(code){
  FCK.InsertHtml(code);
// alert(code);
}
FCKObject.RegisterAction('Add',addSign);
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
  if(tagName == 'IMG' && tag.className == "SIGN"){
  //if(tagName == 'INPUT'&&tagName == 'SELECT'){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NSign','签章控件属性');
  }
}
FCK.ContextMenu.RegisterListener(DSMenuListener);
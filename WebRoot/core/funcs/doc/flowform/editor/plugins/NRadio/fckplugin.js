//得到控件对象
var FCKObject = plugins({comName:"NRadio"
  ,item:"NRadio"
  ,page:"nradio.jsp"
  ,inco:"radio.gif"
  ,width:340
  ,height:230
  ,dlgTitle:"单选组件"
  ,btnTitle:"单选组件"});
//为控件对象注测行为


//注册add 方法
function NRadio(code){
  FCK.InsertHtml(code);
}
FCKObject.RegisterAction('Add',NRadio);
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
  if(tag && tag.className == 'RADIO' && tagName == 'IMG'){
    contextMenu.AddSeparator();
    contextMenu.AddItem('deleteDataSelect','删除控件');
    contextMenu.AddItem('NRadio','单选组件属性');
  }
}
FCK.ContextMenu.RegisterListener(DSMenuListener);
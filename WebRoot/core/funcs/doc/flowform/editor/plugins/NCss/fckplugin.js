//得到控件对象

var seqId = parent.seqId;
var FCKObject = plugins({comName:"NCss"
  ,item:"NCss"
  ,page:"index.jsp?seqId=" + seqId
  ,inco:"ncss.gif"
  ,width:500
  ,height:400
  ,dlgTitle:"CSS样式扩展"
  ,btnTitle:"CSS样式扩展"});
//为控件对象注测行为

//注册add 方法
function addCss(code){
  FCK.InsertHtml(code);
// alert(code);
}
FCKObject.RegisterAction('Add',addCss);
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
  //if((tag.className == 'AUTO' && tagName == 'INPUT') || (tag.className == 'AUTO'&&tagName == 'SELECT')){
 //   contextMenu.AddSeparator();
 //   contextMenu.AddItem('deleteDataSelect','删除控件');
 //   contextMenu.AddItem('NCss','宏控件属性');
 // }
}
FCK.ContextMenu.RegisterListener(DSMenuListener);
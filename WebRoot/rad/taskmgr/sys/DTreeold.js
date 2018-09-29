var nodeJsonObj = {
  
    
    
}
function MyTree(rootNodeId,requestUrl,treeStructure,checkboxPara,linkPara){
  var minus = imgPath + "/dtree/minus.gif";
  var blank = imgPath + "/dtree/blank.gif";
  var item = imgPath + "/dtree/folder.gif";
  var plus = imgPath + "/dtree/plus.gif";
  var loading = imgPath + "/dtree/loading.gif";
var isNoTree = treeStructure.isNoTree;
var regular = new Array();
if(isNoTree){
  regular = treeStructure.regular.split(",");
  if(regular.length <1){
  alert("你指定的编码规则不对");
  return;
  }
}
//初始化
 
  var rootUl =  document.createElement("ul");
  
  rootUl.setAttribute("id", "ul-0");
  if(rootNodeId==""||rootNodeId == null){
    document.body.appendChild(rootUl);
  }else{
    var rootNode = $(rootNodeId);
    if(rootNode==null){
      alert("没有你指定的结点");
      return;
    }
    rootNode.appendChild(rootUl);
  }
  
  if(!isNoTree)
  nodeLoad(0);
  else
  { 
    nodeLoad(0, regular[0]);
  }
    
///-------


function nodeLoad(parentId,length){
  
  var parentNode = $("ul-"+parentId);
  //首先判断父结点的子结点是否已经加载,即判断是否含有子结点
  //如没有加载,就加载,如已经加载直接显隐,同时设置为+,或者为-
  var parentImage = $("img1-"+parentId);
  if(parentNode.hasChildNodes()){
        if(parentNode.style.display=="none"){
          parentImage.setAttribute("src", minus);
        parentNode.style.display="";
      }else{
        parentImage.setAttribute("src", plus);
        parentNode.style.display="none";
      }
    }else{
      
      getNodes(parentId, length,false);
    }
}
function getNodes(parentId,length,flag){
  getNodesOnLoading(parentId);
  var url = "";
  if(isNoTree){
    if(parentId == 0){
      url =  requestUrl;
    }else{
      url =  requestUrl + parentId;
    }
    url = url+ "&length="+length;
  }
  else{
    url =  requestUrl + parentId;
  }
  new Ajax.Request(url,{
    method:'get',
    onLoaded:function(){
        getNodesLoaded(parentId);
      },
      onComplete:function(response){
            getNodesResponse(response,flag);
     }
  });
  
}
function getNodesOnLoading(parentId){
  var parentNode = $("ul-"+parentId);
  parentNode.style.display = "";
  var img = document.createElement("img");
  img.setAttribute("id","img-"+parentId);
  img.setAttribute("src",  loading);
  parentNode.appendChild(img);
}
getNodesLoaded = function(parentId){
  var parentNode = $("ul-"+parentId);
  var img = $("img-"+parentId);
  parentNode.removeChild(img);
}
function getNodesResponse(response,flag){
  //alert(flag);
  if(response.readyState==4){
    if(response.status==200){
      var count = response.responseXML.getElementsByTagName("count")[0].firstChild.data;
      var parentNodeId = 0;
      
      if(response.responseXML.getElementsByTagName("parentNodeId")[0].hasChildNodes()){
        parentNodeId =  response.responseXML.getElementsByTagName("parentNodeId")[0].firstChild.data;
        if(parentNodeId==""){
          parentNodeId = 0;
        }
      }
      
      var parentNode = $("ul-"+parentNodeId);
      //去掉加载图标
      if(parentNode.hasChildNodes()){
        parentNode.removeChild(parentNode.firstChild);
      }
      
      if(count>0){
        var menu = response.responseXML.getElementsByTagName("menu");
        for(var i=0; i<menu.length; i++){
          var m = menu[i];
          //取得数据后,遍历整个NODE列表
          //单个li;
          var nodeId = m.getElementsByTagName("id")[0].firstChild.data;
          var name = m.getElementsByTagName("name")[0].firstChild.data;
          var isHaveChild = m.getElementsByTagName("isHaveChild")[0].firstChild.data;
          //=1为有子结点,=0为没有子结点
          var has_child = (isHaveChild==1?true:false);
          
          var li = document.createElement("li");
          li.setAttribute("id", nodeId);
          
          var img1 = document.createElement("img");
          if(has_child){
            img1.setAttribute("id", "img1-"+nodeId);
            img1.setAttribute("src",  plus);
            img1.onclick=function(){
              var id = this.id.substr(5);
              var length = id.length;
              var j = 0;
              //var regular = treeStructure.regular.split(",");
              for(var i=0;i<regular.length;i++){
                j=parseInt(j)+parseInt(regular[i]);
                if(length==j){
                  length = parseInt(j)+parseInt(regular[i+1]);
                  break;
                }
              }
              nodeLoad(id,length);
            };
          }else{
            img1.setAttribute("src",   blank);
          }
          
          var img2 = document.createElement("img");
          img2.setAttribute("src",   item);
          if(has_child){
            img2.setAttribute("id", "img2-"+nodeId);
            img2.onclick=function(){
              var id = this.id.substr(5);
              var length = id.length;
              var j = 0;
              //var regular = treeStructure.regular.split(",");
              for(var i=0;i<regular.length;i++){
                j=parseInt(j)+parseInt(regular[i]);
                if(length==j){
                  length = parseInt(j)+parseInt(regular[i+1]);
                  break;
                }
              }
              nodeLoad(id,length);
            };
          }
          if(checkboxPara.isHaveCheckbox){
          var checkbox = document.createElement("input");
          checkbox.setAttribute("id", "checkbox-"+nodeId);
          checkbox.setAttribute("type", "checkbox");
          checkbox.onclick = function(){
            var id = this.id.substr(9);
            var node = $(id);
            if(this.checked){
              checkboxPara.checkedFun(node.id);
              setAllChildCheckboxChecked(node);
              setParentNodeCheckbox(this);
            }else{
               checkboxPara.disCheckedFun(node.id);
               setAllChildCheckBoxDisChecked(node);
               setParentNodeDisCheckbox(this);
            }
            
          }
          }
          var a = document.createElement("a");
          a.setAttribute("id","a-"+nodeId);
          //alert(linkPara.clickFunc);
          if(linkPara.clickFunc){
            $(a).observe('click',linkPara.clickFunc.bind(window,nodeId));
            a.setAttribute("href", "#");
          } else if(linkPara.isHaveLink&&linkPara.linkAddress!=null&&linkPara.linkAddress!=""){
            if (linkPara.linkAddress.indexOf("javascript:") < 0) {
              a.setAttribute("href", linkPara.linkAddress+nodeId);
              if(linkPara.target==null||linkPara.target==""){
                linkPara.target = '_blank';
              }
              a.setAttribute('target',linkPara.target);
            }else {
            }
          }else{
            a.setAttribute("href", "#");
          }
          if(has_child){
            //a.setAttribute("href", "#");
            a.onclick=function(){ 
              var id = this.id.substr(2);
              var length = id.length;
              var j = 0;
              //var regular = treeStructure.regular.split(",");
              for(var i=0;i<regular.length;i++){
                j=parseInt(j)+parseInt(regular[i]);
                if(length==j){
                  length = parseInt(j)+parseInt(regular[i+1]);
                  break;
                }
              }
              nodeLoad(id,length);
            };  
          }
          var text = document.createTextNode(name);
          a.appendChild(text);
          
          li.appendChild(img1);
          li.appendChild(img2);
          if(checkboxPara.isHaveCheckbox){
            li.appendChild(checkbox);
          }
          li.appendChild(a);
          if(has_child){
            var ul = document.createElement("ul");
            ul.setAttribute("id", "ul-"+nodeId);
            ul.style.display = "none";
            li.appendChild(ul);
          }
          parentNode.appendChild(li);
          
          //显隐,当不是根结点时,就没有img这个标签,因此去掉
          if(parentNodeId != 0){
            var parentImage = $("img1-"+parentNodeId);
            parentImage.setAttribute("src",  minus);
          }
        }
        if(checkboxPara.isHaveCheckbox){
        if(flag){
          var lis = parentNode.getElementsByTagName("li");
          var nodes = parentNode.getElementsByTagName("input");
          for(var i = 0 ;i < nodes.length ;i++){
            var n = nodes[i];
            n.checked = true;
            checkboxPara.checkedFun(n.id.substr(9));
            
          }
          for(var i = 0 ;i<lis.length; i++){
            var li1 = lis[i];
            setAllChildCheckboxChecked(li1);
          }
        }
        }
        
        }
    }
    else if(response.status==404){
      alert("404 错误");
    }
    else if(response.status==403){
      alert("403 错误");
    }
    else if(response.status==401){
      alert("401 错误");
    }
    else {
      alert("未知错误");
  }
  }
  }
function addNode(nodeJsonObj){
  
  
}
function setAllChildCheckboxChecked(node){
  var id = node.id;
  var ul = $("ul-"+id);
  if(ul!=null){
    //说明已经加载,直接展开,没有加载则加载 ,然后....
    if(ul.hasChildNodes()){
      var parentImage = $("img1-"+id);
       parentImage.setAttribute("src",  minus);
      ul.style.display = "";
      
      var lis = ul.getElementsByTagName("li");
      var nodes = ul.getElementsByTagName("input");
      
      for(var i = 0 ;i < nodes.length ;i++){
        var n = nodes[i];
        
        n.checked = true;
        checkboxPara.checkedFun(n.id.substr(9));
      }
      
      for(var i = 0 ;i<lis.length; i++){
        var li = lis[i];
        setAllChildCheckboxChecked(li);
      }
    }else{
      var length = id.length;
      var j = 0;
      //var regular = treeStructure.regular.split(",");
      for(var i=0;i<regular.length;i++){
        j=parseInt(j)+parseInt(regular[i]);
        if(length==j){
          length = parseInt(j)+parseInt(regular[i+1]);
          break;
        }
      }
      getNodes(id, length, true);
    }
    
  }
}
function setAllChildCheckBoxDisChecked(node){
  var nodes = node.getElementsByTagName("input");
  for(var i = 0 ;i < nodes.length ;i++){
    var n = nodes[i];
    if(n.checked != false){
      n.checked=false;
      checkboxPara.disCheckedFun(n.id.substr(9));
    }
  }
}
function setParentNodeCheckbox(node){
  var id = node.id.substr(9);
  var li = $(id);
  var parentlu = li.parentNode;
  id = parentlu.id.substr(3);
  if(id==0){
    return ;
  }
  var parentCheckbox = $("checkbox-"+id);
  if(parentCheckbox.checked){
    return;
  }
  parentCheckbox.checked = true;
  checkboxPara.checkedFun(id);
  setParentNodeCheckbox(parentCheckbox);
}
function setParentNodeDisCheckbox(node){
  var id = node.id.substr(9);
  li = $(id);
  var parentlu = li.parentNode;
  id = parentlu.id.substr(3);
  if(id==0){
    return ;
  }
  var lis = parentlu.childNodes;

  for(var i = 0 ;i < lis.length ;i++ ){
    li = lis[i];
    var checkbox =  $("checkbox-"+li.id);
    if(checkbox.checked){
      return ;
    }
  }
  var parentCheckbox = $("checkbox-"+id);
  parentCheckbox.checked = false;
  checkboxPara.disCheckedFun(id);
    setParentNodeDisCheckbox(parentCheckbox);
}


}

function OnceLoadTree(rootNodeId,requestUrl,treeStructure,checkboxPara,linkPara){
  
  var minus = imgPath + "/dtree/minus.gif";
  var blank = imgPath + "/dtree/blank.gif";
  var item = imgPath + "/dtree/folder.gif";
  var plus = imgPath + "/dtree/plus.gif";
  var loading = imgPath + "/dtree/loading.gif";
  
  //-----初始化-----------
  var isNoTree = treeStructure.isNoTree;
  var regular = new Array();
  if(isNoTree){
    regular = treeStructure.regular.split(",");
    if(regular.length <1){
    alert("你指定的编码规则不对");
    return;
    }
  }
  //初始化

  var rootUl =  document.createElement("ul");
  
  rootUl.setAttribute("id", "ul-0");
  if(rootNodeId==""||rootNodeId == null){
    document.body.appendChild(rootUl);
  }else{
    var rootNode = $(rootNodeId);
    if(rootNode==null){
      alert("没有你指定的结点");
      return;
    }
    rootNode.appendChild(rootUl);
  }
  
  if(!isNoTree)
  nodeLoad(0);
  else
  { 
    nodeLoad(0, regular[0]);
  }
    
//-------------------------
  function nodeLoad(parentId,length){
    var parentNode = $("ul-"+parentId);
    //首先判断父结点的子结点是否已经加载,即判断是否含有子结点
    //如没有加载,就加载,如已经加载直接显隐,同时设置为+,或者为
    if(parentNode.hasChildNodes()){
        setNodeShowOrHide(parentId);
      }else{
        getNodesOnLoading(parentId);
        var url = "";
        if(isNoTree){
          if(parentId == 0){
            url =  requestUrl;
          }else{
            url =  requestUrl + parentId;
          }
          url = url+ "&length="+length;
        }
        else{
          url =  requestUrl + parentId;
        }
        
        new Ajax.Request(url,{
          method:'get',
          onLoaded:function(){
              getNodesLoaded(parentId);
            },
            onComplete:function(response){
                  getNodesResponse(response);
           }
        });
      }
    
  }
  function setNodeShowOrHide(id){
    var node = $("ul-"+id);
    var image = $("img1-"+id);
     if(node.style.display=="none"){
          image.setAttribute("src",minus);
        node.style.display="";
      }else{
        image.setAttribute("src",plus);
        node.style.display="none";
      }
  }
  function getNodesOnLoading(parentId){
    var parentNode = $("ul-"+parentId);
    if(parentId==0){
      parentNode.style.display = "";
    }
    
    var img = document.createElement("img");
    img.setAttribute("id","img-"+parentId);
    img.setAttribute("src", loading); 
    parentNode.appendChild(img);
  }
  function getNodesLoaded(parentId){
    var parentNode = $("ul-"+parentId);
    var img = $("img-"+parentId);
    parentNode.removeChild(img);
  }
  function getNodesResponse(response){
    if(response.readyState==4){
      if(response.status==200){
        var count = response.responseXML.getElementsByTagName("count")[0].firstChild.data;
        var parentNodeId = 0;
        if(response.responseXML.getElementsByTagName("parentNodeId")[0].hasChildNodes()){
          parentNodeId =  response.responseXML.getElementsByTagName("parentNodeId")[0].firstChild.data;
          if(parentNodeId==""){
            parentNodeId = 0;
          }
        }
        var parentNode = $("ul-"+parentNodeId);
        
        //去掉加载图标
        if(parentNode.hasChildNodes()){
          parentNode.removeChild(parentNode.firstChild);
        }
        
        if(count>0){
          var menu = response.responseXML.getElementsByTagName("menu");
          for(var i=0; i<menu.length; i++){
            var m = menu[i];
            //取得数据后,遍历整个NODE列表
            var nodeId = m.getElementsByTagName("id")[0].firstChild.data;
            var name = m.getElementsByTagName("name")[0].firstChild.data;
            var isHaveChild = m.getElementsByTagName("isHaveChild")[0].firstChild.data;
            //=1为有子结点,=0为没有子结点
            var has_child = (isHaveChild==1?true:false);
            
            var li = document.createElement("li");
            li.setAttribute("id", nodeId);
            
            var img1 = document.createElement("img");
            if(has_child){
              img1.setAttribute("id", "img1-"+nodeId);
              img1.setAttribute("src", plus);
              img1.onclick=function(){
                var id = this.id.substr(5);
                setNodeShowOrHide(id);
              };
            }else{
              img1.setAttribute("src",  blank);
            }
            
            var img2 = document.createElement("img");
            img2.setAttribute("src",  item);
            if(has_child){
              img2.setAttribute("id", "img2-"+nodeId);
              img2.onclick=function(){
                var id = this.id.substr(5);
                setNodeShowOrHide(id);
              };
            }
            if(checkboxPara.isHaveCheckbox){
            var checkbox = document.createElement("input");
            checkbox.setAttribute("id", "checkbox-"+nodeId);
            checkbox.setAttribute("type", "checkbox");
            checkbox.onclick = function(){
              var id = this.id.substr(9);
              if(this.checked){
                 setNodeChecked(id);
              }else{
                checkboxPara.disCheckedFun(id);
                setNodeDisChecked(id); 
              }
            }
            }
            
            var a = document.createElement("a");
            a.setAttribute("id","a-"+nodeId);
            if(linkPara.clickFunc){
              $(a).observe('click',linkPara.clickFunc.bind(window,nodeId));
            } else if(linkPara.isHaveLink&&linkPara.linkAddress!=null&&linkPara.linkAddress!=""){
              a.setAttribute("href", linkPara.linkAddress+nodeId);
              if(linkPara.target==null||linkPara.target==""){
                linkPara.target = '_blank';
              }
              a.setAttribute('target',linkPara.target);
            }else{
              a.setAttribute("href", "#");
            }
            if(has_child){
              //a.setAttribute("href", "#");
              a.onclick=function(){ 
                var id = this.id.substr(2);
                setNodeShowOrHide(id);
              };  
            }
            var text = document.createTextNode(name);
            a.appendChild(text);
            
            li.appendChild(img1);
            li.appendChild(img2);
            if(checkboxPara.isHaveCheckbox){
              li.appendChild(checkbox);
            }
            li.appendChild(a);
            
            if(has_child){
              var ul = document.createElement("ul");
              ul.setAttribute("id", "ul-"+nodeId);
              ul.style.display = "none";
              li.appendChild(ul);
            }
            parentNode.appendChild(li);
            
            if(has_child){
              if(isNoTree){
                var length = nodeId.length;
                var j = 0;
              //var regular = treeStructure.regular.split(",");
                for(var k=0;k<regular.length;k++){
                j=parseInt(j)+parseInt(regular[k]);
                if(length==j){
                  length = parseInt(j)+parseInt(regular[k+1]);
                  break;
                }
                }
                nodeLoad(nodeId,length);
               }else{
                nodeLoad(nodeId);
               }
            }
          }
          }
      }
      else if(response.status==404){
        alert("404 错误");
      }
      else if(response.status==403){
        alert("403 错误");
      }
      else if(response.status==401){
        alert("401 错误");
      }
      else {
        alert("未知错误");
    }
    }
    }
  function setNodeChecked(id){
    var li = $(id);
    var uls = li.getElementsByTagName("ul");
    for(var i=0;i<uls.length;i++){
      var ul =  uls[i];
      var imgid = "img1-"+ul.id.substr(3);
      ul.style.display = "";
      $(imgid).setAttribute("src",minus);
    }
    var checkboxs = li.getElementsByTagName("input");
    for(var i=0;i<checkboxs.length;i++){
      var checkbox = checkboxs[i];
      checkbox.checked = true;
      checkboxPara.checkedFun(checkbox.id.substr(9));
    }
    setParentNodeChecked(li);
  }
  function setParentNodeChecked(li){
    var parentUl = li.parentNode;
    var parentId = parentUl.id.substr(3);
    var checkboxId = "checkbox-"+parentId;
    var parentCheckbox = $(checkboxId);
    if(parentCheckbox==null){
      return;
    }else{
      if(parentCheckbox.checked){
        return;
      }
      parentCheckbox.checked = true;
      
      checkboxPara.checkedFun(parentUl.id.substr(3));
      var parentLi = $(parentId);
      setParentNodeChecked(parentLi);
    }
  }
  function setNodeDisChecked(id){
    var li = $(id);
    var checkboxs = li.getElementsByTagName("input");
    for(var i=0;i<checkboxs.length;i++){
      var checkbox = checkboxs[i];
      if(checkbox.checked!=false){
      checkbox.checked = false;
      checkboxPara.disCheckedFun(checkbox.id.substr(9));
      }
    }
    setParentNodeDisChecked(li);
  }
  function setParentNodeDisChecked(li){
    var parentUl = li.parentNode;
    var id = parentUl.id.substr(3);
    if(id == 0){
      return;
    }
    var childLis = parentUl.childNodes;
    for(var i=0;i<childLis.length;i++){
      var childCheckbox = $("checkbox-"+childLis[i].id);
      if(childCheckbox.checked)
        return;
    }
    $("checkbox-"+id).checked = false;
    checkboxPara.disCheckedFun(id);
    setParentNodeDisChecked($(id));
  }
}


function MyTreeInit(rootNodeId,requestUrl,treeStructure,isOnceLoad,checkboxPara,linkPara){
    if(isOnceLoad){
      new OnceLoadTree(rootNodeId,requestUrl,treeStructure,checkboxPara,linkPara);
    }else{
      
      new MyTree(rootNodeId,requestUrl,treeStructure,checkboxPara,linkPara)
    }
  
}

function test(id){
  alert("dischecked:"+id);
}
function test1(id){
  alert("checked:"+id);
}

function test2(id){
  
}
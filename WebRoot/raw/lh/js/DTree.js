/*
     提供的数据对象
  {rtState:"0", rtMsrg:"成功返回!", rtData:[
          {nodeId:'5',name:'1.1.3',isHaveChild:1,extData:'1.1.3',imgAddrss:''}
          ,{nodeId:'4',name:'1.1.2',isHaveChild:0,extData:'1.1.3',imgAddrss:''}
          ,{nodeId:'9',name:'1.1.1',isHaveChild:0,extData:'1.1.3',imgAddrss:''}
          ]}
*/
var DTree = Class.create();
DTree.prototype = {
  initialize: function(parameters) {
    this.config(parameters);
  },
  config:function(parameters){
   /*parameters:
    * {bindToContainerId,requestUrl,isOnceLoad,treeStructure,checkboxPara,linkPara}
    * bindToContainerId 字符串  容器Id 默认为boby
    * requestUrl 字符串 请求路径
    * isOnceLoad 布尔  是否一次加载 默认为false
    * treeStructure :
    *    {isNoTree,regular}
    *     isNoTree  布尔  树的节点的nodeId是否编码  默认为false
    *     regular   字符串  如果树的节点的nodeId是否编码，需指字编码规则形如2,3,4,5，第段用逗号分割
    * checkboxPara:
    *     {isHaveCheckbox,disCheckedFun,checkedFun}
    *     isHaveCheckbox 布尔 是否有选框  默认为false
    *     disCheckedFun 函数  如果有选框，则当取消选择时要执行的函数,函数参数为此节点的ID
    *     checkedFun 函数 如果有选框，则当选择时要执行的函数,函数参数为此节点的ID
    * linkPara:
    *     {clickFunc,linkAddress,target}
    *     clickFunc 函数 点击链接时要执行的函数,函数参数为此节点的ID
    *     linkAddress 字符串 点击链接时要打开的地址,如果指定了上面的函数，点击链接时将不会打开此地址
    *     target 字符串 指定点击链接时在什么地方打开上面的链接地址，默为为一个'_blank'
    */
    //初始化参数
    
    var bindNode = $(parameters.bindToContainerId) ? $(parameters.bindToContainerId) : $(document.body);
    this.isOnceLoad = parameters.isOnceLoad;
    this.requestUrl = parameters.requestUrl ? parameters.requestUrl : contextPath + '/raw/lh/dtree/YHDTreeAct/getTree.act?id=';
    this.checkboxPara = parameters.checkboxPara ? parameters.checkboxPara : {isHaveCheckbox:false};
    this.linkPara = parameters.linkPara ? parameters.linkPara : {};
    this.treeStructure = parameters.treeStructure ? parameters.treeStructure :{isNoTree:false,regular:'3,2,2,4'};
    this.isFolder = parameters.isFolder;
    //设置常量
    this.setHowEncode("");
    this.setImage(imgPath);
    this.root = "0";
    //创建根节点
    this.rootUl = document.createElement("ul");
    this.rootUl.id = this.ulEncode + this.root;
    this.rootUl.style.listStyle = "none";
    
    this.rootUl.style.marginLeft = '-30px';
    this.rootUl.style.paddingLeft = '30';
    //一个控件只能邦定一个树,当然可以做到邦定多个树
    bindNode.tree = this;
    bindNode.appendChild(this.rootUl);
    
    this.isNoTree = this.treeStructure.isNoTree;
    this.regular = new Array();
    if(this.isNoTree){
      this.regular = this.treeStructure.regular.split(",");
      if(this.regular.length <1){
        alert("你指定的编码规则不对");
        return;
      }
    }
    this.currNodeId = "";
  },
  setHowEncode:function(encode){
    this.ulEncode = encode + '-ul-';
    this.liEncode = encode + '-li-';
    this.aEncode = encode + '-a-';
    this.imgEncode = encode + '-img-';
    this.img1Encode = encode + '-img1-';
    this.img2Encode = encode + '-img2-';
    this.checkboxEncode = encode + '-checkbox-';
  },
  setImage:function(path){
    this.minus = path + "/dtree/minus.gif";
    this.blank = path + "/dtree/blank.gif";
    this.item = path + "/dtree/folder.gif";
    this.plus = path + "/dtree/plus.gif";
    this.loading = path + "/dtree/loading.gif";
    this.expland = path + "/dtree/folderOpen.gif";
  },
  show:function(){
    if(this.isOnceLoad){
      this.onceLoadTree();
    }else{
      if(!this.isNoTree)
        this.treeLoad(this.root);
      else{ 
          this.treeLoad(this.root, this.regular[0]);
      }
    }
  },
  treeLoad:function(parentId,length){
    var parentNode = $(this.ulEncode + parentId);
    
    //首先判断父结点的子结点是否已经加载,即判断是否含有子结点
    //如没有加载,就加载,如已经加载直接显隐,同时设置为+,或者为-
    var parentImage = $(this.img1Encode + parentId);
    if(this.isFolder){
      var parentImageFolder = $(this.img2Encode + parentId);
    }
    if(parentNode.hasChildNodes()){
      if(parentNode.style.display=="none"){
        parentImage.src = this.minus;
        parentNode.style.display="";
        if(this.isFolder){
          parentImageFolder.src = this.expland;
        }
      }else{
        if(parentImage){
          parentImage.src = this.plus;
          parentNode.style.display="none";
          if(this.isFolder){
            parentImageFolder.src = this.item;
          }
        }
      }
    }else{
      
      this.getNodes(parentId, length,false);
    }
  },
  getNodes:function(parentId,length,flag){
    //加载noding图标
    this.getNodesOnLoading(parentId);
    var url = "";
    if(this.isNoTree){
      //根节点'0',传父节点nodeId空串
      if(parentId == this.root){
        url =  this.requestUrl;
      }else{
        url =  this.requestUrl + parentId;
      }
      url = url+ "&length="+length;
    }
    else{
      url =  this.requestUrl + parentId;
    }
    var json = getJsonRs(url);
   
    var parentNode = $(this.ulEncode + parentId);
     //去掉加载图标
    if(parentNode.hasChildNodes()){
      parentNode.removeChild(parentNode.firstChild);
    }
    //{nodeId:'5',name:'1.1.1',parentId:2,isHaveChild:0,extData:'',imgAddrss:''}  
    if(json.rtState == '0' && json.rtData.length > 0){
      var nodes = json.rtData;
      for(var i = 0; i<nodes.length; i++){
        //取得数据后,遍历整个NODE列表
        var nodeJsonObj = nodes[i];
        //=1为有子结点,=0为没有子结点
        nodeJsonObj.parentId = parentId;
        //flag第一次加载时不用级联为false，当点击checkbox时，级联展开所有下级
        this.loadNode(nodeJsonObj,flag);
      }
      //当有checkbox时，一般级联展开子节点，如果点击checkbox时，这时就要级联展开子节点
      if(this.checkboxPara.isHaveCheckbox && flag){
        var lis = parentNode.getElementsByTagName("li");
        var nodes = parentNode.getElementsByTagName("input");
        for(var i = 0 ;i < nodes.length ;i++){
          var n = nodes[i];
          n.checked = true;
          this.checkboxPara.checkedFun(n.id.substr(this.checkboxEncode.length));
        }
        for(var i = 0 ;i<lis.length; i++){
          var childLi = lis[i];
          this.setAllChildCheckboxChecked(childLi.id.substr(this.liEncode.length));
        }
      }
    }
  },
  getNodesOnLoading:function(parentId){
    var parentNode = $(this.ulEncode + parentId);
    parentNode.style.display = "";
    var img = document.createElement("img");
    img.id = this.imgEncode + parentId;
    img.src = this.loading;
    parentNode.appendChild(img);
  },
  loadNode:function(nodeJsonObj){
    //------------------
    var nodeId = nodeJsonObj.nodeId;
    var name = nodeJsonObj.name;
    //=1为有子结点,=0为没有子结点
    var isHaveChild = (nodeJsonObj.isHaveChild == 1?true:false);
    var parentId = this.root;
    if(nodeJsonObj.parentId){
      parentId = nodeJsonObj.parentId; 
    }
    var extData = '';
    var imgAddress = this.item;
    if(nodeJsonObj.extData){
      extData = nodeJsonObj.extData;
    }
    
    if(nodeJsonObj.imgAddress){
      imgAddress =  nodeJsonObj.imgAddress;
      
    }
    
    //---------------------
    /*<li id='nodeId'>
     *   <img id="img1-nodeId" scr="plus" onclick="imageAndAClick(nodeId,isHaveChild)"/>
     *   <img id="img2-nodeId" scr="imgAddress" onclick="imageAndAClick(nodeId,isHaveChild)"/>
     *   {<input id="checkbox-nodeId" type="checkbox" onclick="checkboxClick(nodeId,checkbox)"/>}
     *   <a id="a-nodeId" parentId="parentId" isHaveChild="isHaveChild" extData="extData" onclick="linkPara.clickFunc(nodeId);imageAndAClick(nodeId,isHaveChild)" href="#">
     *  ||<a id="a-nodeId" parentId="parentId" isHaveChild="isHaveChild" extData="extData" onclick="imageAndAClick(nodeId,isHaveChild)" href="linkPara.address+nodeId" target="linkPara.target">
     *   name</a>
     *   {<ul id="ul-nodeId" style="display:none"></ul>}
     *  </li>
     */
    var parentNode = $(this.ulEncode + parentId);
    if(parentNode != this.rootUl){
      if(document.all){
        parentNode.style.marginLeft = '20px';//IE
      }else{
        parentNode.style.paddingLeft = '20px';//fireFox
      }
    }
    //<li id='nodeId'>
    var li = document.createElement("li");
    
    li.style.listStyle = "none";
    li.id = this.liEncode + nodeId;
    //<img id="img1-nodeId" scr="plus" onclick="imageAndAClick(nodeId,isHaveChild)"/>
    var img1 = document.createElement("img");
    img1.id = this.img1Encode + nodeId;
    if(isHaveChild){
      img1.src = this.plus;
    }else{
      img1.src = this.blank;
    }
    
    $(img1).observe('click',this.imageAndAClick.bindAsEventListener(this,nodeId));
    
    //<img id="img2-nodeId" scr="imgAddress" onclick="imageAndAClick(nodeId,isHaveChild)"/>
    var img2 = document.createElement("img");
    img2.src = imgAddress;
   
    img2.id = this.img2Encode + nodeId;
    $(img2).observe('click',this.imageAndAClick.bindAsEventListener(this,nodeId));
    
    //{<input id="checkbox-nodeId" type="checkbox" onclick="checkboxClick(nodeId,checkbox)"/>}
    if(this.checkboxPara.isHaveCheckbox){
      var checkbox = document.createElement("input");
      checkbox.id = this.checkboxEncode + nodeId;
      checkbox.type = "checkbox";
      $(checkbox).observe('click',this.checkboxClick.bindAsEventListener(this,nodeId,checkbox));
    }
    
   /*<a id="a-nodeId" parentId="parentId" isHaveChild="isHaveChild" extData="extData" onclick="linkPara.clickFunc(nodeId);imageAndAClick(nodeId,isHaveChild)" href="#">
    *  ||<a id="a-nodeId" parentId="parentId" isHaveChild="isHaveChild" extData="extData" onclick="imageAndAClick(nodeId,isHaveChild)" href="linkPara.address+nodeId" target="linkPara.target">
    *   name</a>
    */
    var a = document.createElement("a");
    a.id = this.aEncode + nodeId;
   
    a.isHaveChild = isHaveChild;
    a.parentId = parentId;
    a.extData = extData;
    
    
    if(this.linkPara.clickFunc){
      $(a).observe('click',this.linkPara.clickFunc.bind(window,nodeId));
      a.href = "#";
    }else if(this.linkPara.linkAddress){
      if(this.linkPara.linkAddress.indexOf("javascript:") < 0) {
        a.setAttribute("href", this.linkPara.linkAddress + nodeId);
        if(this.linkPara.target){
          this.linkPara.target = '_blank';
        }
        a.target = this.linkPara.target;
      }
    }else{
      a.href = "#";
    }
    $(a).observe('click',this.imageAndAClick.bindAsEventListener(this,nodeId));
    a.innerHTML = name;
    
    li.appendChild(img1);
    li.appendChild(img2);
    if(this.checkboxPara.isHaveCheckbox){
      li.appendChild(checkbox);
    }
    li.appendChild(a);
    //{<ul id="ul-nodeId" style="display:none"></ul>}
    if(isHaveChild){
      var ul = document.createElement("ul");
      ul.id = this.ulEncode + nodeId;
      ul.style.display = "none";
      li.appendChild(ul);
    }
    
    parentNode.appendChild(li);
    
    //显隐,当不是根结点且不是一次性加载时,就没有img这个标签,因此去掉
    if(parentId != this.root && !this.isOnceLoad){
      var parentImage = $(this.img1Encode + parentId);
      var parentImageFolder = $(this.img2Encode + parentId);
      parentImage.src = this.minus;
      if(this.isFolder){
        parentImageFolder.src = this.expland;
      }
      
    }
    
  },
   setAllChildCheckboxChecked:function(id){
     var ul = $(this.ulEncode + id);
     if(ul!=null){
       //说明已经加载,直接展开,没有加载则加载 ,然后....
       if(ul.hasChildNodes()){
         var parentImage = $(this.img1Encode + id);
         parentImage.src = this.minus;
         ul.style.display = "";
         
         var lis = ul.getElementsByTagName("li");
         var nodes = ul.getElementsByTagName("input");
         
         for(var i = 0 ;i < nodes.length ;i++){
           var n = nodes[i];
           n.checked = true;
           this.checkboxPara.checkedFun(n.id.substr(this.checkboxEncode.length));
         }
         for(var i = 0 ;i<lis.length; i++){
           var li = lis[i];
           this.setAllChildCheckboxChecked(li.id.substr(this.liEncode.length));
         }
       }else{
         //如果是此树一次加载，就不用去加载
         if(!this.isOnceLoad){
           var length = id.length;
           if(this.isNoTree){
             length = this.getChildNodeLength(id.length);
           }
           this.getNodes(id, length, true);
         }
       }
     }
   },
   setAllChildCheckBoxDisChecked:function(id){
     var ul = $(this.ulEncode + id);
     if(ul != null){
       var nodes = ul.getElementsByTagName("input");
       for(var i = 0 ;i < nodes.length ;i++){
         var n = nodes[i];
         if(n.checked != false){
           n.checked = false;
           this.checkboxPara.disCheckedFun(n.id.substr(this.checkboxEncode.length));
         }
       }
     }
   },
   setParentNodeCheckbox:function(node){
     var id = node.id.substr(this.checkboxEncode.length);
     var li = $(this.liEncode + id);
     var parentlu = li.parentNode;
     id = parentlu.id.substr(this.ulEncode.length);
     if(id == this.root){
       return ;
     }
     var parentCheckbox = $(this.checkboxEncode + id);
     if(parentCheckbox.checked){
       return;
     }
     parentCheckbox.checked = true;
     this.checkboxPara.checkedFun(id);
     this.setParentNodeCheckbox(parentCheckbox);
   },
   setParentNodeDisCheckbox:function(node){
     var id = node.id.substr(this.checkboxEncode.length);
     li = $(this.liEncode + id);
     var parentlu = li.parentNode;
     id = parentlu.id.substr(this.ulEncode.length);
     if(id == this.root){
       return ;
     }
     var lis = parentlu.childNodes;

     for(var i = 0 ;i < lis.length ;i++ ){
       li = lis[i];
       var checkbox =  $( this.checkboxEncode +li.id.substr(this.liEncode.length) );
       if(checkbox.checked){
         return ;
       }
     }
     var parentCheckbox = $(this.checkboxEncode + id);
     parentCheckbox.checked = false;
     this.checkboxPara.disCheckedFun(id);
     this.setParentNodeDisCheckbox(parentCheckbox);
   },
   imageAndAClick:function(){
     var id = arguments[1];
     var a = $(this.aEncode + id);
     if(a){
       if(this.currNodeId){
         $(this.aEncode + this.currNodeId).style.backgroundColor = "";
       }
       this.currNodeId = id;
       a.style.backgroundColor = "#CCCCCC";
       var isHaveChild = a.isHaveChild;
    
       if(isHaveChild){
         if(this.isNoTree){
           var length = this.getChildNodeLength(id.length);
           this.treeLoad(id,length);
         }else{
           this.treeLoad(id);
         }
       }
     }else{
       alert('你点的节点不存在！');
     }
   },
   checkboxClick:function(){
     var id = arguments[1];
     var checkbox = arguments[2];
     this.currNodeId = id;
     if(checkbox.checked){
       this.checkboxPara.checkedFun(id);
       this.setAllChildCheckboxChecked(id);
       this.setParentNodeCheckbox(checkbox);
     }else{
       this.checkboxPara.disCheckedFun(id);
       this.setAllChildCheckBoxDisChecked(id);
       this.setParentNodeDisCheckbox(checkbox);
     }
   },
   removeNode:function(id){
     var li = $(this.liEncode + id);
     if(li == null){
       alert('没有找到你要删除的结点!');
       return ;
     }
     var parentNode = li.parentNode;
     parentNode.removeChild(li);
     //是否是移出的是最后一个子节点
     if(!parentNode.hasChildNodes()){
       var hisParentNode = parentNode.parentNode;
       if(hisParentNode.id == this.ulEncode + this.root)
       return;
       hisParentNode.removeChild(parentNode);
       
       var parentImage1 = hisParentNode.firstChild;
       parentImage1.src = this.blank;
       var parentA = hisParentNode.lastChild;
       parentA.isHaveChild = false;
       
     }
   },
   getCurrNode:function(){
     if(this.currNodeId){
       return this.getNode(this.currNodeId);
     }else{
       return null;
     }
   },
   getNode:function(id){
     if(id && $(this.liEncode + id)){
       var nodeJsonObj = new Object();
       var a = $(this.aEncode + id);
       var img2 = $(this.img2Encode + id);
       nodeJsonObj.nodeId = id;
       nodeJsonObj.name = a.innerHTML;
       nodeJsonObj.extData = a.extData;
       nodeJsonObj.isHaveChild = a.isHaveChild;
       nodeJsonObj.parentId =  a.parentId;
       //如果是nodeId是编码形式的，当它的 parentId == this.root时,设置parentId = ""
       if(this.isNoTree && nodeJsonObj.parentId == this.root){
         nodeJsonObj.parentId = "";
       }
       nodeJsonObj.imgAddress = img2.src;
       return nodeJsonObj;
     }else{
       return null;
     }
   },
   onceLoadTree:function(){
     //加载noding图标
     this.getNodesOnLoading(this.root);
     var tree = getJsonRs(this.requestUrl);
     var parentNode = $(this.ulEncode + this.root);
     //去掉加载图标
     if(parentNode.hasChildNodes()){
       parentNode.removeChild(parentNode.firstChild);
     }
     if(tree.rtState == '0'){
       if(tree.rtData.length>0){
         var rootNode = {nodeId:this.root,isHaveChild:1};
         this.onceLoadChildNodes(rootNode,tree.rtData);
       }
     }
   },
   onceLoadChildNodes:function(parentNode,nodes){
     var parentId = parentNode.nodeId;
     var parentUl = $(this.ulEncode + parentId);
     //如果是根结点，显示,否则隐藏
     if(parentUl != null)
       if(parentId == this.root){
         parentUl.style.display = '';
       }else{
         parentUl.style.display = 'none';
       }
     if(parentNode.isHaveChild == 1){
       for(var i = 0 ;i < nodes.length ;i++){
         var node = nodes[i];
         if(!this.isNoTree){
           if(node.parentId == parentId){
             this.loadNode(node);
             //去掉此节点
             this.onceLoadChildNodes(node, nodes);
           }
         }else{
           var isHaveParentStr = false;
           var length = this.regular[0];
           if(parentId == this.root || this.startWith(node.nodeId,parentId)){
             isHaveParentStr = true;
             if(parentId != this.root){
               length = this.getChildNodeLength(parentId.length);
             }
           }
           
           if( isHaveParentStr && node.nodeId.length == parseInt(length)){
             node.parentId = parentId;
             this.loadNode(node);
             //去掉此节点
             this.onceLoadChildNodes(node, nodes);
           }
         }
       }
     }
   },
   startWith:function(nodeId,parentId){
     if(parentId==null||parentId=="")
       return true;
       if(nodeId.substr(0,parentId.length)==parentId)
       return true;
       else
       return false;
       return true;
   },
   getChildNodeLength:function(length){
     var j = 0;
     for(var i = 0; i<this.regular.length ; i++){
       j= parseInt(j) + parseInt(this.regular[i]);
       if(length == j){
         length = parseInt(j) + parseInt(this.regular[i+1]);
         break;
       }
     }
     return length;
   },
   getParentNodeLength:function(length){
     var j = 0;
     for(var i = 0; i<this.regular.length ; i++){
       j= parseInt(j) + parseInt(this.regular[i]);
       if(length == j){
         length = parseInt(j) - parseInt(this.regular[i]);
         break;
       }
     }
     return length;
   },
   addNode:function(nodeJsonObj){
     //添加的是否是第一个子节点
     var parentId = this.root;
     if(nodeJsonObj.parentId){
       parentId = nodeJsonObj.parentId; 
     }else{
       if(this.isNoTree){
         //当是编码树的时候，parentId是根据nodeId的长度来算的
         if(!this.checkNoId(nodeJsonObj.nodeId.length)){
           alert('你指定的nodeId:' + nodeJsonObj.nodeId + '的编码不对!');
           return;
         }
         var parentIdLength = this.getParentNodeLength(nodeJsonObj.nodeId.length);
         var parentId = nodeJsonObj.nodeId.substring(0,parentIdLength);
         nodeJsonObj.parentId = parentId;
       }
     }
     var parentNode = $(this.ulEncode + parentId);
     //为空时
     if(!parentNode){
       var parentImage1 = $(this.img1Encode + parentId);
       if(!parentImage1){
         alert('没有找到指定的父节点');
         return ;
       }
       var parentLi = parentImage1.parentNode;
       parentImage1.src = this.minus;
       var parentA = $(this.aEncode + parentId);
       parentA.isHaveChild = true;
       parentNode = document.createElement("ul");
       parentNode.id = this.ulEncode + parentId;
       parentLi.appendChild(parentNode);
     }else{
       //不为空
       //parentNode
       var parentA = $(this.aEncode + parentId);
       if(parentA.isHaveChild 
           && parentNode.style.display == "none"){
         if(this.isNoTree){
           if(parentId == this.root){
             parentId = "";
           }
           var length = this.getChildNodeLength(parentId.length);
           this.treeLoad(parentId, length);
         }else{
           this.treeLoad(parentId);
         }
       }
     }
     this.loadNode(nodeJsonObj)
   },
   checkNoId:function(length){
     var j = 0;
     for(var i = 0; i<this.regular.length ; i++){
       j= parseInt(j) + parseInt(this.regular[i]);
       if(length == j){
         return true;
       }
     }
     return false;
   },
   getFirstNode:function(){
     var firstLi = this.rootUl.firstChild;
     return this.getNode(firstLi.id.substr(this.liEncode.length));
   },
   open:function(nodeId){
     var parentNode = $(this.ulEncode + nodeId);
     if(parentNode){
       if(this.isNoTree){
         var length =  this.getChildNodeLength(nodeId.length);
       }
       var parentImage = parentNode.parentNode.firstChild;
       if(parentNode.hasChildNodes()){
         if(parentNode.style.display=="none"){
           parentImage.src = this.minus;
           parentNode.style.display="";
         }
       }else{
         this.getNodes(nodeId, length,false);
       }
     }else{
       alert('没有你指定的节点或你指定的节点没有子节点！');
     }
   },
   close:function(nodeId){
     var parentNode = $(this.ulEncode + nodeId);
     if(parentNode){
       var parentImage = parentNode.parentNode.firstChild;
       if(parentNode.hasChildNodes()){
         if(parentNode.style.display != "none"){
           parentImage.src = this.plus;
           parentNode.style.display="none";
         }
       } 
     }else{
       alert('没有你指定的节点或你指定的节点没有子节点！');
     }
   },
   click:function(id){
     this.linkPara.clickFunc(id);
   }
};


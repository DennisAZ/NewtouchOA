//编码分隔符
var CODE_SPLIT = "-";
//树相关常量定义
var TREE_CONST = new TreeConst;

/**
 * 检测数组中是非含有某个元素
 * elem           检测对象
 */
if (!Array.prototype.contains) {    
	Array.prototype.contains = function(elem) {
		if (!elem) {
		  return false;
		}
		for (var i = 0; i < this.length; i++) {
		  if (this[i] == elem) {
		    return true;
		  }
		}
		return false;
	}
};

/**
 * 在数组中增加数据
 * @data        需要添加的数据
 * @index       索引
 */
if (!Array.prototype.add) {    
	Array.prototype.add = function(data, index) {		
    if (index < 0) {
      return;
    }
    if (index == null || index >= this.length) {
      index = this.length;
    }
    for (var i = this.length; i > index; i--) {
      this[i] = this[i - 1];
    }
    this[index] = data;
	}
};

/**
 * 在数组中批量增加数据
 * @dataList    需要添加的数据
 * @index       索引
 */
if (!Array.prototype.addAll) {    
  Array.prototype.addAll = function(dataList, index) {   
    if (index < 0) {
      return;
    }
    var dataCnt = dataList.size();
    if (!dataCnt || dataCnt < 1) {
      return;
    }
    if (index == null || index >= this.length) {
      index = this.length;
    }
    for (var i = this.length + dataCnt - 1; i > index + dataCnt - 1; i--) {
      this[i] = this[i - dataCnt];
    }
    for (var i = index; i < index + dataCnt; i++) {
      this[i] = dataList.get(i - index);
    }
  }
};

/**
 * 在数组中设置数据
 * @data        需要添加的数据
 * @index       索引
 */
if (!Array.prototype.setAt) {    
	Array.prototype.setAt = function(data, index) {		
    if (index < 0) {
      return;
    }
    if (index == null || index >= this.length) {
      index = this.length;
    }    
    this[index] = data;
	}
};

/**
 * 取得数组的大小
 */
if (!Array.prototype.size) {
  Array.prototype.size = function() {
    return this.length;
  }
}

/**
 * 取得数组中的数据
 * @index       索引
 */
if (!Array.prototype.get) {    
	Array.prototype.get = function(index) {
	  if (index == null || index < 0 || index >= this.size()) {
	    return null;
	  }
	  return this[index];
	}
}

/**
 * 取得数组中的数据
 */
if (!Array.prototype.getLast) {    
	Array.prototype.getLast = function() {
	  return this.get(this.size() - 1);
	}
}

/**
 * 删除数组中的数据
 * @index       索引
 */
if (!Array.prototype.remove) {    
	Array.prototype.remove = function(index) {
    if (index == null) {
      index = this.length - 1;
    }
    if (index < 0) {
      return;
    }
    if (index >= this.length) {
      return;
    }
    for (var i = index; i < this.length - 1; i++) {
      this[i] = this[i + 1];
    }
    this[this.length - 1] = null;
    this.length--;
	}
};

/**
 * 清除所有对象
 */
if (!Array.prototype.clear) {
  Array.prototype.clear = function() {
    for (var i = 0; i < this.size(); i++) {
      this[i] = null;
    }
    this.length = 0;
  }
}

/**
 * 取得从0索引开始搜索找到的第一个索引
 * @return        存在=第一个索引值；不存在=-1
 */
if (!Array.prototype.indexOf) {
  Array.prototype.indexOf = function(data) {
    for (var i = 0; i < this.size(); i++) {
      if (this[i] == data) {
        return i;
      }
    }
    return -1;
  }
};

/**
 * 取得从0索引开始搜索找到的最后一个索引
 * @return       存在=最后一个索引值；不存在=-1
 */
if (!Array.prototype.lastIndexOf) {
  Array.prototype.lastIndexOf = function(data) {
    for (var i = this.size() - 1; i >= 0; i--) {
      if (this[i] == data) {
        return i;
      }
    }
    return -1;
  }
};

/**
 * 取得从0索引开始搜索找到的第一个大于指定数据的索引
 * @return        存在=第一个索引值；不存在=-1
 */
if (!Array.prototype.indexOfGreat) {
  Array.prototype.indexOfGreat = function(data) {
    for (var i = 0; i < this.size(); i++) {
      if (this[i] > data) {
        return i;
      }
    }
    return -1;
  }
};


/**
 * 复制数组对象
 * @other        被复制的对象
 */
if (!Array.prototype.clone) {  
  Array.prototype.clone = function(other) {
    this.clear();
    for (var i = 0; i < other.size(); i++) {
      this.add(other.get(i));
    }
  }
}

/**
 * 对象排序
 */
if (!Array.prototype.sortObj) {
  Array.prototype.sortObj = function(key, isDesc, type) {
    for (var i = 0, n = this.size(); i < n; i++) {
      var currIndex = i;
      for (var j = i + 1; j < n; j++) {
        if (isDesc) {
          if (compare(this[j], this[currIndex], key, type) > 0) {
            currIndex = j;
          }
        }else {
          if (compare(this[j], this[currIndex], key, type) < 0) {
            currIndex = j;
          }
        }
      }
      if (currIndex != i) {
        var tmpObj = this[i];
        this[i] = this[currIndex];
        this[currIndex] = tmpObj;
      }
    }
  }
}

/**
 * 哈希表
 */
function ArrayHashMap() {
  //键值列表
  this.keyList = new Array();
  //值列表
  this.valueList = new Array();
  
  /**
   * 添加元素
   * @key            元素名称
   * @obj            元素对象
   * @keyIndex       键值索引
   */
  this.put = function(key, obj) {
    var keyIndex = this.keyList.indexOf(key);
    if (keyIndex < 0) {
      this.keyList.add(key);
      this.valueList.add(obj);
    }else {
      this.keyList.setAt(key, keyIndex);
      this.valueList.setAt(obj, keyIndex);
    }    
  }
  
  /**
   * 按键值取得对象
   * @key     对象的键值
   */
  this.get = function(key) {
    var keyIndex = this.keyList.indexOf(key);
    if (keyIndex < 0) {
      return null;
    }
    return this.valueList.get(keyIndex);
  }
  
  /**
   * 按键值删除对象
   * @key     对象键值
   */
  this.remove = function(key) {
    var keyIndex = this.keyList.indexOf(key);
    if (keyIndex < 0) {
      return;
    }
    this.keyList.remove(keyIndex);
    this.valueList.remove(keyIndex);
  }
  
  /**
   * 删除最后一个元素
   */
  this.removeLast = function() {
    var index = this.size() - 1;
    this.valueList.remove(index);
    this.keyList.remove(index);
  }
  
  /**
   * 删除所有对象
   */
  this.clear = function() {
    this.keyList.clear();
    this.valueList.clear();
  }
  
  /**
   * 取得键值的数目
   */
  this.getKeyCnt = function() {
    return this.keyList.size();
  }
  
  /**
   * 以键值的索引取得键值
   * @keyIndex       键值索引
   */
  this.getKey = function(keyIndex) {
    return this.keyList.get(keyIndex);
  }
  
  /**
   * 取得对象数目
   */
  this.getValueCnt = function() {
    return this.valueList.size();
  }
  
  /**
   * 以索引取得对象
   */
  this.getValue = function(valueIndex) {
    return this.valueList.get(valueIndex);
  }
  
  /**
   * 判断是否含有键值
   * @key             键值
   */
  this.containsKey = function(key) {
    return this.keyList.contains(key);
  }
  
  /**
   * 判断是否含有对象
   * @objValue        对象值
   */
  this.containsValue = function(objValue) {
    return this.valueList.contains(objValue);
  }
  
  /**
   * 查找键值的索引
   * @key         键值
   */
  this.keyIndexOf = function(key) {
    return this.keyList.indexOf(key);
  }
  
  /**
   * 查找大于key键值的索引
   * @key         键值
   */
  this.keyIndexOfGreat = function(key) {
    return this.keyList.indexOfGreat(key);
  }
  
  /**
   * 哈希表中对象的数目
   */
  this.size = function() {
    return this.keyList.size();
  }
}

/**
 * 编码结构
 * @code          编码
 * @desc          描述
 */
function CodeRecord(code, desc) {
  this.code = code;
  this.desc = desc;
  
  /**
   * 取得编码
   */
  this.getCode = function() {
    if (this.code === "") {
      return "";
    }
    return this.code ? this.code : 0;
  }
  
  /**
   * 设置编码
   * @code      编码
   */
  this.setCode = function(code) {
    this.code = code;
  }
  
  /**
   * 取得描述
   */
  this.getDesc = function() {
    return this.desc ? this.desc : "";
  }
  
  /**
   * 设置描述
   * @desc      描述
   */
  this.setDesc = function(desc) {
    this.desc = desc;
  }
}

/**
 * 树相关常量定义
 */
function TreeConst() {
  //根节点
  this.ROOT_NODE = 0;
  //分支节点
  this.BRANCH_NODE = 1;
  //叶子节点
  this.LEAF_NODE = 2;
}
/**
 * 树的节点
 */
function TreeNode(nodeNo, dataArray) {
  //节点编码
  this.nodeNo = nodeNo;
  //节点数据
  this.dataArray = dataArray;
  //节点状态
  this.isOpenState = false;
  //是否是目录
  this.isFolderState = true;
  //是否选中
  this.isSelected = false;
  //风格属性
  this.styleClass = null;
  //在列表中的索引
  this.index = -1;
  //子节点索引列表
  this.childList = new Array();
  //父亲节点索引
  this.parentNode = null;
  
  /**
   * 取得节点编码
   */
  this.getNodeNo = function() {
    return this.nodeNo ? this.nodeNo : "";
  }
  
  /**
   * 取得父级编码
   */
  this.getParentNo = function() {
    var parentNo = null;
    var lastIndex = nodeNo.lastIndexOf(CODE_SPLIT);
    if (lastIndex > 0) {
      parentNo = nodeNo.substring(0, lastIndex);
    }
    return parentNo;
  }
  
  /**
   * 取得根节点编码
   */
  this.getRootNo = function() {
    var rootNo = this.nodeNo;
    var index = nodeNo.indexOf(CODE_SPLIT);
    if (index > 0) {
      rootNo = nodeNo.substring(0, index);
    }
    return rootNo;
  }
  
  /**
   * 增加数据
   * @data    数据
   * @index
   */
  this.addData = function(data, index) {
    this.dataArray.add(data, index);
  }
  
  /**
   * 在指定位置增加数据   
   * @data     数据
   * @index    数据的索引位置
   */
  this.setAt = function(data, index) {
    this.dataArray.setAt(data, index);
  }
  
  /**
   * 取得数据
   * @index
   */
  this.getData = function(index) {
    return this.dataArray.get(index);
  }
  
  /**
   * 取得数据的长度
   */
  this.getLength = function() {
    return this.dataArray.size();
  }
  
  /**
   * 按照编码判断是否为下一层次子节点
   */
  this.isChild = function(node) {
    if (!node) {
      return false;
    }
    if (this.isDesndntNode(node.getNodeNo())
        && (this.getLevelCnt() - node.getLevelCnt() == 1)) {
      return true;
    }
    return false;
  }
  /**
   * 按照编码判断是否为后裔节点
   */
  this.isDesndntNode = function(nodeNo) {
    if (!nodeNo) {
      return false;
    }
    var thisNo = this.getNodeNo();
    if (thisNo.length > nodeNo.length && thisNo.indexOf(nodeNo) == 0) {
      return true;
    }
    return false;
  }
  
  /**
   * 取得层次数
   */
  this.getLevelCnt = function() {
    var nodeNo = this.getNodeNo();
    if (!nodeNo) {
      return 0;
    }
    var tmpArray = nodeNo.split(CODE_SPLIT);
    return tmpArray.length - 1;
  }
  
  /**
   * 设置节点是否是打开的
   * @isOpen
   */
  this.setOpen = function(isOpen) {
    this.isOpenState = isOpen;
  }
  
  /**
   * 判断节点是否打开状态
   */
  this.isOpen = function() {
    return this.isOpenState;
  }
  
  /**
   * 增加子节点索引
   * @childIndex
   */
  this.addChild = function(child) {
    this.childList.add(child);
  }
  
  /**
   * 删除字节点索引
   * @childIndex
   */
  this.removeChild = function(childNo) {
    for (var i = 0; i < this.childList.size(); i++) {
      if (this.childList.get(i).getNodeNo() == childNo) {
        this.childList.remove(i);
        break;
      }
    }
  }
  
  
  /**
   * 取得节点属性
   */
  this.getChildCnt = function() {
    return this.childList.size();
  }
  
  /**
   * 判断是否是根节点
   */
  this.isRoot = function() {
    if (this.getLevelCnt() == 0) {
      return true;
    }
    return false;
  }
  
  /**
   * 判断是否有子节点
   */
  this.hasChild = function() {
    return this.getChildCnt() > 0;
  }
  
  /**
   * 判断是否是叶子节点
   */
  this.isLeaf = function() {
    return (!this.hasChild());
  }
  
  /**
   * 设置是否是目录
   * @isFolder
   */
  this.setFolder = function(isFolder) {
    this.isFolderState = isFolder;
  }
  
  /**
   * 判断是否是目录
   */
  this.isFolder = function() {
    return this.isFolderState;
  }
  
  /**
   * 设置风格属性
   * @styleClass 
   */
  this.setStyleClass = function(styleClass) {
    this.styleClass = styleClass;
  }
  /**
   * 取得风格属性
   */
  this.getStyleClass = function() {
    return this.styleClass ? this.styleClass : "";
  }
}

/**
 * 以一维数组存储的森林
 */
function TreeArray() {
  //编码列表
  this.noList = new Array();
  //节点列表
  this.nodeList = new Array();
  
  /**
   * 取得节点数目
   */
  this.getNodeCnt = function() {
    return this.nodeList.size();
  }
  
  /**
   * 按照节点编码查找节点的索引
   * @节点编码
   */
  this.indexOfNode = function(nodeNo) {
    var index = this.noList.indexOf(nodeNo);
    return index;
  }
  
  /**
   * 查找第一个比nodeNo大的节点位置
   * @nodeNo       节点编码
   */
  this.indexOfGreat = function(nodeNo) {
    return this.noList.indexOfGreat(nodeNo);
  }
  
  /**
   * 查找最后一个后裔节点的位置
   * @nodeNo
   */
  this.indexOfLastDesndnt = function(nodeNo) {
    var rtIndex = -1;
    var nodeIndex = this.indexOfNode(nodeNo);
    var nodeCnt = this.getNodeCnt();
    for (var i = nodeIndex + 1; i < nodeCnt; i++) {
      var currNode = this.getNode(i);
      if (currNode.isDesndntNode(nodeNo)) {
        rtIndex = i;
      }else {
        break;
      }
    }
    
    return rtIndex;
  }
  
  /**
   * 按升序添加节点，节点需要是按升序排序的
   */
  this.addNodeAscOrder = function(node) {
    var nodeNo = node.getNodeNo();
    if (nodeNo == null) {
      return;
    }
    //测试是否有相同的节点
    if (this.getNodeByNo(nodeNo)) {
      alert("jsp.js.datastructs.js_1");
      return;
    }
    
    var parentNo = node.getParentNo();
    var parentNode = null;
    if (parentNo) {
      var parentNode = this.getNodeByNo(parentNo);
      if (parentNode) {
        parentNode.setFolder(true);
      }else {
        alert(lm("jsp.js.datastructs.js_2"));
        return;
      }
    }
    var indexOfGreat = this.indexOfGreat(nodeNo);
    if (indexOfGreat < 0) {
      this.addNode(node);
    }else {
      this.addNode(node, indexOfGreat);
    }
    //在父级节点注册
    if (parentNode) {
      parentNode.addChild(node);
      node.parentNode = parentNode;
    }
  }
  
  /**
   * 增加节点
   * @node       节点对象
   * @index      位置索引
   */
  this.addNode = function(node, index) {
    this.noList.add(node.getNodeNo(), index);
    this.nodeList.add(node, index);
    if (index >= 0) {
      node.index = index;
    }else {
      node.index = this.nodeList.size() - 1;
    }
    for (var i = index + 1; i < this.nodeList.size(); i++) {
      var currNode = this.getNode(i);
      currNode.index = i;
    }
  }
  
  /**
   * 取得节点
   * @index
   */
  this.getNode = function(index) {
    return this.nodeList.get(index);
  }
  
  /**
   * 取得节点
   * @nodeNo
   */
  this.getNodeByNo = function(nodeNo) {
    return this.getNode(this.indexOfNode(nodeNo));
  }
  
  /**
   * 按索引删除节点，不管树的结果层次
   */
  this.removeANode = function(index) {
    this.noList.remove(index);
    this.nodeList.remove(index);
    for (var i = index; i < this.nodeList.size(); i++) {
      var currNode = this.getNode(i);
      currNode.index = i;
    }
  }
  
  /**
   * 删除节点
   * @index
   */
  this.removeByIndex = function(index) {
    var nodeNo = this.getNode(index).getNodeNo();
    this.removeCascade(nodeNo);
  }
  /**
   * 删除节点
   * @nodeNo
   */
  this.removeCascade = function(nodeNo) {
    if (!nodeNo) {
      return;
    }
    if (this.noList.indexOf(nodeNo) < 0) {
      return;
    }
    var node = this.getNodeByNo(nodeNo);
    var nodeCnt = this.getNodeCnt();
    for (var i = nodeCnt - 1; i >= 0; i--) {
      var node = this.getNode(i);
      var currNo = node.getNodeNo();
      if (currNo == nodeNo || node.isDesndntNode(nodeNo)) {
        this.removeANode(i);
      }
    }
    var parentNo = node.getParentNo();
    if (parentNo) {
      var parentNode = this.getNodeByNo(parentNo);
      parentNode.parentNode.removeChild(nodeNo);
    }
  }
  
  /**
   * 设置节点的状态
   * @isOpen      是否打开
   */
  this.setOpen = function(nodeNo, isOpen) {
    var node = this.getNodeByNo(nodeNo);
    node.setOpen(isOpen);
  }
  
  /**
   * 及联设置选择状态
   * @nodeNo            节点编码
   * @isChecked         是否被选中
   */
  this.setSelectedCascade = function(nodeNo, isChecked) {
    var node = this.getNodeByNo(nodeNo);
    node.isSelected = isChecked;
    
    //向上刷新
    this.refreshUp(node);
    //向下刷新
    this.refreshDown(node);
  }
  
  /**
   * 向上刷新节点的选择状态
   * node          当前节点
   */
  this.refreshUp = function(node) {
    if (node.isRoot()) {
      return;
    }
    var parentNode = node.parentNode;
    var hasChildChecked = false;
    for (var i = 0; i < parentNode.childList.size(); i++) {
      var currNode = parentNode.childList.get(i);
      if (currNode.isSelected) {
        hasChildChecked = true;
        break;
      }
    }
    parentNode.isSelected = hasChildChecked;
    if (!parentNode.isRoot()) {
      this.refreshUp(parentNode);
    }
  }
  
  /**
   * 向上刷新节点的选择状态
   * @node          当前节点
   */
  this.refreshDown = function(node) {
    if (node.isLeaf()) {
      return;
    }
    for (var i = 0; i < node.childList.size(); i++) {
      var currNode = node.childList.get(i);
      currNode.isSelected = node.isSelected;
      if (currNode.isLeaf()) {
        continue;
      }else {
        this.refreshDown(currNode);
      }
    }
  }
  
  /**
   * 取得第一个被选中的节点
   */
  this.getFirstSelectedNode = function() {
    for (var i = 0; i < this.nodeList.size(); i++) {
      var currNode = this.nodeList.get(i);
      if (currNode.isSelected) {
        return currNode;
      }
    }
    return null;
  }
}
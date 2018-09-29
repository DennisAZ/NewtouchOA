package com.psit.struts.util.web;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * HTML工具类 <br>
 */
public class HTMLUtils {
	Document doc = null;//document对象
	Element bodyE = null;//body对象
	public HTMLUtils(File file,String charset) throws IOException{
		doc = Jsoup.parse(file, charset);
		bodyE = doc.body();
	}
	
	public HTMLUtils(String html,String charset) {
		doc = Jsoup.parse(html);
		if(doc.body()!=null){
			bodyE = doc.body();
		}
		else{
			bodyE = doc;
		}
	}
	
	/**
	 * 移除页面节点 <br>
	 * @param attributes	移除的元素包含的属性（值）
	 */
	public void removeNodes( String[][] attributes){
		Elements removeEls = new Elements();
		for(String[] att : attributes){
			if(att.length>1){
				removeEls.addAll(bodyE.getElementsByAttributeValue(att[0], att[1]));//移除包含指定属性及值的元素
			}
			else{
				removeEls.addAll(bodyE.getElementsByAttribute(att[0]));//移除包含指定属性的元素
			}
			
		}
		if(removeEls.size()>0){
			removeEls.remove();
		}
	}
	
	/**
	 * 移除页面节点 <br>
	 * @param excludeElPos	移除的元素父节点位置
	 * @param excludeElIndexs	移除的元素索引
	 */
	public void removeNodes( int[][] excludeElPos, int[][] excludeElIndexs){
		Elements removeEls = new Elements();
		Element excludeEl = null;
		for(int i=0;i<excludeElPos.length;i++){
			excludeEl = bodyE;
			if(excludeElPos[i]!=null){
				for(int pos : excludeElPos[i]){
					excludeEl= excludeEl.child(pos);
				}
			}
			if(excludeElIndexs[i]!=null){
				for(int index : excludeElIndexs[i]){
					if(index>=0){
						removeEls.add(excludeEl.child(index));
					}
					else{
						removeEls.add(excludeEl.child(excludeEl.children().size()+index));
					}
				}
			}
		}
		if(removeEls.size()>0){
			removeEls.remove();
		}
	}
	
	/**
	 * 将对象转换为html字符串
	 * @param elements
	 * @return String <br>
	 */
	public String parseHTML(Elements elements){
		if(elements.size()>0){
			return elements.outerHtml();
		}
		else{
			return null;
		}
	}
	
	/**
	 * 根据标签名删除元素 <br>
	 * @param elements		父节点
	 * @param excludeTags	删除的标签名数组
	 */
	public void removeElementsByTags(Element element, String[] excludeTags){
		Elements removeEls = new Elements() ;
		Elements tempEls = new Elements() ;
		for(String tag : excludeTags){
			tempEls = element.getElementsByTag(tag);
			if(tempEls.size()>0){
				removeEls.addAll(element.getElementsByTag(tag));
			}
		}
		removeEls.remove();
		/*
		element.getElementsByTag(tagName)
		if(elements.size()>0){
			Iterator<Element> eIt = elements.iterator();
			boolean isAppend=true;
			while(eIt.hasNext()){
				Element e = eIt.next();
				if(excludeTags!=null){
					isAppend=true;
					for(String excludeTag : excludeTags){
						if(e.tagName().equals(excludeTag)){
							isAppend = false;
							break;
						}
					}
				}
				if(isAppend){
					newElements.add(e);
				}
			}
		}
		return newElements;*/
	}
	/**
	 * 根据位置索引过滤dom对象 <br>
	 * @param elements
	 * @param excludeIndexs	排除的位置索引数组
	 * @return Elements <br>
	 */
	public Elements getElementsByExcludeIndexs(Elements elements, int[] excludeIndexs){
		Elements newElements = new Elements();
		if(elements.size()>0){
			Iterator<Element> eIt = elements.iterator();
			boolean isAppend=true;
			int curI = 0;
			while(eIt.hasNext()){
				Element e = eIt.next();
				if(excludeIndexs!=null){
					isAppend=true;
					for(int i : excludeIndexs){
						if(curI==i){
							isAppend = false;
							break;
						}
					}
				}
				if(isAppend){
					newElements.add(e);
				}
				curI++;
			}
		}
		return newElements;
	}
	
	/**
	 * 获得子节点中的指定元素的文本<br>
	 * @param parentNode	父节点
	 * @param tagNames		指定元素的标签
	 * @param indexs		指定元素的位置索引
	 * @return String <br>
	 */
	public String getNodeTextByIndex(Element parentNode,LinkedList<String> tagNames,LinkedList<Integer> indexs){
		if(tagNames !=null && indexs !=null){
			String tagName = tagNames.pop();
			int index = indexs.pop();
			Elements elements = parentNode.children();
			Iterator<Element> eIt = elements.iterator();
			int i=0;
			while(eIt.hasNext()){
				Element curE = eIt.next();
				if(curE.tagName().equals(tagName)){
					if(i==index){
						if(tagNames.size()>0){
							return getNodeTextByIndex(curE,tagNames,indexs);
						}
						else{
							return curE.text();
						}
					}
					i++;
				}
			}
		}
		return null;
	}
	
	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public Element getBodyE() {
		return bodyE;
	}

	public void setBodyE(Element bodyE) {
		this.bodyE = bodyE;
	}
	
	
}

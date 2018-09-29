package com.psit.struts.util;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.hibernate.collection.AbstractPersistentCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

import com.psit.struts.util.format.GetDate;

/**
 * JSON转换类 <br>
 * @author rabbit
 */
public class JSONConvert {

    public static final Logger log = Logger.getLogger(JSONConvert.class);
    
    //集合转JSONArray
    public JSONArray collect2JSONArray(Collection collect) {
        return collect2JSONArray(collect, null);
    }
    
    public JSONArray collect2JSONArray(Collection collect, Collection jsonAwareCollect) {
        JSONArray jsonArray = new JSONArray();
        for (Iterator iter = collect.iterator(); iter.hasNext();) {
            Object object = iter.next();
            if (object instanceof Collection) {
            	//递归
                object = collect2JSONArray((Collection)object, jsonAwareCollect);
            } else if (object instanceof Map ||
                object instanceof CharSequence ||
                object instanceof Number ) {
                object = JSONObject.fromObject(object);
//            	object = JSONArray.fromObject(object);
            } else if (!(object instanceof Object[])){
                object = model2JSON(object, jsonAwareCollect);
            }
            jsonArray.add(object);
        }
        return jsonArray;
    }

    public JSONObject model2JSON(Object model) {
        return model2JSON(model, null);
    }
    
    /**
     * 把model转换为JSON对象，凡是实现JSONNotAware的不引入，按照get方法得到属性信息
     * 向量的判断暂时只有Collection，也就是说会对Collection中所有JSONNotAware的不引入，
     * 目的是减少不必要的延迟加载，需要引入的话把名字通过jsonAwareArray传入
     * @param model 需要转换的对象
     * @param jsonAwareArray 需要引入的变量
     * @return
     */
    public JSONObject model2JSON(Object model, Collection jsonAwareCollect) {
        JSONObject jsonObject = new JSONObject();
        //方法名集合
        Method[] methods = model.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            /*BaseModel中不需要getClass*/
            if (method.getName().equals("getClass")) {
                continue;
            }
            if (!ModelUtils.isGetMethod(method)) {
                continue;
            }
            Class returnType = method.getReturnType();
            /*不需要转换的类型*/
            if (returnType == Document.class || 
                returnType == byte[].class ||
                returnType == Logger.class ||
                returnType == LazyInitializer.class
               ) {
                continue;
            }
            String propertyName = ModelUtils.getPropertyName(method.getName());
            Object getObj =null;
            try{
            getObj=ModelUtils.invokeGetMethod(model, method);
            }catch(IllegalArgumentException e){
            	e.printStackTrace();
            }
            if (getObj == null) {
                continue;
            }
            Collection subCollect = getAwareSubList(jsonAwareCollect, propertyName);
            if (getObj instanceof LazyInitializer) {
                continue;
            } else if (getObj instanceof Date) {
                getObj = GetDate.parseStrTime((Date) getObj);
            } else if (getObj instanceof HibernateProxy) {
                if (subCollect == null) {
                    //只有多对一的情况下会到此逻辑，如果没有指定加载这个对象，只加载idStr
                    Class clazz = getObj.getClass();
                    JSONObject tempJSONObject = new JSONObject();
                    tempJSONObject.put(ModelUtils.getIdName(getObj), ModelUtils.getIdValue(getObj));
                    getObj = tempJSONObject;
                } else {
                    getObj = model2JSON(getObj, subCollect);
                }
            } else if (getObj instanceof AbstractPersistentCollection) {//Hibernate的List，可能触发延迟加载
                if (subCollect == null) {
                    continue;
                }
                JSONArray jsonArray = new JSONArray();
                for (Iterator iter = ((Collection) getObj).iterator(); iter.hasNext();) {
                    jsonArray.add(model2JSON(iter.next(), subCollect));
                }
                getObj = jsonArray;
            }
            else if(getObj instanceof Collection){//form中的List
            	getObj = collect2JSONArray((Collection)getObj,jsonAwareCollect);
            }
            try {
//            	String[]a = new String[]{"a","b"};
            	if(jsonObject.get(propertyName)==null)
                jsonObject.put(propertyName, getObj);
            }catch(net.sf.json.JSONException ex){
            	 ex.printStackTrace();
            }
            catch (Exception ex) {
                log.info("propertyName:" + propertyName + ",ex:" + ex.getMessage());
                ex.printStackTrace();
            }
        }
        return jsonObject;
    }
    
    /**从原始的jsonAwareList中查找前缀为name的，去除前缀后，放到新的List中返回
     * 例，user.group，去除前缀后就是group，返回的结果主要用于递归下一层的需要
     * @param jsonAwareCollect 原始jsonAwareList
     * @param name 需要判断的属性名称
     * @return 如果连名字符合的都没有返回null，如果有符合的但是没有subList返回new LinkedList()
     */
    private static Collection getAwareSubList(Collection jsonAwareCollect, String name) {
        if (jsonAwareCollect == null) {
            return null;
        }
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name is empty");
        }
        boolean needAware = false;
        Collection awareSubCollect = new LinkedList();
        for (Iterator iter = jsonAwareCollect.iterator(); iter.hasNext();) {
            String element = (String) iter.next();
            if (StringUtils.isEmpty(element)) {
                throw new IllegalArgumentException("element is empty");
            }
            if (element.startsWith(name + ".")) {
                String subStr = element.substring(name.length() + 1);
                if (StringUtils.isEmpty(subStr)) {
                    throw new IllegalArgumentException("subStr is empty, element:" + element);
                }
                awareSubCollect.add(subStr);
                needAware = true;
            } else if (element.startsWith(name)) {
                needAware = true;
            }
        }
        if (!needAware) {
            return null;
        }
        return awareSubCollect;
    }
}

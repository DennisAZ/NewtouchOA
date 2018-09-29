package com.psit.struts.util;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
/**
 * 实体对象操作工具类 <br>
 * create_date: Mar 10, 2011,1:26:39 PM<br>
 * @author rabbit
 */
public class ModelUtils {

	private static final Logger log = Logger.getLogger(ModelUtils.class);
	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * 
	 * @param obj
	 *            Object
	 * @param propertyName
	 *            String
	 * @return Field
	 */
	public static Field getDeclaredField(Object obj, String propertyName) {
		Assert.notNull(obj);
		Assert.hasText(propertyName);
		return getDeclaredField(obj.getClass(), propertyName);
	}

	/**
	 * 循环向上转型,获取对象的DeclaredField.
	 * 
	 * @param clazz
	 *            Class
	 * @param propertyName
	 *            String
	 * @return Field
	 */
	public static Field getDeclaredField(Class clazz, String propertyName) {
		Assert.notNull(clazz);
		Assert.hasText(propertyName);
		for (Class superClass = clazz; superClass != Object.class; superClass = superClass
				.getSuperclass()) {
			try {
				return superClass.getDeclaredField(propertyName);
			} catch (NoSuchFieldException e) {
				// Field不在当前类定义,继续向上转型
			}
		}
		throw new IllegalArgumentException("No such field: " + clazz.getName()
				+ '.' + propertyName);
	}

	/**
	 * 暴力获取对象变量值,忽略private,protected修饰符的限制.
	 * 
	 * @param obj
	 *            Object
	 * @param propertyName
	 *            String
	 * @return getObject
	 */
	public static Object forceGetProperty(Object obj, String propertyName) {
		Assert.notNull(obj);
		Assert.hasText(propertyName);
		Field field = getDeclaredField(obj, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		Object result = getFieldValue(obj, field);
		field.setAccessible(accessible);
		return result;
	}

	/**
	 * 暴力设置对象变量值,忽略private,protected修饰符的限制
	 * 
	 * @param object
	 *            Object
	 * @param propertyName
	 *            String
	 * @param value
	 *            Object
	 */
	public static void forceSetProperty(Object object, String propertyName,
			Object value) {
		Assert.notNull(object);
		Assert.hasText(propertyName);
		Field field = getDeclaredField(object, propertyName);
		boolean accessible = field.isAccessible();
		field.setAccessible(true);
		setFieldValue(object, field, value);
		field.setAccessible(accessible);
	}

	/**
	 * 转换非RuntimeException
	 * 
	 * @param obj
	 *            Object
	 * @param field
	 *            Field
	 * @return Object
	 * @see java.lang.Field#get(Object)
	 */
	public static Object getFieldValue(Object obj, Field field) {
		Assert.notNull(obj);
		Assert.notNull(field);
		try {
			return field.get(obj);
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	/**
	 * 转换非RuntimeException
	 * 
	 * @param obj
	 *            Object
	 * @param field
	 *            Field
	 * @return Object
	 * @see java.lang.Field#get(Object)
	 */
	public static void setFieldValue(Object obj, Field field, Object value) {
		Assert.notNull(obj);
		Assert.notNull(field);
		try {
			field.set(obj, value);
		} catch (IllegalAccessException ex) {
			ReflectionUtils.handleReflectionException(ex);
		}
	}

	/**
	 * 转换非RuntimeException
	 * 
	 * @param bean
	 *            Object
	 * @param name
	 *            String
	 * @return Object
	 * @see org.apache.commons.beanutils.PropertyUtils#getProperty(Object,
	 *      String)
	 */
	public static Object getProperty(Object bean, String name) {
		try {
			return PropertyUtils.getProperty(bean, name);
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	/**
	 * 转换非RuntimeException
	 * 
	 * @param bean
	 *            Object
	 * @param name
	 *            String
	 * @param value
	 *            Object
	 * @see org.apache.commons.beanutils.PropertyUtils#setProperty(Object,
	 *      String, Object)
	 */
	public static void setProperty(Object bean, String name, Object value) {
		try {
			PropertyUtils.setProperty(bean, name, value);
		} catch (IllegalAccessException ex) {
			ReflectionUtils.handleReflectionException(ex);
		} catch (InvocationTargetException ex) {
			ReflectionUtils.handleReflectionException(ex);
		} catch (NoSuchMethodException ex) {
			ReflectionUtils.handleReflectionException(ex);
		}
	}

	/**
	 * 解析json字符串成JSONObject，JSONObject中的data数据再解析成JSONArray
	 * 处理JSONArray中的JSONObject 根据JSONObject中的键值对应关系，复制到obj中相应的setMethod中。
	 * 
	 * @param jsonStr
	 *            jsonStr
	 * @param cls
	 *            class
	 */
	public static List convertObj(String jsonStr, Class cls) {
		JSONObject jsonObj = JSONObject.fromObject(jsonStr);
		return convertObj(jsonObj.getJSONArray("data"), cls);
	}

	/**
	 * 处理JSONArray中的JSONObject 根据JSONObject中的键值对应关系，复制到obj中相应的setMethod中。
	 * 
	 * @param jsonArray
	 *            JSONArray
	 * @param cls
	 *            class
	 */
	public static List convertObj(JSONArray jsonArray, Class cls) {
		List list = new LinkedList();
		for (Iterator iterator = jsonArray.iterator(); iterator.hasNext();) {
			JSONObject jsonObj = (JSONObject) iterator.next();
			list.add(convertObj(jsonObj, cls));
		}
		return list;
	}

	/**
	 * 根据JSONObject中的键值对应关系，复制到obj中相应的setMethod中。
	 * 
	 * @param cls
	 *            class
	 * @param cls
	 *            class
	 */
	public static Object convertObj(JSONObject jsonObj, Class cls) {
		Object obj = createInstance(cls);
		convertObj(jsonObj, obj);
		return obj;
	}

	/**
	 * 根据JSONObject中的键值对应关系，复制到obj中相应的setMethod中。
	 * 
	 * @param jsonObj
	 *            JSONObject
	 * @param obj
	 *            需要赋值的临时对象
	 */
	public static void convertObj(JSONObject jsonObj, Object obj) {
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(obj
				.getClass());
		for (int i = 0; i < pds.length; i++) {
			PropertyDescriptor pd = pds[i];
			if (jsonObj.containsKey(pd.getName())
					&& pd.getPropertyType() == String.class) {
				invokeSetMethod(obj, pd.getWriteMethod(), jsonObj.get(pd
						.getName()));
			}
		}
	}

	public static Object createInstance(Class cls) {
		try {
			return cls.newInstance();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	/**
	 * 把对象中所有null属性并且类型为String的赋值""，确保非空。
	 * 
	 * @param obj
	 */
	public static void setValueWithoutNull(Object obj) {
		if (obj == null) {
			return;
		}
		PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(obj
				.getClass());
		for (int i = 0; i < pds.length; i++) {
			PropertyDescriptor pd = pds[i];
			Object getObj = invokeGetMethod(obj, pd.getReadMethod());
			if (getObj == null && pd.getPropertyType() == String.class) {
				invokeSetMethod(obj, pd.getWriteMethod(), "");
			}
		}
	}

	public static boolean equals(Object x, Object y) {
		return x == y || (x != null && y != null && x.equals(y));
	}

	/**
	 * 把source对象中非空的值COPY到target中 ModelUtils.transferValue(source, target);
	 * 
	 * @param source
	 * @param target
	 */
	public static void transferValue(Object source, Object target) {
		if (source == target) {
			return;
		}
		Method[] methodsOld = source.getClass().getMethods();
		Method[] methodsNew = target.getClass().getMethods();
		for (int i = 0; i < methodsOld.length; i++) {
			if (!methodsOld[i].getName().startsWith("get")) {
				continue;
			}
			// suffix of methodName
			String suffix = methodsOld[i].getName().substring(3);
			for (int j = 0; j < methodsNew.length; j++) {
				if (!methodsNew[j].getName().equals("set" + suffix)) {
					continue;
				}
				if (!isGetMethod(methodsOld[i])
						|| !isSetMethod(methodsNew[j])
						/* get的返回类型要和SET的参数类型一致 */
						|| methodsNew[j].getParameterTypes()[0] != methodsOld[i]
								.getReturnType()) {
					continue;
				}

				Object getObj = invokeGetMethod(source, methodsOld[i]);
				if (getObj != null) {
					invokeSetMethod(target, methodsNew[j], getObj);
				}
				break;
			}
		}
	}

	/**
	 * get没有参数，get方法是public的
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isGetMethod(Method method) {
		if (method == null) {
			return false;
		}
		if (!method.getName().startsWith("get")) {
			return false;
		}
		if (method.getParameterTypes().length > 0) {
			return false;
		}
		if (!Modifier.isPublic(method.getModifiers())) {
			return false;
		}
		return true;
	}

	/**
	 * set的参数必须只有一个，set的返回类型为void，set方法是public的
	 * 
	 * @param method
	 * @return
	 */
	public static boolean isSetMethod(Method method) {
		if (method == null) {
			return false;
		}
		if (!method.getName().startsWith("set")) {
			return false;
		}
		if (method.getParameterTypes().length != 1) {
			return false;
		}
		if (method.getReturnType() != Void.TYPE) {
			return false;
		}
		if (!Modifier.isPublic(method.getModifiers())) {
			return false;
		}
		return true;
	}

	/**
	 * 调用get方法，转换非RuntimeException
	 * 
	 * @param object
	 * @param method
	 * @return
	 */
	public static Object invokeGetMethod(Object object, Method method) {
		try {
			return method.invoke(object, new Object[0]);
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	/**
	 * 调用set方法，转换非RuntimeException
	 * 
	 * @param object
	 * @param method
	 * @return
	 */
	public static void invokeSetMethod(Object object, Method method,
			Object value) {
		try {
			method.invoke(object, new Object[] { value });
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		} catch (InvocationTargetException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	/**
	 * 获得某个对象中一个指定方法名的方法对象，转换非RuntimeException
	 * 
	 * @param Class
	 *            指定对象类型
	 * @param methodName
	 *            方法名
	 * @param parameterTypes
	 *            方法参数类型数组
	 * @return 方法对象
	 */
	public static Method getMethod(Class clazz, String methodName,
			Class[] parameterTypes) {
		try {
			return clazz.getMethod(methodName, parameterTypes);
		} catch (SecurityException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		} catch (NoSuchMethodException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	/**
	 * 把一个List<model>变成一个Map，map中的key是指定的名称，
	 * 原始List中model.getKeyName相等的都归为一类放到map中key对应的list中
	 * 
	 * @param models
	 *            List<model>
	 * @param keyName
	 *            指定的归类名称
	 * @return Map<String, List<model>>
	 */
	public static Map list2MapWithListValue(Collection models, String keyName) {
		if (keyName == null || keyName.length() < 2) {
			throw new IllegalArgumentException("invalid keyName:" + keyName);
		}
		String methodName = "get" + keyName.substring(0, 1).toUpperCase()
				+ keyName.substring(1);
		Map map = new HashMap();
		for (Iterator iter = models.iterator(); iter.hasNext();) {
			Object element = (Object) iter.next();
			Method getMethod = getMethod(element.getClass(), methodName,
					new Class[0]);
			Object getObj = invokeGetMethod(element, getMethod);
			String key = getObj == null ? null : getObj.toString();
			List subList = (List) map.get(key);
			if (subList == null) {
				subList = new LinkedList();
				map.put(key, subList);
			}
			subList.add(element);
		}
		return map;
	}

	/**
	 * 根据一个方法名，得到它的属性名，方法只有两种get方法和set方法，属性名第一个强制变小写
	 * 
	 * @param methodName
	 * @return
	 */
	public static String getPropertyName(String methodName) {
		return methodName.substring(3, 4).toLowerCase()
				+ methodName.substring(4);
	}

	/**
	 * 根据一个属性名，得到它的get方法名
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getGetMehodName(String propertyName) {
		return "get" + StringUtils.capitalize(propertyName);
	}

	/**
	 * 根据一个属性名，得到它的set方法名
	 * 
	 * @param propertyName
	 * @return
	 */
	public static String getSetMehodName(String propertyName) {
		return "set" + StringUtils.capitalize(propertyName);
	}

	/**
	 * 调用newInstance方法，转换非RuntimeException
	 * 
	 * @param clazz
	 * @return
	 */
	public static Object newInstance(Class clazz) {
		Assert.notNull(clazz);
		try {
			return clazz.newInstance();
		} catch (InstantiationException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		} catch (IllegalAccessException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException(ex.getMessage());
		}
	}

	/**
	 * 获取hibernate对象主键值
	 * @param model
	 * @return String
	 */
	public static String getIdValue(Object model) {
		if (model instanceof HibernateProxy) {
			return ((HibernateProxy) model).getHibernateLazyInitializer().getIdentifier().toString();
		}
		return SpringContextTool.getClassMetadata(model.getClass())
				.getIdentifier(model, EntityMode.POJO).toString();
	}

	/**
	 * 获取hibernate对象主键属性名
	 * @param model
	 * @return String
	 */
	public static String getIdName(Object model) {
		Class<?> cls = model.getClass();
		if (model instanceof HibernateProxy) {
			cls = ((HibernateProxy) model).getHibernateLazyInitializer()
					.getPersistentClass();
		}
		ClassMetadata classMetadata = SpringContextTool.getClassMetadata(cls);
		return classMetadata.getIdentifierPropertyName();
	}
}

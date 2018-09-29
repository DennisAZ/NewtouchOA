package yh.core.funcs.portal.util.rules;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import yh.core.util.form.YHFOM;

public abstract class YHModulesRule {
  public static final String DISPLAY_PREFIX = "DISPLAY:";
  protected Map<String, String[]> map;
  protected Map<String, String> attribute;
  
  public YHModulesRule() {
    this.map = new HashMap<String, String[]>();
    this.attribute = new HashMap<String, String>();
  }
  
  public Map<String, String[]> getRule() {
    return this.map;
  }

  public void put(String key, String[] value) {
    this.map.put(key, value);
  }
  
  public void setAttribute(String key, String value) {
    this.attribute.put(key, value);
  }

  public StringBuffer toJson(Object o) throws Exception {
    Map<String, String> m = new HashMap<String, String>();
    if (o instanceof Map){
      Map<String, String> d = (Map<String, String>)o;
      
      for (Iterator<Entry<String,String[]>> it = map.entrySet().iterator(); it.hasNext();) {
        Entry<String,String[]> e = it.next();
        String[] values = e.getValue();
        StringBuffer sb = new StringBuffer();
        for (String value : values) {
          if (value != null) {
            if (value.startsWith(DISPLAY_PREFIX)) {
              sb.append(value.replaceAll(DISPLAY_PREFIX, ""));
            }
            else {
              sb.append(d.get(value));
            }
          }
          
        }
        m.put(e.getKey(), sb.toString());
      }
      
      m.putAll(attribute);
    }
    return YHFOM.toJson(m);
  }
}
package yh.core.funcs.portal.util.rules;

public class YHLinkRule extends YHModulesRule {
  public static final String TYPE = "link";
  
  public YHLinkRule (String[] title, String[] href) {
    super();
    attribute.put("type", TYPE);
    map.put("text", title);
    map.put("href", href);
  }
  
  public YHLinkRule (String title, String href) {
    super();
    attribute.put("type", TYPE);
    map.put("text", new String[]{title});
    map.put("href", new String[]{href});
  }
}
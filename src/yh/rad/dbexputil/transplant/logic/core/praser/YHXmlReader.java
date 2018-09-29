package yh.rad.dbexputil.transplant.logic.core.praser;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

public class YHXmlReader implements ElementHandler {
    
    @Override
    public void onEnd(ElementPath arg0) {
    }

    @Override
    public void onStart(ElementPath arg0) {
      Element dblpElm = arg0.getCurrent();
      if (!"name".equals(dblpElm.getName())) {
        return;
      }
      dblpElm.detach();
    }


}

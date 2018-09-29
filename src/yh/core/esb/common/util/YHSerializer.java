package yh.core.esb.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectInputValidation;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;

public class YHSerializer<T> implements Serializable {
  private Class<T> type;
  
  public void serialize(File f, T t) throws IOException {
    serialize(new FileOutputStream(f), t);
  }
  
  public T deserialize(File f) throws IOException, ClassNotFoundException {
    return deserialize(new FileInputStream(f));
  }
  
  public void serialize(FileOutputStream out, T t) throws IOException {
    ObjectOutputStream  s = new  ObjectOutputStream (out);
    s.writeObject(t);
    s.flush();
    out.flush();
    out.close();
    s.close();
  }
  
  public T deserialize(FileInputStream in) throws IOException, ClassNotFoundException {
    try {
      ObjectInputStream s = new ObjectInputStream(in);
      Object o = s.readObject();
      s.close();
      in.close();
      return (T)o;
    } catch (ClassCastException e) {
      return null;
    }
  }
}

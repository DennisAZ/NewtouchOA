package yh.core.funcs.jexcel.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import yh.core.data.YHDbRecord;
import yh.core.global.YHConst;

public class YHCSVUtil {
  public static void CVSWrite (PrintWriter printWriter ,ArrayList<YHDbRecord> dataArray ) throws Exception{
    YHCVSWriter csvw = new YHCVSWriter(printWriter);
    csvw.writeAll(dataArray);
    csvw.flush();
    csvw.close();
  }
  
  public static ArrayList<YHDbRecord> CVSReader(InputStream ips) throws Exception {
    return CVSReader(ips, YHConst.CSV_FILE_CODE);
  }
  public static ArrayList<YHDbRecord> CVSReader(InputStream ips, String charset) throws Exception {
    YHCSVReader csvReader = null;
    String[] header = null;
    ArrayList<YHDbRecord> result = new ArrayList<YHDbRecord>();
    try {
      csvReader = new YHCSVReader(new InputStreamReader(ips, charset), ',');// importFile为要导入的文本格式逗号分隔的csv文件，提供getXX/setXX方法
      if (csvReader != null) {
        header = csvReader.readNext();
        String[] csvRow = null;// row

        while ((csvRow = csvReader.readNext()) != null) {
          YHDbRecord dbRecord = new YHDbRecord();
          for (int i = 0; i < csvRow.length; i++) {
            String temp = csvRow[i];
            if ("".equals(temp)) {
              temp = null;
            }
            if (header.length > i) {
              dbRecord.addField(header[i], temp);
            }
          }
          result.add(dbRecord);
        }
      }
    } catch (Exception e) {
      throw e;
    }
    return result;
  }
}

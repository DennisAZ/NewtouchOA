package yh.lucene.logic;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

import yh.core.util.YHUtility;

public class timeFilter extends Filter {
   private String startTime;
   private String endTime;
  
  //设置权限参数列表,将orgId值传入SearchFilesFilter
  public timeFilter(String startTime,String endTime) {
     this.startTime=startTime;
     this.endTime=endTime;
    }
  
  @SuppressWarnings("deprecation")
  @Override
  public DocIdSet getDocIdSet(IndexReader reader) throws IOException {
    //生成一个bitset，大小为索引中文档总数
   OpenBitSet result=new OpenBitSet();  //设置数组长度
 //   OpenBitSet result=new OpenBitSet(reader.maxDoc());
    result.set(0,result.size()-1);    //设置数组长度
   // int dd=   reader.numDocs();
    TermDocs td =null;
    
    try

    {    
    
       Term term = new Term("term","term");
       td = reader.termDocs(term);//获取不到
        while (td.next())
        { //过滤器
          Document doc=reader.document(td.doc());
          boolean filter=this.timeFilter(doc);
            if(filter){
              result.set(td.doc());
            }
         }
        
    }catch(Exception e){
      e.printStackTrace();
    }
    finally
    {
     /* if(null!=td){
        td.close();
      }*/
    }
    return result;
  }   

  
  public boolean timeFilter(Document doc)throws Exception{
    boolean filter=true;
     String searchTime=doc.get("time");     //过滤的判断  都包含与的条件   或字段不进行处理      满足非字段
     if(!YHUtility.isNullorEmpty(this.startTime)){
       if(this.startTime.compareToIgnoreCase(searchTime)>0){
         filter=false;
       }
     }
     if(!filter||!YHUtility.isNullorEmpty(this.endTime)){
       if(this.endTime.compareToIgnoreCase(searchTime)<0){
         filter=false;
       }
     }
     
     
    return filter;
  }
  
  
}

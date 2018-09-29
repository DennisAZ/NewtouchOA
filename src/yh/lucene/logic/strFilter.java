package yh.lucene.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermDocs;
import org.apache.lucene.search.DocIdSet;
import org.apache.lucene.search.Filter;
import org.apache.lucene.util.OpenBitSet;

import yh.core.util.YHUtility;

public class strFilter extends Filter {      //存放当前用户能访问的文件所属的组织ID列表 ,存放orgId值列表
     private ArrayList strList;
     private String field;
 
     //设置权限参数列表,将orgId值传入SearchFilesFilter
    public strFilter(String field,String condition) {
      strList = new ArrayList();
       this.field=field;
      String str[]=condition.split(",");
        for(int i=0;i<str.length;i++){
             if(!YHUtility.isNullorEmpty(str[i])){
                strList.add(str[i]) ;   
            }
          }
         
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
            boolean filter=this.strFilter(doc);
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
    
  /**
   * 过滤过程
   * */
    public boolean strFilter(Document doc)throws Exception{
      boolean filter=false;
       String stationId=doc.get("stationId");     //过滤的判断  都包含与的条件   或字段不进行处理      满足非字段
       for(int i=0;i<this.strList.size();i++){
         String conStr=(String)this.strList.get(i);
         if(conStr.equals(stationId)){
           filter=true;
           break;
         }
       }
      return filter;
    }
    
}


package yh.lucene.logic;
import java.util.*;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;

public class MutiFilter{    
        private List<Filter> filterList;
        public MutiFilter(){
            filterList = new ArrayList<Filter>();
        }
        public void addFilter(Filter selfFilter){
            filterList.add(selfFilter);//加入List，可以增加多個过滤
        }
        public Query getFilterQuery(Query query){
            for(int i=0;i<filterList.size();i++){
                //取出多個过滤器，在结果中再次定位结果
                query = new FilteredQuery(query, filterList.get(i));
            }
            return query;
        }    
}


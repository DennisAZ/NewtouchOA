package oa.spring.db.dataSet;

import java.sql.ResultSet;
import java.sql.SQLException;


public class DataSetFactory {

    public static DataSet getDataSet(ResultSet rs) throws SQLException {
        DataSet dataSet = new CommonDataSet(rs);
        return dataSet;
    }

}

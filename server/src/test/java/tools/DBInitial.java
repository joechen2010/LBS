package tools;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;

public class DBInitial {

    @Autowired
    DataSource dataSource;

    public void init() {
        try {
            System.out.println("insert test data to db");
            //DbUnitUtils.loadDbUnitData(dataSource);
        } catch (Exception e) {
            System.err.println("Error occurs during test data creating.....");
            e.printStackTrace();
        }
    }

}

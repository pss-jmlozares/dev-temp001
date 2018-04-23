package database;

import com.mchange.v2.c3p0.DataSources;
import modelclasses.Rec_sys_obj_prop_menusuite_org;
import org.eclnt.jsfserver.defaultscreens.Statusbar;
import org.javalite.activejdbc.Base;

import javax.sql.DataSource;
import java.util.List;


public class ConnectionPool {

    public static DataSource dataSourcePooled;
    private static ConnectionPool conn = null;
    private DataSource dataSourceUnPooled;

    public ConnectionPool() {
        myconnection();
    }

    public static ConnectionPool getInstance() {
        if (conn == null) {
            conn = new ConnectionPool();
        }
        return conn;
    }

    public void myconnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            dataSourceUnPooled = DataSources.unpooledDataSource("jdbc:mysql://localhost/ppav2", "root", "");
            dataSourcePooled = DataSources.pooledDataSource(dataSourceUnPooled);
        } catch (Throwable throwable) {
            Statusbar.outputSuccessWithPopup(throwable.toString());
        }
    }

    public List<Rec_sys_obj_prop_menusuite_org> getTypeAndObjectID(String runParam) {
        Base.open(dataSourceUnPooled);
        List<Rec_sys_obj_prop_menusuite_org> list = Rec_sys_obj_prop_menusuite_org.where("GUID=?", runParam);
        for(Rec_sys_obj_prop_menusuite_org dsf : list){

        }
        Base.close();
        return list;
    }
}
package managedbeans;


import database.ConnectionPool;
import modelclasses.*;
import org.eclnt.jsfserver.defaultscreens.Statusbar;
import org.javalite.activejdbc.Base;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GenerateUI {
    public static void main(String[] args){
        generateModel();
        generateList();

    }

    public static void generateMainUI(){
    try{
        ConnectionPool.getInstance();
        Base.open(ConnectionPool.dataSourcePooled);
        SuperGenerator gent = new SuperGenerator();
        ArrayList<String> arrayList = new ArrayList<>();
        List<Rec_sys_obj_prop_menusuite_org> runParam = Rec_sys_obj_prop_menusuite_org.where("Level = ? and Visible = ?", 1, "Yes" );
        for (Rec_sys_obj_prop_menusuite_org list : runParam) {
            arrayList.add(list.getString("GUID"));
        }
        gent.generateMainUI("MainMenu","D:\\Cramz\\PPAv2withImage\\webcontent\\",
                "D:\\Cramz\\PPAv2withImage\\src\\managedbeans\\",0,arrayList);
    }catch(Throwable t){
        t.printStackTrace();
        Statusbar.outputAlert(t.toString());
    }finally {
        Base.close();
    }
}

    public static void generateModel(){

        String classpath = "D:\\Cramz\\PPAv2withImage\\src\\modelclasses\\";
        String tablename = "posted_calculate_table";
        String PK = "id";
        int objectID = 300014;

        ArrayList<ArrayList<String>> arr = BuildDefaultListValues(tablename);
        ArrayList<String> defaultValues = arr.get(0);
        try{
            ConnectionPool.getInstance();
            Base.open(ConnectionPool.dataSourcePooled);
            SuperGenerator Model = new SuperGenerator();
            ArrayList<String> array = new ArrayList<>();
            List<Rec_sys_obj_prop_table_fieldcaption> fieldcaptions = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ?",objectID);
            for (Rec_sys_obj_prop_table_fieldcaption f: fieldcaptions) {
                array.add(f.getString("fieldname"));
            }
            Model.generateModel(classpath,tablename,PK,array,defaultValues);

            }catch(Throwable t){
            t.printStackTrace();
            }finally {
            Base.close();
        }
    }

    public static void generateList(){

        String tablename = "posted_calculate_table";
        ArrayList<ArrayList<String>> arr = BuildDefaultListValues(tablename);
        String listName="PostedCalculateTableList";
        int objectID=300014;

        ArrayList<String> dataTypeValues = arr.get(1);

        try{
            ConnectionPool.getInstance();
            Base.open(ConnectionPool.dataSourcePooled);
            SuperGenerator mod = new SuperGenerator();
            ArrayList<String> array = new ArrayList<>();
            List<Rec_sys_obj_prop_table_fieldcaption> fieldcaptions = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ?", objectID);
            for (Rec_sys_obj_prop_table_fieldcaption f: fieldcaptions) {
                array.add(f.getString("fieldname"));
            }
            mod.generateList(listName, "D:\\Cramz\\PPAv2withImage\\webcontent\\",
                    "D:\\Cramz\\PPAv2withImage\\src\\managedbeans\\" ,array,tablename,true,false,dataTypeValues);

        }catch(Throwable t){
            t.printStackTrace();
        }finally {
            Base.close();
        }
    }

    public static void generateCard(){
        SuperGenerator card = new SuperGenerator();

        int pageID = 300003;
        String tablename = "profile";
        String pageCardName = "ProfileCard";
        String listName="ProfileList";

        ArrayList<ArrayList<String>> arr = BuildDefaultListValues(tablename);
        ArrayList<String> dataTypeValues = arr.get(1);
        try{
            ConnectionPool.getInstance();
            Base.open(ConnectionPool.dataSourcePooled);
            ArrayList<String> array = new ArrayList<>();
            List<Rec_sys_pageproperties_pagecard_pane_paneitem> paneitems = Rec_sys_pageproperties_pagecard_pane_paneitem.where("pageid = ?", pageID);
            for (Rec_sys_pageproperties_pagecard_pane_paneitem list:paneitems) {
                    array.add(list.getString("fieldname"));
            }
            card.generateCard(pageCardName,listName,tablename,"D:\\Cramz\\PPAv2withImage\\webcontent\\","D:\\Cramz\\PPAv2withImage\\src\\managedbeans\\",array,dataTypeValues);
            }catch(Throwable t){
            t.printStackTrace();
            }finally {
            Base.close();
        }
    }

    public static void generateDocument(){
        SuperGenerator generator = new SuperGenerator();
        String listname = "ScheduleHeaderList";
        String tablename = "schedule_header";
        String subListname = "ScheduleLineList";
        String documentName = "ScheduleHeaderDocument";
        int pageID = 300010;

        String jspPath = "D:\\Cramz\\PPAv2withImage\\webcontent\\";
        String javaPath= "D:\\Cramz\\PPAv2withImage\\src\\managedbeans\\";
        ArrayList<ArrayList<String>> arrayLists = BuildDefaultListValues(tablename);
        ArrayList<String> defaultTypeValues = arrayLists.get(1);
            try{
                ConnectionPool.getInstance();
                Base.open(ConnectionPool.dataSourcePooled);
                ArrayList<String> arrayList = new ArrayList<>();
                ArrayList<Integer> integerArrayList = new ArrayList<>();
                List<Rec_sys_pageproperties_document_pane_paneitem> list =
                        Rec_sys_pageproperties_document_pane_paneitem.where("pageid = ?",pageID);
                for (Rec_sys_pageproperties_document_pane_paneitem paneitem:list) {
                    arrayList.add(paneitem.getString("fieldname"));
                }

                generator.generateDocumentCard(documentName,listname,tablename,subListname,pageID,jspPath,
                        javaPath,arrayList,integerArrayList,defaultTypeValues);
                }catch(Throwable t){
                t.printStackTrace();
                }finally {
                Base.close();
            }



    }

    public static ArrayList<ArrayList<String>> BuildDefaultListValues(String tableName){
        try{
            ArrayList<ArrayList<String>> multiDArray = new ArrayList<>();

            ArrayList<String> myList = new ArrayList<>();
            ArrayList<String> typeList = new ArrayList<>();

            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ppav2", "root", "");
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM "+tableName+"");
            ResultSetMetaData rsMetaData = rs.getMetaData();
            int numberOfColumns = rsMetaData.getColumnCount() + 1;

            ArrayList<String> list = new ArrayList<>();
            for (int ii = 1; ii < numberOfColumns; ii++) {
                list.add(rsMetaData.getColumnTypeName(ii).split(" ")[0]);
            }

            ArrayList<String> datatypes = new ArrayList<>();
            datatypes.add("BINARY");
            datatypes.add("INTEGER");
            datatypes.add("VARCHAR");
            datatypes.add("TINYINT");
            datatypes.add("DECIMAL");
            datatypes.add("DATETIME");
            datatypes.add("TINYBLOB");
            datatypes.add("BIGINT");
            datatypes.add("DATE");
            datatypes.add("TIME");
            datatypes.add("TIMESTAMP");
            datatypes.add("NVARCHAR");
            datatypes.add("LONGBLOB");

            ArrayList<String> defaultvalue = new ArrayList<>();
            defaultvalue.add("");
            defaultvalue.add("0");
            defaultvalue.add("");
            defaultvalue.add("0");
            defaultvalue.add("0.00");
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            defaultvalue.add(dateFormat.format(date));
            defaultvalue.add("");
            defaultvalue.add("0");
            DateFormat dateFormat2 = new SimpleDateFormat("yyyy/MM/dd");
            defaultvalue.add(dateFormat2.format(date));
            DateFormat dateFormat3 = new SimpleDateFormat("HH:mm:ss");
            defaultvalue.add(dateFormat3.format(date));
            defaultvalue.add("");
            defaultvalue.add("");
            defaultvalue.add("");

            for (String s : list){
                if (s.equals(datatypes.get(datatypes.indexOf(s)))){
                    myList.add(defaultvalue.get(datatypes.indexOf(s)));
                    typeList.add(datatypes.get(datatypes.indexOf(s)));
                }
            }

            for (int count = 0; count < myList.size(); count++){
                System.out.println(myList.get(count) + "=" + typeList.get(count));
            }

            multiDArray.add(myList);
            multiDArray.add(typeList);
            return multiDArray;
        }catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}

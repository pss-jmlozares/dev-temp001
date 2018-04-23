package managedbeans;

import database.ConnectionPool;


import modelclasses.Rec_import_time_table;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.eclnt.editor.annotations.CCGenClass;
import org.eclnt.jsfserver.bufferedcontent.DefaultUploadContent;
import org.eclnt.jsfserver.bufferedcontent.UploadContentMgr;
import org.eclnt.jsfserver.defaultscreens.Statusbar;
import org.eclnt.jsfserver.elements.events.BaseActionEventPopupMenuItem;
import org.eclnt.jsfserver.elements.impl.FIXGRIDItem;
import org.eclnt.jsfserver.elements.impl.FIXGRIDListBinding;
import org.eclnt.jsfserver.elements.impl.ROWDYNAMICCONTENTBinding;
import org.eclnt.jsfserver.pagebean.PageBean;
import org.javalite.activejdbc.Base;


import javax.faces.event.ActionEvent;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@CCGenClass(expressionBase = "#{d.TableImporter}")

public class TableImporter extends PageBean implements Serializable {

    MyGridListBinding m_rowBind = new MyGridListBinding();
    ROWDYNAMICCONTENTBinding m_dynamicList = new ROWDYNAMICCONTENTBinding();
    ROWDYNAMICCONTENTBinding m_dynamicRibbon = new ROWDYNAMICCONTENTBinding();

    TableImporter.ICloseListener closeListener;
    private IListener m_listener;
    private AppTool tool;
    byte[] m_excelBytes = new byte[0];

    public class ExcelUpload extends DefaultUploadContent {
        public void beginPassing(){}
        public void passClientFile(String fileName, byte[] content)
        {
            m_excelBytes = content;
        }
        public void endPassing(){}
    }

    public TableImporter(){
        UploadContentMgr.add(m_excelUpload);//Storing the Uploaded data
    }

    ExcelUpload m_excelUpload = new ExcelUpload();
    private MyRow Record;
    public MyGridListBinding getRowBind() { return m_rowBind; }
    public ROWDYNAMICCONTENTBinding getDynamicList() { return m_dynamicList; }
    public ROWDYNAMICCONTENTBinding getDynamicRibbon() { return m_dynamicRibbon;}
    public String getUploadURL() { return m_excelUpload.getURL(); }


    public void onCalculate(ActionEvent event) {

        try {
            FileInputStream fs = new FileInputStream("C:/Users/Cramz/Desktop/Test.xlsx");
            XSSFWorkbook wb = new XSSFWorkbook(fs);
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            DataFormatter df = new DataFormatter();

            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();
            int cols = 0; // No of columns
            int tmp = 0;

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for(int i = 1; i <= rows; i++) {
                row = sheet.getRow(i);
                if(row != null) {
                    tmp = sheet.getRow(i).getPhysicalNumberOfCells();
                    if(tmp > cols) cols = tmp;

                    Record = new MyRow();
                    Record.setDate(String.valueOf(df.formatCellValue(row.getCell(0))));
                    Record.setDay(String.valueOf(row.getCell(1)));
                    Record.setProfile_Code(String.valueOf(df.formatCellValue(row.getCell(2))));
                    Record.setName(String.valueOf(row.getCell(3)));
                    Record.setDepartment(String.valueOf(row.getCell(4)));
                    Record.setPosition(String.valueOf(row.getCell(5)));
                    Record.setMorning_IN(String.valueOf(df.formatCellValue(row.getCell(6))));
                    Record.setMorning_OUT(String.valueOf(df.formatCellValue(row.getCell(7))));
                    Record.setAfternoon_IN(String.valueOf(df.formatCellValue(row.getCell(8))));
                    Record.setAfternoon_OUT(String.valueOf(df.formatCellValue(row.getCell(9))));
                    m_rowBind.getItems().add(Record);

                    Statusbar.outputSuccessWithPopup(String.valueOf(row.getCell(6)));
                }
            }
          /*  for(int r = 2; r < rows; r++) {
                row = sheet.getRow(r);
                if(row != null) {

                }
            }*/
        }
        catch(Throwable t) {
            t.printStackTrace();
            Statusbar.outputAlert(t.toString());
        }
    }

    public void onOK(javax.faces.event.ActionEvent event) {
        closeListener.reactOnClose();
    }

    public void onImportFile(javax.faces.event.ActionEvent event) {
        try {
            byte[] bytes = m_excelBytes;
            importData(bytes);

        } catch (Throwable t) {
            t.printStackTrace();
            Statusbar.outputAlert(t.toString());
        }
    }

    //TODO Data from Excel to FixGrid
    private void importData(byte[] excelFile) {
        try {
            ConnectionPool.getInstance();
            Base.open(ConnectionPool.dataSourcePooled);
            ByteArrayInputStream bis = new ByteArrayInputStream(excelFile);
            XSSFWorkbook wb = new XSSFWorkbook(bis);
            XSSFSheet sheet = wb.getSheetAt(0);
            XSSFRow row;
            DataFormatter df = new DataFormatter();
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("h:mm a");
            DateTimeFormatter dateFormat0 = DateTimeFormatter.ofPattern("M/dd/yy");
            DateTimeFormatter dateFormat1 = DateTimeFormatter.ofPattern("M/d/yy");
            int rows; // No of rows
            rows = sheet.getPhysicalNumberOfRows();

            // This trick ensures that we get the data properly even if it doesn't start from first few rows
            for(int i = 0; i <= rows; i++) {
                row = sheet.getRow(i);
                if(row != null) {
                    Rec_import_time_table itt = new Rec_import_time_table();

                    LocalTime time1 = LocalTime.parse(String.valueOf(df.formatCellValue(row.getCell(6))),timeFormat);
                    LocalTime time2 = LocalTime.parse(String.valueOf(df.formatCellValue(row.getCell(7))),timeFormat);
                    LocalTime time3 = LocalTime.parse(String.valueOf(df.formatCellValue(row.getCell(8))),timeFormat);
                    LocalTime time4 = LocalTime.parse(String.valueOf(df.formatCellValue(row.getCell(9))),timeFormat);

                    if (df.formatCellValue(row.getCell(0)).equals(dateFormat0)){
                        LocalDate date = LocalDate.parse(String.valueOf(df.formatCellValue(row.getCell(0))),dateFormat0);
                        itt.set("Date",String.valueOf(date));
                    } else {
                        LocalDate date = LocalDate.parse(String.valueOf(df.formatCellValue(row.getCell(0))),dateFormat1);
                        itt.set("Date",String.valueOf(date));
                    }
                    itt.set("timestamp","");
                    itt.set("Day",String.valueOf(row.getCell(1)));
                    itt.set("Profile_Code",String.valueOf(df.formatCellValue(row.getCell(2))));
                    itt.set("Name",String.valueOf(row.getCell(3)));
                    itt.set("Department",String.valueOf(row.getCell(4)));
                    itt.set("Position",String.valueOf(row.getCell(5)));
                    itt.set("morning_IN",String.valueOf(time1));
                    itt.set("morning_OUT",String.valueOf(time2));
                    itt.set("afternoon_IN",String.valueOf(time3));
                    itt.set("afternoon_OUT",String.valueOf(time4));

                    if (itt.saveIt()){
                        Statusbar.outputSuccess("Import Success!!");
                    } else {
                        Statusbar.outputAlert("Nothing have been imported!!");
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
            Statusbar.outputAlert(t.toString());
        } finally {
            Base.close();
        }
    }

    public String getPageName() {
        return "/TableImporter.jsp";
    }

    public String getRootExpressionUsedInPage() {
        return "#{d.TableImporter}";
    }

    /* Initialization of the bean. Add any parameter that is required within your scenario. */
    public void prepare(TableImporter.ICloseListener listener) {
        try {
            closeListener = listener;
        } catch (Throwable t) {
            Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
        }
    }

    public void prepare(IListener listener) {
        m_listener = listener;
    }

    public class MyGridListBinding extends FIXGRIDListBinding<MyRow> {
        @Override
        protected void onPopupMenuItem(BaseActionEventPopupMenuItem event) {
            if ("NEW".equals(event.getCommand())) {

            } else if ("COLUMNS".equals(event.getCommand())) {
                m_rowBind.onEditColumnDetails(null);
            }
        }
    }
    /* Listener to the user of the page bean. */
    public interface IListener {
    }

    public interface ICloseListener {
        void reactOnClose();
    }

    public class MyRow extends FIXGRIDItem implements java.io.Serializable {
        String i_Profile_Code;
        String i_afternoon_OUT;
        String i_afternoon_IN;
        String i_morning_OUT;
        String i_morning_IN;
        String i_Position;
        String i_Department;
        String i_Name;

        String i_Day;
        String i_Date;

        public String getProfile_Code() {
            return i_Profile_Code;
        }
        public void setProfile_Code(String value) {
            this.i_Profile_Code = value;
        }

        public String getAfternoon_OUT() {
            return i_afternoon_OUT;
        }
        public void setAfternoon_OUT(String value) {
            this.i_afternoon_OUT = value;
        }

        public String getAfternoon_IN() { return i_afternoon_IN; }
        public void setAfternoon_IN(String value) { this.i_afternoon_IN = value; }

        public String getMorning_OUT() { return i_morning_OUT; }
        public void setMorning_OUT(String value) { this.i_morning_OUT = value; }

        public String getMorning_IN() { return i_morning_IN; }
        public void setMorning_IN(String value) { this.i_morning_IN = value; }

        public String getPosition() { return i_Position; }
        public void setPosition(String value) { this.i_Position = value; }

        public String getDepartment() { return i_Department; }
        public void setDepartment(String value) { this.i_Department = value; }

        public String getName() { return i_Name; }
        public void setName(String value) { this.i_Name = value; }

        public String getDay() { return i_Day; }
        public void setDay(String value) { this.i_Day = value; }

        public String getDate() { return i_Date; }
        public void setDate(String value) { this.i_Date = value; }

        public void onRowPopupMenuItem(BaseActionEventPopupMenuItem event){
            if ("REMOVE".equals(event.getCommand())) {
//                onDelete(null);
            } else if ("NEW".equals(event.getCommand())) {
//                onNew(null);
            } else if ("EDIT".equals(event.getCommand())) {
//                onEdit(null);
            }
        }


    }

    // ------------------------------------------------------------------------
    // private usage
    // ------------------------------------------------------------------------
}

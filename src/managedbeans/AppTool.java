package managedbeans;

import database.ConnectionPool;
import modelclasses.*;
import org.eclnt.jsfserver.defaultscreens.OKPopup;
import org.eclnt.jsfserver.defaultscreens.Statusbar;
import org.javalite.activejdbc.Base;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AppTool {
    public String hashPassword(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(s.getBytes());
            byte[] b = md.digest();
            StringBuffer stringBuffer = new StringBuffer();
            for (byte bb : b) {
                stringBuffer.append(Integer.toHexString(bb & 0xff).toString());
            }
            return stringBuffer.toString();
        }catch (Throwable t){
            alert(t.toString());
            return null;
        }
    }

    public boolean intToBoolean(int value) {
        boolean flag = (value == 1) ? true : false;
        return flag;
    }

    public String intToBooleanToString(int value) {
        String flag = (intToBoolean(value) != true) ? "false" : "true";
        return flag;
    }

    public String booleanToString(String value) {
        String flag = (value.equals("true")) ? "1" : "0";
        return flag;
    }

    public int StringToBooleanToInt(String value) {
        return value.equals("true") ? 1 : 0;
    }

    public String formatSQLDate(java.sql.Date val) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(val);
    }

    public String formatJavaDate(java.util.Date val) {
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(val);
    }

    public String formatJavaTime(java.util.Date val) {
        DateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(val);
    }

    public String formatSQLTime(java.sql.Date val) {
        DateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(val);
    }

    public String formatTime24H(String val) {
        return LocalTime.parse(val, DateTimeFormatter.ofPattern("h:mm a")).
                format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String formatToDefaultDate(String val) {
        return LocalDate.parse(val, DateTimeFormatter.ofPattern("MM/dd/yyyy")).
                format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    public String formatToDate(String val) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date = dateFormat.parse(val);
            return dateFormat.format(date);
        }catch(Throwable t){
            err(t.toString());
            t.printStackTrace();
            return null;
        }
    }

    public String formatToDate(java.util.Date val) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            return dateFormat.format(val);
        }catch(Throwable t){
            err(t.toString());
            t.printStackTrace();
            return null;
        }
    }

    public String formatToDateTime(java.util.Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
        return dateFormat.format(date);
    }

    public String formatToDateTime(String date) {
        try{
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy/MM/dd")).format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        }catch(Throwable t){
            err(t.toString());
            t.printStackTrace();
            return null;
        }
    }

    public String roundQty(String qty) {
        String value = null;
        try {
            //TODO Get Unit Amount Rounding Precision
            List<Rec_general_ledger_setup> generalLedgerSetups = Rec_general_ledger_setup.findAll();
            for (Rec_general_ledger_setup generalLedgerSetup : generalLedgerSetups) {
                BigDecimal bigDecimal = new BigDecimal(qty);
                if (generalLedgerSetup.getInteger("UnitAmount_Rounding_Precision") != 0) {
                    int no = generalLedgerSetup.getInteger("UnitAmount_Rounding_Precision");

                    value = String.format("%,."+no+"f", bigDecimal.setScale(no, RoundingMode.HALF_UP));
                } else {
                    value = String.format("%,.1f", bigDecimal.setScale(2, RoundingMode.HALF_UP));
                }
            }

            return value;
        } catch (Throwable t) {

            Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
            return null;
        }
    }


//todo ============================Popup alerts==================================
    public void alert(String text) {
        Statusbar.outputAlert(text).setLeftTopReferenceCentered();
    }
    public void success(String text) {
        Statusbar.outputSuccessWithPopup(text).setLeftTopReferenceCentered();
    }
    public void success(float text) {
        Statusbar.outputSuccessWithPopup(String.valueOf(text)).setLeftTopReferenceCentered();
    }
    public void success(long text) {
        Statusbar.outputSuccessWithPopup(String.valueOf(text)).setLeftTopReferenceCentered();
    }
    public void success(int text) {
        Statusbar.outputSuccessWithPopup(String.valueOf(text)).setLeftTopReferenceCentered();
    }
    public void msg(String text) {
        Statusbar.outputMessage(text);
    }
    public void err(String text) {
        Statusbar.outputError(text);
    }
    public void pop(String text) {
        OKPopup p = OKPopup.createInstance("Details", text);
        p.getModalPopup().maximize(true);
        p.setTextAlign("left");
    }
// todo ==========================================================================


    public ArrayList<String> mySeparatorMethodForList(String column_name, int objectID){
        try{
            ArrayList<String> contentlist = new ArrayList<>();
            List<Rec_sys_obj_prop_table_fieldcaption> ff = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ? And showColumn = ? And fieldname = ?", objectID, "Yes", column_name);
            for (Rec_sys_obj_prop_table_fieldcaption f : ff) {
                if (f.getString("componentType").equals("ComboBox")) {
                    String[] content = f.getString("optionString").split(",");
                    for (String s : content) {
                        contentlist.add(s);
                    }
                }
            }
            return contentlist;
        }catch (Throwable t){
            alert(t.toString());
            return null;
        }
    }

    public int mySeparatorMethodForCard(String column_name, String fieldvalue, int listObjID){
        try{
            int value = 0;
            ConnectionPool.getInstance();
            Base.open(ConnectionPool.dataSourcePooled);
            ArrayList<String> contentlist = new ArrayList<>();
            List<Rec_sys_obj_prop_table_fieldcaption> ff = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ? And showColumn = ? AND fieldname = ?", listObjID, "Yes", column_name);
            for (Rec_sys_obj_prop_table_fieldcaption f : ff) {
                if (f.getString("componentType").equals("ComboBox")) {
                    String[] content = f.getString("optionString").split(",");
                    for (String s : content) {
                        contentlist.add(s);
                    }
                    value = contentlist.indexOf(fieldvalue);
                }
            }
            return value;
        }catch (Throwable t){
            alert(t.toString());
            return 0;
        }finally {
            Base.close();
        }
    }

}

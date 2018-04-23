package modelclasses;
import org.eclnt.jsfserver.defaultscreens.*;
import org.javalite.activejdbc.*;
import org.javalite.activejdbc.annotations.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.*;
import database.ConnectionPool;
@Table("calculate_table")
@CompositePK("id")
public class Rec_calculate_table extends Model{

	public boolean insert(ArrayList<String> list, int listObjID){
		return onInsert(list, listObjID);
	}

	public boolean modify(ArrayList<String> list, int listObjID) {
		return onModify(list, listObjID);
 	}

	public boolean delete(String code) {
		return onDelete(code);
	}

	private String preInsert(int listObjID, String code){
		try{
			ArrayList<String> captionArray = new ArrayList<String>();
			List<Rec_sys_obj_prop_table_fieldcaption> captions = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ?", listObjID);
			for(Rec_sys_obj_prop_table_fieldcaption caption : captions) {
				String fieldname = caption.getString("fieldname");
				captionArray.add(fieldname);
			}
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			
			ArrayList<String> array = new ArrayList<String>();
            String timestamp = ""; 
            String id = code; 
            String Date = dateFormat.format(date); 
            String Day = ""; 
            String Profile_Code = ""; 
            String Profile_Name = ""; 
            String Department = ""; 
            String Position = ""; 
            String Actual_morning_IN = "17:20:57"; 
            String Scheduled_morning_IN = "17:20:57"; 
            String Actual_morning_OUT = "17:20:57"; 
            String Scheduled_morning_OUT = "17:20:57"; 
            String Actual_afternoon_IN = "17:20:57"; 
            String Scheduled_afternoon_IN = "17:20:57"; 
            String Actual_afternoon_OUT = "17:20:57"; 
            String Scheduled_afternoon_OUT = "17:20:57"; 
            String Late = "0"; 
            String Absent = "0"; 
            String UnderTime = "0";
            String OverTime = "0";

			array.add(timestamp);
			array.add(id);
			array.add(Date);
			array.add(Day);
			array.add(Profile_Code);
			array.add(Profile_Name);
			array.add(Department);
			array.add(Position);
			array.add(Actual_morning_IN);
			array.add(Scheduled_morning_IN);
			array.add(Actual_morning_OUT);
			array.add(Scheduled_morning_OUT);
			array.add(Actual_afternoon_IN);
			array.add(Scheduled_afternoon_IN);
			array.add(Actual_afternoon_OUT);
			array.add(Scheduled_afternoon_OUT);
			array.add(Late);
			array.add(Absent);
			array.add(UnderTime);
			array.add(OverTime);
			Rec_calculate_table record = new Rec_calculate_table();
			for(int count = 0; count < captionArray.size(); count++) {
				record.set(captionArray.get(count), array.get(count));
			}
			record.saveIt();
			return id;
		}catch(Throwable t){
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return null;
		}
	}

	private boolean onInsert(ArrayList<String> row, int listObjID) {
		try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);
			Base.openTransaction();
			String code = preInsert(listObjID, row.get(1));
			ArrayList<String> captionArray = new ArrayList<String>();
			List<Rec_sys_obj_prop_table_fieldcaption> captions = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ?", listObjID);
			for(Rec_sys_obj_prop_table_fieldcaption caption : captions) {
				String fieldname = caption.getString("fieldname");
				captionArray.add(fieldname);
			}
			Rec_calculate_table rec = Rec_calculate_table.findFirst("id = ?", code);
			for(int count = 2; count < captionArray.size(); count++) {
				rec.set(captionArray.get(count), row.get(count));
			}
			rec.saveIt();
			Base.commitTransaction();
			return true;
		}catch(Throwable t){
			Base.rollbackTransaction();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return false;
		}finally{
			Base.close();
		}
	}
	private boolean onModify(ArrayList<String> row, int listObjID){
		try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);
			Base.openTransaction();
			ArrayList<String> captionArray = new ArrayList<String>();
			List<Rec_sys_obj_prop_table_fieldcaption> captions = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ?", listObjID);
			for(Rec_sys_obj_prop_table_fieldcaption caption : captions) {
				String fieldname = caption.getString("fieldname");
				captionArray.add(fieldname);
			}
			Rec_calculate_table rec = Rec_calculate_table.findFirst("id = ?", row.get(1));
			for(int count = 2; count < captionArray.size(); count++) {
				rec.set(captionArray.get(count), row.get(count));
			}
			rec.saveIt();
			Base.commitTransaction();
			return true;
		}catch(Throwable t){
			Base.rollbackTransaction();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return false;
		}finally{
			Base.close();
		}
	}
	private boolean onDelete(String code){
		try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);
			Base.openTransaction();
			Rec_calculate_table rec = Rec_calculate_table.findFirst("id = ?", code);
			rec.delete();
			Base.commitTransaction();
			return true;
		}catch(Throwable t){
			Base.rollbackTransaction();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return false;
		}finally {
			Base.close();
		}
	}
}

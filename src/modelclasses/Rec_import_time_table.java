package modelclasses;
import org.eclnt.jsfserver.defaultscreens.*;
import org.javalite.activejdbc.*;
import org.javalite.activejdbc.annotations.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.*;
import database.ConnectionPool;
@Table("import_time_table")
@CompositePK({"id","Date","Profile_Code"})
public class Rec_import_time_table extends Model{

	public boolean insert(ArrayList<String> list, int listObjID){
		return onInsert(list, listObjID);
	}

	public boolean modify(ArrayList<String> list, int listObjID) {
		return onModify(list, listObjID);
 	}

	public boolean delete(String code) {
		return onDelete(code);
	}

	private String preInsert(int listObjID, String code,String code1,String code2){
		try{
			ArrayList<String> captionArray = new ArrayList<String>();
			List<Rec_sys_obj_prop_table_fieldcaption> captions = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ?", listObjID);
			for(Rec_sys_obj_prop_table_fieldcaption caption : captions) {
				String fieldname = caption.getString("fieldname");
				captionArray.add(fieldname);
			}
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat DayFormat = new SimpleDateFormat("EEEE");
			Date date = new Date();
			
			ArrayList<String> array = new ArrayList<String>();
            String timestamp = ""; 
            String id = code;
            String Date = code1;
            String Day = DayFormat.format(date);
            String Profile_Code = code2;
            String Name = ""; 
            String Department = ""; 
            String Position = ""; 
            String morning_IN = timeFormat.format(date);
            String morning_OUT = timeFormat.format(date);
            String afternoon_IN = timeFormat.format(date);
            String afternoon_OUT = timeFormat.format(date);

			array.add(timestamp);
			array.add(id);
			array.add(Date);
			array.add(Day);
			array.add(Profile_Code);
			array.add(Name);
			array.add(Department);
			array.add(Position);
			array.add(morning_IN);
			array.add(morning_OUT);
			array.add(afternoon_IN);
			array.add(afternoon_OUT);
			Rec_import_time_table record = new Rec_import_time_table();
			for(int count = 0; count < captionArray.size(); count++) {
				record.set(captionArray.get(count), array.get(count));
			}
			record.saveIt();
			return id;
		}catch(Throwable t){
		    t.printStackTrace();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return null;
		}
	}

	private boolean onInsert(ArrayList<String> row, int listObjID) {
		try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);
			Base.openTransaction();
			String code = preInsert(listObjID, row.get(1),row.get(2),row.get(4));
			ArrayList<String> captionArray = new ArrayList<String>();
			List<Rec_sys_obj_prop_table_fieldcaption> captions = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ?", listObjID);
			for(Rec_sys_obj_prop_table_fieldcaption caption : captions) {
				String fieldname = caption.getString("fieldname");
				captionArray.add(fieldname);
			}
			Rec_import_time_table rec = Rec_import_time_table.findFirst("id = ?", code);
			for(int count = 2; count < captionArray.size(); count++) {
				rec.set(captionArray.get(count), row.get(count));
			}
			rec.saveIt();
			Base.commitTransaction();
			return true;
		}catch(Throwable t){
			Base.rollbackTransaction();
			t.printStackTrace();
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
			Rec_import_time_table rec = Rec_import_time_table.findFirst("id = ?", row.get(1));
			for(int count = 2; count < captionArray.size(); count++) {
				rec.set(captionArray.get(count), row.get(count));
			}
			rec.saveIt();
			Base.commitTransaction();
			return true;
		}catch(Throwable t){
			Base.rollbackTransaction();
			t.printStackTrace();
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
			Rec_import_time_table rec = Rec_import_time_table.findFirst("id = ?", code);
			rec.delete();
			Base.commitTransaction();
			return true;
		}catch(Throwable t){
			Base.rollbackTransaction();
			t.printStackTrace();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return false;
		}finally {
			Base.close();
		}
	}
}

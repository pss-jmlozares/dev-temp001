package modelclasses;
import org.eclnt.jsfserver.defaultscreens.*;
import org.javalite.activejdbc.*;
import org.javalite.activejdbc.annotations.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.*;
import database.ConnectionPool;
@Table("schedule_header")
@CompositePK("Code")
public class Rec_schedule_header extends Model{

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
            String Code = code; 
            String Description = ""; 
            String Cutoff_fromDate = dateFormat.format(date);
            String Cutoff_toDate = dateFormat.format(date);

			array.add(timestamp);
			array.add(Code);
			array.add(Description);
			array.add(Cutoff_fromDate);
			array.add(Cutoff_toDate);
			Rec_schedule_header record = new Rec_schedule_header();
			for(int count = 0; count < captionArray.size(); count++) {
				record.set(captionArray.get(count), array.get(count));
			}
			record.saveIt();
			return Code;
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
			String code = preInsert(listObjID, row.get(1));
			ArrayList<String> captionArray = new ArrayList<String>();
			List<Rec_sys_obj_prop_table_fieldcaption> captions = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ?", listObjID);
			for(Rec_sys_obj_prop_table_fieldcaption caption : captions) {
				String fieldname = caption.getString("fieldname");
				captionArray.add(fieldname);
			}
			Rec_schedule_header rec = Rec_schedule_header.findFirst("Code = ?", code);
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
			Rec_schedule_header rec = Rec_schedule_header.findFirst("Code = ?", row.get(1));
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
			Rec_schedule_header rec = Rec_schedule_header.findFirst("Code = ?", code);
			Rec_schedule_line.delete("Schedule_Code = ?", (Object) code);
			rec.delete();
			Base.commitTransaction();
			return true;
		}catch(Throwable t){
			Base.rollbackTransaction();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
            t.printStackTrace();
			return false;
		}finally {
			Base.close();
		}
	}
}

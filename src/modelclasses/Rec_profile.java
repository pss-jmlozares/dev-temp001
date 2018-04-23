package modelclasses;
import org.eclnt.jsfserver.defaultscreens.*;
import org.javalite.activejdbc.*;
import org.javalite.activejdbc.annotations.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.*;
import database.ConnectionPool;
@Table("profile")
@CompositePK("Profile_No_")
public class Rec_profile extends Model{

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
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
			Date date = new Date();
			
			ArrayList<String> array = new ArrayList<String>();
            String timestamp = ""; 
            String Profile_No_ = code; 
            String First_Name = ""; 
            String Middle_Name = ""; 
            String Last_Name = ""; 
            String Age = "0"; 
            String Gender = "0";
            String BirthDate = dateFormat.format(date); 
            String BirthPlace = ""; 
            String Religion = "0"; 
            String Picture = ""; 
            String Department = ""; 
            String Position = ""; 
            String default_morning_IN = timeFormat.format(date);
            String default_morning_OUT = timeFormat.format(date);
            String default_afternoon_IN = timeFormat.format(date);
            String default_afternoon_OUT = timeFormat.format(date);
            String Rest_Day = "SUNDAY";

			array.add(timestamp);
			array.add(Profile_No_);
			array.add(First_Name);
			array.add(Middle_Name);
			array.add(Last_Name);
			array.add(Age);
			array.add(Gender);
			array.add(BirthDate);
			array.add(BirthPlace);
			array.add(Religion);
			array.add(Picture);
			array.add(Department);
			array.add(Position);
			array.add(default_morning_IN);
			array.add(default_morning_OUT);
			array.add(default_afternoon_IN);
			array.add(default_afternoon_OUT);
            array.add(Rest_Day);
			Rec_profile record = new Rec_profile();
			for(int count = 0; count < captionArray.size(); count++) {
				record.set(captionArray.get(count), array.get(count));
			}
			record.saveIt();
			return Profile_No_;
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
			Rec_profile rec = Rec_profile.findFirst("Profile_No_ = ?", code);
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
			Rec_profile rec = Rec_profile.findFirst("Profile_No_ = ?", row.get(1));
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
			Rec_profile rec = Rec_profile.findFirst("Profile_No_ = ?", code);
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

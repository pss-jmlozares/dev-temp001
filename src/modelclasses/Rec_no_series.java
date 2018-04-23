package modelclasses;
import org.eclnt.jsfserver.defaultscreens.*;
import org.javalite.activejdbc.*;
import org.javalite.activejdbc.annotations.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.*;
import database.ConnectionPool;
@Table("no_series")
@CompositePK("Code")
public class Rec_no_series extends Model{
	public boolean insert(ArrayList<String> list, int listObjID){
		if(onInsert(list, listObjID)){
			return true;
		}else{
			return false;
		}
	}
	public boolean modify(ArrayList<String> list, int listObjID) {
		if (onModify(list, listObjID)) {
			return true;
		}else{
			return false;
		}
 	}
	public boolean delete(String code) {
		if (onDelete(code)) {
			return true;
		}else{
			return false;
		}
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
	        String Default_Nos_ = "0";
	        String Manual_Nos_ = "0";
	        String Date_Order = "0";
	        String Dist_Location_Prefix = "0";
			
		    array.add(timestamp);
		    array.add(Code);
		    array.add(Description);
		    array.add(Default_Nos_);
		    array.add(Manual_Nos_);
		    array.add(Date_Order);
		    array.add(Dist_Location_Prefix);
			Rec_no_series record = new Rec_no_series();
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
			Rec_no_series rec = Rec_no_series.findFirst("Code = ?", code);
			for(int count = 2; count < captionArray.size(); count++) {
				rec.set(captionArray.get(count), row.get(count));
			}
			rec.saveIt();
			Base.commitTransaction();
			return true;
		}catch(Throwable t){
		    t.printStackTrace();
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
			Rec_no_series rec = Rec_no_series.findFirst("Code = ?", row.get(1));
			for(int count = 2; count < captionArray.size(); count++) {
				rec.set(captionArray.get(count), row.get(count));
			}
			rec.saveIt();
			Base.commitTransaction();
			return true;
		}catch(Throwable t){
		    t.printStackTrace();
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
			Rec_no_series rec = Rec_no_series.findFirst("Code = ?", code);
			Rec_no_series_line.delete("Series_Code = ?", (Object) code);
			rec.delete();
			Base.commitTransaction();
			return true;
		}catch(Throwable t){
		    t.printStackTrace();
			Base.rollbackTransaction();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return false;
		}finally {
			Base.close();
		}
	}
}

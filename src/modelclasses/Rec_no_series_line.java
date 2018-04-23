package modelclasses;
import org.eclnt.jsfserver.defaultscreens.*;
import org.javalite.activejdbc.*;
import org.javalite.activejdbc.annotations.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.*;
import database.ConnectionPool;
@Table("no_series_line")
@CompositePK({"Series_Code","Line_No_"})
public class Rec_no_series_line extends Model{
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
	public boolean delete(String code1, String code2) {
		if (onDelete(code1, code2)) {
			return true;
		}else{
			return false;
		}
	}
	private String preInsert(int listObjID, String code1, String code2){
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
	        String Series_Code = code1;
	        String Line_No_ = code2;
	        String Starting_Date = dateFormat.format(date);
	        String Starting_No_ = "";
	        String Ending_No_ = "";
	        String Warning_No_ = "";
	        String Incrementby_No_ = "0";
	        String Last_No_Used = "";
	        String Open = "0";
	        String Last_Date_Used = dateFormat.format(date);
			
		    array.add(timestamp);
		    array.add(Series_Code);
		    array.add(Line_No_);
		    array.add(Starting_Date);
		    array.add(Starting_No_);
		    array.add(Ending_No_);
		    array.add(Warning_No_);
		    array.add(Incrementby_No_);
		    array.add(Last_No_Used);
		    array.add(Open);
		    array.add(Last_Date_Used);
			Rec_no_series_line record = new Rec_no_series_line();
			for(int count = 0; count < captionArray.size(); count++) {
				record.set(captionArray.get(count), array.get(count));
			}
			record.saveIt();
			return Series_Code;
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
			String code = preInsert(listObjID, row.get(1), row.get(2));
			ArrayList<String> captionArray = new ArrayList<String>();
			List<Rec_sys_obj_prop_table_fieldcaption> captions = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ?", listObjID);
			for(Rec_sys_obj_prop_table_fieldcaption caption : captions) {
				String fieldname = caption.getString("fieldname");
				captionArray.add(fieldname);
			}


			Rec_no_series_line rec = Rec_no_series_line.findFirst("Series_Code = ? And Line_No_ = ?", code, row.get(2));
			for(int count = 3; count < captionArray.size(); count++) {
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


			Rec_no_series_line rec = Rec_no_series_line.findFirst("Series_Code = ? And Line_No_ = ?", row.get(1), row.get(2));
			for(int count = 3; count < captionArray.size(); count++) {
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
	private boolean onDelete(String code1, String code2){
		try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);
			Base.openTransaction();
			Rec_no_series_line rec = Rec_no_series_line.findFirst("Series_Code = ? And Line_No_ = ?", code1, code2);
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

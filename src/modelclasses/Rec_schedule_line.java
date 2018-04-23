package modelclasses;
import org.eclnt.jsfserver.defaultscreens.*;
import org.javalite.activejdbc.*;
import org.javalite.activejdbc.annotations.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.*;
import database.ConnectionPool;
@Table("schedule_line")
@CompositePK({"Schedule_Code","Line_No"})
public class Rec_schedule_line extends Model{

	public boolean insert(ArrayList<String> list, int listObjID){
		return onInsert(list, listObjID);
	}

	public boolean modify(ArrayList<String> list, int listObjID) {
		return onModify(list, listObjID);
 	}

	public boolean delete(String code,String code1) {
		return onDelete(code,code1);
	}

	private String preInsert(int listObjID, String code){
		try{
			ArrayList<String> captionArray = new ArrayList<String>();
			List<Rec_sys_obj_prop_table_fieldcaption> captions = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ?", listObjID);
			for(Rec_sys_obj_prop_table_fieldcaption caption : captions) {
				String fieldname = caption.getString("fieldname");
				captionArray.add(fieldname);
			}
            //TODO GET LINENO
            int no;
            Object maxCol = Base.firstCell("select max(Line_No) from schedule_line where Schedule_Code = ?", code);
            no = (maxCol == null) ? 0 : Integer.parseInt(String.valueOf(maxCol));
            no += 1;

			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
            DateFormat DayFormat = new SimpleDateFormat("EEEE");
			Date date = new Date();
			
			ArrayList<String> array = new ArrayList<String>();
            String timestamp = ""; 
            String Schedule_Code = code; 
            String Line_No = String.valueOf(no);
            String Date = dateFormat.format(date);
            String Day = DayFormat.format(date);
            String Profile_Code = ""; 
            String Name = ""; 
            String Department = ""; 
            String Position = ""; 
            String morning_IN = timeFormat.format(date);
            String morning_OUT = timeFormat.format(date);
            String afternoon_IN = timeFormat.format(date);
            String afternoon_OUT = timeFormat.format(date);

			array.add(timestamp);
			array.add(Schedule_Code);
			array.add(Line_No);
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
			Rec_schedule_line record = new Rec_schedule_line();
			for(int count = 0; count < captionArray.size(); count++) {
				record.set(captionArray.get(count), array.get(count));
			}
			record.saveIt();
			return Schedule_Code;
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

            //TODO GET LINENO
            int no;
            Object maxCol1 = Base.firstCell("select max(Line_No) from schedule_line where Schedule_Code = ?", code);
            if (maxCol1 == null) no = 0;
            else no = Integer.parseInt(String.valueOf(maxCol1));


			Rec_schedule_line rec = Rec_schedule_line.findFirst("Schedule_Code = ? and Line_No = ?", code,no);
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
			Rec_schedule_line rec = Rec_schedule_line.findFirst("Schedule_Code = ?", row.get(1));
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
	private boolean onDelete(String code,String code1){
		try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);
			Base.openTransaction();
			Rec_schedule_line rec = Rec_schedule_line.findFirst("Schedule_Code = ? and Line_No= ?", code,code1);
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

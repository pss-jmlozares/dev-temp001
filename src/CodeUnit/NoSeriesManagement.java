package CodeUnit;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import modelclasses.*;
import org.eclnt.jsfserver.defaultscreens.Statusbar;
import org.eclnt.jsfserver.util.HttpSessionAccess;
import org.javalite.activejdbc.Base;

import database.ConnectionPool;
import org.javalite.activejdbc.StaleModelException;

public class NoSeriesManagement {
	private Rec_no_series_line rec = new Rec_no_series_line();

	private String noSeriesCode;
	public String getNextNo(String noSeriesCode, boolean modifySeries) {
		this.noSeriesCode = noSeriesCode;
		if (!"".equals(noSeriesCode)) {
			String lastNoUsed = null;
			String startingNo = null;
			int incrementByNo = 0;
			try {
			    ConnectionPool.getInstance();
				Base.open(ConnectionPool.dataSourcePooled);

				List<Rec_no_series_line> lines = Rec_no_series_line.where("Series_Code = ?", noSeriesCode);
				for (Rec_no_series_line line : lines) {
					startingNo = line.getString("Starting_No_");    //<-- Get starting no
					lastNoUsed = line.getString("Last_No_Used");    //<-- Get last no used
					incrementByNo = line.getInteger("Incrementby_No_");    //<-- Get increment by no
				}
			} catch (Throwable t) {
				t.printStackTrace();
				Statusbar.outputError(t.toString());
				return null;
			} finally {
				Base.close();
			}

			if (modifySeries) {
				if (lastNoUsed.equals("")) {
					lastNoUsed = startingNo;
				}
				return incrementNoText(lastNoUsed, incrementByNo);
			} else {
				return null;
			}
		}else{
			return "";
		}
	}

	private String incrementNoText(String lastNoUsed, int incrementByNo) {
		int startPos;
		int endPos;
		int intNo;
		String newNo;

		ArrayList<Integer> positions = getIntegerPos(lastNoUsed);

		startPos = positions.get(0);
		endPos = positions.get(1);

		intNo = Integer.parseInt(lastNoUsed.substring(startPos, endPos + 1));

		newNo = String.valueOf(intNo + incrementByNo);

		return replaceNoText(lastNoUsed, newNo, 0, startPos, endPos);
	}

	private ArrayList<Integer> getIntegerPos(String lastNoUsed) {
		try {
			ArrayList<Integer> positions = new ArrayList<Integer>();

			int startPos = 0;
			int endPos = 0;
			boolean isDigit;

			if(!lastNoUsed.equals("")) {
				int i = lastNoUsed.length() - 1;

				do {
					isDigit = Character.toString(lastNoUsed.charAt(i)).matches("[0-9]");

					if(isDigit) {
						if(endPos == 0) {
							endPos = i;
						}
						startPos = i;
					}
					i -= 1;
				}while((i != 0 || startPos == 0) && isDigit);
				positions.add(startPos);
				positions.add(endPos);
			}

			return positions;
		}catch(Throwable t) {
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return null;
		}

	}

	private String replaceNoText(String lastNoUsed, String newNo, int fixedLength, int startPos, int endPos) {
		String startNo = null;
		String endNo = null;
		String zeroNo = null;
		int newLength;
		int oldLength;

		if(startPos > 1) {
			startNo = lastNoUsed.substring(0, startPos);
		}

		newLength = newNo.length();
		oldLength = endPos - startPos + 1;

		if(oldLength > newLength) {
			zeroNo = padStr(oldLength - newLength, '0');
		}
		if(updateLastNoUsed(startNo + zeroNo + newNo)){
			return startNo + zeroNo + newNo;
		}else{
			return null;
		}
	}

	private String padStr(int length, char character) {
		try {
			return String.format("%"+length+"s", "").replace(' ', character);
		}catch(Throwable t) {
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return null;
		}
	}

	private boolean updateLastNoUsed(String no) {
		boolean flag = false;
		ArrayList<String> arrayList = new ArrayList<>();
		try {
            ConnectionPool.getInstance();
            Base.open(ConnectionPool.dataSourcePooled);
            /* TODO Get current date time */
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();

			//todo Get line no
			long lineNo = Rec_no_series_line.count("Series_Code = ?", noSeriesCode);

			Rec_no_series_line rec = Rec_no_series_line.findFirst("Series_Code = ? AND Line_No_ = ?", noSeriesCode, lineNo);
			arrayList.add(rec.getString("timestamp"));
			arrayList.add(rec.getString("Series_Code"));
			arrayList.add(rec.getString("Line_No_"));
			arrayList.add(rec.getString("Starting_Date"));
			arrayList.add(rec.getString("Starting_No_"));
			arrayList.add(rec.getString("Ending_No_"));
			arrayList.add(rec.getString("Warning_No_"));
			arrayList.add(rec.getString("Incrementby_No_"));
			arrayList.add(no);
			arrayList.add(rec.getString("Open"));
			arrayList.add(dateFormat.format(date));
			arrayList.add(rec.getString("record_version"));
		}catch (Throwable t){
			Statusbar.outputAlert(t.toString());
			return false;
		}finally {
			Base.close();
		}

		if(rec.modify(arrayList, noSeriesLineID())){
			flag = true;
		}else{
			flag = false;
		}
		return flag;
	}

	private int noSeriesLineID(){
		try{
            ConnectionPool.getInstance();
            Base.open(ConnectionPool.dataSourcePooled);
			Rec_sys_obj_prop_page propPage = Rec_sys_obj_prop_page.findFirst("sourcemodel = ?", "Rec_no_series_line");
			return propPage.getInteger("objectid");
		}catch (Throwable t){
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return 0;
		}finally {
			Base.close();
		}
	}

}

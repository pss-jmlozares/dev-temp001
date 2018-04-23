package managedbeans;

import java.io.*;


import org.eclnt.jsfserver.elements.events.BaseActionEventPopupMenuItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.eclnt.editor.annotations.*;
import org.eclnt.jsfserver.elements.events.BaseActionEventValueHelp;
import org.eclnt.jsfserver.elements.impl.*;
import org.eclnt.jsfserver.pagebean.*;
import org.eclnt.jsfserver.elements.componentnodes.*;
import org.eclnt.jsfserver.defaultscreens.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.*;
import modelclasses.*;
import org.javalite.activejdbc.*;
import database.ConnectionPool;


import javax.faces.event.ActionEvent;

@CCGenClass (expressionBase="#{d.ScheduleLineList}")

public class ScheduleLineList extends PageBean implements Serializable{
	private Rec_schedule_line listModel = new Rec_schedule_line();
	private ModalPopup modalPopup = new ModalPopup();
	private AppTool tool = new AppTool();

	private int objectID;
	public void setObjectID(int value) {this.objectID = value;}

	private String objectName;
	private boolean m_attrDeleteAllowed;
	private boolean m_attrModifyAllowed;
	private boolean m_attrInsertAllowed;
	private int m_attrCardPageID;
	private String fieldSequence;

	private MyRow currentRecord;
	private MyRow xRecord;
	private ArrayList<String> fieldNameArray;

	MyGridListBinding m_rowBind = new MyGridListBinding();
	public MyGridListBinding getRowBind() { return m_rowBind; }
	ROWDYNAMICCONTENTBinding m_dynamicList = new ROWDYNAMICCONTENTBinding();
	public ROWDYNAMICCONTENTBinding getDynamicList() { return m_dynamicList; }
	ROWDYNAMICCONTENTBinding m_dynamicRibbon = new ROWDYNAMICCONTENTBinding();
	public ROWDYNAMICCONTENTBinding getDynamicRibbon() { return m_dynamicRibbon; }

	//Todo passing variables from header
	private String schdleCode;
    public void setSchdleCode(String value) { this.schdleCode = value; }
    private String cutOffFromDate;
    public void setCutOffFromDate(String value) { this.cutOffFromDate = value; }
    private String cutOffToDate;
    public void setCutOffToDate(String value) {this.cutOffToDate = value; }
    private boolean isViewMode;
    public void setIsViewMode(boolean value) { this.isViewMode = value; }

    public class MyRow extends FIXGRIDItem implements java.io.Serializable{
		boolean enabled = false;
		public boolean getEnabled(){return enabled;}
		public void setEnabled(boolean enabled){this.enabled = enabled;}

		String vartimestamp;
		public String getVartimestamp(){return vartimestamp;}
		public void setVartimestamp(String value){this.vartimestamp = value;}

		String varSchedule_Code;
		public String getVarSchedule_Code(){return varSchedule_Code;}
		public void setVarSchedule_Code(String value){this.varSchedule_Code = value;}

		String varLine_No;
		public String getVarLine_No(){return varLine_No;}
		public void setVarLine_No(String value){this.varLine_No = value;}

		String varDate;
        public String getVarDate() { return varDate; }
        public void setVarDate(String value) { this.varDate = value; }

		String varDay;
        public String getVarDay() { return varDay; }
        public void setVarDay(String value){ this.varDay = value; }

        String varProfile_Code;
		public String getVarProfile_Code(){return varProfile_Code;}
		public void setVarProfile_Code(String value){this.varProfile_Code = value;}

		String varName;
		public String getVarName(){return varName;}
		public void setVarName(String value){this.varName = value;}

		String varDepartment;
		public String getVarDepartment(){return varDepartment;}
		public void setVarDepartment(String value){this.varDepartment = value;}

		String varPosition;
		public String getVarPosition(){return varPosition;}
		public void setVarPosition(String value){this.varPosition = value;}

		String varmorning_IN;
		public String getVarmorning_IN(){return varmorning_IN;}
		public void setVarmorning_IN(String value){this.varmorning_IN = value;}

		String varmorning_OUT;
		public String getVarmorning_OUT(){return varmorning_OUT;}
		public void setVarmorning_OUT(String value){this.varmorning_OUT = value;}

		String varafternoon_IN;
		public String getVarafternoon_IN(){return varafternoon_IN;}
		public void setVarafternoon_IN(String value){this.varafternoon_IN = value;}

		String varafternoon_OUT;
		public String getVarafternoon_OUT(){return varafternoon_OUT;}
		public void setVarafternoon_OUT(String value){this.varafternoon_OUT = value;}

		public void onRowPopupMenuItem(BaseActionEventPopupMenuItem event){
		    if(!isViewMode){
                if ("REMOVE".equals(event.getCommand())) {
                    onDelete(null);
                } else if ("NEW".equals(event.getCommand())) {
                    onNew(null);
                } else if ("EDIT".equals(event.getCommand())) {
                    onEdit(null);
                }
            }
		}

		public void onRowSelect() {
			if(m_rowBind.getItems().indexOf(m_rowBind.getSelectedItem()) != m_rowBind.getItems().indexOf(currentRecord)) {
				if(currentRecord == null) {
					currentRecord = m_rowBind.getSelectedItem();
				}else {
					xRecord = currentRecord;
					currentRecord = m_rowBind.getSelectedItem();
					if(xRecord.getEnabled()) {
						if(xRecord.getVartimestamp().equals("")) {
							//Write code to insert
							if(listModel.insert(pastRowArray(), objectID)){
								refresh();
								Statusbar.outputSuccess("Record Successfully Saved!");
							}
						}else{
							//Write code to update
							if(listModel.modify(pastRowArray(), objectID)){
								refresh();
								Statusbar.outputSuccess("Updates Successfully Saved!");
							}
						}
					}
				}
			}
		}

		public void onRowExecute(){
			if(!isViewMode){
                onEdit(null);
            }
		}
	}

	public class MyGridListBinding extends FIXGRIDListBinding<MyRow>{
		@Override
		protected void onPopupMenuItem(BaseActionEventPopupMenuItem event){
		    if(!isViewMode){
                if ("NEW".equals(event.getCommand())) {
                    onNew(null);
                }else if ("COLUMNS".equals(event.getCommand())){
                    m_rowBind.onEditColumnDetails(null);
                }
            }
		}
	}

	public void initProperties(){
		try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);
			Rec_sys_obj_prop_page pageAttr = Rec_sys_obj_prop_page.findFirst("objectid = ?", objectID);
			m_attrCardPageID = pageAttr.getInteger("cardPageID");
			m_attrInsertAllowed = tool.intToBoolean(pageAttr.getInteger("insertAllowed"));
			m_attrModifyAllowed = tool.intToBoolean(pageAttr.getInteger("modifyAllowed"));
			m_attrDeleteAllowed = tool.intToBoolean(pageAttr.getInteger("deleteAllowed"));
			objectName = pageAttr.getString("objectname");

			Rec_sys_obj_prop_pageview pageView = Rec_sys_obj_prop_pageview.findFirst("objectid = ?", objectID);
			if(pageView != null){
				fieldSequence = pageView.getString("fieldsequence");}
		}catch(Throwable t){
			t.printStackTrace();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
		}finally{
			Base.close();
		}
	}

	public void onNew(javax.faces.event.ActionEvent event) {
		if(m_attrCardPageID != 0){
			if(m_attrInsertAllowed) {
				//Write code to Pop up card page
				popupCard(emptyRow(), true);
			}else{
				Statusbar.outputError("Insert allowed is equal to false.");
			}
		}else{
			if(m_attrInsertAllowed) {
				MyRow row = emptyRow();
				row.setEnabled(true);
				m_rowBind.getItems().add(row);
				m_rowBind.deselectCurrentSelection();
				m_rowBind.selectItem(row);
				m_rowBind.ensureItemToBeDisplayed(row);
			}else {
				Statusbar.outputError("Insert allowed is equal to false.");
			}
		}
	}

	public void onEdit(javax.faces.event.ActionEvent event) {
		if(m_attrCardPageID != 0){
			if(m_rowBind.getItems().size() != 0){
				if(m_attrModifyAllowed) {
					if(m_rowBind.isItemSelected()){
						//Write code to pop up card
						popupCard(currentRecord, true);
					}else{
						Statusbar.outputError("No selection of item detected.");
					}
				}else{
					Statusbar.outputError("Modify allowed is equal to false.");
				}
			}else{
				Statusbar.outputError("No Records Yet.");
			}
		}else{
			if(m_rowBind.getItems().size() != 0){
				if(m_attrModifyAllowed) {
					if(m_rowBind.isItemSelected()) {
						if(m_rowBind.getSelectedItem().getEnabled() == false) {
							m_rowBind.getSelectedItem().setEnabled(true);
						}else{
							//TODO ITS A NEW ROW
							if(currentRecord.getVartimestamp().equals("")){
								//TODO Insert Mode
								if(listModel.insert(currentRowArray(), objectID)){
									refresh();
									Statusbar.outputSuccess("Record Successfully Saved!");
								}
							}else{
								//TODO Edit Mode
								if(listModel.modify(currentRowArray(), objectID)){
									refresh();
									Statusbar.outputSuccess("Updates Successfully Saved!");
								}
							}
						}
					}else{
						Statusbar.outputError("No selection of item detected.");
					}
				}else {
					Statusbar.outputError("Modify allowed is equal to false.");
				}
			}else{
				Statusbar.outputError("No Records Yet.");
			}
		}
	}

	public void onView(javax.faces.event.ActionEvent event) {
		if(m_attrCardPageID == 0) {
			Statusbar.outputError("Card page not available.");
		}else{
			//Write code to pop up card
			if(m_rowBind.getItems().size() != 0){
				if(m_rowBind.isItemSelected()){
					popupCard(currentRecord, false);
				}else{
					Statusbar.outputError("No Selection Of Item Detected.");
				}
			}else{
				Statusbar.outputError("No Records Yet.");
			}
		}
	}

	public void onDelete(javax.faces.event.ActionEvent event) {
		if(m_attrDeleteAllowed) {
			if(m_rowBind.getItems().size() != 0){
				if(m_rowBind.isItemSelected()) {
					if(m_rowBind.getSelectedItems().size() != 1){
						for(MyRow myRow : m_rowBind.getSelectedItems()){
							if(!"".equals(myRow.getVartimestamp())){
								if(listModel.delete(myRow.getVarSchedule_Code(),myRow.getVarLine_No())) {
									m_rowBind.getItems().remove(myRow);
									Statusbar.outputSuccess("Record Successfully Deleted!");
								}else{
									Statusbar.outputError("Failed to delete record!");
								}
							}else{
								m_rowBind.getItems().remove(myRow);
								Statusbar.outputSuccess("Record Successfully Deleted!");
							}
						}
					}else{
						if(!"".equals(currentRecord.getVartimestamp())){
							if(listModel.delete(currentRecord.getVarSchedule_Code(),currentRecord.getVarLine_No())) {
								m_rowBind.getItems().remove(m_rowBind.getSelectedItem());
								Statusbar.outputSuccess("Record Successfully Deleted!");
							}else{
								Statusbar.outputError("Failed to delete record!");
							}
						}else{
							m_rowBind.getItems().remove(m_rowBind.getSelectedItem());
							Statusbar.outputSuccess("Record Successfully Deleted!");
						}
					}
				}else {
					Statusbar.outputError("No selection of item detected.");
				}
			}else{
				Statusbar.outputError("No Records Yet.");
			}
		}else {
			Statusbar.outputError("Delete allowed is equal to false.");
		}
	}

	public void onGenerate(ActionEvent event){
        if (!isViewMode) {
            try {
                ConnectionPool.getInstance();
                Base.open(ConnectionPool.dataSourcePooled);
                DateTimeFormatter date = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                LocalDate fromDate = LocalDate.parse(cutOffFromDate, date);
                LocalDate toDate = LocalDate.parse(cutOffToDate, date);

                long range = Duration.between(fromDate.atStartOfDay(), toDate.atStartOfDay()).toDays();

                List<Rec_profile> list = Rec_profile.findAll();
                for (Rec_profile a : list) {

                    for (long count = 0; count <= range; count++) {
                        //TODO GET LINENO
                        int no;
                        Object maxCol = Base.firstCell("select max(Line_No) from schedule_line where Schedule_Code = ?", schdleCode);
                        no = (maxCol == null) ? 0 : Integer.parseInt(String.valueOf(maxCol));
                        no += 1;

                        Rec_schedule_line rs = new Rec_schedule_line();

                        String name = a.getString("First_Name") + " " + a.getString("Last_Name");
                        DayOfWeek test = fromDate.plusDays(count).getDayOfWeek();
                        if (test != DayOfWeek.valueOf(a.getString("Rest_Day"))){
                            rs.set("timestamp", "");
                            rs.set("Schedule_Code", schdleCode);
                            rs.set("Line_No", no);
                            rs.set("Profile_Code", a.getString("Profile_No_"));
                            rs.set("Name", name);
                            rs.set("Department", a.getString("Department"));
                            rs.set("Position", a.getString("Position"));
                            rs.set("morning_IN", a.getTime("default_morning_IN"));
                            rs.set("morning_OUT", a.getTime("default_morning_OUT"));
                            rs.set("afternoon_IN", a.getTime("default_afternoon_IN"));
                            rs.set("afternoon_OUT", a.getTime("default_afternoon_OUT"));
                            rs.set("Date", String.valueOf(fromDate.plusDays(count)));
                            rs.set("Day", String.valueOf(fromDate.plusDays(count).getDayOfWeek()));
                            rs.saveIt();
                        }
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
                Statusbar.outputAlert(t.toString());
            } finally {
                Base.close();
                refresh();
            }
        }else {
            Statusbar.outputAlert("Generating TimeKeeper is disabled.");
        }
    }

    public LocalDate add(LocalDate date, long workdays) {
        if (workdays < 1) {
            return date;
        }

        LocalDate result = date;
        int addedDays = 0;
        while (addedDays < workdays) {
            result = result.plusDays(1);
            if (!(result.getDayOfWeek() == DayOfWeek.SATURDAY ||
                    result.getDayOfWeek() == DayOfWeek.SUNDAY)) {
                ++addedDays;
            }
        }

        return result;
    }

    public long getActualNumberOfDaysToAdd(long workdays, int dayOfWeek) {
        if (dayOfWeek < 6) { // date is a workday
            return workdays + (workdays + dayOfWeek - 1) / 5 * 2;
        } else { // date is a weekend
            return workdays + (workdays - 1) / 5 * 2 + (7 - dayOfWeek);
        }
    }

    public LocalDate add2(LocalDate date, long workdays) {
        if (workdays < 1) {
            return date;
        }

        return date.plusDays(getActualNumberOfDaysToAdd(workdays, date.getDayOfWeek().getValue()));
    }

	public void onExportList(javax.faces.event.ActionEvent event){
		m_rowBind.onOpenGridFunctions(null);
	}

	public void build(){
		try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);

			//TODO Get All Field Names On This List
			fieldNameArray = new ArrayList<String>();
			List<Rec_sys_obj_prop_table_fieldcaption> fieldProperties = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ?", objectID);
			for(Rec_sys_obj_prop_table_fieldcaption fieldProperty : fieldProperties) {
				fieldNameArray.add(fieldProperty.getString("fieldname"));
			}
			//TODO Get Fix Grid Attributes
			ArrayList<String> fixGridAttrArray = new ArrayList<String>();
			Set<String> set = Rec_sys_obj_prop_page_list_fixgrid.attributeNames();
			for(String attr : set){fixGridAttrArray.add(attr);}
			fixGridAttrArray.remove(9);
			fixGridAttrArray.remove(10);
			//TODO Get Fix Grid Based On Object ID
			List<Rec_sys_obj_prop_page_list_fixgrid> fixGrids = Rec_sys_obj_prop_page_list_fixgrid.where("objectid = ?", objectID);
			for(Rec_sys_obj_prop_page_list_fixgrid fixGrid : fixGrids){}
			//TODO Get Table Data
			List<Rec_schedule_line> results = Rec_schedule_line.where("Schedule_Code = ?",schdleCode);
			for(Rec_schedule_line result : results){
				MyRow row = new MyRow();
				row.setVartimestamp(result.getString(fieldNameArray.get(0)));
				row.setVarSchedule_Code(result.getString(fieldNameArray.get(1)));
				row.setVarLine_No(result.getString(fieldNameArray.get(2)));
				row.setVarDate(tool.formatSQLDate(result.getDate(fieldNameArray.get(3))));
				row.setVarDay(result.getString(fieldNameArray.get(4)));
				row.setVarProfile_Code(result.getString(fieldNameArray.get(5)));
				row.setVarName(result.getString(fieldNameArray.get(6)));
				row.setVarDepartment(result.getString(fieldNameArray.get(7)));
				row.setVarPosition(result.getString(fieldNameArray.get(8)));
				row.setVarmorning_IN(tool.formatSQLTime(result.getDate(fieldNameArray.get(9))));
				row.setVarmorning_OUT(tool.formatSQLTime(result.getDate(fieldNameArray.get(10))));
				row.setVarafternoon_IN(tool.formatSQLTime(result.getDate(fieldNameArray.get(11))));
				row.setVarafternoon_OUT(tool.formatSQLTime(result.getDate(fieldNameArray.get(12))));
				m_rowBind.getItems().add(row);
			}
			//TODO Declare String Array For Variables To Be Shown
			ArrayList<String> variableArray = new ArrayList<String>();
			variableArray.clear();

			//TODO Get Field Properties For Columns To Be Shown
			List<Rec_sys_obj_prop_table_fieldcaption> fieldProperties2 = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ? AND showColumn = ?", objectID, "Yes");
			for(Rec_sys_obj_prop_table_fieldcaption fieldProperty : fieldProperties2){
				variableArray.add("var"+fieldProperty.getString("fieldname")+"");
			}
			//TODO Build XML For This Page
			StringBuffer xml = new StringBuffer();
			xml.append("<t:fixgrid ");
			for(int i = 0; i < fixGridAttrArray.size(); i++){
				xml.append(fixGridAttrArray.get(i) + "='" + fixGrids.get(0).getString(fixGridAttrArray.get(i)) + "' ");
			}
			xml.append("multiselect = 'true' rowpopupmenu='GRIDROWPOPUPMENU' popupmenu='GRIDPOPUPMENU'>");
			xml.append("<t:gridcol width='20'>");
			xml.append("<t:gridrowselector>");
			xml.append("</t:gridrowselector>");
			xml.append("</t:gridcol>");
			int col = 0;
			for(Rec_sys_obj_prop_table_fieldcaption fieldProperty : fieldProperties2){
				//TODO Get Option String For ComboBox
				ArrayList<String> options = getOptions(fieldProperty.getString("fieldname"));
 				xml.append("<t:gridcol multiline = 'true' text='" + fieldProperty.getString("caption") + "' width='100%'>");
				switch(fieldProperty.getString("componentType")) {
				case "Field":
					if(col == 1 || col == 2 || col == 3 || col == 4 || col == 5 || col == 6 || col == 7){
						xml.append("<t:field text='" + ".{" + variableArray.get(col) + "}" + "' focusable= 'false' enabled='false'/>");
						break;
					}else{
						xml.append("<t:field text='" + ".{" + variableArray.get(col) + "}" + "' enabled='.{enabled}' flush = 'true'/>");
						break;
					}

				case "CheckBox":
					xml.append("<t:checkbox align = 'center' selected='" + ".{" + variableArray.get(col) + "}" + "' enabled='.{enabled}'></t:checkbox>");
					break;
				case "ComboBox":
					xml.append("<t:combobox value = '.{" + variableArray.get(col) + "}' width='100%' enabled='.{enabled}'>");
					for (int counter = 0; counter < options.size(); counter++) {
						xml.append("<t:comboboxitem value = '" + counter + "' text='" + options.get(counter) + "' ></t:comboboxitem>");
					}
					xml.append("</t:combobox>");
					break;
				case "ComboField":
					xml.append("<t:combofield activationhotkey = 'alt-40' enabled='.{enabled}' actionListener = '#{d.ScheduleLineList.onComboField"+fieldProperty.getString("fieldname")+"}' text='" + ".{" + variableArray.get(col) + "}" + "'></t:combofield>");
					break;
				}
				xml.append("</t:gridcol>");
				col++;
			}
			xml.append("</t:fixgrid>");
			m_dynamicList.setContentXml(xml.toString());

			m_rowBind.setColumnsequence(fieldSequence);
			m_rowBind.setDefaultColumnsequence(fieldSequence);
			//TODO Build Ribbon
			PANENode foldable = buildRibbonNodes(objectID);
			m_dynamicRibbon.setContentNode(foldable);
		}catch(Throwable t){
			t.printStackTrace();
			Statusbar.outputError(t.toString());
		}finally{
			Base.close();
		}
	}

	private ArrayList<String> getOptions(String fieldName){
		try {
			ArrayList<String> options = new ArrayList<String>();
			options.clear();
			String optionString = null;
			List<Rec_sys_obj_prop_table_fieldcaption> fieldProps = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ? AND fieldname = ?", objectID, fieldName);
			for(Rec_sys_obj_prop_table_fieldcaption fieldProp : fieldProps) {
				optionString = fieldProp.getString("optionString");
			}
			String[] listOptionString = optionString.split(",");
			for(String x : listOptionString) {
				options.add(x);
			}
			return options;
		}catch(Throwable t) {
			t.printStackTrace();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return null;
		}
	}

	private ArrayList<String> currentRowArray(){
        try{
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add(currentRecord.getVartimestamp());
            arrayList.add(schdleCode);
            arrayList.add(currentRecord.getVarLine_No());
            arrayList.add(tool.formatToDefaultDate(currentRecord.getVarDate()));
            arrayList.add(currentRecord.getVarDay());
            arrayList.add(currentRecord.getVarProfile_Code());
            arrayList.add(currentRecord.getVarName());
            arrayList.add(currentRecord.getVarDepartment());
            arrayList.add(currentRecord.getVarPosition());
            arrayList.add(tool.formatTime24H(currentRecord.getVarmorning_IN()));
            arrayList.add(tool.formatTime24H(currentRecord.getVarmorning_OUT()));
            arrayList.add(tool.formatTime24H(currentRecord.getVarafternoon_IN()));
            arrayList.add(tool.formatTime24H(currentRecord.getVarafternoon_OUT()));
            return arrayList;
        }catch(Throwable t){
            t.printStackTrace();
            Statusbar.outputAlert(t.toString());
            return null;
        }
	}

	private ArrayList<String> pastRowArray(){
        try{
            ArrayList<String> arrayList = new ArrayList<String>();
            arrayList.add(xRecord.getVartimestamp());
            arrayList.add(schdleCode);
            arrayList.add(xRecord.getVarLine_No());
            arrayList.add(tool.formatToDefaultDate(xRecord.getVarDate()));
            arrayList.add(xRecord.getVarDay());
            arrayList.add(xRecord.getVarProfile_Code());
            arrayList.add(xRecord.getVarName());
            arrayList.add(xRecord.getVarDepartment());
            arrayList.add(xRecord.getVarPosition());
            arrayList.add(tool.formatTime24H(xRecord.getVarmorning_IN()));
            arrayList.add(tool.formatTime24H(xRecord.getVarmorning_OUT()));
            arrayList.add(tool.formatTime24H(xRecord.getVarafternoon_IN()));
            arrayList.add(tool.formatTime24H(xRecord.getVarafternoon_OUT()));
            return arrayList;
        }catch(Throwable t){
            t.printStackTrace();
            Statusbar.outputAlert(t.toString());
            return null;
        }
	}

	private void refresh(){
		try{
			Base.open(ConnectionPool.dataSourcePooled);
			m_rowBind.getItems().clear();
			//TODO Get Table Data
			List<Rec_schedule_line> results = Rec_schedule_line.where("Schedule_Code = ?",schdleCode);
			for(Rec_schedule_line result : results){
				MyRow row = new MyRow();
                row.setVartimestamp(result.getString(fieldNameArray.get(0)));
                row.setVarSchedule_Code(result.getString(fieldNameArray.get(1)));
                row.setVarLine_No(result.getString(fieldNameArray.get(2)));
                row.setVarDate(tool.formatSQLDate(result.getDate(fieldNameArray.get(3))));
                row.setVarDay(result.getString(fieldNameArray.get(4)));
                row.setVarProfile_Code(result.getString(fieldNameArray.get(5)));
                row.setVarName(result.getString(fieldNameArray.get(6)));
                row.setVarDepartment(result.getString(fieldNameArray.get(7)));
                row.setVarPosition(result.getString(fieldNameArray.get(8)));
                row.setVarmorning_IN(tool.formatSQLTime(result.getDate(fieldNameArray.get(9))));
                row.setVarmorning_OUT(tool.formatSQLTime(result.getDate(fieldNameArray.get(10))));
                row.setVarafternoon_IN(tool.formatSQLTime(result.getDate(fieldNameArray.get(11))));
                row.setVarafternoon_OUT(tool.formatSQLTime(result.getDate(fieldNameArray.get(12))));
				m_rowBind.getItems().add(row);
			}
			currentRecord = null;
			xRecord = null;
		}catch(Throwable t){
			Statusbar.outputError(t.toString());
		}finally{
			Base.close();
//			MyRow myRow = emptyRow();
//			myRow.setEnabled(true);
//			m_rowBind.getItems().add(myRow);
//			m_rowBind.selectItem(myRow);
//			m_rowBind.ensureItemToBeDisplayed(myRow);
		}
	}

	private void popupCard(MyRow row, boolean isEnabled){
		//Write code to pop up card
        /*try{
            ScheduleLineList popupCardVar = new ScheduleLineList();
            popupCardVar.setEnabled(isEnabled);
            popupCardVar.setListObjID(objectID);
            popupCardVar.setCardPageID(m_attrCardPageID);
            popupCardVar.setValues(row);
            popupCardVar.buildcard();

            ModalPopup popup = openModalPopup(popupCardVar, "ScheduleLineList Card", 0, 0,
                    new ModalPopup.IModalPopupListener() {
                        @Override
                        public void reactOnPopupClosedByUser() {
                            closePopup(popupCardVar);
                        }
                    });
            popup.setLeftTopReferenceCentered();
            popup.maximize(true);
            popupCardVar.prepare(new ScheduleLineList.ICloseListener() {
                @Override
                public void reactOnClose() {
                    refresh();
                    closePopup(popupCardVar);
                }
            });
        }catch(Throwable t){
            tool.err(t.toString());
        }*/
	}

	private MyRow emptyRow(){
		DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        DateFormat DayFormat = new SimpleDateFormat("EEEE");
        DateFormat timeFormat = new SimpleDateFormat("h:mm:ss a");
		Date date = new Date();
		MyRow row = new MyRow();
		row.setVartimestamp("");
		row.setVarSchedule_Code(schdleCode);
		row.setVarLine_No("0");
		row.setVarDate(dateFormat.format(date));
		row.setVarDay(DayFormat.format(date));
		row.setVarProfile_Code("");
		row.setVarName("");
		row.setVarDepartment("");
		row.setVarPosition("");
		row.setVarmorning_IN(timeFormat.format(date));
		row.setVarmorning_OUT(timeFormat.format(date));
		row.setVarafternoon_IN(timeFormat.format(date));
		row.setVarafternoon_OUT(timeFormat.format(date));
		return row;
	}

	private ArrayList<String> emptyRowArray(){
		//TODO Get Date
		Date date = new Date();
		ArrayList<String> row = new ArrayList<String>();
		row.add("");
		row.add(schdleCode);
		row.add(tool.formatJavaDate(date));
		row.add("");
		row.add("0");
		row.add("");
		row.add("");
		row.add("");
		row.add("");
		row.add(tool.formatJavaTime(date));
		row.add(tool.formatJavaTime(date));
		row.add(tool.formatJavaTime(date));
		row.add(tool.formatJavaTime(date));
		return row;
	}

	private PANENode buildRibbonNodes(int objID){
		int tabCount = 0;
		List<Rec_sys_ribbontab_properties> ribbonTab = Rec_sys_ribbontab_properties.where("objectid = ?", objID);
		for(Rec_sys_ribbontab_properties tab : ribbonTab){tabCount = tab.getInteger("tabcount");}
		ArrayList<String> cols = new ArrayList<String>();
		Set<String> set = Rec_sys_ribbontabitem_properties.attributeNames();
		for(String x : set){cols.add(x);}
		List<Rec_sys_ribbontabitem_properties> ribbonTabItems = Rec_sys_ribbontabitem_properties.where("`Page ID` = ?", objID);
		for(Rec_sys_ribbontabitem_properties tabItem : ribbonTabItems){}
		List<Rec_sys_ribbonitem_properties> ribbonItems = Rec_sys_ribbonitem_properties.where("`Page ID` = ?", objID);
		for(Rec_sys_ribbonitem_properties ribbonItem : ribbonItems){}
		PANENode p = new PANENode();
		p.setWidth("100%");
		{
			ROWNode r = new ROWNode();
			p.addSubNode(r);
			{
				FOLDABLEPANENode foldable = new FOLDABLEPANENode();
				foldable.setText(objectName + " List");
				foldable.setWidth("100%");
				foldable.setPadding("bottom:1");
				foldable.setInnerborder("none");
				foldable.setInnerpadding("0");
				foldable.setOpened(true);
				r.addSubNode(foldable);
				{
					ROWNode rr = new ROWNode();
					foldable.addSubNode(rr);
					{
						PANENode pane = new PANENode();
						pane.setWidth("100%");
						rr.addSubNode(pane);
						{
							ROWNode row = new ROWNode();
							pane.addSubNode(row);
							{
								TABBEDPANENode tabPane = new TABBEDPANENode();
								tabPane.setOnlydrawselectedtab(true);
								tabPane.setStylevariant("topmenu");
								tabPane.setTabindent("30");
								tabPane.setTabstyle("classic");
								tabPane.setTransparent(true);
								tabPane.setWidth("100%");
								row.addSubNode(tabPane);
								{
									for(int tabIndex = 0; tabIndex < tabCount; tabIndex++){
										TABBEDPANETABNode tabPaneTab = new TABBEDPANETABNode();
										tabPaneTab.setText(ribbonTabItems.get(tabIndex).getString(cols.get(0)));
										tabPane.addSubNode(tabPaneTab);
										{
											ROWNode row2 = new ROWNode();
											row2.setColdistance(10);
											tabPaneTab.addSubNode(row2);
											{
												for(int paneIndex = 0; paneIndex < ribbonTabItems.get(tabIndex).getInteger(cols.get(3)); paneIndex++){
													PANENode pane2 = new PANENode();
													pane2.setHeight("100%");
													pane2.setBackground("white");
													pane2.setPadding("5");
													row2.addSubNode(pane2);
													{
														ROWNode row5 = new ROWNode();
														pane2.addSubNode(row5);
														{
															String caption = null;
															List<Rec_sys_ribbonpane_properties> ribbonPane = Rec_sys_ribbonpane_properties.where("`Page ID` = ? AND tabindex = ? AND paneid = ?", objID,tabIndex, paneIndex);
															for(Rec_sys_ribbonpane_properties value : ribbonPane){caption = value.getString("caption");}
															LABELNode label = new LABELNode();
															label.setAlign("center");
															label.setBackground("#00000010");
															label.setWidth("100%");
															label.setText(caption);
															row5.addSubNode(label);
														}
														ROWNode row3 = new ROWNode();
														pane2.addSubNode(row3);
														{
															for (int btnIndex = 0; btnIndex < ribbonItems.size(); btnIndex++) {
																String btnCaption = null;
																String btnFunctionCode = null;
																List<Rec_sys_ribbonitem_properties> ribbonItem = Rec_sys_ribbonitem_properties.where("`Page ID` = ? AND tabindex = ? AND paneindex = ? AND rowindex = ? AND buttonid = ?", objID, tabIndex, paneIndex, 0, btnIndex);
																for(Rec_sys_ribbonitem_properties btnCap : ribbonItem){
																	btnCaption = btnCap.getString("caption");
																	btnFunctionCode = btnCap.getString("functioncode");
																	BUTTONNode btn = new BUTTONNode();
																	btn.setHeight(25);
																	btn.setImageheight("20");
																	btn.setImagewidth("20");
																	btn.setImage(btnCap.getString("Image"));
																	btn.setActionListener(btnFunctionCode);
																	btn.setContentareafilled(false);
																	btn.setText(btnCaption);
																	if (tabIndex == 0 && paneIndex == 0){
																		btn.setEnabled(pagePropList().get(btnIndex));
																	}
																	row3.addSubNode(btn);
																}
															}
														}
														ROWNode row4 = new ROWNode();
														pane2.addSubNode(row4);
														{
															for (int btnIndex = 0; btnIndex < ribbonItems.size(); btnIndex++) {
																String btnCaption = null;
																String btnFunctionCode = null;
																List<Rec_sys_ribbonitem_properties> ribbonItem = Rec_sys_ribbonitem_properties.where("`Page ID` = ? AND tabindex = ? AND paneindex = ? AND rowindex = ? AND buttonid = ?", objID, tabIndex, paneIndex, 1, btnIndex);
																for(Rec_sys_ribbonitem_properties btnCap : ribbonItem){
																	btnCaption = btnCap.getString("caption");
																	btnFunctionCode = btnCap.getString("functioncode");
																	BUTTONNode btn = new BUTTONNode();
																	btn.setHeight(25);
																	btn.setImageheight("20");
																	btn.setImagewidth("20");
																	btn.setImage(btnCap.getString("Image"));
																	btn.setActionListener(btnFunctionCode);
																	btn.setContentareafilled(false);
																	btn.setText(btnCaption);
																	row4.addSubNode(btn);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return p;
	}

	private ArrayList<Boolean> pagePropList(){
		ArrayList<Boolean> arrayList = new ArrayList<>();
        arrayList.add(m_attrModifyAllowed);
		arrayList.add(m_attrInsertAllowed);
		arrayList.add(true);
		arrayList.add(m_attrDeleteAllowed);
		return arrayList;
	}

	//TODO onComboField for Profile_Code
    public void onComboFieldProfile_Code(ActionEvent ae){
        if(ae instanceof BaseActionEventValueHelp){
            try{
                Base.open(ConnectionPool.dataSourcePooled);

                IdTextSelection sel = IdTextSelection.createInstance();

                List<Rec_profile> prof = Rec_profile.findAll();
                for (Rec_profile list:prof){
                    String Name = list.getString("First_Name")+" "+list.getString("Last_Name");
                    sel.addLine(list.getString("Profile_No_"),Name);
                }
                sel.setCallBack(new ISetIdText() {
                    @Override
                    public void setIdText(String s,String s1) {
                        currentRecord.setVarProfile_Code(s);
                        currentRecord.setVarName(s1);
                        try{
                            Base.open(ConnectionPool.dataSourcePooled);
                            Rec_profile pro = Rec_profile.findFirst("Profile_No_ = ?",s);
                            currentRecord.setVarDepartment(pro.getString("Department"));
                            currentRecord.setVarPosition(pro.getString("Position"));
                        }catch(Throwable t){
                            t.printStackTrace();
                            Statusbar.outputAlert(t.toString());
                        }finally {
                            Base.close();
                        }
                    }
                });

            }catch(Throwable t){
                t.printStackTrace();
                Statusbar.outputAlert(t.toString());
            }finally {
                Base.close();
            }
        }
    }


	public interface IListener{}
	private IListener m_listener;
	public ScheduleLineList(){
	}
	public String getPageName() { return "/ScheduleLineList.jsp"; }
	public String getRootExpressionUsedInPage() { return "#{d.ScheduleLineList}"; }
	public void prepare(IListener listener){m_listener = listener;}
}

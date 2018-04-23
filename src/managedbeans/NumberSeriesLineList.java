package managedbeans;

import java.io.*;
import org.eclnt.jsfserver.elements.events.BaseActionEventPopupMenuItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.eclnt.editor.annotations.*;
import org.eclnt.jsfserver.elements.impl.*;
import org.eclnt.jsfserver.pagebean.*;
import org.eclnt.jsfserver.elements.componentnodes.*;
import org.eclnt.jsfserver.defaultscreens.*;
import java.util.*;
import modelclasses.*;
import org.javalite.activejdbc.*;
import database.ConnectionPool;
@CCGenClass (expressionBase="#{d.NumberSeriesLineList}")

public class NumberSeriesLineList extends PageBean implements Serializable{
	private Rec_no_series_line listModel = new Rec_no_series_line();
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

	public class MyRow extends FIXGRIDItem implements java.io.Serializable{
		boolean enabled = false;
		public boolean getEnabled(){return enabled;}
		public void setEnabled(boolean enabled){this.enabled = enabled;}

        boolean enabledPK = false;
        public boolean getEnabledPK(){return enabledPK;}
        public void setEnabledPK(boolean enabled){this.enabledPK = enabled;}

		String vartimestamp;
		public String getVartimestamp(){return vartimestamp;}
		public void setVartimestamp(String value){this.vartimestamp = value;}

		String varSeries_Code;
		public String getVarSeries_Code(){return varSeries_Code;}
		public void setVarSeries_Code(String value){this.varSeries_Code = value;}

		String varLine_No_;
		public String getVarLine_No_(){return varLine_No_;}
		public void setVarLine_No_(String value){this.varLine_No_ = value;}

		String varStarting_Date;
		public String getVarStarting_Date(){return varStarting_Date;}
		public void setVarStarting_Date(String value){this.varStarting_Date = value;}

		String varStarting_No_;
		public String getVarStarting_No_(){return varStarting_No_;}
		public void setVarStarting_No_(String value){this.varStarting_No_ = value;}

		String varEnding_No_;
		public String getVarEnding_No_(){return varEnding_No_;}
		public void setVarEnding_No_(String value){this.varEnding_No_ = value;}

		String varWarning_No_;
		public String getVarWarning_No_(){return varWarning_No_;}
		public void setVarWarning_No_(String value){this.varWarning_No_ = value;}

		String varIncrementby_No_;
		public String getVarIncrementby_No_(){return varIncrementby_No_;}
		public void setVarIncrementby_No_(String value){this.varIncrementby_No_ = value;}

		String varLast_No_Used;
		public String getVarLast_No_Used(){return varLast_No_Used;}
		public void setVarLast_No_Used(String value){this.varLast_No_Used = value;}

		String varOpen;
		public String getVarOpen(){return varOpen;}
		public void setVarOpen(String value){this.varOpen = value;}

		String varLast_Date_Used;
		public String getVarLast_Date_Used(){return varLast_Date_Used;}
		public void setVarLast_Date_Used(String value){this.varLast_Date_Used = value;}

		public void onRowPopupMenuItem(BaseActionEventPopupMenuItem event){
			if ("REMOVE".equals(event.getCommand())) {
					onDelete(null);
				} else if ("NEW".equals(event.getCommand())) {
					onNew(null);
				} else if ("EDIT".equals(event.getCommand())) {
					onEdit(null);
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
			onEdit(null);
		}
	}

	public class MyGridListBinding extends FIXGRIDListBinding<MyRow>{
		@Override
		protected void onPopupMenuItem(BaseActionEventPopupMenuItem event){
			if ("NEW".equals(event.getCommand())) {
				onNew(null);
			}else if ("COLUMNS".equals(event.getCommand())){
				m_rowBind.onEditColumnDetails(null);
			}
		}
	}

	public void initProperties(){
		try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);
			List<Rec_sys_obj_prop_page> pageAttrs = Rec_sys_obj_prop_page.where("objectid = ?", objectID);
			for(Rec_sys_obj_prop_page pageAttr:pageAttrs){
                m_attrCardPageID = pageAttr.getInteger("cardPageID");
                m_attrInsertAllowed = tool.intToBoolean(pageAttr.getInteger("insertAllowed"));
                m_attrModifyAllowed = tool.intToBoolean(pageAttr.getInteger("modifyAllowed"));
                m_attrDeleteAllowed = tool.intToBoolean(pageAttr.getInteger("deleteAllowed"));
                objectName = pageAttr.getString("objectname");
            }
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
			if(m_attrInsertAllowed == true) {
				//Write code to Pop up card page
				popupCard(emptyRowArray(), true);
			}else{
				Statusbar.outputError("Insert allowed is equal to false.");
			}
		}else{
            if(m_attrInsertAllowed == true) {
                int line_no_ = 1;
                try{
                    ConnectionPool.getInstance();
                    Base.open(ConnectionPool.dataSourcePooled);

			        if (Rec_no_series_line.count("Series_Code = ?", m_listener.code()) != 0){
                        Object last_line_no_ = Base.firstCell("select max(Line_No_) from no_series_line where Series_Code = ?", m_listener.code());
                        line_no_ = Integer.parseInt(last_line_no_.toString()) + 1;
                    }

                }catch(Throwable t){
                    tool.err(t.toString());
                    t.printStackTrace();
                }finally{
                    Base.close();
                }

                MyRow row = emptyRow();
                row.setVarSeries_Code(m_listener.code());
                row.setVarLine_No_(String.valueOf(line_no_));

                row.setEnabled(true);
                row.setEnabledPK(false);
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
				if(m_attrModifyAllowed == true) {
					if(m_rowBind.isItemSelected()){
						//Write code to pop up card
						popupCard(currentRowArray(), true);
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
				if(m_attrModifyAllowed == true) {
					if(m_rowBind.isItemSelected()) {
						if(m_rowBind.getSelectedItem().getEnabled() == false) {
							m_rowBind.getSelectedItem().setEnabled(true);
							m_rowBind.getSelectedItem().setEnabledPK(false);
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
					popupCard(currentRowArray(), false);
				}else{
					Statusbar.outputError("No Selection Of Item Detected.");
				}
			}else{
				Statusbar.outputError("No Records Yet.");
			}
		}
	}

	public void onDelete(javax.faces.event.ActionEvent event) {
        boolean condition = false;
		if(m_attrDeleteAllowed == true) {
			if(m_rowBind.getItems().size() != 0){
				if(m_rowBind.isItemSelected()) {
					if(m_rowBind.getSelectedItems().size() != 1){
						for(MyRow myRow : m_rowBind.getSelectedItems()){
                            if (!myRow.getVarStarting_No_().isEmpty()) {
                                if (listModel.delete(myRow.getVarSeries_Code(), myRow.getVarLine_No_())) {
                                    m_rowBind.getItems().remove(myRow);
                                    condition = true;
                                } else {
                                    Statusbar.outputError("Failed to delete record!");
                                }
                            }else {
                                condition = true;
                                m_rowBind.getItems().remove(myRow);
                            }
						}
					}else{
                        if (!currentRecord.getVarStarting_No_().isEmpty()) {
                            if (listModel.delete(currentRecord.getVarSeries_Code(), currentRecord.getVarLine_No_())) {
                                m_rowBind.getItems().remove(m_rowBind.getSelectedItem());
                                condition = true;
                            } else {
                                Statusbar.outputError("Failed to delete record!");
                            }
                        }else {
                            condition = true;
                            m_rowBind.getItems().remove(m_rowBind.getSelectedItem());
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

        if(condition){
            Statusbar.outputSuccess("Record Successfully Deleted!");
        }else {
            Statusbar.outputError("Failed to delete record!");
        }
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
			List<Rec_no_series_line> results = Rec_no_series_line.where("Series_Code = ?", m_listener.code());
			for(Rec_no_series_line result : results){
				MyRow row = new MyRow();
				row.setVartimestamp(result.getString(fieldNameArray.get(0)));
				row.setVarSeries_Code(result.getString(fieldNameArray.get(1)));
				row.setVarLine_No_(result.getString(fieldNameArray.get(2)));
				row.setVarStarting_Date(tool.formatSQLDate(result.getDate(fieldNameArray.get(3))));
				row.setVarStarting_No_(result.getString(fieldNameArray.get(4)));
				row.setVarEnding_No_(result.getString(fieldNameArray.get(5)));
				row.setVarWarning_No_(result.getString(fieldNameArray.get(6)));
				row.setVarIncrementby_No_(result.getString(fieldNameArray.get(7)));
				row.setVarLast_No_Used(result.getString(fieldNameArray.get(8)));
				row.setVarOpen(tool.intToBooleanToString(result.getInteger(fieldNameArray.get(9))));
				row.setVarLast_Date_Used(tool.formatSQLDate(result.getDate(fieldNameArray.get(10))));
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
 				xml.append("<t:gridcol text='" + fieldProperty.getString("caption") + "' width='100%'>");
				switch(fieldProperty.getString("componentType")) {
				case "Field":
					xml.append("<t:field text='" + ".{" + variableArray.get(col) + "}" + "' enabled='.{enabled}'/>");
					break;
                case "PKField":
                    xml.append("<t:field text='" + ".{" + variableArray.get(col) + "}" + "' enabled='.{enabledPK}'/>");
                    break;
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
					xml.append("<t:combofield activationhotkey = 'alt-40' enabled='.{enabled}' actionListener = '#{d.NumberSeriesLineList.onComboField"+fieldProperty.getString("fieldname")+"}' text='" + ".{" + variableArray.get(col) + "}" + "'></t:combofield>");
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
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return null;
		}
	}

	private ArrayList<String> currentRowArray(){
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(currentRecord.getVartimestamp());
		arrayList.add(currentRecord.getVarSeries_Code());
		arrayList.add(currentRecord.getVarLine_No_());
		arrayList.add(tool.formatToDefaultDate(currentRecord.getVarStarting_Date()));
		arrayList.add(currentRecord.getVarStarting_No_());
		arrayList.add(currentRecord.getVarEnding_No_());
		arrayList.add(currentRecord.getVarWarning_No_());
		arrayList.add(currentRecord.getVarIncrementby_No_());
		arrayList.add(currentRecord.getVarLast_No_Used());
		arrayList.add(tool.booleanToString(currentRecord.getVarOpen()));
		arrayList.add(tool.formatToDefaultDate(currentRecord.getVarLast_Date_Used()));
		return arrayList;
	}

	private ArrayList<String> pastRowArray(){
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(xRecord.getVartimestamp());
		arrayList.add(xRecord.getVarSeries_Code());
		arrayList.add(xRecord.getVarLine_No_());
		arrayList.add(tool.formatToDefaultDate(xRecord.getVarStarting_Date()));
		arrayList.add(xRecord.getVarStarting_No_());
		arrayList.add(xRecord.getVarEnding_No_());
		arrayList.add(xRecord.getVarWarning_No_());
		arrayList.add(xRecord.getVarIncrementby_No_());
		arrayList.add(xRecord.getVarLast_No_Used());
		arrayList.add(tool.booleanToString(xRecord.getVarOpen()));
		arrayList.add(tool.formatToDefaultDate(xRecord.getVarLast_Date_Used()));
		return arrayList;
	}

	private void refresh(){
		try{
			Base.open(ConnectionPool.dataSourcePooled);
			m_rowBind.getItems().clear();
			//TODO Get Table Data
            List<Rec_no_series_line> results = Rec_no_series_line.where("Series_Code = ?", m_listener.code());
			for(Rec_no_series_line result : results){
				MyRow row = new MyRow();
				row.setVartimestamp(result.getString(fieldNameArray.get(0)));
				row.setVarSeries_Code(result.getString(fieldNameArray.get(1)));
				row.setVarLine_No_(result.getString(fieldNameArray.get(2)));
				row.setVarStarting_Date(tool.formatSQLDate(result.getDate(fieldNameArray.get(3))));
				row.setVarStarting_No_(result.getString(fieldNameArray.get(4)));
				row.setVarEnding_No_(result.getString(fieldNameArray.get(5)));
				row.setVarWarning_No_(result.getString(fieldNameArray.get(6)));
				row.setVarIncrementby_No_(result.getString(fieldNameArray.get(7)));
				row.setVarLast_No_Used(result.getString(fieldNameArray.get(8)));
				row.setVarOpen(tool.intToBooleanToString(result.getInteger(fieldNameArray.get(9))));
				row.setVarLast_Date_Used(tool.formatSQLDate(result.getDate(fieldNameArray.get(10))));
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

	private void popupCard(ArrayList<String> row, boolean isEnabled){
		//Write code to pop up card
        /*try{
            NumberSeriesLineList popupCardVar = new NumberSeriesLineList();
            popupCardVar.setEnabled(isEnabled);
            popupCardVar.setListObjID(objectID);
            popupCardVar.setCardPageID(m_attrCardPageID);
            popupCardVar.setValues(row);
            popupCardVar.buildcard();

            ModalPopup popup = openModalPopup(popupCardVar, "NumberSeriesLineList Card", 0, 0,
                    new ModalPopup.IModalPopupListener() {
                        @Override
                        public void reactOnPopupClosedByUser() {
                            closePopup(popupCardVar);
                        }
                    });
            popup.setLeftTopReferenceCentered();
            popup.maximize(true);
            popupCardVar.prepare(new NumberSeriesLineList.ICloseListener() {
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
		Date date = new Date();
		MyRow row = new MyRow();
		row.setVartimestamp("");
		row.setVarSeries_Code("");
		row.setVarLine_No_("0");
		row.setVarStarting_Date(dateFormat.format(date));
		row.setVarStarting_No_("");
		row.setVarEnding_No_("");
		row.setVarWarning_No_("");
		row.setVarIncrementby_No_("0");
		row.setVarLast_No_Used("");
		row.setVarOpen("0");
		row.setVarLast_Date_Used(dateFormat.format(date));
		return row;
	}

	private ArrayList<String> emptyRowArray(){
		//TODO Get Date
		Date date = new Date();
		ArrayList<String> row = new ArrayList<String>();
		row.add("");
		row.add("");
		row.add("0");
		row.add(tool.formatJavaDate(date));
		row.add("");
		row.add("");
		row.add("");
		row.add("0");
		row.add("");
		row.add("0");
		row.add(tool.formatJavaDate(date));
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
		arrayList.add(m_attrInsertAllowed);
		arrayList.add(m_attrModifyAllowed);
		arrayList.add(true);
		arrayList.add(m_attrDeleteAllowed);
		return arrayList;
	}

	public interface IListener{
	    String code();
    }
	private IListener m_listener;
	public NumberSeriesLineList(){
	}
	public String getPageName() { return "/NumberSeriesLineList.jsp"; }
	public String getRootExpressionUsedInPage() { return "#{d.NumberSeriesLineList}"; }
	public void prepare(IListener listener){m_listener = listener;}
}

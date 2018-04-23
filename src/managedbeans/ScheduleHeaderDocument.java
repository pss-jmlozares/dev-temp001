package managedbeans;
import org.eclnt.jsfserver.elements.events.BaseActionEventFlush;
import org.eclnt.jsfserver.elements.impl.*;
import org.eclnt.jsfserver.elements.componentnodes.*;
import org.eclnt.jsfserver.defaultscreens.*;
import javax.faces.event.ActionEvent;
import org.eclnt.jsfserver.pagebean.*;
import modelclasses.*;
import org.javalite.activejdbc.*;
import java.sql.*;
import java.util.*;
import java.io.*;
import javax.faces.event.*;
import org.eclnt.editor.annotations.*;
import database.ConnectionPool;
import managedbeans.ScheduleHeaderList.MyRow;
@CCGenClass (expressionBase="#{d.ScheduleHeaderDocument}")

public class ScheduleHeaderDocument extends PageBean implements Serializable{
    private AppTool tool = new AppTool();
	private ScheduleLineList subForm;
	private Rec_schedule_header rec = new Rec_schedule_header();

    public ScheduleHeaderDocument(){}

    IPageBean includeList;
    public IPageBean getIncludeList() { return includeList; }
	ROWDYNAMICCONTENTBinding dynamicRibbon = new ROWDYNAMICCONTENTBinding();
	public ROWDYNAMICCONTENTBinding getDynamicRibbon() { return dynamicRibbon; }
    ROWDYNAMICCONTENTBinding dynamiccontent = new ROWDYNAMICCONTENTBinding();
    public ROWDYNAMICCONTENTBinding getDynamiccontent() { return dynamiccontent; }

	private int objectID;
	public void setObjectID(int value){this.objectID = value;}
	private int listObjID;
	public void setListObjID(int value) {this.listObjID = value;}

	private boolean enabled;
	public boolean getEnabled() {return enabled;}
	public void setEnabled(boolean value) {this.enabled = value;}
    private String fieldtimestamp;
    public String getFieldtimestamp(){return fieldtimestamp;}
    public void setFieldtimestamp(String value){this.fieldtimestamp = value;}
    private String fieldCode;
    public String getFieldCode(){return fieldCode;}
    public void setFieldCode(String value){this.fieldCode = value;}
    private String fieldDescription;
    public String getFieldDescription(){return fieldDescription;}
    public void setFieldDescription(String value){this.fieldDescription = value;}
    private String fieldCutoff_fromDate;
    public String getFieldCutoff_fromDate(){return fieldCutoff_fromDate;}
    public void setFieldCutoff_fromDate(String value){this.fieldCutoff_fromDate = value;}
    private String fieldCutoff_toDate;
    public String getFieldCutoff_toDate(){return fieldCutoff_toDate;}
    public void setFieldCutoff_toDate(String value){this.fieldCutoff_toDate = value;}
    
	public void onSendRequest(ActionEvent event){
	}

	public void onDeclineRequest(ActionEvent event){
	}

	public void onAcceptRequest(ActionEvent event){
	}

    public void onOk(ActionEvent event){
        if (!noEntry(getFieldDescription())){
            if(getEnabled()){
                if(getFieldtimestamp().equals("")){
                    if(rec.insert(fieldValues(), listObjID)){
                        closeListener.reactOnClose();
                    }
                }else{
                    if(rec.modify(fieldValues(), listObjID)){
                        closeListener.reactOnClose();
                    }
                }
            }
        }
    }

	public void setValues(ArrayList<String> row){
	setFieldtimestamp(row.get(0));
	setFieldCode(row.get(1));
	setFieldDescription(row.get(2));
	setFieldCutoff_fromDate(row.get(3));
	setFieldCutoff_toDate(row.get(4));
	}

	public void builddocument(){
//		preSave();

		int subPageID = 0;
		ArrayList<String> foldname = new ArrayList<String>();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<Integer> componentCount = new ArrayList<Integer>();
		ArrayList<String> componentType = new ArrayList<String>();
		ArrayList<String> componentStatus = new ArrayList<String>();
		ArrayList<Integer> getpaneindex = new ArrayList<Integer>();
		ArrayList<Integer> getColIndex = new ArrayList<Integer>();
		ArrayList<String> getfieldname = new ArrayList<String>();
		ArrayList<String> fieldNameArray = new ArrayList<>();
		int fold = 0;

		try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);

			List<Rec_sys_pageproperties_document_pane> pane = Rec_sys_pageproperties_document_pane.where("pageid = ?", 300010);
			for(Rec_sys_pageproperties_document_pane rec: pane){
				foldname.add(rec.getString("caption"));
			}

			List<Rec_sys_pageproperties_document_pane_paneitem> captions = Rec_sys_pageproperties_document_pane_paneitem.where("pageid = ? AND showComponent = ?", 300010, 1);
			for(Rec_sys_pageproperties_document_pane_paneitem caption : captions){
				list.add(caption.getString("fieldname"));
				componentCount.add(caption.getInteger("componentCount"));
				componentType.add(caption.getString("fieldtype"));
				componentStatus.add(tool.intToBooleanToString(caption.getInteger("componentStatus")));
				getpaneindex.add(caption.getInteger("foldablepaneindex"));
				getColIndex.add(caption.getInteger("colIndex"));
				getfieldname.add(caption.getString("caption"));
				fieldNameArray.add(caption.getString("fieldname"));
			}

			List<Rec_sys_pageproperties_document> pagecards = Rec_sys_pageproperties_document.where("pageid = ?", 300010);
			for(Rec_sys_pageproperties_document pagecard: pagecards){
				fold = pagecard.getInteger("foldablepanecount");
				subPageID = pagecard.getInteger("subPageID");
			}

			PANENode paneNode = new PANENode();
			paneNode.setWidth("100%");
			paneNode.setHeight("100%");
			{
				ROWDYNAMICCONTENTNode dynamicContentNode = new ROWDYNAMICCONTENTNode();
				dynamicContentNode.setContentbinding("#{d.ScheduleHeaderDocument.dynamicRibbon}");
				paneNode.addSubNode(dynamicContentNode);

				for(int count = 0; count < fold; count++){
					ROWDISTANCENode rowdistanceNode = new ROWDISTANCENode();
					paneNode.addSubNode(rowdistanceNode);
					rowdistanceNode.setHeight("10");

					ROWNode parentrow = new ROWNode();
					paneNode.addSubNode(parentrow);
					{
						FOLDABLEPANENode foldablePaneNode = new FOLDABLEPANENode();
						parentrow.addSubNode(foldablePaneNode);
						foldablePaneNode.setWidth("100%");
						foldablePaneNode.setHeight("100%");
						foldablePaneNode.setText(foldname.get(count));
						if(count == 0){
							foldablePaneNode.setOpened(true);
						}else{
							foldablePaneNode.setOpened(false);
						}

						{
							ROWNode row = new ROWNode();
							foldablePaneNode.addSubNode(row);
							{
								//TODO Loop For The Col Count
								for (int colIndex = 0; colIndex < pane.get(count).getInteger("colCount"); colIndex++){
									COLSYNCHEDPANENode colsynchedpaneNode = new COLSYNCHEDPANENode();
									row.addSubNode(colsynchedpaneNode);
									colsynchedpaneNode.setHeight("100%");

									ArrayList<String> options = null;
									for(int filter = 0; filter < list.size(); filter++){
										int change = getpaneindex.get(filter);
										int col = getColIndex.get(filter);
										if(change == count) {
											if (col == colIndex) {
												if (componentType.get(filter).equals("ComboBox")) {
													options = getOptions(fieldNameArray.get(filter));
												}
												NodeBuilder(colsynchedpaneNode, componentType.get(filter), getfieldname.get(filter), fieldNameArray.get(filter), options, componentStatus.get(filter));
											}
										}
									}
									COLDISTANCENode coldistanceNode = new COLDISTANCENode();
									coldistanceNode.setWidth(50);
									row.addSubNode(coldistanceNode);
								}
							}
						}
					}
				}
				ROWDISTANCENode rowdistanceNode = new ROWDISTANCENode();
				paneNode.addSubNode(rowdistanceNode);
				rowdistanceNode.setHeight("10");

				FOLDABLEPANENode lineFoldable = new FOLDABLEPANENode();
				lineFoldable.setWidth("100%");
				lineFoldable.setHeight("500");
				lineFoldable.setText("Line");
				paneNode.addSubNode(lineFoldable);
				{
					ROWPAGEBEANINCLUDENode pageBean = new ROWPAGEBEANINCLUDENode();
					pageBean.setPagebeanbinding("#{d.ScheduleHeaderDocument.includeList}");
					lineFoldable.addSubNode(pageBean);
				}
			}
			dynamiccontent.setContentNode(paneNode);

			//TODO Build Ribbon For This Document Page
			PANENode foldable = buildRibbonNodes(objectID);
			dynamicRibbon.setContentNode(foldable);
		}catch(DBException t){
			t.printStackTrace();
			Statusbar.outputAlert("You dont have permission to select records from this table!").setLeftTopReferenceCentered();
		}catch(Throwable t){
			t.printStackTrace();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
		}finally{
			Base.close();
			//TODO Instantiate Sub List
			instantiateSubList(subPageID);
		}
    }

	private void instantiateSubList(int subPageID) {
        try{
            subForm = new ScheduleLineList();
            subForm.setObjectID(subPageID);
            subForm.setSchdleCode(getFieldCode());
            subForm.setCutOffToDate(getFieldCutoff_toDate());
            subForm.setCutOffFromDate(getFieldCutoff_fromDate());
            subForm.initProperties();

			if (!getEnabled()){
			    subForm.setIsViewMode(true);
            }
            subForm.build();
            includeList = subForm;
        }catch(Throwable t){
            t.printStackTrace();
            Statusbar.outputAlert(t.toString());
        }
	}

	private void preSave(){
		try {
			if (getEnabled()) {
				if (getFieldtimestamp().equals("")) {
					if (rec.insert(fieldValues(), listObjID)) {
						setFieldtimestamp("x");
					}
				}
			}
		}catch (Throwable t){
			t.printStackTrace();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
		}
	}

	private ArrayList<String> getOptions(String fieldName) {
		try{
			ArrayList<String> options = new ArrayList<String>();
			options.clear();
			String optionString = null;
			List<Rec_sys_obj_prop_table_fieldcaption> fieldProps = Rec_sys_obj_prop_table_fieldcaption.where("objectid = ? AND fieldname = ?", listObjID, fieldName);
			for(Rec_sys_obj_prop_table_fieldcaption fieldProp : fieldProps) {
				optionString = fieldProp.getString("optionString");
			}
			String[] listOptionString = optionString.split(",");
			for(String x : listOptionString) {
				options.add(x);
			}
			return options;
		}catch(Throwable t){
			t.printStackTrace();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
			return null;
		}
	}

	private ArrayList<String> fieldValues(){
		ArrayList<String> arrayList = new ArrayList<>();
		arrayList.add(getFieldtimestamp());
		arrayList.add(getFieldCode());
		arrayList.add(getFieldDescription());
		arrayList.add(tool.formatToDefaultDate(getFieldCutoff_fromDate()));
		arrayList.add(tool.formatToDefaultDate(getFieldCutoff_toDate()));
		return arrayList;
	}

	private void NodeBuilder(COLSYNCHEDPANENode colsynchedpaneNode, String componentType, String fieldname, String field, ArrayList<String> options, String status){
		if (!getEnabled()){
			status = "false";
		}

       	COLSYNCHEDROWDISTANCENode colsynchedrowdistanceNode = new COLSYNCHEDROWDISTANCENode();
        colsynchedpaneNode.addSubNode(colsynchedrowdistanceNode);
        colsynchedrowdistanceNode.setHeight("10");
        
        COLSYNCHEDROWNode colsynchedrowNode = new COLSYNCHEDROWNode();
        colsynchedpaneNode.addSubNode(colsynchedrowNode);
        
        LABELNode labelNode = new LABELNode();
        colsynchedrowNode.addSubNode(labelNode);
        labelNode.setText(fieldname);
        
        COLDISTANCENode coldistanceNode = new COLDISTANCENode();
        colsynchedrowNode.addSubNode(coldistanceNode);
        coldistanceNode.setWidth("20");
        
		switch(componentType) {
		case "TextArea":
			TEXTAREANode textareaNode = new TEXTAREANode();
			colsynchedrowNode.addSubNode(textareaNode);
			textareaNode.setEnabled(status);
			textareaNode.setMaxlength(100);
			textareaNode.setText("#{d.ScheduleHeaderDocument.field"+field+"}");
			textareaNode.setWidth("250");
			textareaNode.setHeight("50");
			break;
		case "Field":
			FIELDNode fieldNode = new FIELDNode();
			colsynchedrowNode.addSubNode(fieldNode);
			fieldNode.setFlush(true);
			fieldNode.setEnabled(status);
            fieldNode.setActionListener("#{d.ScheduleHeaderDocument.onField"+field+"}");
			fieldNode.setText("#{d.ScheduleHeaderDocument.field"+field+"}");
			fieldNode.setWidth("250");
			break;
		case "CheckBox":
			CHECKBOXNode checkBoxNode = new CHECKBOXNode();
			colsynchedrowNode.addSubNode(checkBoxNode);
			checkBoxNode.setEnabled(status);
			checkBoxNode.setSelected("#{d.ScheduleHeaderDocument.field"+field+"}");
            checkBoxNode.setFlush(true);
			break;
		case "ComboBox":
			COMBOBOXNode comboBoxNode = new COMBOBOXNode();
			colsynchedrowNode.addSubNode(comboBoxNode);
			comboBoxNode.setEnabled(status);
			comboBoxNode.setValue("#{d.ScheduleHeaderDocument.field"+field+"}");
			comboBoxNode.setDrawoddevenrows(true);
			comboBoxNode.setWidth("250");
			for(int counter = 0; counter < options.size(); counter++) {
				{
					COMBOBOXITEMNode comboBoxItemNode = new COMBOBOXITEMNode();
					comboBoxItemNode.setValue(""+counter+"");
					comboBoxItemNode.setText(options.get(counter));
					comboBoxNode.addSubNode(comboBoxItemNode);
				}
			}
			break;
		case "ComboField":
			COMBOFIELDNode comboFieldNode = new COMBOFIELDNode();
			colsynchedrowNode.addSubNode(comboFieldNode);
			comboFieldNode.setFlush(true);
			comboFieldNode.setWidth(250);
			comboFieldNode.setEnabled(status);
			comboFieldNode.setActionListener("#{d.ScheduleHeaderDocument.onComboField"+field+"}");
			comboFieldNode.setText("#{d.ScheduleHeaderDocument.field"+field+"}");
			break;
		}
	}

	public PANENode buildRibbonNodes(int objID){
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
				foldable.setText("Function Menu");
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

    //TODO field in Code
    public void onFieldCode(ActionEvent event){
        if (event instanceof BaseActionEventFlush){
            subForm.setSchdleCode(getFieldCode());
            subForm.setCutOffFromDate(getFieldCutoff_fromDate());
            subForm.setCutOffToDate(getFieldCutoff_toDate());
            rec.insert(fieldValues(), listObjID);
            setFieldtimestamp("x");
        }
    }

    //TODO field in Description
    public void onFieldDescription(ActionEvent ae){
        if (ae instanceof BaseActionEventFlush){
            rec.modify(fieldValues(),listObjID);
        }
    }

    private boolean noEntry(String code){
        try{
            if(!"".equals(code)){
                return  false;
            }else{
                return true;
            }
        }catch(Throwable t){
            t.printStackTrace();
            Statusbar.outputAlert(t.toString());
            return false;
    }
        }

    ICloseListener closeListener;
     public interface ICloseListener{
        public void reactOnClose();
    }
    
    public void prepare(ICloseListener listener){
        try{
            closeListener = listener;
        }catch(Throwable t){
            Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
        }
    }
    public interface IListener {}
    private IListener m_listener;
    public void prepare(IListener listener) { m_listener = listener; }
    public String getPageName(){
        return "/ScheduleHeaderDocument.jsp";
    }
    @Override
    public String getRootExpressionUsedInPage(){
        return "#{d.ScheduleHeaderDocument}"; 
    }
}

package managedbeans;
import java.io.*;
import java.util.*;
import org.eclnt.editor.annotations.*;
import org.eclnt.jsfserver.defaultscreens.*;
import org.eclnt.jsfserver.elements.componentnodes.*;
import org.eclnt.jsfserver.elements.events.BaseActionEventValueHelp;
import org.eclnt.jsfserver.elements.impl.*;
import org.eclnt.jsfserver.pagebean.*;
import org.eclnt.util.valuemgmt.ValueManager;
import org.javalite.activejdbc.*;
import database.ConnectionPool;
import modelclasses.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import managedbeans.ProfileList.MyRow;

import javax.faces.event.ActionEvent;

@CCGenClass (expressionBase="#{d.ProfileCard}")

public class ProfileCard extends PageBean implements Serializable{
    public ProfileCard(){
    }

	private Rec_profile rec = new Rec_profile();

	private int listObjID;
	public void setListObjID(int value) {this.listObjID = value;}
	private int cardPageID;
	public void setCardPageID(int value) {this.cardPageID = value;}

	private AppTool tool = new AppTool();

	boolean enabled;
	public boolean getEnabled() {return enabled;}
	public void setEnabled(boolean value) {this.enabled = value;}
	String fieldtimestamp;
	public String getFieldtimestamp(){return fieldtimestamp;}
	public void setFieldtimestamp(String value){this.fieldtimestamp = value;}
	String fieldProfile_No_;
	public String getFieldProfile_No_(){return fieldProfile_No_;}
	public void setFieldProfile_No_(String value){this.fieldProfile_No_ = value;}
	String fieldFirst_Name;
	public String getFieldFirst_Name(){return fieldFirst_Name;}
	public void setFieldFirst_Name(String value){this.fieldFirst_Name = value;}
	String fieldMiddle_Name;
	public String getFieldMiddle_Name(){return fieldMiddle_Name;}
	public void setFieldMiddle_Name(String value){this.fieldMiddle_Name = value;}
	String fieldLast_Name;
	public String getFieldLast_Name(){return fieldLast_Name;}
	public void setFieldLast_Name(String value){this.fieldLast_Name = value;}
	String fieldAge;
	public String getFieldAge(){return fieldAge;}
	public void setFieldAge(String value){this.fieldAge = value;}
	String fieldGender;
	public String getFieldGender(){return fieldGender;}
	public void setFieldGender(String value){this.fieldGender = value;}
	String fieldBirthDate;
	public String getFieldBirthDate(){return fieldBirthDate;}
	public void setFieldBirthDate(String value){this.fieldBirthDate = value;}
	String fieldBirthPlace;
	public String getFieldBirthPlace(){return fieldBirthPlace;}
	public void setFieldBirthPlace(String value){this.fieldBirthPlace = value;}
	String fieldReligion;
	public String getFieldReligion(){return fieldReligion;}
	public void setFieldReligion(String value){this.fieldReligion = value;}
	String fieldPicture;
	public String getFieldPicture(){return fieldPicture;}
	public void setFieldPicture(String value){this.fieldPicture = value;}
	String fieldDepartment;
	public String getFieldDepartment(){return fieldDepartment;}
	public void setFieldDepartment(String value){this.fieldDepartment = value;}
	String fieldPosition;
	public String getFieldPosition(){return fieldPosition;}
	public void setFieldPosition(String value){this.fieldPosition = value;}
	String fielddefault_morning_IN;
	public String getFielddefault_morning_IN(){return fielddefault_morning_IN;}
	public void setFielddefault_morning_IN(String value){this.fielddefault_morning_IN = value;}
	String fielddefault_morning_OUT;
	public String getFielddefault_morning_OUT(){return fielddefault_morning_OUT;}
	public void setFielddefault_morning_OUT(String value){this.fielddefault_morning_OUT = value;}
	String fielddefault_afternoon_IN;
	public String getFielddefault_afternoon_IN(){return fielddefault_afternoon_IN;}
	public void setFielddefault_afternoon_IN(String value){this.fielddefault_afternoon_IN = value;}
	String fielddefault_afternoon_OUT;
	public String getFielddefault_afternoon_OUT(){return fielddefault_afternoon_OUT;}
	public void setFielddefault_afternoon_OUT(String value){this.fielddefault_afternoon_OUT = value;}
	String fieldRest_Day;
	public String getFieldRest_Day(){return fieldRest_Day;}
	public void setFieldRest_Day(String value){this.fieldRest_Day = value;}

    ROWDYNAMICCONTENTBinding dynamiccontent = new ROWDYNAMICCONTENTBinding();
    public ROWDYNAMICCONTENTBinding getDynamiccontent() { return dynamiccontent; }
    ROWDYNAMICCONTENTBinding dynamicRibbon = new ROWDYNAMICCONTENTBinding();
    public ROWDYNAMICCONTENTBinding getDynamicRibbon() { return dynamicRibbon; }

    public void onPreview(javax.faces.event.ActionEvent event){
        try{
            Base.open(ConnectionPool.dataSourcePooled);
            ImageViewer imv = new ImageViewer();
            imv.setNo(getFieldProfile_No_());
            if (!getEnabled()){
                imv.setViewMode(true);
            }
            Rec_profile list = Rec_profile.findFirst("Profile_No_ = ?", getFieldProfile_No_());
            imv.setImportPhoto("/hex("+ ValueManager.encodeHexString(list.getBytes("Picture"))+")");

            ModalPopup modalPopup = openModalPopup(imv, "Image Preview", 500, 400, new ModalPopup.IModalPopupListener() {
                @Override
                public void reactOnPopupClosedByUser() {
                    closePopup(imv);

                }
            });
            imv.prepare(new ImageViewer.ICloseListener() {
                @Override
                public void reactOnClose() {
                    closePopup(imv);
                }
            });
            modalPopup.setLeftTopReferenceCentered();
            modalPopup.maximize(false);
        }catch(Throwable t){
            t.printStackTrace();
            Statusbar.outputAlert(t.toString());
        } finally {
            Base.close();
        }
    }

	public void onOk(javax.faces.event.ActionEvent event) {
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
		}else{
			closeListener.reactOnClose();
		}
	}

	public void setValues(ArrayList<String> row){
		setFieldtimestamp(row.get(0));
		setFieldProfile_No_(row.get(1));
		setFieldFirst_Name(row.get(2));
		setFieldMiddle_Name(row.get(3));
		setFieldLast_Name(row.get(4));
		setFieldAge(row.get(5));
		setFieldGender(row.get(6));
		setFieldBirthDate(row.get(7));
		setFieldBirthPlace(row.get(8));
		setFieldReligion(row.get(9));
		setFieldPicture(row.get(10));
		setFieldDepartment(row.get(11));
		setFieldPosition(row.get(12));
		setFielddefault_morning_IN(row.get(13));
		setFielddefault_morning_OUT(row.get(14));
		setFielddefault_afternoon_IN(row.get(15));
		setFielddefault_afternoon_OUT(row.get(16));
		setFieldRest_Day(row.get(17));
	}

    public void buildcard(){
		preSave();
		ArrayList<String> foldname = new ArrayList<String>();
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<Integer> componentCount = new ArrayList<Integer>();
		ArrayList<String> componentType = new ArrayList<String>();
		ArrayList<Integer> getpaneindex = new ArrayList<Integer>();
		ArrayList<Integer> getColIndex = new ArrayList<Integer>();
		ArrayList<String> getcardfieldname = new ArrayList<String>();
		int fold = 0;
    	try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);

			List<Rec_sys_pageproperties_pagecard_pane> pane = Rec_sys_pageproperties_pagecard_pane.where("pageid = ?", cardPageID);
			for(Rec_sys_pageproperties_pagecard_pane rec: pane){
				foldname.add(rec.getString("caption"));
			}

			List<Rec_sys_pageproperties_pagecard_pane_paneitem> captions = Rec_sys_pageproperties_pagecard_pane_paneitem.where("pageid = ? AND showComponent = ?", cardPageID, 1);
			for(Rec_sys_pageproperties_pagecard_pane_paneitem caption : captions){
				list.add(caption.getString("caption"));
				componentCount.add(caption.getInteger("componentCount"));
				componentType.add(caption.getString("fieldtype"));
				getpaneindex.add(caption.getInteger("foldablepaneindex"));
				getColIndex.add(caption.getInteger("colIndex"));
				getcardfieldname.add(caption.getString("fieldname"));
			}

			List<Rec_sys_pageproperties_pagecard> pagecards = Rec_sys_pageproperties_pagecard.where("pageid = ?", cardPageID);
			for(Rec_sys_pageproperties_pagecard pagecard: pagecards){
				fold = pagecard.getInteger("foldablepanecount");
			}

			PANENode paneNode = new PANENode();
			paneNode.setHeight("100%");
			paneNode.setWidth("100%");
			paneNode.setBackground("#DBDBDB");
			paneNode.setPadding("10");
			{
				ROWDYNAMICCONTENTNode dynamicContentNode = new ROWDYNAMICCONTENTNode();
				dynamicContentNode.setContentbinding("#{d.ProfileCard.dynamicRibbon}");
				paneNode.addSubNode(dynamicContentNode);

				for(int count = 0; count < fold; count++){
					ROWDISTANCENode rowdistanceNode = new ROWDISTANCENode();
					paneNode.addSubNode(rowdistanceNode);
					rowdistanceNode.setHeight("10");

					ROWNode rowNodeparent = new ROWNode();
					paneNode.addSubNode(rowNodeparent);
					{
						FOLDABLEPANENode foldablepaneNode = new FOLDABLEPANENode();
						rowNodeparent.addSubNode(foldablepaneNode);
						foldablepaneNode.setHeight("100%");
						foldablepaneNode.setWidth("100%");
						foldablepaneNode.setText(foldname.get(count));

						if(count == 0){
							foldablepaneNode.setOpened(true);
						}else{
							foldablepaneNode.setOpened(false);
						}

						{
							ROWNode rowNode = new ROWNode();
							foldablepaneNode.addSubNode(rowNode);
							{
								//TODO Loop For The Col Count
								for (int colIndex = 0; colIndex < pane.get(count).getInteger("colCount"); colIndex++){
									COLSYNCHEDPANENode colsynchedpaneNode = new COLSYNCHEDPANENode();
									rowNode.addSubNode(colsynchedpaneNode);
									colsynchedpaneNode.setHeight("100%");

									ArrayList<String> options = null;
									for(int filter = 0; filter < list.size(); filter++){
										int change = getpaneindex.get(filter);
										int col = getColIndex.get(filter);
										if(change == count){
											if(col == colIndex) {
												if (componentType.get(filter).equals("ComboBox")) {
													options = getOptions(listfieldnames().get(filter));
												}
												NodeBuilder(colsynchedpaneNode, componentType.get(filter), list.get(filter), listfieldnames().get(filter), options);
											}
										}
									}

									COLDISTANCENode coldistanceNode = new COLDISTANCENode();
									coldistanceNode.setWidth(50);
									rowNode.addSubNode(coldistanceNode);
								}
							}
						}
					}
				}
			}
			dynamiccontent.setContentNode(paneNode);

			//CHANGE THE PARAMETER VALUE
			PANENode foldable = buildRibbonNodes(cardPageID);
			dynamicRibbon.setContentNode(foldable);

		}catch(Throwable t){
			t.printStackTrace();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
		}finally{
			Base.close();
		}

	}

	private ArrayList<String> fieldValues(){
		ArrayList<String> arrayList = new ArrayList<String>();
		arrayList.add(getFieldtimestamp());
		arrayList.add(getFieldProfile_No_());
		arrayList.add(getFieldFirst_Name());
		arrayList.add(getFieldMiddle_Name());
		arrayList.add(getFieldLast_Name());
		arrayList.add(getFieldAge());
		arrayList.add(getFieldGender());
		arrayList.add(tool.formatToDefaultDate(getFieldBirthDate()));
		arrayList.add(getFieldBirthPlace());
		arrayList.add(getFieldReligion());
		arrayList.add(getFieldPicture());
		arrayList.add(getFieldDepartment());
		arrayList.add(getFieldPosition());
		arrayList.add(tool.formatTime24H(getFielddefault_morning_IN()));
		arrayList.add(tool.formatTime24H(getFielddefault_morning_OUT()));
		arrayList.add(tool.formatTime24H(getFielddefault_afternoon_IN()));
		arrayList.add(tool.formatTime24H(getFielddefault_afternoon_OUT()));
		arrayList.add(getFieldRest_Day());
		return arrayList;
	}

	private void preSave(){
		try{
			if (getEnabled()) {
				if (getFieldtimestamp().equals("")) {
					if (rec.insert(fieldValues(), listObjID)) {
						setFieldtimestamp("x");
					}
				}
			}
		}catch (Throwable t){
			t.printStackTrace();
			Statusbar.outputAlert(t.toString());
		}
	}

    private ArrayList<String> listfieldnames(){
        try{
            ArrayList<String> list = new ArrayList<String>();
            List<Rec_sys_pageproperties_pagecard_pane_paneitem> s = Rec_sys_pageproperties_pagecard_pane_paneitem.where("pageid = ? AND showComponent = ?", cardPageID, 1);
            for (Rec_sys_pageproperties_pagecard_pane_paneitem c : s){
                list.add(c.getString("fieldname"));
            }
            return list;
        }catch (Throwable t) {
            t.printStackTrace();
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
            return null;
        }
    }

	private ArrayList<String> getOptions(String fieldName){
		try {
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
			Statusbar.outputError(t.toString());
			return null;
		}
	}

	public void NodeBuilder(COLSYNCHEDPANENode colsynchedpaneNode, String componentType, String fieldname, String count, ArrayList<String> options){
    	COLSYNCHEDROWDISTANCENode colsynchedrowdistanceNode = new COLSYNCHEDROWDISTANCENode();
        colsynchedpaneNode.addSubNode(colsynchedrowdistanceNode);
        colsynchedrowdistanceNode.setHeight("10");

        COLSYNCHEDROWNode colsynchedrowNode = new COLSYNCHEDROWNode();
        colsynchedpaneNode.addSubNode(colsynchedrowNode);
        {
            LABELNode labelNode = new LABELNode();
            colsynchedrowNode.addSubNode(labelNode);
            labelNode.setText(fieldname);

            COLDISTANCENode coldistanceNode = new COLDISTANCENode();
            colsynchedrowNode.addSubNode(coldistanceNode);
            coldistanceNode.setWidth("20");

			switch(componentType) {
			case "Field":
				FIELDNode fieldNode = new FIELDNode();
				colsynchedrowNode.addSubNode(fieldNode);
				if (count.equals("Profile_No_")){
				    if(count.equals("")){
				        fieldNode.setEnabled(true);
                    } else {
				        fieldNode.setEnabled(false);
                    }
                } else {
                    fieldNode.setEnabled("#{d.ProfileCard.enabled}");
                }

                if (count.equals("Age")){
				    fieldNode.setRestricttokeys("0123456789");
                } else {
                    fieldNode.setRestricttokeys("");
                }
				fieldNode.setText("#{d.ProfileCard.field"+count+"}");
				fieldNode.setWidth("250");
				break;
			case "CheckBox":
				CHECKBOXNode checkBoxNode = new CHECKBOXNode();
				colsynchedrowNode.addSubNode(checkBoxNode);
				checkBoxNode.setEnabled("#{d.ProfileCard.enabled}");
				checkBoxNode.setSelected("#{d.ProfileCard.field"+count+"}");
				break;
			case "ComboBox":
				COMBOBOXNode comboBoxNode = new COMBOBOXNode();
				colsynchedrowNode.addSubNode(comboBoxNode);
				comboBoxNode.setEnabled("#{d.ProfileCard.enabled}");
				comboBoxNode.setValue("#{d.ProfileCard.field"+count+"}");
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
				comboFieldNode.setWidth(250);
				comboFieldNode.setEnabled("#{d.ProfileCard.enabled}");
				comboFieldNode.setActionListener("#{d.ProfileCard.onComboField"+count+"}");
				comboFieldNode.setText("#{d.ProfileCard.field"+count+"}");
				break;
			}
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

    //TODO onComboField Department
    public void onComboFieldDepartment(ActionEvent event){
        if (event instanceof BaseActionEventValueHelp){
            try{
                Base.open(ConnectionPool.dataSourcePooled);

                IdTextSelection select = IdTextSelection.createInstance();

                List<Rec_department_setup> series = Rec_department_setup.findAll();
                for (Rec_department_setup no:series){
                    select.addLine(no.getString("Code"),no.getString("Description"));
                }
                select.setCallBack(new ISetId() {
                    @Override
                    public void setId(String s) {
                        setFieldDepartment(s);
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

    //TODO onComboField Position
    public void onComboFieldPosition(ActionEvent event){
        if (event instanceof BaseActionEventValueHelp){
            try{
                Base.open(ConnectionPool.dataSourcePooled);

                IdTextSelection select = IdTextSelection.createInstance();

                List<Rec_position_setup> series = Rec_position_setup.findAll();
                for (Rec_position_setup no:series){
                    select.addLine(no.getString("Code"),no.getString("Description"));
                }
                select.setCallBack(new ISetId() {
                    @Override
                    public void setId(String s) {
                        setFieldPosition(s);
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

    public void onComboFieldRest_Day(ActionEvent ae){
        if (ae instanceof BaseActionEventValueHelp){
            try{
                Base.open(ConnectionPool.dataSourcePooled);

                IdTextSelection select = IdTextSelection.createListInstance();

                select.addLine("01","SUNDAY");
                select.addLine("02","MONDAY");
                select.addLine("03","TUESDAY");
                select.addLine("04","WEDNESDAY");
                select.addLine("05","THURSDAY");
                select.addLine("06","FRIDAY");
                select.addLine("07","SATURDAY");

                select.setCallBack(new ISetIdText() {
                    @Override
                    public void setIdText(String s,String s1) {
                        setFieldRest_Day(s1);
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

	public interface ICloseListener{void reactOnClose();}
	ICloseListener closeListener;
	public void prepare(ICloseListener listener) {
		try{
			closeListener = listener;
		}catch(Throwable t){
			Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
		}
	}
    public interface IListener{}
    private IListener m_listener;
    public void prepare(IListener listener){
         m_listener = listener;
    }
    @Override
    public String getPageName(){
        return "/ProfileCard.jsp";
    }
    @Override
    public String getRootExpressionUsedInPage(){
        return "#{d.ProfileCard}";
    }
}

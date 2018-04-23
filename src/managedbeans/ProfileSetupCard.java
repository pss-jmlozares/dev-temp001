package managedbeans;
import java.io.*;
import java.util.*;
import org.eclnt.editor.annotations.*;
import org.eclnt.jsfserver.defaultscreens.*;
import org.eclnt.jsfserver.elements.componentnodes.*;
import org.eclnt.jsfserver.elements.events.BaseActionEventValueHelp;
import org.eclnt.jsfserver.elements.impl.*;
import org.eclnt.jsfserver.pagebean.*;
import org.javalite.activejdbc.*;
import database.ConnectionPool;
import modelclasses.*;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import managedbeans.ProfileSetupList.MyRow;

import javax.faces.event.ActionEvent;

@CCGenClass (expressionBase="#{d.ProfileSetupCard}")

public class ProfileSetupCard extends PageBean implements Serializable{
    public ProfileSetupCard(){
    }
    
	private Rec_profile_setup rec = new Rec_profile_setup();

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
	String fieldPrimary_Key;
	public String getFieldPrimary_Key(){return fieldPrimary_Key;}
	public void setFieldPrimary_Key(String value){this.fieldPrimary_Key = value;}
	String fieldProfile_Nos;
	public String getFieldProfile_Nos(){return fieldProfile_Nos;}
	public void setFieldProfile_Nos(String value){this.fieldProfile_Nos = value;}
	
    ROWDYNAMICCONTENTBinding dynamiccontent = new ROWDYNAMICCONTENTBinding();
    public ROWDYNAMICCONTENTBinding getDynamiccontent() { return dynamiccontent; }
    ROWDYNAMICCONTENTBinding dynamicRibbon = new ROWDYNAMICCONTENTBinding();
    public ROWDYNAMICCONTENTBinding getDynamicRibbon() { return dynamicRibbon; }
    
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
		setFieldPrimary_Key(row.get(1));
		setFieldProfile_Nos(row.get(2));
	}

    public void buildcard(){
//		preSave();
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
				dynamicContentNode.setContentbinding("#{d.ProfileSetupCard.dynamicRibbon}");
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
//			PANENode foldable = buildRibbonNodes(cardPageID);
//			dynamicRibbon.setContentNode(foldable);

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
		arrayList.add(getFieldPrimary_Key());
		arrayList.add(getFieldProfile_Nos());
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

				if(count.equals("Primary_Key")){
					if(!getFieldPrimary_Key().equals("")){
						fieldNode.setEnabled(false);
					}else{
						fieldNode.setEnabled(true);
					}
				}else {

				}

				fieldNode.setText("#{d.ProfileSetupCard.field"+count+"}");
				fieldNode.setWidth("250");
				break;
			case "CheckBox":
				CHECKBOXNode checkBoxNode = new CHECKBOXNode();
				colsynchedrowNode.addSubNode(checkBoxNode);
				checkBoxNode.setEnabled("#{d.ProfileSetupCard.enabled}");
				checkBoxNode.setSelected("#{d.ProfileSetupCard.field"+count+"}");
				break;
			case "ComboBox":
				COMBOBOXNode comboBoxNode = new COMBOBOXNode();
				colsynchedrowNode.addSubNode(comboBoxNode);
				comboBoxNode.setEnabled("#{d.ProfileSetupCard.enabled}");
				comboBoxNode.setValue("#{d.ProfileSetupCard.field"+count+"}");
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
				comboFieldNode.setEnabled("#{d.ProfileSetupCard.enabled}");
				comboFieldNode.setActionListener("#{d.ProfileSetupCard.onComboField"+count+"}");
				comboFieldNode.setText("#{d.ProfileSetupCard.field"+count+"}");
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

    //TODO onComboField Profile Nos
    public void onComboFieldProfile_Nos(ActionEvent event){
        if (event instanceof BaseActionEventValueHelp){
            try{
                Base.open(ConnectionPool.dataSourcePooled);

                IdTextSelection select = IdTextSelection.createInstance();

                List<Rec_no_series> series = Rec_no_series.findAll();
                for (Rec_no_series no:series){
                    select.addLine(no.getString("Code"),no.getString("Description"));
                }
                select.setCallBack(new ISetId() {
                    @Override
                    public void setId(String s) {
                        setFieldProfile_Nos(s);
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
        return "/ProfileSetupCard.jsp";
    }
    @Override
    public String getRootExpressionUsedInPage(){
        return "#{d.ProfileSetupCard}"; 
    }
}

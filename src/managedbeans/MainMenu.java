package managedbeans;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import org.eclnt.editor.annotations.*;
import org.eclnt.jsfserver.defaultscreens.*;
import org.eclnt.jsfserver.elements.componentnodes.*;
import org.eclnt.jsfserver.elements.impl.*;
import org.eclnt.jsfserver.pagebean.*;
import org.javalite.activejdbc.*;
import org.eclnt.jsfserver.util.HttpSessionAccess;
import modelclasses.*;
import database.ConnectionPool;
@CCGenClass (expressionBase="#{d.MainMenu}")
public class MainMenu extends PageBean implements Serializable{
	IPageBean m_instanceList;
	public IPageBean getInstanceList() { return m_instanceList; }
	int m_contentIndex = 0;
	public int getContentIndex() { return m_contentIndex; }
	public void setContentIndex(int value) { this.m_contentIndex = value; }
	FIXGRIDTreeBinding<TreeNode> m_tree0 = new FIXGRIDTreeBinding<TreeNode>();
	public FIXGRIDTreeBinding<TreeNode> getTree0() { return m_tree0; }
	FIXGRIDTreeBinding<TreeNode> m_tree1 = new FIXGRIDTreeBinding<TreeNode>();
	public FIXGRIDTreeBinding<TreeNode> getTree1() { return m_tree1; }
	ROWDYNAMICCONTENTBinding m_dynamicBarContent = new ROWDYNAMICCONTENTBinding();
	public ROWDYNAMICCONTENTBinding getDynamicBarContent() { return m_dynamicBarContent; }
	ROWDYNAMICCONTENTBinding m_dynamicOutlookBar = new ROWDYNAMICCONTENTBinding();
	public ROWDYNAMICCONTENTBinding getDynamicOutlookBar() { return m_dynamicOutlookBar; }
	public class TreeNode extends FIXGRIDTreeItem implements java.io.Serializable{
		String i_runParam;
		public String getRunParam() { return i_runParam; }
		public void setRunParam(String value) { this.i_runParam = value; }
		String i_methodName;
		public String getMethodName() { return i_methodName; }
		public void setMethodName(String value) { this.i_methodName = value; }
		String i_className;
		public String getClassName() {return i_className;}
		public void setClassName(String value) {this.i_className = value;}
		public TreeNode(FIXGRIDTreeItem parentNode, String text, String runParam, String methodName, int status, String className){
			super(parentNode);
			setText(text);
			setRunParam(runParam);
			setMethodName(methodName);
			setStatus(status);
			setClassName(className);
		}
		public void onRowSelect(){
			if(getStatusInt() == STATUS_CLOSED) {
				setStatus(STATUS_OPENED);
			}else if(getStatusInt() == STATUS_OPENED) {
				setStatus(STATUS_CLOSED);
			}else if(getStatusInt() == STATUS_ENDNODE) {
				try {
					ConnectionPool connPool = ConnectionPool.getInstance();
					List<Rec_sys_obj_prop_menusuite_org> list = connPool.getTypeAndObjectID(getRunParam());
					for(Rec_sys_obj_prop_menusuite_org rec : list) {
						if(!rec.getString("RunObjectType").equals(null) && rec.getInteger("RunObjectID") != 0) {
							switch(rec.getString("RunObjectType")) {
							case "Page":
								Class<?> clazz = Class.forName("managedbeans."+getClassName()+"");
								Object obj = clazz.newInstance();
								Method method = clazz.getDeclaredMethod("setObjectID", int.class);
								Method method2 = clazz.getDeclaredMethod("initProperties");
								Method method3 = clazz.getDeclaredMethod("build");
								method.invoke(obj, rec.getInteger("RunObjectID"));
								method2.invoke(obj);
								method3.invoke(obj);
								m_instanceList = (IPageBean) obj;
								break;
							}
						}
					}
				} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public void onMenuItem0(javax.faces.event.ActionEvent event) {buildMenuNodes("#{d.MainMenu.tree0}");}
	public void onMenuItem1(javax.faces.event.ActionEvent event) {buildMenuNodes("#{d.MainMenu.tree1}");}
	public void buildMenuNodes(String objectBind){
		PANENode pane = new PANENode();
		pane.setHeight("100%");
		pane.setWidth("100%");
		{
			ROWNode row = new ROWNode();
			pane.addSubNode(row);
			{
				FIXGRIDNode grid = new FIXGRIDNode();
				grid.setObjectbinding(objectBind);
				grid.setSuppressheadline("true");
				grid.setWidth("100%");
				grid.setHeight("100%");
				grid.setSbvisibleamount("15");
				grid.setReselectable(true);
				row.addSubNode(grid);
				{
					GRIDCOLNode col = new GRIDCOLNode();
					col.setWidth("100%");
					grid.addSubNode(col);
					{
						TREENODENode tree = new TREENODENode();
						col.addSubNode(tree);
					}
				}
			}
		}
		m_dynamicBarContent.setContentNode(pane);
	}
	public void buildMenuSuite(){
		try{
			ConnectionPool.getInstance();
			Base.open(ConnectionPool.dataSourcePooled);
			Base.openTransaction();
			ArrayList<TreeNode> treeNodeElement = new ArrayList<TreeNode>();
			ArrayList<String> treeNodeGUID = new ArrayList<String>();
			ArrayList<FIXGRIDTreeBinding<TreeNode>> fixGrid = new ArrayList<FIXGRIDTreeBinding<TreeNode>>();
			FIXGRIDTreeBinding<TreeNode> fixGridX = new FIXGRIDTreeBinding<TreeNode>();
			fixGrid.add(m_tree0);
			fixGrid.add(m_tree1);
				OUTLOOKBARNode outlookBarNode = new OUTLOOKBARNode();
				outlookBarNode.setHeight("100%");
				outlookBarNode.setWidth("100%");
				outlookBarNode.setValue("#{d.MainMenu.contentIndex}");
				{
				List<Rec_sys_obj_prop_menusuite_org> list2 = Rec_sys_obj_prop_menusuite_org.where("Type = ? AND Visible = ? AND Level = ?", "Menu", "Yes", 1).orderBy("Sequence asc");
				int countX = 0;
					for(Rec_sys_obj_prop_menusuite_org rec2 : list2) {
						String menuGUID = rec2.getString("GUID");
						OUTLOOKBARITEMNode outlookBarItemNode = new OUTLOOKBARITEMNode();
						outlookBarItemNode.setText(rec2.getString("Caption"));
						outlookBarItemNode.setActionListener("#{d.MainMenu.onMenuItem" + countX +"}");
						outlookBarNode.addSubNode(outlookBarItemNode);
						for(int levelCounter = 2; levelCounter <= 10; levelCounter++) {
							List<Rec_sys_obj_prop_menusuite_org> list = Rec_sys_obj_prop_menusuite_org.where("MenuGUID = ? AND Level = ? AND Visible = ?", menuGUID, levelCounter, "Yes").orderBy("Sequence asc");
							for(Rec_sys_obj_prop_menusuite_org rec : list) {
								treeNodeGUID.add(rec.getString("GUID"));
								TreeNode nodeElement = null;
								if(levelCounter == 2) {
									if(rec.getString("Type").equals("MenuGroup")) {
										fixGridX = fixGrid.get(countX);
										nodeElement = new TreeNode(fixGridX.getRootNode(), rec.getString("Caption"), rec.getString("GUID"), "method" + rec.getInteger("id") +"", TreeNode.STATUS_CLOSED, rec.getString("ClassName"));
									}else{
										nodeElement = new TreeNode(fixGridX.getRootNode(), rec.getString("Caption"), rec.getString("GUID"), "method" + rec.getInteger("id") +"", TreeNode.STATUS_ENDNODE, rec.getString("ClassName"));
									}
								}else{
									if(treeNodeGUID.contains(rec.getString("ParentGUID"))) {
										if(rec.getString("Type").equals("MenuGroup")) {
											nodeElement = new TreeNode(treeNodeElement.get(treeNodeGUID.indexOf(rec.getString("ParentGUID"))), rec.getString("Caption"), rec.getString("GUID"), "method" + rec.getInteger("id") +"", TreeNode.STATUS_CLOSED, rec.getString("ClassName"));
										}else{
											nodeElement = new TreeNode(treeNodeElement.get(treeNodeGUID.indexOf(rec.getString("ParentGUID"))), rec.getString("Caption"), rec.getString("GUID"), "method" + rec.getInteger("id") +"", TreeNode.STATUS_ENDNODE, rec.getString("ClassName"));
										}
									}
								}
								treeNodeElement.add(nodeElement);
							}
							Base.commitTransaction();
						}
						countX++;
					}
					Base.commitTransaction();
					OUTLOOKBARCONTENTNode outlookBarContentNode = new OUTLOOKBARCONTENTNode();

					outlookBarNode.addSubNode(outlookBarContentNode);
					{
						ROWDYNAMICCONTENTNode rowDynamicContentNode = new ROWDYNAMICCONTENTNode();
						rowDynamicContentNode.setContentbinding("#{d.MainMenu.dynamicBarContent}");
						outlookBarContentNode.addSubNode(rowDynamicContentNode);
					}
				}
				m_dynamicOutlookBar.setContentNode(outlookBarNode);
		}catch(Throwable t){
			Base.rollbackTransaction();
			t.printStackTrace();
			Statusbar.outputAlert(t.toString());
		}finally{
			Base.close();
		}
	}
//	private void openModalPopup() {
//		final UserLogIn uli = new UserLogIn();
//		ModalPopup modal = openModalPopup(uli, "Log In", 374, 178, new ModalPopup.IModalPopupListener() {
//			@Override
//			public void reactOnPopupClosedByUser(){
//				closePopup(uli);
//			}
//		});
//		uli.prepare(new UserLogIn.ICloseListener(){
//			@Override
//			public void reactOnClose(){
//				modal.close();
//				HttpSessionAccess.getCurrentHttpSession().setAttribute("UserID", uli.getUserName());
//				buildMenuNodes("#{d.MainMenu.tree0}");
//				buildMenuSuite();
//			}
//		});
//	}
	public interface IListener{}
	private IListener m_listener;
	public MainMenu(){
		buildMenuNodes("#{d.MainMenu.tree0}");
		buildMenuSuite();
	}
	public String getPageName() { return "/MainMenu.jsp"; }
	public String getRootExpressionUsedInPage() { return "#{d.MainMenu}"; }
	public void prepare(IListener listener){m_listener = listener;}
}

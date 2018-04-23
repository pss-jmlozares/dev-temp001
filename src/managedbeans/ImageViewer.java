package managedbeans;

import database.ConnectionPool;
import modelclasses.Rec_profile;
import org.eclnt.editor.annotations.CCGenClass;
import org.eclnt.jsfserver.defaultscreens.Statusbar;
import org.eclnt.jsfserver.elements.events.BaseActionEventUpload;
import org.eclnt.jsfserver.pagebean.PageBean;
import org.eclnt.util.valuemgmt.ValueManager;
import org.javalite.activejdbc.Base;

import javax.faces.event.ActionEvent;
import java.io.Serializable;

@CCGenClass(expressionBase = "#{d.ImageViewer}")

public class ImageViewer
        extends PageBean
        implements Serializable {
    String m_ImportPhoto;
    String No;
    ImageViewer.ICloseListener closeListener;
    private IListener m_listener;
    private boolean viewMode;

    public ImageViewer() {
    }

    public String getImportPhoto() {
        return m_ImportPhoto;
    }


    // ------------------------------------------------------------------------
    // inner classes
    // ------------------------------------------------------------------------

    public void setImportPhoto(String value) {
        this.m_ImportPhoto = value;
    }

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    public void setNo(String no) {
        this.No = no;
    }


    // ------------------------------------------------------------------------
    // constructors & initialization
    // ------------------------------------------------------------------------

    public void onOKMethod(javax.faces.event.ActionEvent event) {
        try {
            closeListener.reactOnClose();

        } catch (Throwable t) {
            t.printStackTrace();
            Statusbar.outputAlert(t.toString());
        }
    }

    public void onImportPhoto(ActionEvent event) {
        if (!viewMode) {
            if (event instanceof BaseActionEventUpload) {
                try {
                    ConnectionPool.getInstance();
                    Base.open(ConnectionPool.dataSourcePooled);

                    BaseActionEventUpload bae = (BaseActionEventUpload) event;
                    byte[] imageByte = bae.getHexBytes();
                    setImportPhoto("/hex(" + ValueManager.encodeHexString(imageByte) + ")");

                    Rec_profile rec_profile = Rec_profile.findFirst("Profile_No_ = ?", No);
                    rec_profile.set("Picture", imageByte);
                    rec_profile.saveIt();

                } catch (Throwable t) {
                    t.printStackTrace();
                    Statusbar.outputAlert(t.toString());
                } finally {
                    Base.close();
                }
            }
        }
    }

    public String getPageName() {
        return "/ImageViewer.jsp";
    }

    public String getRootExpressionUsedInPage() {
        return "#{d.ImageViewer}";
    }

    public void setViewMode(boolean viewMode) {
        this.viewMode = viewMode;
    }

    public void prepare(ImageViewer.ICloseListener listener) {
        try {
            closeListener = listener;
        } catch (Throwable t) {
            Statusbar.outputAlert(t.toString()).setLeftTopReferenceCentered();
        }
    }

    public void prepare(ImageViewer.IListener listener) {
        m_listener = listener;
    }

    /* Initialization of the bean. Add any parameter that is required within your scenario. */
    public interface ICloseListener {
        void reactOnClose();
    }

    public interface IListener {
    }
    /* Listener to the user of the page bean. */


    // ------------------------------------------------------------------------
    // private usage
    // ------------------------------------------------------------------------
}

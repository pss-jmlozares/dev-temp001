package managedbeans;

import java.io.Serializable;
import java.util.List;

import database.ConnectionPool;
import modelclasses.*;
import org.eclnt.editor.annotations.CCGenClass;
import org.eclnt.jsfserver.defaultscreens.Statusbar;
import org.eclnt.jsfserver.elements.events.BaseActionEventInvoke;
import org.eclnt.jsfserver.elements.events.BaseActionEventUpload;
import org.eclnt.jsfserver.pagebean.PageBean;
import org.eclnt.util.valuemgmt.ValueManager;
import org.javalite.activejdbc.Base;

import javax.faces.event.ActionEvent;

@CCGenClass (expressionBase="#{d.PhotoStorage}")

public class PhotoStorage
    extends PageBean 
    implements Serializable
{
    String m_dataForImage;
    public String getDataForImage() { return m_dataForImage; }
    public void setDataForImage(String value) { this.m_dataForImage = value; }


    String m_ShowPhoto;
    public String getShowPhoto() { return m_ShowPhoto; }
    public void setShowPhoto(String value) { this.m_ShowPhoto = value; }

    String m_fileName;
    public String getFileName() { return m_fileName; }
    public void setFileName(String value) { this.m_fileName = value; }

    public void onUploadFile(javax.faces.event.ActionEvent event) {
        if (event instanceof BaseActionEventUpload){
            try{
                ConnectionPool.getInstance();
                Base.open(ConnectionPool.dataSourcePooled);

                BaseActionEventUpload bae =(BaseActionEventUpload)event;
                setFileName(bae.getClientFileName());
                byte[] imageByte = bae.getHexBytes();
                setShowPhoto("/hex("+ValueManager.encodeHexString(imageByte)+")");

                photo_storage photo = new photo_storage();
                    photo.set("timestamp","ss");
                    photo.set("Primary_Key", "15");
                    photo.set("File_Name",getFileName());
                    photo.set("Image_Data",imageByte);

                photo.saveIt();
//                Statusbar.outputMessage("ID = "+photo.getString("Primary_Key")+"File " + bae.getClientFileName() + " (length="+m_ShowPhoto.length()/2+") was uploaded.");


            }catch(Throwable t){
                t.printStackTrace();
                Statusbar.outputAlert(t.toString());
            }finally {
                Base.close();
            }
        }
    }

    public void onShowImage(javax.faces.event.ActionEvent event) {
        try{
            ConnectionPool.getInstance();
            Base.open(ConnectionPool.dataSourcePooled);

            List<photo_storage> photo = photo_storage.where("Primary_Key = ?",87);
            for (photo_storage so:photo){
                setShowPhoto("/hex("+ValueManager.encodeHexString(so.getBytes("Image_Data"))+")");
                setFileName(so.getString("File_Name"));
                setDataForImage(so.getString("Image_Data"));

//                    System.out.println(so.getBytes("Image_Data").length/2);
            }
            Statusbar.outputMessage("Hey");
        }catch(Throwable t){
            t.printStackTrace();
            Statusbar.outputAlert(t.toString());
        }finally {
            Base.close();
        }
    }

    private void haveData() {
        try{
            ConnectionPool.getInstance();
            Base.open(ConnectionPool.dataSourcePooled);
            photo_storage pot = photo_storage.findFirst("Primary_Key = ?",10);
            setDataForImage(String.valueOf(pot.getString("Image_Data").length()/2));
        }catch(Throwable t){
            t.printStackTrace();
            Statusbar.outputAlert(t.toString());
        }finally {
            Base.close();
        }
    }
    // ------------------------------------------------------------------------
    // inner classes
    // ------------------------------------------------------------------------
    
    /* Listener to the user of the page bean. */
    public interface IListener
    {
        void close();
    }
    
    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------
    
    private IListener m_listener;
    
    // ------------------------------------------------------------------------
    // constructors & initialization
    // ------------------------------------------------------------------------

    public PhotoStorage() {
//        haveData();
    }



    public String getPageName() { return "/PhotoStorage.jsp"; }
    public String getRootExpressionUsedInPage() { return "#{d.PhotoStorage}"; }

    // ------------------------------------------------------------------------
    // public usage
    // ------------------------------------------------------------------------

    /* Initialization of the bean. Add any parameter that is required within your scenario. */
    public void prepare(IListener listener)
    {
        m_listener = listener;
    }

    // ------------------------------------------------------------------------
    // private usage
    // ------------------------------------------------------------------------
}

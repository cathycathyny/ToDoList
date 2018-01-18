package tdlm.data;

import java.util.ArrayList;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import tdlm.gui.Workspace;
import saf.components.AppDataComponent;
import saf.AppTemplate;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class DataManager implements AppDataComponent {
    // FIRST THE THINGS THAT HAVE TO BE SAVED TO FILES
    
    // NAME OF THE TODO LIST
    String name;
    
    // LIST OWNER
    String owner;
    
    // THESE ARE THE ITEMS IN THE TODO LIST
    ObservableList<ToDoItem> items = FXCollections.observableArrayList();
    
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
	// KEEP THE APP FOR LATER
	app = initApp;
    }
    
    public ObservableList<ToDoItem> getItems() {
	return items;
    }
    
    public String getName() {
        if(name==null){
            return "";
        }
        return name;
    }
    
    public String getOwner() {
        if(owner==null){
            return "";
        }
        return owner;
    }

    public void addItem(ToDoItem item) {
        items.add(item);
    }
    
    public void removeItem(ToDoItem item) {
        items.remove(item);
    }
    
    public void setName(String todoName){
        this.name=todoName;
    }
    
    public void setOwner(String todoOwner){
        this.owner=todoOwner;
    }
    
    //get the index of an item
    public int getIndex(ToDoItem itemPos) {
        return items.indexOf(itemPos);
    }
    
    public int getSize() {
        return items.size();
    }
    
    public static void swap( ObservableList list, int firstInd, int secondInd )
    {
        Object temp = list.set( firstInd, list.get( secondInd ) ) ;
        list.set( secondInd, temp ) ;
    }
    
    
    /**
     * 
     */
    @Override
    public void reset() {
	// NOW MAKE THE NODES
	Workspace workspace = (Workspace) app.getWorkspaceComponent();

	// reset name and owner
	name=null;
        owner=null;
        
        //clear all the element inside the list
        items.clear();
        workspace.reloadWorkspace();
    }
}

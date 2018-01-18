package tdlm.controller;

import java.time.LocalDate;
import java.util.Locale;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import tdlm.data.DataManager;
import tdlm.gui.Workspace;
import saf.AppTemplate;
import tdlm.PropertyType;
import tdlm.data.ToDoItem;

/**
 * This class responds to interactions with todo list editing controls.
 * 
 * @author McKillaGorilla
 * @author Siqing Lee
 * @version 1.0
 */
public class ToDoListController {
    AppTemplate app;
    
    static final String CLASS_BORDERED_PANE = "bordered_pane";
    // add items dialog box
    Stage dialogStage;
    GridPane gridpane;
    BorderPane root;
    Scene addScene;
    
    Label categoryLabel;
    TextField categoryTextField;
    
    Label descriptionLabel;
    TextField descriptionTextField;
    
    Label startDateLabel;
    DatePicker startDatePicker;
    
    Label endDateLabel;
    DatePicker endDatePicker;
    
    Label completedLabel;
    CheckBox completed;
    
    HBox buttonBox;
    Button okButton;
    Button cancelButton;
            
    
    public ToDoListController(AppTemplate initApp) {
	app = initApp;
    }
    
    public void processAddItem() {	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
        
        //create a dialog to ask user what to add
        createdialog();
        dialogHandlers();
	workspace.reloadWorkspace();
    }
    
    public void processRemoveItem(ToDoItem removeItem) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        DataManager dataManager = (DataManager)app.getDataComponent();
        dataManager.removeItem(removeItem);
        workspace.reloadWorkspace();
    }
    
    public void processMoveUpItem(ToDoItem moveUpItem) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        DataManager datalist = (DataManager)app.getDataComponent();
        int pos =datalist.getIndex(moveUpItem);
        DataManager.swap(datalist.getItems(), pos, pos-1);
        workspace.reloadWorkspace();
    }
    
    public void processMoveDownItem(ToDoItem moveDownItem) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        DataManager datalist = (DataManager)app.getDataComponent();
        int pos =datalist.getIndex(moveDownItem);
        DataManager.swap(datalist.getItems(), pos, pos+1);
        workspace.reloadWorkspace();
    }
    
    public void processEditItem(ToDoItem editItem) {
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        createdialog();
        categoryTextField.setText(editItem.getCategory());
        descriptionTextField.setText(editItem.getDescription());
        startDatePicker.setValue(editItem.getStartDate());
        endDatePicker.setValue(editItem.getEndDate());
        completed.setSelected(editItem.getCompleted());
        
        editHandlers(editItem);
        workspace.reloadWorkspace();
    }
    
    public void createdialog(){
        // Create the dialog Stage.
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        dialogStage = new Stage();
        dialogStage.setTitle(props.getProperty(PropertyType.ADD_HEADER));
        root = new BorderPane();
        addScene = new Scene(root);
        addScene.getStylesheets().add("tdlm/css/tdlm_style.css");
        
        gridpane = new GridPane();
        gridpane.getStyleClass().add("gridpane"); 
        ColumnConstraints column1 = new ColumnConstraints(100);
        ColumnConstraints column2 = new ColumnConstraints(50, 150, 300);
        column2.setHgrow(Priority.ALWAYS);
        gridpane.getColumnConstraints().addAll(column1, column2);

        categoryLabel = new Label(props.getProperty(PropertyType.ADD_CATEGORY));
        categoryTextField = new TextField();
        descriptionLabel = new Label(props.getProperty(PropertyType.ADD_DESCRIPTION));
        descriptionTextField = new TextField();
        startDateLabel = new Label(props.getProperty(PropertyType.ADD_START_DATE));
        startDatePicker = new DatePicker();
        endDateLabel = new Label(props.getProperty(PropertyType.ADD_END_DATE));
        endDatePicker = new DatePicker();
        completedLabel = new Label(props.getProperty(PropertyType.ADD_COMPLETED));
        completed = new CheckBox();
        
        Locale.setDefault(Locale.US);  
        startDatePicker.setValue(LocalDate.now());
        endDatePicker.setValue(startDatePicker.getValue().plusDays(1));
        buttonBox = new HBox();
        okButton = new Button(props.getProperty(PropertyType.OK_BUTTON));
        cancelButton = new Button(props.getProperty(PropertyType.CANCEL_BUTTON));
        buttonBox.getChildren().addAll(okButton, cancelButton);
        
        // Category
        GridPane.setHalignment(categoryLabel, HPos.LEFT);
        gridpane.add(categoryLabel, 0, 0);
        GridPane.setHalignment(categoryTextField, HPos.LEFT);
        gridpane.add(categoryTextField, 1, 0);
        
        // Description
        GridPane.setHalignment(descriptionLabel, HPos.LEFT);
        gridpane.add(descriptionLabel, 0, 1);
        GridPane.setHalignment(descriptionTextField, HPos.LEFT);
        gridpane.add(descriptionTextField, 1, 1);
        
        // Start Date
        GridPane.setHalignment(startDateLabel, HPos.LEFT);
        gridpane.add(startDateLabel, 0, 2);
        GridPane.setHalignment(startDatePicker, HPos.LEFT);
        gridpane.add(startDatePicker, 1, 2);
        
        // End Date
        GridPane.setHalignment(endDateLabel, HPos.LEFT);
        gridpane.add(endDateLabel, 0, 3);
        GridPane.setHalignment(endDatePicker, HPos.LEFT);
        gridpane.add(endDatePicker, 1, 3);
        
        //completed check box
        GridPane.setHalignment(completedLabel, HPos.LEFT);
        gridpane.add(completedLabel, 0, 4);
        GridPane.setHalignment(completed, HPos.LEFT);
        gridpane.add(completed, 1, 4);
        
        // ok and cancel button
        GridPane.setHalignment(buttonBox, HPos.LEFT);
        gridpane.add(buttonBox, 1, 5);
        
        root.setCenter(gridpane);
        
        // Show the dialog and wait until the user closes it
        dialogStage.setScene(addScene);
        dialogStage.show();
    }
    
    private void dialogHandlers(){
        DataManager dataManager = (DataManager)app.getDataComponent();
        
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                ToDoItem todoitem = new ToDoItem();
                todoitem.setCategory(categoryTextField.getText());
                todoitem.setDescription(descriptionTextField.getText());
                todoitem.setStartDate((LocalDate) startDatePicker.getValue());
                todoitem.setEndDate((LocalDate) endDatePicker.getValue());
                todoitem.setCompleted(completed.isSelected());
                
                dataManager.addItem(todoitem);
                dialogStage.close();
            }
        });
                       
        //close the dialog
        cancelButton.setOnAction(e->{
            dialogStage.close();
        });
        
    };
    
    private void editHandlers(ToDoItem editItem){
        DataManager dataManager = (DataManager)app.getDataComponent();
        
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                editItem.setCategory(categoryTextField.getText());
                editItem.setDescription(descriptionTextField.getText());
                editItem.setStartDate((LocalDate) startDatePicker.getValue());
                editItem.setEndDate((LocalDate) endDatePicker.getValue());
                editItem.setCompleted(completed.isSelected());
                dialogStage.close();
            }
        });
                       
        //close the dialog
        cancelButton.setOnAction(e->{
            dialogStage.close();
        });
        
    };
}

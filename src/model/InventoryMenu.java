package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

import static sample.FXMLUtils.CreateButton;
import static sample.FXMLUtils.CreateLabel;

public class InventoryMenu extends PauseMenu {

    //For showing the inventory itself
    private TableView<InventoryMenuItem> inventoryView;

    private VBox childVBox;
    private HBox hbox;
    private Button closeButton;
    private Label title;
    private Label equippedLabel;
    private ImageView equippedItemIcon;

    public InventoryMenu(String ID, int width, int height, int xPos, int yPos) {
        super(ID, width, height, xPos, yPos);
        CreateLayout(width,height);
    }

    public void setEquippedItemIcon(Image itemIcon){
        equippedItemIcon.setImage(itemIcon);
    }

    //Create the layout and elements in the menu
    //Starts at top-level VBox and moves downwards
    private void CreateLayout(int width, int height){

        //Title and exit button
        title = CreateLabel("INVENTORY","inventoryMenuTitle",width,150, TextAlignment.CENTER,false);
        closeButton = CreateButton("Close","inventoryCloseButton",125,100,event->this.hide());

        //HBox within the VBox
        hbox = new HBox(150);
        hbox.setAlignment(Pos.CENTER);
        hbox.setPrefWidth(width);

        //Table to hold and display all the inventory items
        inventoryView = CreateTable();

        //VBox adjacent to table
        childVBox = new VBox(10);
        childVBox.setPrefWidth(hbox.getWidth()/2);
        childVBox.setAlignment(Pos.CENTER_LEFT);

        equippedLabel = CreateLabel("Equipped","itemEquippedLabel",200,200,TextAlignment.LEFT,true);
        equippedItemIcon = new ImageView(new Image(this.getClass().getResourceAsStream("/sprites/healthKit.png")));
        equippedItemIcon.setFitWidth(childVBox.getWidth());

        childVBox.getChildren().addAll(equippedLabel,equippedItemIcon);
        hbox.getChildren().addAll(childVBox,inventoryView);
        this.vbox.getChildren().addAll(title,hbox,closeButton);

    }

    public TableView CreateTable(){

        inventoryView = new TableView<InventoryMenuItem>();
        inventoryView.setEditable(false);
        inventoryView.setId("inventory");
        inventoryView.setPrefWidth(250);

        //Create table column
        TableColumn<InventoryMenuItem, Image> itemList = new TableColumn<InventoryMenuItem, Image>("Items");
        itemList.setCellValueFactory(new PropertyValueFactory("icon"));
        itemList.setPrefWidth(inventoryView.getPrefWidth()/2);

        TableColumn<InventoryMenuItem, String> nameList = new TableColumn<InventoryMenuItem, String>("Names");
        nameList.setCellValueFactory(new PropertyValueFactory("name"));
        nameList.setPrefWidth(inventoryView.getPrefWidth()/2);

        itemList.setCellFactory(new Callback<TableColumn<InventoryMenuItem, Image>, TableCell<InventoryMenuItem, Image>>() {
            @Override
            public TableCell<InventoryMenuItem, Image> call(TableColumn<InventoryMenuItem, Image> inventoryMenuItemImageTableColumn) {
                TableCell<InventoryMenuItem,Image> cell = new TableCell<InventoryMenuItem,Image>(){
                    @Override
                    public void updateItem(Image icon, boolean empty){
                        if (icon!=null){
                            ImageView imageView = new ImageView();
                            imageView.setFitHeight(50);
                            imageView.setFitWidth(50);
                            imageView.setImage(icon);

                            setGraphic(imageView);
                        }
                    }
                };
                System.out.println("=========================");
                System.out.println(cell.getIndex());
                System.out.println("=========================");
                return cell;
            }
        });

        //Remove some columns?
        //inventoryView.getColumns().remove(0);
        //inventoryView.getColumns().remove(0);

        //Add columns to table view - need to find a way that makes the table only have 1 column
        inventoryView.getColumns().addAll(itemList,nameList);

        //Add rows to table view - should be taken from player inventory
        ObservableList<InventoryMenuItem> items = FXCollections.observableArrayList();
        for (int i=0;i<3;i++){
            InventoryMenuItem testItem = new InventoryMenuItem(new SimpleStringProperty("test"),
                    new SimpleStringProperty("ipsum dolor sit amet"));
            items.add(testItem);
        }

        inventoryView.setItems(items);

        return inventoryView;

    }
}

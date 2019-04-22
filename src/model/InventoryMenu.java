package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import javafx.scene.Node;


public class InventoryMenu extends PauseMenu {

    //For showing the inventory itself
    private TableView<InventoryMenuItem> inventoryView;

    private VBox innerVBox;
    private HBox hbox;
    private ImageView equippedItemIcon;

    public InventoryMenu(String ID, int width, int height, int xPos, int yPos) {
        super(ID, width, height, xPos, yPos);
        innerVBox = new VBox(10);
        hbox = new HBox(width/5);
        innerVBox.setPrefWidth(hbox.getWidth()/2);
    }

    public void setEquippedItemIcon(ImageView equippedItemIcon) {
        this.equippedItemIcon = equippedItemIcon;
    }

    public void setEquippedItemIconImage(Image itemIcon){
        equippedItemIcon.setImage(itemIcon);
    }

    //Should refactor if I have getters as well
    public void AddNodeToInnerVBox(int pos, Node node){
        this.innerVBox.getChildren().add(pos,node);
    }

    public void AddNodeToHBox(int pos, Node node){
        this.hbox.getChildren().add(pos,node);
    }

    public HBox getHbox() {
        return hbox;
    }

    public VBox getInnerVBox() {
        return innerVBox;
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

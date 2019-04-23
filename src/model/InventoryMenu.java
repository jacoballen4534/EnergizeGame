package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
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
    private TableView<Item> inventoryView;

    private VBox innerVBox;
    private HBox hbox;
    private ImageView equippedItemIcon;

    public InventoryMenu(String ID, int width, int height, int xPos, int yPos) {
        super(ID, width, height, xPos, yPos);
        innerVBox = new VBox(10);
        hbox = new HBox(width/5);
        innerVBox.setPrefWidth(hbox.getWidth()/2);
        innerVBox.setAlignment(Pos.CENTER);
    }

    public void setEquippedItemIcon(ImageView equippedItemIcon) {
        this.equippedItemIcon = equippedItemIcon;
    }

    public void setEquippedItemIconImage(Image itemIcon){
        equippedItemIcon.setImage(itemIcon);
    }

    //Should refactor these out since I have getters as well
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

        inventoryView = new TableView<Item>();
        inventoryView.setEditable(false);
        inventoryView.setId("inventory");
        inventoryView.setPrefWidth(250);

        //Create table column
        TableColumn<Item, Image> itemList = new TableColumn<Item, Image>("Items");
        itemList.setCellValueFactory(new PropertyValueFactory("icon"));
        itemList.setPrefWidth(inventoryView.getPrefWidth()/2);

        TableColumn<Item, String> nameList = new TableColumn<Item, String>("Names");
        nameList.setCellValueFactory(new PropertyValueFactory("name"));
        nameList.setPrefWidth(inventoryView.getPrefWidth()/2);

        itemList.setCellFactory(new Callback<TableColumn<Item, Image>, TableCell<Item, Image>>() {
            @Override
            public TableCell<Item, Image> call(TableColumn<Item, Image> itemImageTableColumn) {
                TableCell<Item,Image> cell = new TableCell<Item,Image>(){
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

        //Add columns to table view - need to find a way that makes the table only have 1 column
        inventoryView.getColumns().addAll(itemList,nameList);

        //Add rows to table view - should be taken from player inventory
        ObservableList<Item> items = FXCollections.observableArrayList();
        for (int i=0;i<3;i++){
            Item testItem = new Pickup(0,0,PreLoadedImages.healthPickupSprite,32,32);
            testItem.setName("Health Kit");
            items.add(testItem);
        }

        inventoryView.setItems(items);

        return inventoryView;

    }
}

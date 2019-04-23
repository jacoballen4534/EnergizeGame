package model;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


public class InventoryMenu extends PauseMenu {

    //For showing the inventory itself
    private TableView inventoryView;

    private VBox innerVBox;
    private HBox hbox;
    private ImageView equippedItemIcon;
    private Inventory inventory;

    public InventoryMenu(String ID, Inventory inventory, int width, int height, int xPos, int yPos) {
        super(ID, width, height, xPos, yPos);
        this.inventory = inventory;
        innerVBox = new VBox(0);
        hbox = new HBox(120);
        innerVBox.setPrefWidth(100);
        innerVBox.setAlignment(Pos.CENTER);
    }

    public void setEquippedItemIcon(ImageView equippedItemIcon) {
        this.equippedItemIcon = equippedItemIcon;
    }

    public void setEquippedItemIconImage(Image itemIcon){
        equippedItemIcon.setImage(itemIcon);
    }

    public HBox getHbox() {
        return hbox;
    }

    public VBox getInnerVBox() {
        return innerVBox;
    }

    public TableView<Item> CreateTable(){

        inventoryView = new TableView();
        inventoryView.setEditable(false);
        inventoryView.setId("inventory");
        inventoryView.setPrefWidth(250);

        //Create table column
        TableColumn iconColumn = new TableColumn("Icon");
        iconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));
        iconColumn.setPrefWidth(inventoryView.getPrefWidth()/2);

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(inventoryView.getPrefWidth()/2);

//        itemList.setCellFactory(itemImageTableColumn -> {
//            TableCell<Item,Image> cell = new TableCell<Item,Image>(){
//                @Override
//                public void updateItem(Image icon, boolean empty){
//                    if (icon!=null){
//                        ImageView imageView = new ImageView();
//                        imageView.setFitHeight(50);
//                        imageView.setFitWidth(50);
//                        imageView.setImage(icon);
//
//                        setGraphic(imageView);
//                    }
//                }
//            };
//        System.out.println("=========================");
//        System.out.println(cell.getIndex());
//        System.out.println("=========================");
//        return cell;
//        });

        //Add columns to table view
        inventoryView.getColumns().addAll(iconColumn,nameColumn);

        //Add rows to table view - should be taken from player inventory
        /*ObservableList<Item> items = FXCollections.observableArrayList();
        for (int i=0;i<3;i++){
            Item testItem = new Pickup(0,0,PreLoadedImages.healthPickupSprite,32,32);
            testItem.setName("Health Kit");
            items.add(testItem);
        }

        inventoryView.setItems(items);*/

        return inventoryView;

    }

    public void UpdateTable(){

        Image equippedItemIcon;
        if (inventory.getEquippedItem() != null) {
            equippedItemIcon = inventory.getEquippedItem().getJFXImage();
        } else {
            equippedItemIcon = SwingFXUtils.toFXImage(PreLoadedImages.emptyItemSlot,null);
        }

        this.setEquippedItemIconImage(equippedItemIcon);

        if (!inventoryView.getItems().isEmpty()) inventoryView.getItems().clear();

        if (inventory.getItemCount() == 0){
            return;
        }


        ArrayList<Item> items = inventory.getItemList();
        items.forEach(item -> {
            System.out.println(item.getName());
            inventoryView.getItems().add(item);
            ObservableList<Item> check = inventoryView.getItems();
        });
    }
}

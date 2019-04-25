package model;

import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;


public class InventoryMenu extends PauseMenu {

    //Macros
    private final int INVENTORY_TABLE_WIDTH = 300;

    //For showing the inventory itself
    private TableView inventoryView;

    private Inventory inventory;

    //Nodes within the menu
    private VBox innerVBox;
    private HBox hbox;
    private ImageView equippedItemIcon;

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

    public TableView<Item> CreateTable(EventHandler onItemSelect){

        inventoryView = new TableView();
        inventoryView.setEditable(false);
        inventoryView.setId("inventory");
        inventoryView.setPrefWidth(INVENTORY_TABLE_WIDTH);

        //Create table column
        TableColumn iconColumn = new TableColumn("Icon");
        iconColumn.setCellValueFactory(new PropertyValueFactory<>("icon"));
        iconColumn.setPrefWidth(inventoryView.getPrefWidth()/3);

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setPrefWidth(inventoryView.getPrefWidth()/3);

        TableColumn quantityColumn = new TableColumn("Quantity");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        nameColumn.setPrefWidth(inventoryView.getPrefWidth()/3);

        //Add columns to table view
        inventoryView.getColumns().addAll(iconColumn,nameColumn,quantityColumn);

        //Set an on-click event for each row
        inventoryView.setRowFactory(tableView -> {
            TableRow<Item> row = new TableRow<>();
            row.setOnMouseClicked(event->{
                Item rowData = row.getItem();
                inventory.changeEquippedItem(rowData);
                System.out.println("User has clicked: " + rowData.getName());
            });
            //row.setOnMouseClicked(onItemSelect);
            return row;
        });

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
            //ObservableList<Item> check = inventoryView.getItems();
        });
    }
}

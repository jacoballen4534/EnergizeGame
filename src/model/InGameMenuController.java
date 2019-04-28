package model;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import sample.Game;

import static sample.FXMLUtils.*;
import static sample.Game.SCREEN_HEIGHT;
import static sample.Game.SCREEN_WIDTH;

public class InGameMenuController {

    //==Sizing macros==//
    //--Pause Menu--//
    private static final int PAUSE_MENU_WIDTH = 300;
    private static final int PAUSE_MENU_HEIGHT = 500;
    private static final int PAUSE_MENU_BTN_WIDTH = 250;
    private static final int PAUSE_MENU_BTN_HEIGHT = 50;
    private static final int PAUSE_MENU_TITLE_WIDTH = 250;
    private static final int PAUSE_MENU_TITLE_HEIGHT = 130;

    //--Inventory Menu--//
    private static final int INVENTORY_MENU_WIDTH = 500;
    private static final int INVENTORY_MENU_HEIGHT = 500;
    private static final int INVENTORY_TITLE_WIDTH = 300;
    private static final int INVENTORY_TITLE_HEIGHT = 100;
    private static final int CLOSE_MENU_BTN_WIDTH = 125;
    private static final int CLOSE_MENU_BTN_HEIGHT = 100;

    //--Save Menu--//
    private static final int SAVE_MENU_WIDTH = 300;
    private static final int SAVE_MENU_HEIGHT = 150;
    private static final int SAVE_TITLE_WIDTH = 200;
    private static final int SAVE_TITLE_HEIGHT = 50;
    private static final int SAVE_BUTTON_WIDTH = 125;
    private static final int SAVE_BUTTON_HEIGHT = 30;
    private static final int SAVE_NAME_INPUT_WIDTH = 250;
    private static final int SAVE_NAME_INPUT_HEIGHT = 50;

    //--Options Menu--//
    private static final int OPTIONS_MENU_WIDTH = 400;
    private static final int OPTIONS_MENU_HEIGHT = 400;

    //--Confirmation Menu--//
    private static final int CONFIRMATION_MENU_WIDTH = 300;
    private static final int CONFIRMATION_MENU_HEIGHT = 100;
    private static final int CONFIRMATION_TITLE_WIDTH = 250;
    private static final int CONFIRMATION_TITLE_HEIGHT = 100;
    private static final int CONFIRMATION_BUTTON_WIDTH = 100;
    private static final int CONFIRMATION_BUTTON_HEIGHT = 40;

    //==Event macros==//
    //--Pause Menu--//
    private EventHandler resumeEvent;
    private Runnable unpause;
    private EventHandler inventoryMenuEvent = mouseEvent ->{
        this.pauseMenu.hide();
        this.inventoryMenu.show();
        this.inventoryMenu.UpdateTable();
    };
    private EventHandler saveMenuEvent = mouseEvent->{
        System.out.println("Shows save game menu");
        this.pauseMenu.hide();
        this.saveGameMenu.show();
    };
    private EventHandler optionsMenuEvent = mouseEvent->{
        System.out.println("Shows options menu");
        this.pauseMenu.hide();
        this.optionsMenu.show();
    };
    private EventHandler quitToTitleEvent = mouseEvent-> {
        this.pauseMenu.hide();
        this.exitToTitleConfirmation.show();
    };

    private EventHandler quitToDesktopEvent = mouseEvent-> {
        this.pauseMenu.hide();
        this.exitToDesktopConfirmation.show();
    };
    //--Inventory Menu--//
    private EventHandler closeInventoryMenuEvent = mouseEvent->{
        this.inventoryMenu.hide();
//        this.pauseMenu.show(); //Goes back to pause menu. Instead go back to game
        this.unpause.run();
    };
    //--Save Menu--//
    private EventHandler saveGameEvent = mouseEvent -> {

        Utilities.saveNewMapSeed(this.saveGameMenu.getTextInput(),Game.getRandomSeed());
        System.out.println("The current map has been saved");

        this.saveGameMenu.hide();
        this.pauseMenu.show();
    };

    private EventHandler closeSaveMenuEvent = mouseEvent->{
        this.saveGameMenu.hide();
        this.pauseMenu.show();
    };
    //--Options Menu--//
    private EventHandler closeOptionsMenuEvent = mouseEvent->{
        this.optionsMenu.hide();
        this.pauseMenu.show();
    };
    private EventHandler changeVolumeEvent = event -> System.out.println("Change volume");
    private EventHandler changeKeybindingsEvent = event -> System.out.println("Change keybindings");
    //--Confirmation Menu(s)--//
    private EventHandler returnToTitleEvent;
    private EventHandler returnToDesktopEvent = event -> {
        Handler.disconnectFromServer();
        System.exit(0);
    };

    //Menus
    private PauseMenu pauseMenu;
    private InventoryMenu inventoryMenu;
    private ConfirmationMenu exitToTitleConfirmation;
    private ConfirmationMenu exitToDesktopConfirmation;
    private SaveGameMenu saveGameMenu;
    private OptionsMenu optionsMenu;

    public InGameMenuController(Inventory inventory, Runnable unpauseGame, EventHandler returnToTitleScreen) {

        this.resumeEvent = event -> {this.pauseMenu.hide();unpauseGame.run();};
        this.unpause = unpauseGame;
        this.returnToTitleEvent = returnToTitleScreen;

        //Generate menu layouts
        this.pauseMenu = CreatePauseMenu(pauseMenu);
        this.inventoryMenu = CreateInventoryMenu(inventory);
        this.saveGameMenu = CreateSaveGameMenu(saveGameMenu);
        this.optionsMenu = CreateOptionsMenu(optionsMenu);

        this.exitToTitleConfirmation = CreateConfirmationMenu("Are you sure?", "confirmationMenu",
                returnToTitleEvent);
        this.exitToDesktopConfirmation = CreateConfirmationMenu("Are you sure?",
                "confirmationMenu", returnToDesktopEvent);

        //Hide all menus
        this.pauseMenu.hide();
        this.exitToTitleConfirmation.hide();
        this.exitToDesktopConfirmation.hide();
    }

    public void showPauseMenu(){
        pauseMenu.show();
    }

    public void hidePauseMenu(){
        pauseMenu.hide();
        exitToTitleConfirmation.hide();
    }

    public void showInventoryMenu(){
        pauseMenu.hide();
        inventoryMenu.UpdateTable();
        inventoryMenu.show();
    }

    public void AddMenusToRoot(AnchorPane root){
        //root.getChildren().add(HUD); //Include HUD as part of menu controller
        root.getChildren().add(pauseMenu);
        root.getChildren().add(inventoryMenu);
        root.getChildren().add(saveGameMenu);
        root.getChildren().add(optionsMenu);
        root.getChildren().add(exitToTitleConfirmation);
        root.getChildren().add(exitToDesktopConfirmation);
    }

    private PauseMenu CreatePauseMenu(PauseMenu pauseMenu){

        int nodePos = 1;

        pauseMenu = new PauseMenu("pauseMenu",PAUSE_MENU_WIDTH,PAUSE_MENU_HEIGHT,
                SCREEN_WIDTH/2,SCREEN_HEIGHT/2);

        Label titleLabel = CreateLabel("Pause menu","pauseMenuTitle",PAUSE_MENU_TITLE_WIDTH,PAUSE_MENU_TITLE_HEIGHT,
                TextAlignment.CENTER,true);
        pauseMenu.SetLabelAsTitle(titleLabel);

        Button resumeButton = CreateButton("Resume","resumeButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT,resumeEvent);
        Button inventoryButton = CreateButton("Inventory","inventoryButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT, inventoryMenuEvent);
        Button saveButton = CreateButton("Save Map","saveButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT, saveMenuEvent);
        Button optionsButton = CreateButton("Options","optionsButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT, optionsMenuEvent);
        Button exitToTitleButton = CreateButton("Quit to Main Menu","quitToTitleButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT,quitToTitleEvent);
        Button exitToDesktopButton = CreateButton("Quit to Desktop","quitToDesktopButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT,quitToDesktopEvent);

        pauseMenu.AddNodeToVBox(nodePos++,resumeButton);
        pauseMenu.AddNodeToVBox(nodePos++,inventoryButton);
        pauseMenu.AddNodeToVBox(nodePos++,saveButton);
        pauseMenu.AddNodeToVBox(nodePos++,optionsButton);
        pauseMenu.AddNodeToVBox(nodePos++,exitToTitleButton);
        pauseMenu.AddNodeToVBox(nodePos++,exitToDesktopButton);

        return pauseMenu;
    }

    private ConfirmationMenu CreateConfirmationMenu(String title, String ID,
                                                    EventHandler confirmationAction){
        ConfirmationMenu confirmationMenu = new ConfirmationMenu(ID,
                CONFIRMATION_MENU_WIDTH,CONFIRMATION_MENU_HEIGHT,SCREEN_WIDTH/2,SCREEN_HEIGHT/2);

        Label titleLabel = CreateLabel(title,"confirmationMenuTitle",
                CONFIRMATION_TITLE_WIDTH,CONFIRMATION_TITLE_HEIGHT,TextAlignment.CENTER,false);
        confirmationMenu.SetLabelAsTitle(titleLabel);

        Button confirm = CreateButton("Quit","confirmationMenuButton",
                CONFIRMATION_BUTTON_WIDTH,CONFIRMATION_BUTTON_HEIGHT,confirmationAction);
        Button cancel = CreateButton("Cancel","confirmationMenuButton",
                CONFIRMATION_BUTTON_WIDTH,CONFIRMATION_BUTTON_HEIGHT,event -> {
                    confirmationMenu.hide();
                    pauseMenu.show();
                });

        HBox hbox = confirmationMenu.getHbox();

        hbox.getChildren().addAll(confirm,cancel);

        confirmationMenu.AddNodeToVBox(1,hbox);

        return confirmationMenu;
    }

    private SaveGameMenu CreateSaveGameMenu(SaveGameMenu saveGameMenu){

        int vboxNodePos = 1;
        int hboxNodePos = 0;

        saveGameMenu = new SaveGameMenu("saveMenu",SAVE_MENU_WIDTH,SAVE_MENU_HEIGHT,SCREEN_WIDTH/2,SCREEN_HEIGHT/2);

        Label saveTitle = CreateLabel("Save Map","saveMenuTitle",
                SAVE_TITLE_WIDTH,SAVE_TITLE_HEIGHT,TextAlignment.CENTER,false);
        saveGameMenu.SetLabelAsTitle(saveTitle);

        Button saveButton = CreateButton("SAVE","saveMenuButton",
                SAVE_BUTTON_WIDTH,SAVE_BUTTON_HEIGHT, saveGameEvent);
        Button cancelButton = CreateButton("CANCEL","saveMenuButton",
                SAVE_BUTTON_WIDTH,SAVE_BUTTON_HEIGHT,closeSaveMenuEvent);

        saveGameMenu.AddNodeToHBox(hboxNodePos++,saveButton);
        saveGameMenu.AddNodeToHBox(hboxNodePos++,cancelButton);

        TextField saveNameInput = CreateTextField("Enter Map Name","saveNameTextField",
                SAVE_NAME_INPUT_WIDTH,SAVE_NAME_INPUT_HEIGHT);

        saveGameMenu.setTextField(saveNameInput);

        saveGameMenu.AddNodeToVBox(vboxNodePos++,saveNameInput);
        saveGameMenu.AddNodeToVBox(vboxNodePos++,saveGameMenu.getHBox());

        return saveGameMenu;
    }

    private InventoryMenu CreateInventoryMenu(Inventory inventory){

        //To keep track of node positions within the menu
        int outerVBoxNodePos = 1;

        //Create the menu
        inventoryMenu = new InventoryMenu("inventoryMenu", inventory,
                INVENTORY_MENU_WIDTH,INVENTORY_MENU_HEIGHT,SCREEN_WIDTH/2,SCREEN_HEIGHT/2);

        VBox innerVbox = inventoryMenu.getInnerVBox();
        HBox hbox = inventoryMenu.getHbox();

        //Create and set the title of the menu
        Label inventoryTitle = CreateLabel("Inventory","inventoryMenuTitle",
                INVENTORY_TITLE_WIDTH,INVENTORY_TITLE_HEIGHT,TextAlignment.CENTER,false);
        inventoryMenu.SetLabelAsTitle(inventoryTitle);

        //Create the table of items
        TableView table = inventoryMenu.CreateTable();

        //Label for equipped item icon
        Label equippedLabel = CreateLabel("Equipped Item","itemEquippedLabel",150,100,TextAlignment.LEFT,true);

        //Create the imageview for the equipped item
        ImageView equippedItemIcon = CreateImageView(SwingFXUtils.toFXImage(PreLoadedImages.emptyItemSlot,null), 50,50);

        inventoryMenu.setEquippedItemIcon(equippedItemIcon);

        //Button to close the inventory menu and return to pause screen
        Button closeMenu = CreateButton("Close","closeInventoryMenu",
                CLOSE_MENU_BTN_WIDTH,CLOSE_MENU_BTN_HEIGHT,closeInventoryMenuEvent);

        innerVbox.getChildren().addAll(equippedLabel,equippedItemIcon);

        hbox.getChildren().addAll(innerVbox,table);

        inventoryMenu.AddNodeToVBox(outerVBoxNodePos++,inventoryMenu.getHbox());
        inventoryMenu.AddNodeToVBox(outerVBoxNodePos++,closeMenu);

        return inventoryMenu;
    }

    private OptionsMenu CreateOptionsMenu(OptionsMenu optionsMenu){

        int nodePos = 1;

        optionsMenu = new OptionsMenu("optionsMenu",OPTIONS_MENU_WIDTH,OPTIONS_MENU_HEIGHT,
                SCREEN_WIDTH/2,SCREEN_HEIGHT/2);

        Label title = CreateLabel("OPTIONS","optionsMenuTitle",
                OPTIONS_MENU_WIDTH,OPTIONS_MENU_HEIGHT/3,TextAlignment.CENTER,false);
        optionsMenu.SetLabelAsTitle(title);

        Button changeVolume = CreateButton("Change volume","changeVolumeButton",
                OPTIONS_MENU_WIDTH/2,OPTIONS_MENU_HEIGHT/6,changeVolumeEvent);
        Button changeKeybindings = CreateButton("Change keybindings","changeKeybindingsButton",
                OPTIONS_MENU_WIDTH/2,OPTIONS_MENU_HEIGHT/6,changeKeybindingsEvent);
        Button closeOptionsMenu = CreateButton("Close","closeOptionsMenu",
                OPTIONS_MENU_WIDTH/2,OPTIONS_MENU_HEIGHT/6,closeOptionsMenuEvent);

        optionsMenu.AddNodeToVBox(nodePos++,changeVolume);
        optionsMenu.AddNodeToVBox(nodePos++,changeKeybindings);
        optionsMenu.AddNodeToVBox(nodePos++,closeOptionsMenu);

        return optionsMenu;
    }
}

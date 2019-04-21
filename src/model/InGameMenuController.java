package model;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.TextAlignment;

import static sample.FXMLUtils.CreateButton;
import static sample.FXMLUtils.CreateLabel;
import static sample.Game.SCREEN_HEIGHT;
import static sample.Game.SCREEN_WIDTH;

public class InGameMenuController {

    //Sizing macros
    private static final int PAUSE_MENU_WIDTH = 300;
    private static final int PAUSE_MENU_HEIGHT = 500;
    private static final int PAUSE_MENU_BTN_WIDTH = 250;
    private static final int PAUSE_MENU_BTN_HEIGHT = 50;

    private static final int INVENTORY_MENU_WIDTH = 500;
    private static final int INVENTORY_MENU_HEIGHT = 500;

    private static final int SAVE_MENU_WIDTH = 500;
    private static final int SAVE_MENU_HEIGHT = 500;

    private static final int OPTIONS_MENU_WIDTH = 300;
    private static final int OPTIONS_MENU_HEIGHT = 300;

    private static final int CONFIRMATION_MENU_WIDTH = 250;
    private static final int CONFIRMATION_MENU_HEIGHT = 150;

    //Event macros
    //--Pause Menu
    private EventHandler resumeEvent;// = mouseEvent->this.pauseMenu.hide();
    private EventHandler inventoryEvent = mouseEvent ->{
        //this.pauseMenu.hide();
        this.inventoryMenu.show();
    };
    private EventHandler saveEvent = mouseEvent->System.out.println("Shows save game menu");
    private EventHandler optionsEvent = mouseEvent->System.out.println("Shows options menu");
    private EventHandler quitToTitleEvent = mouseEvent->this.exitToTitleConfirmation.show();
    private EventHandler quitToDesktopEvent = mouseEvent->this.exitToDesktopConfirmation.show();

    //Menus
    private PauseMenu pauseMenu;
    private InventoryMenu inventoryMenu;
    private ConfirmationMenu exitToTitleConfirmation;
    private ConfirmationMenu exitToDesktopConfirmation;
    private SaveGameMenu saveGameMenu;
    private OptionsMenu optionsMenu;

    public InGameMenuController(Runnable unpauseGame, EventHandler returnToTitleScreen) {

        this.inventoryMenu = new InventoryMenu("inventoryMenu",INVENTORY_MENU_WIDTH,INVENTORY_MENU_HEIGHT,
                SCREEN_WIDTH/2,SCREEN_HEIGHT/2);

        this.resumeEvent = event -> {this.pauseMenu.hide();unpauseGame.run();};
        this.pauseMenu = CreatePauseMenu(pauseMenu);
        this.exitToTitleConfirmation = CreateConfirmationMenu("Are you sure?", "confirmationMenu",
                returnToTitleScreen);
        this.exitToDesktopConfirmation = CreateConfirmationMenu("Are you sure about that?",
                "confirmationMenu", event->System.exit(0));

        //Hide all menus
        this.pauseMenu.hide();
        this.exitToTitleConfirmation.hide();
        this.exitToDesktopConfirmation.hide();
    }

    private void ShowMenu(String menuID){
        //Implement and make public
    }

    private void HideMenu(String menuID){
        //Implement and make public
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
        inventoryMenu.show();
    }

    public void AddMenusToRoot(Group root){
        root.getChildren().add(pauseMenu);
        root.getChildren().add(inventoryMenu);
        //root.getChildren().add(saveGameMenu);
        //root.getChildren().add(optionsMenu);
        root.getChildren().add(exitToTitleConfirmation);
        root.getChildren().add(exitToDesktopConfirmation);
    }

    private ConfirmationMenu CreateConfirmationMenu(String title, String ID,
                                                    EventHandler confirmationAction){
        ConfirmationMenu confirmationMenu = new ConfirmationMenu(title,ID,
                CONFIRMATION_MENU_WIDTH,CONFIRMATION_MENU_HEIGHT,SCREEN_WIDTH/2,SCREEN_HEIGHT/2,confirmationAction);
        return confirmationMenu;
    }

    private PauseMenu CreatePauseMenu(PauseMenu pauseMenu){
        int nodePos = 1;
        pauseMenu = new PauseMenu("pauseMenu",PAUSE_MENU_WIDTH,PAUSE_MENU_HEIGHT,
                SCREEN_WIDTH/2,SCREEN_HEIGHT/2);

        Label titleLabel = CreateLabel("Pause menu","pauseMenuTitle",PAUSE_MENU_WIDTH,100,
                TextAlignment.CENTER,true);
        pauseMenu.SetLabelAsTitle(titleLabel);

        Button resumeButton = CreateButton("Resume","resumeButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT,resumeEvent);
        Button inventoryButton = CreateButton("Inventory","inventoryButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT,inventoryEvent);
        Button saveButton = CreateButton("Save Game","saveButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT,saveEvent);
        Button optionsButton = CreateButton("Options","optionsButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT,optionsEvent);
        Button exitToTitleButton = CreateButton("Quit to Main Menu","quitToTitleButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT,quitToTitleEvent);
        Button exitToDesktopButton = CreateButton("Quit to Desktop","quitToDesktopButton",
                PAUSE_MENU_BTN_WIDTH,PAUSE_MENU_BTN_HEIGHT,quitToDesktopEvent);

        pauseMenu.AddButtonToVBox(nodePos++,resumeButton);
        pauseMenu.AddButtonToVBox(nodePos++,inventoryButton);
        pauseMenu.AddButtonToVBox(nodePos++,saveButton);
        pauseMenu.AddButtonToVBox(nodePos++,optionsButton);
        pauseMenu.AddButtonToVBox(nodePos++,exitToTitleButton);
        pauseMenu.AddButtonToVBox(nodePos++,exitToDesktopButton);

        return pauseMenu;
    }
}

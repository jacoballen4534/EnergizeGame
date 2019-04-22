package sample;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.Iterator;

public class FXMLUtils {

    public static Button CreateButton(String text, String id, int width, int height, EventHandler onClick){
        Button button = new Button(text);
        button.setId(id);
        button.setPrefSize(width,height);
        button.setOnMouseClicked(onClick);
        return button;
    }

    public static Label CreateLabel(String text, String id, int prefWidth, int prefHeight,
                                    TextAlignment alignment, boolean doWrapText){
        Label label = new Label(text);
        label.setPrefSize(prefWidth,prefHeight);
        label.setTextAlignment(alignment);
        label.setWrapText(doWrapText);
        label.setId(id);
        return label;
    }

    public static TextField CreateTextField(String text, String id, int prefWidth, int prefHeight){
        TextField textField = new TextField(text);
        textField.setId(id);
        textField.setPrefSize(prefWidth,prefHeight);
        return textField;
    }

    public static ImageView CreateImageView(Image img, int fitWidth, int fitHeight){
        ImageView imageView = new ImageView(img);
        imageView.setFitWidth(fitWidth);
        imageView.setFitHeight(fitHeight);
        return imageView;
    }

    public static javafx.scene.Node getNodeByID(String id, Pane container){
        ObservableList<javafx.scene.Node> menuLabels = container.getChildren();
        Iterator<javafx.scene.Node> it = menuLabels.iterator();
        while (it.hasNext()) {
            Node node = it.next();
            if (node.getId().equals(id)){
                //System.out.println("Found node: " + id);
                return node;
            }
        }
        return null;
    }

    public static VBox CreateSubMenu(){
        VBox subMenu = new VBox();
        subMenu.getStyleClass().setAll("submenu");
        subMenu.setId("subMenuVBox");
        return subMenu;
    }

    public static void CreateMenuLabel(VBox menu, String labelText, String styleClass, int position, EventHandler mouseOnClick){
        Label label = new Label(labelText);
        label.setId(labelText);
        label.getStyleClass().setAll(styleClass);
        if (mouseOnClick != null) label.setOnMouseClicked(mouseOnClick);
        menu.getChildren().add(position,label);
    }

}

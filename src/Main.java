import java.io.File;
import java.util.List;

import Core.DocumentClassifyResult;
import Core.DocumentsClassifier;
import Tools.Utils;
import UX.DocumentClassifyMainWindow;
import UX.DocumentRevisedFilesWindow;
import UX.TitleEntryClassifyMainWindow;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Main extends Application implements EventHandler<ActionEvent> {

    private Stage mainWindow;
    private DirectoryChooser directoryChooser;

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.mainWindow = primaryStage;
        this.mainWindow.setTitle("广发 File Classification");
        this.directoryChooser = new DirectoryChooser();
        this.directoryChooser.setTitle("Select pdf files root folder");

//        DocumentClassifyMainWindow.apply(this.mainWindow, directoryChooser);

        TitleEntryClassifyMainWindow.apply(this.mainWindow, directoryChooser);

        this.mainWindow.show();
    }

    @Override
    public void handle(ActionEvent event) {
    }
}

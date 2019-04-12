import java.io.File;
import java.util.List;

import Core.ClassifyResult;
import Core.Document;
import Core.DocumentsClassifier;
import Tools.Utils;
import UX.RevisedFilesWindow;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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

        Text inputText = new Text();
        inputText.setText("Please select input folder");
        Text outputText = new Text();
        outputText.setText("Please select output folder");
        Label failedSelectLabel = new Label();

        final Button inputFolderButton, outputFolderButton, startClassifyButton;
        inputFolderButton = new Button();
        inputFolderButton.setText("Input Browser");
        inputFolderButton.setOnAction(e -> {
            File dir = directoryChooser.showDialog(this.mainWindow);
            if (dir != null) {
                inputText.setText(dir.getAbsolutePath());
            } else {
                inputText.setText("Filed to select directory!");
            }
        });

        outputFolderButton = new Button();
        outputFolderButton.setText("Output Browser");
        outputFolderButton.setOnAction(e -> {
            File dir = directoryChooser.showDialog(this.mainWindow);
            if (dir != null) {
                outputText.setText(dir.getAbsolutePath());
            } else {
                outputText.setText("Filed to select directory!");
            }
        });

        ListView<String> warnList = new ListView<>();
        ListView<String> uncertainList = new ListView<>();

        startClassifyButton = new Button();
        startClassifyButton.setText("Start to classify!");
        startClassifyButton.setOnAction(e -> {
            String inputPath = inputText.getText();
            String outputPath = outputText.getText();

            if (!this.checkInputAndOutputPath(inputPath, outputPath)) {
                failedSelectLabel.setText("Failed to select input or output path. Please select them first");
                return;
            } else {
                failedSelectLabel.setText(null);
            }

            try {
                DocumentsClassifier dc = new DocumentsClassifier(outputPath);
                List<File> files = Utils.getAllFiles(inputPath);
                ClassifyResult result = dc.classify(files);

                RevisedFilesWindow revisedFilesWindow = new RevisedFilesWindow(result);
                revisedFilesWindow.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        BorderPane layout = this.getMainLayerout();
        ((VBox)layout.getTop()).getChildren().addAll(inputText, inputFolderButton, outputText, outputFolderButton, startClassifyButton, failedSelectLabel);

        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);

        this.mainWindow.show();
    }

    @Override
    public void handle(ActionEvent event) {
    }

    private BorderPane getMainLayerout() {
        BorderPane mainLayout = new BorderPane();

        VBox top = new VBox();
        top.setAlignment(Pos.CENTER);

        GridPane center = new GridPane();

        HBox bottom = new HBox();
        bottom.setAlignment(Pos.CENTER);

        mainLayout.setTop(top);
        mainLayout.setBottom(bottom);
        mainLayout.setCenter(center);

        return mainLayout;
    }

    private boolean checkInputAndOutputPath(String inputPath, String outputPath) {
        if (inputPath.indexOf("\\") < 0 || outputPath.indexOf("\\") < 0) {
            return false;
        }
        if (inputPath.indexOf("Failed") > -1 || outputPath.indexOf("Failed") > -1) {
            return false;
        }
        return true;
    }

//    private GridPane get
}

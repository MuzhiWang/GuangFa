import java.io.File;
import java.util.List;

import Core.DocumentClassifyResult;
import Core.DocumentsClassifier;
import Tools.Utils;
import UX.RevisedFilesWindow;
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

        Label inputLable = new Label();
        inputLable.setText("Input folder:");
        TextField inputText = new TextField();
        inputText.setText("Please select input folder");
        inputText.setOnMouseClicked(e -> {
            File dir = directoryChooser.showDialog(this.mainWindow);
            if (dir != null) {
                inputText.setText(dir.getAbsolutePath());
            }
        });


        TextField outputText = new TextField();
        Label outputLable = new Label();
        outputLable.setText("Output folder:");
        outputText.setText("Please select output folder");
        outputText.setOnMouseClicked(e -> {
            File dir = directoryChooser.showDialog(this.mainWindow);
            if (dir != null) {
                outputText.setText(dir.getAbsolutePath());
            }
        });


        Label failedSelectLabel = new Label();
        failedSelectLabel.setTextFill(Color.FIREBRICK);
        failedSelectLabel.setText("Failed to select input or output path. Please select them first");
        failedSelectLabel.setVisible(false);

        Button inputFolderButton, outputFolderButton, startClassifyButton;
        inputFolderButton = new Button();
        inputFolderButton.setText("Input Browser");
        inputFolderButton.setOnAction(e -> {
            File dir = directoryChooser.showDialog(this.mainWindow);
            if (dir != null) {
                inputText.setText(dir.getAbsolutePath());
            }
        });

        outputFolderButton = new Button();
        outputFolderButton.setText("Output Browser");
        outputFolderButton.setOnAction(e -> {
            File dir = directoryChooser.showDialog(this.mainWindow);
            if (dir != null) {
                outputText.setText(dir.getAbsolutePath());
            }
        });

        startClassifyButton = new Button();
        startClassifyButton.setText("Start to classify!");
        startClassifyButton.setOnAction(e -> {
            String inputPath = inputText.getText();
            String outputPath = outputText.getText();

            if (!this.checkInputAndOutputPath(inputPath, outputPath)) {
                failedSelectLabel.setVisible(true);
                return;
            } else {
                failedSelectLabel.setVisible(false);
            }

            try {
                DocumentsClassifier dc = new DocumentsClassifier(outputPath);
                List<File> files = Utils.getAllFiles(inputPath);
                DocumentClassifyResult result = dc.classify(files);

                RevisedFilesWindow revisedFilesWindow = new RevisedFilesWindow(result);
                revisedFilesWindow.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(20);
        layout.setVgap(20);

        layout.add(inputLable, 0, 0);
        layout.add(inputText, 1, 0, 2, 1);
        layout.add(inputFolderButton, 4, 0);
        layout.add(outputLable, 0, 2);
        layout.add(outputText, 1, 2, 2, 1);
        layout.add(outputFolderButton, 4, 2, 2, 1);
        layout.add(startClassifyButton, 2, 4, 4, 2);
        layout.add(failedSelectLabel, 0, 6, 4, 1);

        Scene scene = new Scene(layout, 640, 480);
        primaryStage.setScene(scene);

        this.mainWindow.show();
    }

    @Override
    public void handle(ActionEvent event) {
    }

    private boolean checkInputAndOutputPath(String inputPath, String outputPath) {
        return (this.checkStringIsPath(inputPath) && this.checkStringIsPath(outputPath));
    }

    private boolean checkStringIsPath(String str) {
        return (str.indexOf("\\") > -1 && str.indexOf(".") < 0);
    }
}

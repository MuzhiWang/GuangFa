package UX;

import Core.DocumentClassifyResult;
import Core.DocumentsClassifier;
import Core.TitleCollection;
import Core.TitleEntryClassifier;
import Tools.Utils;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.List;

/**
 * Created by muwang on 4/18/2019.
 */
public class TitleEntryClassifyMainWindow {

    public static void apply(Stage window, DirectoryChooser directoryChooser) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel file");

        Label excelLabel = new Label();
        excelLabel.setText("Excel file:");
        TextField excelText = new TextField();
        excelText.setText("Please select excel file.");
        excelText.setOnMouseClicked(e -> {
            File file = fileChooser.showOpenDialog(window);
            if (file != null) {
                excelText.setText(file.getAbsolutePath());
            }
        });

        Label inputLable = new Label();
        inputLable.setText("Input folder:");
        TextField inputText = new TextField();
        inputText.setText("Please select input folder");
        inputText.setOnMouseClicked(e -> {
            File dir = directoryChooser.showDialog(window);
            if (dir != null) {
                inputText.setText(dir.getAbsolutePath());
            }
        });

        TextField outputText = new TextField();
        Label outputLable = new Label();
        outputLable.setText("Output folder:");
        outputText.setText("Please select output folder");
        outputText.setOnMouseClicked(e -> {
            File dir = directoryChooser.showDialog(window);
            if (dir != null) {
                outputText.setText(dir.getAbsolutePath());
            }
        });


        Label failedSelectLabel = new Label();
        failedSelectLabel.setTextFill(Color.FIREBRICK);
        failedSelectLabel.setText("Failed to select input or output path. Please select them first");
        failedSelectLabel.setVisible(false);

        Button excelSelectButton, inputFolderButton, outputFolderButton, startClassifyButton;

        excelSelectButton = new Button();
        excelSelectButton.setText("Excel file Browser");
        excelSelectButton.setOnAction(e -> {
            File dir = directoryChooser.showDialog(window);
            if (dir != null) {
                excelText.setText(dir.getAbsolutePath());
            }
        });

        inputFolderButton = new Button();
        inputFolderButton.setText("Input Browser");
        inputFolderButton.setOnAction(e -> {
            File dir = directoryChooser.showDialog(window);
            if (dir != null) {
                inputText.setText(dir.getAbsolutePath());
            }
        });

        outputFolderButton = new Button();
        outputFolderButton.setText("Output Browser");
        outputFolderButton.setOnAction(e -> {
            File dir = directoryChooser.showDialog(window);
            if (dir != null) {
                outputText.setText(dir.getAbsolutePath());
            }
        });

        startClassifyButton = new Button();
        startClassifyButton.setText("Start to classify!");
        startClassifyButton.setOnAction(e -> {
            String excelPath = excelText.getText();
            String inputPath = inputText.getText();
            String outputPath = outputText.getText();

            if (!checkInputAndOutputPath(inputPath, outputPath)) {
                failedSelectLabel.setVisible(true);
                return;
            } else {
                failedSelectLabel.setVisible(false);
            }

            try {
                TitleEntryClassifier classifier = new TitleEntryClassifier(inputPath, outputPath);
                TitleCollection titleCollection = classifier.classify(excelPath, false);

                TitleEntryRevisedFilesWindow revisedFilesWindow = new TitleEntryRevisedFilesWindow(titleCollection.errorCollection);
                revisedFilesWindow.show();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });


        GridPane layout = new GridPane();
        layout.setAlignment(Pos.CENTER);
        layout.setHgap(20);
        layout.setVgap(20);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(excelLabel, excelText, excelSelectButton);
//        layout.add(excelLabel, 0, 0);
//        layout.add(excelText, 1, 0, 2, 1);
//        layout.add(excelSelectButton, 4, 0);
        layout.add(vBox, 0, 0);

        layout.add(inputLable, 0, 1);
        layout.add(inputText, 1, 1, 2, 1);
        layout.add(inputFolderButton, 4, 1);

        layout.add(outputLable, 0, 3);
        layout.add(outputText, 1, 3, 2, 1);
        layout.add(outputFolderButton, 4, 3, 2, 1);

        layout.add(startClassifyButton, 2, 5, 4, 2);
        layout.add(failedSelectLabel, 0, 7, 4, 1);

        Scene scene = new Scene(layout, 640, 480);
        window.setScene(scene);
    }

    private static boolean checkInputAndOutputPath(String inputPath, String outputPath) {
        return (checkStringIsPath(inputPath) && checkStringIsPath(outputPath));
    }

    private static boolean checkStringIsPath(String str) {
        return (str.indexOf("\\") > -1 && str.indexOf(".") < 0);
    }
}

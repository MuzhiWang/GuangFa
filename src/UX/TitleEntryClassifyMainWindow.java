package UX;

import Core.DocumentClassifyResult;
import Core.DocumentsClassifier;
import Core.TitleCollection;
import Core.TitleEntryClassifier;
import Settings.FileSettings;
import Tools.Utils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by muwang on 4/18/2019.
 */
public class TitleEntryClassifyMainWindow {

    private final static Font labelFont = Font.font("Arial", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 16);

    public static void apply(Stage window, DirectoryChooser directoryChooser) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Excel file");

        Label excelLabel = new Label();
        excelLabel.setText("Excel file:");
        excelLabel.setFont(labelFont);
        TextField excelText = new TextField();
        excelText.setText("Please select excel file");
        excelText.setMinWidth(200);
        Label excelErrorLabel = new Label();
        excelErrorLabel.setText("Select file must be excel file, which end up with '.xlsx' or 'xls'");
        excelErrorLabel.setTextFill(Color.FIREBRICK);
        excelErrorLabel.setVisible(false);
        excelText.setOnMouseClicked(e -> {
            File file = fileChooser.showOpenDialog(window);
            if (file != null) {
                excelText.setText(file.getAbsolutePath());
                if (checkExcelFile(excelText.getText())) {
                    excelErrorLabel.setVisible(false);
                } else {
                    excelErrorLabel.setVisible(true);
                }
            }
        });

        Label inputLable = new Label();
        inputLable.setText("Input folder:");
        inputLable.setFont(labelFont);
        TextField inputText = new TextField();
        inputText.setText("Please select input folder");
        inputText.setMinWidth(200);
        inputText.setOnMouseClicked(e -> {
            File dir = directoryChooser.showDialog(window);
            if (dir != null) {
                inputText.setText(dir.getAbsolutePath());
            }
        });

        TextField outputText = new TextField();
        Label outputLable = new Label();
        outputLable.setFont(labelFont);
        outputLable.setText("Output folder:");
        outputText.setText("Please select output folder");
        outputText.setMinWidth(200);
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
            File file = fileChooser.showOpenDialog(window);
            if (file != null) {
                excelText.setText(file.getAbsolutePath());
                if (checkExcelFile(excelText.getText())) {
                    excelErrorLabel.setVisible(false);
                } else {
                    excelErrorLabel.setVisible(true);
                }
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

            if (!checkInputAndOutputPath(inputPath, outputPath) || !checkExcelFile(excelPath)) {
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
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(20));
        vBox.setBorder(new Border(new BorderStroke(Color.BLACK,
                BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        vBox.getChildren().addAll(excelLabel, excelText, excelSelectButton, excelErrorLabel);
        layout.add(vBox, 0, 0, 2, 1);

        layout.add(inputLable, 0, 1);
        layout.add(inputText, 1, 1, 2, 1);
        layout.add(inputFolderButton, 4, 1);

        layout.add(outputLable, 0, 3);
        layout.add(outputText, 1, 3, 2, 1);
        layout.add(outputFolderButton, 4, 3, 2, 1);

        layout.add(startClassifyButton, 2, 5, 4, 2);
        layout.add(failedSelectLabel, 0, 7, 4, 1);

        VBox pbVbox = new VBox();
        createProgressBar(pbVbox);
        layout.add(pbVbox, 2, 8);

        Scene scene = new Scene(layout, 640, 480);
        window.setScene(scene);
    }

    private static boolean checkInputAndOutputPath(String inputPath, String outputPath) {
        return (checkStringIsPath(inputPath) && checkStringIsPath(outputPath));
    }

    private static boolean checkStringIsPath(String str) {
        return (str.indexOf("\\") > -1 && str.indexOf(".") < 0);
    }

    private static boolean checkExcelFile(String filePath) {
        Pattern pattern = Pattern.compile(FileSettings.EXCEL_FORMAT);
        Matcher matcher = pattern.matcher(filePath);
        return matcher.find();
    }

    static double ii = 0;
    private static void createProgressBar(Pane pane) {
        ii = 0;

        // create a progressbar
        ProgressBar pb = new ProgressBar();

        // create a tile pane
        TilePane r = new TilePane();

        // creating button
        Button b = new Button("Increase");

        // action event
        EventHandler<ActionEvent> event = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)
            {
                if (b.getText().equals("End")) {
                    b.setText("Increase");
                    ii = 0;
                    pb.setProgress(ii);
                    return;
                }

                if (ii >= 1) {
                    b.setText("End");
                    pb.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
                    return;
                }

                // set progress to different level of progressbar
                ii += 0.05;
                pb.setProgress(ii);
            }
        };

        // set on action
        b.setOnAction(event);

        pane.getChildren().addAll(pb, b);
    }
}

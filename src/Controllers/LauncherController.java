package Controllers;

import Language.LanguageLearner;
import Views.Launcher;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;

public class LauncherController {
    // FXML attributes
    public Button selectInputFile;
    public Button generateButton;
    public Button refreshButton;
    public TextArea outputTextArea;
    public TextArea realTimeTextArea;
    public Label inputFileLabel;
    public Label adHocFileLabel;
    public ChoiceBox<Integer> languageLevelChoiceBox;

    private final LanguageLearner languageLearner = new LanguageLearner();
    private final Launcher launcher = Launcher.getInstance();

    private final FileChooser fileChooser = new FileChooser();
    private File selectedInputFile;
    private File selectedAdHocFile;
    private String lastUsedInput;

    private enum GenerateMode { REALTIME, REFRESH, SUBMIT }


    @FXML
    private void initialize() {
        languageLevelChoiceBox.setItems(FXCollections.observableArrayList(1, 2, 3, 4, 5, 6));
        languageLevelChoiceBox.setValue(1);
        generateButton.setOnAction(e -> {
            try {
                generate(GenerateMode.SUBMIT);
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        });
        refreshButton.setOnAction(e -> {
            try {
                generate(GenerateMode.REFRESH);
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        });
    }

    public void generate(GenerateMode mode) throws IOException {
        String input;
        // Retrieve the input
        switch (mode) {
            case REALTIME:
                input = realTimeTextArea.getText();
                break;
            case REFRESH:
                input = lastUsedInput;
                break;
            case SUBMIT:
                if (selectedInputFile == null) {
                    inputFileLabel.setText("Please select an input file");
                    return;
                }
                input = new String(Files.readAllBytes(selectedInputFile.toPath()));
                break;
            default:
                throw new IOException("The input was unable to be located!");
        }

        lastUsedInput = input;

        // Retrieve the Ad Hoc List
        HashSet<String> adHocList = getAdHocList();

        // Retrieve the level
        int level = languageLevelChoiceBox.getValue();

        String output = languageLearner.process(input,
                level,
                adHocList);
        outputTextArea.setText(output);
    }

    public void textFieldPressed(KeyEvent e) throws Exception {
        if (e.getCode() == KeyCode.ENTER) {
            generate(GenerateMode.REALTIME);
            realTimeTextArea.setText("");
        }
    }

    public void selectInputFile() {
        selectedInputFile = fileChooser.showOpenDialog(launcher.getRootStage());
        if (selectedInputFile != null) {
            inputFileLabel.setText(selectedInputFile.getPath());
        }
    }

    public void selectAdHocFile() {
        selectedAdHocFile = fileChooser.showOpenDialog(launcher.getRootStage());
        if (selectedAdHocFile != null) {
            adHocFileLabel.setText(selectedAdHocFile.getPath());
        }
    }

    private HashSet<String> getAdHocList() throws IOException {
        HashSet<String> adHocList = new HashSet<>();
        if (selectedAdHocFile == null) return adHocList;
        String[] input = new String(Files.readAllBytes(selectedAdHocFile.toPath())).split("\\s+");
        for (String s : input) {
            adHocList.add(s.toLowerCase());
        }
        return adHocList;
    }

}

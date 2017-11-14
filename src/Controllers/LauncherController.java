package Controllers;

import Language.LanguageLearner;
import Views.Launcher;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;

public class LauncherController {
    public Button select;
    public TextArea outputText;
    public TextField levelText;
    public Label languageSelectLabel;

    final LanguageLearner languageLearner = new LanguageLearner();
    final Launcher launcher = Launcher.getInstance();

    private final FileChooser fileChooser = new FileChooser();
    private File selectedFile;

    private final String[] AD_HOC_LIST = {};

    public void generate() throws Exception {
        if (selectedFile == null) {
            languageSelectLabel.setText("Please select an input file");
            return;
        }
        String input = new String(Files.readAllBytes(selectedFile.toPath()));
        String output = languageLearner.process(input,
                Integer.parseInt(levelText.getText()),
                new HashSet<>(Arrays.asList(AD_HOC_LIST)));
        outputText.setText(output);
    }

    public void textFieldPressed(KeyEvent e) throws Exception {
        if (e.getCode() == KeyCode.ENTER) {
            generate();
        }
    }

    public void selectInputFile() {
        selectedFile = fileChooser.showOpenDialog(launcher.getRootStage());
        languageSelectLabel.setText(selectedFile.getPath());
    }

}

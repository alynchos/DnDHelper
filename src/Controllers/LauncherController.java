package Controllers;

import Language.LanguageLearner;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.Arrays;
import java.util.HashSet;

public class LauncherController {
    public Button button;
    public TextField input;
    public TextField outputText;
    public TextField levelText;

    LanguageLearner languageLearner = new LanguageLearner();

    private final String[] AD_HOC_LIST = {};

    public void generate() throws Exception {
        String output = languageLearner.process(input.getText(),
                Integer.parseInt(levelText.getText()),
                new HashSet<>(Arrays.asList(AD_HOC_LIST)));
        outputText.setText(output);
    }

    public void textFieldPressed(KeyEvent e) throws Exception {
        if(e.getCode() == KeyCode.ENTER) {
            generate();
        }
    }
}

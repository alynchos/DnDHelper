import Language.LanguageLearner;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

public class LanguageLearnerTest {

    //<editor-fold desc="Ad Hoc List">
    private final String[] AD_HOC_LIST = {
            "alex",
            "balor",
            "balon",
            "bilor",
            "bannuc",
            "gullington",
            "tanarukk",
            "dontrok",
            "ifrit",
            "grognar",
            "gruumsh",
            "luthic",
            "orog",
            "obould",
    };
    //</editor-fold>

    @Test
    public void levelTest() {
        LanguageLearner learner = new LanguageLearner();
        Path inputFile = Paths.get("/Users/alexly/Documents/languageLearnerTest1");
        try {
            String input = new String(Files.readAllBytes(inputFile));
            for (int i = 1; i < 7; i++) {
                String output = learner.process(input, i, new HashSet<>(Arrays.asList(AD_HOC_LIST)));
                System.out.println(String.format("LEVEL %d: %s", i, output));
            }
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}

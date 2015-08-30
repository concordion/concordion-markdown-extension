package examples.concordion.markdown;

import org.concordion.api.extension.Extensions;
import org.concordion.ext.MarkdownExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(MarkdownExtension.class)
public class Simple {

    private String x;

    public String getX() {
        return x;
    }

    public int add(int a, int b) {
        return a + b;
    }

    public void setup() {
        x = "setup";
    }

    public void run() {
        x = "run";
    }
}

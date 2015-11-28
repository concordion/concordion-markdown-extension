package examples.concordion.markdownExamples;

import org.concordion.api.extension.Extensions;
import org.concordion.ext.MarkdownExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(MarkdownExtension.class)
public class Tables {
    
    private String time;

    public String getGreeting() {
        return "Hello World!";
    }

    public String greetingFor(String firstName) {
        return "Hello " + firstName + "!";
    }
    
    public void setCurrentTime(String time) {
        this.time = time;
    }
    
    public String getDailyGreeting() {
        if (time != null && time.endsWith("AM")) {
            return "Good Morning World!";
        }
        return getGreeting();
    }
}

package examples.concordion.markdownExamples;

import org.concordion.api.extension.Extensions;
import org.concordion.ext.MarkdownExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
@Extensions(MarkdownExtension.class)
public class SplittingNamesTable {
    public Result split(String fullName) {
        Result result = new Result();
        String[] words = fullName.split(" ");
        result.firstName = words[0];
        result.lastName = words[1];
        return result;
    }

    class Result {
        public String firstName;
        public String lastName;
    }
}

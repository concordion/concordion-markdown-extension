package examples.concordion.markdownExamples;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.concordion.api.extension.Extension;
import org.concordion.ext.MarkdownExtension;
import org.concordion.integration.junit4.ConcordionRunner;
import org.concordion.internal.FileTarget;
import org.junit.runner.RunWith;

@RunWith(ConcordionRunner.class)
public class PartialMatchesTest {

    @Extension
    public MarkdownExtension ex = new MarkdownExtension().withSourceHtmlSavedTo(new FileTarget(new File("/tmp")));

    private Set<String> usernamesInSystem = new HashSet<String>();

    public void setUpUser(String username) {
        usernamesInSystem.add(username);
    }

    public Iterable<String> getSearchResultsFor(String searchString) {
        SortedSet<String> matches = new TreeSet<String>();
        for (String username : usernamesInSystem) {
            if (username.contains(searchString)) {
                matches.add(username);
            }
        }
        return matches;
    }
}
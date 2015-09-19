package org.concordion.ext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.concordion.api.Resource;
import org.concordion.api.Source;
import org.concordion.api.Target;
import org.concordion.internal.ClassPathSource;
import org.concordion.markdown.MarkdownParser;

public class MarkdownClassPathSource implements Source {

    private final Source classPathSource = new ClassPathSource();
    private final MarkdownParser markdownParser = new MarkdownParser();
    private String concordionNamespacePrefix = "concordion";
    private Target interimHtmlTarget;
    
    @Override
    public InputStream createInputStream(Resource resource) throws IOException {
        String markdown = read(resource);
        String html = markdownParser.markdownToHtml(markdown, concordionNamespacePrefix);
        html = wrapBody(html);

        if (interimHtmlTarget != null) {
            interimHtmlTarget.write(new Resource(resource.getPath() + ".html"), html);
        }

        return new ByteArrayInputStream(html.getBytes());
    }

    private String read(Resource resource) throws IOException {
        InputStream inputStream = classPathSource.createInputStream(resource);
        Scanner scanner = null;
        try {
            scanner = new Scanner(inputStream, "UTF-8");
            return scanner.useDelimiter("\\A").next();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private String wrapBody(String body) {
        return "<html xmlns:" + concordionNamespacePrefix + "='http://www.concordion.org/2007/concordion'><body>" + body + "</body></html>";
    }

    @Override
    public boolean canFind(Resource resource) {
        return classPathSource.canFind(resource);
    }

    public void setInterimHtmlTarget(Target target) {
        this.interimHtmlTarget = target;
    }

    public void setConcordionNamespacePrefix(String concordionNamespacePrefix) {
        this.concordionNamespacePrefix = concordionNamespacePrefix;
    }
}

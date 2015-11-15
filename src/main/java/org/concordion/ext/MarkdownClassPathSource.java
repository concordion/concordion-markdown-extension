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
    private String concordionNamespacePrefix = "concordion";
    private Target sourceHtmlTarget;
    private int pegdownExtensions;
    
    @Override
    public InputStream createInputStream(Resource resource) throws IOException {
        if (resource.getName().endsWith(MarkdownExtension.MARKDOWN_FILE_EXTENSION)) {
            String markdown = read(resource);
            MarkdownParser markdownParser = new MarkdownParser(pegdownExtensions);
            String html = markdownParser.markdownToHtml(markdown, concordionNamespacePrefix);
            html = wrapBody(html);
    
            if (sourceHtmlTarget != null) {
                Resource sourceHtmlResource = new Resource(resource.getPath() + ".html");
                sourceHtmlTarget.write(sourceHtmlResource, html);
                System.out.println(String.format("[Source: %s]", sourceHtmlTarget.resolvedPathFor(sourceHtmlResource)));
            }
    
            return new ByteArrayInputStream(html.getBytes());
        } else {
            return classPathSource.createInputStream(resource);
        }
    }

    private String read(Resource resource) throws IOException {
        InputStream inputStream = classPathSource.createInputStream(resource);
        Scanner scanner = null;
        try {
            scanner = new Scanner(inputStream, "UTF-8");
            if (scanner.hasNext()) {
                return scanner.useDelimiter("\\A").next();
            }
            return "";
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

    public void setSourceHtmlTarget(Target target) {
        this.sourceHtmlTarget = target;
    }

    public void withPegdownExtensions(int extensions) {
        this.pegdownExtensions = extensions;
    }
}

package org.concordion.ext;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import jdk.nashorn.internal.ir.ThrowNode;

import org.concordion.api.Resource;
import org.concordion.api.Source;
import org.concordion.internal.ClassPathSource;
import org.concordion.markdown.MarkdownParser;

public class MarkdownClassPathSource implements Source {

    private Source classPathSource = new ClassPathSource();
    private MarkdownParser markdownParser = new MarkdownParser();
    
    @Override
    public InputStream createInputStream(Resource resource) throws IOException {
        String markdown = read(resource);
        String html = markdownParser.markdownToHtml(markdown);        
            
        System.out.println(html);
        html = wrapBody(html);
        return new ByteArrayInputStream(html.getBytes());
    }

    private String read(Resource resource) throws IOException {
        String markdown;
        Scanner scanner = null;
        try {
            InputStream inputStream = classPathSource.createInputStream(resource);
            scanner = new Scanner(inputStream,"UTF-8");
            markdown = scanner.useDelimiter("\\A").next();
        } finally {
            scanner.close();
        }
        return markdown;
    }

    private String wrapBody(String body) {
        return "<html xmlns:concordion='http://www.concordion.org/2007/concordion'><body>" + body + "</body></html>";
    }

    @Override
    public boolean canFind(Resource resource) {
        return classPathSource.canFind(resource);
    }
}

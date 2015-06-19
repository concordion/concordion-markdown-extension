package org.concordion.ext;

import org.concordion.api.Resource;
import org.concordion.api.SpecificationLocator;
import org.concordion.internal.util.Check;

public class MarkdownLocator implements SpecificationLocator {

    @Override
    public Resource locateSpecification(Object fixture) {
        Check.notNull(fixture, "Fixture is null");
        
        String dottedClassName = fixture.getClass().getName();
        String slashedClassName = dottedClassName.replaceAll("\\.", "/");
        String specificationName = slashedClassName.replaceAll("(Fixture|Test)$", "");
        String resourcePath = "/" + specificationName + ".md";
        
        return new Resource(resourcePath);
    }
}

/*
 * Copyright (c) 2013 Nigel Charman, New Zealand 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.concordion.ext;

import org.concordion.api.Source;
import org.concordion.api.SpecificationLocator;
import org.concordion.api.Target;
import org.concordion.api.extension.ConcordionExtender;
import org.concordion.api.extension.ConcordionExtension;
import org.concordion.internal.FileTargetWithSuffix;

/**
 * 
 */
public class MarkdownExtension implements ConcordionExtension {

    public static final String EXTENSION_NAMESPACE = "urn:concordion-extensions:2013";
    private SpecificationLocator locator = new MarkdownLocator();
    private Source source = new MarkdownClassPathSource();
    private Target target = new FileTargetWithSuffix("html");
    
    @Override
    public void addTo(ConcordionExtender concordionExtender) {
        concordionExtender.withSpecificationLocator(locator).withSource(source).withTarget(target);
    }
}

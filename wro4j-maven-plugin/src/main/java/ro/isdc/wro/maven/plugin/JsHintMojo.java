/**
 * Copyright wro4j@2011
 */
package ro.isdc.wro.maven.plugin;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import org.apache.maven.plugin.MojoExecutionException;

import ro.isdc.wro.extensions.processor.js.JsHintProcessor;
import ro.isdc.wro.extensions.processor.support.linter.LinterException;
import ro.isdc.wro.model.resource.Resource;
import ro.isdc.wro.model.resource.processor.ResourceProcessor;


/**
 * Maven plugin used to validate js scripts defined in wro model using <a href="http://jshint.com/">jsLint</a>.
 *
 * @goal jshint
 * @phase compile
 * @requiresDependencyResolution runtime
 * @author Alex Objelean
 * @since 1.3.5
 */
public class JsHintMojo
    extends AbstractSingleProcessorMojo {
  /**
   * {@inheritDoc}
   */
  @Override
  protected ResourceProcessor createResourceProcessor() {
    final ResourceProcessor processor = new JsHintProcessor() {
      @Override
      public void process(final Resource resource, final Reader reader, final Writer writer)
          throws IOException {
        getLog().info("processing resource: " + resource);
        if (resource != null) {
          getLog().info("processing resource: " + resource.getUri());
        }
        super.process(resource, reader, writer);
      }

      protected void onLinterException(final LinterException e, final Resource resource)
          throws Exception {
        getLog().error(
            e.getErrors().size() + " errors found while processing resource: " + resource.getUri() + " Errors are: "
                + e.getErrors());
        if (!isFailNever()) {
          throw new MojoExecutionException("Errors found when validating resource: " + resource);
        }
      };
    }.setOptions(getOptions());
    return processor;
  }
}

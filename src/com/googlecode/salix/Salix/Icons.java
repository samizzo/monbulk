package com.googlecode.salix.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Icons and other resources.
 *
 * @author <a href="mailto:richard.richter@siemens-enterprise.com">Richard "Virgo" Richter</a>
 */
public interface Icons extends ClientBundle {
	Icons INSTANCE = GWT.create(Icons.class);

	@ClientBundle.Source("com/googlecode/salix/client/elbow-end-minus-nl.gif")
	ImageResource collapse();

	@ClientBundle.Source("com/googlecode/salix/client/elbow-end-plus-nl.gif")
	ImageResource expand();

	@ClientBundle.Source("com/googlecode/salix/client/elbow.gif")
	ImageResource elbow();

	@ClientBundle.Source("com/googlecode/salix/client/elbow-plus.gif")
	ImageResource elbowExpand();

	@ClientBundle.Source("com/googlecode/salix/client/elbow-minus.gif")
	ImageResource elbowCollapse();

	@ClientBundle.Source("com/googlecode/salix/client/elbow-end.gif")
	ImageResource elbowEnd();

	@ClientBundle.Source("com/googlecode/salix/client/elbow-end-plus.gif")
	ImageResource elbowEndExpand();

	@ClientBundle.Source("com/googlecode/salix/client/elbow-end-minus.gif")
	ImageResource elbowEndCollapse();

	@ClientBundle.Source("com/googlecode/salix/client/elbow-line.gif")
	ImageResource elbowLine();
}

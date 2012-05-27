package com.googlecode.salix.Salix;

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

	@Source("com/googlecode/salix/Salix/elbow-end-minus-nl.gif")
	ImageResource collapse();

	@Source("com/googlecode/salix/Salix/elbow-end-plus-nl.gif")
	ImageResource expand();

	@Source("com/googlecode/salix/Salix/elbow.gif")
	ImageResource elbow();

	@Source("com/googlecode/salix/Salix/elbow-plus.gif")
	ImageResource elbowExpand();

	@Source("com/googlecode/salix/Salix/elbow-minus.gif")
	ImageResource elbowCollapse();

	@Source("com/googlecode/salix/Salix/elbow-end.gif")
	ImageResource elbowEnd();

	@Source("com/googlecode/salix/Salix/elbow-end-plus.gif")
	ImageResource elbowEndExpand();

	@Source("com/googlecode/salix/Salix/elbow-end-minus.gif")
	ImageResource elbowEndCollapse();

	@Source("com/googlecode/salix/Salix/elbow-line.gif")
	ImageResource elbowLine();
}

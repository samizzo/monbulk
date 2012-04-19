package daris.Monbulk.MethodBuilder.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class MethodViewerPopup extends Composite {

	private static MethodViewerPopupUiBinder uiBinder = GWT
			.create(MethodViewerPopupUiBinder.class);

	interface MethodViewerPopupUiBinder extends
			UiBinder<Widget, MethodViewerPopup> {
	}

	public MethodViewerPopup() {
		initWidget(uiBinder.createAndBindUi(this));
	}

}

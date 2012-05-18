package monbulk.MethodBuilder.client.view;



import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import monbulk.shared.Architecture.IPresenter;
import monbulk.shared.Architecture.IPresenter.DockedPresenter;
import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.IView.IFormView;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormField;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.StackedCategories;


public class MethodForm extends baseForm implements IFormView {

	public MethodForm(FormPresenter presenter) {
		super(presenter);
	}
	
}

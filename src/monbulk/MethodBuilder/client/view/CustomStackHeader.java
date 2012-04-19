package daris.Monbulk.MethodBuilder.client.view;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.Widget;

import daris.Monbulk.shared.Architecture.IPresenter;
import daris.Monbulk.shared.Architecture.IPresenter.FormPresenter;
import daris.Monbulk.shared.Architecture.IView;

public class CustomStackHeader extends Composite implements IView {

	private static CustomStackHeaderUiBinder uiBinder = GWT
			.create(CustomStackHeaderUiBinder.class);

	interface CustomStackHeaderUiBinder extends
			UiBinder<Widget, CustomStackHeader> {
	}
	private FormPresenter Presenter;
	private String Title;
	private String CommandArgument;
	
	@UiField 
	Label PanelTitle;
	
	@UiField
	PushButton btnEdit;
	@UiField
	PushButton btnDelete;
	
	public CustomStackHeader(String Title, String Argument) {
		initWidget(uiBinder.createAndBindUi(this));
		this.Title= Title;
		this.CommandArgument = Argument;
		this.btnEdit.setStyleName("btnEditing");
		//this.btnDelete.setStyleName("Delete");
		this.PanelTitle.setText(Title);
	}

	public String getTitle(){return this.Title;}
	public String getCommandArgument(){return this.CommandArgument;}
	public void setTitle(String tmpTitle){ this.Title= tmpTitle;PanelTitle.setText(tmpTitle);}
	@Override
	public void setPresenter(IPresenter presenter) {
		this.Presenter = (FormPresenter)presenter;
		
	}
	
	@Override
	public void setData(ArrayList<String> someList) {
		// TODO Auto-generated method stub
		
	}
	@UiHandler("btnEdit")
	public void onClick2(ClickEvent e)
	{
		this.Presenter.FormComplete(this.CommandArgument, "Edit");
	}
	@UiHandler("btnDelete")
	public void onClick(ClickEvent e)
	{
		this.Presenter.FormComplete(this.CommandArgument, "Delete");
	}
	public void setState(Boolean isSelected)
	{
		if(isSelected)
		{
			this.btnEdit.setStyleName("btnEditing");
		}
		else
		{
			this.btnEdit.setStyleName("btnEdit");
		}
	}

}

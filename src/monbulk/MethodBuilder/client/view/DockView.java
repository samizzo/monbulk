package daris.Monbulk.MethodBuilder.client.view;


import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import daris.Monbulk.shared.Architecture.IPresenter.DockedPresenter;
import daris.Monbulk.shared.Architecture.IView.IDockView;
import daris.Monbulk.shared.Architecture.IView.IDraggable;
import daris.Monbulk.shared.Architecture.IPresenter;
import daris.Monbulk.shared.Model.IPojo;
import daris.Monbulk.shared.Model.StackedCategories;
import daris.Monbulk.shared.Model.StackedCategories.ItemData;
import daris.Monbulk.shared.Model.StackedCategories.ParentCategory;
import daris.Monbulk.shared.Model.pojo.pojoMetaData;

public class DockView extends Composite implements IDockView, IDraggable {

	private static DockViewUiBinder uiBinder = GWT
			.create(DockViewUiBinder.class);

	interface DockViewUiBinder extends UiBinder<Widget, DockView> {
	}
	
	private DockedPresenter Presenter;
	
	
	@UiField
	StackLayoutPanel MetaDataChildStack;
	
	
	
	@UiField
	PushButton btnMetaData;
	
	
	
	VerticalPanel MetadataItems;
	public DockView() {
		initWidget(uiBinder.createAndBindUi(this));
	
	}

	@Override
	public void setPresenter(IPresenter presenter) {
		// TODO Auto-generated method stub
		Presenter = (DockedPresenter) presenter;
	}

	@Override
	public void setData(ArrayList<String> someList) {
		// TODO Auto-generated StackedCategories
	}
	private void SetMetaData(StackedCategories someData)
	{
		Iterator<ParentCategory> i = someData.getAllParents().iterator();
		//ArrayList<VerticalPanel> MetadataItems = new ArrayList<VerticalPanel>();
		//TODO - we should add a Search Panel
		this.MetaDataChildStack.clear();
		while(i.hasNext())
		{
			ParentCategory tmpCategory = i.next();
			//this.MetaDataChildStack.add(w)
			//this.MetaDataChildStack.setHeaderText()
			if(tmpCategory.getChildItems().size() > 0)
			{
				//StackLayoutPanel childPanel = new StackLayoutPanel(Unit.PX);
				
				Iterator<ItemData> i2 = tmpCategory.getChildItems().iterator();
				
				VerticalPanel tmpPanel = new VerticalPanel();
				
				
				tmpPanel.setStyleName("DraggablePanel");
				tmpPanel.setSpacing(0);
				
				int j =0;
				while(i2.hasNext())
				{
					//childPanel.add(null,c,20);
					
					ItemData tmpItem = i2.next();
					DraggableCellWidget tmpWidg = new DraggableCellWidget(j,true,tmpItem.getPOJO());
					tmpWidg.setName(tmpItem.getName());
					tmpWidg.setPresenter((IPresenter) this.Presenter);
					
					if(tmpItem.getID()=="disabled")
					{
						tmpWidg.disableWidget();
					}
					else
					{
						tmpWidg.enableWidget();
					}
					tmpPanel.add(tmpWidg);
					j++;
					/*ItemData tmpItem = i2.next();
					if(!tmpItem.equals(null))
					{
						childPanel.add(null, tmpItem.getName(), 20);
						
					}*/
					//TODO This needs to be a new Header widget with Drag option, Add Button and View button
				}
				ScrollPanel PanelParent = new ScrollPanel();
				PanelParent.add(tmpPanel);
				this.MetaDataChildStack.add(PanelParent,new HTML(tmpCategory.getParent().getName()),41);
//				childPanel.add(new HTML(""), i2.next().getName(), 22);
				
			}
			else
			{
				this.MetaDataChildStack.add(new HTML("No Items found"),new HTML(tmpCategory.getParent().getName()),41);
			}
			//this.MetaDataChildStack.add(new)	
		}
		
	}
	
	public Boolean RemoveMetaDataItem(int widgetIndex)
	{
		
		if(!this.MetadataItems.equals(null))
		{
			if(!this.MetadataItems.getWidget(widgetIndex).equals(null))
			{
				DraggableCellWidget tmpWidget = (DraggableCellWidget) this.MetadataItems.getWidget(widgetIndex);
				//this.MetadataItems.getWidget(widgetIndex).setStyleName("disabled");
				tmpWidget.disableWidget();
				
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
		
	}
	private void SetHelpData(StackedCategories someData)
	{
		
	}
	@Override
	public void setTabData(StackedCategories someData, String TabName) {
		// TODO Auto-generated method stub
		if(TabName=="Help")
		{
			this.SetHelpData(someData);
		}
		else if(TabName=="MetaData")
		{
			this.SetMetaData(someData);
		}
		
		
	}

	@Override
	public void setPresenter(DockedPresenter presenter) {
		// TODO Auto-generated method stub
		Presenter = (DockedPresenter) presenter;
	}

	
	@UiHandler("btnMetaData")
	public void onClick1(ClickEvent e)
	{
	
		this.btnMetaData.setStylePrimaryName("btnMeta-active");
		this.MetaDataChildStack.setVisible(true);
	}

	@Override
	public void setPojoData(ArrayList<IPojo> someData, String TabName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean DragItem(IPojo someItem) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean DroptItem(IPojo someItem) {
		// TODO Auto-generated method stub
		return null;
	}

}

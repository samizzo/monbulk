package monbulk.client.desktop.Apps;

import arc.gui.gwt.object.ObjectDetailedView;
import arc.gui.gwt.object.ObjectEventHandler;
import arc.gui.gwt.object.ObjectNavigator;
import arc.gui.gwt.object.ObjectNavigatorSelectionHandler;
import arc.gui.gwt.object.ObjectNavigator.DisplayOn;
import arc.gui.gwt.widget.panel.HorizontalSplitPanel;
import arc.gui.gwt.widget.scroll.ScrollPolicy;
import arc.mf.object.ObjectResolveHandler;
import arc.mf.object.tree.Node;
import arc.mf.object.tree.NodeListener;

import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ResizeComposite;

import monbulk.shared.widgets.Window.IWindow;
import monbulk.shared.widgets.Window.WindowSettings;
import daris.client.model.object.DObject;
import daris.client.model.object.DObjectRef;
import daris.client.model.object.tree.DObjectTree;
import daris.client.model.object.tree.DObjectTreeNode;
import daris.client.model.repository.RepositoryRef;
import daris.client.ui.DObjectBrowser;
import daris.client.ui.DObjectGUIRegistry;
public class DarisInterface extends AbsolutePanel implements IWindow {

	private WindowSettings m_windowSettings;
	private ObjectNavigator _ov;
	private ObjectDetailedView _dv;
	private NodeListener _nl;
	public DarisInterface()
	{
		m_windowSettings = new WindowSettings();
		m_windowSettings.width = 1000;
		m_windowSettings.height = 678;
		m_windowSettings.minWidth = 1000;
		m_windowSettings.minHeight = 678;
		m_windowSettings.windowId = "DarisInterface";
		m_windowSettings.windowTitle = "DaRIS";
		this.setWidth("1000px");
		this.setHeight("678px");
		buildDaris();
		
	}
	@Override
	public WindowSettings getWindowSettings() {
		
		return this.m_windowSettings;
	}
	
	@Override
	public void onPreviewNativeEvent(NativePreviewEvent event) {
		// TODO Auto-generated method stub

	}
	public void buildDaris()
	{
		//this.add(DObjectBrowser.instance().getVerticalPanel());
		DObjectBrowser.instance().reset();
		DObjectBrowser.instance().show();
		this.add(DObjectBrowser.instance().window().parent());
		//this.add(D)
	}

}

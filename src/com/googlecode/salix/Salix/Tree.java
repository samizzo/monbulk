package com.googlecode.salix.Salix;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.*;

import java.util.Comparator;

/**
 * Main tree class replacing GWT tree.
 *
 * @author <a href="mailto:peter.manka@siemens-enterprise.com">Peter Manka</a>
 * @author <a href="mailto:richard.richter@siemens-enterprise.com">Richard "Virgo" Richter</a>
 */
public class Tree extends Composite implements HasSelectionHandlers<TreeItem> {
	/**
	 * Main panel contains the whole tree with all its children.
	 */
	private Panel mainPanel = new FlowPanel();

	/**
	 * Internal root node - invisible and non-accessible for user.
	 */
	private TreeItem root = new TreeItem();

	/**
	 * Selected tree item.
	 */
	private TreeItem selected;

	private TreeConfig treeConfig = TreeConfig.DEFAULT;

	private boolean drawn;

	public Tree() {
		root.setTree(this);
		mainPanel.add(root.getMainPanel());
		initWidget(mainPanel);
	}

	public TreeItem addItem(String name) {
		return addItem(new TreeItem(name));
	}

	public TreeItem addItem(Widget widget) {
		return addItem(new TreeItem(widget));
	}

	public TreeItem addItem(TreeItem rootItem) {
		root.addItem(rootItem);
		return rootItem;
	}

	public TreeItem getSelectedItem() {
		return selected;
	}
	
	public void clear()	{
		root.removeItems();
	}
	
	public TreeItem getItem(int index) {
		return root.getChild(index);
	}
	
	public int getItemCount() {
		return root.getChildCount();
	}
	
	public void removeItem(TreeItem item) {
		item.remove();
	}

	public void setSelectedItem(TreeItem item) {
		setSelectedItem(item, true);
	}

	public void setSelectedItem(TreeItem item, boolean fireEvents) {
		if (item == null) {
			if (selected == null) {
				return;
			}
			selected.setSelected(false);
			selected = null;
			return;
		}

		onSelection(item, fireEvents, true);
	}

	private void onSelection(TreeItem item, boolean fireEvents, boolean moveFocus) {
		if (item == root) {
			return;
		}

		if (selected != null) {
			selected.setSelected(false);
		}
		selected = item;

		if (selected != null) {
			selected.getWidget().getElement().scrollIntoView();
			selected.setSelected(true);
			if (fireEvents) {
				SelectionEvent.fire(this, selected);
			}
		}
	}

	public void sortChildren(Comparator<TreeItem> TreeItemComparator) {
		root.sortChildren(TreeItemComparator);
	}

	private Widget actionIcon(ImageResource imageResource, String tooltip, ClickHandler clickHandler) {
		Image image = new Image(imageResource);
		image.setTitle(tooltip);
		image.addStyleName("pointer");
		if (clickHandler != null) {
			image.addClickHandler(clickHandler);
		}
		return image;
	}

	public Widget expandWidget(ClickHandler clickHandler) {
		return actionIcon(treeConfig.getIcons().expand(), "expand", clickHandler);
	}

	public Widget collapseWidget(ClickHandler clickHandler) {
		return actionIcon(treeConfig.getIcons().collapse(), "collapse", clickHandler);
	}

	public Tree setConfiguration(TreeConfig config) {
		treeConfig = config;
		return this;
	}

	public TreeConfig configuration() {
		return treeConfig;
	}

	public HandlerRegistration addSelectionHandler(SelectionHandler<TreeItem> handler) {
		return addHandler(handler, SelectionEvent.getType());
	}

	public boolean isDrawn() {
		return drawn;
	}

	public void draw() {
		drawn = true;
		root.draw(true);
	}

	public void removeSelected() {
		if (selected != null) {
			selected.remove();
		}
	}
}

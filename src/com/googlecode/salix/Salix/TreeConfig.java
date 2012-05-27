package com.googlecode.salix.client;

import java.util.Comparator;

/**
 * Tree Configuration contains customizable features (icons, etc).
 *
 * @author <a href="mailto:richard.richter@siemens-enterprise.com">Richard "Virgo" Richter</a>
 */
public class TreeConfig {
	/**
	 * Default configuration used if no custom is specified.
	 */
	public static final TreeConfig DEFAULT = new TreeConfig();

	/**
	 * Offset (padding) for the next tree level - default is 16 pixels.
	 */
	private int offsetPx = 16;

	private String classTreeItem = "gwt-TreeItem";
	private String classTreeItemSelected = "gwt-TreeItem-selected";
	private boolean showConnectors = true;
	private Icons icons = Icons.INSTANCE;
	private Comparator<TreeItem> comparator;

	public String getClassTreeItem() {
		return classTreeItem;
	}

	public TreeConfig setClassTreeItem(String classTreeItem) {
		this.classTreeItem = classTreeItem;
		return this;
	}

	public String getClassTreeItemSelected() {
		return classTreeItemSelected;
	}

	public TreeConfig setClassTreeItemSelected(String classTreeItemSelected) {
		this.classTreeItemSelected = classTreeItemSelected;
		return this;
	}

	public int getOffsetPx() {
		return offsetPx;
	}

	public TreeConfig setOffsetPx(int offsetPx) {
		this.offsetPx = offsetPx;
		return this;
	}

	public boolean isShowConnectors() {
		return showConnectors;
	}

	public TreeConfig setShowConnectors(boolean showConnectors) {
		this.showConnectors = showConnectors;
		return this;
	}

	public Icons getIcons() {
		return icons;
	}

	public TreeConfig setIcons(Icons icons) {
		this.icons = icons;
		return this;
	}

	public Comparator<TreeItem> getComparator() {
		return comparator;
	}

	public TreeConfig setComparator(Comparator<TreeItem> comparator) {
		this.comparator = comparator;
		return this;
	}
}

package monbulk.shared.Model;

import java.util.ArrayList;
import java.util.Iterator;


public class StackedCategories {

	//Defines a Standard Item
	public class ItemData
	{
		private String CategoryName;
		private String CategoryID;
		private IPojo CategoryHTML;
		public ItemData(String cName, String cID, IPojo cHTML)
		{
			this.CategoryName = cName;
			this.CategoryID = cID;
			this.CategoryHTML = cHTML;
		}
		public ItemData(String cName, String cID)
		{
			this.CategoryName = cName;
			this.CategoryID = cID;
			this.CategoryHTML = null;
		}
		public IPojo getPOJO()
		{
			return this.CategoryHTML;
		}
		public void setHTML(IPojo HTML)
		{
			this.CategoryHTML = HTML;
		}
		public String getID()
		{
			return this.CategoryID;
		}
		public void setID(String ID)
		{
			this.CategoryID= ID;
		}
		public String getName()
		{
			return this.CategoryName;
		}
		public void setName(String Name)
		{
			this.CategoryName = Name;
		}
	}
	//Defines a Parent Category Item with an ArrayList of CHildren
	public class ParentCategory
	{
		private ItemData CategoryParent;
		private ArrayList<ItemData> ChildCategories;
		
		public ParentCategory(String ParentCategoryName, String ParentCategoryID)
		{
			this.CategoryParent = new ItemData(ParentCategoryName, ParentCategoryID);
			this.ChildCategories = new ArrayList<ItemData>();
		}
		public void AddChild(String cName, String cID, IPojo cHTML)
		{
			ItemData tmpItem = new ItemData(cName,cID,cHTML);
			this.ChildCategories.add(tmpItem);
		}
		public Boolean RemoveChild(String cName)
		{
			Iterator<ItemData> i = ChildCategories.iterator();
			while(i.hasNext())
			{
				ItemData tmpItem = (ItemData) i.next();
				if(tmpItem.CategoryName.equals(cName))
				{
					this.ChildCategories.remove(tmpItem);
					return true;
				}
			}
			return false;
			
		}
		public ItemData getChildItemForName(String childName)
		{
			Iterator<ItemData> i = ChildCategories.iterator();
			while(i.hasNext())
			{
				ItemData tmpItem = (ItemData) i.next();
				if(tmpItem.CategoryName.equals(childName))
				{
					return tmpItem;
				}
			}
			return null;
		}
		public ItemData getChildItemForID(String ID)
		{
			Iterator<ItemData> i = ChildCategories.iterator();
			while(i.hasNext())
			{
				ItemData tmpItem = i.next();
				if(tmpItem.CategoryID.equals(ID))
				{
					return tmpItem;
				}
			}
			return null;
		}
		public ArrayList<ItemData> getChildItems()
		{
			return this.ChildCategories;
		}
		public ArrayList<String> getChildTitles()
		{
			//return this.ChildCategories.;
			Iterator<ItemData> i = this.ChildCategories.iterator();
			ArrayList<String> titles = new ArrayList<String>();
			while(i.hasNext())
			{
				ItemData tmpItem = i.next();
				titles.add(tmpItem.CategoryName);
			}
			return titles;
		}
		public ItemData getParent()
		{
			return CategoryParent;
		}
	}
	//Defines an Array of Parent Categories
	private ArrayList<ParentCategory>  ParentCategories;
	
	public StackedCategories()
	{
		this.ParentCategories = new ArrayList<ParentCategory>();
	}
	public void addParent(String ID, String Name)
	{
		ParentCategory tmpItem = new ParentCategory(Name,ID);
		ParentCategories.add(tmpItem);
	}
	public ParentCategory getParentForName(String ParentName)
	{
		Iterator<ParentCategory> i = this.ParentCategories.iterator();
		while(i.hasNext())
		{
			ParentCategory tmpItem = i.next();
			if(tmpItem.CategoryParent.CategoryName.equals(ParentName))
			{
				return tmpItem;
			}
		}
		return null;
	}
	public ArrayList<ParentCategory> getAllParents()
	{
		return this.ParentCategories;
	}
	public Boolean RemoveChild(String ParentName, String ChildName)
	{
		return this.getParentForName(ParentName).RemoveChild(ChildName);
	}
	public void AddChild(String ParentName, String ChildName, String ChildID,IPojo HTML)
	{
		this.getParentForName(ParentName).AddChild(ChildName, ChildID, HTML);
		return;
	}
}

package daris.Monbulk.MethodBuilder.client.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


import daris.Monbulk.shared.view.IResult;
import daris.Monbulk.shared.view.ISearchFilter;
import daris.Monbulk.shared.Form.FormBuilder;
import daris.Monbulk.shared.Model.IPojo;
import daris.Monbulk.shared.Model.StackedCategories;
import daris.Monbulk.shared.Model.pojo.pojoMethod;
import daris.Monbulk.MethodBuilder.shared.iMBModel;




public class MethodModel extends baseModel implements iMBModel{
		
		protected pojoMethod DOMethod;
		
		public MethodModel(String MethodID)
		{
			this.DOMethod = new pojoMethod();
			super.convertPojoToForm(DOMethod);
			
		}
		
		@Override
		public ArrayList<IResult> Search(ArrayList<ISearchFilter> searchFilters) {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
		public void saveData() {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void loadData(String ID) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void loadData(IPojo someDataObject) {
			this.DOMethod = (pojoMethod) someDataObject;
			
		}

		@Override
		public void Update(FormBuilder formData) {
			// TODO Auto-generated method stub
			
		}

	
}

package monbulk.MethodBuilder.client.model;


import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


import monbulk.shared.view.IResult;
import monbulk.shared.view.ISearchFilter;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Model.IPojo;
import monbulk.shared.Model.StackedCategories;
import monbulk.shared.Model.pojo.pojoMethod;
import monbulk.MethodBuilder.shared.iMBModel;




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

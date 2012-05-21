package monbulk.MethodBuilder.client.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;




import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;



import monbulk.MetadataEditor.MetadataEditor;
import monbulk.MetadataEditor.MetadataSelectWindow;
import monbulk.MethodBuilder.client.PreviewWindow;
import monbulk.MethodBuilder.client.PreviewWindow.SupportedFormats;
import monbulk.client.desktop.Desktop;
import monbulk.shared.Architecture.IPresenter.FormPresenter;
import monbulk.shared.Architecture.IView.IFormView;
import monbulk.shared.Architecture.IView.IDraggable;
import monbulk.shared.Events.DragEvent;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormWidget;
import monbulk.shared.Model.pojo.pojoMetaData;
import monbulk.shared.Model.pojo.pojoStepDetails;
import monbulk.shared.Model.pojo.pojoStudy;
import monbulk.shared.Services.Dictionary;
import monbulk.shared.Services.Dictionary.Entry;
import monbulk.shared.Services.DictionaryService;
import monbulk.shared.Services.DictionaryService.GetDictionaryHandler;
import monbulk.shared.widgets.Window.OkCancelWindow;


public class StepForm extends SubjectPropertiesForm implements IFormView,IDraggable,GetDictionaryHandler {

	private int AddedRowIndex; 
	protected final FlexTable _StepMetaDataList;
	
	
	public StepForm(FormPresenter presenter) {
		
		super(presenter);
		_StepMetaDataList = new FlexTable();
		
		//super.MetaDatatable
		//Modality.
	}
	@Override
	public void ClearForm() {
		super.ClearForm();
		this.MetaDatatable.clear();
		this._StepMetaDataList.clear();
	}
	@Override
	public void LoadForm(FormBuilder someForm) {
		super.LoadForm(someForm,true);
		
		int requiredIndex = this.getWidgetIndexForName(pojoStepDetails.SubjectMetaDataField);
		
		this.setMetaDataLocation(requiredIndex+1); //we want this after the spacing row
		this._formItems.add(_StepMetaDataList);
		this.linkMetaDataEditor(pojoStepDetails.SubjectMetaDataField, this.MetaDatatable);
		
		FormWidget tmpWidg = this.getFormWidgetForName(pojoStepDetails.HasStudyField);
		hideStudyDetails();
		if(tmpWidg !=null)
		{
			tmpWidg.getFormWidget().addHandler(new ValueChangeHandler<Boolean>()
			{
	
				@Override
				public void onValueChange(ValueChangeEvent<Boolean> event) {
					// TODO Auto-generated method stub
					if(event.getValue())
					{
						showStudyDetails();
						
					}
					else
					
						hideStudyDetails();
					}
					//event.getValue();
				}
				
			,ValueChangeEvent.getType());
			
			DictionaryService service = DictionaryService.get();
			if (service != null)
			{
				
				service.getDictionary(pojoStudy.DICOM_DICTIONARY, this);
				service.getDictionary(pojoStudy.STUDYTYPE_DICTIONARY, this);


			}
		}
		//final FormWidget tmpWidg2 = this.getFormWidgetForName(pojoStepDetails.SubjectMetaDataField);
		
		this.linkMetaDataEditor(pojoStudy.STUDY_METADATA, this._StepMetaDataList);
		
	}
	
	@Override
	public void setData(ArrayList<String> someList) {
		// TODO Auto-generated method stub
		
	}

	private final void hideStudyDetails()
	{
		this.hideShowWidget(pojoStudy.DicomModalityField,false,false);
		this.hideShowWidget(pojoStudy.StudyTypeField,false,false);
		this.hideShowWidget(pojoStudy.STUDY_METADATA,false,false);
		this._StepMetaDataList.setVisible(false);
		
	}
	private final void showStudyDetails()
	{
		this.hideShowWidget(pojoStudy.DicomModalityField,true,true);
		this.hideShowWidget(pojoStudy.StudyTypeField,true,true);
		this.hideShowWidget(pojoStudy.STUDY_METADATA,true,true);
		this._StepMetaDataList.setVisible(true);
	}

	@Override
	public void onGetDictionary(Dictionary dictionary) {
		
		try
		{
			if(dictionary.getName()==pojoStudy.DICOM_DICTIONARY)
			{
				FormWidget tmpForm = this.getFormWidgetForName(pojoStudy.DicomModalityField);
				ListBox tmpWidg = (ListBox)tmpForm.getFormWidget();
				Collection<Entry> tmpList = dictionary.getEntries();
				Iterator<Entry> i = tmpList.iterator();
				while(i.hasNext())
				{
					Entry item = i.next();
					tmpWidg.addItem(item.getTerm());
				}
			}
			if(dictionary.getName()==pojoStudy.STUDYTYPE_DICTIONARY)
			{
				FormWidget tmpForm= this.getFormWidgetForName(pojoStudy.StudyTypeField);
				ListBox tmpWidg = (ListBox)tmpForm.getFormWidget();
				Collection<Entry> tmpList = dictionary.getEntries();
				Iterator<Entry> i = tmpList.iterator();
				while(i.hasNext())
				{
					Entry item = i.next();
					tmpWidg.addItem(item.getTerm());
				}
			}
		}
		catch(Exception ex)
		{
			Window.alert("Exception caught @ StepForm.onGEeDictionary" + ex.getMessage());
		}
		
	}

	


	
}

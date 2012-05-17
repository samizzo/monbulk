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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;



import monbulk.MetadataEditor.MetadataEditor;
import monbulk.MethodBuilder.client.PreviewWindow;
import monbulk.client.desktop.Desktop;
import monbulk.shared.Architecture.IView.IFormView;
import monbulk.shared.Architecture.IView.IDraggable;
import monbulk.shared.Form.FormBuilder;
import monbulk.shared.Form.FormWidget;
import monbulk.shared.Model.pojo.pojoStepDetails;
import monbulk.shared.Model.pojo.pojoStudy;
import monbulk.shared.Services.Dictionary;
import monbulk.shared.Services.Dictionary.Entry;
import monbulk.shared.Services.DictionaryService;
import monbulk.shared.Services.DictionaryService.GetDictionaryHandler;


public class StepForm extends SubjectPropertiesForm implements IFormView,IDraggable,GetDictionaryHandler {

	private int AddedRowIndex; 
	
	
	
	public StepForm() {
		
		super();
		
		//Modality.
	}

	@Override
	public void LoadForm(FormBuilder someForm) {
		super.LoadForm(someForm);
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
		final FormWidget tmpWidg2 = this.getFormWidgetForName(pojoStepDetails.SubjectMetaDataField);
		PushButton tmpButton = (PushButton)tmpWidg2.getFormWidget();
		tmpButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				String FieldName = tmpWidg2.getWidgetName();
				//MetadataEditor tmpEditor = new MetadataEditor();
				Desktop d = Desktop.get();
				//PreviewWindow _newWin = new PreviewWindow("MethodPreviewWindow","Preview Method");
				d.show("MetadataEditor", true);
				//tmpEditor.
				
			}

			
			
		});

	}
	@Override
	public void setData(ArrayList<String> someList) {
		// TODO Auto-generated method stub
		
	}

	private final void hideStudyDetails()
	{
		this.getFormWidgetForName(pojoStudy.DicomModalityField).setVisible(false);
		this.getFormWidgetForName(pojoStudy.StudyTypeField).setVisible(false);
		this.getFormWidgetForName(pojoStudy.STUDY_METADATA).setVisible(false);
		//OK Big hack
		this._formItems.getWidget(6).setVisible(false);
		this._formItems.getWidget(7).setVisible(false);
		this._formItems.getWidget(8).setVisible(false);
		this._formItems.getWidget(9).setVisible(false);
		this._formItems.getWidget(12).setVisible(false);
		this._formItems.getWidget(13).setVisible(false);
		
		
	}
	private final void showStudyDetails()
	{
		this.getFormWidgetForName(pojoStudy.DicomModalityField).setVisible(true);
		this.getFormWidgetForName(pojoStudy.StudyTypeField).setVisible(true);
		this.getFormWidgetForName(pojoStudy.STUDY_METADATA).setVisible(true);
		this._formItems.getWidget(6).setVisible(true);
		this._formItems.getWidget(7).setVisible(true);
		this._formItems.getWidget(8).setVisible(true);
		this._formItems.getWidget(9).setVisible(true);
		this._formItems.getWidget(12).setVisible(true);
		this._formItems.getWidget(13).setVisible(true);
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

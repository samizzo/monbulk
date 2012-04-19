package daris.Monbulk.Desktop.Services;

import java.util.Arrays;

import daris.Monbulk.shared.Services.*;

// NOTE: The monbulk services would actually talk to the monbulk back end, whatever that may be.
public class MonbulkMetadataService extends MetadataService
{
	// Returns a list of all available metadata types.
	public void getMetadataTypes(GetMetadataTypesHandler handler)
	{
		// Return test data.
		String[] testTypes =
			{
				"asset",
			    "hfi-bruker-series",
			    "hfi-bruker-study",
			    "hfi.pssd.EAE.microscopy",
			    "hfi.pssd.EAE.optic-nerve.removal",
			    "hfi.pssd.EAE.optic-nerve.section",
			    "hfi.pssd.EAE.perfusion",
			    "hfi.pssd.EAE.stain",
			    "hfi.pssd.ImageHD.combined",
			    "hfi.pssd.PET.study",
			    "hfi.pssd.anaesthetic",
			    "hfi.pssd.animal.disease",
			    "hfi.pssd.animal.genetics",
			    "hfi.pssd.animal.kill",
			    "hfi.pssd.animal.modified-genetics",
			    "hfi.pssd.animal.subject",
			    "hfi.pssd.dicom.series",
			    "hfi.pssd.ethics",
			    "hfi.pssd.human.contact",
			    "hfi.pssd.human.education",
			    "hfi.pssd.human.identity",
			    "hfi.pssd.human.name",
			    "hfi.pssd.human.subject",
			    "hfi.pssd.identity",
			    "hfi.pssd.project",
			    "hfi.pssd.recovery",
			    "hfi.pssd.subject",
			    "hfi.pssd.subject.exclusion",
			    "hfi.pssd.time-point",
			    "mf-dicom-patient",
			    "mf-dicom-prefs",
			    "mf-dicom-project",
			    "mf-dicom-series",
			    "mf-dicom-study",
			    "mf-dicom-subject",
			    "mf-document",
			    "mf-forum",
			    "mf-forum-participant",
			    "mf-forum-topic",
			    "mf-forum-topic-post",
			    "mf-geo-pdf",
			    "mf-image",
			    "mf-image-exif",
			    "mf-image-iptc",
			    "mf-keyword",
			    "mf-name",
			    "mf-note",
			    "mf-revision-history",
			    "mf-snapshot-account",
			    "mf-snapshot-file",
			    "mf-system-user",
			    "mf-user",
			    "pssd-acquisition",
			    "pssd-dataset",
			    "pssd-derivation",
			    "pssd-dicom-server-registry",
			    "pssd-ex-method",
			    "pssd-method",
			    "pssd-method-rsubject",
			    "pssd-method-subject",
			    "pssd-notification",
			    "pssd-object",
			    "pssd-project",
			    "pssd-project-harvest",
			    "pssd-repository-description",
			    "pssd-role-member-registry",
			    "pssd-study",
			    "pssd-subject",
			    "pssd-transform",
			};
		handler.onGetMetadataTypes(Arrays.asList(testTypes));
	}
	
	// Returns a specific metadata type.
	public void getMetadata(String name, GetMetadataHandler handler)
	{
		handler.onGetMetadata(new Metadata("name", "description", "label"));
	}
	
	// Returns whether the named metadata exists.
	public void metadataExists(String name, MetadataExistsHandler handler)
	{
		handler.onMetadataExists(name, true);
	}
	
	public void createMetadata(String name, CreateMetadataHandler handler)
	{
		Metadata m = new Metadata(name, "", "");
		handler.onCreateMetadata(m, true);
	}
	
	public void destroyMetadata(String name, DestroyMetadataHandler handler)
	{
		handler.onDestroyMetadata(name, true);
	}
}

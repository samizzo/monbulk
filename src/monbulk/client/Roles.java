package monbulk.client;

public class Roles
{
	public static class MethodBuilder
	{
		public static final String CREATE = "monbulk.method-builder.create";
		public static final String READONLY = "monbulk.method-builder.read-only";
		public static final String ADMIN = "monbulk.method-builder.admin";
	}
	
	public static class MetadataEditor
	{
		public static final String CREATE = "monbulk.metadata-editor.create";
		public static final String READONLY = "monbulk.metadata-editor.read-only";
		public static final String ADMIN = "monbulk.metadata-editor.admin";
	}
}

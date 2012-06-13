# ============================================================================
# proc: buildSubjecttypes
# args: none
# description: Builds the metadata item for describing subject properties - used for extensibility and search.
# ============================================================================
proc buildSubjectTypes { dictionary} {
		asset.doc.type.update \
		    :create true \
		    :type mbi.subject.properties \
		    :label "Subject Properties" \
		    :description "Document type for subject properties when added to a method" \
		    :generated-by application \
		    :tag "Subject" \
		    :definition < \
		       :element -name subjectName  -type string -min-occurs 1 -max-occurs 1 -label "Subject Name" \
		       :element -name subjectType -type enumeration  -dictionary $dictionary \
		       		-min-occurs 0 -max-occurs 1 -label "Subject Type" \
		     > \
	
}

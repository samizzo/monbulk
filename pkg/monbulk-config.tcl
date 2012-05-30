#
# monbulk-config.tcl
#

source dictionary.tcl

# Create the dictionary.  This will only create it if it doesn't already exist.
createDictionary monbulk.settings

# Add default_namespace setting.
addDictionaryEntry monbulk.settings "default_namespace" "mbi"

source roles.tcl

# Create the monbulk roles.

# metadata editor roles
createRole monbulk.metadata-editor.create
createRole monbulk.metadata-editor.read-only
createRole monbulk.metadata-editor.admin

# method builder roles
createRole monbulk.method-builder.create
createRole monbulk.method-builder.read-only
createRole monbulk.method-builder.admin


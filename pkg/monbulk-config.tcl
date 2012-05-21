#
# monbulk-config.tcl
#

source dictionary.tcl

# Create the dictionary.  This will only create it if it doesn't already exist.
createDictionary monbulk.settings

# Add default_namespace setting.
addDictionaryEntry monbulk.settings "default_namespace" "mbi"

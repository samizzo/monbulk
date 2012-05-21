# ============================================================================
# proc: createDictionary
# args: dictionary - the dictionary to create.
# description: create a dictionary.
# ============================================================================
proc createDictionary { dictionary } {

        if { [xvalue exists [dictionary.exists :name $dictionary]] == "false" } {
                dictionary.create :name $dictionary :case-sensitive true
        }

}

# ============================================================================
# proc: destroyDictionary
# args: dictionary - the dictionary to destroy.
# description: destroy a dictionary.
# ============================================================================
proc destroyDictionary { dictionary } {

        if { [xvalue exists [dictionary.exists :name $dictionary]] == "true" } {
                dictionary.destroy :name $dictionary
        }

}

# ============================================================================
# proc: addDictionaryEntry
# args: dictionary - the dictionary name.
#       term - the term to add
#       definition - the definition to add. (optional)
# description: add a dictionary entry. Add only if the term does not exist.
# ============================================================================
proc addDictionaryEntry { dictionary term { definition 0 } } {

        if { [lsearch [xvalues term [dictionary.entries.list :dictionary $dictionary]] $term] == -1 } {
                if { $definition != 0 } {
                        dictionary.entry.add :dictionary $dictionary :term $term :definition $definition
                } else {
                        dictionary.entry.add :dictionary $dictionary :term $term
                }
        }

}

# ============================================================================
# proc: removeDictionaryEntry
# args: dictionary - the dictionary name.
#       term - the term to remove
#       definition - the definition to remove. (optional)
# description: remove a dictionary entry.
# ============================================================================
proc removeDictionaryEntry { dictionary term { definition 0 } } {

        if { [xvalue exists [dictionary.contains :dictionary $dictionary :term $term]] != "true" } {
                if { $definition != 0 } {
                        dictionary.entry.remove :dictionary $dictionary :term $term :definition $definition
                } else {
                        dictionary.entry.remove :dictionary $dictionary :term $term
                }
        }

}


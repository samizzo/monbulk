# ============================================================================
# proc: createRole
# args: role - the role to create
# description: create a new role
# ============================================================================
proc createRole { role } {

        if { [xvalue exists [authorization.role.exists :role $role]] == "false" } {
                authorization.role.create :role $role
        }

}

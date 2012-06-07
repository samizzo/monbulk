!!! NOTE !!!
------------

Don't forget to bump the app version number in build.properties when deploying
a new version of the app!

The monbulk build.properties is in:

monbulk\build.properties


SVN
---

The svn repository for Monbulk is at:

http://medimage.versi.edu.au/repos/daris-monbulk

The Monbulk project lives in trunk/Monbulk.


Monbulk
-------

The Monbulk project comprises the method builder and the metadata editor.
Monbulk is its own lightweight desktop environment that provides access to
these two editors.  Monbulk talks to a MediaFlux instance; thus to log in
to the Monbulk environment you use the usual MediaFlux or Daris login.


Building and deploying
----------------------

In the Monbulk directory, trunk/Monbulk, there are two files:

	build.xml
	build.properties

These are 'ant' build files.  build.properties specifies the server settings,
and build.xml specifies the ant build targets and rules.

Running ant in this directory will build, package, and deploy Monbulk.  To build
the default target:

	ant

This will do a full clean and build and deploy to a MediaFlux instance sitting
on the localhost.  To build and deploy to the Versi MediaFlux instance use:

	ant deploy-medimage

The Versi MediaFlux server is on:

	medimage.versi.edu.au

Note that you can only ssh to this machine from within the Versi network (e.g.
via vpn or from the Versi offices).

On this machine, the MediaFlux server is run from:

	~aglenn/dev/mflux_srv

Monbulk (and any other MediaFlux packages, e.g. Daris) is deployed to:

	~aglenn/dev/mflux_srv/ext/packages

Monbulk is accessed via:

http://medimage.versi.edu.au:443/monbulk/Monbulk.html

Note that after deploying you may have to force a refresh in your browser to
ensure you get the latest files.

In order to deploy to any MediaFlux machine, use the 'deploy' target from the
build file:

	ant deploy

This will prompt you for the server settings (name, port, transport) and the
user to install as (domain, username, password).

Settings and roles
------------------

Deploying to a server will also install the monbulk settings dictionary and
create the monbulk roles which are used to limit access.  These are installed
in the pkg/monbulk-config.tcl script.  This script is run from pkg/__install.tcl
which is run by MediaFlux when a new package is installed.

The roles are as follows:

Metadata editor:
	- monbulk.metadata-editor.create
	- monbulk.metadata-editor.read-only
	- monbulk.metadata-editor.admin

Method builder:
	- monbulk.method-builder.create
	- monbulk.method-builder.read-only
	- monbulk.method-builder.admin

'Create' access means a user can create new methods or metadata, edit existing
methods or metadata, but not remove methods or metadata.

'Read-only' access means a user can only view methods and metadata and not
create or make any changes.

'Admin' access means a user can do everything a user with 'Create' access can
do, with the addition of being able to remove methods and metadata.


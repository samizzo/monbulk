Monbulk
=======

Monbulk is a lightweight desktop environment that provides a way to easily
create plugin tools to access MediaFlux via the ajax services a MediaFlux
server exposes.

The following plugins currently exist:

	- Method builder
	- Metadata editor

The following MediaFlux services are currently exposed:

	- Metadata (also known as doc types)
	- Dictionaries
	- Methods
	- Users

Building and deploying
======================

Don't forget to bump the app version number in build.properties when deploying
a new version of the app!

The monbulk build.properties is in:

	monbulk\build.properties

In the Monbulk root directory there are two files:

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
==================

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

ADDED: 21/6/2012
A default metadata item to store subject properties has been created. This is also set in the Settings file and 
installed on Deployment

Services
========

The service interface is a convenient way to access the MediaFlux services.
Initially it was planned to write it in a generic way such that different
implementations of the services could be written and installed, so that,
for example, the Monbulk plugins could talk to an xnat back end instead (by
creating xnat versions of all services).  However, this probably won't happen.

Services implement the iService interface, and each individual service
provides a specific set of functionality.  Generally the API for each service
requires the caller to pass in a callback handler which will be called when
the service finishes executing (e.g. when retrieving an individual metadata,
the handler will call the callback which will be passed the new Metadata
object).

Services are registered in the service registry.  Client code can use the
registry to get an instance of a specific kind of service.  Additionally,
each service provides a static helped method to get an instance of itself
from the registry.

All services are registered in the main Monbulk entry point.

Metadata service
----------------

The metadata service API provides functionality to:

	- retrieve existing metadata
	- check if a metadata exists
	- retrieve a list of all metadata names
	- create a new metadata
	- destroy an existing metadata
	- update an existing metadata
	- rename an existing metadata

Dictionary service
------------------

The dictionary service API provides functionality to:

	- retrieve an existing dictionary
	- check if a dictionary exists
	- retrieve a list of all dictionary names
	- destroy an existing dictionary
	- create a new dictionary
	- add entries to an existing dictionary

Method service
--------------

The method service provides functionality to:

	- retrieve a list of all method names
	- retrieve an existing method
	- check if a method exists
	- create and update existing methods
	- Delete an existing method
	- Clone a Method 

User service
------------

The user service provides functionality to:

	- retrieve info about an existing user

The user service is used to check user roles in order to limit access to
certain features (depending on the available roles).

Desktop
=======

The Monbulk desktop is created in the Monbulk.java file.  This file also
registers the available services, shows the login screen, sets up the user,
and loads the Monbulk settings.  Monbulk settings are stored in a dictionary
called 'monbulk.settings' on the MediaFlux server (currently the only setting
that exists is the namespace setting).

Monbulk plugins implement the IWindow interface and are registered in
Monbulk.java into the single Desktop instance.  The plugins are wrapped
into an appletWindow instance which extends the GWT DialogBox class.
appletWindow provides plugins with a resizeable window with a title caption,
maximise and close buttons, and window decorations.

Windows are identified by a string id (e.g. "MetadataEditor").

Note that in Desktop.java there is a private class called Window which
ideally should be refactored into appletWindow (it's a bit messy at the
moment).

The Desktop class creates a launcher button for each plugin that requires
one.  Note that not all windows that are registered in the Desktop have
a launcher button.  For example, there are some windows that can be shown
as popup windows and thus do not require a button on the deskto (e.g. the
metadata select window, which shows a list of available metadata that the
user can select from).  Some plugins register extra windows themselves; for
example the metadata editor has a widget of type MetadataProperties, which
registers an ElementEditor window itself (the ElementEditor window is an
IWindow).

The Desktop instance provides functionality for operating with an IWindow,
such as:

	- showing and hiding
	- setting the window title
	- retrieving an instance of a specific window
	- registering new windows
	- resizing and maximising windows (in conjunction with appletWindow)
	- bringing windows to the front of the z order

OkCancelWindow
--------------

There is a base class called OkCancelWindow which is an IWindow.  This is a
convenience base class that provides a content panel for adding widgets to,
along with 'Ok' and 'Cancel' buttons.  A validator callback can also be
registered, which can be used to stop the window from being hidden when a
user presses the 'Ok' button.

This is currently used for some popup windows such as the metadata select
window (MetadataSelectWindow.java), the metadata element properties popup
editor (ElementEditor.java), and some popup windows in the method builder.


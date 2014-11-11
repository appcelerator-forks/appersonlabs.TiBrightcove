# TiBrightcove Module

## Quick Start

[![gitTio](http://gitt.io/badge.png)](http://gitt.io/component/com.appersonlabs.tibrightcove)

Download the latest distribution ZIP-file or install using the [gitTio CLI](http://gitt.io/cli):

`$ gittio install com.appersonlabs.tibrightcove`

## API

This document follows these conventions:

* Text in `code font` refer to module objects.  For example, player is a generic term
  but `player` refers to the TiBrightcove video player object.
* Object functions are listed with parentheses and properties without.  Constants are
  implemented as read-only properties.
* All function parameters are required unless they are specifically marked *optional*.

### Module

Load the TiBrightcove module using the built-in `require()` function:

    var tibrightcove = require('com.appersonlabs.tibrightcove');

You may load the module in each JavaScript file where it is used, or load it once in your
`app.js` or `alloy.js` file and assign it to a global variable.

### Video

`Video` objects represent single video files in your Brightcove Video Cloud account.
A `Video` is a container that aggregates metadata about the video content, zero or
more sources of video data, and an optional collection of cue points describing
locations in the video.

You cannot create new `Video` objects using this module.  You can fetch individual
`Video` objects using the methods on the `Catalog` object or get a list of `Video`
objects from a `Playlist` object.  Once you have a `Video` object,
you can access its properties or provide it to a 'Player' view object for playback.

#### Properties

**properties**

(object, read-only) The properties of the video.  Properties will vary, but usually
include `foo`, `bar`, **TODO get list of common properties** 

**cuePoints**

(array of `CuePoint` objects, read-only) Cue points associated with the video.  If
there are no cue points, an empty array will be returned.

**sources**

(array of ??, read-only) List of source data locations where the video data can be
fetched.  Normally, you won't need to access source data directly; the `Player`
object will fetch the sources in the correct order for playback.

### Catalog

The `Catalog` object provides methods for fetching videos and playlists from your
Brightcove Video Cloud account.  The Brightcove Media API makes asynchronous requests
to get media data, so all of the methods in this object take a callback function
which is run when the request is complete.  The callback function has a single
parameter, an object containing the following properties:

* **code** (number) the error code, if there was an error in the method call
* **error** (string) a text description of the error, if there was an error in the
method call.
* **result** (object) the result of the method call. Usually this is a `Playlist`
or `Video` object.
* **success** (boolean) true if the method call succeeded.

`Catalog` objects are created from the module object as follows:

    var tibrightcove = require('com.appersonlabs.tibrightcove');
    var catalog = tibrightcove.createCatalog({ token: 'my-brightcove-api-token' });

The `token` property in the creation dictionary is required.  You can get your
Brightcove API token from **TODO token URL**.

Here's a simple example of how you can fetch a video using the catalog:

    // "player" is a Player object
    catalog.findVideoWithVideoID('34643423452345', function(e) {
        if (e.success) {
            player.video = e.result;
        }
        else {
            Ti.API.error(e.error);
        }
    });

**TODO explain reference ID vs video/playlist ID**

#### Methods

**findPlaylistWithPlaylistID(playlistID, callback, options)**

* `playlistID` (string) the Brightcove Video Cloud ID of the playlist to fetch.
* `callback` (function) a callback function that is executed by the module when the
  request to the Brightcove Media API has completed.
* `options` (object, optional) additional string parameters to add to the Brightcove
  Media API request.

**findPlaylistWithReferenceID(referenceID, callback, options)**

* `referenceID` (string) the Brightcove Video Cloud reference ID of the playlist to fetch.
* `callback` (function) a callback function that is executed by the module when the
  request to the Brightcove Media API has completed.
* `options` (object, optional) additional string parameters to add to the Brightcove
  Media API request.

**findVideoWithVideoID(videoID, callback, options)**

* `videoID` (string) the Brightcove Video Cloud ID of the video to fetch.
* `callback` (function) a callback function that is executed by the module when the
  request to the Brightcove Media API has completed.
* `options` (object, optional) additional string parameters to add to the Brightcove
  Media API request.

**findVideoWithReferenceID(referenceID, callback, options)**

* `referenceID` (string) the Brightcove Video Cloud reference ID of the video to fetch.
* `callback` (function) a callback function that is executed by the module when the
  request to the Brightcove Media API has completed.
* `options` (object, optional) additional string parameters to add to the Brightcove
  Media API request.

#### Callback Functions

The callback functions are executed when the asynchronous call to the Media API is completed.
The function receives a single argument: an object which contains the following properties:

* `source` (Catalog object, required): the Catalog object that was used to make the request
* `type` (String, required): either "success" for successful requests or "error" if the request
  was not successful
* `result` (Playlist or Video object): if the call was successful, the `result` property will
  contain the playlist or video requested from the server; otherwise, undefined.
* `code` (number): the error code if the call was not successful; otherwise undefined.
* `error` (string): the error description if the call was not successful; otherwise undefined.


### Player

### Playlist



## Callbacks

code
error
result
success
var brightcove = require('com.appersonlabs.brightcove');


var catalog = brightcove.createCatalog({
  token: 'RNj-yS616_T1GQ4znMqS3ut3ijXuGrG69w3oYJBMVahURwi7P4ZH4Q..',
});

var player = brightcove.createPlayerView({
  top: 20,
  left: 20,
  width: 200,
  height: 200,
});

var playButton = Ti.UI.createButton({
  systemButton: Ti.UI.iPhone.SystemButton.PLAY,
});
playButton.addEventListener('click', function(e) {
  player.play();
});

var pauseButton = Ti.UI.createButton({
  systemButton: Ti.UI.iPhone.SystemButton.PAUSE,
});
pauseButton.addEventListener('click', function(e) {
  player.pause();
});

var advanceButton = Ti.UI.createButton({
  systemButton: Ti.UI.iPhone.SystemButton.FAST_FORWARD,
});
advanceButton.addEventListener('click', function(e) {
  player.advanceToNext();
});

var toolbar = Ti.UI.iOS.createToolbar({
  items: [playButton, pauseButton, advanceButton],
  bottom: 0,
  borderTop: true,
  borderBottom: false,
});

var videoButton = Ti.UI.createButton({
  bottom: 48,
  title: 'Load Video',
});
videoButton.addEventListener('click', function() {
  catalog.findVideoWithVideoID('3873079666001', function(e) {
    if (e.type === 'success') {
      player.video = e.result;
      dump_video_to_log(e.result);
    }
    else {
      Ti.API.error(e.error);
    }
  })
});

var win = Ti.UI.createWindow({
	backgroundColor:'white'
});

win.add(player);
win.add(toolbar);
win.add(videoButton);

win.addEventListener('open', function(e) {
  catalog.findPlaylistWithPlaylistID('3868842075001', function(e) {
    Ti.API.info(JSON.stringify(e));
    if (e.type === 'success') {
      player.playlist = e.result;
      
      // log properties
      var playlist = e.result;
      Ti.API.info("playlist properties"+JSON.stringify(playlist.properties));
      for (i in playlist.videos) {
        dump_video_to_log(playlist.videos[i]);
      }
      for (i in playlist.cuePoints) {
        dump_cue_point_to_log(playlist.cuePoints[i]);
      }
    }
    else {
      Ti.API.error(e.error);
    }
  })
});

win.open();

function dump_video_to_log(video) {
  // log informtion about the video
  Ti.API.info("video properties: "+JSON.stringify(video.properties));
  for (var i in video.sources) {
    var source = video.sources[i];
    Ti.API.info("source "+i);
    Ti.API.info("  url: "+source.url);
    Ti.API.info("  deliveryMethod: "+source.deliveryMethod);
    Ti.API.info("  properties: "+JSON.stringify(source.properties));
  }
}

function dump_cue_point_to_log(cuePoint) {
  // log informtion about the video
  Ti.API.info("  position: "+cuePoint.position);
  Ti.API.info("  type: "+cuePoint.type);
  Ti.API.info("  properties: "+JSON.stringify(cuePoint.properties));
}

var brightcove = require('com.appersonlabs.brightcove');

var videos = {
  "sample1": "3868942966001",
  "sample2": "3868587647001",
  "agility": "3873079666001"
};

var catalog = brightcove.createCatalog({
  token: 'RNj-yS616_T1GQ4znMqS3ut3ijXuGrG69w3oYJBMVahURwi7P4ZH4Q..',
});

var player = brightcove.createPlayerView({
  top: 20,
  left: 20,
  width: 200,
  height: 200,
});
player.addEventListener('play', function(e) {
  Ti.API.info("play event received");
});
player.addEventListener('pause', function(e) {
  Ti.API.info("pause event received");
});
player.addEventListener('ready', function(e) {
  Ti.API.info("ready event received");
});
player.addEventListener('end', function(e) {
  Ti.API.info("end event received");
});

var playButton = Ti.UI.createButton({
  title: "Play",
});
playButton.addEventListener('click', function(e) {
  player.play();
});

var pauseButton = Ti.UI.createButton({
  title: "Pause",
});
pauseButton.addEventListener('click', function(e) {
  player.pause();
});

var advanceButton = Ti.UI.createButton({
  title: "Advance",
});
advanceButton.addEventListener('click', function(e) {
  player.advanceToNext();
});


function makeVideoButton(id, number) {
  var result = Ti.UI.createButton({
    title: 'Video '+number,
  });
  result.addEventListener('click', function() {
    catalog.findVideoWithVideoID(id, function(e) {
      if (e.type === 'success') {
        player.video = e.result;
        dump_video_to_log(e.result);
      }
      else {
        Ti.API.error(e.error);
      }
    });
  });
  return result;
}

var playlistButton = Ti.UI.createButton({
  title: 'Playlist',
});
playlistButton.addEventListener('click', function() {
  catalog.findPlaylistWithPlaylistID('3868842075001', function(e) {
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
  });
});

var toolbar = Ti.UI.createView({
  layout: "horizontal",
  bottom: 0,
  height: Ti.UI.SIZE,
});

toolbar.add(playButton);
toolbar.add(pauseButton);
toolbar.add(advanceButton);
toolbar.add(makeVideoButton(videos.sample1, '1'));
toolbar.add(makeVideoButton(videos.sample2, '2'));
toolbar.add(makeVideoButton(videos.agility, '3'));
toolbar.add(playlistButton);


var win = Ti.UI.createWindow({
	backgroundColor:'white'
});

win.add(player);
win.add(toolbar);

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

var brightcove = require('com.appersonlabs.brightcove');

var win = Ti.UI.createWindow({
	backgroundColor:'white'
});

var player = brightcove.createPlayerView();
win.add(player);

win.open();

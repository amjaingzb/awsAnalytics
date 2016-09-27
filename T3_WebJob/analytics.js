var express = require('express');
var app = express();
//var app = require('express').createServer();
var fs  = require('fs') ;

var myRes1 = function (req, res) {
    res.send('Hello Duniya!');
};
var myRes2 = function (req, res) {
      fs.readFile('data.json', 'utf8', function (err,data) {
          if (err) { return console.log(err); }
//          console.log(data);
//          fs.readFile('analytics.html','utf8',function(err, htmlTemplate){
          fs.readFile('bar.html','utf8',function(err, htmlTemplate){
            if (err) { return console.log(err); }
            htmlTemplate = htmlTemplate.replace("<!--CHARTDATA-->", data.trim());
            res.send(htmlTemplate);
          });
      });
};
app.get('/',myRes1);
app.get('/ankit',myRes2);
/*
var fProcessPostFromYearForm = function(req, res) {
  // Prepare output in JSON format
  response = {
    startYear:req.body.startYear,
    endYear:req.body.endYear
  };
  console.log(response);
  res.end(JSON.stringify(response));
};
app.post('/process_yearRange', fProcessPostFromYearForm);
*/

httpServer = app.listen(3000, function () {
    console.log('Analytics app listening on port 3000!');
});

var io  = require('socket.io')(httpServer);

io.on('connection', function(socket){
  console.log('a user connected');
  socket.on('disconnect', function(){
    console.log('user disconnected');
  });
  socket.on('graphChangeMsg', function(msg){
    console.log('message: ' + msg);
  });
});

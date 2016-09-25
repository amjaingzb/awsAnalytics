var express = require('express');
var app = express();

var myRes1 = function (req, res) {
    res.send('Hello Duniya!');
};
var myRes2 = function (req, res) {
//    res.send('Hello Ankit!');
      fs = require('fs')
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
app.listen(3000, function () {
    console.log('Example app listening on port 3000!');
});

const express = require('express')
const app = express()
const path = require('path')
const PORT = process.env.PORT || 5000

var admin = require('firebase-admin');
var serviceAccount = require('./ucla-cs117-fall2018-firebase-adminsdk-ma6gj-465d58bfd1.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: 'https://ucla-cs117-fall2018.firebaseio.com'
});

app.get('/', function(req, res){
	var db = admin.database();
	var user = req.query.user;
	var userRef = db.ref(user);

	var location_latitude = req.query.latitude;
	var location_longitude = req.query.longitude;

	userRef.push().set(String(Date.now()) + "/" + String(location_latitude) + "/" + String(location_longitude));
	res.send("finished")
});
  
app.listen(PORT, () => console.log(`Listening on ${ PORT }`));
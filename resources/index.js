// set up Expresss

var express = require('express');

var app = express();

// set up EJS

app.set('view engine', 'ejs');

// set up BodyParser

var bodyParser = require('body-parser');
app.use(bodyParser.urlencoded({ extended: true }));

console.log("hello hello");


// import the Person class from Person.js

var User = require('./Person.js');

/***************************************/



// route for creating a new person

// this is the action of the "create new user" form

app.post('/create', (req, res) => {
	console.log("in create");
	// construct the User from the form data which is in the request body
	console.log(req.body);

	var resource = new User ({
		summary: req.body.summary,
		category: req.body.category,
		address: req.body.address,
		website: req.body.website,
		lat: req.body.lat,
		lon: req.body.lon,
		phonenumber: req.body.phonenumber,
		isSpotify: req.body.isSpotify,
		name: req.body.name,
	});

	// save the person to the database

	resource.save( (err) => { 

		if (err) {
			res.type('html').status(200);
			res.write('uh oh: ' + err);
			console.log(err);
			res.end();
		}
		else {
		    // display the "successfull created" page using EJS
		    console.log("hello hello");
		    res.render('created', {person : resource});
		}
	} ); 
}
);

// deleted jazz


app.use('/delete', (req, res) => {
	console.log("delete called" + req.body.name);

	User.deleteOne({
		name: req.body.name
	}, function (err) {
		if (err) {
			console.log("err" + err);
			return res.end();
		} else {
			console.log("successful");
			return res.redirect("/all");
		}
	})
});

app.use('/person', (req, res) => {
	var name = req.query.name;
	console.log(req.query);
	User.findOne( {name : name}, (err, person) => {
		if (err) {
			res.type('html').status(500);
			res.write('uh oh: ' + err);
			console.log(err);
			res.end();
		} else if (!person) {
			res.type('html').status(200);
			res.write('uh oh: ' + err);
			console.log(err);
			res.end();
		} else {
			res.render('personInfo', { person : person});
		}
	});
});

app.use('/update', (req, res) => {
	var name = req.body.name;
	console.log(name + " in update");
	User.findOne( {name : name}, (err, person) => {
		if (err) {
			res.type('html').status(500);
			res.write('uh oh: ' + err);
			console.log(err);
			res.end();
		} else if (!person) {
			res.type('html').status(200);
			res.write('uh oh: ' + err);
			console.log(err);
			res.end();
		} else {
			person.summary = req.body.summary;
			person.category = req.body.category;
			person.address = req.body.address;
			person.website = req.body.website;
			person.lat = req.body.lat;
			person.lon = req.body.lon;
			person.phonenumber = req.body.phonenumber;
			person.isSpotify = req.body.isSpotify;
			person.name = req.body.name;
			person.save( (err) => { 

		        if (err) {
			        res.type('html').status(200);
			        res.write('uh oh: ' + err);
			   		console.log(err);
			    	res.end();
		   		}
		   		else {
		       		console.log("hello hello");
		        	res.render('updated', {person : person});
		   		}
	        } ); 
		}
	});
});



// route for showing all the users

app.use('/all', (req, res) => {
	console.log("in all");

	// find all the User objects in the database

	User.find( {}, (err, persons) => {

		if (err) {

			res.type('html').status(200);
			console.log('uh oh' + err);
			res.write(err);

		}

		else {
			if (persons.length == 0) {
				res.type('html').status(200);
				res.write('There are no people');
				res.end();
				return;
			}

		    // use EJS to show all the users
		    res.render('all', { persons: persons });
		}

	}); 

});



// route for accessing data via the web api
// to use this, make a request for /api to get an array of all Person objects
// or /api?name=[whatever] to get a single object

app.use('/api', (req, res) => {

	console.log("LOOKING FOR SOMETHING?");
	// construct the query object
	var queryObject = {};

	if (req.query.name) {
	    // if there's a name in the query parameter, use it here
	    queryObject = { "name" : req.query.name };
	}

	User.find( queryObject, (err, persons) => {

		console.log(persons);

		if (err) {

			console.log('uh oh' + err);

			res.json({});

		}

		else if (persons.length == 0) {

		    // no objects found, so send back empty json

		    res.json({});

		}

		else if (persons.length == 1 ) {
			var person = persons[0];
		    // send back a single JSON object
		    res.json( { "name" : person.name , "summary" : person.summary, "category" : person.category, "address" : person.address,
		"website" : person.website, "lat" : person.lat, "lon" : person.lon, "phonenumber" : person.phonenumber, "isSpotify" : person.isSpotify} );
		}
		else {
		    // construct an array out of the result
		    var returnArray = [];
		    persons.forEach( (person) => {
		    	returnArray.json( { "name" : person.name , "summary" : person.summary, "category" : person.category, "address" : person.address,
		"website" : person.website, "lat" : person.lat, "lon" : person.lon, "phonenumber" : person.phonenumber, "isSpotify" : person.isSpotify} );
		    });
		    // send it back as JSON Array
		    res.json(returnArray); 
		}
	});

});

/*************************************************/



app.use('/public', express.static('public'));

app.use('/', (req, res) => { res.redirect('/public/personform.html'); } );



app.listen(3003,  () => {

	console.log('Listening on port 3002');

});
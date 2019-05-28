var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb+srv://jackgoettle:jackgoettle@cluster0-aj7k8.mongodb.net/test?retryWrites=true');


var Schema = mongoose.Schema;

var resourceSchema = new Schema({
	name: {type: String, required: true, unique: false},
	summary: {type: String, required: false, unique: false},
	category: {type: String, required: false, unique: false},
	address: {type: String, required: false, unique: false},
	website: {type: String, required: false, unique: false},
	lat: {type: String, required: false, unique: false},
	lon: {type: String, required: false, unique: false},
	phonenumber: {type: String, required: false, unique: false},
	isSpotify: {type: Boolean, required: false, unique: false}
    });

// export resourceSchema as a class called Person
module.exports = mongoose.model('Person', resourceSchema);

resourceSchema.methods.standardizeName = function() {
    this.name = this.name.toLowerCase();
    return this.name;
}

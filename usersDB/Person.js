var mongoose = require('mongoose');

// the host:port must match the location where you are running MongoDB
// the "myDatabase" part can be anything you like
mongoose.connect('mongodb+srv://jackgoettle1234:jackgoettle1234@cluster0-92jxt.mongodb.net/test?retryWrites=true');


var Schema = mongoose.Schema;

var personSchema = new Schema({
	name: {type: String, required: true, unique: false},
	username: {type: String, required: true, unique: true},
	password: {type: String, required: true, unique: false},
	accountNum: {type: String, required: true, unique: true},
	mentalHealth: {type: String, required: false, unique: false},
	stress: {type: String, required: false, unique: false},
	physicalHealth: {type: String, required: false, unique: false},
	community: {type: String, required: false, unique: false}
    });

// export personSchema as a class called Person
module.exports = mongoose.model('Person', personSchema);

personSchema.methods.standardizeName = function() {
    this.name = this.name.toLowerCase();
    return this.name;
}

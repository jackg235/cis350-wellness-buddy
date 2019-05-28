# WELLNESS BUDDY 
A personalized user-friendly app for connecting students and wellness resources at The University of Pennsylvania, using data stored in a database, and a web app for an administrator.

### Motivation
The University of Pennsylvania is working to improve the lives of students and the quality of their mental health resources. Students often feel like they are alone in their personal struggles and do not have anywhere to turn for help from the university besides a CAPS appointment with a specialist. However, at the end of the day, the university actually offers many services that remain heavily underutilized due to a lack of awareness from the students. Our app aims to facilitate a larger utilization of and greater impact for the pre-existing resources at Penn because though user customizability. While the benefits brought to the student user is self-evident, the services at Penn also stand to gain a lot from this application in regard to a more efficient use of resources.

### Features
We created an app that filters the mental health and mindfulness resources at Penn for different users’ needs, effectively streamlining the process of finding the proper assistance. Once the preferences are determined through a short survey, the student will receive a filtered selection of resources tailored to their specific needs in categories such as sleep improvement programs, meditation, counseling and support groups, and even music. 

We store and access the user and resource data in a database via a web-based RESTful API. We also implemented a web app console for the admin to add, delete and search both users and resources. 

### Usage: 
The main intended use for this app is for the average jo to receive a categorized, comprehensive and low-stress set of recourses at Penn and for admin to distribute resources at Penn. 

## Design 
The classes are sectioned into two categories: classes which represent a page and classes which are more backend. 
MainActivity is the login screen, CreateAccountActivity is for creating an account, SurveyActivity is for actually representing the survey and processing the responses while tempSurvey contains information about the survey itself (questions, responses, etc). HomeActivity is the screen the user is taken to after they log in with all of the resources/recommendations. DietPage, FitnessPage, CapsPage, etc. are all pages for a specific type of resource.
GriefReferralPage implements GoogleMaps, SpotifyPlayer implements Spotify. All of the pages use CurrentUser (which is a singleton) to keep track of the current user and their mental health value fields. ResourceDB is a singleton class which gets all of the resources from Mongo (via AsyncResourceClient). All of the AsyncClients handle some variety of interacting with Mongo: AsyncClient handles login, AsyncClientCheckForUser handles checking if a user is already in the database, AsyncCreateClient handles creating a new user and storing them in the database, AsyncResourceClient handles accessing the resource database on Mongo, Proc deals with calculating a user's recommended resources based on their responses to the survey, which is used by RecsPage to generate their recommended resources.

## Installation: 
User app: We found that Android studio was the most useful tool to build this app, running it on the Nexus 5 API 28 emulator to test. The Android app accessed database information using REST. 

Backend and admin web app: Our system uses MongoDB to store data and Node Express to serve dynamic web content.

The Node JS files for the resource database can be found in the resources file. The Node JS files for the user database can be found in the usersDB file. You will need to install node modules (see: https://www.tutorialsteacher.com/nodejs/what-is-node-package-manager) in order to run these files. 

To start the app, first, run Node JS on your terminal. In one terminal window, cd into the mongodb folder on your laptop and run the command `./bin/mongod --dbpath [path to db directory]` if you have Mac or `\bin\mongod --dbpath [path to db directory]` if you have a Windows laptop. Your terminal should state "waiting for connections on port 27017" Next open a second terminal window, and cd into your Node project and run the command: `node index.js`. You should see "Listening on Port 3000". However, in order to access the database, run "localhost:3001" to access the User Database and "localhost:3002" to access the Resource Database. 

`localhost:3001/all` – will list all the Persons in the database
`localhost:3001/public/personform.html` – contains the user creation form 
`localhost:3001/public/delete.html` – please input the username of the person you would like to delete and that person will be removed from the database

Next, open Android Studio and and click "Run" and enjoy the app!

### Built with
 * [Android Studio](https://developer.android.com/studio) 

 * [Node.js](https://nodejs.org/en/download/) for the JavaScript runtime environment
 * Express for handling basic web server functionality
 * BodyParser for parsing form data in a POST request
 * EJS for creating HTML templates
 * Mongoose for accessing the database
 * [MongoDB](https://mongodb.com/download) for storing data


## Support: 
For help with an issue or inquiries email carly6298@gmail.com.

## Roadmap and Collaboration: 
In the future, we would love to update the app with more resources and design a more advanced way of processing user profiles to make the recommendations page as useful of a tool as possible. We also would love to implement a way for users to give feedback to the resource centers. We are very open to contributions! We found that Android studio was the most useful tool to build this app, running it on the Nexus 5 API 28 emulator to test. 

## Authors: 
 * Jack Goettle, Eli Kalish, Anjali Maheshwari, Carolyn Ryan, Alex Wysoczanski. 
 * With support from Chris Murphy and Jason Wang. 

### Project status:
Development is at a pause but may begin up again in the future. 

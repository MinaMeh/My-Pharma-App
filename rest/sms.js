const accountSid = 'ACdbac13ce25985b732b9b8650ebb4408b';
	const authToken = '2f4dcc54bea07e37681276b8e79fd036';
const client = require('twilio')(accountSid, authToken);

client.messages
  .create({
     body: 'This is the ship that made the Kessel Run in fourteen parsecs?',
     from: '+16099644454',
     to: '+213557469181'
   })
  .then(message => console.log(message.sid));

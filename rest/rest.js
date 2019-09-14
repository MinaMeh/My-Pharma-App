var express = require("express");
var mysql = require("mysql");
var app = express();
var braintree = require('braintree');

var multer = require('multer')
var FCM = require('fcm-node');
var MySQLEvents = require('mysql-events');

var BosyParser= require("body-parser");
app.use(express.static('public'));
var gateway = require('./gateway');

app.use(BosyParser.json());
var fs = require('fs');
var path = require('path');
var crypto = require('crypto');
BT_ENVIRONMENT='sandbox'
BT_MERCHANT_ID='nyyd4m5j7tvwjrgh'
BT_PUBLIC_KEY='yspkxynyc3xw5dmk'
BT_PRIVATE_KEY='1ba145d7c99db14c4a68ca49cfe9b9d5'


//database connection
var connection = mysql.createConnection({
    host     : 'localhost',
    user     : 'root',
    password : 'rootroot',
    database:'MyPharma'
});
connection.connect();
var dsn = {
    host:     'localhost',
    user:     'root',
    password: 'rootroot',
  };
  

// server creation

var server = app.listen(8082,function(){
    var host = server.address().address
    var port = server.address().port
});
var storage = multer.diskStorage({
    destination: './uploads/',
    filename: function(req, file, cb) {
      cb(null, file.originalname)
    }
  });
    const upload = multer({dest: __dirname + '/public/uploads/images'});

  app.get('/getPharmacies',function(req,res){  
      var query = "select * from pharmacies ";
      connection.query(query,function(error,results){
        // rest service
    if (error) throw error;
    res.send(JSON.stringify(results));
})
});
app.get('/getVilles',function(req,res){  
    var query = "select * from villes ";
   connection.query(query,function(error,results){
    if (error) throw error;
    res.send(JSON.stringify(results));
})
});
app.get('/getPharmaciesParVille/:ville',function(req,res){  
    var query = "select * from pharmacies natural join villes where villes.nom =?";
   connection.query(query,[req.params.ville],function(error,results){
    if (error) throw error;
    res.send(JSON.stringify(results));
})
});


app.get('/getUserCommandes/:user',function(req,res){  
    var query = "select * from commandes  where user_id =?";
   connection.query(query,[req.params.user],function(error,results){
    if (error) throw error;
    res.send(JSON.stringify(results));
})
});
app.get('/getNbCommandes',function(req,res){  
    var query = "select count(*) as nb from commandes ";
   connection.query(query,function(error,results){
    if (error) throw error;
    res.send(JSON.stringify(results[0].nb));
})
});
app.get('/getCommandePharmacie/:commande',function(req,res){  
    var query = "select * from commandes natural join pharmacies   where commande_id =? LIMIT 1";
   connection.query(query,[req.params.commande],function(error,results){
    if (error) throw error;
    res.send(JSON.stringify(results[0]));
})
});
app.get('/getPharmacie/:id',function(req,res){  
    var query = "select * from pharmacies  where pharmacie_id =?";
   connection.query(query,[req.params.id],function(error,results){
    if (error) throw error;
    res.send(JSON.stringify(results[0]));
})
});
app.get('/getCommandeById/:commande',function(req,res){  
    var query = "select * from commandes  where commande_id =?";
   connection.query(query,[req.params.commande],function(error,results){
    if (error) throw error;
    res.send(JSON.stringify(results[0]));
})
});
app.get('/getUser/:telephone',function(req,res){  
    var query = "select * from users  where telephone =?";
   connection.query(query,[req.params.telephone],function(error,results){
    if (error) throw error;
    res.send(JSON.stringify(results));
})
});
app.get('/getUserById/:nss',function(req,res){  
    var query = "select * from users  where nss =?";
   connection.query(query,[req.params.nss],function(error,results){
    if (error) throw error;
    res.send(JSON.stringify(results[0]));
})
});

app.post('/addUser',function(req,res){
    var user= req.body
    var query = "insert  into users  values (?,?,?,?,?,?,?) ";
   connection.query(query,[user.nss, user.nom, user.prenom, user.adresse, user.telephone, user.password, user.device_token],function(error,results){
    if (error)     
    res.send(error);
    else
    res.send(JSON.stringify("success"));
})
});
app.post('/addCommande',function(req,res){
    var commande= req.body
    console.log(commande)

    var query = "insert  into commandes  values (?,?,?,?,?,?,?) ";
   connection.query(query,[commande.commande_id, commande.photo, commande.user_id, commande.pharmacie_id,commande.etat , commande.date, commande.amount],function(error,results){
    if (error)     
    res.send(error);
    else
    res.send(JSON.stringify("success"));


})
});
/*
app.post("/upload",
    multer({
      storage: storage
    }).single('upload'), function(req, res) {
      console.log(req.file);
      //console.log(req.body);
     // res.redirect("/uploads/" + req.file.filename);
      //console.log(req.file.filename);
      //return res.status(200).end();
    });
*/
app.post('/upload', upload.single('photo'), (req, res) => {
    file=req.file
    if(file) {
        console.log(file)
        //res.redirect("/uploads/" + file.filename);
        res.send(JSON.stringify(file.filename));
        console.log(file.filename)
        
    }
    else throw 'error';
});
    app.post('/sendsms',function(req,res){
    var user= req.body
    const accountSid = 'ACdbac13ce25985b732b9b8650ebb4408b';
	const authToken = '2f4dcc54bea07e37681276b8e79fd036';
	const client = require('twilio')(accountSid, authToken);

	client.messages.create({
     body: 'Votre code d\'activtion pour MyPharma est '+ user.password,
     from: '+16099644454',
     to: user.telephone
   })
  .then(message => console.log(message.sid));
  /*if (error)     
    res.send(JSON.stringify("erreur"));*/
   // else
    res.send(JSON.stringify("success"));
})

app.post('/updateToken',function(req,res){
    var user= req.body
    var query = "update users  set `device_token` =? WHERE `nss`= ? ";
   connection.query(query,[user.device_token, user.nss],function(error,results){
    if (error)     
    res.send(error);
    else
    res.send(JSON.stringify("success"));
})
});

app.post('/updatePassword',function(req,res){
    var newpass= req.body
    var query = "update users  set `password` =? WHERE `nss`= ? ";
   connection.query(query,[newpass.password, newpass.nss],function(error,results){
    if (error)     
    res.send(error);
    else
    res.send(JSON.stringify("success"));
})
});
app.post('/updateCommande',function(req,res){
  var commande= req.body
  var query = "update commandes  set `etat` =? WHERE `commande_id`= ? ";
 connection.query(query,[commande.etat, commande.commande_id],function(error,results){
  if (error)     
  res.send(error);
  else
  res.send(JSON.stringify("success"));
})
});
/*
var fcm = new FCM("AAAAES87tOU:APA91bE6bn0HerSym70NwggmvAjaTuT9a9Un-ZniZssHQ10l9AOe1v-o7dlBuQcAUFy7ZZHlk_xT5cCNKHtiSX3qcaxkDk2OCi_-tYx_VyU5Z9VhXx8DOOao3n50vpAAQkgUliwZ7oR1");

var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
   
   to:"dat2D9atQ-s:APA91bFCNb5eLKJblAsVeHhmj3ugfT02BuiasXY5j88BMTCqJ51JCEccTZ6BpaCoBALxkHiEpLCZvscLFzI3id8gYo4JYODJMVGp8PW6ZjBlaiqQsLV3-q2fQxo_OOZ9KgjYuscGCIxO",
    notification: {
        title: 'De la pharmacie: ', 
        body: 'La commande est traitée' 
    }

};

fcm.send(message,function(err, response){
    if (err) 
    {
        console.log("Something has gone wrong!",err);
    } 
    else 
    {
        console.log("Successfully sent with response: ");
     
     
    }
}); */
var mysqlEventWatcher = MySQLEvents(dsn);
var watcher =mysqlEventWatcher.add(
    'MyPharma.commandes',
    function (oldRow, newRow, event) {
       //row updated
      if (oldRow !== null && newRow !== null) {
          console.log(newRow.fields.etat)
        if (newRow.fields.etat==='T'){
            var token;
            var query = "select device_token from users where  nss=?";
            connection.query(query,[newRow.fields.user_id],function(error,results){
                if (error) { console.log(error);}
                else{
                    token=results[0].device_token.toString();
                    console.log(token);
                    var fcm = new FCM("AAAAES87tOU:APA91bE6bn0HerSym70NwggmvAjaTuT9a9Un-ZniZssHQ10l9AOe1v-o7dlBuQcAUFy7ZZHlk_xT5cCNKHtiSX3qcaxkDk2OCi_-tYx_VyU5Z9VhXx8DOOao3n50vpAAQkgUliwZ7oR1");

                    var message = { //this may vary according to the message type (single recipient, multicast, topic, et cetera)
                       
                          to: token,
                          notification: {
                            title: 'My Pharma ', 
                            body: 'Votre commande est traitée' 
                        }
                    
                    };
                    
                    fcm.send(message,function(err, response){
                        if (err) 
                        {
                            console.log("Something has gone wrong!",err);
                        } 
                        else 
                        {
                            console.log("Successfully sent with response: ");
                         
                         
                        }
                    });
                }
              });
          


        }
    }
   
      //detailed event information
      //console.log(event)
    }, 
    'match this string or regex'
  );
  


  var TRANSACTION_SUCCESS_STATUSES = [
    braintree.Transaction.Status.Authorizing,
    braintree.Transaction.Status.Authorized,
    braintree.Transaction.Status.Settled,
    braintree.Transaction.Status.Settling,
    braintree.Transaction.Status.SettlementConfirmed,
    braintree.Transaction.Status.SettlementPending,
    braintree.Transaction.Status.SubmittedForSettlement
  ];
  
  function formatErrors(errors) {
    var formattedErrors = '';
  
    for (var i in errors) { // eslint-disable-line no-inner-declarations, vars-on-top
      if (errors.hasOwnProperty(i)) {
        formattedErrors += 'Error: ' + errors[i].code + ': ' + errors[i].message + '\n';
      }
    }
  
    return formattedErrors;
  }
  
  function createResultObject(transaction) {
    var result;
    var status = transaction.status;
  
    if (TRANSACTION_SUCCESS_STATUSES.indexOf(status) !== -1) {
      result = {
        header: 'Sweet Success!',
        icon: 'success',
        message: 'Your test transaction has been successfully processed. See the Braintree API response and try again.'
      };
    } else {
      result = {
        header: 'Transaction Failed',
        icon: 'fail',
        message: 'Your test transaction has a status of ' + status + '. See the Braintree API response and try again.'
      };
    }
  
    return result;
  }
  
  app.get('/', function (req, res) {
    res.redirect('/checkouts/new');
  });


  // create token for client before payment

  app.get('/checkouts/new', function (req, res) {
    gateway.clientToken.generate({}, function (err, response) {
     res.send(JSON.stringify(response))
    });
  });
   
  app.get('/checkouts/:id', function (req, res) {
    var result;
    var transactionId = req.params.id;
  
    gateway.transaction.find(transactionId, function (err, transaction) {
      result = createResultObject(transaction);
      res.render('checkouts/show', {transaction: transaction, result: result});
    });
  });

  //process checkout

  app.post('/checkouts', function (req, res) {
    var transactionErrors;
    var amount = req.body.amount; // In production you should not take amounts directly from clients
    var nonce = req.body.payment_method_nonce;
    console.log(req.body)
  
    gateway.transaction.sale({
      amount: amount,
      paymentMethodNonce: nonce,
      options: {
        submitForSettlement: true
      }
    }, function (err, result) {
      if (result.success || result.transaction) {
        res.send(result)
      } else {
        transactionErrors = result.errors.deepErrors();
        //req.flash('error', {msg: formatErrors(transactionErrors)});
        //res.redirect('checkouts/new');
        res.send(formatErrors( transactionErrors ))
      }
    });
  });
  
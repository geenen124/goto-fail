/**
 * Created by Bart on 17/05/2016.
 */
var express = require('express');
var router = express.Router();

/* GET home page. */
router.get('/', function(req, res, next) {
    res.render('timeline');
});

module.exports = router;

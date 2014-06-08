![](https://raw.githubusercontent.com/cornetto/istweet/master/Logo/logo_small.png)
## İşTweet: Turkish Job Search Engine for Twitter
=======

İşTweet (pronounced as ɪʃ - twiːt) is a Twitter search engine for jobs in Turkish language.


### Demo

Working demo is availble at: [istweet.com](http://istweet.com)  


### System Overview

Design of İşTweet is divided into four main components according to their functionalities. 

[](https://raw.githubusercontent.com/cornetto/istweet/master/Logo/system_overview.png)

* **Twitter Catcher** is in touch with Twitter API for gathering tweets. 
* **Database system** is the place where all raw and processed data are stored. 
* **Search server** is responsible for information retrieval tasks. 
* **User interface** enables users to search for job openings on tweets and provide feedback if necessary.


### Configuration
 
Twitter Catcher configuration

solr configuration

**User Interface**:
* [solrstrap.js](https://github.com/cornetto/istweet/blob/master/Web%20GUI/Source/bootstrap/js/solrstrap.js) file has a variable named as SERVERROOT and it should contain the URL of the search server.
* User interface should be deployed to a web server or any platform that can run HTML and JS.


[Türkçe açıklama için tıklayınız!](https://github.com/cornetto/istweet/blob/master/README_tr.md)  

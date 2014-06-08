![](https://raw.githubusercontent.com/cornetto/istweet/master/Logo/logo_small.png)
## İşTweet: Turkish Job Search Engine for Twitter
=======

İşTweet (pronounced as ɪʃ - twiːt) is a Twitter search engine for jobs in Turkish language.


### Demo

Working demo is availble at: [istweet.com](http://istweet.com)  


### System Overview

Design of İşTweet is divided into four main components according to their functionalities. 

![](https://raw.githubusercontent.com/cornetto/istweet/master/Logo/system_overview.png)

* **Twitter Catcher** is in touch with Twitter API for gathering tweets. 
* **Database system** is the place where all raw and processed data are stored. 
* **Search server** is responsible for information retrieval tasks. 
* **User interface** enables users to search for job openings on tweets and provide feedback if necessary.


### Configuration
 

**Twitter Catcher**:
* [twitter4j.properties](https://github.com/cornetto/istweet/blob/master/Twitter%20Catcher/twitter-catcher/src/main/resources/twitter4j.properties) file should be changed to reflect the Twitter application credentials gathered from [dev.twitter.com](https://dev.twitter.com)
* [catcher.properties](https://github.com/cornetto/istweet/blob/master/Twitter%20Catcher/twitter-catcher/src/main/resources/catcher.properties) file should be changed to reflect MySQL user, password and URLs for "Raw Tweet Database" and "Processed Tweet Database".
* Twitter Catcher should be deployed to an application server that supports J2EE technologies, like JBoss of Glassfish.

**Database Storage:**:
* For database system, MySQL is used in İşTweet.
* Any MySQL server that enables IP based remote access can be used with the system.


**Search Server:**:
* For search server, Solr is used and configuration files are provided.
* [data-config.xml](https://github.com/cornetto/istweet/blob/master/Solr%20Configuration/solr/core0/conf/data-config.xml) file should be changed to have MySQL username, password and URL the "Processed Tweet Database".
* Solr can be deployed to any server that enables remote REST calls.


**User Interface**:
* [solrstrap.js](https://github.com/cornetto/istweet/blob/master/Web%20GUI/Source/bootstrap/js/solrstrap.js) file has a variable named as SERVERROOT and it should contain the URL of the search server.
* User interface should be deployed to a web server or any platform that can run HTML and JS.


[Türkçe açıklama için tıklayınız!](https://github.com/cornetto/istweet/blob/master/README_tr.md)  

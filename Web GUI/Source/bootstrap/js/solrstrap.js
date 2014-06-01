

//CONST- CHANGE ALL THESE TO TELL SOLRSTRAP ABOUT THE LOCATION AND STRUCTURE OF YOUR SOLR
var SERVERROOT = 'http://istweet.com/solr/core0/select/'; //SELECT endpoint
var HITTITLE = 'id';                                        //Name of the title field- the heading of each hit
var HITBODY = 'text';                                          //Name of the body field- the teaser text of each hit
var HITUSER = 'user';                                          //Name of the body field- the teaser text of each hit
var HITURL = 'url'; 
var TWEETURL = 'firstURL';
var TWEETDATE = 'date';
var TWEETPOP = 'popularity';
var HITSPERPAGE = 30;



//when the page is loaded- do this
  $(document).ready(function() {
 
    $('div[offset="0"]').loadSolrResults(getURLParam('q'), getURLParam('city'), Handlebars.compile($("#hit-template").html()), Handlebars.compile($("#result-summary-template").html()), 0);
    $('#searchbox').attr('value', getURLParam('q'));
	if(getURLParam('q') == "")
	{ 	   $('#loading').hide();}
	$('#searchcitybox').val(getURLParam('city')).trigger("change");
    $('#searchbox').focus();
  });

//when the searchbox is typed- do this
 //$('#searchbox').keyup(function() {
  $('#searchbutton').on('click', function() {
    if ($('#searchbox').val().length > -1) {
      $('div[offset="0"]').loadSolrResults($('#searchbox').val(), $('#searchcitybox').val(), Handlebars.compile($("#hit-template").html()), Handlebars.compile($("#result-summary-template").html()), 0);
	    window.location = '?q=' + $('#searchbox').val() + '&city=' + $('#searchcitybox').val();
	     $('#loading').show();
    }
    else {
      $('#rs').css({ opacity: 0.5 });
	   $('#loading').show();
	
    }
  });

  //jquery plugin allows resultsets to be painted onto any div.
  (function( $ ){
    $.fn.loadSolrResults = function(q, qcity, hitTemplate, summaryTemplate, offset) {
      $(this).getSolrResults(q,  qcity, hitTemplate, summaryTemplate, offset);
    };
  })( jQuery );


  //jquery plugin allows autoloading of next results when scrolling.
  (function( $ ){
    $.fn.loadSolrResultsWhenVisible = function(q,  qcity, hitTemplate, summaryTemplate, offset) {
      elem = this;
      $(window).scroll(function(event){
        if (isScrolledIntoView(elem) && !$(elem).attr('loaded')) {
          //dont instantsearch and autoload at the same time
          if ($('#searchbox').val() != getURLParam('q')) {
            window.location = '?q=' + $('#searchbox').val() + '&city=' + $('#searchcitybox').val();
          }
          $(elem).attr('loaded', true);
          $(elem).getSolrResults(q, qcity,  hitTemplate, summaryTemplate, offset);
          $(window).unbind('scroll');
        }
      });
    };
  })( jQuery );

 

  //jquery plugin for takling to solr
  (function( $ ){
    $.fn.getSolrResults = function(q, qcity, hitTemplate, summaryTemplate, offset) {
      var rs = this;
	  console.log(qcity);
      $(rs).parent().css({ opacity: 0.5 });
	  

      $.getJSON(SERVERROOT + "?json.wrf=?",
        {
          'rows': HITSPERPAGE,
          'wt': 'json',
          'q': q + ' ' + qcity,
          'start': offset,
		  'sort': 'date desc, popularity desc',
		  'fq': 'fb_unrelated:[0 TO 1]',
		  'spellcheck.build': true
        },
        function(result){
		 
          console.log(result);
		  	 
          if (result.response.docs.length > 0) {
            if (offset == 0) {
              rs.empty();          
              rs.append(summaryTemplate({totalresults: result.response.numFound, query: q, queryCity: qcity}));
              rs.siblings().remove();
            }
            for (var i = 0; i < result.response.docs.length; i++) {
				var URLstring="";
 
				URLstring.concat("https://twitter.com/", result.response.docs[i][HITUSER], "/statuses/", result.response.docs[i][HITTITLE],"/");
				 
				 var tempDate = result.response.docs[i][TWEETDATE].split(/[- : T]/);
				 formattedDate = showDate(tempDate[0], tempDate[1]-1, tempDate[2]);
				 
              rs.append(hitTemplate({	id:result.response.docs[i][HITTITLE], 
										title: result.response.docs[i][HITUSER],
										user: result.response.docs[i][HITUSER], 
										text: result.response.docs[i][HITBODY], 
										url: URLstring, 
										tweet_url:result.response.docs[i][TWEETURL], 
										date: formattedDate,
										popularity: result.response.docs[i][TWEETPOP]}));
            }
            $(rs).parent().css({ opacity: 1 });
			
            //if more results to come- set up the autoload div
            if ((+HITSPERPAGE+offset) < +result.response.numFound) {
              var nextDiv = document.createElement('div');
              $(nextDiv).attr('offset', +HITSPERPAGE+offset);
              rs.parent().append(nextDiv);
              $(nextDiv).loadSolrResultsWhenVisible(q,qcity, hitTemplate, summaryTemplate, +HITSPERPAGE+offset);
            }
			console.log("end loading..");
			 $('#loading').hide();
          }
		  else
		  {
		 
		   if(result.spellcheck.suggestions.length > 0)
		   {
				rs.getSolrResults(result.spellcheck.suggestions[3], qcity, hitTemplate, summaryTemplate, offset);
		   }
		   else{
				console.log("empty response");
				rs.empty();
				rs.append(summaryTemplate({totalresults: 0, query: q, queryCity: qcity}));
			   rs.siblings().remove();
				$('#loading').hide();
		   }
		  
		
		  }
        });
	   
    };
  })( jQuery );


  //utility function for grabbling URLs
  function getURLParam(name) {
    name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
    var regexS = "[\\?&]" + name + "=([^&#]*)";
    var regex = new RegExp(regexS);
    var results = regex.exec(window.location.search);
    if(results == null)
      return "";
    else
      return decodeURIComponent(results[1].replace(/\+/g, " "));
  }
  


  //utility function that checks to see if an element is onscreen
  function isScrolledIntoView(elem) {
    var docViewTop = $(window).scrollTop();
    var docViewBottom = docViewTop + $(window).height();
    var elemTop = $(elem).offset().top;
    var elemBottom = elemTop + $(elem).height();
    return ((elemBottom <= docViewBottom) && (elemTop >= docViewTop));
  }

  
  $(document).on('click','.feedback',function(){
  var id =  $(this).attr('id');
  console.log(id);
  var feedbackURL = "http://istweet.com/catcher/rest/feedback/unrelated/" + id
  $.get( feedbackURL );
	
	$(this).hide();
});
 
 
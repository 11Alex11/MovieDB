$(document).ready(function () {
    var queryString = window.location.search;
    var urlParams = new URLSearchParams(queryString);
    var searchFor = urlParams.get('searchFor');
    var searchText = urlParams.get('searchText');
    var searchPage = urlParams.get('searchPage');
    if(searchFor!="" && searchText!=""){
        search(searchFor,searchText);
    }
});


function search(searchFor, searchText){
    switch(searchFor){
        case "1":
            getMoviesImdbId(searchText);
            break;
        case "2":
            getActors();
            break;
        default:

            break;
    }

}


function clearErrors() {
    $('#errorMessages').empty();
}

function clearMainDiv() {
    $('#mainDiv').empty();
}

function getMovieByImdbId(imdbId){
    var url = encodeURI("https://data-imdb1.p.rapidapi.com/movie/id/" + imdbId + "/");
    console.log(url)
    const settings = {
	"async": true,
	"crossDomain": true,
	"url": url,
	"method": "GET",
	"headers": {
		"x-rapidapi-host": "data-imdb1.p.rapidapi.com",
		"x-rapidapi-key": "d6f5eaacb1mshf1db0cf7f0b0622p1bf671jsn578b35b8cbc3"
	}
    };

    $.ajax(settings).done(function (response) {
        $.each(response, function (index, movie){
            var template = document.getElementById("movieTemplate");
            var divToAppend = $("#mainDiv");
            var movieTemplate = template.content.cloneNode(true);
            $(movieTemplate).find("#imdbId").text(movie.imdb_id);
            $(movieTemplate).find("#title").text(movie.title);
            $(movieTemplate).find("#image").attr("src",movie.image_url);
            $(movieTemplate).find("#releaseDate").text(movie.year);
            $(movieTemplate).find("#description").text(movie.description);
            $(movieTemplate).find("#popularity").text(movie.popularity);
            $(movieTemplate).find("#userRating").text(movie.rating);
            $(movieTemplate).find("#contentRating").text(movie.content_rating);
            $(movieTemplate).find("#length").text(time_convert(movie.movie_length));
            var castMembers = getActorsByMovieImdbId(movie.imdb_id);
            divToAppend.append(movieTemplate);
        });
    }).fail(function (response){
        $('#errorMessages')
        .append($('<li>')
        .attr({class: 'list-group-item list-group-item-danger'})
        .text('Error calling web service. Please try again later.'));
    });
}

function getMoviesImdbId(searchText){
    var url = encodeURI('https://data-imdb1.p.rapidapi.com/movie/imdb_id/byTitle/'+searchText + "/");
    
    const settings = {
	"async": true,
	"crossDomain": true,
	"url": url,
	"method": "GET",
	"headers": {
		"x-rapidapi-host": "data-imdb1.p.rapidapi.com",
		"x-rapidapi-key": "d6f5eaacb1mshf1db0cf7f0b0622p1bf671jsn578b35b8cbc3"
	}
    };

    $.ajax(settings).done(function (response) {
        $.each(response, function (index, movie){
            $.each(movie, function(movieIndex, movieImdb){
                console.log(response);
                getMovieByImdbId(movieImdb.imdb_id);
            });
        });
    }).fail(function (response){
        $('#errorMessages')
        .append($('<li>')
        .attr({class: 'list-group-item list-group-item-danger'})
        .text('Error calling web service. Please try again later.'));
    });
}


function getActorsByMovieImdbId(imdbId){
    var url = encodeURI('https://data-imdb1.p.rapidapi.com/movie/id/' + imdbId +'/cast/');
    console.log(url);
    var castMembers = null;
    const settings = {
	"async": true,
	"crossDomain": true,
	"url": url,
	"method": "GET",
	"headers": {
		"x-rapidapi-host": "data-imdb1.p.rapidapi.com",
		"x-rapidapi-key": "d6f5eaacb1mshf1db0cf7f0b0622p1bf671jsn578b35b8cbc3"
	}
    };

    $.ajax(settings).done(function (response) {
        castMembers = response.roles;
    }).fail(function (response){
        $('#errorMessages')
        .append($('<li>')
        .attr({class: 'list-group-item list-group-item-danger'})
        .text('Error calling web service. Please try again later.'));
    });
    return castMembers;
}



function time_convert(num)
 { 
  const hours = Math.floor(num / 60);  
  const minutes = num % 60;
  return `${hours}h ${minutes}m`;         
}

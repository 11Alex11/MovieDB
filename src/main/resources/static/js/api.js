var data = [];
var pageSize = 10;
var curPage = 0;
var curOption = 1;
$(document).ready(function () {
    search();
});


function search(){
    $('#searchButton').click(function (event) {
        clearErrors();
        clearMainDiv();
        var option = $('#searchApiFor').val();
        var searchText  = $('#searchText').val();
        switch(option){
            case "1":
                curOption = "1";
                getMoviesImdbId(searchText);
                break;
            case "2":
                curOption = "2";
                getActorsImdbId(searchText);
                break;
            default:

                break;
        }
    });  
}

function displayData(){
    switch(curOption){
        case "1":
            displayMovies();
            break;
        case "2":
            
            displayActors();
            break;
        default:

            break;
    }
}

function displayMovies(){
    for(var i = 0; i < data.length && i< pageSize;i++){
        var movie = data[i+(curPage*pageSize)];
        getMovieByImdbId(movie.imdb_id);
    }
}

function displayActors(){
    for(var i = 0; i < data.length && i< pageSize;i++){
        var actor = data[i+(curPage*pageSize)];
        getActorByImdbId(actor.imdb_id);
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
            var template2 = document.getElementById("genreTemplate");
            var movieTemplate = template.content.cloneNode(true);
            //Set input values 
            $(movieTemplate).find(".movieImdbIdIn").val(movie.imdb_id);
            $(movieTemplate).find(".movieTitleIn").val(movie.title);
            $(movieTemplate).find(".movieImageUrlIn").val(movie.banner);
            $(movieTemplate).find(".movieReleaseDateIn").val(movie.release);
            $(movieTemplate).find(".movieDescriptionIn").val(movie.description);
            $(movieTemplate).find(".moviePopularityIn").val(movie.popularity);
            $(movieTemplate).find(".movieUserRatingIn").val(movie.rating);
            $(movieTemplate).find(".movieContentRatingIn").val(movie.content_rating);
            $(movieTemplate).find(".movieVideoUrl").val(movie.trailer);
            $(movieTemplate).find(".moviePlotIn").val(movie.plot);
            $(movieTemplate).find(".movieLengthIn").val(movie.movie_length);

            $(movieTemplate).find(".movieImdbId").text(movie.imdb_id);
            $(movieTemplate).find(".movieTitle").text(movie.title);
            $(movieTemplate).find(".movieImage").attr("src",movie.banner);
            $(movieTemplate).find(".movieReleaseDate").text(movie.year);
            $(movieTemplate).find(".movieDescription").text(movie.description);
            $(movieTemplate).find(".moviePopularity").text(movie.popularity);
            $(movieTemplate).find(".movieUserRating").text(movie.rating);
            $(movieTemplate).find(".movieContentRating").text(movie.content_rating);
            $(movieTemplate).find(".movieLength").text(time_convert(movie.movie_length));
            $.each(movie.gen, function (index, genre){
                console.log(genre.genre);
                var genreTemplate = template2.content.cloneNode(true);
                
                $(genreTemplate).find(".genreNameIn").val(genre.genre);

                
                $(genreTemplate).find(".genreId").text(genre.id);
                $(genreTemplate).find(".genreName").text(genre.genre);
                $(movieTemplate).find(".genresDiv").append(genreTemplate);
                
            });
            
            getActorsByMovieImdbId(movie.imdb_id,movieTemplate);
            

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
    data = [];
    page = 0;
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
                data.push(movieImdb);
            });
        });
        // Calulate page nav
        $("#pageNav").empty();
        var numPages = Math.ceil(data.length / pageSize);
        for(var i=0; i<numPages; i++){
            var listElement = '<li class="page-item"><button value="' + (i+1) +'" class="page-link" onClick="changePage(this.value)" >' + (i+1) + '</button></li>';
            $("#pageNav").append(listElement);
        }
        changePage(1);
    }).fail(function (response){
        $('#errorMessages')
        .append($('<li>')
        .attr({class: 'list-group-item list-group-item-danger'})
        .text('Error calling web service. Please try again later.'));
    });
}

function changePage(pageNum){
    clearMainDiv();
    curPage = pageNum-1;
    displayData();
}

function getActorsByMovieImdbId(imdbId, movieTemplate){
    var url = encodeURI('https://data-imdb1.p.rapidapi.com/movie/id/' + imdbId +'/cast/');
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
        var template = document.getElementById("castMemberTemplate");
        
        var divToAppend = $("#mainDiv");
        var numDirectors = 0;
        var numWriters = 0;
        var numActors = 0;
        $.each(response, function (index, roles2){
            $.each(roles2, function(index, roles){
            $.each(roles, function(index, castMember){
                if(castMember.role.includes("uncredited")){
                    console.log(castMember)
                    return;
                }
                var castTemplate = template.content.cloneNode(true);
                
                $(castTemplate).find(".castImdbIdIn").val(castMember.actor.imdb_id);
                $(castTemplate).find(".castMemberNameIn").val(castMember.actor.name);
                $(castTemplate).find(".castMemberRoleIn").val(castMember.role);
                
                
                $(castTemplate).find(".castMemberImdbId").text(castMember.actor.imdb_id);
                $(castTemplate).find(".castMemberName").text(castMember.actor.name);
                $(castTemplate).find(".castMemberRole").text(castMember.role);
                if(castMember.role === "Director"){
                    $(castTemplate).find(".castMemberRole").hide();
                    numDirectors++;
                    if(numDirectors>3){
                        $(castTemplate).find(".castMemberDiv").hide();
                    }
                    $(movieTemplate).find(".directorDiv").append(castTemplate);
                }
                else if(castMember.role === "Writer"){
                    $(castTemplate).find(".castMemberRole").hide();
                    numWriters++;
                    if(numWriters>3){
                        $(castTemplate).find(".castMemberDiv").hide();
                    }
                    $(movieTemplate).find(".writerDiv").append(castTemplate);
                }
                else{
                    if(castMember.actor.name)
                    numActors++;
                    if(numActors>6){
                        $(castTemplate).find(".castMemberDiv").hide();
                    }
                    $(movieTemplate).find(".actorDiv").append(castTemplate);
                }
            });
            });
        });
        divToAppend.append(movieTemplate);
    }).fail(function (response){
        $('#errorMessages')
        .append($('<li>')
        .attr({class: 'list-group-item list-group-item-danger'})
        .text('Error calling web service. Please try again later.'));
    });
}

function getActorsImdbId(searchText){
    var url = encodeURI('https://data-imdb1.p.rapidapi.com/actor/imdb_id_byName/'+searchText + "/");
    data = [];
    page = 0;
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
        console.log(response);
        $.each(response, function (index, results){
            $.each(results, function(index, actorImdbId){
                data.push(actorImdbId);
            });
        });
        // Calulate page nav
        $("#pageNav").empty();
        var numPages = Math.ceil(data.length / pageSize);
        for(var i=0; i<numPages; i++){
            var listElement = '<li class="page-item"><button value="' + (i+1) +'" class="page-link" onClick="changePage(this.value)" >' + (i+1) + '</button></li>';
            $("#pageNav").append(listElement);
        }
        changePage(1);
    }).fail(function (response){
        $('#errorMessages')
        .append($('<li>')
        .attr({class: 'list-group-item list-group-item-danger'})
        .text('Error calling web service. Please try again later.'));
    });
}

function getActorByImdbId(imdbId){
    var url = encodeURI("https://data-imdb1.p.rapidapi.com/actor/id/" + imdbId + "/");
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
        $.each(response, function (index, cast){
            var template = document.getElementById("castTemplate");
            var divToAppend = $("#mainDiv");
            var castTemplate = template.content.cloneNode(true);
            
            $(castTemplate).find(".castImdbId").val(cast.imdb_id);
            $(castTemplate).find(".castNameIn").val(cast.name);
            $(castTemplate).find(".castImageUrlIn").val(cast.image_url);
            $(castTemplate).find(".castBioIn").val(cast.partial_bio);
            $(castTemplate).find(".castBirthdateIn").val(cast.birth_date);
            $(castTemplate).find(".castBirthplaceIn").val(cast.birth_place);
            

            $(castTemplate).find(".castName").text(cast.name);
            $(castTemplate).find(".castImage").attr("src",cast.image_url);
            $(castTemplate).find(".castBio").text(cast.partial_bio);
            $(castTemplate).find(".castBirthdate").text(parseDate(cast.birth_date));
            $(castTemplate).find(".castBirthplace").text(cast.birth_place);
            getActorFullBioByActorImdbId(cast.imdb_id, castTemplate);
            
        });
    }).fail(function (response){
        $('#errorMessages')
        .append($('<li>')
        .attr({class: 'list-group-item list-group-item-danger'})
        .text('Error calling web service. Please try again later.'));
    });
}


function getActorFullBioByActorImdbId(imdbId, castTemplate){
    var url = encodeURI('https://data-imdb1.p.rapidapi.com/actor/id/' + imdbId +'/bio/');
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
        
        var divToAppend = $("#mainDiv");
        
        $(castTemplate).find(".castFullBioIn").val(response.results.biography.bio);
        
        $(castTemplate).find(".castFullBio").text(response.results.biography.bio);
        divToAppend.append(castTemplate);
    }).fail(function (response){
        $('#errorMessages')
        .append($('<li>')
        .attr({class: 'list-group-item list-group-item-danger'})
        .text('Error calling web service. Please try again later.'));
    });
}


function time_convert(num)
 { 
  const hours = Math.floor(num / 60);  
  const minutes = num % 60;
  return `${hours}h ${minutes}m`;         
}

function parseDate(s) {
  var date = new Date(s);
  var dateStrings = date.toString().split(' ');
  
  return dateStrings[1] +" " + dateStrings[2] + ", " + dateStrings[3];
}


$(document).ready(function () {
    newCast();
});

function newCast(){
    $('#newCastButton').click(function (event) {
        var template = document.getElementById("castRowTemplate");
        var castRowTemplate = template.content.cloneNode(true);
        
        
        $("#castTable").append(castRowTemplate);
    });  
}

function removeRow(button){
    $(button).parents('tr').remove();
}
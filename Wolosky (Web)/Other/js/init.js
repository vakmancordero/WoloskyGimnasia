$(document).ready(function(){
    
    $('.slider').slider({full_width: true});
    
    $('.button-collapse').sideNav();
    $('.parallax').parallax();
    
});

$('.maps').click(function () {
    $('.maps iframe').css("pointer-events", "auto");
});

$( ".maps" ).mouseleave(function() {
  $('.maps iframe').css("pointer-events", "none"); 
});


$(document).ready(function() {

	$(".icon-menu").click(function(e) {

		e.preventDefault();

	});

	$(".menu_bar").click(function(e) {

		e.preventDefault();

	});
	
	$('.maps').click(function () {
    $('.maps iframe').css("pointer-events", "auto");
});

$( ".maps" ).mouseleave(function() {
  $('.maps iframe').css("pointer-events", "none"); 
});

});

var contador = 1;

$(function() {
	
	$('.icon-menu').click(function(){
		
		if (contador == 1) {
			$('.nav1').animate({
				left: '0'
			});
			contador = 0;
		} else {
			$('.nav1').animate({
				left: '-100%'
			});
			contador = 1;
		}
		
	});

});
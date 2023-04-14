
document.addEventListener("DOMContentLoaded",function(){

	console.log("this is document addEventListner");





    document.querySelectorAll(".hide-btn")[0].addEventListener("click", function(){
        console.log("clicked");
        var sections = document.querySelectorAll(".image-section");
        for(var i=0; i<sections.length; i++) {
            sections[i].classList.add("hide");
        }
    });

    document.querySelectorAll(".show-btn")[0].addEventListener("click", function(){
        console.log("clicked");
        var sections = document.querySelectorAll(".image-section");
        for(var i=0; i<sections.length; i++) {
            sections[i].classList.remove("hide");
        }
    });

});
console.log("this is java-script");


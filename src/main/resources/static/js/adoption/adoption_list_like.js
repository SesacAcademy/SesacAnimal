document.addEventListener("DOMContentLoaded", function() {

    const like = document.getElementById('like');
    console.log("like",like);

    like.addEventListener('click',function (event){
        event.stopPropagation();
        event.preventDefault();

        console.log("likebutton");

    });
});
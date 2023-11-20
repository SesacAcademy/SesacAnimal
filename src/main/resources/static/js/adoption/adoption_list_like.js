document.addEventListener("DOMContentLoaded", function() {

    // const adoptionListItems = document.querySelectorAll('li[data-adoption-like]');
    //
    // console.log("size check", adoptionListItems.length);
    // adoptionListItems.forEach(function (item){
    //     console.log("item:>>",item.getAttribute("data-adoption-like").length);
    //
    // });


    const like = document.getElementsByClassName('like');
    const likeArray = Array.from(like);
    // console.log("like",like);


    likeArray.forEach(function(button) {
        const postId = button.querySelector('input[name="postId"]').value;
        const likeIcon = button.querySelector('span.like-icon');
        // const status = button.querySelector('input[name="like"]').value;
        const likeCount = button.querySelector(".like-count");


        // if(status == "1"){
        //     likeIcon.style.color = "red";
        // }else{
        //     likeIcon.style.color = "#3f464d";
        // }
        button.addEventListener('click',function (event) {
            event.stopPropagation();
            event.preventDefault();


            console.log("likeCount innerText:>>",likeCount.innerText);

            $.ajax({
                type: 'POST',
                url: '/v1/adoption/',
                contentType: 'application/json',
                data: JSON.stringify({postId: postId}),
                success: function (response) {
                    // 서버 응답 처리
                    console.log("response:>>", response);

                    // 추가로 필요한 로직 수행
                    // if(response == "1"){
                    //     status.value = "1"
                    // }else{
                    //     status.value = "0"
                    // }
                    console.log("status :>>",response.status);
                    console.log("likeCount :>>",response.likeCount);
                    if(response.status == 1){
                        likeIcon.style.color = "red";
                    }else{
                        likeIcon.style.color = "#3f464d";
                    }

                    likeCount.innerText =response.likeCount;
                    // console.log("likeCount",likeCount)


                },
                error: function (error) {
                    // 에러 처리
                    console.error("error:>>", error);
                }
            });
        });
    });

});



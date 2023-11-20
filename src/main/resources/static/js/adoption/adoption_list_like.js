document.addEventListener("DOMContentLoaded", function() {

    const like = document.getElementsByClassName('like');
    const likeArray = Array.from(like);


    likeArray.forEach(function(button) {
        const postId = button.querySelector('input[name="postId"]').value;
        const likeIcon = button.querySelector('span.like-icon');
        const likeCount = button.querySelector(".like-count");


        button.addEventListener('click',function (event) {
            event.stopPropagation();
            event.preventDefault();

            $.ajax({
                type: 'POST',
                url: '/v1/adoption/',
                contentType: 'application/json',
                data: JSON.stringify({postId: postId}),
                success: function (response) {
                    // 서버 응답 처리
                    console.log("response:>>", response);

                    // 색상 및 좋아요 수 변경 로직
                    // 좋아요 상태에 따라 스타일 변경
                    if (response.status) {
                        likeIcon.style.color = "#f86868"; // 좋아요 상태일 때 빨간색으로 변경
                    } else {
                        likeIcon.style.color = "#3f464d"; // 좋아요 상태가 아닐 때 검정색으로 변경
                    }

                    likeCount.innerText =response.likeCount;

                },
                error: function (error) {
                    // 에러 처리
                    console.error("error:>>", error);
                }
            });
        });
    });

});



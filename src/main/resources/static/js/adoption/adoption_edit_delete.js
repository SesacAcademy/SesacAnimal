document.addEventListener('DOMContentLoaded', function () {


    const postId = document.querySelector("#postId").value;
    console.log("postId check:>>",postId)

    // 게시글 삭제용 버튼
    const postDeleteButton = document.getElementById("delete-post-button");
    // 게시글 삭제용 모달
    let postModal = new bootstrap.Modal(document.getElementById("post-modal"));
    let postModalOk = document.getElementById("post-delete-ok");

    // 이미지 삭제용 버튼
    const ImageDeleteButtons = document.querySelectorAll(".adoption-edit .image-container button");
    // 이미지 삭제용 모달
    let imageModal = new bootstrap.Modal(document.getElementById("image-modal"));
    let imageModalOk = document.getElementById("image-delete-ok");
    
    //이미지 삭제 버튼 눌렀을 시
    ImageDeleteButtons.forEach(function(button) {
        button.addEventListener("click", function () {
            imageModal.show();
            let deleteIndex = button.closest('p').querySelector('input[type="hidden"]').value;

            // 확인 버튼 클릭 이벤트 처리
            imageModalOk.addEventListener("click", function () {
                button.closest('p').style.display='none';

                $.ajax({
                    type: 'POST',
                    url: '/v1/adoption/edit/' + postId,
                    contentType: 'application/json',
                    data: JSON.stringify({ deleteImageIndex: deleteIndex }),
                    success: function (response) {
                        // 서버 응답 처리
                        console.log("response:>>", response);
                        // 추가로 필요한 로직 수행
                    },
                    error: function (error) {
                        // 에러 처리
                        console.error("error:>>", error);
                    }
                });
                imageModal.hide();  // 모달 닫기
            });
        });
    });

    //게시글 삭제 버튼 눌렀을 시
    postDeleteButton.addEventListener("click", function () {
        postModal.show();
        // 확인 버튼 클릭 이벤트 처리
        postModalOk.addEventListener("click", function () {

            $.ajax({
                type: 'POST',
                url: '/v1/adoption/edit/' + postId,
                contentType: 'application/json',
                data: JSON.stringify({ deletePostId: postId }),
                success: function (response) {
                    // 서버 응답 처리
                    console.log("post Delete response:>>", response);
                    // 추가로 필요한 로직 수행
                    window.location.href=response;
                },
                error: function (error) {
                    // 에러 처리
                    console.error("post Delete error:>>", error);
                }
            });
            postModal.hide();  // 모달 닫기
        });
    });



});

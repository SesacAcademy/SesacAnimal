document.addEventListener('DOMContentLoaded', function() {
    // 댓글 수정하기 로직
    const replyEditButton = document.getElementsByClassName('reply-button-edit');
    for (let button of replyEditButton) {
        button.addEventListener('click', function() {
            const textarea = button.parentElement.parentElement.querySelector('.comment-contents');


            if(button.innerText === "수정하기"){
                textarea.readOnly = false;
                textarea.style.backgroundColor = "#eee";
                textarea.style.border = "1px solid #000";
                button.innerText = "수정완료";
                button.style.color="#c1dcf9";
                button.type="button";
            }else{
                textarea.readOnly = true
                textarea.style.backgroundColor = "#fff";
                textarea.style.border = "1px solid #fff";
                button.innerText = "수정하기";
                button.style.color="#f0b062";
                button.type="submit";
            }

        });
    }

    // 댓글 삭제하기 영역
    const deleteParentButton = document.getElementsByClassName('reply-button-delete');
    const deleteModal = new bootstrap.Modal(document.getElementById("delete-modal"));
    const deleteCommentModalOk = document.getElementById("comment-delete-ok");
    const postId = document.querySelector("#postId").value;

    for (let button of deleteParentButton) {
        button.addEventListener('click', function () {
            deleteModal.show();

            let deleteIndex = button.parentElement.parentElement.querySelector('input[name="commentId"]').value;
            // let deleteIndex = button.closest('p').querySelector('input[type="hidden"]').value;

            // 확인 버튼 클릭 이벤트 처리
            deleteCommentModalOk.addEventListener("click", function () {
                $.ajax({
                    type: 'DELETE',
                    url: '/v1/adoption/' + postId,
                    contentType: 'application/json',
                    data: JSON.stringify({ deleteCommentParentIndex: deleteIndex }),
                    success: function (response) {
                        // 서버 응답 처리
                        console.log("response:>>", response);
                        // 추가로 필요한 로직 수행
                        window.location.href='/v1/adoption/'+postId;
                    },
                    error: function (error) {
                        // 에러 처리
                        console.error("error:>>", error);
                    }
                });
                deleteModal.hide();  // 모달 닫기
            });

        });
    }


    // 대댓글 보기,  쓰기 영역 toggle
    let moreAndHideButtons = document.getElementsByClassName('reply-button');

    for (let button of moreAndHideButtons) {
        button.addEventListener('click', function() {

            if (button.textContent == '대댓글 보기') {
                let reReply = button.parentElement.parentElement.parentElement.parentElement.nextElementSibling.querySelector('.re-reply');

                if (reReply) {
                    toggleDisplayStyle(reReply);
                }
            } else if (button.textContent == '대댓글 작성') {
                let reReplyForm = button.parentElement.parentElement.parentElement.parentElement.nextElementSibling.querySelector('.re-reply-form');

                if (reReplyForm) {
                    toggleDisplayStyle(reReplyForm);
                }
            }
        });
    }

    function toggleDisplayStyle(element) {
        if (element.style.display === 'none' || element.style.display === '') {
            element.style.display = 'flex';
        } else {
            element.style.display = 'none';
        }
    }


    // 대댓글 수정할때 로직
    // 수정하기 누르면 readonly가 풀리고 글 작성할 수 있게 변하고 버튼이 전송버튼으로 변경됨
    const reReplyEditButton = document.getElementsByClassName('re-reply-button-edit');

    for (let button of reReplyEditButton) {
        button.addEventListener('click', function() {
            const textarea = button.parentElement.parentElement.querySelector('.comment-contents');

            if(button.innerText === "수정하기"){
                textarea.readOnly = false;
                textarea.style.backgroundColor = "#eee";
                button.innerText = "수정완료";
                button.style.color="#c1dcf9";
                button.type="button";
            }else{
                textarea.readOnly = true
                textarea.style.backgroundColor = "#fff";
                button.innerText = "수정하기";
                button.style.color="#f0b062";
                button.type="submit";
            }

        });
    }

    // 댓글 삭제하기 영역
    const deleteChildButton = document.getElementsByClassName('re-reply-button-delete');

    for (let button of deleteChildButton) {
        button.addEventListener('click', function () {
            deleteModal.show();

            let deleteIndex = button.parentElement.parentElement.querySelector('input[name="commentId"]').value;
            // let deleteIndex = button.closest('p').querySelector('input[type="hidden"]').value;

            // 확인 버튼 클릭 이벤트 처리
            deleteCommentModalOk.addEventListener("click", function () {
                $.ajax({
                    type: 'DELETE',
                    url: '/v1/adoption/' + postId,
                    contentType: 'application/json',
                    data: JSON.stringify({ deleteCommentChildIndex: deleteIndex }),
                    success: function (response) {
                        // 서버 응답 처리
                        console.log("response:>>", response);
                        // 추가로 필요한 로직 수행
                        window.location.href='/v1/adoption/'+postId;
                    },
                    error: function (error) {
                        // 에러 처리
                        console.error("error:>>", error);
                    }
                });
                deleteModal.hide();  // 모달 닫기
            });

        });
    }

});

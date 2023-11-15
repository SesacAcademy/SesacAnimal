document.addEventListener('DOMContentLoaded', function() {
    // 대댓글 보기,  쓰기 영역 toggle
    let moreAndHideButtons = document.getElementsByClassName('reply-button');

    for (let button of moreAndHideButtons) {
        button.addEventListener('click', function() {
            console.log(button);
            if (button.textContent == '대댓글 보기') {
                let reReply = button.parentElement.closest('p').nextElementSibling.querySelector('.re-reply');
                console.log("reply",reReply);
                if (reReply) {
                    toggleDisplayStyle(reReply);
                }
            } else if (button.textContent == '대댓글 작성') {
                let reReplyForm = button.closest('p').nextElementSibling.querySelector('.re-reply-form');
                console.log("reReplyForm",reReplyForm);
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


    // 댓글 수정할때 로직
    // 수정하기 누르면 readonly가 풀리고 글 작성할 수 있게 변하고 버튼이 전송버튼으로 변경됨
    const editButton = document.getElementsByClassName('reply-button-edit');

    for (let button of editButton) {
        button.addEventListener('click', function() {
            const textarea = button.parentElement.querySelector('.comment-contents');

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

});

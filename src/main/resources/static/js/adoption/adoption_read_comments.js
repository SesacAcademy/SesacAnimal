// document.addEventListener('DOMContentLoaded', function() {
//
//     let moreAndHideButtons = document.getElementsByClassName('reply-button');
//
//     for (let button of moreAndHideButtons) {
//         button.addEventListener('click', function() {
//             if (button.textContent == '대댓글 보기') {
//                 let reReply = button.parentElement.closest('p').nextElementSibling;
//
//                 if (reReply) {
//                     toggleDisplayStyle(reReply);
//                 }
//             } else if (button.textContent == '대댓글 작성') {
//                 let reReplyForm = button.closest('p').nextElementSibling.querySelector('.re-reply-form');
//
//                 if (reReplyForm) {
//                     toggleDisplayStyle(reReplyForm);
//                 }
//             }
//         });
//     }
//
//     function toggleDisplayStyle(element) {
//         if (element.style.display === 'none' || element.style.display === '') {
//             element.style.display = 'flex';
//         } else {
//             element.style.display = 'none';
//         }
//     }
//
// });

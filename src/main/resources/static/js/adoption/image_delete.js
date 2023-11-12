document.addEventListener('DOMContentLoaded', function () {
    // let elementp = document.querySelector(".adoption-edit .image-list p");
    // let element = document.querySelector(".adoption-edit .image-list");
    // console.log("element p:>>",elementp);
    // console.log("element :>>",element);
    // console.log("element children:>>",element.children);
    // console.log("element childElementCount:>>",element.childElementCount);
    // console.log("element classList.length :>>",element.classList.length);

    let container = document.querySelector(".adoption-edit .image-container");
    let buttons = document.querySelectorAll(".adoption-edit .image-container button");
    console.log("container children:>>",container.children);
    console.log("container childrenLength:>>",container.children.length);
    console.log("container childElementCount:>>",container.childElementCount);
    console.log("container classList:>>",container.classList);

    console.log("button :>>",buttons.length);

    // console.log("button input:>>",button.closest('input').querySelector('input[type="hidden"]').value);

    buttons.forEach(function(button) {
        button.addEventListener("click", function () {
            let dataIndex = button.closest('p').querySelector('input[type="hidden"]').value;
            console.log("이미지 인덱스: " + dataIndex);
            // 여기에서 AJAX를 사용하여 서버에 데이터 업데이트 요청 및 로직 추가
            // updateAdoptionStatus(dataIndex);
        });
    });

    // console.log("element",element.);
    /* 삭제 버튼 클릭 이벤트 리스너 등록 */
    // for (let i = 0; i < 5; i++) {
    //     let removeButton = document.getElementById("remove-button" + i);
    //     removeButton.addEventListener("click", function () {
    //         // AJAX를 사용하여 서버에 데이터 업데이트 요청
    //         updateAdoptionStatus(i);
    //         });
    //     }


    /* AJAX를 사용하여 서버에 데이터 업데이트 요청하는 함수 */
    // function updateAdoptionStatus(index) {
    //     // XMLHttpRequest 객체 생성
    //     var xhr = new XMLHttpRequest();
    //
    //     // 요청 설정
    //     xhr.open("POST", "/updateAdoptionStatus", true);
    //     xhr.setRequestHeader("Content-Type", "application/json");
    //
    //     // 요청 본문 작성
    //     var data = { index: index };
    //     xhr.send(JSON.stringify(data));
    //
    //     // 응답 처리
    //     xhr.onreadystatechange = function () {
    //         if (xhr.readyState === 4 && xhr.status === 200) {
    //             // 성공적으로 업데이트되었을 때의 동작
    //             alert("이미지 " + index + "의 상태가 업데이트되었습니다.");
    //         }
    //     };
    //     }

    });

const containers = document.querySelectorAll('.missing-detail-recomment-card-container');

containers.forEach((dom) => {
    const recommentButton = dom.querySelector(".missing-recomment-button");
    const recommentForm = dom.querySelector(".missing-recomment-form");
    const mainCommentInputForm = dom.querySelector(".missing-comment-form");

    const drawRecommentInput = () => {
        const action = mainCommentInputForm.action;
        recommentForm.style.display = "block";

        const title = document.createElement('div');
        title.textContent = "대댓글 작성";
        title.style.marginBottom = "10px";


        const div = document.createElement('div');
        div.style.display = "flex";
        div.style.flexDirection = "row";

        const input = document.createElement('input');
        input.type = 'text';
        input.name = 'comment';
        input.style.width = "70%";
        input.style.marginRight = "10px";

        const button = document.createElement('button');
        button.textContent = '작성';

        div.appendChild(input);
        div.appendChild(button);


        recommentForm.appendChild(title);
        recommentForm.appendChild(div);

    };


    recommentButton.addEventListener("click", () => {

        drawRecommentInput();
    });
});



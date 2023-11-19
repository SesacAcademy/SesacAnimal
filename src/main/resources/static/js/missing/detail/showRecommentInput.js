const containers = document.querySelectorAll('.missing-detail-recomment-card-container');

containers.forEach((dom) => {
    const recommentButton = dom.querySelector(".missing-comment-button-new");
    const recommentForm = dom.querySelector(".missing-recomment-form");
    const mainCommentInputForm = dom.querySelector(".missing-comment-form");
    let isFirst = true;

    const drawRecommentInput = () => {
        const action = mainCommentInputForm.action;
        const currentDisplay = recommentForm.style.display;
        recommentForm.style.display = currentDisplay === "block" ? "none" : "block";

        if (!isFirst) return;

        const title = document.createElement('div');
        title.textContent = "대댓글 작성";
        title.style.marginBottom = "10px";
        title.style.fontSize = "1.2rem";


        const div = document.createElement('div');
        div.style.display = "flex";
        div.style.flexDirection = "row";
        div.style.padding ="5px 0";

        const input = document.createElement('input');
        input.type = 'text';
        input.name = 'comment';
        input.style.width = "70%";
        input.style.marginRight = "10px";
        input.style.borderRadius = "4px";

        const button = document.createElement('button');
        button.textContent = '작성';
        button.style.fontSize = "1rem";
        button.style.display = "flex";
        button.style.justifyContent = "center";
        button.style.alignItems = "center";
        button.style.padding = "10px 10px";
        button.style.border = "1px solid black";
        button.style.borderRadius = "10px";
        button.style.cursor = "pointer";

        div.appendChild(input);
        div.appendChild(button);


        recommentForm.appendChild(title);
        recommentForm.appendChild(div);
        isFirst = false;
    };


    recommentButton?.addEventListener("click", () => {
        drawRecommentInput();
    });
});



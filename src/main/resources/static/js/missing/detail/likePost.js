

const button = document.querySelector(".missing-card-like");
const postId = document.querySelector(".missing-card-post-id").textContent;
const emptyHeart = document.querySelector(".empty-heart");
const filledHeart = document.querySelector(".filled-heart");
const likeCount = document.querySelector(".missing-like-count");


let lock = false;
button.addEventListener('click',async () => {
    const { isLiked } = await toggleLike();

    if (isLiked == 1) {
        emptyHeart.style.display = "none";
        filledHeart.style.display = "block";
        likeCount.textContent = Number(likeCount.textContent) + 1;
    } else {
        emptyHeart.style.display = "block";
        filledHeart.style.display = "none";
        if (likeCount.textContent > 0) likeCount.textContent = Number(likeCount.textContent) - 1;
    }

});

const toggleLike = async () => {
    if (!lock) {
        lock = true;
        const res = await fetch("/v1/missing/like", {
            method: "POST",
            headers: {
                'Content-type': 'application/json'
            },
            body: JSON.stringify({ postId })
        });

        const result = await res.json();
        lock = false;
        return result;
    }
}
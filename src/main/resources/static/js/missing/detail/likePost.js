

const button = document.querySelector(".missing-card-like");
const postId = document.querySelector(".missing-card-post-id").textContent;
const emptyHeart = document.querySelector(".empty-heart");
const filledHeart = document.querySelector(".filled-heart");

button.addEventListener('click',async () => {
    const { isLiked } = await toggleLike();

    if (isLiked == 1) {
        emptyHeart.style.display = "none";
        filledHeart.style.display = "block";
    } else {
        emptyHeart.style.display = "block";
        filledHeart.style.display = "none";
    }

});

const toggleLike = async () => {
    const res = await fetch("/v1/missing/like", {
        method: "POST",
        headers: {
            'Content-type': 'application/json'
        },
        body: JSON.stringify({ postId })
    });

    const result = await res.json();

    return result;
}
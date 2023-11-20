const signoutButton = document.getElementById('member-mypage-signout-button');

signoutButton.addEventListener('click', signout);

function signout() {

    const checkSignout = confirm("정말로 회원 탈퇴를 하시겠습니까?");

    if (checkSignout) {

        const config = {"Content-Type": 'application/json'};

        axios.delete("/v1/api/member", null, config)
        .then(function (response) {
            console.log(response)

            if (response.data.statusCode === 200) {
                alert("회원 탈퇴에 성공하였습니다.");
                location.href="/v1/auth/login";
            }

            else if (response.data.statusCode === 400) {
                console.log(response)

                if (response.data.context === null) {
                    alert(response.data.message);
                }
            }

        }).catch(function (error) {
            console.log(error);
            alert('회원 탈퇴에 실패하였습니다.');
        });
    }
}
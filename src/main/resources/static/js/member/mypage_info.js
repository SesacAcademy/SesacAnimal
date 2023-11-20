const signoutButton = document.getElementById('member-mypage-signout-button');

signoutButton.addEventListener('click', signout);

function signout() {

    const checkSignout = confirm("정말로 회원 탈퇴를 하시겠습니까?");

    if (checkSignout) {

        axios.delete("/v1/api/auth/phone", {
            'email': email,
            'name': name,
            'phone': phone,
            'authCode' : authCode
        }, config).then(function (response) {
            console.log(response)

            if (response.data.statusCode === 200) {

            }



        }).catch(function (error) {
            console.log(error);

            alert('비밀번호 찾기에 실패하였습니다.');
        });
    }
}
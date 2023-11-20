

const changePasswordButton = document.getElementById('member-mypage-change-password-button');
const cancelPasswordButton = document.getElementById('member-mypage-cancel-password-button');

changePasswordButton.addEventListener('click', changePassword);
cancelPasswordButton.addEventListener('click', cancelPassword);

function isValidPassword(password) {
    let passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$/

    return passwordRegex.test(password);
}

function changePassword() {

    const inputOldPassword = document.getElementById('member-mypage-old-password').value;
    const inputNewPassword = document.getElementById('member-mypage-new-password').value;
    const inputCheckNewPassword = document.getElementById('member-mypage-check-new-password').value;

    if(!isValidPassword(inputOldPassword)) {
        alert("기존 비밀번호를 제대로 입력해주세요.");
        return;
    }

    if(!isValidPassword(inputNewPassword)) {
        alert("새로운 비밀번호를 형식에 맞추어 입력해주세요.");
        return;
    }

    if(inputNewPassword !== inputCheckNewPassword) {
        alert("새 비밀번호가 일치하지 않습니다.");
        return;
    }

    const config = {"Content-Type": 'application/json'};

    axios.patch("/v1/api/member/mypage/password", {
        'oldPassword': inputOldPassword,
        'newPassword': inputNewPassword
    }, config).then(function (response) {
        console.log(response)

        if (response.data.statusCode === 200) {
            alert("비밀번호를 성공적으로 변경하였습니다.");
            location.href="/";
        }

        else if (response.data.statusCode === 400) {
            console.log(response)

            if (response.data.context === null) {
                // alert(response.data.message);
                alert("기존 비밀번호가 틀렸습니다.");
            }

            else {
                const errorData = response.data.context;

                console.log(response.data);

                if (errorData.oldPassword !== undefined) {
                    alert(errorData.oldPassword);
                }

                if (errorData.newPassword !== undefined) {
                    alert(errorData.newPassword);
                }
            }
        }


    }).catch(function (error) {
        console.log(error);
        alert('비밀번호 변경에 실패하였습니다.');
    });
}

function cancelPassword() {
    location.href="/v1/member/mypage/info";
}
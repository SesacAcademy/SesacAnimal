document.getElementById('member-agree').addEventListener('click', function () {
    const agreeModel = new bootstrap.Modal(document.getElementById('member-agree-model'));
    agreeModel.show();
});

document.getElementById('member-info').addEventListener('click', function () {
    const infoModel = new bootstrap.Modal(document.getElementById('member-info-model'));
    infoModel.show();
});


let timerInterval = "";

const authEmailButton = document.getElementById('member-auth-email-button');
const authEmailCheckButton = document.getElementById('member-auth-check');
const signupButton = document.getElementById('member-login');

authEmailButton.addEventListener('click', createToken);
authEmailCheckButton.addEventListener('click', checkToken);
signupButton.addEventListener('click', signup);

function isValidEmail(email) {
    let emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

    return emailRegex.test(email);
}

function isValidPhone(phone) {
    let koreanPhoneNumberPattern = /^01[016789]-\d{3,4}-\d{4}$/;

    // 정규 표현식을 사용하여 검증
    if (koreanPhoneNumberPattern.test(phone)) {
        return false;
    } else {
        return true;
    }
}

function createToken() {
    const emailAuthBlock = document.getElementById('member-auth-email-block');
    const emailAuthButton = document.getElementById('member-auth-email-button');

    const email = document.getElementById('member-email').value;

    if (!isValidEmail(email)) {
        alert("이메일 형식이 틀렸습니다.");
        return;
    }

    axios({
        method: "get",
        url: "/v1/api/auth/email?email=" + email,
    }).then(function (response) {
        if (response.data.statusCode === 204) {
            console.log(response);
            emailAuthBlock.style.display = 'flex';
            emailAuthButton.style.display = 'none';

            document.getElementById('member-auth-email-button').style.display = 'none';
            alert(email + ' 이메일로 인증번호를 발송하였습니다.');

            const timerElement = document.getElementById("member-timer");
            let remainingTime = 180; // 3분을 초로 표현

            timerInterval = setInterval(function() {
                const minutes = Math.floor(remainingTime / 60);
                const seconds = remainingTime % 60;
                const formattedTime = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
                timerElement.textContent = `${formattedTime}`;
                remainingTime--;

                if (remainingTime < 0) {
                    clearInterval(timerInterval);
                    alert('인증시간이 종료되었습니다.');
                    emailAuthBlock.style.display = 'none';
                    // timerElement.textContent = "인증 시간이 종료되었습니다.";
                    emailAuthButton.style.display = 'inline-block';
                }
            }, 1000);
        }
        else if (response.data.statusCode === 409) {
            alert(response.data.message);
            console.log(response);
        }

    }).catch(function (error) {
        console.log(error);
        alert('메일 인증에 실패하였습니다.');
    });
}

function checkToken() {
    const email = document.getElementById('member-email').value;
    const token = document.getElementById('member-auth-token').value;
    const timer = document.getElementById('member-timer');
    const check = document.getElementById('member-auth-check');

    axios.post("/v1/api/auth/email", {
        'email': email,
        'token': token
    }).then(function (response) {
        if (response.data.statusCode === 204) {
            console.log(response)
            alert(response.data.message);

            timer.style.display = 'none';
            check.classList.remove('btn-outline-secondary');
            check.classList.add('btn-success');
            check.innerText ="확인 완료";
            clearInterval(timerInterval);
            authEmailButton.removeEventListener('click', createToken);
            authEmailCheckButton.removeEventListener('click', checkToken);
            document.getElementById('member-auth-token').disabled = true;
        }
        else if (response.data.statusCode === 400) {
            console.log(response)
            alert(response.data.message);
        }
    }).catch(function (error) {
        console.log(error);
        alert('메일 인증에 실패하였습니다.');
    });
}

function signup() {
    const email = document.getElementById('member-email').value;
    const token = document.getElementById('member-auth-token').value;
    const name = document.getElementById('member-name').value;
    const password = document.getElementById('member-password').value;
    const passwordCheck = document.getElementById('member-password-check').value;
    const phone = document.getElementById('member-phone').value;

    if (!isValidEmail(email)) {
        alert("이메일 형식이 틀렸습니다.");
        return;
    }

    if (token === "") {
        alert("이메일 인증을 해주세요");
        return;
    }

    if (name === "") {
        alert("이름을 입력해주세요.");
        return;
    }

    if (password === "") {
        alert("비밀번호를 입력해주세요.");
        return;
    }

    if (password !== passwordCheck) {
        alert("비밀번호가 일치하지 않습니다.");
        return;
    }

    if (isValidPhone(phone)) {
        alert("휴대폰 번호 형식이 틀렸습니다.");
        return;
    }

    const config = {"Content-Type": 'application/json'};

    axios.post("/v1/api/auth/signup", {
        'email': email,
        'token': token,
        'name': name,
        'password': password,
        'phone': phone
    }, config).then(function (response) {
        if (response.data.statusCode === 200) {
            alert("회원가입이 완료되었습니다.");
            location.href="/v1/auth/login";
        }
        else if (response.data.statusCode === 400) {

            // 인증번호가 만료된 경우
            if (response.data.context === null) {
                alert(response.data.message);
                alert("새로고침하여 다시 진행해주세요.");
            }

            // 값이 틀린 경우
            else {
                const errorData = response.data.context;

                console.log(response.data);

                if (errorData.name !== undefined) {
                    alert(response.data.context.name);
                }

                if (errorData.password !== undefined) {
                    alert(response.data.context.password);
                }

                if (errorData.phone !== undefined) {
                    alert(response.data.context.phone);
                }

                if (errorData.token !== undefined) {
                    alert(response.data.context.token);
                }
            }
        }
        // 이메일이 중복된 경우 (회원가입 도중, 해당 이메일로 가입된 경우)
        else if (response.data.statusCode === 409) {
            console.log(response.data);
            alert(response.data.message);
            alert("새로고침하여 다시 진행해주세요.");
        }
    }).catch(function (error) {
        console.log(error);
        alert('메일 인증에 실패하였습니다.');
    });
}

// const timerInterval = setInterval(function() {
//     const minutes = Math.floor(remainingTime / 60);
//     const seconds = remainingTime % 60;
//     const formattedTime = `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`;
//     timerElement.textContent = `${formattedTime}`;
//     remainingTime--;
//
//     if (remainingTime < 0) {
//         clearInterval(timerInterval);
//         alert('인증시간이 종료되었습니다.');
//         emailAuthBlock.style.display = 'none';
//         // timerElement.textContent = "인증 시간이 종료되었습니다.";
//         emailAuthButton.style.display = 'inline-block';
//     }
// }, 1000);

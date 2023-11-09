// "a" 태그를 클릭하면 모달 열기
document.getElementById('member-id-model').addEventListener('click', function () {
    var idModal = new bootstrap.Modal(document.getElementById('id-model'));
    idModal.show();
});

document.getElementById('member-password-model').addEventListener('click', function () {
    var passwordModal = new bootstrap.Modal(document.getElementById('password-model'));
    passwordModal.show();
});

function isValidEmail(email) {
    let emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

    return emailRegex.test(email);
}

function isValidPassword(password) {
    let passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$/

    return passwordRegex.test(password);
}

const loginButton = document.getElementById('member-login');

loginButton.addEventListener('click', login);

function login() {
    const email = document.getElementById('member-login-email').value;
    const password = document.getElementById('member-login-password').value;

    if (!isValidEmail(email)) {
        alert("이메일 형식이 틀렸습니다.");
        return;
    }

    if (!isValidPassword(password)) {
        alert("비밀번호를 규칙에 맞추어 입력해주세요.");
        return;
    }

    const config = {"Content-Type": 'application/json'};

    axios.post("/v1/api/auth/login", {
        'email': email,
        'password': password,
    }, config).then(function (response) {
        console.log(response)

        if (response.data.statusCode === 200) {
            alert("로그인 하였습니다.");
            location.href="/";
        }

        else if (response.data.statusCode === 400) {
            alert(response.data.message);
        }

    }).catch(function (error) {
        console.log(error);
        alert('로그인에 실패하였습니다.');
    });
}
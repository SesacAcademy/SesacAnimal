// "a" 태그를 클릭하면 모달 열기
document.getElementById('member-id-model').addEventListener('click', function () {
    // input 박스 display 속성 none으로
    const findEmailTag = document.querySelectorAll(".member-find-hide");
    const inputEmail = document.getElementById('member-find-email');
    const inputEmailBox = document.getElementById('member-find-appear');

    inputEmailBox.style.display = "none";
    inputEmail.value = "";

    // 선택한 요소들의 display 속성을 "none"으로 변경
    for (var i = 0; i < findEmailTag.length; i++) {
        findEmailTag[i].style.display = "flex";
    }

    var idModal = new bootstrap.Modal(document.getElementById('id-model'));
    idModal.show();
});

document.getElementById('member-password-model').addEventListener('click', function () {
    var passwordModal = new bootstrap.Modal(document.getElementById('password-model'));
    passwordModal.show();
});

function isVaildName(name) {
    let nameRegex = /^[가-힣]{2,4}$/;

    return nameRegex.test(name);
}

function isValidEmail(email) {
    let emailRegex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;

    return emailRegex.test(email);
}

function isValidPassword(password) {
    let passwordRegex = /^(?=.*[a-zA-Z])(?=.*[0-9]).{8,25}$/

    return passwordRegex.test(password);
}

function isValidPhone(phone) {
    let koreanPhoneNumberPattern = /^01[016789]-\d{3,4}-\d{4}$/;

    return koreanPhoneNumberPattern.test(phone);
}

const loginButton = document.getElementById('member-login');
const findEmailButton = document.getElementById('member-find-email-button');

loginButton.addEventListener('click', login);
findEmailButton.addEventListener('click', findEmail);

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

function findEmail() {

    const inputName = document.getElementById('member-find-email-name');
    const inputPhone = document.getElementById('member-find-email-phone');
    const inputEmail = document.getElementById('member-find-email');
    const inputEmailBox = document.getElementById('member-find-appear');
    const name = inputName.value;
    const phone = inputPhone.value;

    if (!isVaildName(name)) {
        alert("이름을 제대로 입력해주세요.");
        return;
    }

    if (!isValidPhone(phone)) {
        alert("휴대폰 번호 형식이 틀렸습니다.");
        return;
    }

    const config = {"Content-Type": 'application/json'};

    axios.post("/v1/api/find/email", {
        'name': name,
        'phone': phone,
    }, config).then(function (response) {
        console.log(response)

        if (response.data.statusCode === 200) {
            alert(response.data.context);

            // 기존에 입력한 값 제거
            inputName.value = "";
            inputPhone.value = "";

            // input 박스 display 속성 none으로
            const findEmailTag = document.querySelectorAll(".member-find-hide");

            // 선택한 요소들의 display 속성을 "none"으로 변경
            for (var i = 0; i < findEmailTag.length; i++) {
                findEmailTag[i].style.display = "none";
            }

            // 찾은 이메일 세팅
            inputEmailBox.style.display = "flex";
            inputEmail.value = response.data.context;
        }

        else if (response.data.statusCode === 400) {
            // 해당 정보로 가입된 이메일이 없는 경우
            if (response.data.context === null) {
                alert(response.data.message);
            }

            // 값 형식이 틀린 경우
            else {
                const errorData = response.data.context;

                console.log(response.data);

                if (errorData.name !== undefined) {
                    alert(response.data.context.name);
                }

                if (errorData.phone !== undefined) {
                    alert(response.data.context.phone);
                }
            }
        }

    }).catch(function (error) {
        console.log(error);
        alert('아이디 찾기에 실패하였습니다.');
    });
}

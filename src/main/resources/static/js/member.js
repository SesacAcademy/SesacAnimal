document.getElementById('member-agree').addEventListener('click', function () {
    const agreeModel = new bootstrap.Modal(document.getElementById('member-agree-model'));
    agreeModel.show();
});

document.getElementById('member-info').addEventListener('click', function () {
    const infoModel = new bootstrap.Modal(document.getElementById('member-info-model'));
    infoModel.show();
});

// 타이머 함수
document.getElementById('member-auth-email-button').addEventListener('click', function () {
    const emailAuthBlock = document.getElementById('member-auth-email-block');
    const emailAuthButton = document.getElementById('member-auth-email-button');

    emailAuthBlock.style.display = 'flex';
    emailAuthButton.style.display = 'none';
    alert('test@naver.com 이메일로 인증번호를 발송하였습니다.');

    document.getElementById('member-auth-email-button').style.display = 'none';

    const timerElement = document.getElementById("member-timer");
    let remainingTime = 180; // 5분을 초로 표현

    const timerInterval = setInterval(function() {
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
});
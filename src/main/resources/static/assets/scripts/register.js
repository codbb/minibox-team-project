const $join = document.querySelector('.join');
const $txtWrap = $join.querySelector(':scope > .txt-wrap');

const $agree = document.querySelector('.agree');
const $serviceCheck = $agree.querySelector(':scope > .txt-wrap > label > .serviceCheck');
const $agreeCheck = $agree.querySelector(':scope > .txt-wrap > label > .agreeCheck');
const $agreeBtn = $agree.querySelector(':scope > .txt-wrap > .button-wrap > .--button.-purple');

const $main = document.querySelector('.main');

const $success = document.querySelector('.success');

$txtWrap.onclick = () => {
    $join.hide();
    $agree.show();
}

$agreeBtn.onclick = () => {
    if (!$serviceCheck.checked) {
        alert('서비스 이용 약관 동의에 체크 해주세요.');
        return;
    }
    if (!$agreeCheck.checked) {
        alert('개인정보 수집 및 이용 동의에 체크 해주세요.');
        return;
    }
    $agree.hide();
    $main.show();
}

{
    const $registerForm = document.getElementById('registerForm');

    $registerForm.onsubmit = (e) => {
        e.preventDefault();
        const $emailLabel = $registerForm.findLabel('email');
        const $passwordLabel = $registerForm.findLabel('password');
        const $passwordCheckLabel = $registerForm.findLabel('passwordCheck');
        const $nicknameLabel = $registerForm.findLabel('nickname');
        const $contactLabel = $registerForm.findLabel('contact');
        const $birthdateLabel = $registerForm.findLabel('birthdate');
        $emailLabel.setValid($registerForm['email'].value.length >= 8 && $registerForm['email'].value.length <= 50);
        $passwordLabel.setValid($registerForm['password'].value.length >= 6 && $registerForm['password'].value.length <= 50, '올바른 비밀번호를 입력해 주세요.');
        if ($passwordLabel.isValid()) {
            $passwordCheckLabel.setValid($registerForm['password'].value === $registerForm['passwordCheck'].value, '비밀번호가 서로 일치하지 않습니다.');
        }
        $nicknameLabel.setValid($registerForm['nickname'].value.length >= 2 && $registerForm['nickname'].value.length <= 10);
        $contactLabel.setValid($registerForm['contact'].value.length >= 10 && $registerForm['contact'].value.length <= 12);
        $birthdateLabel.setValid(/^(\d{4})-(\d{2})-(\d{2})$/.test($registerForm['birthdate'].value));
        if (!$emailLabel.isValid() || !$passwordLabel.isValid() || !$passwordCheckLabel.isValid() || !$nicknameLabel.isValid() || !$contactLabel.isValid() || !$birthdateLabel.isValid()) {
            return;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('email', $registerForm['email'].value);
        formData.append('password', $registerForm['password'].value);
        formData.append('nickname', $registerForm['nickname'].value);
        formData.append('contact', $registerForm['contact'].value);
        formData.append('birthdate', $registerForm['birthdate'].value);
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            Loading.hide();
            if (xhr.status < 200 || xhr.status >= 300) {
                alert('요청을 전송하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
                return;
            }
            const response = JSON.parse(xhr.responseText);
            if (response['results'] === 'success') {
                alert('회원가입해 주셔서 감사합니다.\n입력하신 이메일로 계정을 인증할 수 있는 링크를 전송하였습니다.\n계정 인증 후 로그인할 수 있으며,\n해당 링크는 24시간 동안만 유효하니 유의해 주세요.');
                $main.hide();
                $success.show();
            } else if (response['results'] === 'failure_duplicate_contact') {
                alert(`입력하신 연락처(${$registerForm['contact'].value})는 이미 사용 중입니다.\n다른 연락처를 사용해 주세요.`);
            } else if (response['results'] === 'failure_duplicate_email') {
                alert(`입력하신 이메일(${$registerForm['email'].value})은 이미 사용 중입니다.\n다른 이메일을 사용해 주세요.`);
            } else if (response['results'] === 'failure_duplicate_nickname') {
                alert(`입력하신 닉네임(${$registerForm['nickname'].value}}은 이미 사용 중입니다.\n다른 닉네임을 사용해 주세요.`);
            } else {
                alert(`알 수 없는 이유로 회원가입에 실패하였습니다.\n잠시 후 다시 시도해 주세요.`);
            }
        };
        xhr.open('POST', '../user/');
        xhr.send(formData);
        Loading.show(0);
    };
}

const $recoverPasswordForm = document.getElementById('recoverPassword');

$recoverPasswordForm.onsubmit = (e) => {
    e.preventDefault();
    const $passwordLabel = $recoverPasswordForm.findLabel('password');
    const $passwordCheckLabel = $recoverPasswordForm.findLabel('passwordCheck');
    $passwordLabel.setValid($recoverPasswordForm['password'].value.length >= 6 && $recoverPasswordForm['password'].value.length <= 50, '올바른 비밀번호를 입력해 주세요.');
    if ($passwordLabel.isValid()) {
        $passwordCheckLabel.setValid($recoverPasswordForm['password'].value === $recoverPasswordForm['passwordCheck'].value, '비밀번호가 서로 일치하지 않습니다.');
    }
    if (!$passwordLabel.isValid() || !$passwordCheckLabel.isValid()) {
        return;
    }

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('userEmail', $recoverPasswordForm['userEmail'].value);
    formData.append('key', $recoverPasswordForm['key'].value);
    formData.append('password', $recoverPasswordForm['password'].value);
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
            alert('비밀번호를 성공적으로 재설정하였습니다.');
            location.href='/';
        } else if (response['results'] === 'failure') {
            alert('비밀번호를 재설정할 수 없습니다. 링크가 올바르지 않거나 링크가 손상되었을 수 있습니다.');
        } else if (response['results'] === 'failure_expired') {
            alert('비밀번호를 재설정 할 수 없습니다. 해당 링크는 더 이상 유효하지 않습니다. 미니박스에서 인증 링크를 다시 받을 수 있습니다.');
        } else {
            alert('서버가 알 수 없는 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('PATCH', '/user/recover-password');
    xhr.send(formData);
    Loading.show();
};
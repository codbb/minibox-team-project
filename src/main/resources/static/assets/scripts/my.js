const $deleteForm = document.getElementById('deleteForm');
const $cancel = $deleteForm.querySelector(':scope > .button-wrap > .cancel');
const $paymentDeleteForm = document.getElementById('paymentDeleteForm');
const $paymentDeleteCancel = $paymentDeleteForm.querySelector(':scope > .button-wrap > .cancel');
const $userDeleteBtn = document.getElementById('userDeleteBtn');
const $modifyForm = document.getElementById('modifyForm');

const $myBtn = document.querySelector('.my-btn');
const $passwordBtn = document.querySelector('.password-btn');
const $reserveBtn = document.querySelector('.reserve-btn');
const $reviewBtn = document.querySelector('.review-btn');
const $likeBtn = document.querySelector('.like-btn');

const $myContainer = document.querySelector('.my-container');
const $passwordContainer = document.querySelector('.password-container');
const $reserveContainer = document.querySelector('.reserve-container');
const $reviewContainer = document.querySelector('.review-container');
const $likeContainer = document.querySelector('.like-container');

$myBtn.onclick = () => {
    $myContainer.show();
    $passwordContainer.hide();
    $reserveContainer.hide();
    $reviewContainer.hide();
    $likeContainer.hide();

    $myBtn.classList.add('on');
    $passwordBtn.classList.remove('on');
    $reserveBtn.classList.remove('on');
    $reviewBtn.classList.remove('on');
    $likeBtn.classList.remove('on');
}

$passwordBtn.onclick = () => {
    $myContainer.hide();
    $passwordContainer.show();
    $reserveContainer.hide();
    $reviewContainer.hide();
    $likeContainer.hide();

    $myBtn.classList.remove('on');
    $passwordBtn.classList.add('on');
    $reserveBtn.classList.remove('on');
    $reviewBtn.classList.remove('on');
    $likeBtn.classList.remove('on');
}

$reserveBtn.onclick = () => {
    $myContainer.hide();
    $passwordContainer.hide();
    $reserveContainer.show();
    $reviewContainer.hide();
    $likeContainer.hide();

    $myBtn.classList.remove('on');
    $passwordBtn.classList.remove('on');
    $reserveBtn.classList.add('on');
    $reviewBtn.classList.remove('on');
    $likeBtn.classList.remove('on');
}

$reviewBtn.onclick = () => {
    $myContainer.hide();
    $passwordContainer.hide();
    $reserveContainer.hide();
    $reviewContainer.show();
    $likeContainer.hide();

    $myBtn.classList.remove('on');
    $passwordBtn.classList.remove('on');
    $reserveBtn.classList.remove('on');
    $reviewBtn.classList.add('on');
    $likeBtn.classList.remove('on');
}

$likeBtn.onclick = () => {
    $myContainer.hide();
    $passwordContainer.hide();
    $reserveContainer.hide();
    $reviewContainer.hide();
    $likeContainer.show();

    $myBtn.classList.remove('on');
    $passwordBtn.classList.remove('on');
    $reserveBtn.classList.remove('on');
    $reviewBtn.classList.remove('on');
    $likeBtn.classList.add('on');
}

$userDeleteBtn.onclick = () => {
    $cover.onclick = () => {
        $deleteForm.hide();
        $cover.hide();
    }
    $cancel.onclick = () => {
        $deleteForm.hide();
        $cover.hide()
    }
    $deleteForm.show();
    $cover.show();
}

// 개인정보 수정
{
    $modifyForm.onsubmit = (e) => {
        e.preventDefault();
        const $passwordLabel = $modifyForm.findLabel('password');
        const $nicknameLabel = $modifyForm.findLabel('nickname');
        const $contactLabel = $modifyForm.findLabel('contact');
        const $birthdateLabel = $modifyForm.findLabel('birthdate');
        $passwordLabel.setValid($modifyForm['password'].value.length >= 6 && $modifyForm['password'].value.length <= 50, '6자리 이상 50자리 미만');
        $nicknameLabel.setValid($modifyForm['nickname'].value.length >= 2 && $modifyForm['nickname'].value.length <= 10, '2자리 이상 10자리 미만');
        $contactLabel.setValid($modifyForm['contact'].value.length >= 10 && $modifyForm['contact'].value.length <= 12, '11자리');
        $birthdateLabel.setValid(/^(\d{4})-(\d{2})-(\d{2})$/.test($modifyForm['birthdate'].value), '4자리-2자리-2자리');
        if (!$passwordLabel.isValid() || !$nicknameLabel.isValid() || !$contactLabel.isValid() || !$birthdateLabel.isValid()) {
            return;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('password', $modifyForm['password'].value);
        formData.append('nickname', $modifyForm['nickname'].value);
        formData.append('contact', $modifyForm['contact'].value);
        formData.append('birthdate', $modifyForm['birthdate'].value);
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
                alert('회원정보를 정상적으로 수정하였습니다.\n로그아웃 됩니다.\n다시 로그인 해주세요.');
                location.href='/logout';
            } else if (response['results'] === 'failure_password') {
                alert(`비밀번호가 일치하지 않습니다.`);
            } else if (response['results'] === 'failure_duplicate_contact') {
                alert(`입력하신 연락처(${$modifyForm['contact'].value})는 이미 사용 중입니다.\n다른 연락처를 사용해 주세요.`);
            } else if (response['results'] === 'failure_duplicate_email') {
                alert(`입력하신 이메일(${$modifyForm['email'].value})은 이미 사용 중입니다.\n다른 이메일을 사용해 주세요.`);
            } else if (response['results'] === 'failure_duplicate_nickname') {
                alert(`입력하신 닉네임(${$modifyForm['nickname'].value}}은 이미 사용 중입니다.\n다른 닉네임을 사용해 주세요.`);
            } else {
                alert(`알 수 없는 이유로 회원가입에 실패하였습니다. 잠시 후 다시 시도해 주세요.`);
            }
        };
        xhr.open('PATCH', 'user/modify-user');
        xhr.send(formData);
        Loading.show(0);
    }
}

// 회원탈퇴
{
    const $deleteForm = document.getElementById('deleteForm');
    $deleteForm.onsubmit = (e) => {
        e.preventDefault();

        const xhr = new XMLHttpRequest();
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
                alert('정상적으로 회원탈퇴 되었습니다.\n그동안 이용해 주셔서 감사합니다.');
                location.href='/logout';
            } else {
                alert(`알 수 없는 이유로 회원가입에 실패하였습니다. 잠시 후 다시 시도해 주세요.`);
            }
        };
        xhr.open('DELETE', 'user/cancel-user');
        xhr.send();
        Loading.show();
    }
}

// 비밀번호 변경
{
    const $changePasswordForm = document.getElementById('changePasswordForm');
    $changePasswordForm.onsubmit = (event) => {
        event.preventDefault();
        const $beforePasswordLabel = $changePasswordForm.findLabel('beforePassword');
        const $newPasswordLabel = $changePasswordForm.findLabel('newPassword');
        const $newPasswordCheckLabel = $changePasswordForm.findLabel('newPasswordCheck');
        $beforePasswordLabel.setValid($changePasswordForm['beforePassword'].value.length >= 6 && $changePasswordForm['beforePassword'].value.length <= 50);
        $newPasswordLabel.setValid($changePasswordForm['newPassword'].value.length >= 6 && $changePasswordForm['newPassword'].value.length <= 50);
        if ($newPasswordLabel.isValid()) {
            $newPasswordCheckLabel.setValid($changePasswordForm['newPassword'].value === $changePasswordForm['newPasswordCheck'].value, '새 비밀번호가 서로 일치하지 않습니다.');
        }
        if (!$beforePasswordLabel.isValid() || !$newPasswordLabel.isValid() || !$newPasswordCheckLabel.isValid()) {
            return;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('beforePassword', $changePasswordForm['beforePassword'].value);
        formData.append('newPassword', $changePasswordForm['newPassword'].value);
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
                alert('비빌번호를 성공적으로 변경하였습니다.\n로그아웃 됩니다.\n다시 로그인 해주세요.');
                location.href='/logout';
            } else if (response['results'] === 'failure_password') {
                alert('현재 비밀번호가 일치하지 않습니다.');
            } else {
                alert(`알 수 없는 이유로 비밀번호 변경에 실패하였습니다. 잠시 후 다시 시도해 주세요.`);
            }
        };
        xhr.open('PATCH', 'user/change-password');
        xhr.send(formData);
        Loading.show(0);
    }
}

// 결제취소
{
    const $paymentCancels = document.querySelectorAll('.payment-cancel');
    for (const $paymentCancel of $paymentCancels) {
        $paymentCancel.onclick = () => {
            $paymentDeleteForm.show();
            $cover.show();
            $cover.onclick = () => {
                $cover.hide();
                $paymentDeleteForm.hide();
            }
            $paymentDeleteCancel.onclick = () => {
                $cover.hide();
                $paymentDeleteForm.hide();
            }

            const paymentId = $paymentCancel.dataset['id'];
            // const playDe = $paymentCancel.dataset['date'];

            $paymentDeleteForm.onsubmit = (e) => {
                e.preventDefault();

                const xhr = new XMLHttpRequest();
                const formData = new FormData();
                formData.append('paymentId', paymentId);
                // formData.append('playDe', playDe);
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
                        alert('결제가 취소되었습니다.');
                        location.reload();
                    } else if (response['results'] === 'failure_expired') {
                        alert('지난 영화는 취소가 불가능 합니다.');
                    } else if (response['results'] === 'failure') {
                        alert('결제에 취소에 실패하였습니다. 잠시 후 다시 시도해 주세요.');
                    } else {
                        alert('알 수 없는 이유로 결제 취소에 실패하였습니다. 잠시 후 다시 시도해 주세요.');
                    }
                };
                xhr.open('DELETE', '/booking/cancel-payment');
                xhr.send(formData);
                Loading.show(0);
            }
        }
    }
}
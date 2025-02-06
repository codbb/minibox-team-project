const $recoverPassForm = document.getElementById('recoverPassForm');

$recoverPassForm.onsubmit = (e) => {
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
        console.log(response);
        if (response['results'] === 'success') {
            alert('입력하신 이메일로 비밀번호를 재설정할 수 있는 링크를 포함한 메일을 전송하였습니다');
            location.href='/';
        } else if (response['results'] === 'failure') {
            alert('입력하신 이메일과 일치하는 계정 정보를 찾을 수 없습니다.');
        } else {
            alert('서버가 알 수 없는 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('POST', `/user/recover-password?email=${$recoverPassForm['email'].value}`);
    xhr.send();
    Loading.show(0);
};
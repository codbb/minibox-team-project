const $recoverForm = document.getElementById('recoverForm');
const $result = $recoverForm.querySelector(':scope > .result');

$recoverForm.onsubmit = (e) => {
    e.preventDefault();
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert('요청을 전송하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        if(response['results'] === 'success') {
            $result.innerText = `입력하신 연락처로 찾은 계정의 이메일은 ${response['email']}입니다`;
        } else if (response['results'] === 'failure') {
            $result.innerText = '입력하신 연락처와 일치하는 계정 정보를 찾을 수 없습니다.';
            $result.style.color = 'red';
        } else {
            alert('서버가 알 수 없는 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.');
        }
    };
    xhr.open('GET', `../user/recover-email?contact=${$recoverForm['contact'].value}`);
    xhr.send();
};

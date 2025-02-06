const $mainForm = document.getElementById('mainForm');

$mainForm.onsubmit = (e) => {
    e.preventDefault();

    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('selCity', $mainForm['selCity'].value);
    formData.append('name', $mainForm['name'].value);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {

            return;
        }

    };
    xhr.open('POST', location.href);
    xhr.send(formData);

}


{
    // 극장 -> 지역 클릭시 색 활성화 및 아래 하위 요소 출력
    const buttons = document.querySelectorAll(".sel-city");
    const theaterLists = document.querySelectorAll(".theater-list");
    theaterLists.forEach((list) => (list.style.display = "none"));

    buttons.forEach((button, index) => { // 초기화면 서울
        if (button.textContent === "서울") {
            button.classList.add("active");
            theaterLists[index].style.display = "block";
        } else {
            theaterLists[index].style.display = "none";
        }

        button.addEventListener('click', () => { // 클릭시 이동 및 active 효과
            buttons.forEach((num) => num.classList.remove("active"));

            button.classList.add("active");

            theaterLists.forEach((list) => (list.style.display = "none"));

            const targetTheaterList = theaterLists[index];
            targetTheaterList.style.display = "block";
        });
    });
}


// {
//     // 나의 선호극장 정보 옆에 있는 로그인하기 버튼을 눌러 로그인
//     const $moveLogin = document.getElementById('moveLogin');
//     $moveLogin.onclick = (e) => {
//         e.preventDefault();
//         // 커버 클릭하면 로그인창과 커버 닫기
//         $cover.onclick = () => {
//             $cover.hide();
//             $loginForm.hide();
//         }
//         // 닫기 버튼 클릭하면 로그인창과 커버 닫기
//         $close.onclick = () => {
//             $cover.hide();
//             $loginForm.hide();
//         }
//         $cover.show();
//         $loginForm.reset(); // 필드 초기화 ( 닫기하고 다시 열면 내용이 남아있어서 )
//         $loginForm.show();
//         $loginForm['email'].focus();
//     }
// }
// 영화 검색 기능
const $searchMovieNm = document.querySelector('.all-movie-search');
const $movieSearchBtn = document.getElementById('btnAllMovieSearch');
$movieSearchBtn.addEventListener('click', () => {
    // 영화 이름을 쿼리 문자열로 전달
    const inputMovieNm = $searchMovieNm.value.trim(); // 입력값
    console.log(inputMovieNm)
    location.href = `/movie?searchText=${encodeURIComponent(inputMovieNm)}`;
});

// 영화 좋아요 기능
const $movieList = document.querySelector('.main-movie-list');
const $movieListItems = $movieList.querySelectorAll('li');
const $isLogin = document.querySelector('.btn-util > input[name="isLogin"]'); // 로그인 여부
const $userNick = document.getElementById("userNick"); // 유저 닉네임
function isUserLogin() {
    return $isLogin.value === "true";
} // 로그인 체크 함수
$movieListItems.forEach((li) => {
    const $mainMovieId = li.querySelector('.btn-util > input[name="movieId"]'); // 영화 Id
    const $mainLikeBtn = li.querySelector('.btn-util > .btn-like');
    const $heartIcon = li.querySelector('.btn-util > .btn-like > .ico-heart-toggle-gray');
    const $mainLikeCnt = li.querySelector('.btn-util > .btn-like > .like-cnt');

    // 로그인 시 찜한 영화 하트 아이콘 활성화
    if (isUserLogin()) {
        const xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                return;
            }
            // 로그인한 유저의 찜한 영화 목록
            const likedMovies = JSON.parse(xhr.responseText);
            // 찜한 영화 목록에서 현재 영화가 있는지 확인
            if (likedMovies.includes(parseInt($mainMovieId.value))) {
                $heartIcon.classList.add('on');
            }
        };
        xhr.open('GET', `../?userId=${$userNick.value}`);
        xhr.send();
    }

    $mainLikeBtn.addEventListener('click', function () {
        const isLiked = $heartIcon.classList.contains('on'); // 현재 하트 상태 (찜 여부)
        const currentLikeCount = parseInt($mainLikeCnt.innerText.trim(), 10); // 현재 좋아요 수

        // 로그인 여부 확인
        if (!isUserLogin()) {
            alert('로그인 후 이용해 주세요.');
            return;
        }

        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('movieId', $mainMovieId.value);
        formData.append('userId', $userNick.value);
        // 현재 상태의 반대값 전송
        if (!isLiked) {
            formData.append('like', 'true')
        } else {
            formData.append('like', 'false')
        }

        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                alert('요청 처리 중 오류가 발생했습니다. 잠시 후 다시 시도해 주세요.');
                return;
            }
            if ($mainMovieId.value <= 0) {
                alert('유효하지 않은 입력값입니다.')
                return;
            }
            // 서버 응답 성공 시 업데이트
            $heartIcon.classList.toggle('on');
            if (!isLiked) {
                $mainLikeCnt.innerText = currentLikeCount + 1;
            } else {
                $mainLikeCnt.innerText = Math.max(currentLikeCount - 1, 0);
            }
        };
        xhr.open('POST', `../`);
        xhr.send(formData);

    });
});


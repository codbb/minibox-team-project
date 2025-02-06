// 로그인 상태 체크
const $isLoggedIn = document.getElementById('isLoggedIn').value;
function isUserLoggedIn() {
    return $isLoggedIn === "true";
}
const $userNick = document.getElementById("userNick")
// 경로 배열
const paths = ['', '/comingsoon', '/domestic', '/overseas', '/film'];

const $movieList = document.querySelector('.movieList')
const $movieListItems = $movieList.querySelectorAll("li");
$movieListItems.forEach((li) => {

    // 영화 개봉 상태에 따른 예매 버튼
    const $prdtStatButton = li.querySelector("input[name='stat-button']");
    const $movieStat = $prdtStatButton.value;
    const $statBtnList = li.querySelector(".statBtnList");
    const statBtnLi = $statBtnList.children;
    // 모든 자식 요소 순회
    Array.from(statBtnLi).forEach((child) => {
        if (child.classList.contains($movieStat)) {
            // $movieStat 값을 클래스로 가진 요소는 보이기
            child.style.display = "block";
        } else {
            // 나머지 안 보이기
            child.style.display = "none";
        }
    });

    // 예매 버튼 클릭 시 빠른 예매로 넘어가기
    const $bokdBtns = li.querySelectorAll('.bokdBtn');
    $bokdBtns.forEach((bokdBtn) => {
        bokdBtn.addEventListener('click', (e) => {
            e.preventDefault();
            e.stopPropagation();
            const $movieNm = li.querySelector('input[name="search-movieNm"]');
            const movieNm = $movieNm.value;
            console.log(movieNm);

            // 영화 이름을 쿼리 문자열로 전달
            location.href = `/booking/?movieNm=${encodeURIComponent(movieNm)}`;
        });
    });

    // 영화 포스터에 마우스를 올렸을 때 영화 줄거리 정보 띄우기
    const $poster = li.querySelector(".movie-list-info");
    let isHovered = false; // 포스터가 마우스를 올린 상태인지 추적하는 변수

    $poster.addEventListener("mouseenter", () => {
        if (!isHovered) {
            const $movieScore = li.querySelector(".movie-score");
            $movieScore.classList.add("on");
            $movieScore.style.opacity = "1";
            $movieScore.style.transform = "translateY(0px)";
            isHovered = true; // 마우스를 올린 상태로 설정
        }
    });
    $poster.addEventListener("mouseleave", (e) => {
        const $movieScore = li.querySelector(".movie-score");

        // 마우스가 포스터 영역을 벗어난 경우에만 opacity 0을 설정
        if (!e.relatedTarget || !$poster.contains(e.relatedTarget)) {
            $movieScore.classList.remove("on");
            $movieScore.style.opacity = "0";
            $movieScore.style.transform = "translateY(10px)";
            isHovered = false; // 마우스를 뗀 상태로 설정
        }
    });

    // 영화 찜하기
    const $movieLikeBtn = li.querySelector('.btn-util > .btn-like');
    const $movieLikeCount = li.querySelector('.btn-like > .movieLikeCnt');
    const $heartIcon = li.querySelector('.ico-heart-toggle-gray');
    const $movieIdInput = li.querySelector("input[name='movie-id']");
    const movieId = $movieIdInput.value;

    // 로그인 시 찜한 영화 하트 아이콘 활성화
    if (isUserLoggedIn()) {
        paths.forEach((path) => {
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
                if (likedMovies.includes(parseInt(movieId))) {
                    $heartIcon.classList.add('on');
                }
            };
            xhr.open('GET', `../movie${path}?userId=${$userNick.value}`);
            xhr.send();
        })
    }

    $movieLikeBtn.addEventListener('click', function () {
        const isLiked = $heartIcon.classList.contains('on'); // 현재 하트 상태 (찜 여부)
        const currentLikeCount = parseInt($movieLikeCount.innerText.trim(), 10); // 현재 좋아요 수

        // 로그인 여부 확인
        if (!isUserLoggedIn()) {
            alert('로그인 후 이용해 주세요.');
            return;
        }

        paths.forEach((path) => {
            const xhr = new XMLHttpRequest();
            const formData = new FormData();
            formData.append('movieId', movieId);
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
                if (movieId <= 0) {
                    alert('유효하지 않은 입력값입니다.')
                    return;
                }
                // 서버 응답 성공 시 업데이트
                $heartIcon.classList.toggle('on');
                if (!isLiked) {
                    $movieLikeCount.innerText = currentLikeCount + 1;
                } else {
                    $movieLikeCount.innerText = Math.max(currentLikeCount - 1, 0);
                }
            };
            xhr.open('POST', `../movie${path}`);
            xhr.send(formData);
        })
    });
});

//----------------------------------------------------------------------------------------
// 검색 기능
const $movieSearch = document.querySelector('.movie-search');
const $searchButton = $movieSearch.querySelector('.btn-search-input');
const $searchInput = $movieSearch.querySelector('.input-text');
const $listNoResult = document.getElementById('noDataDiv');
const $searchCount = document.getElementById('totCnt');
// 초기 영화 갯수
$searchCount.innerText = `${$movieListItems.length}`;
// 검색 버튼 클릭 이벤트
$searchButton.addEventListener('click', function () {
    const $gridItem = document.querySelector('div.movie-list');
    $gridItem.style.display = 'grid';
    const movieName = $searchInput.value.trim(); // 입력 값 가져오기

    // 검색 값이 비어 있으면 초기 상태로 복원
    if (movieName === '') {
        $searchCount.innerText = `${$movieListItems.length}`;
        $movieListItems.forEach((li, index) => {
            li.style.display = 'block';
            li.style.marginTop = index >= 4 ? '60px' : '0';
            $movieList.style.display = 'block';
            $listNoResult.style.display = 'none';
        });

        return; // 더 이상 검색 로직 실행 안 함
    }

    // 검색 대상 List 안에 영화들의 input 태그 value 가져오기 (해당 영화 리스트의 영화 제목들)
    const movieItems = $movieList.querySelectorAll('li input[name="search-movieNm"]');
    const movieNames = Array.from(movieItems).map(input => input.value);

    // 검색어와 비교
    const matchingMovies = movieNames.filter(name => name.includes(movieName));

    // 초기 상태로 복원
    $movieListItems.forEach((li, index) => {
        li.style.display = 'block';
        li.style.marginTop = index >= 4 ? '60px' : '0';
        $movieList.style.display = 'block';
        $listNoResult.style.display = 'none';
    });

    if (matchingMovies.length > 0) {
        $searchCount.innerText = `${matchingMovies.length}`;
        $movieListItems.forEach(li => {
            const input = li.querySelector('input[name="search-movieNm"]');
            const movieValue = input ? input.value : '';
            if (movieValue.includes(movieName)) {
                li.style.display = 'block'; // 검색 조건에 맞는 li 표시
            } else {
                li.style.display = 'none'; // 검색 조건에 맞지 않는 li 숨기기
            }
        });

        // 필터링된 li 태그에 margin-top 적용
        const visibleItems = Array.from($movieListItems).filter(li => li.style.display !== 'none');
        visibleItems.forEach((li, index) => {
            if (index >= 4) {
                li.style.marginTop = '60px';
            } else {
                li.style.marginTop = '0';
            }
        });
    } else { // 검색 결과가 존재하지 않을 경우
        $searchCount.innerText = `0`;
        $movieList.style.display = 'none';
        $listNoResult.style.display = 'block';
    }
});

// 개봉작만 보기
const $onAirBtn = document.querySelector('.onair-condition > .btnOnAir');
const $prdtStat = $movieList.querySelectorAll('li input[name="search-prdtStatNm"]');
const $movieStats = Array.from($prdtStat).map(input => input.value);

$onAirBtn.addEventListener('click', function () {
    $onAirBtn.classList.toggle('on');
    if ($onAirBtn.classList.contains('on')) {
        // 개봉 여부 판단
        const $onAirMovies = $movieStats.filter(stat => !stat.includes('예정') && !stat.includes('준비') && !stat.includes('기타'));
        $searchCount.innerText = `${$onAirMovies.length}`;
        $movieListItems.forEach(li => {
            const $statItem = li.querySelector('input[name="search-prdtStatNm"]');
            const $statValue = $statItem ? $statItem.value : '';
            if (!$statValue.includes('예정') && !$statValue.includes('준비') && !$statValue.includes('기타')) {
                li.style.display = 'block'; // 검색 조건에 맞는 li 표시
            } else {
                li.style.display = 'none'; // 검색 조건에 맞지 않는 li 숨기기
            }
        });

        // 필터링된 li 태그에 margin-top 적용
        const visibleItems = Array.from($movieListItems).filter(li => li.style.display !== 'none');
        visibleItems.forEach((li, index) => {
            if (index >= 4) {
                li.style.marginTop = '60px';
            } else {
                li.style.marginTop = '0';
            }
        });
    } else {
        $searchCount.innerText = `${$movieListItems.length}`;
        $movieListItems.forEach(li => {
            li.style.display = 'block';
        });
    }
});

// 개봉일순, 가나다순으로 보기 (아직 덜 함)
const $openDtBtn = document.querySelector('.btnOpenDt');
const $titleBtn = document.querySelector('.btnTitle');
$openDtBtn.addEventListener('click', function () {
    $titleBtn.classList.remove('on');
    $openDtBtn.classList.add('on');
});
$titleBtn.addEventListener('click', function () {
    $openDtBtn.classList.remove('on');
    $titleBtn.classList.add('on');
});

//---------------------------------------------------------------------------------------
// 전체영화 검색기능
const $movieResultList = document.querySelector('.movie-list-result');
const $divMovieList = document.querySelector('.movie-list');
const $movieListUtil = document.querySelector('.movie-list-util');
const $movieResultListUtil = document.querySelector('.movie-list-util-result');
const $searchAllMovieCount = document.getElementById('allSearchTotCnt');
const $onAirBtnAllMovie = document.querySelector('.onair-condition > .btnOnAir-allMovie');
if ($movieResultList) {
    const $movieResultListItems = $movieResultList.querySelectorAll("li");
    $movieResultListItems.forEach((li) => {

        // 영화 개봉 상태에 따른 예매 버튼
        const $prdtStatButton = li.querySelector("input[name='stat-button']");
        const $movieStat = $prdtStatButton.value;
        const $statBtnList = li.querySelector(".statBtnList");
        const statBtnLi = $statBtnList.children;
        // 모든 자식 요소 순회
        Array.from(statBtnLi).forEach((child) => {
            if (child.classList.contains($movieStat)) {
                // $movieStat 값을 클래스로 가진 요소는 보이기
                child.style.display = "block";
            } else {
                // 나머지 안 보이기
                child.style.display = "none";
            }
        });

        // 예매 버튼 클릭 시 빠른 예매로 넘어가기
        const $bokdBtns = li.querySelectorAll('.bokdBtn');
        $bokdBtns.forEach((bokdBtn) => {
            bokdBtn.addEventListener('click', (e) => {
                e.preventDefault();
                e.stopPropagation();
                const $movieNm = li.querySelector('input[name="search-movieNm"]');
                const movieNm = $movieNm.value;
                console.log(movieNm);

                // 영화 이름을 쿼리 문자열로 전달
                location.href = `/booking/?movieNm=${encodeURIComponent(movieNm)}`;
            });
        });

        // 영화 포스터에 마우스를 올렸을 때 영화 줄거리 정보 띄우기
        const $poster = li.querySelector(".movie-list-info");
        let isHovered = false; // 포스터가 마우스를 올린 상태인지 추적하는 변수

        $poster.addEventListener("mouseenter", () => {
            if (!isHovered) {
                const $movieScore = li.querySelector(".movie-score");
                $movieScore.classList.add("on");
                $movieScore.style.opacity = "1";
                $movieScore.style.transform = "translateY(0px)";
                isHovered = true; // 마우스를 올린 상태로 설정
            }
        });
        $poster.addEventListener("mouseleave", (e) => {
            const $movieScore = li.querySelector(".movie-score");

            // 마우스가 포스터 영역을 벗어난 경우에만 opacity 0을 설정
            if (!e.relatedTarget || !$poster.contains(e.relatedTarget)) {
                $movieScore.classList.remove("on");
                $movieScore.style.opacity = "0";
                $movieScore.style.transform = "translateY(10px)";
                isHovered = false; // 마우스를 뗀 상태로 설정
            }
        });

        // 영화 찜하기
        const $movieLikeBtn = li.querySelector('.btn-util > .btn-like');
        const $movieLikeCount = li.querySelector('.btn-like > .movieLikeCnt');
        const $heartIcon = li.querySelector('.ico-heart-toggle-gray');
        const $movieIdInput = li.querySelector("input[name='movie-id']");
        const movieId = $movieIdInput.value;

        // 로그인 시 찜한 영화 하트 아이콘 활성화
        if (isUserLoggedIn()) {
            paths.forEach((path) => {
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
                    if (likedMovies.includes(parseInt(movieId))) {
                        $heartIcon.classList.add('on');
                    }
                };
                xhr.open('GET', `../movie${path}?userId=${$userNick.value}`);
                xhr.send();
            })
        }

        $movieLikeBtn.addEventListener('click', function () {
            const isLiked = $heartIcon.classList.contains('on'); // 현재 하트 상태 (찜 여부)
            const currentLikeCount = parseInt($movieLikeCount.innerText.trim(), 10); // 현재 좋아요 수

            // 로그인 여부 확인
            if (!isUserLoggedIn()) {
                alert('로그인 후 이용해 주세요.');
                return;
            }

            paths.forEach((path) => {
                const xhr = new XMLHttpRequest();
                const formData = new FormData();
                formData.append('movieId', movieId);
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
                    if (movieId <= 0) {
                        alert('유효하지 않은 입력값입니다.')
                        return;
                    }
                    // 서버 응답 성공 시 업데이트
                    $heartIcon.classList.toggle('on');
                    if (!isLiked) {
                        $movieLikeCount.innerText = currentLikeCount + 1;
                    } else {
                        $movieLikeCount.innerText = Math.max(currentLikeCount - 1, 0);
                    }
                };
                xhr.open('POST', `../movie${path}`);
                xhr.send(formData);
            })
        });
    });

    // 메인 페이지에서 전체영화 검색 버튼 클릭 시, 입력된 검색어 받기
    document.addEventListener('DOMContentLoaded', () => {
        // URL 에서 영화 이름 가져오기
        const params = new URLSearchParams(window.location.search);
        const searchText = params.get('searchText');

        if (searchText && searchText !== '') {
            // 초기 영화 갯수
            $searchAllMovieCount.innerText = `${$movieResultListItems.length}`;

            // 기존 박스오피스 영화 리스트 숨기고 전체 영화 리스트 나타내기
            $divMovieList.style.display = 'none';
            $movieListUtil.style.display = 'none';
            $movieResultList.style.display = 'grid';
            $movieResultListUtil.style.display = 'block';

            // 검색 대상 List 안에 영화들의 input 태그 value 가져오기 (해당 영화 리스트의 영화 제목들)
            const movieItems = $movieResultList.querySelectorAll('li input[name="search-movieNm"]');
            const movieNames = Array.from(movieItems).map(input => input.value);

            // 검색어와 비교
            const matchingAllMovies = movieNames.filter(name => name.includes(searchText));

            // 초기 상태로 복원
            $movieResultListItems.forEach((li, index) => {
                li.style.display = 'block';
                li.style.marginTop = index >= 4 ? '60px' : '0';
                $movieResultList.style.display = 'grid';
                $listNoResult.style.display = 'none';
            });

            if (matchingAllMovies.length > 0) {
                $searchAllMovieCount.innerText = `${matchingAllMovies.length}`;
                $movieResultListItems.forEach(li => {
                    const input = li.querySelector('input[name="search-movieNm"]');
                    const movieValue = input ? input.value : '';
                    if (movieValue.includes(searchText)) {
                        li.style.display = 'block'; // 검색 조건에 맞는 li 표시
                    } else {
                        li.style.display = 'none'; // 검색 조건에 맞지 않는 li 숨기기
                    }
                });

                // 필터링된 li 태그에 margin-top 적용
                const visibleItems = Array.from($movieResultListItems).filter(li => li.style.display !== 'none');
                visibleItems.forEach((li, index) => {
                    if (index >= 4) {
                        li.style.marginTop = '60px';
                    } else {
                        li.style.marginTop = '0';
                    }
                });
            } else { // 검색 결과가 존재하지 않을 경우
                $searchAllMovieCount.innerText = `0`;
                $movieResultList.style.display = 'none';
                $listNoResult.style.display = 'block';
            }
        }
    });

    // 전체영화 중에서 개봉작만
    const $allMoviePrdtStat = $movieResultList.querySelectorAll('li input[name="search-prdtStatNm"]');
    const $allMovieStats = Array.from($allMoviePrdtStat).map(input => input.value);
    $onAirBtnAllMovie.addEventListener('click', function () {
        $onAirBtnAllMovie.classList.toggle('on');
        if ($onAirBtnAllMovie.classList.contains('on')) {
            // 개봉 여부 판단
            const $onAirAllMovies = $allMovieStats.filter(stat => !stat.includes('예정') && !stat.includes('준비') && !stat.includes('기타'));
            $searchAllMovieCount.innerText = `${$onAirAllMovies.length}`;

            Array.from($movieResultListItems).filter(li => li.style.display !== 'none').forEach((li, index) => {
                if (index >= 4) {
                    li.style.marginTop = '60px';
                } else {
                    li.style.marginTop = '0';
                }
            });

            const filteredLi = Array.from($movieResultListItems).filter(li => li.style.display !== 'none');
            filteredLi.forEach((li) => {
                const $statItem = li.querySelector('input[name="search-prdtStatNm"]');
                const $statValue = $statItem ? $statItem.value : '';
                if (!$statValue.includes('예정') && !$statValue.includes('준비') && !$statValue.includes('기타')) {
                    li.style.display = 'block'; // 검색 조건에 맞는 li 표시
                } else {
                    li.style.display = 'none'; // 검색 조건에 맞지 않는 li 숨기기
                }
            });
            $searchAllMovieCount.innerText = `${Array.from($movieResultListItems).filter(li => li.style.display !== 'none').length}`;

            // 필터링된 li 태그에 margin-top 적용
            const allMoviesVisibleItems = Array.from($movieResultListItems).filter(li => li.style.display !== 'none');
            allMoviesVisibleItems.forEach((li, index) => {
                if (index >= 4) {
                    li.style.marginTop = '60px';
                } else {
                    li.style.marginTop = '0';
                }
            });
        }
        else {
            $searchAllMovieCount.innerText = `${$movieResultListItems.length}`;
            $movieResultListItems.forEach(li => {
                li.style.display = 'block';
            });
        }
    });
}

//------------------------------------------------------------------------------------------
// 영화 포스터가 없는 경우 준비중 포스터 띄우기
const $noImages = document.querySelectorAll('.poster');
$noImages.forEach(($img) => {
    if (!$img.getAttribute('src')) { // src 속성이 없거나 비어있는 경우
        $img.setAttribute('src', '/assets/images/common/bg/bg-noimage-notmain.png'); // 기본 이미지 설정
        const $screenTypeDiv = $img.nextElementSibling; // 바로 다음 형제 요소 선택
        if ($screenTypeDiv && $screenTypeDiv.tagName === 'DIV') { // 형제가 <div>인지 확인
            $screenTypeDiv.style.display = 'none'; // 해당 <div> 숨기기
        }
    }
});



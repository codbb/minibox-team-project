// 로그인 상태 체크
const $isLoggedIn = document.getElementById('isLoggedIn').value;

function isUserLoggedIn() {
    return $isLoggedIn === "true";
}

const $userNick = document.getElementById("userNick")
const $movieCd = document.getElementById("movieCd")

// 영화 찜 버튼 클릭 이벤트
const $movieId = document.getElementById('movieId');
const $likeBtn = document.querySelector('.btn-util > .btn-like');
const $likeCount = document.querySelector('.like-cnt');
// 유저가 찜한 영화인지 확인
if (isUserLoggedIn()) {
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
        if (likedMovies.includes(parseInt($movieId.value))) {
            $likeBtn.classList.add('on');
        }
    };
    xhr.open('GET', `../movie-detail/like?userId=${$userNick.value}`);
    xhr.send();
}
$likeBtn.addEventListener('click', function () {
    const isLiked = $likeBtn.classList.contains('on'); // 현재 하트 상태 (찜 여부)
    const currentLikeCount = parseInt($likeCount.innerText.trim(), 10); // 현재 좋아요 수
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('movieId', $movieId.value);
    // 로그인 여부 확인
    if (isUserLoggedIn()) {
        formData.append('userId', $userNick.value);
    } else {
        alert('로그인 후 이용해 주세요.');
    }
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
        if ($movieId.value <= 0) {
            alert('유효하지 않은 입력값입니다.')
            return;
        }
        // 서버 응답 성공 시 업데이트
        $likeBtn.classList.toggle('on');
        if (!isLiked) {
            $likeCount.innerText = currentLikeCount + 1;
        } else {
            $likeCount.innerText = Math.max(currentLikeCount - 1, 0);
        }
    };
    xhr.open('POST', `../movie-detail/like`);
    xhr.send(formData);
});

// 공유하기 버튼 클릭 이벤트
const shareWrap = document.getElementById('shareWrap');
document.querySelector('.sns-share').addEventListener('click', (event) => {
    event.preventDefault();
    if (shareWrap.style.display === 'block') {
        shareWrap.style.display = 'none';
    } else {
        shareWrap.style.display = 'block';
    }
});

// 예매 버튼 클릭 시 빠른 예매로 넘어가기
const $reserveBtn = document.querySelector('div.reserve > a.reserve.btn');
$reserveBtn.addEventListener('click', (e) => {
    e.preventDefault();
    e.stopPropagation();
    const $movieNm = document.getElementById('movieNm');
    const movieNm = $movieNm.value;
    // 영화 이름을 쿼리 문자열로 전달
    location.href = `/booking/?movieNm=${encodeURIComponent(movieNm)}`;
});

// 탭 이동
const $contentData = document.getElementById('contentData');
const $tabList = $contentData.querySelectorAll(':scope > .inner-wrap > .tab-list > ul > li');
const $defaultTab = document.querySelector('.default-tab');
const $commentTab = document.querySelector('.comment-tab');
const $infoContent = document.querySelectorAll('div.infoContent');
const $oneContent = document.querySelectorAll('div.oneContent');
const $movieStillList = document.querySelector('div.movie-detail-poster');

if ($defaultTab.classList.contains('on')) {
    if (!$movieStillList.classList.contains('hidden')) {
        $movieStillList.classList.add('hidden');
    }
}

$tabList.forEach(item => {
    item.addEventListener('click', event => {
        event.preventDefault(); // 기본 동작 방지
        event.stopPropagation(); // 이벤트 전파 중단

        // 모든 리스트에서 'on' 클래스 제거
        $tabList.forEach(li => li.classList.remove('on'));
        // 클릭된 리스트에 'on' 클래스 추가
        item.classList.add('on');

        // 선택된 탭에 따라 컨텐츠 표시
        if ($defaultTab.classList.contains('on')) {
            $infoContent.forEach(content => {
                if (content.classList.contains('hidden')) {
                    content.classList.remove('hidden');
                }
            });
            $oneContent.forEach(content => {
                if (content.classList.contains('hidden')) {
                    content.classList.remove('hidden');
                }
            });
            if (!$movieStillList.classList.contains('hidden')) {
                $movieStillList.classList.add('hidden');
            }
        } else if ($commentTab.classList.contains('on')) {
            $infoContent.forEach(content => {
                if (!content.classList.contains('hidden')) {
                    content.classList.add('hidden');
                }
            });
            $oneContent.forEach(content => {
                if (content.classList.contains('hidden')) {
                    content.classList.remove('hidden');
                }
            });
            if (!$movieStillList.classList.contains('hidden')) {
                $movieStillList.classList.add('hidden');
            }
        } else {
            $infoContent.forEach(content => {
                if (!content.classList.contains('hidden')) {
                    content.classList.add('hidden');
                }
            });
            $oneContent.forEach(content => {
                if (!content.classList.contains('hidden')) {
                    content.classList.add('hidden');
                }
            });
            if ($movieStillList.classList.contains('hidden')) {
                $movieStillList.classList.remove('hidden');
            }
        }
    });
});


// 관람평 달기
const $reviewList = $contentData.querySelector(':scope > .inner-wrap > .movie-idv-story > .reviewList');
// reviewList 첫 번째, 두 번째 자식 <li> 요소 유지
const $firstReviewItem = $reviewList.querySelector(':scope > li:first-child');
const $wrtReview = $reviewList.querySelector(':scope > li.wrtReview');

// 관람평 쓰기 버튼 클릭 이벤트
// 관람평 실제 작성
const $wrtReviewBox = document.getElementById('wrtReviewBox');
const tooltipLayer = document.getElementById('tooltip-layer');
// 로그인 했을 때만 가능하도록
if (isUserLoggedIn()) {
    document.querySelector('.wrtReviewBtn').addEventListener('click', (event) => {
        event.preventDefault();
        if ($wrtReviewBox.style.display === 'block') {
            $wrtReviewBox.style.display = 'none';
        } else {
            $wrtReviewBox.style.display = 'block';
        }
    });
}
// 로그아웃 상태에서는 로그인 바로가기
else if (!isUserLoggedIn()) {
    document.querySelector('.wrtReviewBtn').addEventListener('click', (event) => {
        event.preventDefault();
        if (tooltipLayer.style.display === 'none') {
            tooltipLayer.style.display = 'block';
        } else {
            tooltipLayer.style.display = 'none';
        }
    });
}
// 로그인 바로가기 서비스
const $loginService = tooltipLayer.querySelector(':scope > .loginTagClick > [rel="login"]')
{
    if ($loginButton != null) {
        // 로그인 버튼 클릭했을 때 로그인창과 커버를 보여준다
        $loginService.onclick = (event) => {
            event.preventDefault();
            // 커버 클릭하면 로그인창과 커버 닫기
            $cover.onclick = () => {
                $cover.hide();
                $loginForm.hide();
            }
            // 닫기 버튼 클릭하면 로그인창과 커버 닫기
            $close.onclick = () => {
                $cover.hide();
                $loginForm.hide();
            }
            $cover.show();
            $loginForm.reset(); // 필드 초기화 ( 닫기하고 다시 열면 내용이 남아있어서 )
            $loginForm.show();
            $loginForm['email'].focus();
            if (typeof localStorage.getItem('rememberedEmail') === 'string') {
                $loginForm['email'].value = localStorage.getItem('rememberedEmail');
                $loginForm['rememberEmail'].checked = true;
                $loginForm['password'].focus();
            }
        }
    }
}


// 관람 키워드 버튼 선택
document.addEventListener("DOMContentLoaded", () => {
    const $keywordButtons = document.querySelectorAll(".keyword-btn");

    $keywordButtons.forEach((button) => {
        button.addEventListener("click", () => {
            // 클릭된 버튼의 활성화 상태를 토글
            button.classList.toggle("active");
        });
    });
});
const $keywords = document.querySelectorAll('button[name="keyword"]')
// 선택된 관람평점 값 가져오기
const getSelectedRating = () => {
    const selectedInput = document.querySelector('input[name="rating"]:checked');
    return selectedInput ? selectedInput.value : null;
};
// 선택된 키워드 값 가져오기
const getSelectedKeywords = () => {
    const selectedKeywords = [];
    $keywords.forEach(button => {
        if (button.classList.contains('active')) {
            selectedKeywords.push(button.value);
        }
    });
    return selectedKeywords;
};

// 상대 시간 계산 함수로 관람평 작성 시간 나타내기
function getRelativeTime(dateString) {
    const now = new Date();
    const past = new Date(dateString);

    const diffInSeconds = Math.floor((now - past) / 1000);

    if (diffInSeconds < 60) {
        return `${diffInSeconds}초 전`;
    }
    const diffInMinutes = Math.floor(diffInSeconds / 60);
    if (diffInMinutes < 60) {
        return `${diffInMinutes}분 전`;
    }
    const diffInHours = Math.floor(diffInMinutes / 60);
    if (diffInHours < 24) {
        return `${diffInHours}시간 전`;
    }
    const diffInDays = Math.floor(diffInHours / 24);
    if (diffInDays < 7) {
        return `${diffInDays}일 전`;
    }
    const diffInWeeks = Math.floor(diffInDays / 7);
    if (diffInWeeks < 5) {
        return `${diffInWeeks}주 전`;
    }
    const diffInMonths = Math.floor(diffInDays / 30);
    if (diffInMonths < 12) {
        return `${diffInMonths}달 전`;
    }
    const diffInYears = Math.floor(diffInMonths / 12);
    return `${diffInYears}년 전`;
}

// 관람평 추가
const appendReview = (review) => {
    const $reviewItem = new DOMParser().parseFromString(`
                            <li class="type01 oneContentTag reviewItem">
                            <input name="reviewIndex" type="hidden" value="${review['index']}">
                                <div class="story-area">
                                    <div class="user-prof">
                                        <div class="prof-img">
                                            <img class="profile-image" alt="프로필 사진" title="프로필 사진">
                                        </div>
                                        <p class="user-id">${review['nickname']}</p>
                                    </div>
                                    <div class="story-box">
                                        <div class="story-wrap review">
                                            <div class="tit">관람평</div>
                                            <div class="story-cont">
                                                <div class="story-point"><span>${review['rating']}</span></div> <!-- 평점 -->
                                                <div class="story-recommend"><em>${review['keyword']}</em></div>          <!-- 키워드 -->
                                                <div class="story-txt">${review['content']}</div>                <!-- 관람평 내용 -->
                                                <div class="story-like">                     <!-- 추천/좋아요 -->
                                                    <button class="oneLikeBtn" title="댓글 추천" type="button">
                                                        <i class="iconset ico-like-gray"></i>
                                                        <span>${review['like']}</span> <!-- 추천 수 -->
                                                    </button>
                                                </div>
                                                <div class="story-util">  <!-- 댓글 신고 -->
                                                    <div class="post-function">
                                                        <div class="wrap">
                                                            <button class="btn-alert" type="button">옵션보기</button>
                                                            <div class="balloon-space user">       <!-- 댓글 신고 창 -->
                                                                <div class="balloon-cont">
                                                                    <div class="user">
                                                                        <p class="reset a-c">
                                                                            스포일러 및 욕설/비방하는
                                                                            <br>내용이 있습니까?
                                                                        </p>
                                                                        <button type="button" class="maskOne">댓글 신고
                                                                            <i class="iconset ico-arr-right-green"></i>
                                                                        </button>
                                                                    </div>
                                                                    <div class="btn-close">
                                                                        <a href="#" title="닫기">
                                                                            <img class="btn-close-image" alt="닫기">
                                                                        </a>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="balloon-space user-true">
                                                               <div class="balloon-cont">
                                                                 <div class="user">
                                                                   <p class="reset a-c">
                                                                     수정하고 싶은 내용이 있습니까?
                                                                   </p>
                                                                   <button type="button" class="review-delete-btn">댓글 삭제
                                                                     <i class="iconset ico-arr-right-green"></i>
                                                                   </button>
                                                                   <button type="button" class="review-modify-btn">댓글 수정
                                                                     <i class="iconset ico-arr-right-green"></i>
                                                                   </button>
                                                                 </div>
                                                                 <div class="btn-close">
                                                                   <a href="#" title="닫기">
                                                                     <img class="btn-close-image" alt="닫기">
                                                                   </a>
                                                                 </div>
                                                               </div>
                                                             </div>
                                                        </div>
                                                    </div>
                                                </div>               
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="story-date">
                                    <div><span>${getRelativeTime(review['createdAt'])}</span></div>
                                </div>
                            </li>
`, 'text/html').querySelector('li.reviewItem');
    // 프로필 이미지 URL 설정
    const profileImage = $reviewItem.querySelector('.profile-image');
    profileImage.src = '/assets/images/mypage/bg-photo.png';
    // 리뷰 좋아요 아이콘 설정
    const likeIcon = $reviewItem.querySelector('.ico-like-gray');
    likeIcon.style.backgroundImage = 'url(/assets/images/common/ico/ico-like-g.png)';
    // 리뷰 옵션보기 아이콘 설정
    const btnAlert = $reviewItem.querySelector('.btn-alert');
    btnAlert.style.background = 'url(/assets/images/common/btn/btn-alert.png) center no-repeat';
    // 댓글 신고, 수정 바로가기 화살표 아이콘 설정
    const icoArrRightGreen = $reviewItem.querySelectorAll('.ico-arr-right-green');
    icoArrRightGreen.forEach((element) => {
        element.style.backgroundImage = 'url(/assets/images/common/ico/ico-arr-right-green.png)';
    });
    // 옵션 창 닫기 버튼 이미지 URL 설정
    const arrCloseBtn = $reviewItem.querySelectorAll('.btn-close-image');
    arrCloseBtn.forEach((element) => {
        element.src = '/assets/images/common/btn/btn-close-tooltip.png';
    });

    // 리뷰 옵션보기 버튼 클릭 이벤트
    const optionBalloonSpace = $reviewItem.querySelector('.user');
    const optionBalloonSpaceUser = $reviewItem.querySelector('.user-true');
    $reviewItem.querySelector('.btn-alert').addEventListener('click', (event) => {
        event.preventDefault();
        if (isUserLoggedIn() && $userNick.value === review['nickname']) {
            optionBalloonSpaceUser.style.display = 'block';
        } else if (!isUserLoggedIn() || $userNick.value !== review['nickname']) {
            optionBalloonSpace.style.display = 'block';
        }
    });
    // 옵션 창 닫기 버튼 클릭 이벤트
    const $closeButtons = $reviewItem.querySelectorAll('.btn-close'); // 모든 닫기 버튼 선택
    $closeButtons.forEach((button) => {
        button.addEventListener('click', (event) => {
            event.preventDefault();
            if (optionBalloonSpace.style.display === 'block') {
                optionBalloonSpace.style.display = 'none';
            }
            if (optionBalloonSpaceUser.style.display === 'block') {
                optionBalloonSpaceUser.style.display = 'none';
            }
        });
    });

    return $reviewList.append($reviewItem);
};

// 관람평 삭제
const deleteReview = (reviewIndex, reviewElement) => {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }

        if (xhr.status >= 200 && xhr.status < 300) {
            // 성공적으로 삭제되면 DOM 에서 관람평 제거
            reviewElement.remove();
            alert('관람평이 삭제되었습니다.');
        } else {
            alert('관람평을 삭제하는 중 오류가 발생했습니다. 다시 시도해 주세요.');
        }
    };

    // DELETE 요청 설정
    xhr.open('DELETE', `../movie-detail/comment/?index=${reviewIndex}`);
    xhr.send();
};
    // 삭제 버튼 이벤트 리스너
const addDeleteEventListeners = () => {
    const deleteButtons = document.querySelectorAll('.review-delete-btn');
    deleteButtons.forEach((button) => {
        button.addEventListener('click', (event) => {
            event.preventDefault();

            // 삭제 확인 메시지 표시
            const confirmDelete = confirm('정말로 이 관람평을 삭제하시겠습니까?');
            if (!confirmDelete) return;

            // 삭제 대상 관람평 요소 및 index 가져오기
            const reviewElement = button.closest('li.reviewItem');
            const reviewIndex = reviewElement.querySelector('input[name="reviewIndex"]').value;

            deleteReview(reviewIndex, reviewElement); // 삭제 요청 함수 호출
        });
    });
};

// 관람평 수정
const modifyReview = () => {
    const $reviewModifyBtn = document.querySelector('.review-modify-btn');
    $reviewModifyBtn.addEventListener('click', () => {

    });
};

// 관람평 불러오기
const loadReview = () => {
    const url = new URL(location.href);
    // 기존 리스트 초기화 (1,2 번째 아이템은 유지)
    $reviewList.innerHTML = '';
    if ($firstReviewItem) {
        $reviewList.append($firstReviewItem);
    }
    // 로그인 상태일 경우에만 리뷰 작성 폼 추가
    if (isUserLoggedIn() && $wrtReview) {
        $reviewList.append($wrtReview);
    }
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status === 200) {
            const response = JSON.parse(xhr.responseText)
            response.forEach((review) => {
                appendReview(review) // 관람평 DOM 에 추가
            });
            addDeleteEventListeners(); // 삭제 버튼 이벤트 리스너 추가

        } else if (xhr.status === 404) {
            console.log('관람평이 존재하지 않습니다.');
        } else {
            alert('관람평 정보를 불러오지 못하였습니다. 잠시 후 다시 시도해 주세요.');
        }

    };
    xhr.open('GET', `../movie-detail/comment?&movieCd=${url.searchParams.get('movieCd')}`);
    xhr.send();
};
loadReview();

// 관람평 제출
const $reviewForm = document.getElementById("reviewForm");
const postReview = () => {
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('movieCd', $movieCd.value);
    if (isUserLoggedIn()) {
        formData.append('nickname', $userNick.value);
    } else {
        alert('로그인 후 이용해 주세요.');
        return;
    }
    // 선택된 관람 평점 추가
    const rating = getSelectedRating();
    if (rating) {
        formData.append('rating', rating);
    } else {
        alert('관람 평점을 선택해 주세요.');
        return;
    }
    // 선택된 키워드 추가
    const keywords = getSelectedKeywords();
    formData.append('keyword', keywords.join(',')); // 키워드를 쉼표로 구분하여 추가
    // 관람평 내용 추가
    formData.append('content', $reviewForm['content'].value);

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            alert('관람평을 작성하지 못하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }
        $reviewForm['rating'].value = '';
        $reviewForm['keyword'].value = '';
        $reviewForm['content'].value = '';
        $reviewForm['content'].focus();
        loadReview();
    };
    xhr.open('POST', '../movie-detail/comment');
    xhr.send(formData);
};

// 관람평 작성 후 '작성' 버튼을 클릭하였을 때
if (isUserLoggedIn()) {
    $reviewForm.onsubmit = (e) => {
        e.preventDefault();
        postReview();
        // 괌람평 제출 후 점수, 키워드, 내용 초기화
        $keywords.forEach(button => {
            if (button.classList.contains('active')) {
                button.classList.remove('active');
            }
        });
        const selectedRating = document.querySelector('input[name="rating"]:checked');
        if (selectedRating) {
            selectedRating.checked = false;
        }
    }
}



// 날짜를 나타나게 하는 함수

const scheduleList = [];

document.addEventListener("DOMContentLoaded", function () {
    const prevBtn = document.getElementById("prevBtn");
    const nextBtn = document.getElementById("nextBtn");
    const dateWrapper = document.getElementById("dateWrapper");

    const today = new Date();
    const daysToShow = 14; // 한 번에 보여줄 날짜 수
    let currentStartDate = new Date(today);

    // 요일 배열 (일 ~ 토)
    const weekdays = ["일", "월", "화", "수", "목", "금", "토"];

    // 현재 날짜와 비교해서 이전으로 이동 못하게 설정
    function isPastToday(date) {
        // 오늘 날짜보다 과거 날짜인지 비교 (시각은 제외하고, 날짜만 비교)
        const today = new Date();
        today.setHours(0, 0, 0, 0);  // 시간을 00:00:00으로 설정
        date.setHours(0, 0, 0, 0);    // 시간을 00:00:00으로 설정
        return date < today;  // 오늘보다 작은 날짜일 경우 true
    }


    function renderDates(startDate) {
        dateWrapper.innerHTML = ""; // 기존 날짜 제거
        for (let i = 0; i < daysToShow; i++) {
            const date = new Date(startDate);
            date.setDate(startDate.getDate() + i);

            const day = date.getDate();
            const weekday = weekdays[date.getDay()];

            // 날짜 버튼 생성
            const button = document.createElement("button");
            button.className = "time-box";
            button.innerHTML = `<div>${weekday}</div><div>${day}</div>`;
            button.dataset.date = date.toISOString().split("T")[0];
            button.style.width = "75px";
            if (i === 0) button.classList.add("active"); // 첫 번째 날짜 활성화

            // 요일별 색상 적용
            if (date.getDay() === 6) {
                button.classList.add("saturday"); // 토요일
            } else if (date.getDay() === 0) {
                button.classList.add("sunday"); // 일요일
            }


            // 날짜가 현재 날짜보다 이전이라면 클릭 못하게 비활성화
            if (isPastToday(date)) {
                button.disabled = true; // 날짜가 과거일 경우 비활성화
                button.classList.add("disabled");
            }

            button.addEventListener("click", () => {
                // 기존 활성화된 버튼의 클래스 제거
                document.querySelectorAll(".time-box").forEach((btn) =>
                    btn.classList.remove("active")
                );
                // 클릭된 버튼 활성화
                button.classList.add("active");
                // renderSchedule(scheduleList);  // scheduleList는 모든 상영 시간표 데이터 배열
                loadSchedules();
            });
            dateWrapper.appendChild(button);
        }

        // prevBtn 버튼을 현재 날짜와 비교하여 비활성화 처리
        if (isPastToday(currentStartDate)) {
            prevBtn.disabled = true;
            prevBtn.classList.add("disabled");
        } else {
            prevBtn.disabled = false;
            prevBtn.classList.remove("disabled");
        }
    }

    // 이전 버튼 클릭
    prevBtn.addEventListener("click", () => {
        // currentStartDate가 현재 날짜보다 이전으로 설정되지 않도록 방지
        if (!isPastToday(currentStartDate)) {
            currentStartDate.setDate(currentStartDate.getDate() - daysToShow);
            renderDates(currentStartDate);
        }
    });

    // 다음 버튼 클릭
    nextBtn.addEventListener("click", () => {
        currentStartDate.setDate(currentStartDate.getDate() + daysToShow);
        renderDates(currentStartDate);
    });

    // 초기 날짜 렌더링
    renderDates(currentStartDate);

    // 날짜 선택 후 상영시간표 렌더링
    // renderSchedule(scheduleList);  // 선택된 날짜에 맞는 상영 시간표 렌더링
});

// 영화 선택 버튼 클릭 시 동작
document.querySelectorAll('.movie-choice .btn').forEach(button => {
    button.addEventListener('click', function () {
        // 모든 버튼에서 'on' 클래스 제거
        document.querySelectorAll('.movie-choice .btn').forEach(btn => {
            btn.classList.remove('on');
        });

        // 클릭된 버튼에 'on' 클래스 추가
        this.classList.add('on');

        // 영화가 선택되었을 때 처리할 추가 작업을 여기서 할 수 있습니다.
        // 예: 선택된 영화 정보를 보여주기
        document.getElementById('choiceMovieList').style.display = 'block';
        document.getElementById('choiceMovieNone').style.display = 'none';

        loadSchedules();
    });
});


// 극장 지역 선택시 하위 지점을 나타내는 함수
{
    const areaButtons = document.querySelectorAll(".btn.area-btn");
    const theaterLists = Array.from(document.querySelectorAll(".depth.theater-list"));
    theaterLists.forEach((button) => {
        button.style.display = "none";
    });

    const theaterButtons = theaterLists.reduce((buttons, list) => buttons.concat(Array.from(list.querySelectorAll('.btn'))), []);
    theaterButtons.forEach((theaterButton) => {
        theaterButton.onclick = () => {
            // 이전에 선택된 극장에서 "active" 클래스 제거
            theaterButtons.forEach((btn) => btn.classList.remove("active"));
            // 클릭된 극장 버튼에 "active" 클래스 추가
            theaterButton.classList.add("active");

            loadSchedules();
        };
    });

    areaButtons.forEach((button, index) => {
        if (button.classList.contains("on")) {
            const targetTheaterButton = theaterLists[index];
            targetTheaterButton.style.display = "block";
        }

        button.addEventListener('click', () => {
            areaButtons.forEach((num) => num.classList.remove("on"));

            button.classList.add("on");

            theaterLists.forEach((btn) => (btn.style.display = "none"));

            const targetTheaterButton = theaterLists[index];
            targetTheaterButton.style.display = "block";

            // 예전꺼
            // theaterButtons.forEach((theaterButton) => {
            //     const theaterBtn = theaterButton.querySelectorAll(".btn");
            //     theaterBtn.forEach((button) => {
            //         button.onclick = () => {
            //             // 이전에 선택된 극장에서 "active" 클래스 제거
            //             theaterBtn.forEach((btn) => btn.classList.remove("active"));
            //             // 클릭된 극장 버튼에 "active" 클래스 추가
            //             button.classList.add("active");
            //
            //             // const brchno = theaterButton.dataset['brchNo'];
            //             // const $brchNoValue = document.getElementById('brchNo');
            //             // $brchNoValue.value = theaterButton.dataset['data-brchno'];
            //
            //             // data-brchno 값을 가져와서
            //             const brchNoValue = button.getAttribute('data-brchno');
            //
            //             // <input> 요소에 값 설정
            //             const inputElement = document.getElementById('brchNo');
            //             inputElement.value = brchNoValue;
            //
            //             loadSchedules();
            //         };
            //     });
            // });
            // 예전꺼
        });
    });
}

// 시간 버튼 이동
// 한 번에 이동할 거리 (px 단위)
const moveDistance = 150;  // 원하는 만큼 크기를 조정할 수 있습니다 (예: 420px, 600px, 등)

// 이동할 최대 위치 (오른쪽으로 이동할 수 있는 범위)
const minLeft = -520;  // 오른쪽으로 이동할 수 있는 최대 범위
const maxLeft = 0;     // 왼쪽으로 이동할 수 있는 최소 범위

document.querySelector('.btn-prev-time').addEventListener('click', function() {
    const view = document.querySelector('.view');
    let left = parseInt(getComputedStyle(view).left);

    // 왼쪽으로 이동할 수 있는 경우만 이동 (left가 maxLeft보다 작은 경우)
    if (left < maxLeft) {
        view.style.left = (left + moveDistance) + 'px'; // moveDistance만큼 왼쪽으로 이동
    }
});

document.querySelector('.btn-next-time').addEventListener('click', function() {
    const view = document.querySelector('.view');
    let left = parseInt(getComputedStyle(view).left);

    // 오른쪽으로 이동할 수 있는 경우만 이동 (left가 minLeft보다 큰 경우)
    if (left > minLeft) {
        view.style.left = (left - moveDistance) + 'px'; // moveDistance만큼 오른쪽으로 이동
    }
});

// 현재 시간을 HH 형식으로 가져오기
const currentTime = new Date();
const currentHour = currentTime.getHours();  // 0부터 23까지의 시간 값

// 모든 시간 버튼 가져오기
const hourButtons = document.querySelectorAll('.hour');

// 각 버튼에 대해 opacity를 설정
hourButtons.forEach(button => {
    const buttonTime = parseInt(button.getAttribute('data-time'));  // 각 버튼의 data-time 속성 (0부터 24까지)

    // 현재 시간보다 이전 시간이라면 opacity 0.5, 아니면 opacity 1
    if (buttonTime < currentHour) {
        button.style.opacity = '0.5';  // 이전 시간
    } else {
        button.style.opacity = '1';    // 현재 시간과 이후 시간
    }
});


// 상영시간표
const loadSchedules = () => {
    const date = dateWrapper.querySelector('.time-box.active')?.dataset['date'];
    const movieNm = document.body.querySelector('.movie-choice .btn.on')?.dataset['movieid'] ?? '';
    const areaDataset = document.body.querySelector('.theater-choice .depth .btn.active')?.dataset;
    const areaNo = areaDataset?.['areano'];
    const brchNo = areaDataset?.['brchno'];
    if (date == null || areaNo == null || brchNo == null) {
        return;
    }
    const xhr = new XMLHttpRequest();
    const formData = new FormData();
    formData.append('movieNm', movieNm);
    formData.append('areaNo', areaNo);
    formData.append('brchNo', brchNo);
    formData.append('date', date);
    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }
        if (xhr.status < 200 || xhr.status >= 300) {
            // alert('!!!');
            return;
        }
        const response = JSON.parse(xhr.responseText);
        renderSchedule(response['schedules']);
        localStorage.setItem('/booking/::lastAreaNo', areaNo);
        localStorage.setItem('/booking/::lastBrchNo', brchNo);
    };
    xhr.open('POST', '/booking/schedule');
    xhr.send(formData);
};

const $loginBtn = document.querySelector('.login-button');

const $quickReserveInclude = document.querySelector('.quick-reserve-include');
const $seatContainer = document.querySelector('.seat-container');

// 전역 변수로 선택된 스케줄 정보를 저장
let selectedSchedule = null;

function renderSchedule(schedules) {
    const container = document.getElementById('playScheduleList');
    container.innerHTML = '';  // 기존의 콘텐츠를 초기화 (새로 고침 시 이전 내용 제거)

    if (!schedules || schedules.length === 0) {
        container.innerHTML = '<p>상영시간표가 없습니다.</p>';
        return;
    }

    if (schedules.length === 0) {
        container.innerHTML = '<p>선택된 날짜에 해당하는 상영시간표가 없습니다.</p>';
        return;
    }

    // // 날짜별로 상영 시간표를 분류
    // schedules.forEach(schedule => {
    //     if (!schedulesByDate[schedule.playDe]) {
    //         schedulesByDate[schedule.playDe] = [];
    //     }
    //     schedulesByDate[schedule.playDe].push(schedule);
    // });
    //
    // const container = document.getElementById('playScheduleList');
    // container.innerHTML = '';  // 기존의 콘텐츠를 초기화 (새로 고침 시 이전 내용 제거)
    //
    // const filteredSchedules = schedulesByDate[selectedDate] || [];
    //
    // if (filteredSchedules.length === 0) {
    //     container.innerHTML = '<p>선택된 날짜에 해당하는 상영시간표가 없습니다.</p>';
    //     return;
    // }


    // 현재 시간 가져오기 (HH:mm 형식)
    // const currentTime = new Date();
    // const currentHour = currentTime.getHours();
    // const currentMinute = currentTime.getMinutes();
    // const formattedCurrentTime = `${currentHour}:${currentMinute < 10 ? '0' + currentMinute : currentMinute}`;
    // 현재 날짜 가져오기 (최종)
    const currentDate = new Date();
    const currentYear = currentDate.getFullYear();
    const currentMonth = currentDate.getMonth() + 1;  // 월은 0부터 시작하므로 +1
    const currentDay = currentDate.getDate();
    const currentHour = currentDate.getHours();
    const currentMinute = currentDate.getMinutes();
    const formattedCurrentDate = `${currentYear}-${currentMonth < 10 ? '0' + currentMonth : currentMonth}-${currentDay < 10 ? '0' + currentDay : currentDay}`;
    const formattedCurrentTime = `${currentHour}:${currentMinute < 10 ? '0' + currentMinute : currentMinute}`;

    // 상영시간이 현재 시간 이후인 영화만 필터링
    // const filteredSchedules = schedules.filter(schedule => {
    //     return schedule.playStartTime >= formattedCurrentTime;  // 상영 시작 시간이 현재 시간보다 이후인 경우
    // });
    // const filteredSchedules = schedules;
    // 오늘 날짜인지 확인
    const isToday = schedules.some(schedule => {
        const scheduleDate = schedule.playDe.split(" ")[0];  // 상영시간에서 날짜만 추출

        const scheduleTime = schedule.playStartTime.split(" ")[0];  // 상영 시간만 추출 (HH:mm)

        // console.log("상영 날짜", scheduleDate);
        // console.log("상영 시간", scheduleTime);
        // console.log("오늘 날짜", formattedCurrentDate);
        return scheduleDate === formattedCurrentDate;  // 오늘 날짜와 비교
    });

    // 오늘 날짜인 경우에만 필터링 적용
    let filteredSchedules = schedules;

    if (isToday) {
        filteredSchedules = schedules.filter(schedule => {
            const scheduleDate = schedule.playDe.split(" ")[0];  // 상영 날짜
            const scheduleTime = schedule.playStartTime.split(" ")[0];  // 상영 시간 (HH:mm)

            // 상영 날짜가 오늘인 경우만 필터링
            if (scheduleDate === formattedCurrentDate) {
                const [scheduleHour, scheduleMinute] = scheduleTime.split(':').map(Number);

                // 상영 시작 시간과 오늘 날짜를 결합하여 Date 객체로 생성
                const scheduleDateTime = new Date(currentYear, currentMonth - 1, currentDay, scheduleHour, scheduleMinute);

                console.log(`상영 시작 시간: ${scheduleDateTime}`);
                console.log(`현재 시간: ${currentDate}`);

                // 현재 시간과 비교하여, 현재 시간 이후의 상영만 표시
                return scheduleDateTime > currentDate;
            }
            return true;
        });
    }


    // 필터링된 상영시간표가 없으면 메시지 출력
    if (filteredSchedules.length === 0) {
        container.innerHTML = '<p>선택된 시간 이후의 상영시간표가 없습니다.</p>';
        return;
    }

    // schedules.forEach(schedule => {
    filteredSchedules.forEach(schedule => {
        const liElement = document.createElement('li');  // 각 상영 시간을 담을 li 요소

        const button = document.createElement('button');
        button.onclick = () => {
            const xhr = new XMLHttpRequest();
            xhr.onreadystatechange = () => {
                if (xhr.readyState !== XMLHttpRequest.DONE) {
                    return;
                }
                if (xhr.status < 200 || xhr.status >= 300) {
                    alert('??')
                    return;
                }
                const seats = JSON.parse(xhr.responseText);
                seatWrapper.innerHTML = '';
                for (const seat of seats) {
                    const $input = document.createElement('button');
                    $input.innerText = seat['seatNo'];
                    $input.name = 'seats';
                    $input.type = 'button';
                    $input.value = seat['id'];
                    $input.dataset.id = seat['seatNo'];
                    $input.classList.add('seat');
                    if (seat['booked'] === true) {
                        $input.setAttribute('disabled', '');
                    } else {
                        $input.onclick = (e) => {
                            const seatValue = e.target.value;
                            const seatIndex = selectedSeats.indexOf(seatValue);

                            const seatDataId = e.target.dataset.id;
                            const seatViewNameIndex = selectedSeatsView.indexOf(seatDataId);

                            // 이미 선택된 좌석을 클릭하면 선택 해제
                            if (seatIndex !== -1) {
                                e.target.classList.remove('clicked');
                                selectedSeats.splice(seatIndex, 1); // 배열에서 삭제
                                selectedSeatsView.splice(seatViewNameIndex, 1);
                            }
                            // 아직 선택되지 않은 좌석을 클릭하면 선택
                            else {
                                if (selectedSeats.length < 4) {  // 최대 4개 좌석까지만 선택 가능
                                    e.target.classList.add('clicked');
                                    selectedSeats.push(seatValue);
                                    selectedSeatsView.push(seatDataId);
                                } else {
                                    alert('최대 4개 좌석까지만 선택 가능합니다.');
                                }
                            }

                            // 선택된 좌석을 결제 화면에 표시
                            // document.querySelector('.view-seat').innerText = `선택된 좌석: ${selectedSeats.join(', ')}`;
                            document.querySelector('.view-seat').innerText = `${selectedSeatsView.join(', ')}`;

                            // 결제 폼의 'seatNo'에 선택된 좌석 정보 입력
                            document.querySelector('.seat-input').value = selectedSeats.join(', ');


                            // 선택된 좌석에 따른 금액 갱신
                            updateTotalAmount();
                        };
                    }
                    seatWrapper.append($input);
                }
            };
            xhr.open('GET', `/booking/seats?scheduleId=${schedule.id}`);
            xhr.send();
        }
        button.classList.add('btn');
        button.setAttribute('type', 'button');

        button.appendChild(document.createElement('div')).textContent = '';

        // 데이터 속성 설정
        button.dataset.id = schedule.id;
        button.dataset.arr = `${schedule.movieNm},${schedule.brchNm},${schedule.theabNo},${schedule.playDe},${schedule.playStartTime},${schedule.playEndTime}`;

        // 상영 시작 시간
        const timeElement = document.createElement('span');
        timeElement.classList.add('time');

        const startTimeStrong = document.createElement('strong');
        startTimeStrong.setAttribute('title', '상영 시작');
        // / 시:분:초 형식에서 시:분까지만 추출
        const [hour, minute] = schedule.playStartTime.split(':');
        startTimeStrong.textContent = `${hour}:${minute}`;
        // startTimeStrong.textContent = schedule.playStartTime;
        timeElement.appendChild(startTimeStrong);

        timeElement.appendChild(document.createElement('span')).textContent = '~';

        const endTimeEm = document.createElement('span');
        endTimeEm.setAttribute('title', '상영 종료');
        // 시:분:초 형식에서 시:분까지만 추출
        const [endHour, endMinute] = schedule.playEndTime.split(':');
        endTimeEm.textContent = `${endHour}:${endMinute}`;
        // endTimeEm.textContent = schedule.playEndTime;
        timeElement.appendChild(endTimeEm);

        // 영화 제목 및 형식
        const titleElement = document.createElement('span');
        titleElement.classList.add('title');

        const movieStrong = document.createElement('strong');
        movieStrong.setAttribute('title', '영화 제목');
        movieStrong.textContent = schedule.movieNm;
        titleElement.appendChild(movieStrong);

        const formatEm = document.createElement('em');
        formatEm.textContent = '2D';  // 상영 포맷은 예시로 2D로 설정
        titleElement.appendChild(formatEm);

        // 극장 정보 및 좌석 정보
        const infoElement = document.createElement('span');
        infoElement.classList.add('info');

        const theaterSpan = document.createElement('span');
        theaterSpan.classList.add('theater');
        theaterSpan.setAttribute('title', '극장');
        theaterSpan.textContent = schedule.brchNm;
        infoElement.appendChild(theaterSpan);

        const theabSpan = document.createElement('span');
        theabSpan.classList.add('theab');
        theabSpan.setAttribute('title', '관');
        theabSpan.textContent = schedule.theabNo;
        infoElement.appendChild(theabSpan);

        const seatSpan = document.createElement('span');
        seatSpan.classList.add('seat');

        const nowStrong = document.createElement('strong');
        nowStrong.classList.add('now');
        nowStrong.setAttribute('title', '잔여좌석');
        seatSpan.appendChild(nowStrong);

        seatSpan.appendChild(document.createElement('span')).textContent = '/';

        const allEm = document.createElement('em');
        allEm.classList.add('all');
        allEm.setAttribute('title', '전체좌석');
        seatSpan.appendChild(allEm);

        infoElement.appendChild(seatSpan);

        // 최종적으로 버튼에 모든 요소 추가
        button.appendChild(timeElement);
        button.appendChild(titleElement);
        button.appendChild(infoElement);

        // li 요소에 버튼 추가
        liElement.appendChild(button);

        // li 요소에 클릭 이벤트 리스너 추가
        liElement.addEventListener('click', () => {
            if ($loginBtn != null) {
                $cover.show();
                $loginForm.show();
                $cover.onclick = () => {
                    $cover.hide();
                    $loginForm.hide();
                }
                $close.onclick = () => {
                    $cover.hide();
                    $loginForm.hide();
                }
                return;
            }
            $quickReserveInclude.hide();
            $seatContainer.show();
            // 선택된 상영 정보를 객체로 저장
            selectedSchedule = {
                id: button.dataset.id,
                arr: button.dataset.arr.split(','),
                movieNm: schedule.movieNm,
                brchNm: schedule.brchNm,
                theabNo: schedule.theabNo,
                playDe: schedule.playDe,
                playStartTime: schedule.playStartTime,
                playEndTime: schedule.playEndTime,
            };

            // 선택된 스케줄을 콘솔에 출력
            // console.log(schedule);
            // console.log(selectedSchedule.movieNm);
            // console.log("선택된 상영 시간표:", selectedSchedule);

            document.querySelector('.getMovieNms').textContent = selectedSchedule.movieNm;
            document.querySelector('.getBrchNm').textContent = selectedSchedule.brchNm;
            document.querySelector('.getTheabNo').textContent = selectedSchedule.theabNo;
            document.querySelector('.getPlayDe').textContent = selectedSchedule.playDe;
            document.querySelector('.getPlayStartTime').textContent = selectedSchedule.playStartTime;
            document.querySelector('.getPlayEndTime').textContent = selectedSchedule.playEndTime;

            // 선택된 스케줄 정보를 input 필드에 담기
            const scheduleInput = document.querySelector('.schedule-input');
            scheduleInput.value = `${selectedSchedule.id}`;  // 원하는 형식으로 데이터 설정
        });

        // container에 li 요소 추가
        container.appendChild(liElement);
    });
}


const $paymentContainer = document.querySelector('.payment-container');
const $paymentBtn = document.querySelector('.payment-btn');
const $paymentBtnBack = document.querySelector('.payment-btn-back');

const $paymentForm = document.getElementById("paymentForm");
const $paymentFormBtn = document.querySelector(".payment-form-btn");

const $bookingContainer = document.querySelector('.booking-container');

$paymentBtn.onclick = () => {
    $cover.onclick = () => {
        $cover.hide();
        $paymentForm.hide();
    }
    $paymentForm.show();
    $cover.show();
}
$paymentBtnBack.onclick = () => {
    $paymentContainer.hide();
    $seatContainer.show();
}

$paymentFormBtn.onclick = () => {
    $paymentContainer.hide();
    $cover.hide();
    $bookingContainer.show();
}

// // 1좌석당 가격 설정 (10,000원)
const seatPrice = 10000;

// 선택된 좌석을 저장할 배열
let selectedSeats = [];
let selectedSeatsView = [];

// 좌석을 담을 wrapper
const seatWrapper = document.querySelector('.seat-wrapper');

// // 금액 표시를 위한 요소
const totalAmountElement = document.querySelector('.total-amount');

// // 좌석 이름을 "A1", "A2" 등으로 매핑하는 함수
// function mapping(input, i, j) {
//     const rowNames = ['A', 'B', 'C', 'D', 'E', 'F'];  // 행 이름 (A, B, C)
//     // input.value = rowNames[i] + (j + 1);  // 'A1', 'A2', ... 형식으로 값 설정
//     // input.textContent = input.value;  // 버튼에 텍스트로 좌석 이름 추가
//     input.textContent = rowNames[i] + (j + 1);
//     input.value = i * 6 + (j + 1);
// }

const $seatBtn = document.querySelector('.seat-btn');
const $seatBtnBack = document.querySelector('.seat-btn-back');
$seatBtn.onclick = () => {
    $seatContainer.hide();
    $paymentContainer.show();
}
$seatBtnBack.onclick = () => {
    $seatContainer.hide();
    $quickReserveInclude.show();
}

function updateTotalAmount() {
    const totalAmount = selectedSeats.length * seatPrice;
    totalAmountElement.textContent = `총 금액 : ${totalAmount.toLocaleString()}원`
    const $charge = document.querySelector('.charge');
    const $chargeInput = document.querySelector('.charge-input');
    $charge.value = totalAmount;
    $chargeInput.value = totalAmount;
}

// // 결제 폼 전송 처리
// const $bookingForm = document.getElementById('bookingForm');
// $bookingForm.onsubmit = (e) => {
//     e.preventDefault();
//
//     const xhr = new XMLHttpRequest();
//     const formData = new FormData();
//     formData.append('scheduleId', $bookingForm['scheduleId'].value);
//     formData.append('seatId', $bookingForm['seatIds'].value);
//     formData.append('charge', $bookingForm['charge'].value);
//
//     // const seatt = $bookingForm['seatId'].value.split(", ");
//     // console.log(seatt);
//
//     xhr.onreadystatechange = () => {
//         if (xhr.readyState !== XMLHttpRequest.DONE) {
//             return;
//         }
//         Loading.hide();
//         if (xhr.status < 200 || xhr.status >= 300) {
//             alert('요청을 전송하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
//             return;
//         }
//         const response = JSON.parse(xhr.responseText);
//         if (response['results'] === 'success') {
//             alert('결제를 완료했습니다.');
//             location.href = '/my';
//         } else if (response['results'] === 'failure_unsigned') {
//             alert('로그인 되어 있지 않습니다. 로그인 해주세요.');
//         } else {
//             alert('결제에 실패했습니다.');
//         }
//     };
//     xhr.open('POST', location.href);
//     xhr.send(formData);
//     Loading.show(0);
// }

// 결제 폼 전송 처리 (AJAX 방식으로 서버에 데이터 전송)
const $bookingForm = document.getElementById('bookingForm');
$bookingForm.onsubmit = (e) => {
    e.preventDefault();  // 기본 폼 제출 동작 방지

    // const selectedSeats = [];
    const seatInputs = document.querySelectorAll(".seat-input"); // 좌석 선택 input 요소들

    seatInputs.forEach(function(input) {
        if (input.checked) {
            selectedSeats.push(input.value);  // 체크된 좌석의 값을 배열에 추가
        }
    });

    console.log(selectedSeats)

    // 선택된 좌석이 없다면, 에러 메시지 처리
    if (selectedSeats.length === 0) {
        alert("좌석을 선택해 주세요.");
        event.preventDefault(); // 폼 전송을 막음
        return;
    }

    // 선택된 좌석이 최대 4개를 초과하면 에러 처리
    if (selectedSeats.length > 4) {
        alert("최대 4개의 좌석만 예매할 수 있습니다.");
        event.preventDefault(); // 폼 전송을 막음
        return;
    }

    // 선택된 좌석들의 ID를 쉼표로 구분된 문자열로 만듦
    const seatIdsInput = document.querySelector("input[name='seatIds']");
    seatIdsInput.value = selectedSeats.join(','); // 배열을 쉼표로 구분된 문자열로 변환하여 숨겨진 입력 필드에 저장

    // FormData 객체 생성
    const formData = new FormData();
    formData.append('scheduleId', $bookingForm['scheduleId'].value);  // 예매할 영화 일정 ID
    formData.append('seatIds', $bookingForm['seatIds'].value);  // 선택된 좌석 ID들
    formData.append('charge', $bookingForm['charge'].value);  // 결제 금액

    // XMLHttpRequest 객체 생성 (AJAX 요청)
    const xhr = new XMLHttpRequest();

    xhr.onreadystatechange = () => {
        if (xhr.readyState !== XMLHttpRequest.DONE) {
            return;
        }

        // 로딩 상태를 숨김
        Loading.hide();

        if (xhr.status < 200 || xhr.status >= 300) {
            alert('요청을 전송하는 도중 오류가 발생하였습니다. 잠시 후 다시 시도해 주세요.');
            return;
        }

        const response = JSON.parse(xhr.responseText);

        // 서버 응답에 따른 처리
        if (response['results'] === 'success') {
            alert('결제를 완료했습니다.');
            location.href = '/my';  // 예매 완료 후 마이 페이지로 리다이렉트
        } else if (response['results'] === 'failure_unsigned') {
            alert('로그인 되어 있지 않습니다. 로그인 해주세요.');
        } else {
            alert('결제에 실패했습니다.');
        }
    };

    // AJAX 요청 보내기
    xhr.open('POST', location.href);
    xhr.send(formData);

    // 로딩 상태 표시 (잠시 동안)
    Loading.show(0);
};

// { // 이전의 선택한것을 기록을 남기는것
//     if (localStorage.getItem('/booking/::lastAreaNo') != null && localStorage.getItem('/booking/::lastBrchNo') != null) {
//         const areaNo = localStorage.getItem('/booking/::lastAreaNo');
//         const brchNo = localStorage.getItem('/booking/::lastBrchNo');
//         const $brch = quickCity.querySelector(`.btn[data-areano="${areaNo}"][data-brchno="${brchNo}"]`);
//         if ($brch) {
//             const $area = $brch.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.querySelector(':scope > .btn');
//             $area.dispatchEvent(new Event('click'));
//             $brch.dispatchEvent(new Event('click'));
//             loadSchedules();
//
//             $brch.classList.add('active');
//             $brch.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.style.display = 'block';
//             $brch.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.querySelector(':scope > .btn').classList.add('on');
//         }
//     }
// }

// 영화 페이지에서 예매 버튼 클릭 시, 영화이름 활성화
document.addEventListener('DOMContentLoaded', () => {
    // URL 에서 영화 이름 가져오기
    const params = new URLSearchParams(window.location.search);
    const movieNm = params.get('movieNm');
    // console.log(movieNm)

    if(movieNm != null) {
        // 해당 영화 이름 버튼 활성화
        const $bookingMovieNmBtn = document.querySelector(`button[data-movieid="${movieNm}"]`);
        $bookingMovieNmBtn.classList.add('on');
    }
});

function fetchSchedule() {
    const inquiryPlayDate = document.getElementById("playDate").value; // 사용자가 입력한 날짜
    const brchNo = document.getElementById("brchNo").value; // 사용자가 입력한 날짜
    fetch("/fetchSchedule", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ playDate: inquiryPlayDate, brchNo: brchNo})
    })
        .then(response => response.json())
        .then(data => {
            if (data.result === "SUCCESS") {
                alert("상영 일정이 성공적으로 DB에 저장되었습니다.");
            } else {
                alert("상영 일정 저장에 실패했습니다.");
            }
        })
        .catch(error => console.error("Error:", error));
}
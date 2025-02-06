// const $container = document.getElementById('container');
//
// $container.onsubmit = (e) => {
//     e.preventDefault();
//     const xhr = new XMLHttpRequest();
//     xhr.onreadystatechange = () => {
//         if (xhr.readyState !== XMLHttpRequest.DONE) {
//             return;
//         }
//         if (xhr.status < 200 || xhr.status >= 300) {
//
//             return;
//         }
//
//     };
//     xhr.open('POST', location.href);
//     xhr.send();
// }
//
// // const date = new Date();
// // // console.log(date.getFullYear());
// // const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
// // const reserveDate = document.querySelector(".reserve-date");
// //
// //
// // const weekOfDay = ["일", "월", "화", "수", "목", "금", "토"]
// // const year = date.getFullYear();
// // const month = date.getMonth();
// // for (i = date.getDate(); i <= lastDay.getDate(); i++) {
// //
// //     const button = document.createElement("button");
// //     const spanWeekOfDay = document.createElement("span");
// //     const spanDay = document.createElement("span");
// //
// //     //class넣기
// //     button.classList = "movie-date-wrapper"
// //     spanWeekOfDay.classList = "movie-week-of-day";
// //     spanDay.classList = "movie-day";
// //
// //     //weekOfDay[new Date(2020-03-날짜)]
// //     const dayOfWeek = weekOfDay[new Date(year + "-" + month + "-" + i).getDay()];
// //
// //     //요일 넣기
// //     if (dayOfWeek === "토") {
// //         spanWeekOfDay.classList.add("saturday");
// //         spanDay.classList.add("saturday");
// //     } else if (dayOfWeek === "일") {
// //         spanWeekOfDay.classList.add("sunday");
// //         spanDay.classList.add("sunday");
// //     }
// //     spanWeekOfDay.innerHTML = dayOfWeek;
// //     button.append(spanWeekOfDay);
// //     //날짜 넣기
// //     spanDay.innerHTML = i;
// //     button.append(spanDay);
// //     //button.append(i);
// //     reserveDate.append(button);
// //
// //     dayClickEvent(button);
// // }
// //
// // function dayClickEvent(button) {
// //     button.addEventListener("click", function() {
// //         const movieDateWrapperActive = document.querySelectorAll(".movie-date-wrapper-active");
// //         movieDateWrapperActive.forEach((list) => {
// //             list.classList.remove("movie-date-wrapper-active");
// //         })
// //         button.classList.add("movie-date-wrapper-active");
// //     })
// // }
//
//
// // 현재 날짜를 yyyyMMdd 형식으로 반환하는 함수
// const getCurrentDate = () => {
//     const today = new Date();
//     const year = today.getFullYear();
//     const month = String(today.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
//     const day = String(today.getDate()).padStart(2, '0'); // 날짜가 한 자릿수일 경우 0으로 채움
//
//     return `${year}${month}${day}`;
// };
//
// // 데이터를 받아오는 함수
// // const loadData = () => {
// //     const currentDate = getCurrentDate(); // 현재 날짜 가져오기
// //     // const url = `https://megabox.co.kr/on/oh/ohc/Brch/schedulePage.do?brchNo1=&crtDe=&detailType=area&firstAt=N&masterType=brch&playDe=${currentDate}`;
// //     const url = `https://megabox.co.kr/on/oh/ohb/SimpleBooking/selectBokdList.do`;
// //
// //
// //     // fetch를 사용하여 데이터를 받아오기
// //     fetch(url)
// //         .then(response => {
// //             // HTTP 상태 코드가 200번대가 아닌 경우 오류를 던짐
// //             if (!response.ok) {
// //                 throw new Error('네트워크 응답에 문제가 발생하였습니다.');
// //             }
// //             return response.json(); // JSON 파싱
// //         })
// //         .then(data => {
// //             // displayData(data.megaMap.movieFormList); // 데이터를 화면에 표시
// //             // const movieCd = (new URL(location.href)).searchParams.get('movieCd');
// //             // const realBrchCode = (new URL(location.href)).searchParams.get('realBrchCode');
// //             // const brchCode = (new URL(location.href)).searchParams.get('brchCode');
// //             const date = data.megaMap.movieFormDeList;
// //             const movies = data.megaMap.movieFormList;
// //             const l = []
// //             for(const movie of movies){
// //                 const playEndTimeArray = movie.playEndTime.split(':');
// //                 let playEndTimeHour = parseInt(playEndTimeArray[0]);
// //                 if (playEndTimeHour >= 24) {
// //                     playEndTimeHour -= 24;
// //                 }
// //                 playEndTimeHour = playEndTimeHour.toString().padStart(2, '0');
// //                 playEndTimeArray[0] = playEndTimeHour;
// //
// //                 const formattedPlayDe = movie.playDe.replace(/-/g, '');
// //
// //                 const obj = {
// //                     movieNm: movie.movieNm,
// //                     // movieCd: movieCd,
// //                     // areaCd: movie.areaCd,
// //                     playDe: formattedPlayDe,
// //                     playStartTime: movie.playStartTime,
// //                     playEndTime: playEndTimeArray.join(':'),
// //                     // brchNm: movie.brchNm,
// //                     // restSeatCnt: movie.restSeatCnt,
// //                     brchNo: movie.brchNo,
// //                     theabExpoNm: movie.theabExpoNm,
// //                     // realBrchCode: realBrchCode,
// //                 }
// //                 l.push(obj);
// //                 console.log(obj);
// //             }
// //
// //             // 아래 부분 활성화시 데이터가 DB에 저장(새로 고침 할때마다 저장 되므로 유의)
// //             fetch('/booking/save',{
// //                 method: "POST",
// //                 headers: {"Content-Type": "application/json"},
// //                 body: JSON.stringify(l)
// //             }).then()
// //
// //         })
// //         .catch(error => {
// //             console.error('데이터 로드 오류 : ', error);
// //         });
// // };
// //
// // // 데이터를 서버에 저장하는 함수
// // const saveData = (data) => {
// //     fetch('/save-movie-data', {
// //         method: 'POST',
// //         headers: {
// //             'Content-Type': 'application/json'
// //         },
// //         body: JSON.stringify({movieData: data})
// //     })
// //         .then(response => response.json())
// //         .then(result => {
// //             console.log('데이터 저장 완료 : ', result);
// //         })
// //         .catch(error => {
// //             console.error('데이터 저장 오류 : ', error);
// //         });
// // };
//
//
// // 데이터를 화면에 표시하는 함수
// // const displayData = (data) => {
// //     const container = document.getElementById('data-container');
// //     container.innerHTML = '';
// //     if (Array.isArray(data) && data.length > 0) {
// //         data.forEach(item => {
// //             const movieItem = document.createElement('div');
// //             movieItem.classList.add('movie-item');
// //
// //             const movieNm = document.createElement('h3');
// //             movieNm.textContent = item.movieNm;
// //
// //             const playTime = document.createElement('p');
// //             playTime.textContent = `상영 시간: ${item.playStartTime} ~ ${item.playEndTime}`;
// //
// //             const brchNm = document.createElement('p');
// //             brchNm.textContent = `상영관: ${item.brchNm}`;
// //
// //             const restSeatCnt = document.createElement('p');
// //             restSeatCnt.textContent = `${item.restSeatCnt}석`;
// //
// //             const theabExpoNm = document.createElement('p');
// //             theabExpoNm.textContent = item.theabExpoNm;
// //
// //             const brchNo = document.createElement('p');
// //             brchNo.textContent = item.brchNo;
// //
// //             movieItem.appendChild(movieNm);
// //             movieItem.appendChild(playTime);
// //             movieItem.appendChild(brchNm);
// //             movieItem.appendChild(restSeatCnt);
// //             movieItem.appendChild(theabExpoNm);
// //             movieItem.appendChild(brchNo);
// //             container.appendChild(movieItem);
// //
// //         });
// //     } else {
// //         container.innerHTML = '데이터가 없습니다.';
// //     }
// // };
//
//
//
//
// const loadData = () => {
//     const currentDate = getCurrentDate(); // 현재 날짜 가져오기
//     const url = `https://megabox.co.kr/on/oh/ohb/SimpleBooking/selectBokdList.do?brchNo1=&crtDe=&detailType=area&firstAt=N&masterType=brch&playDe=${currentDate}`;
//
//     // fetch를 사용하여 데이터를 받아오기
//     fetch(url)
//         .then(response => {
//             // HTTP 상태 코드가 200번대가 아닌 경우 오류를 던짐
//             if (!response.ok) {
//                 throw new Error('네트워크 응답에 문제가 발생하였습니다.');
//             }
//             return response.json(); // JSON 파싱
//         })
//         .then(data => {
//             console.log(data);
//             const movieData = getDataAsArray(data);
//             const filteredData = filterDataByBrchNo(data); // brch_no에 맞게 필터링
//             insertDataToDB(filteredData); // DB에 삽입
//         })
//         .catch(error => {
//             console.error('데이터 로드 오류 : ', error);
//         });
// };
//
// // 데이터가 배열이 아닐 경우 배열로 변환하는 함수
// const getDataAsArray = (data) => {
//     if (Array.isArray(data)) {
//         return data; // 이미 배열이라면 그대로 반환
//     } else if (data && data.items && Array.isArray(data.items)) {
//         return data.items; // 예: { items: [...] } 형태일 경우
//     } else {
//         // 단일 객체일 경우 배열로 감싸서 반환
//         return [data];
//     }
// };
//
// // brch_no 조건에 맞는 데이터만 필터링하는 함수
// const filterDataByBrchNo = (data) => {
//     const filteredData = data.map(movie => {
//         const brchNo = movie.brchNo;
//
//         // theaters 테이블에 해당 brchNo가 존재하는지 백엔드에서 확인하도록 합니다.
//         if (brchNo) {
//             return {
//                 movieNm: movie.movieNm,
//                 playDe: movie.playDe,
//                 playStartTime: movie.playStartTime,
//                 playEndTime: movie.playEndTime,
//                 brchNo: movie.brchNo,
//                 theabExpoNm: movie.theabExpoNm,
//                 // 필요한 다른 필드들 추가
//             };
//         }
//         return null;
//     }).filter(item => item !== null); // null 값은 필터링하여 제거
//     return filteredData;
// };
//
// // DB에 데이터를 삽입하는 함수
// const insertDataToDB = (filteredData) => {
//     fetch('/booking/save', {
//         method: "POST",
//         headers: {"Content-Type": "application/json"},
//         body: JSON.stringify(filteredData),
//     })
//         .then(response => {
//             if (response.ok) {
//                 console.log('데이터가 성공적으로 삽입되었습니다.');
//             } else {
//                 console.error('DB 삽입 실패');
//             }
//         })
//         .catch(error => {
//             console.error('DB 삽입 오류 : ', error);
//         });
// };
//
// window.addEventListener('DOMContentLoaded', loadData);
//
// setInterval(loadData, 24 * 60 * 60 * 1000);

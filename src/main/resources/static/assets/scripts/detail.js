const {
    ClassicEditor,
    AccessibilityHelp,
    Alignment,
    Autoformat,
    AutoImage,
    Autosave,
    BlockQuote,
    Bold,
    CloudServices,
    Code,
    CodeBlock,
    Essentials,
    FindAndReplace,
    FontBackgroundColor,
    FontColor,
    FontFamily,
    FontSize,
    FullPage,
    GeneralHtmlSupport,
    Heading,
    Highlight,
    HorizontalLine,
    HtmlComment,
    HtmlEmbed,
    ImageBlock,
    ImageCaption,
    ImageInline,
    ImageInsert,
    ImageInsertViaUrl,
    ImageResize,
    ImageStyle,
    ImageTextAlternative,
    ImageToolbar,
    ImageUpload,
    Indent,
    IndentBlock,
    Italic,
    Link,
    LinkImage,
    List,
    ListProperties,
    MediaEmbed,
    Paragraph,
    PasteFromOffice,
    RemoveFormat,
    SelectAll,
    ShowBlocks,
    SimpleUploadAdapter,
    SourceEditing,
    SpecialCharacters,
    SpecialCharactersArrows,
    SpecialCharactersCurrency,
    SpecialCharactersEssentials,
    SpecialCharactersLatin,
    SpecialCharactersMathematical,
    SpecialCharactersText,
    Strikethrough,
    Style,
    Subscript,
    Superscript,
    Table,
    TableCaption,
    TableCellProperties,
    TableColumnResize,
    TableProperties,
    TableToolbar,
    TextTransformation,
    TodoList,
    Underline,
    Undo
} = CKEDITOR;

const editorConfig = {
    toolbar: {
        items: [
            'undo',
            'redo',
            '|',
            'sourceEditing',
            'showBlocks',
            'findAndReplace',
            '|',
            'heading',
            'style',
            '|',
            'fontSize',
            'fontFamily',
            'fontColor',
            'fontBackgroundColor',
            '|',
            'bold',
            'italic',
            'underline',
            'strikethrough',
            'subscript',
            'superscript',
            'code',
            'removeFormat',
            '|',
            'specialCharacters',
            'horizontalLine',
            'link',
            'insertImage',
            'mediaEmbed',
            'insertTable',
            'highlight',
            'blockQuote',
            'codeBlock',
            'htmlEmbed',
            '|',
            'alignment',
            '|',
            'bulletedList',
            'numberedList',
            'todoList',
            'outdent',
            'indent'
        ],
        shouldNotGroupWhenFull: false
    },
    plugins: [
        AccessibilityHelp,
        Alignment,
        Autoformat,
        AutoImage,
        Autosave,
        BlockQuote,
        Bold,
        CloudServices,
        Code,
        CodeBlock,
        Essentials,
        FindAndReplace,
        FontBackgroundColor,
        FontColor,
        FontFamily,
        FontSize,
        FullPage,
        GeneralHtmlSupport,
        Heading,
        Highlight,
        HorizontalLine,
        HtmlComment,
        HtmlEmbed,
        ImageBlock,
        ImageCaption,
        ImageInline,
        ImageInsert,
        ImageInsertViaUrl,
        ImageResize,
        ImageStyle,
        ImageTextAlternative,
        ImageToolbar,
        ImageUpload,
        Indent,
        IndentBlock,
        Italic,
        Link,
        LinkImage,
        List,
        ListProperties,
        MediaEmbed,
        Paragraph,
        PasteFromOffice,
        RemoveFormat,
        SelectAll,
        ShowBlocks,
        SimpleUploadAdapter,
        SourceEditing,
        SpecialCharacters,
        SpecialCharactersArrows,
        SpecialCharactersCurrency,
        SpecialCharactersEssentials,
        SpecialCharactersLatin,
        SpecialCharactersMathematical,
        SpecialCharactersText,
        Strikethrough,
        Style,
        Subscript,
        Superscript,
        Table,
        TableCaption,
        TableCellProperties,
        TableColumnResize,
        TableProperties,
        TableToolbar,
        TextTransformation,
        TodoList,
        Underline,
        Undo
    ],
    fontFamily: {
        supportAllValues: true
    },
    fontSize: {
        options: [10, 12, 14, 'default', 18, 20, 22],
        supportAllValues: true
    },
    heading: {
        options: [
            {
                model: 'paragraph',
                title: 'Paragraph',
                class: 'ck-heading_paragraph'
            },
            {
                model: 'heading1',
                view: 'h1',
                title: 'Heading 1',
                class: 'ck-heading_heading1'
            },
            {
                model: 'heading2',
                view: 'h2',
                title: 'Heading 2',
                class: 'ck-heading_heading2'
            },
            {
                model: 'heading3',
                view: 'h3',
                title: 'Heading 3',
                class: 'ck-heading_heading3'
            },
            {
                model: 'heading4',
                view: 'h4',
                title: 'Heading 4',
                class: 'ck-heading_heading4'
            },
            {
                model: 'heading5',
                view: 'h5',
                title: 'Heading 5',
                class: 'ck-heading_heading5'
            },
            {
                model: 'heading6',
                view: 'h6',
                title: 'Heading 6',
                class: 'ck-heading_heading6'
            }
        ]
    },
    htmlSupport: {
        allow: [
            {
                name: /^.*$/,
                styles: true,
                attributes: true,
                classes: true
            }
        ]
    },
    image: {
        toolbar: [
            'toggleImageCaption',
            'imageTextAlternative',
            '|',
            'imageStyle:inline',
            'imageStyle:wrapText',
            'imageStyle:breakText',
            '|',
            'resizeImage'
        ]
    },
    initialData:
        '<h2 class="tit small mt70" style="color: #503396">시설안내</h2><h3 class="tit small">보유시설</h3><div class="theater-facility"><p><img src="https://img.megabox.co.kr/static/pc/images/common/ico/ico-facility-comfort.png">컴포트</p><p><img src="https://img.megabox.co.kr/static/pc/images/common/ico/ico-facility-theater.png">일반상영관</p><p><img src="https://img.megabox.co.kr/static/pc/images/common/ico/ico-facility-disabled.png">장애인석</p></div><h3 class="tit small mt30"><br data-cke-filler="true"></h3><h3 class="tit small mt30">층별안내</h3><ul class="dot-list"><li><span class="font-gblue" style="color: #01738b;">8층 : </span> 매표소, 매점, 에스컬레이터, 엘리베이터 , 남자 · 여자 화장실, 비상계단 3 </li><li><span class="font-gblue" style="color: #01738b;">9층 : </span> 1관, 2관, 남자 · 여자 화장실, 엘리베이터, 비상계단3</li><li><span class="font-gblue" style="color: #01738b;">10층 : </span> 3관, 4관, 엘리베이터2, 남자 · 여자 화장실, 비상계단 3 </li><li><span class="font-gblue" style="color: #01738b;">11층 : </span> 5관, 6관, 7관, 엘리베이터2, 남자 · 여자 화장실, 비상계단 3 </li></ul><h2 class="tit small mt70" style="color: #503396;">교통안내</h2><h3 class="tit small">약도</h3><ul class="dot-list"><li><span class="font-gblue" style="color: #01738b;">도로명주소 : </span> 서울특별시 서초구 서초대로 77길 3 (서초동) 아라타워 8층</li></ul><div class="location-map-btn mt15"><div class="btn-group left"><a href="http://m.map.naver.com/map.nhn?lng=127.0264086&amp;lat=37.498214&amp;level=2" class="button purple" target="_blank" title="새창열림" style="color: #ffffff; background-color: #503396; border: 0; border-radius: 4px; margin: 0 3px; box-sizing: border-box; height: 30px; display: inline-block; padding: 0 15px; text-align: center; line-height: 30px;">실시간 길찾기</a></div><div class="btn-group left"><br data-cke-filler="true"></div></div><h3 class="tit small mt30">주차</h3><div class="parking-info"><div class="parking-section" style="border-radius: 10px; padding: 0 0 30px 0;"><div class="icon-box"></div><div class="tit">주차안내</div><img src="https://img.megabox.co.kr/static/pc/images/common/ico/ico-parking.png\n"><ul class="dot-list"><li>아라타워 건물 內 지하 3층 ~ 지하 6층 주차장 이용</li></ul></div></div><div class="parking-section" style="border-radius: 10px; padding: 0 0 30px 0;"><div class="icon-box"></div><div class="info"><p class="tit">주차확인</p><img src="https://img.megabox.co.kr/static/pc/images/common/ico/ico-confirm.png"><ul class="dot-list"><li>8층 주차정산기에서 티켓 하단 바코드 인증 후 할인 등록</li><li>영화 관람 시 3시간 30분 : 3천원 (이후 10분당 1천원 / 카드결제만 가능)</li></ul></div></div><div class="parking-section" style="border-radius: 10px; padding: 0 0 30px 0;"><div class="icon-box"></div><div class="info"><p class="tit">주차요금</p><img src="\thttps://img.megabox.co.kr/static/pc/images/common/ico/ico-cash.png"><ul class="dot-list"><li>건물 내 타 매장과 주차 할인 중복 적용 불가</li><li>주차 공간이 협소하므로 가급적이면 대중교통을 이용 바랍니다. (지하철 2호선 강남역 출구 9번 출구)</li></ul></div></div></div><h3 class="tit small mt30"><br data-cke-filler="true"></h3><h3 class="tit small mt30">대중교통</h3><div class="public-transportation"><div class="transportation-section" style="border-radius: 10px; padding: 0 0 30px 0;"><div class="icon-box"></div><div class="info"><p class="tit">버스</p><img src="https://img.megabox.co.kr/static/pc/images/common/ico/ico-bus.png"><ul class="dot-list"><li>140번, 144번, 145번, 146번, 360번, 402번, 420번, 440번, 441번, 452번, 470번, 741번</li><li>3412번, 4312번, 서초03번, 서초 09번 </li><li>9404번, 9408번, 9409번, 9503번, 9711번, 9500번, 9501번, 9510번, 9800번, 9801번, 9802번, 9901번, M4403번, M7412번</li></ul></div></div><div class="transportation-section" style="border-radius: 10px; padding: 0 0 30px 0;"><div class="icon-box"></div><div class="info"><p class="tit">지하철</p><img src="https://img.megabox.co.kr/static/pc/images/common/ico/ico-metro.png\n"><ul class="dot-list"><li>지하철 2호선 , 신분당선 ‘강남역 -&gt; 지하철 9번 출구 좌측 연결통로 이용</li></ul></div></div></div><br><br><h2 class="tit small">영화관람료 안내</h2><div class="fee-table-box"><div class="fee-table">\t<p class="fee-table-tit">  2D</p><div class="table-wrap"><table class="data-table a-c"><colgroup><col><col style="width:25%;"><col style="width:25%;"><col style="width:25%;"></colgroup><thead><tr><th scope="col">요일</th><th scope="col">상영시간</th><th scope="col">일반</th><th scope="col">청소년</th></tr></thead><tbody><tr><td style="text-align: center">월~일</td><td style="text-align: center">일반</td><td style="text-align: center">10,000</td><td style="text-align: center">10,000</td></tr>',

    language: 'ko',
    link: {
        addTargetToExternalLinks: true,
        defaultProtocol: 'https://',
        decorators: {
            toggleDownloadable: {
                mode: 'manual',
                label: 'Downloadable',
                attributes: {
                    download: 'file'
                }
            }
        }
    },
    list: {
        properties: {
            styles: true,
            startIndex: true,
            reversed: true
        }
    },
    placeholder: 'Type or paste your content here!',
    style: {
        definitions: [
            {
                name: 'Article category',
                element: 'h3',
                classes: ['category']
            },
            {
                name: 'Title',
                element: 'h2',
                classes: ['document-title']
            },
            {
                name: 'Subtitle',
                element: 'h3',
                classes: ['document-subtitle']
            },
            {
                name: 'Info box',
                element: 'p',
                classes: ['info-box']
            },
            {
                name: 'Side quote',
                element: 'blockquote',
                classes: ['side-quote']
            },
            {
                name: 'Marker',
                element: 'span',
                classes: ['marker']
            },
            {
                name: 'Spoiler',
                element: 'span',
                classes: ['spoiler']
            },
            {
                name: 'Code (dark)',
                element: 'pre',
                classes: ['fancy-code', 'fancy-code-dark']
            },
            {
                name: 'Code (bright)',
                element: 'pre',
                classes: ['fancy-code', 'fancy-code-bright']
            }
        ]
    },
    table: {
        contentToolbar: ['tableColumn', 'tableRow', 'mergeTableCells', 'tableProperties', 'tableCellProperties']
    },
    simpleUpload: {
        uploadUrl: './image'
    }
};


function showList(element) { // 지역 위에 마우스 올리면 하위 지점 나오게 하는 함수
    const theaterList = element.querySelector('.theater-list');
    if (theaterList) {
        theaterList.style.display = 'block';
    }
}

function hideList(element) { // 지역 위에 마우스 떼면 하위 지점 숨기게 하는 함수
    const theaterList = element.querySelector('.theater-list');
    if (theaterList) {
        theaterList.style.display = 'none';
    }
}


// Ckeditor 활용하는 함수
const $addFacilityForm = document.getElementById('addFacilityForm');

ClassicEditor.create($addFacilityForm['content'], editorConfig).then((editor) => {
    $addFacilityForm.onsubmit = (e) => {
        e.preventDefault();
        const xhr = new XMLHttpRequest();
        const formData = new FormData();
        formData.append('brchNo', (new URL(location.href)).searchParams.get('brchNo'));
        formData.append('title', $addFacilityForm['title'].value);
        formData.append('content', editor.getData());

        console.log("Form Data Branch Code:", formData.get('brchNo'));
        console.log("Form Data Title:", formData.get('title'));
        console.log("Form Data Content:", formData.get('content'));
        xhr.onreadystatechange = () => {
            if (xhr.readyState !== XMLHttpRequest.DONE) {
                return;
            }
            if (xhr.status < 200 || xhr.status >= 300) {
                alert('내용을 변경하지 못하였습니다. 잠시 후 다시 시도해주세요. :(');
                return;
            }
            const response = JSON.parse(xhr.responseText);
            console.log("Server Response:", response);
            if (response['result'] === true) {
                location.href = `/theater/detail?brchNo=${new URL(location.href).searchParams.get('brchNo')} `
            } else {
                alert('내용을 작성하지 못하였습니다. 잠시 후 다시 시도해주세요. :(');
            }
        };

        xhr.onerror = () => {
            alert('네트워크 오류가 발생하였습니다. 잠시 후 다시 시도해주세요. :(');
        };

        xhr.open('POST', '/theater/write');
        xhr.send(formData);
    };
});

// const loadData = () => {
//     const xhr = new XMLHttpRequest();
//     xhr.onreadystatechange = () => {
//         if (xhr.readyState !== XMLHttpRequest.DONE) {
//             return;
//         }
//         if (xhr.status < 200 || xhr.status >= 300) {
//
//             return;
//         }
//         const responseData = JSON.parse(xhr.responseText);
//         displayData(responseData.megaMap.movieFormList);
//     };
//     xhr.open('GET', 'https://megabox.co.kr/on/oh/ohc/Brch/schedulePage.do?brchNo1=&crtDe=&detailType=area&firstAt=N&masterType=brch&playDe=20241223');
//     xhr.send();
// };
//
// const saveData = (data) => {
//     fetch('/save-movie-data', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({movieData: data})
//     })
//         .then(response => response.json())
//         .then(result => {
//         console.log('데이터 저장 완료 : ', result);
//     })
//         .catch(error => {
//         console.error('데이터 저장 오류 : ', error);
//     })
// }
//
// const displayData = (data) => {
//     const container = document.getElementById('data-container');
//     if (Array.isArray(data) && data.length > 0) {
//         const dataHTML = data.map(item => {
//             return `
//                 <div class="movie-item">
//                     <h3>${item.movieNm}</h3>
//                     <p>상영 시간: ${item.playStartTime} - ${item.playEndTime}</p>
//                     <p>상영관: ${item.brchNm}</p>
//                     <img src="${item.moviePosterImg}" alt="${item.movieNm}" />
//                 </div>
//             `;
//         }).join('');
//         container.innerHTML = dataHTML;
//     } else {
//         container.innerHTML = '데이터가 없습니다.';
//     }
// }
// loadData();

// const loadData = () => {
//     const xhr = new XMLHttpRequest();
//     xhr.onreadystatechange = () => {
//         if (xhr.readyState !== XMLHttpRequest.DONE) {
//             return;
//         }
//         if (xhr.status < 200 || xhr.status >= 300) {
//
//             return;
//         }
//         const responseData = JSON.parse(xhr.responseText);
//         displayData(responseData.megaMap.movieFormList);
//     };
//     xhr.open('GET', 'https://megabox.co.kr/on/oh/ohc/Brch/schedulePage.do?brchNo1=&crtDe=&detailType=area&firstAt=N&masterType=brch&playDe=20241223');
//     xhr.send();
// }
// const displayData = (data) => {
//     const container = document.getElementById('data-container');
//     if (Array.isArray(data) && data.length > 0) {
//         const dataHTML = data.map(item => {
//             return `
//                 <div class="movie-item">
//                     <h3>${item.movieNm}</h3>
//                     <p>상영 시간: ${item.playStartTime} - ${item.playEndTime}</p>
//                     <p>상영관: ${item.brchNm}</p>
//                     <img src="${item.moviePosterImg}" alt="${item.movieNm}" />
//                 </div>
//             `;
//         }).join('');
//         container.innerHTML = dataHTML;
//     } else {
//         container.innerHTML = '데이터가 없습니다.';
//     }
// }
// loadData();

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
// const loadData = () => {
//     const currentDate = getCurrentDate(); // 현재 날짜 가져오기
//     const url = `https://megabox.co.kr/on/oh/ohc/Brch/schedulePage.do?brchNo1=&crtDe=&detailType=area&firstAt=N&masterType=brch&playDe=${currentDate}`;
//
//     // fetch를 사용하여 데이터를 받아오기
//     fetch(url)
//         .then(response => {
//         // HTTP 상태 코드가 200번대가 아닌 경우 오류를 던짐
//         if (!response.ok) {
//             throw new Error('네트워크 응답에 문제가 발생하였습니다.');
//         }
//         return response.json(); // JSON 파싱
//     })
//         .then(data => {
//             // displayData(data.megaMap.movieFormList); // 데이터를 화면에 표시
//             // const movieCd = (new URL(location.href)).searchParams.get('movieCd');
//             // const realBrchCode = (new URL(location.href)).searchParams.get('realBrchCode');
//             // const brchCode = (new URL(location.href)).searchParams.get('brchCode');
//             const date = data.megaMap.movieFormDeList;
//             const movies = data.megaMap.movieFormList;
//             const l = []
//             for(const movie of movies){
//                 const playEndTimeArray = movie.playEndTime.split(':');
//                 let playEndTimeHour = parseInt(playEndTimeArray[0]);
//                 if (playEndTimeHour >= 24) {
//                     playEndTimeHour -= 24;
//                 }
//                 playEndTimeHour = playEndTimeHour.toString().padStart(2, '0');
//                 playEndTimeArray[0] = playEndTimeHour;
//
//                 const formattedPlayDe = movie.playDe.replace(/-/g, '');
//
//                 const obj = {
//                     movieNm: movie.movieNm,
//                     // movieCd: movieCd,
//                     areaCd: movie.areaCd,
//                     playDe: formattedPlayDe,
//                     playStartTime: movie.playStartTime,
//                     playEndTime: playEndTimeArray.join(':'),
//                     brchNm: movie.brchNm,
//                     restSeatCnt: movie.restSeatCnt,
//                     theabExpoNm: movie.theabExpoNm,
//                     brchNo: movie.brchNo,
//                     // realBrchCode: realBrchCode,
//                 }
//                 l.push(obj);
//                 console.log(obj);
//             }
//
//             // 아래 부분 활성화시 데이터가 DB에 저장(새로 고침 할때마다 저장 되므로 유의)
//             fetch('/booking/save',{
//                 method: "POST",
//                 headers: {"Content-Type": "application/json"},
//                 body: JSON.stringify(l)
//             }).then()
//
//     })
//         .catch(error => {
//         console.error('데이터 로드 오류 : ', error);
//     });
// };
//
// // 데이터를 서버에 저장하는 함수
// const saveData = (data) => {
//     fetch('/save-movie-data', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json'
//         },
//         body: JSON.stringify({movieData: data})
//     })
//         .then(response => response.json())
//         .then(result => {
//             console.log('데이터 저장 완료 : ', result);
//         })
//         .catch(error => {
//             console.error('데이터 저장 오류 : ', error);
//     });
// };

//
// const saveMovieToDatabase = async (movie) => {
//     try {
//         const response = await fetch('http://localhost:8080/booking/save', {
//             method: 'POST',
//             headers: {
//                 'Content-Type': 'application/json'
//             },
//             body: JSON.stringify(movie)
//         });
//         if (response.ok) {
//             const data = await response.json();
//             console.log('저장된 영화 : ', data);
//         } else {
//             console.error('저장 실패 : ', response.statusText);
//         }
//     } catch (error) {
//         console.error('영화 저장 중 오류 : ', error);
//     }
// };
// saveMovieToDatabase();
//

// // 데이터를 화면에 표시하는 함수
// const displayData = (data) => {
//     const container = document.getElementById('data-container');
//     container.innerHTML = '';
//     if (Array.isArray(data) && data.length > 0) {
//         data.forEach(item => {
//             const movieItem = document.createElement('div');
//             movieItem.classList.add('movie-item');
//
//             const movieNm = document.createElement('h3');
//             movieNm.textContent = item.movieNm;
//
//             const playTime = document.createElement('p');
//             playTime.textContent = `상영 시간: ${item.playStartTime} ~ ${item.playEndTime}`;
//
//             const brchNm = document.createElement('p');
//             brchNm.textContent = `상영관: ${item.brchNm}`;
//
//             const restSeatCnt = document.createElement('p');
//             restSeatCnt.textContent = `${item.restSeatCnt}석`;
//
//             const theabExpoNm = document.createElement('p');
//             theabExpoNm.textContent = item.theabExpoNm;
//
//             movieItem.appendChild(movieNm);
//             movieItem.appendChild(playTime);
//             movieItem.appendChild(brchNm);
//             movieItem.appendChild(restSeatCnt);
//             movieItem.appendChild(theabExpoNm);
//
//             container.appendChild(movieItem);
//
//         });
//     } else {
//         container.innerHTML = '데이터가 없습니다.';
//     }
// };
//
// window.addEventListener('DOMContentLoaded', loadData);
//
// setInterval(loadData, 24 * 60 * 60 * 1000);


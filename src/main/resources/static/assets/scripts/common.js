HTMLElement.prototype.hide = function () {
    this.classList.remove('-visible')
    return this;
}

HTMLElement.prototype.show = function () {
    this.classList.add('-visible');
    return this;
}

/**
 * @param {string} dataId
 * @return {HTMLLabelElement}
 */
HTMLFormElement.prototype.findLabel = function (dataId) {
    return this.querySelector(`label.--obj-label[data-id="${dataId}"]`);
}

/**
 * @param {boolean} b
 * @param {string|undefined} warningText
 * @returns {HTMLLabelElement}
 */
HTMLLabelElement.prototype.setValid = function (b, warningText = undefined) {
    if (b === true) {
        this.classList.remove('-invalid');
    } else if (b === false) {
        this.classList.add('-invalid');
        if (typeof warningText === 'string') {
            this.querySelector(':scope > ._warning').innerText = warningText;
        }
    }
    return this;
}

/**
 * @returns {boolean}
 */
HTMLLabelElement.prototype.isValid = function () {
    return !this.classList.contains('-invalid');
}

class Loading {
    /** @type {HTMLElement} */
    static $element;

    static hide() {
        Loading.$element.hide();
    }

    /**
     * @param {number} delay
     */
    static show(delay = 50) {
        if (Loading.$element == null) {
            const $element = document.createElement('div');
            $element.classList.add('---loading');
            const $icon = document.createElement('img');
            $icon.classList.add('_icon');
            $icon.setAttribute('alt', '');
            $icon.setAttribute('src', 'data:image/png;base64,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');
            const $text = document.createElement('span');
            $text.classList.add('_text');
            $text.innerText = '잠시만 기다려 주세요.';
            $element.append($icon, $text);
            document.body.prepend($element);
            Loading.$element = $element;
        }
        setTimeout(() => Loading.$element.show(), delay);
    }
}

const $cover = document.getElementById('cover');
const $loginForm = document.getElementById('login-form');
const $header = document.getElementById('header');
const $close = $loginForm.querySelector(':scope > .login-top > .close');
const $loginButton = $header.querySelector(':scope > .content-wrap-header > .util > li > .login-button');
{
    // const $close = $loginForm.querySelector(':scope > .login-top > .close');
    if ($loginButton != null) {
        // 로그인 버튼 클릭했을 때 로그인창과 커버를 보여준다
        $loginButton.onclick = (event) => {
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

{
    // 로그인 전송
    $loginForm.onsubmit = (e) => {
        e.preventDefault();

        const xhr = new XMLHttpRequest();
        const url = new URL(location.href);
        url.pathname = '/user/';
        url.searchParams.set('email', $loginForm['email'].value);
        url.searchParams.set('password', $loginForm['password'].value);

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
                if ($loginForm['rememberEmail'].checked) {
                    localStorage.setItem('rememberedEmail', $loginForm['email'].value);
                } else {
                    localStorage.removeItem('rememberedEmail');
                }
                location.reload();
            } else if (response['results'] === 'failure_not_verified') {
                alert(`해당 계정의 이메일 인증이 완료되지 않았습니다. 이메일을 확인해 주세요.`);
            } else if (response['results'] === 'failure_deleted') {
                alert('해당 계정은 탈퇴한 상태입니다. 관리자에게 문의해 주세요.');
            } else if (response['results'] === 'failure_suspended') {
                alert('해당 계정은 정지된 상태입니다. 관리자에게 문의해 주세요.');
            } else if (response['results'] === 'failure') {
                alert('이메일 혹은 비밀번호가 올바르지 않습니다. 다시 확인해 주세요.');
            } else {
                alert('서버가 알 수 없는 응답을 반환하였습니다. 잠시 후 다시 시도해 주세요.');
            }
        };
        xhr.open('GET', url.toString());
        xhr.send();
        Loading.show(0);
    }
}

const $headerSearch = $header.querySelector('.header-search');
{
    // header 영화 검색 layer
    const $layerHeaderSearch = document.getElementById('layer-header-search');
    if ($headerSearch != null) {
        $headerSearch.addEventListener('click', (e) => {
            e.preventDefault();
            $layerHeaderSearch.classList.toggle('on');
        });
    }

    // 영화 검색 기능
    const $headerSearchInput = document.getElementById('headerMovieName');
    const $headerSearchBtn = document.getElementById('btnHeaderSearch');
    if ($headerSearchBtn != null) {
        $headerSearchBtn.addEventListener('click', () => {
            // 영화 이름을 쿼리 문자열로 전달
            const inputMovieNm = $headerSearchInput.value.trim(); // 입력값
            console.log(inputMovieNm)
            location.href = `/movie?searchText=${encodeURIComponent(inputMovieNm)}`;
        });
    }
}

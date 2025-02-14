let timerInterval = null;

// ✅ 로그인 여부 확인 (서버에서 전달된 값 사용 또는 세션 기반)
const isLoggedIn = sessionStorage.getItem('isLoggedIn') === 'true';

document.addEventListener('DOMContentLoaded', () => {
    const timerElement = document.getElementById('session-timer');
    if (sessionStorage.getItem('isLoggedIn') === 'true') {
        //console.log('로그인 상태:', sessionStorage.getItem('isLoggedIn'));

        timerElement.style.display = 'inline';
        showSessionTimerImmediately();
        startSessionTimer();
    } else {
        timerElement.style.display = 'none';
    }
    initSPA();
});

const logoutForm = document.querySelector('.logout-form');
if (logoutForm) {
    logoutForm.addEventListener('submit',() => {
        sessionStorage.clear();
    })
}

function initSPA() {
    document.querySelectorAll('[data-link]').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const url = e.target.getAttribute('href');
            history.pushState(null, '', url);
            loadPage(url);
        });
    });

    window.addEventListener('popstate', () => loadPage(location.pathname));
}

function loadPage(url) {
    fetch(url)
        .then(response => response.text())
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');

            const contentElement = document.getElementById('content');
            const newContent = doc.getElementById('content');

            if (contentElement && newContent) {
                contentElement.innerHTML = newContent.innerHTML;
            } else {
                console.warn(" 'content' 요소가 존재하지 않습니다.");
            }

            const isAuthenticated = doc.querySelector('meta[name="isAuthenticated"]');
            if (isAuthenticated) {
                const isLoggedInStatus = isAuthenticated.content === 'true';
                sessionStorage.setItem('isLoggedIn', isLoggedInStatus);

                if (isLoggedInStatus) {
                    document.getElementById('session-timer').style.display = 'inline';
                    syncSessionTimer();
                } else {
                    document.getElementById('session-timer').style.display = 'none';
                }
            }

            initSPA(); // ✅ 중복 호출 방지
        })
        .catch(err => console.error('페이지 로드 실패:', err));
}





function startSessionTimer() {
    if (timerInterval) return;

    let sessionTime = sessionStorage.getItem('sessionTime')
        ? parseInt(sessionStorage.getItem('sessionTime'))
        : 600;

    const timerElement = document.getElementById('session-timer');

    timerInterval = setInterval(() => {
        sessionTime--;
        sessionStorage.setItem('sessionTime', sessionTime);

        const minutes = Math.floor(sessionTime / 60);
        const seconds = sessionTime % 60;
        timerElement.textContent = `${minutes}분 ${seconds < 10 ? '0' : ''}${seconds}초 남음`;

        if (sessionTime === 60) showExtendButton();
        if (sessionTime <= 0) {
            clearInterval(timerInterval);
            alert('세션이 만료되었습니다. 다시 로그인하세요.');
            sessionStorage.clear();
            location.href = '/login';
        }
    }, 1000);
}

function syncSessionTimer() {
    const timerElement = document.getElementById('session-timer');
    let sessionTime = sessionStorage.getItem('sessionTime')
        ? parseInt(sessionStorage.getItem('sessionTime'))
        : 600;

    const minutes = Math.floor(sessionTime / 60);
    const seconds = sessionTime % 60;
    timerElement.textContent = `${minutes}분 ${seconds < 10 ? '0' : ''}${seconds}초 남음`;
}

function showSessionTimerImmediately() {
    syncSessionTimer();
}

function showExtendButton() {
    if (document.getElementById('extend-session-btn')) return;

    const btn = document.createElement('button');
    btn.id = 'extend-session-btn';
    btn.textContent = '세션 연장';
    btn.onclick = () => {
        sessionStorage.setItem('sessionTime', 600); // 10분 연장
        document.getElementById('extend-session-btn').remove();
        syncSessionTimer();
    };
    document.body.appendChild(btn);
}

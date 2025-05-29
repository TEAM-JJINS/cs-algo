document.addEventListener("DOMContentLoaded", () => {
    const email = document.querySelector('#email');
    const code = document.querySelector('#code');
    const submitBtn = document.querySelector('#submitBtn');
    const agreePolicy = document.querySelector('#agreePolicy');
    const form = document.querySelector('#subscribeForm');
    const modalEl = document.getElementById("subscribeModal");
    const modal = bootstrap.Modal.getInstance(modalEl) || new bootstrap.Modal(modalEl);

    let isCodeVerified = false;

    function resetState() {
        submitBtn.disabled = true;
        isCodeVerified = false;
        email.disabled = false;
        code.disabled = false;
        form.reset();
    }

    resetState();

    // 인증 코드 전송
    document.querySelector('#sendCodeBtn')?.addEventListener('click', async () => {
        try {
            if (!email.value) {
                alert("이메일을 입력해주세요.");
                return;
            }

            const res = await fetch('/subscribe/request-code', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: new URLSearchParams({email: email.value})
            });

            const data = await res.json();

            if (!res.ok) {
                alert(data.message || "인증 코드 요청에 실패했습니다.");
                return;
            }

            alert(data.message || "이메일로 인증 코드가 전송되었습니다. 코드를 입력해주세요.");
            email.disabled = true;
        } catch (err) {
            alert("서버 통신 오류가 발생했습니다.");
        }
    });

    // 인증 코드 검증
    document.querySelector('#verifyCodeBtn')?.addEventListener('click', async () => {
        try {
            if (!email.value || !code.value) {
                alert("이메일과 인증 코드를 입력해주세요.");
                return;
            }

            const res = await fetch('/subscribe/verify-code', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: new URLSearchParams({email: email.value, code: code.value})
            });

            const data = await res.json();

            if (!res.ok) {
                alert(data.message || "인증 코드 검증에 실패했습니다.");
                return;
            }

            alert(data.message || "인증 코드가 검증되었습니다. 구독을 진행해주세요.");
            code.disabled = true;
            isCodeVerified = true;
            if (agreePolicy.checked) {
                submitBtn.disabled = false;
            }
        } catch (err) {
            alert("서버 통신 오류가 발생했습니다.");
        }
    });

    // 개인정보 처리방침 동의
    agreePolicy?.addEventListener("change", () => {
        submitBtn.disabled = !(isCodeVerified && agreePolicy.checked);
    });

    // 구독 완료
    form.onsubmit = async (e) => {
        e.preventDefault();
        try {
            const res = await fetch('/subscribe', {
                method: 'POST',
                headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                body: new URLSearchParams({email: email.value})
            });

            const data = await res.json();

            if (!res.ok) {
                alert(data.message || "구독 요청에 실패했습니다.");
                return;
            }

            alert(data.message || "구독이 완료되었습니다.");
            modal.hide();
            resetState();
        } catch (err) {
            alert("구독 요청 중 오류가 발생했습니다.");
        }
    };
});

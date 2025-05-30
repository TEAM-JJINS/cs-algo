import { fetchWithOptions } from './http.js';

document.addEventListener("DOMContentLoaded", () => {
    const email = document.querySelector('#email');
    const code = document.querySelector('#code');
    const submitBtn = document.querySelector('#submitBtn');
    const agreePolicy = document.querySelector('#agreePolicy');
    const form = document.querySelector('#subscriptionForm');
    const modalEl = document.getElementById("subscriptionModal");
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

    document.querySelector('#sendCodeBtn')?.addEventListener('click', async () => {
        if (!email.value) {
            alert("이메일을 입력해주세요.");
            return;
        }

        const { ok, data } = await fetchWithOptions({
            url: '/subscription/request-code',
            method: 'POST',
            body: { email: email.value },
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        });

        alert(data.message || (ok ? "코드가 전송되었습니다." : "실패했습니다."));
        if (ok) email.disabled = true;
    });

    document.querySelector('#verifyCodeBtn')?.addEventListener('click', async () => {
        if (!email.value || !code.value) {
            alert("이메일과 인증 코드를 입력해주세요.");
            return;
        }

        const { ok, data } = await fetchWithOptions({
            url: '/subscription/verify-code',
            method: 'POST',
            body: { email: email.value, code: code.value },
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        });

        alert(data.message || (ok ? "성공했습니다." : "실패했습니다."));
        if (ok) {
            code.disabled = true;
            isCodeVerified = true;
            if (agreePolicy.checked) submitBtn.disabled = false;
        }
    });

    agreePolicy?.addEventListener("change", () => {
        submitBtn.disabled = !(isCodeVerified && agreePolicy.checked);
    });

    form.onsubmit = async (e) => {
        e.preventDefault();

        const { ok, data } = await fetchWithOptions({
            url: '/subscription',
            method: 'POST',
            body: { email: email.value },
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' }
        });

        alert(data.message || (ok ? "구독 완료" : "구독 실패"));
        if (ok) {
            modal.hide();
            resetState();
        }
    };
});

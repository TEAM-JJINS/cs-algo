import { deleteWithQuery } from './http.js';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('unsubscriptionForm');
    const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('unsubscriptionModal'));
    const userId = document.querySelector('meta[name="user-id"]').content;

    if (!userId) {
        alert('사용자 ID는 필수입니다.');
        return;
    }
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const {ok, data} = await deleteWithQuery('/unsubscription', { userId });
        alert(data.message || (ok ? "구독 해지 완료" : "구독 해지 실패"));

        if (ok) {
           modal.hide();
           window.location.href = '/';
        }
    });
});

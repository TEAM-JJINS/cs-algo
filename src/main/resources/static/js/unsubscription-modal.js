import { deleteByUserId } from './http.js';

document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('unsubscriptionForm');
    const modal = bootstrap.Modal.getOrCreateInstance(document.getElementById('unsubscriptionModal'));
    const userId = document.querySelector('meta[name="user-id"]').content;
    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        try {
            await deleteByUserId('/unsubscription', { userId });
            alert('구독이 해지되었습니다.');
            modal.hide();

            window.location.href = '/';
        } catch (err) {
            alert('구독 해지 중 오류가 발생했습니다: ' + err.message);
        }
    });
});

import {fetchWithOptions} from './http.js';

async function deleteUser(userId) {
    if (!confirm("정말로 삭제하시겠습니까?")) return;

    const {ok, data} = await fetchWithOptions({
        url: `/admin/users/${userId}`,
        method: 'DELETE'
    });

    if (ok) {
        alert("삭제 완료");
        location.reload();
    } else {
        alert(data?.message || "삭제 실패");
    }
}

window.deleteUser = deleteUser;

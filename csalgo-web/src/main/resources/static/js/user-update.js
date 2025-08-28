import {fetchWithOptions} from './http.js';

async function updateUserRole(userId, newRole) {
    const payload = {role: newRole};

    const {ok, data} = await fetchWithOptions({
        url: `/admin/users/${userId}/role`,
        method: 'PUT',
        body: payload,
        headers: {
            'Content-Type': 'application/json'
        }
    });

    if (ok) {
        alert("권한이 변경되었습니다.");
        window.location.reload();
    } else {
        alert(data?.message || "권한 변경 실패");
    }
}

window.updateUserRole = updateUserRole;

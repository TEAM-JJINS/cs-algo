import {getCookie, setCookie} from './cookie.js';

export async function refreshToken() {
    const refresh = getCookie("refreshToken");
    if (!refresh) return false;

    const response = await fetch("/admin/refresh", {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({refreshToken: refresh})
    });

    if (response.ok) {
        const data = await response.json();
        setCookie("accessToken", data.accessToken, 1);
        setCookie("refreshToken", data.refreshToken, 14);
        return true;
    }
    return false;
}

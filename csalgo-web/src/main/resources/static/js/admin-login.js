import {postFormData} from './http.js';

document.querySelector("#loginForm").addEventListener("submit", async (e) => {
    e.preventDefault();

    const email = document.querySelector("#email").value;
    const password = document.querySelector("#password").value;

    const {ok, data} = await postFormData("/admin/login", {email, password});

    if (ok) {
        document.cookie = `accessToken=${data.accessToken}; path=/; max-age=3600`;
        document.cookie = `refreshToken=${data.refreshToken}; path=/; max-age=604800`;
        window.location.href = "/admin/dashboard";
    } else {
        alert(data.message || "로그인 실패");
    }
});

import {getCookie} from './cookie.js';
import {refreshToken} from './refresh.js';

// 일반 form-urlencoded 요청
export async function postFormData(url, bodyObj) {
    try {
        const res = await fetch(url, {
            method: 'POST',
            headers: {'Content-Type': 'application/x-www-form-urlencoded'},
            body: new URLSearchParams(bodyObj)
        });

        const data = await res.json();
        return {ok: res.ok, data};
    } catch (err) {
        return {ok: false, data: {message: "서버 통신 오류가 발생했습니다."}};
    }
}

// JSON 기반 fetch (queryParams 지원)
export async function fetchWithOptions({
                                           url,
                                           method = 'GET',
                                           queryParams = {},
                                           body = null,
                                           headers = {}
                                       }) {
    try {
        const queryString = new URLSearchParams(queryParams).toString();
        const fullUrl = queryString ? `${url}?${queryString}` : url;

        const finalHeaders = {'Content-Type': 'application/json', ...headers};
        const finalBody = body && method !== 'GET' ? JSON.stringify(body) : null;

        const res = await fetch(fullUrl, {method, headers: finalHeaders, body: finalBody});
        const data = await res.json();

        return {ok: res.ok, data};
    } catch (err) {
        return {ok: false, data: {message: '서버 통신 오류가 발생했습니다.'}};
    }
}

// 인증 포함 fetch (401 발생 시 refreshToken 후 재시도)
export async function fetchWithAuth({url, method = 'GET', body = null}) {
    let accessToken = getCookie("accessToken");

    let res = await fetch(url, {
        method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${accessToken}`
        },
        body: body ? JSON.stringify(body) : null
    });

    // accessToken 만료 → refresh 시도
    if (res.status === 401) {
        const refreshed = await refreshToken();
        if (refreshed) {
            accessToken = getCookie("accessToken");
            res = await fetch(url, {
                method,
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${accessToken}`
                },
                body: body ? JSON.stringify(body) : null
            });
        }
    }

    const data = await res.json().catch(() => ({}));
    return {ok: res.ok, data};
}

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

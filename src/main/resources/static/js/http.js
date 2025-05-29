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

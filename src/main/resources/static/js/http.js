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

    const finalHeaders = { ...headers };
    let finalBody = null;

    if (body && method !== 'GET') {
      const contentType = finalHeaders['Content-Type'];

      if (contentType === 'application/x-www-form-urlencoded') {
        finalBody = new URLSearchParams(body).toString();
      } else {
        finalHeaders['Content-Type'] = 'application/json';
        finalBody = JSON.stringify(body);
      }
    }

    const res = await fetch(fullUrl, {
      method,
      headers: finalHeaders,
      body: finalBody
    });

    const data = await res.json();
    return { ok: res.ok, data };
  } catch (err) {
    return {
      ok: false,
      data: { message: '서버 통신 오류가 발생했습니다.' }
    };
  }
}

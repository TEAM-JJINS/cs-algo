export function setCookie(name, value, days = 1) {
    const expires = new Date();
    expires.setDate(expires.getDate() + days);
    document.cookie = `${name}=test; Path=/; Expires=${expires.toUTCString()}; SameSite=None; Secure`;
}

export function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

export function deleteCookie(name) {
    document.cookie = `${name}=; Path=/; Expires=Thu, 01 Jan 1970 00:00:00 GMT; SameSite=None; Secure`;
}

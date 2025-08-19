export function setCookie(name, value, days = 1) {
    const expires = new Date();
    expires.setDate(expires.getDate() + days);
    document.cookie = `${name}=${value}; path=/; expires=${expires.toUTCString()}; SameSite=Strict; Secure`;
}

export function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}

export function deleteCookie(name) {
    document.cookie = `${name}=; path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT`;
}

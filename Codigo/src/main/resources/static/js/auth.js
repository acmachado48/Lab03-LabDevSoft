// Envia o JWT recebido no login em todas as chamadas à API protegida.
// Se o token estiver ausente ou expirado (401), limpa a sessão e volta para o login.
function getToken() {
    const usuario = JSON.parse(localStorage.getItem('usuarioLogado') || 'null');
    return usuario?.token || null;
}

async function apiFetch(url, options = {}) {
    const headers = new Headers(options.headers || {});
    const token = getToken();
    if (token) headers.set('Authorization', `Bearer ${token}`);

    const res = await fetch(url, { ...options, headers });
    if (res.status === 401) {
        localStorage.removeItem('usuarioLogado');
        window.location.href = 'index.html';
    }
    return res;
}

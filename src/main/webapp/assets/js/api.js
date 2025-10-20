const API = {
    async post(url, data) {
        const r = await fetch(url, { method: 'POST', headers: {'Content-Type':'application/json'}, body:JSON.stringify(data)});
        if (!r.ok) throw new Error(await r.text());
        return r.json();
    },
    async get(url) {
        const r = await fetch(url);
        if (!r.ok) throw new Error(await r.text());
        return r.json();
    },
    async del(url) {
        const r = await fetch(url, { method:'DELETE' });
        if (!r.ok) throw new Error(await r.text());
        return r.json();
    }
};
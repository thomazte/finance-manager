const today = new Date();
const ano = today.getFullYear();
const mes = today.getMonth() + 1;

async function carregarDashboard() {
    const d = await API.get(`api/dashboard?ano=${ano}&mes=${mes}`);
    document.getElementById('saldo').textContent =
        d.saldo.toLocaleString('pt-BR',{style:'currency',currency:'BRL'});
    const ul = document.getElementById('porCategoria');
    ul.innerHTML = '';
    d.porCategoria.forEach(i=>{
        const li = document.createElement('li');
        li.textContent = `${i.categoria}: ${i.total.toLocaleString('pt-BR',{style:'currency',currency:'BRL'})}`;
        ul.appendChild(li);
    });
}

document.getElementById('logout').onclick = async ()=>{
    await API.post('api/auth/logout', {});
    location.href = 'index.html';
};

carregarDashboard().catch(console.error);
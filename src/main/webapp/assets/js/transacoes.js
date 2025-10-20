async function carregarCategorias() {
    const cats = await API.get('api/categorias');
    const sel = document.getElementById('categoria');
    sel.innerHTML = cats.map(c=>`<option value="${c.id}">${c.nome}</option>`).join('');
}

async function carregarTransacoes() {
    const itens = await API.get(`api/transacoes?ano=${ano}&mes=${mes}`);
    const ul = document.getElementById('lista');
    ul.innerHTML = '';
    itens.forEach(t=>{
        const li = document.createElement('li');
        li.innerHTML = `
      <span>${t.data} • ${t.tipo} • ${t.categoriaNome||''}</span>
      <strong>${t.valor.toLocaleString('pt-BR',{style:'currency',currency:'BRL'})}</strong>
      <button data-id="${t.id}">Excluir</button>`;
        ul.appendChild(li);
    });
    ul.querySelectorAll('button[data-id]').forEach(b=>{
        b.onclick = async ()=>{
            await API.del('api/transacoes/'+b.dataset.id);
            await carregarDashboard(); await carregarTransacoes();
        };
    });
}

document.getElementById('formTrans').addEventListener('submit', async (e)=>{
    e.preventDefault();
    const payload = {
        tipo: document.getElementById('tipo').value,
        data: document.getElementById('data').value,
        valor: Number(document.getElementById('valor').value),
        descricao: document.getElementById('desc').value,
        categoriaId: Number(document.getElementById('categoria').value)
    };
    await API.post('api/transacoes', payload);
    e.target.reset();
    await carregarDashboard(); await carregarTransacoes();
});

carregarCategorias().then(carregarTransacoes).catch(console.error);
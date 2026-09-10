document.getElementById('formPessoa').addEventListener('submit', async (e) => {
    e.preventDefault();

    const dados = {
        nome: document.getElementById('nome').value,
        email: document.getElementById('email').value
    };

    const r = await fetch('http://localhost:8080/api/pessoas', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(dados)
    });

    document.getElementById('resultado').innerText = 'HTTP ' + r.status;

    const data = await r.json();

    document.getElementById('uuid').innerText = 'ID: ' + data.id;
});
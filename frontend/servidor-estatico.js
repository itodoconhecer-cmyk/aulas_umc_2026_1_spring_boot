const http = require('http');
const fs = require('fs');
const path = require('path');

const porta = 5500;
const raiz = __dirname;

const tipos = {
    '.html': 'text/html; charset=utf-8',
    '.css': 'text/css; charset=utf-8',
    '.js': 'text/javascript; charset=utf-8'
};

const servidor = http.createServer((request, response) => {
    const url = request.url === '/' ? '/index.html' : request.url;
    const arquivo = path.normalize(path.join(raiz, decodeURIComponent(url)));

    if (!arquivo.startsWith(raiz)) {
        response.writeHead(403);
        response.end('Acesso negado');
        return;
    }

    fs.readFile(arquivo, (erro, conteudo) => {
        if (erro) {
            response.writeHead(404);
            response.end('Arquivo nao encontrado');
            return;
        }

        response.writeHead(200, {
            'Content-Type': tipos[path.extname(arquivo)] || 'text/plain; charset=utf-8'
        });
        response.end(conteudo);
    });
});

servidor.listen(porta, '127.0.0.1', () => {
    console.log('Frontend rodando em http://127.0.0.1:' + porta);
});

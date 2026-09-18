import { CadastroPessoaResponse } from '../dto/CadastroPessoaResponse.js';

export class PessoaService {
    constructor(baseUrl) {
        this.baseUrl = baseUrl;
    }

    async cadastrarSimples(request) {
        return this.enviarJson('', 'POST', request);
    }

    async cadastrarCompleto(request) {
        return this.enviarJson('/completo', 'POST', request);
    }

    async listar() {
        const data = await this.enviar('', 'GET');
        return data.map((item) => new CadastroPessoaResponse(item));
    }

    async buscarPorId(id) {
        const data = await this.enviar('/' + id, 'GET');
        return new CadastroPessoaResponse(data);
    }

    async atualizar(id, request) {
        return this.enviarJson('/' + id, 'PUT', request);
    }

    async excluir(id) {
        await this.enviar('/' + id, 'DELETE', false);
    }

    async enviarJson(caminho, metodo, body) {
        const data = await this.enviar(caminho, metodo, true, body);
        return new CadastroPessoaResponse(data);
    }

    async enviar(caminho, metodo, esperaJson = true, body = null) {
        const opcoes = {
            method: metodo,
            headers: {}
        };

        if (body) {
            opcoes.headers['Content-Type'] = 'application/json';
            opcoes.body = JSON.stringify(body);
        }

        const response = await fetch(this.baseUrl + caminho, opcoes);

        if (!response.ok) {
            throw new Error('HTTP ' + response.status);
        }

        if (!esperaJson || response.status === 204) {
            return null;
        }

        return response.json();
    }
}

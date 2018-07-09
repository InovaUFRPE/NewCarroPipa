from flask import render_template, jsonify, request
from flask_sqlalchemy import SQLAlchemy, BaseQuery
from app import app, db

from app.models.ModelUsuario import Usuario
from app.models.ModelPessoa import Pessoa
from app.models.ModelEndereco import Endereco
from app.models.ModelEmpresa import Empresa
from app.models.ModelCliente import Cliente
from app.models.ModelCaminhao import Caminhao
from app.models.ModelMotorista import Motorista
from app.models.ModelRanking import Ranking
from app.models.ModelFormaPagto import FormaPagto
from app.models.ModelPagamento import Pagamento


@app.route("/index/<user>")
@app.route("/", defaults={'user': None})
def index(user):
    return render_template('index.html',
                           user=user)


'''----------------------------Usuario--------------------------------'''

def usuario_add(email,senha):
    i = Usuario(email,senha)
    db.session.add(i)
    db.session.commit()
    return "Usuário \"" + i.email + "\" incluido com sucesso!"

def usuario_delete(id_usuario):
    d = Usuario.query.get(id_usuario)
    db.session.delete(d)
    db.session.commit()
    return "Usuário \"" + d.email + "\" excluído com sucesso!"

def usuario_get(id_usuario):
    g = Usuario.query.get(id_usuario)
    return "Usuário \"" + g.email

def usuario_update(id_usuario,email,senha):
    u = Usuario.query.get(id_usuario)
    u.email = email
    u.senha = senha
    db.session.commit()
    return "Usuário \"" + u.email + "\" alterado com sucesso!"

@app.route("/usuario/<id_usuario>",methods=['GET'])
@app.route("/usuario/", defaults={'id_usuario': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/usuario", defaults={'id_usuario': None}, methods=['POST','GET','DELETE','PUT'])
def usuario(id_usuario):
    if (request.method == 'POST'):
        some_json = request.get_json()
        if usuario_add(some_json['email'],some_json['senha']):
            return jsonify({'sucesso':True}), 201
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        if usuario_delete(some_json['id_usuario']):
            return jsonify({'sucesso':True}), 202
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'GET'):
        result = usuario_get(id_usuario)
        if result:
            return jsonify({'sucesso':True, 'id_usuario':result['id_usuario'],'email':result['email'],'senha':result['senha'] })
        return jsonify({'sucesso':False})
    
    elif (request.method == 'PUT'):
        some_json = request.get_json()
        if usuario_update(some_json['id_usuario'], some_json['email'], some_json['senha']):
            return jsonify({'sucesso':True}), 200
        return jsonify({'sucesso':False}), 400


'''----------------------------Pessoa--------------------------------'''

def pessoa_add(nomerazaosocial,foto,telefone,id_usuario,tipopessoa,cpfcnpj):
    i = Pessoa(nomerazaosocial,foto,telefone,id_usuario,tipopessoa,cpfcnpj)
    db.session.add(i)
    db.session.commit()
    return "Pessoa \"" + i.nomerazaosocial + "\" incluida com sucesso!"

def pessoa_delete(id_pessoa):
    d = Pessoa.query.get(id_pessoa)
    db.session.delete(d)
    db.session.commit()
    return "Pessoa \"" + d.nomerazaosocial + "\" excluída com sucesso!"

def pessoa_get(id_pessoa):
    g = Pessoa.query.get(id_pessoa)
    return "Pessoa \"" + g.nomerazaosocial

def pessoa_update(id_pessoa,nomerazaosocial,foto,telefone,id_usuario,tipopessoa,cpfcnpj):
    u = Pessoa.query.get(id_pessoa)
    u.nomerazaosocial = nomerazaosocial
    u.foto = foto
    u.telefone = telefone
    u.id_usuario = id_usuario
    u.tipopessoa = tipopessoa
    u.cpfcnpj = cpfcnpj
    db.session.commit()
    return "Pessoa \"" + u.nomerazaosocial + "\" alterada com sucesso!"

@app.route("/pessoa/<id_pessoa>",methods=['GET'])
@app.route("/pessoa/", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/pessoa", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
def pessoa(id_pessoa):
    if (request.method == 'POST'):
        some_json = request.get_json()
        if pessoa_add(some_json['nomerazaosocial'],some_json['foto'],some_json['telefone'],some_json['id_usuario'],some_json['tipopessoa'],some_json['cpfcnpj']):
            return jsonify({'sucesso':True}), 201
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        if pessoa_delete(some_json['id_pessoa']):
            return jsonify({'sucesso':True}), 202
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'GET'):
        result = pessoa_get(id_pessoa)
        if result:
            return jsonify({'sucesso':True, 'id_pessoa':result['id_pessoa'],'nomerazaosocial':result['nomerazaosocial'],'foto':result['foto'],'telefone':result['telefone'],'tipopessoa':result['tipopessoa'],'cpfcnpj':result['cpfcnpj']})
        return jsonify({'sucesso':False})
    
    elif (request.method == 'PUT'):
        some_json = request.get_json()
        if pessoa_update(some_json['id_pessoa'],some_json['nomerazaosocial'],some_json['foto'],some_json['telefone'],some_json['id_usuario'],some_json['tipopessoa'],some_json['cpfcnpj']):
            return jsonify({'sucesso':True}), 200
        return jsonify({'sucesso':False}), 400


'''----------------------------Endereco--------------------------------'''

def endereco_add(id_pessoa,logradouro,complemento,bairro,cidade,cep,uf):
    i = Endereco(id_pessoa,logradouro,complemento,bairro,cidade,cep,uf)
    db.session.add(i)
    db.session.commit()
    return "Endereco \"" + i.logradouro + "\" incluido com sucesso!"

def endereco_delete(id_endereco):
    d = Endereco.query.get(id_endereco)
    db.session.delete(d)
    db.session.commit()
    return "Endereco \"" + d.logradouro + "\" excluído com sucesso!"

def endereco_get(id_endereco):
    g = Endereco.query.get(id_endereco)
    return "Endereco \"" + g.logradouro

def endereco_update(id_pessoa,logradouro,complemento,bairro,cidade,cep,uf):
    u = Endereco.query.get(id_endereco)
    u.logradouro = logradouro
    u.complemento = complemento
    u.bairro = bairro
    u.cidade = cidade
    u.cep = cep
    u.uf = uf
    db.session.commit()
    return "Endereco \"" + u.logradouro + "\" alterado com sucesso!"

@app.route("/endereco/<id_pessoa>",methods=['GET'])
@app.route("/endereco/", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/endereco", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
def endereco(id_pessoa):
    if (request.method == 'POST'):
        some_json = request.get_json()
        if endereco_add(some_json['id_pessoa'],some_json['lograouro'],some_json['complemento'],some_json['bairro'],some_json['cidade'],some_json['cep'],some_json['uf']):
            return jsonify({'sucesso':True}), 201
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        if endereco_delete(some_json['id_pessoa']):
            return jsonify({'sucesso':True}), 202
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'GET'):
        result = endereco_get(id_usuario)
        if result:
            return jsonify({'sucesso':True, 'id_pessoa':result['id_pessoa'],'lograouro':result['lograouro'],'complemento':result['complemento'],'bairro':result['bairro'],'cidade':result['cidade'],'cep':result['cep'],'uf':result['uf']})
        return jsonify({'sucesso':False})
    
    elif (request.method == 'PUT'):
        some_json = request.get_json()
        if endereco_update(some_json['id_pessoa'],some_json['lograouro'],some_json['complemento'],some_json['bairro'],some_json['cidade'],some_json['cep'],some_json['uf']):
            return jsonify({'sucesso':True}), 200
        return jsonify({'sucesso':False}), 400


'''----------------------------Empresa--------------------------------'''

def empresa_add(id_pessoa_emp):
    i = Empresa(id_pessoa_emp)
    db.session.add(i)
    db.session.commit()
    return "Empresa \"" + str(i.id_pessoa_emp) + "\" incluida com sucesso!"

def empresa_delete(id_pessoa_emp):
    d = Empresa.query.get(id_pessoa_emp)
    db.session.delete(d)
    db.session.commit()
    return "Empresa \"" + str(d.id_pessoa_emp) + "\" excluída com sucesso!"

def empresa_get(id_pessoa_emp):
    g = Empresa.query.get(id_pessoa_emp)
    return "Empresa \"" + str(g.id_pessoa_emp)

@app.route("/empresa/<id_pessoa>",methods=['GET'])
@app.route("/empresa/", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/empresa", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
def empresa(id_pessoa):
    if (request.method == 'POST'):
        some_json = request.get_json()
        if empresa_add(some_json['id_pessoa']):
            return jsonify({'sucesso':True}), 201
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        if empresa_delete(some_json['id_pessoa']):
            return jsonify({'sucesso':True}), 202
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'GET'):
        result = empresa_get(id_pessoa)
        if result:
            return jsonify({'sucesso':True, 'id_pessoa':result['id_pessoa']})
        return jsonify({'sucesso':False})


'''----------------------------Cliente--------------------------------'''

def cliente_add(id_pessoa):
    i = Cliente(id_pessoa)
    db.session.add(i)
    db.session.commit()
    return "Cliente \"" + str(i.id_pessoa) + "\" incluida com sucesso!"

def cliente_delete(id_pessoa):
    d = Cliente.query.get(id_pessoa)
    db.session.delete(d)
    db.session.commit()
    return "Cliente \"" + str(d.id_pessoa) + "\" excluída com sucesso!"

def cliente_get(id_pessoa):
    g = Cliente.query.get(id_pessoa)
    return "Cliente \"" + str(g.id_pessoa)

@app.route("/cliente/<id_pessoa>",methods=['GET'])
@app.route("/cliente/", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/cliente", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
def cliente(id_pessoa):
    if (request.method == 'POST'):
        some_json = request.get_json()
        if cliente_add(some_json['id_pessoa']):
            return jsonify({'sucesso':True}), 201
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        if cliente_delete(some_json['id_pessoa']):
            return jsonify({'sucesso':True}), 202
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'GET'):
        result = cliente_get(id_pessoa)
        if result:
            return jsonify({'sucesso':True, 'id_pessoa':result['id_pessoa']})
        return jsonify({'sucesso':False})


'''----------------------------Caminhao--------------------------------'''

def caminhao_add(placa,capacidade,modelo,id_pessoa_emp):
    i = Caminhao(placa,capacidade,modelo,id_pessoa_emp)
    db.session.add(i)
    db.session.commit()
    return "Caminhao \"" + i.placa + "\" incluido com sucesso!"

def caminhao_delete(id_caminhao):
    d = Caminhao.query.get(id_caminhao)
    db.session.delete(d)
    db.session.commit()
    return "Caminhao \"" + d.placa + "\" excluído com sucesso!"

def caminhao_get(id_caminhao):
    g = Caminhao.query.get(id_caminhao)
    return "Caminhao \"" + g.placa

def caminhao_update(id_caminhao,placa,capacidade,modelo,id_pessoa_emp):
    u = Caminhao.query.get(id_caminhao)
    u.placa = placa
    u.capacidade = capacidade
    u.modelo = modelo
    u.id_pessoa_emp = id_pessoa_emp
    db.session.commit()
    return "Caminhao \"" + u.placa + "\" alterado com sucesso!"

@app.route("/caminhao/<id_caminhao>",methods=['GET'])
@app.route("/caminhao/", defaults={'id_caminhao': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/caminhao", defaults={'id_caminhao': None}, methods=['POST','GET','DELETE','PUT'])
def caminhao(id_caminhao):
    if (request.method == 'POST'):
        some_json = request.get_json()
        if caminhao_add(some_json['placa'],some_json['capacidade'],some_json['modelo'],some_json['id_pessoa_emp']):
            return jsonify({'sucesso':True}), 201
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        if caminhao_delete(some_json['id_caminhao']):
            return jsonify({'sucesso':True}), 202
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'GET'):
        result = caminhao_get(id_caminhao)
        if result:
            return jsonify({'sucesso':True, 'id_caminhao':result['id_caminhao'],'placa':result['placa'],'capacidade':result['capacidade'],'modelo':result['modelo'],'id_pessoa_emp':result['id_pessoa_emp']})
        return jsonify({'sucesso':False})
    
    elif (request.method == 'PUT'):
        some_json = request.get_json()
        if caminhao_update(some_json['id_caminhao'],some_json['placa'],some_json['capacidade'],some_json['modelo'],some_json['id_pessoa_emp']):
            return jsonify({'sucesso':True}), 200
        return jsonify({'sucesso':False}), 400


'''----------------------------Motorista--------------------------------'''

def motorista_add(id_pessoa,id_caminhao,id_pessoa_emp):
    i = Motorista(id_pessoa,id_caminhao,id_pessoa_emp)
    db.session.add(i)
    db.session.commit()
    return "Motorista \"" + str(i.id_pessoa) + "\" incluido com sucesso!"

def motorista_delete(id_pessoa):
    d = Motorista.query.get(id_pessoa)
    db.session.delete(d)
    db.session.commit()
    return "Motorista \"" + str(d.id_pessoa) + "\" excluído com sucesso!"

def motorista_get(id_pessoa):
    g = Motorista.query.get(id_pessoa)
    return "Motorista \"" + str(g.id_pessoa)

def motorista_update(id_pessoa,id_caminhao,id_pessoa_emp):
    u = Motorista.query.get(id_pessoa)
    u.id_caminhao = id_caminhao
    u.id_pessoa_emp = id_pessoa_emp
    db.session.commit()
    return "Motorista \"" + str(u.id_pessoa) + "\" alterado com sucesso!"

@app.route("/motorista/<id_pessoa>",methods=['GET'])
@app.route("/motorista/", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/motorista", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
def motorista(id_pessoa):
    if (request.method == 'POST'):
        some_json = request.get_json()
        if motorista_add(some_json['id_pessoa'],some_json['id_caminhao'],some_json['id_pessoa_emp']):
            return jsonify({'sucesso':True}), 201
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        if motorista_delete(some_json['id_pessoa']):
            return jsonify({'sucesso':True}), 202
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'GET'):
        result =motorista_get(id_pessoa)
        if result:
            return jsonify({'sucesso':True, 'id_pessoa':result['id_pessoa'],'nomerazaosocial':result['nomerazaosocial'],'telefone':result['telefone'],'tipopessoa':result['tipopessoa'],'cpfcnpj':result['cpfcnpj']})
        return jsonify({'sucesso':False})
    
    elif (request.method == 'PUT'):
        some_json = request.get_json()
        if motorista_update(some_json['id_pessoa'],some_json['id_caminhao'],some_json['id_pessoa_emp']):
            return jsonify({'sucesso':True}), 200
        return jsonify({'sucesso':False}), 400


'''----------------------------Pedido--------------------------------'''

def pedido_add(id_pessoa_cli,id_pessoa_mot,valor,datahora,checkin,imediatoprogramado,confirmaprogramado):
    i = Pedido(id_pessoa_cli,id_pessoa_mot,valor,datahora,checkin,imediatoprogramado,confirmaprogramado)
    db.session.add(i)
    db.session.commit()
    return "Pedido \"" + str(i.id_pedido) + "\" incluido com sucesso!"

def pedido_delete(id_pedido):
    d = Pedido.query.get(id_pedido)
    db.session.delete(d)
    db.session.commit()
    return "Pedido \"" + str(d.id_pedido) + "\" excluído com sucesso!"

def pedido_get(id_pedido):
    g = Pedido.query.get(id_pedido)
    return "Pedido \"" + str(g.id_pedido)

def pedido_update(id_pedido,id_pessoa_cli,id_pessoa_mot,valor,datahora,checkin,imediatoprogramado,confirmaprogramado):
    u = Pedido.query.get(id_pedido)
    u.id_pessoa_cli = id_pessoa_cli
    u.id_pessoa_mot = id_pessoa_mot
    u.valor = valor
    u.datahora = datahora
    u.checkin = checkin
    u.imediatoprogramado = imediatoprogramado
    u.confirmaprogramado = confirmaprogramado
    db.session.commit()
    return "Pedido \"" + str(u.id_pedido) + "\" alterado com sucesso!"

@app.route("/pedido/<id_pedido>",methods=['GET'])
@app.route("/pedido/", defaults={'id_pedido': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/pedido", defaults={'id_pedido': None}, methods=['POST','GET','DELETE','PUT'])
def pedido(id_pedido):
    if (request.method == 'POST'):
        some_json = request.get_json()
        if pedido_add(some_json['id_pessoa_cli'],some_json['id_pessoa_mot'],some_json['valor'],some_json['datahora'],some_json['checkin'],some_json['imediatoprogramado'],some_json['confirmaprogramado']):
            return jsonify({'sucesso':True}), 201
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        if pedido_delete(some_json['id_pedido']):
            return jsonify({'sucesso':True}), 202
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'GET'):
        result = pedido_get(id_pedido)
        if result:
            return jsonify({'sucesso':True, 'id_pedido':result['id_pedido'],'id_pessoa_cli':result['id_pessoa_cli'],'id_pessoa_mot':result['id_pessoa_mot'],'valor':result['valor'],'datahora':result['datahora'],'checkin':result['checkin'],'imediatoprogramado':result['imediatoprogramado'],'confirmaprogramado':result['confirmaprogramado']})
        return jsonify({'sucesso':False})
    
    elif (request.method == 'PUT'):
        some_json = request.get_json()
        if pedido_update(some_json['id_pedido'],some_json['id_pessoa_cli'],some_json['id_pessoa_mot'],some_json['valor'],some_json['datahora'],some_json['checkin'],some_json['imediatoprogramado'],some_json['confirmaprogramado']):
            return jsonify({'sucesso':True}), 200
        return jsonify({'sucesso':False}), 400


'''----------------------------Ranking--------------------------------'''

def ranking_add(id_pessoa_deu,id_pedido,nota,comentario):
    i = Ranking(id_pessoa_deu,id_pedido,nota,comentario)
    db.session.add(i)
    db.session.commit()
    return "Ranking \"" + str(i.id_pessoa_deu) + "\" incluido com sucesso!"

def ranking_delete(id_pessoa_deu,id_pedido):
    d = Ranking.query.get(id_pessoa_deu,id_pedido)
    db.session.delete(d)
    db.session.commit()
    return "Ranking \"" + str(d.id_pessoa_deu) + "\" excluído com sucesso!"

def ranking_get(id_pessoa_deu):
    g = Ranking.query.get(id_pessoa_deu,id_pedido)
    return "Ranking \"" + str(g.id_pessoa_deu)

def ranking_update(id_pessoa_deu,id_pedido,nota,comentario):
    u = Ranking.query.get(id_pessoa_deu,id_pedido)
    u.nota = nota
    u.comentario = comentario
    db.session.commit()
    return "Ranking \"" + str(u.id_pessoa_deu) + "\" alterado com sucesso!"

@app.route("/ranking/<id_pessoa_deu>,<id_pedido>",methods=['GET'])
@app.route("/ranking/", defaults={'id_pessoa_deu': None,'id_pedido': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/ranking", defaults={'id_pessoa_deu': None,'id_pedido': None}, methods=['POST','GET','DELETE','PUT'])
def ranking(id_pessoa_deu,id_pedido):
    if (request.method == 'POST'):
        some_json = request.get_json()
        if ranking_add(some_json['id_pessoa_deu'],some_json['id_pedido'],some_json['nota'],some_json['comentario']):
            return jsonify({'sucesso':True}), 201
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        if ranking_delete(some_json['id_pessoa_deu'],some_json['id_pedido']):
            return jsonify({'sucesso':True}), 202
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'GET'):
        result = ranking_get(id_pessoa_deu,id_pedido)
        if result:
            return jsonify({'sucesso':True, 'id_pessoa_deu':result['id_pessoa_deu'],'id_pedido':result['id_pedido'],'nota':result['nota'],'comentario':result['comentario']})
        return jsonify({'sucesso':False})
    
    elif (request.method == 'PUT'):
        some_json = request.get_json()
        if ranking_update(some_json['id_pessoa_deu'],some_json['id_pedido'],some_json['nota'],some_json['comentario']):
            return jsonify({'sucesso':True}), 200
        return jsonify({'sucesso':False}), 400


'''----------------------------FormaPagto--------------------------------'''

def formapagto_add(descricao):
    i = FormaPagto(descricao)
    db.session.add(i)
    db.session.commit()
    return "FormaPagto \"" + i.descricao + "\" incluido com sucesso!"

def formapagto_delete(id_formapagto):
    d = FormaPagto.query.get(id_formapagto)
    db.session.delete(d)
    db.session.commit()
    return "FormaPagto \"" + d.descricao + "\" excluído com sucesso!"

def formapagto_get(id_formapagto):
    g = FormaPagto.query.get(id_formapagto)
    return "FormaPagto \"" + g.descricao

def formapagto_update(id_formapagto,descricao):
    u = FormaPagto.query.get(id_formapagto)
    u.descricao = descricao
    db.session.commit()
    return "FormaPagto \"" + u.descricao + "\" alterado com sucesso!"

@app.route("/formapagto/<id_formapagto>",methods=['GET'])
@app.route("/formapagto/", defaults={'id_formapagto': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/formapagto", defaults={'id_formapagto': None}, methods=['POST','GET','DELETE','PUT'])
def formapagto(id_formapagto):
    if (request.method == 'POST'):
        some_json = request.get_json()
        if formapagto_add(some_json['descricao']):
            return jsonify({'sucesso':True}), 201
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        if formapagto_delete(some_json['id_formapagto']):
            return jsonify({'sucesso':True}), 202
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'GET'):
        result = formapagto_get(id_formapagto)
        if result:
            return jsonify({'sucesso':True, 'id_formapagto':result['id_formapagto'],'descricao':result['descricao']})
        return jsonify({'sucesso':False})
    
    elif (request.method == 'PUT'):
        some_json = request.get_json()
        if formapagto_update(some_json['id_formapagto'],some_json['descricao']):
            return jsonify({'sucesso':True}), 200
        return jsonify({'sucesso':False}), 400


'''----------------------------Pagamento--------------------------------'''

def pagamento_add(id_formapagto,valor,id_pedido,datahora):
    i = Pagamento(id_formapagto,valor,id_pedido,datahora)
    db.session.add(i)
    db.session.commit()
    return "Pagamento \"" + str(i.id_pagamento) + "\" incluido com sucesso!"

def pagamento_delete(id_pagamento):
    d = Pagamento.query.get(id_pagamento)
    db.session.delete(d)
    db.session.commit()
    return "Pagamento \"" + str(d.id_pagamento) + "\" excluído com sucesso!"

def pagamento_get(id_pagamento):
    g = Pagamento.query.get(id_pagamento)
    return "Pagamento \"" + str(g.id_pagamento)

def pagamento_update(id_pagamento,id_formapagto,valor,id_pedido,datahora):
    u = Pagamento.query.get(id_pagamento)
    u.id_formapagto = id_formapagto
    u.valor = valor
    u.id_pedido = id_pedido
    u.datahora = datahora
    db.session.commit()
    return "Pagamento \"" + str(u.id_pagamento) + "\" alterado com sucesso!"

@app.route("/pagamento/<id_pagamento>",methods=['GET'])
@app.route("/pagamento/", defaults={'id_pagamento': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/pagamento", defaults={'id_pagamento': None}, methods=['POST','GET','DELETE','PUT'])
def pagamento(id_pagamento):
    if (request.method == 'POST'):
        some_json = request.get_json()
        if pagamento_add(some_json['id_formapagto'],some_json['valor'],some_json['id_pedido'],some_json['datahora']):
            return jsonify({'sucesso':True}), 201
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        if pagamento_delete(some_json['id_pagamento']):
            return jsonify({'sucesso':True}), 202
        return jsonify({'sucesso':False}), 400

    elif (request.method == 'GET'):
        result = pagamento_get(id_pagamento)
        if result:
            return jsonify({'sucesso':True, 'id_pagamento':result['id_formapagto'],'descricao':result['descricao']})
        return jsonify({'sucesso':False})
    
    elif (request.method == 'PUT'):
        some_json = request.get_json()
        if pagamento_update(some_json['id_pagamento'],some_json['id_formapagto'],some_json['valor'],some_json['id_pedido'],some_json['datahora']):
            return jsonify({'sucesso':True}), 200
        return jsonify({'sucesso':False}), 400

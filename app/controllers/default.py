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
    return {'sucesso':True,'id_usuario':i.id_usuario,'email':i.email,'senha':i.senha}

def usuario_delete(id_usuario):
    d = Usuario.query.get(id_usuario)
    if d == None:
        return {'sucesso':False, 'mensagem':'usuário não existe.'}
    db.session.delete(d)
    db.session.commit()
    return {'sucesso':True, 'mensagem':'usuário removido com sucesso.'}

def usuario_get(id_usuario):
    g = Usuario.query.get(id_usuario)
    if g == None:
        return {'sucesso':False, 'mensagem':'usuario não existe.'}
    return {'sucesso':True,'mensagem':'usuário retornado com sucesso.','id_usuario':g.id_usuario,'email':g.email,'senha':g.senha}

def usuario_update(id_usuario,email,senha):
    u = Usuario.query.get(id_usuario)
    if u == None:
        return {'sucesso':False, 'mensagem':'usuário não existe.'}
    u.email = email
    u.senha = senha
    db.session.commit()

    return {'sucesso':True,'mensagem':'usuário atualizado com sucesso.','id_usuario':u.id_usuario,'email':u.email,'senha':u.senha}

@app.route("/usuarios/<id_usuario>",methods=['GET','DELETE'])
@app.route("/usuarios/", defaults={'id_usuario': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/usuarios", defaults={'id_usuario': None}, methods=['POST','GET','DELETE','PUT'])
def usuario(id_usuario):
    if (request.method == 'POST'):
        some_json = request.get_json()
        result = usuario_add(some_json['email'],some_json['senha'])    
        if result['sucesso']:
            return jsonify(result), 201
        return jsonify(result), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        result = usuario_delete(id_usuario)
        if result['sucesso']:
            return jsonify(result), 202
        return jsonify(result), 400

    elif (request.method == 'GET'):
        result = usuario_get(id_usuario)
        if result['sucesso']:
            return jsonify(result), 200
        return jsonify(result), 400

    elif (request.method == 'PUT'):
        some_json = request.get_json()
        result = usuario_update(some_json['id_usuario'], some_json['email'], some_json['senha'])
        if result['sucesso']:
            return jsonify(result), 200
        return jsonify(result), 400


'''----------------------------Pessoa--------------------------------'''

def pessoa_add(nomerazaosocial,foto,telefone,id_usuario,tipopessoa,cpfcnpj):
    i = Pessoa(nomerazaosocial,foto,telefone,id_usuario,tipopessoa,cpfcnpj)
    db.session.add(i)
    db.session.commit()
    return {'sucesso':True, 'mensagem':'pessoa cadastrado com sucesso.','id_pessoa':i.id_pessoa,'nomerazaosocial':i.nomerazaosocial,'foto':i.foto,'telefone':i.telefone,'id_usuario':i.id_usuario,'tipopessoa':i.tipopessoa,'cpfcnpj':i.cpfcnpj}

def pessoa_delete(id_pessoa):
    d = Pessoa.query.get(id_pessoa)
    if d == None:
        return {'sucesso':False, 'mensagem':'pessoa não existe.'}
    db.session.delete(d)
    db.session.commit()
    return {'sucesso':True, 'mensagem':'pessoa removida com sucesso.'}

def pessoa_get(id_pessoa):
    g = Pessoa.query.get(id_pessoa)
    if g == None:
        return {'sucesso':False, 'mensagem':'pessoa não existe.'}
    return {'sucesso':True,'mensagem':'pessoa retornada com sucesso.','id_pessoa':g.id_pessoa,'nomerazaosocial':g.nomerazaosocial,'foto':g.foto,'telefone':g.telefone,'id_usuario':g.id_usuario,'tipopessoa':g.tipopessoa,'cpfcnpj':g.cpfcnpj}

def pessoa_update(id_pessoa,nomerazaosocial,foto,telefone,id_usuario,tipopessoa,cpfcnpj):
    u = Pessoa.query.get(id_pessoa)
    if u == None:
        return {'sucesso':False, 'mensagem':'pessoa não existe.'}
    u.nomerazaosocial = nomerazaosocial
    u.foto = foto
    u.telefone = telefone
    u.id_usuario = id_usuario
    u.tipopessoa = tipopessoa
    u.cpfcnpj = cpfcnpj
    db.session.commit()
    return {'sucesso':True,'mensagem':'pessoa atualizada com sucesso.','id_pessoa':u.id_pessoa,'nomerazaosocial':u.nomerazaosocial,'foto':u.foto,'telefone':u.telefone,'id_usuario':u.id_usuario,'tipopessoa':u.tipopessoa,'cpfcnpj':u.cpfcnpj}

@app.route("/pessoas/<id_pessoa>",methods=['GET','DELETE'])
@app.route("/pessoas/", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/pessoas", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
def pessoa(id_pessoa):
    if (request.method == 'POST'):
        some_json = request.get_json()
        result = pessoa_add(some_json['nomerazaosocial'],some_json['foto'],some_json['telefone'],some_json['id_usuario'],some_json['tipopessoa'],some_json['cpfcnpj'])
        if result['sucesso']:
            return jsonify(result), 201
        return jsonify(result), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        result = pessoa_delete(id_pessoa)
        if result['sucesso']:
            return jsonify(result), 202
        return jsonify(result), 400

    elif (request.method == 'GET'):
        result = pessoa_get(id_pessoa)
        if result['sucesso']:
            return jsonify(result), 200
        return jsonify(result), 400
    
    elif (request.method == 'PUT'):
        some_json = request.get_json()
        result = pessoa_update(some_json['id_pessoa'],some_json['nomerazaosocial'],some_json['foto'],some_json['telefone'],some_json['id_usuario'],some_json['tipopessoa'],some_json['cpfcnpj'])
        if result['sucesso']:
            return jsonify(result), 200
        return jsonify(result), 400


'''----------------------------Endereco--------------------------------'''

def endereco_add(id_pessoa,logradouro,complemento,bairro,cidade,cep,uf):
    i = Endereco(id_pessoa,logradouro,complemento,bairro,cidade,cep,uf)
    db.session.add(i)
    db.session.commit()
    return {'sucesso':True, 'mensagem':'endereco cadastrado com sucesso.','id_pessoa':i.id_pessoa,'logradouro':i.logradouro,'complemento':i.complemento,'bairro':i.bairro,'cidade':i.cidade,'cep':i.cep,'uf':i.uf}

def endereco_delete(id_pessoa):
    d = Endereco.query.get(id_pessoa)
    if d == None:
        return {'sucesso':False, 'mensagem':'endereco não existe.'}
    db.session.delete(d)
    db.session.commit()
    return {'sucesso':True, 'mensagem':'endereco removido com sucesso.'}

def endereco_get(id_pessoa):
    g = Endereco.query.get(id_pessoa)
    if g == None:
        return {'sucesso':False, 'mensagem':'endereco não existe.'}
    return {'sucesso':True,'mensagem':'endereco cadastrado com sucesso.','id_pessoa':g.id_pessoa,'logradouro':g.logradouro,'complemento':g.complemento,'bairro':g.bairro,'cidade':g.cidade,'cep':g.cep,'uf':g.uf}

def endereco_update(id_pessoa,logradouro,complemento,bairro,cidade,cep,uf):
    u = Endereco.query.get(id_pessoa)
    if u == None:
        return {'sucesso':False, 'mensagem':'endereco não existe.'}
    u.logradouro = logradouro
    u.complemento = complemento
    u.bairro = bairro
    u.cidade = cidade
    u.cep = cep
    u.uf = uf
    db.session.commit()
    return {'sucesso':True,'mensagem':'endereco atualizado com sucesso.','id_pessoa':u.id_pessoa,'logradouro':u.logradouro,'complemento':u.complemento,'bairro':u.bairro,'cidade':u.cidade,'cep':u.cep,'uf':u.uf}

@app.route("/enderecos/<id_pessoa>",methods=['GET','DELETE'])
@app.route("/enderecos/", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/enderecos", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
def endereco(id_pessoa):
    if (request.method == 'POST'):
        some_json = request.get_json()
        result = endereco_add(some_json['id_pessoa'],some_json['logradouro'],some_json['complemento'],some_json['bairro'],some_json['cidade'],some_json['cep'],some_json['uf'])
        if result['sucesso']:
            return jsonify(result), 201
        return jsonify(result), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        result = endereco_delete(id_pessoa)
        if result['sucesso']:
            return jsonify(result), 202
        return jsonify(result), 400

    elif (request.method == 'GET'):
        result = endereco_get(id_pessoa)
        if result['sucesso']:
            return jsonify(result), 200
        return jsonify(result), 400
    
    elif (request.method == 'PUT'):
        some_json = request.get_json()
        result = endereco_update(some_json['id_pessoa'],some_json['logradouro'],some_json['complemento'],some_json['bairro'],some_json['cidade'],some_json['cep'],some_json['uf'])
        if result['sucesso']:
            return jsonify(result), 200
        return jsonify(result), 400


'''----------------------------Cliente--------------------------------'''

def cliente_add(email,senha,nomerazaosocial,foto,telefone,tipopessoa,cpfcnpj,logradouro,complemento,bairro,cidade,cep,uf):
    h = Usuario.query.filter(Usuario.email == email).first()
    if h == None:
        h = Usuario(email, senha)
        db.session.add(h)
        #db.session.commit()
    
    i = Pessoa.query.filter(Pessoa.id_usuario == h.id_usuario).first()
    if i == None:
        i = Pessoa(nomerazaosocial,foto,telefone,h.id_usuario,tipopessoa,cpfcnpj)
        db.session.add(i)
        #db.session.commit()

    j = Endereco.query.filter(Endereco.id_pessoa == i.id_pessoa).first()
    if j == None:
        j = Endereco(i.id_pessoa,logradouro,complemento,bairro,cidade,cep,uf)
        db.session.add(j)
        #db.session.commit()
    
    k = Cliente.query.filter(Cliente.id_pessoa == i.id_pessoa).first()
    if k == None:
        k = Cliente(i.id_pessoa)
        db.session.add(k)
        db.session.commit()
    else:
        return {'sucesso':False, 'mensagem':'cliente inexistente.'}

    return {'sucesso':True, 'mensagem':'cliente cadastrado com sucesso.'}#,'id_pessoa':k.id_pessoa,'cpfcnpj':i.cpfcnpj,'email':h.email}

def cliente_delete(id_pessoa):
    d = Cliente.query.get(id_pessoa)
    if d == None:
        return {'sucesso':False, 'mensagem':'cliente não existe.'}

    e = Pessoa.query.get(d.id_pessoa)
    f = Usuario.query.get(e.id_usuario)
    p = Endereco.query.get(d.id_pessoa)

    db.session.delete(d)
    #db.session.commit()
    db.session.delete(p)
    #db.session.commit()
    db.session.delete(e)
    #db.session.commit()
    db.session.delete(f)
    db.session.commit()

    return {'sucesso':True, 'mensagem':'cliente removido com sucesso.'}

def cliente_get(id_pessoa):
    g = Cliente.query.get(id_pessoa)
    if g == None:
        return {'sucesso':False, 'mensagem':'cliente não existe.'}
    h = Pessoa.query.get(str(g.id_pessoa))
    i = Usuario.query.get(h.id_usuario)

    return {'sucesso':True,'mensagem':'cliente retornado com sucesso.','id_pessoa':g.id_pessoa,'cpfcnpj':h.cpfcnpj,'nomerazaosocial':h.nomerazaosocial,'email':i.email}

@app.route("/clientes/<id_pessoa>",methods=['GET','DELETE'])
@app.route("/clientes/", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/clientes", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
def cliente(id_pessoa):
    if (request.method == 'POST'):
        some_json = request.get_json()
        result = cliente_add(some_json['email'],some_json['senha'],some_json['nomerazaosocial'],some_json['foto'],some_json['telefone'],some_json['tipopessoa'],some_json['cpfcnpj'],some_json['logradouro'],some_json['complemento'],some_json['bairro'],some_json['cidade'],some_json['cep'],some_json['uf'])
        if result['sucesso']:
            return jsonify(result), 201
        return jsonify(result), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        result = cliente_delete(id_pessoa)
        if result['sucesso']:
            return jsonify(result), 202
        return jsonify(result), 400

    elif (request.method == 'GET'):
        result = cliente_get(id_pessoa)
        if result['sucesso']:
            return jsonify(result), 200
        return jsonify(result), 400

'''----------------------------Empresa--------------------------------'''

def empresa_add(email,senha,nomerazaosocial,foto,telefone,tipopessoa,cpfcnpj,logradouro,complemento,bairro,cidade,cep,uf):
    h = Usuario.query.filter(Usuario.email == email).first()
    if h == None:
        h = Usuario(email, senha)
        db.session.add(h)
    
    i = Pessoa.query.filter(Pessoa.id_usuario == h.id_usuario).first()
    if i == None:
        i = Pessoa(nomerazaosocial,foto,telefone,h.id_usuario,tipopessoa,cpfcnpj)
        db.session.add(i)

    j = Endereco.query.filter(Endereco.id_pessoa == i.id_pessoa).first()
    if j == None:
        j = Endereco(i.id_pessoa,logradouro,complemento,bairro,cidade,cep,uf)
        db.session.add(j)
    
    k = Empresa.query.filter(Empresa.id_pessoa == i.id_pessoa).first()
    if k == None:
        k = Empresa(i.id_pessoa)
        db.session.add(k)
        db.session.commit()
    else:
        return {'sucesso':False, 'mensagem':'empresa inexistente.'}

    return {'sucesso':True, 'mensagem':'empresa cadastrada com sucesso.'}#,'id_pessoa':k.id_pessoa,'cpfcnpj':i.cpfcnpj,'email':h.email}

def empresa_delete(id_pessoa):
    d = Empresa.query.get(id_pessoa)
    if d == None:
        return {'sucesso':False, 'mensagem':'empresa não existe.'}

    e = Pessoa.query.get(d.id_pessoa)
    f = Usuario.query.get(e.id_usuario)
    p = Endereco.query.get(d.id_pessoa)

    db.session.delete(d)
    db.session.delete(p)
    db.session.delete(e)
    db.session.delete(f)
    db.session.commit()

    return {'sucesso':True, 'mensagem':'empresa removida com sucesso.'}

def empresa_get(id_pessoa):
    g = Empresa.query.get(id_pessoa)
    if g == None:
        return {'sucesso':False, 'mensagem':'empresa não existe.'}
    h = Pessoa.query.get(str(g.id_pessoa))
    i = Usuario.query.get(h.id_usuario)

    return {'sucesso':True,'mensagem':'empresa retornada com sucesso.','id_pessoa':g.id_pessoa,'cpfcnpj':h.cpfcnpj,'nomerazaosocial':h.nomerazaosocial,'email':i.email}

@app.route("/empresas/<id_pessoa>",methods=['GET','DELETE'])
@app.route("/empresas/", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/empresas", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
def empresa(id_pessoa):
    if (request.method == 'POST'):
        some_json = request.get_json()
        result = empresa_add(some_json['email'],some_json['senha'],some_json['nomerazaosocial'],some_json['foto'],some_json['telefone'],some_json['tipopessoa'],some_json['cpfcnpj'],some_json['logradouro'],some_json['complemento'],some_json['bairro'],some_json['cidade'],some_json['cep'],some_json['uf'])
        if result['sucesso']:
            return jsonify(result), 201
        return jsonify(result), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        result = empresa_delete(id_pessoa)
        if result['sucesso']:
            return jsonify(result), 202
        return jsonify(result), 400

    elif (request.method == 'GET'):
        result = empresa_get(id_pessoa)
        if result['sucesso']:
            return jsonify(result), 200
        return jsonify(result), 400


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

@app.route("/caminhoes/<id_caminhao>",methods=['GET'])
@app.route("/caminhoes/", defaults={'id_caminhao': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/caminhoes", defaults={'id_caminhao': None}, methods=['POST','GET','DELETE','PUT'])
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

def motorista_add(email,senha,nomerazaosocial,foto,telefone,tipopessoa,cpfcnpj,logradouro,complemento,bairro,cidade,cep,uf):
    h = Usuario.query.filter(Usuario.email == email).first()
    if h == None:
        h = Usuario(email, senha)
        db.session.add(h)
    
    i = Pessoa.query.filter(Pessoa.id_usuario == h.id_usuario).first()
    if i == None:
        i = Pessoa(nomerazaosocial,foto,telefone,h.id_usuario,tipopessoa,cpfcnpj)
        db.session.add(i)

    j = Endereco.query.filter(Endereco.id_pessoa == i.id_pessoa).first()
    if j == None:
        j = Endereco(i.id_pessoa,logradouro,complemento,bairro,cidade,cep,uf)
        db.session.add(j)
    
    k = Motorista.query.filter(Motorista.id_pessoa == i.id_pessoa).first()
    if k == None:
        k = Motorista(i.id_pessoa,None,None)
        db.session.add(k)
        db.session.commit()
    else:
        return {'sucesso':False, 'mensagem':'motorista inexistente.'}

    return {'sucesso':True, 'mensagem':'motorista cadastrado com sucesso.'}#,'id_pessoa':k.id_pessoa,'cpfcnpj':i.cpfcnpj,'email':h.email}

def motorista_delete(id_pessoa):
    d = Motorista.query.get(id_pessoa)
    if d == None:
        return {'sucesso':False, 'mensagem':'motorista não existe.'}

    e = Pessoa.query.get(d.id_pessoa)
    f = Usuario.query.get(e.id_usuario)
    p = Endereco.query.get(d.id_pessoa)

    db.session.delete(d)
    db.session.delete(p)
    db.session.delete(e)
    db.session.delete(f)
    db.session.commit()

    return {'sucesso':True, 'mensagem':'motorista removido com sucesso.'}

def motorista_get(id_pessoa):
    g = Motorista.query.get(id_pessoa)
    if g == None:
        return {'sucesso':False, 'mensagem':'motorista não existe.'}
    h = Pessoa.query.get(str(g.id_pessoa))
    i = Usuario.query.get(h.id_usuario)

    return {'sucesso':True,'mensagem':'motorista retornado com sucesso.','id_pessoa':g.id_pessoa,'cpfcnpj':h.cpfcnpj,'nomerazaosocial':h.nomerazaosocial,'email':i.email}

@app.route("/motoristas/<id_pessoa>",methods=['GET','DELETE'])
@app.route("/motoristas/", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/motoristas", defaults={'id_pessoa': None}, methods=['POST','GET','DELETE','PUT'])
def motorista(id_pessoa):
    if (request.method == 'POST'):
        some_json = request.get_json()
        result = motorista_add(some_json['email'],some_json['senha'],some_json['nomerazaosocial'],some_json['foto'],some_json['telefone'],some_json['tipopessoa'],some_json['cpfcnpj'],some_json['logradouro'],some_json['complemento'],some_json['bairro'],some_json['cidade'],some_json['cep'],some_json['uf'])
        if result['sucesso']:
            return jsonify(result), 201
        return jsonify(result), 400

    elif (request.method == 'DELETE'):
        some_json = request.get_json()
        result = motorista_delete(id_pessoa)
        if result['sucesso']:
            return jsonify(result), 202
        return jsonify(result), 400

    elif (request.method == 'GET'):
        result = motorista_get(id_pessoa)
        if result['sucesso']:
            return jsonify(result), 200
        return jsonify(result), 400


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

@app.route("/pedidos/<id_pedido>",methods=['GET'])
@app.route("/pedidos/", defaults={'id_pedido': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/pedidos", defaults={'id_pedido': None}, methods=['POST','GET','DELETE','PUT'])
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

@app.route("/rankings/<id_pessoa_deu>,<id_pedido>",methods=['GET'])
@app.route("/rankings/", defaults={'id_pessoa_deu': None,'id_pedido': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/rankings", defaults={'id_pessoa_deu': None,'id_pedido': None}, methods=['POST','GET','DELETE','PUT'])
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

@app.route("/formapagtos/<id_formapagto>",methods=['GET'])
@app.route("/formapagtos/", defaults={'id_formapagto': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/formapagtos", defaults={'id_formapagto': None}, methods=['POST','GET','DELETE','PUT'])
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

@app.route("/pagamentos/<id_pagamento>",methods=['GET'])
@app.route("/pagamentos/", defaults={'id_pagamento': None}, methods=['POST','GET','DELETE','PUT'])
@app.route("/pagamentos", defaults={'id_pagamento': None}, methods=['POST','GET','DELETE','PUT'])
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

# LOGIN

@app.route("/login/", methods=['POST'])
@app.route("/login", methods=['POST'])
def loginUsuario():
    if (request.method == 'POST'):
        some_json = request.get_json()
        if 'email' not in some_json or 'senha' not in some_json or 'tipoPessoa' not in some_json:
            return jsonify({'sucesso':False,'mensagem':'Parâmetro(s) faltando no Json'}), 404

        g = Usuario.query.filter(Usuario.email == some_json['email']).first()

        if g == None:
            return jsonify({'sucesso':False,'mensagem':'Usuário não cadastrado'}), 404
        if g.senha != some_json['senha']:
            return jsonify({'sucesso':False,'mensagem':'Senha não confere'}), 404

        p = Pessoa.query.filter(Pessoa.id_pessoa == g.id_usuario).first()
        if p == None:
            return jsonify({'sucesso':False,'mensagem':'Usuário não foi cadastrado corretamente'}), 404
        
        g = {'sucesso':True,'mensagem':'Logado com sucesso','email':some_json['senha']}

        if g['sucesso'] == False:
            return jsonify(g), 404
        else:
            return jsonify(g), 200
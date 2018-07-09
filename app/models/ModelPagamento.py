from app import db

class Pagamento(db.Model):
    __tablename__ = "pagamento"

    id_pagamento = db.Column(db.Integer, primary_key=True)
    id_formapagto = db.Column(db.Integer, db.ForeignKey('formapagto.id_formapagto'))
    valor = db.Column(db.Float)
    id_pedido = db.Column(db.Integer, db.ForeignKey('pedido.id_pedido'))
    dataHora = db.Column(db.DateTime)

    formapagto = db.relationship('FormaPagto', foreign_keys=id_formapagto)
    pedido = db.relationship('Pedido', foreign_keys=id_pedido)

    def __init__(self, id_formapagto, valor, id_pedido, dataHora):
        self.id_formapagto = id_formapagto
        self.valor = valor
        self.id_pedido = id_pedido
        self.dataHora = dataHora

    def __repr__(self):
        return "<Pagamento %r>" % self.valor

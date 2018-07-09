from app import db

class Ranking(db.Model):
    __tablename__ = "ranking"

    id_pessoa_deu = db.Column(db.Integer, db.ForeignKey('pessoa.id_pessoa'), primary_key=True)
    id_pedido = db.Column(db.Integer, db.ForeignKey('pedido.id_pedido'), primary_key=True)
    nota = db.Column(db.Integer)
    comentario = db.Column(db.String)

    pessoa = db.relationship('Pessoa', foreign_keys=id_pessoa_deu)
    pedido = db.relationship('Pedido', foreign_keys=id_pedido)

    def __init__(self, id_pessoa_deu, id_pedido, nota, comentario):
        self.id_pessoa_deu = id_pessoa_deu
        self.id_pedido = id_pedido
        self.nota = nota
        self.comentario = comentario

    def __repr__(self):
        return "<Ranking %r>" % self.nota

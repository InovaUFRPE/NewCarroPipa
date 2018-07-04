from app import db

class FormaPagto(db.Model):
    __tablename__ = "formapagto"

    id_formapagto = db.Column(db.Integer, primary_key=True)
    descricao = db.Column(db.String)

    def __init__(self, descricao):
        self.descricao = descricao

    def __repr__(self):
        return "<FormaPagto %r>" % self.descricao

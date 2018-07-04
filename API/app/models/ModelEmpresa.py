from app import db

class Empresa(db.Model):
    __tablename__ = "empresa"

    id_pessoa_emp = db.Column(db.Integer, primary_key=True)

    def __init__(self, id_pessoa_emp):
        self.id_pessoa_emp = id_pessoa_emp

    def __repr__(self):
        return "<Empresa %r>" % self.id_pessoa_emp

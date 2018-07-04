from app import db

class Usuario(db.Model):
    __tablename__ = "usuario"

    id_usuario = db.Column(db.Integer, primary_key=True)
    email = db.Column(db.String, unique=True)
    senha = db.Column(db.String)

    def __init__(self, email, senha):
        self.email = email
        self.senha = senha

    def __repr__(self):
        return "<Usuario %r>" % self.email

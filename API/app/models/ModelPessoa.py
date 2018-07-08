from app import db
        
class Pessoa(db.Model):
    __tablename__ = "pessoa"

    id_pessoa = db.Column(db.Integer, primary_key=True)
    nomerazaosocial = db.Column(db.String)
    foto = db.Column(db.String)
    id_usuario = db.Column(db.Integer, db.ForeignKey('usuario.id_usuario'))
    tipoPessoa = db.Column(db.String)
    cpfCnpj = db.Column(db.String, unique=True)

    usuario = db.relationship('Usuario', foreign_keys=id_usuario)

    def __init__(self, nomerazaosocial, foto, telefone, id_usuario, tipoPessoa, cpfCnpj):
        self.nomerazaosocial = nomerazaosocial
        self.foto = foto
        self.telefone = telefone
        self.id_usuario = id_usuario
        self.tipoPessoa = tipoPessoa
        self.cpfCnpj = cpfCnpj

    def __repr__(self):
        return "<Pessoa %r>" % self.nomerazaosocial

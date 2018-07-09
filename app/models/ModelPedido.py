from app import db

class Pedido(db.Model):
    __tablename__ = "pedido"

    id_pedido = db.Column(db.Integer, primary_key=True)
    id_pessoa_cli = db.Column(db.Integer, db.ForeignKey('cliente.id_pessoa'))
    id_pessoa_mot = db.Column(db.Integer, db.ForeignKey('motorista.id_pessoa'))
    valor = db.Column(db.Float)
    dataHora = db.Column(db.DateTime)
    checkIn = db.Column(db.String)
    imediatoProgramado = db.Column(db.String)
    confirmadoProgramado = db.Column(db.String)

    cliente = db.relationship('Cliente', foreign_keys=id_pessoa_cli)
    motorista = db.relationship('Motorista', foreign_keys=id_pessoa_mot)

    def __init__(self, id_pessoa_cli, id_pessoa_mot, valor, dataHora, checkIn, imediatoProgramado, confirmadoProgramado):
        self.id_pessoa_cli = id_pessoa_cli
        self.id_pessoa_mot = id_pessoa_mot
        self.valor = valor
        self.dataHora = dataHora
        self.checkIn = checkIn
        self.imediatoProgramado = imediatoProgramado
        self.confirmadoProgramado = confirmadoProgramado

    def __repr__(self):
        return "<Pedido %r>" % self.id_pessoa_cli

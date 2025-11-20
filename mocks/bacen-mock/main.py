from fastapi import FastAPI, HTTPException

app = FastAPI(title="Cadastro Mock API")

FAKE_CLIENTES = {
    "1": {"nome": "João Silva", "conta_ativa": True},
    "2": {"nome": "Maria Souza", "conta_ativa": False},
    "3": {"nome": "Carlos Alberto", "conta_ativa": True},
}

@app.get("/cliente/{cliente_id}")
def get_cliente(cliente_id: str):
    cliente = FAKE_CLIENTES.get(cliente_id)

    if not cliente:
        raise HTTPException(status_code=404, detail="Cliente não encontrado")

    return cliente

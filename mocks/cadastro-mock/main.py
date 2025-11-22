from fastapi import FastAPI, HTTPException

app = FastAPI(title="Cadastro Mock API")

FAKE_CUSTOMERS = {
    "1": {"name": "João Silva", "active_account": True},
    "2": {"name": "Maria Souza", "active_account": False},
    "3": {"name": "Carlos Alberto", "active_account": True},
}

@app.get("/customer/{customer_id}")
def get_customer(customer_id: str):
    customer = FAKE_CUSTOMERS.get(customer_id)

    if not customer:
        raise HTTPException(status_code=404, detail="Customer not found")

    return customer


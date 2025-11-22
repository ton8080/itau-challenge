from fastapi import FastAPI, HTTPException

app = FastAPI(title="Cadastro Mock API")

FAKE_CUSTOMERS = {
    "1": {"nome": "João Silva"},
    "2": {"nome": "Maria Souza"},
    "3": {"nome": "Carlos Alberto"},
}

@app.get("/customer/{customer_id}")
def get_customer(customer_id: str):
    customer = FAKE_CUSTOMERS.get(customer_id)

    if not customer:
        raise HTTPException(status_code=404, detail="Customer not found")

    return customer


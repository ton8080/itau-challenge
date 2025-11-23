from fastapi import FastAPI, HTTPException

app = FastAPI(title="Cadastro Mock API")

FAKE_CUSTOMERS = {
    "db5ab4c8-6a6b-4bd5-a20c-919793e128d3": {"name": "João", "active_account": True},
    "9d965035-bba5-4e5e-bccd-28d19e69c86e": {"name": "Maria", "active_account": False},
    "914f6284-ee9d-47ad-ab0e-6b4a3a39a554": {"name": "Carlos", "active_account": True},
}

@app.get("/customer/{customer_id}")
def get_customer(customer_id: str):
    customer = FAKE_CUSTOMERS.get(customer_id)

    if not customer:
        raise HTTPException(status_code=404, detail="Customer not found")

    return customer


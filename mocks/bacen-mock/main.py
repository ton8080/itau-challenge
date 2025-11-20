import random
from fastapi import FastAPI, HTTPException

app = FastAPI(title="BACEN Mock API")

RATE_LIMIT_PROBABILITY = 0.15

@app.post("/notificar")
def notificar(payload: dict):
    if random.random() < RATE_LIMIT_PROBABILITY:
        raise HTTPException(status_code=429, detail="Rate limit exceeded")

    return {
        "status": "OK",
        "transacao": payload.get("transacao_id", "N/A")
    }
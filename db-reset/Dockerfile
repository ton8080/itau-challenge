FROM python:3.11-slim

WORKDIR /app

COPY requirements.txt ./
RUN pip install --no-cache-dir -r requirements.txt

COPY reset_daily.py ./

ENV DB_HOST=postgres
ENV DB_PORT=5432
ENV DB_NAME=bankdb
ENV DB_USER=itau
ENV DB_PASSWORD=itau

CMD ["python", "reset_daily.py"]

import psycopg2
import schedule
import time
import os

DB_HOST = os.getenv("DB_HOST", "postgres")
DB_PORT = os.getenv("DB_PORT", "5432")
DB_NAME = os.getenv("DB_NAME", "bankdb")
DB_USER = os.getenv("DB_USER", "itau")
DB_PASSWORD = os.getenv("DB_PASSWORD", "itau")

def reset_daily_transferred():
    try:
        conn = psycopg2.connect(
            host=DB_HOST,
            port=DB_PORT,
            dbname=DB_NAME,
            user=DB_USER,
            password=DB_PASSWORD
        )
        cursor = conn.cursor()
        cursor.execute("""
                       UPDATE accounts
                       SET daily_transferred = 0,
                           daily_reset = CURRENT_DATE;
                       """)
        conn.commit()
        cursor.close()
        conn.close()
        print(" daily_transferred resetado com sucesso!")
    except Exception as e:
        print(f" Erro ao resetar daily_transferred: {e}")

schedule.every().day.at("00:00").do(reset_daily_transferred)

print("Serviço de reset diário iniciado...")
while True:
    schedule.run_pending()
    time.sleep(60)

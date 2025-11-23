#!/bin/bash
set -e

echo "=== LocalStack READY script ==="

QUEUE_NAME="bacen-notification-queue"

echo "1) Verificando se a fila SQS existe..."
if awslocal sqs get-queue-url --queue-name "$QUEUE_NAME" > /dev/null 2>&1; then
    echo "Fila $QUEUE_NAME já existe."
else
    echo "Fila $QUEUE_NAME não existe. Criando..."
    awslocal sqs create-queue --queue-name "$QUEUE_NAME"
    echo "Fila $QUEUE_NAME criada com sucesso."
fi

QUEUE_URL=$(awslocal sqs get-queue-url --queue-name "$QUEUE_NAME" --output text)
QUEUE_ARN=$(awslocal sqs get-queue-attributes --queue-url $QUEUE_URL --attribute-names QueueArn --output text | awk '{print $2}')

echo "   Queue URL: $QUEUE_URL"
echo "   Queue ARN: $QUEUE_ARN"
echo "LocalStack READY script finalizado!"

#!/bin/bash

echo "🚧 Derrubando containers e removendo volumes..."
sudo docker compose down -v

#echo "🚀 Subindo serviços novamente..."
#sudo docker compose up -d

echo "🚀 Subindo serviços novamente..."
sudo docker compose up

echo "✅ Finalizado! Serviços rodando:"
sudo docker compose ps


# como usar:
# dar permissão para executar
# chmod +x reset-compose.sh
# ./reset-compose.sh


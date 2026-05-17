# ============================================
# Stage 1: Build do projeto Maven
# ============================================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copia arquivos do Maven primeiro (cache de dependências)
COPY pom.xml .

# Copia o código-fonte
COPY src ./src

# Faz o build (gera o .jar)
RUN apt-get update && apt-get install -y maven \
    && mvn clean package -DskipTests

# ============================================
# Stage 2: Imagem final (menor e mais rápida)
# ============================================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copia o .jar gerado no stage anterior
COPY --from=build /app/target/*.jar app.jar

# Copia o frontend
COPY frontend /app/frontend

# Instala Python para servir o frontend
RUN apt-get update && apt-get install -y python3 \
    && rm -rf /var/lib/apt/lists/*

# Expõe as portas
EXPOSE 8080 5500

# Script para subir backend e frontend juntos
RUN echo '#!/bin/bash\n\
java -jar /app/app.jar &\n\
cd /app/frontend && python3 -m http.server 5500\n\
' > /app/start.sh && chmod +x /app/start.sh

CMD ["/app/start.sh"]
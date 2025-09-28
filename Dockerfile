# Estágio 1: Build (Compilação)
# Imagem completa do JDK para compilar o projeto com o Gradle
FROM eclipse-temurin:21-jdk-jammy as builder

# Define o diretório de trabalho dentro do contêiner
WORKDIR /app

# Copia os arquivos de build para aproveitar o cache de camadas do Docker
COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle

# Copia o código-fonte da aplicação
COPY src ./src

# Executa o build do Gradle para gerar o arquivo JAR executável
# --no-daemon é uma boa prática para ambientes de CI/CD e Docker
RUN ./gradlew bootJar --no-daemon

# Estágio 2: Runtime (Execução)
# Imagem JRE (Java Runtime Environment), que é muito menor e mais segura
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app

# Copia apenas o JAR gerado no estágio de build para a imagem final
COPY --from=builder /app/build/libs/*.jar app.jar

# Expõe a porta que a aplicação Spring Boot usa
EXPOSE 8080

# Comando para iniciar a aplicação quando o contêiner subir
ENTRYPOINT ["java", "-jar", "app.jar"]
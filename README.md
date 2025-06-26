# Microsserviço de Compras de Vinhos API
- Microsserviço de Compras de Vinhos

# 🛠️ Tecnologias Utilizadas

- Spring Boot 3.4.5
- HTTP Client (para integrações)
- Lombok
- Gson 2.10.1 (Google)
- SpringDoc OpenAPI 2.5.0
- JUnit 4.13.2 (para testes unitários)
- Logs (Slf4j)
- Spring Cache com Caffeine – utilizado para cachear as respostas dos endpoints, 
- (listarClientesFieis/recomendacaoDeVinho)

# ✅ Requisitos para Rodar o Projeto

- Para executar este projeto corretamente, é necessário que sua máquina atenda aos seguintes requisitos:
- Java JDK 17 ou superior instalado
- IntelliJ IDEA 2025 (recomendado): facilita a configuração do projeto e a integração com ferramentas do 
- ecossistema Spring

# Para executar o aplicativo basta seguir os passos abaixo.

- 1 - Entre na pasta onde está o jar do projeto
- 2 - Execute o seguinte comando com cmd
- 3 - java -jar ecommerce-0.0.1-SNAPSHOT.jar --spring.config.location = file: C: /Config/application.yml

# Documentação para testar a API (Swagger)
- http://localhost:8077/api/v1/swagger-ui/index.html#/

### Arquitetura de solução baseada em AWS
![Captura de Tela 2019-05-12 às 15 18 49](https://res.cloudinary.com/duep7y7ve/image/upload/v1750900266/ih1dqh1zvohcqzeenswk.jpg)
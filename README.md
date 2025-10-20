# 💰 Finance Manager

Sistema web de **gerenciamento financeiro pessoal**, desenvolvido em **Java (Servlet + DAO + JDBC)**, com integração ao **PostgreSQL**.

---

## 🚀 Tecnologias Utilizadas

- **Java 17+**
- **Apache Tomcat 10.1**
- **Maven**
- **Jakarta Servlet API 6.0**
- **PostgreSQL / JDBC**
- **HTML, CSS e JavaScript (frontend simples)**

---

## 🧩 Estrutura do Projeto

finance-manager/
├── src/
│ ├── main/
│ │ ├── java/br/com/finman/
│ │ │ ├── model/ → Classes de domínio (Transacao, Usuario, etc)
│ │ │ ├── dao/ → Acesso a dados via JDBC
│ │ │ └── web/servlet/ → Servlets e rotas REST
│ │ ├── resources/ → Arquivo db.properties
│ │ └── webapp/ → HTML, CSS, JS
│ └── test/ → (opcional) Testes unitários
├── pom.xml → Dependências Maven
├── db.properties → Configuração do banco
└── README.md


---

## 🧠 Funcionalidades

- Cadastro e autenticação de usuário
- Registro de **transações** (receitas e despesas)
- Filtro por mês/ano
- Cálculo de saldo mensal
- Totalização por categoria
- Logout seguro via sessão

---

🗄️ Banco de Dados

O sistema usa **PostgreSQL**.  
Crie o banco com o nome:

```sql
CREATE DATABASE finman;
CREATE TABLE usuario (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(80) NOT NULL,
  email VARCHAR(120) UNIQUE NOT NULL,
  senha VARCHAR(255) NOT NULL
);

CREATE TABLE categoria (
  id SERIAL PRIMARY KEY,
  nome VARCHAR(60) NOT NULL
);

CREATE TABLE transacao (
  id SERIAL PRIMARY KEY,
  usuario_id INT REFERENCES usuario(id),
  categoria_id INT REFERENCES categoria(id),
  tipo VARCHAR(10) NOT NULL,
  valor DECIMAL(10,2) NOT NULL,
  data DATE NOT NULL,
  descricao TEXT
);

## ⚙️ Configuração do Projeto
1. Clone o repositório:
git clone https://github.com/seu-usuario/finance-manager.git


2. Configure o arquivo src/main/resources/db.properties:
db.url=jdbc:postgresql://localhost:5432/finman
db.user=seu_usuario
db.pass=sua_senha


3.Importe o projeto no IntelliJ IDEA
e configure o Tomcat 10.1 como servidor.

4. Execute o servidor e acesse:
http://localhost:8080/finance-manager/

👨🏻‍💻 Autor
Thomaz Arthur Correia de Oliveira
Estudante de Análise e Desenvolvimento de Sistemas (ADS)

🔗: https://www.linkedin.com/in/thomaz-arthur-a2a95b145/

🧾 Licença
Projeto criado para fins acadêmicos.
Você pode usar o código livremente para estudo ou extensão.
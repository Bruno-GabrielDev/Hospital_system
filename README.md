# Hospital System

Sistema hospitalar desenvolvido como trabalho da disciplina **Verificação, Validação e Teste de Software** — IFSP São Carlos.

## 🛠️ Tecnologias

**Backend:**
- Java 21
- Spring Boot 3.4.3
- Spring Security + JWT
- Spring Data JPA
- SQLite
- Swagger/OpenAPI
- Maven

**Frontend:**
- HTML5 + CSS3 + JavaScript (puro, sem framework)

**Testes:**
- JUnit 5
- Mockito
- AssertJ
- Pitest (mutação)

## 📋 Pré-requisitos

- Java 21+
- Maven 3.8+
- Python 3 (para servir o frontend)
- Git

## 🚀 Como Executar

### 1. Clonar o repositório

```bash
git clone https://github.com/Bruno-GabrielDev/Hospital_system.git
cd Hospital_system
```

### 2. Subir o Backend

Na raiz do projeto (onde está o `pom.xml`):

```bash
mvn spring-boot:run
```

O backend sobe em **http://localhost:8080**.

Swagger disponível em: **http://localhost:8080/api/v1/api-docs**

### 3. Subir o Frontend

Em outro terminal, na pasta `frontend`:

```bash
cd frontend
python3 -m http.server 5500
```

Abre no navegador: **http://localhost:5500**

## 👤 Como Usar

1. Acessa http://localhost:5500
2. Clica em **Registrar** e cria uma conta:
   - Nome, Sobrenome, Email, Senha
3. Faz **Login** com o email e senha criados
4. No dashboard, navega pelas abas:
   - **Pacientes** — cadastrar e listar (cada paciente precisa de documento único)
   - **Médicos** — cadastrar e listar (cada médico precisa de CRM único)
   - **Procedimentos** — cadastrar e listar
   - **Atendimentos** — criar, fechar, faturar e cancelar atendimentos

### ⚠️ Observações Importantes

- O banco SQLite usa `create-drop`, ou seja, **todos os dados são apagados quando o backend reinicia**.
- Documentos de pacientes e CRMs de médicos são únicos — não pode cadastrar dois iguais.
- A data do atendimento deve estar no **futuro**.
- O token JWT expira em 2 horas e 24 minutos.

## 🧪 Como Executar os Testes

### Todos os testes
```bash
mvn test
```

### Apenas testes TDD
```bash
mvn test -Dgroups=TDD
```

### Apenas testes Funcionais
```bash
mvn test -Dgroups=Functional
```

### Apenas testes Estruturais
```bash
mvn test -Dgroups=Structural
```

### Todos os testes unitários
```bash
mvn test -Dgroups=UnitTest
```

### Testes de Mutação (Pitest)
```bash
mvn test-compile org.pitest:pitest-maven:mutationCoverage
```

Para visualizar o relatório do Pitest:

```bash
./open-pitest-report.sh
```

E acessa http://localhost:8081

## 📁 Estrutura do Projeto

```
hospital-tdd-base/
├── src/main/java/br/ifsp/hospital/
│   ├── domain/
│   │   ├── model/          ← Entidades e Value Objects (puro, sem JPA)
│   │   ├── repository/     ← Interfaces de repositório
│   │   └── service/        ← Serviços de domínio
│   ├── infrastructure/
│   │   ├── entity/         ← Entidades JPA
│   │   └── repository/     ← Adaptadores JPA
│   ├── controller/         ← Controllers REST
│   ├── security/           ← JWT + Spring Security
│   └── exception/          ← Handlers globais
├── src/test/java/br/ifsp/hospital/
│   ├── annotation/         ← @UnitTest, @TDD, @Functional, @Structural
│   ├── domain/             ← Testes TDD
│   ├── functional/         ← Testes funcionais (PE e AVL)
│   ├── structural/         ← Testes estruturais (100% branch)
│   └── suite/              ← Suítes de teste
├── frontend/
│   ├── index.html          ← Tela de login/registro
│   ├── dashboard.html      ← Tela principal
│   ├── api.js              ← Integração com a API
│   ├── style.css           ← Estilos
│   └── README.md           ← README do frontend
└── pom.xml
```

## 📡 Endpoints da API

Todos os endpoints (exceto `/register` e `/authenticate`) requerem autenticação via Bearer Token.

### Autenticação
- `POST /api/v1/register` — registrar usuário
- `POST /api/v1/authenticate` — fazer login

### Pacientes
- `POST /api/v1/patients` — cadastrar paciente
- `GET /api/v1/patients` — listar pacientes
- `GET /api/v1/patients/{id}` — buscar por ID

### Médicos
- `POST /api/v1/doctors` — cadastrar médico
- `GET /api/v1/doctors` — listar médicos
- `GET /api/v1/doctors/{id}` — buscar por ID

### Procedimentos
- `POST /api/v1/procedures` — cadastrar procedimento
- `GET /api/v1/procedures` — listar procedimentos
- `GET /api/v1/procedures/{id}` — buscar por ID

### Atendimentos
- `POST /api/v1/appointments` — criar atendimento
- `POST /api/v1/appointments/{id}/procedures` — adicionar procedimento
- `PATCH /api/v1/appointments/{id}/close` — fechar
- `PATCH /api/v1/appointments/{id}/bill` — faturar
- `GET /api/v1/appointments/{id}/bill` — calcular conta
- `PATCH /api/v1/appointments/{id}/reschedule` — reagendar
- `PATCH /api/v1/appointments/{id}/cancel` — cancelar
- `GET /api/v1/appointments` — listar todos
- `GET /api/v1/appointments/{id}` — buscar por ID
- `GET /api/v1/appointments/patient/{patientId}` — por paciente
- `GET /api/v1/appointments/doctor/{doctorId}` — por médico

## 📊 Cobertura de Testes

- **TDD:** todos os cenários do backlog cobertos
- **Funcional:** Partição de Equivalência (PE) e Análise de Valor Limite (AVL)
- **Estrutural:** 100% Branch coverage no domínio
- **Mutação:** análise via Pitest

## 👥 Equipe

- Bruno Gabriel (Bruno-GabrielDev)
- Eduardo Ferraz (DudsFerraz)
- Rhuan (Rhuan-aa)

## 🎓 Disciplina

Verificação, Validação e Teste de Software — IFSP São Carlos
Prof. Dr. Lucas Oliveira

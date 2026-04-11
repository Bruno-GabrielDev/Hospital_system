# Hospital System — Etapa 2

Sistema hospitalar com Spring Boot, TDD e DDD.

## Estrutura do Projeto

```
src/main/java/br/ifsp/hospital/
├── domain/
│   ├── model/          ← Entidades e Value Objects de domínio (SEM JPA)
│   │   ├── Money.java              ← Value Object imutável
│   │   ├── Patient.java            ← Entidade de domínio
│   │   ├── Doctor.java
│   │   ├── Procedure.java
│   │   ├── AppointmentProcedure.java
│   │   ├── MedicalAppointment.java ← Agregado raiz
│   │   ├── InsuranceType.java
│   │   └── AppointmentStatus.java
│   ├── repository/     ← Interfaces de repositório (portas de saída)
│   └── service/        ← Serviços de domínio (@Service, sem JPA)
│       ├── AppointmentService.java
│       ├── InsuranceCoverageService.java
│       ├── PatientAppService.java
│       ├── DoctorAppService.java
│       └── ProcedureAppService.java
├── infrastructure/
│   ├── entity/         ← Entidades JPA (@Entity) — SEM regras de negócio
│   └── repository/     ← Adaptadores JPA implementando as interfaces de domínio
├── controller/         ← Controllers REST
├── security/           ← JWT + Spring Security
└── exception/          ← Handlers globais
```

```
src/test/java/br/ifsp/hospital/
├── annotation/         ← @UnitTest, @TDD, @Functional
├── domain/             ← Testes com @UnitTest @TDD
│   ├── MedicalAppointmentTDDTest.java
│   ├── InsuranceCoverageServiceTDDTest.java
│   └── AppointmentServiceTDDTest.java
├── functional/         ← Testes com @UnitTest @Functional
│   ├── MedicalAppointmentFunctionalTest.java
│   ├── InsuranceCoverageFunctionalTest.java
│   └── MoneyFunctionalTest.java
└── suite/
    ├── TDDTestSuite.java      ← Roda apenas @TDD
    └── UnitTestSuite.java     ← Roda todos os @UnitTest
```

## Executar os Testes

```bash
# Todos os testes
mvn test

# Apenas testes TDD (tag @TDD)
mvn test -Dgroups=TDD

# Todos os testes unitários (tag @UnitTest)
mvn test -Dgroups=UnitTest

# Apenas testes funcionais (tag @Functional)
mvn test -Dgroups=Functional
```

## Conventional Commits — Padrão para esta Etapa

```
# Formato
<tipo>(escopo): <descrição> [- closes #<issue>]

# Tipos usados
feat     → nova funcionalidade
test     → adição ou ajuste de teste
fix      → correção de bug
refactor → refatoração sem mudança de comportamento
chore    → configuração, build, etc.

# Exemplos de commits TDD (Red → Green → Refactor)
test(appointment): add failing test for #5 create appointment with OPEN status
feat(appointment): implement MedicalAppointment.of() to pass test #5 - closes #5

test(appointment): add failing test for #14 block calculation without procedures
feat(appointment): implement calculateGrossTotal guard for empty procedures - closes #14

test(insurance): add failing tests for #16 and #17 insurance discount
feat(insurance): implement BASIC 30% and PREMIUM 70% discount - closes #16 closes #17

test(reschedule): add failing test for #58 max reschedule limit
feat(reschedule): enforce MAX_RESCHEDULES=3 in domain model - closes #58

fix(appointment): correct rounding in calculateGrossTotal for #33
refactor(domain): extract Money.applyDiscount to reuse in InsuranceCoverageService
```

## Regra de Ouro da Separação Domínio/Persistência

| Onde | Pode ter | Não pode ter |
|---|---|---|
| `domain/model/` | Regras de negócio, validações | `@Entity`, `@Column`, JPA |
| `infrastructure/entity/` | `@Entity`, `@Table`, `@Column` | Regras de negócio |
| `domain/service/` | Lógica de orquestração | Dependência direta de JPA |
| `infrastructure/repository/` | Implementação JPA | Regras de negócio |

## API REST

Após iniciar: `http://localhost:8080/api/v1/api-docs`

### Fluxo completo
```
POST /api/v1/register          → criar usuário do sistema
POST /api/v1/authenticate      → obter token JWT
POST /api/v1/patients          → cadastrar paciente         🔒
POST /api/v1/doctors           → cadastrar médico           🔒
POST /api/v1/procedures        → cadastrar procedimento     🔒
POST /api/v1/appointments      → criar atendimento          🔒
POST /api/v1/appointments/{id}/procedures  → adicionar proc 🔒
PATCH /api/v1/appointments/{id}/reschedule → reagendar      🔒
PATCH /api/v1/appointments/{id}/close      → fechar         🔒
GET  /api/v1/appointments/{id}/bill        → calcular conta 🔒
PATCH /api/v1/appointments/{id}/bill       → faturar        🔒
```

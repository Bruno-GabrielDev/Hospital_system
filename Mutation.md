# 🧬 Relatório de Mutantes Equivalentes

Este documento descreve os **mutantes equivalentes** gerados pelo [PiTest](https://pitest.org/) durante a análise de testes de mutação do projeto Hospital System.


---

## 📦 `MedicalAppointment`

**Pacote:** `br.ifsp.hospital.domain.model`

| Linha | ID do Mutante | Justificativa de Equivalência |
|:-----:|:--------------|:------------------------------|
| — | `restore: Removed assignment to member variable status` | No método `restore`, o campo `status` é atribuído com o valor passado por parâmetro. Como o construtor privado já inicializa o status como `OPEN` e os testes de restauração básicos usam `OPEN`, a remoção da linha produz o mesmo estado final. |
| — | `restore: Removed assignment to member variable rescheduleCount` | O campo `rescheduleCount` é do tipo `int`. No Java, campos `int` são inicializados como `0` por padrão pela JVM. Se o teste de `restore` passa `0`, a remoção da atribuição não altera o valor do campo. |

---

## 📦 `Procedure`

**Pacote:** `br.ifsp.hospital.domain.model`

| Linha | ID do Mutante | Justificativa de Equivalência |
|:-----:|:--------------|:------------------------------|
| 11 | `Removed assignment to member variable gracePeriodDays` | A linha original é `private int gracePeriodDays = 0;`. Como campos numéricos primitivos em Java são inicializados com `0` por padrão pela JVM, remover a atribuição explícita não altera o valor inicial da variável. |

---

## 📦 `Money`

**Pacote:** `br.ifsp.hospital.domain.model`

| Linha | ID do Mutante | Justificativa de Equivalência |
|:-----:|:--------------|:------------------------------|
| — | `removed call to java/util/Objects::hashCode` | O PiTest tenta trocar `Objects.hashCode(amount)` por `amount.hashCode()`. Como o campo `amount` é validado como **não-nulo** no construtor, a proteção contra `null` oferecida por `Objects.hashCode` torna-se redundante — o comportamento final é idêntico. |

---

## 📦 `Doctor` / `Patient` / `Procedure` — Contratos de Hash

**Pacote:** `br.ifsp.hospital.domain.model`

| Linha | ID do Mutante | Justificativa de Equivalência |
|:-----:|:--------------|:------------------------------|
| — | `removed call to java/util/Objects::hashCode` | Substituição da chamada estática `Objects.hashCode(id)` pela chamada direta `id.hashCode()`. Como o campo `id` é **obrigatório e nunca nulo** para entidades persistidas ou restauradas, a lógica final é a mesma. |

---

## 📊 Resumo

| Classe | Mutantes Equivalentes |
|:-------|:---------------------:|
| `MedicalAppointment` | 2 |
| `Procedure` | 1 |
| `Money` | 1 |
| `Doctor` | 1 |
| `Patient` | 1 |
| **Total** | **6** |

---

> 📝 **Nota:** Este documento é atualizado sempre que novas mutações equivalentes são identificadas durante a análise de qualidade do código.
>
> 🔧 **Como gerar o relatório:** `mvn test-compile org.pitest:pitest-maven:mutationCoverage`
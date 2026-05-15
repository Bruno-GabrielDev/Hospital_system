# Relatório de Mutantes Equivalentes

Este documento descreve os mutantes gerados pelo PiTest que foram identificados como **equivalentes**. Mutantes equivalentes alteram o bytecode ou a estrutura do código, mas não alteram a lógica funcional, tornando-os impossíveis de serem "mortos" por testes unitários.

## br.ifsp.hospital.domain.model.MedicalAppointment

| Linha | ID do Mutante | Justificativa de Equivalência |
| :--- | :--- | :--- |
| - | `restore : Removed assignment to member variable status` | No método `restore`, o campo `status` é atribuído com o valor passado no parâmetro. Como o construtor privado já inicializa o status como `OPEN` e os testes de restauração básicos usam `OPEN`, a remoção da linha produz o mesmo estado final. |
| - | `restore : Removed assignment to member variable rescheduleCount` | Similar ao status, o `rescheduleCount` é do tipo `int`. No Java, campos `int` são inicializados como `0` por padrão. Se o teste de `restore` passa `0`, a remoção da atribuição não altera o valor do campo. |

## br.ifsp.hospital.domain.model.Procedure

| Linha | ID do Mutante | Justificativa de Equivalência |
| :--- | :--- | :--- |
| 11 | `Removed assignment to member variable gracePeriodDays` | A linha original é `private int gracePeriodDays = 0;`. No Java, campos numéricos primitivos são inicializados com `0` por padrão pela JVM. Remover a atribuição explícita não altera o valor inicial da variável. |

## br.ifsp.hospital.domain.model.Money

| Linha | ID do Mutante | Justificativa de Equivalência |
| :--- | :--- | :--- |
| - | `removed call to java/util/Objects::hashCode` | O PiTest tenta trocar `Objects.hashCode(amount)` por `amount.hashCode()`. Como o campo `amount` é validado como não-nulo no construtor, a proteção contra `null` do `Objects.hashCode` torna-se redundante, resultando em comportamento idêntico. |

## br.ifsp.hospital.domain.model.Doctor / Patient / Procedure (Contratos de Hash)

| Linha | ID do Mutante | Justificativa de Equivalência |
| :--- | :--- | :--- |
| - | `removed call to java/util/Objects::hashCode` | Substituição da chamada estática de utilitário pela chamada direta no objeto (`id.hashCode()`). Como o `id` é obrigatório e nunca nulo para entidades persistidas ou restauradas, a lógica final é a mesma. |

---
*Nota: Este documento deve ser atualizado sempre que novas mutações equivalentes forem identificadas durante o processo de análise de qualidade do código.*

## Circuit Breaker : fonctionnement simple

Un **Circuit Breaker** permet de protéger une application lorsqu'un autre service devient indisponible.

Dans ton projet :

```
ebank-service  ──────>  customer-service
```

Si `customer-service` fonctionne, tout va bien.

Si `customer-service` tombe, le Circuit Breaker évite que `ebank-service` continue à lui envoyer des requêtes qui vont échouer.

## Les 3 états

### 1\. CLOSED

Tout fonctionne normalement.

```
ebank-service
      |
      v
customer-service
      |
      v
    réponse
```

Les requêtes passent normalement.

---

### 2\. OPEN

Si plusieurs appels échouent, le Circuit Breaker ouvre le circuit.

```
ebank-service
      |
      X
customer-service
```

Les nouvelles requêtes ne sont plus envoyées à `customer-service`.

À la place, le **fallback** est exécuté.

Dans ton cas :

```
default Customer getDefaultCustomer(Long id, Throwable e) {
    return new Customer(
        id,
        "Not available",
        "Not available"
    );
}
```

Donc :

```
ebank-service
      |
      v
Circuit Breaker
      |
      v
   fallback
      |
      v
"Not available"
```

---

### 3\. HALF\_OPEN

Après un certain temps, le Circuit Breaker teste si `customer-service` est de nouveau disponible.

Si le test réussit :

```
HALF_OPEN
    |
    v
customer-service OK
    |
    v
CLOSED
```

Si le test échoue :

```
HALF_OPEN
    |
    v
customer-service KO
    |
    v
OPEN
```

## Exemple dans ton projet

Tu as :

```
@CircuitBreaker(
    name = "customerService",
    fallbackMethod = "getDefaultCustomer"
)
Customer getCustomerById(@PathVariable Long id);
```

Lorsque tu fais :

```
customerRestClient.getCustomerById(10L);
```

### Cas normal

```
ebank-service
    |
    | getCustomerById(10)
    v
customer-service
    |
    v
Customer #10
```

### Si customer-service est indisponible

```
ebank-service
    |
    v
Circuit Breaker
    |
    X customer-service
    |
    v
getDefaultCustomer(10, exception)
    |
    v
Customer "Not available"
```

## À retenir

```
CLOSED    → les appels passent normalement

OPEN      → les appels sont bloqués
            + fallback utilisé

HALF_OPEN → on teste si le service est revenu
```

L'objectif est simple : **une panne de `customer-service` ne doit pas faire tomber tout `ebank-service`.**
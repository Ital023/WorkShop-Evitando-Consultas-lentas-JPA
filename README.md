# WorkShop Evitando Consultas Lentas JPA

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

# Sobre o projeto
Neste repositório, explico detalhadamente o problema N+1 no contexto de relacionamentos Many-to-One e Many-to-Many usando JPA Hibernate. A ideia é demonstrar como consultas ineficientes podem surgir ao acessar entidades relacionadas e como isso afeta a performance do banco de dados.

Você encontrará exemplos práticos de como identificar o problema e as melhores abordagens para solucioná-lo, como o uso de FetchType.LAZY, JOIN FETCH, e outras otimizações.

Este workshop é ideal para quem quer entender melhor o comportamento do JPA em relação a consultas SQL e melhorar a performance das aplicações Spring Boot.

### Tópicos discutidos no WorkShop
  - JPQL
  - JOIN FETCH ManyToOne (todos)
  - JOIN FETCH ManyToMany (todos)
  - JOIN FETCH ManyToOne (paginado)

### Qual a discussão do WorkShop?
Sobre como algumas consultas geradas pela JPA, ORM, podem ser extremamentes ineficientes.
#### Por que o resultado da consulta JPQL é ineficiente?
R: A consulta vai ser traduzida para o SQL e enviada ao DB, nessa conversão, pode acontecer resultados que geram várias idas e vindas ao DB, igual ao problema N + 1.
Sendo necessário alguns cuidados, sempre olhar o log para verificar se a consulta está ineficiente.

## Muitos para Um (Many To One)
### Código a ser analisado

Temos duas entidades relacionadas por Many To One
  - Employee
    ```
      @Entity
      @Table(name = "tb_employee")
      public class Employee {
      
      	@Id
      	@GeneratedValue(strategy = GenerationType.IDENTITY)
      	private Long id;
      	private String name;
      	private Double salary;
      	
      	@ManyToOne
      	@JoinColumn(name = "department_id")	
      	private Department department;
      
      	Getters() & Setters()...
      }

    ```
  - Department
    ```
      @Entity
      @Table(name = "tb_department")
      public class Department {
      
      	@Id
      	@GeneratedValue(strategy = GenerationType.IDENTITY)
      	private Long id;
      	private String name;
      	
      	Getters() & Setters()...
      }

    ```
#### Então é proposto que seja feita uma consulta no GetMapping da maneira casual, veja o exemplo: 
  - Employee Controller

  ```
  @RestController
  @RequestMapping(value = "/employees")
  public class EmployeeController {
  
  	@Autowired
  	private EmployeeRepository employeeRepository;
  	
  	@GetMapping
  	public List<Employee> findAll() {
  		return employeeRepository.findAll();
  	}
  }
  ```
#### O retorno do JSON, aparentemente está ok. Será mesmo? Essa consulta é realmente eficiente?
- Veja o retorno do JSON
  ```
  [
      {
          "id": 1,
          "name": "Maria",
          "salary": 4000.0,
          "department": {
              "id": 1,
              "name": "Financeiro"
          }
      },
      {
          "id": 2,
          "name": "Alex",
          "salary": 4000.0,
          "department": {
              "id": 4,
              "name": "Vendas"
          }
      },
      {
          "id": 3,
          "name": "Carlos",
          "salary": 4000.0,
          "department": {
              "id": 5,
              "name": "Cobrança"
          }
      },
      {
          "id": 4,
          "name": "Mario",
          "salary": 4000.0,
          "department": {
              "id": 1,
              "name": "Financeiro"
          }
      },
      {
          "id": 5,
          "name": "Teresa",
          "salary": 4000.0,
          "department": {
              "id": 1,
              "name": "Financeiro"
          }
      }
      ...
  ```
#### No entanto, se formos ver o log de acesso ao banco, iremos se deparar com esse log: 
 ```
  Hibernate: 
      select
          e1_0.id,
          e1_0.department_id,
          e1_0.name,
          e1_0.salary 
      from
          tb_employee e1_0
  Hibernate: 
      select
          d1_0.id,
          d1_0.name 
      from
          tb_department d1_0 
      where
          d1_0.id=?
  Hibernate: 
      select
          d1_0.id,
          d1_0.name 
      from
          tb_department d1_0 
      where
          d1_0.id=?
  Hibernate: 
      select
          d1_0.id,
          d1_0.name 
      from
          tb_department d1_0 
      where
          d1_0.id=?
  Hibernate: 
      select
          d1_0.id,
          d1_0.name 
      from
          tb_department d1_0 
      where
          d1_0.id=?
  Hibernate: 
      select
          d1_0.id,
          d1_0.name 
      from
          tb_department d1_0 
      where
          d1_0.id=?
```
#### O que aconteceu?
 - Foi feito um acesso toda vez que é visto um novo ID, enfraquecendo a consulta SQL.
 - Isso deixa a consulta da JPA ineficiente, sendo necessário a tomada de medidas preventivas na consulta para que isso não aconteça.

#### Como resolver esse problema?
- Podemos colocar uma consulta query personalizada usando o JOIN FETCH
    - Dessa maneira faz com que o JPA Hibernate faça um JOIN e realize a busca em apenas 1 ida ao DB, sendo assim, mais eficiente.

    - Configuração no repository
    
     ```
    @Query(value = "SELECT obj FROM Employee obj JOIN FETCH obj.department")
        List<Employee> searchAll();
    ```

    - Configuração no controller
       
     ```
    @GetMapping
	public List<Employee> findAll() {
		return employeeRepository.searchAll();
	}
    ```
   
### Na busca paginada de Many to One é um pouco diferente
- Temos que adicionar um countQuery
     - Configuração no repository

    ```
    @Query(value = "SELECT obj FROM Employee obj JOIN FETCH obj.department",
    countQuery = "SELECT COUNT(obj) FROM Employee obj JOIN obj.department")
    Page<Employee> searchAll(Pageable pageable);
    ```

    - Configuração no controller

    ```
    @GetMapping(value = "/pageable")
	public Page<Employee> findAllPageable(Pageable pageable) {
		return employeeRepository.searchAll(pageable);
	}
    ```



# Tecnologias utilizadas
## Back end
- Java
- Spring Boot
- JPA / Hibernate
- Maven
  
# Como executar o projeto

## Back end
Pré-requisitos: Java 17

```bash
# clonar repositório
git clone https://github.com/Ital023/WorkShop-Evitando-Consultas-lentas-JPA.git

# executar o projeto
./mvnw spring-boot:run
```

## 🤝 Colaboradores

Agradecemos às seguintes pessoas que contribuíram para este projeto:

<table>
  <tr>
    <td align="center">
      <a href="https://github.com/Ital023" title="Github do Ítalo Miranda">
        <img src="https://avatars.githubusercontent.com/u/113559117?v=4" width="100px;" alt="Foto do Ítalo Miranda no GitHub"/><br>
        <sub>
          <b>Ítalo Miranda</b>
        </sub>
      </a>
    </td>
  </tr>
</table>

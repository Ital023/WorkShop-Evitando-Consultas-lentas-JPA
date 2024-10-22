# Spring Security Estudos Italo
![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)

# Sobre o projeto
Este repositório é destinado aos meus estudos sobre Spring Security com OAuth2. O projeto implementa um Authorization Server e Resource Server, utilizando as interfaces do Spring Security para gerenciar autenticação e autorização, focando em práticas seguras e modernas para proteger APIs RESTful.

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
#### Conclusao
 - É feito um acesso toda vez que é visto um novo ID, enfraquecendo a consulta SQL.





# Tecnologias utilizadas
## Back end
- Java
- Spring Boot
- JPA / Hibernate
- Maven
- Oauth2
- Resource Server
- Authorization Server
  

# Rotas
&#9679;	Produtos

| Método | Caminho                      | Descrição                                           | Role Necessária                  |
| ------ | ---------------------------- | -------------------------------------------------- | -------------------------------- |
| GET    | /products                  | Retorna uma lista de produtos                        | Nenhuma            |
| GET    | /products/{id}             | Retorna um produto específico pelo ID.              | Nenhuma                          |
| POST   | /products                  | Adiciona um novo produto.                           | Nenhuma                     |

# Como executar o projeto

## Back end
Pré-requisitos: Java 17

```bash
# clonar repositório
git clone https://github.com/Ital023/Spring-Security.git

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

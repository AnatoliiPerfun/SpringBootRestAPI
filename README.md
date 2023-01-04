
## SpringBoot Rest Api 

### Description

This is a simple Rest Api using SpringBoot and Spring Data JPA. To read XML files and store them in a database.

### How to run

Application can be run via this command (assuming that [docker compose](https://docs.docker.com/compose/install/)) is installed in the system:

### How to use

The following steps will run a local instance of the Temporal Server using the default configuration file (`docker-compose.yml`):

1. Clone this repository.
2. Change directory into the root of the project.
3. Run the `docker-compose up` command.

```bash
git clone https://github.com/AnatoliiPerfun/SpringBootRestAPI.git
cd  docker-compose
docker-compose up
```

Clone this repository.
Change directory into the root of the project.
Run the docker-compose up command.

If you run command:

```bash
docker-compose up
```

### Application starts on: `localhost:8080/api`

To remove all containers and volumes:

```
docker-compose down -v
```

***

#### Important to Parse XML file first using POST request first, otherwise you will not see data.

***

##  Endpoints

* `POST /api/xyz`
* requires valid xml Multipart file attached in request body parameter `file`. <br>

* `GET /api/xyz`
* retrieves Data entities. Endpoint allows paging, sorting and filtering.
    * `page` & `size` for paging;
    * `sort`, which expects `key,value` pair where:
        * **key** = entity property (see json below), e.g. `id`or`newspaperName`,
        * **value** = sort direction (`asc` or `desc`).




# STACK

- Play Framework
- jOOq
- mockito
- PostgreSQL

# GET STARTED

The best way to get started with this project is to download Docker where you can do so here https://docs.docker.com/ 

Once you have installed Docker in your machine, the only thing left is to execute the following command in the project root folder:

**docker-compose up**

Everything needed to run this project is already wired up. Therefore you do not need to do anything else, just execute the above command.

To know if everything is up correctly you should see the following messages:

```
Starting crm-api_postgres_1 ... done
Starting crm-api_sbt_1      ... done
```

Afterwards, You will need to do two more steps in order to test the API.

* Access to http://localhost:9000. You should see a message like this:

    ![evolutions](evolution.png?raw=true "Database needs evolution")

* Click on *Apply this script now!*
* **Enjoy!**

# Endpoints

List of Endpoints available [here](ENDPOINTS.md).

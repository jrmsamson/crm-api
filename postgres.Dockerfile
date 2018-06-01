FROM postgres:10

ENV POSTGRES_PASSWORD=crmapi

EXPOSE 5432

COPY postgres_extensions.sql /docker-entrypoint-initdb.d
FROM hseeberger/scala-sbt

WORKDIR /code

EXPOSE 9000

ENTRYPOINT [ "sbt", "run" ]